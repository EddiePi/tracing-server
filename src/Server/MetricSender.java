package Server;

import info.StageMetrics;
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
    Socket socket = new Socket("192.168.32.120", 2003);
    Writer writer = new OutputStreamWriter(socket.getOutputStream());

    public MetricSender() throws IOException {
    }

    public void sendTaskMetrics(Task task) {
        try {
            List<String> metrics = buildTaskMetric(task);
            for(String sentMessage: metrics) {
                writer.write(sentMessage);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendStageMetrics(StageMetrics sm) {
        try {
            List<String> metrics = buildStageMetric(sm);
            for(String sentMessage: metrics) {
                writer.write(sentMessage);
            }
            writer.flush();
        } catch (IOException e) {
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
        TaskMetrics metricsToSend = task.taskMetrics.get(task.taskMetrics.size() - 1);
        taskPrefix = SPARK_PREFIX + task.appId + "." + "job_" + task.jobId + "." +
                "stage_" + task.stageId + "." + "task_" + task.taskId + ".";
        timeStampSeg = metricsToSend.timestamp.toString();

        // cpu usage string
        pathSeg = taskPrefix + "CPU";
        valueSeg = df.format(metricsToSend.cpuUsage);
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        // execution mem string
        pathSeg = taskPrefix + "execution-memory";
        valueSeg = metricsToSend.execMemoryUsage.toString();
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        // storage mem string
        pathSeg = taskPrefix + "storage-memory";
        valueSeg = metricsToSend.storeMemoryUsage.toString();
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        return metricsStr;
    }

    private List<String> buildStageMetric(StageMetrics metrics) {
        List<String> metricsStr = new ArrayList<>();
        String stagePrefix;
        String pathSeg;
        String valueSeg;
        String timeStampSeg;
        DecimalFormat df = new DecimalFormat("0.000");
        stagePrefix = SPARK_PREFIX + metrics.appId + "." + "job_" + metrics.jobId + "." +
                "stage_" + metrics.stageId + ".";
        timeStampSeg = metrics.timestamp.toString();

        // cpu usage string
        pathSeg = stagePrefix + "CPU";
        valueSeg = df.format(metrics.cpuUsage);
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        // execution mem string
        pathSeg = stagePrefix + "execution-memory";
        valueSeg = metrics.execMemoryUsage.toString();
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        // storage mem string
        pathSeg = stagePrefix + "storage-memory";
        valueSeg = metrics.storeMemoryUsage.toString();
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");
    }


}