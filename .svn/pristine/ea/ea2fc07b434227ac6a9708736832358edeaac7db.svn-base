package com.renren.mobile.web;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

/**
 * at 下午3:01, 12-4-27
 *
 * @author afpro
 */
public class URLDispatcher {
    private final static Pattern simpleSplitPattern = Pattern.compile("^([^:/]+://[^/]+)?([^\\?]+)(\\?([^#]+))?");
    private final static Pattern splitURLPattern = Pattern.compile("(^|/)([^/?]+)");
    private final static Pattern splitQueryPattern = Pattern.compile("([^&=]+)(=([^&=]+))?");
    private final static ThreadLocal<String> url = new ThreadLocal<String>();
    private final static ThreadLocal<ArrayList<String>> parts = new ThreadLocal<ArrayList<String>>();
    public final static String TAG_PARAM_NAME = "[tag]";

    public static String[] getPathAndQueryString(String url) {
        if (url == null) {
            return new String[]{null, null};
        }

        final Matcher matcher = simpleSplitPattern.matcher(url);
        if (!matcher.find()) {
            return new String[]{null, null};
        }

        final String path = matcher.group(2);
        final String query = matcher.group(4);
        return new String[]{path, query};
    }

    public static ArrayList<String> getParts(String path) {
        if (isEmpty(path)) {
            return new ArrayList<String>(0);
        }

        final Matcher matcher = splitURLPattern.matcher(path);
        final ArrayList<String> list = new ArrayList<String>();
        while (matcher.find()) {
            final String part = matcher.group(2);
            list.add(part);
        }
        return list;
    }

    public static Map<String, String> getQueries(String queryString) {
        if (isEmpty(queryString)) {
            return Collections.emptyMap();
        }

        final Map<String, String> map = new TreeMap<String, String>();
        final Matcher m = splitQueryPattern.matcher(queryString);
        while (m.find()) {
            final String name = m.group(1);
            final String value = m.group(3);
            map.put(Uri.decode(name), Uri.decode(value));
        }
        return map;
    }

    public static interface Receiver {
        /**
         * @param params  path中指定需要的参数
         * @param queries URL中query部分
         * @return 是否要占有本URL，如果返回false则会匹配其他能够匹配的Receiver
         */
        boolean got(Map<String, String> params, Map<String, String> queries);
    }

    private static class State {
        Receiver receiver = null;
        String tag = null;
        Map<String, State> map = new TreeMap<String, State>();
        SimpleReadOnlyMap<Integer, String> need = null;

        private static boolean isTag(String part) {
            return part.startsWith("[") && part.endsWith("]");
        }

        private static boolean isVar(String part) {
            return part.startsWith("${") && part.endsWith("}");
        }

        boolean end(String[] parts, Map<String, String> queries) {
            if (receiver != null && need != null) {
                Map<String, String> params = new TreeMap<String, String>();
                for (int i = 0, count = need.size(); i < count; i++) {
                    final int idx = need.getKey(i);
                    final String name = need.getValue(i);
                    params.put(name, parts[idx]);
                }
                if (tag != null) {
                    params.put(TAG_PARAM_NAME, tag);
                }
                return receiver.got(params, queries);
            }
            return false;
        }

        boolean nextPart(String[] parts, Map<String, String> queries, int offset) {
            if (offset >= parts.length) {
                return end(parts, queries);
            }
            final String part = parts[offset];
            final State nextAll = map.get("");
            final State nextThis = map.get(part);
            if (nextThis != null) {
                if (nextThis.nextPart(parts, queries, offset + 1)) {
                    return true;
                }
            }
            if (nextAll != null) {
                if (nextAll.nextPart(parts, queries, offset + 1)) {
                    return true;
                }
            }
            return false;
        }

        void addRule(String[] parts, int offset, Receiver receiver) {
            switch (parts.length - offset) {
                case 1:
                    if (isTag(parts[offset])) {
                        final String tagString = parts[offset];
                        tag = tagString.substring(1, tagString.length() - 1);
                    } else {
                        break;
                    }
                case 0:
                    this.receiver = receiver;
                    boolean[] want = new boolean[parts.length];

                    int count = 0;
                    for (int i = 0; i < parts.length; i++) {
                        if (isVar(parts[i])) {
                            count++;
                            want[i] = true;
                        } else {
                            want[i] = false;
                        }
                    }

                    Integer[] idx = new Integer[count];
                    String[] str = new String[count];
                    for (int i = 0, j = 0; i < want.length && j < count; i++) {
                        final String part = parts[i];
                        if (want[i]) {
                            idx[j] = i;
                            str[j] = part.substring(2, part.length() - 1);
                            j++;
                        }
                    }

                    need = new SimpleReadOnlyMap<Integer, String>(idx, str);
                    return;
            }

            // step
            final String part = parts[offset];
            if (isVar(part)) {
                State target = map.get("");
                if (target == null) {
                    map.put("", target = new State());
                }
                target.addRule(parts, offset + 1, receiver);
            } else {
                int beg = 0;
                while (beg < part.length()) {
                    int end = part.indexOf('|', beg);
                    String name;
                    if (end > 0) {
                        name = part.substring(beg, end);
                        beg = end + 1;
                    } else {
                        name = part.substring(beg);
                        beg = part.length();
                    }
                    if (!isEmpty(name)) {
                        State target = map.get(name);
                        if (target == null) {
                            map.put(name, target = new State());
                        }
                        target.addRule(parts, offset + 1, receiver);
                    }
                }
            }
        }
    }

    State root = new State();
    ReadWriteLock lock = new ReentrantReadWriteLock();

    public void clearRules() {
        lock.writeLock().lock();
        try {
            root = new State();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addRule(String pattern, Receiver receiver) {
        if (pattern == null || receiver == null) {
            return;
        }

        final String[] pathAndQuery = getPathAndQueryString(pattern);
        if (pathAndQuery[0] == null) {
            return;
        }

        final ArrayList<String> parts = getParts(pathAndQuery[0]);
        lock.writeLock().lock();
        try {
            root.addRule(parts.toArray(new String[parts.size()]), 0, receiver);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean dispatch(String url) {
        if (url == null) {
            return false;
        }

        final String[] pathAndQuery = getPathAndQueryString(url);
        final ArrayList<String> parts = getParts(pathAndQuery[0]);

        final String preUrl = URLDispatcher.url.get();
        final ArrayList<String> preParts = URLDispatcher.parts.get();
        URLDispatcher.url.set(url);
        URLDispatcher.parts.set(parts);
        final Map<String, String> queries = getQueries(pathAndQuery[1]);
        lock.readLock().lock();
        try {
            return root.nextPart(parts.toArray(new String[parts.size()]), queries, 0);
        } finally {
            lock.readLock().unlock();
            URLDispatcher.url.set(preUrl);
            URLDispatcher.parts.set(preParts);
        }
    }

    public static String currentURL() {
        return url.get();
    }

    public static int currentPartCount() {
        final ArrayList<String> parts = URLDispatcher.parts.get();
        return parts == null ? 0 : parts.size();
    }

    public static String getPart(int index) {
        final ArrayList<String> parts = URLDispatcher.parts.get();
        if (parts == null || index < 0 || index >= parts.size()) {
            return null;
        }
        return parts.get(index);
    }

    public static String getTag(final Map<String, String> params) {
        return params.get(TAG_PARAM_NAME);
    }
}
