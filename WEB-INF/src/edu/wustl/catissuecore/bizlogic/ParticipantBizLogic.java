/**
 * <p>Title: ParticipantHDAO Class>
 * <p>Description:	ParticipantHDAO is used to add Participant's information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 23, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.CacheException;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.ParticipantRegistrationCacheManager;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.lookup.DefaultLookupParameters;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.exception.UserNotAuthorizedException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * ParticipantHDAO is used to to add Participant's information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class ParticipantBizLogic extends CatissueDefaultBizLogic
{
	private List<Long> cprIdList = new ArrayList<Long>();
	/**
	 * Saves the Participant object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws BizLogicException 
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
		Participant participant = (Participant) obj;
		dao.insert(participant, true);
		Collection participantMedicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();


		if (participantMedicalIdentifierCollection == null)
		{
			participantMedicalIdentifierCollection = new LinkedHashSet();
		}
		if (participantMedicalIdentifierCollection.isEmpty())
		{
			//add a dummy participant MedicalIdentifier for Query.
			ParticipantMedicalIdentifier participantMedicalIdentifier = new ParticipantMedicalIdentifier();
			participantMedicalIdentifier.setMedicalRecordNumber(null);
			participantMedicalIdentifier.setSite(null);
			participantMedicalIdentifierCollection.add(participantMedicalIdentifier);
		}

		//Inserting medical identifiers in the database after setting the participant associated.
		Iterator it = participantMedicalIdentifierCollection.iterator();
		while (it.hasNext())
		{
			ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier) it.next();
			pmIdentifier.setParticipant(participant);
			dao.insert(pmIdentifier, true);
		}
		
		//Inserting collection Protocol Registration info in the database after setting the participant associated.
		//Abhishek Mehta
		Collection collectionProtocolRegistrationCollection = participant.getCollectionProtocolRegistrationCollection();

		if (collectionProtocolRegistrationCollection != null)
		{

			registerToCPR(dao, sessionDataBean, participant);
		}
		else
		{
			insertAuthData(participant);
		}
		}catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		
	}

	/**
	 * @param participant
	 * @throws DAOException
	 */
	private void insertAuthData(Participant participant) throws DAOException
	{
		Set protectionObjects = new HashSet();
		protectionObjects.add(participant);

	}

	/**
	 * @param dao
	 * @param sessionDataBean
	 * @param participant
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	private void registerToCPR(DAO dao, SessionDataBean sessionDataBean, Participant participant) throws BizLogicException
	{
		CollectionProtocolRegistrationBizLogic cprBizLogic = new CollectionProtocolRegistrationBizLogic();
		Collection cprCollection = participant.getCollectionProtocolRegistrationCollection();

		Iterator itcprCollection = cprCollection.iterator();

		while (itcprCollection.hasNext())
		{
			CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) itcprCollection.next();
			cpr.setParticipant(participant);
			cpr.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
			cprBizLogic.insert(cpr, dao, sessionDataBean);
			cprIdList.add(cpr.getId());
		}
	}

	/**
	 * This method gets called after insert method. Any logic after insertnig object in database can be included here.
	 * @param obj The inserted object.
	 * @param dao the dao object
	 * @param sessionDataBean session specific data
	 * @throws DAOException
	 * @throws UserNotAuthorizedException 
	 * */
	protected void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		Participant participant = (Participant) obj;
		Collection collectionProtocolRegistrationCollection = participant.getCollectionProtocolRegistrationCollection();
		updateCache(obj);
		Iterator itCollectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection.iterator();
		ParticipantRegistrationCacheManager participantRegCacheManager = new ParticipantRegistrationCacheManager();
		while (itCollectionProtocolRegistrationCollection.hasNext())
		{
			CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) itCollectionProtocolRegistrationCollection
					.next();
			participantRegCacheManager.registerParticipant(collectionProtocolRegistration.getCollectionProtocol().getId().longValue(),
					collectionProtocolRegistration.getParticipant().getId().longValue(), collectionProtocolRegistration.getProtocolParticipantIdentifier());
		}
		
	}

	/**
	 * This method gets called after update method. Any logic after updating into database can be included here.
	 * @param dao the dao object
	 * @param currentObj The object to be updated.
	 * @param oldObj The old object.
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 * */
	protected void postUpdate(DAO dao, Object currentObj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException
	{
		Participant participant = (Participant) currentObj;
		Collection cprCollection = participant.getCollectionProtocolRegistrationCollection();
		updateCache(currentObj);
		//Added updation of Collection Protocol Registration
		//(Abhishek Mehta)
		CollectionProtocolRegistrationBizLogic cprBizLogic = new CollectionProtocolRegistrationBizLogic();
		ParticipantRegistrationCacheManager participantRegCacheManager = new ParticipantRegistrationCacheManager();
		Iterator iteratorCPRCollection = cprCollection.iterator();

		Participant oldparticipant = (Participant) oldObj;
		Collection oldCPRCollection = oldparticipant.getCollectionProtocolRegistrationCollection();

		Long cpId, participantId;
		String protocolParticipantId;

		CollectionProtocolRegistration oldCollectionProtocolRegistration;
		CollectionProtocolRegistration collectionProtocolRegistration;
		while (iteratorCPRCollection.hasNext())
		{
			collectionProtocolRegistration = (CollectionProtocolRegistration) iteratorCPRCollection.next();
			if(collectionProtocolRegistration.getCollectionProtocol().getId() != null)
			{
				cpId = collectionProtocolRegistration.getCollectionProtocol().getId().longValue();

				oldCollectionProtocolRegistration = getCollectionProtocolRegistrationOld(cpId, oldCPRCollection);
				if (oldCollectionProtocolRegistration == null)
				{
					participantId = collectionProtocolRegistration.getParticipant().getId().longValue();
					protocolParticipantId = collectionProtocolRegistration.getProtocolParticipantIdentifier();

					if (protocolParticipantId == null)
					{
						protocolParticipantId = Constants.DOUBLE_QUOTES;
					}

					if (collectionProtocolRegistration.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED))
					{
						participantRegCacheManager.deRegisterParticipant(cpId, participantId, protocolParticipantId);
					}
					else
					{
					participantRegCacheManager.registerParticipant(cpId, participantId, protocolParticipantId);
					}
				}
				else
				{
					cprBizLogic.postUpdate(dao, collectionProtocolRegistration, oldCollectionProtocolRegistration, sessionDataBean);
				}
			}
		}
	}

	/**
	 * Returns CollectionProtocolRegistration object if it exist in collection
	 * @param collectionProtocolId
	 * @param collectionProtocolRegistrationCollection
	 * @return
	 */
	private CollectionProtocolRegistration getCollectionProtocolRegistrationOld(long collectionProtocolId,
			Collection collectionProtocolRegistrationCollection)
	{
		Iterator itCollectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection.iterator();
		while (itCollectionProtocolRegistrationCollection.hasNext())
		{
			CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) itCollectionProtocolRegistrationCollection
					.next();
			long cpId = collectionProtocolRegistration.getCollectionProtocol().getId();
			if (cpId == collectionProtocolId)
			{
				return collectionProtocolRegistration;
			}
		}
		return null;
	}

	/**
	 *  This method updates the cache for MAP_OF_PARTICIPANTS, should be called in postInsert/postUpdate 
	 * @param obj - participant object
	 */
	private synchronized void updateCache(Object obj)
	{
		Participant participant = (Participant) obj;
		Participant cloneParticipant = new Participant(participant);
		//getting instance of catissueCoreCacheManager and getting participantMap from cache
		CatissueCoreCacheManager catissueCoreCacheManager = null;
		//Map mapOfParticipantMedicalIdentifierCollection = new HashMap();
		try
		{
			catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
			HashMap participantMap = (HashMap) catissueCoreCacheManager.getObjectFromCache(Constants.MAP_OF_PARTICIPANTS);
			if (cloneParticipant.getActivityStatus().equalsIgnoreCase(Status.ACTIVITY_STATUS_DISABLED.toString()))
			{
				participantMap.remove(cloneParticipant.getId());
			}
			else
			{
				//Added retrival of Participant Medical Identifier
				//(Virender Mehta)
				/*Collection participantMedicalIdentifier = participant.getParticipantMedicalIdentifierCollection();
				if (participantMedicalIdentifier != null)
				{
					Iterator participantMedicalIdentifierItr = participantMedicalIdentifier.iterator();
					while (participantMedicalIdentifierItr.hasNext())
					{
						ParticipantMedicalIdentifier participantIdentifier = (ParticipantMedicalIdentifier) participantMedicalIdentifierItr.next();
						if (participantIdentifier.getMedicalRecordNumber() == null)
						{
							participant.setParticipantMedicalIdentifierCollection(null);
						}
						else
						{
							String medicalRecordNo = participantIdentifier.getMedicalRecordNumber();
							String siteId = participantIdentifier.getSite().getId().toString();
							Long participantId = participantIdentifier.getParticipant().getId();
							if (mapOfParticipantMedicalIdentifierCollection.get(participantId) == null)
							{
								mapOfParticipantMedicalIdentifierCollection.put(participantId, new ArrayList());
							}
							((ArrayList) mapOfParticipantMedicalIdentifierCollection.get(participantId)).add(medicalRecordNo);
							((ArrayList) mapOfParticipantMedicalIdentifierCollection.get(participantId)).add(siteId);
							participant.setParticipantMedicalIdentifierCollection((Collection) mapOfParticipantMedicalIdentifierCollection
									.get(participantId));
						}
					}
				}*/
				participantMap.put(cloneParticipant.getId(), cloneParticipant);
			}
		}
		catch (CacheException e)
		{
			Logger.out.debug("Exception occured while getting instance of cachemanager");
			e.printStackTrace();
		}

	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws BizLogicException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			Participant participant = (Participant) obj;
			Participant oldParticipant = (Participant) oldObj;

			/*Collection raceCollection = participant.getRaceCollection();
		participant.setRaceCollection(null);*/

			//deleteOldParticipantRaceColl(oldParticipant.getRaceCollection(),dao);

			dao.update(participant);
			//insertNewParticipantRaceColl(raceCollection,participant,sessionDataBean,dao);
			//Audit of Participant.
			((HibernateDAO)dao).audit(obj, oldObj);

			Collection oldParticipantMedicalIdentifierCollection = (Collection) oldParticipant.getParticipantMedicalIdentifierCollection();
			Collection participantMedicalIdentifierCollection = participant.getParticipantMedicalIdentifierCollection();
			Iterator it = participantMedicalIdentifierCollection.iterator();

			//Updating the medical identifiers of the participant
			while (it.hasNext())
			{
				ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier) it.next();

				/**
				 * Start: Change for API Search   --- Jitendra 06/10/2006
				 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
				 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
				 * So we removed default class level initialization on domain object and are initializing in method
				 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
				 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
				 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
				 */
				ApiSearchUtil.setParticipantMedicalIdentifierDefault(pmIdentifier);
				//End:-  Change for API Search 

				pmIdentifier.setParticipant(participant);
				if(pmIdentifier.getId()!=null)
				{
					dao.update(pmIdentifier);
				}
				else if(pmIdentifier.getId()==null||pmIdentifier.getId().equals(""))
				{
					dao.insert(pmIdentifier,true);
				}

				//Audit of ParticipantMedicalIdentifier.
				ParticipantMedicalIdentifier oldPmIdentifier = (ParticipantMedicalIdentifier) getCorrespondingOldObject(
						oldParticipantMedicalIdentifierCollection, pmIdentifier.getId());

				((HibernateDAO)dao).audit(pmIdentifier, oldPmIdentifier);
			}

			//Updating the Collection Protocol Registration of the participant
			//(Abhishek Mehta)
			CollectionProtocolRegistrationBizLogic cprBizLogic = new CollectionProtocolRegistrationBizLogic();

			Collection oldCollectionProtocolRegistrationCollection = oldParticipant.getCollectionProtocolRegistrationCollection();
			Collection cprCollection = participant.getCollectionProtocolRegistrationCollection();

			Iterator itCPRColl = cprCollection.iterator();
			while (itCPRColl.hasNext())
			{
				CollectionProtocolRegistration collectionProtReg = (CollectionProtocolRegistration) itCPRColl.next();

				ApiSearchUtil.setCollectionProtocolRegistrationDefault(collectionProtReg);

				if(collectionProtReg.getCollectionProtocol().getId()!=null&&!collectionProtReg.equals(""))
				{
					collectionProtReg.setParticipant(participant);

					//Audit of CollectionProtocolRegistration.
					CollectionProtocolRegistration oldcollectionProtocolRegistration = (CollectionProtocolRegistration) getCorrespondingOldObject(
							oldCollectionProtocolRegistrationCollection, collectionProtReg.getId());

					if (collectionProtReg.getId() == null) // If Collection Protocol Registration is not happened for given participant
					{
						collectionProtReg.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
						cprBizLogic.insert(collectionProtReg, dao, sessionDataBean);
						cprIdList.add(collectionProtReg.getId());
						continue;
					}
					cprBizLogic.update(dao, collectionProtReg, oldcollectionProtocolRegistration, sessionDataBean);
				}
			}

			//Disable the associate collection protocol registration
			Logger.out.debug("participant.getActivityStatus() " + participant.getActivityStatus());
			if (participant.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED))
			{
				Logger.out.debug("participant.getActivityStatus() " + participant.getActivityStatus());
				Long participantIDArr[] = {participant.getId()};

				CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic) BizLogicFactory.getInstance().getBizLogic(
						Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
				bizLogic.disableRelatedObjectsForParticipant(dao, participantIDArr);
			}
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	private void deleteOldParticipantRaceColl(Collection raceCollection, DAO dao) throws DAOException
	{
		if(raceCollection != null)
		{
			Iterator itr = raceCollection.iterator();
			while(itr.hasNext())
			{
				Race race = (Race) itr.next();
				dao.delete(race);
			}
		}
	}
	
	private void insertNewParticipantRaceColl(Collection raceCollection,Participant participant,SessionDataBean sessionDataBean,DAO dao) throws DAOException, UserNotAuthorizedException
	{
		if(raceCollection != null)
		{
			Iterator itr = raceCollection.iterator();
			while(itr.hasNext())
			{
				Race race = (Race) itr.next();
				race.setParticipant(participant);
				dao.insert(race,false);
			}
		}
	}
	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
	 *//*
	protected void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		Logger.out.debug(" privilegeName:" + privilegeName + " objectType:" + objectType + " objectIds:" + Utility.getArrayString(objectIds)
				+ " userId:" + userId + " roleId:" + roleId + " assignToUser:" + assignToUser);
		super.setPrivilege(dao, privilegeName, objectType, objectIds, userId, roleId, assignToUser, assignOperation);

		CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic) BizLogicFactory.getInstance().getBizLogic(
				Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		bizLogic.assignPrivilegeToRelatedObjectsForParticipant(dao, privilegeName, objectIds, userId, roleId, assignToUser, assignOperation);
	}
*/
	/**
	 * Assigns the privilege to related objects for Collection Protocol Registration.
	 * @param dao
	 * @param privilegeName
	 * @param longs
	 * @param userId
	 * @param roleId
	 * @param assignToUser
	 * @throws DAOException
	 * @throws SMException
	 */
/*	public void assignPrivilegeToRelatedObjectsForCPR(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId,
			boolean assignToUser, boolean assignOperation) throws SMException, DAOException
	{
		List listOfSubElement = super.getRelatedObjects(dao, CollectionProtocolRegistration.class,
				new String[]{Constants.PARTICIPANT_IDENTIFIER_IN_CPR + "." + Constants.SYSTEM_IDENTIFIER}, new String[]{Constants.SYSTEM_IDENTIFIER},
				objectIds);
		Logger.out.debug(" CPR Ids:" + Utility.getArrayString(objectIds) + " Related Participant Ids:" + listOfSubElement);
		if (!listOfSubElement.isEmpty())
		{
			super.setPrivilege(dao, privilegeName, Participant.class, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
					assignOperation);
		}
	}*/

	/**
	 * This method check for duplicate collection protocol registration for given participant
	 * @param collectionProtocolRegistrationCollection
	 * @return
	 */
	private boolean isDuplicateCollectionProtocol(Collection collectionProtocolRegistrationCollection)
	{
		Collection newCollectionProtocolRegistrationCollection = new LinkedHashSet();
		Iterator itCollectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection.iterator();
		while (itCollectionProtocolRegistrationCollection.hasNext())
		{
			CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) itCollectionProtocolRegistrationCollection
					.next();
			if(collectionProtocolRegistration.getCollectionProtocol()!=null&&!collectionProtocolRegistration.equals(""))
			{
				long collectionProtocolId = collectionProtocolRegistration.getCollectionProtocol().getId().longValue();
				if (isCollectionProtocolExist(newCollectionProtocolRegistrationCollection, collectionProtocolId))
				{
				return true;
				}
				else
				{
					newCollectionProtocolRegistrationCollection.add(collectionProtocolRegistration);
				}
			
			}
		}
		return false;
	}

	private boolean isCollectionProtocolExist(Collection collectionProtocolRegistrationCollection, long collectinProtocolId)
	{
		boolean isCollectionProtocolExist = false;
		Iterator itCollectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection.iterator();
		while (itCollectionProtocolRegistrationCollection.hasNext())
		{
			CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) itCollectionProtocolRegistrationCollection
					.next();
			long cpId = collectionProtocolRegistration.getCollectionProtocol().getId().longValue();
			if (cpId == collectinProtocolId)
			{
				isCollectionProtocolExist = true;
				return isCollectionProtocolExist;
			}
		}

		return false;
	}

	/**
	 * 
	 * @param dao
	 * @param cprId
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	protected boolean isSpecimenExistsForRegistration(DAO dao, Long cprId) throws BizLogicException
	{

		String hql = " select " + " elements(scg.specimenCollection) " + "from "
				+ " edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr"
				+ ", edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg" + ", edu.wustl.catissuecore.domain.Specimen as s"
				+ " where cpr.id = " + cprId + " and " + " cpr.id = scg.collectionProtocolRegistration.id and"
				+ " scg.id = s.specimenCollectionGroup.id and " + " s.activityStatus = '" + Constants.ACTIVITY_STATUS_ACTIVE + "'";

		List scgList = (List) executeHqlQuery(dao, hql);
		if ((scgList != null) && (scgList).size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	protected boolean isSpecimenExists(DAO dao, Long participantId) throws BizLogicException
	{

			String hql = " select" + " elements(scg.specimenCollection) " + "from" + " edu.wustl.catissuecore.domain.Participant as p"
			+ ",edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr"
			+ ", edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg" + ", edu.wustl.catissuecore.domain.Specimen as s"
			+ " where p.id = " + participantId + " and" + " p.id = cpr.participant.id and"
			+ " cpr.id = scg.collectionProtocolRegistration.id and" + " scg.id = s.specimenCollectionGroup.id and " + " s.activityStatus = '"
			+ Constants.ACTIVITY_STATUS_ACTIVE + "'";

			List specimenList = (List) executeHqlQuery(dao, hql);
			if ((specimenList != null) && (specimenList).size() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		

	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		Participant participant = (Participant) obj;
		Validator validator = new Validator();
		//Added by Ashish Gupta

		String message = "";
		if (participant == null)
		{
			throw getBizLogicException(null, "domain.object.null.err.msg", "Participant");
		}

		String errorKeyForBirthDate = "";
		String errorKeyForDeathDate = "";

		String birthDate = Utility.parseDateToString(participant.getBirthDate(),CommonServiceLocator.getInstance().getDatePattern());
		if (!validator.isEmpty(birthDate))
		{
			errorKeyForBirthDate = validator.validateDate(birthDate, true);
			if (errorKeyForBirthDate.trim().length() > 0)
			{
				message = ApplicationProperties.getValue("participant.birthDate");
				throw getBizLogicException(null, errorKeyForBirthDate, message);
			}
		}

		String deathDate = Utility.parseDateToString(participant.getDeathDate(),CommonServiceLocator.getInstance().getDatePattern());
		if (!validator.isEmpty(deathDate))
		{
			errorKeyForDeathDate = validator.validateDate(deathDate, true);
			if (errorKeyForDeathDate.trim().length() > 0)
			{
				message = ApplicationProperties.getValue("participant.deathDate");
				throw getBizLogicException(null, errorKeyForDeathDate, message);
			}
		}

		if (participant.getVitalStatus() == null || !participant.getVitalStatus().equals("Dead"))
		{
			if (!validator.isEmpty(deathDate))
			{
				throw getBizLogicException(null, "participant.invalid.enddate", "");
			}
		}
		if ((!validator.isEmpty(birthDate) && !validator.isEmpty(deathDate))
				&& (errorKeyForDeathDate.trim().length() == 0 && errorKeyForBirthDate.trim().length() == 0))
		{
			boolean errorKey1 = validator.compareDates(Utility.parseDateToString(participant.getBirthDate(), CommonServiceLocator.getInstance().getDatePattern()),
					Utility.parseDateToString(participant.getDeathDate(), CommonServiceLocator.getInstance().getDatePattern()));

			if (!errorKey1)
			{
			
				throw getBizLogicException(null, "participant.invaliddate", "");
			}
		}

		if (!validator.isEmpty(participant.getSocialSecurityNumber()))
		{
			if (!validator.isValidSSN(participant.getSocialSecurityNumber()))
			{
				message = ApplicationProperties.getValue("participant.socialSecurityNumber");
				throw getBizLogicException(null, "errors.invalid", message);
			}
		}

		Collection paticipantMedicicalCollection = participant.getParticipantMedicalIdentifierCollection();
		if (paticipantMedicicalCollection != null && !paticipantMedicicalCollection.isEmpty())
		{
			Iterator itr = paticipantMedicicalCollection.iterator();
			while (itr.hasNext())
			{
				ParticipantMedicalIdentifier participantIdentifier = (ParticipantMedicalIdentifier) itr.next();
				Site site = participantIdentifier.getSite();
				String medicalRecordNo = participantIdentifier.getMedicalRecordNumber();
				if (validator.isEmpty(medicalRecordNo) || site == null || site.getId() == null)
				{
					throw getBizLogicException(null, "errors.participant.extiden.missing", "");
				}
			}
		}

		Collection collectionProtocolRegistrationCollection = participant.getCollectionProtocolRegistrationCollection();
		if (collectionProtocolRegistrationCollection != null && !collectionProtocolRegistrationCollection.isEmpty())
		{
			Iterator itrCollectionProtocolRegistration = collectionProtocolRegistrationCollection.iterator();
			while (itrCollectionProtocolRegistration.hasNext())
			{
				CollectionProtocolRegistration collectionProtocolRegistrationIdentifier = (CollectionProtocolRegistration) itrCollectionProtocolRegistration
						.next();
				if(collectionProtocolRegistrationIdentifier.getCollectionProtocol()!=null&&!collectionProtocolRegistrationIdentifier.equals(""))
				{
					long collectionProtocolTitle = collectionProtocolRegistrationIdentifier.getCollectionProtocol().getId().longValue();
					String collectionProtocolRegistrationDate = Utility.parseDateToString(collectionProtocolRegistrationIdentifier.getRegistrationDate(),
							CommonServiceLocator.getInstance().getDatePattern());
					String errorKey = validator.validateDate(collectionProtocolRegistrationDate, true);
					if (collectionProtocolTitle <= 0 || errorKey.trim().length() > 0)
					{
						throw getBizLogicException(null, "errors.participant.collectionProtocolRegistration.missing", "");
					}
					
				}
				//				check the activity status of all the specimens associated to the collection protocol registration
				if (collectionProtocolRegistrationIdentifier.getActivityStatus() != null
						&& collectionProtocolRegistrationIdentifier.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
				{
					
						boolean isSpecimenExist = (boolean) isSpecimenExistsForRegistration(dao, (Long) collectionProtocolRegistrationIdentifier
								.getId());
						if (isSpecimenExist)
						{
							throw getBizLogicException(null, "collectionprotocolregistration.scg.exists", "");
						}

					
				
				}

			}
		}

		boolean isDuplicateCollectionProtocol = isDuplicateCollectionProtocol(collectionProtocolRegistrationCollection);
		if (isDuplicateCollectionProtocol)
		{
			throw getBizLogicException(null, "errors.participant.duplicate.collectionProtocol", "");
		}

		//Validation for Blank Participant 
		//		if (validator.isEmpty(participant.getLastName())
		//				&& validator.isEmpty(participant.getFirstName())
		//				&& validator.isEmpty(participant.getMiddleName())
		//				&& validator.isEmpty(participant.getBirthDate().toString())
		//				&& (validator.isEmpty(participant.getDeathDate().toString()))
		//				&& !validator.isValidOption(participant.getGender())
		//				&& !validator.isValidOption(participant.getVitalStatus())
		//				&& !validator.isValidOption(participant.getSexGenotype())
		//				&& participant.getEthnicity().equals("-1")
		//				&& validator.isEmpty(socialSecurityNumberPartA + socialSecurityNumberPartB
		//						+ socialSecurityNumberPartC))
		//		{			
		//			throw new DAOException(ApplicationProperties.getValue("errors.participant.atLeastOneFieldRequired"));
		//		}

		//Validations for Add-More Block
		//		 String className = "ParticipantMedicalIdentifier:";
		//		 String key1 = "_Site_" + Constants.SYSTEM_IDENTIFIER;
		//		 String key2 = "_medicalRecordNumber";
		//		 String key3 = "_" + Constants.SYSTEM_IDENTIFIER;
		//		 int index = 1;
		//
		//		 while(true)
		//		 {
		//		 String keyOne = className + index + key1;
		//		 String keyTwo = className + index + key2;
		//		 String keyThree = className + index + key3;
		//		 
		//		 String value1 = (String)values.get(keyOne);
		//		 String value2 = (String)values.get(keyTwo);
		//		 
		//		 if(value1 == null || value2 == null)
		//		 {
		//		 break;
		//		 }
		//		 else if(!validator.isValidOption(value1) && value2.trim().equals(""))
		//		 {
		//		 values.remove(keyOne);
		//		 values.remove(keyTwo);
		//		 values.remove(keyThree);
		//		 }
		//		 else if((validator.isValidOption(value1) && value2.trim().equals("")) || (!validator.isValidOption(value1) && !value2.trim().equals("")))
		//		 {
		//		 errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.participant.missing",ApplicationProperties.getValue("participant.msg")));
		//		 break;
		//		 }
		//		 index++;
		//		 }

		// END

		if (!validator.isEmpty(participant.getVitalStatus()))
		{
			List vitalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_VITAL_STATUS, null);
			if (!Validator.isEnumeratedOrNullValue(vitalStatusList, participant.getVitalStatus()))
			{
				throw getBizLogicException(null, "participant.gender.errMsg", "");
			}
		}

		if (!validator.isEmpty(participant.getGender()))
		{
			List genderList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_GENDER, null);

			if (!Validator.isEnumeratedOrNullValue(genderList, participant.getGender()))
			{
				throw getBizLogicException(null, "participant.gender.errMsg", "");
			}
		}

		if (!validator.isEmpty(participant.getSexGenotype()))
		{
			List genotypeList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_GENOTYPE, null);
			if (!Validator.isEnumeratedOrNullValue(genotypeList, participant.getSexGenotype()))
			{
				throw getBizLogicException(null, "participant.genotype.errMsg", "");
			}
		}

		Collection raceCollection = participant.getRaceCollection();
		if (raceCollection != null && !raceCollection.isEmpty())
		{
			List raceList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RACE, null);
			Iterator itr = raceCollection.iterator();
			while (itr.hasNext())
			{
				Race race = (Race) itr.next();
				if (race != null)
				{
					String race_name = (String) race.getRaceName();
					if (!validator.isEmpty(race_name) && !Validator.isEnumeratedOrNullValue(raceList, race_name))
					{
						throw getBizLogicException(null, "participant.race.errMsg", "");
					}
				}
			}
		}

		if (!validator.isEmpty(participant.getEthnicity()))
		{
			List ethnicityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_ETHNICITY, null);
			if (!Validator.isEnumeratedOrNullValue(ethnicityList, participant.getEthnicity()))
			{
				throw getBizLogicException(null, "participant.ethnicity.errMsg", "");
			}
		}

		if (operation.equals(Constants.ADD))
		{
			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(participant.getActivityStatus()))
			{
				throw getBizLogicException(null, "activityStatus.active.errMsg", "");
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, participant.getActivityStatus()))
			{
				throw getBizLogicException(null, "activityStatus.errMsg", "");
			}
		}

		//check the activity status of all the specimens associated to the participant
		if (participant.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
		{
			
				boolean isSpecimenExist = (boolean) isSpecimenExists(dao, (Long) participant.getId());
				if (isSpecimenExist)
				{
					throw getBizLogicException(null, "participant.specimen.exists", "");
				}

			
		}
		return true;
	}

	/**
	 * @param participant - participant object 
	 * @return - List 
	 * @throws Exception
	 */
	public List getListOfMatchingParticipants(Participant participant,LookupLogic lookupLogic) throws Exception
	{
		// getting the instance of ParticipantLookupLogic class
		//	LookupLogic participantLookupLogic = (LookupLogic) Utility.getObject(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_ALGO));

		// Creating the DefaultLookupParameters object to pass as argument to lookup function
		// This object contains the Participant with which matching participant are to be found and the cutoff value.
		DefaultLookupParameters params = new DefaultLookupParameters();
		params.setObject(participant);

		List listOfParticipants = new ArrayList();
		// getting instance of catissueCoreCacheManager and getting participantMap from cache
		CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
		HashMap participantMap = (HashMap) catissueCoreCacheManager.getObjectFromCache(Constants.MAP_OF_PARTICIPANTS);

		Object participants[] = participantMap.values().toArray();
		listOfParticipants.addAll(participantMap.values());

		params.setListOfParticipants(listOfParticipants);

		//calling thr lookup function which returns the List of ParticipantResuld objects.
		//ParticipantResult object contains the matching participant and the probablity.
	List matchingParticipantList = lookupLogic.lookup(params);

		return matchingParticipantList;

	}

	/**
	 * This function returns the list of all the objects present in the Participant table.
	 * Two queries are executed from this function to get participant data and race data.
	 * Participant objects are formed by retrieving only required data, making it time and space efficient 
	 * @return - List of participants 
	 */
	/*public Map getAllParticipants() throws Exception
	{
		// Initialising instance of IBizLogic
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String sourceObjectName = Participant.class.getName();
		String selectColumnNames[] = {"id", "lastName", "firstName", "middleName", "birthDate", "gender", "sexGenotype", "ethnicity",
				"socialSecurityNumber", "activityStatus", "deathDate", "vitalStatus"};
		String whereColumnName[] = {"activityStatus"};
		String whereColumnCondition[] = {"!="};
		Object whereColumnValue[] = {Constants.ACTIVITY_STATUS_DISABLED};

		// getting all the participants from the database 
		List listOfParticipants = bizLogic.retrieve(sourceObjectName, selectColumnNames, whereColumnName, whereColumnCondition, whereColumnValue,
				null);

		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		dao.openSession(null);

		//String queryStr = "SELECT * FROM CATISSUE_RACE WHERE PARTICIPANT_ID IN (SELECT PARTICIPANT_ID FROM CATISSUE_PARTICIPANT WHERE ACTIVITY_STATUS!='DISABLED')";
		String participantMedicalIdentifierStr = "(SELECT * FROM catissue_part_medical_id WHERE (PARTICIPANT_ID IN (SELECT PARTICIPANT_ID FROM CATISSUE_PARTICIPANT WHERE ACTIVITY_STATUS!='DISABLED')) AND MEDICAL_RECORD_NUMBER!='NULL')";
		//List listOfRaceObjects = new ArrayList();
		List listOfParticipantMedicalIdentifier = new ArrayList();
		try
		{
			//listOfRaceObjects = dao.executeQuery(queryStr, null, false, null);
			listOfParticipantMedicalIdentifier = dao.executeQuery(participantMedicalIdentifierStr, null, false, null);
		}
		catch (Exception ex)
		{
			throw new DAOException(ex.getMessage());
		}
		dao.closeSession();
		
		
		DAO hibernateDao = DAOFactory.getInstance().getDAO(0);
		
		hibernateDao.openSession(null);
			
			//String queryStr = "SELECT * FROM CATISSUE_RACE WHERE PARTICIPANT_ID IN (SELECT PARTICIPANT_ID FROM CATISSUE_PARTICIPANT WHERE ACTIVITY_STATUS!='DISABLED')";
			String queryStr = "from Race R1 where R1.participant.id in (select P1.id from Participant P1 where P1.activityStatus != 'DISABLED')";
			//String participantMedicalIdentifierStr = Medi"(SELECT * FROM catissue_part_medical_id WHERE (PARTICIPANT_ID IN (SELECT PARTICIPANT_ID FROM CATISSUE_PARTICIPANT WHERE ACTIVITY_STATUS!='DISABLED')) AND MEDICAL_RECORD_NUMBER!='NULL')";
			//String participantMedicalIdentifierStr = "from ParticipantMedicalIdentifier";
					// pmi where pmi.participant.id in (select P2.id from Particiant P2 where P2.activityStatus != 'DISABLED') AND pmi.medicalRecordNumber!='NULL'";
			List listOfRaceObjects = new ArrayList();
			//List listOfParticipantMedicalIdentifier = new ArrayList();
			try
			{
				listOfRaceObjects = hibernateDao.executeQuery(queryStr, null, false, null);
				//listOfParticipantMedicalIdentifier = dao.executeQuery(participantMedicalIdentifierStr, null, false, null);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				throw new DAOException(ex.getMessage());
			}
			hibernateDao.closeSession();
		
		
		Map mapOfRaceCollection = new HashMap();
		for (int i = 0; i < listOfRaceObjects.size(); i++)
		{
			Race race = (Race) listOfRaceObjects.get(i);
			Long participantId = race.getParticipant().getId();
			//String race = (String) objectArray.get(1);
			if (mapOfRaceCollection.get(participantId) == null)
			{
				mapOfRaceCollection.put(participantId, new HashSet());
			}
			((HashSet) mapOfRaceCollection.get(participantId)).add(race);
		}
		//Added retrival of Participant Medical Identifier
		//(Virender Mehta)
		Map mapOfParticipantMedicalIdentifierCollection = new HashMap();
		for (int i = 0; i < listOfParticipantMedicalIdentifier.size(); i++)
		{
			List objectArray = (ArrayList) listOfParticipantMedicalIdentifier.get(i);
			Long participantId = (new Long(objectArray.get(3).toString()));
			String participantMedicalIdentifier = (String) objectArray.get(1);
			String participantMedicalIdentifierSite = (String) objectArray.get(2);
			if (mapOfParticipantMedicalIdentifierCollection.get(participantId) == null)
			{
				mapOfParticipantMedicalIdentifierCollection.put(participantId, new ArrayList());
			}
			((ArrayList) mapOfParticipantMedicalIdentifierCollection.get(participantId)).add(participantMedicalIdentifier);
			((ArrayList) mapOfParticipantMedicalIdentifierCollection.get(participantId)).add(participantMedicalIdentifierSite);
		}
		Map mapOfParticipants = new HashMap();
		for (int i = 0; i < listOfParticipants.size(); i++)
		{
			Object[] obj = (Object[]) listOfParticipants.get(i);

			Long id = (Long) obj[0];
			String lastName = (String) obj[1];
			String firstName = (String) obj[2];
			String middleName = (String) obj[3];
			Date birthDate = (Date) obj[4];
			String gender = (String) obj[5];
			String sexGenotype = (String) obj[6];
			String ethnicity = (String) obj[7];
			String socialSecurityNumber = (String) obj[8];
			String activityStatus = (String) obj[9];
			Date deathDate = (Date) obj[10];
			String vitalStatus = (String) obj[11];

			Participant participant = new Participant(id, lastName, firstName, middleName, birthDate, gender, sexGenotype,
					(Collection) mapOfRaceCollection.get(id), ethnicity, socialSecurityNumber, activityStatus, deathDate, vitalStatus,
					(Collection) mapOfParticipantMedicalIdentifierCollection.get(id));
			mapOfParticipants.put(participant.getId(), participant);
		}
		return mapOfParticipants;

	}*/
	public Map<Long,Participant> getAllParticipants() throws BizLogicException
	{
		DAO dao = null;
		Map<Long,Participant> mapOfParticipants = new HashMap<Long,Participant>();
		try
		{
			dao = openDAOSession(null);

			
			String participantQueryStr = "from " + Participant.class.getName() + " where activityStatus !='" + Status.ACTIVITY_STATUS_DISABLED.toString() + "'";

			List<Participant> listOfParticipants = dao.executeQuery(participantQueryStr);
			if(listOfParticipants != null)
			{
				Iterator<Participant> participantIterator = listOfParticipants.iterator();
				while(participantIterator.hasNext())
				{
					Participant participant = (Participant)participantIterator.next();
					Participant cloneParticipant = new Participant(participant); 
					Long participantId = cloneParticipant.getId();
					mapOfParticipants.put(participantId, cloneParticipant);
				}
			}
		}
		catch (DAOException e) 
		{
			throw getBizLogicException(e, "dao.error", "Couldn't get participant");
		}
		finally
		{
			closeDAOSession(dao);
		}
		return mapOfParticipants;
	}

	/**
	 * This function takes identifier as parameter and returns corresponding Participant
	 * @return - Participant object
	 */
	public Participant getParticipantById(Long identifier) throws Exception
	{
		// Initialising instance of IBizLogic
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String sourceObjectName = Participant.class.getName();

		// getting all the participants from the database 
		Object object = bizLogic.retrieve(sourceObjectName, identifier);
		Participant participant = (Participant) object;
		return participant;

	}

	public List getColumnList(List columnList,StringBuffer partMRNColName) throws BizLogicException
	{
		List displayList = new ArrayList();
		JDBCDAO jdbcDao = null;
		try
		{
			jdbcDao = openJDBCSession();
			jdbcDao.openSession(null);
			String sql = "SELECT  columnData.COLUMN_NAME,displayData.DISPLAY_NAME FROM "
					+ "CATISSUE_INTERFACE_COLUMN_DATA columnData,CATISSUE_TABLE_RELATION relationData,"
					+ "CATISSUE_QUERY_TABLE_DATA tableData,CATISSUE_SEARCH_DISPLAY_DATA displayData "
					+ "where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and " + "relationData.PARENT_TABLE_ID = tableData.TABLE_ID and "
					+ "relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and "
					+ "columnData.IDENTIFIER = displayData.COL_ID and tableData.ALIAS_NAME = 'Participant'";

			Logger.out.debug("DATA ELEMENT SQL : " + sql);
			List list = jdbcDao.executeQuery(sql);
			Iterator iterator1 = columnList.iterator();

			while (iterator1.hasNext())
			{
				String colName1 = (String) iterator1.next();
				Logger.out.debug("colName1------------------------" + colName1);
				Iterator iterator2 = list.iterator();
				while (iterator2.hasNext())
				{
					List rowList = (List) iterator2.next();
					String colName2 = (String) rowList.get(0);
					Logger.out.debug("colName2------------------------" + colName2);
					if (colName1.equals(colName2))
					{
						if(colName1.equals(Constants.PARTICIPANT_MEDICAL_RECORD_NO))
						{
							partMRNColName.append((String) rowList.get(1)); 
						}
						displayList.add((String) rowList.get(1));
					}
				}
			}
			jdbcDao.closeSession();
		}
		catch (Exception exp)
		{
			throw getBizLogicException(exp, "dao.error", "");
		}
		finally
		{
			closeJDBCSession(jdbcDao);
		}

		return displayList;
	}

	public String getPageToShow()
	{
		return new String();
	}

	public List getMatchingObjects()
	{
		return new ArrayList();
	}

	/**
	 * Executes hql Query and returns the list of associated scg id
	 * @param participant Participant
	 * @throws BizLogicException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public List getSCGList(Long participantId) throws BizLogicException
	{
		DAO dao  = null;
		try
		{
			String scgHql = "select scg.id, scg.surgicalPathologyNumber, scg.identifiedSurgicalPathologyReport.id "
				+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg, "
				+ " edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr," + " edu.wustl.catissuecore.domain.Participant as p "
				+ " where p.id = " + participantId + " and p.id = cpr.participant.id "
				+ " and scg.id in elements(cpr.specimenCollectionGroupCollection)";

			dao = openDAOSession(null);
			List list = null;

			list = dao.executeQuery(scgHql);
			return list;
		}
		catch(Exception exp)
		{
			throw getBizLogicException(exp, "dao.error", "");
		}
		finally
		{
			closeDAOSession(dao);
		}
	}

	/**
	 * Executes hql Query and returns the results.
	 * @param hql String hql
	 * @throws BizLogicException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	private List executeHqlQuery(DAO dao, String hql) throws BizLogicException
	{
		try
		{
			List list = dao.executeQuery(hql);
			return list;
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		
	}
	
	public List getCPForUserWithRegistrationAcess(long userId) throws BizLogicException
	{
		List<NameValueBean> cpList = new ArrayList<NameValueBean>();
		Set<Long> cpIds = new HashSet<Long>();
		cpList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
		DAO dao = null;
		try 
		{
			dao = openDAOSession(null);
			User user = (User) dao.retrieveById(User.class.getName(),userId);
			PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
			PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(user.getLoginName());
			Collection <CollectionProtocol> cpCollection = user.getAssignedProtocolCollection();
			if (cpCollection != null && !cpCollection.isEmpty())
			{
				for (CollectionProtocol cp : cpCollection)
				{
					StringBuffer sb = new StringBuffer();
					sb.append(CollectionProtocol.class.getName()).append("_").append(cp.getId());
					boolean hasPrivilege = privilegeCache.hasPrivilege(sb.toString(), 
							Variables.privilegeDetailsMap.get(Constants.CP_BASED_VIEW_FILTRATION));
					
					if (hasPrivilege)
					{
						cpList.add(new NameValueBean(cp.getShortTitle(),cp.getId()));
						cpIds.add(cp.getId());
					}
				}
			}
			
			UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
			Set<Long> siteIds = userBizLogic.getRelatedSiteIds(userId);
		
			if (siteIds != null && !siteIds.isEmpty())
			{
				SiteBizLogic siteBizLogic = (SiteBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SITE_FORM_ID);
				for (Long siteId : siteIds)
				{
					String peName = Constants.getCurrentAndFuturePGAndPEName(siteId);
					if (privilegeCache.hasPrivilege(peName,Variables.privilegeDetailsMap.get(Constants.CP_BASED_VIEW_FILTRATION)))
					{
						Collection<CollectionProtocol> cp1Collection = siteBizLogic.getRelatedCPs(siteId);
						
						if (cp1Collection != null && !cp1Collection.isEmpty())
						{
							List<NameValueBean> list = new ArrayList<NameValueBean>();
							for (CollectionProtocol cp1 : cp1Collection)
							{
								if(!cpIds.contains(cp1.getId()))
								{
									list.add(new NameValueBean(cp1.getShortTitle(),cp1.getId()));
								}
							}
							cpList.addAll(list);
						}
								
					}
				}
			}
			

		} catch (DAOException e) {
			throw getBizLogicException(e, "dao.error", "Couldn't get CP for user");
		}
		catch (SMException e) {
			throw AppUtility.handleSMException(e);
		}
		finally
		{
			closeDAOSession(dao);
		}
		
		return cpList;
	}
	
	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 */
	public String getObjectId(DAO dao, Object domainObject) 
	{
		String objectId = Constants.ADD_GLOBAL_PARTICIPANT;
		
		if (domainObject instanceof Participant)
		{
			Participant participant = (Participant) domainObject;
			Collection<CollectionProtocolRegistration> cprCollection = participant.getCollectionProtocolRegistrationCollection();
			if(cprCollection.isEmpty())
			{
				return objectId;
			}
			
			else
			{	
				StringBuffer sb = new StringBuffer();
				boolean isNewCPRPresent = false;
				
				if (cprCollection != null && !cprCollection.isEmpty())
				{
					sb.append(Constants.COLLECTION_PROTOCOL_CLASS_NAME);
					for (CollectionProtocolRegistration cpr : cprCollection)
					{
						if (cpr.getId()==null)
						{
							if(cpr.getCollectionProtocol()==null)
							{
								return objectId;
							}
							sb.append("_").append(cpr.getCollectionProtocol().getId());
							isNewCPRPresent = true;
						}
					}
				}
				if(isNewCPRPresent)
				{
					return sb.toString();
				}
			}
		}
		return objectId;
	}
	
	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeKey(Object domainObject)
    {
    	return Constants.ADD_EDIT_PARTICIPANT;
    }
	
	/**
	 * Over-ridden for the case of Non - Admin user should be able to Add 
	 * Global Participant 
	 * @throws UserNotAuthorizedException 
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.DAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean) throws BizLogicException  
	{
		boolean isAuthorized = false;
		
		try 
		{
			if(sessionDataBean != null && sessionDataBean.isAdmin())
			{
				return true;
			}

			String privilegeName = getPrivilegeName(domainObject);
			String protectionElementName = getObjectId(dao, domainObject);
			PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName());

			if(protectionElementName.equals(Constants.ADD_GLOBAL_PARTICIPANT))
			{
				User user = null;
				try 
				{
					user = (User) dao.retrieveById(User.class.getName(), sessionDataBean.getUserId());
				} 
				catch (DAOException e) 
				{
					Logger.out.error(e.getMessage(), e);
				}
				Collection<CollectionProtocol> cpCollection = user.getAssignedProtocolCollection();
				if (cpCollection != null && !cpCollection.isEmpty())
				{
					for(CollectionProtocol cp : cpCollection)
					{
						if(privilegeCache.hasPrivilege(CollectionProtocol.class.getName()+"_"+cp.getId(), privilegeName))
						{
							isAuthorized = true;
							break;
						}
					}
					if(!isAuthorized)
					{
						isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(privilegeName, sessionDataBean, null);
					}
				} else
				{
					isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(privilegeName, sessionDataBean, null);
				}
			} 
			else
			{
				String [] prArray = protectionElementName.split("_");
				String baseObjectId = prArray[0];
				String objId = "";
				for (int i = 1 ; i < prArray.length;i++)
				{
					objId = baseObjectId + "_" + prArray[i];
					isAuthorized = privilegeCache.hasPrivilege(objId, privilegeName);
					if (!isAuthorized)
					{
						break;
					}
				}

			}

			if(isAuthorized)
			{
				return isAuthorized;
			}
			else
				// Check for ALL CURRENT & FUTURE CASE
			{
				if(!protectionElementName.equals(Constants.ADD_GLOBAL_PARTICIPANT))
				{
					String protectionElementNames[] = protectionElementName.split("_");

					Long cpId = Long.valueOf(protectionElementNames[1]);
					Set<Long> cpIdSet = new UserBizLogic().getRelatedCPIds(sessionDataBean.getUserId(), false);

					if(cpIdSet.contains(cpId))
					{
						//bug 11611 and 11659
						throw AppUtility.getUserNotAuthorizedException(privilegeName, protectionElementName);
					}
					isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(privilegeName, sessionDataBean, protectionElementNames[1]);
				}
			}
			if (!isAuthorized)
			{
				//bug 11611 and 11659
				throw AppUtility.getUserNotAuthorizedException(privilegeName, protectionElementName);    
			}
		} catch (SMException e1) {
			e1.printStackTrace();
		} 
		return isAuthorized;		
	}

	@Override
	public boolean isReadDeniedTobeChecked() {
		return true;
	}
	
	@Override
	public String getReadDeniedPrivilegeName()
	{
		return Permissions.REGISTRATION+","+Permissions.READ_DENIED;
	}
	
	public boolean hasPrivilegeToView(String objName, Long identifier, SessionDataBean sessionDataBean)
	{
		return AppUtility.hasPrivilegeToView(objName, identifier, sessionDataBean, getReadDeniedPrivilegeName());
	}
	
	/**
	 * Returns a list of Specimen objects with their IDs set as TiTLi 
	 * needs only instance IDs in order to refresh indexes
	 * @param the SpecimenCollectionGroup instance
	 * @return list of Specimen objects
	 * @throws BizLogicException
	 */
    private List<Specimen> getSpecimenCollection(SpecimenCollectionGroup scg)throws BizLogicException
	{
		String hql = " select s.id from edu.wustl.catissuecore.domain.Specimen s"
		        + " where s.specimenCollectionGroup.id=" + scg.getId();

		DAO dao = null;
		
		List<Specimen> specimens = new ArrayList<Specimen>();
		try
		{
			dao = openDAOSession(null);
			List specimenIds = dao.executeQuery(hql);
			if (specimenIds != null && (!specimenIds.isEmpty()))
			{
				for (Iterator it = specimenIds.iterator(); it.hasNext();)
				{
					Specimen specimen = new Specimen();
					specimen.setId((Long) it.next());
					specimens.add(specimen);
				}
			}
		}
		catch (Exception e)
		{
			Logger.out.error("Error occured while retrieving Specimen List", e);
		}
		finally
		{
			closeDAOSession(dao);
		}
		return specimens;
	}
	
	@Override
	public void refreshTitliSearchIndex(String operation, Object obj)
	{
		try
		{
			super.refreshTitliSearchIndex(operation, obj);
			Participant participant = (Participant) obj;
			Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection = participant.getCollectionProtocolRegistrationCollection();
			if(collectionProtocolRegistrationCollection != null)
			{
				Iterator<CollectionProtocolRegistration> itcprCollection = collectionProtocolRegistrationCollection.iterator();

				while (itcprCollection.hasNext())
				{
					CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) itcprCollection.next();
					if(cprIdList.contains(cpr.getId()))
					{
						Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection=cpr.getSpecimenCollectionGroupCollection();

						if(specimenCollectionGroupCollection != null)
						{
							Iterator<SpecimenCollectionGroup> itscgCollection = specimenCollectionGroupCollection.iterator();
							while(itscgCollection.hasNext())
							{
								SpecimenCollectionGroup scg = (SpecimenCollectionGroup)itscgCollection.next();
								super.refreshTitliSearchIndex(operation, scg);
								// Collection<Specimen> specimenCollection = scg.getSpecimenCollection();

								// Fetch the Specimens in the given SCG explicitly
								Collection<Specimen> specimenCollection = getSpecimenCollection(scg);
								if (specimenCollection != null)
								{
									Iterator<Specimen> itspecimenCollection = specimenCollection
									.iterator();
									while (itspecimenCollection.hasNext())
									{
										Specimen specimen = (Specimen) itspecimenCollection.next();
										super.refreshTitliSearchIndex(operation, specimen);
									}
								}

							}
						}
					}
				}
			}
		}
		catch(BizLogicException exp)
		{
				Logger.out.debug(exp.getMessage());
		}
	}
}