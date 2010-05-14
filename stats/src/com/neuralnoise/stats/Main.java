package com.neuralnoise.stats;

import java.util.List;
import org.rosuda.JRI.Rengine;
import com.neuralnoise.atd.*;

public class Main {

	public static void core(Rengine re) throws Exception {
		List<ATDError> errs = ATDClient.getErrors("hello world");
		
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
