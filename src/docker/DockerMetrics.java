package docker;

/**
 * Created by Eddie on 2017/2/23.
 */
public class DockerMetrics {
    // docker metrics
    // unit: second
    public Long timeStamp;
    // disk metrics
    public Long diskReadBytes = 0L;
    public Long diskWriteBytes = 0L;
    public Double diskReadRate = 0.0;
    public Double diskWriteRate = 0.0;

    // network metrics
    public Long netReceiveBytes = 0L;
    public Long netTransmitBytes = 0L;
    public Double netReceiveRate = 0.0;
    public Double netTransmitRate = 0.0;

    public DockerMetrics() {
        timeStamp = System.currentTimeMillis() / 1000;
    }
}
