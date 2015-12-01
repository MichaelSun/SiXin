package com.renren.mobile.web.branches.api_8;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import com.renren.mobile.web.branches.api_7.WebViewClientWrapper7;

/**
 * at 4:40 PM, 4/11/12
 *
 * @author afpro
 */
public class WebViewClientWrapper8 extends WebViewClientWrapper7 {
    private final static Class[] param_onReceivedSslError = {WebView.class, SslErrorHandler.class, SslError.class};

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        try {
            invoke("onReceivedSslError", param_onReceivedSslError, view, handler, error);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onReceivedSslError(view, handler, error);
    }
}
