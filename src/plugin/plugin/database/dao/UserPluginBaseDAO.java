package plugin.database.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.core.database.*;
import com.core.orm.ORMUtil;
import plugin.database.User_Plugin_Column;
import plugin.database.User_Plugin_Model;
import plugin.database.sql.Query_User_Plugin;

public class UserPluginBaseDAO extends BaseDAO {
	
	private Insert mInsert = new Insert(this);
	private Delete mDelete = new Delete(this);
	private Update mUpdate = new Update(this);
	private Query mQuery_UserPlugin = new Query_User_Plugin(this);
	
	public UserPluginBaseDAO(BaseDBTable table) {
		super(table);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 插入用户插件订购信息
	 * @param model 要插入的用户插件订购model
	 * @(unchecked)
	 */
	public void insert_UserPlugin(User_Plugin_Model model) {
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(User_Plugin_Model.class, model, values);
		mInsert.insert(values);
	}
	
	/**
	 * 将用户插件订购信息初始化进数据库
	 * @param model 要初始化的用户插件订购信息model
	 * @(unchecked)
	 */
	public void initUserPlugin(User_Plugin_Model model, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(User_Plugin_Model.class, model, values);
		db.insertOrThrow(this.getTableName(), null, values);
	}
	
	/**
	 * 根据用户UID删除用户插件订购信息
	 * @param userId 所根据的用户UID
	 * @(unchecked)
	 */
	public void delete_UserPlugin_By_UserId(int userId) {
		String whereString = User_Plugin_Column.USER_ID + " = " + userId;
		long num = mDelete.delete(whereString);
		
//		Log.d("NCS", "delete了"+num+"个插件");
		
		this.closeDataBase();
	}

	
	/**
	 * 根据插件名称删除用户插件订购信息
	 * @param pluginName 所根据的插件名称
	 * @(unchecked)
	 */
	public void delete_UserPlugin_By_PluginName(String pluginName) {
		String whereString = User_Plugin_Column.PLUGIN_NAME + " Like '%"+pluginName+"%'";
		long num = mDelete.delete(whereString);
		
//		Log.d("NCS", "delete了"+num+"个插件");
		
		this.closeDataBase();
	}
	
	/**
	 * 根据插件名称以及用户UID删除用户插件订购信息
	 * @param pluginName 所根据的插件名称
	 * @param userId 所根据的用户UID
	 * @(unchecked)
	 */
	public void delete_UserPlugin_By_PluginName_And_UserId(String pluginName, int userId) {
		String whereString = User_Plugin_Column.PLUGIN_NAME + " Like '%"+pluginName+"%'" +"  And  "+User_Plugin_Column.USER_ID+" = "+userId;
		long num = mDelete.delete(whereString);
		
//		Log.d("NCS", "delete了"+num+"个插件");
		
		this.closeDataBase();
	}
	
	/**
	 * 根据插件名称以及用户UID更新用户插件订购信息
	 * @param pluginName 所根据的插件名称
	 * @param userId 所根据的用户UID
	 * @param model 用户订购插件的最新信息
	 * @(unchecked)
	 */
	public int update_UserPlugin_By_PluginName_And_UserId(User_Plugin_Model model, String pluginName, int userId) {
		String whereString = User_Plugin_Column.PLUGIN_NAME + " Like '%"+pluginName+"%'" +"  And  "+User_Plugin_Column.USER_ID+" = "+userId;
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormUpdate(User_Plugin_Model.class, model, values);
		return mUpdate.update(values, whereString);
	}
	
	/**
	 * 根据插件名称以及用户UID查询用户插件订购信息
	 * @param pluginName 所根据的插件名称
	 * @param userId 所根据的用户UID
	 * @(unchecked)
	 */
	public User_Plugin_Model query_UserPlugin_By_PluginName_And_UserId(String pluginName, int userId) {
		String[] whereArgs ={
				User_Plugin_Column.USER_ID + " = " + userId,
				User_Plugin_Column.PLUGIN_NAME + " like '%" + pluginName+"%'"
		};
		User_Plugin_Model model = (User_Plugin_Model)this.query1(mQuery_UserPlugin, null, whereArgs, null, null, null);
		return model;
	}
	
	
}
