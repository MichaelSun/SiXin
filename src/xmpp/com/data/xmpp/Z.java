package com.data.xmpp;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;
import com.data.xmpp.childs.Message_Body;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobo.yuan
 * Date: 12-8-23
 * Time: 上午11:30
 */
public class Z extends XMPPNode {

    @XMLMapping(Type= XMLType.ATTRIBUTE,Name="xmlns")
    public String mXmlns;
    
    @XMLMapping(Type=XMLType.NODE, Name="person")
    public Person mPersion;
    
    @XMLMapping(Type=XMLType.NODE, Name="body")
    public Message_Body mBody;
    
    @XMLMapping(Type=XMLType.NODE, Name="profile")
    public Profile mProfile;

    @Override
    public String getNodeName() {
        return "z";
    }
}