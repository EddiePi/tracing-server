package RPCService;

import org.apache.thrift.TException;

/**
 * Created by Eddie on 2017/1/23.
 */
public class TracingServiceImpl implements TracingService.Iface{

    @Override
    public void updateTaskInfo(TaskInfo task) throws TException {

    }

    @Override
    public void updateJobInfo(JobInfo job) throws TException {

    }

    @Override
    public void updateStageInfo(StageInfo stage) throws TException {

    }

    @Override
    public void notifyCommonEvent(SchedulerEvent event) throws TException {

    }

    @Override
    public void notifyTaskEndEvent(TaskEndEvent event) throws TException {

    }
}
