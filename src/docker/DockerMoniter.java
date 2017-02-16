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
public class DockerMoniter {

    private String dockerId;
    String containerId;
    private String blkioPath;
    private String netioPath;
    private MonitorThread moniterThread;

    private Long previousProfileTime = System.currentTimeMillis() / 1000;
    // docker metrics
    // disk metrics
    private Long totalDiskReadBytes = 0L;
    private Long previousDiskReadBytes = 0L;
    private Long totalDiskWriteBytes = 0L;
    private Long previousDiskWriteBytes = 0L;
    private Double currentDiskReadRate = 0.0;
    private Double currentDiskWriteRate = 0.0;

    // TODO: network metrics
    private Long networkUsage = 0L;

    public DockerMoniter(String containerId) {
        this.containerId = containerId;
        this.dockerId = getDockerIdFromContainerId(containerId);
        this.blkioPath= "/sys/fs/cgroup/blkio/docker/" + dockerId + "/";
        // TODO
        // this.netioPath

        moniterThread = new MonitorThread();
    }

    public void start() {
        moniterThread.start();
    }

    public void stop() throws InterruptedException {
        moniterThread.isRunning = false;
        moniterThread.interrupt();
        Thread.sleep(1000);
    }

    public String getDockerIdFromContainerId(String containerId) {
        String command = "docker inspect --format={{.Id}} " + containerId;
        String result = runShellCommand(command);
        return result;
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
            //int count = 3;
            //int index = 0;
            while (isRunning) {
                // monitor the docker info
                updateCgroupValues();
                //if we come here it means we need to sleep for 2s
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isRunning = false;
        }


        private void updateCgroupValues() {
            // calculate the disk rate
            calculateCurrentDiskRate();
        }

        // TODO: change to my own cgroup file
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
                return ;
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

//        public long getCurrentLimitedMemory(){
//            if(!isRunning)
//                return 0;
//            String path = memoryPath+"memory.limit_in_bytes";
//            List<String> readlines=readFileLines(path);
//            if(readlines!=null){
//                limitedMemory = Long.parseLong(readFileLines(path).get(0))/(1024*1024);
//                //LOG.info("get limited memory:"+name+"  "+limitedMemory);
//            }
//            return limitedMemory;
//        }
//
//        //pulled by nodemanager, in termes of M
//        private long getCurrentUsedSwap(){
//            if(!isRunning)
//                return 0;
//
//            String path = memoryPath + "memory.stat";
//            List<String> readlines=readFileLines(path);
//            if(readlines!=null){
//                String SwapString=readlines.get(6);
//                String SwapString1=SwapString.split("\\s++")[1];
//                currentUsedSwap=Long.parseLong(SwapString1)/(1024*1024);
//                //LOG.info("get swap memory:"+name+"  "+currentUsedSwap);
//            }
//            return currentUsedSwap;
//        }



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
}
