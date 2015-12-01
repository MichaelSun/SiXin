package com.renren.mobile.chat.ui.contact;


import com.common.actions.ActionNotMatchException;
import com.common.statistics.BackgroundUtils;
import com.common.utils.Bip;
import com.core.json.JsonObject;
import com.core.json.JsonParser;
import com.core.util.Base64;
import com.data.xmpp.Message;
import com.data.xmpp.Person;
import com.renren.mobile.chat.actions.message.Action_Message;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.setting.SettingDataManager;

public final class ContactMessage extends Action_Message {

	private static final String PROFILE_TYPE_ADD = "add";
	
	/**
	 * <message from="recommendation.sixin.com" to="b_gid@talk.sixin.com">
  <z xmlns="http://recommendation.sixin.com">
    <person>
      <gid>a_gid</gid>
      <domain>talk.sixin.com</domain>
      <from_type>0</from_type><!-- 0通讯录；1人人；2Facebook -->
      <from_text>通讯录</from_text>
      <name>A</name>
      <picture_profile_url>http://img/b/200.jpg</picture_profile_url>
      <!--
        0：被系统自动互相添加为联系人
        1：推荐联系人（双方尚未添加对方）
        2：被对方添加为联系人的提醒（自己尚未添加对方）
        3：发送方请求把接受方加为发送方的联系人（双方尚未添加对方）
      -->
      <type>3</type>
    </person>
    <body type="text"><text>我是小Y啊，赶快加我~</text></body>
  </z>
</message>
	 */
	@Override
	public void processAction(Message node, long id) {
		SettingDataManager.getInstance().obtainSwitchState();
		if(SettingDataManager.getInstance().mSoundState &&
				(BackgroundUtils.getInstance().isAppOnForeground() || SettingDataManager.getInstance().mPushState))
			if (RenRenChatActivity.class.isInstance(GlobalValue.getCurrentActivity())){
				Bip.bipIncomingPush();
			}else if(MainFragmentActivity.class.isInstance(GlobalValue.getCurrentActivity()) 
					&& ((MainFragmentActivity)GlobalValue.getCurrentActivity()).mPager.getCurrentItem() == MainFragmentActivity.Tab.CONTRACT){
				Bip.bipIncomingPush();
			}else if(!BackgroundUtils.getInstance().isAppOnForeground()){
				Bip.bipIncomingPush();
			}
		try {
			if(node.mZNode!=null){
				if(node.mZNode.mProfile!=null){//客户端添加联系人消息（server推送）
					String contact = node.mZNode.mProfile.mValue;
				//	Base64.decode(input, flags)
					if(SystemUtil.mDebug){
						SystemUtil.logd("添加联系人消息base641前="+contact);
					}
					contact = new String(Base64.base64Decode(contact),"utf-8");//<cf TODO> 不知道啥编码				
					if(SystemUtil.mDebug){
						SystemUtil.logd("添加联系人消息base641后1  编码未知="+contact);
					}
					if(node.mZNode.mProfile.mType.contains(PROFILE_TYPE_ADD)){
						addContact(contact);
					}
				//	if(node.mZNode.mProfile.mType=)
				}else if(node.mZNode.mPersion != null){//推荐消息
					SystemUtil.logd("获取到联系人推荐消息type="+node.mZNode.mPersion.mType);
					Person p = node.mZNode.mPersion;
					ContactMessageModel model = new ContactMessageModel();
					model.setType(Integer.parseInt(p.mType.mValue));
					if(SystemUtil.mDebug){
						SystemUtil.logd("Type="+Integer.parseInt(p.mType.mValue));
					}
					model.setGid(Long.parseLong(p.mGid.mValue));
					if(SystemUtil.mDebug){
						SystemUtil.logd("Gid="+Long.parseLong(p.mGid.mValue));
					}
					model.setDomain(p.mDomain.mValue);
					if(SystemUtil.mDebug){
						SystemUtil.logd("Domain="+p.mDomain.mValue);
					}
					model.setFrom_type(Integer.parseInt(p.mFromType.mValue));
					if(SystemUtil.mDebug){
						SystemUtil.logd("From_type="+Integer.parseInt(p.mFromType.mValue));
					}
					model.setFrom_text(p.mFromText.mValue);
					if(SystemUtil.mDebug){
						SystemUtil.logd("From_text="+p.mFromText.mValue);
					}
					model.setName(p.mName.mValue);
					if(SystemUtil.mDebug){
						SystemUtil.logd("Name="+p.mName.mValue);
					}
					model.setHead_url(p.mPictureProfileUrl.mValue);
					if(SystemUtil.mDebug){
						SystemUtil.logd("Head_url="+p.mPictureProfileUrl.mValue);
					}
					if(node.mZNode.mBody!=null){
						model.setBody(node.mZNode.mBody.mTextNode.mValue);
						if(SystemUtil.mDebug){
							SystemUtil.logd("mBody="+node.mZNode.mBody.mTextNode.mValue);
						}
					}else{
						if(SystemUtil.mDebug){
							SystemUtil.logd("mBody is null");
						}
					}
					model.setNativeId(System.currentTimeMillis());
					ContactMessageData.getInstance().notifyData(model);
				}
			}
		} catch (Exception e) {
			if(SystemUtil.mDebug){
				SystemUtil.errord(e.toString());
			}
			e.printStackTrace();
		}
		
	}

	@Override
	public void checkActionType(Message node) throws ActionNotMatchException {
		if(SystemUtil.mDebug){
			SystemUtil.logd("" + node.toXMLString());
		}
		addCheckCase(node.mZNode != null);
		//addCheckCase(!TextUtils.isEmpty(node.mZNode.mXmlns)).
		//addCheckCase("http://recommendation.sixin.com".equals(node.mZNode.mXmlns));
	}
	
	private void addContact(String contact){
		JsonObject jo = (JsonObject) JsonParser.parse(contact);
		ContactModel  model = ContactModel.newParseContactModel(jo);
		if(model!=null){
			int relation = model.getmRelation();
			if((relation&ContactBaseModel.Relationship.IS_SI_XIN)==ContactBaseModel.Relationship.IS_SI_XIN){
				C_ContactsData.getInstance().addContact(model);
			}
		}
	}

}
