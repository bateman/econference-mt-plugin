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

package org.apertium.api.translate.internal;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.ITypingEvent;
import it.uniba.di.cdg.xcore.network.events.TypingEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatComposingEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.one2one.ChatManager;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.util.HashMap;
import java.util.Vector;

import org.apertium.api.exceptions.ApertiumXMLRPCClientException;
import org.apertium.api.translate.Language;
import org.apertium.api.translate.LanguagePair;
import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.Translator;
import org.apertium.api.translate.actions.TranslateConfiguration;
import org.apertium.api.translate.views.TranslateOne2OneView;


public class TranslateChatManager extends ChatManager {
	private HashMap<String, LanguagePair> buddiesLenguages = null;
	private Vector<NotTraslatedMessage> messagesToTranslate = null;
	
	private final static String LANGUAGE_REQUEST = "languageRequest";
	private final static String LANGUAGE_RESPONSE = "languageResponse";
	private final static String LANGUAGE = "language";
	private static final String TRANSLATIONSERVICEERROR = "The resource you chose is not available. The translation feature will be disabled";
	protected int position = 0;
	
	public TranslateChatManager () {
		super();
		viewID = TranslateOne2OneView.ID;
		messagesToTranslate = new Vector<NotTraslatedMessage>();
		buddiesLenguages = new HashMap<String, LanguagePair>();
		//TranslatePlugin.getDefault().addListener(this);
		
	}
	
	@Override
	public void onBackendEvent(IBackendEvent event) {
		
		
		if (event instanceof ChatComposingEvent){
			ChatComposingEvent chatComposingtEvent = (ChatComposingEvent)event;
			
			String eventFrom = chatComposingtEvent.getFrom();
			if (eventFrom.contains("/"))
				eventFrom = eventFrom.split("/")[0];
			
			String contextFrom = chatComposingtEvent.getFrom();
			if (contextFrom.contains("/"))
				contextFrom = contextFrom.split("/")[0];
			
			if (eventFrom.equals(contextFrom)){
				ITypingEvent typingEvent = new TypingEvent(backendHelper.getRoster().getBuddy(getBuddyId()).getName());
				talkView.onTyping(typingEvent);
			}
		}else 
		if (event instanceof ChatMessageReceivedEvent){
			
			ChatMessageReceivedEvent chatMessageReceivedEvent = (ChatMessageReceivedEvent)event;
			
			if (chatMessageReceivedEvent.getBuddy().getId().equals(chatContext.getBuddyId())) {
				String whoID = chatMessageReceivedEvent.getBuddy().getId();
				String whoName = chatMessageReceivedEvent.getBuddy().getName();
				translateMessage(chatMessageReceivedEvent.getMessage(), whoID, whoName);
			}
		}
		
		if(event instanceof ChatExtensionProtocolEvent){
			
			IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
			IChatServiceActions chat = b.getChatServiceAction();
			ChatExtensionProtocolEvent cepe = (ChatExtensionProtocolEvent)event;
			
			if(cepe.getExtensionName().equals(LANGUAGE_REQUEST)){
				HashMap<String, String> param = new HashMap<String, String>();
				TranslateConfiguration c = TranslatePlugin.getDefault().getConfiguration();
				param.put(LANGUAGE, c.getUserLanguage().getCode());
				chat.SendExtensionProtocolMessage(cepe.getFrom(), LANGUAGE_RESPONSE, param);
			}
			
			else if(cepe.getExtensionName().equals(LANGUAGE_RESPONSE)){
				TranslateConfiguration c = TranslatePlugin.getDefault().getConfiguration();				
				String destCode = (String)cepe.getExtensionParameter(LANGUAGE);
				LanguagePair lp = new LanguagePair(c.getUserLanguage(), new Language(destCode));
				buddiesLenguages.put(cepe.getFrom(), lp);
				updateNotTranslatedMessages(cepe.getFrom());
			}
		}
		
	}
	
	private String getLanguageFromRosterBuddy(String name){
		String lan = null;
		
		if(buddiesLenguages.containsKey(name)) {
			LanguagePair lp = buddiesLenguages.get(name);
			lan = lp.getDestLang().getCode();
		}
		return lan;
	}
	
	public void updateNotTranslatedMessages(String newBuddyLauguage){
	    	
	    	Vector<NotTraslatedMessage> messagesToDelete = new Vector<NotTraslatedMessage>();
	    	
	    	for(NotTraslatedMessage mess: messagesToTranslate){
	    		if (newBuddyLauguage.equals(mess.getSender())){
	    			translateMessage(mess.getMessage(), mess.getSender(), mess.getSenderName());
	    			messagesToDelete.add(mess);
	    		}
	    	}
	    	
	    	for(NotTraslatedMessage mess: messagesToDelete){
	    		messagesToTranslate.removeElement(mess);
	    	}
	    	messagesToDelete.clear();
	    	
	 }
	 
	 private void translateMessage(String original, String whoID, String whoName) {
		String translatedMessage = original;
		Translator translator = TranslatePlugin.getDefault().getTranslator();
		TranslateOne2OneView vista = (TranslateOne2OneView) talkView;
		try {
			translatedMessage = translator.translate(original, whoID, getLanguageFromRosterBuddy(whoID));
			
			vista.appendMessage( whoName, original, translatedMessage);
			
		} catch (ApertiumXMLRPCClientException e) {
			messagesToTranslate.add(new NotTraslatedMessage(original, whoID, whoName));
			IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
			IChatServiceActions chat = b.getChatServiceAction();
			HashMap<String, String> param = new HashMap<String, String>();
			chat.SendExtensionProtocolMessage(whoID, LANGUAGE_REQUEST, param);
		} catch (Exception e) {
			UiPlugin.getUIHelper().showMessage(TRANSLATIONSERVICEERROR);
			vista.setTranslationOFF();
		}
	
	 }
	
	 private class NotTraslatedMessage{
		
		private String message;
		private String sender;
		private String senderName;
		
		public String getMessage() {
			return message;
		}
		
		public String getSender() {
			return sender;
		}
		
		public String getSenderName() {
			return senderName;
		}

		public NotTraslatedMessage(String message, String whoID, String whoName) {
			this.message = message;
			this.sender = whoID;
			this.senderName = whoName;
		}
		
	}
	
}