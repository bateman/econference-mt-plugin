package com.neuralnoise.stats;

import java.util.*;

import org.rosuda.JRI.*;

class Stat implements RMainLoopCallbacks {

	public static String doublesToString(List<Double> a) {
		String ret = "";
		boolean first = true;
		for (Double d : a) {
			if (!first) {
				ret += ", ";
			}
			ret += d;
			first = false;
		}

		return ret;
	}

	private static Double pairedTTest(Rengine re, List<Double> a, List<Double> b, String type) {
		Double ret = new Double(-1);

		REXP x = re.eval("t.test(c(" + doublesToString(a) + "), c("
				+ doublesToString(b) + "), paired=TRUE, alternative=\"" + type
				+ "\")");
		
		RVector v = x.asVector();
		REXP pexp = (REXP) v.get(2);

		ret = new Double(pexp.asDouble());

		return ret;
	}

	public static Double pairedTTestLess(Rengine re, List<Double> a, List<Double> b) {
		return pairedTTest(re, a, b, "less");
	}

	public static Double pairedTTestGreater(Rengine re, List<Double> a, List<Double> b) {
		return pairedTTest(re, a, b, "greater");
	}

	public void rWriteConsole(Rengine re, String text, int oType) {
		// System.out.print(text);
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

	public void rFlushConsole(Rengine re) {
	}

	public void rLoadHistory(Rengine re, String filename) {
	}

	public void rSaveHistory(Rengine re, String filename) {
	}
}
