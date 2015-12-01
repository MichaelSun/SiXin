package plugin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.renren.mobile.web.TemplateProcessor;
import com.renren.mobile.web.Utils;
import com.renren.mobile.web.reflect.Collector;
import com.renren.mobile.web.reflect.Filter;
import com.renren.mobile.web.reflect.JSMethod;
import com.renren.mobile.web.webkit.WebViewEx;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import plugin.base.Container;
import plugin.containers.v0.ContainerV0;
import plugin.database.PluginDaoFactoryImpl;
import plugin.database.Plugin_Message_Model;
import plugin.database.dao.PluginMessageDAO;
import plugin.plugins.v0.PluginV0;
import plugin.ui.Html5PluginActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * at 下午12:43, 12-7-18
 *
 * @author afpro
 */
public class Html5Plugin extends PluginV0 {

    private PluginInfo pluginInfo;
    public ContainerV0 container;
    public static String rootName = "android";
    private HashMap<String, Method> methodMap = new HashMap<String, Method>();
    private PluginMessageDAO dao = PluginDaoFactoryImpl.getInstance().buildDAO(PluginMessageDAO.class);
    private List<Plugin_Message_Model> list = new ArrayList<Plugin_Message_Model>();

    public Html5Plugin(Container container, PluginInfo pluginInfo) {
        this.pluginInfo = pluginInfo;
        this.container = (ContainerV0)container;

        Collector.collect(container.getClass(), true, new JSFilter(methodMap));
    }

    @Override
    public int version() {
        return 0;
    }

    @Override
    public String getIcon(String type) {
        return null;
    }

    @Override
    public void start(Context context, Bundle bundle) {
        list = dao.queryMessagesByPluginId(Integer.parseInt(pluginInfo.id()));
        Html5PluginActivity.show(context, pluginInfo.id(), container);
    }

    @Override
    public boolean clearHistory() {
        return false;
    }

    @Override
    public boolean freeMemory() {
        return false;
    }

    @Override
    public void initSettingView(ViewGroup parentView) {
    }

    @Override
    public int getNotificationCount() {
        return 0;
    }

    @Override
    public void onMessageUsingIndex(long index, String content) {
    }

    @Override
    public void onMessageUsingUID(long uid, String content) {
        JSONArray ja = new JSONArray();
        ja.put(content);

        WebViewEx webViewEx = Html5PluginActivity.pool.get(new Integer(getPluginInfo().id()));
        if(webViewEx == null){
            Plugin_Message_Model model = new Plugin_Message_Model();
            model.message = content;
            model.plugin_id = pluginInfo.id();
            model.namespace = pluginInfo.namespace();
            model.insert_time = String.valueOf(System.currentTimeMillis());
            dao.insertMessage(model);
            container.requestStart(pluginInfo.id(), new Bundle());
        }else {
            webViewEx.loadUrl(String.format("javascript:onMessage(%s)", ja.toString()));
        }
    }

    @Override
    public PluginInfo getPluginInfo() {
        return pluginInfo;
    }

    public CustomWebView createView(Context context){
        CustomWebView web = new CustomWebView(context);
        web.getSettings().setDefaultTextEncodingName("UTF-8");
        web.getSettings().setJavaScriptEnabled(true);
        web.setFocusable(true);
        web.requestFocus();
        web.setOnKeyListener(new BackKeyListener());

        web.addJavascriptInterface(new Object(){

            @SuppressWarnings("unused")
            public String call(String methodName, String argsStr){
                Method m = methodMap.get(methodName);
                if (m == null) {
                    Utils.simpleLog("method %s not found", methodName);
                    return null;
                }

                try {

                    JSONArray arguments = new JSONArray();
                    Object args = new JSONTokener(argsStr).nextValue();
                    if(args instanceof JSONArray){
                        arguments = (JSONArray)args;
                    }else {
                        arguments.put(args);
                    }

                    final Class[] parameterTypes = m.getParameterTypes();
                    Object ret;
                    if (parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(JSONArray.class)) {
                        ret = m.invoke(container, arguments);
                    } else {
                        if (parameterTypes.length > arguments.length()) {
                            Utils.simpleLog("not enough parameters (%d<%d)", arguments.length(), parameterTypes.length);
                            return null;
                        }
                        Object[] params = new Object[parameterTypes.length];
                        for (int i = 0; i < params.length; i++) {
                            Object object = arguments.get(i);
                            params[i] = Utils.get(object, parameterTypes[i]);
                        }

                        if (!m.isAccessible()) {
                            m.setAccessible(true);
                        }
                        ret = m.invoke(container, params);
                    }

                    if (ret != null) {
                        return ret.toString();
                    }
                } catch (Exception e) {
                    Utils.logStackTrace(e);
                }
                return null;
            }

        }, rootName);

        web.loadUrl(pluginInfo.url());
//        postData = postData == null ? "" : postData;
//        web.postUrl(pluginInfo.url(), EncodingUtils.getBytes(postData, "base64"));

        Log.v("fuck", "methodMap.size = "+methodMap.size());
        Log.v("fuck", jsInitCode());

        return web;
    }

    @Override
    public void response(Bundle bundle) {
    }

    public class BackKeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (v != null && v instanceof WebView) {
                final WebView webView = (WebView) v;
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if(webView.canGoBack()){
                        webView.goBack();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private final static TemplateProcessor jsTemplate = new TemplateProcessor("${root}.invoke=function(method,args){\n" +
            "    var args_str;\n" +
            "    switch(args.length){\n" +
            "        case 0:\n" +
            "        args_str = '[]';\n" +
            "        break;\n" +
            "        case 1:\n" +
            "        args_str = JSON.stringify([args[0]]);\n" +
            "        break;\n" +
            "        default:\n" +
            "        args_str = JSON.stringify(Array.apply(null, args));\n" +
            "        break;\n" +
            "        }\n" +
            "        console.log(args_str);" +
            "        return ${root}.call(method,args_str);\n" +
            "};\n" +
            "${#method}\n" +
            "        ${root}.${method}=function(){\n" +
            "                console.log('=======================');" +
            "                return ${root}.invoke('${method}',arguments);\n" +
            "        };\n" +
            "${/method}");

    private String jsInitCode(){
        final TemplateProcessor.ArgumentSection as = new TemplateProcessor.ArgumentSection();
        as.putArgument("root", rootName);
        for(String methodName : methodMap.keySet()){
            final TemplateProcessor.ArgumentSection mas = as.addSection("method");
            mas.putArgument("method", methodName);
        }
        return jsTemplate.build(as);
    }

    public class CustomWebView extends WebViewEx{

        public CustomWebView(Context context) {
            super(context);
        }

        @Override
        public void onFinish(String url) {
            String jsCode = jsInitCode();

            jsCode = jsCode.replace("\n"," ");
            this.loadUrl(String.format("javascript:eval(\"%s\");", jsCode));

            if(list.size()>0){
                JSONArray ja = new JSONArray();
                Iterator<Plugin_Message_Model> it = list.iterator();
                while(it.hasNext()){
                    Plugin_Message_Model model = it.next();
                    ja.put(model.message);
                }
                this.loadUrl(String.format("javascript:onMessage(%s)", ja.toString()));
                dao.deleteMessagesByPluginId(Integer.parseInt(pluginInfo.id()));
                list.clear();
            }
        }
    }



}

class JSFilter implements Filter{

    private Map<String, Method> methodMap;
    private final static Set<Class> BASE_TYPE;

    static {
        BASE_TYPE = new HashSet<Class>();
        BASE_TYPE.add(boolean.class);
        BASE_TYPE.add(Boolean.class);
        BASE_TYPE.add(byte.class);
        BASE_TYPE.add(Byte.class);
        BASE_TYPE.add(short.class);
        BASE_TYPE.add(Short.class);
        BASE_TYPE.add(int.class);
        BASE_TYPE.add(Integer.class);
        BASE_TYPE.add(long.class);
        BASE_TYPE.add(Long.class);
        BASE_TYPE.add(float.class);
        BASE_TYPE.add(Float.class);
        BASE_TYPE.add(double.class);
        BASE_TYPE.add(Double.class);
        BASE_TYPE.add(String.class);
    }

    public JSFilter(Map<String, Method> methodMap){
        this.methodMap = methodMap;
    }

    @Override
    public boolean want(Method m) {
        JSMethod jsMethod = m.getAnnotation(JSMethod.class);
        if(jsMethod == null){
            return false;
        }

        for (Class param : m.getParameterTypes()) {
            if (BASE_TYPE.contains(param) || param.isAssignableFrom(JSONObject.class) || param.isAssignableFrom(JSONArray.class)) {
                continue;
            }
            return false;
        }

        return true;
    }

    @Override
    public boolean want(Field f) {
        return false;
    }

    @Override
    public void commonMethodResult(Collection<Method> result) {
        for (Method m : result) {
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            methodMap.put(m.getAnnotation(JSMethod.class).value(), m);
        }
    }

    @Override
    public void staticMethodResult(Collection<Method> result) {
        for (Method m : result) {
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            methodMap.put(m.getAnnotation(JSMethod.class).value(), m);
        }
    }

    @Override
    public void fieldResult(Collection<Field> result) {

    }
}
