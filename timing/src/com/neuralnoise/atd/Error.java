package com.neuralnoise.atd;

import java.util.*;

public class Error {
	public String string = null;
	public String description = null;
	public String precontext = null;
	public List<String> suggestions = null;
	public String type = null;
	public String url = null;

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
		StringBuilder result = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");

		result.append(this.getClass().getName() + " Object {" + NEW_LINE);
		result.append(" String: " + string + NEW_LINE);
		result.append(" Description: " + description + NEW_LINE);
		result.append(" Precontext: " + precontext + NEW_LINE);
		result.append(" Suggestions: " + suggestions + NEW_LINE);
		result.append(" Type: " + type + NEW_LINE);
		result.append("}");

		return result.toString();
	}
}
