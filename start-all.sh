#!/bin/bash

for i in disco-00{12..19}
do
	ssh $i nohup /home/eddie/tracing-server/run.sh
done
wait
exit 0