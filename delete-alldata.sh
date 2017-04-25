#!/bin/bash
rm -rf /home/eddie/tracing-server/data
for i in disco-00{12..19}
do
	ssh $i "rm -rf /home/eddie/tracing-server/data"
done
wait
exit 0
