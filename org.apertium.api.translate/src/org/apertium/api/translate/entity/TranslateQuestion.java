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

public class TranslateQuestion extends MultiChatMessage implements
		ITranslateQuestion {
	private String translatedText;
	private String originalText;
	private int index;
	private boolean notTranslated;

	public TranslateQuestion(String from, String translatedMessage,
			String originalMessage, int questionIndex) {
		super(null, from, translatedMessage);
		translatedText = translatedMessage.replace("\n", " ");
		originalText = originalMessage.replace("\n", " ");
		index = questionIndex;
		notTranslated = translatedText.equals(originalText);
	}
	public TranslateQuestion(String from, String translatedMessage,
			String originalMessage, int questionIndex, boolean translated) {
		super(null, from, translatedMessage);
		translatedText = translatedMessage.replace("\n", " ");
		originalText = originalMessage.replace("\n", " ");
		index = questionIndex;
		notTranslated = !translated;
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
	public int getIndex() {
		return index;
	}
	public String getOriginalText() {
		if (originalText == null) {
			return "";
		}
		return originalText;
	}


	public boolean isNoTranslation() {
		return notTranslated;
	}

	@Override
	public void setTranslatedText(String text) {
		translatedText = text;
		
	}

	
}
