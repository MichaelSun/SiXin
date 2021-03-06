package plugin.helpMe.data.xmpp.childs;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;
import com.data.xmpp.XMPPNode;

public class Topic_Body_Image extends XMPPNode {

//	 <image mine_type='image/jpg' 
//		 tiny='ttp://ip:port/xxx.jpg' 
//			 main='ttp://ip:port/xxx.jpg' 
//				 large='http://ip:port/xxx.jpg'
//					 filename='name1.jpg'/>
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="mine_type")
	public String mMineType;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="main")
	public String mMain;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="large")
	public String mLarge;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="tiny")
	public String mTiny;
	
	
	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return "image";
	}

}
