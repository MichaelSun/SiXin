package com.renren.mobile.web.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * at 2:58 PM, 3/29/12
 * <p/>
 * 用于在指定的类上面寻找想要的方法或者属性
 *
 * @author afpro
 * @see com.renren.mobile.web.reflect.Collector
 */
public interface Filter {
    /**
     * @param m 方法
     * @return true代表本Filter需要这个方法
     */
    boolean want(Method m);

    /**
     * @param f 属性
     * @return true代表本Filter需要这个属性
     */
    boolean want(Field f);

    /**
     * 搜索结束后调用
     *
     * @param result 所有需要的非静态方法
     */
    void commonMethodResult(Collection<Method> result);

    /**
     * 搜索结束后调用
     *
     * @param result 所有需要的静态方法
     */
    void staticMethodResult(Collection<Method> result);

    /**
     * 搜索结束后调用
     *
     * @param result 所有需要的属性
     */
    void fieldResult(Collection<Field> result);
}
