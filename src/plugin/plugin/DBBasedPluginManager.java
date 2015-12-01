package plugin;

import android.os.Handler;

import com.common.R;
import com.common.manager.LoginManager;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.utils.Methods;
import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.Base64;
import com.core.util.CommonUtil;
import plugin.base.Container;
import plugin.base.Plugin;
import plugin.base.PluginManager;
import plugin.database.PluginDaoFactoryImpl;
import plugin.database.Plugin_Column;
import plugin.database.Plugin_Model;
import plugin.database.dao.PluginBaseDAO;
import plugin.plugins.v0.PluginV0;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * at 下午12:26, 12-7-18
 *
 * @author afpro
 */
public class DBBasedPluginManager implements PluginManager {
	
	private List<PluginManagerObserver> mObservers = new LinkedList<PluginManagerObserver>();
	private static Handler handler = new Handler();
	public static boolean pluginLoadedFlag = true;
	public static final int PLUGIN_ID_ATTETION = 2;				//此处保留，因特别关注的特殊性
	
	/**
	 * 保存本地插件的类名
	 * */
	public static final int PLUGIN_ID_BRUSH = -123;
	public static final int PLUGIN_ID_HELPME = 1;
	static HashMap<Integer,String> classNameMap = new HashMap<Integer,String>();
	static {
		classNameMap.put(PLUGIN_ID_ATTETION, "com.renren.mobile.chat.ui.plugins.AttentionPlugin");
		classNameMap.put(PLUGIN_ID_BRUSH, "com.renren.mobile.chat.ui.plugins.BrushPlugin");
	}
	

	/*pluginInstanceMap<pluginId, pluginV0>存放各个插件的对象实例*/
	public static HashMap<Integer, PluginV0> pluginInstanceMap = new HashMap<Integer, PluginV0>();

    public static Container container;

    public static void init(Container container){
         DBBasedPluginManager.container = container;
    }

    @Override
    public boolean isPluginWithPluginIdInstalled(int pluginId) {
    	PluginBaseDAO dao = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
        return dao.query_PluginPluginIdList_By_Status(1).contains(pluginId);
    }

    @Override
    public void installPluginWithPluginId(final int pluginId) {

    	INetResponse response = new INetResponse() {
			@Override
			public void response(final INetRequest req, final JsonValue obj) {
//				Log.v("NCS", "response-installPlugin--" + obj.toString());
				if (obj != null && obj instanceof JsonObject) {
					int result = (int) ((JsonObject) obj).getNum("result");
					if (result == 1) {
						PluginBaseDAO dao = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
				    	Plugin_Model model = dao.query_Plugin_By_PluginId(pluginId);
				    	if (null != model) {							//判断插件是否在数据库里存在
							if (0 == model.plugin_status) {				//判断插件状态是否为未安装
								model.plugin_status = 1;
								List<Plugin_Model> models = new ArrayList<Plugin_Model>();
				    			models.add(model);
								dao.update_Plugins(models);
							}
//							onPluginInstalledOver(true);
						}
					}else{
						onPluginInstalledOver(false);
					}
					
				}
			}
		};
		McsServiceProvider.getProvider().installPlugin(response, pluginId);
		
    }
    
    @Override
    public void uninstallPluginWithPluginId(final int pluginId) {
    	
    	INetResponse response = new INetResponse() {
			@Override
			public void response(final INetRequest req, final JsonValue obj) {
//				Log.v("NCS", "response-uninstallPlugin--" + obj.toString());
				if (obj != null && obj instanceof JsonObject) {
					int result = (int) ((JsonObject) obj).getNum("result");
					if (result == 1) {
						PluginBaseDAO dao = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
				    	Plugin_Model model = dao.query_Plugin_By_PluginId(pluginId);
				    	if (null != model) {							//判断插件是否在数据库里存在
				    		if (0 != model.plugin_status) {				//判断插件状态是否为已安装
								model.plugin_status = 0;
								List<Plugin_Model> models = new ArrayList<Plugin_Model>();
				    			models.add(model);
								dao.update_Plugins(models);
							}
//							onPluginUninstalledOver(true);
						}
					}else{
						onPluginUninstalledOver(false);
					}
					
				}
			}
		};
		McsServiceProvider.getProvider().uninstallPlugin(response, pluginId);
    }
   

    @Override
    public List<Integer> getInstalledPluginIdList() {
    	
    	PluginBaseDAO dao = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
        return dao.query_PluginPluginIdList_By_Status(1);

    }

    @Override
    public List<Integer> getUnInstalledPluginIdList() {
    	PluginBaseDAO dao = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
        return dao.query_PluginPluginIdList_By_Status(0);
    }

    @Override
    public int getPluginIdWithNamespace(String namespace) {
//    	注掉部分为从数据库中取出NameSpace对应插件的PluginId
//    	PluginBaseDAO dao = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
//    	return dao.query_PluginId_By_Namespace(namespace);


    	List<Integer> installedPluginIdList = getInstalledPluginIdList();
    	Plugin tempPlugin;
    	for (int pluginId : installedPluginIdList) {
			tempPlugin = getPlugin(pluginId, false);
			if (tempPlugin.getPluginInfo().namespace().equals(namespace)) {
				return pluginId;
			}
		}
        return -1;
    }
    
	@Override
	public String getJIDWithNamespace(String namespace) {
    	PluginBaseDAO dao = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
    	return dao.query_JID_By_Namespace(namespace);

	}
    
    @Override
    public Plugin getPlugin(int pluginId, boolean createIfNotCreated) {
    	/*下述代码实现每个plugin只有一个实例对象，存放在pluginInstanceMap中*/
    	if (pluginInstanceMap.containsKey(pluginId)) {
			return pluginInstanceMap.get(pluginId);
		} else if (createIfNotCreated) {
	    	PluginBaseDAO dao = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
	    	Plugin_Model model = dao.query_Plugin_By_PluginId(pluginId);
	    	if (null != model) {
	    		PluginInfo pluginInfo = new PluginInfo();
	    		pluginInfo.put(PluginInfo.INFO_ID, String.valueOf(model.plugin_id));
		    	pluginInfo.put(PluginInfo.INFO_JID, model.plugin_jid);
		    	pluginInfo.put(PluginInfo.INFO_NAMESPACE, model.plugin_namespace);
		    	pluginInfo.put(PluginInfo.INFO_URL, model.plugin_url);
                pluginInfo.put(PluginInfo.INFO_PLUGIN_TYPE, String.valueOf(model.plugin_type));
                pluginInfo.put(PluginInfo.INFO_PLUGIN_USAGE, String.valueOf(model.plugin_usage));
                pluginInfo.put(PluginInfo.INFO_ICON, model.plugin_icon_url);
		    	pluginInfo.put(PluginInfo.INFO_NAME, model.plugin_name);
		    	pluginInfo.put(PluginInfo.INFO_DESC, model.plugin_desc);
                pluginInfo.put(PluginInfo.INFO_PLUGIN_CODEC, String.valueOf(model.plugin_codec));

                String className;
		    	if(model.plugin_type==1){
		    		className = model.plugin_class_name==null?classNameMap.get(model.plugin_id):model.plugin_class_name;
		    		 try {
			     			Class clazz = Class.forName(className);
			     			Class[] types = {Container.class, PluginInfo.class};
			     			Constructor constructor = clazz.getConstructor(types);
			     	        Object[] parameters = {container,pluginInfo};
		                     LocalPlugin plugin =  (LocalPlugin) constructor.newInstance(parameters);
		                     pluginInstanceMap.put(pluginId, plugin);
			     	       return plugin;
							
			      		} catch (ClassNotFoundException e) {
			      			e.printStackTrace();
			      		}catch (InstantiationException e) {
			      			e.printStackTrace();
			      		} catch (IllegalAccessException e) {
			      			e.printStackTrace();
			      		}catch (InvocationTargetException e) {
			 				e.printStackTrace();
			 			}catch (NoSuchMethodException e) {
			 				e.printStackTrace();
			 			}catch(Exception e ){
			 				e.printStackTrace();
			 			}
		    	}else{
		    		return new Html5Plugin(container,pluginInfo);
		    	}
	    	}
        }
        return null;
    }

    @Override
	public void settingPluginPush(final int pluginId,final Boolean flagPush) {
    	INetResponse response = new INetResponse() {
			@Override
			public void response(final INetRequest req, final JsonValue obj) {
//				Log.v("NCS", "response-settingPluginPush--" + obj.toString());
				if (obj != null && obj instanceof JsonObject) {
					int result = (int) ((JsonObject) obj).getNum("result");
					if (result == 1) {
						PluginBaseDAO dao = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
				    	Plugin_Model model = dao.query_Plugin_By_PluginId(pluginId);
				    	if (null != model) {							//判断插件是否在数据库里存在
				    		if (null != flagPush) {						//判断插件状态是否为已安装
				    			model.plugin_push=flagPush?1:0;
				    			List<Plugin_Model> models = new ArrayList<Plugin_Model>();
				    			models.add(model);
								dao.update_Plugins(models);
							}
				    		CommonUtil.toast(R.string.plugin_push_success);
							return;
						}
					} else {
						CommonUtil.toast(R.string.plugin_push_failed);
					}
				}
			}
		};
		McsServiceProvider.getProvider().settingPluginPush(response, pluginId, flagPush);
	}

	@Override
	public boolean isPluginPushOpen(int pluginId) {
		PluginBaseDAO dao = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
		Plugin_Model model = dao.query_Plugin_By_PluginId(pluginId);
        return model.plugin_push == 1;
    }
	
	/**
	 * 根据服务器下发的插件信息同步数据库中插件信息
	 * 方法中考虑了更新和新增两种情况
	 * 插件删除暂未考虑
	 */
	public  void updatePluginsFromServer() {
		INetResponse response = new INetResponse() {
			@Override
			public void response(final INetRequest req, final JsonValue obj) {
//				Log.v("NCS", "response-queryAllPluginInfos--" + obj.toString());
				
				try {
					JsonObject data = (JsonObject)obj;
					if(Methods.checkNoError(req, data)){
						if (obj != null && obj instanceof JsonObject && Methods.checkNoError(req, (JsonObject)obj)) {
							PluginBaseDAO dao = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
							List<Plugin_Model> needUpdateModels = new ArrayList<Plugin_Model>();
//							List<Plugin_Model> needInstallModels = new ArrayList<Plugin_Model>();
							
							final JsonObject map = (JsonObject) obj;
							JsonObject installedpluginInfosObject = map.getJsonObject("installed");
							int installedCount = installedpluginInfosObject != null?
									(int) installedpluginInfosObject.getNum("count"):0;
							if (installedCount > 0) {
								JsonArray installedPluginInfoArray = installedpluginInfosObject.getJsonArray("plugin_info");
								JsonObject[] objects = new JsonObject[installedPluginInfoArray.size()];
								installedPluginInfoArray.copyInto(objects);
								for (JsonObject object : objects) {
									int pluginId = (int) object.getNum("plugin_id");
									boolean isPluginInsideOfDatabase = true;
									Plugin_Model model = dao.query_Plugin_By_PluginId(pluginId);
									if (model == null) {
										isPluginInsideOfDatabase = false;
									}
									JsonObject pluginInfoJsonObject = object.getJsonObject("detail");
									model = parseModel(model, pluginInfoJsonObject);
									int push = (int) object.getNum("push");
									model.plugin_push = push;
									model.plugin_status = 1;
									if (isPluginInsideOfDatabase) {
										needUpdateModels.add(model);
//									} else {
//										needInstallModels.add(model);
									}
								}
							}
							
							JsonObject unkownpluginInfosObject = map.getJsonObject("unknown");
							int unkownCount = unkownpluginInfosObject != null?
									(int) unkownpluginInfosObject.getNum("count"):0;
							if (unkownCount > 0) {
								JsonArray unkownPluginInfoArray = unkownpluginInfosObject.getJsonArray("plugin_info");
								JsonObject[] unkownObjects = new JsonObject[unkownPluginInfoArray.size()];
								unkownPluginInfoArray.copyInto(unkownObjects);
								for (JsonObject object : unkownObjects) {
									int pluginId = (int) object.getNum("plugin_id");
									boolean isPluginInsideOfDatabase = true;
									Plugin_Model model = dao.query_Plugin_By_PluginId(pluginId);
									if (model == null) {
										isPluginInsideOfDatabase = false;
									}
									JsonObject pluginInfoJsonObject = object.getJsonObject("detail");
									model = parseModel(model, pluginInfoJsonObject);
									int push = (int) object.getNum("push");
									model.plugin_push = push;
									model.plugin_status = 0;
									if (isPluginInsideOfDatabase) {
										needUpdateModels.add(model);
//									} else {
//										needInstallModels.add(model);
									}
								}
							}
							
//							
							if (needUpdateModels!=null && !needUpdateModels.isEmpty()) {
								updateInstanceMap(needUpdateModels);
								dao.update_Plugins(needUpdateModels);
								onSuccess();
							}
//							if (needInstallModels!=null && !needInstallModels.isEmpty()) {
//								dao.insertPlugins(needInstallModels);
//							}
							if(needUpdateModels.isEmpty()){
								onSuccess();
							}
						}
					} else {
						onError(req, data);
						onFailed();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		String user_id = String.valueOf(LoginManager.getInstance().getLoginInfo().mUserId);
		int type = 3;													//获取全部插件（安装+未安装）
		int detail = 1;													//获取插件详细信息
		McsServiceProvider.getProvider()
				.queryUserPlugins(response, user_id, null, null, type, detail);
	}

	private static Plugin_Model parseModel(Plugin_Model model, JsonObject object) {

		int pluginId = (int) object.getNum(Plugin_Column.PLUGIN_ID);
		if (model == null) { 							//数据库中未包含此插件
			model = new Plugin_Model();
			model.plugin_id = pluginId;
		}
		
		if (object.getJsonValue(Plugin_Column.PlUGIN_GROUP) != null) {
			model.plugin_group = (int) object.getNum(Plugin_Column.PlUGIN_GROUP);
		}
		
		if (object.getJsonValue(Plugin_Column.PLUGIN_NAMESPACE) != null) {
			model.plugin_namespace = (String) object.getString(Plugin_Column.PLUGIN_NAMESPACE);
		}
		
		if (object.getJsonValue(Plugin_Column.PlUGIN_CONFIG_PUSH) != null) {
			model.plugin_config_push = (int) object.getNum(Plugin_Column.PlUGIN_CONFIG_PUSH);
		}
		
		if (object.getJsonValue(Plugin_Column.PLUGIN_ICON_URL) != null) {
			model.plugin_icon_url = (String) object.getString(Plugin_Column.PLUGIN_ICON_URL);
		}
		
		if (object.getJsonValue(Plugin_Column.PlUGIN_TYPE) != null) {
			model.plugin_type = (int) object.getNum(Plugin_Column.PlUGIN_TYPE);
		}
		
		if (object.getJsonValue(Plugin_Column.PLUGIN_JID) != null) {
			model.plugin_jid = (String) object.getString(Plugin_Column.PLUGIN_JID);
		}
		
		if (object.getJsonValue(Plugin_Column.PLUGIN_NAME) != null) {
			model.plugin_name = (String) object.getString(Plugin_Column.PLUGIN_NAME);
		}
		
		if (object.getJsonValue(Plugin_Column.PLUGIN_CODEC) != null) {
			model.plugin_codec = (int) object.getNum(Plugin_Column.PLUGIN_CODEC);
		}
		
		if (object.getJsonValue(Plugin_Column.PLUGIN_DESC) != null) {
			model.plugin_desc = (String) object.getString(Plugin_Column.PLUGIN_DESC);
		}
		
		if (object.getJsonValue(Plugin_Column.PlUGIN_ACCESS_APIS) != null) {
			model.plugin_access_apis = String.valueOf(object.getJsonArray(Plugin_Column.PlUGIN_ACCESS_APIS));
		}
		
		if (object.getJsonValue(Plugin_Column.PLUGIN_URL) != null) {
			model.plugin_url = (String) object.getString(Plugin_Column.PLUGIN_URL);
		}
		
		if (object.getJsonValue(Plugin_Column.PlUGIN_USAGE) != null) {
			model.plugin_usage = (int) object.getNum(Plugin_Column.PlUGIN_USAGE);
		}
		
		if (object.getJsonValue(Plugin_Column.PlUGIN_MESSAGE_TYPE) != null) {
			model.plugin_message_type = (int) object.getNum(Plugin_Column.PlUGIN_MESSAGE_TYPE);
		}

		return model;
	}
	
	
	private void updateInstanceMap(List<Plugin_Model> updateList){
		for(Plugin_Model model : updateList){
			if(pluginInstanceMap.containsKey(model.plugin_id)){
				PluginInfo pluginInfo = pluginInstanceMap.get(model.plugin_id).getPluginInfo(); 
				pluginInfo.put(PluginInfo.INFO_ID, String.valueOf(model.plugin_id));
		    	pluginInfo.put(PluginInfo.INFO_JID, model.plugin_jid);
		    	pluginInfo.put(PluginInfo.INFO_NAMESPACE, model.plugin_namespace);
		    	pluginInfo.put(PluginInfo.INFO_URL, model.plugin_url);
		        pluginInfo.put(PluginInfo.INFO_PLUGIN_TYPE, String.valueOf(model.plugin_type));
		        pluginInfo.put(PluginInfo.INFO_PLUGIN_USAGE, String.valueOf(model.plugin_usage));
		        pluginInfo.put(PluginInfo.INFO_ICON, model.plugin_icon_url);
		    	pluginInfo.put(PluginInfo.INFO_NAME, model.plugin_name);
		    	pluginInfo.put(PluginInfo.INFO_DESC, model.plugin_desc);
		        pluginInfo.put(PluginInfo.INFO_PLUGIN_CODEC, model.plugin_desc);
			}
		}
	}
	
    public void routeMessageWithNamespace(String namespace, long fromId, String message){
        int pluginId = getPluginIdWithNamespace(namespace);
        PluginV0 plugin = (PluginV0)getPlugin(pluginId, true);
        if("1".equals(plugin.getPluginInfo().codec()))
            message = new String(Base64.decode(message));
        plugin.onMessageUsingUID(fromId, message);
    }
    

    public void onPluginInstalledOver(final boolean flagResult) {
    	handler.post(new Runnable() {
			public void run() {
				for (PluginManagerObserver observer : mObservers) {
					observer.onPluginInstalledOver(flagResult);
				}
			}
    	});
	}
    
    public void onPluginUninstalledOver(final boolean flagResult) {
    	handler.post(new Runnable() {
			public void run() {
				for (PluginManagerObserver observer : mObservers) {
					observer.onPluginUninstalledOver(flagResult);
				}
			}
    	});
	}  
    
    public void onError(INetRequest req, JsonObject data){
    	int error_code = (int) data.getNum("error_code");
		String error_msg = data.getString("error_msg");
		if(error_code == 0){
			return ;
		}
		switch (error_code) {
		case -99:
			CommonUtil.toast(R.string.plugin_connect_failed);
			break;
		default:
			CommonUtil.toast(error_msg);
		}
	}
    
    public  void onFailed() {
    	handler.post(new Runnable() {
			public void run() {
				for (PluginManagerObserver observer : mObservers) {
					observer.onFailed();
				}
			}
    	});
	} 
    
    public  void onSuccess() {
    	handler.post(new Runnable() {
			public void run() {
				for (PluginManagerObserver observer : mObservers) {
					observer.onSuccess();
				}
			}
    	});
	} 
    
	public void registorObserver(PluginManagerObserver observer){
		if(!this.mObservers.contains(observer)){
			this.mObservers.add(observer);
		}
	}
	public void unregistorObserver(PluginManagerObserver observer){
		this.mObservers.remove(observer);
	}
}
