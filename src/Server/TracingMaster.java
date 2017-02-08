package Server;

import RPCService.TracingService;
import RPCService.TracingServiceImpl;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;

/**
 * Created by Eddie on 2017/1/23.
 */
public class TracingMaster {
    public static void main(String argv[]) throws Exception {
        try {
            System.out.println("Tracing server start ....");
            TProcessor tprocessor = new TracingService.Processor<TracingService.Iface>
                    (new TracingServiceImpl());

            TServerSocket serverTransport = new TServerSocket(8089);
            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(serverTransport);
            tArgs.processor(tprocessor);
            tArgs.protocolFactory(new TBinaryProtocol.Factory());
            // tArgs.protocolFactory(new TCompactProtocol.Factory());
            // tArgs.protocolFactory(new TJSONProtocol.Factory());
            TServer server = new TThreadPoolServer(tArgs);
            server.serve();

        } catch (Exception e) {
            System.out.println("Tracing Server start error!");
            e.printStackTrace();
        }
    }
}
