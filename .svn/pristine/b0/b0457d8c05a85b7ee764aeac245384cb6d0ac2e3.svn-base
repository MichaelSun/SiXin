package plugin.helpMe.database.dao;

import java.util.List;
import plugin.helpMe.database.HelpMe_Helper_Model;
import plugin.helpMe.database.HelpMe_University_Column;
import plugin.helpMe.database.HelpMe_University_Model;
import plugin.helpMe.database.sql.Query_HelpMe_University;
import plugin.helpMe.database.sql.Query_HelpMe_UniversityList;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.Delete;
import com.core.database.Insert;
import com.core.database.Query;
import com.core.database.Update;
import com.core.orm.ORMUtil;

public class HelpMeUniversityBaseDAO extends BaseDAO {

	private Insert mInsert = new Insert(this);
	private Delete mDelete = new Delete(this);
	private Update mUpdate = new Update(this);
	private Query mQuery_HelpMe_University = new Query_HelpMe_University(this);
	private Query mQuery_HelpMe_UniversityList = new Query_HelpMe_UniversityList(this);
	
	public HelpMeUniversityBaseDAO(BaseDBTable table) {
		super(table);
	}

	/**
	 * 将学校插入数据库
	 * @param model 要插入的学校model
	 * @return 成功插入的学校数目
	 * @(unchecked)
	 */
	public long insertUniversity(HelpMe_University_Model model) {
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(HelpMe_University_Model.class, model, values);
		return mInsert.insert(values);
	}
	
	/**
	 * 将学校初始化进数据库
	 * @param model 要初始化的学校model
	 * @(unchecked)
	 */
	public void initUniversity(HelpMe_University_Model model, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(HelpMe_University_Model.class, model, values);
		db.insertOrThrow(this.getTableName(), null, values);
	}
	
	/**
	 * 根据学校表的主键ID删除学校
	 * @param id 想要删除的学校表的主键ID
	 * @return 成功删除的学校数目
	 * @(unchecked)
	 */
	public long delete_University_By_Id(int id) {
		String whereString = HelpMe_University_Column._ID + " = " + id;
		long num = mDelete.delete(whereString);
		this.closeDataBase();
		return num;
	}
	
	/**
	 * 根据university_key删除学校
	 * @param university_key 想要删除的学校key
	 * @return 成功删除的学校数目
	 * @(unchecked)
	 */
	public long delete_University_By_University_Key(String university_key) {
		String whereString = HelpMe_University_Column.UNIVERSITY_KEY + " Like '%" + university_key + "%'";
		long num = mDelete.delete(whereString);
		this.closeDataBase();
		return num;
	}
	
	/**
	 * 根据学校的主键ID修改学校
	 * @param id 想要修改的学校主键ID
	 * @param model 修改后的学校内容
	 * @return 成功修改的援助关系数目
	 * @(unchecked)
	 */
	public int update_University_By_Id(int id, HelpMe_University_Model model) {
		String whereString = HelpMe_University_Column._ID + " = " + id;
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(HelpMe_University_Model.class, model, values);
		return mUpdate.update(values, whereString);
	}
	
	/**
	 * 根据学校的university_key修改学校
	 * @param university_key 想要修改的学校university_key
	 * @param model 修改后的学校内容
	 * @return 成功修改的援助关系数目
	 * @(unchecked)
	 */
	public int update_University_By_University_Key(int university_key, HelpMe_University_Model model) {
		String whereString = HelpMe_University_Column.UNIVERSITY_KEY + " Like '%" + university_key + "%'";
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(HelpMe_University_Model.class, model, values);
		return mUpdate.update(values, whereString);
	}
	
	/**
	 * 根据学校的主键ID查询学校
	 * @param id 想要查询的学校的主键ID
	 * @return 所查询学校model
	 * @(unchecked)
	 */
	public HelpMe_University_Model query_University_By_Id(int id) {
		String whereString = HelpMe_University_Column._ID + " = " +id;
		HelpMe_University_Model model = (HelpMe_University_Model)mQuery_HelpMe_University.query(null, whereString, null, null, null);
		return model;
	}
	
	/**
	 * 根据学校的university_key查询学校
	 * @param university_key 想要查询的学校的university_key
	 * @return 所查询学校model
	 * @(unchecked)
	 */
	public HelpMe_Helper_Model query_University_By_University_Key(String university_key) {
		String whereString = HelpMe_University_Column.UNIVERSITY_KEY + " Like '%" + university_key + "%'";
		HelpMe_Helper_Model model = (HelpMe_Helper_Model)mQuery_HelpMe_University.query(null, whereString, null, null, null);
		return model;
	}
	
	/**
	 * 根据学校的名字或者名字的一部分查询学校
	 * @param university_name 想要查询的学校的university_name
	 * @return 所查询学校model列表
	 * @(unchecked)
	 */
	public List<HelpMe_Helper_Model> query_University_By_University_Name(String university_name) {
		String whereString = HelpMe_University_Column.UNIVERSITY_KEY + " Like '%" + university_name + "%'";
		@SuppressWarnings("unchecked")
		List<HelpMe_Helper_Model> modelList = (List<HelpMe_Helper_Model>)mQuery_HelpMe_UniversityList.query(null, whereString, null, null, null);
		return modelList;
	}
	
}
