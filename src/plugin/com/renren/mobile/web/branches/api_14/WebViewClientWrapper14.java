package com.renren.mobile.web.branches.api_14;

import android.webkit.WebView;
import com.renren.mobile.web.branches.api_11.WebViewClientWrapper11;

/**
 * at 4:41 PM, 4/11/12
 *
 * @author afpro
 */
public class WebViewClientWrapper14 extends WebViewClientWrapper11 {
    private final static Class[] param_onReceivedLoginRequest = {WebView.class, String.class, String.class, String.class};

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        try {
            invoke("onReceivedLoginRequest", param_onReceivedLoginRequest, view, realm, account, args);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onReceivedLoginRequest(view, realm, account, args);
    }
}
