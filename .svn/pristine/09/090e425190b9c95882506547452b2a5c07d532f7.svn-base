package com.renren.mobile.web;

import com.renren.mobile.web.reflect.JSMethod;
import com.renren.mobile.web.reflect.PluginWrapper;

import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * at 下午12:12, 12-5-10
 *
 * @author afpro
 */
public class EventQueue extends PluginWrapper {
    private static class Event {
        final String category;
        final String content;

        Event(String category, String content) {
            this.category = category;
            this.content = content;
        }

        @Override
        public String toString() {
            final Map<String, String> map = new TreeMap<String, String>();
            map.put("category", category);
            map.put("content", content);
            return Utils.valueToString(map);
        }
    }

    private final Queue<Event> events = new ConcurrentLinkedQueue<Event>();

    @JSMethod("clean")
    public synchronized void clear() {
        events.clear();
    }

    @JSMethod("add")
    public synchronized void add(String name, String content) {
        events.add(new Event(name, content));
    }

    @JSMethod("count")
    public synchronized int count() {
        return events.size();
    }

    @JSMethod("get")
    public synchronized Event get() {
        return events.poll();
    }
}
