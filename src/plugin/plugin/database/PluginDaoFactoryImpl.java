package plugin.database;

import java.util.HashMap;
import java.util.Map;
import com.core.database.DAO;
import com.core.database.DAOFactory;

public class PluginDaoFactoryImpl implements DAOFactory {
	
	private static PluginDaoFactoryImpl sInstance = new PluginDaoFactoryImpl();
	private Map<Class,DAO> mDAOTables = new HashMap<Class,DAO>();
	private PluginDaoFactoryImpl(){}
	
	public static PluginDaoFactoryImpl getInstance() {
		return sInstance;
	}

	public<T> T buildDAO(Class<T> clazz){
		return (T)mDAOTables.get(clazz);
	}
	
	@Override
	public void registorDAO(Class clazz, DAO dao) {
		this.mDAOTables.put(clazz,dao);	
	}

}
