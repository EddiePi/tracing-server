package info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eddie on 2017/1/25.
 */
public class App {
    public String appId;
    public Map<Integer, Job> jobIdToJob;
    public Map<Long, Task> tasks;
    public Map<Long, Task> runningTasks;

    TimeStamps appStamps;

    public App(String appId) {
        this.appId = appId;

        jobIdToJob = new HashMap<>();
        tasks = new HashMap<>();
        appStamps = new TimeStamps();
    }

    public void addOrUpdateTask(Task task) {
        tasks.put(task.taskId, task);
        runningTasks.put(task.taskId, task);
    }

    public void removeRunningTask(Long taskId) {
        runningTasks.remove(taskId);
    }

    public Task getTaskbyId(Long taskId) {
        return tasks.get(taskId);
    }

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

