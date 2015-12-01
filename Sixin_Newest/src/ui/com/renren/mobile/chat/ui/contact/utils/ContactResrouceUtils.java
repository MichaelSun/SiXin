package com.renren.mobile.chat.ui.contact.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.text.TextUtils;

import com.common.utils.Pinyin;
import com.common.utils.SearchAble;
import com.renren.mobile.chat.common.PinyinUtils;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.ThirdContactModel;

/**
 * 根据姓名对联系人进行排序 
 * */
public class ContactResrouceUtils {

	//<cf TODO> 有时间重新整理这块代码
	
	/**
	 * @说明 将联系人资源整合并排序（根据业务） 此方法将底层排序进行了封装为业务工具方法
	 * 
	 * @注意： Aleph需要存入数据库
	 * 
	 * @return 一个完全的列表 没有title 特殊字符排在最后
	 */
	public static ArrayList<ContactModel> combineResrouceOrderd(List<ContactModel> contactList) {
		
//		SystemUtil.errord("nosortList");
//		for (ContactModel contactModel : contactList) {
//			SystemUtil.logd("name="+contactModel.getName()+"#"+contactModel.getmContactName()+"#quanpain="+contactModel.getQuanPin());
//		}
//		SystemUtil.errord("end nosortList");
		
		ArrayList<ContactModel> temArrayList;
		sortContactList(contactList);
		
//		Logd.error("first contactList");
//		for (ContactModel contactModel : contactList) {
//			Logd.log("name="+contactModel.getName()+"#"+contactModel.getContactName());
//		}
//		Logd.error("end first contactList");
		
		synchronized (contactList) {
			temArrayList = new ArrayList<ContactModel>(contactList);
			special2end(temArrayList);
			contactList.clear();
			contactList = null;
		}
		
//		SystemUtil.errord("last contactList");
//		for (ContactModel contactModel : temArrayList) {
//			SystemUtil.logd("name="+contactModel.getName()+"#"+contactModel.getmContactName());
//		}
//		SystemUtil.errord("end last contactList");
		
		return temArrayList;
	}
	
	
	
	/**
	 * 将联系人列表进行初步排序（已经满足官方客户端要求了）
	 * 在列表中的特效静态变量需要在得到数据后进行重新遍历插入
	 * @param myList
	 */
	private static void sortContactList(List<ContactModel> myList) {
		Collections.sort(myList, new Comparator<ContactModel>() {
			@Override
			public int compare(ContactModel object1, ContactModel object2) {
				return ContactResrouceUtils.compares(object1, object2);
			}
		});
	}
	
	/**
	 * 统一的排序比较规则
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!需要将ContactModel继承统一接口    将封装性扩展 ！！！！！！！！！！！！！！！！！！！！！！！
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 */
	private static int compares(SearchAble object1, SearchAble object2) {
		int indexCompare = 0;
		try {
			indexCompare = object1.getQuanPin().compareTo(object2.getQuanPin());
		} catch(NullPointerException npe) {
			indexCompare = 0;
		}
		if (object1 != null && object1.getName() != null && object2 != null && object2.getName() != null) {
			if(object1.getName() != null && object2.getName() != null){
				return (indexCompare == 0) ? object1.getName().compareTo(object2.getName()) : indexCompare;
			}
		}
		return 0;
	}
	
//	public static void dealSpecialCharacter(ArrayList<ContactModel> contactList){
//		special2end(contactList);
//	}
	
	/**
	 * function:将特殊字符置为队尾
	 * @param charac
	 * @return
	 */
		
	synchronized public static void special2end(ArrayList<ContactModel> contactList){

		Collections.sort(contactList, new Comparator<ContactModel>(){

			private boolean isAlpha(char c) {
				return c>='a' && c<='z';
			}
			
			@Override
			public int compare(ContactModel x, ContactModel y) {
				return isAlpha(x.mAleph) ? (isAlpha(y.mAleph) ? 0 : -1) : (isAlpha(y.mAleph) ? 1 : 0 );
			}
			
			});	
	}
	
//	private static void dealSpecialCharacter(ArrayList<ContactModel> contactList){
//		Collections.sort(contactList, new Comparator<ContactModel>(){
//			private boolean isAlpha(char c) {
//				return c>='a' && c<='z';
//			}
//			@Override
//			public int compare(ContactModel x, ContactModel y) {
//				return isAlpha(x.getAleph()) ? (isAlpha(y.getAleph()) ? 0 : -1) : (isAlpha(y.getAleph()) ? 1 : 0 );
//			}
//			
//			});	
//	}
	
	/**
	 * 解析json时用
	 * 
	 * @param friendItem
	 * @param index
	 *            排序时用的索引
	 * @param namePinyin
	 *            搜索时用的索引
	 */
	public static void setPinyinIntoItem(SearchAble friendItem, String index, String namePinyin,int nameLength) {
		Pinyin re;
		if (index == null || namePinyin == null) {
			re = PinyinUtils.getPinyin(PinyinUtils.nameTrim(friendItem.getName()));// 搜索时不能有空格
		} else {
			re = new Pinyin();
			re.setQuanPin(index);
			re.setMultiPinYin(PinyinUtils.string2Array(namePinyin));
		}
		if(re==null){
			re = PinyinUtils.getPinyin("#");  
		}
		if (!TextUtils.isEmpty(friendItem.getName())) {
			friendItem.setQuanPin(re.getQuanPin());
		} else {
			friendItem.setQuanPin("zz");
		}
		
		friendItem.setNamePinyin(re.getMultiPinYin());    //only set hear
		PinyinUtils.insertFriendsCache(PinyinUtils.nameTrim(friendItem.getName()), re);
		//friendItem.initDye(nameLength);
	}

	/////////////////////////<cf TODO> 以后整理这块代码////////////////////////////////////////////////
	
	
public static ArrayList<ThirdContactModel> combineOrderd(List<ThirdContactModel> contactList) {
		
//		SystemUtil.errord("nosortList");
//		for (ThirdContactModel contactModel : contactList) {
//			SystemUtil.logd("name="+contactModel.getName()+"#"+contactModel.getmContactName()+"#quanpain="+contactModel.getQuanPin());
//		}
//		SystemUtil.errord("end nosortList");
		
		ArrayList<ThirdContactModel> temArrayList;
		sortThridContactList(contactList);
		
//		SystemUtil.errord("first contactList");
//		for (ThirdContactModel contactModel : contactList) {
//			SystemUtil.logd("name="+contactModel.getName()+"#"+contactModel.getmContactName());
//		}
//		SystemUtil.errord("end first contactList");
		
		synchronized (contactList) {
			temArrayList = new ArrayList<ThirdContactModel>(contactList);
			specialThrid2end(temArrayList);
		}
		
//		SystemUtil.errord("last contactList");
//		for (ThirdContactModel contactModel : temArrayList) {
//			SystemUtil.logd("name="+contactModel.getName()+"#"+contactModel.getmContactName());
//		}
//		SystemUtil.errord("end last contactList");
		
		return temArrayList;
	}
	
	
	
	/**
	 * 将联系人列表进行初步排序（已经满足官方客户端要求了）
	 * 在列表中的特效静态变量需要在得到数据后进行重新遍历插入
	 * @param myList
	 */
	private static void sortThridContactList(List<ThirdContactModel> myList) {
		Collections.sort(myList, new Comparator<ThirdContactModel>() {
			@Override
			public int compare(ThirdContactModel object1, ThirdContactModel object2) {
				return ContactResrouceUtils.compares(object1, object2);
			}
		});
	}
	
	/**
	 * 统一的排序比较规则
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!需要将ContactModel继承统一接口    将封装性扩展 ！！！！！！！！！！！！！！！！！！！！！！！
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 */
	private static int thridCompares(ThirdContactModel object1, ThirdContactModel object2) {
		int indexCompare = 0;
		try {
			indexCompare = object1.getQuanPin().compareTo(object2.getQuanPin());
		} catch(NullPointerException npe) {
			indexCompare = 0;
		}
		if (object1 != null && object1.getName() != null && object2 != null && object2.getName() != null) {
			if(object1.getName() != null && object2.getName() != null){
				return (indexCompare == 0) ? object1.getName().compareTo(object2.getName()) : indexCompare;
			}
		}
		return 0;
	}
	
//	public static void dealSpecialCharacter(ArrayList<ContactModel> contactList){
//		special2end(contactList);
//	}
	
	/**
	 * function:将特殊字符置为队尾
	 * @param charac
	 * @return
	 */
		
	synchronized public static void specialThrid2end(ArrayList<ThirdContactModel> contactList){

		Collections.sort(contactList, new Comparator<ThirdContactModel>(){

			private boolean isAlpha(char c) {
				return c>='a' && c<='z';
			}
			
			@Override
			public int compare(ThirdContactModel x, ThirdContactModel y) {
				return isAlpha(x.mAleph) ? (isAlpha(y.mAleph) ? 0 : -1) : (isAlpha(y.mAleph) ? 1 : 0 );
			}
			
			});	
	}
	
}
