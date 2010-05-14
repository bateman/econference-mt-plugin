package com.neuralnoise.stats;

import java.util.*;
import org.rosuda.JRI.Rengine;
import com.neuralnoise.atd.*;
import com.neuralnoise.timing.*;

public class Main {

	public static void analyseResults(List<Result> results) {
		List<Result> resultsErrs = new LinkedList<Result>();
		List<Result> resultsNoErrs = new LinkedList<Result>();
		
		List<Double> scoresErrs = new LinkedList<Double>();
		List<Double> scoresNoErrs = new LinkedList<Double>();
		
		for (Result r : results) {
			Double score = new Double(r.rater1 + r.rater2 + r.rater3 + r.rater4);
			
			if (r.errors == null) {
				resultsErrs.add(r);	
				scoresErrs.add(score);
			} else {
				resultsNoErrs.add(r);
				scoresNoErrs.add(score);
			}
		}
	
	}
	
	public static void core(Rengine re) throws Exception {
		ATDClient.ignoreTypes = true;
		
		List<Result> ra = Utils.readResultsCSV("../timing/results/AP.csv", "apertium");
		List<Result> rg = Utils.readResultsCSV("../timing/results/GT.csv", "google");
		
		analyseResults(ra);
		analyseResults(rg);
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
