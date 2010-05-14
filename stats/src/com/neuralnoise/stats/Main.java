package com.neuralnoise.stats;

import java.util.*;
import org.rosuda.JRI.Rengine;
import com.neuralnoise.atd.*;
import com.neuralnoise.timing.*;

public class Main {

	public static void analyseResults(List<Result> results) {
		List<Result> resultsContainingErrors = new LinkedList<Result>();
		List<Result> resultsWithoutErrors = new LinkedList<Result>();
		
		for (Result r : results) {
			if (r.errors == null) {
				resultsWithoutErrors.add(r);	
			} else {
				resultsContainingErrors.add(r);
			}
		}
	}
	
	public static void core(Rengine re) throws Exception {
		ATDClient.ignoreTypes = true;
		
		List<Result> ra = Utils.readResultsCSV("./results/AP.csv", "apertium");
		List<Result> rg = Utils.readResultsCSV("./results/GT.csv", "google");
		
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
