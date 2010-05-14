package com.neuralnoise.stats;


import java.util.List;
import org.rosuda.JRI.Rengine;
import com.neuralnoise.atd.*;
import com.neuralnoise.timing.*;

public class Main {

	public static void core(Rengine re) throws Exception {
		ATDClient.ignoreTypes = true;
		
		List<Result> ra = Utils.readResultsCSV("./results/AP.csv", "apertium");
		List<Result> rg = Utils.readResultsCSV("./results/GT.csv", "google");
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
