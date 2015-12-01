package com.common.emotion.dao;

import java.util.List;

import com.common.emotion.database.EmotionDaoFactoryImpl;
import com.common.emotion.model.EmotionBaseModel;
import com.common.emotion.model.EmotionPackageModel;
import com.common.emotion.model.EmotionThemeModel;

/**
 * Emotion DAO管理器
 * 表情的数据库操作均通过它来操作,上层逻辑不涉及子DAO
 * @author jiaxia
 *
 */
public class EmotionDaoManager {

	private static final EmotionDaoManager sInstance = new EmotionDaoManager();
	//表情包DAO
	public EmotionPackageBaseDao pDao = EmotionDaoFactoryImpl.getInstance().buildDAO(EmotionPackageBaseDao.class);
	//表情主题DAO
	private EmotionThemeDao tDao = EmotionDaoFactoryImpl.getInstance().buildDAO(EmotionThemeDao.class);
	//表情DAO
	private EmotionBaseDAO Dao = EmotionDaoFactoryImpl.getInstance().buildDAO(EmotionBaseDAO.class);
	
	
	private EmotionDaoManager(){
		
	}
	
	public static EmotionDaoManager getInstance(){
		return sInstance;
	}
	/**
	 * 插入表情包 数据(单条)
	 * @param item
	 */
	public synchronized void insertPackage(EmotionPackageModel item){
		if(pDao!=null){
			pDao.insertPackage(item);
		}
	}
	/**
	 * 查询表情包表中所有数据
	 * @return
	 */
	public  List<EmotionPackageModel> queryAllPackage(){
		if(pDao!=null){
			List<EmotionPackageModel> list = pDao.queryAll();
			return list;
		}
		return null;
	}
	/**
	 * 按照Package_Postion升序返回表情包表中所有数据
	 * @return
	 */
	public  List<EmotionPackageModel>queryAllPackageOrderBy(){
		if(pDao!=null){
			return pDao.queryAllOrderBy();
		}
		return null;
	}
	/**
	 * 插入主题数据(多条)
	 * @param themeList
	 */
	public synchronized void insertThemeList(List<EmotionThemeModel> themeList){
		if(tDao!=null){
			tDao.insertThemeList(themeList);
		}
	}
	/**
	 * 插入主题数据(单条)
	 * @param EmotionThemeModel
	 */
	public synchronized void insertTheme(EmotionThemeModel theme,long package_id){
		if(tDao!=null){
			tDao.insertTheme(theme,package_id);
		}
	}
	/**
	 * 根据包ID查询主题表中数据
	 * @param package_id 包ID
	 * @return
	 */
	public List<EmotionThemeModel> queryByPackageId(long package_id){
		if(tDao!=null){
			return tDao.queryByPackageId(package_id);
		}
		return null;
	}
	/**
	 * 插入表情(多条)
	 * @param emotionList EmotionBaseModel
	 */
	public synchronized void insert_EmotionList(List<EmotionBaseModel>emotionList){
		if(Dao!=null){
			Dao.insert_EmotionList(emotionList);
		}
	}
	/**
	 * 插入表情(单条)
	 * @param model EmotionBaseModel
	 */
	public synchronized void insertEmotion(EmotionBaseModel model){
		if(Dao!=null){
			Dao.insertEmotion(model);
		}
	}
	/**
	 * 根据包ID删除表情数据
	 * @param id
	 */
	public synchronized void delete_emotionPackage_ByID(int id){
		if(Dao!=null){
			Dao.delete_emotionPackage_ByID(id);
		}
	}
	/**
	 * 根据包ID和EmotionName删除表情数据
	 * @param id
	 * @param emotonName
	 */
	public synchronized void delete_emotion_ByIDAndName (int id,String emotonName){
		if(Dao!=null){
			Dao.delete_emotion_ByIDAndName(id, emotonName);
		}
	}
	/**
	 * 根据包ID和EmotionName查询表情
	 * @param id  包ID
	 * @param emotionName
	 * @return
	 */
	public EmotionBaseModel query_Emotion_ByIDAndName(int id,String emotionName){
		if(Dao!=null){
			return Dao.query_Emotion_ByIDAndName(id, emotionName);
		}
		return null;
	}
	/**
	 * 根据包ID查询表情数据(多条)
	 * @param id 包ID
	 * @return
	 */
	public List<EmotionBaseModel> query_EmotionList_ByID(int id){
		if(Dao!=null){
			return Dao.query_EmotionList_ByID(id);
		}
		return null;
	}
	/**
	 * 根据包ID和主题ID查询表情列表(多条)
	 * @param pid 包ID
	 * @param tid 主题ID
	 * @return
	 */
	public List<EmotionBaseModel> query_EmotionList_ByPidTid(int pid,int tid){
		if(Dao!=null){
			return Dao.query_EmotionList_ByPidTid(pid, tid);
		}
		return null;
	}
	/**
	 * 
	 * @param pid 包ID
	 * @param pageSize 取前多少个
	 * @return
	 */
	public List<EmotionBaseModel>query_EmotionList_ByPidFrequency(int pid,int pageSize){
		if(Dao!=null){
			return Dao.query_EmotionList_ByPidFrequency(pid, pageSize);
		}
		return null;
	}
	/**
	 * 更新表情数据(主要是更新点击次数)
	 * @param model
	 */
	public synchronized void update_Emotion(EmotionBaseModel model){
		if(Dao!=null){
			Dao.update_Emotion(model);
		}
	}
	/**
	 * 更新多行表情数据(主要是更新点击次数)
	 * @param modelList
	 */
	public synchronized void update_EmotionList(List<EmotionBaseModel>modelList){
		if(Dao!=null){
			Dao.update_EmotionList(modelList);
		}
	}
	
}
