package com.renren.mobile.web.branches.api_8;

import android.webkit.ConsoleMessage;
import com.renren.mobile.web.Utils;
import com.renren.mobile.web.branches.api_7.WebChromeClientWrapper7;

/**
 * at 4:40 PM, 4/11/12
 *
 * @author afpro
 */
public class WebChromeClientWrapper8 extends WebChromeClientWrapper7 {
    private final static Class[] param_onConsoleMessage = {ConsoleMessage.class};

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        try {
            return (Boolean) invoke("onConsoleMessage", param_onConsoleMessage, consoleMessage);
        } catch (NoSuchMethodException ignored) {
        }
        Utils.logJSConsoleMessage(consoleMessage.message(), consoleMessage.lineNumber(), consoleMessage.sourceId());
        return true;
    }
}
