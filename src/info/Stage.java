package info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eddie on 2017/1/23.
 */
public class Stage {
    public Integer stageId; // required
    public String type; // required
    public Integer jobId; // required
    public String appId; // required
    public Map<Long, Task> taskIdToTask;

    public TimeStamps stageStamps;

    public Stage (int stageId, String type, Integer jobId, String appId) {
        this.stageId = stageId;
        this.type = type;
        this.jobId = jobId;
        this.appId = appId;
        this.taskIdToTask = new HashMap<>();
        this.stageStamps = new TimeStamps();
    }

    public boolean addTask (Task taskInfo) {
        if (!taskIdToTask.containsKey(taskInfo.taskId)) {
            taskIdToTask.put(taskInfo.taskId, taskInfo);
            return true;
        }
        return false;
    }

    public void updateTask (Task taskInfo) {
        taskIdToTask.put(taskInfo.taskId, taskInfo);
    }

    // get a task by its taskId. return null if the task is not in the stage.
    public Task getTaskById (Integer taskId) {
        Task task = taskIdToTask.get(taskId);
        return task;
    }

    //get all tasks belong to this stage.
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskIdToTask.values());
    }
}
