package com.renren.mobile.chat.common;

import com.common.statistics.Htf;
import com.renren.mobile.chat.base.inter.NEWSFEED_TYPE;
import com.renren.mobile.chat.base.model.ChatBaseItem;

public class AwokeUtil {

	/**
	 * 获取push唤醒方式
	 * @param type
	 * @return String 
	 */
	public static String getAwokeWay(int type){

		switch(type){
		case ChatBaseItem.MESSAGE_TYPE.TEXT:
			return Htf.TEXT_MESSAGE;
		case ChatBaseItem.MESSAGE_TYPE.VOICE:
			return Htf.VOICE_MESSAGE;
//		case ChatBaseItem.MESSAGE_TYPE.STATUS:
//			awokeWay = awokeWay.append(Htf.STATUE_FEED);
//			break;
		case ChatBaseItem.MESSAGE_TYPE.IMAGE:
			return Htf.IMAGE_MESSAGE;
//		case ChatBaseItem.MESSAGE_TYPE.LBS:
//			awokeWay = awokeWay.append(Htf.LOCATION_FEED);
//			break;
		case ChatBaseItem.MESSAGE_TYPE.FLASH:
			return Htf.EXPRESSION_MESSAGE;
		case NEWSFEED_TYPE.FEED_STATUS_UPDATE:
			//return  Htf.STATUE_FEED;
		case NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_ONE:
			//return  Htf.SINGLE_PHOTO_FEED;
		case NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_MORE:
			//return Htf.MULTI_PHOTO_FEED;
			return Htf.FEED_REMIND;
		default:
			return null;
		}
	}
}
