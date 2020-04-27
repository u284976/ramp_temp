package iotos;

import it.unibo.deis.lia.ramp.RampEntryPoint;

/**
 * @author u284976
 */


public class AssistantController{
    // private static AssistantController assistantController;
    
    private static RampEntryPoint ramp;


    public static void main(String[] args){
        ramp = RampEntryPoint.getInstance(true, null);
        
    }
}