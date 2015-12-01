package com.common.emotion.manager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.common.R;
import com.common.emotion.config.EmotionConfig;
import com.common.emotion.dao.EmotionDaoManager;
import com.common.emotion.emotion.EmotionNameList;
import com.common.emotion.emotion.EmotionPool;
import com.common.emotion.manager.IEmotionManager.OnEmotionParserCallback;
import com.common.emotion.model.EmotionBaseModel;
import com.common.emotion.model.EmotionThemeModel;
import com.common.manager.DataManager.ImageListener;
import com.common.utils.Methods;
import com.core.util.AbstractApplication;
import com.core.util.GIFDecode;
import com.core.util.CommonUtil;
/**
 * 下载图片（保存本地和存入DB）
 * @author jiaxia
 *
 */
public class EmotionDownloadBitmap implements ImageListener {

	private EmotionBaseModel model;
	public OnEmotionParserCallback cakllBack = null;
	private int key ;
	public EmotionDownloadBitmap(EmotionBaseModel model,OnEmotionParserCallback callback,int key){
		this.model = model;
		this.cakllBack = callback;
		this.key = key;
	}
	/**
	 * 获取URL
	 * @return
	 */
	public String getUrl(){
		return this.model.emotion_url;
	}
	
	@Override
	public void onGetData(final byte[] data) {
		Emotion emotion = new Emotion(this.model.emotion_name);
	    InputStream is = new ByteArrayInputStream(data);
		GIFDecode gifCode = new GIFDecode();
		gifCode.read(is);
		CommonUtil.log("jxxxxx", "gifCode count:"+gifCode.getFrameCount());
		for(int k = 0;k<gifCode.getFrameCount();k++){
			emotion.addBitmap(gifCode.getFrame(k));
		}
		emotion.mIsFlash = true;
		final String emotion_path = EmotionConfig.EMOTION_DOWN_PATH+"/"+AbstractApplication.getAppContext().getPackageName()
				                 +"/"+EmotionConfig.EMOTION_DOWN_INNER_PATH+"/"+EmotionConfig.IMG+"/"+this.model.theme_id+"/";
		CommonUtil.log("jxxxxx", "bitmap local path:"+emotion_path);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					File file = new File(emotion_path);	
					if(!file.exists())
					   file.mkdirs();
					file = new File(emotion_path+model.emotion_name+".gif");
					if(!file.exists())
					   file.createNewFile();
					model.emotion_path = emotion_path+model.emotion_name;
	              if(Methods.toFile(file, data)){  //存储正确，则存数据库
	            	  EmotionDaoManager dao  = EmotionDaoManager.getInstance();
					  dao.insertEmotion(model);
					  EmotionThemeModel themeModel = new EmotionThemeModel();
					  themeModel.package_id = EmotionConfig.COOL_PACKAGE_ID;
					  themeModel.theme_id = model.theme_id;
					  themeModel.theme_hidden = EmotionConfig.EMOTION_HIDDEN;
					  dao.insertTheme(themeModel,EmotionConfig.COOL_PACKAGE_ID);
	              }
				} catch (FileNotFoundException e) {
					CommonUtil.log("jxxxxx", "FileNotFound: "+model.emotion_path);
					e.printStackTrace();
				} catch (IOException e) {
					CommonUtil.log("jxxxxx", "IOException: "+model.emotion_path);
					e.printStackTrace();
				}
			}
		}).start();
		//加入缓存
	    EmotionNameList.getEmotionNameList(EmotionConfig.COOL_PACKAGE_ID)
	                  .add(emotion_path+this.model.emotion_name,
	                		  this.model.emotion_name, 
	                		  this.model.emotion_name, 
	                		  this.model.theme_id);
		EmotionPool.getInstance().addCoolEmotion(emotion_path+this.model.emotion_name+".gif", emotion);
		CommonUtil.log("jxxxxx", "emotion: "+emotion_path+this.model.emotion_name+".gif"+"||"+emotion.mEmotionList.size());
		//通知Pool清掉请求
		EmotionDownload.getInstance().removeRequest(key);
	}

	@Override
	public void onGetError() {
        if(this.cakllBack!=null){
        	Bitmap bt = BitmapFactory.decodeResource(AbstractApplication.getAppContext().getResources(), R.drawable.emotion_default);
 		   this.cakllBack.onEmotionError(bt);
        }
        	
	}

	@Override
	public void onGetBitmap(Bitmap bitmap) {
         //回收图片
	     if(bitmap!=null&&!bitmap.isRecycled()){
	    	 bitmap.recycle();
	     }
	}

}
