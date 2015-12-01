package com.common.emotion.emotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.common.emotion.dao.EmotionDaoManager;
import com.common.emotion.model.EmotionBaseModel;
import com.common.emotion.model.EmotionPackageModel;
import com.core.util.CommonUtil;

/****
 * 初始化数据到内存中
 * @author xiaochao.zheng
 *
 */
public class EmotionNameList {
	/* 包Id初始化成功 */
	private static boolean isPageIdIanitSuccess = false;
	/* 表情对照表初始化成功*/
	private static boolean isNameListInitSuccess = false;
	/*存储Node key为包Id*/
	public static HashMap<Integer, Node> EmotionNameTable = new HashMap<Integer, Node>();
	/*包列表*/
	public static ArrayList<Integer> Package_idList = new ArrayList<Integer>();
//	private static HashMap<Integer, ArrayList<Integer>> PackageStyleTable = new HashMap<Integer, ArrayList<Integer>>();
	private static EmotionDaoManager daoManager = EmotionDaoManager.getInstance(); 
     /***
      * 获取包id
      * @return
      */
     public static List<Integer> getPackageList(){
    	 
    	 if(Package_idList.size() <= 0){
    		 initPackList();
    	 }
    	 return Package_idList;
     }
     /****
      * 初始化表情包
      */
	public static void initPackList() {
		if(Package_idList!=null && Package_idList.size()!=0){
			Package_idList.clear();
		}
		if(null == daoManager.pDao){//如果为空的话则显示初始化
			isPageIdIanitSuccess = false;
			return;
		}
		isPageIdIanitSuccess = true;
		List<EmotionPackageModel> pList = daoManager.queryAllPackageOrderBy();
		if(pList!=null){
			
			for (EmotionPackageModel model : pList) {
				Package_idList.add(model.package_id);
			}
		}
	}
	/***	
	 * 初始化 常用表情榜单
	 * @param size 常用表情个数
	 */
	public static void initEmotionRank(int size) {
		if (!isPageIdIanitSuccess) {
			initPackList();
		}

		for (int i = 0; i < Package_idList.size(); ++i) {
			List<EmotionBaseModel> pList = daoManager
					.query_EmotionList_ByPidFrequency(Package_idList.get(i),
							size);// tag
			EmotionRank rank = new EmotionRank(pList == null ? 0 : pList.size());
			if (pList != null) {
				for (EmotionBaseModel model : pList) {
					rank.addToRank(model.emotion_path, model.emotion_code,
							model.theme_id,model.emotion_clicknum);
				}
			}
			EmotionRankManager.getInstance().addNewEmotionRank(
					Package_idList.get(i), rank);
		}
	}
	/***
	 * 初始化表情数据
	 */
	public static void initEmotion_NameList() {
	//	EmotionPackageBaseDao pDao = EmotionDaoFactoryImpl_1.getInstance().buildDAO(EmotionPackageBaseDao.class);
		///要加上判空isPageIdIanitSuccess
		List<EmotionPackageModel> pList  = daoManager.queryAllPackage();
		if(pList== null){
			isNameListInitSuccess = false;
			return ;
		}
		isNameListInitSuccess = true;
		for(EmotionPackageModel model: pList){
			Package_idList.add(model.package_id);
			List<EmotionBaseModel> list = daoManager.query_EmotionList_ByID(model.package_id);
			if (list != null && list.size() > 0) {
				Node node = new Node(list.size());
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i).emotion_hidden.equals("1")){
						node.showlength++;
					}
					node.name[i]= list.get(i).emotion_name;
					node.path[i]= list.get(i).emotion_path;
					node.code[i] = list.get(i).emotion_code;
					node.counter[i] = list.get(i).emotion_clicknum;
					node.setTheme(list.get(i).theme_id,Integer.toString(i));
				}
				node.endSetTheme();
				node.package_id = model.package_id;
				EmotionNameTable.put(model.package_id, node);
			}
			

		}
	}

	/***
	 * 将传入的两位16进制转换成相应的byte[]，只能是两位的啊，
	 * add by zxc
	 * @param str
	 * @return
	 */
	public static byte[] getbyte(String str) {
		byte[] bytes = new byte[str.length()/2];
		int tmp;
		int sum = 0;
		for (int i = 0; i < str.length(); ++i) {

			char ch = str.charAt(i);

			if (i % 2 == 0) {
				if (ch <= '9' && ch >= '0') {
					tmp = ch - 48;
					sum = tmp * 16;
				} else if (ch <= 'F' && ch >= 'A') {//对于小写没有处理，所以小写不能处理
					tmp = ch - 55;
					sum = tmp * 16;
				}
			} else {
				if (ch <= '9' && ch >= '0') {
					tmp = ch - 48;
					sum += tmp;
				} else if (ch <= 'F' && ch >= 'A') {
					tmp = ch - 55;
					sum += tmp;
				}
				bytes[i/2] = (byte) sum;
				sum = 0;
			}
		}
		return bytes;
	}


	/***
	 * 获取package_id 下的表情数据
	 * @param package_id
	 * @return
	 */
	public static Node getEmotionNameList(int package_id){
		if( EmotionNameTable.size()<=0){
				initEmotion_NameList();
			}
		if(!isNameListInitSuccess){
//			Toast.makeText(AbstractRenrenApplication.getAppContext(), "表情初始化失败，无法获取NameList", Toast.LENGTH_SHORT);
			return null;
		}
		return EmotionNameTable.get(package_id);
	}
	/***
	 * 更新计数器
	 * @param package_id
	 * @param index
	 * @param counter
	 */
	public static void updateCounter(int package_id, int index, int counter){
		Node node = EmotionNameTable.get(package_id);
		node.counter[index] = counter;
		EmotionNameTable.put(package_id, node);
	}
	public static String getByteFromSb(String str){
		return (char)getInt(str) + "";
	}
	public static int getInt(String str){
		int tmp ;
		int sum =0;
		int ret=0;
		CommonUtil.log("ret", "str "+ str);
		for (int i = 0; i < str.length(); ++i) {

			char ch = str.charAt(i);

				ret*=16;
				if (ch <= '9' && ch >= '0') {
					tmp = ch - 48;
					sum = tmp * 16;
				} else if (ch <= 'f' && ch >= 'a') {
					tmp = ch - 87;
					sum = tmp * 16;
				}
				 ret +=sum;
					sum = 0;
			
		}
//		CommonUtil.log("ret", "ret " + ret/16);
		return ret/16;
	}
	public static String unicodetoString(int num) {
		return String.valueOf(Integer.toHexString(num)).toUpperCase();
	}
	

}
