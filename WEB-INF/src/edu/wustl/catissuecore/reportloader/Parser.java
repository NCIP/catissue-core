package edu.wustl.catissuecore.reportloader;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;

public interface Parser 
{
	/**
	 * @param fileName name of the file
	 * @throws Exception while parsing the report  
	 */
	public void parse(String fileName)throws Exception;
	
	/**
	 * 
	 * @param participant
	 * @param reportText
	 * @param specimenCollectionGroup
	 * @throws Exception
	 */
	public void parseString(Participant participant,String reportText, SpecimenCollectionGroup specimenCollectionGroup)throws Exception;
	
}
