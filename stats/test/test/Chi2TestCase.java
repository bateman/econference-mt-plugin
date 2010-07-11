package test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.rosuda.JRI.Rengine;

import com.neuralnoise.stats.R;
import com.neuralnoise.stats.Stat;

import junit.framework.TestCase;

public class Chi2TestCase extends TestCase {

	public void testChi2() {
		Rengine re = R.init(new String[] { "-q" });
		
		Random generator = new Random();
		
		List<Integer> yb = new LinkedList<Integer>();
		
		for (int i = 0; i < 100; ++i) {
			yb.add(generator.nextInt(100));
		}
		
		List<Integer> nb = new LinkedList<Integer>();
		
		for (int i = 0; i < 50; ++i) {
			nb.add(generator.nextInt(50));
		}
		
		Map<String, List<Integer>> qties = new TreeMap<String, List<Integer>>();
		
		qties.put("yb", yb);
		qties.put("nb", nb);
		
		Double p = Stat.chi2IndependenceTest(re, qties);
		
		System.out.println("p-value is: " + p);
		
		re.end();
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(Chi2TestCase.class);
	}

}
