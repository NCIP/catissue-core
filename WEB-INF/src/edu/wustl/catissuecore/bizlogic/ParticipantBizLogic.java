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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.CacheException;

import org.apache.commons.codec.language.Metaphone;

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
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
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
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.daofactory.DAOConfigFactory;
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

	/**
	 * Logger object.
	 */
	private transient final Logger logger = Logger.getCommonLogger(ParticipantBizLogic.class);

	/**
	 *  List of cprIds.
	 */
	private final List<Long> cprIdList = new ArrayList<Long>();

	/**
	 * Saves the Participant object in the database.
	 * @param obj The storageType object to be saved.
	 *  @param dao - DAO object
	 * @param sessionDataBean - The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final Participant participant = (Participant) obj;
			//update metaPhoneInformartion
			
			Metaphone metaPhoneObj = new Metaphone();
			String lNameMetaPhone = metaPhoneObj.metaphone(participant.getLastName());
			participant.setMetaPhoneCode(lNameMetaPhone);
			
			dao.insert(participant);
			
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			auditManager.insertAudit(dao, participant);
			Collection<ParticipantMedicalIdentifier> pmiCollection = participant
					.getParticipantMedicalIdentifierCollection();

			if (pmiCollection == null)
			{
				pmiCollection = new LinkedHashSet<ParticipantMedicalIdentifier>();
			}
			if (pmiCollection.isEmpty())
			{
				//add a dummy participant MedicalIdentifier for Query.
				final ParticipantMedicalIdentifier participantMedicalIdentifier = new ParticipantMedicalIdentifier();
				participantMedicalIdentifier.setMedicalRecordNumber(null);
				participantMedicalIdentifier.setSite(null);
				pmiCollection.add(participantMedicalIdentifier);
			}

			//Inserting medical identifiers in the database after setting the participant associated.
			final Iterator iterator = pmiCollection.iterator();
			while (iterator.hasNext())
			{
				final ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier) iterator
						.next();
				pmIdentifier.setParticipant(participant);
				dao.insert(pmIdentifier);
				auditManager.insertAudit(dao, pmIdentifier);
			}

			//Inserting collection Protocol Registration info in the database after setting the participant associated.
			//Abhishek Mehta
			final Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection = participant
					.getCollectionProtocolRegistrationCollection();

			if (collectionProtocolRegistrationCollection == null)
			{

			    this.insertAuthData(participant);
			}
			else
			{
			    this.registerToCPR(dao, sessionDataBean, participant);
				
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}

	}

	/**
	 * @param participant - Participant object.
	 * @throws DAOException throws DAOException
	 */
	private void insertAuthData(Participant participant) throws DAOException
	{
		final Set<Participant> protectionObjects = new HashSet<Participant>();
		protectionObjects.add(participant);

	}

	/**
	 * @param dao - DAO object.
	 * @param sessionDataBean - SessionDataBean object
	 * @param participant - Participant object
	 * @throws DAOException throws DAOException
	 */
	private void registerToCPR(DAO dao, SessionDataBean sessionDataBean, Participant participant)
			throws BizLogicException
	{
		final CollectionProtocolRegistrationBizLogic cprBizLogic = new CollectionProtocolRegistrationBizLogic();
		final Collection<CollectionProtocolRegistration> cprCollection = participant
				.getCollectionProtocolRegistrationCollection();

		final Iterator<CollectionProtocolRegistration> itcprCollection = cprCollection.iterator();

		while (itcprCollection.hasNext())
		{
			final CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) itcprCollection
					.next();
			cpr.setParticipant(participant);
			cpr.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
			cprBizLogic.insert(cpr, dao, sessionDataBean);
			this.cprIdList.add(cpr.getId());
		}
	}

	/**
	 * This method gets called after insert method. Any logic after insertnig object in 
	 * database can be included here.
	 * @param obj The inserted object.
	 * @param dao the dao object
	 * @param sessionDataBean session specific data
	 * @throws DAOException throws  DAOException
	 * */
	protected void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
	//	final Participant participant = (Participant) obj;
//		final Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection = participant
//				.getCollectionProtocolRegistrationCollection();
		
		//this.updateCache(obj);
		super.postInsert(obj, dao, sessionDataBean);
		// Commented for removing the cp based cache. 
		/*	
			Iterator itCollectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection.iterator();
			ParticipantRegistrationCacheManager participantRegCacheManager = new ParticipantRegistrationCacheManager();
			while (itCollectionProtocolRegistrationCollection.hasNext())
			{
				CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) itCollectionProtocolRegistrationCollection
						.next();
				participantRegCacheManager.registerParticipant(collectionProtocolRegistration.getCollectionProtocol().getId().longValue(),
						collectionProtocolRegistration.getParticipant().getId().longValue(), collectionProtocolRegistration.getProtocolParticipantIdentifier());
			}
		*/
		
		
		
	}
	
	
	
	

	/**
	 * This method gets called after update method. Any logic after updating into 
	 * database can be included here.
	 * @param dao the  object
	 * @param currentObj The object to be updated.
	 * @param oldObj The old object.
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException throws BizLogicException
	 * */
	protected void postUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		final Participant participant = (Participant) currentObj;
		final Collection cprCollection = participant.getCollectionProtocolRegistrationCollection();
		// removed participant cache
		//this.updateCache(currentObj);
		
		//Added updation of Collection Protocol Registration
		//(Abhishek Mehta)
		
		final CollectionProtocolRegistrationBizLogic cprBizLogic = new CollectionProtocolRegistrationBizLogic();
		//ParticipantRegistrationCacheManager participantRegCacheManager = new ParticipantRegistrationCacheManager();
		final Iterator iteratorCPRCollection = cprCollection.iterator();

		final Participant oldparticipant = (Participant) oldObj;
		final Collection oldCPRCollection = oldparticipant
				.getCollectionProtocolRegistrationCollection();

		Long cpId;//, participantId;
		String protocolParticipantId;

		CollectionProtocolRegistration oldCPRegistration;
		CollectionProtocolRegistration collectionProtocolRegistration;
		while (iteratorCPRCollection.hasNext())
		{
			collectionProtocolRegistration = (CollectionProtocolRegistration) iteratorCPRCollection
					.next();
			if (collectionProtocolRegistration.getCollectionProtocol().getId() != null)
			{
				cpId = collectionProtocolRegistration.getCollectionProtocol().getId().longValue();

				oldCPRegistration = this.getCollectionProtocolRegistrationOld(cpId,
						oldCPRCollection);
				if (oldCPRegistration == null)
				{
				//	participantId = 
				    collectionProtocolRegistration.getParticipant().getId()
							.longValue();
					protocolParticipantId = collectionProtocolRegistration
							.getProtocolParticipantIdentifier();

					if (protocolParticipantId == null)
					{
						protocolParticipantId = Constants.DOUBLE_QUOTES;
					}
					// Commented by Geeta for removing the cache
					/*
						if (collectionProtocolRegistration.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.toString()))
						{
							participantRegCacheManager.deRegisterParticipant(cpId, participantId, protocolParticipantId);
						}
						else
						{
						participantRegCacheManager.registerParticipant(cpId, participantId, protocolParticipantId);
						}
					*/
				}
				else
				{
					cprBizLogic.postUpdate(dao, collectionProtocolRegistration,
							oldCPRegistration, sessionDataBean);
				}
			}
		}
		super.postUpdate(dao, currentObj, oldObj, sessionDataBean);
	}

	/**
	 * Returns CollectionProtocolRegistration object if it exist in collection
	 * @param collectionProtocolId - Long 
	 * @param collectionProtocolRegistrationCollection - Collection of protocol registration.
	 * @return
	 */
	private CollectionProtocolRegistration getCollectionProtocolRegistrationOld(
			long collectionProtocolId, Collection collectionProtocolRegistrationCollection)
	{
		final Iterator itCollectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection
				.iterator();
		while (itCollectionProtocolRegistrationCollection.hasNext())
		{
			final CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) itCollectionProtocolRegistrationCollection
					.next();
			final long cpId = collectionProtocolRegistration.getCollectionProtocol().getId();
			if (cpId == collectionProtocolId)
			{
				return collectionProtocolRegistration;
			}
		}
		return null;
	}

	/**
	 *  This method updates the cache for MAP_OF_PARTICIPANTS,
	 *  should be called in postInsert/postUpdate.
	 * @param obj - participant object
	 */
	private synchronized void updateCache(Object obj)
	{
		final Participant participant = (Participant) obj;
		final Participant cloneParticipant = new Participant(participant);
		//getting instance of catissueCoreCacheManager and getting participantMap from cache
		CatissueCoreCacheManager catissueCoreCacheManager = null;
		//Map mapOfParticipantMedicalIdentifierCollection = new HashMap();
		try
		{
			catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
			final HashMap participantMap = (HashMap) catissueCoreCacheManager
					.getObjectFromCache(Constants.MAP_OF_PARTICIPANTS);
			if (cloneParticipant.getActivityStatus().equalsIgnoreCase(
					Status.ACTIVITY_STATUS_DISABLED.toString()))
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
		catch (final CacheException e)
		{
			this.logger.debug("Exception occured while getting instance of cachemanager");
			e.printStackTrace();
		}

	}

	/**
	 * Updates the persistent object in the database.
	 * @param oad - DAO object 
	 * @param obj The object to be updated.
	 * @param oldObj - Object 
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final Participant participant = (Participant) obj;
			final Participant oldParticipant = (Participant) oldObj;

			/*Collection raceCollection = participant.getRaceCollection();
			participant.setRaceCollection(null);*/

			//deleteOldParticipantRaceColl(oldParticipant.getRaceCollection(),dao);
			
			String lnameMetaPhone=null;
			Metaphone metaPhoneObj = new Metaphone();
			String lNameMetaPhone = metaPhoneObj.metaphone(participant.getLastName());
		 	participant.setMetaPhoneCode(lNameMetaPhone);
			
			
			dao.update(participant);
			//insertNewParticipantRaceColl(raceCollection,participant,sessionDataBean,dao);
			//Audit of Participant.
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			auditManager.updateAudit(dao, obj, oldObj);

			final Collection oldParticipantMedicalIdentifierCollection = (Collection) oldParticipant
					.getParticipantMedicalIdentifierCollection();
			final Collection participantMedicalIdentifierCollection = participant
					.getParticipantMedicalIdentifierCollection();
			final Iterator it = participantMedicalIdentifierCollection.iterator();

			//Updating the medical identifiers of the participant
			while (it.hasNext())
			{
				final ParticipantMedicalIdentifier pmIdentifier = (ParticipantMedicalIdentifier) it
						.next();

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
				if (pmIdentifier.getId() != null)
				{
					dao.update(pmIdentifier);
				}
				else if (pmIdentifier.getId() == null || pmIdentifier.getId().equals(""))
				{
					dao.insert(pmIdentifier);
					auditManager.insertAudit(dao, pmIdentifier);
				}

				//Audit of ParticipantMedicalIdentifier.
				final ParticipantMedicalIdentifier oldPmIdentifier = (ParticipantMedicalIdentifier) this
						.getCorrespondingOldObject(oldParticipantMedicalIdentifierCollection,
								pmIdentifier.getId());

				auditManager.updateAudit(dao, pmIdentifier, oldPmIdentifier);
			}

			//Updating the Collection Protocol Registration of the participant
			//(Abhishek Mehta)
			final CollectionProtocolRegistrationBizLogic cprBizLogic = new CollectionProtocolRegistrationBizLogic();

			final Collection oldCollectionProtocolRegistrationCollection = oldParticipant
					.getCollectionProtocolRegistrationCollection();
			final Collection cprCollection = participant
					.getCollectionProtocolRegistrationCollection();

			final Iterator itCPRColl = cprCollection.iterator();
			while (itCPRColl.hasNext())
			{
				final CollectionProtocolRegistration collectionProtReg = (CollectionProtocolRegistration) itCPRColl
						.next();

				ApiSearchUtil.setCollectionProtocolRegistrationDefault(collectionProtReg);

				if (collectionProtReg.getCollectionProtocol().getId() != null
						&& !collectionProtReg.equals(""))
				{
					collectionProtReg.setParticipant(participant);

					//Audit of CollectionProtocolRegistration.
					final CollectionProtocolRegistration oldcollectionProtocolRegistration = (CollectionProtocolRegistration) this
							.getCorrespondingOldObject(oldCollectionProtocolRegistrationCollection,
									collectionProtReg.getId());

					if (collectionProtReg.getId() == null) // If Collection Protocol Registration is not happened for given participant
					{
						collectionProtReg.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE
								.toString());
						cprBizLogic.insert(collectionProtReg, dao, sessionDataBean);
						this.cprIdList.add(collectionProtReg.getId());
						continue;
					}
					cprBizLogic.update(dao, collectionProtReg, oldcollectionProtocolRegistration,
							sessionDataBean);
				}
			}

			//Disable the associate collection protocol registration
			this.logger.debug("participant.getActivityStatus() " + participant.getActivityStatus());
			if (participant.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.toString()))
			{
				this.logger.debug("participant.getActivityStatus() "
						+ participant.getActivityStatus());
				final Long participantIDArr[] = {participant.getId()};

				final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				final CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic) factory
						.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
				bizLogic.disableRelatedObjectsForParticipant(dao, participantIDArr);
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * @param raceCollection - collection of race obejct.
	 * @param dao - DAO objects
	 * @throws DAOException throws DAOException
	 */
	private void deleteOldParticipantRaceColl(Collection raceCollection, DAO dao)
			throws DAOException
	{
		if (raceCollection != null)
		{
			final Iterator itr = raceCollection.iterator();
			while (itr.hasNext())
			{
				final Race race = (Race) itr.next();
				dao.delete(race);
			}
		}
	}

	/**
	 * @param raceCollection - Collection of race objects.
	 * @param participant - Participant object.
	 * @param sessionDataBean - SessionDataBean object
	 * @param dao - DAO object
	 * @throws DAOException throws DAOException
	 * @throws UserNotAuthorizedException throws UserNotAuthorizedException
	 */
	private void insertNewParticipantRaceColl(Collection raceCollection, Participant participant,
			SessionDataBean sessionDataBean, DAO dao) throws DAOException,
			UserNotAuthorizedException
	{
		if (raceCollection != null)
		{
			final Iterator itr = raceCollection.iterator();
			while (itr.hasNext())
			{
				final Race race = (Race) itr.next();
				race.setParticipant(participant);
				dao.insert(race);
			}
		}
	}

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
	 */
	/*
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
	 * This method check for duplicate collection protocol registration for given participant.
	 * @param collectionProtocolRegistrationCollection - Coolection of cpr objects
	 * @return boolean value based on duplicate collection protocol
	 */
	private boolean isDuplicateCollectionProtocol(
			Collection collectionProtocolRegistrationCollection)
	{
		final Collection newCollectionProtocolRegistrationCollection = new LinkedHashSet();
		final Iterator itCollectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection
				.iterator();
		while (itCollectionProtocolRegistrationCollection.hasNext())
		{
			final CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) itCollectionProtocolRegistrationCollection
					.next();
			if (collectionProtocolRegistration.getCollectionProtocol() != null
					&& !collectionProtocolRegistration.equals(""))
			{
				final long collectionProtocolId = collectionProtocolRegistration
						.getCollectionProtocol().getId().longValue();
				if (this.isCollectionProtocolExist(newCollectionProtocolRegistrationCollection,
						collectionProtocolId))
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

	/**
	 * @param collectionProtocolRegistrationCollection.
	 * @param collectinProtocolId
	 * @return boolean value based on existace of CP.
	 */
	private boolean isCollectionProtocolExist(Collection collectionProtocolRegistrationCollection,
			long collectinProtocolId)
	{
		boolean isCollectionProtocolExist = false;
		final Iterator itCollectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection
				.iterator();
		while (itCollectionProtocolRegistrationCollection.hasNext())
		{
			final CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) itCollectionProtocolRegistrationCollection
					.next();
			final long cpId = collectionProtocolRegistration.getCollectionProtocol().getId()
					.longValue();
			if (cpId == collectinProtocolId)
			{
				isCollectionProtocolExist = true;
				return isCollectionProtocolExist;
			}
		}

		return false;
	}

	/**
	 * @param dao - DAO object.
	 * @param cprId - Long 
	 * @return boolean value 
	 * @throws DAOException throws BizLogicException
	 */
	protected boolean isSpecimenExistsForRegistration(DAO dao, Long cprId) throws BizLogicException
	{

		final String hql = " select " + " elements(scg.specimenCollection) " + "from "
				+ " edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr"
				+ ", edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg"
				+ ", edu.wustl.catissuecore.domain.Specimen as s" + " where cpr.id = " + cprId
				+ " and " + " cpr.id = scg.collectionProtocolRegistration.id and"
				+ " scg.id = s.specimenCollectionGroup.id and " + " s.activityStatus = '"
				+ Status.ACTIVITY_STATUS_ACTIVE.toString() + "'";

		final List scgList = (List) this.executeHqlQuery(dao, hql);
		if ((scgList != null) && (scgList).size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	/**
	 * @param dao - DAO object.
	 * @param participantId - Long
	 * @return boolean value
	 * @throws BizLogicException : BizLogicException
	 */
	protected boolean isSpecimenExists(DAO dao, Long participantId) throws BizLogicException
	{

		final String hql = " select" + " elements(scg.specimenCollection) " + "from"
				+ " edu.wustl.catissuecore.domain.Participant as p"
				+ ",edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr"
				+ ", edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg"
				+ ", edu.wustl.catissuecore.domain.Specimen as s" + " where p.id = "
				+ participantId + " and" + " p.id = cpr.participant.id and"
				+ " cpr.id = scg.collectionProtocolRegistration.id and"
				+ " scg.id = s.specimenCollectionGroup.id and " + " s.activityStatus = '"
				+ Status.ACTIVITY_STATUS_ACTIVE.toString() + "'";

		final List specimenList = (List) this.executeHqlQuery(dao, hql);
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
		final Participant participant = (Participant) obj;
		final Validator validator = new Validator();
		//Added by Ashish Gupta

		String message = "";
		if (participant == null)
		{
			throw this.getBizLogicException(null, "domain.object.null.err.msg", "Participant");
		}

		String errorKeyForBirthDate = "";
		String errorKeyForDeathDate = "";

		final String birthDate = Utility.parseDateToString(participant.getBirthDate(),
				CommonServiceLocator.getInstance().getDatePattern());
		if (!validator.isEmpty(birthDate))
		{
			errorKeyForBirthDate = validator.validateDate(birthDate, true);
			if (errorKeyForBirthDate.trim().length() > 0)
			{
				message = ApplicationProperties.getValue("participant.birthDate");
				throw this.getBizLogicException(null, errorKeyForBirthDate, message);
			}
		}

		final String deathDate = Utility.parseDateToString(participant.getDeathDate(),
				CommonServiceLocator.getInstance().getDatePattern());
		if (!validator.isEmpty(deathDate))
		{
			errorKeyForDeathDate = validator.validateDate(deathDate, true);
			if (errorKeyForDeathDate.trim().length() > 0)
			{
				message = ApplicationProperties.getValue("participant.deathDate");
				throw this.getBizLogicException(null, errorKeyForDeathDate, message);
			}
		}

		if (participant.getVitalStatus() == null || !participant.getVitalStatus().equals("Dead"))
		{
			if (!validator.isEmpty(deathDate))
			{
				throw this.getBizLogicException(null, "participant.invalid.enddate", "");
			}
		}
		if ((!validator.isEmpty(birthDate) && !validator.isEmpty(deathDate))
				&& (errorKeyForDeathDate.trim().length() == 0 && errorKeyForBirthDate.trim()
						.length() == 0))
		{
			final boolean errorKey1 = validator.compareDates(Utility.parseDateToString(participant
					.getBirthDate(), CommonServiceLocator.getInstance().getDatePattern()), Utility
					.parseDateToString(participant.getDeathDate(), CommonServiceLocator
							.getInstance().getDatePattern()));

			if (!errorKey1)
			{

				throw this.getBizLogicException(null, "participant.invaliddate", "");
			}
		}

		if (!validator.isEmpty(participant.getSocialSecurityNumber()))
		{
			if (!validator.isValidSSN(participant.getSocialSecurityNumber()))
			{
				message = ApplicationProperties.getValue("participant.socialSecurityNumber");
				throw this.getBizLogicException(null, "errors.invalid", message);
			}
		}

		final Collection paticipantMedicicalCollection = participant
				.getParticipantMedicalIdentifierCollection();
		if (paticipantMedicicalCollection != null && !paticipantMedicicalCollection.isEmpty())
		{
			final Iterator itr = paticipantMedicicalCollection.iterator();
			while (itr.hasNext())
			{
				final ParticipantMedicalIdentifier participantIdentifier = (ParticipantMedicalIdentifier) itr
						.next();
				final Site site = participantIdentifier.getSite();
				final String medicalRecordNo = participantIdentifier.getMedicalRecordNumber();
				if (validator.isEmpty(medicalRecordNo) || site == null || site.getId() == null)
				{
					throw this.getBizLogicException(null, "errors.participant.extiden.missing", "");
				}
			}
		}

		final Collection collectionProtocolRegistrationCollection = participant
				.getCollectionProtocolRegistrationCollection();
		if (collectionProtocolRegistrationCollection != null
				&& !collectionProtocolRegistrationCollection.isEmpty())
		{
			final Iterator itrCollectionProtocolRegistration = collectionProtocolRegistrationCollection
					.iterator();
			while (itrCollectionProtocolRegistration.hasNext())
			{
				final CollectionProtocolRegistration collectionProtocolRegistrationIdentifier = (CollectionProtocolRegistration) itrCollectionProtocolRegistration
						.next();
				if (collectionProtocolRegistrationIdentifier.getCollectionProtocol() != null
						&& !collectionProtocolRegistrationIdentifier.equals(""))
				{
					final long collectionProtocolTitle = collectionProtocolRegistrationIdentifier
							.getCollectionProtocol().getId().longValue();
					final String collectionProtocolRegistrationDate = Utility.parseDateToString(
							collectionProtocolRegistrationIdentifier.getRegistrationDate(),
							CommonServiceLocator.getInstance().getDatePattern());
					final String errorKey = validator.validateDate(
							collectionProtocolRegistrationDate, true);
					if (collectionProtocolTitle <= 0 || errorKey.trim().length() > 0)
					{
						throw this.getBizLogicException(null,
								"errors.participant.collectionProtocolRegistration.missing", "");
					}

				}
				//				check the activity status of all the specimens associated to the collection protocol registration
				if (collectionProtocolRegistrationIdentifier.getActivityStatus() != null
						&& collectionProtocolRegistrationIdentifier.getActivityStatus()
								.equalsIgnoreCase(Constants.DISABLED))
				{

					final boolean isSpecimenExist = (boolean) this.isSpecimenExistsForRegistration(
							dao, (Long) collectionProtocolRegistrationIdentifier.getId());
					if (isSpecimenExist)
					{
						throw this.getBizLogicException(null,
								"collectionprotocolregistration.scg.exists", "");
					}

				}

			}
		}

		final boolean isDuplicateCollectionProtocol = this
				.isDuplicateCollectionProtocol(collectionProtocolRegistrationCollection);
		if (isDuplicateCollectionProtocol)
		{
			throw this.getBizLogicException(null,
					"errors.participant.duplicate.collectionProtocol", "");
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
			final List vitalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_VITAL_STATUS, null);
			if (!Validator.isEnumeratedOrNullValue(vitalStatusList, participant.getVitalStatus()))
			{
				throw this.getBizLogicException(null, "participant.gender.errMsg", "");
			}
		}

		if (!validator.isEmpty(participant.getGender()))
		{
			final List genderList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_GENDER, null);

			if (!Validator.isEnumeratedOrNullValue(genderList, participant.getGender()))
			{
				throw this.getBizLogicException(null, "participant.gender.errMsg", "");
			}
		}

		if (!validator.isEmpty(participant.getSexGenotype()))
		{
			final List genotypeList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_GENOTYPE, null);
			if (!Validator.isEnumeratedOrNullValue(genotypeList, participant.getSexGenotype()))
			{
				throw this.getBizLogicException(null, "participant.genotype.errMsg", "");
			}
		}

		final Collection raceCollection = participant.getRaceCollection();
		if (raceCollection != null && !raceCollection.isEmpty())
		{
			final List raceList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_RACE, null);
			final Iterator itr = raceCollection.iterator();
			while (itr.hasNext())
			{
				final Race race = (Race) itr.next();
				if (race != null)
				{
					final String race_name = (String) race.getRaceName();
					if (!validator.isEmpty(race_name)
							&& !Validator.isEnumeratedOrNullValue(raceList, race_name))
					{
						throw this.getBizLogicException(null, "participant.race.errMsg", "");
					}
				}
			}
		}

		if (!validator.isEmpty(participant.getEthnicity()))
		{
			final List ethnicityList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_ETHNICITY, null);
			if (!Validator.isEnumeratedOrNullValue(ethnicityList, participant.getEthnicity()))
			{
				throw this.getBizLogicException(null, "participant.ethnicity.errMsg", "");
			}
		}

		if (operation.equals(Constants.ADD))
		{
			if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(participant.getActivityStatus()))
			{
				throw this.getBizLogicException(null, "activityStatus.active.errMsg", "");
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, participant
					.getActivityStatus()))
			{
				throw this.getBizLogicException(null, "activityStatus.errMsg", "");
			}
		}

		//check the activity status of all the specimens associated to the participant
		if (participant.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
		{

			final boolean isSpecimenExist = (boolean) this.isSpecimenExists(dao, (Long) participant
					.getId());
			if (isSpecimenExist)
			{
				throw this.getBizLogicException(null, "participant.specimen.exists", "");
			}

		}
		return true;
	}

	/**
	 * @param participant - participant object.
	 * @param lookupLogic - LookupLogic object
	 * @return - List of matched participant.
	 * @throws Exception - throws exception.
	 */
	public List getListOfMatchingParticipants(Participant participant, LookupLogic lookupLogic)
			throws Exception
	{
		final DefaultLookupParameters params = new DefaultLookupParameters();
		params.setObject(participant);
		final List matchingParticipantList = lookupLogic.lookup(params);
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

	/**
	 * @return Map of all participant.
	 * @throws BizLogicException throws BizLogicException
	 */
	public Map<Long, Participant> getAllParticipants() throws BizLogicException
	{
		DAO dao = null;
		final Map<Long, Participant> mapOfParticipants = new HashMap<Long, Participant>();
		try
		{
			dao = this.openDAOSession(null);

			final String participantQueryStr = "from " + Participant.class.getName()
					+ " where activityStatus !='" + Status.ACTIVITY_STATUS_DISABLED.toString()
					+ "'";

			final List<Participant> listOfParticipants = dao.executeQuery(participantQueryStr);
			if (listOfParticipants != null)
			{
				final Iterator<Participant> participantIterator = listOfParticipants.iterator();
				while (participantIterator.hasNext())
				{
					final Participant participant = (Participant) participantIterator.next();
					final Participant cloneParticipant = new Participant(participant);
					final Long participantId = cloneParticipant.getId();
					mapOfParticipants.put(participantId, cloneParticipant);
				}
			}
		}
		catch (final DAOException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return mapOfParticipants;
	}

	
	
	
	/**
	 * This method will return the participant records based on MRN,SSN and last name. 
	 * @return Map of all participant.
	 * @throws BizLogicException throws BizLogicException
	 */
	public Map<String, Participant> getAllParticipants(Participant userParticipant)
			throws BizLogicException
	{
		final Map<String, Participant> mapOfParticipants = new HashMap<String, Participant>();
		List<ParticipantMedicalIdentifier> listOfParticipantsMedId = null;
		List<Participant> listOfParticipants = null;
		Participant participant = null;
		Participant cloneParticipant = null;
		ParticipantMedicalIdentifier participantMedIdObj = null;
		DAO dao = null;
		String participantQueryStr = null;
		String medicalRecordNumber = null;
		String siteId = null;
		try
		{
			dao = this.openDAOSession(null);
			if (userParticipant.getParticipantMedicalIdentifierCollection() != null
					&& userParticipant.getParticipantMedicalIdentifierCollection().size() > 0)
			{
				Iterator<ParticipantMedicalIdentifier> iterator = userParticipant
						.getParticipantMedicalIdentifierCollection().iterator();
				while (iterator.hasNext())
				{
					ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier) iterator
							.next();
					medicalRecordNumber = participantMedicalIdentifier.getMedicalRecordNumber();
					siteId = String.valueOf(participantMedicalIdentifier.getSite().getId());
					if (medicalRecordNumber != null && siteId != null)
					{
						participantQueryStr = "from "
								+ ParticipantMedicalIdentifier.class.getName()
								+ " as participantMedId"
								+ " where participantMedId.medicalRecordNumber ='"
								+ medicalRecordNumber + "'" + " and participantMedId.site.id='"
								+ siteId + "'";
						listOfParticipantsMedId = dao.executeQuery(participantQueryStr);
						if (listOfParticipantsMedId != null && !listOfParticipantsMedId.isEmpty())
						{
							Iterator<ParticipantMedicalIdentifier> participantIterator = listOfParticipantsMedId
									.iterator();
							while (participantIterator.hasNext())
							{
								participantMedIdObj = (ParticipantMedicalIdentifier) participantIterator
										.next();
								participant = participantMedIdObj.getParticipant();
								cloneParticipant = new Participant(participant);
								Long participantId = cloneParticipant.getId();
								mapOfParticipants.put(String.valueOf(participantId),
										cloneParticipant);
							}
						}
					}
				}
			}
			if (userParticipant.getSocialSecurityNumber() != null
					&& userParticipant.getSocialSecurityNumber() != "")
			{
				participantQueryStr = "from " + Participant.class.getName() + " as participant"
						+ " where participant.socialSecurityNumber ='"
						+ userParticipant.getSocialSecurityNumber() + "'";
				listOfParticipants = dao.executeQuery(participantQueryStr);
				populateParticipantMap(mapOfParticipants, listOfParticipants);
			}
			if (userParticipant.getLastName() != null && userParticipant.getLastName() != "")
			{
				participantQueryStr = "from " + Participant.class.getName() + " as participant"
						+ " where participant.lastName  like '" + userParticipant.getLastName()
						+ "%'";
				listOfParticipants = dao.executeQuery(participantQueryStr);
				populateParticipantMap(mapOfParticipants, listOfParticipants);
			}

		}
		catch (final DAOException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return mapOfParticipants;
	}

	/**
	 * This method populate the map with participant objects.
	 * @param mapOfParticipants : Map of participants.
	 * @param listOfParticipants : list of participants.
	 */
	private void populateParticipantMap(Map<String, Participant> mapOfParticipants,
			List<Participant> listOfParticipants)
	{
		Participant participant = null;
		Participant cloneParticipant = null;
		if (listOfParticipants != null)
		{
			Iterator<Participant> participantIterator = listOfParticipants.iterator();
			while (participantIterator.hasNext())
			{
				participant = (Participant) participantIterator.next();
				cloneParticipant = new Participant(participant);
				Long participantId = cloneParticipant.getId();
				mapOfParticipants.put(String.valueOf(participantId), cloneParticipant);
			}
		}
	}
	
	
	
	/**
	 * This function takes identifier as parameter and returns 
	 * corresponding Participant.
	 * @return - Participant object
	 */
	public Participant getParticipantById(Long identifier) throws Exception
	{
		// Initialising instance of IBizLogic
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		final String sourceObjectName = Participant.class.getName();

		// getting all the participants from the database 
		final Object object = bizLogic.retrieve(sourceObjectName, identifier);
		final Participant participant = (Participant) object;
		return participant;

	}

	/**
	 * @param columnList - List 
	 * @param partMRNColName - StringBuffer
	 * @return List column list
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getColumnList(List columnList, StringBuffer partMRNColName)
			throws BizLogicException
	{
		final List displayList = new ArrayList();
		JDBCDAO jdbcDao = null;
		try
		{
			jdbcDao = this.openJDBCSession();
			jdbcDao.openSession(null);
			final String sql = "SELECT  columnData.COLUMN_NAME,displayData.DISPLAY_NAME FROM "
					+ "CATISSUE_INTERFACE_COLUMN_DATA columnData,CATISSUE_TABLE_RELATION relationData,"
					+ "CATISSUE_QUERY_TABLE_DATA tableData,CATISSUE_SEARCH_DISPLAY_DATA displayData "
					+ "where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and "
					+ "relationData.PARENT_TABLE_ID = tableData.TABLE_ID and "
					+ "relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and "
					+ "columnData.IDENTIFIER = displayData.COL_ID and tableData.ALIAS_NAME = 'Participant'";

			this.logger.debug("DATA ELEMENT SQL : " + sql);
			final List list = jdbcDao.executeQuery(sql);
			final Iterator iterator1 = columnList.iterator();

			while (iterator1.hasNext())
			{
				final String colName1 = (String) iterator1.next();
				this.logger.debug("colName1------------------------" + colName1);
				final Iterator iterator2 = list.iterator();
				while (iterator2.hasNext())
				{
					final List rowList = (List) iterator2.next();
					final String colName2 = (String) rowList.get(0);
					this.logger.debug("colName2------------------------" + colName2);
					if (colName1.equals(colName2))
					{
						if (colName1.equals(Constants.PARTICIPANT_MEDICAL_RECORD_NO))
						{
							partMRNColName.append((String) rowList.get(1));
						}
						displayList.add((String) rowList.get(1));
					}
				}
			}
			jdbcDao.closeSession();
		}
		catch (final DAOException exp)
		{
			this.logger.debug(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
		finally
		{
			this.closeJDBCSession(jdbcDao);
		}

		return displayList;
	}

	/**
	 * @return page row to show.
	 */
	public String getPageToShow()
	{
		return new String();
	}

	/**
	 * @return List of matching objects.
	 */
	public List getMatchingObjects()
	{
		return new ArrayList();
	}

	/**
	 * Executes hql Query and returns the list of associated scg id.
	 * @param participant Participant
	 * @return List of SCG 
	 * @throws BizLogicException DAOException.
	 */
	public List getSCGList(Long participantId) throws BizLogicException
	{
		DAO dao = null;
		try
		{
			final String scgHql = "select scg.id, scg.surgicalPathologyNumber, scg.identifiedSurgicalPathologyReport.id "
					+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg, "
					+ " edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr,"
					+ " edu.wustl.catissuecore.domain.Participant as p "
					+ " where p.id = "
					+ participantId
					+ " and p.id = cpr.participant.id "
					+ " and scg.id in elements(cpr.specimenCollectionGroupCollection)";

			dao = this.openDAOSession(null);
			List list = null;

			list = dao.executeQuery(scgHql);
			return list;
		}
		catch (final DAOException exp)
		{
			this.logger.debug(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
	}

	/**
	 * Executes hql Query and returns the results.
	 * @param hql String hql
	 * @param dao - DAO object
	 * @return list of objects
	 * @throws BizLogicException DAOException
	 */
	private List executeHqlQuery(DAO dao, String hql) throws BizLogicException
	{
		try
		{
			final List list = dao.executeQuery(hql);
			return list;
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	/**
	 * @param userId - Long.
	 * @return list of CP for users with registration access
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getCPForUserWithRegistrationAcess(long userId) throws BizLogicException
	{
		final List<NameValueBean> cpList = new ArrayList<NameValueBean>();
		final Set<Long> cpIds = new HashSet<Long>();
		cpList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
		DAO dao = null;
		try
		{
			dao = this.openDAOSession(null);
			final User user = (User) dao.retrieveById(User.class.getName(), userId);
			final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
			final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(user
					.getLoginName());
			final Collection<CollectionProtocol> cpCollection = user
					.getAssignedProtocolCollection();
			if (cpCollection != null && !cpCollection.isEmpty())
			{
				for (final CollectionProtocol cp : cpCollection)
				{
					final StringBuffer sb = new StringBuffer();
					sb.append(CollectionProtocol.class.getName()).append("_").append(cp.getId());
					final boolean hasPrivilege = privilegeCache.hasPrivilege(sb.toString(),
							Variables.privilegeDetailsMap.get(Constants.CP_BASED_VIEW_FILTRATION));

					if (hasPrivilege)
					{
						cpList.add(new NameValueBean(cp.getShortTitle(), cp.getId()));
						cpIds.add(cp.getId());
					}
				}
			}
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final UserBizLogic userBizLogic = (UserBizLogic) factory
					.getBizLogic(Constants.USER_FORM_ID);
			final Set<Long> siteIds = userBizLogic.getRelatedSiteIds(userId);

			if (siteIds != null && !siteIds.isEmpty())
			{
				final SiteBizLogic siteBizLogic = (SiteBizLogic) factory
						.getBizLogic(Constants.SITE_FORM_ID);
				for (final Long siteId : siteIds)
				{
					final String peName = Constants.getCurrentAndFuturePGAndPEName(siteId);
					if (privilegeCache.hasPrivilege(peName, Variables.privilegeDetailsMap
							.get(Constants.CP_BASED_VIEW_FILTRATION)))
					{
						final Collection<CollectionProtocol> cp1Collection = siteBizLogic
								.getRelatedCPs(siteId,dao);

						if (cp1Collection != null && !cp1Collection.isEmpty())
						{
							final List<NameValueBean> list = new ArrayList<NameValueBean>();
							for (final CollectionProtocol cp1 : cp1Collection)
							{
								if (!cpIds.contains(cp1.getId()))
								{
									list.add(new NameValueBean(cp1.getShortTitle(), cp1.getId()));
								}
							}
							cpList.addAll(list);
						}

					}
				}
			}

		}
		catch (final DAOException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		catch (final SMException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw AppUtility.handleSMException(e);
		}
		finally
		{
			this.closeDAOSession(dao);
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
		final String objectId = Constants.ADD_GLOBAL_PARTICIPANT;

		if (domainObject instanceof Participant)
		{
			final Participant participant = (Participant) domainObject;
			final Collection<CollectionProtocolRegistration> cprCollection = participant
					.getCollectionProtocolRegistrationCollection();
			if (cprCollection.isEmpty())
			{
				return objectId;
			}

			else
			{
				final StringBuffer sb = new StringBuffer();
				boolean isNewCPRPresent = false;

				if (cprCollection != null && !cprCollection.isEmpty())
				{
					sb.append(Constants.COLLECTION_PROTOCOL_CLASS_NAME);
					for (final CollectionProtocolRegistration cpr : cprCollection)
					{
						if (cpr.getId() == null)
						{
							if (cpr.getCollectionProtocol() == null)
							{
								return objectId;
							}
							sb.append("_").append(cpr.getCollectionProtocol().getId());
							isNewCPRPresent = true;
						}
					}
				}
				if (isNewCPRPresent)
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
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		boolean isAuthorized = false;

		try
		{
			if (sessionDataBean != null && sessionDataBean.isAdmin())
			{
				return true;
			}

			final String privilegeName = this.getPrivilegeName(domainObject);
			final String protectionElementName = this.getObjectId(dao, domainObject);
			final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
					sessionDataBean.getUserName());

			if (protectionElementName.equals(Constants.ADD_GLOBAL_PARTICIPANT))
			{
				User user = null;
				try
				{
					user = (User) dao.retrieveById(User.class.getName(), sessionDataBean
							.getUserId());
				}
				catch (final DAOException e)
				{
					this.logger.error(e.getMessage(), e);
				}
				final Collection<CollectionProtocol> cpCollection = user
						.getAssignedProtocolCollection();
				if (cpCollection != null && !cpCollection.isEmpty())
				{
					for (final CollectionProtocol cp : cpCollection)
					{
						if (privilegeCache.hasPrivilege(CollectionProtocol.class.getName() + "_"
								+ cp.getId(), privilegeName))
						{
							isAuthorized = true;
							break;
						}
					}
					if (!isAuthorized)
					{
						isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(privilegeName,
								sessionDataBean, null);
					}
				}
				else
				{
					isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(privilegeName,
							sessionDataBean, null);
				}
			}
			else
			{
				final String[] prArray = protectionElementName.split("_");
				final String baseObjectId = prArray[0];
				String objId = "";
				for (int i = 1; i < prArray.length; i++)
				{
					objId = baseObjectId + "_" + prArray[i];
					isAuthorized = privilegeCache.hasPrivilege(objId, privilegeName);
					if (!isAuthorized)
					{
						break;
					}
				}

			}

			if (isAuthorized)
			{
				return isAuthorized;
			}
			else
			// Check for ALL CURRENT & FUTURE CASE
			{
				if (!protectionElementName.equals(Constants.ADD_GLOBAL_PARTICIPANT))
				{
					final String protectionElementNames[] = protectionElementName.split("_");

					final Long cpId = Long.valueOf(protectionElementNames[1]);
					final Set<Long> cpIdSet = new UserBizLogic().getRelatedCPIds(sessionDataBean
							.getUserId(), false);

					if (cpIdSet.contains(cpId))
					{
						//bug 11611 and 11659
						throw AppUtility.getUserNotAuthorizedException(privilegeName,
								protectionElementName, domainObject.getClass().getSimpleName());
					}
					isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(privilegeName,
							sessionDataBean, protectionElementNames[1]);
				}
			}
			if (!isAuthorized)
			{
				//bug 11611 and 11659
				throw AppUtility.getUserNotAuthorizedException(privilegeName,
						protectionElementName, domainObject.getClass().getSimpleName());
			}
		}
		catch (final SMException e1)
		{
			this.logger.debug(e1.getMessage(), e1);
			e1.printStackTrace();
		}
		return isAuthorized;
	}

	@Override
	public boolean isReadDeniedTobeChecked()
	{
		return true;
	}

	@Override
	public String getReadDeniedPrivilegeName()
	{
		return Permissions.REGISTRATION + "," + Permissions.READ_DENIED;
	}

	public boolean hasPrivilegeToView(String objName, Long identifier,
			SessionDataBean sessionDataBean)
	{
		return AppUtility.hasPrivilegeToView(objName, identifier, sessionDataBean, this
				.getReadDeniedPrivilegeName());
	}

	/**
	 * Returns a list of Specimen objects with their IDs set as TiTLi 
	 * needs only instance IDs in order to refresh indexes.
	 * @param scg the SpecimenCollectionGroup instance
	 * @return list of Specimen objects
	 * @throws BizLogicException throws BizLogicException
	 */
	private List<Specimen> getSpecimenCollection(SpecimenCollectionGroup scg)
			throws BizLogicException
	{
		final String hql = " select s.id from edu.wustl.catissuecore.domain.Specimen s"
				+ " where s.specimenCollectionGroup.id=" + scg.getId();

		DAO dao = null;

		final List<Specimen> specimens = new ArrayList<Specimen>();
		try
		{
			dao = this.openDAOSession(null);
			final List specimenIds = dao.executeQuery(hql);
			if (specimenIds != null && (!specimenIds.isEmpty()))
			{
				for (final Iterator it = specimenIds.iterator(); it.hasNext();)
				{
					final Specimen specimen = new Specimen();
					specimen.setId((Long) it.next());
					specimens.add(specimen);
				}
			}
		}
		catch (final Exception e)
		{
			this.logger.error("Error occured while retrieving Specimen List", e);
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return specimens;
	}

	@Override
	public void refreshTitliSearchIndexSingle(String operation, Object obj)
	{
		try
		{
			super.refreshTitliSearchIndexSingle(operation, obj);
			final Participant participant = (Participant) obj;
			final Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection = participant
					.getCollectionProtocolRegistrationCollection();
			if (collectionProtocolRegistrationCollection != null)
			{
				final Iterator<CollectionProtocolRegistration> itcprCollection = collectionProtocolRegistrationCollection
						.iterator();

				while (itcprCollection.hasNext())
				{
					final CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) itcprCollection
							.next();
					if (this.cprIdList.contains(cpr.getId()))
					{
						final Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection = cpr
								.getSpecimenCollectionGroupCollection();

						if (specimenCollectionGroupCollection != null)
						{
							final Iterator<SpecimenCollectionGroup> itscgCollection = specimenCollectionGroupCollection
									.iterator();
							while (itscgCollection.hasNext())
							{
								final SpecimenCollectionGroup scg = (SpecimenCollectionGroup) itscgCollection
										.next();
								super.refreshTitliSearchIndexSingle(operation, scg);
								// Collection<Specimen> specimenCollection = scg.getSpecimenCollection();

								// Fetch the Specimens in the given SCG explicitly
								final Collection<Specimen> specimenCollection = this
										.getSpecimenCollection(scg);
								if (specimenCollection != null)
								{
									final Iterator<Specimen> itspecimenCollection = specimenCollection
											.iterator();
									while (itspecimenCollection.hasNext())
									{
										final Specimen specimen = (Specimen) itspecimenCollection
												.next();
										super.refreshTitliSearchIndexSingle(operation, specimen);
									}
								}

							}
						}
					}
				}
			}
		}
		catch (final BizLogicException exp)
		{
			this.logger.debug(exp.getMessage());
		}
	}
	
       
       
    
    /**
     * 
     * @param participant
     * @param cpid
     * @param sessionDataBean
     * @throws ApplicationException 
     */
    public void registerParticipant(Object object, Long cpid,
            String userName) throws ApplicationException
    {
        Participant participant = (Participant)object;
        try
        {
            String operation=Constants.ADD;
            List resultList = new ArrayList<Long>();
            if (participant.getEmpiId() != null)
            {
                String[] selectColumnName={"id"};
                QueryWhereClause queryWhereClause = new QueryWhereClause(Participant.class.getName());
                queryWhereClause.addCondition(new EqualClause("empiId",participant.getEmpiId()));
                resultList = this.retrieve(Participant.class.getName(),
                        selectColumnName, queryWhereClause);
                if (resultList != null && !resultList.isEmpty())
                {
                    participant.setId(Long.valueOf(resultList.get(0).toString()));
                    queryWhereClause = new QueryWhereClause(CollectionProtocolRegistration.class.getName());
                    queryWhereClause.addCondition(new EqualClause("participant.id",resultList.get(0))).andOpr();
                    queryWhereClause.addCondition(new EqualClause("collectionProtocol.id", cpid));
                    resultList = new ArrayList<Long>();
                    resultList = this.retrieve(CollectionProtocolRegistration.class.getName(),
                            selectColumnName, queryWhereClause);
                    operation = Constants.EDIT;
                }
            }
            
            if (resultList == null || resultList.isEmpty())
            {
                addEditParticipant(participant, cpid, userName,operation);
            }
        }
        catch (DAOException e)
        {
            throw new BizLogicException(e);
        }        
        
    }
    
    /**
     * 
     * @param participant
     * @param cpid
     * @param userName
     * @throws Exception 
     * @throws Exception
     */
    private void addEditParticipant(Participant participant,Long cpid,String userName,String operation) throws BizLogicException,ApplicationException
    {                 
            CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
            cpr.setActivityStatus(Constants.ACTIVITY_STATUS_VALUES[1]);
            CollectionProtocol collectionProtocol = new CollectionProtocol();
            collectionProtocol.setId(cpid);
            cpr.setCollectionProtocol(collectionProtocol);
            cpr.setParticipant(participant);
            Collection<CollectionProtocolRegistration> colProtoRegColn = new HashSet<CollectionProtocolRegistration>();
            colProtoRegColn.add(cpr);
            cpr.setRegistrationDate(new Date());
            participant
                    .setCollectionProtocolRegistrationCollection(colProtoRegColn);
            if(operation.equals(Constants.ADD))
            {                
                insert(participant, AppUtility.getSessionDataBean(userName));
            }
            else                
            {
                updateParticipant(userName, participant);
            }        
    }


  /**
   * 
   * @param bizLogic
   * @param userName
   * @param participant
   * @throws ApplicationException
   */
    private void updateParticipant(String userName,
            Participant participant) throws BizLogicException
    {
        HibernateDAO hibernateDao = null;
        try
        {
            String appName =getAppName();
            hibernateDao = (HibernateDAO) DAOConfigFactory.getInstance()
                    .getDAOFactory(appName).getDAO();
            hibernateDao.openSession(null);
            AbstractDomainObject abstractDomainOld;
            abstractDomainOld = (AbstractDomainObject) hibernateDao
                    .retrieveById(Participant.class.getName(), participant
                            .getId());
            update(participant, abstractDomainOld, AppUtility
                    .getSessionDataBean(userName));
        }
        catch (DAOException e)
        {
            throw new BizLogicException(ErrorKey
                    .getErrorKey("common.errors.item"), e,
                    "Error while opening the session");
        }
        finally
        {
            try
            {
                hibernateDao.closeSession();
            }
            catch (Exception e)
            {
                throw new BizLogicException(ErrorKey
                        .getErrorKey("common.errors.item"), e,
                        "Failed while updating ");
            }
        }
    }
    
}