package com.renren.mobile.chat.ui.contact;

import com.common.network.DomainUrl;

public class ContactModelFactory {
	
	
	public ContactModelFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static ContactBaseModel createContactModel(String domain){
		ContactBaseModel contactBaseModel ;
		if(domain.equals(DomainUrl.SIXIN_DOMAIN)||domain.equals(DomainUrl.SIXIN_OLD_DOMAIN)||domain.equals(DomainUrl.SIXIN_RECOMMENDATION_DOMAIN)||domain.endsWith(DomainUrl.SIXIN_DOMAIN)){
			contactBaseModel  = new ContactModel();
		}else{
			contactBaseModel = new ThirdContactModel();
		}
		contactBaseModel.mDomain = domain;
		return contactBaseModel;
	}
	
}
