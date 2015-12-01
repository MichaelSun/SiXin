package com.common.emotion.emotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.util.Log;

import com.common.emotion.config.EmotionConfig;
import com.core.json.JsonObject;
import com.core.json.JsonParser;
//import com.core.util.SystemUtil;

/***
 * 数据库中取出的表情信息全部放在这里
 * 
 * @author zxc
 */
public class Node {
	public int package_id;
	int length;
	public int currentlegth;
	final static private int crease_size = 5;
	private static final String TAG = "Node";

	/***
	 * @param size
	 *            初始化时 path name code 数组的大小
	 */
	public Node(int size) {
		this.currentlegth = size;
		this.length = this.currentlegth+crease_size;
		path = new String[this.currentlegth+crease_size];
		name = new String[this.currentlegth+crease_size];
		code = new String[this.currentlegth+crease_size];
		counter = new Integer[this.currentlegth+crease_size];
		themeMap = new HashMap<String, ArrayList<Integer>>();
		templist = new ArrayList<String>(length);
		themeList = new ArrayList<String>();
	}

	public String[] path;
	public String[] name;
	public String[] code;
	public String[] chineseCode;// /刚开始不会初始化 ,getchineseCodeArray()时才会初始化
	public Integer[] counter;
	public ArrayList<String> templist;// /确保themeList是偶数个
	public ArrayList<String> themeList;
	public HashMap<String, ArrayList<Integer>> themeMap;
	private HashMap<String, Integer> compareMap;
	// public Integer] ishide ;
	int showlength;

	/***
	 * 设置主题 有此方法则必须有endSetTheme()
	 * 
	 * @param themeid
	 * @param i
	 */
	public void setTheme(String themeid, String i) {
		if (themeList != null && !themeList.contains(themeid)) {
			themeList.add(themeid);
		}
		templist.add(themeid);
		templist.add(i);
		// SystemUtil.log("xiaochao", "themeList.size " + templist.size());
	}

	/****
	 * 此方法与setTheme(...)对应的，有setTheme()则必须有endSetTheme()
	 */
	public void endSetTheme() {
		String tmpid = null;
		ArrayList<Integer> list = null;
		while (true) {
			for (Iterator<String> iter = templist.iterator(); iter.hasNext();) {
				String currentValue = iter.next();
				if (tmpid == null && !themeMap.containsKey(currentValue)) {// 当tmpid == null 并且当前themeid不在map中
					tmpid = currentValue;
					list = new ArrayList<Integer>();// /初始化List
					iter.remove();
					list.add(Integer.valueOf(iter.next()));
					iter.remove();
					continue;
				} else if (tmpid.equals(currentValue)) {
					iter.remove();
					list.add(Integer.valueOf(iter.next()));
					iter.remove();
				} else {
					iter.next();
				}
			}
			// 一次查找结束
////			SystemUtil.log("xiaochao",
//					"tmpid " + tmpid + "list size " + list.size());
			themeMap.put(tmpid, list);
			tmpid = null;

			if (templist.size() == 0) {
				break;
			}
		}
		templist.clear();
	}

	/***
	 * 根据主题获取此主题下所有表情的路径
	 * 
	 * @param themeid
	 *            主题id
	 * @return String[] 路径数组 如果没有返回空
	 */
	public String[] getEmotionPathArray(String themeid) {
		if (themeMap != null) {
			ArrayList<Integer> list = themeMap.get(themeid);
			if (list != null) {
				String[] ret = new String[list.size()];
				for (int i = 0; i < list.size(); ++i) {
					ret[i] = path[list.get(i)];
				}
				return ret;
			}
		}
		return null;
	}

	public String[] getEmotionNameArray(String themeid){
		if (themeMap != null) {
			ArrayList<Integer> list = themeMap.get(themeid);
			if (list != null) {
				String[] ret = new String[list.size()];
				for (int i = 0; i < list.size(); ++i) {
					ret[i] = name[list.get(i)];
				}
				return ret;
			}
		}
		return null;
	}
	/**
	 * add by jia.xia
	 * 
	 * @param themeid  主题ID
	 * @param emotion_style:<{@link EmotionConfig.Emotion_SMALL_STYLE}}> (小表情还是中表情)
	 * @return
	 */
	public String[] getEmotionPathArray(String themeid, int emotion_style) {
		if (themeMap != null) {
//			SystemUtil.log("getview", "Node length "+path.length);
			ArrayList<Integer> list = themeMap.get(themeid);
//			SystemUtil.log("getview", "list.size " + list.size());

			if (list != null) {
				int size;
				if (emotion_style == EmotionConfig.EMOTION_SMALL_STYLE) {
					size = EmotionConfig.SMALL_ONE_PAGE_SUM;
					int pageCount = list.size() / size;
					if (list.size() % size != 0) {
						pageCount++;
					}
					
					String[] ret = null;
					if(list.size()+pageCount>EmotionConfig.SMALL_ONE_PAGE_SUM*pageCount){
						pageCount++;
					}
					ret =  new String[ pageCount*EmotionConfig.SMALL_ONE_PAGE_SUM];
					for (int i = 0, k = 0, j = 1; i < (list.size() + pageCount); ++i) {
						if (i == (j * size - 1)) {// 到了页尾
							ret[i] = EmotionConfig.DELETE;
							j++;
						} else {
							if (k < list.size()) {
								ret[i] = path[list.get(k)];
								k++;
							}

						}

					}
					ret[ret.length - 1] = EmotionConfig.DELETE;
					return ret;
				} else if (emotion_style == EmotionConfig.EMOTION_COOL_STYLE) {
					String[] ret = new String[list.size()];
					for (int i = 0; i < list.size(); ++i) {
						ret[i] = path[list.get(i)];
					}
					return ret;
				}

			}
		}
		return null;
	}

	/**
	 * add by jia.xia
	 * 
	 *  @param themeid  主题ID
	 *  @param emotion_style:<{@link EmotionConfig.Emotion_SMALL_STYLE}}> (小表情还是中表情)
	 *            每页表情的个数
	 * @return
	 */
	public String[] getEmotionCodeArray(String themeid, int emotion_style) {
		if (themeMap != null) {

			ArrayList<Integer> list = themeMap.get(themeid);
			if (list != null) {
				int size;
				if (emotion_style == EmotionConfig.EMOTION_SMALL_STYLE) {
					size = EmotionConfig.SMALL_ONE_PAGE_SUM;
					int pageCount = list.size() / size;
					if (list.size() % size != 0) {
						pageCount++;
					}
					String[] ret =null;
					///////容错处理,处理超出页面数量的情况/////////////
					if(list.size()+pageCount>EmotionConfig.SMALL_ONE_PAGE_SUM*pageCount){
						pageCount++;
					}
					ret =  new String[ pageCount*EmotionConfig.SMALL_ONE_PAGE_SUM];
					for (int i = 0, k = 0, j = 1; i < (list.size() + pageCount); ++i) {
						if (i == (j * size - 1)) {// 到了页尾
							ret[i] = EmotionConfig.DELETE;
							j++;
						} else {
							if (k < list.size()) {
								ret[i] = code[list.get(k)];
								k++;
							}

						}

					}
					ret[ret.length - 1] = EmotionConfig.DELETE;
					return ret;
				} else if (emotion_style == EmotionConfig.EMOTION_COOL_STYLE) {
					String[] ret = new String[list.size()];
					for (int i = 0; i < list.size(); ++i) {
						ret[i] = code[list.get(i)];
					}
					return ret;
				}

			}
		}
		return null;
	}

	/***
	 * 根据主题获取此主题下所有表情的转义符
	 * 
	 * @param themeid
	 *            主题ID
	 * @return String[] 编码数组， 如果没有返回空
	 */
	public String[] getEmotionCodeArray(String themeid) {
		if (themeMap != null) {
			ArrayList<Integer> list = themeMap.get(themeid);
			if (list != null) {
				String[] ret = new String[list.size()];
				for (int i = 0; i < list.size(); ++i) {
					ret[i] = code[list.get(i)];
				}
				return ret;
			}
		}
		return null;
	}
	public String[] getEmotionChineseCodeArray(String themeid){
		if(themeMap!=null){
			ArrayList<Integer> list = themeMap.get(themeid);
			if(list != null){
				String[] ret = new String[list.size()];
				for(int i = 0; i< list.size();++i){
					ret[i] = this.getchineseCode(list.get(i));
				}
				return ret;
			}
		}
		return null;
	}

	/***
	 * 根据路径 获取编码
	 * 
	 * @param pathstr
	 *            路径
	 * @return 没有则会得到空，亲 ，记得判空啊
	 */
	public String getCode(String pathstr) {
		for (int i = 0; i < currentlegth; ++i) {
			if (path[i] == pathstr) {
				return code[i];
			}
		}
		return null;
	}

	/**
	 * 根据编码获取路径
	 * 
	 * @param code
	 *            转义符，大表情的内容就是转义符
	 * @return 没有则会返回空，记得判空撒，亲
	 */
	public String getPath(String code) {
		for (int i = 0; i < currentlegth; ++i) {
//			SystemUtil.log("xcc", "code " + code + "  i" + i);
			if (this.getchineseCode(i).equals(code)) {
//				SystemUtil.log("zxc", "return data  " + path[i]);
				return path[i];
			}
		}
		return null;
	}
	public String getPath(String code , String themeId){
//		String[] tmpCode = this.getEmotionCodeArray(themeId);
//		for(int i =0; i< tmpCode.length;++i){
//			if(code.equals(changeToChineseCode(tmpCode[i]))){
//				return this.getEmotionPathArray(themeId)[i];
//			}
//		}
//		return null;
		ArrayList<Integer> list = themeMap.get(themeId);
		if(list!= null){
			for(int i =0; i< list.size();++i){
				if(this.getchineseCode(list.get(i)).equals(code)){
					return path[list.get(i)];
				}
			}
		}
		return null;
	}
	public static String changeToChineseCode(String code){
		JsonObject o = null;
		try {
			o = (JsonObject) JsonParser.parse(code);
		} catch (ClassCastException e) {
			return code;
		}
		if (o == null) {
			return code;
		}
		return o.getString("ch");
	}
	
	/***
	 * 获取中文编码，
	 * 
	 * @param i
	 *            location index
	 * @return 如果没有区分，则返回全部
	 */
	public String getchineseCode(int i) {
		JsonObject o = null;
		try {
			o = (JsonObject) JsonParser.parse(code[i]);
		} catch (ClassCastException e) {
			return code[i];
		}
		if (o == null) {
			return code[i];
		}
		return o.getString("ch");
	}

	/***
	 * 获取此Package_id下的中文转义符
	 * 
	 * @return String[]
	 */
	public String[] getchineseCodeArray() {
		if (chineseCode == null) {
			chineseCode = new String[this.currentlegth];
			for (int i = 0; i < this.currentlegth; ++i) {
				chineseCode[i] = this.getchineseCode(i);
			}
		}
		return chineseCode;
	}

	/****
	 * 获取中文name
	 * 
	 * @param i
	 *            location index
	 * @return 如果没有区分中英文 ，则返回全部
	 */
	public String getchinestName(int i) {
		JsonObject o = null;
		try {
			o = (JsonObject) JsonParser.parse(name[i]);
		} catch (ClassCastException e) {
			return name[i];
		}
		if (o == null) {
			return name[i];
		}
		return o.getString("ch");
	}

	/****
	 * 获取英文name
	 * 
	 * @param i
	 *            location index
	 * @return 如果没有区分中英文 ，则返回全部
	 */
	public String getEnglishName(int i) {
		JsonObject o = null;
		try {
			o = (JsonObject) JsonParser.parse(name[i]);
		} catch (ClassCastException e) {
			return name[i];
		}
		if (o == null) {
			return name[i];
		}
		return o.getString("en");
	}

	/***
	 * 获取此路径哈希值的 索引
	 * 
	 * @param key
	 *            路径的哈希值
	 * @return 返回此表情的index
	 */
	public int getIndex(int key) {
		for (int i = 0; i < this.currentlegth; ++i) {
			if (key == path[i].hashCode()) {
				return i;
			}
		}
		return -1;
	}

	/****
	 * 查找表情编码
	 * 
	 * @param str
	 * @return if null then return -1 or return index
	 */
	public int isContain(String str) {
		if (compareMap == null) {
			compareMap = new HashMap<String, Integer>();
		}
		if (compareMap.size() == 0) {
			for (int i = 0; i < this.currentlegth; ++i) {
				compareMap.put(this.getchineseCode(i), i);
			}
		}
		if (compareMap.containsKey(str)) {
			return compareMap.get(str);
		} else {
//			SystemUtil.log("xiaow", "get null from map " + str);
		}
		return -1;
	}
//	public int getPackageIdBy(int themeId){
//		Set set = themeMap.keySet();
//		for(Iterator<Integer> iter = set.iterator(); iter.hasNext();){
//			int pid = iter.next();
//			if(themeMap.get(pid).contains(themeId)){
//				return  pid;
//			}
//		}
//		return -1;
//	}
	/**
	 * 清空冗余数据
	 */
	public void clearCompareMap() {
		this.compareMap.clear();
	}

	private long getlong(String str) {
		int index = 0;
		long code = 0;
		while (index < str.length()) {
			code = code * 10 + str.charAt(index++);
		}
		return code;
	}
	// private int getHashCode(String str){
	// int hashcode = 0;
	// int seed = 13;
	// for(int i = 0; i<str.length();++i){
	// hashcode = hashcode >> seed+str.charAt(i);
	// }
	// return hashcode & 0X7FFFFFFF;
	// }
	
	public void add(String path,String name,String code,String themeid){
		if(this.currentlegth >= this.length){
			increaseData();   //自增长数组
		}
		
			this.path[this.currentlegth]=path;
			this.name[this.currentlegth]= name;
			this.code[this.currentlegth]=code;
			this.counter[this.currentlegth]=0;
			this.setTheme(themeid, this.currentlegth);
			this.currentlegth++;
	
	}
	/**
	 * 自动增长数组（crease_size）
	 */
	private void increaseData(){
		String[] cPath = new String[this.currentlegth+crease_size];
		String[] cName = new String[this.currentlegth+crease_size];
		String[] cCode = new String[this.currentlegth+crease_size];
		Integer[] cCounter = new Integer[this.currentlegth+crease_size];
		System.arraycopy(this.path, 0, cPath, 0, this.path.length);
		System.arraycopy(this.name, 0, cName, 0, this.name.length);
		System.arraycopy(this.code, 0, cCode, 0, this.code.length);
		System.arraycopy(this.counter, 0, cCounter, 0, this.counter.length);
		
		this.path = cPath;
		this.name = cName;
		this.code = cCode;
		this.counter  = cCounter;
		this.length = this.currentlegth +crease_size;
	}
	
	public void setTheme(String themeid, int index){
		if(themeMap.containsKey(themeid)){
			themeMap.get(themeid).add(index);
		}else{
			ArrayList<Integer> list = new ArrayList<Integer>();
			list.add(index);
			themeMap.put(themeid, list);
			
		}
	}
}
