package org.apertium.api.translate.internal.test;

import static org.junit.Assert.assertTrue;

import org.apertium.api.translate.entity.ITranslateQuestion;
import org.apertium.api.translate.entity.TranslateQuestion;
import org.junit.Test;

public class TranslateQuestionTest {

	private static final String TRANSLATEDQUESTION = "translatedQuestion";
	private static final String FROM = "from";
	private static final String ORIGINALQUESTION = "originalQuestion";
	private static final int INDEX = 0;

	@Test
	public void testTranslateQuestion() {
		new TranslateQuestion(FROM, TRANSLATEDQUESTION, ORIGINALQUESTION, INDEX);
		assertTrue(true);
	}

	@Test
	public void testGetTranslatedText() {
		ITranslateQuestion p = new TranslateQuestion(FROM, TRANSLATEDQUESTION,
				ORIGINALQUESTION, INDEX);
		assertTrue(p.getTranslatedText().equals(TRANSLATEDQUESTION));
	}

	@Test
	public void testGetIndex() {
		ITranslateQuestion p = new TranslateQuestion(FROM, TRANSLATEDQUESTION,
				ORIGINALQUESTION, INDEX);
		assertTrue(p.getIndex() == INDEX);
	}

	@Test
	public void testGetOriginalText() {
		ITranslateQuestion p = new TranslateQuestion(FROM, TRANSLATEDQUESTION,
				ORIGINALQUESTION, INDEX);
		assertTrue(p.getOriginalText().equals(ORIGINALQUESTION));
	}

	@Test
	public void testIsNoTranslation() {
		ITranslateQuestion p = new TranslateQuestion(FROM, ORIGINALQUESTION,
				ORIGINALQUESTION, INDEX);
		assertTrue(p.isNoTranslation());
	}

	@Test
	public void testSetTranslatedText() {
		ITranslateQuestion p = new TranslateQuestion(FROM, ORIGINALQUESTION,
				ORIGINALQUESTION, INDEX);
		assertTrue(p.getTranslatedText().equals(p.getOriginalText()));
		p.setTranslatedText(TRANSLATEDQUESTION);
		assertTrue(p.getTranslatedText().equals(TRANSLATEDQUESTION)
				&& !p.isNoTranslation());

	}

}
