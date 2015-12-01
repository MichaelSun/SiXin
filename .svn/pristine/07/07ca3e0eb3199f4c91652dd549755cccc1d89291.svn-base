package com.common.emotion.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import com.common.emotion.config.EmotionConfig;
import com.common.emotion.dao.EmotionDaoManager;
import com.common.emotion.emotion.EmotionNameList;
import com.common.emotion.model.EmotionBaseModel;
import com.common.emotion.model.EmotionThemeModel;
import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.json.JsonParser;
import com.core.util.CommonUtil;
/**
 * Emotion解析JSON文件
 * @author jiaxia
 *
 */
public class EmotionJsonParser {

	private static EmotionJsonParser sInstance = new EmotionJsonParser();
	
	private EmotionJsonParser(){};
	
	public static EmotionJsonParser getInstance(){
		return sInstance;
	}
	/**
	 * 解析网络下载下来的表情列表
	 * @param object  :JsonObject
	 * @return
	 */
	public static List<EmotionBaseModel> jsonParserNetEmotionFactory(JsonObject object){
		List<EmotionBaseModel>tempList = new ArrayList<EmotionBaseModel>();
		String package_id = object.getString("package_id");
		JsonArray arrayObject = object.getJsonArray("emoticon_list");//?服务器名字貌似有问题
		for(int i = 0;arrayObject!=null&&i<arrayObject.size();i++){
			JsonObject item = (JsonObject)arrayObject.get(i);
			EmotionBaseModel model = new EmotionBaseModel();
			model.emotion_name = item.getString("id");
			model.emotion_code = model.emotion_name;
			model.emotion_url = item.getString("icon_url");
			
				
			model.theme_id = package_id;
			model.package_id = EmotionConfig.COOL_PACKAGE_ID;
			model.emotion_hidden = Integer.toString(EmotionConfig.EMOTION_HIDDEN);
			model.emotion_codestyle = EmotionConfig.EMOTION_CODE_COOL;
			if(model.emotion_url == null||model.emotion_url.equals(""))//数据为假的，也会有JSON，但URL没有，所以为判定条件
				continue;
			
			tempList.add(model);
			
		}
		return tempList;
	}
	/**
	 * 解析JSON文件
	 * @param is  InputStream
	 */
	public void emotionJsonParser(final InputStream is){
		String temp ;
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			while((temp = reader.readLine())!=null){
				sb.append(temp);
			}
			CommonUtil.log("xxxx", "json String:"+sb.toString());
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonObject value = (JsonObject)JsonParser.parse(sb.toString());
		jsonParser(value);
	}
	
	/**
	 * 将Json文件中的数据读出，存入DB
	 * @param file :File
	 */
	public void emotionJsonParser(final File file){
				try {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					StringBuffer sb = new StringBuffer();
					String str = null;
					while((str = reader.readLine())!=null){
						sb.append(str);
					}
					reader.close();
					JsonObject value = (JsonObject)JsonParser.parse(sb.toString());
					jsonParser(value);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	}
	/**
	 * 解析Json串
	 * @param object:JsonObject
	 */
	public void jsonParser(JsonObject object){
		if(object==null)
			return ;
		EmotionDaoManager daoManager = EmotionDaoManager.getInstance();
		long package_id = object.getNum("package_id");
		JsonArray arrayEms = object.getJsonArray("emotions");
		List<EmotionBaseModel> emotionList = null;
		List<EmotionThemeModel>themeList = null;
		if(arrayEms!=null)
		{
			emotionList = new ArrayList<EmotionBaseModel>();
			themeList = new ArrayList<EmotionThemeModel>();
		}
		else
			return ;
		/********************解析主题*********************************/
		for(int i=0;arrayEms!=null&&i<arrayEms.size();i++){
			
			
			JsonObject emItem = (JsonObject)arrayEms.get(i);
			if(emItem!=null){
				EmotionThemeModel thModel = new EmotionThemeModel();
				thModel.package_id = (int)package_id;
				
				thModel.theme_id = emItem.getString("theme_id");
				thModel.theme_position = (int)emItem.getNum("theme_position");
				String themeCheckfile = emItem.getString("theme_check_iconfile");
				String themeUnCheckFile = emItem.getString("theme_uncheck_iconfile");
				String themePath = emItem.getString("theme_path");
			    
				boolean theme_hidden = emItem.getBool("theme_hidden");
				if(theme_hidden == true)
					thModel.theme_hidden = EmotionConfig.EMOTION_HIDDEN;
				else
					thModel.theme_hidden  = EmotionConfig.EMOTION_SHOW;
				thModel.theme_check_icon_path = themePath+File.separator+EmotionConfig.IMG+File.separator+themeCheckfile;
				thModel.theme_uncheck_icon_path = themePath+File.separator+EmotionConfig.IMG+File.separator+themeUnCheckFile;
				
				JsonArray emotionsArr = emItem.getJsonArray("emotion");
				thModel.theme_path = themePath;
				CommonUtil.log("xx", thModel.package_id+"||"+thModel.theme_path);
				////////////////////////////////////
				themeList.add(thModel);
				/*********************解析表情的信息****************************/
				for(int k=0;emotionsArr!=null&&k<emotionsArr.size();k++){
					EmotionBaseModel emotion = new EmotionBaseModel();
					JsonObject  emoItem = (JsonObject)emotionsArr.get(k);
					JsonObject codeObject = emoItem.getJsonObject("code");
					if(codeObject!=null){
						emotion.emotion_name = codeObject.toJsonString();
						emotion.emotion_code = codeObject.toJsonString();
						emotion.emotion_codestyle= EmotionConfig.EMOTION_CODE_SMALL_OTHER;
					}
					String b = emoItem.getString("softbank-unicode");
					if(b!=null){
						emotion.emotion_name = b;
						emotion.emotion_code = new String(EmotionNameList.getByteFromSb(b));
						emotion.emotion_codestyle = EmotionConfig.EMOTION_CODE_EMOJI;
					}
					String file = emoItem.getString("file");
					boolean hidden = emoItem.getBool("hidden");
					
					if(hidden == true)
						emotion.emotion_hidden = String.valueOf(EmotionConfig.EMOTION_HIDDEN);
					else{
						emotion.emotion_hidden = String.valueOf(EmotionConfig.EMOTION_SHOW);
					}
					emotion.emotion_path =themePath+File.separator+EmotionConfig.IMG+File.separator+file;
					emotion.package_id = (int) package_id;
					emotion.theme_id = thModel.theme_id;
					emotionList.add(emotion);
					
				}
		    }
			
		}
		///DB   Theme
		daoManager.insertThemeList(themeList);
		///DB  Emotion
	    daoManager.insert_EmotionList(emotionList);
	 /*   test query theme
	    List<EmotionThemeModel>list = daoManager.queryByPackageId(0);
	    Log.d("xxxxxx", "size:"+list.size());
	    for(int j=0;j<list.size();j++){
	    	EmotionThemeModel m = list.get(j);
	        Log.d("xxxxxx", m.themeicon_path+"||||theme id:"+m.theme_id+"|||pid:"+m.package_id);
	    }
	    test query pid tid
	    List<EmotionBaseModel>list = daoManager.query_EmotionList_ByPidTid(0, 1);
	    Log.d("xxxxxx", "query size:"+list.size());
	    for(int j = 0;j<list.size();j++){
	    	EmotionBaseModel m = list.get(j);
	    	Log.d("xxxxxx", m.emotion_code+"  "+m.emotion_clicknum);
	    }
	    test query
	    List<EmotionBaseModel> list = daoManager.query_EmotionList_ByPidFrequency(0, 30);
	    Log.d("xxxxxxx", "lsit size:"+list.size());
	    Log.d("xxxxxxx","DB pid frequency:code:"+list.size()+"||"+ list.get(3).emotion_code);
	    test update
	    EmotionBaseModel m = emotionList.get(3);
	    EmotionBaseModel mq =new EmotionBaseModel();
	    mq.emotion_path = "emoji/img/0";
	    mq.emotion_clicknum = 100;
	    daoManager.update_Emotion(mq);
	    test update list
	    for(int j = 0;j<emotionList.size();j++){
	    	emotionList.get(j).emotion_clicknum=j+1;
	    }
	    daoManager.update_EmotionList(emotionList);
 
*/
	}
}
