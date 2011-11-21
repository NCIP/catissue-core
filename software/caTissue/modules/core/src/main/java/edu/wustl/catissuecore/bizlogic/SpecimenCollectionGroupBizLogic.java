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
import java.util.TreeMap;

import org.hibernate.LazyInitializationException;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.factory.utils.SpecimenUtility;
import edu.wustl.catissuecore.namegenerator.BarcodeGenerator;
import edu.wustl.catissuecore.namegenerator.BarcodeGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.catissuecore.uiobject.SpecimenCollectionGroupUIObject;
import edu.wustl.catissuecore.util.CollectionProtocolEventComparator;
import edu.wustl.catissuecore.util.CollectionProtocolSeqComprator;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.SpecimenComparator;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValueImpl;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.DomainBeanIdentifierComparator;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
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

	InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);

	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		SpecimenCollectionGroupUIObject scgUIObject = new SpecimenCollectionGroupUIObject();
		insert(obj, scgUIObject,dao,  sessionDataBean);
	}

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
	protected void insert(Object obj, Object uiObject, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			SpecimenCollectionGroupUIObject scgUiObject=(SpecimenCollectionGroupUIObject)uiObject;
			final SpecimenCollectionGroup scg = (SpecimenCollectionGroup) obj;
			boolean reportLoaderFlag = false;
			if (scg.getSpecimenCollectionSite() != null)
			{
				final Object siteObj = scg.getSpecimenCollectionSite();
				if (siteObj != null)
				{
					// check for closed Site
					this.checkStatus(dao, scg.getSpecimenCollectionSite(), "Site");
					scg.setSpecimenCollectionSite((Site) siteObj);
				}
			}
			final String sourceObjectName = CollectionProtocolEvent.class.getName();
			final String[] selectColumnName = {"activityStatus", "collectionProtocol.id",
					"collectionProtocol.activityStatus"};
			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("id", scg.getCollectionProtocolEvent()
					.getId()));

			final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
			CollectionProtocolEvent cpe = null;
			if (!list.isEmpty())
			{
				final Object[] valArr = (Object[]) list.get(0);
				if (valArr != null)
				{
					if (scg.getCollectionProtocolEvent() != null)
					{
						cpe = (CollectionProtocolEvent) scg.getCollectionProtocolEvent();
					}
					else
					{
						InstanceFactory<CollectionProtocolEvent> cpeInstFact = DomainInstanceFactory
								.getInstanceFactory(CollectionProtocolEvent.class);
						cpe = cpeInstFact.createObject();//new CollectionProtocolEvent();
					}
					cpe.setId(scg.getCollectionProtocolEvent().getId());
					cpe.setActivityStatus((String) valArr[0]);

					InstanceFactory<CollectionProtocol> cpInstFact = DomainInstanceFactory
							.getInstanceFactory(CollectionProtocol.class);
					final CollectionProtocol collectionProtocol = cpInstFact.createObject();
					collectionProtocol.setId((Long) valArr[1]);
					collectionProtocol.setActivityStatus((String) valArr[2]);
					cpe.setCollectionProtocol(collectionProtocol);
				}
			}

			Collection specimenColl = null;
			final Long userId = AppUtility.getUserID(dao, sessionDataBean);
			if (Constants.REPORT_LOADER_SCG.equals(scg.getBarcode())
					&& scg.getName().startsWith(Constants.REPORT_LOADER_SCG))
			{
				reportLoaderFlag = true;
				scg.setBarcode(null);
			}
			this.setCollectionProtocolRegistration(dao, scg, null);

			if (cpe != null)
			{
				//final CollectionProtocolEvent cpe = (CollectionProtocolEvent) collectionProtocolEventObj;
				// check for closed CollectionProtocol
				this.checkStatus(dao, cpe.getCollectionProtocol(), "Collection Protocol");
				scg.setCollectionProtocolEvent(cpe);
				if(scg.getName()== null)
				{
					this.generateSCGLabel(scg);
				}
				// check added for Bug #8533
				// Patch: 8533_6
				//if (scg.getIsCPBasedSpecimenEntryChecked())
				if (scgUiObject.getIsCPBasedSpecimenEntryChecked())
				{
					specimenColl = this.getCollectionSpecimen(dao, scg, cpe, userId);
				}
			}

			final String barcode = scg.getName();
			this.generateSCGBarcode(scg);
			if ((barcode != scg.getName()) && barcode != null)
			{
				scg.setBarcode(barcode);
			}
			// This check is added if empty values added by UI tnen shud add
			// default values in parameters
			this.checkSCGEvents(scg.getSpecimenEventParametersCollection(), sessionDataBean);
			dao.insert(scg);
			/*if (specimenColl != null && !reportLoaderFlag
					&& scg.getIsToInsertAnticipatorySpecimens())*/
			if (specimenColl != null && !reportLoaderFlag)
			{
				if(scg.getIsToInsertAnticipatorySpecimens()== null)
				{
					scg.setIsToInsertAnticipatorySpecimens(true);
				}
				if(scg.getIsToInsertAnticipatorySpecimens())
				{
					new NewSpecimenBizLogic().insertMultiple(specimenColl, (DAO) dao, sessionDataBean);
				}
			}
			Collection specimenCollection = scg.getSpecimenCollection();
			if(specimenCollection!=null&&!specimenCollection.isEmpty())
			{
				
				new NewSpecimenBizLogic().insertMultiple(specimenCollection, (DAO) dao, sessionDataBean);
			}
		}
		catch (final DAOException daoExp)
		{
			//this.LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final ApplicationException exp)
		{
			//this.LOGGER.error(exp.getLogMessage(), exp);
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
			ex.printStackTrace();
			//this.LOGGER.error(ex.getMessage(), ex);
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
					.getSpecimenClass(), reqSpecimen);
		}
		catch (final AssignDataException e1)
		{
			//LOGGER.error(e1.getMessage(), e1);
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
		SpecimenUtility.setConsentTierStatusCollectionFromSCG(newSpecimen, specimenCollectionGroup);
		//		/newSpecimen.setConsentTierStatusCollectionFromSCG(specimenCollectionGroup);

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
			final Iterator<SpecimenRequirement> iterator = childrenspecimenList.iterator();
			while (iterator.hasNext())
			{
				final SpecimenRequirement childSpecimen = iterator.next();
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
			//this.LOGGER.error(nameGeneratorException.getMessage(), nameGeneratorException);
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
			//this.LOGGER.error(nameGeneratorException.getMessage(), nameGeneratorException);
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
			//this.LOGGER.error(exception.getMessage(), exception);
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
			//this.LOGGER.debug("Dynamic Group name: " + dynamicGroups[0]);
		}
		catch (final SMException e)
		{
			//this.LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, "sm.err.prot.grp", "");
		}
		catch (final ApplicationException e)
		{
			//this.LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, "sm.err.csm.loc", "");
		}

		return dynamicGroups;

	}

	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
	throws BizLogicException
{

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
	protected void update(DAO dao, Object obj, Object oldObj, Object uiObject, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			SpecimenCollectionGroupUIObject scgUIObject=(SpecimenCollectionGroupUIObject) uiObject;
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
			final Collection<SpecimenEventParameters> spEventColl = specimenCollectionGroup
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

			if (!Constants.WITHDRAW_RESPONSE_NOACTION.equalsIgnoreCase(scgUIObject
					.getConsentWithdrawalOption()))
			{
				this.verifyAndUpdateConsentWithdrawn(specimenCollectionGroup,
						oldspecimenCollectionGroup, dao, sessionDataBean,scgUIObject);
				persistentSCG.setConsentTierStatusCollection(specimenCollectionGroup
						.getConsentTierStatusCollection());

			}
			// Mandar 22-Jan-07 To disable consents accordingly in SCG and
			// Specimen(s) end
			// Mandar 24-Jan-07 To update consents accordingly in SCG and
			// Specimen(s) start uiObject
			/*else if (!specimenCollectionGroup.getApplyChangesTo().equalsIgnoreCase(
					Constants.APPLY_NONE))*/
			else if (!scgUIObject.getApplyChangesTo().equalsIgnoreCase(
					Constants.APPLY_NONE))
			{
				ConsentUtil.updateSpecimenStatusInSCG(specimenCollectionGroup,
						oldspecimenCollectionGroup, dao,scgUIObject);
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
			String barCode = specimenCollectionGroup.getBarcode();
			if ("" != barCode)
			{
				persistentSCG.setBarcode(AppUtility.handleEmptyStrings(barCode));
			}
			setConsetTierStatus(dao, specimenCollectionGroup, persistentSCG);

			// change by pathik
			if (!specimenCollectionGroup.getCollectionProtocolRegistration().getId().equals(0L)
					&& !persistentSCG.getCollectionProtocolRegistration().getId().equals(
							specimenCollectionGroup.getCollectionProtocolRegistration().getId()))
			{
				persistentSCG.setCollectionProtocolRegistration(specimenCollectionGroup
						.getCollectionProtocolRegistration());
			}

			dao.update(persistentSCG, oldspecimenCollectionGroup);

			/**
			 * Name : Ashish Gupta Reviewer Name : Sachin Lale Bug ID: 2741
			 * Patch ID: 2741_6 Description: Method to update events in all
			 * specimens related to this scg
			 */
			// Populating Events in all specimens
			if(scgUIObject.isApplyEventsToSpecimens()) //(specimenCollectionGroup.isApplyEventsToSpecimens())
			{
				this.updateEvents(copiedSCGEventsColl, oldspecimenCollectionGroup, sessionDataBean);
			}

			// Disable the related specimens to this specimen group
			//this.LOGGER.debug("specimenCollectionGroup.getActivityStatus() "
			//+ specimenCollectionGroup.getActivityStatus());
			if (specimenCollectionGroup.getActivityStatus().equals(
					Status.ACTIVITY_STATUS_DISABLED.toString()))
			{
				//this.LOGGER.debug("specimenCollectionGroup.getActivityStatus() "
				//+ specimenCollectionGroup.getActivityStatus());
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
			//this.LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final ApplicationException e)
		{
			//this.LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * @param dao
	 * @param specimenCollectionGroup
	 * @param persistentSCG
	 * @throws BizLogicException
	 */
	private void setConsetTierStatus(DAO dao,
			final SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCollectionGroup persistentSCG) throws BizLogicException
	{
		try
		{
			Collection<ConsentTierStatus> consentColl = specimenCollectionGroup
					.getConsentTierStatusCollection();
			if (consentColl != null && consentColl.iterator().hasNext())
			{
				/*
				 *  To check that the collection is lazily initialized
				 *  iterating the collection is required.
				 */
				consentColl.iterator().next();
			}

			persistentSCG.setConsentTierStatusCollection(specimenCollectionGroup
					.getConsentTierStatusCollection());

		}
		catch (org.hibernate.LazyInitializationException e)
		{
			/*
			 * for #15570
			 *
			 */
			final Collection consentTierStatusCollection = (Collection) this.retrieveAttribute(dao,
					SpecimenCollectionGroup.class, specimenCollectionGroup.getId(),
					"elements(consentTierStatusCollection)");
			persistentSCG.setConsentTierStatusCollection(consentTierStatusCollection);

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
			final User user = instFact.createObject();
			user.setId(sessionDataBean.getUserId());
			final Iterator scgEventCollIter = scgEventColl.iterator();
			while (scgEventCollIter.hasNext())
			{

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
		final User user = instFact.createObject();
		user.setId(sessionDataBean.getUserId());
		String collProcedure = null;
		String collContainer = null;
		String recQty = null;

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
		final User user = instFact.createObject();
		user.setId(sessionDataBean.getUserId());


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
			SpecimenCollectionGroup oldspecimenCollectionGroup, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		if (newEventColl != null && !newEventColl.isEmpty())
		{
			final Iterator newEventCollIter = newEventColl.iterator();
			while (newEventCollIter.hasNext())
			{
			}
		}
		final Collection<Specimen> specimenColl = oldspecimenCollectionGroup
				.getSpecimenCollection();
		if (specimenColl != null && !specimenColl.isEmpty())
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final SpecimenEventParametersBizLogic specimenEventParametersBizLogic = (SpecimenEventParametersBizLogic) factory
					.getBizLogic(Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID);
			final Iterator<Specimen> iter = specimenColl.iterator();
			while (iter.hasNext())
			{
				final Specimen specimen = (Specimen) iter.next();
				final Collection<SpecimenEventParameters> eventColl = specimen
						.getSpecimenEventCollection();
				if (eventColl != null && !eventColl.isEmpty())
				{
					final Iterator eventIter = eventColl.iterator();
					while (eventIter.hasNext())
					{
						final Object eventObj = eventIter.next();

					}
				}
			}
		}
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
		if (cpr.getSpecimenCollectionGroupCollection() == null)
		{
			cpr.setSpecimenCollectionGroupCollection(new HashSet<SpecimenCollectionGroup>());
		}
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
				//this.LOGGER.debug("Value returned:" + whereColumnValue[1]);
			}

			queryWhereClause.addCondition(new EqualClause(whereColumnName[1], whereColumnValue[1]));
			list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
		}
		catch (final DAOException daoexp)
		{
			//this.LOGGER.error(daoexp.getMessage(), daoexp);
			// ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
			throw new BizLogicException(daoexp.getErrorKey(), daoexp, daoexp.getMsgValues());
		}
		return list;
	}

	/**
	 * removed unused code
	 */
	//	/**
	//	 * @param dao : dao
	//	 * @param specimenCollectionGroup : specimenCollectionGroup
	//	 * @param oldSpecimenCollectionGroup : oldSpecimenCollectionGroup
	//	 * @throws BizLogicException : BizLogicException
	//	 */
	//	private void setCollectionProtocolRegistrationOld(DAO dao,
	//			SpecimenCollectionGroup specimenCollectionGroup,
	//			SpecimenCollectionGroup oldSpecimenCollectionGroup) throws BizLogicException
	//	{
	//		final List list = this.getCPRIdList(dao, specimenCollectionGroup,
	//				oldSpecimenCollectionGroup);
	//		if (!list.isEmpty())
	//		{
	//			// check for closed CollectionProtocolRegistration
	//			final CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
	//
	//			/**
	//			 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of
	//			 * Api Search, previoulsy it was failing since there was default
	//			 * class level initialization on domain object. For example in User
	//			 * object, it was initialized as protected String lastName=""; So we
	//			 * removed default class level initialization on domain object and
	//			 * are initializing in method setAllValues() of domain object. But
	//			 * in case of Api Search, default values will not get set since
	//			 * setAllValues() method of domainObject will not get called. To
	//			 * avoid null pointer exception, we are setting the default values
	//			 * same as we were setting in setAllValues() method of domainObject.
	//			 */
	//			ApiSearchUtil.setCollectionProtocolRegistrationDefault(collectionProtocolRegistration);
	//			// End:- Change for API Search
	//
	//			collectionProtocolRegistration.setId((Long) list.get(0));
	//			if (oldSpecimenCollectionGroup != null)
	//			{
	//				final CollectionProtocolRegistration collectionProtocolRegistrationOld = oldSpecimenCollectionGroup
	//						.getCollectionProtocolRegistration();
	//
	//				if (!collectionProtocolRegistration.getId().equals(
	//						collectionProtocolRegistrationOld.getId()))
	//				{
	//					this.checkStatus(dao, collectionProtocolRegistration,
	//							"Collection Protocol Registration");
	//				}
	//			}
	//			else
	//			{
	//				this.checkStatus(dao, collectionProtocolRegistration,
	//						"Collection Protocol Registration");
	//			}
	//
	//			specimenCollectionGroup
	//					.setCollectionProtocolRegistration(collectionProtocolRegistration);
	//		}
	//	}

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
		//this.auditDisabledObjects(dao, "CATISSUE_SPECIMEN_COLL_GROUP", listOfSubElement);
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
	 * check for collected specimens associated with the SCG.
	 * @param scgId : scgId
	 * @param obj : obj
	 * @param dao : dao
	 * @return boolean
	 * @throws BizLogicException : BizLogicException
	 */
	protected boolean isCollectedSpecimenExists(Object obj, DAO dao, Long scgId)
			throws BizLogicException
	{
		boolean isCollectedSpecimenExists = false;
		final String hql = " select" + " count(s.id) " + " from "
				+ " edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg , "
				+ " edu.wustl.catissuecore.domain.Specimen as s" + " where scg.id = " + scgId
				+ " and" + " scg.id = s.specimenCollectionGroup.id and " + " s.activityStatus = '"
				+ Status.ACTIVITY_STATUS_ACTIVE.toString() + "' and s.collectionStatus = '"
				+ Constants.COLLECTION_STATUS_COLLECTED + "'";// elements(scg.specimenCollection)

		final List specimenList = (List) this.executeHqlQuery(hql);
		if ((specimenList != null) && specimenList.size() != 0
				&& !specimenList.get(0).toString().equals("0"))
		{
			isCollectedSpecimenExists = true;
		}
		else
		{
			isCollectedSpecimenExists = false;
		}
		return isCollectedSpecimenExists;

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

			if (group.getCollectionProtocolRegistration().getId() == null)
			{
				if (group.getCollectionProtocolRegistration().getProtocolParticipantIdentifier() == null)
				{
					message = ApplicationProperties
							.getValue("errors.specimenCollectionGroup.collectionprotocolregistration.ppid");
					throw this.getBizLogicException(null, "errors.item.required", message);
				}
				final String hqlQry = "from "+ CollectionProtocolRegistration.class.getName()
						+ " as cpr where cpr.protocolParticipantIdentifier = '" +group.getCollectionProtocolRegistration().getProtocolParticipantIdentifier()
						+ "' and cpr.collectionProtocol.title= '"+group.getCollectionProtocolRegistration().getCollectionProtocol().getTitle()+"'";
				
				final List cprList = executeQuery(hqlQry);
				
				
//				List cprList = retrieve(group.getCollectionProtocolRegistration().getClass()
//						.getName(), "protocolParticipantIdentifier", group
//						.getCollectionProtocolRegistration().getProtocolParticipantIdentifier());
				if (cprList == null || cprList.isEmpty())
				{
					message = ApplicationProperties
							.getValue("specimenCollectionGroup.collectedByProtocolParticipantNumber") +" and " +
							ApplicationProperties
							.getValue("specimenCollectionGroup.protocolTitle");
					throw this.getBizLogicException(null, "errors.item.invalid", message);
				}
				CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) cprList
						.get(0);
				group.setCollectionProtocolRegistration(collectionProtocolRegistration);
			}
			else
			{
				final String sourceObjectName = CollectionProtocolRegistration.class.getName();
				final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
				final String[] selectColumnName = {"id"};

				queryWhereClause.addCondition(new EqualClause("id", group
						.getCollectionProtocolRegistration().getId()));
				final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
				
				if(list.isEmpty())
				{
					throw this.getBizLogicException(null, "errors.item.invalid", ApplicationProperties
							.getValue("specimenCollectionGroup.CollectionProtocoloRegistrationId"));
				}
			}
			boolean invalidSite = true;
			if(group.getSpecimenCollectionSite()!=null)
			{
				if(group.getSpecimenCollectionSite().getId()!=null&&group.getSpecimenCollectionSite().getId().longValue()!=0)
				{
					invalidSite = false;
				}
				else if(group.getSpecimenCollectionSite().getName()!=null)
				{
					invalidSite = false;
				}
			}
			if (invalidSite)
			{
				message = ApplicationProperties.getValue("specimenCollectionGroup.site");
				throw this.getBizLogicException(null, "errors.item.invalid", message);
			}
			else
			{
				String  attribute ="id";
				Object value = group.getSpecimenCollectionSite().getId();
				if(value==null)
				{
					attribute="name";
					value=group.getSpecimenCollectionSite().getName();
				}
				final String sourceObjectName = Site.class.getName();
				final String[] selectColumnName = {"id"};
				final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
				queryWhereClause.addCondition(new EqualClause(attribute,value));
				final List list = dao
						.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
				if (list.isEmpty())
				{
					message = ApplicationProperties.getValue("specimenCollectionGroup.site");
					throw this.getBizLogicException(null, "errors.item.invalid", message);
				}
				group.getSpecimenCollectionSite().setId((Long)list.get(0));
			}
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
			validateCpEvent(dao, group);

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
			/**
			 * bug 15684 start
			 * Used executeQuery() method as Clinical diagnosis with apostrophe in name is giving error
			 * while creating query.
			 */
			StringBuffer sqlBuff = new StringBuffer();
			sqlBuff.append("SELECT PermissibleValueImpl.value FROM "
					+ PermissibleValueImpl.class.getName() + " PermissibleValueImpl");
			sqlBuff
					.append(" WHERE  PermissibleValueImpl.cde.publicId = ? AND PermissibleValueImpl.value = ?");
			List<Object> columnValuesList = new ArrayList<Object>();
			columnValuesList.add("Clinical_Diagnosis_PID");
			columnValuesList.add(group.getClinicalDiagnosis());
			List list = ((HibernateDAO) dao).executeQuery(sqlBuff.toString(), null, null,
					columnValuesList);
			//bug 15684 end
			Iterator iterator = list.iterator();
			if (!iterator.hasNext())
			{
				throw this.getBizLogicException(null, "spg.clinicalDiagnosis.errMsg", "");
			}
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
				//bug 14350
				final boolean isSpecimenExist = (boolean) this.isCollectedSpecimenExists(obj, dao,
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
/*			if (group.getSpecimenEventParametersCollection() != null
					&& !group.getSpecimenEventParametersCollection().isEmpty())
			{
				final Iterator<SpecimenEventParameters> specimenEventCollectionIterator = group
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
						//this.LOGGER.error(e.getMessage(), e);
						throw this
								.getBizLogicException(null, e.getErrorKeyName(), e.getMsgValues());
					}
				}
			}
			else
			{
				throw this.getBizLogicException(null, "error.specimenCollectionGroup.noevents", "");
			}*/
			//validation added for bug #15237
			//			validateCpEvent(dao, group);

			return true;

		}
		catch (final DAOException daoExp)
		{
			//this.LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

		/*
		* catch (ApplicationException e){ logger.debug(e.getMessage(), e);
		* throw getBizLogicException(null, e.getErrorKeyName(), ""); }
		*/
	}

	/**
	 * Validate cp event.
	 *
	 * @param dao the dao
	 * @param group the group
	 *
	 * @throws DAOException the DAO exception
	 * @throws BizLogicException the biz logic exception
	 */
	private void validateCpEvent(DAO dao, final SpecimenCollectionGroup group) throws DAOException,
			BizLogicException
	{
		long cpId = 0;
		long cpEventId = 0;
		String cpEventLabel = "";
		if(group.getCollectionProtocolEvent() != null)
		{
			cpEventLabel = group.getCollectionProtocolEvent().getCollectionPointLabel();
			if (group.getCollectionProtocolEvent().getId() != null)
			{
				cpEventId = group.getCollectionProtocolEvent().getId();
			}
		}

		cpId = getCPId(dao, group, cpId);

		String equalCondition = "";
		Object equalValue = null;

		if (cpEventId >= 1)
		{
			equalCondition = "id";
			equalValue = cpEventId;

			checkCPEvent(dao, cpId, equalCondition, equalValue, group);
		}
		else if (!Validator.isEmpty(cpEventLabel))
		{
			equalCondition = "collectionPointLabel";
			equalValue = cpEventLabel;
			checkCPEvent(dao, cpId, equalCondition, equalValue, group);

		}
		else
		{
			if (group.getCollectionProtocolEvent() == null
					|| group.getCollectionProtocolEvent().getId() == null)
			{
				String message = ApplicationProperties
						.getValue("specimenCollectionGroup.studyCalendarEventPoint");
				throw this.getBizLogicException(null, "errors.item.required", message);
			}
		}

	}

	/**
	 * @param dao
	 * @param group
	 * @param cpId
	 * @return
	 * @throws DAOException
	 */
	private long getCPId(DAO dao, final SpecimenCollectionGroup group, long cpId)
			throws DAOException
	{
		long newCPId = cpId;
		if (group.getCollectionProtocolRegistration().getId() != 0)
		{
			final String sourceObjectName = CollectionProtocolRegistration.class.getName();
			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			final String[] selectColumnName = {"collectionProtocol.id"};

			queryWhereClause.addCondition(new EqualClause("id", group
					.getCollectionProtocolRegistration().getId()));
			final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

			if (!list.isEmpty())
			{
				newCPId = Long.valueOf(list.get(0).toString());
			}
		}
		else if (group.getCollectionProtocolRegistration().getCollectionProtocol() != null)
		{
			newCPId = group.getCollectionProtocolRegistration().getCollectionProtocol().getId();
		}
		return newCPId;
	}

	/**
	 * @param dao
	 * @param cpId
	 * @param equalCondition
	 * @param equalValue
	 * @param x
	 * @throws DAOException
	 * @throws BizLogicException
	 */
	private void checkCPEvent(DAO dao, long cpId, String equalCondition, Object equalValue,
			SpecimenCollectionGroup group) throws DAOException, BizLogicException
	{
		CollectionProtocol collectionProtocol = group.getCollectionProtocolRegistration()
				.getCollectionProtocol();

		String sourceObjectName1 = null;
		QueryWhereClause queryWhereClause1 = null;
		String selectedColumnName = null;
		String[] selectColumnName1 = {selectedColumnName};

		if (collectionProtocol != null)
		{
			selectColumnName1[0] = "collectionProtocol";
			sourceObjectName1 = CollectionProtocolEvent.class.getName();
			queryWhereClause1 = new QueryWhereClause(sourceObjectName1);
			queryWhereClause1.addCondition(new EqualClause(equalCondition, equalValue));
			queryWhereClause1.andOpr();
			queryWhereClause1.addCondition(new EqualClause("collectionProtocol.id",
					collectionProtocol.getId()));
		}
		else if (group.getCollectionProtocolRegistration() != null
				&& group.getCollectionProtocolRegistration().getId() != null)
		{
			selectColumnName1[0] = "collectionProtocol";
			sourceObjectName1 = CollectionProtocolRegistration.class.getName();
			queryWhereClause1 = new QueryWhereClause(sourceObjectName1);
			queryWhereClause1.addCondition(new EqualClause("id", group
					.getCollectionProtocolRegistration().getId()));
		}
		final List list = dao.retrieve(sourceObjectName1, selectColumnName1, queryWhereClause1);
		if (list.size() > 0)
		{
			CollectionProtocol actualCp = (CollectionProtocol) (list.get(0));

			Long actualCpId = actualCp.getId();
			if (actualCpId == null || (actualCpId != 0 && cpId != 0 && actualCpId != cpId))
			{
				String message = ApplicationProperties
						.getValue("specimenCollectionGroup.studyCalenderEventPointAndPPID");
				throw this.getBizLogicException(null, "", message);
			}
			Long cpEventId = getCPEventID(group, actualCp);
			group.getCollectionProtocolEvent().setId(cpEventId);
		}
		else
		{
			String message = ApplicationProperties
					.getValue("specimenCollectionGroup.studyCalendarEventPoint");
			throw this.getBizLogicException(null, "errors.item.invalid", message);
		}

	}

	/**
	 * returns the CPE id
	 * @param group
	 * @param actualCp
	 * @return
	 */
	private Long getCPEventID(SpecimenCollectionGroup group, CollectionProtocol actualCp)
	{
		Collection<CollectionProtocolEvent> cpEventColl = actualCp
				.getCollectionProtocolEventCollection();
		CollectionProtocolEvent cpEvent = null;
		String eventLabel = group.getCollectionProtocolEvent().getCollectionPointLabel();
		Long eventId = group.getCollectionProtocolEvent().getId();
		if (eventLabel != null)
		{
			for (CollectionProtocolEvent event : cpEventColl)
			{
				if (event.getCollectionPointLabel().equals(eventLabel))
				{
					cpEvent = event;
					break;
				}
			}
		}
		else if (eventId != null)
		{
			for (CollectionProtocolEvent event : cpEventColl)
			{
				if (event.getId().equals(eventId))
				{
					cpEvent = event;
					break;
				}
			}
		}
		return cpEvent.getId();
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
			String sourceObjectName = "CATISSUE_SPECIMEN_COLL_GROUP";
			final String[] selectColumnName = {"max(IDENTIFIER) as MAX_IDENTIFIER"};
			return AppUtility.getNextUniqueNo(sourceObjectName, selectColumnName);
		}
		catch (final ApplicationException exp)
		{
			//LOGGER.error(exp.getMessage(), exp);
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
		//this.LOGGER.debug("Start of getSCGTreeForCPBasedView");

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
		//this.LOGGER
		//.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB (SpecimenCollectionGroupBizlogic)-  : "
		//+ (endTime - startTime));
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
			InstanceFactory<SpecimenCollectionGroup> instFact = DomainInstanceFactory
					.getInstanceFactory(SpecimenCollectionGroup.class);
			final SpecimenCollectionGroup scg = instFact.createObject();//new SpecimenCollectionGroup();
			scg.setId((Long) obj1[0]);
			scg.setName((String) obj1[1]);
			scg.setActivityStatus((String) obj1[2]);
			scg.setCollectionStatus((String) obj1[3]);
			scg.setOffset((Integer) obj1[4]);

			InstanceFactory<CollectionProtocolEvent> cpeInstFact = DomainInstanceFactory
					.getInstanceFactory(CollectionProtocolEvent.class);
			final CollectionProtocolEvent cpe = cpeInstFact.createObject();//new CollectionProtocolEvent();
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
			InstanceFactory<CollectionProtocolRegistration> cprInstFact = DomainInstanceFactory
					.getInstanceFactory(CollectionProtocolRegistration.class);
			collectionProtocolRegistration = cprInstFact.createObject();
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

	
	//delete this method.
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
		System.out.println("in SCG createTreeNodeForExistngSCG");
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
				scgNodeLabel = "T" + eventPoint + ": " + collectionPointLabel;
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
					+ specimenCollectionGroup.getCollectionStatus() + "\">");

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
			InstanceFactory<Specimen> instFact = DomainInstanceFactory
					.getInstanceFactory(Specimen.class);
			final Specimen specimen = instFact.createObject();// new Specimen();
			specimen.setId((Long) obj[0]);
			specimen.setLabel((String) obj[1]);
			specimen.setActivityStatus((String) obj[3]);
			specimen.setSpecimenType((String) obj[4]);
			specimen.setCollectionStatus((String) obj[5]);
			Long parentSpecimenId = (Long) obj[2];

			if (parentSpecimenId == null)
			{
				parentSpecimenId = Long.valueOf(0);
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
//		 List collectionEventPara =
//		 (List)getCollectionEventParameters(specimen
//		 .getSpecimenEventCollection());
//		final String hqlCon = "select colEveParam.container from "
//				+ CollectionEventParameters.class.getName()
//				+ " as colEveParam where colEveParam.specimen.id = " + spId;
//		final List container = this.executeQuery(hqlCon);
//		for (int k = 0; k < container.size(); k++)
//		{
//			// CollectionEventParameters collectionEventParameters =
//			// (CollectionEventParameters)collectionEventPara.get(k);
//			final String con = (String) container.get(k);
//			toolTipText += " ; Container : " + con;
//		}

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

	//removed unused method
	//	/**
	//	 * kalpana Bug #5906 Reviewer : vaishali Update the children specimen count
	//	 * of all the parent specimens accept the immediate parent.
	//	 * @param finalList : finalList
	//	 * @param parentSpecimen : parentSpecimen
	//	 * @param countOfChildSpecimenMap : countOfChildSpecimenMap
	//	 * @param countOfChildSpecimen : countOfChildSpecimen
	//	 */
	//	private void updateChildSpecimenCount(List finalList, Specimen parentSpecimen,
	//			Map countOfChildSpecimenMap, Integer countOfChildSpecimen)
	//	{
	//
	//		for (int j = 0; j < finalList.size(); j++)
	//		{
	//			final Specimen specimen = (Specimen) finalList.get(j);
	//			final Long specimenId = (Long) specimen.getId();
	//
	//			if (specimen.getParentSpecimen() != null)
	//			{
	//
	//				if (specimenId != null && parentSpecimen != null
	//						&& specimenId.equals(parentSpecimen.getId()))
	//				{
	//					if (countOfChildSpecimenMap.containsKey(specimenId))
	//					{
	//						Integer newChildCount = (Integer) countOfChildSpecimenMap.get(specimenId);
	//						newChildCount = newChildCount + 1;
	//						countOfChildSpecimenMap.put(specimenId, newChildCount);
	//
	//						this
	//								.updateChildSpecimenCount(finalList, (Specimen) specimen
	//										.getParentSpecimen(), countOfChildSpecimenMap,
	//										countOfChildSpecimen);
	//						return;
	//					}
	//				}
	//			}
	//		}
	//	}

	/**
	 * @param offsetToAdd : offsetToAdd
	 */
	private void addOffset(Integer offsetToAdd)
	{
		Integer offset = null;
		if (this.offsetForCPOrEvent != null)
		{
			offset = Integer.valueOf(this.offsetForCPOrEvent.intValue() + offsetToAdd.intValue());
			this.offsetForCPOrEvent = offset;
		}
		else
		{
			offset = Integer.valueOf(offsetToAdd.intValue());
			this.offsetForCPOrEvent = offset;
		}
	}

	/**
	 * removed unused method
	 */
	//	/**
	//	 * @param offset : offset
	//	 * @param offsetForCPOrEvent : offsetForCPOrEvent
	//	 * @return int
	//	 */
	//	private int getNoOfDaysAccordingToOffset(Integer offset, Integer offsetForCPOrEvent)
	//	{
	//		int noOfDays = 0;
	//		if (offsetForCPOrEvent != null && offset != null
	//				&& offsetForCPOrEvent.intValue() > offset.intValue())
	//		{
	//			noOfDays = offsetForCPOrEvent.intValue() - offset.intValue();
	//		}
	//		if (offsetForCPOrEvent != null && offset == null)
	//		{
	//			noOfDays = offsetForCPOrEvent.intValue();
	//		}
	//		return noOfDays;
	//	}

	/**
	 * @param dao : dao
	 * @param hql : hql
	 * @return List
	 * @throws BizLogicException : BizLogicException
	 */
	private List executeHqlQuery(String hql) throws BizLogicException
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
		if (receivedDate != null&& !receivedDate.equals(""))
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
			SessionDataBean sessionDataBean,SpecimenCollectionGroupUIObject scgUIObject) throws BizLogicException, ApplicationException
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
				final String cprWithdrawOption = scgUIObject
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
			//this.LOGGER.error(daoExp.getMessage(), daoExp);
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
			//this.LOGGER.error(daoExp.getMessage(), daoExp);
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
	public SpecimenCollectionGroup retrieveSCG(DAO dao, SpecimenCollectionGroup scg)
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
			else if (scg.getName() != null)
			{
				queryWhereClause.addCondition(new EqualClause("name", scg.getName()));
			}
			final List dataList = this.getSCGFromDB(queryWhereClause, dao);
			if (dataList != null
					&& !dataList.isEmpty()
					&& Status.ACTIVITY_STATUS_DISABLED.toString().equals(
							((Object[]) dataList.get(0))[1]))
			{
				throw this.getBizLogicException(null, "error.object.disabled",
						"Specimen Collection Group");
			}
			absScg = this.initSCG(dataList, dao, scg);
			return absScg;
		}
		catch (final DAOException daoExp)
		{
			//this.LOGGER.error(daoExp.getMessage(), daoExp);
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
				"collectionProtocolRegistration.protocolParticipantIdentifier",
				"collectionProtocolRegistration.collectionProtocol.id",
				"collectionProtocolRegistration.collectionProtocol.activityStatus",
				"collectionProtocolRegistration.collectionProtocol.specimenLabelFormat",
				"collectionProtocolRegistration.collectionProtocol.derivativeLabelFormat",
				"collectionProtocolRegistration.collectionProtocol.aliquotLabelFormat"};
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
	private SpecimenCollectionGroup initSCG(List dataList, DAO dao, SpecimenCollectionGroup scg)
			throws BizLogicException
	{
		SpecimenCollectionGroup absScg = null;
		if (dataList.size() != 0)
		{
			final Object[] valArr = (Object[]) dataList.get(0);
			if (valArr != null && valArr.length == 9)
			{
				final Long identifier = (Long) valArr[0];
				final String activityStatus = (String) valArr[1];
				InstanceFactory<CollectionProtocolRegistration> cprInstFact = DomainInstanceFactory
						.getInstanceFactory(CollectionProtocolRegistration.class);
				final CollectionProtocolRegistration registration = cprInstFact.createObject();
				registration.setId((Long) valArr[2]);
				if (valArr[3] != null)
				{
					registration.setProtocolParticipantIdentifier(valArr[3].toString());
				}
				InstanceFactory<CollectionProtocol> cpInstFact = DomainInstanceFactory
						.getInstanceFactory(CollectionProtocol.class);
				CollectionProtocol collectionProtocol = cpInstFact.createObject();
				collectionProtocol.setId((Long) valArr[4]);
				collectionProtocol.setActivityStatus((String) valArr[5]);
				if (valArr[6] != null)
				{
					collectionProtocol.setSpecimenLabelFormat(valArr[6].toString());
				}
				if (valArr[7] != null)
				{
					collectionProtocol.setDerivativeLabelFormat(valArr[7].toString());
				}
				if (valArr[8] != null)
				{
					collectionProtocol.setAliquotLabelFormat(valArr[8].toString());
				}
				//For bug #15994
				String CpShortTitle;
				CpShortTitle = retrieveCpShortTitle(valArr[4]);
				collectionProtocol.setShortTitle(CpShortTitle);
				final Collection consentTierStatusCollection = (Collection) this.retrieveAttribute(
						dao, SpecimenCollectionGroup.class, identifier,
						"elements(consentTierStatusCollection)");
				absScg = (SpecimenCollectionGroup) scg;
				absScg.setName(scg.getName());
				absScg.setId(identifier);
				absScg.setActivityStatus(activityStatus);
				if (consentTierStatusCollection != null && !consentTierStatusCollection.isEmpty())
				{
					absScg.setConsentTierStatusCollection(consentTierStatusCollection);
				}
				else
				{
					try
					{
						// This is to handle API test cases. While running from API test cases, this collection is prepopulated
						if ((absScg.getConsentTierStatusCollection() != null)
								&& (!absScg.getConsentTierStatusCollection().isEmpty()))
						{
							absScg.setConsentTierStatusCollection(absScg
									.getConsentTierStatusCollection());
						}
					}
					catch (LazyInitializationException e)
					{
						//						this.LOGGER.error(e.getMessage(),e) ;
						absScg.setConsentTierStatusCollection(consentTierStatusCollection);
					}
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

	/**
	 * @param valArr
	 * @param collectionProtocol
	 * @throws BizLogicException
	 */
	private String retrieveCpShortTitle(final Object cpId) throws BizLogicException
	{
		String hql = "select cp.shortTitle from edu.wustl.catissuecore.domain.CollectionProtocol cp where cp.id="
				+ cpId;
		String shortTitle = "";
		try
		{

			List cpList = AppUtility.executeQuery(hql);
			if (cpList != null && !cpList.isEmpty())
			{
				shortTitle = cpList.get(0).toString();
			}
		}
		catch (ApplicationException exp)
		{
			//this.LOGGER.error(exp.getMessage(),exp) ;
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
		return shortTitle;
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
			InstanceFactory<CollectionProtocolRegistration> cprInstFact = DomainInstanceFactory
					.getInstanceFactory(CollectionProtocolRegistration.class);
			cpr = cprInstFact.createObject();
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
	public String getObjectId(DAO dao, Object domainObject) throws BizLogicException
	{
		String objectId = "";
		if (domainObject instanceof SpecimenCollectionGroup)
		{
			final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) domainObject;
			Long cpId = null;
			final CollectionProtocolRegistration cpr = specimenCollectionGroup
					.getCollectionProtocolRegistration();
			if (cpr.getCollectionProtocol() != null && cpr.getCollectionProtocol().getId() != null)
			{
				cpId = cpr.getCollectionProtocol().getId();
			}
			else
			{
				cpId = retreiveCPId(dao, specimenCollectionGroup);
			}
			objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cpId;
		}
		return objectId;
	}

	/**
	 * generated query to retrieve the CPR id.
	 * @param scg
	 * @return
	 * @throws BizLogicException
	 */
	private Long retreiveCPId(DAO dao, final SpecimenCollectionGroup scg) throws BizLogicException
	{

		final String query = generateQueryToRetrieveCPId(scg);
		Long cpId = executeQuery(dao, query);
		if (cpId == null)
		{
			String message = ApplicationProperties
					.getValue("specimenCollectionGroup.studyCalenderEventPointAndPPID");
			throw this.getBizLogicException(new Exception(message), "", message);
		}

		return cpId;
	}

	/**
	 * generates the query to retrieve Collection Protocol Id based on
	 * CPE and CPR
	 * @param scg
	 * @return
	 */
	private String generateQueryToRetrieveCPId(final SpecimenCollectionGroup scg)
	{

		StringBuffer query = new StringBuffer(
				"select specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.id  from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as specimenCollectionGroup where ");
		modifyQueryForCPE(scg, query);
		modifyQueryForCPR(scg, query);
		return query.toString();
	}

	/**
	 * Modifies the query to retrieve the collection
	 * protocol Id based on CPR
	 * @param scg
	 * @param query
	 */
	private void modifyQueryForCPR(final SpecimenCollectionGroup scg, StringBuffer query)
	{
		if (scg.getCollectionProtocolRegistration().getId() != null)
		{
			query.append(" and specimenCollectionGroup.collectionProtocolRegistration.id = ")
					.append(scg.getCollectionProtocolRegistration().getId());
		}
		else
		{

			query
					.append(
							"   and specimenCollectionGroup.collectionProtocolRegistration.protocolParticipantIdentifier = '")
					.append(
							scg.getCollectionProtocolRegistration()
									.getProtocolParticipantIdentifier()).append("'");
		}
	}

	/**
	 * Modifies the query to retrieve the collection
	 * protocol Id based on CPE
	 * @param scg
	 * @param query
	 */
	private void modifyQueryForCPE(final SpecimenCollectionGroup scg, StringBuffer query)
	{
		if (scg.getCollectionProtocolEvent().getId() != null)
		{
			query.append("specimenCollectionGroup.collectionProtocolEvent.id = ").append(
					scg.getCollectionProtocolEvent().getId());
		}
		else if (scg.getCollectionProtocolEvent().getCollectionPointLabel() != null)
		{
			query
					.append(
							"specimenCollectionGroup.collectionProtocolEvent.collectionPointLabel = '")
					.append(scg.getCollectionProtocolEvent().getCollectionPointLabel()).append("'");
		}
	}

	private Long executeQuery(DAO dao, final String query) throws BizLogicException
	{
		Long cpId = null;
		try
		{
			List<Long> list = null;

			list = dao.executeQuery(query);
			final Iterator<Long> itr = list.iterator();
			while (itr.hasNext())
			{
				cpId = (Long) itr.next();
			}
		}
		catch (DAOException e)
		{
			throw new BizLogicException(e);
		}
		return cpId;
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
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		SpecimenCollectionGroupUIObject scgUIObject = new SpecimenCollectionGroupUIObject();
		return isAuthorized(dao, domainObject, sessionDataBean, scgUIObject);
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
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean,Object uiObject)
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
			//this.LOGGER.error(exp.getMessage(), exp);
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