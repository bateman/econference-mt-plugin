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

package org.apertium.api.translate.views;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.chat.ChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatMessageEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import org.apertium.api.exceptions.ApertiumXMLRPCClientException;
import org.apertium.api.translate.Language;
import org.apertium.api.translate.LanguagePair;
import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.Translator;
import org.apertium.api.translate.actions.TranslateConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

public class TranslateView extends ViewPart implements IBackendEventListener {

	private Composite top = null;
	private SashForm sashForm = null;

	protected StyledText translations = null;
	protected int position = 0;

	public static final String ID = TranslatePlugin.ID + ".views.translateView";
	private static final String SEPARATOR = System
			.getProperty("line.separator");

	private Vector<NotTraslatedMessage> messagesToTranslate = null;
	private HashMap<String, LanguagePair> buddiesLenguages = null;

	private final static String LANGUAGE_REQUEST = "languageRequest";
	private final static String LANGUAGE_RESPONSE = "languageResponse";
	private final static String LANGUAGE = "language";

	public TranslateView() {
		super();
		messagesToTranslate = new Vector<NotTraslatedMessage>();
		buddiesLenguages = new HashMap<String, LanguagePair>();
	}

	@Override
	public void createPartControl(Composite parent) {
		top = new Composite(parent, SWT.NONE);
		top.setLayout(new FillLayout());
		createSashForm();
		TranslatePlugin.getDefault().addListener(this);
	}

	@Override
	public void dispose() {
		System.out.println("TranslateView.dispose()");
		TranslatePlugin.getDefault().removeListener(this);
	}

	@Override
	public void setFocus() {
		translations.setFocus();
	}

	private String getLanguageFromRosterBuddy(String name) {
		String lan = null;

		if (buddiesLenguages.containsKey(name)) {
			LanguagePair lp = buddiesLenguages.get(name);
			lan = lp.getDestLang().getCode();
		}
		return lan;
	}

	private void createSashForm() {
		sashForm = new SashForm(top, SWT.NONE);
		sashForm.setOrientation(org.eclipse.swt.SWT.VERTICAL);

		translations = new StyledText(sashForm, SWT.BORDER | SWT.V_SCROLL
				| SWT.WRAP);
		translations.setEditable(false);
	}

	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	private static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	public void newMessage(String original, String who, boolean toTranslate) {
		Color blu = new Color(Display.getCurrent(), 25, 25, 112);
		Color red = new Color(Display.getCurrent(), 142, 35, 35);

		Translator tran = TranslatePlugin.getDefault().getTranslator();
		String translated = original;

		try {
			translated = tran.translate(original, who,
					getLanguageFromRosterBuddy(who));

			String n = now();

			appendMessage(String.format("[%s - %s] %s", who, n, original), blu);
			appendMessage(String.format("[%s - %s] %s", who, n, translated),
					red);

		} catch (ApertiumXMLRPCClientException e) {
			messagesToTranslate.add(new NotTraslatedMessage(original, who));
			IBackend b = NetworkPlugin.getDefault().getRegistry()
					.getDefaultBackend();
			IChatServiceActions chat = b.getChatServiceAction();
			HashMap<String, String> param = new HashMap<String, String>();
			chat.SendExtensionProtocolMessage(who, LANGUAGE_REQUEST, param);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void newRoomMessage(String original, String who, boolean toTranslate) {
		Color blu = new Color(Display.getCurrent(), 25, 25, 112);
		Color red = new Color(Display.getCurrent(), 142, 35, 35);

		Translator tran = TranslatePlugin.getDefault().getTranslator();
		String translated = original;

		try {
			translated = tran.translate(original, who,
					getLanguageFromRosterBuddy(who));

			String n = now();

			appendMessage(String.format("[%s - %s] %s", who, n, original), blu);
			appendMessage(String.format("[%s - %s] %s", who, n, translated),
					red);

		} catch (ApertiumXMLRPCClientException e) {
			messagesToTranslate.add(new NotTraslatedMessage(original, who));
			IBackend b = NetworkPlugin.getDefault().getRegistry()
					.getDefaultBackend();
			IMultiChatServiceActions chat = b.getMultiChatServiceAction();
			HashMap<String, String> param = new HashMap<String, String>();
			chat.SendExtensionProtocolMessage(LANGUAGE_REQUEST, param);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateNotTranslatedMessages(String newBuddyLauguage) {

		Vector<NotTraslatedMessage> messagesToDelete = new Vector<NotTraslatedMessage>();

		for (NotTraslatedMessage mess : messagesToTranslate) {
			if (newBuddyLauguage.equals(mess.getSender())) {
				newMessage(mess.message, mess.sender, true);
				messagesToDelete.add(mess);
			}
		}

		for (NotTraslatedMessage mess : messagesToDelete) {
			messagesToTranslate.removeElement(mess);
		}

		messagesToDelete.clear();

	}

	public void appendMessage(final String message, final Color color) {
		System.out.println("TranslateView.appendMessage()");

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				String textToAppend = message + SEPARATOR;
				translations.append(textToAppend);

				StyleRange styleRange = new StyleRange();
				styleRange.start = position;
				styleRange.length = textToAppend.length();
				styleRange.fontStyle = SWT.BOLD;
				styleRange.foreground = color;
				translations.setStyleRange(styleRange);

				position += textToAppend.length();
				scrollToEnd();
			}
		});
	}

	protected void scrollToEnd() {
		int n = translations.getCharCount();
		translations.setSelection(n, n);
		translations.showSelection();
	}

	@Override
	public void onBackendEvent(IBackendEvent event) {
		System.out.println("TranslateView.onBackendEvent() - event is "
				+ event.getClass().toString());

		if (event instanceof ChatMessageReceivedEvent) {
			ChatMessageReceivedEvent cmrEvent = (ChatMessageReceivedEvent) event;

			newMessage(cmrEvent.getMessage().trim(), cmrEvent.getBuddy()
					.getId(), true);
		} else if (event instanceof MultiChatMessageEvent) {
			MultiChatMessageEvent mcme = (MultiChatMessageEvent) event;

			newRoomMessage(mcme.getMessage().trim(), mcme.getFrom(), true);

		}

		if (event instanceof ChatExtensionProtocolEvent) {

			IBackend b = NetworkPlugin.getDefault().getRegistry()
					.getDefaultBackend();
			IChatServiceActions chat = b.getChatServiceAction();
			ChatExtensionProtocolEvent cepe = (ChatExtensionProtocolEvent) event;

			if (cepe.getExtensionName().equals(LANGUAGE_REQUEST)) {
				HashMap<String, String> param = new HashMap<String, String>();
				TranslateConfiguration c = TranslatePlugin.getDefault()
						.getConfiguration();
				param.put(LANGUAGE, c.getUserLanguage().getCode());
				chat.SendExtensionProtocolMessage(cepe.getFrom(),
						LANGUAGE_RESPONSE, param);
			}

			else if (cepe.getExtensionName().equals(LANGUAGE_RESPONSE)) {
				TranslateConfiguration c = TranslatePlugin.getDefault()
						.getConfiguration();
				String destCode = (String) cepe.getExtensionParameter(LANGUAGE);
				LanguagePair lp = new LanguagePair(c.getUserLanguage(),
						new Language(destCode));
				buddiesLenguages.put(cepe.getFrom(), lp);
				updateNotTranslatedMessages(cepe.getFrom());
			}
		}

		if (event instanceof MultiChatExtensionProtocolEvent) {

			IBackend b = NetworkPlugin.getDefault().getRegistry()
					.getDefaultBackend();
			IMultiChatServiceActions chat = b.getMultiChatServiceAction();
			MultiChatExtensionProtocolEvent mcepe = (MultiChatExtensionProtocolEvent) event;

			if (mcepe.getExtensionName().equals(LANGUAGE_REQUEST)) {
				HashMap<String, String> param = new HashMap<String, String>();
				TranslateConfiguration c = TranslatePlugin.getDefault()
						.getConfiguration();
				param.put(LANGUAGE, c.getUserLanguage().getCode());
				chat.SendExtensionProtocolMessage(LANGUAGE_RESPONSE, param);
			}

			else if (mcepe.getExtensionName().equals(LANGUAGE_RESPONSE)) {
				TranslateConfiguration c = TranslatePlugin.getDefault()
						.getConfiguration();
				String destCode = (String) mcepe
						.getExtensionParameter(LANGUAGE);
				LanguagePair lp = new LanguagePair(c.getUserLanguage(),
						new Language(destCode));
				buddiesLenguages.put(mcepe.getFrom(), lp);
				updateNotTranslatedMessages(mcepe.getFrom());
			}
		}

	}

	private class NotTraslatedMessage {

		private String message;
		private String sender;

		/*
		 * public String getMessage() { return message; }
		 */
		public String getSender() {
			return sender;
		}

		public NotTraslatedMessage(String message, String sender) {
			super();
			this.message = message;
			this.sender = sender;
		}

	}

}