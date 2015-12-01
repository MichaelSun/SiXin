package com.common.messagecenter.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * User: afpro Date: 12-1-6 Time: 下午5:45 not thread-safe
 */
public class Parser extends DefaultHandler {
	private final static Pattern NO_CONTENT_REGEX = Pattern.compile("^\\s*$");
	private final static SAXParserFactory saxParserFactory = SAXParserFactory
			.newInstance();
	private final SAXParser sax;
	private Element root;
	private Stack<Element> parseStack = new Stack<Element>();

	public Parser() {
		SAXParser saxParser = null;
		try {
			saxParser = saxParserFactory.newSAXParser();
		} catch (Exception ignored) {
		}
		sax = saxParser;
	}

	public Element getRoot() {
		return root;
	}

	public void parse(InputStream inputStream) throws IOException, SAXException {
		sax.parse(new InputSource(inputStream), this);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		Element element = new Element(qName, localName, "");
		element.uri = uri;
		for (int i = 0; i < attributes.getLength(); i++) {
			element.attrs.add(new Element.Attribute(attributes.getLocalName(i),
					attributes.getQName(i), attributes.getValue(i)));
		}
		if (uri != null && mXmlnsUri != null && uri.equals(mXmlnsUri)) {
			element.attrs.add(new Element.Attribute("xmlns", mXmlnsUri));
			mXmlnsUri = null;
		}
		if (parseStack.isEmpty()) {
			root = element;
		} else {
			parseStack.lastElement().childs.add(element);
		}

		parseStack.push(element);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (parseStack.isEmpty()) {
			throw new SAXException("stack is already empty");
		}
		Element element = parseStack.pop();
		if (!(qName.equals(element.tag))) {
			throw new SAXException("start != end");
		}

		if (element.text != null) {
			if (NO_CONTENT_REGEX.matcher(element.text).matches()) {
				element.text = null;
			}
		}
		onStackSizeChanged(parseStack.size());
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		StringBuffer sb = new StringBuffer(128);
		sb.append(ch, start, length);
		Element element = parseStack.lastElement();
		element.text += sb.toString();
	}

	@Override
	public void startDocument() throws SAXException {
		root = null;
		parseStack.clear();
	}

	private String mXmlnsUri = null;

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		mXmlnsUri = uri;
	};

	protected void onStackSizeChanged(int size) {
	}

}
