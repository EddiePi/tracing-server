package docker;

import Utils.ShellCommandExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 2017/2/15.
 */
public class DockerMonitor {

    private String dockerId;
    String containerId;
    // NOTE: this type is String
    String dockerPid = null;
    private String blkioPath;
    private String netFilePath;
    private MonitorThread monitorThread;

    private Long previousProfileTime = System.currentTimeMillis() / 1000;
    // docker metrics
    // disk metrics
    private Long totalDiskReadBytes = 0L;
    private Long previousDiskReadBytes = 0L;
    private Long totalDiskWriteBytes = 0L;
    private Long previousDiskWriteBytes = 0L;
    private Double currentDiskReadRate = 0.0;
    private Double currentDiskWriteRate = 0.0;

    // network metrics
    private String ifaceName;
    private Long totalNetReceiveBytes = 0L;
    private Long previousNetReceiveByte = 0L;
    private Long totalNetTransmitBytes = 0L;
    private Long previousNetTransmitBytes = 0L;
    private Double currentNetReceiveRate = 0.0;
    private Double currentNetTransmitRate = 0.0;

    public DockerMonitor(String containerId) {
        this.containerId = containerId;
        this.dockerId = runShellCommand("docker inspect --format={{.Id}} " + containerId);
        this.dockerPid = runShellCommand("docker inspect --format={{.State.Pid}} " + containerId).trim();
        System.out.println("docker pid: " + dockerPid);
        this.blkioPath= "/sys/fs/cgroup/blkio/docker/" + dockerId + "/";
        this.netFilePath = "/proc/" + dockerPid + "/net/dev";
        setIfaceName(null);

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
            this.ifaceName = "eth0";
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
                printStatus();
                //if we come here it means we need to sleep for 2s
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    //do nothing
                }
            }
            isRunning = false;
        }


        private void updateCgroupValues() {
            // calculate the disk rate
            calculateCurrentDiskRate();

            // calculate the network rate
            calculateCurrentNetRate();
        }

        // calculate the disk I/O rate
        private void calculateCurrentDiskRate() {
            // init timestamps
            Long curTime = System.currentTimeMillis() / 1000;
            Double deltaTime = (curTime - previousProfileTime) * 1.0;
            previousProfileTime = curTime;

            // read data from file
            getDiskServicedBytes();

            // calculate rate
            Long deltaRead = totalDiskReadBytes - previousDiskReadBytes;
            currentDiskReadRate = deltaRead / deltaTime;
            Long deltaWrite = totalDiskWriteBytes - previousDiskWriteBytes;
            currentDiskWriteRate = deltaWrite / deltaTime;
        }

        // read the disk usages from cgroup files
        // and update the metrics in the monitor.
        private void getDiskServicedBytes() {
            if(!isRunning) {
                return;
            }
            String url = blkioPath + "blkio.throttle.io_service_bytes";
            List<String> readLines = readFileLines(url);
            if (readLines != null) {
                String readStr = readLines.get(0).split(" ")[2];
                previousDiskReadBytes = totalDiskReadBytes;
                totalDiskReadBytes = Long.parseLong(readStr);


                String writeStr = readLines.get(1).split(" ")[2];
                previousDiskWriteBytes = totalDiskWriteBytes;
                totalDiskWriteBytes = Long.parseLong(writeStr);
            }
        }
        // calculate the network I/O rate
        private void calculateCurrentNetRate() {
            Long curTime = System.currentTimeMillis() / 1000;
            Double deltaTime = (curTime - previousProfileTime) * 1.0;
            previousProfileTime = curTime;

            getNetServicedBytes();

            Long deltaReceive = totalNetReceiveBytes - previousNetReceiveByte;
            currentNetReceiveRate = deltaReceive / deltaTime;
            Long deltaTransmit = totalNetTransmitBytes - previousNetTransmitBytes;
            currentNetTransmitRate = deltaTransmit / deltaTime;
        }

        // read the network usage from 'proc' files
        // and update the metrics in the monitor.
        private void getNetServicedBytes() {
            if (!isRunning) {
                return;
            }
            String result = runShellCommand("cat " + netFilePath + " | grep " + ifaceName);
            System.out.println(result);
            if (result != null) {
                String receiveStr = result.split("\t")[1];
                previousNetReceiveByte = totalNetReceiveBytes;
                totalNetReceiveBytes = Long.parseLong(receiveStr);

                String transmitStr = result.split("\t")[9];
                previousNetTransmitBytes = totalNetTransmitBytes;
                totalNetReceiveBytes = Long.parseLong(transmitStr);
            }
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
        System.out.print("total read: " + totalDiskReadBytes +
        " total write: " + totalDiskWriteBytes +
        " read rate: " + currentDiskReadRate +
        " write rate: " + currentDiskWriteRate + "\n" +
        " total receive: " + totalNetReceiveBytes +
        " total transmit: " + totalNetTransmitBytes +
        " receive rate: " + currentNetReceiveRate +
        " transmit rate: " + currentNetTransmitRate + "\n");
    }
}
