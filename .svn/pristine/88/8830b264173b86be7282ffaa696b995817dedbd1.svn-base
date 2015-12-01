package com.renren.mobile.web.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * at 2:06 PM, 3/29/12
 *
 * @author afpro
 */
public class Collector {
    /**
     * 在指定的类上面寻找需要的方法和属性
     *
     * @param begin        起始类
     * @param end          终止类（不包含）
     * @param filtersParam 过滤器
     */
    public static void collect(Class begin, Class end, Filter... filtersParam) {
        if (begin == null || filtersParam == null || filtersParam.length <= 0) {
            return;
        }

        List<Filter> filters = new ArrayList<Filter>();
        for (Filter filter : filtersParam) {
            if (filter != null) {
                filters.add(filter);
            }
        }
        final int length = filters.size();

        @SuppressWarnings("unchecked")
        final Set<Method>[] commonMethodsArray = new Set[length];
        @SuppressWarnings("unchecked")
        final List<Method>[] staticMethodsArray = new List[length];
        @SuppressWarnings("unchecked")
        final List<Field>[] fieldsArray = new List[length];

        for (int i = 0; i < length; i++) {
            commonMethodsArray[i] = new TreeSet<Method>(MethodComparator.instance);
            staticMethodsArray[i] = new LinkedList<Method>();
            fieldsArray[i] = new LinkedList<Field>();
        }

        final Queue<Class> inheritance = new LinkedList<Class>();
        for (Class c = begin; c != null && c != end; c = c.getSuperclass()) {
            inheritance.add(c);
        }

        while (!inheritance.isEmpty()) {
            final Class c = inheritance.remove();

            for (Method m : c.getDeclaredMethods()) {
                boolean isStatic = Modifier.isStatic(m.getModifiers());
                for (int i = 0; i < length; i++) {
                    if (filters.get(i).want(m)) {
                        (isStatic ? staticMethodsArray[i] : commonMethodsArray[i]).add(m);
                    }
                }
            }

            for (Field f : c.getDeclaredFields()) {
                for (int i = 0; i < length; i++) {
                    if (filters.get(i).want(f)) {
                        fieldsArray[i].add(f);
                    }
                }
            }
        }

        for (int i = 0; i < length; i++) {
            final Filter filter = filters.get(i);
            filter.commonMethodResult(commonMethodsArray[i]);
            filter.staticMethodResult(staticMethodsArray[i]);
            filter.fieldResult(fieldsArray[i]);
        }
    }

    public static void collect(Class begin, boolean toParent, Filter... filtersParam) {
        if (begin == null) {
            return;
        }
        collect(begin, toParent ? null : begin.getSuperclass(), filtersParam);
    }

    /**
     * 取得所有被复写过的方法
     *
     * @param ori    父类
     * @param target 子类
     * @return 返回所有被复写过的方法
     */
    public static TreeSet<Method> getOverrideMethods(final Class ori, final Class target) {
        if (ori == null || target == null || !ori.isAssignableFrom(target)) {
            return null;
        }

        final TreeSet<Method> oriMethods = new TreeSet<Method>(MethodComparator.instance);
        Collections.addAll(oriMethods, ori.getMethods());

        final TreeSet<Method> decMethods = new TreeSet<Method>(MethodComparator.instance);
        collect(target, ori, new Filter() {
            @Override
            public void commonMethodResult(Collection<Method> result) {
                decMethods.addAll(result);
            }

            @Override
            public boolean want(Method m) {
                return true;
            }

            @Override
            public boolean want(Field f) {
                return false;
            }

            @Override
            public void staticMethodResult(Collection<Method> result) {
            }

            @Override
            public void fieldResult(Collection<Field> result) {
            }
        });

        final TreeSet<Method> overrideMethods = new TreeSet<Method>(MethodComparator.instance);
        for (Method m : decMethods) {
            if (oriMethods.contains(m)) {
                overrideMethods.add(m);
            }
        }

        return overrideMethods;
    }
}

class MethodComparator implements Comparator<Method> {
    public final static MethodComparator instance = new MethodComparator();

    @Override
    public int compare(Method x, Method y) {
        final String xName = x.getName();
        final String yName = y.getName();
        if (!xName.equals(yName)) {
            return xName.compareTo(yName);
        }

        final Class xRet = x.getReturnType();
        final Class yRet = y.getReturnType();
        if (xRet != yRet) {
            return xRet.getName().compareTo(yRet.getName());
        }

        final Class[] xParams = x.getParameterTypes();
        final Class[] yParams = y.getParameterTypes();

        final int xLength = xParams.length;
        final int yLength = yParams.length;
        if (xLength != yLength) {
            return xLength - yLength;
        }

        final int maxLength = Math.max(xLength, yLength);
        for (int i = 0; i < maxLength; i++) {
            final Class xp = xParams[i];
            final Class yp = yParams[i];
            if (xp != yp) {
                return xp.getName().compareTo(yp.getName());
            }
        }

        return 0;
    }
}
