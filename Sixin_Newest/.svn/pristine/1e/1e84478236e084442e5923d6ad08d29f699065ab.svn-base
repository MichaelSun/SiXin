package com.renren.mobile.chat.ui.contact;

import java.util.LinkedList;
import java.util.List;


/**
 * @author dingwei.chen1988@gmail.com
 * @说明 首字母搜索
 * */
public class C_AphleSearchUtil {
	private static  String mPreSearchKey = null;
	private static List<ContactModel> mPreSearchList = null;
	
	public static void search(String searchKey,List<ContactModel> srcList,List<ContactModel> descList){
		String searchkeyStr = searchKey.trim();
		char[] match_keys = searchkeyStr.toCharArray();
		
		List<ContactModel> des = null;
		for(char match_char:match_keys){
			des = new LinkedList<ContactModel>();
			search(match_char, srcList, des,match_keys.length);
			srcList =des;
		}
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("{");
		for(ContactModel m: srcList){
			sbuilder.append(m.mContactName+",");
		}
		sbuilder.append("}");
	}
	private static void search(char matchChar,List<ContactModel> srcList,List<ContactModel> descList,int searchKeyLength){
		for(ContactModel model:srcList){
			String contactName = model.mContactName;
			if(contactName.length()<searchKeyLength){
				continue;
			}else{
				int index = getIndex(contactName, matchChar);//名字中找匹配
				if(index!=-1){
					descList.add(model);
					continue;
				}else{
					char[][] to_match_pinyin = model.getNamePinyin();//首字母中找匹配
					for(int row = 0; row < to_match_pinyin.length; row++){
						char[] chars = to_match_pinyin[row];
						index = getIndex(chars, matchChar);
						if(index!=-1){
							descList.add(model);
							break;
						}
					}
					continue;
				}
			}
		}
	}
	public static  int getIndex(String chars,char c){
		int k = 0;
		int result = -1;
		int length = chars.length();
		while( k <length){
			if(c == chars.charAt(k)){
				result = k;
				break;
			}
			k++;
		}
		return result;
	}
	public static int getIndex(char[] chars,char c){
		int k = 0;
		int result = -1;
		for(;k<chars.length;k++){
			if(chars[k]==c){
				result = k;
				break;
			}
		}
		return result;
	}
}
