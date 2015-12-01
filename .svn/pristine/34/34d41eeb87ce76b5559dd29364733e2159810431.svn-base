package com.data.xmpp.childs;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;
import com.data.xmpp.XMPPNode;

/**
 * Message_Action
 * @author : xiaoguang.zhang
 * Date: 12-8-13
 * Time: 下午7:44
 * @说明 message下action子节点
 */
public class Message_Action extends XMPPNode {

    @XMLMapping(Type= XMLType.ATTRIBUTE,Name="type")
    public String mType;

    @Override
    public String getNodeName() {
        return "action";
    }

    public String getType(){
        if(mType!=null){
            return mType;
        }
        return "unknow";
    }

    public Message_Action(){}

    public Message_Action(String type){
        this.addAttribute("type", type);
    }

    public Message_Action(Object type){
        this.addAttribute("type", type);
    }
}
