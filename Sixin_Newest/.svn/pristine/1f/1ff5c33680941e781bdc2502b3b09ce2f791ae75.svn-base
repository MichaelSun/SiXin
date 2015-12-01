package com.renren.mobile.chat.base.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XmlParseUtil {

	public static Element parse(String message, String tag) {
		Element rootNode = null;
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		ByteArrayInputStream inputStream = null;
		// 首先找到xml文件
		factory = DocumentBuilderFactory.newInstance();
		try {
			// 找到xml，并加载文档
			builder = factory.newDocumentBuilder();
			inputStream = new ByteArrayInputStream(message.getBytes());
			document = builder.parse(inputStream);
			// 找到根Element
			Element root = document.getDocumentElement();
			return root;
		} catch (Exception e) {}
		finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rootNode;
	}

	public static boolean containChildNode(Node root) {
		if (root.getChildNodes() == null) {
			return false;
		} else if (root.getChildNodes().getLength() == 0) {
			return false;
		}
		return true;
	}

	public static List<Node> getNodes(String childName, Node root) {
		List<Node> list = new ArrayList<Node>();
		NodeList nodeList = root.getChildNodes();
		int sum = nodeList.getLength();
		for (int k = 0; k < sum; k++) {
			Node n = nodeList.item(k);
			if (n.getNodeName().equals(childName)) {
				list.add(n);
			}
		}
		return list;
	}

	public static String getAttribute(String name, Node n) {
		if(n.getAttributes().getNamedItem(name) == null){
			return "";
		}else{
			return n.getAttributes().getNamedItem(name).getNodeValue();
		}
		
		
	}

}
