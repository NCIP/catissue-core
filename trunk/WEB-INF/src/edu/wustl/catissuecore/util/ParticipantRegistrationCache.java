
package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.sf.ehcache.CacheException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.logger.Logger;

/**
 * This class handles all ParticipantRegistration Cache related operations
 * @author vaishali_khandelwal
 */
class ParticipantRegistrationCache
{

	//participantRegistrationInfoList contains list of ParticipantRegistration Info objects
	List participantRegistrationInfoList;

	/**
	 * Constructor which gets the participantRegistrationInfo list from cache and stores in participantRegistrationInfoList  
	 *
	 */
	public ParticipantRegistrationCache()
	{
		this.participantRegistrationInfoList = getParticipantRegInfoListFromCache();
	}

	/**
	 * This function gets the ParticipantRegInifoList from the cache.
	 * @return list
	 */
	private List getParticipantRegInfoListFromCache()
	{
		List participantRegInfoList = null;
		try
		{
			//getting instance of catissueCoreCacheManager and getting participantMap from cache
			CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
			participantRegInfoList = (Vector) catissueCoreCacheManager.getObjectFromCache(Constants.LIST_OF_REGISTRATION_INFO);
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
			Logger.out.info("Error while accessing cache");
		}
		catch (CacheException e)
		{
			e.printStackTrace();
			Logger.out.info("Error while accessing cache");
		}
		return participantRegInfoList;
	}

	/**
	 *	This method adds the cpID and cpTitle in the ParticipantRegistrationInfo object
	 *	and add this object to participantRegistrationInfoList; 
	 * 	@param cpId collection protocol ID
	 * 	@param cpTitle collection protocol title
	 */
	public void addNewCP(Long cpId, String cpTitle)
	{
		//This method adds the cpID and cpTitle in the ParticipantRegistrationInfo object 
		//and add this object to participantRegistrationInfoList;

		//Creating the new ParticipantRegInfo object and storing the collection protocol info 
		ParticipantRegistrationInfo participantRegInfo = new ParticipantRegistrationInfo();
		participantRegInfo.setCpId(cpId);
		participantRegInfo.setCpTitle(cpTitle);
		List participantInfoList = new ArrayList();
		participantRegInfo.setParticipantInfoCollection(participantInfoList);
			
		participantRegistrationInfoList.add(participantRegInfo);
	}

	/**
	 *	This method updates the title of the collection protocol.
	 *	first find out the participantRegistrationInfo object in participantRegistrationInfoList
	 *	where cpID = cpId and the updates the cpTitle with newTitle.
	 * 	@param cpId
	 * 	@param newTitle
	 */
	public void updateCPTitle(Long cpId, String newTitle)
	{
		//This method updates the title of the collection protocol.
		//first find out the participantRegistrationInfo object in participantRegistrationInfoList 
		//where cpID = cpId and the updates the cpTitle with newTitle.

		Iterator itr = participantRegistrationInfoList.iterator();
		// Iterating thru whole participsantRegistrationInfoList and 
		//get the object in which cpId = cpId
		while (itr.hasNext())
		{
			ParticipantRegistrationInfo participantRegInfo = (ParticipantRegistrationInfo) itr.next();
			if (participantRegInfo.getCpId().longValue() == cpId.longValue())
			{
				//Set the new CP title
				participantRegInfo.setCpTitle(newTitle);
				break;
			}
		}
	}

	/**
	 * 	This method searches the participantRegistrationInfo object in the
	 *  participantRegistrationInfoList where cpID = cpId and removes this object
	 *  from the List.
	 * 	@param cpId
	 */
	public void removeCP(Long cpId)
	{
		//This method searches the participantRegistrationInfo object in the 
		//participantRegistrationInfoList where cpID = cpId and removes this object
		//from the List.
		Iterator itr = participantRegistrationInfoList.iterator();
		while (itr.hasNext())
		{
			ParticipantRegistrationInfo participantRegInfo = (ParticipantRegistrationInfo) itr.next();
			if (participantRegInfo.getCpId().longValue() == cpId.longValue())
			{
				participantRegistrationInfoList.remove(participantRegInfo);
				break;
			}
		}
	}

	/**
	 *	This method searches the participantRegistrationInfo object in the 
	 *	participantRegistrationInfoList where cpID = cpId and adds the participantId in 
	 *	participantIdCollection
	 * 	@param cpId
	 * 	@param participantID
	 */
	public void registerParticipant(Long cpId, Long participantID, String protocolParticipantID)
	{
		//This method searches the participantRegistrationInfo object in the 
		//participantRegistrationInfoList where cpID = cpId and adds the participantId in 
		//participantIdCollection

		Iterator itr = participantRegistrationInfoList.iterator();

		//Iterate thru whole list and check weather any ParticipantRegInfo object is there in list with given collection protol Id.
		//If it is present then add particpantId in participantCollection 

		while (itr.hasNext())
		{
			ParticipantRegistrationInfo participantRegInfo = (ParticipantRegistrationInfo) itr.next();
			if (participantRegInfo.getCpId().longValue() == cpId.longValue())
			{
				List participantInfoList = (List) participantRegInfo.getParticipantInfoCollection();

				if (participantID != null)
				{
					String participantInfo = participantID.toString() + ":";
					if (protocolParticipantID != null && !protocolParticipantID.equals(""))
						participantInfo = participantInfo + protocolParticipantID;

					participantInfoList.add(participantInfo);
				}
				break;
			}
		}

	}

	/**
	 * 	This method searches the participantRegistrationInfo object in the 
	 *	participantRegistrationInfoList where cpID = cpId and removes the participantId from
	 *	participantIdCollection
	 *	@param cpId
	 * 	@param participantID
	 */
	public void deRegisterParticipant(Long cpId, Long participantID, String protocolparticipantID)
	{
		//This method searches the participantRegistrationInfo object in the 
		//participantRegistrationInfoList where cpID = cpId and removes the participantId from
		//participantIdCollection

		Iterator itr = participantRegistrationInfoList.iterator();

		//Iterate thru whole list and check weather any ParticipantRegInfo object is there in list with given collection protol Id.
		//If it is present then remove given add particpantId from participantCollection 

		while (itr.hasNext())
		{
			ParticipantRegistrationInfo participantRegInfo = (ParticipantRegistrationInfo) itr.next();
			if (participantRegInfo.getCpId().longValue() == cpId.longValue())
			{
				List participantInfoList = (List) participantRegInfo.getParticipantInfoCollection();
				if (participantID != null)
				{
					String participantInfo = participantID.toString() + ":";
					if (protocolparticipantID != null && !protocolparticipantID.equals(""))
						participantInfo = participantInfo + protocolparticipantID;

					participantInfoList.remove(participantInfo);
				}
				
				
				break;
			}
		}
	}

	/**
	 * 	This method searches the participantRegistrationInfo object in the 
	 *	participantRegistrationInfoList where cpID = cpId and returns the Participant 
	 *	Collection
	 * 	@param cpId
	 * 	@return
	 */
	public List getParticipantInfoCollection(Long cpId)
	{
		//This method searches the participantRegistrationInfo object in the 
		//participantRegistrationInfoList where cpID = cpId and returns the Participant 
		//Collection
		List participantInfoList = null;
		Iterator itr = participantRegistrationInfoList.iterator();
		while (itr.hasNext())
		{
			ParticipantRegistrationInfo participantRegInfo = (ParticipantRegistrationInfo) itr.next();
			if (participantRegInfo.getCpId().longValue() == cpId.longValue())
			{
				participantInfoList = (List) participantRegInfo.getParticipantInfoCollection();
				break;
			}
		}
		return participantInfoList;
	}

	/**
	 * This method returns a list of CP ids and CP titles 
	 * from the participantRegistrationInfoList
	 * @return
	 */
	public List getCPDetailCollection()
	{
		//This method returns a list of CP ids and CP titles from the participantRegistrationInfoList
		List cpDetailsList = new ArrayList();
		Iterator itr = participantRegistrationInfoList.iterator();
		while (itr.hasNext())
		{
			ParticipantRegistrationInfo participantRegInfo = (ParticipantRegistrationInfo) itr.next();
			NameValueBean cpDetails = new NameValueBean(participantRegInfo.getCpTitle(), participantRegInfo.getCpId());
			cpDetailsList.add(cpDetails);
		}
		return cpDetailsList;
	}

}
