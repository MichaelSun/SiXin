package com.renren.mobile.web;

import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.WebView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

/**
 * at 6:30 PM, 3/2/12
 *
 * @author afpro
 */
public class Utils {
    public final static String TAG = "fuck";

    public static void simpleLog(String format, Object... args) {
//        Log.d(TAG, String.format(format, args));
    }

    public static void logStackTrace(Throwable e) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (e != null) {
            e.printStackTrace(new PrintStream(bos));
        }
//        Log.e(TAG, bos.toString());
    }

    public static void logJSConsoleMessage(String message, int lineNumber, String sourceId) {
//        Log.d(TAG, String.format("console.log(%s), at(%s:%d)", message, sourceId, lineNumber));
    }

    public static void simpleEcho(InputStream ins, OutputStream ous) throws IOException {
        byte[] buf = new byte[4096];
        int length;
        while ((length = ins.read(buf)) >= 0) {
            ous.write(buf, 0, length);
        }
    }

    public static boolean safeSimpleEcho(InputStream is, OutputStream os) {
        try {
            simpleEcho(is, os);
            return true;
        } catch (IOException e) {
            logStackTrace(e);
            return false;
        }
    }

    public static boolean safeClose(Closeable closeable) {
        try {
            closeable.close();
            return true;
        } catch (IOException e) {
            logStackTrace(e);
            return false;
        }
    }

    private static InputStream openAsset(final AssetManager assetManager, final String path) {
        assert assetManager != null && path != null;
        try {
            return assetManager.open(path);
        } catch (IOException e) {
            logStackTrace(e);
            return null;
        }
    }

    public static String safeReadAssetTextFile(final AssetManager assetManager, final String path) {
        assert assetManager != null && path != null;

        final InputStream is = openAsset(assetManager, path);
        if (is == null) {
            return null;
        }

        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                if (safeSimpleEcho(is, bos)) {
                    return bos.toString();
                }
            } finally {
                safeClose(bos);
            }
        } finally {
            safeClose(is);
        }
        return null;
    }

    public static String safeReadAssetAsBase64(final AssetManager assetManager, final String path) {
        assert assetManager != null && path != null;

        final InputStream is = openAsset(assetManager, path);
        if (is == null) {
            return null;
        }

        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                if (safeSimpleEcho(is, bos)) {
                    return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
                }
            } finally {
                safeClose(bos);
            }
        } finally {
            safeClose(is);
        }
        return null;
    }

    public static void valueToString(StringBuilder sb, Object value) {
        if (value == null || value.equals(null)) {
            sb.append("null");
        } else if (value instanceof Number) {
            String string = value.toString();
            if (string.indexOf('.') > 0 && string.indexOf('e') < 0 &&
                    string.indexOf('E') < 0) {
                while (string.endsWith("0")) {
                    string = string.substring(0, string.length() - 1);
                }
                if (string.endsWith(".")) {
                    string = string.substring(0, string.length() - 1);
                }
            }
            sb.append(string);
        } else if (value instanceof Map) {
            sb.append('{');
            final int count = ((Map) value).size();
            int index = 0;
            for (Object o : ((Map) value).entrySet()) {
                final Map.Entry e = (Map.Entry) o;
                valueToString(sb, e.getKey());
                sb.append(':');
                valueToString(sb, e.getValue());
                if (++index < count) {
                    sb.append(',');
                }
            }
            sb.append('}');
        } else if (value instanceof Collection) {
            final int count = ((Collection) value).size();
            int index = 0;
            sb.append('[');
            for (Object o : (Collection) value) {
                valueToString(sb, o);
                if (++index < count) {
                    sb.append(',');
                }
            }
            sb.append(']');
        } else if (value.getClass().isArray()) {
            final int length = Array.getLength(value);
            sb.append('[');
            for (int i = 0; i < length; ) {
                valueToString(sb, Array.get(value, i));
                if (++i < length) {
                    sb.append(',');
                }
            }
            sb.append(']');
        } else if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
            sb.append(value.toString());
        } else {
            quote(sb, value.toString());
        }
    }

    public static void quote(StringBuilder sb, String string) {
        if (string == null || string.length() == 0) {
            sb.append("\"\"");
        }

        char b;
        char c = 0;
        String hhhh;
        int i;
        int len = string.length();

        sb.append('"');
        for (i = 0; i < len; i += 1) {
            b = c;
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '/':
                    if (b == '<') {
                        sb.append('\\');
                    }
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (c < ' ' || (c >= '\u0080' && c < '\u00a0') ||
                            (c >= '\u2000' && c < '\u2100')) {
                        hhhh = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(hhhh.substring(hhhh.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
    }

    public static String quote(String string) {
        StringBuilder sb = new StringBuilder();
        quote(sb, string);
        return sb.toString();
    }

    public static String valueToString(Object value) {
        StringBuilder sb = new StringBuilder();
        valueToString(sb, value);
        return sb.toString();
    }

    public static String jsonArray(Object... values) {
        if (values == null || values.length <= 0) {
            return "[]";
        }
        return valueToString(values);
    }

    public static String jsonParams(Object... values) {
        if (values == null || values.length <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; ) {
            valueToString(sb, values[i]);
            if (++i < values.length) {
                sb.append(',');
            }
        }
        return sb.toString();
    }

    public static boolean isInUIThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

    public static Handler createUIHandler() {
        return new Handler(Looper.getMainLooper());
    }

    public static void assertInUIThread() {
        assert isInUIThread() : "do this in UI thread, please, you fucker.";
    }

    public static String createUrl(String base, Map<String, String> queries) {
        StringBuilder sb = new StringBuilder();
        if (isEmpty(base)) {
            sb.append('/');
        } else {
            sb.append(base);
        }
        if (queries != null && !queries.isEmpty()) {
            sb.append('?');
            for (Map.Entry<String, String> query : queries.entrySet()) {
                if (query != null) {
                    final String k = query.getKey();

                    if (!isEmpty(k)) {
                        sb.append(Uri.encode(k));
                        final String v = query.getValue();
                        if (!isEmpty(v)) {
                            sb.append('=').append(Uri.encode(v));
                        }
                        sb.append('&');
                    }
                }
            }
        }
        return sb.toString();
    }

    public static void triggerCallback(final WebView webView, boolean success, Object callbackId, Object args) {
        if (webView == null || callbackId == null) {
            return;
        }

        final String jsUrl = String.format("javascript:window.Bridge.%sCallback(%s)",
                success ? "success" : "fail", jsonParams(callbackId, args));
        webView.loadUrl(jsUrl);
    }

    public static void triggerCallbackWithParams(final WebView webView, boolean success, Object callbackId, String params) {
        if (webView == null || callbackId == null) {
            return;
        }

        final String jsUrl = String.format("javascript:window.Bridge.%sCallback(%s, %s)",
                success ? "success" : "fail", Utils.jsonParams(callbackId), params);
        webView.loadUrl(jsUrl);
    }

    public static float loc(long renrenLocation) {
        return renrenLocation / 1000000f;
    }

    public static long deLoc(float n) {
        return (long) (n * 1000000);
    }

    public static long deLoc(double n) {
        return (long) (n * 1000000);
    }

    public static int limitIn(int value, int min, int max) {
        if (max < min) {
            return min;
        }

        if (value < min) {
            return min;
        }

        if (value > max) {
            return max;
        }

        return value;
    }

    public static <T> boolean safePut(JSONObject jsonObject, String name, T value) {
        assert jsonObject != null;
        assert name != null;
        try {
            jsonObject.put(name, value);
            return true;
        } catch (Throwable e) {
            logStackTrace(e);
            return false;
        }
    }

    public static long getLong(String str, long defaultValue) {
        if (TextUtils.isEmpty(str)) {
            return defaultValue;
        }

        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int getInt(String str, int defaultValue) {
        if (TextUtils.isEmpty(str)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    public static float getFloat(String str, float defaultValue) {
        if (TextUtils.isEmpty(str)) {
            return defaultValue;
        }

        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static double getDouble(String str, double defaultValue) {
        if (TextUtils.isEmpty(str)) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String createURLSignature(String url, String base, String... params) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        if (base == null) {
            sb.append(URLDispatcher.getPathAndQueryString(url)[0]);
        } else {
            sb.append(base);
        }
        sb.append('|');
        final String query = URLDispatcher.getPathAndQueryString(url)[1];
        final Map<String, String> queries = URLDispatcher.getQueries(query);
        if (params != null && params.length > 0) {
            for (String param : params) {
                final String value = queries.get(param);
                sb.append(param).append('-').append(value).append('|');
            }
        }
        return sb.toString();
    }

    public static boolean toBoolean(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }

        if (string.equalsIgnoreCase("false")) {
            return false;
        }

        boolean allZero = true;
        for (char c : string.toCharArray()) {
            if (c != '0') {
                allZero = false;
                break;
            }
        }

        return !allZero;
    }

    public static Object get(Object object, Class target) throws IllegalArgumentException {
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
}