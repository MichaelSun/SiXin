package com.renren.mobile.chat.model.warpper;
import org.w3c.dom.Node;
import com.data.xmpp.Message;
import com.renren.mobile.chat.base.model.ChatBaseItem;
/**
 * @author dingwei.chen1988@gmail.com
 * @说明: 弱消息提醒
 * */
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
public class ChatMessageWarpper_SoftInfo extends ChatMessageWarpper{
	public ChatMessageWarpper_SoftInfo(){
		this.mMessageType = ChatBaseItem.MESSAGE_TYPE.SOFT_INFO;
		this.mMessageContent = RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_SoftInfo_java_1);		//ChatMessageWarpper_SoftInfo_java_1=苍井空,川滨奈美,堤莎也加,町田梨乃,二阶堂仁美,饭岛爱; 
		this.mComefrom = ChatBaseItem.MESSAGE_COMEFROM.NOTIFY;
	}
	@Override
	public void swapDataFromXML(Message message) {
	}
	@Override
	public String getDescribe() {
		return this.mMessageContent;
	}
}
