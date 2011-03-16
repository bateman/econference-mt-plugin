package org.apertium.api.translate.internal.test;

import static org.junit.Assert.*;

import org.apertium.api.translate.ISO639;
import org.junit.Test;

public class ISO639Test {

	@Test
	public void testISO639() {
		ISO639 iso =new ISO639();
		assertTrue(iso instanceof ISO639);
	}

	@Test
	public void testGetLanguages() {
		ISO639 iso =new ISO639();
		assertTrue(iso.getLanguages().size() > 0);
	}

	@Test
	public void testGetCode() {
		ISO639 iso =new ISO639();
		String lan = iso.getCode("Italian");
		assertTrue(lan.equals("it"));
	}

	@Test
	public void testGetCodes() {
		ISO639 iso =new ISO639();
		assertTrue(iso.getCodes().size() > 0);
	}

	@Test
	public void testGetLanguage() {
		ISO639 iso =new ISO639();
		String lan = iso.getLanguage("it");
		assertTrue(lan.equals("Italian"));
	}

}
