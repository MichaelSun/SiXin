package com.renren.mobile.chat.receiver;

import java.util.List;

import com.common.binder.AbstractPushReceiver;
import com.data.parser.C_SAXParserHandler;
import com.data.parser.C_SAXParserHandler.OnDataParserListener;
import com.data.xmpp.Iq;
import com.data.xmpp.Message;
import com.data.xmpp.Presence;
import com.renren.mobile.chat.actions.ActionDispatcher;

public class PushMessagesReceiver extends AbstractPushReceiver{

	
	
	@Override
	public void onPush(String message) {
		C_SAXParserHandler.getInstance().setOnDataParserListener(XMPPCallback.getInstance());
//		message = "<root>"+message+"</root>";
		C_SAXParserHandler.getInstance().parse(message);
	}

	public static class XMPPCallback implements OnDataParserListener{
		
		private static final XMPPCallback INSTANCE = new XMPPCallback();
		private XMPPCallback(){}
		public static XMPPCallback getInstance(){
			return INSTANCE;
		}
		
		@Override
		public void onParserPresenceNode(List<Presence> arg0) {
			for(Presence p:arg0){
				ActionDispatcher.getInstance().dispatchAction(p);
			}
		}
		
		@Override
		public void onParserMessageNode(List<Message> arg0) {
			for(Message p:arg0){
				ActionDispatcher.getInstance().dispatchAction(p);
			}
		}
		
		@Override
		public void onParserIqNode(List<Iq> arg0) {
			for(Iq p:arg0){
				ActionDispatcher.getInstance().dispatchAction(p);
			}
		}

		@Override
		public void onParserError(String errorMessage) {
			// TODO Auto-generated method stub
			
		}
	}
	
}
