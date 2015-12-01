package com.renren.mobile.chat.sql;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.common.PinyinUtils;
import com.renren.mobile.chat.ui.contact.ContactModel;

/**
 * @author dingwei.chen1988@gmail.com
 * */
public class Query_AllContacts extends Query{

	public Query_AllContacts(BaseDAO dao) {super(dao);}

	@Override
	public Object warpData(Cursor c) {
		List<ContactModel> data = new ArrayList<ContactModel>();
		if(c.getCount()>0){
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ContactModel model = new ContactModel();
				ORMUtil.getInstance().ormQuery(ContactModel.class, model, c);
				if(model.mAlephString!=null && model.mAlephString.length()>0){
					model.mAleph = model.mAlephString.charAt(0);
				}
				//SystemUtil.logd("mNamePinyin_String="+model.mNamePinyin_String);
				model.mNamePinyin = PinyinUtils.string2Array(model.mNamePinyin_String);
//				if(model.mNamePinyin!=null && model.mNamePinyin.length>0){
//					model.setQuanPin(String.valueOf(model.mNamePinyin[0]));
//				}
				model.setName(model.getmContactName());
				model.initDye(PinyinUtils.nameLength(model.getmContactName()));
				data.add(model);
			}
		}
		return data;
	}

}
