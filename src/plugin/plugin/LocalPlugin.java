package plugin;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import plugin.base.Container;
import plugin.plugins.v0.PluginV0;

/**
 * at 下午6:01, 12-7-18
 *
 * @author afpro
 */
public class LocalPlugin extends PluginV0 {

    public final Container container;
    private final PluginInfo pluginInfo;

    public LocalPlugin(Container container, final PluginInfo pluginInfo) {
        this.container = container;
        this.pluginInfo = pluginInfo;
    }

    @Override
    public void onMessageUsingIndex(long index, String content) {
    }

    @Override
    public void onMessageUsingUID(long uid, String content) {
    }

    @Override
    public PluginInfo getPluginInfo() {
        return pluginInfo;
    }

    @Override
    public void initSettingView(ViewGroup parentView) {
    }

    @Override
    public int getNotificationCount() {
        return 0;
    }

	@Override
	public String getIcon(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(Context context, Bundle bundle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean clearHistory() {
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    public boolean freeMemory() {
        return false;
    }

    @Override
    public void response(Bundle bundle) {
    }
}
