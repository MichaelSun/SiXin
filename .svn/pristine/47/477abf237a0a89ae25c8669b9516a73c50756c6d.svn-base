package plugin.ui;

import com.renren.mobile.web.SimpleWeakKVMap;
import plugin.DBBasedPluginManager;
import plugin.Html5Plugin;
import plugin.base.Container;
import plugin.base.PluginManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.renren.mobile.web.webkit.WebViewEx;

/** 
 * 
 * @author yanfei.wu 展示Html5插件的Activity
 * @version 2012-8-16
 */

public class Html5PluginActivity extends Activity{

    public static SimpleWeakKVMap<Integer, WebViewEx> pool = new SimpleWeakKVMap<Integer, WebViewEx>();

	private PluginManager pluginManager = new DBBasedPluginManager();
    private Html5Plugin plugin;
    private WebViewEx web;
    private static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
        int pluginId = intent.getIntExtra("ID", -1);
        Bundle bundle = intent.getBundleExtra("bundle");

        plugin = (Html5Plugin)pluginManager.getPlugin(pluginId, true);
        if(plugin == null)
            return;

        FrameLayout root = new FrameLayout(context);
        web = plugin.createView(context);

        pool.put(pluginId, web);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        root.addView(web, lp);
        setContentView(root);
	}

	public static void show(Context aContext, String pluginId,Container aContainer){
		context = aContext;
        Intent intent = new Intent(context, Html5PluginActivity.class);
        intent.putExtra("ID", pluginId);
        context.startActivity(intent);
    }
}
