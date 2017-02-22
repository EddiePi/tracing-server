package Server;

import RPCService.SparkMonitor;
import docker.DockerMonitor;
import info.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Eddie on 2017/2/21.
 */
public class Tracer {
    public ConcurrentMap<String, App> applications = new ConcurrentHashMap<>();

    public SparkMonitor sm;
    public ConcurrentMap<String, DockerMonitor> containerIdToDM = new ConcurrentHashMap<>();

    public Metrics getTaskMetric(Task t) {
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
        if (a.tasks.containsKey(taskId)) {
            return a.tasks.get(taskId);
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

    public void printTaskInfo() {
        for(App app: applications.values()) {
            for(Task task: app.tasks.values()) {
                task.printTaskMetrics();
            }
        }
    }
}
