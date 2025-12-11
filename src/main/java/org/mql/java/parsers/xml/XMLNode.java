package org.mql.java.parsers.xml;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.mql.java.models.ClassModel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLNode {
	private Node node;
	private XMLNode children[];

	public XMLNode(String source) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(source));
			setNode(document.getDocumentElement());
		} catch (Exception e) {
			System.out.println("Erreur : " + e.getMessage());
		}
	}

	public XMLNode(Node node) {
		super();
		setNode(node);
	}

	public XMLNode() {
		// TODO Auto-generated constructor stub
	}

	public Node getNode() {
		return node;
	}

	public XMLNode[] getChildren() {
		return children;
	}

	public String getName() {
		return node.getNodeName();
	}

	public boolean isNamed(String name) {
		return node.getNodeName().equals(name);
	}

	public XMLNode getChild(String name) {
		for (XMLNode child : children) {
			if (child.isNamed(name)) {
				return child;
			}
		}
		return null;
	}

	public String getValue() {
		Node child = node.getFirstChild();
		if (child != null && child.getNodeType() == Node.TEXT_NODE) {
			return child.getNodeValue();
		}
		return "";
	}

	public void setNode(Node node) {
		this.node = node;
		extractChildren();
	}

	public String getAttribute(String name) {
		Node att = node.getAttributes().getNamedItem(name);
		if (att == null)
			return "";
		return att.getNodeValue();
	}

	public int getIntAttribute(String name) {
		String s = getAttribute(name);
		int value = 0;
		try {
			value = Integer.parseInt(s);
		} catch (Exception e) {
			System.out.println("erreur : " + e.getMessage());
		}
		return value;
	}

	private void extractChildren() {
		NodeList list = node.getChildNodes();
		LinkedList<XMLNode> nodes = new LinkedList<XMLNode>();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
				nodes.add(new XMLNode(list.item(i)));
			}
		}
		children = new XMLNode[nodes.size()];
		nodes.toArray(children);
	}




}
