#!/bin/bash
/home/cwei/tracing-server/stop.sh
while read LINE
do
	ssh $i "/home/cwei/tracing-server/stop.sh"
done < slaves
wait
exit 0
