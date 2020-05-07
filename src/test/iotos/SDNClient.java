package test.iotos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import it.unibo.deis.lia.ramp.core.e2e.BoundReceiveSocket;
import it.unibo.deis.lia.ramp.core.e2e.E2EComm;
import it.unibo.deis.lia.ramp.core.e2e.GenericPacket;
import it.unibo.deis.lia.ramp.core.e2e.UnicastPacket;
import it.unibo.deis.lia.ramp.core.internode.sdn.applicationRequirements.ApplicationRequirements;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerClient.ControllerClientInterface;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerClient.controllerServiceDiscoveryPolicy.ControllerServiceDiscoveryPolicy;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerClient.controllerServiceDiscoveryPolicy.controllerServiceDiscoverer.ControllerServiceDiscoverer;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerMessage.ControllerMessage;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerMessage.ControllerMessageRequest;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerMessage.ControllerMessageResponse;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerMessage.MessageType;
import it.unibo.deis.lia.ramp.core.internode.sdn.pathSelection.PathSelectionMetric;
import it.unibo.deis.lia.ramp.core.internode.sdn.pathSelection.pathDescriptors.PathDescriptor;
import it.unibo.deis.lia.ramp.service.management.ServiceResponse;

/**
 * @author u284976
 */
public class SDNClient extends Thread implements ControllerClientInterface{

    /**
     * parameter
     */
    private static final int CONTROL_FLOW_ID = 0;

    private static final int DEFAULT_FLOW_ID = 1;
    
    // flow time to live value 
    private static final int FLOW_PATHS_TTL = 60 * 1000;
    


    /**
     * variable
     */

    // not used
    private Map<Integer, List<PathDescriptor>> flowMulticastNextHops;

    // store <flow, priority>
    private Map<Integer, Integer> flowPriorities;

    private Map<Integer, PathDescriptor> defaultFlowPaths;

    private Map<Integer, PathDescriptor> flowPaths;

    private ControllerServiceDiscoverer controllerServiceDiscoverer;

    private ControllerServiceDiscoveryPolicy controllerServiceDiscoveryPolicy;




    /**
     * function
     */
    @Override
    public List<PathDescriptor> getFlowMulticastNextHops(int flowId){
        return this.flowMulticastNextHops.get(flowId);
    }
    @Override
    public int getFlowPriority(int flowId){
        Integer flowPriority = this.flowPriorities.get(flowId);
        if (flowPriority != null)
            return flowPriority;
        else
            return -1;
    }
    @Override
    public String[] getFlowPath(int destNodeId, int flowId){
        String[] flowPath = null;
        PathDescriptor flowPathDescriptor;
        
        if (flowId == DEFAULT_FLOW_ID)
            flowPathDescriptor = this.defaultFlowPaths.get(destNodeId);
        else
            flowPathDescriptor = this.flowPaths.get(flowId);

        /*
         * If a path exists in the maps for the specified flowId, check TTL and, if valid, return it
         */
        if (flowPathDescriptor != null) {
            long elapsed = System.currentTimeMillis() - flowPathDescriptor.getCreationTime();
            /*
             * If the path is valid, return it
             */
            if (elapsed < FLOW_PATHS_TTL) {
                flowPath = flowPathDescriptor.getPath();
                if (flowId != DEFAULT_FLOW_ID)
                    System.out.println("ControllerClient: entry found for flow " + flowId + ", returning the new flow path");
                else
                    System.out.println("ControllerClient: entry found for default flow, returning the new flow path");
            } else {
                /*
                 * If the path is not valid and the flowId is not the default one, send a request for a new one to the controller
                 */
                if (flowId != DEFAULT_FLOW_ID) {
                    System.out.println("ControllerClient: entry found for flow " + flowId + ", but its validity has expired, sending request to the controller");

                    /*
                     * The path request is not the first one for this application,
                     * so applicationRequirements is null
                     */
                    flowPathDescriptor = sendNewPathRequest(new int[]{destNodeId}, null, null, flowId);
                    flowPath = flowPathDescriptor.getPath();
                } else
                    System.out.println("ControllerClient: entry found for default flow, but its validity has expired, returning null");
            }
        }
        /*
         * If a path doesn't exist in the map for the specified flowId, return null
         */
        else {
            if (flowId != DEFAULT_FLOW_ID)
                System.out.println("ControllerClient: no entry found for flow " + flowId + ", returning null");
            else
                System.out.println("ControllerClient: no entry found for default flow, returning null");
        }
        return flowPath;
    }

    
    @Override
    public PathDescriptor sendNewPathRequest(int destNodeId, PathSelectionMetric pathSelectionMetric) {
        return sendNewPathRequest(new int[]{destNodeId}, null, pathSelectionMetric, GenericPacket.UNUSED_FIELD);
    }

    private PathDescriptor sendNewPathRequest(int[] destNodeIds, ApplicationRequirements applicationRequirements, PathSelectionMetric pathSelectionMetric, int flowId) {
        /*
         * TODO Remove me
         */
        log("sendNewPathRequest");
        
        LocalDateTime localDateTime;
        String timestamp;
        int packetSize;
        int payloadSize;

        PathDescriptor newPath = null;
        BoundReceiveSocket responseSocket;

        /*
         * Controller service has to be found before sending any message
         */
        ServiceResponse serviceResponse = this.controllerServiceDiscoverer.getControllerService();
        if (serviceResponse == null) {
            System.out.println("ControllerClient: controller service not found, cannot bind client socket");
        } else {
            try {
                /*
                 * Use the protocol specified by the controller
                 */
                responseSocket = E2EComm.bindPreReceive(serviceResponse.getProtocol());
                /*
                 * Send a path request to the controller for a certain flowId,
                 * currentDest is also necessary for the search on the controller
                 * side (it can be chosen as the path to be used), as well as destNodeId.
                 */
                ControllerMessageRequest requestMessage = new ControllerMessageRequest(MessageType.PATH_REQUEST, responseSocket.getLocalPort(), destNodeIds, null, applicationRequirements, pathSelectionMetric, flowId);

                E2EComm.sendUnicast(serviceResponse.getServerDest(), serviceResponse.getServerNodeId(), serviceResponse.getServerPort(), serviceResponse.getProtocol(), CONTROL_FLOW_ID, E2EComm.serialize(requestMessage));

                /*
                 * TODO Remove me
                 */
                localDateTime = LocalDateTime.now();
                timestamp = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
                log("PATH_REQUEST sent at: " + timestamp);

                System.out.println("ControllerClient: request for a new path for flow " + flowId + " sent to the controller");

                /*
                 * Wait for ControllerService response
                 */
                GenericPacket gp = null;
                try {
                    gp = E2EComm.receive(responseSocket);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*
                 * TODO Remove me
                 */
                packetSize = E2EComm.objectSizePacket(gp);
                localDateTime = LocalDateTime.now();
                timestamp = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

                if (gp instanceof UnicastPacket) {
                    UnicastPacket up = (UnicastPacket) gp;
                    Object payload = null;
                    try {
                        payload = E2EComm.deserialize(up.getBytePayload());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /*
                     * TODO Remove me
                     */
                    payloadSize = up.getBytePayload().length;

                    if (payload instanceof ControllerMessage) {
                        ControllerMessage controllerMessage = (ControllerMessage) payload;
                        ControllerMessageResponse responseMessage;

                        switch (controllerMessage.getMessageType()) {
                            case PATH_RESPONSE:
                                /*
                                 * TODO Remove me
                                 */
                                log("PATH_RESPONSE received at: " + timestamp + ", responseSize: " + packetSize + ", payloadSize: " + payloadSize);

                                responseMessage = (ControllerMessageResponse) controllerMessage;
                                /*
                                 * Set the received path creation time and add it to the known flow paths.
                                 */
                                newPath = responseMessage.getNewPaths().get(0);
                                if (newPath != null) {
                                    if (flowId != GenericPacket.UNUSED_FIELD) {
                                        System.out.println("ControllerClient: response with a new path for flow " + flowId + " received from the controller");
                                    } else {
                                        System.out.println("ControllerClient: response with a new path received from the controller");
                                    }
                                } else
                                    System.out.println("ControllerClient: null path received from the controller for flow " + flowId);
                                break;
                            default:
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return newPath;
    }

    @Override
    public PathDescriptor sendFixPathRequest(int sourceNodeId, int destNodeId, int flowId){
        LocalDateTime localDateTime;
        String timestamp;
        int packetSize;
        int payloadSize;

        PathDescriptor newPath = null;
        BoundReceiveSocket responseSocket = null;

        /*
         * Controller service has to be found before sending any message
         */
        ServiceResponse serviceResponse = this.controllerServiceDiscoverer.getControllerService();
        if (serviceResponse == null) {
            System.out.println("ControllerClient: controller service not found, cannot bind client socket");
        } else {
            try {
                /*
                 * Use the protocol specified by the controller
                 */
                responseSocket = E2EComm.bindPreReceive(serviceResponse.getProtocol());
                /*
                 * Send a path request to the controller for a certain flowId,
                 * currentDest is also necessary for the search on the controller
                 * side (it can be chosen as the path to be used), as well as destNodeId.
                 */
                ControllerMessageRequest requestMessage = new ControllerMessageRequest(MessageType.FIX_PATH_REQUEST, responseSocket.getLocalPort(), new int[]{destNodeId}, null, null, null, flowId);
                requestMessage.setSourceNodeId(sourceNodeId);

                E2EComm.sendUnicast(serviceResponse.getServerDest(), serviceResponse.getServerNodeId(), serviceResponse.getServerPort(), serviceResponse.getProtocol(), CONTROL_FLOW_ID, E2EComm.serialize(requestMessage));

                /*
                 * TODO Remove me
                 */
                localDateTime = LocalDateTime.now();
                timestamp = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
                log("FIX_PATH_REQUEST sent at: " + timestamp);

                System.out.println("ControllerClient: request for fixing an existing path for flow " + flowId + " sent to the controller");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        GenericPacket gp = null;
        try {
            gp = E2EComm.receive(responseSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * TODO Remove me
         */
        packetSize = E2EComm.objectSizePacket(gp);
        localDateTime = LocalDateTime.now();
        timestamp = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

        if (gp instanceof UnicastPacket) {
            UnicastPacket up = (UnicastPacket) gp;
            Object payload = null;
            try {
                payload = E2EComm.deserialize(up.getBytePayload());
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*
             * TODO Remove me
             */
            payloadSize = up.getBytePayload().length;

            if (payload instanceof ControllerMessage) {
                ControllerMessage controllerMessage = (ControllerMessage) payload;
                ControllerMessageResponse responseMessage;

                switch (controllerMessage.getMessageType()) {
                    case FIX_PATH_RESPONSE:
                        /*
                         * TODO Remove me
                         */
                        log("FIX_PATH_RESPONSE received at: " + timestamp + ", responseSize: " + packetSize + ", payloadSize: " + payloadSize);

                        responseMessage = (ControllerMessageResponse) controllerMessage;
                        /*
                         * Set the received path creation time and add it to the known flow paths.
                         */
                        newPath = responseMessage.getNewPaths().get(0);
                        log("Path: " + Arrays.toString(newPath.getPath()));
                        if (newPath != null) {
                            System.out.println("ControllerClient: response with a fixed path for flow " + flowId + " received from the controller");
                        } else
                            System.out.println("ControllerClient: null fixed path received from the controller for flow " + flowId);
                        break;
                    default:
                        break;
                }
            }
        }
        if (newPath != null)
            return newPath;
        else
            return null;
    }

    /**
     * not used
     */
    @Override
    public int sendOsRoutingUpdatePriorityRequest(int routeId, boolean switchToOsRouting){
        return 0;
    }


    /**
     * not used
     */
    public void log(String message){

    }
    public void logRule(String message){

    }
}