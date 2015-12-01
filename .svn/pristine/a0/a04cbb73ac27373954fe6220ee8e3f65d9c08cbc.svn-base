package com.renren.mobile.web;

import org.json.JSONArray;

import java.net.URI;

/**
 * at 5:06 PM, 3/1/12
 *
 * @author afpro
 */
public interface Plugin {
    /**
     * 返回这个插件所包含的所有方法名
     *
     * @return 方法名
     */
    String[] methods();

    /**
     * 有额外的JS代码
     *
     * @return 返回额外的JS代码，如果不需要则返回{@code null;}
     */
    String externalJSCode();

    /**
     * JS方法
     *
     * @param method    方法名
     * @param arguments 参数
     * @return 返回给JS的值
     */
    String call(String method, JSONArray arguments);

    /**
     * 处理一个来自{@link android.webkit.WebView}的本地请求
     *
     * @param uri URI
     * @return 返回非null表示这个插件处理了这个请求
     */
    WebResponse handleUrlRequest(URI uri);
}