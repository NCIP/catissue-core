
package edu.wustl.catissuecore.util;

import java.util.List;

import edu.wustl.catissuecore.domain.Participant;

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
	 * @param cpId
	 * @param cpTitle
	 */
	public synchronized void addNewCP(Long cpId, String cpTitle)
	{
		participantRegCache.addNewCP(cpId, cpTitle);
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
	 */
	public List getCPDetailCollection()
	{
		return participantRegCache.getCPDetailCollection();
	}

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
