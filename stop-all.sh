#!/bin/bash
/home/eddie/tracing-server/stop.sh
while read LINE
do
	ssh $LINE "/home/eddie/tracing-server/stop.sh"
done < slaves
wait
exit 0
