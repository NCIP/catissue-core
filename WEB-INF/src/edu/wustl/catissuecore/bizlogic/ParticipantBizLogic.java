/**
 * <p>Title: ParticipantHDAO Class>
 * <p>Description:	ParticipantHDAO is
 * used to add Participant's information
 * into the database using Hibernate.</p>
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

			final Metaphone metaPhoneObj = new Metaphone();
			final String lNameMetaPhone = metaPhoneObj.metaphone(participant.getLastName());
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
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
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
	 * This method gets called after insert method
	 * Any logic after insertnig object in
	 * database can be included here.
	 * @param obj The inserted object.
	 * @param dao the dao object
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException throws  BizLogicException
	 * */
	protected void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		super.postInsert(obj, dao, sessionDataBean);
	}

	/**
	 * This method gets called after update method.
	 * Any logic after updating into
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
		final CollectionProtocolRegistrationBizLogic cprBizLogic = new CollectionProtocolRegistrationBizLogic();
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
					collectionProtocolRegistration.getParticipant().getId().longValue();
					protocolParticipantId = collectionProtocolRegistration
							.getProtocolParticipantIdentifier();

					if (protocolParticipantId == null)
					{
						protocolParticipantId = Constants.DOUBLE_QUOTES;
					}
				}
				else
				{
					cprBizLogic.postUpdate(dao, collectionProtocolRegistration, oldCPRegistration,
							sessionDataBean);
				}
			}
		}
		super.postUpdate(dao, currentObj, oldObj, sessionDataBean);
	}

	/**
	 * Returns CollectionProtocolRegistration object if it exist
	 * in collection.
	 * @param collectionProtocolId - Long
	 * @param collectionProtocolRegistrationCollection - Collection of protocol registration.
	 * @return CollectionProtocolRegistration object.
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
				participantMap.put(cloneParticipant.getId(), cloneParticipant);
			}
		}
		catch (final CacheException e)
		{
			this.logger.error("Exception occured while getting instance of cachemanager");
			e.printStackTrace();
		}

	}

	/**
	 * Updates the persistent object in the database.
	 * @param dao - DAO object
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
			final String lnameMetaPhone = null;
			final Metaphone metaPhoneObj = new Metaphone();
			final String lNameMetaPhone = metaPhoneObj.metaphone(participant.getLastName());
			participant.setMetaPhoneCode(lNameMetaPhone);

			dao.update(participant);
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
				 * In Case of Api Search, previoulsy it was failing
				 * since there was default class level initialization
				 * on domain object. For example in User object,
				 * it was initialized as protected String lastName="";
				 * So we removed default class level initialization
				 * on domain object and are initializing in method
				 * setAllValues() of domain object. But in case
				 * of Api Search, default values will not get set
				 * since setAllValues() method of domainObject
				 * will not get called. To avoid null
				 * pointer exception,we are setting
				 * the default values same as we
				 * were setting in setAllValues() method
				 * of domainObject.
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
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
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
	 * @param collectionProtocolRegistrationCollection : collectionProtocolRegistrationCollection.
	 * @param collectinProtocolId : collectinProtocolId.
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
	 * @param dao : DAO object.
	 * @param cprId - Long
	 * @return boolean value
	 * @throws BizLogicException
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
	 * @param dao : DAO object.
	 * Overriding the parent class's method to
	 * validate the enumerated attribute values.
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
					final String raceName = (String) race.getRaceName();
					if (!validator.isEmpty(raceName)
							&& !Validator.isEnumeratedOrNullValue(raceList, raceName))
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
			this.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return mapOfParticipants;
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
	public List getColumnList(List<String> columnList, StringBuffer partMRNColName)
			throws BizLogicException
	{
		final List<String> displayList = new ArrayList<String>();
		JDBCDAO jdbcDao = null;
		try
		{
			jdbcDao = this.openJDBCSession();
			jdbcDao.openSession(null);
			final String sql = "SELECT  columnData.COLUMN_NAME,displayData.DISPLAY_NAME FROM "
					+ "CATISSUE_INTERFACE_COLUMN_DATA columnData,"
					+ "CATISSUE_TABLE_RELATION relationData,"
					+ "CATISSUE_QUERY_TABLE_DATA tableData,"
					+ "CATISSUE_SEARCH_DISPLAY_DATA displayData "
					+ "where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and "
					+ "relationData.PARENT_TABLE_ID = tableData.TABLE_ID and "
					+ "relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and "
					+ "columnData.IDENTIFIER = displayData.COL_ID and"
					+ " tableData.ALIAS_NAME = 'Participant'";

			this.logger.debug("DATA ELEMENT SQL : " + sql);
			final List list = jdbcDao.executeQuery(sql);
			final Iterator<String> iterator1 = columnList.iterator();

			while (iterator1.hasNext())
			{
				final String colName1 = (String) iterator1.next();
				this.logger.debug("colName1------------------------" + colName1);
				final Iterator iterator2 = list.iterator();
				while (iterator2.hasNext())
				{
					final List<String> rowList = (List) iterator2.next();
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
			this.logger.error(exp.getMessage(), exp);
			exp.printStackTrace();
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
			final String scgHql = "select scg.id, scg.surgicalPathologyNumber,"
					+ " scg.identifiedSurgicalPathologyReport.id "
					+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg, "
					+ " edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr,"
					+ " edu.wustl.catissuecore.domain.Participant as p " + " where p.id = "
					+ participantId + " and p.id = cpr.participant.id "
					+ " and scg.id in elements(cpr.specimenCollectionGroupCollection)";

			dao = this.openDAOSession(null);
			List list = null;

			list = dao.executeQuery(scgHql);
			return list;
		}
		catch (final DAOException exp)
		{
			this.logger.error(exp.getMessage(), exp);
			exp.printStackTrace();
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
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace() ;
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
								.getRelatedCPs(siteId, dao);

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
			this.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		catch (final SMException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			throw AppUtility.handleSMException(e);
		}
		finally
		{
			this.closeDAOSession(dao);
		}

		return cpList;
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check.
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
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'.
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
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic
	 * #isAuthorized(edu.wustl.common.dao.DAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
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
					e.printStackTrace() ;
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
			this.logger.error(e1.getMessage(), e1);
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
				for (final Iterator<Long> it = specimenIds.iterator(); it.hasNext();)
				{
					final Specimen specimen = new Specimen();
					specimen.setId((Long) it.next());
					specimens.add(specimen);
				}
			}
		}
		catch (final Exception e)
		{
			this.logger.error("Error occured while retrieving Specimen List"+e.getMessage(), e);
			e.printStackTrace();
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
			this.logger.error(exp.getMessage());
			exp.printStackTrace();
		}
	}

	/**
	 * @param participant :participant.
	 * @param cpid : cpid
	 * @param sessionDataBean :sessionDataBean
	 * @throws ApplicationException : ApplicationException
	 */
	public void registerParticipant(Object object, Long cpid, String userName)
			throws ApplicationException
	{
		final Participant participant = (Participant) object;
		try
		{
			String operation = Constants.ADD;
			List resultList = new ArrayList<Long>();
			if (participant.getEmpiId() != null)
			{
				final String[] selectColumnName = {"id"};
				QueryWhereClause queryWhereClause = new QueryWhereClause(Participant.class
						.getName());
				queryWhereClause.addCondition(new EqualClause("empiId", participant.getEmpiId()));
				resultList = this.retrieve(Participant.class.getName(), selectColumnName,
						queryWhereClause);
				if (resultList != null && !resultList.isEmpty())
				{
					participant.setId(Long.valueOf(resultList.get(0).toString()));
					queryWhereClause = new QueryWhereClause(CollectionProtocolRegistration.class
							.getName());
					queryWhereClause.addCondition(
							new EqualClause("participant.id", resultList.get(0))).andOpr();
					queryWhereClause.addCondition(new EqualClause("collectionProtocol.id", cpid));
					resultList = new ArrayList<Long>();
					resultList = this.retrieve(CollectionProtocolRegistration.class.getName(),
							selectColumnName, queryWhereClause);
					operation = Constants.EDIT;
				}
			}

			if (resultList == null || resultList.isEmpty())
			{
				this.addEditParticipant(participant, cpid, userName, operation);
			}
		}
		catch (final DAOException daoEx)
		{
			this.logger.error(daoEx.getMessage(),daoEx);
			daoEx.printStackTrace();
			throw new BizLogicException(daoEx);
		}

	}

	/**
	 * @param participant : participant.
	 * @param cpid : cpid
	 * @param userName : username.
	 * @param operation :operation
	 * @throws BizLogicException :BizLogicException
	 * @throws ApplicationException : ApplicationException
	 */
	private void addEditParticipant(Participant participant, Long cpid, String userName,
			String operation) throws BizLogicException, ApplicationException
	{
		final CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		cpr.setActivityStatus(Constants.ACTIVITY_STATUS_VALUES[1]);
		final CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setId(cpid);
		cpr.setCollectionProtocol(collectionProtocol);
		cpr.setParticipant(participant);
		final Collection<CollectionProtocolRegistration> colProtoRegColn = new HashSet<CollectionProtocolRegistration>();
		colProtoRegColn.add(cpr);
		cpr.setRegistrationDate(new Date());
		participant.setCollectionProtocolRegistrationCollection(colProtoRegColn);
		if (operation.equals(Constants.ADD))
		{
			this.insert(participant, AppUtility.getSessionDataBean(userName));
		}
		else
		{
			this.updateParticipant(userName, participant);
		}
	}

	/**
	 * @param userName : user name.
	 * @param participant : participant
	 * @throws BizLogicException : BizLogicException
	 */
	private void updateParticipant(String userName, Participant participant)
			throws BizLogicException
	{
		HibernateDAO hibernateDao = null;
		try
		{
			final String appName = this.getAppName();
			hibernateDao = (HibernateDAO) DAOConfigFactory.getInstance().getDAOFactory(appName)
					.getDAO();
			hibernateDao.openSession(null);
			AbstractDomainObject abstractDomainOld;
			abstractDomainOld = (AbstractDomainObject) hibernateDao.retrieveById(Participant.class
					.getName(), participant.getId());
			this.update(participant, abstractDomainOld, AppUtility.getSessionDataBean(userName));
		}
		catch (final DAOException e)
		{
			this.logger.error(e.getMessage(),e) ;
			e.printStackTrace() ;
			throw new BizLogicException(ErrorKey.getErrorKey("common.errors.item"), e,
					"Error while opening the session");
		}
		finally
		{
			try
			{
				hibernateDao.closeSession();
			}
			catch (final Exception e)
			{
				this.logger.error(e.getMessage(),e) ;
				e.printStackTrace();
				throw new BizLogicException(ErrorKey.getErrorKey("common.errors.item"), e,
						"Failed while updating ");
			}
		}
	}

}