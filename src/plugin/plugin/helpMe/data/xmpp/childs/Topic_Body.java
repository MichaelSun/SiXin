package plugin.helpMe.data.xmpp.childs;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;
import com.data.xmpp.XMPPNode;

public class Topic_Body extends XMPPNode{
	
	@XMLMapping(Type=XMLType.NODE,Name="text")
	public Topic_Body_Text mTextNode;
	
	@XMLMapping(Type=XMLType.NODE,Name="audio")
	public Topic_Body_Audio mAudioNode;
	
	@XMLMapping(Type=XMLType.NODE,Name="image")
	public Topic_Body_Image mImageNode;
	
	@Override
	public String getNodeName() {
		return "body";
	}
	
}
