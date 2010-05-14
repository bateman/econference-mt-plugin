#!/bin/bash

java -cp lib/*:bin com.neuralnoise.timing.Utils $@ > utils.out 2>&1
