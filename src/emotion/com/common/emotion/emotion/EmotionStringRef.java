package com.common.emotion.emotion;

import java.util.HashMap;

import com.common.emotion.config.EmotionConfig;
/***
 * 引用
 * @author zxc
 *
 */
public class EmotionStringRef {
	private  HashMap<String,EmotionString> map;
	private static EmotionStringRef mInstance;
	private EmotionStringRef(int size) {
		map= new HashMap<String,EmotionString>(size);
	}
	/***
	 * 获取实例
	 * @return
	 */
	public static EmotionStringRef getInstacne(){
		if(mInstance == null){
			mInstance = new EmotionStringRef(10);
		}
		return mInstance;
	}
//	public EmotionString getNext(){
//		for)
//	}
	/***
	 * 添加引用
	 * @param key
	 * @param emotionString
	 */
	public void put(String key, EmotionString emotionString){
		this.map.put(key, emotionString);
		if(this.map.size() > EmotionConfig.EMOTIONREF_SZIE){
			this.map.clear();
		}
	}
	/***
	 * 获取引用
	 * @param key
	 * @return
	 */
	public EmotionString get(String key){
		return map==null?null:map.get(key);
	}
	public void remove(String key){
		this.map.remove(key);
	}
	public void clear(){
		this.map.clear();
	}
	
}

