package RPCService;

import docker.DockerMonitor;
import org.apache.thrift.TException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eddie on 2017/1/23.
 */
public class TracingServiceImpl implements TracingService.Iface{

    private Map<String, DockerMonitor> dockerMonitorMap = new HashMap<>();

    @Override
    public void updateTaskInfo(TaskInfo task) throws TException {
//        System.out.print("taskId: " + task.taskId +
//                " containerId: " + task.containerId +
//                " stageId: " + task.stageId +
//                " jobId: " + task.jobId +
//                " appId: " + task.appId +
//                " cpu usage: " + task.cpuUsage +
//                " execution memory: " + task.execMemory +
//                " storage memory: " + task.storeMemory +
//                " start time: " + task.startTime +
//                " end time: " + task.finishTime + "\n");
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

    @Override
    public void notifyContainerEvent(ContainerEvent event) throws TException {
        String containerId = event.containerId;
        if (event.action.equals("ADD")) {
            if(!dockerMonitorMap.containsKey(containerId)) {
                DockerMonitor dockerMonitor = new DockerMonitor(containerId);
                dockerMonitor.start();
                dockerMonitorMap.put(containerId, dockerMonitor);
            }
        }

        // TODO
        if (event.action.equals("REMOVE")) {
            if (dockerMonitorMap.containsKey(containerId)) {
                DockerMonitor dockerMonitor = dockerMonitorMap.remove(containerId);
                dockerMonitor.stop();
            }
        }
    }
}
