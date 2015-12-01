package com.renren.mobile.web.reflect;

import android.text.TextUtils;
import com.renren.mobile.web.Plugin;
import com.renren.mobile.web.Utils;
import com.renren.mobile.web.WebResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

/**
 * at 6:44 PM, 3/7/12
 *
 * @author afpro
 */
public class PluginWrapper implements Plugin {
    final Object realObject;
    final Map<String, Method> methodMap = new HashMap<String, Method>();
    final Method[] resourceMethods;
    final Method[] jsCodeMethods;
    final Field[] jsCodeFields;
    final Map<String, Pattern> patternMap = new TreeMap<String, Pattern>();

    public PluginWrapper() {
        realObject = this;

        final List<Method> resourceMethods = new LinkedList<Method>();
        final List<Method> jsCodeMethods = new LinkedList<Method>();
        final List<Field> jsCodeFields = new LinkedList<Field>();
        Collector.collect(realObject.getClass(), true,
                new ResourceMethodFilter(resourceMethods, patternMap),
                new JSFilter(this, methodMap),
                new JSCodeFilter(jsCodeMethods, jsCodeFields)
        );

        this.resourceMethods = resourceMethods.toArray(new Method[resourceMethods.size()]);
        this.jsCodeFields = jsCodeFields.toArray(new Field[jsCodeFields.size()]);
        this.jsCodeMethods = jsCodeMethods.toArray(new Method[jsCodeMethods.size()]);
    }

    public PluginWrapper(Object realObject) {
        this.realObject = realObject == null ? this : realObject;

        final List<Method> resourceMethods = new LinkedList<Method>();
        final List<Method> jsCodeMethods = new LinkedList<Method>();
        final List<Field> jsCodeFields = new LinkedList<Field>();
        Collector.collect(this.realObject.getClass(), true,
                new ResourceMethodFilter(resourceMethods, patternMap),
                new JSFilter(this, methodMap),
                new JSCodeFilter(jsCodeMethods, jsCodeFields)
        );

        this.resourceMethods = resourceMethods.toArray(new Method[resourceMethods.size()]);
        this.jsCodeFields = jsCodeFields.toArray(new Field[jsCodeFields.size()]);
        this.jsCodeMethods = jsCodeMethods.toArray(new Method[jsCodeMethods.size()]);
    }

    public PluginWrapper(Object realObject, Class certainClass) {
        this.realObject = realObject == null ? this : realObject;
        if (certainClass == null || !certainClass.isAssignableFrom(this.realObject.getClass())) {
            throw new IllegalArgumentException("illegal certainClass argument.");
        }
        final List<Method> resouceMethods = new LinkedList<Method>();
        final List<Method> jsCodeMethods = new LinkedList<Method>();
        final List<Field> jsCodeFields = new LinkedList<Field>();
        Collector.collect(certainClass, false,
                new ResourceMethodFilter(resouceMethods, patternMap),
                new JSFilter(this, methodMap),
                new JSCodeFilter(jsCodeMethods, jsCodeFields)
        );

        this.resourceMethods = resouceMethods.toArray(new Method[resouceMethods.size()]);
        this.jsCodeFields = jsCodeFields.toArray(new Field[jsCodeFields.size()]);
        this.jsCodeMethods = jsCodeMethods.toArray(new Method[jsCodeMethods.size()]);
    }

    public boolean need(String jsMethodName) {
        return true;
    }

    private static Object get(Object object, Class target) throws IllegalArgumentException {
        if (object == null || target == null) {
            return null;
        }

        if (target.isAssignableFrom(object.getClass())) {
            return object;
        }

        if (target == String.class) {
            return object.toString();
        }

        boolean toBoolean = false;
        boolean toByte = false;
        boolean toShort = false;
        boolean toInt = false;
        boolean toLong = false;
        boolean toFloat = false;
        boolean toDouble = false;

        if (target == boolean.class || target == Boolean.class) {
            toBoolean = true;
        } else if (target == byte.class || target == Byte.class) {
            toByte = true;
        } else if (target == short.class || target == Short.class) {
            toShort = true;
        } else if (target == int.class || target == Integer.class) {
            toInt = true;
        } else if (target == long.class || target == Long.class) {
            toLong = true;
        } else if (target == float.class || target == Float.class) {
            toFloat = true;
        } else if (target == double.class || target == Double.class) {
            toDouble = true;
        }

        if (String.class == object.getClass()) {
            String str = (String) object;
            if (toBoolean) {
                return "t".equalsIgnoreCase(str) || "true".equalsIgnoreCase(str);
            }
            try {
                if (toByte) {
                    return Byte.parseByte(str);
                }
                if (toShort) {
                    return Short.parseShort(str);
                }
                if (toInt) {
                    return Integer.parseInt(str);
                }
                if (toLong) {
                    return Long.parseLong(str);
                }
                if (toFloat) {
                    return Float.parseFloat(str);
                }
                if (toDouble) {
                    return Double.parseDouble(str);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(String.format("can't change %s to %s", object.getClass().getName(), target.getName()), e);
            }
        }

        if (object instanceof Number) {
            Number num = (Number) object;
            if (toBoolean) {
                return num.intValue() != 0;
            }
            if (toByte) {
                return num.byteValue();
            }
            if (toShort) {
                return num.shortValue();
            }
            if (toInt) {
                return num.intValue();
            }
            if (toLong) {
                return num.longValue();
            }
            if (toFloat) {
                return num.floatValue();
            }
            if (toDouble) {
                return num.doubleValue();
            }
        }

        if (object == JSONObject.NULL) {
            return null;
        }
        throw new IllegalArgumentException(String.format("can't change %s to %s", object.getClass().getName(), target.getName()));
    }

    @Override
    public String[] methods() {
        return methodMap.keySet().toArray(new String[methodMap.size()]);
    }

    @Override
    public String externalJSCode() {
        StringBuilder sb = new StringBuilder();

        // js code method
        for (Method m : jsCodeMethods) {
            try {
                if (!m.isAccessible()) {
                    m.setAccessible(true);
                }
                String str = (String) m.invoke(realObject);
                if (!TextUtils.isEmpty(str)) {
                    sb.append(str).append(';');
                }
            } catch (IllegalAccessException ignored) {
            } catch (InvocationTargetException e) {
                Utils.logStackTrace(e);
            }
        }

        // js code field
        for (Field f : jsCodeFields) {
            try {
                String str = (String) f.get(realObject);
                if (!TextUtils.isEmpty(str)) {
                    sb.append(str).append(';');
                }
            } catch (IllegalAccessException ignored) {
            }
        }

        return sb.length() <= 0 ? null : sb.toString();
    }

    @Override
    public String call(String method, JSONArray arguments) {
        Method m = methodMap.get(method);
        if (m == null) {
            Utils.simpleLog("method %s not found", method);
            return null;
        }

        try {
            final Class[] parameterTypes = m.getParameterTypes();
            Object ret;
            if (parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(JSONArray.class)) {
                ret = m.invoke(realObject, arguments);
            } else {
                if (parameterTypes.length > arguments.length()) {
                    Utils.simpleLog("not enough parameters (%d<%d)", arguments.length(), parameterTypes.length);
                    return null;
                }
                Object[] params = new Object[parameterTypes.length];
                for (int i = 0; i < params.length; i++) {
                    Object object = arguments.get(i);
                    params[i] = get(object, parameterTypes[i]);
                }

                if (!m.isAccessible()) {
                    m.setAccessible(true);
                }
                ret = m.invoke(realObject, params);
            }

            if (ret != null) {
                return ret.toString();
            }
        } catch (Exception e) {
            Utils.simpleLog("js call(method={%s:%s}, target={%s}, arg=%s)", method, m, realObject, arguments);
            Utils.logStackTrace(e);
        }
        return null;
    }

    @Override
    public WebResponse handleUrlRequest(final URI uri) {
        for (Method m : resourceMethods) {
            String filter = m.getAnnotation(ResourceMethod.class).value();
            if (TextUtils.isEmpty(filter) || patternMap.get(filter).matcher(uri.getPath()).matches()) {
                Utils.simpleLog("match{%s<->%s}", filter, uri.getPath());
                try {
                    if (!m.isAccessible()) {
                        m.setAccessible(true);
                    }
                    WebResponse response = (WebResponse) m.invoke(realObject, uri);
                    if (response != null) {
                        return response;
                    }
                } catch (Exception e) {
                    Utils.simpleLog("http call: method{%s} uri{%s})", m, uri);
                    Utils.logStackTrace(e);
                }
            }
        }
        return null;
    }
}

class ResourceMethodFilter implements Filter {
    private final List<Method> result;
    private final Map<String, Pattern> patternMap;

    public ResourceMethodFilter(List<Method> result, Map<String, Pattern> patternMap) {
        this.result = result;
        this.patternMap = patternMap;
    }

    @Override
    public boolean want(Method method) {
        if (method.isAnnotationPresent(ResourceMethod.class)) {
            final Class retType = method.getReturnType();
            final Class[] paramTypes = method.getParameterTypes();
            if (WebResponse.class.isAssignableFrom(retType)) {
                if (paramTypes.length == 1 && paramTypes[0].isAssignableFrom(URI.class)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean want(Field f) {
        return false;
    }

    private void add(Method m) {
        result.add(m);
        String pat = m.getAnnotation(ResourceMethod.class).value();
        if (!TextUtils.isEmpty(pat)) {
            if (!patternMap.containsKey(pat)) {
                patternMap.put(pat, Pattern.compile(pat));
            }
        }
    }

    @Override
    public void commonMethodResult(Collection<Method> result) {
        for (Method m : result) {
            add(m);
        }
    }

    @Override
    public void staticMethodResult(Collection<Method> result) {
        for (Method m : result) {
            add(m);
        }
    }

    @Override
    public void fieldResult(Collection<Field> result) {
    }
}

class JSFilter implements Filter {
    private final Map<String, Method> methodMap;
    private final PluginWrapper pluginWrapper;
    private final static Set<Class> BASE_TYPE;

    static {
        BASE_TYPE = new HashSet<Class>();
        BASE_TYPE.add(boolean.class);
        BASE_TYPE.add(Boolean.class);
        BASE_TYPE.add(byte.class);
        BASE_TYPE.add(Byte.class);
        BASE_TYPE.add(short.class);
        BASE_TYPE.add(Short.class);
        BASE_TYPE.add(int.class);
        BASE_TYPE.add(Integer.class);
        BASE_TYPE.add(long.class);
        BASE_TYPE.add(Long.class);
        BASE_TYPE.add(float.class);
        BASE_TYPE.add(Float.class);
        BASE_TYPE.add(double.class);
        BASE_TYPE.add(Double.class);
        BASE_TYPE.add(String.class);
    }

    public JSFilter(PluginWrapper pluginWrapper, Map<String, Method> methodMap) {
        this.pluginWrapper = pluginWrapper;
        this.methodMap = methodMap;
    }

    @Override
    public boolean want(Method method) {
        JSMethod jsMethod = method.getAnnotation(JSMethod.class);
        if (jsMethod == null || !pluginWrapper.need(jsMethod.value())) {
            return false;
        }

        for (Class param : method.getParameterTypes()) {
            if (BASE_TYPE.contains(param) || param.isAssignableFrom(JSONObject.class) || param.isAssignableFrom(JSONArray.class)) {
                continue;
            }
            return false;
        }

        return true;
    }

    @Override
    public boolean want(Field f) {
        return false;
    }

    @Override
    public void commonMethodResult(Collection<Method> result) {
        for (Method m : result) {
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            methodMap.put(m.getAnnotation(JSMethod.class).value(), m);
        }
    }

    @Override
    public void staticMethodResult(Collection<Method> result) {
        for (Method m : result) {
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            methodMap.put(m.getAnnotation(JSMethod.class).value(), m);
        }
    }

    @Override
    public void fieldResult(Collection<Field> result) {
    }
}

class JSCodeFilter implements Filter {
    private final List<Method> methodResult;
    private final List<Field> fieldResult;

    public JSCodeFilter(List<Method> methodResult, List<Field> fieldResult) {
        this.methodResult = methodResult;
        this.fieldResult = fieldResult;
    }

    @Override
    public boolean want(Method method) {
        if (method.isAnnotationPresent(JSCode.class)) {
            final Class retType = method.getReturnType();
            final Class[] paramTypes = method.getParameterTypes();
            if (paramTypes.length == 0 && String.class.isAssignableFrom(retType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean want(Field field) {
        return field.isAnnotationPresent(JSCode.class) && String.class.isAssignableFrom(field.getType());
    }

    @Override
    public void commonMethodResult(Collection<Method> result) {
        methodResult.addAll(result);
        for (Method m : result) {
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            methodResult.add(m);
        }
    }

    @Override
    public void staticMethodResult(Collection<Method> result) {
        methodResult.addAll(result);
        for (Method m : result) {
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            methodResult.add(m);
        }
    }

    @Override
    public void fieldResult(Collection<Field> result) {
        for (Field f : result) {
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            fieldResult.add(f);
        }
    }
}
