package com.neuralnoise.atd;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

public class ATDClient {
	
	public static String baseUrl = "http://195.62.234.69:1049";
	//public static String baseUrl = "http://127.0.0.1:1049";
	public static String baseQuery = "/checkDocument?data=%s";

	public static boolean ignoreTypes = false;
	public static boolean verbose = false;
	
	public static final String[] typesToIgnore = { "Bias Language", "Cliches", "Complex Expression", "Diacritical Marks", "Double Negatives", "Hidden Verbs", "Jargon Language", "Passive voice", "Phrases to Avoid", "Redundant Expression" };
	
	public static void serialize(Document doc, OutputStream out) throws Exception {
		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer serializer;
		try {
			serializer = tfactory.newTransformer();
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");

			serializer.transform(new DOMSource(doc), new StreamResult(out));
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static List<Error> getErrors(String text) throws Exception {
		URL url = new URL(baseUrl + String.format(baseQuery, URLEncoder.encode(text, "UTF-8")));
		URLConnection conn = url.openConnection();

		conn.setRequestProperty("User-Agent", "ATDClient");
		conn.setRequestProperty("Referer", baseUrl);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(conn.getInputStream());

		doc.getDocumentElement().normalize();
	
		if (verbose) {
			serialize(doc, System.out);
		}
		
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
						Node suggestionNode = suggestionNodes.item(k);
						if (suggestionNode.getNodeName().equals("option")) {
							error.suggestions.add(suggestionNode.getTextContent());
						}
					}
				}
			}
			
			if (!(ignoreTypes && Arrays.asList(typesToIgnore).contains(error.type))) {	
				errors.add(error);
			}
		}
		
		return errors;
	}
	
	public static void main(String[] args) throws Exception {
		String text = "i am a troll and i dont like it";
		
		List<Error> errors = getErrors(text);
		
		for (Error e : errors) {
			System.out.println(e.toString());
		}
	}

}
