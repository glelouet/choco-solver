package org.chocosolver.parser.annot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * indicates the class represents a solution of a problem. Each variable it
 * contains must then be assigned a source of possible values.
 *
 * @author glelouet
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Solution {

}
