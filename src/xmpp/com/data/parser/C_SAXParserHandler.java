package com.data.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.common.messagecenter.base.Utils;
import com.core.util.CommonUtil;
import com.data.xmpp.Iq;
import com.data.xmpp.Message;
import com.data.xmpp.Presence;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.XMPPNodeFactory;
import com.data.xmpp.Z;

/**
 * @version 1.0
 * @date 	2012-06-04
 * @author  dingwei.chen1988@gmail.com
 * @项目说明: 
 * 			1.实现XMPP的数据转义
 * 			2.由XML数据格式到JAVA对象的数据转换
 * 			3.离开移动终端调试代码
 * @XMPP:
 * 			XMPP每个节点有对应的类,继承于{@link com.data.xmpp.XMPPNode}
 * @XML2JAVA:
 * 			XOMapping 通过注解{@link com.data.xml.XMLMapping} 完成
 * @注:
 * 			SAX回调调用次序:
 * 			{@link #startDocument()} 开启文档
 * 			[	
	 * 			{@link #startElement(String, String, String, org.xml.sax.Attributes)} 解析一个节点
	 * 			{@link #characters(char[], int, int)} 解析节点中的值
	 * 			{@link #endElement(String, String, String)} 解析节点结束
 * 			]* 中间过程调用多次
 * 			{@link #endDocument()}  结束文档
 * @see .OnDataParserListener XMPP数据解析回调,主要提供给第三方
 * 			
 * */
public class C_SAXParserHandler extends DefaultHandler{

	/*SAX方式解析XML文件*/
	private SAXParser mParser = null;
	/*SAX提供的解析回调*/
	private static C_SAXParserHandler sHandler = new C_SAXParserHandler();
	private XMPPNode mNodeRoot = null;
	private XMPPNode mTag = null;
	private String mPreNodeName = null;
	private String mOldTmp = null;
	private int mLevel = 0;
	
	public static C_SAXParserHandler getInstance(){
		return sHandler;
	}
	private C_SAXParserHandler(){
		try {
			
			mParser = SAXParserFactory.newInstance().newSAXParser();//只相当于一个代理实际解析通过tagparser解析
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void testParser(){
		try {
			this.parse(new FileInputStream(new File("b.txt")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static interface Transaction extends OnDataParserListener{
		public void commit();
		public void clear();
		public List<Message> getMessages();
	}
	class TransactionImpl implements Transaction{

		OnDataParserListener mTListener = null;
		List<Message> mM = new ArrayList<Message>();
		List<Iq> mI = new ArrayList<Iq>();
		List<Presence> mP = new ArrayList<Presence>();
		
		
		public TransactionImpl(OnDataParserListener listener){
			this.mTListener = listener;
			if(this.mTListener==null){
				throw new RuntimeException("OnDataParserListener can not be null");
			}
		}
		@Override
		public void commit() {
			mTListener.onParserMessageNode(mM);
			mTListener.onParserIqNode(mI);
			mTListener.onParserPresenceNode(mP);
		}
		@Override
		public void onParserMessageNode(List<Message> list) {
			mM.addAll(list);
		}
		@Override
		public void onParserPresenceNode(List<Presence> list) {
			mP.addAll(list);
		}
		@Override
		public void onParserIqNode(List<Iq> list) {
			mI.addAll(list);
		}
		@Override
		public void onParserError(String errorMessage) {}
		@Override
		public void clear() {
			// TODO Auto-generated method stub
			mM.clear();
			mP.clear();
			mI.clear();
		}
		@Override
		public List<Message> getMessages() {
			// TODO Auto-generated method stub
			return this.mM;
		}
		
	}
	Transaction mTransactionLock = null;
	public Transaction beginTransaction(OnDataParserListener listener){
		if(mTransactionLock==null){
			mTransactionLock = new TransactionImpl(listener);
		}
		mTransactionLock.clear();
		return mTransactionLock;
	}
	
	Transaction mTransaction = null;
	public synchronized void parse(String text,Transaction transaction){
		if(text == null || text.trim().length() == 0){
			return;
		}
		mTransaction = transaction;
		text = text.replaceAll("stream:stream", "stream");
		mParseXML = text;
		ByteArrayInputStream bais = new ByteArrayInputStream(text.getBytes());
		C_SAXParserHandler.getInstance().parse(bais);//转义成为本进程数据
		try {
			bais.close();
		} catch (Exception e) {}
	}
	
	public synchronized void parse(String text){
		this.parse(text, null);
	}
	
	public void parse(InputStream inputStream){
		try {
			InputSource is = new InputSource(inputStream);
			is.setEncoding("utf-8");
			mParser.parse(is, this);
			
		} catch (Exception e) {
			onParserError(e);
		}
	}
	
	
	@Override
	public void startDocument() throws SAXException {
		mNodeRoot = null;
		mTag = null;
		mPreNodeName = null;
		mOldTmp = null;
		mLevel = 0;
		XMPPNodeFactory.getInstance().clear();
	};
	private String mParseXML = null;
	@Override
	public void endDocument() throws SAXException {
		this.onParserMessageNode(XMPPNodeFactory.getInstance().mMessages);
		this.onParserIqNode(XMPPNodeFactory.getInstance().mIqs);
		this.onParserPresenceNode(XMPPNodeFactory.getInstance().mPresences);
	};
	
	@Override
	public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
		++mLevel;
		mOldTmp = null;
		if(mNodeRoot==null){
			mNodeRoot = XMPPNodeFactory.getInstance().createXMPPRootNode(qName);
			if(mNodeRoot==null){
				return ;
			}
			mTag = mNodeRoot;
			mTag.processAttribute(attributes);
		}else{
			long time =System.currentTimeMillis();
			XMPPNode node = XMPPNodeFactory.getInstance().createXMPPChildNode(qName);
			if(node==null){
				return;
			}
			if( "z".equals(qName) && node instanceof Z){
				Z z = (Z) node;
				z.mXmlns = uri;
			}
			node.processAttribute(attributes);
			node.setParent(mTag);
			mTag.addChildNode(node);
			this.mTag = node;
		}
		mPreNodeName = qName;
	};
	
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		mOldTmp = null;
		--mLevel;
		if(mTag!=null&&mTag.getNodeName().equals(qName)){
			mTag = mTag.mParent;
		}
		if(mNodeRoot!=null&&mNodeRoot.getNodeName().equals(qName)){
			mNodeRoot = null;
		}
	};
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String str = new String(ch,start,length);
		if(mPreNodeName!=null&&mTag!=null && mPreNodeName.equals(mTag.getNodeName())){
			if(mOldTmp!=null){
				mOldTmp+=str;
			}else{
				mOldTmp= str;
			}
			if(mOldTmp!=null&&mOldTmp.length()>0){
				mTag.mValue = mOldTmp;
			}
		}
	};
	
	@Override
	public void error(org.xml.sax.SAXParseException e) throws SAXException {
	};
	
	/*test by pc*/
	public static void main(String...args){
		new C_SAXParserHandler().testParser();
	}
	public static interface OnDataParserListener{
		public void onParserMessageNode(List<Message> list);
		public void onParserPresenceNode(List<Presence> list);
		public void onParserIqNode(List<Iq> list);
		public void onParserError(String errorMessage);
	}
	OnDataParserListener mListener = null;
	public void setOnDataParserListener (OnDataParserListener listener){
		mListener = listener;
	}
	public void onParserMessageNode(List<Message> list){
		if(this.mTransaction!=null){
			this.mTransaction.onParserMessageNode(list);
			return;
		}
		if(mListener!=null&&list!=null&& list.size()>0){
			mListener.onParserMessageNode(list);
		}
	}
	public void onParserPresenceNode(List<Presence> list){
		if(this.mTransaction!=null){
			this.mTransaction.onParserPresenceNode(list);
			return;
		}
		if(mListener!=null&&list!=null&& list.size()>0){
			mListener.onParserPresenceNode(list);
		}
	}
	public void onParserIqNode(List<Iq> list){
		if(this.mTransaction!=null){
			this.mTransaction.onParserIqNode(list);
			return;
		}
		if(mListener!=null&&list!=null&& list.size()>0){
			mListener.onParserIqNode(list);
		}
	}
	public void onParserError(Exception e){
		if(mListener!=null){
			mListener.onParserError(e+"");
		}
	}
}
