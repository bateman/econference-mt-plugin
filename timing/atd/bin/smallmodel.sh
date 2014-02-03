#!/bin/bash
# 
# Create a language model for low-memory AtD
#
rm -f models/model.zip
rm -rf tmp
java -Dfile.encoding=UTF-8 -Xmx3840M -XX:+AggressiveHeap -XX:+UseParallelGC -jar lib/sleep.jar utils/bigrams/buildsmallmodel.sl
cd tmp
zip -r ../models/model.zip . 1>/dev/null 2>/dev/null
cd ..
rm -rf tmp
