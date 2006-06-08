
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.AbstractBizLogic;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.lookup.DefaultLookupParameters;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.lookup.LookupParameters;
import edu.wustl.common.lookup.LookupResult;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
/**
 * @author vaishali_khandelwal
 *
 * This class is for finding out the matching participant with respect to given participant
 */
public class ParticipantLookupLogic implements LookupLogic
{	
	public List lookup(LookupParameters params) throws Exception
	{
		DefaultLookupParameters participantParams=(DefaultLookupParameters)params;
		
		Participant participant=(Participant)participantParams.getObject();
		Double cutoff=participantParams.getCutoff();
		
		AbstractBizLogic bizLogic = new DefaultBizLogic();
		String sourceObjectName = Participant.class.getName();
  	
		String[] whereColumnName = {"firstName","middleName","lastName","birthDate","deathDate","socialSecurityNumber"};
		String[] whereColumnCondition = {"=","=","=","=","=","="};
		Object[] whereColumnValue = {participant.getFirstName(),participant.getMiddleName(),participant.getLastName(),participant.getBirthDate(),participant.getDeathDate(),participant.getSocialSecurityNumber()};
	
		String joinCondition = Constants.OR_JOIN_CONDITION;
		List listOfParticipants=bizLogic.retrieve(sourceObjectName,whereColumnName,whereColumnCondition,whereColumnValue,joinCondition);
	
		List participants=searchMatchingParticipant(participant,listOfParticipants,cutoff);
  	
		return participants;

	}

	/**
	 * Searches the participant which has matching probablity more than cutoff. 
	 * @param srcParticipant participant with which comparision is to be done.
	 * @param listOfParticipants List of all participants which has atleast one matching parameter.
	 * @param cutoff is the value such that the participants above the cutoff values are stored in List.
	 ** */
	private List searchMatchingParticipant(Participant srcParticipant, List listOfParticipants,Double cutoff) throws Exception
	{
		List participants=new ArrayList();
		int count=0;
		Iterator itr=listOfParticipants.iterator();
		/*Iterates through all the Participants from the list */
		while(itr.hasNext())
		{
			count=0;
			Participant destParticipant=(Participant)itr.next();
			
			if(srcParticipant.getFirstName()!=null && !srcParticipant.getFirstName().trim().equals("")&& srcParticipant.getFirstName().equalsIgnoreCase(destParticipant.getFirstName()))
			{
				count++;
			}
			if(srcParticipant.getMiddleName()!=null && !srcParticipant.getMiddleName().trim().equals("")&&srcParticipant.getMiddleName().equalsIgnoreCase(destParticipant.getMiddleName()))
			{
				count++;
			}
			if(srcParticipant.getLastName()!=null && !srcParticipant.getLastName().trim().equals("") && srcParticipant.getLastName().equalsIgnoreCase(destParticipant.getLastName()))
			{
				count++;
			}
			if(srcParticipant.getBirthDate()!=null  && destParticipant.getBirthDate()!=null&& srcParticipant.getBirthDate().compareTo(destParticipant.getBirthDate())==0)
			{
				count++;
			}
			if(srcParticipant.getDeathDate()!=null  && destParticipant.getDeathDate()!=null && srcParticipant.getDeathDate().compareTo(destParticipant.getDeathDate())==0)
			{
				count++;
			}
			if(srcParticipant.getSocialSecurityNumber()!=null &&!srcParticipant.getSocialSecurityNumber().trim().equals("")&& srcParticipant.getSocialSecurityNumber().equals(destParticipant.getSocialSecurityNumber()))
			{
				count++;
			}
			
			//Finding the probablity.
			Double probablity=new Double((100/6)*count);
			if(probablity.doubleValue()>=cutoff.doubleValue())
			{
				LookupResult result=new LookupResult();
				result.setObject(destParticipant);
				result.setProbablity(probablity);
				participants.add(result);
			}
		}
		return participants;
	}
 
	public static void main(String[] args) throws Exception
	{
		Variables.catissueHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		PropertyConfigurator.configure(Variables.catissueHome+"\\WEB-INF\\src\\"+"ApplicationResources.properties");
		
		Logger.out.debug("here");
		
		DBUtil.currentSession();
		
		System.out.println("Hello");
		/*Participant participant=new Participant();
    	participant.setFirstName("aaa");
    	participant.setMiddleName("aaa");
    	participant.setLastName("aaa");
    	participant.setActivityStatus("Active");
    	participant.setBirthDate(new Date());
    	participant.setEthnicity("aaaa");
    	participant.setGender("Female Gender");
    	participant.setSexGenotype("sadasd");
    	participant.setRace("asdasd");
    	participant.setEthnicity("adada");
    	participant.setSocialSecurityNumber("123-45-6789");
    	
    	List list=participantLookup(participant);
    	Logger.out.debug("-----after function size:"+list.size());
    	Iterator itr=list.iterator();
    	while(itr.hasNext())
    	{
    		Participant participant1=(Participant)itr.next();
    		Logger.out.info("Object:"+participant1);
    	
    	}
    	*/
    	
		
	}
}