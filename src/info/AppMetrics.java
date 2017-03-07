package info;

/**
 * Created by Eddie on 2017/3/6.
 */
public class AppMetrics extends Metrics {
    public String appId;
    public AppMetrics(String appId) {
        super();
        this.appId = appId;
    }

    @Override
    public Metrics clone() {
        AppMetrics amclone = new AppMetrics(this.appId);
        amclone.timestamp = this.timestamp;
        amclone.cpuUsage = this.cpuUsage;
        amclone.execMemoryUsage = this.execMemoryUsage;
        amclone.storeMemoryUsage = this.storeMemoryUsage;
        return null;
    }
}
