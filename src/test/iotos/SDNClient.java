package test.iotos;

import it.unibo.deis.lia.ramp.RampEntryPoint;
import it.unibo.deis.lia.ramp.core.e2e.BoundReceiveSocket;
import it.unibo.deis.lia.ramp.core.e2e.E2EComm;
import it.unibo.deis.lia.ramp.core.internode.Dispatcher;
import it.unibo.deis.lia.ramp.core.internode.sdn.applicationRequirements.ApplicationRequirements;
import it.unibo.deis.lia.ramp.core.internode.sdn.applicationRequirements.TrafficType;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerClient.ControllerClient;
import it.unibo.deis.lia.ramp.core.internode.sdn.pathSelection.PathSelectionMetric;
import it.unibo.deis.lia.ramp.service.management.ServiceDiscovery;
import it.unibo.deis.lia.ramp.service.management.ServiceManager;
import it.unibo.deis.lia.ramp.service.management.ServiceResponse;

public class SDNClient{

    static RampEntryPoint ramp;

    static ControllerClient controllerClient;


    public static void main(String[] args){

        System.out.println("================================");
        System.out.println("SDN Client starting...");
        System.out.println("version : 2020-05-28");
        System.out.println("================================");

        ramp = RampEntryPoint.getInstance(true, null);

        /**
         * wait a few second to allow the node to discover 
         * neighbor
         * ex. init. heartbeater
         */
        try{
            Thread.sleep(5*1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        ramp.forceNeighborsUpdate();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ramp != null && controllerClient != null) {
                        System.out.println("ShutdownHook is being executed: gracefully stopping RAMP...");
                        controllerClient.stopClient();
                        ramp.stopRamp();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

        controllerClient = ControllerClient.getInstance();

        System.out.println("========================================");
        System.out.println("get controller client instance done!!!!!");
        System.out.println("========================================");
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            //TODO: handle exception
        }

        /**
         * register service and waiting message by other client
         */
        BoundReceiveSocket applicationSocket = null;
        try{
            applicationSocket = E2EComm.bindPreReceive(E2EComm.UDP);
        }catch(Exception e){
            e.printStackTrace();
        }
        String nodeID = Integer.toString(Dispatcher.getLocalRampId());
        ServiceManager.getInstance(false).registerService(
            "application" + nodeID,             // serviceName
            applicationSocket.getLocalPort(),   // servicePort
            E2EComm.UDP                         // protocol
        );

        System.out.println("========================================");
        System.out.println("register Service to local management done!!");
        System.out.println("========================================");
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            //TODO: handle exception
        }

        // wait controller notice ( topo all edge are measure throughput complete)
        long startTime = 0;
        while (true) {
            startTime = controllerClient.getReadyToTest();
            if(startTime != 0){
                break;
            }

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        
        ServiceResponse appService = null;
        try {
            switch (nodeID) {
                case "25":
                    appService = ServiceDiscovery.findServices(
                        5,
                        "application1",
                        3000,
                        1
                    ).elementAt(0);
                    break;
                case "26":
                    appService = ServiceDiscovery.findServices(
                        5, 
                        "application1",
                        3000,
                        1
                    ).elementAt(0);
                    break;
                case "19":
                    appService = ServiceDiscovery.findServices(
                        5, 
                        "application11",
                        3000,
                        1
                    ).elementAt(0);
                    break;
                case "21":
                    appService = ServiceDiscovery.findServices(
                        5, 
                        "application13",
                        3000,
                        1
                    ).elementAt(0);
                    break;
                case "10":
                    appService = ServiceDiscovery.findServices(
                        5, 
                        "application1",
                        3000,
                        1
                    ).elementAt(0);
                    break;
                case "24":
                    appService = ServiceDiscovery.findServices(
                        5, 
                        "application14",
                        3000,
                        1
                    ).elementAt(0);
                    break;
                case "23":
                    appService = ServiceDiscovery.findServices(
                        5, 
                        "application10",
                        3000,
                        1
                    ).elementAt(0);
                    break;
                case "16":
                    appService = ServiceDiscovery.findServices(
                        5, 
                        "application26",
                        3000,
                        1
                    ).elementAt(0);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("========================================");
        System.out.println("find Service done!!!");
        if(appService != null){
            for(String s : appService.getServerDest()){
                System.out.println(s);
            }
        }
        System.out.println("========================================");

        ApplicationRequirements applicationRequirements = null;
        // if(nodeID.equals("2")){
        //     applicationRequirements = new ApplicationRequirements(
        //         TrafficType.VIDEO_STREAM,   // trafficType
        //         1,                          // payloadSize
        //         5,                          // GenPacketPerSeconds
        //         1000.0,                     // requireDelay
        //         2000.0,                     // requireThroughput
        //         300                         // duration
        //     );
        // }else if(nodeID.equals("3")){
        //     applicationRequirements = new ApplicationRequirements(
        //         TrafficType.FILE_TRANSFER,  // trafficType
        //         100,                        // payloadSize
        //         1,                          // GenPacketPerSeconds
        //         3000.0,                     // requireDelay
        //         2000.0,                     // requireThroughput
        //         300                         // duration
        //     );
        // }
        switch (nodeID) {
            case "25":
                applicationRequirements = new ApplicationRequirements(
                    TrafficType.FILE_TRANSFER,
                    16,
                    500,
                    2000.0,
                    8000.0,
                    300
                );
                break;
            case "26":
                applicationRequirements = new ApplicationRequirements(
                    TrafficType.FILE_TRANSFER,
                    20,
                    400,
                    2000.0,
                    8000.0,
                    300
                );
                break;
            case "19":
                applicationRequirements = new ApplicationRequirements(
                    TrafficType.FILE_TRANSFER,
                    16,
                    500,
                    2000.0,
                    8000.0,
                    300
                );
                break;
            case "21":
                applicationRequirements = new ApplicationRequirements(
                    TrafficType.FILE_TRANSFER,
                    16,
                    300,
                    2000.0,
                    8000.0,
                    300
                );
                break;
            case "10":
                applicationRequirements = new ApplicationRequirements(
                    TrafficType.VIDEO_STREAM,
                    16,
                    300,
                    2000.0,
                    4800.0,
                    300
                );
                break;
            case "24":
                applicationRequirements = new ApplicationRequirements(
                    TrafficType.VIDEO_STREAM,
                    20,
                    200,
                    2000.0,
                    4000.0,
                    300
                );
                break;
            case "23":
                applicationRequirements = new ApplicationRequirements(
                    TrafficType.VIDEO_STREAM,
                    20,
                    200,
                    2000.0,
                    4000.0,
                    300
                );
                break;
            case "16":
                applicationRequirements = new ApplicationRequirements(
                    TrafficType.VIDEO_STREAM,
                    8,
                    300,
                    2000.0,
                    1600.0,
                    300
                );
                break;
            default:
                break;
        }

        int[] destNodeID = {appService.getServerNodeId()};
        int[] destNodePort = {appService.getServerPort()};

        while (System.currentTimeMillis() < startTime) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                //TODO: handle exception
            }
        }

        System.out.println("========================================");
        System.out.println("send request to SDNController!!!!!");
        System.out.println("========================================");
        int flowID = controllerClient.getFlowId(
            applicationRequirements,
            destNodeID,
            destNodePort,
            PathSelectionMetric.GENETIC_ALGO
        );
        System.out.println("========================================");
        System.out.println("receive response!!!!!");
        System.out.println("========================================");
        String[] path = controllerClient.getFlowPath(appService.getServerNodeId(), flowID);

        for(String s : path){
            System.out.println(s);
        }
        System.out.println("========================================");
        System.out.println("process done!!!!!");
        System.out.println("========================================");
    }
}