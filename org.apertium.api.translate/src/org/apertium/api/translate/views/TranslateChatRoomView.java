/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2012 Collaborative Development Group - Dipartimento di Informatica, 
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

import it.uniba.di.cdg.xcore.m2m.ui.views.ChatRoomView;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.chat.ChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatExtensionProtocolEvent;

import java.util.HashMap;

import org.apertium.api.translate.model.MTParticipantAdapterFactory;
import org.apertium.api.translate.model.UserLanguages;

public class TranslateChatRoomView extends ChatRoomView {

	public static final String LANGUAGE_UPDATE = "languageUpdate";
	// public static final String GETLANGUAGE = "GET_LANGUAGE";
	public static final String UPDATE = "UPDATE";
	private final static String LANGUAGE = "language";
	private static final String LANGUAGE_USER = "languageuser";

	public TranslateChatRoomView() {
		super();
		adapterFactory = null;
		adapterFactory = new MTParticipantAdapterFactory();
		InfoLanguage listener = new InfoLanguage();
		IBackend b = NetworkPlugin.getDefault().getRegistry()
				.getDefaultBackend();
		b.getHelper().registerBackendListener(listener);
	}

	/**
	 * This view unique id.
	 */
	public static final String ID = "org.apertium.api.translate.translateChatRoomView";

	/**
	 * The listener to chat room model events.
	 */

	private class InfoLanguage implements IBackendEventListener {

		@Override
		public void onBackendEvent(IBackendEvent event) {
			if (event instanceof MultiChatExtensionProtocolEvent) {
				MultiChatExtensionProtocolEvent mcepe = (MultiChatExtensionProtocolEvent) event;
				if (mcepe.getExtensionName().equals(LANGUAGE_UPDATE)) {
					if (!(mcepe.getFrom().endsWith(NetworkPlugin.getDefault()
							.getRegistry().getDefaultBackend().getUserId()))) {
						UserLanguages u = UserLanguages.getInstance();
						HashMap<String, String> lang = u.get_languages();
						lang.put(mcepe.getExtensionParameter(LANGUAGE_USER),
								mcepe.getExtensionParameter(LANGUAGE));
						u.set_languages(lang);

					}
					refreshView();
				}
			}

			if (event instanceof ChatExtensionProtocolEvent) {
				ChatExtensionProtocolEvent cepe = (ChatExtensionProtocolEvent) event;
				if (cepe.getExtensionName().equals(UPDATE)) {
					refreshView();
				}

			}
		}

	}
}
