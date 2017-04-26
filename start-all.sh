#!/bin/bash
export TRACINGSERVER_HOME=/home/eddie/tracing-server

$TRACINGSERVER_HOME/run.sh
for i in disco-00{12..19}
do
	ssh $i "/home/eddie/tracing-server/run.sh > $TRACINGSERVER_HOME/anomalies.log 2>&1 &"
done
wait
exit 0
