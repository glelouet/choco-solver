/**
 *  Copyright (c) 1999-2011, Ecole des Mines de Nantes
 *  All rights reserved.
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the Ecole des Mines de Nantes nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package solver.requests.conditions;

import solver.requests.AbstractRequestWithVar;
import solver.requests.ConditionnalRequest;
import solver.variables.EventType;

import java.io.Serializable;

/**
 * A condition on request scheduling.
 * #validateScheduling can react on a request updating to compute (or update) a condition
 * that validates (or not) the scheduling of the request
 * <p/>
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 17/11/11
 */
public interface ICondition<R extends AbstractRequestWithVar> extends Serializable {

    /**
     * Keep informed the condition of the modification of one of its related requests.
     * If the condition is newly validate, schedule all related requests, if any.
     *
     * @param request recently modified request
     * @param event   event requiring a validation
     */
    boolean validateScheduling(R request, EventType event);

    /**
     * Return the next condition to check, if <code>this</code> is not valid </br>
     * Default value is ICondition.NO_CONDITION
     *
     * @return
     */
    ICondition next();

    /**
     * Link the <code>request</code> to the condition
     *
     * @param request condition request
     */
    public void linkRequest(R request);

    public static enum Default implements ICondition<ConditionnalRequest> {
        NO_CONDITION;

        @Override
        public boolean validateScheduling(ConditionnalRequest request, EventType event) {
            return false;
        }

        @Override
        public ICondition next() {
            return NO_CONDITION;
        }

        @Override
        public void linkRequest(ConditionnalRequest request) {
        }


    }
}
