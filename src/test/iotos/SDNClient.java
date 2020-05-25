package test.iotos;

import it.unibo.deis.lia.ramp.RampEntryPoint;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerClient.ControllerClient;
import it.unibo.deis.lia.ramp.service.management.ServiceDiscovery;
import it.unibo.deis.lia.ramp.service.management.ServiceResponse;

public class SDNClient{

    static RampEntryPoint ramp;

    static ControllerClient controllerClient;

    public static void main(String[] args){

        System.out.println("================================");
        System.out.println("SDN Client starting...");
        System.out.println("version : 2020-05-18");
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

        
        

        // try {
        //     String[] addresses = new String[1];
        //     addresses[0] = "10.0.0.1";

        //     ServiceResponse service = ServiceDiscovery.findService(addresses, "SDNController");

        //     System.out.println("=====================");
        //     System.out.println(service.toString());
        //     System.out.println("service node iD = " + service.getServerNodeId());
        //     System.out.println("=====================");


        // } catch (Exception e) {
        // }
        

        
    }
}