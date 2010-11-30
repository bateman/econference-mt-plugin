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

package org.apertium.api.translate.entity;

import it.uniba.di.cdg.xcore.m2m.service.MultiChatMessage;

public class TranslateMultiChatMessage extends MultiChatMessage implements
		ITranslateMessage {
	private String translatedText;
	private boolean isPrivateMessage;
	private boolean noTranslation;
	private String originalText;
	private boolean isSystemMessage;

	public TranslateMultiChatMessage(String from, String translatedMessage,
			String originalMessage, boolean private_Message,
			boolean isSystem_Message) {
		super(null, from, translatedMessage);
		translatedText = translatedMessage.replace("\n", " ");
		originalText = originalMessage.replace("\n", " ");
		isPrivateMessage = private_Message;
		isSystemMessage = isSystem_Message;
		noTranslation = translatedText.equals(originalText);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apertium.api.translate.internal.ITranslateMessage#getTranslatedText()
	 */
	@Override
	public String getTranslatedText() {
		return translatedText;
	}

	public String getOriginalText() {
		if (originalText == null) {
			return "";
		}
		return originalText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apertium.api.translate.internal.ITranslateMessage#isPrivateMessage()
	 */
	@Override
	public boolean isPrivateMessage() {
		return isPrivateMessage;
	}

	public boolean isNoTranslation() {
		return noTranslation;
	}

	@Override
	public boolean isSystemMessage() {
		return isSystemMessage;
	}

}
