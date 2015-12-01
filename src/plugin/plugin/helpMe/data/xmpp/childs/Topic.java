package plugin.helpMe.data.xmpp.childs;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;
import com.data.xmpp.XMPPNode;

public class Topic extends XMPPNode {

	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="type")
	public String mType;
	
	@XMLMapping(Type=XMLType.NODE,Name="topic_id")
	public Topic_Body mTopicId;
	
	@XMLMapping(Type=XMLType.NODE,Name="body")
	public Topic_Body mBody;
	
	@XMLMapping(Type=XMLType.NODE,Name="followers")
	public Topic_Followers mFollowers;
	
	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return "topic";
	}

	public String getType(){
		if(mType!=null){
			return mType;
		}
		return "unknow";
	}
	
	public Topic(){};
	
	public Topic(String type) {
		this.addAttribute("type", type);
	}
	
}
