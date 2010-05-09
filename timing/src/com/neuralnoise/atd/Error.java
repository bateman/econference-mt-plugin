package com.neuralnoise.atd;

import java.util.*;

public class Error {
	public String string = null;
	public String description = null;
	public String precontext = null;
	public List<String> suggestions = null;
	public String type = null;
	public String url = null;
	
	public Error(String s, String d, String p, List<String> ss, String t, String u) {
		string = s;
		description = d;
		precontext = p;
		suggestions = ss;
		type = t;
		url = u;
	}
}
