package org.apertium.api.translate.actions;

import org.apertium.api.translate.Language;
import org.apertium.api.translate.Services;

public class TranslateConfiguration {
	private Language userLanguage = null;
	private Services.ServiceType service = null;
	private String url = null;
	
	public TranslateConfiguration() {
		// default is EN
		userLanguage = new Language("en");
		service = Services.ServiceType.NONE;
		url = "http://www.neuralnoise.com:6173/RPC2";
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

}
