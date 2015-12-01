package com.common.emotion.dao;

import java.util.List;

import android.content.ContentValues;

import com.common.emotion.database.EmotionTheme_Column;
import com.common.emotion.model.EmotionThemeModel;
import com.common.emotion.sql.Query_EmotionTheme;
import com.common.emotion.sql.Query_EmotionThemeList;
import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.Query;
import com.core.orm.ORMUtil;
/**
 * 主题表DB操作DAO
 * @author jiaxia
 *
 */
public class EmotionThemeDao extends BaseDAO {

	protected Query query_theme = new Query_EmotionTheme(this);
	protected Query query_themelist = new Query_EmotionThemeList(this);
	
	public EmotionThemeDao(BaseDBTable table) {
		super(table);
		
	}
/**
 * 插入一列数据(一个包里的表情主题)
 * @param themeList
 * @checked
 */
	protected void insertThemeList(List<EmotionThemeModel> themeList){
		if(themeList == null)
			return ;
		this.beginTransaction();
		for(EmotionThemeModel model:themeList){
			if(queryThemeByPidTid(model.package_id, model.theme_id)!=null){ //唯一性处理
				continue;
			}
			ContentValues value = new ContentValues();
			ORMUtil.getInstance().ormInsert(EmotionThemeModel.class, model, value);
			mInsert.insert(value);
		}
		this.commit();
	}
	/**
	 * 插入一个主题数据(理论上用不到)
	 * @param theme
	 */
	protected void insertTheme(EmotionThemeModel theme){
		if(theme == null)
			return ;
		
			ContentValues value = new ContentValues();
			ORMUtil.getInstance().ormInsert(EmotionThemeModel.class, theme, value);
			mInsert.insert(value);
	}
	/**
	 *  先查询数据库中是否有此主题，然后再插入
	 * @param theme
	 * @param package_id
	 */
	protected void insertTheme(EmotionThemeModel theme,long package_id){
		if(theme == null)
			return;
		if(package_id != -1){ //查询DB
			EmotionThemeModel m = queryThemeByPidTid(package_id, theme.theme_id);
			if(m == null){ //当数据库没有这条信息，则插入。
				insertTheme(theme);
			}
		}
		else{ //不查询DB
			insertTheme(theme);
		}
	}
/**
 * 根据表情包ID查询该表情包下的所有主题信息
 * @param package_id
 * @return
 * @checked
 */
	protected List<EmotionThemeModel> queryByPackageId(long package_id){
		String whereString = EmotionTheme_Column.PACKAGE_ID +" = "+package_id;
		return (List<EmotionThemeModel>)query_themelist.query(null, whereString, null, null, null);
	}
	/**
	 * 根据包ID和主题ID查询单个主题
	 * @param package_id
	 * @param theme_id
	 * @return
	 */
	protected EmotionThemeModel queryThemeByPidTid(long package_id,String theme_id){
		String whereString = EmotionTheme_Column.PACKAGE_ID +" = "+package_id +" and "
	                            +EmotionTheme_Column.THEME_ID +" = '"+theme_id+"'";
		
		return query_theme.query(null, whereString, null, null, null, null);
	}
}
