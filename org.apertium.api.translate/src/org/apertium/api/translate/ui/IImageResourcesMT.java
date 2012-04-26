/**
 * This file is part of the eConference project and it is distributed under the 
 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2005 Collaborative Development Group - Dipartimento di Informatica, 
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
package org.apertium.api.translate.ui;

import it.uniba.di.cdg.xcore.ui.IImageResources;

/**
 * Give access to the shared images available in the application.
 */
public interface IImageResourcesMT extends IImageResources {
	
	public static final String ICON_PATH = "icons/";

    public static final String ICON_FILES_EXT = ".png";
       
    //flags
    public static final String FLAG_BN="flag_of_bangladesh";
    public static final String FLAG_FR="flag_of_france";
    public static final String FLAG_GE="flag_of_germany";
    public static final String FLAG_HI="flag_of_india";
    public static final String FLAG_IT="flag_of_italy";
    public static final String FLAG_JA="flag_of_japan";
    public static final String FLAG_PL="flag_of_poland";
    public static final String FLAG_PT="flag_of_portugal";
    public static final String FLAG_RU="flag_of_russia";
    public static final String FLAG_AR="flag_of_saudi_arabia";
    public static final String FLAG_ES="flag_of_spain";
    public static final String FLAG_TH="flag_of_thailand";
    public static final String FLAG_ZH="flag_of_the_prc";
    public static final String FLAG_EN="flag_of_the_united_kingdom";
    public static final String FLAG_TR="flag_of_turkey";
    public static final String FLAG_KO="flag_of_south_korea"; 
    public static final String FLAG_DEF="flag_default";
   }
