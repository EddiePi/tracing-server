package Server;

import RPCService.SparkMonitor;
import docker.DockerMetrics;
import docker.DockerMonitor;
import info.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Eddie on 2017/2/21.
 */
public class Tracer {
    private TracerConf conf = TracerConf.getInstance();
    public ConcurrentMap<String, App> applications = new ConcurrentHashMap<>();

    public SparkMonitor sm;
    public ConcurrentMap<String, DockerMonitor> containerIdToDM = new ConcurrentHashMap<>();
    private int runningAppCount = 0;
    private boolean isTest = true;
    Integer reportInterval = conf.getIntegerOrDefault("tracer.report-interval", 1000);
    private class TestTracingRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                updateRunningApp();
                if (runningAppCount > 0) {
                    if (isTest) {
                        printTaskInfo();
                        printHighLevelInfo();
                    } else {
                        sendInfoToDatabase();
                    }
                }
                try {
                    Thread.sleep(reportInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    TestTracingRunnable runnable = new TestTracingRunnable();

    Thread tThread = new Thread(runnable);

    public List<TaskMetrics> getTaskMetrics(Task t) {
        return t.taskMetrics;
    }

    private static final Tracer instance = new Tracer();

    private MetricSender ms;

    private Tracer(){}

    public static Tracer getInstance() {
        return instance;
    }

    // start rpc server
    public void init() {
        sm = new SparkMonitor();
        sm.startServer();
        tThread.start();
        try {
            ms = new MetricSender();
        } catch(IOException e) {

        }
    }


    // this method is always called after the 'getOrCreateTask' is called
    public synchronized void updateTask(Task task) {
        for (App a: applications.values()) {
            if (!a.appId.equals(task.appId)) {
                continue;
            } else {
                a.addOrUpdateTask(task);
                break;
            }
        }
    }

    public Task getOrCreateTask(String appId, int jobId, int stageId,
                                int stageAttemptId, long taskId, String containerId) {
        // get task from map if it exist.
        // otherwise create the task.
        App a = getOrCreateApp(appId);
        // quick return if the task exists.
        if (a.getAllTasks().containsKey(taskId)) {
            return a.getAllTasks().get(taskId);
        }
        Task task = a.getTaskById(jobId, stageId, taskId);
        if (task == null) {
            task = new Task(taskId, stageId, stageAttemptId, jobId, appId, containerId);
            TaskMetrics newTaskMetrics = new TaskMetrics();
            newTaskMetrics.status = "INIT";
            task.taskMetrics.add(newTaskMetrics);
            a.addOrUpdateTask(task);
        }
        return task;
    }

    private void updateTaskDockerInfo(Map<Long, Task> taskMap) {
        Map<String, Integer> containerIdToTaskNumber = new HashMap<>();
        for(Task task: taskMap.values()) {
            if (task.containerId == null) {
                continue;
            }
            Integer taskNum = containerIdToTaskNumber.get(task.containerId);
            if (taskNum == null) {
                containerIdToTaskNumber.put(task.containerId, 1);
            } else {
                containerIdToTaskNumber.put(task.containerId, taskNum + 1);
            }
        }
        if (containerIdToTaskNumber.size() == 0) {
            return;
        }
        for(DockerMonitor dockerMonitor: containerIdToDM.values()) {
            dockerMonitor.updateCgroupValues();
        }
        for(Task task: taskMap.values()) {
            if (task.containerId == null) {
                continue;
            }
            DockerMonitor dm = containerIdToDM.get(task.containerId);
            if (dm == null) {
                continue;
            }
            task.setMetricsFromDocker(dm.getLatestDockerMetrics(),
                    containerIdToTaskNumber.get(task.containerId));
        }
    }

//    public Stage getOrCreateStage(Job job, int stageId) {
//        Stage stage = job.getStageById(stageId);
//        if (stage == null) {
//            stage = new Stage(stageId, job.jobId, job.appId);
//            job.updateStage(stage);
//        }
//        return stage;
//    }
//
//    public Job getOrCreateJob(App app, int jobId) {
//        Job job = app.getJobById(jobId);
//        if (job == null) {
//            job = new Job(jobId, app.appId);
//            app.updateJob(job);
//        }
//        return job;
//    }

    public App getOrCreateApp(String appId) {
        App app;
        if (applications.containsKey(appId)) {
            app = applications.get(appId);
        } else {
            app = new App(appId);
            applications.put(appId, app);
        }
        return app;
    }

    // TEST
    public void printTaskInfo() {
        DecimalFormat df = new DecimalFormat("0.000");
        for(App app: applications.values()) {
            Double cpuUsage = 0D;
            Long execMem = 0L;
            Long storeMem = 0L;
            Double diskReadRate = 0D;
            Double diskWriteRate = 0D;
            Double netRecRate = 0D;
            Double netTransRate = 0D;
            updateTaskDockerInfo(app.getReporingTasks());
            Map<Long, Task> taskMap = app.getAndClearReportingTasks();
            for(Task task: taskMap.values()) {
                System.out.print("task metrics size: " + task.taskMetrics.size());
                for (TaskMetrics m : task.taskMetrics) {
                    if (m.cpuUsage < 0) {
                        continue;
                    }
                    cpuUsage += m.cpuUsage;
                    execMem += m.execMemoryUsage;
                    storeMem += m.storeMemoryUsage;
                    diskReadRate += m.diskReadRate;
                    diskWriteRate += m.diskWriteRate;
                    netRecRate += m.netRecRate;
                    netTransRate += m.netTransRate;

                }
            }
            System.out.print("app: " + app.appId + " has " + taskMap.size() + " tasks. " +
                    "cpu usage: " + df.format(cpuUsage) + " exec mem: " + execMem +
                    " store mem: " + + storeMem + "\n");
            System.out.print("the following info is constructed from docker." +
            " disk read rate: " + diskReadRate + " disk write rate: " + diskWriteRate +
            " net rec rate: " + netRecRate + " net trans rate: " + netTransRate + "\n");
        }
    }

    // TEST
    public void printHighLevelInfo() {
        DecimalFormat df = new DecimalFormat("0.000");
        for (App app: applications.values()) {
            List<StageMetrics> stageMetricsList = app.getAndClearReportingStageMetrics();
            System.out.print("number of stage to report: " + stageMetricsList.size() + "\n");
            for (StageMetrics metrics: stageMetricsList) {
                System.out.print("stages: " + metrics.stageId +
                        " cpu usage: " + df.format(metrics.cpuUsage) + " exec mem: " + metrics.execMemoryUsage +
                        " store mem: " + +metrics.storeMemoryUsage + "\n");

            }
            List<JobMetrics> jobMetricsList = app.getAndClearReportingJobMetrics();
            System.out.print("number of job to report: " + jobMetricsList.size() + "\n");
            for (JobMetrics metrics: jobMetricsList) {
                System.out.print("job: " + metrics.jobId +
                        " cpu usage: " + df.format(metrics.cpuUsage) + " exec mem: " + metrics.execMemoryUsage +
                        " store mem: " + +metrics.storeMemoryUsage + "\n");

            }
            List<AppMetrics> appMetricsList = app.getAndClearReportingAppMetrics();
            System.out.print("number of app to report: " + appMetricsList.size() + "\n");
            for (AppMetrics metrics: appMetricsList) {
                System.out.print("app: " + metrics.appId +
                        " cpu usage: " + df.format(metrics.cpuUsage) + " exec mem: " + metrics.execMemoryUsage +
                        " store mem: " + +metrics.storeMemoryUsage + "\n");
            }
        }
    }

    public void sendInfoToDatabase() {
        for(App app: applications.values()) {
            Map<Long, Task> taskMap = app.getAndClearReportingTasks();
            updateTaskDockerInfo(taskMap);
            for(Task task: taskMap.values()) {
                ms.sendTaskMetrics(task);
            }
            List<StageMetrics> sml = app.getAndClearReportingStageMetrics();
            for(StageMetrics sm: sml) {
                ms.sendStageMetrics(sm);
            }
            List<JobMetrics> jml = app.getAndClearReportingJobMetrics();
            for(JobMetrics jm: jml) {
                ms.sendJobMetrics(jm);
            }
            List<AppMetrics> aml = app.getAndClearReportingAppMetrics();
            for(AppMetrics am: aml) {
                ms.sendAppMetrics(am);
            }
        }
    }

    // TEST
    public void printDockerInfo() {
        for(DockerMonitor dm: containerIdToDM.values()) {
            dm.printStatus();
        }
    }

    private void updateRunningApp() {
        int runningCount = 0;
        for (App app: applications.values()) {
            if (app.hasReportingTask) {
                runningCount++;
            }
        }
        this.runningAppCount = runningCount;
    }
}
