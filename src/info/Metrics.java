package info;

/**
 * Created by Eddie on 2017/1/25.
 */
public class Metrics {
    // cpu
    public Double cpuUsage;

    // memory
    public Long execMemoryUsage;
    public Long storeMemoryUsage;

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

    // time
    public Long startTimeStamp;
    public Long finishTimeStamp;

    public String status;
}
