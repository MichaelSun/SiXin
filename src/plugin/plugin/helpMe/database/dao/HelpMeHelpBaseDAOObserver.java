package plugin.helpMe.database.dao;

import plugin.helpMe.database.HelpMe_Help_Model;

/** 
 * @author changsheng.ning
 * @version 2012-8-22
 */
public interface HelpMeHelpBaseDAOObserver {
	public void onInsert(HelpMe_Help_Model model);
	public void onDelete();
	public void onUpdate();
}
