package com.neuralnoise.stats;

import java.util.*;

import org.rosuda.JRI.*;

class Stat implements RMainLoopCallbacks {
  
  public static void test(Rengine re) {
    List<Double> la = new LinkedList<Double>();
    List<Double> lb = new LinkedList<Double>();
    
    la.add(1.0);
    la.add(2.0);
    la.add(3.0);
    la.add(4.0);
    la.add(5.0);
    la.add(6.0);
    
    lb.add(2.0);
    lb.add(4.0);
    lb.add(3.0);
    lb.add(5.0);
    lb.add(6.0);
    lb.add(1.0);
    
      Double pl = Stat.ttestLess(re, la, lb);
      Double pg = Stat.ttestGreater(re, la, lb);
        
        System.out.println(pl + " " + pg);
  }
  
  public static String doublesToString(List<Double> a) {
    String ret = "";
    boolean first = true;
    for (Double d : a) {
      if (!first) {
        ret += ", ";
      }
      ret += d;
      first = false;
    }
    
    return ret;
  }
  
  public static Double ttest(Rengine re, List<Double> a, List<Double> b, String type) {
    Double ret = new Double(-1);
    
        REXP x = re.eval("t.test(c(" + doublesToString(a) + "), c(" + doublesToString(b) + "), paired=TRUE, alternative=\"" + type + "\")");
        //System.out.println(x);
        
    RVector v = x.asVector();
        REXP pexp = (REXP) v.get(2);
        
        ret = new Double(pexp.asDouble());
    
        return ret;
  }
  
  public static Double ttestLess(Rengine re, List<Double> a, List<Double> b) {
    return ttest(re, a, b, "less");
  }
  
  public static Double ttestGreater(Rengine re, List<Double> a, List<Double> b) {
    return ttest(re, a, b, "greater");
  }
  
    public void rWriteConsole(Rengine re, String text, int oType) {
      //System.out.print(text);
    }
    
    public void rBusy(Rengine re, int which) {
        System.out.println("rBusy("+which+")");
    }
    
    public String rReadConsole(Rengine re, String prompt, int addToHistory) {
    return prompt;
    }
    
    public void rShowMessage(Rengine re, String message) {
        System.out.println("rShowMessage \"" + message + "\"");
    }
  
    public String rChooseFile(Rengine re, int newFile) {
    return null;
    }
    
    public void   rFlushConsole (Rengine re) { }
  
    public void   rLoadHistory  (Rengine re, String filename) { }
    
    public void   rSaveHistory  (Rengine re, String filename) { }

}