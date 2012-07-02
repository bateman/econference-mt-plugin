/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2006 - 2012 Collaborative Development Group - Dipartimento di Informatica, 
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
import org.apertium.api.translate.Language;
import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.ui.IImageResourcesMT;
import org.eclipse.swt.graphics.Image;


public class Flags {

	
	
	public static Image getFlag(Language userLanguage)
	{
	
	Image im2 = null;

	if (userLanguage!=null) {
		im2 = new Image(null, 22, 10);	
		String idLanguage=userLanguage.getCode();
		
		if (idLanguage.equalsIgnoreCase("ar")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_AR)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("bn")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_BN)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("en")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_EN)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("es")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_ES)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("fr")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_FR)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("de")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_GE)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("hi")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_HI)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("it")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_IT)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("ja")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_JA)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("pl")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_PL)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("pt")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_PT)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("ru")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_RU)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("th")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_TH)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("tr")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_TR)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("zh")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_ZH)
					.createImage();
		} else if (idLanguage.equalsIgnoreCase("ko")) {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_KO)
					.createImage();
		} else {
			im2 = TranslatePlugin.getDefault()
					.getImageDescriptor(IImageResourcesMT.FLAG_DEF)
					.createImage();
		}
	}
	return im2;
	}

	
}
