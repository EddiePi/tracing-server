#!/bin/bash
export TRACINGSERVER_HOME=/home/eddie/tracing-server

for i in disco-00{12..19}
do
	scp -r ./out/ eddie@$i:$TRACINGSERVER_HOME/
done
wait
exit 0

