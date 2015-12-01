package plugin.containers.v0;

import android.os.Bundle;
import android.text.TextUtils;
import com.common.manager.LoginManager;
import com.common.network.DomainUrl;
import com.common.utils.Methods;
import com.core.util.Base64;
import com.core.util.CommonUtil;
import com.data.xmpp.XMPPNodeFactory;
import com.data.xmpp.XMPPNodeRoot;
import com.data.xmpp.Z;
import com.renren.mobile.web.reflect.JSMethod;
import org.json.JSONObject;
import plugin.DBBasedPluginManager;
import plugin.base.Container;
import plugin.base.Plugin;
import plugin.base.PluginManager;
import plugin.plugins.v0.ChatClientResponse;
import plugin.plugins.v0.PluginV0;

/**
 * at 上午11:18, 12-7-19
 *
 * @author afpro
 */
public abstract class ContainerV0 implements Container {


    public static final String TYPE_MESSAGE = "message";
    public static final String TYPE_IQ = "iq";
    public static final String TYPE_PRESENCE = "presence";


    protected PluginManager pluginManager = new DBBasedPluginManager();

    @Override
    public int version() {
        return 0;
}

    @JSMethod("requestStart")
    public abstract void requestStart(String pluginId, Bundle bundle);//请求打开插件

    @JSMethod("refreshSessionId")
    public abstract String refreshSessionId(String pluginId, String type);//刷新sessionId

    public abstract void sendImage(String pluginId, String url, ChatClientResponse chatClientResponse);//发送图片

    public abstract void addFocus(String pluginId, long uid, ChatClientResponse chatClientResponse);//添加特别关注

    public abstract void removeFocus(String pluginId, long uid, ChatClientResponse chatClientResponse);//取消特别关注

    //ui接口
    public abstract void openContactUi(String pluginId);//跳转联系人列表
    public abstract void openDialogUiByUid(String pluginId, long uid);//跳转聊天界面
    public void openDialogUiByIndex(String pluginId, long index){
         openDialogUiByUid(pluginId, Methods.indexToUid(index));
    }

    //数据接口
    @JSMethod("getUserUID")
    public abstract JSONObject getUserUID(String pluginId);
    @JSMethod("getUserInfo")
    public abstract JSONObject getUserInfo(String pluginId);
    public JSONObject getRoasterInfo(String pluginId){
        JSONObject contacts = getRoasterDetailedInfo(pluginId);

        return contacts;
    }
    @JSMethod("getRoasterDetailedInfo")
    public abstract JSONObject getRoasterDetailedInfo(String pluginId);

    //消息接口
//    public abstract void sendMessage2Server(String callerJID, String body);
//    public void sendMessageUsingIndex(String callerJID, long index, String body){
//         sendMessageUsingUID(callerJID, Methods.indexToUid(index), body);
//    }
//    public abstract void sendMessageUsingUID(String callerJID, long uid, String body);
//    public void setMessage2Server(String callerJID, String body){
//          sendMessage2Server(callerJID, body);
//    }
//    public void setMessageUsingIndex(String callerJID, long index, String body){
//          sendMessageUsingIndex(callerJID, index, body);
//    }
//    public void setMessageUsingUID(String callerJID,long uid, String body){
//          sendMessageUsingUID(callerJID, uid, body);
//    }

    public abstract void sendMessage(String callerJID, String body, ChatClientResponse chatClientResponse);

    @JSMethod("sendMessageToServer")
    public void sendMessageToServer(String namespace, String to, String z){
        sendMessage2Server(TYPE_MESSAGE, namespace, to, z);
    }
    @JSMethod("sendIqToServer")
    public void sendIqToServer(String namespace, String to, String z){
        sendMessage2Server(TYPE_IQ, namespace, to, z);
    }
    @JSMethod("sendPresenceToServer")
    public void sendPresenceToServer(String namespace, String to, String z){
        sendMessage2Server(TYPE_PRESENCE, namespace, to, z);
    }

    @JSMethod("sendMessageToUid")
    public void sendMessageToUid(String namespace, long uid, String z){
        sendMessageUsingUID(TYPE_MESSAGE, namespace, uid, z);
    }
    @JSMethod("sendIqToUid")
    public void sendIqToUid(String namespace, long uid, String z){
        sendMessageUsingUID(TYPE_IQ, namespace, uid, z);
    }
    @JSMethod("sendPresenceToUid")
    public void sendPresenceToUid(String namespace, long uid, String z){
        sendMessageUsingUID(TYPE_PRESENCE, namespace, uid, z);
    }

    private void sendMessage2Server(String tag, String namespace, String to, String z){
        String callerJID = pluginManager.getJIDWithNamespace(namespace);
        int pluginId = pluginManager.getPluginIdWithNamespace(namespace);
        Plugin plugin = pluginManager.getPlugin(pluginId, true);
        String messageTo = getMessageTo(callerJID, to);
        if(messageTo == null || plugin == null){
            return;
        }
        if("1".equals(plugin.getPluginInfo().codec()))
            z = new String(Base64.encode(z.getBytes()));
        String xmlString = createXMLString(tag, String.valueOf(LoginManager.getInstance().getLoginInfo().mUserId), messageTo, z, namespace);
        CommonUtil.log("yxb", "xmlString = "+xmlString);
        sendMessage(callerJID, xmlString, (PluginV0)plugin);
    }

    private void sendMessageUsingUID(String tag, String namespace, long uid, String z){
        String callerJID = pluginManager.getJIDWithNamespace(namespace);
        int pluginId = pluginManager.getPluginIdWithNamespace(namespace);
        Plugin plugin = pluginManager.getPlugin(pluginId, true);
        if(callerJID == null || z == null || plugin == null){
            return;
        }
        if("1".equals(plugin.getPluginInfo().codec()))
            z = new String(Base64.encode(z.getBytes()));
        String xmlString = createXMLString(tag, String.valueOf(LoginManager.getInstance().getLoginInfo().mUserId), uid+"@"+DomainUrl.SIXIN_DOMAIN, z, namespace);
        sendMessage(callerJID, xmlString, (PluginV0)plugin);
    }

    private String createXMLString(String tag, String from, String to, String zString, String namespace){
        XMPPNodeRoot root = XMPPNodeFactory.getInstance().obtainRootNode(tag, from + "@" + DomainUrl.SIXIN_DOMAIN, to, String.valueOf(System.currentTimeMillis()));
        if(TYPE_MESSAGE.equals(tag)){
           root.addAttribute("type", "chat");
        }
        Z z = new Z();
        z.addAttribute("xmlns", namespace);
        z.mValue = zString;
        root.addChildNode(z);
        return root.toXMLString();
    }

    private String getMessageTo(String callerJID, String to){
        String[] ndForJID = splitTo(callerJID);
        String[] ndForTo = splitTo(to);
        if(ndForJID == null){
             return null;
        }
        if(ndForTo == null){
             return callerJID;
        }
        if(TextUtils.isEmpty(ndForJID[0])){
             if(ndForTo[1].equals(ndForJID[1])){
                  return to;
             }
        }
        if(callerJID.equals(to)){
             return callerJID;
        }
        return null;
    }

    private String[] splitTo(String nodeAndDomain){
        if(TextUtils.isEmpty(nodeAndDomain)){
               return null;
        }
        String[] res = nodeAndDomain.split("@");
        if(res.length == 2){
             return res;
        }else if(res.length == 1){
             return new String[] {"", res[0]};
        }

        return null;
    }

}
