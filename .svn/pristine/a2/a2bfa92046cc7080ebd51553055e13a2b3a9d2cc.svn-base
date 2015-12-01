package com.renren.mobile.web;

import java.util.Arrays;
import java.util.Comparator;

/**
 * at 下午4:06, 12-4-27
 *
 * @author afpro
 */
public class SimpleReadOnlyMap<K extends Comparable<K>, V> {
    final K[] keys;
    final V[] values;
    final int length;

    public SimpleReadOnlyMap(final K[] $keys, final V[] $values) {
        assert $keys != null : "null keys";
        assert $values != null : "null values";
        assert $keys.length == $values.length : "different length";

        keys = $keys.clone();
        values = $values.clone();
        length = $keys.length;

        Integer[] idx = new Integer[length];
        for (int i = 0; i < length; i++) {
            idx[i] = i;
        }

        Arrays.sort(idx, new Comparator<Integer>() {
            @Override
            public int compare(Integer x, Integer y) {
                return $keys[x].compareTo($keys[y]);
            }
        });

        for (int i = 0; i < length; i++) {
            keys[i] = $keys[idx[i]];
            values[i] = $values[idx[i]];
        }
    }

    public V get(K k) {
        if (k == null) {
            return null;
        }

        final int idx = Arrays.binarySearch(keys, k);
        if (idx >= 0) {
            return values[idx];
        }
        return null;
    }

    // 看好哦 是set不是put哦
    public void set(K k, V v) {
        if (k == null) {
            return;
        }

        final int idx = Arrays.binarySearch(keys, k);
        if (idx < 0) {
            return;
        }

        values[idx] = v;
    }

    public K[] cloneKeys() {
        return length <= 0 ? null : keys.clone();
    }

    public V[] cloneValues() {
        return length <= 0 ? null : values.clone();
    }

    public boolean containsKey(K key) {
        return Arrays.binarySearch(keys, key) >= 0;
    }

    public int size() {
        return length;
    }

    public K getKey(int index) {
        return keys[index];
    }

    public V getValue(int index) {
        return values[index];
    }
}
