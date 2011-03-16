package org.apertium.api.translate.internal.test;

import static org.junit.Assert.*;

import org.apertium.api.translate.Language;
import org.junit.Test;

public class LanguageTest {

	@Test
	public void testLanguage() {
		Language l = new Language("it");
		assertTrue(l instanceof Language);
	}

	@Test
	public void testGetName() {
		Language l = new Language("it");
		assertTrue(l.getName().equals("Italian"));
	}

	@Test
	public void testSetName() {
		Language l = new Language("it");
		l.setName("English");
		assertTrue(l.getName().equals("English"));
	}

	@Test
	public void testGetCode() {
		Language l = new Language("it");
		assertTrue(l.getCode().equals("it"));
	}

	@Test
	public void testSetCode() {
		Language l = new Language("it");
		l.setCode("en");
		assertTrue(l.getCode().equals("en"));
	}

	@Test
	public void testEqualsObject() {
		Language l1 = new Language("it");
		Language l2 = new Language("en");
		Language l3 = new Language("it");
		Object l4 = new Object();
		assertTrue(!l1.equals(l2));
		assertTrue(l1.equals(l3));
		assertTrue(!l1.equals(l4));
	}

	@Test
	public void testToString() {
		Language l = new Language("it");
		String t = l.getName().concat(" (").concat(l.getCode()).concat(")");
		assertTrue(l.toString().equals(t));

	}

}
