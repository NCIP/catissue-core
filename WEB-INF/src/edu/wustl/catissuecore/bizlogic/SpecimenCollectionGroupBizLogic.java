/**
 * <p>
 * Title: UserHDAO Class>
 * <p>
 * Description: UserHDAO is used to add user information into the database using
 * Hibernate.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00 Created on Apr 13, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.namegenerator.BarcodeGenerator;
import edu.wustl.catissuecore.namegenerator.BarcodeGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CollectionProtocolEventComparator;
import edu.wustl.catissuecore.util.CollectionProtocolSeqComprator;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.SpecimenComparator;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValueImpl;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.DomainBeanIdentifierComparator;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.locator.CSMGroupLocator;
import edu.wustl.security.manager.SecurityManagerFactory;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class SpecimenCollectionGroupBizLogic extends CatissueDefaultBizLogic
{

	/**
	 * Logger for the class.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(SpecimenCollectionGroupBizLogic.class);

	/**
	 * Saves the user object in the database.
	 * @param dao : dao
	 * @param obj
	 *            The user object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 * @throws
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
			boolean reportLoaderFlag = false;
			if (specimenCollectionGroup.getSpecimenCollectionSite() != null)
			{
				final Object siteObj = specimenCollectionGroup.getSpecimenCollectionSite();
				/*final Object siteObj = dao.retrieveById(Site.class.getName(),
						specimenCollectionGroup.getSpecimenCollectionSite().getId());*/
				if (siteObj != null)
				{
					// check for closed Site
					this.checkStatus(dao, specimenCollectionGroup.getSpecimenCollectionSite(),
							"Site");
					specimenCollectionGroup.setSpecimenCollectionSite((Site) siteObj);
				}
			}
			final String sourceObjectName = CollectionProtocolEvent.class.getName();
			final String[] selectColumnName = {"activityStatus", "collectionProtocol.id",
					"collectionProtocol.activityStatus"};
			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("id", specimenCollectionGroup
					.getCollectionProtocolEvent().getId()));

			final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
			CollectionProtocolEvent cpe = null;
			if (list.size() != 0)
			{
				final Object[] valArr = (Object[]) list.get(0);
				if (valArr != null)
				{
					if (specimenCollectionGroup.getCollectionProtocolEvent() != null)
					{
						cpe = (CollectionProtocolEvent) specimenCollectionGroup
								.getCollectionProtocolEvent();
					}
					else
					{
						cpe = new CollectionProtocolEvent();
					}
					cpe.setId(specimenCollectionGroup.getCollectionProtocolEvent().getId());
					cpe.setActivityStatus((String) valArr[0]);

					final CollectionProtocol collectionProtocol = new CollectionProtocol();
					collectionProtocol.setId((Long) valArr[1]);
					collectionProtocol.setActivityStatus((String) valArr[2]);
					cpe.setCollectionProtocol(collectionProtocol);
				}
			}

			/*final Object collectionProtocolEventObj = dao.retrieveById(
					CollectionProtocolEvent.class.getName(), specimenCollectionGroup
							.getCollectionProtocolEvent().getId());*/
			Collection specimenCollection = null;
			final Long userId = AppUtility.getUserID(dao, sessionDataBean);
			if (Constants.REPORT_LOADER_SCG.equals(specimenCollectionGroup.getBarcode())
					&& specimenCollectionGroup.getName().startsWith(Constants.REPORT_LOADER_SCG))
			{
				reportLoaderFlag = true;
				specimenCollectionGroup.setBarcode(null);
			}
			this.setCollectionProtocolRegistration(dao, specimenCollectionGroup, null);

			if (cpe != null)
			{
				//final CollectionProtocolEvent cpe = (CollectionProtocolEvent) collectionProtocolEventObj;
				// check for closed CollectionProtocol
				this.checkStatus(dao, cpe.getCollectionProtocol(), "Collection Protocol");
				specimenCollectionGroup.setCollectionProtocolEvent(cpe);
				this.generateSCGLabel(specimenCollectionGroup);
				// check added for Bug #8533
				// Patch: 8533_6
				if (specimenCollectionGroup.getIsCPBasedSpecimenEntryChecked())
				{
					specimenCollection = this.getCollectionSpecimen(dao, specimenCollectionGroup,
							cpe, userId);
				}
			}

			final String barcode = specimenCollectionGroup.getName();
			this.generateSCGBarcode(specimenCollectionGroup);
			if ((barcode != specimenCollectionGroup.getName()) && barcode != null)
			{
				specimenCollectionGroup.setBarcode(barcode);
			}
			// This check is added if empty values added by UI tnen shud add
			// default values in parameters
			this.checkSCGEvents(specimenCollectionGroup.getSpecimenEventParametersCollection(),
					sessionDataBean);
			dao.insert(specimenCollectionGroup);
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			auditManager.insertAudit(dao, specimenCollectionGroup);
			if (specimenCollection != null && !reportLoaderFlag)
			{
				new NewSpecimenBizLogic().insertMultiple(specimenCollection, (DAO) dao,
						sessionDataBean);
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
		catch (final ApplicationException exp)
		{
			this.logger.debug(exp.getLogMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
	}

	/**
	 * @param specimenCollectionGroup : specimenCollectionGroup
	 * @param cpe : cpe
	 * @param dao
	 * @param userId : userId
	 * @return Collection
	 */
	private Collection<AbstractDomainObject> getCollectionSpecimen(DAO dao,
			SpecimenCollectionGroup specimenCollectionGroup, CollectionProtocolEvent cpe,
			Long userId)
	{
		Collection<AbstractDomainObject> cloneSpecimenCollection = null;
		try
		{
			final Collection<SpecimenRequirement> reqSpecimenCollection = (Collection) this
					.retrieveAttribute(dao, CollectionProtocolEvent.class, cpe.getId(),
							"elements(specimenRequirementCollection)");
			if (reqSpecimenCollection != null || !reqSpecimenCollection.isEmpty())
			{
				cpe.setSpecimenRequirementCollection(reqSpecimenCollection);
			}
			final List<SpecimenRequirement> reqSpecimenList = new LinkedList<SpecimenRequirement>(
					reqSpecimenCollection);
			CollectionProtocolUtil.getSortedCPEventList(reqSpecimenList);
			if (reqSpecimenList != null && !reqSpecimenList.isEmpty())
			{
				cloneSpecimenCollection = new LinkedHashSet<AbstractDomainObject>();
				final Iterator<SpecimenRequirement> itReqSpecimenCollection = reqSpecimenList
						.iterator();
				while (itReqSpecimenCollection.hasNext())
				{
					final SpecimenRequirement specimenRequirement = itReqSpecimenCollection.next();
					if (Constants.NEW_SPECIMEN.equals(specimenRequirement.getLineage()))
					{
						final Specimen cloneSpecimen = this.getCloneSpecimen(specimenRequirement,
								null, specimenCollectionGroup, userId);
						// kalpana : bug #6224
						/*
						 * if(edu.wustl.catissuecore.util.global.Variables.
						 * isSpecimenLabelGeneratorAvl) { LabelGenerator
						 * specimenLableGenerator = LabelGeneratorFactory
						 * .getInstance
						 * (Constants.SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME);
						 * specimenLableGenerator.setLabel(cloneSpecimen); }
						 */
						cloneSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
						cloneSpecimenCollection.add(cloneSpecimen);
					}
				}
			}
		}
		catch (final Exception ex)
		{
			this.logger.debug(ex.getMessage(), ex);
			ex.printStackTrace();
		}

		return cloneSpecimenCollection;
	}

	/**
	 * @param reqSpecimen : reqSpecimen
	 * @param pSpecimen : pSpecimen
	 * @param specimenCollectionGroup : specimenCollectionGroup
	 * @param userId : userId
	 * @return Specimen
	 */
	private Specimen getCloneSpecimen(SpecimenRequirement reqSpecimen, Specimen pSpecimen,
			SpecimenCollectionGroup specimenCollectionGroup, Long userId)
	{
		Specimen newSpecimen;
		try
		{
			newSpecimen = (Specimen) new SpecimenObjectFactory().getDomainObject(reqSpecimen
					.getClassName(), reqSpecimen);
		}
		catch (final AssignDataException e1)
		{
			this.logger.debug(e1.getMessage(), e1);
			e1.printStackTrace();
			return null;
		}
		newSpecimen.setParentSpecimen(pSpecimen);
		// bug no. 7690
		/*
		 * if (!reqSpecimen.getSpecimenEventCollection().isEmpty() &&
		 * reqSpecimen.getSpecimenEventCollection() != null) {
		 * newSpecimen.setPropogatingSpecimenEventCollection
		 * (reqSpecimen.getSpecimenEventCollection(), userId); }
		 */
		newSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
		newSpecimen.setConsentTierStatusCollectionFromSCG(specimenCollectionGroup);

		/*
		 * Collection childrenSpecimenCollection =
		 * reqSpecimen.getChildSpecimenCollection(); if
		 * (childrenSpecimenCollection != null &&
		 * !childrenSpecimenCollection.isEmpty()) { Collection childrenSpecimen
		 * = new LinkedHashSet(); Iterator<SpecimenRequirement> it =
		 * childrenSpecimenCollection.iterator(); while (it.hasNext()) {
		 * SpecimenRequirement childSpecimen = it.next(); Specimen
		 * newchildSpecimen = getCloneSpecimen(childSpecimen, newSpecimen,
		 * specimenCollectionGroup, userId);
		 * childrenSpecimen.add(newchildSpecimen); }
		 * newSpecimen.setChildSpecimenCollection(childrenSpecimen); }
		 */
		final Collection childrenSpecimenCollection = reqSpecimen.getChildSpecimenCollection();
		final List childrenspecimenList = new LinkedList(childrenSpecimenCollection);
		CollectionProtocolUtil.getSortedCPEventList(childrenspecimenList);

		if (childrenspecimenList != null && !childrenspecimenList.isEmpty())
		{
			final Collection childrenSpecimen = new LinkedHashSet();
			final Iterator<SpecimenRequirement> it = childrenspecimenList.iterator();
			while (it.hasNext())
			{
				final SpecimenRequirement childSpecimen = it.next();
				final Specimen newchildSpecimen = this.getCloneSpecimen(childSpecimen, newSpecimen,
						specimenCollectionGroup, userId);
				childrenSpecimen.add(newchildSpecimen);
			}
			newSpecimen.setChildSpecimenCollection(childrenSpecimen);
		}

		return newSpecimen;
	}

	/**
	 * @param specimenCollectionGroup : specimenCollectionGroup
	 * @throws BizLogicException : BizLogicException
	 */
	private void generateSCGLabel(SpecimenCollectionGroup specimenCollectionGroup)
			throws BizLogicException
	{
		try
		{
			if (Variables.isSpecimenCollGroupLabelGeneratorAvl)
			{
				final LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory
						.getInstance(Constants.SPECIMEN_COLL_GROUP_LABEL_GENERATOR_PROPERTY_NAME);
				specimenCollectionGroupLableGenerator.setLabel(specimenCollectionGroup);
			}
		}
		catch (final NameGeneratorException nameGeneratorException)
		{
			this.logger.debug(nameGeneratorException.getMessage(), nameGeneratorException);
			throw this.getBizLogicException(nameGeneratorException, "name.generator.exp", "");
		}
	}

	/**
	 * Method to generate barcode for the SpecimenCollectionGroup.
	 * @param specimenCollectionGroup
	 *            Object of SpecimenCollectionGroup
	 * @throws BizLogicException
	 *             DAO exception
	 */
	private void generateSCGBarcode(SpecimenCollectionGroup specimenCollectionGroup)
			throws BizLogicException
	{
		try
		{
			if (Variables.isSpecimenCollGroupBarcodeGeneratorAvl)
			{
				final BarcodeGenerator specimenCollectionGroupBarcodeGenerator = BarcodeGeneratorFactory
						.getInstance(Constants.SPECIMEN_COLL_GROUP_BARCODE_GENERATOR_PROPERTY_NAME);
				specimenCollectionGroupBarcodeGenerator.setBarcode(specimenCollectionGroup);
			}
		}
		catch (final NameGeneratorException nameGeneratorException)
		{
			this.logger.debug(nameGeneratorException.getMessage(), nameGeneratorException);
			throw this.getBizLogicException(nameGeneratorException, "name.generator.exp", "");
		}
	}

	/**
	 * This function used to get specimenCollectionGroup object from id and
	 * populate all its associated entities.
	 * @param scgId
	 *            SpecimenCollectionGroup id
	 * @param bean
	 *            SessionDataBean
	 * @param retrieveAssociates
	 *            flag for retrieveById associated entities or not.
	 * @return object of CollectionProtocol
	 * @throws BizLogicException
	 *             If fails to retrieveById any of the required entity.
	 */
	public SpecimenCollectionGroup getSCGFromId(Long scgId, SessionDataBean bean,
			boolean retrieveAssociates, DAO dao) throws BizLogicException
	{
		try
		{
			final Object object = dao.retrieveById(SpecimenCollectionGroup.class.getName(), scgId);

			if (object == null)
			{
				throw this.getBizLogicException(null, "failed.find.scg", scgId.toString());
			}
			final SpecimenCollectionGroup specCollGroup = (SpecimenCollectionGroup) object;
			if (retrieveAssociates)
			{
				this.retreiveAssociates(scgId, specCollGroup);
			}
			return specCollGroup;
		}
		catch (final DAOException exception)
		{
			this.logger.debug(exception.getMessage(), exception);
			throw this.getBizLogicException(exception, exception.getErrorKeyName(), exception
					.getMsgValues());
		}
	}

	/**
	 * @param scgId : scgId
	 * @param specCollGroup : specCollGroup
	 * @throws BizLogicException : BizLogicException
	 */
	private void retreiveAssociates(Long scgId, SpecimenCollectionGroup specCollGroup)
			throws BizLogicException
	{
		final CollectionProtocolRegistration collProtReg = specCollGroup
				.getCollectionProtocolRegistration();

		if (collProtReg == null)
		{
			throw this.getBizLogicException(null, "scg.id.null", scgId.toString());
		}
		final CollectionProtocol collProt = collProtReg.getCollectionProtocol();
		if (collProt == null)
		{
			throw this.getBizLogicException(null, "failed.find.scg", scgId.toString());
		}
		final Collection<Specimen> specimenCollection = specCollGroup.getSpecimenCollection();
		this.retrieveSpecimens(specimenCollection);
	}

	/**
	 * @param specimenCollection : specimenCollection
	 */
	private void retrieveSpecimens(Collection specimenCollection)
	{
		if (specimenCollection == null)
		{
			return;
		}

		final Iterator<Specimen> specIterator = specimenCollection.iterator();
		while (specIterator.hasNext())
		{
			final Specimen specimen = specIterator.next();
			final Collection childSpecimenCollection = specimen.getChildSpecimenCollection();
			this.retrieveSpecimens(childSpecimenCollection);
		}
	}

	/**
	 *
	 * @param obj : obj
	 * @return Set
	 */
	private Set getProtectionObjects(AbstractDomainObject obj)
	{
		final Set protectionObjects = new HashSet();

		final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		protectionObjects.add(specimenCollectionGroup);

		this.logger.debug(protectionObjects.toString());
		return protectionObjects;
	}

	/**
	 * @param obj : obj
	 * @return String[]
	 * @throws BizLogicException : BizLogicException
	 */
	protected String[] getDynamicGroups(AbstractDomainObject obj) throws BizLogicException
	{
		final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		final String[] dynamicGroups = new String[1];
		String name;
		try
		{
			name = CSMGroupLocator.getInstance().getPGName(null, CollectionProtocol.class);
			dynamicGroups[0] = SecurityManagerFactory.getSecurityManager()
					.getProtectionGroupByName(
							specimenCollectionGroup.getCollectionProtocolRegistration(), name);
			this.logger.debug("Dynamic Group name: " + dynamicGroups[0]);
		}
		catch (final SMException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, "sm.err.prot.grp", "");
		}
		catch (final ApplicationException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, "sm.err.csm.loc", "");
		}

		return dynamicGroups;

	}

	/**
	 * Updates the persistent object in the database.
	 * @param dao : dao
	 * @param oldObj : oldObj
	 * @param obj
	 *            The object to be updated.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
			final SpecimenCollectionGroup oldspecimenCollectionGroup = (SpecimenCollectionGroup) oldObj;
			// lazy false change

			final Object object = dao.retrieveById(SpecimenCollectionGroup.class.getName(),
					oldspecimenCollectionGroup.getId());

			SpecimenCollectionGroup persistentSCG = null;
			if (object != null)
			{
				persistentSCG = (SpecimenCollectionGroup) object;
			}

			// Adding default events if they are null from API
			final Collection spEventColl = specimenCollectionGroup
					.getSpecimenEventParametersCollection();

			// Copy the SCG events for propagating changes in specimens.
			final Collection copiedSCGEventsColl = this.copySCGEvents(spEventColl);
			if (spEventColl == null || spEventColl.isEmpty())
			{
				this.setDefaultEvents(specimenCollectionGroup, sessionDataBean);
			}
			else
			{
				// Collection pEvtPrmColl =
				// persistentSCG.getSpecimenEventParametersCollection();
				// Iterator evntIterator = pEvtPrmColl.iterator();
				if (persistentSCG.getSpecimenEventParametersCollection() == null
						|| persistentSCG.getSpecimenEventParametersCollection().isEmpty())
				{
					// Collection newScgEventColl =
					// checkSCGEvents(spEventColl,sessionDataBean);
					persistentSCG.setSpecimenEventParametersCollection(spEventColl);
				}
				final Collection pEvtPrmColl = persistentSCG.getSpecimenEventParametersCollection();
				final Iterator evntIterator = pEvtPrmColl.iterator();
				while (evntIterator.hasNext())
				{
					final SpecimenEventParameters event = (SpecimenEventParameters) evntIterator
							.next();
					final SpecimenEventParameters newEvent = (SpecimenEventParameters) this
							.getCorrespondingObject(spEventColl, event.getClass());
					this.updateEvent(event, newEvent, sessionDataBean);
					// spEventColl.remove(newEvent);
				}
			}
			// Check for different closed site
			final Site oldSite = oldspecimenCollectionGroup.getSpecimenCollectionSite();
			final Site site = specimenCollectionGroup.getSpecimenCollectionSite();
			if (oldSite == null && site != null)
			{
				this.checkStatus(dao, site, "Site");
			}
			else if (!site.getId().equals(oldSite.getId()))
			{
				this.checkStatus(dao, site, "Site");
			}
			// site check complete
			final Long oldEventId = oldspecimenCollectionGroup.getCollectionProtocolEvent().getId();
			final Long eventId = specimenCollectionGroup.getCollectionProtocolEvent().getId();
			if (oldEventId.longValue() != eventId.longValue())
			{
				// -- check for closed CollectionProtocol
				final Object proxyObject = dao.retrieveById(
						CollectionProtocolEvent.class.getName(), specimenCollectionGroup
								.getCollectionProtocolEvent().getId());
				if (proxyObject != null)
				{
					// check for closed CollectionProtocol
					final CollectionProtocolEvent cpe = (CollectionProtocolEvent) proxyObject;
					if (!cpe.getCollectionProtocol().getId().equals(
							oldspecimenCollectionGroup.getCollectionProtocolEvent()
									.getCollectionProtocol().getId()))
					{
						this.checkStatus(dao, cpe.getCollectionProtocol(), "Collection Protocol");
					}

					specimenCollectionGroup
							.setCollectionProtocolEvent((CollectionProtocolEvent) proxyObject);
				}
			}
			// CollectionProtocol check complete.

			// setCollectionProtocolRegistration(dao, specimenCollectionGroup,
			// oldspecimenCollectionGroup);

			// Mandar 22-Jan-07 To disable consents accordingly in SCG and
			// Specimen(s) start

			if (!Constants.WITHDRAW_RESPONSE_NOACTION.equalsIgnoreCase(specimenCollectionGroup
					.getConsentWithdrawalOption()))
			{
				this.verifyAndUpdateConsentWithdrawn(specimenCollectionGroup,
						oldspecimenCollectionGroup, dao, sessionDataBean);
				persistentSCG.setConsentTierStatusCollection(specimenCollectionGroup
						.getConsentTierStatusCollection());

			}
			// Mandar 22-Jan-07 To disable consents accordingly in SCG and
			// Specimen(s) end
			// Mandar 24-Jan-07 To update consents accordingly in SCG and
			// Specimen(s) start
			else if (!specimenCollectionGroup.getApplyChangesTo().equalsIgnoreCase(
					Constants.APPLY_NONE))
			{
				ConsentUtil.updateSpecimenStatusInSCG(specimenCollectionGroup,
						oldspecimenCollectionGroup, dao);
			}
			// Mandar 24-Jan-07 To update consents accordingly in SCG and
			// Specimen(s) end
			// change by pathik
			// specimenCollectionGroup.setCollectionProtocolRegistration(
			// persistentSCG.getCollectionProtocolRegistration());

			Integer offset = specimenCollectionGroup.getOffset();
			if (offset != null)
			{

				if (oldspecimenCollectionGroup.getOffset() != null)
				{
					offset = offset - oldspecimenCollectionGroup.getOffset();
				}
				if (offset != 0)
				{
					// updateOffset(offset, specimenCollectionGroup,
					// sessionDataBean, dao);
					this.getDetailsOfCPRForSCG(specimenCollectionGroup
							.getCollectionProtocolRegistration(), dao);
					final CollectionProtocolRegistrationBizLogic cprBizLogic = new CollectionProtocolRegistrationBizLogic();
					cprBizLogic.checkAndUpdateChildOffset(dao, sessionDataBean,
							oldspecimenCollectionGroup.getCollectionProtocolRegistration(), offset
									.intValue());
					cprBizLogic.updateForOffset(dao, sessionDataBean, specimenCollectionGroup
							.getCollectionProtocolRegistration(), offset.intValue());
				}
			}

			persistentSCG.setSpecimenCollectionSite(site);
			persistentSCG.setOffset(offset);
			persistentSCG.setCollectionStatus(specimenCollectionGroup.getCollectionStatus());
			persistentSCG.setComment(specimenCollectionGroup.getComment());
			persistentSCG.setActivityStatus(specimenCollectionGroup.getActivityStatus());
			persistentSCG.setSurgicalPathologyNumber(specimenCollectionGroup
					.getSurgicalPathologyNumber());
			persistentSCG.setClinicalDiagnosis(specimenCollectionGroup.getClinicalDiagnosis());
			persistentSCG.setClinicalStatus(specimenCollectionGroup.getClinicalStatus());
			persistentSCG.setName(specimenCollectionGroup.getName());
			persistentSCG.setBarcode(specimenCollectionGroup.getBarcode());
			persistentSCG.setConsentTierStatusCollection(specimenCollectionGroup
					.getConsentTierStatusCollection());

			// change by pathik
			if (!specimenCollectionGroup.getCollectionProtocolRegistration().getId().equals(0L)
					&& !persistentSCG.getCollectionProtocolRegistration().getId().equals(
							specimenCollectionGroup.getCollectionProtocolRegistration().getId()))
			{
				persistentSCG.setCollectionProtocolRegistration(specimenCollectionGroup
						.getCollectionProtocolRegistration());
			}

			dao.update(persistentSCG);

			/**
			 * Name : Ashish Gupta Reviewer Name : Sachin Lale Bug ID: 2741
			 * Patch ID: 2741_6 Description: Method to update events in all
			 * specimens related to this scg
			 */
			// Populating Events in all specimens
			if (specimenCollectionGroup.isApplyEventsToSpecimens())
			{
				this.updateEvents(copiedSCGEventsColl, oldspecimenCollectionGroup, dao,
						sessionDataBean);
			}
			// Audit.
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			auditManager.updateAudit(dao, obj, oldObj);

			final SpecimenCollectionGroup oldSpecimenCollectionGroup = (SpecimenCollectionGroup) oldObj;

			// Disable the related specimens to this specimen group
			this.logger.debug("specimenCollectionGroup.getActivityStatus() "
					+ specimenCollectionGroup.getActivityStatus());
			if (specimenCollectionGroup.getActivityStatus().equals(
					Status.ACTIVITY_STATUS_DISABLED.toString()))
			{
				this.logger.debug("specimenCollectionGroup.getActivityStatus() "
						+ specimenCollectionGroup.getActivityStatus());
				final Long specimenCollectionGroupIDArr[] = {specimenCollectionGroup.getId()};

				final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				final NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) factory
						.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
				bizLogic.disableRelatedObjectsForSpecimenCollectionGroup(dao,
						specimenCollectionGroupIDArr);
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final ApplicationException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * @param objectCollection : objectCollection
	 * @param eventClass : eventClass
	 * @return Object
	 */
	private Object getCorrespondingObject(Collection objectCollection, Class eventClass)
	{
		final Iterator iterator = objectCollection.iterator();
		while (iterator.hasNext())
		{
			final AbstractDomainObject abstractDomainObject = (AbstractDomainObject) iterator
					.next();
			if (abstractDomainObject.getClass().hashCode() == eventClass.hashCode())
			{
				return abstractDomainObject;
			}
		}
		return null;
	}

	/**
	 * @param scgEventColl : scgEventColl
	 * @return Collection
	 */
	private Collection copySCGEvents(Collection scgEventColl)
	{
		final Collection<SpecimenEventParameters> newSCGEventColl = new HashSet<SpecimenEventParameters>();

		if (scgEventColl != null && !scgEventColl.isEmpty())
		{
			final Iterator scgEventCollIter = scgEventColl.iterator();
			while (scgEventCollIter.hasNext())
			{
				final Object scgEventCollObj = scgEventCollIter.next();
				if (scgEventCollObj instanceof CollectionEventParameters)
				{
					final CollectionEventParameters collEventParam = (CollectionEventParameters) scgEventCollObj;
					final CollectionEventParameters newCollEventParam = new CollectionEventParameters(
							collEventParam);
					newCollEventParam.setUser(collEventParam.getUser());
					newCollEventParam.setTimestamp(collEventParam.getTimestamp());
					newSCGEventColl.add(newCollEventParam);
				}
				if (scgEventCollObj instanceof ReceivedEventParameters)
				{

					final ReceivedEventParameters recEventParam = (ReceivedEventParameters) scgEventCollObj;
					final ReceivedEventParameters newRecEventParam = new ReceivedEventParameters(
							recEventParam);
					newRecEventParam.setUser(recEventParam.getUser());
					newRecEventParam.setTimestamp(recEventParam.getTimestamp());
					newSCGEventColl.add(newRecEventParam);
				}
			}
		}
		return newSCGEventColl;
	}

	/**
	 * @param scgEventColl : scgEventColl
	 * @param sessionDataBean : sessionDataBean
	 */
	private void checkSCGEvents(Collection scgEventColl, SessionDataBean sessionDataBean)
	{
		if (scgEventColl != null && !scgEventColl.isEmpty())
		{
			final User user = new User();
			user.setId(sessionDataBean.getUserId());
			final Iterator scgEventCollIter = scgEventColl.iterator();
			while (scgEventCollIter.hasNext())
			{
				final Object scgEventCollObj = scgEventCollIter.next();
				if (scgEventCollObj instanceof CollectionEventParameters)
				{
					final CollectionEventParameters collEventParam = (CollectionEventParameters) scgEventCollObj;
					if (collEventParam.getUser() == null)
					{
						collEventParam.setUser(user);
					}
					if (collEventParam.getCollectionProcedure() != null
							&& collEventParam.getCollectionProcedure().equals(""))
					{
						collEventParam.setCollectionProcedure(Constants.NOT_SPECIFIED);
					}
					if (collEventParam.getContainer() != null
							&& collEventParam.getContainer().equals(""))
					{
						collEventParam.setContainer(Constants.NOT_SPECIFIED);
					}
				}
				if (scgEventCollObj instanceof ReceivedEventParameters)
				{
					final ReceivedEventParameters recEventParam = (ReceivedEventParameters) scgEventCollObj;
					if (recEventParam.getUser() == null)
					{
						recEventParam.setUser(user);
					}

					if (recEventParam.getReceivedQuality() != null
							&& recEventParam.getReceivedQuality().equals(""))
					{
						recEventParam.setReceivedQuality(Constants.NOT_SPECIFIED);

					}

				}
			}
		}

	}

	/**
	 * @param event : event
	 * @param newEvent : newEvent
	 * @param sessionDataBean : sessionDataBean
	 */
	private void updateEvent(SpecimenEventParameters event, SpecimenEventParameters newEvent,
			SessionDataBean sessionDataBean)
	{
		final User user = new User();
		user.setId(sessionDataBean.getUserId());
		String collProcedure = null;
		String collContainer = null;
		String recQty = null;
		if (event instanceof CollectionEventParameters)
		{
			final CollectionEventParameters toChangeCollectionEventParameters = (CollectionEventParameters) event;
			final CollectionEventParameters newCollectionEventParameters = (CollectionEventParameters) newEvent;
			collProcedure = newCollectionEventParameters.getCollectionProcedure();
			collContainer = newCollectionEventParameters.getContainer();
			if (newCollectionEventParameters.getUser() != null)
			{
				toChangeCollectionEventParameters.setUser(newCollectionEventParameters.getUser());
			}
			toChangeCollectionEventParameters.setTimestamp(newCollectionEventParameters
					.getTimestamp());

			if (!StringUtils.isBlank(collProcedure) && !collProcedure.equals(Constants.CP_DEFAULT))
			{
				toChangeCollectionEventParameters
						.setCollectionProcedure(newCollectionEventParameters
								.getCollectionProcedure());
			}
			if (newCollectionEventParameters.getComment() != null
					&& !newCollectionEventParameters.getComment().equals(""))
			{
				toChangeCollectionEventParameters.setComment(newCollectionEventParameters
						.getComment());
			}
			if (!StringUtils.isBlank(collContainer) && !collContainer.equals(Constants.CP_DEFAULT))
			{
				toChangeCollectionEventParameters.setContainer(newCollectionEventParameters
						.getContainer());
			}

			if (toChangeCollectionEventParameters.getUser() == null)
			{
				toChangeCollectionEventParameters.setUser(user);
			}
			if (toChangeCollectionEventParameters.getCollectionProcedure().equals(""))
			{
				toChangeCollectionEventParameters
						.setCollectionProcedure((String) DefaultValueManager
								.getDefaultValue(Constants.DEFAULT_COLLECTION_PROCEDURE));
			}
			if (toChangeCollectionEventParameters.getContainer().equals(""))
			{
				toChangeCollectionEventParameters.setContainer((String) DefaultValueManager
						.getDefaultValue(Constants.DEFAULT_CONTAINER));
			}
		}
		else
		{
			final ReceivedEventParameters toChanagereceivedEventParameters = (ReceivedEventParameters) event;
			final ReceivedEventParameters newreceivedEventParameters = (ReceivedEventParameters) newEvent;
			recQty = newreceivedEventParameters.getReceivedQuality();
			if (newreceivedEventParameters.getComment() != null
					&& !newreceivedEventParameters.getComment().equals(""))
			{
				toChanagereceivedEventParameters
						.setComment(newreceivedEventParameters.getComment());
			}
			if (!StringUtils.isBlank(recQty) && !recQty.equals(Constants.CP_DEFAULT))
			{
				toChanagereceivedEventParameters.setReceivedQuality(newreceivedEventParameters
						.getReceivedQuality());
			}
			toChanagereceivedEventParameters
					.setTimestamp(newreceivedEventParameters.getTimestamp());
			if (newreceivedEventParameters.getUser() != null)
			{
				toChanagereceivedEventParameters.setUser(newreceivedEventParameters.getUser());
			}

			if (toChanagereceivedEventParameters.getUser() == null)
			{
				toChanagereceivedEventParameters.setUser(user);
			}
			if (toChanagereceivedEventParameters.getReceivedQuality().equals(""))
			{
				toChanagereceivedEventParameters.setReceivedQuality((String) DefaultValueManager
						.getDefaultValue(Constants.DEFAULT_RECEIVED_QUALITY));
			}
		}
	}

	/**
	 * @param specimenCollectionGroup : specimenCollectionGroup
	 * @param sessionDataBean
	 *            Sets the default events if they are not specified
	 */
	private void setDefaultEvents(SpecimenCollectionGroup specimenCollectionGroup,
			SessionDataBean sessionDataBean)
	{
		final Collection specimenEventColl = new HashSet();
		final User user = new User();
		user.setId(sessionDataBean.getUserId());
		final CollectionEventParameters collectionEventParameters = EventsUtil
				.populateCollectionEventParameters(user);
		collectionEventParameters.setSpecimenCollectionGroup(specimenCollectionGroup);
		specimenEventColl.add(collectionEventParameters);

		final ReceivedEventParameters receivedEventParameters = EventsUtil
				.populateReceivedEventParameters(user);
		receivedEventParameters.setSpecimenCollectionGroup(specimenCollectionGroup);
		specimenEventColl.add(receivedEventParameters);

		specimenCollectionGroup.setSpecimenEventParametersCollection(specimenEventColl);
	}

	/**
	 * @param newEventColl : newEventColl
	 * @param oldspecimenCollectionGroup : oldspecimenCollectionGroup
	 * @param dao : dao
	 * @param sessionDataBean : sessionDataBean
	 * @throws BizLogicException : BizLogicException
	 */
	private void updateEvents(Collection newEventColl,
			SpecimenCollectionGroup oldspecimenCollectionGroup, DAO dao,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		CollectionEventParameters scgCollectionEventParameters = null;
		ReceivedEventParameters scgReceivedEventParameters = null;
		if (newEventColl != null && !newEventColl.isEmpty())
		{
			final Iterator newEventCollIter = newEventColl.iterator();
			while (newEventCollIter.hasNext())
			{
				final Object newEventCollObj = newEventCollIter.next();
				if (newEventCollObj instanceof CollectionEventParameters)
				{
					scgCollectionEventParameters = (CollectionEventParameters) newEventCollObj;
					continue;
				}
				else if (newEventCollObj instanceof ReceivedEventParameters)
				{
					scgReceivedEventParameters = (ReceivedEventParameters) newEventCollObj;
				}
			}
		}
		// populateEventsInSpecimens(oldspecimenCollectionGroup,)
		final Collection specimenColl = oldspecimenCollectionGroup.getSpecimenCollection();
		if (specimenColl != null && !specimenColl.isEmpty())
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final SpecimenEventParametersBizLogic specimenEventParametersBizLogic = (SpecimenEventParametersBizLogic) factory
					.getBizLogic(Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID);
			final Iterator iter = specimenColl.iterator();
			while (iter.hasNext())
			{
				final Specimen specimen = (Specimen) iter.next();
				final Collection eventColl = specimen.getSpecimenEventCollection();
				if (eventColl != null && !eventColl.isEmpty())
				{
					final Iterator eventIter = eventColl.iterator();
					while (eventIter.hasNext())
					{
						final Object eventObj = eventIter.next();
						if (eventObj instanceof CollectionEventParameters)
						{
							final CollectionEventParameters collectionEventParameters = (CollectionEventParameters) eventObj;
							final CollectionEventParameters newcollectionEventParameters = this
									.populateCollectionEventParameters(eventObj,
											scgCollectionEventParameters, collectionEventParameters);
							specimenEventParametersBizLogic.update(newcollectionEventParameters,
									collectionEventParameters, sessionDataBean);
							continue;
						}
						else if (eventObj instanceof ReceivedEventParameters)
						{
							final ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters) eventObj;
							final ReceivedEventParameters newReceivedEventParameters = this
									.populateReceivedEventParameters(receivedEventParameters,
											scgReceivedEventParameters);
							specimenEventParametersBizLogic.update(newReceivedEventParameters,
									receivedEventParameters, sessionDataBean);
						}
					}
				}
			}
		}
	}

	/**
	 * @param eventObj : eventObj
	 * @param scgCollectionEventParameters : scgCollectionEventParameters
	 * @param collectionEventParameters : collectionEventParameters
	 * @return CollectionEventParameters
	 */
	private CollectionEventParameters populateCollectionEventParameters(Object eventObj,
			CollectionEventParameters scgCollectionEventParameters,
			CollectionEventParameters collectionEventParameters)
	{
		final String collProcedure = scgCollectionEventParameters.getCollectionProcedure();
		final String collContainer = scgCollectionEventParameters.getContainer();
		final CollectionEventParameters newcollectionEventParameters = new CollectionEventParameters();
		if (collProcedure != null && !collProcedure.equals("")
				&& !collProcedure.equals(Constants.CP_DEFAULT))
		{
			newcollectionEventParameters.setCollectionProcedure(scgCollectionEventParameters
					.getCollectionProcedure());
		}
		else
		{
			newcollectionEventParameters.setCollectionProcedure(collectionEventParameters
					.getCollectionProcedure());
		}

		if (collContainer != null && !collContainer.equals("")
				&& !collContainer.equals(Constants.CP_DEFAULT))
		{
			newcollectionEventParameters.setContainer(scgCollectionEventParameters.getContainer());
		}
		else
		{
			newcollectionEventParameters.setContainer(collectionEventParameters.getContainer());
		}

		newcollectionEventParameters.setTimestamp(scgCollectionEventParameters.getTimestamp());

		if (scgCollectionEventParameters.getUser() != null
				&& !scgCollectionEventParameters.getUser().getId().equals(""))
		{
			newcollectionEventParameters.setUser(scgCollectionEventParameters.getUser());
		}
		else
		{
			newcollectionEventParameters.setUser(collectionEventParameters.getUser());
		}

		if (scgCollectionEventParameters.getComment() != null
				&& !scgCollectionEventParameters.getComment().equals(""))
		{
			newcollectionEventParameters.setComment(scgCollectionEventParameters.getComment());
		}
		else
		{
			newcollectionEventParameters.setComment(collectionEventParameters.getComment());
		}

		newcollectionEventParameters.setSpecimen(collectionEventParameters.getSpecimen());
		newcollectionEventParameters.setSpecimenCollectionGroup(collectionEventParameters
				.getSpecimenCollectionGroup());
		newcollectionEventParameters.setId(collectionEventParameters.getId());
		return newcollectionEventParameters;
	}

	/**
	 * @param receivedEventParameters : receivedEventParameters
	 * @param scgReceivedEventParameters : scgReceivedEventParameters
	 * @return ReceivedEventParameters
	 */
	private ReceivedEventParameters populateReceivedEventParameters(
			ReceivedEventParameters receivedEventParameters,
			ReceivedEventParameters scgReceivedEventParameters)
	{
		final String recQty = scgReceivedEventParameters.getReceivedQuality();
		final ReceivedEventParameters newReceivedEventParameters = new ReceivedEventParameters();
		if (recQty != null && !recQty.equals("") && !recQty.equals(Constants.CP_DEFAULT))
		{
			newReceivedEventParameters.setReceivedQuality(scgReceivedEventParameters
					.getReceivedQuality());
		}
		else
		{
			newReceivedEventParameters.setReceivedQuality(receivedEventParameters
					.getReceivedQuality());
		}

		newReceivedEventParameters.setTimestamp(scgReceivedEventParameters.getTimestamp());

		if (scgReceivedEventParameters.getUser() != null
				&& scgReceivedEventParameters.getUser().getId() != -1)
		{
			newReceivedEventParameters.setUser(scgReceivedEventParameters.getUser());
		}
		else
		{
			newReceivedEventParameters.setUser(receivedEventParameters.getUser());
		}

		newReceivedEventParameters.setId(receivedEventParameters.getId());
		if (scgReceivedEventParameters.getComment() != null
				&& !scgReceivedEventParameters.getComment().equals(""))
		{
			newReceivedEventParameters.setComment(scgReceivedEventParameters.getComment());
		}
		else
		{
			newReceivedEventParameters.setComment(receivedEventParameters.getComment());
		}

		newReceivedEventParameters.setSpecimen(receivedEventParameters.getSpecimen());
		newReceivedEventParameters.setSpecimenCollectionGroup(receivedEventParameters
				.getSpecimenCollectionGroup());
		return newReceivedEventParameters;
	}

	/**
	 * @param dao : dao
	 * @param specimenCollectionGroup : specimenCollectionGroup
	 * @param oldSpecimenCollectionGroup : oldSpecimenCollectionGroup
	 * @throws BizLogicException : BizLogicException
	 * @throws DAOException : DAOException
	 */
	private void setCollectionProtocolRegistration(DAO dao,
			SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCollectionGroup oldSpecimenCollectionGroup) throws BizLogicException,
			DAOException
	{
		CollectionProtocolRegistration cpr = specimenCollectionGroup
				.getCollectionProtocolRegistration();
		if (cpr == null)
		{
			cpr = oldSpecimenCollectionGroup.getCollectionProtocolRegistration();
		}
		if (cpr == null)
		{
			throw this.getBizLogicException(null, "cpr.nt.null", "");
		}
		Long identifier = null;

		if (cpr.getId() != null && cpr.getId().longValue() > 0)
		{
			identifier = cpr.getId();
		}
		else
		{
			identifier = this.getCPRIDFromParticipant(dao, specimenCollectionGroup,
					oldSpecimenCollectionGroup);
		}
		cpr = (CollectionProtocolRegistration) dao.retrieveById(cpr.getClass().getName(),
				identifier);
		specimenCollectionGroup.setCollectionProtocolRegistration(cpr);
		specimenCollectionGroup.getCollectionProtocolRegistration()
				.getSpecimenCollectionGroupCollection().add(specimenCollectionGroup);
	}

	/**
	 * @param dao : dao
	 * @param specimenCollectionGroup : specimenCollectionGroup
	 * @param oldSpecimenCollectionGroup : oldSpecimenCollectionGroup
	 * @return Long
	 * @throws BizLogicException : BizLogicException
	 */
	private Long getCPRIDFromParticipant(DAO dao, SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCollectionGroup oldSpecimenCollectionGroup) throws BizLogicException
	{
		final List list = this.getCPRIdList(dao, specimenCollectionGroup,
				oldSpecimenCollectionGroup);
		if (!list.isEmpty())
		{
			return ((Long) list.get(0));
		}
		return null;
	}

	/**
	 * @param dao : dao
	 * @param specimenCollectionGroup : specimenCollectionGroup
	 * @param oldSpecimenCollectionGroup : oldSpecimenCollectionGroup
	 * @return Lsit
	 * @throws BizLogicException : BizLogicException
	 */
	private List getCPRIdList(DAO dao, SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCollectionGroup oldSpecimenCollectionGroup) throws BizLogicException
	{
		final String sourceObjectName = CollectionProtocolRegistration.class.getName();
		final String[] selectColumnName = {edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER};
		final String[] whereColumnName = new String[2];
		final Object[] whereColumnValue = new Object[2];
		final CollectionProtocolRegistration cpr = specimenCollectionGroup
				.getCollectionProtocolRegistration();

		whereColumnName[0] = "collectionProtocol."
				+ edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER;
		whereColumnValue[0] = specimenCollectionGroup.getCollectionProtocolRegistration()
				.getCollectionProtocol().getId();
		List list = new ArrayList();
		try
		{
			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause(whereColumnName[0], whereColumnValue[0]))
					.andOpr();

			if (cpr.getParticipant() != null)
			{
				// check for closed Participant
				final Participant participantObject = cpr.getParticipant();

				if (oldSpecimenCollectionGroup != null)
				{
					final Participant participantObjectOld = oldSpecimenCollectionGroup
							.getCollectionProtocolRegistration().getParticipant();
					if (!participantObject.getId().equals(participantObjectOld.getId()))
					{
						this.checkStatus(dao, participantObject, "Participant");
					}
				}
				else
				{
					this.checkStatus(dao, participantObject, "Participant");
				}

				whereColumnName[1] = "participant."
						+ edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER;
				whereColumnValue[1] = specimenCollectionGroup.getCollectionProtocolRegistration()
						.getParticipant().getId();
			}
			else
			{
				whereColumnName[1] = "protocolParticipantIdentifier";
				whereColumnValue[1] = specimenCollectionGroup.getCollectionProtocolRegistration()
						.getProtocolParticipantIdentifier();
				this.logger.debug("Value returned:" + whereColumnValue[1]);
			}

			queryWhereClause.addCondition(new EqualClause(whereColumnName[1], whereColumnValue[1]));
			list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
		}
		catch (final DAOException daoexp)
		{
			this.logger.debug(daoexp.getMessage(), daoexp);
			// ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
			throw new BizLogicException(daoexp.getErrorKey(), daoexp, daoexp.getMsgValues());
		}
		return list;
	}

	/**
	 * @param dao : dao
	 * @param specimenCollectionGroup : specimenCollectionGroup
	 * @param oldSpecimenCollectionGroup : oldSpecimenCollectionGroup
	 * @throws BizLogicException : BizLogicException
	 */
	private void setCollectionProtocolRegistrationOld(DAO dao,
			SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCollectionGroup oldSpecimenCollectionGroup) throws BizLogicException
	{
		final List list = this.getCPRIdList(dao, specimenCollectionGroup,
				oldSpecimenCollectionGroup);
		if (!list.isEmpty())
		{
			// check for closed CollectionProtocolRegistration
			final CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

			/**
			 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of
			 * Api Search, previoulsy it was failing since there was default
			 * class level initialization on domain object. For example in User
			 * object, it was initialized as protected String lastName=""; So we
			 * removed default class level initialization on domain object and
			 * are initializing in method setAllValues() of domain object. But
			 * in case of Api Search, default values will not get set since
			 * setAllValues() method of domainObject will not get called. To
			 * avoid null pointer exception, we are setting the default values
			 * same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setCollectionProtocolRegistrationDefault(collectionProtocolRegistration);
			// End:- Change for API Search

			collectionProtocolRegistration.setId((Long) list.get(0));
			if (oldSpecimenCollectionGroup != null)
			{
				final CollectionProtocolRegistration collectionProtocolRegistrationOld = oldSpecimenCollectionGroup
						.getCollectionProtocolRegistration();

				if (!collectionProtocolRegistration.getId().equals(
						collectionProtocolRegistrationOld.getId()))
				{
					this.checkStatus(dao, collectionProtocolRegistration,
							"Collection Protocol Registration");
				}
			}
			else
			{
				this.checkStatus(dao, collectionProtocolRegistration,
						"Collection Protocol Registration");
			}

			specimenCollectionGroup
					.setCollectionProtocolRegistration(collectionProtocolRegistration);
		}
	}

	// public void disableRelatedObjects(DAO dao, Long collProtRegIDArr[])
	// throws
	// DAOException
	// {
	// List listOfSubElement = super.disableObjects(dao,
	// SpecimenCollectionGroup.class, "collectionProtocolRegistration",
	// "CATISSUE_SPECIMEN_COLL_GROUP", "COLLECTION_PROTOCOL_REG_ID",
	// collProtRegIDArr);
	// if (!listOfSubElement.isEmpty())
	// {
	// NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic)
	//BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
	// bizLogic.disableRelatedObjectsForSpecimenCollectionGroup(dao,
	// Utility.toLongArray(listOfSubElement));
	// }
	// }
	/**
	 * @param dao : dao
	 * @param collProtRegIDArr : collProtRegIDArr[]
	 * @throws BizLogicException : BizLogicException
	 */
	public void disableRelatedObjects(DAO dao, Long collProtRegIDArr[]) throws BizLogicException
	{
		final List listOfSubElement = this.getRelatedObjects(dao, SpecimenCollectionGroup.class,
				"collectionProtocolRegistration", collProtRegIDArr);
		this.disableRelatedObjects(dao, "CATISSUE_ABS_SPECI_COLL_GROUP",
				Constants.SYSTEM_IDENTIFIER_COLUMN_NAME, edu.wustl.common.util.Utility
						.toLongArray(listOfSubElement));
		this.auditDisabledObjects(dao, "CATISSUE_SPECIMEN_COLL_GROUP", listOfSubElement);
		if (!listOfSubElement.isEmpty())
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) factory
					.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			bizLogic.disableRelatedObjectsForSpecimenCollectionGroup(dao,
					edu.wustl.common.util.Utility.toLongArray(listOfSubElement));
		}
	}

	/**
	 * @param dao
	 * @param privilegeName
	 * @param objectIds
	 * @param assignToUser
	 * @param roleId
	 * @param longs
	 * @throws BizLogicException
	 * @throws SMException
	 */
	/*
	 * public void assignPrivilegeToRelatedObjects(DAO dao, String
	 * privilegeName, Long[] objectIds, Long userId, String roleId, boolean
	 * assignToUser, boolean assignOperation) throws SMException, DAOException {
	 * List listOfSubElement = super.getRelatedObjects(dao,
	 * SpecimenCollectionGroup.class, "collectionProtocolRegistration",
	 * objectIds); if (!listOfSubElement.isEmpty()) { super.setPrivilege(dao,
	 * privilegeName, SpecimenCollectionGroup.class,
	 * edu.wustl.common.util.Utility.toLongArray(listOfSubElement), userId,
	 * roleId, assignToUser, assignOperation); NewSpecimenBizLogic bizLogic =
	 * (NewSpecimenBizLogic)
	 * BizLogicFactory.getInstance().getBizLogic(Constants.
	 * NEW_SPECIMEN_FORM_ID);
	 * bizLogic.assignPrivilegeToRelatedObjectsForSCG(dao, privilegeName,
	 * edu.wustl.common.util.Utility.toLongArray(listOfSubElement), userId,
	 * roleId, assignToUser, assignOperation); } }
	 *//**
						* @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String,
						*      Class, Long[], Long, String, boolean)
						*/
	/*
	 * protected void setPrivilege(DAO dao, String privilegeName, Class
	 * objectType, Long[] objectIds, Long userId, String roleId, boolean
	 * assignToUser, boolean assignOperation) throws SMException, DAOException {
	 * super.setPrivilege(dao, privilegeName, objectType, objectIds, userId,
	 * roleId, assignToUser, assignOperation); NewSpecimenBizLogic bizLogic =
	 * (NewSpecimenBizLogic)
	 * BizLogicFactory.getInstance().getBizLogic(Constants.
	 * NEW_SPECIMEN_FORM_ID);
	 * bizLogic.assignPrivilegeToRelatedObjectsForSCG(dao, privilegeName,
	 * objectIds, userId, roleId, assignToUser, assignOperation); }
	 */

	/**
	 * check for the specimen associated with the SCG.
	 * @param scgId : scgId
	 * @param obj : obj
	 * @param dao : dao
	 * @return boolean
	 * @throws BizLogicException : BizLogicException
	 */
	protected boolean isSpecimenExists(Object obj, DAO dao, Long scgId) throws BizLogicException
	{

		final String hql = " select" + " elements(scg.specimenCollection) " + " from "
				+ " edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg , "
				+ " edu.wustl.catissuecore.domain.Specimen as s" + " where scg.id = " + scgId
				+ " and" + " scg.id = s.specimenCollectionGroup.id and " + " s.activityStatus = '"
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
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values.
	 * @param obj : obj
	 * @param dao : dao
	 * @param operation : operation
	 * @throws BizLogicException : BizLogicException
	 * @return boolean
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		try
		{
			final SpecimenCollectionGroup group = (SpecimenCollectionGroup) obj;

			// Added by Ashish

			if (group == null)
			{
				throw this.getBizLogicException(null, "domain.object.null.err.msg",
						"SpecimenCollectionGroup");
			}

			final Validator validator = new Validator();
			String message = "";

			if (group.getCollectionProtocolRegistration() == null)
			{
				message = ApplicationProperties
						.getValue("errors.specimenCollectionGroup.collectionprotocolregistration");
				throw this.getBizLogicException(null, "errors.item.required", message);
			}

			/*if (group.getCollectionProtocolRegistration().getCollectionProtocol() == null
				|| group.getCollectionProtocolRegistration().getCollectionProtocol().
				getId() == null)
			{
				message = ApplicationProperties
						.getValue("errors.specimenCollectionGroup.collectionprotocol");
				throw this.getBizLogicException(null, "errors.invalid", message);
			}

			if ((group.getCollectionProtocolRegistration().getProtocolParticipantIdentifier()
			 	== null && (group.getCollectionProtocolRegistration().getParticipant()
			 	== null || group.getCollectionProtocolRegistration().getParticipant()
			 	.getId() == null)))
			{
				throw this.getBizLogicException(null,
						"errors.collectionprotocolregistration.atleast", message);
			}*/

			if (group.getSpecimenCollectionSite() == null
					|| group.getSpecimenCollectionSite().getId() == null
					|| group.getSpecimenCollectionSite().getId() == 0)
			{
				message = ApplicationProperties.getValue("specimenCollectionGroup.site");
				throw this.getBizLogicException(null, "errors.item.invalid", message);
			}

			// Check what user has selected Participant Name / Participant
			// Number

			// if participant name field is checked.
			//if(group.getCollectionProtocolRegistration().getParticipant().getId
			// ()
			// == -1)
			// {
			// message =
			// ApplicationProperties.getValue(
			// "specimenCollectionGroup.protocoltitle");
			// throw new
			//DAOException(ApplicationProperties.getValue("errors.item.required"
			// ,message));
			// String message =
			// ApplicationProperties.getValue(
			// "specimenCollectionGroup.collectedByParticipant");
			// throw new DAOException("errors.item.selected", new
			// String[]{message});
			// }

			//if(!validator.isValidOption(group.getCollectionProtocolRegistration
			// ().getProtocolParticipantIdentifier()))
			// {
			// String message =
			// ApplicationProperties.getValue(
			// "specimenCollectionGroup.collectedByProtocolParticipantNumber");
			// throw new DAOException("errors.item.selected", new
			// String[]{message});
			// }

			if (validator.isEmpty(group.getName()))
			{
				if ((Constants.ADD.equals(operation) && !edu.wustl.catissuecore.util.global.Variables.isSpecimenCollGroupLabelGeneratorAvl)
						|| Constants.EDIT.equals(operation))
				{
					message = ApplicationProperties.getValue("specimenCollectionGroup.groupName");
					throw this.getBizLogicException(null, "errors.item.required", message);
				}
			}

			// Mandatory Field : Study Calendar event point
			if (group.getCollectionProtocolEvent() == null
					|| group.getCollectionProtocolEvent().getId() == null)
			{
				message = ApplicationProperties
						.getValue("specimenCollectionGroup.studyCalendarEventPoint");
				throw this.getBizLogicException(null, "errors.item.required", message);
			}

			// Mandatory Field : clinical Diagnosis
			if (validator.isEmpty(group.getClinicalDiagnosis()))
			{
				message = ApplicationProperties
						.getValue("specimenCollectionGroup.clinicalDiagnosis");
				throw this.getBizLogicException(null, "errors.item.required", message);
			}

			// Mandatory Field : clinical Status
			if (validator.isEmpty(group.getClinicalStatus()))
			{
				message = ApplicationProperties.getValue("specimenCollectionGroup.clinicalStatus");
				throw this.getBizLogicException(null, "errors.item.required", message);
			}

			// Condition for medical Record Number.

			// TO DO FOR 6756
			final String sourceObjectName = PermissibleValueImpl.class.getName();
			final String[] selectColumnName = {"value"};
			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause
					.addCondition(new EqualClause("cde.publicId", "Clinical_Diagnosis_PID"))
					.andOpr().addCondition(new EqualClause("value", group.getClinicalDiagnosis()));

			Iterator<Object> iterator;
			iterator = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause)
					.iterator();
			if (!iterator.hasNext())
			{
				throw this.getBizLogicException(null, "spg.clinicalDiagnosis.errMsg", "");
			}
			/*
			 * String[] whereColumnName ={"cde.publicId"}; //
			 * "storageContainer."
			 * +edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER
			 * String[] whereColumnCondition = {"=" }; Object[] whereColumnValue
			 * ={"Clinical_Diagnosis_PID"}; String joinCondition = null;
			 */
			/*final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause
					.addCondition(new EqualClause("cde.publicId", "Clinical_Diagnosis_PID"));

			Iterator<Object> iterator;

			iterator = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause)
					.iterator();
			final List clinicalDiagnosisList = new ArrayList();
			while (iterator.hasNext())
			{
				final String clinicaDiagnosisvalue = (String) iterator.next();
				clinicalDiagnosisList.add(new NameValueBean(clinicaDiagnosisvalue,
						clinicaDiagnosisvalue));

			}*/

			// List clinicalDiagnosisList =
			// CDEManager.getCDEManager().getPermissibleValueList
			// (Constants.CDE_NAME_CLINICAL_DIAGNOSIS, null);
			/*if (!Validator.isEnumeratedValue(clinicalDiagnosisList, group.getClinicalDiagnosis()))
			{
				throw this.getBizLogicException(null, "spg.clinicalDiagnosis.errMsg", "");
			}*/

			// NameValueBean undefinedVal = new
			// NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
			final List clinicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_CLINICAL_STATUS, null);
			if (!Validator.isEnumeratedValue(clinicalStatusList, group.getClinicalStatus()))
			{

				throw this.getBizLogicException(null, "collectionProtocol.clinicalStatus.errMsg",
						"");
			}

			if (operation.equals(Constants.ADD))
			{
				if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(group.getActivityStatus()))
				{
					throw this.getBizLogicException(null, "activityStatus.active.errMsg", "");
				}
			}
			else
			{
				if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, group
						.getActivityStatus()))
				{
					throw this.getBizLogicException(null, "activityStatus.errMsg", "");
				}
			}

			// Bug #7808
			if (!Validator.isEnumeratedValue(Constants.SCG_COLLECTION_STATUS_VALUES, group
					.getCollectionStatus()))
			{
				throw this.getBizLogicException(null,
						"specimencollectiongroup.collectionStatus.errMsg", "");
			}

			// check the activity status of all the specimens associated to the
			// Specimen Collection Group
			if (group.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
			{

				final boolean isSpecimenExist = (boolean) this.isSpecimenExists(obj, dao,
						(Long) group.getId());
				if (isSpecimenExist)
				{
					throw this.getBizLogicException(null,
							"specimencollectiongroup.specimen.exists", "");
				}

			}
			/*
			 * Bug ID: 4165 Patch ID: 4165_11 Description: Validation added to
			 * check incorrect events added through API
			 */
			// Events Validation
			if (group.getSpecimenEventParametersCollection() != null
					&& !group.getSpecimenEventParametersCollection().isEmpty())
			{
				final Iterator specimenEventCollectionIterator = group
						.getSpecimenEventParametersCollection().iterator();
				while (specimenEventCollectionIterator.hasNext())
				{
					final Object eventObject = specimenEventCollectionIterator.next();
					try
					{
						EventsUtil.validateEventsObject(eventObject, validator);
					}
					catch (final ApplicationException e)
					{
						this.logger.debug(e.getMessage(), e);
						throw this
								.getBizLogicException(null, e.getErrorKeyName(), e.getMsgValues());
					}
				}
			}
			else
			{
				throw this.getBizLogicException(null, "error.specimenCollectionGroup.noevents", "");
			}

			return true;

		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		/*
		* catch (ApplicationException e){ logger.debug(e.getMessage(), e);
		* throw getBizLogicException(null, e.getErrorKeyName(), ""); }
		*/
	}

	/**
	 * @return String
	 */
	public String getPageToShow()
	{
		return new String();
	}

	/**
	 * @return List
	 */
	public List getMatchingObjects()
	{
		return new ArrayList();
	}

	/**
	 * @return int
	 * @throws BizLogicException : BizLogicException
	 */
	public int getNextGroupNumber() throws BizLogicException
	{
		try
		{
			final String sourceObjectName = "CATISSUE_SPECIMEN_COLL_GROUP";
			final String[] selectColumnName = {"max(IDENTIFIER) as MAX_IDENTIFIER"};
			return AppUtility.getNextUniqueNo(sourceObjectName, selectColumnName);
		}
		catch (final ApplicationException exp)
		{
			this.logger.debug(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
	}

	/**
	 * Patch Id : FutureSCG_8 Description : method to get SCGTree ForCPBasedView
	 */
	/**
	 * Creates tree which consists of SCG nodes and specimen nodes under each
	 * SCG if available. For a CPR if there is any SCG created those are shown
	 * with its details like '<# event day>_<Event pointlabel>
	 * <SCG_recv_date>'. When user clicks on this node ,Edit SCG page
	 * will be shown on right side panel of the screen, where now user can edit
	 * this SCG. But if there are no SCGs present for a CPR , a future(dummy)
	 * SCG is shown in tree as '<# event day>_<Event point label>'. When user
	 * clicks on this node , Add SCG page will be shown on right side panel of
	 * the screen, where now user can actually add new SCG and specimens for
	 * this CPR.
	 * @param cpId
	 *            id of collection protocol
	 * @param participantId
	 *            id of participant
	 * @return Vector tree data structure
	 * @throws BizLogicException
	 *             daoException
	 * @throws ClassNotFoundException
	 *             classNotFoundException
	 */
	Integer offsetForCPOrEvent = null;

	Integer offsetForArmCP = null;

	/**
	 * @param cpId : cpId
	 * @param participantId : participantId
	 * @return String
	 * @throws BizLogicException : BizLogicException
	 * @throws ClassNotFoundException : ClassNotFoundException
	 */
	public String getSCGTreeForCPBasedView(Long cpId, Long participantId) throws BizLogicException,
			ClassNotFoundException
	{

		final long startTime = System.currentTimeMillis();
		this.offsetForCPOrEvent = null;
		this.logger.debug("Start of getSCGTreeForCPBasedView");

		Date regDate = null;
		Integer offset = null;
		final String hql1 = "select cpr.registrationDate,cpr.offset from "
				+ CollectionProtocolRegistration.class.getName()
				+ " as cpr where cpr.collectionProtocol.id = " + cpId.toString()
				+ " and cpr.participant.id = " + participantId.toString();
		final List list = this.executeQuery(hql1);
		if (list != null && list.size() > 0)
		{
			final Object[] obj = (Object[]) list.get(0);
			regDate = (Date) obj[0];
			offset = (Integer) obj[1];

		}
		this.offsetForCPOrEvent = offset;
		// creating XML String rep of SCGs,specimens & child specimens ::Addded
		// by baljeet
		final StringBuffer xmlString = new StringBuffer();

		xmlString.append("<node>");

		this.childCPtree(xmlString, cpId, participantId, regDate, this.offsetForCPOrEvent);
		xmlString.append("</node>");
		final long endTime = System.currentTimeMillis();
		this.logger
				.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB (SpecimenCollectionGroupBizlogic)-  : "
						+ (endTime - startTime));
		return xmlString.toString();

	}

	/**
	 * get all the associated scg and specimens to cp and participant.
	 * @param xmlString : xmlString
	 * @param cpId : cpId
	 * @param participantId : participantId
	 * @param regDate : regDate
	 * @param parentOffset : parentOffset
	 * @throws BizLogicException : BizLogicException
	 * @throws ClassNotFoundException : ClassNotFoundException
	 */
	public void childCPtree(StringBuffer xmlString, Long cpId, Long participantId, Date regDate,
			Integer parentOffset) throws BizLogicException, ClassNotFoundException
	{
		// done
		final Date evtLastDate = this.SCGTreeForCPBasedView(xmlString, cpId, participantId,
				regDate, parentOffset);

		final String hql = "select  cp." + Constants.CHILD_COLLECTION_PROTOCOL_COLLECTION
				+ " from " + CollectionProtocol.class.getName() + " as cp where cp.id= "
				+ cpId.toString();

		final List cpchildList = this.executeQuery(hql);
		final CollectionProtocolSeqComprator comparator = new CollectionProtocolSeqComprator();
		Collections.sort(cpchildList, comparator);

		if (cpchildList.size() != 0)
		{
			for (int count = 0; count < cpchildList.size(); count++)
			{
				final CollectionProtocol colProt = (CollectionProtocol) cpchildList.get(count);

				String dispName = colProt.getShortTitle();

				final Date participantRegDate = this.getPartRegDateForCP(colProt, participantId,
						regDate, evtLastDate, parentOffset);

				String anticipatoryDate = "";
				if (participantRegDate != null)
				{
					// bug no:6526 date format changed to mm-dd-yyyy
					anticipatoryDate = edu.wustl.common.util.Utility
							.parseDateToString(participantRegDate, CommonServiceLocator
									.getInstance().getDatePattern());
					dispName = colProt.getShortTitle() + ":" + anticipatoryDate;

				}
				final CollectionProtocolRegistration cpr = this.chkParticipantRegisteredToCP(
						participantId, colProt.getId());
				String participantRegStatus = "Pending";
				if (cpr != null)
				{
					participantRegStatus = "Registered";
				}

				xmlString.append("<node id= \"" + Constants.SUB_COLLECTION_PROTOCOL + "_"
						+ colProt.getId() + "\" " + "name=\"" + dispName + "\" " + "toolTip=\""
						+ colProt.getTitle() + "\" " + "type=\"" + colProt.getType() + "\" "
						+ "cpType=\"" + colProt.getType() + "\" " + "regDate=\"" + anticipatoryDate
						+ "\" " + "participantRegStatus=\"" + participantRegStatus + "\">");
				if (cpr != null)
				{
					if (cpr.getOffset() != null)
					{
						this.addOffset(cpr.getOffset());
					}
				}
				this.childCPtree(xmlString, colProt.getId(), participantId, participantRegDate,
						this.offsetForCPOrEvent);
				xmlString.append("</node>");
			}
		}
	}

	/**
	 * get CollectionProtocolEvents from the collection of SCG get all the SCG
	 * associated to CPR,get the list of CollectionProtocolEvents from SCG
	 * Created a map having key as id of CollectionProtocolEvent and value as
	 * the list of scg assosiated to it.
	 * @param xmlString : xmlString
	 * @param cpId : cpId
	 * @param participantId : participantId
	 * @param regDate : regDate
	 * @param parentOffset : parentOffset
	 * @return Date
	 * @throws BizLogicException : BizLogicException
	 * @throws ClassNotFoundException : ClassNotFoundException
	 */
	public Date SCGTreeForCPBasedView(StringBuffer xmlString, Long cpId, Long participantId,
			Date regDate, Integer parentOffset) throws BizLogicException, ClassNotFoundException
	{
		/**
		 * replaced get SCGColelction call with HQL.
		 */
		final String hql = "select scg.id,scg.name,scg.activityStatus,scg.collectionStatus,scg.offset,"
				+ "scg.collectionProtocolEvent.id,scg.collectionProtocolEvent.studyCalendarEventPoint,"
				+ "scg.collectionProtocolEvent.collectionPointLabel from "
				+ SpecimenCollectionGroup.class.getName()
				+ " as scg where scg.collectionProtocolRegistration.collectionProtocol.id = "
				+ cpId.toString()
				+ " and scg.collectionProtocolRegistration.participant.id = "
				+ participantId.toString()
				+ " and scg.activityStatus <> '"
				+ Status.ACTIVITY_STATUS_DISABLED.toString()
				+ "' order by scg.collectionProtocolEvent.studyCalendarEventPoint";

		final List list = this.executeQuery(hql);

		/**
		 * Map to store key as event id and value as collectionprotocolevt
		 * object
		 */
		final Map<Long, CollectionProtocolEvent> collectionProtocolEventsIdMap = new HashMap<Long, CollectionProtocolEvent>();
		/**
		 * Iterate and create SCG and CollectionProtocolEvent objects After this
		 * loop a collectionProtocolEventsIdMap will populate with valus like
		 * key = event id and value = CollectionProtocolEvent object with
		 * respective SCGCollection
		 */
		for (int i = 0; i < list.size(); i++)
		{
			final Object[] obj1 = (Object[]) list.get(i);
			final SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
			scg.setId((Long) obj1[0]);
			scg.setName((String) obj1[1]);
			scg.setActivityStatus((String) obj1[2]);
			scg.setCollectionStatus((String) obj1[3]);
			scg.setOffset((Integer) obj1[4]);

			final CollectionProtocolEvent cpe = new CollectionProtocolEvent();
			cpe.setId((Long) obj1[5]);
			cpe.setStudyCalendarEventPoint((Double) obj1[6]);
			cpe.setCollectionPointLabel((String) obj1[7]);

			final CollectionProtocolEvent tempCpe = collectionProtocolEventsIdMap.get(cpe.getId());

			if (tempCpe == null)
			{
				final Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection = new HashSet<SpecimenCollectionGroup>();
				specimenCollectionGroupCollection.add(scg);
				cpe.setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);
				collectionProtocolEventsIdMap.put(cpe.getId(), cpe);

			}
			else
			{
				tempCpe.getSpecimenCollectionGroupCollection().add(scg);
			}
			scg.setCollectionProtocolEvent(cpe);
		}

		/**
		 * Get the distinct collectionprotocolevnt object from the Map Sort the
		 * Event objects
		 */

		final List collectionProtocolEvents = new ArrayList(collectionProtocolEventsIdMap.values());
		final CollectionProtocolEventComparator comparator = new CollectionProtocolEventComparator();
		Collections.sort(collectionProtocolEvents, comparator);

		/**
		 * Iterate over CPE obects Get SCGCollection Sort SCG list of the CPE.
		 * Create XML node
		 */
		final Iterator collectionProtocolEventsItr = collectionProtocolEvents.iterator();
		Date eventLastDate = new Date();
		while (collectionProtocolEventsItr.hasNext())
		{
			final CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) collectionProtocolEventsItr
					.next();
			final List scgList = new ArrayList<SpecimenCollectionGroup>(collectionProtocolEvent
					.getSpecimenCollectionGroupCollection());
			final DomainBeanIdentifierComparator idComparator = new DomainBeanIdentifierComparator();
			Collections.sort(scgList, idComparator);
			if (scgList != null && !scgList.isEmpty())
			{
				eventLastDate = this.createTreeNodeForExistngSCG(xmlString, collectionProtocolEvent
						.getStudyCalendarEventPoint(), collectionProtocolEvent
						.getCollectionPointLabel(), scgList, regDate, parentOffset);
			}
		}
		return eventLastDate;
	}

	/**
	 * Get the CollectionProtocolRegistration associated to CP and participant.
	 * @param colProt : colProt
	 * @param participantId : participantId
	 * @return CollectionProtocolRegistration
	 * @throws BizLogicException : BizLogicException
	 * @throws ClassNotFoundException : ClassNotFoundException
	 */
	/*
	 * private CollectionProtocolRegistration
	 * getCollectionProtocolReg(CollectionProtocol colProt, Long participantId)
	 * { CollectionProtocolRegistration collectionProtocolRegistration = null;
	 * Iterator collectionProtocolRegistrationCollItr =
	 * colProt.getCollectionProtocolRegistrationCollection().iterator(); while
	 * (collectionProtocolRegistrationCollItr.hasNext()) {
	 * collectionProtocolRegistration = (CollectionProtocolRegistration)
	 * collectionProtocolRegistrationCollItr.next(); if
	 * (collectionProtocolRegistration
	 * .getParticipant().getId().equals(participantId)) { return
	 * collectionProtocolRegistration; } } return
	 * collectionProtocolRegistration; }
	 */
	/**
	 * get Collection Protocol Reg.
	 */
	private CollectionProtocolRegistration getCollectionProtocolReg(CollectionProtocol colProt,
			Long participantId) throws BizLogicException, ClassNotFoundException
	{
		CollectionProtocolRegistration collectionProtocolRegistration = null;
		final String hql = "select cpr.id,cpr.registrationDate from "
				+ CollectionProtocolRegistration.class.getName()
				+ " as cpr where cpr.collectionProtocol.id=" + colProt.getId()
				+ " and cpr.participant.id=" + participantId.toString();
		final List list = this.executeQuery(hql);
		if (list != null && !list.isEmpty())
		{
			collectionProtocolRegistration = new CollectionProtocolRegistration();
			final Object[] obj = (Object[]) list.get(0);
			collectionProtocolRegistration.setId((Long) obj[0]);
			collectionProtocolRegistration.setRegistrationDate((Date) obj[1]);
		}

		/*
		 * if(colProt.getCollectionProtocolRegistrationCollection() != null &&
		 * !colProt.getCollectionProtocolRegistrationCollection().isEmpty()) {
		 * Iterator collectionProtocolRegistrationCollItr =
		 * colProt.getCollectionProtocolRegistrationCollection().iterator();
		 * while (collectionProtocolRegistrationCollItr.hasNext()) {
		 * collectionProtocolRegistration = (CollectionProtocolRegistration)
		 * collectionProtocolRegistrationCollItr.next(); if
		 * (collectionProtocolRegistration
		 * .getParticipant().getId().equals(participantId)) { return
		 * collectionProtocolRegistration; } } }
		 */
		return collectionProtocolRegistration;
	}

	/**
	 * @param colProt : colProt
	 * @param participantId : participantId
	 * @param parentRegDate : parentRegDate
	 * @param eventLastDate : eventLastDate
	 * @param parentOffset : parentOffset
	 * @return Date
	 * @throws BizLogicException : BizLogicException
	 * @throws ClassNotFoundException : ClassNotFoundException
	 */
	private Date getPartRegDateForCP(CollectionProtocol colProt, Long participantId,
			Date parentRegDate, Date eventLastDate, Integer parentOffset) throws BizLogicException,
			ClassNotFoundException
	{
		Date participantRegDate = null;
		// bug 6560 fix

		final Double eventPoint = colProt.getStudyCalendarEventPoint();
		final CollectionProtocolRegistration cpr = this.getCollectionProtocolReg(colProt,
				participantId);
		if (cpr == null)
		{
			if (colProt.getSequenceNumber() != null)
			{
				int noOfDaysToAdd = 0;
				if (eventPoint != null)
				{
					if (parentRegDate != null)
					{
						noOfDaysToAdd = eventPoint.intValue();
						if (this.offsetForCPOrEvent != null)
						{
							noOfDaysToAdd = noOfDaysToAdd + this.offsetForCPOrEvent.intValue();
						}
						// This check is beaause child cp take there anticipated
						// dates according to there parent cp and if in parent
						// date already offset is captured then no need ot take
						// that offeet in child
						if (parentOffset != null)
						{
							noOfDaysToAdd = noOfDaysToAdd - parentOffset.intValue();
						}
						participantRegDate = AppUtility.getNewDateByAdditionOfDays(parentRegDate,
								noOfDaysToAdd);

					}
					else if (eventLastDate != null)
					{
						participantRegDate = AppUtility.getNewDateByAdditionOfDays(eventLastDate,
								Constants.DAYS_TO_ADD_CP);
					}
					else if (parentRegDate != null)
					{
						participantRegDate = AppUtility.getNewDateByAdditionOfDays(parentRegDate,
								Constants.DAYS_TO_ADD_CP);
					}
				}
			}
		}
		else
		{
			participantRegDate = cpr.getRegistrationDate();
		}

		return participantRegDate;

	}

	/**
	 * @param specimenCollGroup : specimenCollGroup
	 * @return Collection
	 * @throws BizLogicException  : BizLogicException
	 */
	private Collection getCollectionEventParameters(SpecimenCollectionGroup specimenCollGroup)
			throws BizLogicException
	{
		final Collection collectionEventParameters = new ArrayList();
		if (specimenCollGroup.getSpecimenEventParametersCollection() == null
				|| specimenCollGroup.getSpecimenEventParametersCollection().isEmpty())
		{
			final String hql = "select  scg.specimenEventParametersCollection from "
					+ SpecimenCollectionGroup.class.getName() + " as scg where scg.id= "
					+ specimenCollGroup.getId().toString();

			final List scgEventList = this.executeQuery(hql);
			specimenCollGroup.setSpecimenEventParametersCollection(scgEventList);
		}
		if (specimenCollGroup.getSpecimenEventParametersCollection() != null
				&& !specimenCollGroup.getSpecimenEventParametersCollection().isEmpty())
		{

			final Iterator specimenEventParaColItr = specimenCollGroup
					.getSpecimenEventParametersCollection().iterator();
			while (specimenEventParaColItr.hasNext())
			{
				final SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) specimenEventParaColItr
						.next();
				if (specimenEventParameters instanceof CollectionEventParameters)
				{
					collectionEventParameters.add(specimenEventParameters);
				}

			}
		}
		return collectionEventParameters;
	}

	/**
	 * @param xmlString : xmlString
	 * @param eventPoint : eventPoint
	 * @param collectionPointLabel : collectionPointLabel
	 * @param scgList : scgList
	 * @param regDate : regDate
	 * @param parentOffset : parentOffset
	 * @return Date
	 * @throws BizLogicException : BizLogicException
	 * @throws ClassNotFoundException : ClassNotFoundException
	 */
	private Date createTreeNodeForExistngSCG(StringBuffer xmlString, Double eventPoint,
			String collectionPointLabel, List scgList, Date regDate, Integer parentOffset)
			throws BizLogicException, ClassNotFoundException
	{
		Date eventLastDate = null;

		for (int i = 0; i < scgList.size(); i++)
		{
			final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) scgList
					.get(i);

			String scgNodeLabel = "";
			// String scgActivityStatus = (String) obj1[2];

			/**
			 * Name: Vijay Pande Reviewer Name: Aarti Sharma recievedEvent
			 * related to scg is trieved from db and, proper receivedDate and
			 * scgNodeLabel are set to set toolTip of the node
			 */
			if (specimenCollectionGroup.getOffset() != null)
			{
				this.addOffset(specimenCollectionGroup.getOffset());
			}
			String receivedDate = "";
			if (specimenCollectionGroup.getId() != null && specimenCollectionGroup.getId() > 0)
			{

				final Collection eventsColl = this
						.getCollectionEventParameters(specimenCollectionGroup);

				if (eventsColl != null && !eventsColl.isEmpty())
				{
					receivedDate = "";
					final Iterator iter = eventsColl.iterator();
					while (iter.hasNext())
					{
						final CollectionEventParameters collectionEventParameters = (CollectionEventParameters) iter
								.next();
						eventLastDate = collectionEventParameters.getTimestamp();
						// bug no:6526 date format changed to mm-dd-yyyy
						receivedDate = edu.wustl.common.util.Utility.parseDateToString(
								collectionEventParameters.getTimestamp(), CommonServiceLocator
										.getInstance().getDatePattern());
						scgNodeLabel = "T" + eventPoint + ": " + collectionPointLabel + ": "
								+ receivedDate;
						break;
					}
				}
				if (scgNodeLabel.equalsIgnoreCase("") && receivedDate.equalsIgnoreCase(""))
				{
					int noOfDaysToAdd = 0;
					if (eventPoint != null)
					{
						noOfDaysToAdd += eventPoint.intValue();
					}
					if (this.offsetForCPOrEvent != null)
					{
						noOfDaysToAdd += this.offsetForCPOrEvent.intValue();
					}
					// This check is beaause event take there anticipated dates
					// according to there parent and if in parent date already
					// offset is captured then no need ot take that offeet in
					// child
					if (parentOffset != null)
					{
						noOfDaysToAdd -= parentOffset.intValue();
					}

					final Date evtDate = AppUtility.getNewDateByAdditionOfDays(regDate,
							noOfDaysToAdd);
					eventLastDate = evtDate;
					// bug no:6526 date format changed to mm-dd-yyyy
					receivedDate = edu.wustl.common.util.Utility.parseDateToString(evtDate,
							CommonServiceLocator.getInstance().getDatePattern());
					// String toolTipText = scgNodeLabel+ ": "+scgName;//
					scgNodeLabel = "T" + eventPoint + ": " + collectionPointLabel + ": "
							+ receivedDate;
				}
			}
			final String toolTipText = getToolTipText(eventPoint.toString(), collectionPointLabel,
					receivedDate);

			// creating SCG node
			xmlString.append("<node id= \"" + Constants.SPECIMEN_COLLECTION_GROUP + "_"
					+ specimenCollectionGroup.getId().toString() + "\" " + "name=\"" + scgNodeLabel
					+ "\" " + "toolTip=\"" + toolTipText + "\" " + "type=\""
					+ Constants.SPECIMEN_COLLECTION_GROUP + "\" " + "scgCollectionStatus=\""
					+ specimenCollectionGroup.getCollectionStatus() + "\" " + "evtDate=\""
					+ receivedDate + "\">");

			// Adding specimen Nodes to SCG tree
			this.addSpecimenNodesToSCGTree(xmlString, specimenCollectionGroup);
			xmlString.append("</node>");

		}
		return eventLastDate;
	}

	/**
	 * 1. Get all specimens attributes of given scg id 2. Iterate over a
	 * specimen attribute 2.1 create specimen object 2.3 Add specimen in the
	 * TreeMap where key is parentId and value is List of SpeicmenObjects 3. Get
	 * List of Speicmen Object from TreeMap where parentID =0 i.e. speicmen
	 * having no parent. 4. Iterate over above list 4.1 Call createXML of
	 * specimen 4.1.1 Generate xml of specimen 4.1.2 Get childSpeicmen from
	 * TreeMap 4.1.3 recursively call createXML on childSpecimens
	 * @param xmlString : xmlString
	 * @param specimenCollectionGroup : specimenCollectionGroup
	 * @throws BizLogicException : BizLogicException
	 * @throws ClassNotFoundException : ClassNotFoundException
	 */
	private void addSpecimenNodesToSCGTree(StringBuffer xmlString,
			SpecimenCollectionGroup specimenCollectionGroup) throws BizLogicException,
			ClassNotFoundException
	{
		/**
		 * TreeMap to store key as parentSpecimeniD and Child as List of child
		 * specimens
		 */
		final TreeMap<Long, List<Specimen>> specimenChildrenMap = new TreeMap<Long, List<Specimen>>();

		final String hql = "select sp.id,sp.label,sp.parentSpecimen.id,sp.activityStatus,sp.specimenType,sp.collectionStatus	from "
				+ Specimen.class.getName()
				+ " as sp where sp.specimenCollectionGroup.id = "
				+ specimenCollectionGroup.getId()
				+ " and sp.activityStatus <> '"
				+ Status.ACTIVITY_STATUS_DISABLED.toString() + "' order by sp.id";

		final List list = this.executeQuery(hql);

		/**
		 * Iterate over a resultlist the output after this iteration is a
		 * TreeMap populated with key = parentSpecimenID and value = List of
		 * SpecimenObject which is child of Parentkey IF specimen has no parent
		 * then parent key is store as Long(0)
		 */
		for (int i = 0; i < list.size(); i++)
		{
			final Object[] obj = (Object[]) list.get(i);
			final Specimen specimen = new Specimen();
			specimen.setId((Long) obj[0]);
			specimen.setLabel((String) obj[1]);
			specimen.setActivityStatus((String) obj[3]);
			specimen.setSpecimenType((String) obj[4]);
			specimen.setCollectionStatus((String) obj[5]);
			Long parentSpecimenId = (Long) obj[2];

			if (parentSpecimenId == null)
			{
				parentSpecimenId = new Long(0);
			}

			List<Specimen> listOfSpecimens = specimenChildrenMap.get(parentSpecimenId);
			if (listOfSpecimens == null)
			{
				listOfSpecimens = new ArrayList<Specimen>();
				listOfSpecimens.add(specimen);
				specimenChildrenMap.put(parentSpecimenId, listOfSpecimens);
			}
			else
			{
				listOfSpecimens.add(specimen);
			}
		}
		/**
		 * Start creating a node of those specimns having no parent
		 */
		if (!specimenChildrenMap.keySet().isEmpty())
		{
			final List<Specimen> specList = specimenChildrenMap.get(Long.valueOf(0));
			final SpecimenComparator comparator = new SpecimenComparator();
			Collections.sort(specList, comparator);
			for (final Specimen spec : specList)
			{
				this.createSpecimenXML(xmlString, spec, specimenChildrenMap);
			}
		}

	}

	/**
	 * @param xmlString : xmlString
	 * @param specimen : specimen
	 * @param specimenChildrenMap : specimenChildrenMap
	 * @throws BizLogicException : BizLogicException
	 * @throws ClassNotFoundException : ClassNotFoundException
	 */
	private void createSpecimenXML(StringBuffer xmlString, Specimen specimen,
			TreeMap<Long, List<Specimen>> specimenChildrenMap) throws BizLogicException,
			ClassNotFoundException
	{
		final Long spId = specimen.getId();
		String spLabel1 = specimen.getLabel();
		// String spActivityStatus = (String) specimens[3];
		final String type = specimen.getSpecimenType();
		final String spCollectionStatus = specimen.getCollectionStatus();

		if (spLabel1 == null)
		{
			spLabel1 = type;
		}
		// Added later for toolTip text for specimens
		String toolTipText = "Label : " + spLabel1 + " ; Type : " + type;
		// List collectionEventPara =
		// (List)getCollectionEventParameters(specimen
		// .getSpecimenEventCollection());
		final String hqlCon = "select colEveParam.container from "
				+ CollectionEventParameters.class.getName()
				+ " as colEveParam where colEveParam.specimen.id = " + spId;
		final List container = this.executeQuery(hqlCon);
		for (int k = 0; k < container.size(); k++)
		{
			// CollectionEventParameters collectionEventParameters =
			// (CollectionEventParameters)collectionEventPara.get(k);
			final String con = (String) container.get(k);
			toolTipText += " ; Container : " + con;
		}
		xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" "
				+ "name=\"" + spLabel1 + "\" " + "toolTip=\"" + toolTipText + "\" " + "type=\""
				+ Constants.SPECIMEN + "\" " + "collectionStatus=\"" + spCollectionStatus + "\">");
		// Collection childrenSpecimen = specimen.getChildSpecimenCollection();
		// Get childrens of curretn specimen from TreeMap
		final List<Specimen> childrenSpecimen = (List) specimenChildrenMap.get(spId);
		if (childrenSpecimen != null)
		{
			final SpecimenComparator comparator = new SpecimenComparator();
			Collections.sort(childrenSpecimen, comparator);
			for (final Specimen childSpecimen : childrenSpecimen)
			{
				if (!Status.ACTIVITY_STATUS_DISABLED.toString().equals(
						childSpecimen.getActivityStatus()))
				{
					this.createSpecimenXML(xmlString, childSpecimen, specimenChildrenMap);
				}
			}
		}
		xmlString.append("</node>");
	}

	/**
	 * kalpana Bug #5906 Reviewer : vaishali Update the children specimen count
	 * of all the parent specimens accept the immediate parent.
	 * @param finalList : finalList
	 * @param parentSpecimen : parentSpecimen
	 * @param countOfChildSpecimenMap : countOfChildSpecimenMap
	 * @param countOfChildSpecimen : countOfChildSpecimen
	 */
	private void updateChildSpecimenCount(List finalList, Specimen parentSpecimen,
			Map countOfChildSpecimenMap, Integer countOfChildSpecimen)
	{

		for (int j = 0; j < finalList.size(); j++)
		{
			final Specimen specimen = (Specimen) finalList.get(j);
			final Long specimenId = (Long) specimen.getId();
			Long parentSpecimenId = null;

			if (specimen.getParentSpecimen() != null)
			{
				parentSpecimenId = (Long) specimen.getParentSpecimen().getId();

				if (specimenId != null && parentSpecimen != null
						&& specimenId.equals(parentSpecimen.getId()))
				{
					if (countOfChildSpecimenMap.containsKey(specimenId))
					{
						Integer newChildCount = (Integer) countOfChildSpecimenMap.get(specimenId);
						newChildCount = newChildCount + 1;
						countOfChildSpecimenMap.put(specimenId, newChildCount);

						this
								.updateChildSpecimenCount(finalList, (Specimen) specimen
										.getParentSpecimen(), countOfChildSpecimenMap,
										countOfChildSpecimen);
						return;
					}
				}
			}
		}
	}

	/**
	 * @param offsetToAdd : offsetToAdd
	 */
	private void addOffset(Integer offsetToAdd)
	{
		Integer offset = null;
		if (this.offsetForCPOrEvent != null)
		{
			offset = new Integer(this.offsetForCPOrEvent.intValue() + offsetToAdd.intValue());
			this.offsetForCPOrEvent = offset;
		}
		else
		{
			offset = new Integer(offsetToAdd.intValue());
			this.offsetForCPOrEvent = offset;
		}
	}

	/**
	 * @param offset : offset
	 * @param offsetForCPOrEvent : offsetForCPOrEvent
	 * @return int
	 */
	private int getNoOfDaysAccordingToOffset(Integer offset, Integer offsetForCPOrEvent)
	{
		int noOfDays = 0;
		if (offsetForCPOrEvent != null && offset != null
				&& offsetForCPOrEvent.intValue() > offset.intValue())
		{
			noOfDays = offsetForCPOrEvent.intValue() - offset.intValue();
		}
		if (offsetForCPOrEvent != null && offset == null)
		{
			noOfDays = offsetForCPOrEvent.intValue();
		}
		return noOfDays;
	}

	/**
	 * @param dao : dao
	 * @param hql : hql
	 * @return List
	 * @throws BizLogicException : BizLogicException
	 */
	private List executeHqlQuery(DAO dao, String hql) throws BizLogicException
	{
		final List list = this.executeQuery(hql);
		return list;
	}

	/**
	 * This function sets the data in QuertTreeNodeData object adds in a list of
	 * these nodes.
	 * @param identifier
	 * @param objectName
	 * @param displayName
	 * @param parentIdentifier
	 * @param parentObjectName
	 * @param combinedParentIdentifier
	 * @param combinedParentObjectName
	 * @param activityStatus
	 * @param vector
	 */

	// Commented out as it is not required after Flex related chages
	/*
	 * private void setQueryTreeNode(String identifier, String objectName,
	 * String displayName, String parentIdentifier, String parentObjectName,
	 * String combinedParentIdentifier, String combinedParentObjectName, String
	 * activityStatus, String toolTipText, Vector vector) { if
	 * (!activityStatus.equals(Constants.ACTIVITY_STATUS_DISABLED)) {
	 * QueryTreeNodeData treeNode = new QueryTreeNodeData();
	 * treeNode.setIdentifier(identifier); treeNode.setObjectName(objectName);
	 * treeNode.setDisplayName(displayName);
	 * treeNode.setParentIdentifier(parentIdentifier);
	 * treeNode.setParentObjectName(parentObjectName);
	 * treeNode.setCombinedParentIdentifier(combinedParentIdentifier);
	 * treeNode.setCombinedParentObjectName(combinedParentObjectName);
	 * treeNode.setToolTipText(toolTipText); vector.add(treeNode); } }
	 */
	/**
	 * Patch Id : FutureSCG_14 Description : method to getToolTipText
	 */
	/**
	 * Generates tooltip for SCGs in c based views.
	 * @param eventDays
	 *            no of days
	 * @param collectionPointLabel
	 *            label of CPE
	 * @param receivedDate
	 *            received date for specimens
	 * @return String Tooltip text
	 */
	private static String getToolTipText(String eventDays, String collectionPointLabel,
			String receivedDate)
	{
		final StringBuffer toolTipText = new StringBuffer();
		toolTipText.append("Event point : ");
		toolTipText.append(eventDays);
		toolTipText.append(";  Collection point label : ");
		toolTipText.append(collectionPointLabel);
		if (receivedDate != null)
		{
			toolTipText.append(";  Received date : ");
			toolTipText.append(receivedDate);
		}
		return toolTipText.toString();
	}

	/**
	 *  Mandar : 15-Jan-07 For Consent Tracking Withdrawal -------- start.
	 * This method verifies and updates SCG and child elements for withdrawn.
	 * consents
	 * @param specimenCollectionGroup : specimenCollectionGroup
	 * @param oldspecimenCollectionGroup : oldspecimenCollectionGroup
	 * @param dao : dao
	 * @param sessionDataBean : sessionDataBean
	 * @throws BizLogicException : BizLogicException
	 * @throws ApplicationException : ApplicationException
	 */
	private void verifyAndUpdateConsentWithdrawn(SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCollectionGroup oldspecimenCollectionGroup, DAO dao,
			SessionDataBean sessionDataBean) throws BizLogicException, ApplicationException
	{
		final Collection newConsentTierStatusCollection = specimenCollectionGroup
				.getConsentTierStatusCollection();
		final Iterator itr = newConsentTierStatusCollection.iterator();
		while (itr.hasNext())
		{
			final ConsentTierStatus consentTierStatus = (ConsentTierStatus) itr.next();
			if (consentTierStatus.getStatus().equalsIgnoreCase(Constants.WITHDRAWN))
			{
				final long consentTierID = consentTierStatus.getConsentTier().getId().longValue();
				final String cprWithdrawOption = specimenCollectionGroup
						.getConsentWithdrawalOption();
				ConsentUtil.updateSCG(specimenCollectionGroup, oldspecimenCollectionGroup,
						consentTierID, cprWithdrawOption, dao, sessionDataBean);
				// break;
			}
		}
	}

	/**
	 * @param identifier : id
	 * @return String
	 * @throws BizLogicException : BizLogicException
	 */
	public String retriveSCGNameFromSCGId(String identifier) throws BizLogicException
	{
		String scgName = "";
		final String[] selectColumnName = {"name"};
		final String[] whereColumnName = {"id"};
		final String[] whereColumnCondition = {"="};
		final Object[] whereColumnValue = {Long.parseLong(identifier)};

		DAO dao = null;
		try
		{
			dao = this.openDAOSession(null);

			final QueryWhereClause queryWhereClause = new QueryWhereClause(
					SpecimenCollectionGroup.class.getName());
			queryWhereClause.addCondition(new EqualClause("id", Long.parseLong(identifier)));

			final List scgList = dao.retrieve(SpecimenCollectionGroup.class.getName(),
					selectColumnName, queryWhereClause);
			if (scgList != null && !scgList.isEmpty())
			{

				scgName = (String) scgList.get(0);
			}

		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return scgName;
	}

	/**
	 * @param identifier : id
	 * @return Map
	 * @throws BizLogicException : BizLogicException
	 */
	public Map getSpecimenList(Long identifier) throws BizLogicException
	{
		DAO dao = null;
		try
		{
			dao = this.openDAOSession(null);
			final Object object = dao.retrieveById(SpecimenCollectionGroup.class.getName(),
					identifier);

			if (object != null)
			{
				final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) object;
				final Collection specimenCollection = specimenCollectionGroup
						.getSpecimenCollection();
				return CollectionProtocolUtil.getSpecimensMap(specimenCollection, "E1");
			}
			else
			{
				return null;
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			// dao.commit();
			this.closeDAOSession(dao);
		}
	}

	/*
	 * private void updateOffset(Integer offset, SpecimenCollectionGroup
	 * specimenCollectionGroup, SessionDataBean sessionDataBean, DAO dao) throws
	 * BizLogicException { //CollectionProtocolRegistration cpr =
	 * getCollectionProtocolRegForSCG(specimenCollectionGroup, dao);
	 * //specimenCollectionGroup.setCollectionProtocolRegistration(cpr);
	 * CollectionProtocolEvent event = (CollectionProtocolEvent)
	 * specimenCollectionGroup.getCollectionProtocolEvent();
	 * CollectionProtocolRegistration cpr =
	 * specimenCollectionGroup.getCollectionProtocolRegistration(); if (cpr !=
	 * null) { String sourceObjName = SpecimenCollectionGroup.class.getName();
	 * String whereColName[] = {"collectionProtocolEvent.id",
	 * "collectionProtocolRegistration.id"}; String whereColCond[] = {">", "="};
	 * Object whereColVal[] = {event.getId(), cpr.getId()}; List
	 * specimenCollectionGroupCollection = (List) dao.retrieve(sourceObjName,
	 * null, whereColName, whereColCond, whereColVal,
	 * Constants.AND_JOIN_CONDITION); Collection
	 * specimenCollectionGroupCollection = (Collection)
	 * dao.retrieveAttribute(CollectionProtocolRegistration.class.getName(), cpr
	 * .getId(), Constants.COLUMN_NAME_SCG_COLL); if
	 * (specimenCollectionGroupCollection != null &&
	 * !specimenCollectionGroupCollection.isEmpty()) { Iterator
	 * specimenCollectionGroupIterator =
	 * specimenCollectionGroupCollection.iterator(); while
	 * (specimenCollectionGroupIterator.hasNext()) { SpecimenCollectionGroup scg
	 * = (SpecimenCollectionGroup) specimenCollectionGroupIterator.next();
	 * //if(scg.getId().longValue() >
	 * specimenCollectionGroup.getId().longValue()) //{ if (scg.getOffset() !=
	 * null) scg.setOffset(scg.getOffset() + offset); else
	 * scg.setOffset(offset); dao.update(scg); //} } } } }
	 */
	/**
	 * @param cpr : cpr
	 * @param dao : dao
	 * @throws BizLogicException : BizLogicException
	 * @throws DAOException : DAOException
	 */
	private void getDetailsOfCPRForSCG(CollectionProtocolRegistration cpr, DAO dao)
			throws BizLogicException, DAOException
	{
		if (cpr != null && cpr.getId() != null)
		{
			final String sourceObjName = CollectionProtocolRegistration.class.getName();
			final String[] selectColName = {"participant", "collectionProtocol"};
			final String[] whereColName = {"id"};
			final String[] whereColCond = {"="};
			final Object[] whereColVal = {cpr.getId()};

			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjName);
			queryWhereClause.addCondition(new EqualClause("id", cpr.getId()));

			final List list = dao.retrieve(sourceObjName, selectColName, queryWhereClause);
			if (list != null && !list.isEmpty())
			{
				final Object[] obj = (Object[]) list.get(0);
				final Participant participant = (Participant) obj[0];
				final CollectionProtocol collectionProtocol = (CollectionProtocol) obj[1];
				cpr.setParticipant(participant);
				cpr.setCollectionProtocol(collectionProtocol);
			}
		}
	}

	/**
	 * @param dao : dao
	 * @param scg : scg
	 * @return SpecimenCollectionGroup
	 * @throws BizLogicException : BizLogicException
	 */
	//bug 13776 start
	public SpecimenCollectionGroup retrieveSCG(DAO dao, AbstractSpecimenCollectionGroup scg)
			throws BizLogicException
	{
		try
		{
			SpecimenCollectionGroup absScg = null;
			final String sourceObjectName = SpecimenCollectionGroup.class.getName();
			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			if (scg.getId() != null)
			{
				queryWhereClause.addCondition(new EqualClause("id", scg.getId()));
			}
			else if (scg.getGroupName() != null)
			{
				queryWhereClause.addCondition(new EqualClause("name", scg.getGroupName()));
			}
			final List dataList = this.getSCGFromDB(queryWhereClause, dao);
			absScg = this.initSCG(dataList, dao, scg);
			return absScg;
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * This method is used to retrieve SCG using ID or name. 
	 * @param queryWhereClause - where clause
	 * @param dao - dao
	 * @return List of result
	 * @throws DAOException - DAOException
	 */
	private List getSCGFromDB(QueryWhereClause queryWhereClause, DAO dao) throws DAOException
	{
		final String sourceObjectName = SpecimenCollectionGroup.class.getName();
		final String[] selectColumnName = {"id", "activityStatus",
				"collectionProtocolRegistration.id",
				"collectionProtocolRegistration.collectionProtocol.id",
				"collectionProtocolRegistration.collectionProtocol.activityStatus"};
		final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
		return list;
	}

	/**
	 * This method is used to set attributes to SCG.
	 * @param dataList - dataList
	 * @param dao - dao
	 * @param scg - scg
	 * @return SCG obj
	 * @throws BizLogicException - BizLogicException
	 */
	private SpecimenCollectionGroup initSCG(List dataList, DAO dao,
			AbstractSpecimenCollectionGroup scg) throws BizLogicException
	{
		SpecimenCollectionGroup absScg = null;
		if (dataList.size() != 0)
		{
			final Object[] valArr = (Object[]) dataList.get(0);
			if (valArr != null && valArr.length == 5)
			{
				final Long identifier = (Long) valArr[0];
				final String activityStatus = (String) valArr[1];
				final CollectionProtocolRegistration registration = new CollectionProtocolRegistration();
				registration.setId((Long) valArr[2]);
				final CollectionProtocol collectionProtocol = new CollectionProtocol();
				collectionProtocol.setId((Long) valArr[3]);
				collectionProtocol.setActivityStatus((String) valArr[4]);

				final Collection consentTierStatusCollection = (Collection) this.retrieveAttribute(
						dao, SpecimenCollectionGroup.class, identifier,
						"elements(consentTierStatusCollection)");
				absScg = (SpecimenCollectionGroup) scg;
				absScg.setName(scg.getGroupName());
				absScg.setId(identifier);
				absScg.setActivityStatus(activityStatus);
				if (consentTierStatusCollection != null && !consentTierStatusCollection.isEmpty())
				{
					absScg.setConsentTierStatusCollection(consentTierStatusCollection);
				}
				else
				{
					absScg.setConsentTierStatusCollection(absScg.getConsentTierStatusCollection());
				}
				registration.setCollectionProtocol(collectionProtocol);
				absScg.setCollectionProtocolRegistration(registration);
			}
		}
		else
		{
			absScg = (SpecimenCollectionGroup) scg;
			absScg.setId(scg.getId());
		}
		return absScg;
	}

	//bug 13776 end
	/**
	 * @param participantId : participantId
	 * @param collectionProtocolId : collectionProtocolId
	 * @return CollectionProtocolRegistration
	 * @throws BizLogicException : BizLogicException
	 */
	public CollectionProtocolRegistration chkParticipantRegisteredToCP(Long participantId,
			Long collectionProtocolId) throws BizLogicException
	{
		// Date regDate = null;
		CollectionProtocolRegistration cpr = null;
		final String cprHqlQuery = "select cpr.id,cpr.registrationDate,cpr.offset from "
				+ CollectionProtocolRegistration.class.getName()
				+ " as cpr where cpr.participant.id = " + participantId.toString()
				+ " and cpr.collectionProtocol.id = " + collectionProtocolId.toString();
		final List cprList = this.executeQuery(cprHqlQuery);
		if (cprList != null && !cprList.isEmpty())
		{
			cpr = new CollectionProtocolRegistration();
			final Object[] obj = (Object[]) cprList.get(0);
			final Date regDate = (Date) obj[1];
			cpr.setRegistrationDate(regDate);
			if (obj[2] != null)
			{
				final Integer offset = (Integer) obj[2];
				// offsetForCPOrEvent = offset;
				/*
				 * if (!Constants.ARM_CP_TYPE.equals(type)) offsetForArmCP =
				 * offsetForCPOrEvent;
				 */
				cpr.setOffset(offset);
			}

			return cpr;
		}
		return null;
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check.
	 * (non-Javadoc)
	 * @param dao : dao
	 * @param domainObject : domainObject
	 * @return String
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO,
	 *      java.lang.Object)
	 */
	public String getObjectId(DAO dao, Object domainObject)
	{
		String objectId = "";
		if (domainObject instanceof SpecimenCollectionGroup)
		{
			final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) domainObject;
			final CollectionProtocolRegistration cpr = specimenCollectionGroup
					.getCollectionProtocolRegistration();
			final CollectionProtocol cp = cpr.getCollectionProtocol();
			objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cp.getId();
		}
		return objectId;
	}

	/**
	 * To get PrivilegeName for authorization check from.
	 * 'PermissionMapDetails.xml' (non-Javadoc)
	 * @param domainObject : domainObject
	 * @return String
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeKey(Object domainObject)
	{
		return Constants.ADD_EDIT_SCG;
	}

	/**
	 * (non-Javadoc).
	 * @param dao : dao
	 *  @param domainObject : domainObject
	 *  @param sessionDataBean : sessionDataBean
	 *  @return boolean
	 * @throws BizLogicException : BizLogicException
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.DAO,
	 *      java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			boolean isAuthorized = false;
			String protectionElementName = null;

			if (sessionDataBean != null && sessionDataBean.isAdmin())
			{
				return true;
			}
			// Get the base object id against which authorization will take
			// place
			if (domainObject instanceof List)
			{
				final List list = (List) domainObject;
				for (final Object domainObject2 : list)
				{
					protectionElementName = this.getObjectId(dao, domainObject2);
				}
			}
			else
			{
				protectionElementName = this.getObjectId(dao, domainObject);
			}
			// Get the required privilege name which we would like to check for
			// the logged in user.
			final String privilegeName = this.getPrivilegeName(domainObject);
			isAuthorized = AppUtility.checkPrivilegeOnCP(domainObject, protectionElementName,
					privilegeName, sessionDataBean);

			if (!isAuthorized)
			{
				// bug 11611 and 11659
				throw AppUtility.getUserNotAuthorizedException(privilegeName,
						protectionElementName, domainObject.getClass().getSimpleName());
			}
			return isAuthorized;
		}
		catch (final ApplicationException exp)
		{
			this.logger.debug(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
	}

	/**
	 * @return boolean
	 */
	@Override
	public boolean isReadDeniedTobeChecked()
	{
		return true;
	}

	/**
	 * get Read Denied Privilege Name.
	 * @return String
	 */
	@Override
	public String getReadDeniedPrivilegeName()
	{
		return Permissions.REGISTRATION + "," + Permissions.READ_DENIED;
	}

	/**
	 * @param objName : objName
	 * @param identifier : identifier
	 * @param sessionDataBean : sessionDataBean
	 * @return boolean
	 */
	public boolean hasPrivilegeToView(String objName, Long identifier,
			SessionDataBean sessionDataBean)
	{
		return AppUtility.hasPrivilegeToView(objName, identifier, sessionDataBean, this
				.getReadDeniedPrivilegeName());
	}
}