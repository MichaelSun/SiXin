package com.renren.mobile.web.branches;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import com.renren.mobile.web.branches.api_11.HardwareAccelerate;
import com.renren.mobile.web.branches.api_11.WebViewClientWrapper11;
import com.renren.mobile.web.branches.api_11.WebViewFragmentOperation;
import com.renren.mobile.web.branches.api_14.WebChromeClientWrapper14;
import com.renren.mobile.web.branches.api_14.WebViewClientWrapper14;
import com.renren.mobile.web.branches.api_7.WebChromeClientWrapper7;
import com.renren.mobile.web.branches.api_7.WebViewClientWrapper7;
import com.renren.mobile.web.branches.api_8.WebChromeClientWrapper8;
import com.renren.mobile.web.branches.api_8.WebViewClientWrapper8;
import com.renren.mobile.web.reflect.WebChromeClientWrapper;
import com.renren.mobile.web.reflect.WebViewClientWrapper;

/**
 * at 4:11 PM, 4/11/12
 *
 * @author afpro
 */
public class Branches {
    public final static int apiLevel = Build.VERSION.SDK_INT;

    public static WebChromeClientWrapper createWebChromeClientWrapper() {
        if (apiLevel >= 14) {
            return new WebChromeClientWrapper14();
        }
        if (apiLevel >= 8) {
            return new WebChromeClientWrapper8();
        }
        return new WebChromeClientWrapper7();
    }

    public static WebViewClientWrapper createWebViewClientWrapper() {
        if (apiLevel >= 14) {
            return new WebViewClientWrapper14();
        }
        if (apiLevel >= 11) {
            return new WebViewClientWrapper11();
        }
        if (apiLevel >= 8) {
            return new WebViewClientWrapper8();
        }
        return new WebViewClientWrapper7();
    }

    public static void onPauseWebView(WebView webView) {
        if (webView != null && apiLevel >= 11) {
            WebViewFragmentOperation.onPause(webView);
        }
    }

    public static void onResumeWebView(WebView webView) {
        if (webView != null && apiLevel >= 11) {
            WebViewFragmentOperation.onResume(webView);
        }
    }

    public static void enableHardwareAccelerate(Window window) {
        if (apiLevel >= 11 && window != null) {
            HardwareAccelerate.enable(window);
        }
    }

    public static void enableHardwareAccelerate(Activity activity) {
        if (apiLevel >= 11 && activity != null) {
            HardwareAccelerate.enable(activity);
        }
    }

    public static void disableHardwareAccelerate(Window window) {
        if (apiLevel >= 11 && window != null) {
            HardwareAccelerate.disable(window);
        }
    }

    public static void disableHardwareAccelerate(Activity activity) {
        if (apiLevel >= 11 && activity != null) {
            HardwareAccelerate.disable(activity);
        }
    }

    public static void disableLayer(View view) {
        if (apiLevel >= 11) {
            HardwareAccelerate.disableLayer(view);
        }
    }

    public static void enableHardwareLayer(View view) {
        if (apiLevel >= 11) {
            HardwareAccelerate.enableHardwareLayer(view);
        }
    }
}
