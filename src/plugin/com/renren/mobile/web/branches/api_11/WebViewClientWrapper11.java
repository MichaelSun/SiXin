package com.renren.mobile.web.branches.api_11;

import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import com.renren.mobile.web.WebResponse;
import com.renren.mobile.web.branches.api_8.WebViewClientWrapper8;
import com.renren.mobile.web.webkit.WebViewEx;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * at 6:31 PM, 4/11/12
 *
 * @author afpro
 */
public class WebViewClientWrapper11 extends WebViewClientWrapper8 {
    private final static Class[] param_shouldInterceptRequest = {WebView.class, String.class};

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (view instanceof WebViewEx) {
            try {
                final URI uri = new URI(url);
                if (!uri.isAbsolute() || WebViewEx.scheme.equalsIgnoreCase(uri.getScheme())) {
                    WebResponse response = ((WebViewEx) view).resource(uri);
                    if (response != null) {
                        return new WebResourceResponse(response.mimeType, null, response.data);
                    }
                }
            } catch (URISyntaxException ignored) {
            }
        }
        try {
            return (WebResourceResponse) invoke("shouldInterceptRequest", param_shouldInterceptRequest, view, url);
        } catch (NoSuchMethodException ignored) {
        }
        return super.shouldInterceptRequest(view, url);
    }
}
