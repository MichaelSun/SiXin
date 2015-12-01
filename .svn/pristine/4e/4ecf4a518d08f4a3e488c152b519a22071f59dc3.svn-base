package plugin.helpMe.database;

import com.core.database.Column;
import com.core.database.DatabaseColumn;
import com.core.database.DatabaseTypeConstant;

/**
 * @author changsheng.ning
 */
public interface HelpMe_Help_Column extends DatabaseColumn {

	@Column(defineType=DatabaseTypeConstant.INT+" "+DatabaseTypeConstant.PRIMARY)  		
	public final static String _ID = "id";								//主键ID
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String HOST_UID = "host_uid";					//求助人的uid

	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String MAX_HELPERS = "max_helpers";				//最多参与人数，默认5
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String CUR_HELPERS = "current_helpers";			//当前参与人数，默认0
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String TOPIC_TITLE = "title";					//求助标题
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String TOPIC_CONTENT = "content";				//求助内容
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String UNIVERSITY_KEY = "university_key";		//所在学校代码
}
