package spam;

import java.io.IOException;



public class mainSPAM {

	public mainSPAM()
	{
		//if SPAM
		
		//if ("SPAM".equals(algorithmName)) {
            AlgoSPAM algo = new AlgoSPAM();
            algo.setMaximumPatternLength(4);//getParamAsInteger(parameters[1])
            
            /*if ("".equals(parameters[1]) == false) {
                }*/
            //algo.runAlgorithm(inputFile, outputFile, getParamAsDouble(parameters[0]));
            algo.printStatistics(); 
            
            
       /*     
       //} 
		//if CM-SPAM
		//else
		//if ("CM-SPAM".equals(algorithmName)) {
        	AlgoCMSPAM algo = new AlgoCMSPAM();
            if ("".equals(parameters[1]) == false) {
                algo.setMaximumPatternLength(getParamAsInteger(parameters[1]));
            }
    		
    		// execute the algorithm with minsup = 2 sequences  (50 %)
    		algo.runAlgorithm(inputFile, outputFile, getParamAsDouble(parameters[0]));     // minsup = 106   k = 1000   BMS
    		algo.printStatistics();
        //} 
	   // else 
    	//if VMSP
	    	//if ("VMSP".equals(algorithmName)) {
        	AlgoVMSP algo = new AlgoVMSP();
            if ("".equals(parameters[1]) == false) {
                algo.setMaximumPatternLength(getParamAsInteger(parameters[1]));
            }
    		
    		// execute the algorithm with minsup = 2 sequences  (50 %)
    		algo.runAlgorithm(inputFile, outputFile, getParamAsDouble(parameters[0]));     // minsup = 106   k = 1000   BMS
    		algo.printStatistics();
       // }else 
    	// if ("VGEN".equals(algorithmName)) {
        	AlgoVGEN algo = new AlgoVGEN();
            if ("".equals(parameters[1]) == false) {
                algo.setMaximumPatternLength(getParamAsInteger(parameters[1]));
            }

    		// execute the algorithm 
    		algo.runAlgorithm(inputFile, outputFile, getParamAsDouble(parameters[0])); 
    		algo.printStatistics();
        //}
         * 
         * 
         */	
            }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       StringBuffer sb = new StringBuffer();
		
		//sb.append("Testing \n");
		//sb.append("for string \n");
		sb.append("1 -1 1 2 -1 2 3 -1 -21 -1 1 2 3 -1 -24 -1 1 -1 1 2 -1 2 3 -1 -24 -1 1 4 -1 -2 \n");
		sb.append("1 -1 -2 \n");
		sb.append("1 2 3 -1 -2 \n");
		
		System.out.println(sb.toString());
		
		double minsupRel = 0.5;
		AlgoSPAM algo = new AlgoSPAM();
        algo.setMaximumPatternLength(4);//getParamAsInteger(parameters[1])
        
        /*if ("".equals(parameters[1]) == false) {
            }*/
        //algo.runAlgorithm(inputFile, outputFile, getParamAsDouble(parameters[0]));
        try {
			algo.runAlgorithm(sb, minsupRel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        algo.printStatistics(); 
	}

}
