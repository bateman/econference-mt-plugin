package org.apertium.api.translate.internal.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sf.okapi.connectors.google.GoogleMTv2Connector;
import net.sf.okapi.connectors.google.GoogleMTv2Parameters;

import org.apertium.api.exceptions.ApertiumXMLRPCClientException;
import org.apertium.api.translate.Translator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TranslatorTest {
	Translator translator = null;

	@Before
	public void initialize() {
		translator = new Translator();
	}

	@Test
	public void testTranslator() {
		assertTrue(translator != null);
	}

	@Test
	public void testLoadMTProperties() {
		assertNotNull(Translator.getGtApiKeyVal());
	}

	@Test
	public void testTranslateGoogle() throws InterruptedException,
			ApertiumXMLRPCClientException {
		GoogleMTv2Parameters param = new GoogleMTv2Parameters();
		param.setApiKey("AIzaSyDMoCPi857TLhqcINY8WUydTlVTLooxO4E");
		Object connector = new GoogleMTv2Connector();
		((GoogleMTv2Connector) connector).setParameters(param);
		for (int i = 0; i < 100; i++) {
			String transl = Translator._translate(
					"tradurre il messaggio da Italiano a Inglese", "it",
					"en", connector);
			System.out.println(transl);
			Assert.assertEquals(
					"the message translated from Italian to English", transl);
			Thread.sleep(500);
		}

	}

}
