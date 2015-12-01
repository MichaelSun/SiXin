package com.common.utils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yisong.li@renren-inc 拼音搜索相关
 * 
 */
public class PinyinSearch {
	
	private static final String UNICODE="unicode";
	public static final int MAX_LENGTH = 20;
	private static String preKey = null;

	public static void  mapSearch(String searchKey, List<SearchAble> list,  List<SearchAble> results) {
		
		//long start=Logd.getTime();

		if (!searchKey.equals("")) {
			searchKey = searchKey.toLowerCase();//小写
			searchKey = nameTrim(searchKey);//删除空行
			searchKey = QBchange(searchKey);//unicode转换
			ArrayList<SearchAble> re = new ArrayList<SearchAble>();
			ArrayList<SearchAble> searchList = null;
			if (preKey != null && searchKey.startsWith(preKey) && results!=null && results.size() != 0) {
				searchList = new ArrayList<SearchAble>(results);
			} else {
				searchList = new ArrayList<SearchAble>(list);
			}
			preKey = searchKey;
			for (SearchAble item : searchList) {
				if(item.getName()==null){                                            ////////////////////////add
					continue;
				}
				final String name = nameTrim(item.getName());  /////
				char[][] initArrayArray = item.getNamePinyin(); //得到拼音数组
				if(initArrayArray == null){//没有拼音数组的不能搜索
					continue;
				}
				boolean isMatched = false;
				int searchKeyLen = searchKey.length();
				int nameLen = name.length();
				
				for (int i = 0; searchKeyLen + i <= nameLen; i++) {
					int ordinalInName = i;
					if (!isMatched) {
						for (int ordinalInKey = 0; ordinalInKey < searchKeyLen ; ordinalInKey++, ordinalInName++) {
							// TODO nullpointer exception
							char[] initArray = initArrayArray[ordinalInName];
							boolean isOk = false;
							for (char init : initArray) {
								if (searchKey.charAt(ordinalInKey) == init) {
									item.nameSearched(ordinalInName);
									isOk = true;
									break;
								}
							}
							if (searchKey.charAt(ordinalInKey) == name.charAt(ordinalInName)) {
								item.nameSearched(ordinalInName);
								isOk = true;
							}
							if (isOk) {
								if (ordinalInKey == searchKey.length() - 1) {
									isMatched = true;
									re.add(item);
								}
							} else {
								break;
							}
						}
					} else {
						break;
					}
				}
			}

			list.clear();
			list.addAll(re);
		} 
		//Logd.log("time="+(Logd.getTime()-start));//g12 310个联系人  3毫秒左右
		
	}

	/**
	 * 全角字符传化成半角
	 * 
	 * @param QJstr
	 *            全角字符
	 * @return 半角字符
	 */
	public static final String QBchange(String QJstr) {
		StringBuilder outStr = new StringBuilder();
		String Tstr = "";
		byte[] b = null;

		int len=QJstr.length();
		for (int i = 0; i < len ; i++) {
			try {
				Tstr = QJstr.substring(i, i + 1);
				b = Tstr.getBytes(UNICODE);
			} catch (java.io.UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (b[3] == -1) {
				b[2] = (byte) (b[2] + 32);
				b[3] = 0;
				try {
					//outStr = outStr + new String(b, "unicode");
					outStr.append(new String(b, UNICODE));
				} catch (java.io.UnsupportedEncodingException e) {
					//e.printStackTrace();
				}
			} else{
				//outStr = outStr + Tstr;
				outStr.append(Tstr);
			}
				
		}
		return outStr.toString();
	}
	
	/**
	 * 生成索引的时候可以过滤掉名字里面的特殊字符(现在只有空格)
	 * 
	 * @param name
	 * @return
	 */
	public static String nameTrim(String name) {
		if(name == null){
			return "#";
		}
		name = name.trim();
		return name;
	}
}
