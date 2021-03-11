package org.chocosolver.parser.annot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public interface ISolution {

	public default List<String> check() {
		List<String> ret = new ArrayList<>();
		Class<? extends ISolution> cl = getClass();
		for (Method meth : cl.getMethods()) {
			// only for methods of type @Check public boolean NAME(){}
			if (meth.getReturnType() == boolean.class && meth.getParameterCount() == 0
					&& meth.isAnnotationPresent(Check.class)) {
				try {
					boolean pass = (boolean) meth.invoke(this);
					if (!pass) {
						ret.add(meth.getName());
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new UnsupportedOperationException("catch this", e);
				}
			}
		}
		return ret;
	}

}
