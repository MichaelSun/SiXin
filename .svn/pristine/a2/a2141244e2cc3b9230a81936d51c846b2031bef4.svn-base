package com.common.messagecenter.base;

import java.util.LinkedList;
import java.util.List;

/**
 * @author yang-chen
 */
public class Element {

	public static final int POLL = 1;
	public static final int CHAT = 2;

	public int type;

	public static class Attribute {
		// <tag {qName}={value} || {qName}={val}>
		public final String localName;
		public final String qName;
		public String value;

		public Attribute(String localname, String qname, String value) {
			this.localName = localname;
			this.qName = qname;
			this.value = value;
		}

		public Attribute(String qname, Object value) {
			this.localName = qname;
			this.qName = qname;
			this.value = value != null ? value.toString() : null;
		}
	}

	// <{tag} xmlns:{prefix}={uri} >{text}</{tag}>
	public String tag; // tag
	public String prefix;
	public String uri;
	public String localName; // <{tag} /> | <{prefix}:{localName} />
	public String text;
	public final List<Attribute> attrs = new LinkedList<Attribute>();
	public final List<Element> childs = new LinkedList<Element>();

	public Element() {
		this("");
	}

	public Element(String tag) {
		this(tag, "");
	}

	public Element(String tag, Object text) {
		this(tag, tag, text.toString());
	}

	public Element(String tag, String localName, String text) {
		this(tag, "", "", localName, text);
	}

	public Element(String tag, String prefix, String uri, String localName,
			String text) {
		this.tag = tag;
		this.prefix = prefix;
		this.uri = uri;
		this.localName = localName;
		this.text = text;
	}

	public List<Element> getChild(String name) {
		if(childs == null){
			return null;
		}
		List<Element> result = new LinkedList<Element>();
		for (Element e : childs) {
			if (e.localName.equals(name)) {
				result.add(e);
			}
		}
		return result;
	}

	public Element getFirstChild(String name) {
		if(childs == null){
			return null;
		}
		List<Element> result = getChild(name);
		return result.size() != 0 ? result.get(0) : null;
	}

	public String getAttr(String name) {
		if(attrs == null){
			return null;
		}
		for (Attribute a : attrs) {
			if (name.equals(a.qName) || name.equals(a.localName)) {
				return a.value;
			}
		}
		return null;
	}

	public final void setAttrDate(String[] attrName, Object... arg) {
		LOOP: for (int i = 0; i < attrName.length; ++i) {
			for (Attribute a : attrs) {
				if (attrName[i].equals(a.qName)
						|| attrName[i].equals(a.localName)) {
					a.value = arg[i].toString();
					continue LOOP;
				}
			}
			attrs.add(new Attribute(attrName[i], arg[i].toString()));
		}
	}

	public final void setChildDate(String[] childName, Object... arg) {
		LOOP: for (int i = 0; i < childName.length; ++i) {
			for (Element a : childs) {
				if (a.tag.equals(childName[i])
						|| a.localName.equals(childName[i])) {
					a.text = arg[i].toString();
					continue LOOP;
				}
			}
			childs.add(new Element(childName[i], arg[i].toString()));
		}
	}

	public boolean flag() {
		return true;
	}

}
