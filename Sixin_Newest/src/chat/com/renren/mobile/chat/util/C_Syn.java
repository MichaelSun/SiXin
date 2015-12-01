package com.renren.mobile.chat.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.SyncAdapterType;
import android.util.Log;

import com.common.manager.LoginManager;
import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.data.parser.C_SAXParserHandler;
import com.data.parser.C_SAXParserHandler.OnDataParserListener;
import com.data.parser.C_SAXParserHandler.Transaction;
import com.data.xmpp.Iq;
import com.data.xmpp.Message;
import com.data.xmpp.Presence;
import com.renren.mobile.chat.actions.ActionDispatcher;
import com.renren.mobile.chat.actions.models.SynHistoryRecordModel;
import com.renren.mobile.chat.activity.RenRenChatActivity.CanTalkable.GROUP;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.dao.SynHistoryRecordDAO;
/**
 * @author dingwei.chen
 * */
public class C_Syn implements OnDataParserListener{

	private static final C_Syn INSTANCE = new C_Syn();
	SynHistoryRecordDAO mSynHistoryDAO = null;
	private C_Syn(){
		mSynHistoryDAO = DAOFactoryImpl.getInstance().buildDAO(SynHistoryRecordDAO.class);
	}
	
	public void updateSynTime(long localId,long chatId,long time){
		  SynHistoryRecordModel synHistoryRecordModel = new SynHistoryRecordModel();
          synHistoryRecordModel.mToChatId = chatId;
          synHistoryRecordModel.mLastSynTime = time;
          synHistoryRecordModel.mLocalId = localId;
          mSynHistoryDAO.updateSynHitoryRecordInfo(chatId, synHistoryRecordModel);
	}
	
	
	
	Set<Long> mSynRequestSet = new HashSet<Long>();
	
	
	public static C_Syn getInstance(){
		return INSTANCE;
	}
	
	public void syn(boolean isGroup,long toChatUserId,String userName,OnSynListener listener){
		synchronized (mSynRequestSet) {
			if(mSynRequestSet.contains(toChatUserId)){
				return ;
			}
			mSynRequestSet.add(toChatUserId);
		}
		McsServiceProvider.getProvider().getHistoryMessages(
				isGroup,
				toChatUserId, 
				new SynResponse(toChatUserId,isGroup?GROUP.GROUP.Value:GROUP.CONTACT_MODEL.Value,userName,listener));
		if(listener!=null){
			listener.onSynStart();
		}
	}
	public static interface OnSynListener{
		public void onSynStart();
		public void onBeginInsert(long uid,int isGroup);
		public void onSynOver(long uid,int isGroup,boolean isSuccess,long time,int count);
	}
	
	
	class SynResponse extends INetReponseAdapter{
		OnSynListener mListener;
		String mName = null;
		long mUserId = -1L;
		int mIsGroup = GROUP.CONTACT_MODEL.Value;
		long mCallTime = System.currentTimeMillis();
		public long mLocalUserId = 0L;
		public SynResponse(long userid,int isGroup,String name,OnSynListener listener){
			mListener = listener;
			mName = name;
			mUserId = userid;
			this.mIsGroup = isGroup;
			mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
		}
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			int count = (int)data.getNum("count");
//			SystemUtil.log("syn", "success = "+data);
			long time = System.currentTimeMillis();
			if(count==0){
				  if(mListener!=null){
	                	mListener.onSynOver(mUserId,mIsGroup,true,time,count);
	              }
				return;
			}
            JsonArray array = data.getJsonArray("history_list");
            Transaction transaction = C_SAXParserHandler.getInstance().beginTransaction(C_Syn.this);
            parse(array,transaction);
            	if(mListener!=null){
                	mListener.onBeginInsert(mUserId,mIsGroup);
                }
                List<Message> messages = transaction.getMessages();
                for(Message m:messages){
//                	m.mFromName = mName;
                	m.mOffline ="syn";
                }
                transaction.commit();
                
                SystemUtil.log("syn", "C_Syn parse commit");
                if(mListener!=null){
                	mListener.onSynOver(mUserId,mIsGroup,true,time,count);
                }
                updateSynTime(this.mLocalUserId, mUserId,time);
		}
		@Override
		public void onError(INetRequest req, JsonObject data) {
			if(data.getNum("error_code")==-99){
				SystemUtil.toast("请检查您的网络提示，稍后进行重试");
			}else{
				SystemUtil.toast("同步失败");
			}
			 if(mListener!=null){
	            	mListener.onSynOver(mUserId,mIsGroup,false,0,0);
	        }
		}
		@Override
		protected void onResponseOver() {
			SystemUtil.log("syn", "C_Syn onResponseOver()");
			synchronized (mSynRequestSet) {
				mSynRequestSet.remove(mUserId);
			}
//			 if(mListener!=null){
//	             mListener.onSynOver(mUserId,mIsGroup,false,0,0);
//	        }
		}
		@Override
		protected void onResponseBegin() {}
		@Override
		protected void onResponseException(Exception e) {
			 SystemUtil.log("syn", "C_Syn parse exception = "+e+":"+SystemUtil.printStackElements(e.getStackTrace()));
		}
	}
	
	 private void parse(JsonArray array,Transaction t) {
		 long time = System.currentTimeMillis();
		 SystemUtil.log("syn", "C_Syn parse start");
	        JsonValue[] objs = new JsonValue[array.size()];
	        array.copyInto(objs);
			for(JsonValue v : objs) {
	            String messageJson = v.toString();
//	            SystemUtil.log("syn",messageJson);
	            C_SAXParserHandler.getInstance().parse(messageJson, t);
			}
			SystemUtil.log("syn", "C_Syn parse over:"+(System.currentTimeMillis()-time));
	}

	@Override
	public void onParserMessageNode(List<Message> list) {
		ActionDispatcher.getInstance().batchDispatchAction(list);
	}

	@Override
	public void onParserPresenceNode(List<Presence> list) {
		ActionDispatcher.getInstance().batchDispatchAction(list);
	}

	@Override
	public void onParserIqNode(List<Iq> list) {
		ActionDispatcher.getInstance().batchDispatchAction(list);
	}

	@Override
	public void onParserError(String errorMessage) {
		// TODO Auto-generated method stub
		SystemUtil.log("syn", "error "+errorMessage);
	}
	
}
