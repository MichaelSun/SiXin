package com.renren.mobile.web.branches.api_14;

import android.view.View;
import android.webkit.WebChromeClient;
import com.renren.mobile.web.branches.api_8.WebChromeClientWrapper8;

/**
 * at 4:41 PM, 4/11/12
 *
 * @author afpro
 */
public class WebChromeClientWrapper14 extends WebChromeClientWrapper8 {
    private final static Class[] param_onShowCustomView = {View.class, int.class, WebChromeClient.CustomViewCallback.class};

    @Override
    public void onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback callback) {
        try {
            invoke("onShowCustomView", param_onShowCustomView, view, requestedOrientation, callback);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onShowCustomView(view, requestedOrientation, callback);
    }
}
