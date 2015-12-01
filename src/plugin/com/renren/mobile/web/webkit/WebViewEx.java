package com.renren.mobile.web.webkit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.*;
import com.renren.mobile.web.*;
import com.renren.mobile.web.branches.Branches;
import com.renren.mobile.web.reflect.WebChromeClientWrapper;
import com.renren.mobile.web.reflect.WebViewClientWrapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * at 10:34 AM, 3/20/12
 *
 * @author afpro
 */
public class WebViewEx extends WebView implements ResourceCallback {
    private final static Executor jsAsyncExecutor = new ThreadPoolExecutor(0, 3, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public static class JSObject {
        final static AtomicLong cnt = new AtomicLong(1);
        final WebViewEx webViewEx;
        final String rootName;
        final Map<String, Plugin> pluginMap;
        final Lock lock;

        public JSObject(final WebViewEx webViewEx) {
            this.webViewEx = webViewEx;
            rootName = webViewEx.rootName;
            pluginMap = webViewEx.pluginMap;
            lock = webViewEx.pluginMapLock.readLock();
        }

        @SuppressWarnings("unused")
        public String call(String pluginName, String methodName, String argument) {
            try {
                Plugin plugin = null;
                lock.lock();
                try {
                    plugin = pluginMap.get(pluginName);
                } finally {
                    lock.unlock();
                }
                if (plugin != null) {
                    Object args = new JSONTokener(argument).nextValue();
                    webViewEx.makeCurrent();
                    if (args instanceof JSONArray) {
                        return plugin.call(methodName, (JSONArray) args);
                    } else {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(args);
                        return plugin.call(methodName, jsonArray);
                    }
                } else {
                    // log for bridge
                    Utils.simpleLog("unsupported call: plugin{%s}, method{%s}", pluginName, methodName);
                }
            } catch (JSONException e) {
                Utils.logStackTrace(e);
            } catch (ClassCastException e) {
                Utils.logStackTrace(e);
            }
            return null;
        }

        @SuppressWarnings("unused")
        public boolean asyncCall(String pluginName, final String methodName, String argument) {
            try {
                Plugin plugin = null;
                lock.lock();
                try {
                    plugin = pluginMap.get(pluginName);
                } finally {
                    lock.unlock();
                }
                if (plugin != null) {
                    Object args = new JSONTokener(argument).nextValue();
                    JSONArray jsonArray = null;
                    if (args instanceof JSONArray) {
                        jsonArray = (JSONArray) args;
                    } else {
                        jsonArray = new JSONArray();
                        jsonArray.put(args);
                    }

                    final Plugin $plugin = plugin;
                    final JSONArray $args = jsonArray;
                    jsAsyncExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                webViewEx.makeCurrent();
                                $plugin.call(methodName, $args);
                            } catch (Throwable e) {
                                Utils.logStackTrace(e);
                            }
                        }
                    });
                    return true;
                } else {
                    // log for bridge
                    Utils.simpleLog("unsupported call: plugin{%s}, method{%s}", pluginName, methodName);
                }
            } catch (JSONException e) {
                Utils.logStackTrace(e);
            } catch (ClassCastException e) {
                Utils.logStackTrace(e);
            }
            return false;
        }

        @SuppressWarnings("unused")
        public String uri(String path) {
            return webViewEx.uri(path);
        }

        @SuppressWarnings("unused")
        public String js() {
            return webViewEx.jsCode();
        }

        @SuppressWarnings("unused")
        public String uuid() {
            synchronized (JSObject.class) {
                return "uuid_0x" + Long.toHexString(cnt.incrementAndGet());
            }
        }
    }

    /* --------- Event Queue --------- */
    public final EventQueue eventQueue = new EventQueue();

    public void runJavascript(String code) {
        eventQueue.add("eval", code);
    }

    /* ------------ KVMap ------------ */
    final static SimpleWeakKVMap<String, WebViewEx> pool = new SimpleWeakKVMap<String, WebViewEx>();

    /**
     * 根据{@link #getRandomName()}所得的名字取得一个WebViewEx
     *
     * @param name {@link #getRandomName()}得到的名字
     * @return WebViewEx实例
     */
    public static WebViewEx get(String name) {
        return pool.get(name);
    }

    static void put(String name, WebViewEx webViewEx) {
        pool.put(name, webViewEx);
    }

    static void remove(String name) {
        pool.remove(name);
    }

    /* -----------unique name gen------------ */
    static long globalIndex = 0;
    final String randomName;

    String genRandomName() {
        synchronized (WebViewEx.class) {
            return getClass().getSimpleName() + "_0x" + Long.toHexString(globalIndex++);
        }
    }

    /* ----------- webview context ------------ */
    final static ThreadLocal<String> currentWebViewEx = new ThreadLocal<String>();

    public void makeCurrent() {
        if (currentWebViewEx() != this) {
            currentWebViewEx.set(randomName);
        }
    }

    public static WebViewEx currentWebViewEx() {
        return get(currentWebViewEx.get());
    }

    public static String currentWebViewExName() {
        return currentWebViewEx.get();
    }

    /**
     * @return 这个实例的名字
     */
    public String getRandomName() {
        return randomName;
    }

    /* ------------- Draw delegate -------------- */
    DrawDelegate drawDelegate = null;
    final ReadWriteLock drawDelegateLock = new ReentrantReadWriteLock();
    final Rect globalVisibleRect = new Rect();

    public DrawDelegate getDrawDelegate() {
        drawDelegateLock.readLock().lock();
        try {
            return drawDelegate;
        } finally {
            drawDelegateLock.readLock().unlock();
        }
    }

    public void setDrawDelegate(DrawDelegate drawDelegate) {
        drawDelegateLock.writeLock().lock();
        try {
            this.drawDelegate = drawDelegate;
        } finally {
            drawDelegateLock.writeLock().unlock();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        final DrawDelegate drawDelegate = getDrawDelegate();
        int[] sequence;
        if (drawDelegate == null || (sequence = drawDelegate.drawSequence()) == null) {
            super.dispatchDraw(canvas);
        } else {
            getGlobalVisibleRect(globalVisibleRect);
            for (int action : sequence) {
                switch (action) {
                    case DrawDelegate.DRAW_WEBVIEW:
                        canvas.save();
                        super.dispatchDraw(canvas);
                        canvas.restore();
                        break;
                    case DrawDelegate.DRAW_DELEGATE:
                        canvas.save();
                        drawDelegate.draw(canvas, globalVisibleRect.left, globalVisibleRect.top, globalVisibleRect.width(), globalVisibleRect.height());
                        canvas.restore();
                        break;
                }
            }
        }
    }

    /* -------------Custom Clients------------- */
    final WebChromeClientWrapper webChromeClientWrapper = Branches.createWebChromeClientWrapper();
    final WebViewClientWrapper webViewClientWrapper = Branches.createWebViewClientWrapper();

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        if (client == webChromeClientWrapper) {
            super.setWebChromeClient(client);
        } else {
            webChromeClientWrapper.setClient(client);
        }
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        if (client == webViewClientWrapper) {
            super.setWebViewClient(client);
        } else {
            webViewClientWrapper.setClient(client);
        }
    }

    void initWebViewXClients() {
        setWebViewClient(webViewClientWrapper);
        setWebChromeClient(webChromeClientWrapper);
    }

    /* ---------Touch Control---------- */
    boolean disableTouch = false;

    public boolean isDisableTouch() {
        return disableTouch;
    }

    public void setDisableTouch(boolean disableTouch) {
        this.disableTouch = disableTouch;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return disableTouch || super.dispatchTouchEvent(ev);
    }

    /* --------Plugin Map---------- */
    final Map<String, Plugin> pluginMap = new HashMap<String, Plugin>();
    final ReadWriteLock pluginMapLock = new ReentrantReadWriteLock();

    public void addPlugin(String name, Plugin plugin) {
        if (TextUtils.isEmpty(name) || plugin == null) {
            return;
        }

        pluginMapLock.writeLock().lock();
        try {
            pluginMap.put(name, plugin);
        } finally {
            pluginMapLock.writeLock().unlock();
        }
    }

    public void removePlugin(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }

        pluginMapLock.writeLock().lock();
        try {
            pluginMap.remove(name);
        } finally {
            pluginMapLock.writeLock().unlock();
        }
    }

    /* --------Resource callback------- */
    boolean interceptResource = true;
    public final static String scheme = Branches.apiLevel >= 11 ? "renren" : "http";
    private final static TemplateProcessor jsTemplate = new TemplateProcessor("" +
            "${root-external-pre};" +
            "${root}._invoke = function (plugin, method, args) {\n" +
            "    var args_str;\n" +
            "    switch (args.length) {\n" +
            "        case 0:\n" +
            "            args_str = '[]';\n" +
            "            break;\n" +
            "        case 1:\n" +
            "            args_str = JSON.stringify([args[0]]);\n" +
            "            break;\n" +
            "        default:\n" +
            "            args_str = JSON.stringify(Array.apply(null, args));\n" +
            "            break;\n" +
            "   }\n" +
            "    return ${root}.call(plugin, method, args_str);\n" +
            "};\n" +
            "\n" +
            "${#plugin}\n" +
            "    ${root}.${plugin} = {};\n" +
            "    ${#method}\n" +
            "        ${root}.${plugin}.${method} = function() {\n" +
            "            return ${root}._invoke('${plugin}', '${method}', arguments);\n" +
            "        };\n" +
            "    ${/method}\n" +
            "    ${external};\n" +
            "${/plugin}\n" +
            "${root-external-aft};\n" +
            "\n" +
            "${root}.event._cbs = {};\n" +
            "${root}.event.register = function(category, func) {\n" +
            "    if(typeof func != 'function') {\n" +
            "        console.log('add function please!');\n" +
            "    } else {\n" +
            "        ${root}.event._cbs[category] = {func:func, once:arguments.length>=3&&arguments[2]};\n" +
            "    }\n" +
            "};\n" +
            "${root}.event.unregister = function(category) {\n" +
            "    ${root}.event._cbs[category] = null;\n" +
            "};\n" +
            "${root}.event.trigger = function(category, content) {\n" +
            "    var cb = ${root}.event._cbs[category];\n" +
            "    try {\n" +
            "        if(cb == null) {\n" +
            "            console.log('unknown category ' + category);\n" +
            "        } else {\n" +
            "            cb.func(content);\n" +
            "            if(cb.once) {${root}.event._cbs[category]=null;}\n" +
            "        }\n" +
            "    } catch(err) {\n" +
            "        console.log(err);\n" +
            "    }\n" +
            "};\n" +
            "\n" +
            "${root}.event.register('eval', function(code){\n" +
            "    try {\n" +
            "        eval(code);\n" +
            "    } catch(err) {\n" +
            "        console.log(err);\n" +
            "    }\n" +
            "}, false);\n" +
            "\n" +
            "setInterval(function(){\n" +
            "    var evt_str = ${root}.event.get();\n" +
            "    if(typeof evt_str != 'undefined' && evt_str != null) {\n" +
            "        try {\n" +
            "            evt = eval('(' + evt_str + ')');\n" +
            "            ${root}.event.trigger(evt.category, evt.content);\n" +
            "        } catch(err) {\n" +
            "            console.log(err);\n" +
            "        }\n" +
            "    }\n" +
            "}, 500);");

    String rootName = null;
    String externalPreJSCode = null;
    String externalAftJSCode = null;
    boolean initialized = false;

    BaseHttpServer httpd = null;
    int port = 0;

    public String getRootName() {
        return rootName;
    }

    public String getExternalPreJSCode() {
        return externalPreJSCode;
    }

    public void setExternalPreJSCode(String externalPreJSCode) {
        this.externalPreJSCode = externalPreJSCode;
    }

    public String getExternalAftJSCode() {
        return externalAftJSCode;
    }

    public void setExternalAftJSCode(String externalAftJSCode) {
        this.externalAftJSCode = externalAftJSCode;
    }

    public synchronized void init(String rootName, boolean needResourceSupport) {
        if (initialized) {
            return;
        }

        if (needResourceSupport) {
            if (Branches.apiLevel <= 11) {
                try {
                    httpd = new BaseHttpServer(0);
                    port = httpd.getSocket().getLocalPort();
                    httpd.setResourceCallback(this);
                } catch (IOException e) {
                    throw new RuntimeException("create socket failed.", e);
                }
            }
        }

        this.rootName = rootName;
        getSettings().setJavaScriptEnabled(true);
        getSettings().setPluginState(WebSettings.PluginState.ON);
        getSettings().setSavePassword(false);
        getSettings().setSaveFormData(false);
        addJavascriptInterface(createJSObject(), rootName);
        addPlugin("event", eventQueue);
        initialized = true;
    }

    protected JSObject createJSObject() {
        return new JSObject(this);
    }

    public String jsCode() {
        final TemplateProcessor.ArgumentSection as = new TemplateProcessor.ArgumentSection();
        as.putArgument("root", rootName);

        as.putArgument("root-external-pre", externalPreJSCode);
        as.putArgument("root-external-aft", externalAftJSCode);

        pluginMapLock.readLock().lock();
        try {
            for (Map.Entry<String, Plugin> entry : pluginMap.entrySet()) {
                final String name = entry.getKey();
                final Plugin plugin = entry.getValue();
                final String external = plugin.externalJSCode();

                final TemplateProcessor.ArgumentSection pas = as.addSection("plugin");
                pas.putArgument("plugin", name);
                if (external != null) {
                    pas.putArgument("external", external);
                }

                for (String method : entry.getValue().methods()) {
                    final TemplateProcessor.ArgumentSection mas = pas.addSection("method");
                    mas.putArgument("method", method);
                }
            }
        } finally {
            pluginMapLock.readLock().unlock();
        }
        return jsTemplate.build(as);
    }

    public boolean isInterceptResource() {
        return interceptResource;
    }

    public void setInterceptResource(boolean interceptResource) {
        this.interceptResource = interceptResource;
    }

    public final String uri(String path) {
        try {
            return new URI(scheme, null, "localhost", port, path, null, null).toString();
        } catch (URISyntaxException e) {
            Utils.logStackTrace(e);
            return "";
        }
    }

    @Override
    public WebResponse resource(URI uri) {
        if (!interceptResource) {
            Utils.simpleLog("interceptResource is false");
            return null;
        }
        Utils.simpleLog("uri=%s", uri);

        if ("/android.js".equalsIgnoreCase(uri.getPath())) {
            byte[] data = jsCode().getBytes();
            return new WebResponse("text/javascript", data.length, new ByteArrayInputStream(data));
        }

        pluginMapLock.readLock().lock();
        makeCurrent();
        try {
            WebResponse response;
            for (Plugin plugin : pluginMap.values()) {
                if ((response = plugin.handleUrlRequest(uri)) != null) {
                    return response;
                }
            }
        } finally {
            pluginMapLock.readLock().unlock();
        }
        return null;
    }

    final Handler handler = new Handler();

    public WebViewEx(Context context) {
        super(context);
        CookieSyncManager.createInstance(context);
        randomName = genRandomName();
        put(randomName, this);
        initWebViewXClients();
    }

    public WebViewEx(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        CookieSyncManager.createInstance(context);
        randomName = genRandomName();
        put(randomName, this);
        initWebViewXClients();
    }

    public WebViewEx(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        CookieSyncManager.createInstance(context);
        randomName = genRandomName();
        put(randomName, this);
        initWebViewXClients();
    }

    public WebViewClient getWebViewClient() {
        return webViewClientWrapper.getClient();
    }

    public WebChromeClient getWebChromeClient() {
        return webChromeClientWrapper.getClient();
    }

    public void invokeJS(final String jsCode) {
        assert initialized : "initialize first";
        eventQueue.add("eval", jsCode);
    }

    public void pureInvokeJS(final String jsCode) {
        assert initialized : "initialize first";
        loadUrl("javascript:" + jsCode);
    }

    public void onStart(String url) {
    }

    public void onFinish(String url) {
    }

    public void onTitle(String url) {
    }

    public void onError(String url, int code, String desc) {
    }

    public boolean onJsAlert(String url, String msg, JsResult result) {
        return false;
    }

    public boolean onJsConfirm(String url, String msg, JsResult result) {
        return false;
    }

    public boolean onJsPrompt(String url, String message, String defaultValue, JsPromptResult result) {
        return false;
    }

    public boolean shouldOverride(String url) {
        return false;
    }
}

