
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.AbstractBizLogic;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.lookup.DefaultLookupParameters;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.lookup.LookupParameters;
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
		
		Participant participant=(Participant)
		participantParams.getObject();
		Double cutoff=participantParams.getCutoff();
		
		AbstractBizLogic bizLogic = new DefaultBizLogic();
		String sourceObjectName = Participant.class.getName();
  	
		String[] whereColumnName = {"firstName","middleName","lastName","birthDate","deathDate","socialSecurityNumber"};
		String[] whereColumnCondition = {"LIKE","LIKE","LIKE","=","=","="};
		Object[] whereColumnValue = {participant.getFirstName()+"%",participant.getMiddleName()+"%",participant.getLastName()+"%",participant.getBirthDate(),participant.getDeathDate(),participant.getSocialSecurityNumber()};
		
		String joinCondition = Constants.OR_JOIN_CONDITION;
		//getting the matching participants from the database whose atleast one parameter matches with the given participqant
		List listOfParticipants=bizLogic.retrieve(sourceObjectName,whereColumnName,whereColumnCondition,whereColumnValue,joinCondition);
	
		//calling the searchMatchingParticipant to filter the participant list according to given cutoff value
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
			
			if(srcParticipant.getFirstName()!=null && !srcParticipant.getFirstName().trim().equals("")&& destParticipant.getFirstName().startsWith(srcParticipant.getFirstName()))
			{
				count++;
			}
			if(srcParticipant.getMiddleName()!=null && !srcParticipant.getMiddleName().trim().equals("")&&destParticipant.getMiddleName().startsWith(srcParticipant.getMiddleName()))
			{
				count++;
			}
			if(srcParticipant.getLastName()!=null && !srcParticipant.getLastName().trim().equals("") && destParticipant.getLastName().startsWith(srcParticipant.getLastName()))
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
			Double probablity=new Double((100*count)/6);
			if(probablity.doubleValue()>=cutoff.doubleValue())
			{
				DefaultLookupResult result=new DefaultLookupResult();
				result.setObject(destParticipant);
				result.setProbablity(probablity);
				participants.add(result);
			}
		}
		return participants;
	}
 
	
}