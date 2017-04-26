#!/bin/bash
export TRACINGSERVER_HOME=/home/eddie/tracing-server

rm $TRACINGSERVER_HOME/anomalies.log
for i in disco-00{12..19}
do
	ssh $i "rm $TRACINGSERVER_HOME/anomalies.log"
done
wait
exit 0
