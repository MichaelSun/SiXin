package plugin.plugins.v0;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import plugin.base.Plugin;

/**
 * at 上午11:18, 12-7-19
 *
 * @author afpro
 */
public abstract class PluginV0 implements Plugin, ChatClientResponse{
    @Override
    public int version() {
        return 0;
    }

    //是否显示icon，是返回icon url,否返回null
    public abstract String getIcon(String type);
    //打开插件
    public abstract void start(Context context, Bundle bundle);
    //清除历史记录
    public abstract boolean clearHistory();
    //清空插件缓存
    public abstract boolean freeMemory();
    //初始化插件个性化设置的view
    public abstract void initSettingView(ViewGroup parentView);
    //获取插件消息数
    public abstract int getNotificationCount();

    //消息被路由到插件后的回调
    public abstract void onMessageUsingIndex(long index, String content);
    public abstract void onMessageUsingUID(long uid, String content);
}
