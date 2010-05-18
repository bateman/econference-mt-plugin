package com.neuralnoise.stats;

import java.text.DecimalFormat;
import java.util.*;

import org.rosuda.JRI.Rengine;
import com.neuralnoise.atd.*;
import com.neuralnoise.timing.*;

public class Main {

	public static Double avg(List<Double> scores) {
		Double ret = 0.0;
		for (Double score : scores) {
			ret += score;
		}
		return ret / (new Double(scores.size()));
	}

	public static String showDouble(Double val) {
		DecimalFormat formatter = new DecimalFormat("#.########");
		String ret = formatter.format(val).replaceAll(",", ".");
		return ret;
	}

	public static void analyseResults(Rengine re, List<Result> results, String engine) {
		List<Result> resultsErrs = new LinkedList<Result>();
		List<Result> resultsNoErrs = new LinkedList<Result>();
		
		List<Double> scoresErrs = new LinkedList<Double>();
		List<Double> scoresNoErrs = new LinkedList<Double>();
		
		for (Result result : results) {
			Double score = new Double(result.rater1 + result.rater2 + result.rater3 + result.rater4);
			
			if (result.errors.size() == 0) {
				resultsErrs.add(result);	
				scoresErrs.add(score);
			} else {
				resultsNoErrs.add(result);
				scoresNoErrs.add(score);
			}
		}
		
		List<List<Double>> plots = new LinkedList<List<Double>>();
		plots.add(scoresErrs);
		plots.add(scoresNoErrs);
		
		Stat.boxplot(re, plots, "results/" + engine + "_bw.png");
		
		//Stat.histogram(re, scoresErrs, "results/" + engine + "_errs.png");
		//Stat.histogram(re, scoresNoErrs, "results/" + engine + "_noerrs.png");
		
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
			
			ATDClient.ignoreTypes = false;
			core(re);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			re.end();
		}
	}

}
