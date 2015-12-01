package com.renren.mobile.web;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * at 4:34 PM, 3/30/12
 *
 * @author afpro
 */
public class SimpleWeakKVMap<K, V> {
    private final static class WPWithKey<K, V> extends WeakReference<V> {
        final K k;

        public WPWithKey(K k, V v, ReferenceQueue<? super V> q) {
            super(v, q);
            this.k = k;
        }
    }

    private final ReferenceQueue<V> referenceQueue = new ReferenceQueue<V>();
    private final Map<K, WPWithKey<K, V>> realMap = new ConcurrentHashMap<K, WPWithKey<K, V>>();

    public void cleanUnreferencedEntry() {
        WPWithKey<K, V> wp;
        while ((wp = (WPWithKey<K, V>) referenceQueue.poll()) != null) {
            realMap.remove(wp.k);
            Utils.simpleLog("remove %s from SimpleWeakKVMap", wp.k.toString());
        }
    }

    public void clear() {
        realMap.clear();
        cleanUnreferencedEntry();
    }

    public boolean containsKey(K o) {
        cleanUnreferencedEntry();
        return realMap.containsKey(o);
    }

    public V get(K o) {
        try {
            if (o != null) {
                WPWithKey<K, V> wp = realMap.get(o);
                if (wp != null) {
                    return wp.get();
                }
            }
            return null;
        } finally {
            cleanUnreferencedEntry();
        }
    }

    public boolean isEmpty() {
        cleanUnreferencedEntry();
        return realMap.isEmpty();
    }

    public Set<K> keySet() {
        cleanUnreferencedEntry();
        return realMap.keySet();
    }

    public V put(K k, V v) {
        cleanUnreferencedEntry();
        realMap.put(k, new WPWithKey<K, V>(k, v, referenceQueue));
        return v;
    }

    public V remove(K o) {
        cleanUnreferencedEntry();
        try {
            /**
             * {@link #remove(Object)} was called in {@link #get(Object)}
             */
            return get(o);
        } finally {
            realMap.remove(o);
        }
    }

    public int size() {
        cleanUnreferencedEntry();
        return realMap.size();
    }
}
