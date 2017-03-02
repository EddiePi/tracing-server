package Server;

import RPCService.SparkMonitor;
import info.Task;
import info.TaskMetrics;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 2017/3/2.
 */
public class MetricSender {

    static String SPARK_PREFIX = "spark.";

    public void sendMetrics(Task task) {
        try (
                Socket socket = new Socket("192.168.32.120", 2003);
                Writer writer = new OutputStreamWriter(socket.getOutputStream());
        ) {
            List<String> metrics = buildTaskMetric(task);
            for(String sentMessage: metrics) {
                writer.write(sentMessage);
            }
            writer.flush();
            Thread.sleep(1000);
        } catch (
                IOException e
                )

        {
            e.printStackTrace();
        } catch (
                InterruptedException e
                )

        {
            e.printStackTrace();
        }
    }

    private List<String> buildTaskMetric(Task task) {
        List<String> metricsStr = new ArrayList<>();
        String taskPrefix;
        String pathSeg;
        String valueSeg;
        String timeStampSeg;
        DecimalFormat df = new DecimalFormat("0.000");
        TaskMetrics metricsToSend = task.metrics.get(task.metrics.size() - 1);
        taskPrefix = SPARK_PREFIX + task.appId + "." + "job_" + task.jobId + "." +
                "stage_" + task.stageId + "." + "task_" + task.taskId + ".";
        timeStampSeg = metricsToSend.timestamp.toString();

        // cpu usage string
        pathSeg = taskPrefix + "CPU";
        valueSeg = metricsToSend.cpuUsage.toString();
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        // execution mem string
        pathSeg = taskPrefix + "execution-memory";
        valueSeg = df.format(metricsToSend.execMemoryUsage);
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        // storage mem string
        pathSeg = taskPrefix + "storage-memory";
        valueSeg = df.format(metricsToSend.storeMemoryUsage);
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        return metricsStr;
    }
}