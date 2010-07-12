package com.neuralnoise.stats;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import org.rosuda.JRI.Rengine;
import com.neuralnoise.atd.*;
import com.neuralnoise.timing.*;

public class Main {

	public static Double avg(List<Integer> scores) {
		Double ret = 0.0;
		for (Integer score : scores) {
			ret += score;
		}
		return ret / (new Double(scores.size()));
	}

	public static String showDouble(Double val) {
		DecimalFormat formatter = new DecimalFormat("#.########");
		String ret = formatter.format(val).replaceAll(",", ".");
		return ret;
	}
	
	public static void showStatsAboutErrors(List<Result> results) {
		List<ATDError> errors = new LinkedList<ATDError>();
		
		for (Result r : results) {
			if (r.errors != null) {
				for (ATDError e : r.errors) {
					errors.add(e);
				}
			}
		}
		
		Map<String, Integer> stats = new HashMap<String, Integer>();
		
		for (ATDError e : errors) {
			if (stats.containsKey(e.type)) {
				stats.put(e.type, stats.get(e.type) + 1);
			} else {
				stats.put(e.type, 1);
			}
			//System.out.println(e);
		}
		
		for (String t : stats.keySet()) {
			System.out.println("\t" + t + " -> " + stats.get(t));
		}
	}

	public static void analyseResults(Rengine re, List<Result> results, String engine) throws IOException {
		Integer adequateWithErrors = 0;
		Integer inadequateWithErrors = 0;
		
		Integer adequateWithoutErrors = 0;
		Integer inadequateWithoutErrors = 0;
		
		for (Result result : results) {
			System.out.print(result.toCSV());
			
			Integer scoresSum = result.rater1 + result.rater2 + result.rater3 + result.rater4;
			if (result.errors.size() > 0) {
				if (scoresSum < 10) {
					adequateWithErrors += 1;
				} else {
					inadequateWithErrors += 1;
				}
			} else {
				if (scoresSum < 10) {
					adequateWithoutErrors += 1;
				} else {
					inadequateWithoutErrors += 1;
				}
			}
		}
		
		System.out.println("Engine: " + engine);
		
		System.out.println("Adequate With Errors: " + adequateWithErrors);
		System.out.println("Indequate With Errors: " + inadequateWithErrors);
		
		System.out.println("Adequate Without Errors: " + adequateWithoutErrors);
		System.out.println("Indequate Without Errors: " + inadequateWithoutErrors);
	}
	
	public static void _analyseResults(Rengine re, List<Result> results, String engine) {
		List<Result> resultsErrs = new LinkedList<Result>();
		List<Result> resultsNoErrs = new LinkedList<Result>();
		
		List<Integer> scoresErrs = new LinkedList<Integer>();
		List<Integer> scoresNoErrs = new LinkedList<Integer>();
		
		List<Integer> allScoresErrs = new LinkedList<Integer>();
		List<Integer> allScoresNoErrs = new LinkedList<Integer>();
		
		for (Result result : results) {			
			Integer score = new Integer(result.rater1 + result.rater2 + result.rater3 + result.rater4);
			
			if (result.errors.size() > 0) {
				resultsErrs.add(result);
				scoresErrs.add(score);
				
				allScoresErrs.add(result.rater1);
				allScoresErrs.add(result.rater2);
				allScoresErrs.add(result.rater3);
				allScoresErrs.add(result.rater4);
				
			} else {
				resultsNoErrs.add(result);
				scoresNoErrs.add(score);
				
				allScoresNoErrs.add(result.rater1);
				allScoresNoErrs.add(result.rater2);
				allScoresNoErrs.add(result.rater3);
				allScoresNoErrs.add(result.rater4);
				
			}
		}
		
		showStatsAboutErrors(resultsErrs);
		
		List<List<Integer>> plots = new LinkedList<List<Integer>>();
		
		plots.add(scoresErrs);
		plots.add(scoresNoErrs);
		
		Stat.boxplot(re, plots, "results/" + engine + "_boxplot.png");
		
		String allScoresErrsFreq = Stat.frequenciesToString(Stat.frequencies(allScoresErrs));
		Stat.plotlikert(re, allScoresErrsFreq, "results/" + engine + "_err_plotlikert.png");
		
		String allScoresNoErrsFreq = Stat.frequenciesToString(Stat.frequencies(allScoresNoErrs));
		Stat.plotlikert(re, allScoresNoErrsFreq, "results/" + engine + "_noerr_plotlikert.png");
		
		System.out.println(engine + ": scoresErrs contains " + scoresErrs.size() + " elements, avg: " + avg(scoresErrs));
		System.out.println(engine + ": scoresNoErrs contains " + scoresNoErrs.size() + " elements, avg: " + avg(scoresNoErrs));
		
		Double less = Stat.unpairedTTestLess(re, scoresErrs, scoresNoErrs);
		Double greater = Stat.unpairedTTestGreater(re, scoresErrs, scoresNoErrs);
		
		System.out.println("p-values: less " + showDouble(less) + ", greater " + showDouble(greater));
	}
	
	public static void core(Rengine re) throws Exception {		
		List<Result> ra = Utils.readResultsCSV("../timing/results/AP.csv", "apertium");
		List<Result> rg = Utils.readResultsCSV("../timing/results/GT.csv", "google");
		
		analyseResults(re, ra, "apertium");
		analyseResults(re, rg, "google");
	}
	
	public static void main(String[] args) {
		Rengine re = R.init(args);
		try {

			re.eval("library(monash)");
			
			ATDClient.ignoreTypes = false;
			core(re);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			re.end();
		}
	}

}
