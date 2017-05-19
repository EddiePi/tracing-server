#!/bin/bash
rm -rf /home/eddie/tracedata/data
for i in disco-00{12..19}
do
	ssh $i "rm -rf /home/eddie/tracedata/data"
done
wait
exit 0
