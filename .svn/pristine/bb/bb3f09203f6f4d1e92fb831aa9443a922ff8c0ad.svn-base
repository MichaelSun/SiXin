package com.common.utils;

import static android.util.Log.*;

/**
 * at 下午4:12, 12-7-17
 *
 * @author afpro
 */
public class Logger {
    private final String tag;
    private final int minPriority;

    public Logger(String tag, int minPriority) {
        this.tag = tag;
        this.minPriority = minPriority;
    }

    public Logger(Class clazz, int minPriority) {
        this(clazz.getName(), minPriority);
    }

    public Logger(String tag) {
        this(tag, ERROR);
    }

    public Logger(Class clazz) {
        this(clazz.getName(), ERROR);
    }

    public void log(int priority, String format, Object... args) {
        if (priority >= minPriority) {
            println(priority, tag, args == null || args.length <= 0 ? format : String.format(format, args));
        }
    }

    public void v(String format, Object... args) {
        log(VERBOSE, tag, format, args);
    }

    public void d(String format, Object... args) {
        log(DEBUG, tag, format, args);
    }

    public void i(String format, Object... args) {
        log(INFO, tag, format, args);
    }

    public void w(String format, Object... args) {
        log(WARN, tag, format, args);
    }

    public void e(String format, Object... args) {
        log(ERROR, tag, format, args);
    }

    public void a(String format, Object... args) {
        log(ASSERT, tag, format, args);
    }
}
