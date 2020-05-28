package test.iotos;

import it.unibo.deis.lia.ramp.RampEntryPoint;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerClient.ControllerClient;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerService.ControllerService;

public class SDNController{
    
    static ControllerService controllerService;

    static ControllerClient controllerClient;

    static RampEntryPoint ramp;


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

        try {
			Thread.sleep(2*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		controllerClient = ControllerClient.getInstance();
		
		try {
			Thread.sleep(2*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


        // controllerService.displayGraph();
    }
}