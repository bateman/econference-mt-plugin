package test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.rosuda.JRI.Rengine;

import com.neuralnoise.stats.R;
import com.neuralnoise.stats.Stat;

import junit.framework.TestCase;

public class Chi2TestCase extends TestCase {

	public void testChi2() {
		Rengine re = R.init(new String[0]);
		
		List<Integer> yb = new LinkedList<Integer>();
		
		yb.add(1);
		yb.add(2);
		yb.add(3);
		
		List<Integer> nb = new LinkedList<Integer>();
		
		nb.add(4);
		nb.add(5);
		nb.add(6);
		
		Map<String, List<Integer>> qties = new HashMap<String, List<Integer>>();
		
		Stat.chi2IndependenceTest(re, qties);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(Chi2TestCase.class);
	}

}
