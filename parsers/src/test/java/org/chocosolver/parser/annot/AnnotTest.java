package org.chocosolver.parser.annot;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class AnnotTest {

	/**
	 * problem : we have an array of positions, we want to reduce the sum.
	 *
	 * @author
	 *
	 */
	@Solution
	public static class SolutionExample implements ISolution {

		@Range(to = 10)
		int a;

		// Both variable receive the same annotation
		@Range(to = 11)
		int b, c;

		@Minimize
		int sum;

		@Check
		public boolean sum() {
			return a + b + c == sum;
		}

		@Check
		public boolean order() {
			return a < b && b < c;
		}

		@Check
		public boolean diffSame() {
			return a>c-b;
		}

	}

	@Test
	public void testShowAnnotation() throws NoSuchFieldException, SecurityException {
		Class<SolutionExample> cl = SolutionExample.class;
		Field fld = cl.getDeclaredField("c");
		Range annot = fld.getAnnotationsByType(Range.class)[0];
		Assert.assertNotNull(annot);
		Assert.assertEquals(11, annot.to());
		Assert.assertEquals(0, annot.from());

	}

	@Test
	public void testCheck() {
		SolutionExample sol = new SolutionExample();
		sol.sum = 5;
		sol.b = 1;
		sol.c = 2;
		Assert.assertEquals(Arrays.asList("sum", "diffSame"), sol.check());
	}

}
