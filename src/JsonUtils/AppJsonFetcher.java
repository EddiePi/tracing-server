package JsonUtils;

import Server.TracerConf;
import info.App;
import info.Job;
import info.Stage;
import info.Task;

/**
 * Created by Eddie on 2017/3/30.
 */
public class AppJsonFetcher {
    App app;
    TracerConf conf;
    String storagePrefix;
    String urlPrefix;
    String urlSuffix;
    public AppJsonFetcher(TracerConf conf, App app) {
        this.app = app;
        this.conf = conf;
        urlPrefix = conf.getStringOrDefault("tracer.database.host", "localhost") + "/render?target=";
        urlSuffix = "&format=json";
        storagePrefix = conf.getStringOrDefault("tracer.storage.root", "./");
    }

    public void fetch() {
        String appId = app.appId;
        String appURL = urlPrefix + appId;
        String appStoragePath = storagePrefix + appId;
        fetchAllMetrics(appURL, appStoragePath);
        for(Job job: app.getAllJobs()) {
            String jobId = job.jobId.toString();
            String jobURL = appURL + ".job_" + jobId;
            String jobStoragePath = appStoragePath + "/job_" + jobId;
            fetchAllMetrics(jobURL, jobStoragePath);
            for(Stage stage: job.getAllStage()) {
                String stageId = stage.stageId.toString();
                String stageURL = jobURL + ".stage_" + stageId;
                String stageStoragePath = jobStoragePath + "/stage_" + stageId;
                fetchAllMetrics(stageURL, stageStoragePath);
                for(Task task: stage.getAllTasks()) {
                    String taskId = task.taskId.toString();
                    String taskURL = stageURL + ".task_" + taskId;
                    String taskStoragePath = stageStoragePath + "/task_" + taskId;
                    fetchAllMetrics(taskURL, taskStoragePath);
                }
            }
        }
    }

    private void fetchAllMetrics(String prefix, String destPath) {
        for(String name: MetricNames.names) {
            String urlAndName = prefix + "." + name + urlSuffix;
            JsonCopier.copyJsonFromURL(urlAndName, destPath, urlAndName);

            //TEST
            //System.out.print("url, name: " + urlAndName + " destPath: " + destPath + "\n");
        }
    }
}

