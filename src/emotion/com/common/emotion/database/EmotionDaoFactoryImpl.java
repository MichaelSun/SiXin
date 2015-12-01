package com.common.emotion.database;

import java.util.HashMap;
import java.util.Map;
import com.core.database.DAO;
import com.core.database.DAOFactory;

public class EmotionDaoFactoryImpl implements DAOFactory {

	private static EmotionDaoFactoryImpl sInstance = new EmotionDaoFactoryImpl();
	private Map<String,DAO> mDAOs = new HashMap<String,DAO>();
	private Map<Class,DAO> mDAOTables = new HashMap<Class,DAO>();
	private EmotionDaoFactoryImpl(){}
	
	public static EmotionDaoFactoryImpl getInstance(){
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
