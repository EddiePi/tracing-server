package info;

/**
 * Created by Eddie on 2017/1/25.
 */
public class TaskMetrics extends Metrics {

    public TaskMetrics() {
        super();
    }
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
