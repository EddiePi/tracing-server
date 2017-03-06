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

}
