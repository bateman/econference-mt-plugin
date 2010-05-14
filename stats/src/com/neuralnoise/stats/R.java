package com.neuralnoise.stats;

import org.rosuda.JRI.Rengine;

public class R {
  public static Rengine init(String[] args) {
    //Rengine.DEBUG = 50;
    
    Rengine re = new Rengine(args, false, new Stat());

    if (!Rengine.versionCheck()) {
      System.err.println("Version mismatch: Java files don't match library version.");
      System.exit(-1);
    }

        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            System.exit(-1);
        }
        
        System.out.println("% R loaded.");
        
        //re.eval("library(rJava)", false);
        
        return re;
  }
}