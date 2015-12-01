package com.renren.mobile.web.branches.api_7;

import android.graphics.Bitmap;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import com.renren.mobile.web.Utils;
import com.renren.mobile.web.reflect.WebViewClientWrapper;
import com.renren.mobile.web.webkit.WebViewEx;

/**
 * at 4:38 PM, 4/11/12
 *
 * @author afpro
 */
public class WebViewClientWrapper7 extends WebViewClientWrapper {
    private final static Class[] param_doUpdateVisitedHistory = {WebView.class, String.class, boolean.class};

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        try {
            invoke("doUpdateVisitedHistory", param_doUpdateVisitedHistory, view, url, isReload);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    private final static Class[] param_onFormResubmission = {WebView.class, Message.class, Message.class};

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        try {
            invoke("onFormResubmission", param_onFormResubmission, view, dontResend, resend);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onFormResubmission(view, dontResend, resend);
    }

    private final static Class[] param_onLoadResource = {WebView.class, String.class};

    @Override
    public void onLoadResource(WebView view, String url) {
        try {
            invoke("onLoadResource", param_onLoadResource, view, url);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onLoadResource(view, url);
    }

    private final static Class[] param_onPageFinished = param_onLoadResource;

    @Override
    public void onPageFinished(WebView view, String url) {
        if (view instanceof WebViewEx) {
            ((WebViewEx) view).eventQueue.clear();
            try {
                ((WebViewEx) view).onFinish(url);
            } catch (Throwable e) {
                Utils.logStackTrace(e);
            }
        }
        CookieSyncManager.getInstance().sync();
        try {
            invoke("onPageFinished", param_onPageFinished, view, url);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onPageFinished(view, url);
    }

    private final static Class[] param_onPageStarted = {WebView.class, String.class, Bitmap.class};

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (view instanceof WebViewEx) {
            try {
                ((WebViewEx) view).onStart(url);
            } catch (Throwable e) {
                Utils.logStackTrace(e);
            }
        }
        try {
            invoke("onPageStarted", param_onPageStarted, view, url, favicon);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onPageStarted(view, url, favicon);
    }

    private final static Class[] param_onReceivedError = {WebView.class, int.class, String.class, String.class};

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (view instanceof WebViewEx) {
            try {
                ((WebViewEx) view).onError(failingUrl, errorCode, description);
            } catch (Throwable e) {
                Utils.logStackTrace(e);
            }
        }
        try {
            invoke("onReceivedError", param_onReceivedError, view, errorCode, description, failingUrl);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        Utils.simpleLog("Got error{%d:%s} at %s", errorCode, description, failingUrl);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    private final static Class[] param_onReceivedHttpAuthRequest = {WebView.class, HttpAuthHandler.class, String.class, String.class};

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        try {
            invoke("onReceivedHttpAuthRequest", param_onReceivedHttpAuthRequest, view, handler, host, realm);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    private final static Class[] param_onScaleChanged = {WebView.class, float.class, float.class};

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        try {
            invoke("onScaleChanged", param_onScaleChanged, view, oldScale, newScale);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        Utils.simpleLog("scale: %f -> %f", oldScale, newScale);
        super.onScaleChanged(view, oldScale, newScale);
    }

    private final static Class[] param_onTooManyRedirects = param_onFormResubmission;

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        try {
            invoke("onTooManyRedirects", param_onTooManyRedirects, view, cancelMsg, continueMsg);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    private final static Class[] param_onUnhandledKeyEvent = {WebView.class, KeyEvent.class};

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        try {
            invoke("onUnhandledKeyEvent", param_onUnhandledKeyEvent, view, event);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onUnhandledKeyEvent(view, event);
    }

    private final static Class[] param_shouldOverrideKeyEvent = param_onUnhandledKeyEvent;

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        try {
            return (Boolean) invoke("shouldOverrideKeyEvent", param_shouldOverrideKeyEvent, view, event);
        } catch (NoSuchMethodException ignored) {
        }
        return super.shouldOverrideKeyEvent(view, event);
    }

    private final static Class[] param_shouldOverrideUrlLoading = param_onLoadResource;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (view instanceof WebViewEx) {
            try {
                if (((WebViewEx) view).shouldOverride(url)) {
                    return true;
                }
            } catch (Throwable e) {
                Utils.logStackTrace(e);
            }
        }
        try {
            return (Boolean) invoke("shouldOverrideUrlLoading", param_shouldOverrideUrlLoading, view, url);
        } catch (NoSuchMethodException ignored) {
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
}
