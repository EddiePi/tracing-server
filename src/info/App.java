package info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Eddie on 2017/1/25.
 */
public class App {
    public String appId;
    public Map<Integer, Job> jobIdToJob;
    private ConcurrentMap<Long, Task> tasks;
    private Map<Long, Task> tasksToReport;
    public boolean hasRunningTask = false;

    TimeStamps appStamps;

    public App(String appId) {
        this.appId = appId;

        jobIdToJob = new HashMap<>();
        tasks = new ConcurrentHashMap<>();
        tasksToReport = new HashMap<>();
        appStamps = new TimeStamps();
    }

    public synchronized void addOrUpdateTask(Task task) {
        tasks.put(task.taskId, task);
        Task newReportingTask = tasksToReport.get(task.taskId);
        if (newReportingTask == null) {
            newReportingTask = task;
            newReportingTask.metrics
        }
        tasksToReport.put(task.taskId, task);
        if (!hasRunningTask) {
            hasRunningTask = true;
        }
        //tasksToReport.put(task.taskId, task);
    }

    // we don't want other class to change tasks map. so clone it.
    public Map<Long, Task> getAllTasks() {
        Map<Long, Task> taskClone = new HashMap<>(tasks);
        return taskClone;
    }

    public Map<Long, Task> getReportingTasks() {
        Map<Long, Task> taskClone = new HashMap<>(tasksToReport);
        tasksToReport.clear();
        return taskClone;
    }

    public Task getTaskbyId(Long taskId) {
        return tasks.get(taskId);
    }

    // TODO all work related to the abstract phase (stage, job) will be refined later.
    public boolean addJob(Job jobInfo) {
        if (!jobIdToJob.containsKey(jobInfo.jobId)) {
            jobIdToJob.put(jobInfo.jobId, jobInfo);
            return true;
        }
        return false;
    }

    public void updateJob(Job jobInfo) {
        jobIdToJob.put(jobInfo.jobId, jobInfo);
    }

    public Job getJobById (Integer jobId) {
        Job job = jobIdToJob.get(jobId);
        return job;
    }

    public List<Job> getAllJobs() {
        return new ArrayList<>(jobIdToJob.values());
    }
}

