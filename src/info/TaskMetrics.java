package info;

/**
 * Created by Eddie on 2017/1/25.
 */
public class TaskMetrics extends Metrics {

    public TaskMetrics() {
        super();
    }

    // time
    public Long startTimeStamp;
    public Long finishTimeStamp;

    public String status;   //INIT, RUNNING, SUCCESS, FAILED

    @Override
    public Metrics clone() {
        TaskMetrics tmclone = new TaskMetrics();
        tmclone.timestamp = this.timestamp;
        tmclone.cpuUsage = this.cpuUsage;
        tmclone.execMemoryUsage = this.execMemoryUsage;
        tmclone.storeMemoryUsage = this.storeMemoryUsage;
        tmclone.diskReadBytes = this.diskReadBytes;
        tmclone.diskReadRate = this.diskReadRate;
        tmclone.diskWriteBytes = this.diskWriteBytes;
        tmclone.diskWriteRate = this.diskWriteRate;
        tmclone.netRecBytes = this.netRecBytes;
        tmclone.netRecRate = this.netRecRate;
        tmclone.netTransBytes = this.netTransBytes;
        tmclone.netTransRate = this.netTransRate;
        tmclone.startTimeStamp = this.startTimeStamp;
        tmclone.finishTimeStamp = this.finishTimeStamp;
        tmclone.status = this.status;
        return tmclone;
    }
}
