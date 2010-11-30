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

package org.apertium.api.translate.actions;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;

import org.apertium.api.translate.Language;
import org.apertium.api.translate.Services;
import org.apertium.api.translate.Services.ServiceType;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class TranslateConfiguration {
	private static final String CONFIGURATION_NODE_QUALIFIER = "it.uniba.di.cdg.xcore.econference.mt";
	private static final String MT_CONFIG_PATH_NODE = "mt_";
	private static final String MT_SERVICE_PROVIDER = "mt_service_provider";
	private static final String MT_SERVICE_URL = "mt_service_url";
	private static final String MT_USER_LANGUAGE = "mt_user_language";
	private static final String MT_PREFERENCES_HASH = "mt_preference_hash";
	
	private Language userLanguage = null;
	private Services.ServiceType service = null;
	private Services services = null;
	private String url = null;
	
	public TranslateConfiguration() {
		// default is EN
		userLanguage = new Language("en");
		service = Services.ServiceType.NONE;
		url = "Enter server url";
		services = new Services();
	}

	public TranslateConfiguration clona() {
		TranslateConfiguration ret = new TranslateConfiguration();
		ret.setUserLanguage(userLanguage);
		ret.setService(service);
		ret.setUrl(url);
		return ret;
	}
	
	public Language getUserLanguage() {
		return userLanguage;
	}

	public void setUserLanguage(Language lang) {
		this.userLanguage = lang;
	}

	public Services.ServiceType getService() {
		return service;
	}
	
	public void setService(Services.ServiceType service) {
		this.service = service;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		Services s = new Services();
		String ret = "service: " + s.getService(service) + " url: " + url + " user lang: " + userLanguage;
		return ret;
	}
	
	@Override
	public boolean equals(Object aThat) {
		if (this == aThat)
			return true;
		if (!(aThat instanceof TranslateConfiguration))
			return false;
		
		boolean ret = true;
		TranslateConfiguration that = (TranslateConfiguration) aThat;
		
		ret &= url.equals(that.getUrl());
		ret &= userLanguage.equals(that.getUserLanguage());
		ret &= service.equals(that.getService());
		
		return ret;
	}

	public void loadProperties() {
		System.out.println("Loading entries");
		Preferences preferences = new ConfigurationScope()
				.getNode(CONFIGURATION_NODE_QUALIFIER);
		String id = NetworkPlugin.getDefault().getRegistry()
				.getDefaultBackend().getUserId();

		Preferences connections = preferences.node(MT_CONFIG_PATH_NODE + id);
		System.out.println("LOAD: path node: " + MT_CONFIG_PATH_NODE + id);
		Preferences node = connections.node(MT_PREFERENCES_HASH);

		System.out.println("LOAD: path node: " + node);

		int mtServiceProvider = node.getInt(MT_SERVICE_PROVIDER, -1);
		if (-1 != mtServiceProvider) {
			ServiceType serviceType = services
					.getServiceType(mtServiceProvider);
			setService(serviceType);
		}
		String mtServiceUrl = node.get(MT_SERVICE_URL, null);
		if (null != mtServiceUrl)
			setUrl(mtServiceUrl);
		String mtUserlLanguageCode = node.get(MT_USER_LANGUAGE, null);
		if (null != mtUserlLanguageCode) {
			Language userLang = new Language(mtUserlLanguageCode);			
			setUserLanguage(userLang);
		}
		
	}

	public void storeProperties() {
		System.out.println("Storing MT preferences");
		Preferences preferences = new ConfigurationScope()
				.getNode(CONFIGURATION_NODE_QUALIFIER);
		String userAccountId = NetworkPlugin.getDefault().getRegistry()
				.getDefaultBackend().getUserId();
		System.out.println("STORE: id: " + userAccountId);

		Preferences connections = preferences.node(MT_CONFIG_PATH_NODE
				+ userAccountId);
		System.out.println("STORE: path node:" + MT_CONFIG_PATH_NODE
				+ userAccountId);

		Preferences connection = connections.node(MT_PREFERENCES_HASH);

		connection.putInt(MT_SERVICE_PROVIDER, getService()
				.ordinal());
		connection.put(MT_SERVICE_URL, getUrl());
		connection.put(MT_USER_LANGUAGE, getUserLanguage()
				.getCode());
		try {
			connections.flush();
		} catch (BackingStoreException e) {
			System.err
					.println("Error storing MT preferences. Next time defaults will be used.");
			e.printStackTrace();
		}
		
	}

}
