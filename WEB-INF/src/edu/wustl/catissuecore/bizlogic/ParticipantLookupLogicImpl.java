
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.AbstractBizLogic;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
/**
 * @author vaishali_khandelwal
 *
 * This class is for finding out the matching participant with respect to given participant
 */
public class ParticipantLookupLogicImpl implements ParticipantLookupLogic
{
	/**
	 * Finds out the matching participants. 
	 * @param participant 
	 **/
	public List participantLookup(Participant participant) throws Exception
    {
    	AbstractBizLogic bizLogic = new DefaultBizLogic();
	  	String sourceObjectName = Participant.class.getName();
	  	
	  	String[] whereColumnName = {"firstName","middleName","lastName","birthDate","socialSecurityNumber"};
	  	String[] whereColumnCondition = {"=","=","=","=","="};
	  	Object[] whereColumnValue = {participant.getFirstName(),participant.getMiddleName(),participant.getLastName(),participant.getBirthDate(),participant.getSocialSecurityNumber()};
		
	  	String joinCondition = Constants.OR_JOIN_CONDITION;
	  	List listOfParticipants=bizLogic.retrieve(sourceObjectName,whereColumnName,whereColumnCondition,whereColumnValue,joinCondition);
		
	  	List participants=searchMatchingParticipant(participant,listOfParticipants);
	  	
    	return participants;
    	
    }
	
	/**
	 * Searches the participant which has more that 3 matching parameters. 
	 * @param srcParticipant participant with which comparision is to be done.
	 * @param listOfParticipants List of all participants which has atleast one matching parameter.
	 ** */
	private List searchMatchingParticipant(Participant srcParticipant, List listOfParticipants) throws Exception
	{
		List participants=new ArrayList();
		int count=0;
		Iterator itr=listOfParticipants.iterator();
		/*Iterates through all the Participants from the list */
		while(itr.hasNext())
		{
			count=0;
			Participant destParticipant=(Participant)itr.next();
			Logger.out.info("Participant Birth Date in Database:"+destParticipant.getBirthDate());
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
			if(srcParticipant.getBirthDate()!=null  && srcParticipant.getBirthDate().compareTo(destParticipant.getBirthDate())==0)
			{
				count++;
			}
			if(srcParticipant.getDeathDate()!=null  && srcParticipant.getDeathDate().compareTo(destParticipant.getDeathDate())==0)
			{
				count++;
			}
			if(srcParticipant.getSocialSecurityNumber()!=null &&!srcParticipant.getSocialSecurityNumber().trim().equals("")&& srcParticipant.getSocialSecurityNumber().equals(destParticipant.getSocialSecurityNumber()))
			{
				count++;
			}
			
			//If more than three parameters are matching then add in participants list
			if(count>=3)
			{
				//creating a list and adding all the values of participant object
				List participantInfo=new ArrayList();
				participantInfo.add(destParticipant.getSystemIdentifier());
				participantInfo.add(destParticipant.getLastName());
				participantInfo.add(destParticipant.getFirstName());
				participantInfo.add(destParticipant.getMiddleName());
				participantInfo.add(destParticipant.getBirthDate());
				participantInfo.add(destParticipant.getSocialSecurityNumber());
				int probability=100/5*count;
				participantInfo.add(new Integer(probability).toString()+"%");
				
				participants.add(participantInfo);
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