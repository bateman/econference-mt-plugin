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

	private static final String baseUrl = "http://195.62.234.69:1049";
	private static final String baseQuery = "/checkDocument?data=%s";

	public static List<Error> getErrors(String text) throws IOException, SAXException, ParserConfigurationException {
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
				String name = childNode.getNodeName();
				
				if (name.equals("string")) {
					error.string = childNode.getTextContent();
				} else if (name.equals("description")) {
					error.description = childNode.getTextContent();
				} else if (name.equals("precontext")) {
					error.precontext = childNode.getTextContent();
				} else if (name.equals("type")) {
					error.type = childNode.getTextContent();
				} else if (name.equals("url")) {
					error.url = childNode.getTextContent();
				} else if (name.equals("suggestions")) {
					error.suggestions = new LinkedList<String>();
					
					NodeList suggestionNodes = childNode.getChildNodes();
					for (int k = 0; k < suggestionNodes.getLength(); ++k) {
						Node suggestionNode = childNodes.item(k);
						error.suggestions.add(suggestionNode.getTextContent());
					}
				}
			}
			
			errors.add(error);
		}
		
		return errors;
	}
	
	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
		String text = "i am a troll and i dont like it";
		
		List<Error> errors = getErrors(text);
		
		for (Error e : errors) {
			System.out.println(e);
		}
	}

}
