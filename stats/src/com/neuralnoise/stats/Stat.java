package com.neuralnoise.stats;

import java.util.*;

import org.rosuda.JRI.*;

class Stat implements RMainLoopCallbacks {

	public static boolean VERBOSE = false;

	public static void main(String[] args) {
		Rengine re = R.init(args);
		try {
			List<Double> la = new LinkedList<Double>();
			List<Double> lb = new LinkedList<Double>();

			la.add(1.0);
			la.add(2.0);
			la.add(3.0);
			la.add(4.0);
			la.add(5.0);
			la.add(6.0);

			lb.add(2.0);
			lb.add(4.0);
			lb.add(3.0);
			lb.add(5.0);
			lb.add(6.0);
			lb.add(1.0);

			Double pl = Stat.pairedTTestLess(re, la, lb);
			Double pg = Stat.pairedTTestGreater(re, la, lb);

			System.out.println(pl + " " + pg);
			
			List<List<Double>> plots = new LinkedList<List<Double>>();
			plots.add(la);
			
			boxplot(re, plots, "x.png");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			re.end();
		}
	}

	private static String doublesToString(List<Double> a) {
		StringBuffer ret = new StringBuffer();
		boolean first = true;
		for (Double d : a) {
			if (!first) {
				ret.append(", ");
			}
			ret.append(d);
			first = false;
		}
		return ret.toString();
	}

	public static void histogram(Rengine re, List<Double> v, String pngname) {
		re.eval("png(file=\"" + pngname + "\")");
		String toEval = "hist(sort(c(" + doublesToString(v) + ")))";
		re.eval(toEval);
		re.eval("dev.off()");
	}
	
	public static void boxplot(Rengine re, List<List<Double>> vals, String pngname) {
		re.eval("png(file=\"" + pngname + "\")");
		String toEval = "boxplot(";
		boolean first = true;
		for (List<Double> v : vals) {
			toEval += (first ? "" : ", ") + "sort(c(" + doublesToString(v) + "))";
			first = false;
		}
		toEval += ")";
		re.eval(toEval);
		re.eval("dev.off()");
	}
	
	private static Double tTest(Rengine re, List<Double> a, List<Double> b, String type, boolean paired) {
		String toEval = "t.test(c(" + doublesToString(a) + "), c(" + doublesToString(b) + "), " +
		"paired=" + (paired ? "TRUE" : "FALSE") + ", alternative=\"" + type + "\")";
		REXP x = re.eval(toEval);
		RVector v = x.asVector();
		REXP pexp = (REXP) v.get(2);
		return new Double(pexp.asDouble());
	}

	public static Double pairedTTestLess(Rengine re, List<Double> a, List<Double> b) {
		return tTest(re, a, b, "less", true);
	}

	public static Double pairedTTestGreater(Rengine re, List<Double> a, List<Double> b) {
		return tTest(re, a, b, "greater", true);
	}

	public static Double unpairedTTestLess(Rengine re, List<Double> a, List<Double> b) {
		return tTest(re, a, b, "less", false);
	}

	public static Double unpairedTTestGreater(Rengine re, List<Double> a, List<Double> b) {
		return tTest(re, a, b, "greater", false);
	}
	
	public void rWriteConsole(Rengine re, String text, int oType) {
		//System.out.print(text);
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
