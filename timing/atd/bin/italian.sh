#
# startup script for AtD web service
#

#!/bin/sh

export PRODUCTION=/home/atd
export ATD_HOME=/home/atd/atd
export LOG_DIR=$ATD_HOME/logs

export LC_CTYPE=en_US.UTF-8
export LANG=en_US.UTF-8

java -server -Datd.lang=it -Dfile.encoding=UTF-8 -Dsleep.pattern_cache_size=8192 -Dbind.interface=127.0.0.1 -Dserver.port=1053 -Xmx3840M -XX:+AggressiveHeap -XX:+UseParallelGC -Dsleep.classpath=$ATD_HOME/lib:$ATD_HOME/service/code -Dsleep.debug=24 -classpath $ATD_HOME/lib/sleep.jar:$PRODUCTION/moconti/moconti.jar:$ATD_HOME/lib/* httpd.Moconti atdconfig.sl
