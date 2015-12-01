package com.common.emotion.emotion;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import com.common.emotion.dao.EmotionDaoManager;
import com.common.emotion.model.EmotionBaseModel;



/***
 * @author zxc
 * 常用表情
 * 
 */
public class EmotionRank {
	private LinkedList<RankNode> queueList;//存储的是当前package下的排行榜
	int size;
	
	private boolean isdatachanged = false;
	private boolean iscodechanged = false;
	private boolean isthemeidchanged = false;
	/***
	 * 
	 * @param size 缓存的初始大小
	 */
	public EmotionRank(int size) {
		this.size = size;
//		queue= new ConcurrentLinkedQueue<Emotion>();
		queueList = new LinkedList<RankNode>();
//		rankList = new LinkedList<EmotionRank.RankNode>();
	}
	
	
	/**
	 * 批量初始化表情缓存
	 * @param list
	 */
	public void InitEmotionCache(LinkedList<RankNode> list){
		queueList =  (LinkedList<RankNode>) list.clone();
	}
	/****
	 * 进行单独的添加
	 * @param path 路径
	 * @param clicknum 点击次数
	 */
	public void addToRank(final String path, final String code,final String themeId,final int clicknum){
				RankNode rankNode = new RankNode(path,code,clicknum,themeId);
				
				int local_clicknum = check(rankNode);
				if(local_clicknum != -1){
					rankNode.counter = local_clicknum + clicknum;
//					queueList.add(rankNode);
				}
				add(rankNode);
				isdatachanged = true;
				iscodechanged = true;
				isthemeidchanged = true;
				
	}
	
	/**
	 * 添加到queueList
	 * @param ranknode
	 * @return
	 */
	private boolean add(RankNode ranknode){
		if(queueList.size()==0){
			queueList.add(ranknode);
			return true;
		}
		if(ranknode.counter > queueList.getFirst().counter){
			queueList.addFirst(ranknode);
			return true;
		}
		for(ListIterator<RankNode> iter = queueList.listIterator(); iter.hasNext();){
			int c = iter.next().counter;
//			SystemUtil.log(TAG, ranknode.counter + " : " + c);
			if(ranknode.counter > c){
				iter.add(ranknode);
				return true;
			}
		}
		queueList.add(ranknode);
		return true;
	}
	
	/***
	 * 获取 topszie 的表情的主题ID;
	 * @param topSize
	 * @return
	 */
	public String[] getTopEmotionThemeId(int topSize){
		if(!isthemeidchanged){
			return null;
		}
		String[] str = null;
		if(queueList!=null){
			if(topSize<=queueList.size()){
				str= new String[topSize];
				int i =0;
				for(Iterator<RankNode> iter = queueList.iterator(); iter.hasNext();){
					if(i<topSize){
						str[i++]= iter.next().theme_id;
					}else{
						iter.next();
					}
				}
			}else{
				str = new String[queueList.size()];
				int i =0;
				for(Iterator<RankNode> iter = queueList.iterator();iter.hasNext();){
					str[i++] = iter.next().theme_id;
				}
			}
			isdatachanged = false;
			return str;
		}
		return null;
	}
	
	/***
	 * 获取点击数 前 topSize的表情路径
	 * @param topSize 
	 * @return String数组
	 */
	public String[] getTopEmotionPath(int topSize){
		if(!isdatachanged){
			return null;
		}
		String[] str = null;
		if(queueList!=null){
			if(topSize<=queueList.size()){
				str= new String[topSize];
				int i =0;
				for(Iterator<RankNode> iter = queueList.iterator(); iter.hasNext();){
					if(i<topSize){
						str[i++]= iter.next().path;
					}else{
						iter.next();
					}
				}
			}else{
				str = new String[queueList.size()];
				int i =0;
				for(Iterator<RankNode> iter = queueList.iterator();iter.hasNext();){
					str[i++] = iter.next().path;
				}
			}
			isdatachanged = false;
//			SystemUtil.log(TAG, "getTopEmotionPath" + ( System.currentTimeMillis()-start));
			return str;
		}
		return null;
	}
	/***
	 * 获取点击数前topSize的表情路径
	 * @param topSize
	 * @return String[]
	 */
	public String[] getTopEmotionCode(int topSize){

		if(!iscodechanged){
			return null;
		}
		String[] str = null;
		if(queueList!=null){
			if(topSize<=queueList.size()){
				str= new String[topSize];
				int i =0;
				for(Iterator<RankNode> iter = queueList.iterator(); iter.hasNext();){
					if(i<topSize){
						str[i++]= iter.next().code;
					}else{
						break;
					}
				}
			}else{
				str = new String[queueList.size()];
				int i =0;
				for(Iterator<RankNode> iter = queueList.iterator();iter.hasNext();){
					str[i++] = iter.next().code;
				}
			}
			iscodechanged = false;
//			SystemUtil.log(TAG, "getTopEmotionCode" + ( System.currentTimeMillis()-start));

			return str;
		}
		return null;
	}
	/***
	 * 获取点击数 后 lastSize的表情路径
	 * @param lastSize 
	 * @return String数组
	 */
	public String[] getLastEmotionPath(int lastSize){
		if(!isdatachanged){
			return null;
		}
		if(queueList != null){
			String[] str = null;
			if(lastSize<=queueList.size()){
				str = new String[lastSize];
				int i =0;
				int j = lastSize;
				for(Iterator<RankNode> iter = queueList.iterator();iter.hasNext();++i){
					if(i >= queueList.size() - lastSize){
						str[--j] = iter.next().path; 
					}else{
						iter.next();
					}
				}
			}else if(queueList.size()>=0){
				str = new String[queueList.size()];
				int s = queueList.size();
				for(int i = queueList.size() -1; i >=0; --i){
					str[s-i-1] = queueList.get(i).path;
				}
			}
			isdatachanged = false;
			return str;
		}else{
			return null;
		}
	}
	/***
	 * 获取点击数前LastSize的表情路径
	 * @param LastSize
	 * @return String[]
	 */
	public String[] getLastEmotionCode(int lastSize){
		if(!iscodechanged){
			return null;
		}
		if(queueList != null){
			String[] str = null;
			if(lastSize<=queueList.size()){
				str = new String[lastSize];
				int i =0;
				int j = lastSize;
				for(Iterator<RankNode> iter = queueList.iterator();iter.hasNext();++i){
					if(i >= queueList.size() - lastSize){
						str[--j] = iter.next().code; 
					}else{
						iter.next();
					}
				}
			}else if(queueList.size()>=0){
				str = new String[queueList.size()];
				int s = queueList.size();
				for(int i = queueList.size() -1; i >=0; --i){
					str[s-i-1] = queueList.get(i).code;
				}
			}
			iscodechanged = false;

			return str;
		}else{ 
			return null;
		}
	}
	/**
	 * 更新表情的点击次数
	 * @param path
	 * @param index
	 * @return
	 */
	private  boolean updateRankNode(String path,int index){
		for(int i =0; i < queueList.size();++i){
			if(queueList.get(i).path.equals(path)){
				RankNode nodetmp = new RankNode();
				nodetmp.counter += index;
				nodetmp.path = path;
				queueList.set(i, nodetmp);
				return true;
			}
		}
		return false;
	}

	/***
	 * 
	 * @param rankNode
	 * @return  当没有此表情的时候返回-1， 有的话则返回此表情的点击次数
	 */
	public int check(RankNode rankNode){
		if(queueList == null){
			return -1;
		}
		if(queueList.size() >0){
			if(queueList.getFirst().path.equals(rankNode.path)){
				int ret_clickNum = queueList.getFirst().counter;
//				SystemUtil.log(TAG, "remove first ");
				queueList.removeFirst();
				return ret_clickNum;
			}
		}
		for(Iterator<RankNode> iter = queueList.iterator(); iter.hasNext();){
			RankNode node = iter.next();
			int ret_clickNum = 0;
			if(rankNode.path.equals(node.path)){
//				SystemUtil.log(TAG,"remove " + node.path + " counter " + node.counter);
				ret_clickNum = node.counter;
				
				iter.remove();
				return ret_clickNum;
			}
		}
		return -1;
	}
	/***
	 * 回写到数据库，一定要执行啊
	 */
	public void setDataToDB() {
		for(RankNode node:queueList){
			EmotionBaseModel model = new EmotionBaseModel();
			model.emotion_path = node.path;
			model.emotion_clicknum = node.counter;
			EmotionDaoManager.getInstance().update_Emotion(model);
		}
	}
	/***
	 * 设置为默认模式
	 */
	public void settobeDefault() {
		isdatachanged = true;
		iscodechanged = true;
		isthemeidchanged = true;
	}
	
	public int size(){
		if(queueList!=null){
			return queueList.size();
		}
		return -1;
	}
	
	/**
	 * @author zxc
	 *	排行榜上表情节点
	 */
	public class RankNode{
		public RankNode(String path ,String code, int counter, String theme_id) {
			this.counter = counter;
			this.path = path;
			this.code = code;
			this.theme_id = theme_id;
		}
		public RankNode() {
			// TODO Auto-generated constructor stub
		}
		public int counter;
		public String theme_id;
		public String path;
		public String code;
	}
	
}
