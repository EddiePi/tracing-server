package Server;

import docker.DockerMetrics;
import info.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 2017/3/2.
 * This class is used by 'Tracer' and 'DockerMonitor'.
 */
public class MetricSender {
    private TracerConf conf = TracerConf.getInstance();
    static String SPARK_PREFIX = "spark.";
    String databaseHost = conf.getStringOrDefault("tracer.database.host", "192.168.32.120");
    Integer databasePort = conf.getIntegerOrDefault("tracer.database.port", 2003);
    Socket socket = new Socket(databaseHost, databasePort);
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

    public void sendJobMetrics(JobMetrics jm) {
        try {
            List<String> metrics = buildJobMetrics(jm);
            for(String sentMessage: metrics) {
                writer.write(sentMessage);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAppMetrics(AppMetrics am) {
        try {
            List<String> metrics = buildAppMetrics(am);
            for(String sentMessage: metrics) {
                writer.write(sentMessage);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDockerMetrics(DockerMetrics dm) {
        try {
            List<String> metrics = buildDockerMetrics(dm);
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

        return metricsStr;
    }

    private List<String> buildJobMetrics(JobMetrics metrics) {
        List<String> metricsStr = new ArrayList<>();
        String jobPrefix;
        String pathSeg;
        String valueSeg;
        String timeStampSeg;
        DecimalFormat df = new DecimalFormat("0.000");
        jobPrefix = SPARK_PREFIX + metrics.appId + "." + "job_" + metrics.jobId + ".";
        timeStampSeg = metrics.timestamp.toString();

        // cpu usage string
        pathSeg = jobPrefix + "CPU";
        valueSeg = df.format(metrics.cpuUsage);
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        // execution mem string
        pathSeg = jobPrefix + "execution-memory";
        valueSeg = metrics.execMemoryUsage.toString();
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        // storage mem string
        pathSeg = jobPrefix + "storage-memory";
        valueSeg = metrics.storeMemoryUsage.toString();
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        return metricsStr;
    }

    private List<String> buildAppMetrics(AppMetrics metrics) {
        List<String> metricsStr = new ArrayList<>();
        String appPrefix;
        String pathSeg;
        String valueSeg;
        String timeStampSeg;
        DecimalFormat df = new DecimalFormat("0.000");
        appPrefix = SPARK_PREFIX + metrics.appId + ".";
        timeStampSeg = metrics.timestamp.toString();

        // cpu usage string
        pathSeg = appPrefix + "CPU";
        valueSeg = df.format(metrics.cpuUsage);
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        // execution mem string
        pathSeg = appPrefix + "execution-memory";
        valueSeg = metrics.execMemoryUsage.toString();
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        // storage mem string
        pathSeg = appPrefix + "storage-memory";
        valueSeg = metrics.storeMemoryUsage.toString();
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        return metricsStr;
    }

    private List<String> buildDockerMetrics(DockerMetrics metrics) {
        List<String> metricsStr = new ArrayList<>();
        String dockerPrefix;
        String pathSeg;
        String valueSeg;
        String timeStampSeg;
        DecimalFormat df = new DecimalFormat("0.000");
        dockerPrefix = SPARK_PREFIX + metrics.containerId + ".";
        timeStampSeg = metrics.timestamp.toString();

        // disk read rate
        pathSeg = dockerPrefix + "disk-read-rate";
        valueSeg = df.format(metrics.diskReadRate);
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        // disk write rate
        pathSeg = dockerPrefix + "disk-write-rate";
        valueSeg = df.format(metrics.diskWriteRate);
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        // net rec rate
        pathSeg = dockerPrefix + "network-receive-rate";
        valueSeg = df.format(metrics.netRecRate);
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        // net trans rate
        pathSeg = dockerPrefix + "network-transfer-rate";
        valueSeg = df.format(metrics.netTransRate);
        metricsStr.add(pathSeg + " " + valueSeg + " " + timeStampSeg + "\n");

        return metricsStr;
    }
}