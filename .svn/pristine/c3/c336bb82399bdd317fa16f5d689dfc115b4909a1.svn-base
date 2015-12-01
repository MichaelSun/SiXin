package plugin.helpMe.database;

import java.util.HashMap;
import java.util.Map;
import com.core.database.DAO;
import com.core.database.DAOFactory;

public class HelpMeDaoFactoryImpl implements DAOFactory {

	private static HelpMeDaoFactoryImpl sInstance = new HelpMeDaoFactoryImpl();
	private Map<Class,DAO> mDAOTables = new HashMap<Class,DAO>();
	private HelpMeDaoFactoryImpl(){};
	
	public static HelpMeDaoFactoryImpl getInstance() {
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
