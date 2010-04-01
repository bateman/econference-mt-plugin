package org.apertium.api.translate.actions;

import org.apertium.api.translate.*;

public class TranslateConfiguration {
	private LanguagePair langPair = null;
	private Services.ServiceType service = null;
	private String url = null;
	
	public TranslateConfiguration() {
		langPair = new LanguagePair("en", "it");
		service = Services.ServiceType.NONE;
		url = "http://www.neuralnoise.com:6173/RPC2";
	}

	public TranslateConfiguration clona() {
		TranslateConfiguration ret = new TranslateConfiguration();
		ret.setLangPair(langPair);
		ret.setService(service);
		ret.setUrl(url);
		return ret;
	}
	
	public LanguagePair getLangPair() {
		return langPair;
	}

	public void setLangPair(LanguagePair langPair) {
		this.langPair = langPair;
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
		String ret = "service: " + s.getService(service) + " url: " + url + " pair: " + langPair;
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
		ret &= langPair.equals(that.getLangPair());
		ret &= service.equals(that.getService());
		
		return ret;
	}

}
