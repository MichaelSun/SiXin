package plugin.base;

import java.util.BitSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * at 3:03 PM, 7/16/12
 * <p/>
 *
 * @author afpro
 */
public class Permission {
    final BitSet bits = new BitSet();
    final static Map<String, Integer> bitsIndexMap = new TreeMap<String, Integer>();
    final static ReadWriteLock lock = new ReentrantReadWriteLock();

    public Permission() {
    }

    public Permission(String... permissions) {
        if (permissions != null && permissions.length > 0) {
            for (String permission : permissions) {
                bits.set(getIndex(permission), true);
            }
        }
    }

    public Permission(Permission permission) {
        if (permission != null) {
            bits.or(permission.bits);
        }
    }

    static int getIndex(String permission) {
        lock.readLock().lock();
        try {
            final Integer idx = bitsIndexMap.get(permission);
            if (idx != null) {
                return idx;
            }
        } finally {
            lock.readLock().unlock();
        }

        lock.writeLock().lock();
        try {
            final Integer idx = bitsIndexMap.get(permission);
            if (idx != null) {
                return idx;
            }

            final int nextId = bitsIndexMap.size();
            bitsIndexMap.put(permission, nextId);
            return nextId;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 设定特定权限是否需要
     */
    public void set(final String permission, boolean want) {
        bits.set(getIndex(permission), want);
    }

    /**
     * 包含指定权限
     * @param permission 需要包括的权限
     */
    public void include(Permission permission) {
        if (permission != null) {
            bits.or(permission.bits);
        }
    }

    /**
     * 包含指定权限
     * @param permissions 需要包括的权限
     */
    public void include(String... permissions) {
        if (permissions != null && permissions.length > 0) {
            for (String permission : permissions) {
                set(permission, true);
            }
        }
    }

    /**
     * 清除所有权限
     */
    public void clear() {
        bits.clear();
    }

    /**
     * @return 返回权限状态
     */
    public boolean get(final String permission) {
        return bits.get(getIndex(permission));
    }

    /**
     * 判定当前权限是否涵盖了所有目标权限
     *
     * @param want 需要的权限
     * @return true代表本权限涵盖了所有目标权限
     */
    public boolean above(final Permission want) {
        final BitSet bits = (BitSet) this.bits.clone();
        bits.or(want.bits);
        bits.xor(this.bits);
        return bits.cardinality() == 0;
    }

    /**
     * 是否包含所有这些权限
     *
     * @param permissions 需要的权限
     * @return true代表包含所有
     */
    public boolean has(String... permissions) {
        if (permissions == null || permissions.length <= 0) {
            return true;
        }

        for (String permission : permissions) {
            if (!bits.get(getIndex(permission))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "Permission, allowed: " + bits.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o instanceof Permission) {
            return bits.equals(((Permission) o).bits);
        }

        if (o instanceof BitSet) {
            return bits.equals(bits);
        }

        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return bits.hashCode();
    }
}
