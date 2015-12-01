package com.renren.mobile.web.reflect;

import android.webkit.WebChromeClient;
import com.renren.mobile.web.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * at 4:12 PM, 4/11/12
 *
 * @author afpro
 */
public class WebChromeClientWrapper extends WebChromeClient {
    private WebChromeClient client;
    private TreeSet<Method> overrideMethods;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public final void setClient(WebChromeClient client) {
        lock.writeLock().lock();
        try {
            this.client = client;
            if (client == null) {
                return;
            }
            overrideMethods = Collector.getOverrideMethods(WebChromeClient.class, client.getClass());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public final WebChromeClient getClient() {
        lock.readLock().lock();
        try {
            return client;
        } finally {
            lock.readLock().unlock();
        }
    }

    private Method getMethod(String methodName, Class... args) {
        try {
            Method ori = WebChromeClient.class.getMethod(methodName, args);
            if (overrideMethods != null && !overrideMethods.isEmpty()) {
                for (Method m : overrideMethods) {
                    if (MethodComparator.instance.compare(m, ori) == 0) {
                        return m;
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            Utils.logStackTrace(e);
        }
        return null;
    }

    protected final Object invoke(String name, Class[] argTypes, Object... args) throws NoSuchMethodException {
        lock.readLock().lock();
        try {
            if (client != null) {
                Method m = getMethod(name, argTypes);
                if (m != null) {
                    try {
                        return m.invoke(client, args);
                    } catch (IllegalAccessException e) {
                        Utils.logStackTrace(e);
                    } catch (IllegalArgumentException e) {
                        Utils.logStackTrace(e);
                    } catch (InvocationTargetException e) {
                        Utils.logStackTrace(e);
                    }
                }
            }
            throw new NoSuchMethodException();
        } finally {
            lock.readLock().unlock();
        }
    }
}
