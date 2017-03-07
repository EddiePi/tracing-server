package info;

/**
 * Created by Eddie on 2017/3/4.
 */
public class StageMetrics extends Metrics {
    public String appId;
    public Integer jobId;
    public Integer stageId;

    public StageMetrics(String appId, Integer jobId, Integer stageId) {
        super();
        this.appId = appId;
        this.jobId = jobId;
        this.stageId = stageId;
    }

    @Override
    public Metrics clone() {
        StageMetrics smclone = new StageMetrics(this.appId, this.jobId, this.stageId);
        smclone.timestamp = this.timestamp;
        smclone.cpuUsage = this.cpuUsage;
        smclone.execMemoryUsage = this.execMemoryUsage;
        smclone.storeMemoryUsage = this.storeMemoryUsage;
        return smclone;
    }
}
