package plugin.database;

import android.provider.BaseColumns;
import com.core.database.Column;
import com.core.database.DatabaseTypeConstant;

/**
 * Created with IntelliJ IDEA.
 * User: tiger
 * Date: 9/18/12
 * Time: 2:51 PM
 */
public class Plugin_Message_Column implements BaseColumns {
    @Column(defineType= DatabaseTypeConstant.INT)
	public final static String PLUGIN_ID = "plugin_id";

	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String NAMESPACE = "namespace";

	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String MESSAGE = "message";

    @Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String INSERT_TIME = "insert_time";
}
