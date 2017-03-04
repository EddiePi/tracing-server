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
    public ConcurrentMap<Integer, List<StageMetrics>> stageMetricsToReport;
    public boolean hasReportingTask = false;

    TimeStamps appStamps;

    public App(String appId) {
        this.appId = appId;

        jobIdToJob = new HashMap<>();
        tasks = new ConcurrentHashMap<>();
        tasksToReport = new ConcurrentHashMap<>();
        stageMetricsToReport = new ConcurrentHashMap<>();
        appStamps = new TimeStamps();
    }

    public void addOrUpdateTask(Task task) {
        synchronized (this) {
            tasks.put(task.taskId, task);
            Task newReportingTask = tasksToReport.get(task.taskId);
            if (newReportingTask == null) {
                newReportingTask = task.clone();
                newReportingTask.taskMetrics.clear();
                tasksToReport.put(newReportingTask.taskId, newReportingTask);
            }
            newReportingTask.taskMetrics.add(task.taskMetrics.get(task.taskMetrics.size() - 1));
            hasReportingTask = true;

        }
        //tasksToReport.put(task.taskId, task);
    }

    // we don't want other class to change tasks map. so clone it.
    public Map<Long, Task> getAllTasks() {
        Map<Long, Task> taskClone = new HashMap<>(tasks);
        return taskClone;
    }

    private void buildStageMetricsToReport(Map<Long, Task> tasksToReport) {
        for(Task task: tasksToReport.values()) {
            // if this task has been reported we go to next task
            if (task.lastMetrics == null) {
                continue;
            }
            Integer stageId = task.stageId;
            List<StageMetrics> stageMetricsList;
            if (!stageMetricsToReport.containsKey(stageId)) {
                stageMetricsList = new ArrayList<>();
                stageMetricsToReport.put(stageId, stageMetricsList);

            } else {
                stageMetricsList = stageMetricsToReport.get(stageId);
            }
            StageMetrics stageMetrics = new StageMetrics(task.appId, task.jobId, task.stageId);
            stageMetrics.cpuUsage += task.lastMetrics.cpuUsage;
            stageMetrics.execMemoryUsage += task.lastMetrics.execMemoryUsage;
            stageMetrics.storeMemoryUsage += task.lastMetrics.storeMemoryUsage;
            stageMetricsList.add(stageMetrics);
        }
    }

    public List<StageMetrics> getAndClearReportingStageMetrics() {
        synchronized (this) {
            List<StageMetrics> stageMetricsList = new ArrayList<>();
            Map<Integer, List<StageMetrics>> stageMetricsClone = new HashMap<>(stageMetricsToReport);
            stageMetricsToReport.clear();
            for(List<StageMetrics> sml: stageMetricsClone.values()) {
                for(StageMetrics sm: sml) {
                    stageMetricsList.add(sm);
                }
            }
            return stageMetricsList;
        }
    }

    public Map<Long, Task> getAndClearReportingTasks() {
        synchronized (this) {
            Map<Long, Task> taskMapClone = new HashMap<>(tasksToReport);
            buildStageMetricsToReport(taskMapClone);
            tasksToReport.clear();
            hasReportingTask = false;
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

