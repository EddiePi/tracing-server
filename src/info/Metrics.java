package info;

/**
 * Created by Eddie on 2017/3/2.
 */
public class Metrics {
    public Metrics() {
        timestamp = System.currentTimeMillis() / 1000;
    }
    public Long timestamp;
    // cpu
    public Double cpuUsage = 0.0D;

    // memory
    public Long execMemoryUsage = 0L;
    public Long storeMemoryUsage = 0L;
}
