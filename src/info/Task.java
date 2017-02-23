package info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 2017/1/23.
 */
public class Task {
    // these fields are task identifiers
    public Long taskId; // required
    public Integer stageId; // required
    public Integer stageAttemptId; // required
    public Integer jobId; // required
    public String appId; // required
    public String containerId;

    public List<Metrics> metrics;
    public TimeStamps taskStamps;

    public Task (long taskId, int stageId, int stageAttemptId, int jobId, String appId, String containerId) {
        this.taskId = taskId;
        this.stageId = stageId;
        this.stageAttemptId = stageAttemptId;
        this.jobId = jobId;
        this.appId = appId;
        this.containerId = containerId;

        this.metrics = new ArrayList<>();
        this.taskStamps = new TimeStamps();
    }

    public void initTask (long startTime) {
        this.taskStamps.startTimeMillis = startTime;
        Metrics initMetrics = new Metrics();
        initMetrics.status = "RUNNING";
        this.metrics.add(initMetrics);
    }

    //
    public void appendMetrics(Double cpuUsage, Long execMemoryUsage, Long storeMemoryUsage) {
        Metrics m = new Metrics();
        m.cpuUsage = cpuUsage;
        m.execMemoryUsage = execMemoryUsage;
        m.storeMemoryUsage = execMemoryUsage;
        metrics.add(m);
    }

    public void appendMetrics(Metrics m) {
        metrics.add(m);
    }

    public Task clone() {
        Task taskClone = new Task(this.taskId, this.stageId, this.stageAttemptId,
                this.jobId, this.appId, this.containerId);
        taskClone.metrics.addAll(this.metrics);
        taskClone.taskStamps = this.taskStamps.clone();
        return taskClone;
    }
//    // update the task  metrics
//    public void updateTask (long finishTime, double cpuUsage, int peakMemoryUsage, String status) {
//        this.taskStamps.finishTimeMillis = finishTime;
//        this.metrics.cpuUsage = cpuUsage;
//        this.metrics.peakMemoryUsage = peakMemoryUsage;
//        updateTaskStatus(status);
//    }
//
//    // maybe we want to update the status alone
//    public void updateTaskStatus (String status) {
//        this.metrics.status = status;
//    }
//
//    public void updateCpuUsage (double cpuUsage) {
//        this.metrics.cpuUsage = cpuUsage;
//    }
//
//    public void updatePeakMemory (int peakMemoryUsage) {
//        this.metrics.peakMemoryUsage = peakMemoryUsage;
//    }

    // TEST
    public void printTaskMetrics() {
        Metrics last;
        if (metrics.size() > 0) {
            last = metrics.get(metrics.size() - 1);
        } else {
            return;
        }
        System.out.print("task: " + taskId + "start time: " + last.startTimeStamp +
        " cpu: " + last.cpuUsage +
        " exec mem: " + last.execMemoryUsage +
        " store mem: " + last.storeMemoryUsage + "\n" +
        " disk read: " + last.diskReadBytes +
        " disk write: " + last.diskWriteBytes +
        " net rec: " + last.netRecBytes +
        " net trans: " + last.netTransBytes + "\n\n");
    }
}
