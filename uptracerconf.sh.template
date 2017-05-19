#!/bin/bash
export TRACINGSERVER_HOME=/home/eddie/tracing-server

for i in disco-00{12..19}
do
	scp  ./conf/tracer.conf eddie@$i:$TRACINGSERVER_HOME/conf.tracer.conf
done
wait
exit 0

