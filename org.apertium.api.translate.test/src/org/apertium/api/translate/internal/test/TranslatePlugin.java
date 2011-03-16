package org.apertium.api.translate.internal.test;

import org.apertium.api.translate.actions.TranslateConfiguration;

public class TranslatePlugin {
	private TranslateConfiguration configuration;

	public TranslatePlugin() {
		configuration = new TranslateConfiguration();
	}
	public TranslateConfiguration getConfiguration() {
		System.out.println("TranslatePlugin.getTConfiguration()");
		return configuration;
	}
}
