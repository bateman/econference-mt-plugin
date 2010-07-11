package com.neuralnoise.stats;

import java.util.*;

import org.rosuda.JRI.*;

public class Stat implements RMainLoopCallbacks {

	public static boolean VERBOSE = false;

	public static void main(String[] args) {
		Rengine re = R.init(args);
		try {
			List<Integer> l = new LinkedList<Integer>();
			
			l.add(1);
			l.add(2);
			l.add(2);
			l.add(3);
			
			System.out.println(Stat.frequenciesToString(Stat.frequencies(l)));
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			re.end();
		}
	}
	
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
	
	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
		List<T> list = new ArrayList<T>(c);
		java.util.Collections.sort(list);
		return list;
	}
	
	public static String frequenciesToString(Map<Integer, Integer> f) {
		StringBuffer ret = new StringBuffer();
		boolean first = true;
		for (Integer d : asSortedList(f.keySet())) {
			if (!first) {
				ret.append(", ");
			}
			ret.append("rep(" + d + ", " + f.get(d) + ")");
			first = false;
		}
		return ret.toString();
	}
	
	public static Map<Integer, Integer> frequencies(List<Integer> vals) {
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
		eval(re, "png(file=\"" + pngname + "\")");
		String toEval = "hist(sort(c(" + integersToString(v) + ")))";
		eval(re, toEval);
		eval(re, "dev.off()");
	}
	
	public static void boxplot(Rengine re, List<List<Integer>> vals, String pngname) {
		eval(re, "png(file=\"" + pngname + "\")");
		String toEval = "boxplot(";
		boolean first = true;
		for (List<Integer> v : vals) {
			toEval += (first ? "" : ", ") + "sort(c(" + integersToString(v) + "))";
			first = false;
		}
		toEval += ")";
		eval(re, toEval);
		eval(re, "dev.off()");
	}
	
	public static void scatter(Rengine re, List<List<Integer>> vals, String pngname) {
		eval(re, "png(file=\"" + pngname + "\")");
		String toEval = "plot(";
		boolean first = true;
		for (List<Integer> v : vals) {
			toEval += (first ? "" : ", ") + "sort(c(" + integersToString(v) + "))";
			first = false;
		}
		toEval += ")";
		eval(re, toEval);
		eval(re, "dev.off()");
	}
	
	public static void plotlikert(Rengine re, String freq, String pngname) {
		eval(re, "png(file=\"" + pngname + "\")");
		eval(re, "plot.likert(likert(c(" + freq + "), upper=4, c('Completely Inadequate', 'Poorly Adequate', 'Fairly Adequate', 'Completely Adequate')))");
		eval(re, "dev.off()");
	}
	
	public static void boxplotlikert(Rengine re, String freq1, String freq2, String pngname) {
		eval(re, "png(file=\"" + pngname + "\")");
		eval(re, "boxplot.likert(likert(c(" + freq1 + "), likert(c(" + freq2 + "), upper=4, c('Completely Inadequate', 'Poorly Adequate', 'Fairly Adequate', 'Completely Adequate')))");
		eval(re, "dev.off()");
	}
	
	private static Double tTest(Rengine re, List<Integer> a, List<Integer> b, String type, boolean paired) {
		String toEval = "t.test(c(" + integersToString(a) + "), c(" + integersToString(b) + "), " +
		"paired=" + (paired ? "TRUE" : "FALSE") + ", alternative=\"" + type + "\")";
		REXP x = eval(re, toEval);
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
	
	public static Double chi2IndependenceTest(Rengine re, Map<String, List<Integer>> qties) {
		String frame = "data.frame(";
		boolean first = true;
		for (String key : qties.keySet()) {
			List<Integer> vals = qties.get(key);
			String dimension = key + "=c(" + integersToString(vals) + ")";
			if (!first) {
				frame += ", ";
			}
			frame += dimension;
			first = false;
		}
		frame += ")";
		
		REXP x = eval(re, "chisq.test(" + frame + ")");
		
		RVector v = x.asVector();
		REXP pexp = (REXP) v.get(2);
		
		return new Double(pexp.asDouble());
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
	
	public static REXP eval(Rengine re, String command) {
		System.out.println("Executing: " + command);
		return re.eval(command);
	}
}
