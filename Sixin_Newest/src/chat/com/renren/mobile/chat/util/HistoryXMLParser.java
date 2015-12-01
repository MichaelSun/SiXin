package com.renren.mobile.chat.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.data.xmpp.Iq;
import com.data.xmpp.Message;
import com.data.xmpp.Presence;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.XMPPNodeFactory;

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
public class HistoryXMLParser extends DefaultHandler{

	/*SAX方式解析XML文件*/
	private SAXParser mParser = null;
	/*SAX提供的解析回调*/
	private static HistoryXMLParser sHandler = new HistoryXMLParser();
	private XMPPNode mNodeRoot = null;
	private XMPPNode mTag = null;
	private String mPreNodeName = null;
	private String mOldTmp = null;
	
	OnHistoryParserListener mListener = null;
	
	public static HistoryXMLParser getInstance(){
		return sHandler;
	}
	private HistoryXMLParser(){
		try {
			
			mParser = SAXParserFactory.newInstance().newSAXParser();//只相当于一个代理实际解析通过tagparser解析
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void testParser(){
//		int k = 10;
		try {
//			while(k-->0){
				this.parse(new FileInputStream(new File("f:/b.txt")));
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void parse(String text, OnHistoryParserListener listener){
		mListener = listener;
		if(text == null || text.trim().length() == 0){
			return;
		}
		text = text.replaceAll("stream:stream", "stream");
		mParseXML = text;
		ByteArrayInputStream bais = new ByteArrayInputStream(text.getBytes());
		HistoryXMLParser.getInstance().parse(bais);//转义成为本进程数据
		try {
			bais.close();
		} catch (Exception e) {}
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
		XMPPNodeFactory.getInstance().clear();
	};
	private String mParseXML = null;
	@Override
	public void endDocument() throws SAXException {
		this.onParserMessageNode(XMPPNodeFactory.getInstance().mMessages);
//		this.onParserIqNode(XMPPNodeFactory.getInstance().mIqs);
//		this.onParserPresenceNode(XMPPNodeFactory.getInstance().mPresences);
	};
	
	
	@Override
	public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
		mOldTmp = null;
		if(mNodeRoot==null){
			mNodeRoot = XMPPNodeFactory.getInstance().createXMPPRootNode(qName);
			if(mNodeRoot==null){
				return ;
			}
			mTag = mNodeRoot;
			mTag.processAttribute(attributes);
		}else{
			XMPPNode node = XMPPNodeFactory.getInstance().createXMPPChildNode(qName);
			if(node!=null){
				node.setParent(mTag);
				this.mTag = node;
				mTag.processAttribute(attributes);
			}
		}
		mPreNodeName = qName;
	};
	
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		mOldTmp = null;
		if(mTag!=null&&mTag.getNodeName().equals(qName)){
			mTag = mTag.mParent;
		}
		if(mNodeRoot!=null&&mNodeRoot.getNodeName().equals(qName)){
			mNodeRoot = null;
		}
	};
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(mPreNodeName!=null&&mTag!=null && mPreNodeName.equals(mTag.getNodeName())){
			if(mOldTmp!=null){
				mOldTmp+=new String(ch,start,length).trim();
			}else{
				mOldTmp= new String(ch,start,length).trim();
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
		new HistoryXMLParser().testParser();
	}
	public static interface OnHistoryParserListener{
		public void onParserMessageNode(List<Message> list);
		public void onParserError(String errorMessage);
	}
//	OnHistoryParserListener mListener = null;
//	public void setOnDataParserListener (OnHistoryParserListener listener){
//		mListener = listener;
//	}
	public void onParserMessageNode(List<Message> list){
		if(mListener!=null&&list!=null&& list.size()>0){
			mListener.onParserMessageNode(list);
		}
	}
//	public void onParserPresenceNode(List<Presence> list){
//		System.err.println("callback Presence:"+list.size());
//		if(mListener!=null&&list!=null&& list.size()>0){
//			mListener.onParserPresenceNode(list);
//		}
//	}
//	public void onParserIqNode(List<Iq> list){
//		System.err.println("callback Iq:"+list.size());
//		if(mListener!=null&&list!=null&& list.size()>0){
//			mListener.onParserIqNode(list);
//		}
//	}
	public void onParserError(Exception e){
		if(mListener!=null){
			mListener.onParserError(e+"");
		}
	}
}