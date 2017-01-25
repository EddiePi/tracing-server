package info;

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

    public Metrics metrics;
    public TimeStamps taskStamps;

    public Task (long taskId, int stageId, int stageAttemptId, int jobId, String appId) {
        this.taskId = taskId;
        this.stageId = stageId;
        this.stageAttemptId = stageAttemptId;
        this.jobId = jobId;
        this.appId = appId;

        this.metrics = new Metrics();
        this.taskStamps = new TimeStamps();
    }

    public void initTask (long startTime) {
        this.taskStamps.startTimeMillis = startTime;
        this.metrics.status = "RUNNING";
    }

    // update the task  metrics
    public void updateTask (long finishTime, double cpuUsage, int peakMemoryUsage, String status) {
        this.taskStamps.finishTimeMillis = finishTime;
        this.metrics.cpuUsage = cpuUsage;
        this.metrics.peakMemoryUsage = peakMemoryUsage;
        updateTaskStatus(status);
    }

    // maybe we want to update the status alone
    public void updateTaskStatus (String status) {
        this.metrics.status = status;
    }

    public void updateCpuUsage (double cpuUsage) {
        this.metrics.cpuUsage = cpuUsage;
    }

    public void updatePeakMemory (int peakMemoryUsage) {
        this.metrics.peakMemoryUsage = peakMemoryUsage;
    }
}
