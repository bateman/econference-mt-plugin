package com.neuralnoise.atd;

import java.util.*;

public class Error {
	public String string = null;
	public String description = null;
	public String precontext = null;
	public List<String> suggestions = null;
	public String type = null;
	public String url = null;
	
	public String sentence = null;
	public String translation = null;
	public String engine = null;
	
	public Error(String s, String d, String p, List<String> ss, String t,
			String u) {
		string = s;
		description = d;
		precontext = p;
		suggestions = ss;
		type = t;
		url = u;
	}

	public Error() {

	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");

		ret.append(this.getClass().getName() + " Object {" + NEW_LINE);
		ret.append(" String: " + string + NEW_LINE);
		ret.append(" Description: " + description + NEW_LINE);
		ret.append(" Precontext: " + precontext + NEW_LINE);
		ret.append(" Suggestions: " + suggestions + NEW_LINE);
		ret.append(" Type: " + type + NEW_LINE);
		
		if (sentence != null) {
			ret.append(" Sentence: " + sentence + NEW_LINE);
		}
		
		if (translation != null) {
			ret.append(" Translation: " + translation + NEW_LINE);
		}
		
		if (engine != null) {
			ret.append(" Engine: " + engine + NEW_LINE);
		}
		
		
		ret.append("}");

		return ret.toString();
	}
	
	public String getCompact() {
		StringBuilder ret = new StringBuilder();
		ret.append("{" + string + "}[" + description + "]");
		return ret.toString();
	}
}
