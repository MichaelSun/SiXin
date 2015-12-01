package plugin;

import java.util.HashMap;

import com.common.R;


public class PluginHelper {
	
	public static int ICON_SMALL = 1;
	public static int ICON_BIG =2;

	public interface FUNCTION_BUTTON_TYPE {
		String attentionPluginID = "2"; //人人特别关注插件
		
	}
	private final static HashMap<String, Integer> sType2SmallDrawable = new HashMap<String, Integer>();
	static{
		sType2SmallDrawable.put(FUNCTION_BUTTON_TYPE.attentionPluginID, 				R.drawable.cy_renren_icon);
	
	}
	
	private final static HashMap<String, Integer> sType2BigDrawable = new HashMap<String, Integer>();
	static{
		sType2BigDrawable.put(FUNCTION_BUTTON_TYPE.attentionPluginID, 				R.drawable.attention_renren_icon);
	
	}
	/**
	 * 返回插件Icon的方法
	 * @param id,插件的plugin_id
	 * @param IconType, 需要的Icon是大图标还是小图标
	 * @return Icon图片资源的ID
	 */
	public static int getIcon(String id,int IconType){
		
		if(IconType == ICON_SMALL){
			if(sType2SmallDrawable.containsKey(id)){
				return sType2SmallDrawable.get(id);
			}else{
				return R.drawable.cy_renren_icon;
			}
		}else if(IconType == ICON_BIG){
			if(sType2BigDrawable.containsKey(id)){
				return sType2BigDrawable.get(id);
			}else{
				return R.drawable.attention_renren_icon;
			}
		}
		
		return 0;
	}
	
}
