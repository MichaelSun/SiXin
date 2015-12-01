package plugin.database;

import com.core.orm.ORM;

/**
 * Created with IntelliJ IDEA.
 * User: tiger
 * Date: 9/18/12
 * Time: 2:52 PM
 */
public class Plugin_Message_Model {

    @ORM(mappingColumn = Plugin_Message_Column.PLUGIN_ID)
	public String plugin_id;

	@ORM(mappingColumn = Plugin_Message_Column.NAMESPACE)
	public String namespace;

	@ORM(mappingColumn = Plugin_Message_Column.MESSAGE)
	public String message;

	@ORM(mappingColumn = Plugin_Message_Column.INSERT_TIME)
	public String insert_time;

}
