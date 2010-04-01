package org.apertium.api.translate;

import java.util.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;

public class Languages {
	private Map<String, String> languageToCode = null;
	private Map<String, String> codeToLanguage = null;
	
	public Languages(Document doc) throws XPathExpressionException {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		XPathExpression expr = xpath.compile("//iso_639_entries/iso_639_entry[@iso_639_1_code]");
		
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		
		languageToCode = new HashMap<String, String>();
		codeToLanguage = new HashMap<String, String>();
		
		NodeList nodes = (NodeList) result;
		for (int i = 0; i < nodes.getLength(); ++i) {
			NamedNodeMap attrs = nodes.item(i).getAttributes();
			
			Node name = attrs.getNamedItem("name");
			Node code = attrs.getNamedItem("iso_639_1_code");
			
			languageToCode.put(name.getNodeValue(), code.getNodeValue());
			codeToLanguage.put(code.getNodeValue(), name.getNodeValue());
		}
	}
	
	public Set<String> getLanguages() {
		return languageToCode.keySet();
	}
	
	public String getCode(String language) {
		return languageToCode.get(language);
	}
	
	public Set<String> getCodes() {
		return codeToLanguage.keySet();
	}
	
	public String getLanguage(String code) {
		return codeToLanguage.get(code);
	}
}
