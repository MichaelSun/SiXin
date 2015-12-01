package plugin.base;

import java.util.List;

/**
 * at 下午12:59, 12-7-17
 *
 * @author afpro
 */
public interface PluginManager {
    public boolean isPluginWithPluginIdInstalled(int pluginId);
    public void installPluginWithPluginId(int pluginId);
    public void uninstallPluginWithPluginId(int pluginId);
    public List<Integer> getInstalledPluginIdList();
    public List<Integer> getUnInstalledPluginIdList();
    public int getPluginIdWithNamespace(String namespace);
    public String getJIDWithNamespace(String namespace);
    public Plugin getPlugin(int pluginId, boolean createIfNotCreated);
    public void settingPluginPush(int pluginId, Boolean flagPush);
    public boolean isPluginPushOpen(int pluginId);
}
