package com.common.emotion.dao;

import java.util.List;
import android.content.ContentValues;

import com.common.emotion.config.EmotionConfig;
import com.common.emotion.database.Emotion_Column;
import com.common.emotion.model.EmotionBaseModel;
import com.common.emotion.sql.Query_Emotion;
import com.common.emotion.sql.Query_EmotionList;
import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.DAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.core.util.CommonUtil;
/**
 * 表情基本表的DAO
 * @author jiaxia
 *
 */
public class EmotionBaseDAO extends BaseDAO {

	protected Query query_emotion = new Query_Emotion(this);
	protected Query query_emotionlist = new Query_EmotionList(this);
	public EmotionBaseDAO(BaseDBTable table) {
		super(table);
	}
   
	/**
	 * 将整个表情包插入数据库
	 * @param emotionPackage
	 *  @(checked)
	 */
	protected void insert_EmotionList(List<EmotionBaseModel>emotionPackage){
		this.beginTransaction();
		for(EmotionBaseModel model : emotionPackage){
			ContentValues values = new ContentValues();
			ORMUtil.getInstance().ormInsert(EmotionBaseModel.class, model, values);
			mInsert.insert(values);
		}
		this.commit();
	}
	/**
	 * 将单个表情插入数据库
	 * @param model
	 * @(checked)
	 */
	protected void insertEmotion(EmotionBaseModel model){
		ContentValues values = new ContentValues();
		EmotionBaseModel m = query_Emotion_ByPath(model.emotion_path);
		if(m == null){   //当数据库中没有相同路径的表情单元，则插入数据库
			ORMUtil.getInstance().ormInsert(EmotionBaseModel.class, model, values);
			mInsert.insert(values);
		}
		
	}
	/**
	 * 删除表情包
	 * @param package_id
	 * @(checked)
	 */
	protected void delete_emotionPackage_ByID(int id){
		//String whereString  = Emotion_Column.EMOTION_TYPE +" Like '%"+ emotionType+"%'";
		String whereString = Emotion_Column.PACKAGE_ID +" = "+id;
		mDelete.delete(whereString);
	}
	/**
	 * 删除单个表情
	 * @param emotionType
	 * @param emotonName
	 * @(checked)
	 */
	protected void delete_emotion_ByIDAndName (int id,String emotonName){
		String whereString = Emotion_Column.PACKAGE_ID + " = "+ id +"  And  "+Emotion_Column.EMOTION_NAME+" Like '%"+emotonName+"%'";
		long num = mDelete.delete(whereString);
	}
	/**
	    * 根据包名和表情名获得一个表情
	    * @param emotionPackageid
	    * @param emotionName
	    * @return
	    * @(checked)
	    */
	protected EmotionBaseModel query_Emotion_ByIDAndName(int id,String emotionName){
			String[] whereArgs ={
					Emotion_Column.PACKAGE_ID + " = " + id,
					Emotion_Column.EMOTION_NAME + " like '%" + emotionName+"%'"
			};
			String whereString = Emotion_Column.PACKAGE_ID +" = "+id +" and "+Emotion_Column.EMOTION_NAME +" like '%"+emotionName+"%'";

			
			EmotionBaseModel model =   (EmotionBaseModel)this.query1(query_emotion, null, whereArgs, null, null, null);
			return model;
		}
	protected EmotionBaseModel query_Emotion_ByPath(String path){
		String whereString  = Emotion_Column.EMOTION_PATH +" = '"+path+"'";
		EmotionBaseModel model = (EmotionBaseModel)query_emotion.query(null, whereString, null, null, null);
	    return model;
	}
		/**
		 * 根据包名获得整个表情包内表情
		 * @param emotionType
		 * @return
		 * @(checked)
		 */
	protected List<EmotionBaseModel> query_EmotionList_ByID(int id){
			String whereString = Emotion_Column.PACKAGE_ID +"  = "+id;
			List<EmotionBaseModel>modelist =  (List<EmotionBaseModel>)query_emotionlist.query(null, whereString, null, null, null);
			return modelist;
		}
		/**
		 * 根据包ID+主题Id查询表情列表
		 * @param pid 包ID
		 * @param tid  主题ID
		 * @return
		 * @checked
		 */
	protected List<EmotionBaseModel> query_EmotionList_ByPidTid(int pid,int tid){
			String whereString = Emotion_Column.PACKAGE_ID +" = "+pid +" and "
		                         +Emotion_Column.THEME_ID +" = " +tid;
			List<EmotionBaseModel> modelList = (List<EmotionBaseModel>)query_emotionlist.query(null, whereString, null, null, null);
			return modelList;
		}
		/**
		 * 获取表情包里中表情点击次数>0的前pageSize个.
		 * @param pid 包ID
		 * @param pageSize  多少
		 * @return
		 * @checked
		 */
	protected List<EmotionBaseModel>query_EmotionList_ByPidFrequency(int pid,int pageSize){
			String whereString = Emotion_Column.PACKAGE_ID +" = "+pid +" and "
					                +Emotion_Column.EMOTION_CLICKNUM +" > 0 and "
					                  +Emotion_Column.EMOTION_HIDDEN + " = "+EmotionConfig.EMOTION_SHOW;
			
			List<EmotionBaseModel>list =  (List<EmotionBaseModel>)query_emotionlist.query(null, whereString, null, Emotion_Column.EMOTION_CLICKNUM +" "+DAO.ORDER.DESC, Integer.toString(pageSize));
			return list;
		}
		
		/**
		 * 更新数据库(单条数据)
		 * @param model:EmotionBaseModel
		 * @checked
		 */
	protected void update_Emotion(EmotionBaseModel model){
			if(model == null)
				return ;
			String whereString = Emotion_Column.EMOTION_PATH +" = '"+model.emotion_path+"'";
			EmotionBaseModel m = query_Emotion_ByPath(model.emotion_path);
			m.emotion_clicknum = model.emotion_clicknum;
			ContentValues values = new ContentValues();
			ORMUtil.getInstance().ormInsert(EmotionBaseModel.class, m, values);
			mUpdate.update(values, whereString);
		}
		/**
		 * 更新一系列的表情信息(主要是点击次数)
		 * @param modelList
		 * @checked
		 */
	protected void update_EmotionList(List<EmotionBaseModel>modelList){
			if(modelList == null)
				return ;
			this.beginTransaction();
			for(EmotionBaseModel model : modelList){
				String whereString = Emotion_Column.EMOTION_PATH +" = '"+model.emotion_path+"'";
				ContentValues values = new ContentValues();
				ORMUtil.getInstance().ormInsert(EmotionBaseModel.class, model, values);
				mUpdate.update(values, whereString);
			}
			this.commit();
		}
	
	
}
