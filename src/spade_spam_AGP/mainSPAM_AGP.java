package spade_spam_AGP;

import spade_spam_AGP.candidatePatternsGeneration.CandidateGenerator;
import spade_spam_AGP.candidatePatternsGeneration.CandidateGenerator_Qualitative;
import spade_spam_AGP.idLists.creators.IdListCreator;
import spade_spam_AGP.idLists.creators.IdListCreator_FatBitmap;

public class mainSPAM_AGP {

	public mainSPAM_AGP(){
		//if ("SPAM_AGP".equals(algorithmName)) {
            spade_spam_AGP.dataStructures.creators.AbstractionCreator abstractionCreator = spade_spam_AGP.dataStructures.creators.AbstractionCreator_Qualitative.getInstance();
            IdListCreator idListCreator = IdListCreator_FatBitmap.getInstance();
            CandidateGenerator candidateGenerator = CandidateGenerator_Qualitative.getInstance();
            
            //double minSupport = getParamAsDouble(parameters[0]);
            double minSupport = 0.5;

            spade_spam_AGP.AlgoSPAM_AGP algo = new spade_spam_AGP.AlgoSPAM_AGP(minSupport);
            
            /*
             * if("".equals(parameters[1]) == false){
             * algo.setMaximumPatternLength(getParamAsInteger(parameters[1]));
			}
             */
            spade_spam_AGP.dataStructures.database.SequenceDatabase sd = new spade_spam_AGP.dataStructures.database.SequenceDatabase(abstractionCreator, idListCreator);
            //sd.loadFile(inputFile, minSupport);

            //algo.runAlgorithm(sd,true, false, outputFile);
            System.out.println(algo.printStatistics());
		//}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
