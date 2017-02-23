package Server;

import RPCService.SparkMonitor;
import docker.DockerMonitor;
import info.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Eddie on 2017/2/21.
 */
public class Tracer {
    public ConcurrentMap<String, App> applications = new ConcurrentHashMap<>();

    public SparkMonitor sm;
    public ConcurrentMap<String, DockerMonitor> containerIdToDM = new ConcurrentHashMap<>();
    private int runningAppCount = 0;
    private class TestTracingRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                updateRunningApp();
                if (runningAppCount > 0) {
                    // TODO update real metrics
                    printTaskInfo();
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    TestTracingRunnable runnable = new TestTracingRunnable();

    Thread tThread = new Thread(runnable);

    public List<Metrics> getTaskMetrics(Task t) {
        return t.metrics;
    }

    private static final Tracer instance = new Tracer();

    private Tracer(){}

    public static Tracer getInstance() {
        return instance;
    }

    // start rpc server
    public void init() {
        sm = new SparkMonitor();
        sm.startServer();
        tThread.start();
    }

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

    public Task getOrCreateTask(String appId, int jobId, int stageId, int stageAttemptId, long taskId) {
        // get task from map if it exist.
        // otherwise create the task.
        App a = getOrCreateApp(appId);
        // quick return if the task exists.
        if (a.getAllTasks().containsKey(taskId)) {
            return a.getAllTasks().get(taskId);
        }
        Job j = getOrCreateJob(a, jobId);
        Stage s = getOrCreateStage(j, stageId);
        Task task = s.getTaskById(taskId);
        if (task == null) {
            task = new Task(taskId, stageId, stageAttemptId, jobId, appId);
            s.updateTask(task);
            a.addOrUpdateTask(task);
        }
        return task;
    }

    public Stage getOrCreateStage(Job job, int stageId) {
        Stage stage = job.getStageById(stageId);
        if (stage == null) {
            stage = new Stage(stageId, job.jobId, job.appId);
            job.updateStage(stage);
        }
        return stage;
    }

    public Job getOrCreateJob(App app, int jobId) {
        Job job = app.getJobById(jobId);
        if (job == null) {
            job = new Job(jobId, app.appId);
            app.updateJob(job);
        }
        return job;
    }

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
        for(App app: applications.values()) {
            Double cpuUsage = 0D;
            Map<Long, Task> taskMap = app.getReportingTasks();
            for(Task task: taskMap.values()) {
                Metrics last = task.metrics.get(task.metrics.size() - 1)
                //task.printTaskMetrics();
                if (last.cpuUsage < 0) {
                    continue;
                }
                cpuUsage +=last.cpuUsage;
            }

            System.out.print("app: " + app.appId + " has" + taskMap.size() + " tasks. " +
                    "cpu usage: " + cpuUsage + "\n");
        }
    }

    private void updateRunningApp() {
        int runningCount = 0;
        for (App app: applications.values()) {
            if (app.hasRunningTask) {
                runningCount++;
            }
        }
        this.runningAppCount = runningCount;
    }
}
