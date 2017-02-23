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
    private ConcurrentMap<Long, Task> tasksToReport;
    public boolean hasReportingTask = false;

    TimeStamps appStamps;

    public App(String appId) {
        this.appId = appId;

        jobIdToJob = new HashMap<>();
        tasks = new ConcurrentHashMap<>();
        tasksToReport = new ConcurrentHashMap<>();
        appStamps = new TimeStamps();
    }

    public void addOrUpdateTask(Task task) {
        synchronized (this) {
            tasks.put(task.taskId, task);
            Task newReportingTask = tasksToReport.get(task.taskId);
            if (newReportingTask == null) {
                newReportingTask = task.clone();
                newReportingTask.metrics.clear();
                tasksToReport.put(newReportingTask.taskId, newReportingTask);
            }
            newReportingTask.metrics.add(task.metrics.get(task.metrics.size() - 1));
            if (!hasReportingTask) {
                hasReportingTask = true;
            }
        }
        //tasksToReport.put(task.taskId, task);
    }

    // we don't want other class to change tasks map. so clone it.
    public Map<Long, Task> getAllTasks() {
        Map<Long, Task> taskClone = new HashMap<>(tasks);
        return taskClone;
    }

    public Map<Long, Task> getAndClearReportingTasks() {
        synchronized (this) {
            Map<Long, Task> taskMapClone = new HashMap<>(tasksToReport);
            tasksToReport.clear();
            return taskMapClone;
        }
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

