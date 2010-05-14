package com.neuralnoise.stats;

import org.rosuda.JRI.Rengine;

public class Main {

	public static void main(String[] args) {
		Rengine re = R.init(args);
		try {
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			re.end();
		}
	}

}
