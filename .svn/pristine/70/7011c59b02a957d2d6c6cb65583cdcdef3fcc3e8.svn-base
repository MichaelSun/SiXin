package com.common.emotion.dao;

import java.util.List;
import android.content.ContentValues;

import com.common.emotion.database.EmotionPackage_Column;
import com.common.emotion.model.EmotionPackageModel;
import com.common.emotion.sql.Query_EmotionPackage;
import com.common.emotion.sql.Query_EmotionPackageList;
import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.core.util.CommonUtil;
/**
 * 表情包表的DAO
 * @author jiaxia
 *
 */
public class EmotionPackageBaseDao extends BaseDAO {

   protected Query query_package = new Query_EmotionPackage(this);
   protected Query query_packagelist = new Query_EmotionPackageList(this);
	
	public EmotionPackageBaseDao(BaseDBTable table) {
		super(table);
	}
/**
 * 插入包信息
 * @param item
 * @checked
 */
	protected void insertPackage(EmotionPackageModel item){
		if(queryByPid(item.package_id)!=null) // 唯一性处理
			return;
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(EmotionPackageModel.class, item, values);
		long num = mInsert.insert(values);
//		this.closeDataBase();
	}
	/**
	 * 查询所有的信息
	 * @return
	 * @checked
	 */
	protected List<EmotionPackageModel> queryAll(){
		
         List<EmotionPackageModel>pList = (List<EmotionPackageModel>)query_packagelist.query(null, null, null, null, null);
//         this.closeDataBase();
         return pList;
	}
	/**
	 * 查询所有的信息,按package_position进行升序输出.
	 * @return
	 * @checked
	 */
	protected List<EmotionPackageModel>queryAllOrderBy(){
	    List<EmotionPackageModel>pList = (List<EmotionPackageModel>)query_packagelist.query(null, null, null, EmotionPackage_Column.PACKAGE_POSITION, null);
	    //this.closeDataBase(); 
		return pList;
	}
	/**
	 * 通过包Id查询数据
	 * @param pid  包ID
	 * @return
	 */
	protected EmotionPackageModel queryByPid(int pid){
	    String whereString = EmotionPackage_Column.PACKAGE_ID +" = "+pid;
	    return  (EmotionPackageModel)query_package.query(null, whereString, null, null, null);
	}
}
