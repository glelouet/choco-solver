package org.chocosolver.parser.annot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * indicates a field of type int must be assigned a value inside a range.
 *
 * @author glelouet
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Range {

	public int from() default 0;

	public int to();

	/**
	 * requires the range to be holed. A continuous range will only ever be a
	 * single (min, max) couple while a holed range will be a union of continuous
	 * range, eg (1… 2) (4… 5)
	 *
	 * @return the enumeration requirement of that variable
	 */
	public boolean enumerated() default false;

}
