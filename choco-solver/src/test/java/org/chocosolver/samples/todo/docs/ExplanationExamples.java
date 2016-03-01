/**
 * Copyright (c) 2015, Ecole des Mines de Nantes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    This product includes software developed by the <organization>.
 * 4. Neither the name of the <organization> nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <COPYRIGHT HOLDER> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.chocosolver.samples.todo.docs;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.testng.annotations.Test;

import static org.chocosolver.solver.search.strategy.SearchStrategyFactory.inputOrderLBSearch;
import static org.chocosolver.solver.trace.Chatterbox.showStatistics;

/**
 *
 * @author Charles Prud'homme
 * @version choco
 * @since 03/10/2014
 */
public class ExplanationExamples {

    @Test(groups="1s", timeOut=60000)
    public void dummy() {
        Model model = new Model();
        BoolVar[] bvars = model.boolVarArray("B", 4);
        model.arithm(bvars[2], "=", bvars[3]).post();
        model.arithm(bvars[2], "!=", bvars[3]).post();
        model.getSolver().set(inputOrderLBSearch(bvars));
        model.getSolver().setCBJLearning(false, false);
        showStatistics(model);
        while (model.solve()) ;
    }

    @Test(groups="1s", timeOut=60000)
    public void pigeon() {
        Model model = new Model();
        IntVar[] pigeon = model.intVarArray("p", 5, 1, 4, false);
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 5; j++) {
                model.arithm(pigeon[i], "!=", pigeon[j]).post();
            }
        }
        model.getSolver().set(inputOrderLBSearch(pigeon));
        model.getSolver().setCBJLearning(false, false);
        showStatistics(model);
        while (model.solve()) ;
    }
}