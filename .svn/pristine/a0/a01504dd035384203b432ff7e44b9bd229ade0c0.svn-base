package plugin.base;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * at 1:55 PM, 7/16/12
 *
 * @author afpro
 */
public class ClassType {
    private final static Map<Class, Integer> ciMap = new TreeMap<Class, Integer>();
    private final static Map<Integer, Class> icMap = new TreeMap<Integer, Class>();
    private final static ReadWriteLock lock = new ReentrantReadWriteLock();

    public static int addClass(Class clazz) {
        lock.writeLock().lock();
        try {
            Integer id = ciMap.get(clazz);
            if (id != null) {
                return id;
            }

            int nextId = ciMap.size();
            ciMap.put(clazz, nextId);
            icMap.put(nextId, clazz);
            return nextId;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static int getTypeId(Class clazz) {
        lock.readLock().lock();
        try {
            Integer id = ciMap.get(clazz);
            if (id != null) {
                return id;
            }
        } finally {
            lock.readLock().unlock();
        }
        return addClass(clazz);
    }

    public static Class getTypeClass(int id) {
        lock.readLock().lock();
        try {
            return icMap.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static boolean check(Class clazz, Class target, boolean restrict) {
        return !(clazz == null || target == null) && (restrict ? target == clazz : target.isAssignableFrom(clazz));
    }

    public static boolean check(int clazzId, Class target, boolean restrict) {
        final Class clazz = getTypeClass(clazzId);
        return check(clazz, target, restrict);
    }

    public static boolean check(Class clazz, int targetId, boolean restrict) {
        final Class target = getTypeClass(targetId);
        return check(clazz, target, restrict);
    }

    public static boolean check(int clazzId, int targetId, boolean restrict) {
        Class clazz, target;
        lock.readLock().lock();
        try {
            clazz = icMap.get(clazzId);
            target = icMap.get(targetId);
        } finally {
            lock.readLock().unlock();
        }
        return check(clazz, target, restrict);
    }
}
