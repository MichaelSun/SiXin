package plugin.helpMe.database.dao;

import java.util.List;
import plugin.helpMe.database.HelpMe_Helper_Column;
import plugin.helpMe.database.HelpMe_Helper_Model;
import plugin.helpMe.database.sql.Query_HelpMe_Helper;
import plugin.helpMe.database.sql.Query_HelpMe_HelperList;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.Delete;
import com.core.database.Insert;
import com.core.database.Query;
import com.core.database.Update;
import com.core.orm.ORMUtil;

public class HelpMeHelperBaseDAO extends BaseDAO {

	private Insert mInsert = new Insert(this);
	private Delete mDelete = new Delete(this);
	private Update mUpdate = new Update(this);
	private Query mQuery_HelpMe_Helper = new Query_HelpMe_Helper(this);
	private Query mQuery_HelpMe_HelperList = new Query_HelpMe_HelperList(this);
	
	public HelpMeHelperBaseDAO(BaseDBTable table) {
		super(table);
	}

	/**
	 * 将援助关系插入数据库
	 * @param model 要插入的援助关系model
	 * @return 成功插入的援助关系数目
	 * @(unchecked)
	 */
	public long insertHelper(HelpMe_Helper_Model model) {
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(HelpMe_Helper_Model.class, model, values);
		return mInsert.insert(values);
	}
	
	/**
	 * 将援助关系初始化进数据库
	 * @param model 要初始化的援助关系model
	 * @(unchecked)
	 */
	public void initHelper(HelpMe_Helper_Model model, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(HelpMe_Helper_Model.class, model, values);
		db.insertOrThrow(this.getTableName(), null, values);
	}
	
	/**
	 * 根据援助关系表的主键ID删除援助关系
	 * @param id 想要删除的援助关系表的主键ID
	 * @return 成功删除的援助关系数目
	 * @(unchecked)
	 */
	public long delete_Helper_By_Id(int id) {
		String whereString = HelpMe_Helper_Column._ID + " = " + id;
		long num = mDelete.delete(whereString);
		this.closeDataBase();
		return num;
	}
	
	/**
	 * 根据用户的UID删除援助关系
	 * @param uid 想要删除的援助关系的用户的UID
	 * @return 成功删除的援助关系数目
	 * @(unchecked)
	 */
	public long delete_Helper_By_Uid(int uid) {
		String whereString = HelpMe_Helper_Column.HELPER_UID + " = " + uid;
		long num = mDelete.delete(whereString);
		this.closeDataBase();
		return num;
	}
	
	/**
	 * 根据求助帖id删除援助关系
	 * @param help_id 想要删除的援助关系的求助帖id
	 * @return 成功删除的援助关系数目
	 * @(unchecked)
	 */
	public long delete_Helper_By_Help_Id(int help_id) {
		String whereString = HelpMe_Helper_Column.HELPED_TOPIC_ID + " = " + help_id;
		long num = mDelete.delete(whereString);
		this.closeDataBase();
		return num;
	}
	
	/**
	 * 根据援助状态删除援助关系
	 * @param status 想要删除的援助关系的援助状态
	 * @return 成功删除的援助关系数目
	 * @(unchecked)
	 */
	public long delete_Helper_By_Status(int status) {
		String whereString = HelpMe_Helper_Column.HELPED_STATUS + " = " + status;
		long num = mDelete.delete(whereString);
		this.closeDataBase();
		return num;
	}
	
	/**
	 * 根据援助关系的主键ID修改援助关系
	 * @param id 想要修改的援助关系的主键ID
	 * @param model 修改后的援助关系内容
	 * @return 成功修改的援助关系数目
	 * @(unchecked)
	 */
	public int update_Helper_By_Id(int id, HelpMe_Helper_Model model) {
		String whereString = HelpMe_Helper_Column._ID + " = " + id;
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(HelpMe_Helper_Model.class, model, values);
		return mUpdate.update(values, whereString);
	}
	
	/**
	 * 根据援助关系的外键Help_Id修改援助关系
	 * @param help_id 想要修改的援助关系的外键Help_Id
	 * @param model 修改后的援助关系内容
	 * @return 成功修改的援助关系数目
	 * @(unchecked)
	 */
	public int update_Helper_By_Help_Id(int help_id, HelpMe_Helper_Model model) {
		String whereString = HelpMe_Helper_Column.HELPED_TOPIC_ID + " = " + help_id;
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(HelpMe_Helper_Model.class, model, values);
		return mUpdate.update(values, whereString);
	}
	
	/**
	 * 根据援助关系的主键ID查询援助关系
	 * @param id 想要查询的援助关系的主键ID
	 * @return 所查询援助关系model
	 * @(unchecked)
	 */
	public HelpMe_Helper_Model query_Helper_By_Id(int id) {
		String whereString = HelpMe_Helper_Column._ID + " = " +id;
		HelpMe_Helper_Model model = (HelpMe_Helper_Model)mQuery_HelpMe_Helper.query(null, whereString, null, null, null);
		return model;
	}
	
	/**
	 * 根据援助关系的外键Help_Id查询援助关系
	 * @param help_id 想要查询的援助关系的外键Help_Id
	 * @return 所查询援助关系model
	 * @(unchecked)
	 */
	public HelpMe_Helper_Model query_Helper_By_Help_Id(int help_id) {
		String whereString = HelpMe_Helper_Column.HELPED_TOPIC_ID + " = " + help_id;
		HelpMe_Helper_Model model = (HelpMe_Helper_Model)mQuery_HelpMe_Helper.query(null, whereString, null, null, null);
		return model;
	}
	
	/**
	 * 根据援助关系的援助状态查询援助关系
	 * @param status 想要查询的援助关系的援助状态(1为已援助、0为被取消援助)
	 * @return 所查询援助关系model
	 * @(unchecked)
	 */
	public List<HelpMe_Helper_Model> query_Helper_By_Status(int status) {
		String whereString = HelpMe_Helper_Column.HELPED_STATUS + " = " + status;
		@SuppressWarnings("unchecked")
		List<HelpMe_Helper_Model> modelList = (List<HelpMe_Helper_Model>)mQuery_HelpMe_HelperList.query(null, whereString, null, null, null);
		return modelList;
	}
	
	/**
	 * 根据援助关系的创建时间查询援助关系
	 * @param create_time 想要查询的援助关系的创建时间
	 * @return 所查询援助关系model
	 * @(unchecked)
	 */
	public List<HelpMe_Helper_Model> query_Helper_By_Creat_Time(String create_time) {
		String whereString = HelpMe_Helper_Column.HELPED_TOPIC_CREATE_TIME + " Like '%" + create_time + "%'";
		@SuppressWarnings("unchecked")
		List<HelpMe_Helper_Model> modelList = (List<HelpMe_Helper_Model>)mQuery_HelpMe_HelperList.query(null, whereString, null, null, null);
		return modelList;
	}
	
	/**
	 * 查询所有援助关系
	 * @return 所有援助关系model
	 * @(unchecked)
	 */
	public List<HelpMe_Helper_Model> query_All_Helper() {
		String whereString = "";
		@SuppressWarnings("unchecked")
		List<HelpMe_Helper_Model> modelList = (List<HelpMe_Helper_Model>)mQuery_HelpMe_HelperList.query(null, whereString, null, null, null);
		return modelList;
	}
	
}
