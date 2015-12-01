package plugin.database.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import plugin.database.Plugin_Column;
import plugin.database.Plugin_Model;
import plugin.database.sql.Query_Plugin;
import plugin.database.sql.Query_PluginList;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.Delete;
import com.core.database.Insert;
import com.core.database.Query;
import com.core.database.Update;
import com.core.orm.ORMUtil;

public class PluginBaseDAO extends BaseDAO {

	private Insert mInsert = new Insert(this);
	private Delete mDelete = new Delete(this);
	private Update mUpdate = new Update(this);
	private Query mQuery_Plugin = new Query_Plugin(this);
	private Query mQuery_PluginList = new Query_PluginList(this);
	private List<PluginBaseDAOObserver> mObservers = new LinkedList<PluginBaseDAOObserver>();
	private static Handler handler = new Handler();
	
	public PluginBaseDAO(BaseDBTable table) {
		super(table);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 将插件插入数据库
	 * @param model 要插入的插件model
	 * @(checked)
	 */
	private void insertPlugin(Plugin_Model model) {
		if (!(model.plugin_push == 0)) {			//插入新插件时默认接收数据推送
			model.plugin_push = 1;
		}
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(Plugin_Model.class, model, values);
		mInsert.insert(values);
	}
	
	/**
	 * 将插件List插入数据库
	 * @param model 要插入的插件List
	 * @(checked)
	 */
	public void insertPlugins(List<Plugin_Model> models) {
		if (models != null && !models.isEmpty()) {
			for (Plugin_Model model : models) {
				this.insertPlugin(model);
			}
		}
		onInsert(models);
	}
	
	/**
	 * 将插件初始化进数据库
	 * @param model 要初始化的插件model
	 * @(checked)
	 */
	private void initPlugin(Plugin_Model model, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(Plugin_Model.class, model, values);
		db.insertOrThrow(this.getTableName(), null, values);
	}
	
	/**
	 * 将插件List初始化进数据库
	 * @param model 要初始化的插件List
	 * @(checked)
	 */
	public void initPlugin(List<Plugin_Model> models, SQLiteDatabase db) {
		if (models != null && !models.isEmpty()) {
			for (Plugin_Model model : models) {
				this.initPlugin(model, db);
			}
		}
		onInsert(models);
	}
	
	
	/**
	 * 根据插件pluginId删除插件
	 * @param pluginId 想要删除的插件的pluginId
	 * @return 成功删除的插件数目
	 * @(checked)
	 */
	public long delete_Plugin_By_PluginId(int pluginId) {
		String whereString = Plugin_Column.PLUGIN_ID + " = " + pluginId;
		long num = mDelete.delete(whereString);
		this.closeDataBase();
		onDelete(pluginId);
		return num;
	}
	
	/**
	 * 
	 * 根据插件pluginId获取插件
	 * @param pluginId 想要查询的插件pluginId
	 * @return 满足条件的插件model
	 * @(checked)
	 * 
	 */
	public Plugin_Model query_Plugin_By_PluginId(int pluginId) {
		String[] whereArgs ={
				Plugin_Column.PLUGIN_ID + " = " + pluginId
		};
		Plugin_Model model = (Plugin_Model)this.query1(mQuery_Plugin, null, whereArgs, null, null, null);
		return model;
		
	}
	
	/**
	 * 
	 * 根据插件namespace获取插件
	 * @param namespace 想要查询的插件namespace
	 * @return 满足条件的插件model
	 * @(unchecked)
	 * 
	 */
	public Plugin_Model query_Plugin_By_Namespace(String namespace) {
		String[] whereArgs ={
				Plugin_Column.PLUGIN_NAMESPACE + " Like '%" + namespace + "%'"
		};
		Plugin_Model model = (Plugin_Model)this.query1(mQuery_Plugin, null, whereArgs, null, null, null);
		return model;
		
	}
	
	/**
	 * 
	 * 根据插件namespace获取插件PluginId
	 * @param namespace 想要查询的插件namespace
	 * @return 满足条件的插件PluginId
	 * @(unchecked)
	 * 
	 */
	public int query_PluginId_By_Namespace(String namespace) {
		Plugin_Model model = this.query_Plugin_By_Namespace(namespace);
		return model.plugin_id;
		
	}
	
	/**
	 * 
	 * 根据插件namespace获取插件PluginJID
	 * @param namespace 想要查询的插件namespace
	 * @return 满足条件的插件PluginJID
	 * @(unchecked)
	 * 
	 */
	public String query_JID_By_Namespace(String namespace) {
		Plugin_Model model = this.query_Plugin_By_Namespace(namespace);
		return model.plugin_jid;
		
	}
	
	/**
	 * 
	 * 根据插件pluginId获取插件namespace
	 * @param pluginId 想要查询的插件pluginId
	 * @return 满足条件的插件namespace
	 * @(unchecked)
	 * 
	 */
	public String query_PluginNamespace_By_PluginId(int pluginId) {
		Plugin_Model model = this.query_Plugin_By_PluginId(pluginId);
		return model.plugin_namespace;
		
	}
	
	/**
	 * 
	 * 根据插件plugin_codec获取插件列表
	 * @param codec 想要查询的插件plugin_codec,扩展<z/>XML节内的编码方式 0, 默认的XML格式 1, BASE64编码
	 * @return 满足条件的插件model集合
	 * @(unchecked)
	 * 
	 */
	public List<Plugin_Model> query_PluginList_By_Codec(String codec) {
		String whereString = Plugin_Column.PLUGIN_CODEC + " = " + codec;
		@SuppressWarnings("unchecked")
		List<Plugin_Model>modelList = (List<Plugin_Model>)mQuery_PluginList.query(null, whereString, null, null, null);
		return modelList;
		
	}
	
	/**
	 * 
	 * 根据插件plugin_codec获取插件PluginId列表
	 * @param codec 想要查询的插件plugin_codec,扩展<z/>XML节内的编码方式 0, 默认的XML格式 1, BASE64编码
	 * @return 满足条件的插件PluginId列表
	 * @(unchecked)
	 * 
	 */
	public List<Integer> query_PluginIdList_By_Codec(String codec) {
		List<Plugin_Model>modelList = this.query_PluginList_By_Codec(codec);
		List<Integer> idList = new ArrayList<Integer>();
		if (null != modelList) {
			for (Plugin_Model model : modelList) {
				idList.add(model.plugin_id);
			}
		}
		return idList;
		
	}
	
	/**
	 * 
	 * 根据插件的开启状态获取插件列表
	 * @param plugin_status 想要查询插件的开启状态
	 * @return 满足条件的插件model集合
	 * @(checked)
	 * 
	 */
	public List<Plugin_Model> query_PluginList_By_Status(int plugin_status) {
		String whereString = Plugin_Column.PlUGIN_STATUS + " = " + plugin_status;
		@SuppressWarnings("unchecked")
		List<Plugin_Model>modelList = (List<Plugin_Model>)mQuery_PluginList.query(null, whereString, null, null, null);
		return modelList;
		
	}
	
	/**
	 * 
	 * 根据插件的开启状态获取插件pluginId列表,0为未开启，1为开启
	 * @param plugin_status 想要查询插件的开启状态
	 * @return 满足条件的插件pluginId列表
	 * @(checked)
	 * 
	 */
	public List<Integer> query_PluginPluginIdList_By_Status(int plugin_status) {
		List<Integer> pluginIdList = new ArrayList<Integer>();
		List<Plugin_Model>modelList = this.query_PluginList_By_Status(plugin_status);
		if (null != modelList) {
			for (Plugin_Model plugin_Model : modelList) {
				pluginIdList.add(plugin_Model.plugin_id);
			}
		}
		
		return pluginIdList;
		
	}
	
	/**
	 * 
	 * 根据插件的分组获取插件列表
	 * @param plugin_group 想要查询插件的插件分组:1, 内部插件 2, 合作商插件 3, 第三方插件
	 * @return 满足条件的插件model集合
	 * @(unchecked)
	 * 
	 */
	public List<Plugin_Model> query_PluginList_By_Group(int plugin_group) {
		String whereString = Plugin_Column.PlUGIN_GROUP + " = " + plugin_group;
		@SuppressWarnings("unchecked")
		List<Plugin_Model>modelList = (List<Plugin_Model>)mQuery_PluginList.query(null, whereString, null, null, null);
		return modelList;
		
	}
	
	/**
	 * 
	 * 根据插件的分组获取插件PluginId列表
	 * @param plugin_group 想要查询插件的插件分组:1, 内部插件 2, 合作商插件 3, 第三方插件
	 * @return 满足条件的插件PluginId列表
	 * @(unchecked)
	 * 
	 */
	public List<Integer> query_PluginPluginIdList_By_Group(int plugin_group) {
		List<Integer> idList = new ArrayList<Integer>();
		List<Plugin_Model>modelList = this.query_PluginList_By_Group(plugin_group);
		if (null != modelList) {
			for (Plugin_Model plugin_Model : modelList) {
				idList.add(plugin_Model.plugin_id);
			}
		}
		
		return idList;
		
	}
	
	/**
	 * 
	 * 根据插件type获取插件列表
	 * @param plugin_type 想要查询插件的插件类型 1,本地插件 2, HTML5插件
	 * @return 满足条件的插件model集合
	 * @(unchecked)
	 * 
	 */
	public List<Plugin_Model> query_PluginList_By_Type(int plugin_type) {
		String whereString = Plugin_Column.PlUGIN_TYPE + " = " + plugin_type;
		@SuppressWarnings("unchecked")
		List<Plugin_Model>modelList = (List<Plugin_Model>)mQuery_PluginList.query(null, whereString, null, null, null);
		return modelList;
		
	}
	
	/**
	 * 
	 * 根据插件type获取插件PluginId列表
	 * @param plugin_type 想要查询插件的插件类型 1,本地插件 2, HTML5插件
	 * @return 满足条件的插件PluginId列表
	 * @(unchecked)
	 * 
	 */
	public List<Integer> query_PluginIdList_By_Type(int plugin_type) {
		List<Integer> idList = new ArrayList<Integer>();
		List<Plugin_Model>modelList = this.query_PluginList_By_Type(plugin_type);
		if (null != modelList) {
			for (Plugin_Model plugin_Model : modelList) {
				idList.add(plugin_Model.plugin_id);
			}
		}
		
		return idList;
		
	}
	
	/**
	 * 
	 * 根据插件usage获取插件列表
	 * @param plugin_usage 想要查询插件的插件用途 : 1,输入源类 2,消息通信类 3, 增强扩展类
	 * @return 满足条件的插件model集合
	 * @(unchecked)
	 * 
	 */
	public List<Plugin_Model> query_PluginList_By_Usage(int plugin_usage) {
		String whereString = Plugin_Column.PlUGIN_USAGE + " = " + plugin_usage;
		@SuppressWarnings("unchecked")
		List<Plugin_Model>modelList = (List<Plugin_Model>)mQuery_PluginList.query(null, whereString, null, null, null);
		return modelList;
		
	}
	
	/**
	 * 
	 * 根据插件usage获取插件PluginId列表
	 * @param plugin_usage 想要查询插件的插件用途 : 1,输入源类 2,消息通信类 3, 增强扩展类
	 * @return 满足条件的插件PluginId列表
	 * @(unchecked)
	 * 
	 */
	public List<Integer> query_PluginIdList_By_Usage(int plugin_usage) {
		List<Integer> idList = new ArrayList<Integer>();
		List<Plugin_Model>modelList = this.query_PluginList_By_Usage(plugin_usage);
		if (null != modelList) {
			for (Plugin_Model plugin_Model : modelList) {
				idList.add(plugin_Model.plugin_id);
			}
		}
		
		return idList;
		
	}
	
	/**
	 * 
	 * 根据插件plugin_config_push获取插件列表
	 * @param plugin_config_push 想要查询插件是否包含push配置
	 * @return 满足条件的插件model集合
	 * @(unchecked)
	 * 
	 */
	public List<Plugin_Model> query_PluginList_By_ConfigPush(int plugin_config_push) {
		String whereString = Plugin_Column.PlUGIN_CONFIG_PUSH + " = " + plugin_config_push;
		@SuppressWarnings("unchecked")
		List<Plugin_Model>modelList = (List<Plugin_Model>)mQuery_PluginList.query(null, whereString, null, null, null);
		return modelList;
		
	}
	
	/**
	 * 
	 * 根据插件plugin_config_push获取插件PluginId列表
	 * @param plugin_config_push 想要查询插件是否包含push配置
	 * @return 满足条件的插件PluginId列表
	 * @(unchecked)
	 * 
	 */
	public List<Integer> query_PluginIdList_By_ConfigPush(int plugin_config_push) {
		List<Integer> idList = new ArrayList<Integer>();
		List<Plugin_Model>modelList = this.query_PluginList_By_ConfigPush(plugin_config_push);
		if (null != modelList) {
			for (Plugin_Model plugin_Model : modelList) {
				idList.add(plugin_Model.plugin_id);
			}
		}
		
		return idList;
		
	}
	
	/**
	 * 获取全部插件列表
	 * @return 全部插件model集合
	 * @(checked)
	 */
	public List<Plugin_Model> query_All_PluginList() {
		String whereString = "";
		@SuppressWarnings("unchecked")
		List<Plugin_Model>modelList = (List<Plugin_Model>)mQuery_PluginList.query(null, whereString, null, null, null);
		return modelList;
		
	}
	
	/**
	 * 
	 * 根据插件pluginId更新插件信息
	 * @param pluginId 	想要更新的插件pluginId
	 * @param model 想要更新的插件内容
	 * @return 更新的插件数目
	 * @(checked)
	 * 
	 */
	private int update_Plugin_By_PluginId(int pluginId,Plugin_Model model) {
		String whereString = Plugin_Column.PLUGIN_ID + " = "+ pluginId;
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(Plugin_Model.class, model, values);
		int num = mUpdate.update(values, whereString);
		return num;
		
	}
	
	/**
	 * 
	 * 批量更新插件信息
	 * @param models 想要更新的插件内容List
	 * @return 被更新的插件数目
	 * @(checked)
	 * 
	 */
	public int update_Plugins(List<Plugin_Model> models) {
		int num = 0;
		if (models != null && !models.isEmpty()) {
			for (Plugin_Model model : models) {
				num += this.update_Plugin_By_PluginId(model.plugin_id, model);
			}
		}
		onUpdate(models);
		return num;
		
	}

	public void onInsert(final List<Plugin_Model> messages){
		handler.post(new Runnable() {
			public void run() {
				for(PluginBaseDAOObserver observer : mObservers){
					observer.onInsert(messages);
				}
			}
		});
		
	}
	
	public void onDelete(final int pluginId){
		handler.post(new Runnable() {
			public void run() {
				for(PluginBaseDAOObserver observer : mObservers){
					observer.onDelete(pluginId);
				}
			}
		});
		
	}
	
	public void onUpdate(final List<Plugin_Model> models){
		handler.post(new Runnable() {
			public void run() {
				for(PluginBaseDAOObserver observer : mObservers){
					observer.onUpdate(models);
				}
			}
		});
		
	}
	
	public void registorObserver(PluginBaseDAOObserver observer){
		if(!this.mObservers.contains(observer)){
			this.mObservers.add(observer);
		}
	}
	public void unregistorObserver(PluginBaseDAOObserver observer){
		this.mObservers.remove(observer);
	}
}
