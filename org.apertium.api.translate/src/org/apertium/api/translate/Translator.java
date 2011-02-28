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

import java.net.MalformedURLException;
import java.net.URL;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.connectors.google.GoogleMTConnector;
import net.sf.okapi.connectors.microsoft.MicrosoftMTConnector;
import net.sf.okapi.lib.translation.IQuery;

import org.apertium.api.ApertiumXMLRPCClient;
import org.apertium.api.exceptions.ApertiumXMLRPCClientException;
import org.apertium.api.translate.actions.TranslateConfiguration;

public class Translator {

	private TranslateConfiguration lastConfiguration = null;
	private Object connector = null;

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
				connector = new GoogleMTConnector();
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
//		if (src.equals(dest)) {
//			return ret;
//		}
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
				
			String ret = _translate(text, sourceLanguage, c.getUserLanguage()
					.getCode(), connector);
			return ret;
		} else
			throw new ApertiumXMLRPCClientException(
					"Impossible to find source language");

	}
}
