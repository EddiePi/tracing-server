package info;

/**
 * Created by Eddie on 2017/1/25.
 */
public class Metrics {

    public Metrics() {
        this.timestamp = System.currentTimeMillis();
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

    // time
    public Long startTimeStamp;
    public Long finishTimeStamp;

    public String status;   //INIT, RUNNING, SUCCESS, FAILED
}
