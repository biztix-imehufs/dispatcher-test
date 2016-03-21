package gsp_AGP;

import gsp_AGP.AlgoGSP;
import gsp_AGP.items.SequenceDatabase;
import gsp_AGP.items.creators.AbstractionCreator;
import gsp_AGP.items.creators.AbstractionCreator_Qualitative;

public class mainGSP {

	
	public mainGSP()
	{
		AbstractionCreator abstractionCreator = AbstractionCreator_Qualitative.getInstance();
        double minSupport = 0.5; //getParamAsDouble("0.5");

        AlgoGSP algo = new AlgoGSP(minSupport, 0, Integer.MAX_VALUE, 0, abstractionCreator);
        /*
         * if("".equals(parameters[1]) == false){
         * algo.setMaximumPatternLength(getParamAsInteger(parameters[1]));
		}
         */
        SequenceDatabase sd = new SequenceDatabase(abstractionCreator);
        sd.loadFile(inputFile, minSupport);

        algo.runAlgorithm(sd, true, false, outputFile);
        System.out.println(algo.printStatistics());
	}
	public static void main(String[] args){
		
		AbstractionCreator abstractionCreator = AbstractionCreator_Qualitative.getInstance();
        double minSupport = 0.5; //getParamAsDouble("0.5");

        AlgoGSP algo = new AlgoGSP(minSupport, 0, Integer.MAX_VALUE, 0, abstractionCreator);
        /*
         * if("".equals(parameters[1]) == false){
         * algo.setMaximumPatternLength(getParamAsInteger(parameters[1]));
		}
         */
        SequenceDatabase sd = new SequenceDatabase(abstractionCreator);
        sd.loadFile(inputFile, minSupport);

        algo.runAlgorithm(sd, true, false, outputFile);
        System.out.println(algo.printStatistics());
	}
	
	private static double getParamAsDouble(String value) {
        if (value.contains("%")) {
            value = value.substring(0, value.length() - 1);
            return Double.parseDouble(value) / 100d;
        }
        return Double.parseDouble(value);
    }
}
