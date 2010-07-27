package org.apertium.api.translate;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.chat.ChatExtensionProtocolEvent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.connectors.google.GoogleMTConnector;
import net.sf.okapi.connectors.microsoft.MicrosoftMTConnector;
import net.sf.okapi.lib.translation.IQuery;

import org.apertium.api.ApertiumXMLRPCClient;
import org.apertium.api.exceptions.ApertiumXMLRPCClientException;
import org.apertium.api.translate.actions.TranslateConfiguration;

public class Translator implements IBackendEventListener {
	
	private TranslateConfiguration lastConfiguration = null;
	private Object connector = null;
	private HashMap<String, String> buddiesLenguages;
	private final static String LANGUAGE_REQUEST = "languageRequest";
	private final static String LANGUAGE_RESPONSE = "languageResponse";
	private final static String LANGUAGE = "language";
	
	public Translator() {
		System.out.println("Translator()");
		lastConfiguration = new TranslateConfiguration();
		buddiesLenguages = new HashMap<String, String>();
		
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
				
				IQuery q = (IQuery)connector;
				q.close();
			}
			System.out.println("Translator.refresh() 2");
		
			switch (c.getService()) {
			case MICROSOFT:
				connector = new MicrosoftMTConnector();	
				net.sf.okapi.connectors.microsoft.Parameters p = new net.sf.okapi.connectors.microsoft.Parameters();
				p.setAppId("28AEB40E8307D187104623046F6C31B0A4DF907E");
				((MicrosoftMTConnector)connector).setParameters(p);
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
				
				IQuery q = (IQuery)connector;
				q.open();
			}	
			System.out.println("Translator.refresh() 3");
		}
		System.out.println("Translator.refresh() 4");
		lastConfiguration = c.clona();
	}
	
	public static String _translate(String text, String src, String dest, Object connector) throws InterruptedException, ApertiumXMLRPCClientException {
		String ret = text;
		
		System.out.println("Translator._translate(): " + text + " " + src + " " + dest + " " + connector);
		
		if (connector instanceof IQuery) {
			IQuery q = (IQuery)connector;
			
			q.setLanguages(LocaleId.fromString(src), LocaleId.fromString(dest));
			q.query(text);
			
			if (q.hasNext()) {
				ret = q.next().target.toString();
			}
		} else if (connector instanceof ApertiumXMLRPCClient) {
			ApertiumXMLRPCClient a = (ApertiumXMLRPCClient)connector;
			ret = a.translate(text, src, dest).get("translation");
		}
		
		return ret;
	}
	
	private String getLanguageFromRosterBuddy(String name){
		String lan = null;
		
		if(buddiesLenguages.containsKey(name))
			lan = buddiesLenguages.get(name);
		else{
			IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
			IChatServiceActions chat = b.getChatServiceAction();
			HashMap<String, String> param = new HashMap<String, String>();
			chat.SendExtensionProtocolMessage(name, LANGUAGE_REQUEST, param);
		}
		
		return lan;
	}

	public String translate(String text, String who) throws InterruptedException, ApertiumXMLRPCClientException, MalformedURLException {
		System.out.println("Translator.translate()");
		
		String sourceLanguage = getLanguageFromRosterBuddy(who);
		
		if (sourceLanguage != null){
			TranslateConfiguration c = TranslatePlugin.getDefault().getConfiguration();
			refresh(c);
			
			String ret = _translate(text, 
					sourceLanguage, 
					c.getLangPair().getDestLang().getCode(), 
					connector);
			return ret;
		}else
			throw new ApertiumXMLRPCClientException("Impossible to find source language");
		
	}

	@Override
	public void onBackendEvent(IBackendEvent event) {
		if(event instanceof ChatExtensionProtocolEvent){
			
			IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
			IChatServiceActions chat = b.getChatServiceAction();
			ChatExtensionProtocolEvent cepe = (ChatExtensionProtocolEvent)event;
			
			if(cepe.getExtensionName().equals(LANGUAGE_REQUEST)){
				HashMap<String, String> param = new HashMap<String, String>();
				TranslateConfiguration c = TranslatePlugin.getDefault().getConfiguration();
				param.put(LANGUAGE, c.getLangPair().getDestLang().getCode());
				chat.SendExtensionProtocolMessage(cepe.getFrom(), LANGUAGE_RESPONSE, param);
			}
			
			else if(cepe.getExtensionName().equals(LANGUAGE_RESPONSE)){
				buddiesLenguages.put(cepe.getFrom(), (String)cepe.getExtensionParameter(LANGUAGE));
			}
		}
	}
}
