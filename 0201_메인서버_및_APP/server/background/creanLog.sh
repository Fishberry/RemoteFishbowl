#!/bin/sh

LOGDIR=/var/log
GZIP_DAY=2
DEL_DAY=3

cd $LOGDIR
echo "cd $LOGDIR"

/usr/bin/find . -type f -name "daemon.log" -mtime +$GZIP_DAY -exec /usr/bin/gzip {} \; 2> /dev/null
/usr/bin/find . -type f -name "*.gz" -mtime +$DEL_DAY -exec /bin/rm -f {} \; 2> /dev/null

