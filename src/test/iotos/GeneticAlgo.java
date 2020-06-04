package test.iotos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.Graphs;
import org.graphstream.graph.implementations.MultiNode;

import it.unibo.deis.lia.ramp.core.internode.sdn.applicationRequirements.ApplicationRequirements;
import it.unibo.deis.lia.ramp.core.internode.sdn.applicationRequirements.TrafficType;
import it.unibo.deis.lia.ramp.core.internode.sdn.controllerService.ControllerService;
import it.unibo.deis.lia.ramp.core.internode.sdn.pathSelection.TopologyGraphSelector;
import it.unibo.deis.lia.ramp.core.internode.sdn.pathSelection.pathDescriptors.PathDescriptor;
import it.unibo.deis.lia.ramp.core.internode.sdn.pathSelection.pathSelectors.BreadthFirstFlowPathSelector;

/**
 * @author u284976
 */

public class GeneticAlgo implements TopologyGraphSelector{
    
    
    private Graph topologyGraph;

    public GeneticAlgo(Graph topologyGraph){
        this.topologyGraph = topologyGraph;
    }

    @Override
    public PathDescriptor selectPath(int sourceNodeId, int destNodeId, ApplicationRequirements applicationRequirements, Map<Integer, PathDescriptor> activePaths){

        PathDescriptor tempPath = new BreadthFirstFlowPathSelector(topologyGraph).selectPath(sourceNodeId, destNodeId, null, null);
        Graph checkGraph = Graphs.clone(topologyGraph);

        Map<Integer,PathDescriptor> flowPaths = activePaths;
        /**
         * GeneticAlgo need other flow requirement to calculate fitness value
         */
        Map<Integer,ApplicationRequirements> flowAR = ControllerService.getInstance().getFlowApplicationRequirements();
        /**
         * flowPath only store passing by nodeID and NetworkInterfaceCard
         * so needed additional request the node that proposed the flow request
         */
        Map<Integer,Integer> flowSources = ControllerService.getInstance().getflowSources();

        // flowID = 1 is mean default flow, it will not duplicate other existing FlowID
        flowPaths.put(1, tempPath);
        flowAR.put(1, applicationRequirements);
        flowSources.put(1, sourceNodeId);

        formalMethod(checkGraph, flowPaths, flowAR, flowSources, sourceNodeId);

        PathDescriptor path = null;
        return path;
    }

    @Override
    public Map<Integer, PathDescriptor> getAllPathsFromSource(int sourceNodeId) {
        // TODO:
        Map<Integer, PathDescriptor> paths = new HashMap<>();
        return paths;
    }
    
    private void formalMethod(Graph tempGraph, Map<Integer,PathDescriptor> flowPaths,
                                Map<Integer,ApplicationRequirements> flowAR,
                                Map<Integer,Integer> flowSources,
                                int sourceNodeId){
        /**
         * add attribute in topo's node
         * 
         * cur : already arrive number of flow on this node, initial is 0
         * total : future total number of flow on this node
         * lamda : Possion process parameter means packet rate
         * n : Possion process parameter means packet length
         */
        for(Node node : tempGraph.getEachNode()){
            // Map<String,Double> nodeState = new HashMap<>();
            // nodeState.put("total", 0.0);
            // nodeState.put("cur", 0.0);
            // nodeState.put("lamda", 0.0);
            // nodeState.put("n", 0.0);
            // node.addAttribute("nodeState", nodeState);
            node.addAttribute("total", 0);
            node.addAttribute("cur", 0);
            node.addAttribute("lamda", 0);
            node.addAttribute("n", 0);
        }

        /**
         * 2020-06-04
         * setup "total" attribute by "flowPath"
         * but flowPath only store target node
         * ex A certain flow about "S" to "D", through M1,M2 like below:
         *   S---->M1---->M2---->D
         * 
         * flowPath only store M1,M2,D these three nodeID and NetworkInterfaceCard
         * so needed add the node that proposed the flow request
         * 
         * 2020-06-05 modify to combine flowSource and flowPath First
         * ===========================================
         * requires attention!!!
         * path.getPathNodeIds() is List<Integer> include Source, ex S,M1,M2,D
         * path.getPath() is String[] "not" include Source only M1,M2,D NetworkInterfaceCard
         */
        for(int flowID : flowPaths.keySet()){
            PathDescriptor path = flowPaths.get(flowID);
            List<Integer> pathNodeID = path.getPathNodeIds();
            pathNodeID.add(0, flowSources.get(flowID));
            path.setPathNodeIds(pathNodeID);
            flowPaths.put(flowID, path);

            for(int nodeID : pathNodeID){
                MultiNode node = tempGraph.getNode(Integer.toString(nodeID));
                int total = (int)node.getAttribute("total");
                total = total + 1;
                node.addAttribute("total", total);
            }
        }
        // version : 2020-06-04
        // for(int key : flowPaths.keySet()){
        //     // by flowPath
        //     for(int nodeID : flowPaths.get(key).getPathNodeIds()){
        //         MultiNode node = tempGraph.getNode(Integer.toString(nodeID));
        //         int total = (int)node.getAttribute("total");
        //         total = total + 1;
        //         node.addAttribute("total", total);
        //     }
        //     // by flowSources
        //     MultiNode node = tempGraph.getNode(Integer.toString(flowSources.get(key)));
        //     int total = (int)node.getAttribute("total");
        //     total = total + 1;
        //     node.addAttribute("total", total);
        // }

        boolean allCalculate = false;
        Map<Integer,Boolean> calculate = new HashMap<>();
        for(int flowID : flowPaths.keySet()){
            calculate.put(flowID, false);
        }
        /**
         * starting and setup all flow source node
         * cur++
         * lamda = combine lamda    <---- see code or paper
         * n = combine n            <---- see code or paper
         */
        for(int flowID : calculate.keySet()){
            // version 2020-06-05
            int source = flowPaths.get(flowID).getPathNodeIds().get(0);
            // version 2020-06-04
            // int source = flowSources.get(flowID);
            String nodeID = Integer.toString(source);
            MultiNode sourceNode = tempGraph.getNode(nodeID);
            int current = (int)sourceNode.getAttribute("cur");
            current = current + 1;
            sourceNode.addAttribute("cur", current);

            double lamda = (double)sourceNode.getAttribute("lamda");
            double flow_lamda = (double)flowAR.get(flowID).getPacketRate();
            double new_lamda = lamda + flow_lamda;

            double n = (double)sourceNode.getAttribute("n");
            double flow_n = (double)flowAR.get(flowID).getPakcetLength();
            double new_n = (flow_lamda*flow_n + lamda*n) / (flow_lamda*flow_n);

            sourceNode.addAttribute("lamda", new_lamda);
            sourceNode.addAttribute("n", new_n);
        }

        while(!allCalculate){
            /**
             * to : evrey not calculated flow
             * do : 
             * flow accumulation
             * flow processing
             * flow propagation 
             */ 
            for(int flowID : calculate.keySet()){
                PathDescriptor path = flowPaths.get(flowID);
                /**
                 * recursive to lastnode - 1, such that for loop execute until last edge 
                 * which is between lastnode-1 and lastnode
                 */
                for(int i=0 ; i<path.getPathNodeIds().size()-1 ; i++){
                    String nodeID = Integer.toString(path.getPathNodeIds().get(i));
                    String nextNodeID = Integer.toString(path.getPathNodeIds().get(i+1));
                    MultiNode node = tempGraph.getNode(nodeID);
                    MultiNode nextNode = tempGraph.getNode(nextNodeID);
                    String nextAddress = path.getPath()[i];

                    // maybe more than one edge between two node
                    for(Edge edge : node.getEdgeSetBetween(nextNode)){

                    }

                }
            }



            allCalculate = true;
            for(int flowID : calculate.keySet()){
                if(calculate.get(flowID) == false){
                    allCalculate = false;
                }else{
                    calculate.remove(flowID);
                }
            }
        }

    }

    private double calculateFitness(ApplicationRequirements applicationRequirement, Map<String,Double> measureResult){
        TrafficType trafficType = applicationRequirement.getTrafficType();
        /**
         * weight of parameter
         * a - for delay
         * b - for throughput
         */
        double a;
        double b;
        if(trafficType.equals(TrafficType.VIDEO_STREAM)){
            a = 1;
            b = 0;
        }else if(trafficType.equals(TrafficType.FILE_TRANSFER)){
            a = 0;
            b = 1;
        }

        // measure_delay must lower than required_delay
        double delay = measureResult.get("delay");
        // measure_throughput mest higher than require_throughput
        double throughput = measureResult.get("throughput");
        double fitness = 0;


        return 0;
    }
}