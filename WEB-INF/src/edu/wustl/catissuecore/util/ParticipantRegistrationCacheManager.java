
package edu.wustl.catissuecore.util;

import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.common.exception.ApplicationException;

/**
 * This class handles Cache related operations
 * @author vaishali_khandelwal
 *
 */
public class ParticipantRegistrationCacheManager
{

	ParticipantRegistrationCache participantRegCache;
	ParticipantCache participantCahe;

	public ParticipantRegistrationCacheManager()
	{
		participantRegCache = new ParticipantRegistrationCache();
		participantCahe = new ParticipantCache();
	}

	/**
	 * This nmethod adds the Colletion Protocol ID and title in cache.
	 * @param cpId cp id
	 * @param cpTitle Title
	 * @param cpShortTitle Short Title
	 */
	public synchronized void addNewCP(Long cpId, String cpTitle, String cpShortTitle)
	{
		participantRegCache.addNewCP(cpId, cpTitle, cpShortTitle);
	}

	/**
	 * This method updates the cp Title 
	 * @param cpId
	 * @param newTitle
	 */
	public synchronized void updateCPTitle(Long cpId, String newTitle)
	{
		participantRegCache.updateCPTitle(cpId, newTitle);
	}
	
	/**
	 * This method updates the cp Title
	 * 
	 * @param cpId
	 * @param newShortTitle
	 */
	public synchronized void updateCPShortTitle(Long cpId, String newShortTitle) {
		participantRegCache.updateCPShortTitle(cpId, newShortTitle);
	}

	/**
	 * This mehod removes the CP from cache
	 * @param cpId
	 */
	public synchronized void removeCP(Long cpId)
	{
		participantRegCache.removeCP(cpId);
	}

	/**
	 * this method add the registerd participant in cache.
	 * @param cpId
	 * @param participantID
	 */
	public synchronized void registerParticipant(Long cpId, Long participantID, String protocolParticipantID)
	{
		participantRegCache.registerParticipant(cpId, participantID, protocolParticipantID);
	}

	/**
	 * This method reomves the particpant from the cache
	 * @param cpId
	 * @param participantID
	 */
	public synchronized void deRegisterParticipant(Long cpId, Long participantID, String protocolParticipantID)
	{
		participantRegCache.deRegisterParticipant(cpId, participantID, protocolParticipantID);
	}

	/**
	 * This method gets the participantCollection for particular Collection Protocol
	 * @param cpId
	 * @return
	 */
	/*public List getParticipantCollection(Long cpId)
	{
		return participantRegCache.getParticipantIDCollection(cpId);
	}*/

	/**
	 * This method returns the CP ids and CP titles
	 * @return
	 * @throws ApplicationException 
	 */
	public List getCPDetailCollection() throws ApplicationException
	{
		return participantRegCache.getCPDetailCollection();
	}

//	Smita changes start
	/**
	 * This method returns a list of CP ids and CP titles 
	 * from the participantRegistrationInfoList
	 * @return
	 */
	public Map<Long, String> getCPIDTitleMap()
	{
		return participantRegCache.getCPIDTitleMap();
	}
//	Smita changes end
	
	/**
	 * This method reutns the Participant information for particular Participant Id
	 * @param participantID
	 * @return
	 */
	public Participant getParticipantInfo(Long participantID)
	{
		return participantCahe.getParticipantInfo(participantID);
	}

	/*public List getParticipantsInfo(List participantIdList)
	{
		//This method iterates thru whole participantIdlist and get the participant object 
		//by calling getParticipantInfo(ParticipantID) and stores particpant object in the list
		//and return the list

		return null;
	}*/

	public List getParticipantNames(Long cpId)
	{
		List participantInfoList = participantRegCache.getParticipantInfoCollection(cpId);
		List participantNames = participantCahe.getParticpantNamesWithID(participantInfoList);
		return participantNames;
	}

	/**
	 * This method adds the participant in Map
	 * @param participant the object which is to be added in map
	 */
	public void addParticipant(Participant participant)
	{
		participantCahe.addParticipant(participant);
	}
	
}
