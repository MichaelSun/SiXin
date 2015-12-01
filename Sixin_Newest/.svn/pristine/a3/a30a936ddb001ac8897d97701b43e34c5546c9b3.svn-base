package com.renren.mobile.chat.sql;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.PinyinUtils;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.ThirdContactModel;

public class Query_RenRen_Contacts extends Query{

	public Query_RenRen_Contacts(BaseDAO dao) {super(dao);}

	@Override
	public Object warpData(Cursor c) {
		List<ThirdContactModel> data = new ArrayList<ThirdContactModel>();
		if(c.getCount()>0){
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ThirdContactModel model = new ThirdContactModel();
				ORMUtil.getInstance().ormQuery(ThirdContactModel.class, model, c);
				if(model.mAlephString!=null && model.mAlephString.length()>0){
					model.mAleph = model.mAlephString.charAt(0);
				}
				model.mNamePinyin = PinyinUtils.string2Array(model.mNamePinyin_String);
//				if(model.mNamePinyin!=null && model.mNamePinyin.length>0){
//					model.setQuanPin(String.valueOf(model.mNamePinyin[0]));
//					SystemUtil.logd("xing=="+String.valueOf(model.mNamePinyin[0]));
//				}
				model.setName(model.getmContactName());
				model.initDye(PinyinUtils.nameLength(model.getmContactName()));
				data.add(model);
			}
		}
		return data;
	}

}
