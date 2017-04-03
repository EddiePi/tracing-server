package MetricsSender;

import info.AppMetrics;
import info.ContainerMetrics;
import info.JobMetrics;
import info.StageMetrics;
import org.python.util.PythonInterpreter;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

/**
 * Created by cwei on 4/3/17.
 */
public class PickleMetricsSender extends MetricsSender {
    PythonInterpreter interpreter = new PythonInterpreter();

    public PickleMetricsSender() {
        interpreter.exec("import socket\nimport struct\nimport pickle\n" +
                "ip_port=('192.168.32.120',2004)\n" +
                "sk=socket.socket()\n" +
                "sk.connect(ip_port)\n");
    }

    @Override
    public void sendContainerMetrics(ContainerMetrics metrics) {
        if (metrics.appId == null || metrics.jobId == null || metrics.stageId == null) {
            System.out.print("container metrics with no appId");
            return;
        }
        String timeStr = metrics.timestamp.toString();
        String pathPrefix = SPARK_PREFIX + metrics.appId + "." + "job_" + metrics.jobId + "." +
                "stage_" + metrics.stageId + "." + metrics.containerId + ".";
        interpreter.exec("tuple=[]");
        interpreter.exec("tuple = tuple +" + buildMessageTuple(
                pathPrefix + "CPU", metrics.cpuUsage.toString(), timeStr));
        interpreter.exec("tuple = tuple +" + buildMessageTuple(
                pathPrefix + "execution-memory", metrics.execMemoryUsage.toString(), timeStr));
        interpreter.exec("tuple = tuple +" + buildMessageTuple(
                pathPrefix + "storage-memory", metrics.storeMemoryUsage.toString(), timeStr));
        interpreter.exec("tuple = tuple +" + buildMessageTuple(
                pathPrefix + "disk-read-rate", metrics.diskReadRate.toString(), timeStr));
        interpreter.exec("tuple = tuple +" + buildMessageTuple(
                pathPrefix + "disk-write-rate", metrics.diskWriteRate.toString(), timeStr));
        interpreter.exec("tuple = tuple +" + buildMessageTuple(
                pathPrefix + "net-receive-rate", metrics.netRecRate.toString(), timeStr));
        interpreter.exec("tuple = tuple +" + buildMessageTuple(
                pathPrefix + "net-transfer-rate", metrics.netTransRate.toString(), timeStr));

        interpreter.exec("payload = pickle.dumps(tuple, protocol=2)");
        interpreter.exec("header = struct.pack(\"!L\", len(payload))");
        interpreter.exec("message = header + payload");
        interpreter.exec("sk.sendall(message)");
    }

    @Override
    public void sendStageMetrics(StageMetrics sm) {

    }

    @Override
    public void sendJobMetrics(JobMetrics jm) {

    }

    @Override
    public void sendAppMetrics(AppMetrics am) {

    }

    private static String buildMessageTuple(String path, String value, String timeStr) {
        String m = "[('" + path + "', (" + timeStr + ", " + value + "))]";
        //System.out.print(m + "\n");
        return m;
    }
}
