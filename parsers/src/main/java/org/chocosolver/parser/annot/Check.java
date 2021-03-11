package org.chocosolver.parser.annot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * indicates a variable, an array of variable, a collection of variable, or a
 * method returning such, should be translated to constraints and posted in the
 * solver.
 *
 * @author glelouet
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Check {

}
