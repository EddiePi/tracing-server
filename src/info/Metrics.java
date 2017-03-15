package info;

import docker.DockerMetrics;

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

    // disk
    public Long diskWriteBytes;
    public Long diskReadBytes;
    public Long diskWriteRate;
    public Long diskReadRate;

    // network
    public Long netRecBytes;
    public Long netTransBytes;
    public Long netRecRate;
    public Long netTransRate;

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

        this.diskWriteBytes += otherMetrics.diskWriteBytes;
        this.diskReadBytes += otherMetrics.diskReadBytes;
        this.diskWriteRate += otherMetrics.diskWriteRate;
        this.diskReadRate += otherMetrics.diskReadRate;

        this.netRecBytes += otherMetrics.netRecBytes;
        this.netTransBytes += otherMetrics.netTransBytes;
        this.netRecRate += otherMetrics.netRecRate;
        this.netTransRate += otherMetrics.netTransRate;
    }

    public void fraction(Double rate) {
        this.cpuUsage *= rate;

        this.execMemoryUsage = new Double(this.execMemoryUsage * rate).longValue();
        this.storeMemoryUsage = new Double(this.storeMemoryUsage * rate).longValue();

        this.diskWriteBytes = new Double(this.diskWriteBytes * rate).longValue();
        this.diskReadBytes = new Double(this.diskReadBytes * rate).longValue();
        this.diskWriteRate = new Double(this.diskWriteRate * rate).longValue();
        this.diskReadRate = new Double(this.diskReadRate * rate).longValue();

        this.netRecBytes = new Double(this.netRecBytes * rate).longValue();
        this.netTransBytes = new Double(this.netTransBytes * rate).longValue();
        this.netRecRate = new Double(this.netRecRate * rate).longValue();
        this.netTransRate = new Double(this.netTransBytes * rate).longValue();
    }

    public void setInfoFromDockerMetrics(DockerMetrics dockerMetrics, Integer tasksInDocker) {
        Double rate = 1D / tasksInDocker;

        this.diskWriteBytes = new Double(dockerMetrics.diskWriteBytes * rate).longValue();
        this.diskReadBytes = new Double(dockerMetrics.diskReadBytes * rate).longValue();
        this.diskWriteRate = new Double(dockerMetrics.diskWriteRate * rate).longValue();
        this.diskReadRate = new Double(dockerMetrics.diskReadRate * rate).longValue();

        this.netRecBytes = new Double(dockerMetrics.netRecBytes * rate).longValue();
        this.netTransBytes = new Double(dockerMetrics.netTransBytes * rate).longValue();
        this.netRecRate = new Double(dockerMetrics.netRecRate * rate).longValue();
        this.netTransRate = new Double(dockerMetrics.netTransBytes * rate).longValue();
    }


    public abstract Metrics clone();
}
