package com.renren.mobile.web.branches.api_7;

import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;
import android.webkit.*;
import com.renren.mobile.web.Utils;
import com.renren.mobile.web.reflect.WebChromeClientWrapper;
import com.renren.mobile.web.webkit.WebViewEx;

/**
 * at 4:33 PM, 4/11/12
 *
 * @author afpro
 */
public class WebChromeClientWrapper7 extends WebChromeClientWrapper {
    @Override
    public boolean onJsTimeout() {
        try {
            return (Boolean) invoke("onJsTimeout", null);
        } catch (NoSuchMethodException ignored) {
        }
        Utils.simpleLog("js timeout, kill it.");
        return true;
    }

    private final static Class[] param_onConsoleMessage = {String.class, int.class, String.class};

    @Override
    @SuppressWarnings("deprecated")
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        try {
            invoke("onConsoleMessage", param_onConsoleMessage, message, lineNumber, sourceID);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        Utils.logJSConsoleMessage(message, lineNumber, sourceID);
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        try {
            return (Bitmap) invoke("getDefaultVideoPoster", null);
        } catch (NoSuchMethodException ignored) {
        }
        return super.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView() {
        try {
            return (View) invoke("getVideoLoadingProgressView", null);
        } catch (NoSuchMethodException ignored) {
        }
        return super.getVideoLoadingProgressView();
    }

    private final static Class[] param_getVisitedHistory = {ValueCallback.class};

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        try {
            invoke("getVisitedHistory", param_getVisitedHistory, callback);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.getVisitedHistory(callback);
    }

    private final static Class[] param_onCloseWindow = {WebView.class};

    @Override
    public void onCloseWindow(WebView window) {
        try {
            invoke("onCloseWindow", param_onCloseWindow, window);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onCloseWindow(window);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private final static Class[] param_onCreateWindow = {WebView.class, boolean.class, boolean.class, Message.class};

    @Override
    public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
        try {
            return (Boolean) invoke("onCreateWindow", param_onCreateWindow, view, dialog, userGesture, resultMsg);
        } catch (NoSuchMethodException ignored) {
        }
        return super.onCreateWindow(view, dialog, userGesture, resultMsg);
    }

    private final static Class[] param_onExceededDatabaseQuota = {String.class, String.class, long.class, long.class, long.class, WebStorage.QuotaUpdater.class};

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
        try {
            invoke("onExceededDatabaseQuota", param_onExceededDatabaseQuota, url, databaseIdentifier, currentQuota, estimatedSize, totalUsedQuota, quotaUpdater);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota, estimatedSize, totalUsedQuota, quotaUpdater);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        try {
            invoke("onGeolocationPermissionsHidePrompt", null);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onGeolocationPermissionsHidePrompt();
    }

    private final static Class[] param_onGeolocationPermissionsShowPrompt = {String.class, GeolocationPermissions.Callback.class};

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        try {
            invoke("onGeolocationPermissionsShowPrompt", param_onGeolocationPermissionsShowPrompt, origin, callback);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onHideCustomView() {
        try {
            invoke("onHideCustomView", null);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onHideCustomView();
    }

    private final static Class[] param_onJsAlert = {WebView.class, String.class, String.class, JsResult.class};

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if (view instanceof WebViewEx) {
            try {
                if (((WebViewEx) view).onJsAlert(url, message, result)) {
                    return true;
                }
            } catch (Throwable e) {
                Utils.logStackTrace(e);
            }
        }
        try {
            return (Boolean) invoke("onJsAlert", param_onJsAlert, view, url, message, result);
        } catch (NoSuchMethodException ignored) {
        }
        return super.onJsAlert(view, url, message, result);
    }

    private final static Class[] param_onJsBeforeUnload = {WebView.class, String.class, String.class, JsResult.class};

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        try {
            return (Boolean) invoke("onJsBeforeUnload", param_onJsBeforeUnload, view, url, message, result);
        } catch (NoSuchMethodException ignored) {
        }
        return super.onJsBeforeUnload(view, url, message, result);
    }

    private final static Class[] param_onJsConfirm = param_onJsBeforeUnload;

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        if (view instanceof WebViewEx) {
            try {
                if (((WebViewEx) view).onJsConfirm(url, message, result)) {
                    return true;
                }
            } catch (Throwable e) {
                Utils.logStackTrace(e);
            }
        }
        try {
            return (Boolean) invoke("onJsConfirm", param_onJsConfirm, view, url, message, result);
        } catch (NoSuchMethodException ignored) {
        }
        return super.onJsConfirm(view, url, message, result);
    }

    private final static Class[] param_onJsPrompt = {WebView.class, String.class, String.class, String.class, JsPromptResult.class};

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (view instanceof WebViewEx) {
            try {
                if (((WebViewEx) view).onJsPrompt(url, message, defaultValue, result)) {
                    return true;
                }
            } catch (Throwable e) {
                Utils.logStackTrace(e);
            }
        }
        try {
            return (Boolean) invoke("onJsPrompt", param_onJsPrompt, view, url, message, defaultValue, result);
        } catch (NoSuchMethodException ignored) {
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    private final static Class[] param_onProgressChanged = {WebView.class, int.class};

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        try {
            invoke("onProgressChanged", param_onProgressChanged, view, newProgress);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onProgressChanged(view, newProgress);
    }

    private final static Class[] param_onReachedMaxAppCacheSize = {long.class, long.class, WebStorage.QuotaUpdater.class};

    @Override
    public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
        try {
            invoke("onReachedMaxAppCacheSize", param_onReachedMaxAppCacheSize, spaceNeeded, totalUsedQuota, quotaUpdater);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onReachedMaxAppCacheSize(spaceNeeded, totalUsedQuota, quotaUpdater);
    }


    private final static Class[] param_onReceivedIcon = {WebView.class, Bitmap.class};

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        try {
            invoke("onReceivedIcon", param_onReceivedIcon, view, icon);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onReceivedIcon(view, icon);
    }

    private final static Class[] param_onReceivedTitle = {WebView.class, String.class};

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (view instanceof WebViewEx) {
            try {
                ((WebViewEx) view).onTitle(title);
            } catch (Throwable e) {
                Utils.logStackTrace(e);
            }
        }
        try {
            invoke("onReceivedTitle", param_onReceivedTitle, view, title);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        Utils.simpleLog("Receive title: %s", title);
        super.onReceivedTitle(view, title);
    }

    private final static Class[] param_onReceivedTouchIconUrl = {WebView.class, String.class, boolean.class};

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        try {
            invoke("onReceivedTouchIconUrl", param_onReceivedTouchIconUrl, view, url, precomposed);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onReceivedTouchIconUrl(view, url, precomposed);
    }

    private final static Class[] param_onRequestFocus = {WebView.class};

    @Override
    public void onRequestFocus(WebView view) {
        try {
            invoke("onRequestFocus", param_onRequestFocus, view);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onRequestFocus(view);
    }

    private final static Class[] param_onShowCustomView = {View.class, WebChromeClient.CustomViewCallback.class};

    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        try {
            invoke("onShowCustomView", param_onShowCustomView, view, callback);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        super.onShowCustomView(view, callback);
    }
}
