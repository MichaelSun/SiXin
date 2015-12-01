package com.renren.mobile.chat.test;

import java.util.List;

import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Text;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.util.ChatDataHelper;

public class TestFactory {

//	public static GroupModelWarpper createTestGroupModel(){
//		GroupModelWarpper model = new GroupModelWarpper();
//		model.mMemberCount = 3;
//		model.mMemberIds = "234744463,201994181,258184610";
//		model.mMasterId = 234744463;
//		model.mGroupName = "西夏一品堂";
//		model.mGroupId = 1L;
//		return model;
//	}
	public static ChatMessageWarpper_Text createTestChatModel(int number){
		ChatMessageWarpper_Text model = new ChatMessageWarpper_Text();
		model.mComefrom=ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL;
		model.mMessageContent = "Hello " +number;
		model.mUserName ="zero";
		model.mHeadUrl = "http://hdn.xnimg.cn/photos/hdn121/20120422/2110/h_tiny_nFTg_563b0006179c2f76.jpg";
		model.mIsGroupMessage = ChatBaseItem.MESSAGE_ISGROUP.IS_SINGLE;
		model.mLocalUserId = 9999;
		model.mToChatUserId= 8888;
		model.mGroupId= 8888;
		return model;
	}
	
	
	public static ContactModel createContactModel(){
		ContactModel model = new ContactModel();
		model.mUserId=100022635;
		model.mContactName="王大田";
		model.mDomain = "sixin";
		return model;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void testInsert(){
		int k = 0;
		for(; k <100;k++){
			ChatMessageWarpper_Text m = createTestChatModel(k);
			ChatDataHelper.getInstance().insertToTheDatabase(m);
		}
	}
	public static void testQuery(){
		List<ChatMessageWarpper> list = ChatDataHelper.getInstance().queryChatHistory(9999, 8888, 40, 1, System.currentTimeMillis(), ChatBaseItem.MESSAGE_ISGROUP.IS_SINGLE);
		SystemUtil.log("query", "query size = "+list.size()+","+ChatDataHelper.getInstance().querySumCount());
	}
	
	
	
	
}
