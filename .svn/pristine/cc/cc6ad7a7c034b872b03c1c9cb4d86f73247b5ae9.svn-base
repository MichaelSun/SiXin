package com.data.xmpp;

import android.text.TextUtils;

import com.common.app.AbstractRenrenApplication;
import com.common.manager.LoginManager;
import com.common.messagecenter.base.Utils;
import com.core.util.CommonUtil;
import com.data.xmpp.childs.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author dingwei.chen1988@gmail.com
 * @说明 XMPP节点工厂
 * @根节点：
 * 		根节点是直接包含在最外层<BODY>节点内的节点,目前包含三种：
 * @see com.data.xmpp.Message 
 * @see com.data.xmpp.Presence 
 * @see com.data.xmpp.Iq 
 * */
public final class XMPPNodeFactory {

	private static XMPPNodeFactory sInstance = new XMPPNodeFactory();
	private XMPPNodeFactory(){}
	public static XMPPNodeFactory getInstance(){
		return sInstance;
	}
	public List<Message> mMessages = new LinkedList<Message>();
	public List<Iq> mIqs = new LinkedList<Iq>();
	public List<Presence> mPresences = new LinkedList<Presence>();
	public Map<String,Class> mCache = new HashMap<String, Class>();
	public void clear(){
		mMessages.clear();
		mIqs.clear();
		mPresences.clear();
	}
	
	/**
	 * XMPP生成根节点
	 * */
	public XMPPNode createXMPPRootNode(String rootText){
		XMPPNode node = null;
		if(rootText!=null){
			if(rootText.equals("message")){
				node =  new Message();
				mMessages.add((Message)node);
			}
			if(rootText.equals("presence")){
				node =  new Presence();
				mPresences.add((Presence)node);
			}
			if(rootText.equals("iq")){
				node =  new Iq();
				mIqs.add((Iq)node);
			}
		}
		return node;
	}
	
	public XMPPNodeRoot obtainRootNode(String rootText,String from,String to,Object id){
		XMPPNodeRoot node = null;
		if(rootText!=null){
			if(rootText.equals("message")){
				node =  new Message();
			}//1
			if(rootText.equals("presence")){
				node =  new Presence();
			}//2
			if(rootText.equals("iq")){
				node =  new Iq();
			}//3
			node.addAttribute("from", from);
			node.addAttribute("to", to);
			if(!TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mUserName)){
				node.addAttribute("fname", LoginManager.getInstance().getLoginInfo().mUserName);
			}
			if(id!=null){
				node.addAttribute("id", id);
			}
		}
		return node;
	}

	/*子节点生成*/
	public XMPPNode createXMPPChildNode(String nodeName){
		XMPPNode node =null;
		if(nodeName!=null){
			if(this.mCache.get(nodeName)!=null){
				try {
					node = (XMPPNode)this.mCache.get(nodeName).newInstance();
					return node;
				} catch (Exception e) {
				} 
			}
			if(nodeName.equals("body")){
				node = new Message_Body();
			}//1
			if(nodeName.equals("text")){
				node = new Message_Body_Text();
			}//2
			if(nodeName.equals("feed")){
				node = new Message_Feed();
			}//3
			if(nodeName.equals("x")){
				node = new X();
			}//4
			if(nodeName.equals("item")){
				node = new Item();
			}//5
			if(nodeName.equals("subject")){
				node = new Subject();
			}//6
			if(nodeName.equals("status")){
				node = new Status();
			}//7
			if(nodeName.equals("audio")){
				node = new Message_Body_Audio();
			}//8
			if(nodeName.equals("error")){
				node = new Error();
			}//9
			if(nodeName.equals("image")){
				node = new Message_Body_Image();
			}//10
			if(nodeName.equals("invite")){
				node = new Invite();
			}//11
			if(nodeName.equals("query")){
				node = new Query();
			}//12
			if(nodeName.equals("actor")){
				node = new Actor();
			}//13
			if(nodeName.equals("destroy")){
				node = new Destroy();
			}//14
			if(nodeName.equals("check")){
				node = new Check();
			}//15
			if(nodeName.equals("contact")){
				node = new Contact();
			}//16
            if(nodeName.equals("action")){
            	node = new Message_Action();
            }//17
            if(nodeName.equals("z")){
            	node = new Z();
            }
            if(nodeName.equals("person")){
            	node = new Person();
            }
            if(nodeName.equals("gid")){
            	node = new Gid();
            }
            if(nodeName.equals("profile")){
            	node = new Profile();
            }
            if(nodeName.equals("domain")){
            	node = new Domain();
            }
            if(nodeName.equals("from_type")){
            	node = new FromType();
            }
            if(nodeName.equals("from_text")){
            	node = new FromText();
            }
            if(nodeName.equals("name")){
            	node = new Name();
            }
            if(nodeName.equals("time")){
            	node = new Message_Time();
            }
            if(nodeName.equals("picture_profile_url")){
            	node = new PictureProfileUrl();
            }
            if(nodeName.equals("type")){
            	node = new Type();
            }
            if(node!=null){
            	try {
					this.mCache.put(nodeName, node.getClass());
				} catch (Exception e) {
					CommonUtil.log("xmppnode", "exception = "+e);
				}
            }
            return node;
		}
		return null;
	}
	
}
