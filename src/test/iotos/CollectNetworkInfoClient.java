package test.iotos;

import java.util.HashMap;
import java.util.Vector;

import it.unibo.deis.lia.ramp.RampEntryPoint;
import it.unibo.deis.lia.ramp.core.e2e.BoundReceiveSocket;
import it.unibo.deis.lia.ramp.core.e2e.E2EComm;
import it.unibo.deis.lia.ramp.service.management.ServiceDiscovery;
import it.unibo.deis.lia.ramp.service.management.ServiceManager;
import it.unibo.deis.lia.ramp.service.management.ServiceResponse;

/**
 * @author u284976
 */

public class CollectNetworkInfoClient{
    private static RampEntryPoint ramp;
    private static BoundReceiveSocket clientSocket;
    private static HashMap<String, ServiceResponse> availableClients;

    public static void main(String[] args){
        ramp = RampEntryPoint.getInstance(true, null);

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ramp.forceNeighborsUpdate();

        try{
            clientSocket = E2EComm.bindPreReceive(E2EComm.TCP);
        } catch (Exception e){
            System.out.println("bind Pre Receive socket failed!");
        }
        

        ServiceManager.getInstance(false).registerService(
            "AssistantClientListenr",       // serviceName
            clientSocket.getLocalPort(),    // servicePort
            E2EComm.TCP                     // protocol
        );

        Vector<ServiceResponse> services = null;
        while(true){
            try{
                services = ServiceDiscovery.findServices(
                    1,                          // TTL
                    "AssistantClientListenr",   // serviceName
                    2000,                       // timeout
                    30                          // serviceAmount
                );
            } catch(Exception e){
                e.printStackTrace();
            }

            availableClients = new HashMap<>();
            availableClients.clear();

            for (ServiceResponse service : services) {
                String[] keys = service.getServerDest();
                String key = keys[0];

                if(service.getServerNodeId() != ramp.getNodeId() && !availableClients.containsKey(key)){
                    availableClients.put(key, service);
                }
            }

            System.out.println();
            System.out.println("===============");
            for(String key : availableClients.keySet()){
                System.out.println(key);
            }
            try {
                Thread.sleep(10*1000);    
            } catch (Exception e) {
            }
            
        }
        

    }
}