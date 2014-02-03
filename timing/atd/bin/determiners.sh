#
# generate a list of words that should be prefixed with a determiner (usually)
#

java -Dfile.encoding=UTF-8 -Xmx3840M -XX:+AggressiveHeap -XX:+UseParallelGC -jar lib/sleep.jar utils/rules/finddetwanted.sl
