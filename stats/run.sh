#!/bin/bash

R_HOME=/usr/lib/R
export R_HOME

R_SHARE_DIR=/usr/share/R/share
export R_SHARE_DIR

R_INCLUDE_DIR=/usr/share/R/include
export R_INCLUDE_DIR

R_DOC_DIR=/usr/share/R/doc
export R_DOC_DIR

rm -f results/*.png
java -Xmx1024m -Djava.library.path=$R_HOME/site-library/rJava/jri -cp $R_HOME/site-library/rJava/jri/JRI.jar:bin:../timing/bin:../timing/lib/* com.neuralnoise.stats.Main $@ > run.out 2>&1
