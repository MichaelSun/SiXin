package com.renren.mobile.web;

import java.net.URI;

/**
 * at 6:47 PM, 4/11/12
 *
 * @author afpro
 */
public interface ResourceCallback {
    /**
     * 返回Response
     * @param uri 请求的URI
     * @return 如果不能处理这个请求可以返回null
     */
    WebResponse resource(URI uri);
}
