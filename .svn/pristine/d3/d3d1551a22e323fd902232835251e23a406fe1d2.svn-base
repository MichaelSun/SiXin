package plugin;

import android.text.TextUtils;
import plugin.base.Permission;

import java.util.HashMap;
import java.util.Map;

/**
 * at 上午11:31, 12-7-18
 *
 * @author afpro
 */
public class PluginInfo {
    private final Map<String, String> infos = new HashMap<String, String>();
    private final Permission permission = new Permission();

    public final static String INFO_ID = "id";
    public final static String INFO_JID = "jid";
    public final static String INFO_NAME = "name";
    public final static String INFO_URL = "url";
    public final static String INFO_ICON = "icon";
    public final static String INFO_DESC = "desc";
    public final static String INFO_PLUGIN_CODEC = "plugin_codec";
    public final static String INFO_PLUGIN_TYPE = "plugin_type";
    public final static String INFO_PLUGIN_USAGE = "plugin_usage";
    public final static String INFO_NAMESPACE = "namespace";
    public final static String INFO_PERMISSION_STRING = "permissions";
    public final static String PERMISSION_STRING_SEP = ";";

    public void buildPermission() {
        final String permissionString = infos.get(INFO_PERMISSION_STRING);
        permission.clear();
        if (!TextUtils.isEmpty(permissionString)) {
            final String[] permissions = permissionString.split(PERMISSION_STRING_SEP);
            permission.include(permissions);
        }
    }

    public String get(String key) {
        return infos.get(key);
    }

    public String put(String key, String value) {
        String old = infos.get(key);
        infos.put(key, value);
        return old;
    }

    public String id() {
        return infos.get(INFO_ID);
    }
    
    public String jid() {
        return infos.get(INFO_JID);
    }

    public String name() {
        return infos.get(INFO_NAME);
    }

    public String url(){
        return infos.get(INFO_URL);
    }

    public String pluginType(){
        return infos.get(INFO_PLUGIN_TYPE);
    }

    public String pluginUsage(){
        return infos.get(INFO_PLUGIN_USAGE);
    }

    public String namespace() {
        return infos.get(INFO_NAMESPACE);
    }

    public String icon() {
        return infos.get(INFO_ICON);
    }

    public String desc() {
        return infos.get(INFO_DESC);
    }

    public String codec(){
        return infos.get(INFO_PLUGIN_CODEC);
    }

    public Permission permission() {
        return permission;
    }
}
