package com.renren.mobile.chat.ui.plugins;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import plugin.DBBasedPluginManager;
import plugin.base.PluginManager;
import plugin.containers.v0.ContainerV0;
import plugin.plugins.v0.ChatClientResponse;
import android.os.Bundle;

import com.common.manager.LoginManager;
import com.common.manager.MessageManager;
import com.common.network.NetRequestListener;
import com.common.network.requests.Message_SendByPlugin;
import com.renren.mobile.chat.dao.ContactsDAO;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.ui.contact.ContactModel;

public class PluginContainer extends ContainerV0 {

	public PluginContainer() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addFocus(String arg0, long arg1, ChatClientResponse arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject getRoasterDetailedInfo(String arg0) {
        JSONObject objects = null;

        ContactsDAO contactsDAO = DAOFactoryImpl.getInstance().buildDAO(ContactsDAO.class);
        if (contactsDAO.querySumCount("is_friend = 1") > 0) {
            objects = onQueryFinished(contactsDAO.query_AllContact());
        }
        return objects;
	}

	@Override
	public JSONObject getUserInfo(String arg0) {
        JSONObject jo = new JSONObject();
        try{
            jo.put("user_name", LoginManager.getInstance().getLoginInfo().mUserName);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jo;
	}

	@Override
	public JSONObject getUserUID(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void openContactUi(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openDialogUiByUid(String arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public String refreshSessionId(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeFocus(String arg0, long arg1, ChatClientResponse arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestStart(String arg0, Bundle arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendImage(String arg0, String arg1, ChatClientResponse chatClientResponse) {
		// TODO Auto-generated method stub

	}
	/**
	 * 临时插件发消息
	 * */

    @Override
    public void sendMessage(String callerJID, String body, ChatClientResponse chatClientResponse) {
        final NetRequestListener request ;
        if (null == chatClientResponse) {
            request = new Message_SendByPlugin(body);
        } else {
            request = new Message_SendByPlugin(chatClientResponse, body);
        }
        MessageManager.getInstance().sendMessage(request);
    }

    /**
     * 转换格式 ContactModel 2 JSONObject
     * */
    private JSONObject onQueryFinished(List<ContactModel> contacts) {

        JSONObject objects = null;

        if (contacts != null && contacts.size() > 0) {
            objects = new JSONObject();
            JSONArray array = new JSONArray();
            try {
                for (ContactModel contact : contacts) {
                    JSONObject object = new JSONObject();
                    object.put("uid", contact.mUserId);
                    object.put("name", contact.getDyeContactName());//<cf TODO>确认一下
                    array.put(object);
                }
                objects.put("friend_list", array);
            } catch (JSONException e){
            }
        }

        return objects;
    }

}
