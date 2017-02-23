package docker;

import Utils.ShellCommandExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Eddie on 2017/2/15.
 */
public class DockerMonitor {

    private String dockerId;
    String containerId;
    List<Integer> taksInContainer = new LinkedList<>();

    // NOTE: type of dockerPid is String, NOT int
    String dockerPid = null;
    private String blkioPath;
    private String netFilePath;
    private MonitorThread monitorThread;

    private String ifaceName;
    // docker metrics
    private List<DockerMetrics> metrics;
    int metricsCount = 0;

    public DockerMonitor(String containerId) {
        this.containerId = containerId;
        this.dockerId = runShellCommand("docker inspect --format={{.Id}} " + containerId);
        this.dockerPid = runShellCommand("docker inspect --format={{.State.Pid}} " + containerId).trim();
        System.out.println("docker pid: " + dockerPid);
        this.blkioPath= "/sys/fs/cgroup/blkio/docker/" + dockerId + "/";
        this.netFilePath = "/proc/" + dockerPid + "/net/dev";
        setIfaceName(null);
        metrics = new ArrayList<>();

        monitorThread = new MonitorThread();
    }

    public void start() {
        monitorThread.start();
    }

    public void stop() {
        try {
            monitorThread.isRunning = false;
            monitorThread.interrupt();
        }
        catch (Exception e) {
        }
    }

    public void setIfaceName (String name) {
        if (name != null) {
            this.ifaceName = name;
        } else {
            this.ifaceName = "eno1";
        }
    }

    public String getDockerId() {
        return dockerId;
    }

    public String getDockerPid() {
        return dockerPid;
    }

    // Run a given shell command. return a string as the result
    private String runShellCommand(String command){

        ShellCommandExecutor shExec = null;
        int count = 1;
        while(count < 110){
            //we try 10 times if fails due to device busy
            try {
                shExec = new ShellCommandExecutor(command);
                shExec.execute();

            } catch (IOException e) {
                count++;
                try {
                    Thread.sleep(100*count);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                continue;

            }
            break;
        }
        return shExec.getOutput().trim();
    }

    private class MonitorThread extends Thread {
        private boolean isRunning;

        @Override
        public void run(){
            isRunning = true;
            try {
                Thread.sleep(1100);
            } catch (InterruptedException e) {
                // do nothing
            }
            //int count = 3;
            //int index = 0;
            while (isRunning) {
                // monitor the docker info
                updateCgroupValues();
                // printStatus();
                //if we come here it means we need to sleep for 2s
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    //do nothing
                }
            }
            isRunning = false;
        }


        private void updateCgroupValues() {
            DockerMetrics m = new DockerMetrics();
            // calculate the disk rate
            calculateCurrentDiskRate(m);

            // calculate the network rate
            calculateCurrentNetRate(m);
            metrics.add(m);
            metricsCount++;

            // TEST
            printStatus();
        }

//        private void updatePreviousTime() {
//            previousProfileTime = System.currentTimeMillis() / 1000;
//        }

        // calculate the disk I/O rate
        private void calculateCurrentDiskRate(DockerMetrics m) {
            if (!getDiskServicedBytes(m)) {
                return;
            }
            DockerMetrics previousMetrics = metrics.get(metricsCount - 1);
            // init timestamps
            Double deltaTime = (m.timeStamp - previousMetrics.timeStamp) * 1.0;

            // calculate rate
            Long deltaRead = m.diskReadBytes - previousMetrics.diskReadBytes;
            m.diskReadRate = deltaRead / deltaTime;
            Long deltaWrite = m.diskWriteBytes - previousMetrics.diskWriteBytes;
            m.diskWriteRate = deltaWrite / deltaTime;
        }

        // read the disk usages from cgroup files
        // and update the metrics in the monitor.
        // if it is not running or first read, return false.
        private boolean getDiskServicedBytes(DockerMetrics m) {
            if(!isRunning) {
                return false;
            }
            boolean calRate = true;
            if (metricsCount == 0) {
                calRate = false;
            }

            String url = blkioPath + "blkio.throttle.io_service_bytes";
            List<String> readLines = readFileLines(url);
            if (readLines != null) {
                String readStr = readLines.get(0).split(" ")[2];
                m.diskReadBytes = Long.parseLong(readStr);


                String writeStr = readLines.get(1).split(" ")[2];
                m.diskWriteBytes = Long.parseLong(writeStr);
            }
            return calRate;
        }
        // calculate the network I/O rate
        private void calculateCurrentNetRate(DockerMetrics m) {
            if(!getNetServicedBytes(m)) {
                return;
            }
            DockerMetrics previousMetrics = metrics.get(metricsCount - 1);
            Double deltaTime = (m.timeStamp - previousMetrics.timeStamp) * 1.0;

            Long deltaReceive = m.netReceiveBytes - m.netReceiveBytes;
            m.netReceiveRate = deltaReceive / deltaTime;
            Long deltaTransmit = m.netTransmitBytes - m.netTransmitBytes;
            m.netTransmitRate = deltaTransmit / deltaTime;
        }

        // read the network usage from 'proc' files
        // and update the metrics in the monitor.
        private boolean getNetServicedBytes(DockerMetrics m) {
            if (!isRunning) {
                return false;
            }
            boolean calRate = true;
            if (metricsCount == 0) {
                calRate = false;
            }
            String[] results = runShellCommand("cat " + netFilePath).split("\n");
            String resultLine = null;
            for (String r: results) {
                if (r.matches(".*"+ifaceName+".*")) {
                    resultLine = r;
                    break;
                }
            }

            if (resultLine != null) {
                resultLine = resultLine.trim();
                String receiveStr = resultLine.split("\\s+")[1];
                m.netReceiveBytes = Long.parseLong(receiveStr);

                String transmitStr = resultLine.split("\\s+")[9];
                m.netTransmitBytes = Long.parseLong(transmitStr);
            }
            return calRate;
        }

        private List<String> readFileLines(String path){
            ArrayList<String> results= new ArrayList<String>();
            File file = new File(path);
            BufferedReader reader = null;
            boolean isError=false;
            try {
                reader = new BufferedReader(new FileReader(file));
                String tempString = null;
                int line = 1;
                while ((tempString = reader.readLine()) != null) {
                    results.add(tempString);
                    line++;
                }
                reader.close();
            } catch (IOException e) {
                //if we come to here, then means read file causes errors;
                //if reports this errors mission errors, it means this containers
                //has terminated, but nodemanager did not delete it yet. we stop monitoring
                //here
                if(e.toString().contains("FileNotFoundException")){
                    isRunning=false;
                }
                isError=true;
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }

            if(!isError){
                return results;
            }else{
                return null;
            }
        }
    }

    // TEST
    public void printStatus() {
        if (metricsCount == 0) {
            return;
        }
        DockerMetrics last = metrics.get(metricsCount - 1);
        System.out.print("docker pid: " + dockerPid +
        " total read: " + last.diskReadBytes +
        " total write: " + last.diskWriteBytes +
        " read rate: " + last.diskReadRate +
        " write rate: " + last.diskWriteRate + "\n" +
        "total receive: " + last.netReceiveBytes +
        " total transmit: " + last.netTransmitBytes +
        " receive rate: " + last.netReceiveRate +
        " transmit rate: " + last.netTransmitRate + "\n");
    }
}
