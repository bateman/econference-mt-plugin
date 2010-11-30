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

package org.apertium.api.translate.listeners;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.ui.views.ITalkView.ISendMessagelListener;

public class TranslateListener implements IBackendEventListener,
		ISendMessagelListener {
	@Override
	public void onBackendEvent(IBackendEvent event) {
		System.out.println("TranslateListener.onBackendEvent() - event is "
				+ event.getClass().toString());

		if (event instanceof ChatMessageReceivedEvent) {
			ChatMessageReceivedEvent cmrEvent = (ChatMessageReceivedEvent) event;
			System.out.println("TranslateListener.onBackendEvent(): \""
					+ cmrEvent.getMessage() + "\"");
		}

	}

	@Override
	public void notifySendMessage(String message) {
		System.out.println("TranslateListener.notifySendMessage(): " + message);
	}

}
