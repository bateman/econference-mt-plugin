/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2010 Collaborative Development Group - Dipartimento di Informatica, 
 *                    University of Bari, http://cdg.di.uniba.it
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
