package com.neuralnoise.stats;

import java.util.*;
import org.rosuda.JRI.Rengine;
import com.neuralnoise.atd.*;
import com.neuralnoise.timing.*;

public class Main {

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
		
		System.out.println(engine + ": scoresErrs contains " + scoresErrs.size() + " elements");
		System.out.println(engine + ": scoresNoErrs contains " + scoresNoErrs.size() + " elements");
		
		Double ret = Stat.unpairedTTestLess(re, scoresErrs, scoresNoErrs);
		System.out.println("p-value: " + ret);
	}
	
	public static void core(Rengine re) throws Exception {
		ATDClient.ignoreTypes = true;
		
		List<Result> ra = Utils.readResultsCSV("../timing/results/AP.csv", "apertium");
		List<Result> rg = Utils.readResultsCSV("../timing/results/GT.csv", "google");
		
		analyseResults(re, ra, "apertium");
		analyseResults(re, rg, "google");
	}
	
	public static void main(String[] args) {
		Rengine re = R.init(args);
		try {
			core(re);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			re.end();
		}
	}

}
