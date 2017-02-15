package RPCService;

import org.apache.thrift.TException;

/**pingcd admincd .
 * Created by Eddie on 2017/1/23.
 */
public class TracingServiceImpl implements TracingService.Iface{

    @Override
    public void updateTaskInfo(TaskInfo task) throws TException {
        System.out.print("taskId: " + task.taskId +
        " stageId: " + task.stageId +
        " jobId: " + task.jobId +
        " appId: " + task.appId +
        " cpu usage: " + task.cpuUsage +
        " execution memory: " + task.execMemory +
        " storage memory: " + task.storeMemory +
        " start time: " + task.startTime +
        " end time: " + task.finishTime + "\n");
    }

    @Override
    public void updateJobInfo(JobInfo job) throws TException {

    }

    @Override
    public void updateStageInfo(StageInfo stage) throws TException {

    }

    @Override
    public void notifyCommonEvent(SchedulerEvent event) throws TException {
        String message = "SchedulerEvent received. event: " + event.event;
        if (!event.reason.equals("") || event.reason != null) {
            message += " reason: " + event.reason;
        }
        message += "\n";
        System.out.print(message);
    }

    @Override
    public void notifyTaskEndEvent(TaskEndEvent event) throws TException {
        //System.out.print("TaskEndEvent received. taskId: " + event.taskId +
        //" status: " + event.reason + "\n");
    }
}
