/*
 * This file is part of choco-solver, http://choco-solver.org/
 *
 * Copyright (c) 2020, IMT Atlantique. All rights reserved.
 *
 * Licensed under the BSD 4-clause license.
 *
 * See LICENSE file in the project root for full license information.
 */
package org.chocosolver.solver.variables.view;

import org.chocosolver.solver.ICause;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.learn.ExplanationForSignedClause;
import org.chocosolver.solver.learn.Implications;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.events.IntEventType;
import org.chocosolver.util.ESat;
import org.chocosolver.util.objects.ValueSortedMap;
import org.chocosolver.util.objects.setDataStructures.iterable.IntIterableRangeSet;


/**
 * declare an BoolVar based on X and C, such as (X <= C) is reified by this. <br/> Based on "Views
 * and Iterators for Generic Constraint Implementations" <br/> C. Shulte and G. Tack.<br/> Eleventh
 * International Conference on Principles and Practice of Constraint Programming
 *
 * @author Charles Prud'homme
 * @since 04/02/11
 */
public final class LeqView extends IntBoolView {

    /**
     * A boolean view based on <i>var<i/> such that <i>var<i/> &le; <i>cste<i/>
     *
     * @param var  an integer variable
     * @param cste an int
     */
    public LeqView(final IntVar var, final int cste) {
        super(var, "≤", cste);
    }

    @Override
    public ESat getBooleanValue() {
        if (var.getUB() <= cste) {
            return ESat.TRUE;
        } else if (var.getLB() > cste) {
            return ESat.FALSE;
        }
        return ESat.UNDEFINED;
    }

    @Override
    public boolean instantiateTo(int value, ICause cause) throws ContradictionException {
        assert cause != null;
        boolean done = false;
        if (!this.contains(value)) {
            model.getSolver().getEventObserver().instantiateTo(this, value, cause, getLB(), getUB());
            this.contradiction(this, MSG_EMPTY);
        } else if (!isInstantiated()) {
            model.getSolver().getEventObserver().instantiateTo(this, value, cause, getLB(), getUB());
            notifyPropagators(IntEventType.INSTANTIATE, cause);
            this.fixed.set(true);
            if (reactOnRemoval) {
                delta.add(1 - value, cause);
            }
            if (value == 1) {
                done = var.updateUpperBound(cste, this);
            } else {
                done = var.updateLowerBound(cste + 1, this);
            }
        }
        return done;
    }

    @Override
    public int getDomainSize() {
        return isInstantiated()?1:2;
    }

    @Override
    public boolean isInstantiated() {
        if (var.getUB() <= cste) {
            return true;
        } else return var.getLB() > cste;
    }

    @Override
    public boolean contains(int value) {
        if (value == 0) {
            return cste < var.getUB();
        } else if (value == 1) {
            return var.getLB() <= cste;
        }
        return false;
    }

    @Override
    public boolean isInstantiatedTo(int value) {
        if (value == 0) {
            return cste < var.getLB();
        } else if (value == 1) {
            return var.getUB() <= cste;
        }
        return false;
    }

    @Override
    public int getLB() {
        if (cste < var.getUB()) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getUB() {
        if (var.getLB() <= cste) {
            return 1;
        }
        return 0;
    }

    @Override
    public int nextValue(int v) {
        if (v < 0 && contains(0)) {
            return 0;
        }
        return v <= 0 && contains(1) ? 1 : Integer.MAX_VALUE;
    }

    @Override
    public int nextValueOut(int v) {
        int lb = 0, ub = 1;
        if (var.getLB() > cste) {
            ub = 0;
        } else if (cste >= var.getUB()) {
            lb = 1;
        }
        if (lb - 1 <= v && v <= ub) {
            return ub + 1;
        } else {
            return v + 1;
        }
    }

    @Override
    public int previousValue(int v) {
        if (v > 1 && contains(1)) {
            return 1;
        }
        return v >= 1 && contains(0) ? 0 : Integer.MIN_VALUE;
    }

    @Override
    public int previousValueOut(int v) {
        int lb = 0, ub = 1;
        if (var.getLB() > cste) {
            ub = 0;
        } else if (cste >= var.getUB()) {
            lb = 1;
        }
        if (lb <= v && v <= ub + 1) {
            return lb - 1;
        } else {
            return v - 1;
        }
    }

    @Override
    public void justifyEvent(IntEventType mask, int one, int two, int three) {
        if (this.isInstantiated()) return;
        switch (mask) {
            case DECUPP:
                if (one <= cste) {
                    model.getSolver().getEventObserver().instantiateTo(this, 1, this, 0, 1);
                }
                break;
            case INCLOW:
                if (cste < one) {
                    model.getSolver().getEventObserver().instantiateTo(this, 0, this, 0, 1);
                }
                break;
            case REMOVE:
                if(var.getUB() == one && var.previousValue(one) <= cste){
                    model.getSolver().getEventObserver().instantiateTo(this, 1, this, 0, 1);
                }else if(var.getLB() == one && var.nextValue(one) > cste){
                    model.getSolver().getEventObserver().instantiateTo(this, 0, this, 0, 1);
                }
                break;
            case INSTANTIATE:
                if (one <= cste) {
                    model.getSolver().getEventObserver().instantiateTo(this, 1, this, 0, 1);
                } else {
                    model.getSolver().getEventObserver().instantiateTo(this, 0, this, 0, 1);
                }
                break;
        }
    }

    @Override
    public void explain(ExplanationForSignedClause explanation, ValueSortedMap<IntVar> front, Implications ig, int p) {
        IntVar pivot = ig.getIntVarAt(p);
        int value = getValue();
        if (value == 1) { // b is true and X < c holds
            if (pivot == this) { // b is the pivot
                this.crossWith(explanation.getFreeSet(1), explanation);
                IntIterableRangeSet dom0 = explanation.getComplementSet(var);
                dom0.retainBetween(cste + 1, IntIterableRangeSet.MAX);
                var.joinWith(dom0, explanation);
            } else /*if (pivot == var)*/ { // x is the pivot
                this.joinWith(explanation.getFreeSet(0), explanation);
                IntIterableRangeSet dom0 = explanation.getRootSet(var);
                dom0.retainBetween(IntIterableRangeSet.MIN, cste);
                var.crossWith(dom0, explanation);
            }
        } else if (value == 0) {
            if (pivot == this) { // b is the pivot
                this.crossWith(explanation.getFreeSet(0), explanation);
                IntIterableRangeSet dom0 = explanation.getComplementSet(var);
                dom0.retainBetween(IntIterableRangeSet.MIN, cste);
                var.joinWith(dom0, explanation);
            } else /*if (pivot == vars[0])*/ { // x is the pivot, case e. in javadoc
                this.joinWith(explanation.getFreeSet(1), explanation);
                IntIterableRangeSet dom0 = explanation.getRootSet(var);
                dom0.retainBetween(cste + 1, IntIterableRangeSet.MAX);
                var.crossWith(dom0, explanation);
            }
        }
    }


}
