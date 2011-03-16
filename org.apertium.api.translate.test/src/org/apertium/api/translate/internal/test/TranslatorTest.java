package org.apertium.api.translate.internal.test;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.junit.Test;
import org.apertium.api.exceptions.ApertiumXMLRPCClientException;
import org.apertium.api.translate.Translator;

public class TranslatorTest {

	@Test
	public void testTranslator() {
		Translator translator = null;
		try {
			translator = new Translator();
		} catch (Exception e) {

		}
		assertTrue(translator != null);
	}

	

}
