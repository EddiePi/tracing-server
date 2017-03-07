package info;

/**
 * Created by Eddie on 2017/3/2.
 */
public abstract class Metrics {
    public Metrics() {
        timestamp = System.currentTimeMillis() / 1000;
    }
    public Long timestamp;
    // cpu
    public Double cpuUsage = 0.0D;

    // memory
    public Long execMemoryUsage = 0L;
    public Long storeMemoryUsage = 0L;

    public void reset() {
        timestamp = System.currentTimeMillis() / 1000;
        cpuUsage = 0.0D;
        execMemoryUsage = 0L;
        storeMemoryUsage = 0L;
    }

    public void plus(Metrics otherMetrics) {
        this.cpuUsage += otherMetrics.cpuUsage;
        this.execMemoryUsage += otherMetrics.execMemoryUsage;
        this.storeMemoryUsage += otherMetrics.storeMemoryUsage;
    }

    public void fraction(Double rate) {
        this.cpuUsage *= rate;
        this.execMemoryUsage = new Double(this.execMemoryUsage * rate).longValue();
        this.storeMemoryUsage = new Double(this.storeMemoryUsage * rate).longValue();
    }

    public abstract Metrics clone();
}
