package com.neuralnoise.atd;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ATDClient {

	private static final String baseUrl = "http://www.neuralnoise.com:1049";
	private static final String baseQuery = "/checkDocument?data=%s";

	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
		String text = "i am a troll and i dont like it";

		URL url = new URL(baseUrl + String.format(baseQuery, URLEncoder.encode(text, "UTF-8")));
		URLConnection conn = url.openConnection();

		conn.setRequestProperty("User-Agent", "ATDClient");
		conn.setRequestProperty("Referer", baseUrl);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(conn.getInputStream());

		doc.getDocumentElement().normalize();
		
		NodeList errorNodes = doc.getElementsByTagName("error");
		
		List<Error> errors = new LinkedList<Error>();
		
		for (int i = 0; i < errorNodes.getLength(); ++i) {
			Error error = new Error();
			
			Node errorNode = errorNodes.item(i);
			NodeList childNodes = errorNode.getChildNodes();
			
			for (int j = 0; j < childNodes.getLength(); ++j) {
				Node childNode = childNodes.item(j);
				
				System.out.println(childNode.getNodeName() + ":" + childNode.getNodeValue());
			}
		}
	}

}
