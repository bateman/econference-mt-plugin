/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2006 - 2012 Collaborative Development Group - Dipartimento di Informatica, 
 *                    University of Bari, http://cdg.di.uniba.it
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.apertium.api.translate.internal.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import it.uniba.di.cdg.xcore.ui.formatter.RichFormatting;

import java.util.HashMap;
import java.util.Map;

import org.apertium.api.ApertiumXMLRPCClient;
import org.apertium.api.exceptions.ApertiumXMLRPCClientException;
import org.apertium.api.translate.Translator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TranslatorMock {

	public static String BOLD = RichFormatting.BOLD_MARKER.getCode();
	public static String UNDERLINE = RichFormatting.UNDERLINE_MARKER.getCode();

	@Before
	public void setUp() {
	}

	@Test
	public void test1() throws ApertiumXMLRPCClientException {
		
		//Setup
		ApertiumXMLRPCClient apertium_mock = mock(ApertiumXMLRPCClient.class);

		String test_text1 = "Lorem " + BOLD + "ipsum" + BOLD
				+ " dolor sit amet, consectetur "
				+ "adipisici elit, sed eiusmod tempor incidunt ut labore "
				+ "et dolore magna aliqua. Ut enim ad " + BOLD + "minim" + BOLD
				+ " veniam, quis "
				+ "nostrud exercitation ullamco laboris nisi ut aliquid ex "
				+ "ea commodi consequat.";

		String[] tmp = {"Lorem " , "ipsum" ,
				 " dolor sit amet, consectetur "
				+ "adipisici elit, sed eiusmod tempor incidunt ut labore "
				+ "et dolore magna aliqua. Ut enim ad " , "minim" , " veniam, quis "
				+ "nostrud exercitation ullamco laboris nisi ut aliquid ex "
				+ "ea commodi consequat." };
		
		
		//Stub
		for (String t : tmp) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("translation", t.toUpperCase());
			when(apertium_mock.translate(t, "src", "dest")).thenReturn(map);

		}		
		
		//Exercise
		Translator te = new Translator();
		String res = te.getRichStyleTranslate(test_text1, "src", "dest", apertium_mock);		

		// verify	
		for (String t : tmp) {
			verify(apertium_mock).translate(t, "src", "dest");
		}
		Assert.assertEquals(test_text1.toUpperCase(), res);

		
	}
	
	
	@Test
	public void test2() throws ApertiumXMLRPCClientException {
		
		//Setup
		ApertiumXMLRPCClient apertium_mock = mock(ApertiumXMLRPCClient.class);

		String test_text1 = BOLD+"Lorem ipsum" + BOLD
				+ " dolor sit amet, consectetur "
				+ "adipisici elit, sed eiusmod tempor incidunt ut labore "
				+ "et dolore magna aliqua. Ut enim ad " + BOLD + "minim" + BOLD
				+ " veniam, quis "
				+ "nostrud exercitation ullamco laboris nisi ut aliquid ex "
				+ "ea commodi consequat." + BOLD;

		String[] tmp = {"Lorem ipsum" ,
				 " dolor sit amet, consectetur "
				+ "adipisici elit, sed eiusmod tempor incidunt ut labore "
				+ "et dolore magna aliqua. Ut enim ad " , "minim" ,
				 " veniam, quis "
				+ "nostrud exercitation ullamco laboris nisi ut aliquid ex "
				+ "ea commodi consequat.","" };
		
		
		//Stub
		for (String t : tmp) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("translation", t.toUpperCase());
			when(apertium_mock.translate(t, "src", "dest")).thenReturn(map);

		}		
		
	
		
		//Exercise
		Translator te = new Translator();
		String res = te.getRichStyleTranslate(test_text1, "src", "dest", apertium_mock);		

		// verify	
		for (String t : tmp) {
			verify(apertium_mock).translate(t, "src", "dest");
		}
		Assert.assertEquals(test_text1.toUpperCase(), res);

		
	}
	
	
	
	@Test
	public void test3() throws ApertiumXMLRPCClientException {
		
		//Setup
		ApertiumXMLRPCClient apertium_mock = mock(ApertiumXMLRPCClient.class);

		String test_text1 = BOLD;

		String[] tmp = {"" };
		
		
		//Stub
		for (String t : tmp) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("translation", t.toUpperCase());
			when(apertium_mock.translate(t, "src", "dest")).thenReturn(map);

		}		
		//Exercise
		Translator te = new Translator();
		String res = te.getRichStyleTranslate(test_text1, "src", "dest", apertium_mock);		

		// verify	
		for (String t : tmp) {
			verify(apertium_mock).translate(t, "src", "dest");
		}
		Assert.assertEquals(test_text1.toUpperCase(), res);

		
	}
	
	@Test
	public void test4() throws ApertiumXMLRPCClientException {
		
		//Setup
		ApertiumXMLRPCClient apertium_mock = mock(ApertiumXMLRPCClient.class);
	
		
		String test_text1 = "prova\nprova2";

		String[] tmp = { "prova" ,"prova2"
				 };
		
		
		//Stub
		for (String t : tmp) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("translation", t.toUpperCase());
			when(apertium_mock.translate(t, "src", "dest")).thenReturn(map);

		}		
		//Exercise
		Translator te = new Translator();
		String res = te.getRichStyleTranslate(test_text1, "src", "dest", apertium_mock);		

		// verify	
		for (String t : tmp) {
			verify(apertium_mock).translate(t, "src", "dest");
		}
		Assert.assertEquals(test_text1.toUpperCase(), res);

		
	}
	
	

	@Test
	public void test5() throws ApertiumXMLRPCClientException {
		
		//Setup
		ApertiumXMLRPCClient apertium_mock = mock(ApertiumXMLRPCClient.class);

		String test_text1 = BOLD+"Lorem ipsum" + BOLD
				+ " dolor sit amet, consectetur "
				+ "adipisici elit, sed "+"\n"+" eiusmod tempor incidunt ut labore "
				+ "et dolore magna aliqua. Ut enim ad " + BOLD + "minim" + BOLD
				+ " veniam, quis "
				+ "nostrud exercitation ullamco laboris nisi ut aliquid ex "
				+ "ea commodi consequat." + BOLD;

		String[] tmp = { "Lorem ipsum" ,
				 " dolor sit amet, consectetur "
				+ "adipisici elit, sed "," eiusmod tempor incidunt ut labore "
				+ "et dolore magna aliqua. Ut enim ad " , "minim" , " veniam, quis "
				+ "nostrud exercitation ullamco laboris nisi ut aliquid ex "
				+ "ea commodi consequat." ,""};
		
		
		
		//Stub
		for (String t : tmp) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("translation", t.toUpperCase());
			when(apertium_mock.translate(t, "src", "dest")).thenReturn(map);

		}		
		//Exercise
		Translator te = new Translator();
		String res = te.getRichStyleTranslate(test_text1, "src", "dest", apertium_mock);		

		// verify	
		for (String t : tmp) {
			verify(apertium_mock).translate(t, "src", "dest");
		}
		Assert.assertEquals(test_text1.toUpperCase(), res);

		
	}
	
	
	@Test
	public void test6() throws ApertiumXMLRPCClientException {
		
		//Setup
		ApertiumXMLRPCClient apertium_mock = mock(ApertiumXMLRPCClient.class);

		String test_text1 = BOLD+"Lorem ipsum" + BOLD
				+ " dolor sit amet, consectetur "
				+ "adipisici elit, sed "+"\n"+" eiusmod tempor incidunt ut labore "
				+ "et dolore "+"\t"+"magna aliqua. Ut enim ad " + BOLD + "minim" + BOLD
				+ " veniam, quis "
				+ "nostrud exercitation"+"\r"+"ullamco laboris nisi ut aliquid ex "
				+ "ea commodi consequat." + BOLD;

		String[] tmp = { "Lorem ipsum" , " dolor sit amet, consectetur "
				+ "adipisici elit, sed "," eiusmod tempor incidunt ut labore "
				+ "et dolore ","magna aliqua. Ut enim ad " , "minim" , " veniam, quis "
				+ "nostrud exercitation","ullamco laboris nisi ut aliquid ex "
				+ "ea commodi consequat." ,""};
		
		
		
		//Stub
		for (String t : tmp) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("translation", t.toUpperCase());
			when(apertium_mock.translate(t, "src", "dest")).thenReturn(map);

		}		
		//Exercise
		Translator te = new Translator();
		String res = te.getRichStyleTranslate(test_text1, "src", "dest", apertium_mock);		

		// verify	
		for (String t : tmp) {
			verify(apertium_mock).translate(t, "src", "dest");
		}
		Assert.assertEquals(test_text1.toUpperCase(), res);

		
	}
	



}

