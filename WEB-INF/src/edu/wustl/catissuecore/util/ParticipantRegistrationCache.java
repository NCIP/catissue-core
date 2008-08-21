package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import net.sf.ehcache.CacheException;
import edu.common.dynamicextensions.util.global.Variables;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.SiteBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.util.dbManager.DAOException;
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
	public void addNewCP(Long cpId, String cpTitle, String cpShortTitle)
	{
		//This method adds the cpID and cpTitle in the ParticipantRegistrationInfo object 
		//and add this object to participantRegistrationInfoList;

		//Creating the new ParticipantRegInfo object and storing the collection protocol info 
		ParticipantRegistrationInfo participantRegInfo = new ParticipantRegistrationInfo();
		participantRegInfo.setCpId(cpId);
		participantRegInfo.setCpTitle(cpTitle);
		participantRegInfo.setCpShortTitle(cpShortTitle);
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
	 *	This method updates the title of the collection protocol.
	 *	first find out the participantRegistrationInfo object in participantRegistrationInfoList
	 *	where cpID = cpId and the updates the cpTitle with newTitle.
	 * 	@param cpId
	 * 	@param newShortTitle
	 */
	public void updateCPShortTitle(Long cpId, String newShortTitle)
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
				participantRegInfo.setCpShortTitle(newShortTitle);
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
	 * This method returns a list of CP ids and CP short titles 
	 * from the participantRegistrationInfoList
	 * @return
	 * @throws DAOException 
	 */
	public List getCPDetailCollection()
	{
		HttpSession session = null;
		session = flex.messaging.FlexContext.getHttpRequest().getSession();
		SessionDataBean sessionDataBean = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
		List newList = new Vector();
		List<NameValueBean> cpDetailsList = new ArrayList<NameValueBean>();
		
		UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
		Set cpIds = userBizLogic.getRelatedCPIds(sessionDataBean.getUserId(), true);
			
			
			if(cpIds == null)
			{
				newList.addAll(participantRegistrationInfoList);
			}
			else
			{	
				for(int counter=0; counter<participantRegistrationInfoList.size(); counter++)
				{
					ParticipantRegistrationInfo participantRegistrationInfo = (ParticipantRegistrationInfo) participantRegistrationInfoList.get(counter);
					Long cpId = participantRegistrationInfo.getCpId();
					
					if(cpIds.contains(cpId))
					{
						newList.add(participantRegistrationInfo);
					}
				}
			}
		Set<Long> siteIds = userBizLogic.getRelatedSiteIds(sessionDataBean.getUserId());
		Set<Long> cp_Ids = userBizLogic.getRelatedCPIds(sessionDataBean.getUserId(), false);
		
		if (siteIds != null && !siteIds.isEmpty())
		{
			PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName());
			SiteBizLogic siteBizLogic = (SiteBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SITE_FORM_ID);
			for (Long siteId : siteIds)
			{
				String peName = Constants.getCurrentAndFuturePGAndPEName(siteId);
				if (privilegeCache.hasPrivilege(peName,Variables.privilegeDetailsMap.get(Constants.EDIT_PROFILE_PRIVILEGE)))
				{
					Collection<CollectionProtocol> cpCollection = siteBizLogic.getRelatedCPs(siteId);
					
					if (cpCollection != null && !cpCollection.isEmpty())
					{
						List<NameValueBean> list = new ArrayList<NameValueBean>();
						for (CollectionProtocol cp : cpCollection)
						{
							if(cp_Ids.contains(cp.getId()))
							{
								continue;
							}
                            boolean isPresent = false; 
                            for (NameValueBean nameValueBean : list)
                            {
                                if (nameValueBean.getValue().equalsIgnoreCase(cp.getId().toString()))
                                {
                                    isPresent = true;
                                    break;
                                }
                            }
                            if (!isPresent)
							list.add(new NameValueBean(cp.getShortTitle(),cp.getId()));
						}
						cpDetailsList.addAll(list);
					}
					
				}
			}
		}
		Iterator iter = newList.iterator();
		while (iter.hasNext())
		{
			ParticipantRegistrationInfo participantRegInfo = (ParticipantRegistrationInfo) iter.next();
			NameValueBean cpDetails = new NameValueBean(participantRegInfo.getCpShortTitle(), participantRegInfo.getCpId());
            
            boolean isPresent = false; 
            for (NameValueBean nameValueBean : cpDetailsList)
            {
                if (nameValueBean.getValue().equalsIgnoreCase(participantRegInfo.getCpId().toString()))
                {
                    isPresent = true;
                    break;
                }
            }
            if (!isPresent)
			cpDetailsList.add(cpDetails);
		}
		return cpDetailsList;
	}

//	Smita changes start
	/**
	 * This method returns a list of CP ids and CP titles 
	 * from the participantRegistrationInfoList
	 * @return
	 */
	public Map<Long, String> getCPIDTitleMap()
	{
		//This method returns a list of CP ids and CP titles from the participantRegistrationInfoList
		Map<Long, String> cpIDTitleMap = new HashMap<Long, String>();
		Iterator itr = participantRegistrationInfoList.iterator();
		
		while (itr.hasNext())
		{
			ParticipantRegistrationInfo participantRegInfo = (ParticipantRegistrationInfo) itr.next();
			//NameValueBean cpDetails = new NameValueBean(participantRegInfo.getCpShortTitle(), participantRegInfo.getCpId());
			cpIDTitleMap.put(participantRegInfo.getCpId(), participantRegInfo.getCpTitle());
		}
		
		return cpIDTitleMap;
	}
//	Smita changes end
}

