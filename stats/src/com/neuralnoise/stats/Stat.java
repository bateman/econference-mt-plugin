package com.neuralnoise.stats;

import java.util.*;

import org.rosuda.JRI.*;

class Stat implements RMainLoopCallbacks {

	public static boolean VERBOSE = false;

	private static String integersToString(List<Integer> a) {
		StringBuffer ret = new StringBuffer();
		boolean first = true;
		for (Integer d : a) {
			if (!first) {
				ret.append(", ");
			}
			ret.append(d);
			first = false;
		}
		return ret.toString();
	}

	private static Map<Integer, Integer> frequencies(List<Integer> vals) {
		Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
		for (Integer v : vals) {
			if (ret.containsKey(v)) {
				ret.put(v, ret.get(v) + 1);
			} else {
				ret.put(v, 1);
			}
		}
		return ret;
	}
	
	public static void histogram(Rengine re, List<Integer> v, String pngname) {
		re.eval("png(file=\"" + pngname + "\")");
		String toEval = "hist(sort(c(" + integersToString(v) + ")))";
		re.eval(toEval);
		re.eval("dev.off()");
	}
	
	public static void boxplot(Rengine re, List<List<Integer>> vals, String pngname) {
		re.eval("png(file=\"" + pngname + "\")");
		String toEval = "boxplot(";
		boolean first = true;
		for (List<Integer> v : vals) {
			toEval += (first ? "" : ", ") + "sort(c(" + integersToString(v) + "))";
			first = false;
		}
		toEval += ")";
		re.eval(toEval);
		re.eval("dev.off()");
	}
	
	public static void scatter(Rengine re, List<List<Integer>> vals, String pngname) {
		re.eval("png(file=\"" + pngname + "\")");
		String toEval = "plot(";
		boolean first = true;
		for (List<Integer> v : vals) {
			toEval += (first ? "" : ", ") + "sort(c(" + integersToString(v) + "))";
			first = false;
		}
		toEval += ")";
		re.eval(toEval);
		re.eval("dev.off()");
	}
	
	private static Double tTest(Rengine re, List<Integer> a, List<Integer> b, String type, boolean paired) {
		String toEval = "t.test(c(" + integersToString(a) + "), c(" + integersToString(b) + "), " +
		"paired=" + (paired ? "TRUE" : "FALSE") + ", alternative=\"" + type + "\")";
		REXP x = re.eval(toEval);
		RVector v = x.asVector();
		REXP pexp = (REXP) v.get(2);
		return new Double(pexp.asDouble());
	}

	public static Double pairedTTestLess(Rengine re, List<Integer> a, List<Integer> b) {
		return tTest(re, a, b, "less", true);
	}

	public static Double pairedTTestGreater(Rengine re, List<Integer> a, List<Integer> b) {
		return tTest(re, a, b, "greater", true);
	}

	public static Double unpairedTTestLess(Rengine re, List<Integer> a, List<Integer> b) {
		return tTest(re, a, b, "less", false);
	}

	public static Double unpairedTTestGreater(Rengine re, List<Integer> a, List<Integer> b) {
		return tTest(re, a, b, "greater", false);
	}
	
	public void rWriteConsole(Rengine re, String text, int oType) {
		System.out.print(text);
	}

	public void rBusy(Rengine re, int which) {
		System.out.println("rBusy(" + which + ")");
	}

	public String rReadConsole(Rengine re, String prompt, int addToHistory) {
		return prompt;
	}

	public void rShowMessage(Rengine re, String message) {
		System.out.println("rShowMessage \"" + message + "\"");
	}

	public String rChooseFile(Rengine re, int newFile) {
		return null;
	}

	public void rFlushConsole(Rengine re) { }

	public void rLoadHistory(Rengine re, String filename) { }

	public void rSaveHistory(Rengine re, String filename) { }
}
