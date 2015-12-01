package plugin.helpMe.database;

import com.core.database.Column;
import com.core.database.DatabaseColumn;
import com.core.database.DatabaseTypeConstant;

/**
 * @author changsheng.ning
 */
public interface HelpMe_University_Column extends DatabaseColumn {
	
	@Column(defineType=DatabaseTypeConstant.INT+" "+DatabaseTypeConstant.PRIMARY)  		
	public final static String _ID = "id";								//主键ID
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String UNIVERSITY_KEY = "key";					//学校代码
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String UNIVERSITY_NAME = "name";				//学校名称
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String UNIVERSITY_CREAT_TIME = "create_time";	//创建时间,学校创建时间？？？
}
