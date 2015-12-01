package plugin.helpMe.data.xmpp;

import java.util.List;
import plugin.helpMe.data.xmpp.childs.Topic;
import plugin.helpMe.data.xmpp.childs.Topic_Followers;
import plugin.helpMe.data.xmpp.childs.Topic_Followers_gid;
import plugin.helpMe.data.xmpp.childs.Topic_Topic_Id;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.childs.Message_Body;
import com.data.xmpp.childs.Message_Body_Audio;
import com.data.xmpp.childs.Message_Body_Image;
import com.data.xmpp.childs.Message_Body_Text;

public class XMPPMessage {
	
	/**
	 * @param title 帖子题目
	 * @param imgType 帖子中图片的类型
	 * @param imgTinyUrl 帖子中图片小图的URL地址
	 * @param imgMainUrl 帖子中图片中图的URL地址
	 * @param imgLargeUrl 帖子中图片大图的URL地址
	 * @param content 帖子的文字内容
	 * @param audioType 帖子中的音频文件类型
	 * @param audioUrl 帖子中的音频文件URL地址
	 * @return 帖子的body节点
	 */
	private static XMPPNode getMessageBodyNode(String title, String imgType, String imgTinyUrl, String imgMainUrl,
			String imgLargeUrl, String content, String audioType, String audioUrl) {
		
		XMPPNode bodyNode = new Message_Body();
		
		XMPPNode titleTextNode = new Message_Body_Text();
		if (title != null) {
			titleTextNode.mValue = title;
		} else {
			titleTextNode.mValue = "师妹求助ING";
		}
		bodyNode.addChildNode(titleTextNode);
		
		XMPPNode imageNode = new Message_Body_Image();
		if (imgType != null) {
			imageNode.addAttribute("mine_type", imgType);
		} else {
			imageNode.addAttribute("mine_type", "image/jpg");
		}
		if (imgTinyUrl != null) {
			imageNode.addAttribute("tiny", imgTinyUrl);
		}
		if (imgMainUrl != null) {
			imageNode.addAttribute("main", imgMainUrl);
		}
		if (imgLargeUrl != null) {
			imageNode.addAttribute("large", imgLargeUrl);
		}
		bodyNode.addChildNode(imageNode);
		
		XMPPNode contentTextNode = new Message_Body_Text();
		if (content != null) {
			contentTextNode.mValue = content;
		}
		bodyNode.addChildNode(contentTextNode);
		
		XMPPNode audioNode = new Message_Body_Audio();
		if (audioType != null) {
			audioNode.addAttribute("mine_type", audioType);
		} else {
			audioNode.addAttribute("mine_type", "audio/mp3");
		}
		if (audioUrl != null) {
			audioNode.addAttribute("url", audioUrl);
		}
		bodyNode.addChildNode(audioNode);

		return bodyNode;
		
	}
	
	/**
	 * @param title 帖子题目
	 * @param imgType 帖子中图片的类型
	 * @param imgTinyUrl 帖子中图片小图的URL地址
	 * @param imgMainUrl 帖子中图片中图的URL地址
	 * @param imgLargeUrl 帖子中图片大图的URL地址
	 * @param content 帖子的文字内容
	 * @param audioType 帖子中的音频文件类型
	 * @param audioUrl 帖子中的音频文件URL地址
	 * @return 待发布的帖子的XMPP消息的XML字符串
	 */
	public static String getPublishMessage(String title, String imgType, String imgTinyUrl, String imgMainUrl,
			String imgLargeUrl, String content, String audioType, String audioUrl) {
		
		XMPPNode topicNode = new Topic();
		topicNode.addAttribute("type", "publish");
		XMPPNode bodyNode = XMPPMessage.getMessageBodyNode(title, imgType, imgTinyUrl, imgMainUrl, imgLargeUrl, content, audioType, audioUrl);
		topicNode.addChildNode(bodyNode);
		
		return topicNode.toXMLString();
		
	}
	
	/**
	 * @param title 帖子题目
	 * @param imgType 帖子中图片的类型
	 * @param imgTinyUrl 帖子中图片小图的URL地址
	 * @param imgMainUrl 帖子中图片中图的URL地址
	 * @param imgLargeUrl 帖子中图片大图的URL地址
	 * @param content 帖子的文字内容
	 * @param audioType 帖子中的音频文件类型
	 * @param audioUrl 帖子中的音频文件URL地址
	 * @return 待更新的帖子的XMPP消息的XML字符串
	 */
	public static String getUpdateMessage(String topicId, String title, String imgType, String imgTinyUrl, String imgMainUrl,
			String imgLargeUrl, String content, String audioType, String audioUrl) {
		
		XMPPNode topicNode = new Topic();
		topicNode.addAttribute("type", "update");
		
		XMPPNode topicIdNode = new Topic_Topic_Id();
		topicIdNode.mValue = topicId;
		topicNode.addChildNode(topicIdNode);
		
		XMPPNode bodyNode = XMPPMessage.getMessageBodyNode(title, imgType, imgTinyUrl, imgMainUrl, imgLargeUrl, content, audioType, audioUrl);
		topicNode.addChildNode(bodyNode);
		
		return topicNode.toXMLString();
		
	}
	
	/**
	 * @param topicId 要删除的帖子的topic_id
	 * @return 删除指定id的帖子所需XMPP的XML字符串
	 */
	public static String getRemoveMessage(String topicId) {
		
		XMPPNode topicNode = new Topic();
		topicNode.addAttribute("type", "remove");
		
		XMPPNode topicIdNode = new Topic_Topic_Id();
		topicIdNode.mValue = topicId;
		topicNode.addChildNode(topicIdNode);
		
		return topicNode.toXMLString();
		
	}
	
	/**
	 * @param topicId 要踢人的帖子的topic_id
	 * @param gidList 要踢除的人的List
	 * @return 踢除指定id的帖子中特定人的所需XMPP的XML字符串
	 */
	public static String getKickMessage(String topicId, List<String> gidList) {
		XMPPNode topicNode = new Topic();
		topicNode.addAttribute("type", "kick");
		
		XMPPNode topicIdNode = new Topic_Topic_Id();
		topicIdNode.mValue = topicId;
		topicNode.addChildNode(topicIdNode);
		
		XMPPNode followersNode = new Topic_Followers();
		for (String gid : gidList) {
			XMPPNode gidNode = new Topic_Followers_gid();
			gidNode.mValue = gid;
			followersNode.addChildNode(gidNode);
		}
		topicNode.addChildNode(followersNode);
		
		return topicNode.toXMLString();
		
	}
	
	/**
	 * @param topicId 要加入的帖子的topic_id
	 * @return 加入指定id的帖子所需XMPP的XML字符串
	 */
	public static String getJoinMessage(String topicId) {
		
		XMPPNode topicNode = new Topic();
		topicNode.addAttribute("type", "join");
		
		XMPPNode topicIdNode = new Topic_Topic_Id();
		topicIdNode.mValue = topicId;
		topicNode.addChildNode(topicIdNode);
		
		return topicNode.toXMLString();
		
	}
}
















