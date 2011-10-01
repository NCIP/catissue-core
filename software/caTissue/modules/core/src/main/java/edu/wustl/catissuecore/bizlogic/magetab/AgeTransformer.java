package edu.wustl.catissuecore.bizlogic.magetab;

import java.util.Date;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.logger.Logger;


public class AgeTransformer extends AbstractCharacteristicTransformer {
	private static transient final Logger logger = Logger.getCommonLogger(AgeTransformer.class);
	public AgeTransformer(CharacteristicTransformerConfig config) {
		super("Age", "Age", "Age", config);
	}

	@Override
	public String getCharacteristicValue(Specimen specimen) {
		try {
			Participant participant = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getParticipant();
			Date bDay=participant.getBirthDate();
			Date dDay=participant.getDeathDate();
			String age;
			if(bDay==null)
				age="";
			else if(dDay==null)
			{
				//that means he is alive
				Date date = new Date();				
				long years=	date.getYear()-bDay.getYear();	
				age=years+""; 
			}
			else
			{
				//that means he has both bday and dday
				long years=	dDay.getYear()-bDay.getYear();	
				age=years+""; 
			}			
			logger.debug("The age is '"+age+"' bday is '"+bDay+"' dDay is '"+dDay+"'");
			return age;
		} catch (NullPointerException npe) {
			return "";
		}
		
		
	}

}
