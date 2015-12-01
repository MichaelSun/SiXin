package com.data.xmpp;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;


public class Person extends XMPPNode{
	
    @XMLMapping(Type=XMLType.NODE, Name="gid")
    public Gid mGid;
    
    @XMLMapping(Type=XMLType.NODE, Name="domain")
    public Domain mDomain;
    
    @XMLMapping(Type=XMLType.NODE, Name="from_type")
    public FromType mFromType;
    
    @XMLMapping(Type=XMLType.NODE, Name="from_text")
    public FromText mFromText;
	
    @XMLMapping(Type=XMLType.NODE, Name="name")
    public Name mName;
    
    @XMLMapping(Type=XMLType.NODE, Name="picture_profile_url")
    public PictureProfileUrl mPictureProfileUrl;

    @XMLMapping(Type=XMLType.NODE, Name="type")
    public Type mType;

    
	@Override
	public String getNodeName() {
		return "person";
	}


}
