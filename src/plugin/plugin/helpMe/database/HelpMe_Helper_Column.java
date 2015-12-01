package plugin.helpMe.database;

import com.core.database.Column;
import com.core.database.DatabaseColumn;
import com.core.database.DatabaseTypeConstant;

/**
 * @author changsheng.ning
 */
public interface HelpMe_Helper_Column extends DatabaseColumn {
	
	@Column(defineType=DatabaseTypeConstant.INT+" "+DatabaseTypeConstant.PRIMARY)  		
	public final static String _ID = "id";									//主键ID
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String HELPER_UID = "uid";							//用户uid,此id是师兄id还是师妹id？
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String HELPED_TOPIC_ID = "help_id";					//求助帖的id
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String HELPED_STATUS = "status";					//援助状态，1为已援助、0为被取消援助
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String HELPED_TOPIC_CREATE_TIME = "create_time";	//创建时间
	
}
