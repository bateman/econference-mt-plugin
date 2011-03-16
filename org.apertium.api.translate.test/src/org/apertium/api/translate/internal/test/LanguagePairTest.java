package org.apertium.api.translate.internal.test;

import static org.junit.Assert.*;

import org.apertium.api.translate.Language;
import org.apertium.api.translate.LanguagePair;
import org.junit.Test;

public class LanguagePairTest {

	@Test
	public void testLanguagePairLanguageLanguage() {
		
		LanguagePair lp = new LanguagePair(new Language("it"), new Language("en"));
		assertTrue(lp instanceof LanguagePair);
	}

	@Test
	public void testLanguagePairStringString() {
		LanguagePair lp = new LanguagePair("it", "en");
		assertTrue(lp instanceof LanguagePair);
		
	}

	@Test
	public void testGetSrcLang() {
		LanguagePair lp = new LanguagePair(new Language("it"), new Language("en"));
		assertTrue(lp.getSrcLang().getCode().equals("it"));
	}

	@Test
	public void testSetSrcLang() {
		LanguagePair lp = new LanguagePair(new Language("it"), new Language("en"));
		lp.setSrcLang(new Language("en"));
		assertTrue(lp.getSrcLang().getCode().equals("en"));
	}

	@Test
	public void testGetDestLang() {
		LanguagePair lp = new LanguagePair(new Language("it"), new Language("en"));
		assertTrue(lp.getDestLang().getCode().equals("en"));
	}

	@Test
	public void testSetDestLang() {
		LanguagePair lp = new LanguagePair(new Language("it"), new Language("en"));
		lp.setDestLang(new Language("it"));
		assertTrue(lp.getDestLang().getCode().equals("it"));
	}

	@Test
	public void testEqualsObject() {
		LanguagePair lp1 = new LanguagePair(new Language("it"), new Language("en"));
		LanguagePair lp2 = new LanguagePair(new Language("en"), new Language("it"));
		LanguagePair lp3 = new LanguagePair(new Language("it"), new Language("en"));
		Object lp4 = new Object();
		assertTrue(lp1.equals(lp1));
		assertTrue(!lp1.equals(lp2));
		assertTrue(lp1.equals(lp3));
		assertTrue(!lp1.equals(lp4));
	}

	@Test
	public void testToString() {
		LanguagePair lp = new LanguagePair(new Language("it"), new Language("en"));
		String stringa1 = lp.toString();
		Language l1 = new Language("it");
		Language l2 = new Language("en");
		String stringa2= l1.toString().concat(" - ").concat(l2.toString());
		assertTrue(stringa1.equals(stringa2));
	}

	@Test
	public void testInvert() {
		LanguagePair lp1 = new LanguagePair(new Language("it"), new Language("en"));
		LanguagePair lp2 = lp1.invert();
		LanguagePair lp3 = new LanguagePair(new Language("en"), new Language("it"));
		assertTrue(lp2.equals(lp3));
		
	}

}
