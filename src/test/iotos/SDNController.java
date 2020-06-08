package test.iotos;

import it.unibo.deis.lia.ramp.RampEntryPoint;
import it.unibo.deis.lia.ramp.core.e2e.BoundReceiveSocket;
import it.unibo.deis.lia.ramp.core.e2e.E2EComm;
import it.unibo.deis.lia.ramp.core.internode.Dispatcher;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerClient.ControllerClient;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerService.ControllerService;
import it.unibo.deis.lia.ramp.service.management.ServiceManager;

public class SDNController{
    
    static ControllerService controllerService;

    static ControllerClient controllerClient;

	static RampEntryPoint ramp;
	
	static int countClient = 3;


    public static void main(String[] args){
        
        System.out.println("================================");
        System.out.println("SDN Controller starting...");
        System.out.println("version : 2020-05-28");
        System.out.println("================================");

        ramp = RampEntryPoint.getInstance(true, null);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (ramp != null && controllerService != null) {
						System.out.println("ShutdownHook is being executed: gracefully stopping RAMP...");
						controllerService.stopService();
						ramp.stopRamp();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
        }));
        
        controllerService = ControllerService.getInstance();
		controllerService.setCountClient(countClient);
		

        try {
			Thread.sleep(5*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		controllerClient = ControllerClient.getInstance();
		
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

		controllerService.displayGraph();

		while(true){
			try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(controllerService.getActiveClients().size() >= countClient){
				if(controllerService.checkTopoComplete()){
					break;
				}
			}
		}		
	}
}