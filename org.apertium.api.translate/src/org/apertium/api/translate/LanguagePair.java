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

public class LanguagePair {

	private Language srcLang = null;
	private Language destLang = null;


	public LanguagePair(Language srcLang, Language destLang) {
		this.srcLang = srcLang;
		this.destLang = destLang;
	}

	public LanguagePair(String src, String dest) {
		this.srcLang = new Language(src);
		if (null != dest)
			this.destLang = new Language(dest);
	}
	public Language getSrcLang() {
		return srcLang;
	}

	public void setSrcLang(Language srcLang) {
		this.srcLang = srcLang;
	}

	public Language getDestLang() {
		return destLang;
	}

	public void setDestLang(Language destLang) {
		this.destLang = destLang;
	}

	@Override
	public boolean equals(Object aThat) {
		if (this == aThat)
			return true;
		if (!(aThat instanceof LanguagePair))
			return false;
		LanguagePair that = (LanguagePair) aThat;
		boolean ret = srcLang.equals(that.getSrcLang())
				&& destLang.equals(that.getDestLang());
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(srcLang + " - " + destLang);
		return result.toString();
	}
		
	public LanguagePair invert() {
		return new LanguagePair(destLang, srcLang);
	}
}
