package plugin.helpMe.database.dao;

import java.util.LinkedList;
import java.util.List;
import plugin.helpMe.database.HelpMe_Help_Column;
import plugin.helpMe.database.HelpMe_Help_Model;
import plugin.helpMe.database.sql.Query_HelpMe_Help;
import plugin.helpMe.database.sql.Query_HelpMe_HelpList;
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

/**
 * @author changsheng.ning
 */
public class HelpMeHelpBaseDAO extends BaseDAO {

	private Insert mInsert = new Insert(this);
	private Delete mDelete = new Delete(this);
	private Update mUpdate = new Update(this);
	private Query mQuery_HelpMe_Help = new Query_HelpMe_Help(this);
	private Query mQuery_HelpMe_HelpList = new Query_HelpMe_HelpList(this);
	private List<HelpMeHelpBaseDAOObserver> mObservers = new LinkedList<HelpMeHelpBaseDAOObserver>();
	private static Handler handler = new Handler();
	
	public HelpMeHelpBaseDAO(BaseDBTable table) {
		super(table);
	}
	
	/**
	 * 将求助帖插入数据库
	 * @param model 要插入的求助帖model
	 * @(unchecked)
	 */
	public void insertHelp(HelpMe_Help_Model model) {
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(HelpMe_Help_Model.class, model, values);
		mInsert.insert(values);
		onInsert(model);
	}
	
	/**
	 * 将求助帖初始化进数据库
	 * @param model 要初始化的求助帖model
	 * @(unchecked)
	 */
	public void initHelp(HelpMe_Help_Model model, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(HelpMe_Help_Model.class, model, values);
		db.insertOrThrow(this.getTableName(), null, values);
		onInsert(model);
	}
	
	/**
	 * 根据求助帖的主键ID删除求助帖
	 * @param id 想要删除的求助帖的ID
	 * @return 成功删除的求助帖数目
	 * @(unchecked)
	 */
	public long delete_Help_By_Id(int id) {
		String whereString = HelpMe_Help_Column._ID + " = " +id;
		long num = mDelete.delete(whereString);
		this.closeDataBase();
		onDelete();
		return num;
	}
	
	/**
	 * 根据求助人的UID删除求助帖
	 * @param host_uid 想要删除的求助帖的求助人UID
	 * @return 成功删除的求助帖数目
	 * @(unchecked)
	 */
	public long delete_Help_By_Uid(int host_uid) {
		String whereString = HelpMe_Help_Column.HOST_UID + " = " +host_uid;
		long num = mDelete.delete(whereString);
		this.closeDataBase();
		onDelete();
		return num;
	}
	
	/**
	 * 根据求助帖的主键ID修改求助帖
	 * @param id 想要修改的求助帖的主键ID
	 * @param model 修改后的求助帖内容
	 * @return 成功修改的求助帖数目
	 * @(unchecked)
	 */
	public int update_Help_By_Id(int id, HelpMe_Help_Model model) {
		String whereString = HelpMe_Help_Column._ID + " = " +id;
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(HelpMe_Help_Model.class, model, values);
		onUpdate();
		return mUpdate.update(values, whereString);
	}
	
	/**
	 * 根据求助帖的主键ID查询求助帖
	 * @param id 想要查询的求助帖的主键ID
	 * @return 所查询求助帖model
	 * @(unchecked)
	 */
	public HelpMe_Help_Model query_Help_By_Id(int id) {
		String whereString = HelpMe_Help_Column._ID + " = " +id;
		HelpMe_Help_Model model = (HelpMe_Help_Model)mQuery_HelpMe_Help.query(null, whereString, null, null, null);
		return model;
	}
	
	/**
	 * 根据求助帖的发布人UID查询求助帖
	 * @param uid 想要查询的求助人UID
	 * @return 所查询求助帖model列表
	 * @(unchecked)
	 */
	public List<HelpMe_Help_Model> query_Help_By_Uid(int uid) {
		String whereString = HelpMe_Help_Column.HOST_UID + " = " +uid;
		@SuppressWarnings("unchecked")
		List<HelpMe_Help_Model> modelList = (List<HelpMe_Help_Model>)mQuery_HelpMe_HelpList.query(null, whereString, null, null, null);
		return modelList;
	}
	
	/**
	 * 根据学校key查询求助帖
	 * @param university_key 想要查询的学校key
	 * @return 所查询求助帖model列表
	 * @(unchecked)
	 */
	public List<HelpMe_Help_Model> query_Help_By_UniversityKey(String university_key) {
		String whereString = HelpMe_Help_Column.UNIVERSITY_KEY + " Like '%" + university_key + "%'";
		@SuppressWarnings("unchecked")
		List<HelpMe_Help_Model> modelList = (List<HelpMe_Help_Model>)mQuery_HelpMe_HelpList.query(null, whereString, null, null, null);
		return modelList;
	}
	
	/**
	 * 查询所有求助帖
	 * @return 所有求助帖列表
	 * @(unchecked)
	 */
	public List<HelpMe_Help_Model> query_All_Help() {
		String whereString = "";
		@SuppressWarnings("unchecked")
		List<HelpMe_Help_Model> modelList = (List<HelpMe_Help_Model>)mQuery_HelpMe_HelpList.query(null, whereString, null, null, null);
		return modelList;
	}
	
	public void onInsert(final HelpMe_Help_Model message){
		handler.post(new Runnable() {
			public void run() {
				for(HelpMeHelpBaseDAOObserver observer : mObservers){
					observer.onInsert(message);
				}
			}
		});
		
	}
	
	public void onDelete(){
		handler.post(new Runnable() {
			public void run() {
				for(HelpMeHelpBaseDAOObserver observer : mObservers){
					observer.onDelete();
				}
			}
		});
		
	}
	
	public void onUpdate(){
		handler.post(new Runnable() {
			public void run() {
				for(HelpMeHelpBaseDAOObserver observer : mObservers){
					observer.onUpdate();
				}
			}
		});
		
	}
	
	public void registorObserver(HelpMeHelpBaseDAOObserver observer){
		if(!this.mObservers.contains(observer)){
			this.mObservers.add(observer);
		}
	}
	public void unregistorObserver(HelpMeHelpBaseDAOObserver observer){
		this.mObservers.remove(observer);
	}
}
