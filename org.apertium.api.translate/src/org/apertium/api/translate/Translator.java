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

import it.uniba.di.cdg.xcore.ui.formatter.RichFormatting;

import java.net.MalformedURLException;
import java.net.URL;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.common.query.IQuery;
import net.sf.okapi.connectors.google.GoogleMTv2Connector;
import net.sf.okapi.connectors.google.GoogleMTv2Parameters;
import net.sf.okapi.connectors.microsoft.MicrosoftMTConnector;

import org.apertium.api.ApertiumXMLRPCClient;
import org.apertium.api.exceptions.ApertiumXMLRPCClientException;
import org.apertium.api.translate.actions.TranslateConfiguration;

public class Translator {

	private TranslateConfiguration lastConfiguration = null;
	private Object connector = null;
	private static String apiKey= "AIzaSyDMoCPi857TLhqcINY8WUydTlVTLooxO4E";
	private static MarkerRichPattern[] patterns={
		new MarkerRichPattern(RichFormatting.UNDERLINE_MARKER.getCode(), true,false),
		new MarkerRichPattern(RichFormatting.BOLD_MARKER.getCode(), true,false),
		new MarkerRichPattern(RichFormatting.ITALIC_MARKER.getCode(), true,false),
		new MarkerRichPattern(RichFormatting.BOLD_UNDERLINE_MARKER.getCode(), true,false),
		new MarkerRichPattern(RichFormatting.STRIKEOUT_MARKER.getCode(), true,false),
		new MarkerRichPattern(RichFormatting.LATEX_MARKER.getCode(), false,false),
		new MarkerRichPattern("\n", false,true),
		new MarkerRichPattern("\r", false,true),
		new MarkerRichPattern("\t", false,true),
		new MarkerRichPattern("_NO_", false,false),
	};

	public Translator() {
		System.out.println("Translator()");
		lastConfiguration = new TranslateConfiguration();

	}

	private void refresh(TranslateConfiguration c) throws MalformedURLException {
		System.out.println("Translator.refresh()");
		boolean ref = true;

		if (c != null) {
			if (c.equals(lastConfiguration)) {
				ref = false;
			}
		}

		if (ref) {
			if (connector instanceof IQuery) {
				System.out.println("Translator.refresh() 1.9");

				IQuery q = (IQuery) connector;
				q.close();
			}
			System.out.println("Translator.refresh() 2");

			switch (c.getService()) {
			case MICROSOFT:
				connector = new MicrosoftMTConnector();
				net.sf.okapi.connectors.microsoft.Parameters p = new net.sf.okapi.connectors.microsoft.Parameters();
				p.setAppId("28AEB40E8307D187104623046F6C31B0A4DF907E");
				((MicrosoftMTConnector) connector).setParameters(p);
				break;
			case APERTIUM:
				connector = new ApertiumXMLRPCClient(new URL(c.getUrl()));
				break;
			case GOOGLE:
				GoogleMTv2Parameters param = new GoogleMTv2Parameters();
				param.setApiKey(apiKey);
				connector = new GoogleMTv2Connector();
				((GoogleMTv2Connector)connector).setParameters(param);
				break;
			case NONE:
				connector = null;
				break;
			}
			if (connector instanceof IQuery) {
				System.out.println("Translator.refresh() 2.1");

				IQuery q = (IQuery) connector;
				q.open();
			}
			System.out.println("Translator.refresh() 3");
		}
		System.out.println("Translator.refresh() 4");
		lastConfiguration = c.clona();
	}

	public static String _translate(String text, String src, String dest,
			Object connector) throws InterruptedException,
			ApertiumXMLRPCClientException {
		String ret = text;
		System.out.println("Translator._translate(): " + text + " " + src + " "
				+ dest + " " + connector);

		if (connector instanceof IQuery) {
			IQuery q = (IQuery) connector;

			q.setLanguages(LocaleId.fromString(src), LocaleId.fromString(dest));
			q.query(text);

			if (q.hasNext()) {
				ret = q.next().target.toString();
			}
		} else if (connector instanceof ApertiumXMLRPCClient) {
			ApertiumXMLRPCClient a = (ApertiumXMLRPCClient) connector;
			ret = a.translate(text, src, dest).get("translation");
		}

		return ret;
	}

	public String translate(String text, String who, String sourceLanguage)
			throws InterruptedException, ApertiumXMLRPCClientException,
			MalformedURLException {
		System.out.println("Translator.translate()");
		
		TranslateConfiguration c = TranslatePlugin.getDefault()
			.getConfiguration();	
		refresh(c);

		if (sourceLanguage != null) {
				
//			String ret = _translate(text, sourceLanguage, c.getUserLanguage()
//					.getCode(), connector);
			
			String ret = getRichStyleTranslate(text, sourceLanguage, c.getUserLanguage().getCode(), connector);
			
			
			
			return ret;
		} else
			throw new ApertiumXMLRPCClientException(
					"Impossible to find source language");

	}

	public static String getGetApiKeyVal() {
		return apiKey;
	}

public String getRichStyleTranslate(String text, String src, String dest,
		Object connector)  {
	
	String translated = "";

	int offset = 0;
	for (int i = 0; i < text.length(); i++) {

		for (MarkerRichPattern mp : patterns) {
			if (i + mp.pattern.length() <= text.length()
					&& mp.pattern.equals(text.substring(i,
							i + mp.pattern.length()))) {
				
				
				if (mp.isToTranslate() || mp.isSinglePattern()) {
					
					try {
						translated += _translate(text.substring(offset, i), src,  dest,
								 connector)
						
								+ mp.pattern;
					} catch (Exception e) {						
						translated += text.substring(offset, i)+ mp.pattern;
						e.printStackTrace();
					}
					offset = i + mp.pattern.length();
					i += mp.pattern.length() - 1;

				}				
				else {

					int offsetNoTranslate = text.indexOf(mp.pattern, i
							+ mp.pattern.length());

					if (offsetNoTranslate != -1) {

						try{
						translated += _translate(text.substring(offset, i), src,  dest,
								 connector)+ text.substring(i, offsetNoTranslate
											+ mp.pattern.length());
						
						} catch (Exception e) {						
							translated += text.substring(offset, i)+ text.substring(i, offsetNoTranslate
												+ mp.pattern.length());
							e.printStackTrace();
						}
								
						offset = offsetNoTranslate + mp.pattern.length();
						i = offset - 1;

					}

				}
			}
		}
	}

	if (offset < text.length()) {
		try{
		translated += _translate(text.substring(offset), src,  dest,
				 connector);
	} catch (Exception e) {						
		translated += text.substring(offset);
		e.printStackTrace();
	}
	}


//	TODO 	
//	For unknown reason when google translates a url, 
//	adds on the tail "~~V". This cause problems for 
//	loading images. Temporarily this problem is fixed 
//	deleting from any translation this string.	
	translated=translated.replace("~~V", "");
	
	return translated;
}



}

class MarkerRichPattern {
String pattern;
boolean toTranslate;
boolean isSinglePattern;

public String getPattern() {
	return pattern;
}

public MarkerRichPattern(String pattern, boolean toTranslate,boolean isSinglePattern) {
	super();
	this.pattern = pattern;
	this.toTranslate = toTranslate;
	this.isSinglePattern=isSinglePattern;
}

public void setPattern(String pattern) {
	this.pattern = pattern;
}

public boolean isToTranslate() {
	return toTranslate;
}

public void setToTranslate(boolean toTranslate) {
	this.toTranslate = toTranslate;
}

public boolean isSinglePattern() {
	return isSinglePattern;
}

public void setSinglePattern(boolean isSinglePattern) {
	this.isSinglePattern = isSinglePattern;
}



}

