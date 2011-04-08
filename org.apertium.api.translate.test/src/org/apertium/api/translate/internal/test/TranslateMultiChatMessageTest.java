package org.apertium.api.translate.internal.test;

import static org.junit.Assert.*;

import org.apertium.api.translate.entity.TranslateMultiChatMessage;
import org.junit.Test;



public class TranslateMultiChatMessageTest {

	private static final String FROM = "From";
	private static final String TRANSLATEDMESSAGE = "translatedMessage";
	private static final String ORIGINALMESSAGE = "originalMessage";

	@Test
	public void testTranslateMultiChatMessage() {
		
		TranslateMultiChatMessage message = new TranslateMultiChatMessage(FROM, TRANSLATEDMESSAGE, ORIGINALMESSAGE, true, true);
		assertTrue(message != null);
	}

	@Test
	public void testGetTranslatedText() {
		TranslateMultiChatMessage message = new TranslateMultiChatMessage(FROM, TRANSLATEDMESSAGE, ORIGINALMESSAGE, true, true);
		assertTrue(message.getTranslatedText().equals(TRANSLATEDMESSAGE));
	}

	@Test
	public void testGetOriginalText() {
		TranslateMultiChatMessage message = new TranslateMultiChatMessage(FROM, TRANSLATEDMESSAGE, ORIGINALMESSAGE, true, true);
		assertTrue(message.getOriginalText().equals(ORIGINALMESSAGE));
	}

	@Test
	public void testIsPrivateMessage() {
		TranslateMultiChatMessage message = null;
		message = new TranslateMultiChatMessage(FROM, TRANSLATEDMESSAGE, ORIGINALMESSAGE, true, true);
		assertTrue(message.isPrivateMessage());
		message = new TranslateMultiChatMessage(FROM, TRANSLATEDMESSAGE, ORIGINALMESSAGE, false, true);
		assertTrue(!message.isPrivateMessage());
	}

	@Test
	public void testIsNoTranslation() {
		TranslateMultiChatMessage message = null;
		message = new TranslateMultiChatMessage(FROM, ORIGINALMESSAGE, ORIGINALMESSAGE, true, true);
		assertTrue(message.isNoTranslation());
		message = new TranslateMultiChatMessage(FROM, TRANSLATEDMESSAGE, ORIGINALMESSAGE, true, true, true);
		assertTrue(!message.isNoTranslation());
	}

	@Test
	public void testIsSystemMessage() {
		TranslateMultiChatMessage message = null;
		message = new TranslateMultiChatMessage(FROM, TRANSLATEDMESSAGE, ORIGINALMESSAGE, true, true);
		assertTrue(message.isSystemMessage());
		message = new TranslateMultiChatMessage(FROM, TRANSLATEDMESSAGE, ORIGINALMESSAGE, true, false);
		assertTrue(!message.isSystemMessage());
	}

}
