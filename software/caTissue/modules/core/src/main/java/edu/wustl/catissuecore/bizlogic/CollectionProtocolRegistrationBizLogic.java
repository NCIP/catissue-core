/**
 * <p>Title: UserHDAO Class>
 * <p>Description:	UserHDAO is used to add user information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 * Created on Apr 13, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import edu.wustl.catissuecore.TaskTimeCalculater;
import edu.wustl.catissuecore.dao.GenericDAO;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.factory.utils.SpecimenCollectionGroupUtility;
import edu.wustl.catissuecore.factory.utils.SpecimenUtility;
import edu.wustl.catissuecore.namegenerator.BarcodeGenerator;
import edu.wustl.catissuecore.namegenerator.BarcodeGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.catissuecore.uiobject.CollectionProtocolRegistrationUIObject;
import edu.wustl.catissuecore.uiobject.ParticipantUIObject;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CollectionProtocolSeqComprator;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.ParticipantRegistrationInfo;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.AuditException;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.locator.CSMGroupLocator;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class CollectionProtocolRegistrationBizLogic extends CatissueDefaultBizLogic implements ICollectionProtocolRegistrationBizLogic
{

	/**
	 * logger instance of the class.
	 */
	private static final transient Logger LOGGER = Logger
	.getCommonLogger(CollectionProtocolRegistrationBizLogic.class);
	/**
	 * Saves the user object in the database.
	 * @param obj
	 *            The user object to be saved.
	 * @param session
	 *            The session in which the object is saved.
	 * @throws BizLogicException
	 */
	private static boolean armFound = false;
	/**
	 * dateOfLastEvent.
	 */
	private Date dateOfLastEvent = null;
	/**
	 * offset.
	 */
	private static int offset = 0;
	/**
	 * cntOfStudyCalEventPnt.
	 */
	private int cntOfStudyCalEventPnt = 0;

	private GenericDAO genericDAO;

	private IParticipantBizLogic participantBizLogic;

	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		ParticipantUIObject partUIObject = new ParticipantUIObject();
		insert(obj,partUIObject,dao,sessionDataBean);
	}

	/**
	 * Insert.
	 */
	@Override
	protected void insert(Object obj,Object uiObject, DAO dao, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		ParticipantUIObject partUIObject=(ParticipantUIObject)uiObject;
		offset = 0;
		armFound = false;
		final CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
		final CollectionProtocol objCollectionProtocol = collectionProtocolRegistration.getCollectionProtocol();
		final String ppi = collectionProtocolRegistration.getProtocolParticipantIdentifier();
		CollectionProtocolRegistrationUIObject cprUIObject = new CollectionProtocolRegistrationUIObject();
		Map<Long, CollectionProtocolRegistrationUIObject> cprCollection = partUIObject.getCprUIObject();
		if(cprCollection != null)
		{
			cprUIObject = (CollectionProtocolRegistrationUIObject)(
				(partUIObject.getCprUIObject()).get(objCollectionProtocol.getId()));
		}
		try
		{
			validateCPR(dao, collectionProtocolRegistration,
					objCollectionProtocol, ppi);
			Participant participant = null;
			if (collectionProtocolRegistration.getParticipant() == null)
			{
				participant = this.addDummyParticipant(dao);
				/*final Object participantObj = dao.retrieveById(Participant.class.getName(),
						collectionProtocolRegistration.getParticipant().getId());
				if (participantObj != null)
				{
					participant = (Participant) participantObj;
				}*/
			}
			else if(collectionProtocolRegistration.getParticipant().getId() == null)
			{
				dao.insert(collectionProtocolRegistration.getParticipant());
				participant = collectionProtocolRegistration.getParticipant();
			}
			else
			{
				participant = collectionProtocolRegistration.getParticipant();
			}

			collectionProtocolRegistration.setParticipant(participant);
			final String barcode = collectionProtocolRegistration.getBarcode();
			this.generateCPRBarcode(collectionProtocolRegistration);
			//Label generator for Protocol Participant Identifier
			this.generateProtocolParticipantIdentifierLabel(collectionProtocolRegistration);

			if ((barcode != collectionProtocolRegistration.getBarcode()) && barcode != null)
			{
				collectionProtocolRegistration.setBarcode(AppUtility.handleEmptyStrings(barcode));
			}

			this.insertCPR(collectionProtocolRegistration, dao, sessionDataBean,cprUIObject);
			if (armFound == false
					&& Constants.ARM_CP_TYPE.equals(collectionProtocolRegistration
							.getCollectionProtocol().getType()))
			{
				this.armCheckandRegistration(collectionProtocolRegistration, dao, sessionDataBean,cprUIObject);
			}
		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		catch (final AuditException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * @param dao
	 * @param cpr
	 * @param cp
	 * @param ppi
	 * @throws BizLogicException
	 */
	private void validateCPR(
			DAO dao,
			final CollectionProtocolRegistration cpr,
			final CollectionProtocol cp, final String ppi)
			throws BizLogicException {
		// check for closed Collection Protocol
		this.checkStatus(dao, cpr.getCollectionProtocol(),
		"Cannot register the participant since the Collection Protocol");
		// Check for closed Participant
		this.checkStatus(dao, cpr.getParticipant(), "Participant");
		if (cp != null) {
			boolean isUnique = this.checkUniqueConstraint(dao, cp.getId(), ppi);
			if (!isUnique) {
				final ErrorKey errorKey = ErrorKey
						.getErrorKey("Err.ConstraintViolation");
				throw new BizLogicException(errorKey, null,
						"Participant:Participant Protocol ID within this Collection Protocol");
			}
		}
	}

	/**
	 * Method to generate barcode for the SpecimenCollectionGroup.
	 * @param collectionProtocolRegistration
	 * @throws BizLogicException DAO exception
	 */
	private void generateCPRBarcode(CollectionProtocolRegistration collectionProtocolRegistration)
	throws BizLogicException
	{
		try
		{
			if (Variables.isCollectionProtocolRegistrationBarcodeGeneratorAvl)
			{
				final BarcodeGenerator collectionProtocolRegistrationBarcodeGenerator = BarcodeGeneratorFactory
				.getInstance(Constants.COLL_PROT_REG_BARCODE_GENERATOR_PROPERTY_NAME);
				collectionProtocolRegistrationBarcodeGenerator
				.setBarcode(collectionProtocolRegistration);
			}
		}
		catch (final NameGeneratorException nameGeneratorException)
		{
			LOGGER.error(nameGeneratorException.getMessage(), nameGeneratorException);
			throw this.getBizLogicException(nameGeneratorException, "name.generator.exp", "");
		}
	}

	/**
	 * This method is called when any arm is registered and it has no further
	 * child arms. This method then registers the remaining phases of the parent
	 * CollectionProtocol and is called recursively till the
	 * MainParentCollectionProtocol's phases are registered
	 * @param collectionProtocolRegistration
	 *            The CollectionProtocolRegistration Object for current
	 *            CollectionProtocol
	 * @param dao
	 *            The DAO object
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException bizlogic exception
	 * @throws DAOException dAOException
	 */
	private void armCheckandRegistration(
			CollectionProtocolRegistration collectionProtocolRegistration, DAO dao,
			SessionDataBean sessionDataBean,CollectionProtocolRegistrationUIObject cprUIObject) throws BizLogicException, DAOException
			{
		final Integer sequenceNumber = collectionProtocolRegistration.getCollectionProtocol()
		.getSequenceNumber();
		final CollectionProtocol parentCPofArm = collectionProtocolRegistration
		.getCollectionProtocol().getParentCollectionProtocol();
		Date dateofCP = new Date();
		if (parentCPofArm != null)
		{
			dateofCP = this.getImmediateParentCPdate(parentCPofArm.getId(),
					collectionProtocolRegistration.getParticipant().getId());
			final List<CollectionProtocol> childCPColl = this.getChildColl(parentCPofArm);
			final Iterator<CollectionProtocol> iteratorofchildCP = childCPColl.iterator();
			while (iteratorofchildCP.hasNext())
			{
				if (armFound)
				{
					break;
				}

				final CollectionProtocol protocol = iteratorofchildCP.next();
				if (protocol != null && protocol.getSequenceNumber() != null)
				{
					if (protocol.getSequenceNumber().intValue() > sequenceNumber.intValue())
					{
						final CollectionProtocolRegistration collectionProtocolRegistrationCheck = this
						.getCPRbyCollectionProtocolIDAndParticipantID(dao, protocol.getId(),
								collectionProtocolRegistration.getParticipant().getId());
						if (collectionProtocolRegistrationCheck == null)
						{
							final CollectionProtocolRegistration childCollectionProtocolRegistration = this
							.createCloneOfCPR(collectionProtocolRegistration, protocol);
							this.setRegDate(childCollectionProtocolRegistration, protocol
									.getStudyCalendarEventPoint(), dateofCP);
							this.getTotalOffset(childCollectionProtocolRegistration, dao,
									sessionDataBean);
							//inherits Appropriate date
							if (offset != 0 && protocol.getStudyCalendarEventPoint() != null)
							{
								childCollectionProtocolRegistration
								.setRegistrationDate(AppUtility
										.getNewDateByAdditionOfDays(
												childCollectionProtocolRegistration
												.getRegistrationDate(), offset));
							}
							this.insertCPR(childCollectionProtocolRegistration, dao,
									sessionDataBean,cprUIObject);
						}
						else
						{
							/* this lines of code is for second arm registered manually*/
							this.setRegDate(collectionProtocolRegistrationCheck, protocol
									.getStudyCalendarEventPoint(), dateofCP);
							//if registered CPR has offset on itself
							if (collectionProtocolRegistrationCheck.getOffset() != null && collectionProtocolRegistrationCheck.getOffset().intValue() != 0)
							{
								collectionProtocolRegistrationCheck
								.setRegistrationDate(AppUtility
										.getNewDateByAdditionOfDays(
												collectionProtocolRegistrationCheck
												.getRegistrationDate(),
												collectionProtocolRegistrationCheck
												.getOffset().intValue()));
							}
							//total offset of all the previously registered ColectionProtocols and SCGs
							this.getTotalOffset(collectionProtocolRegistrationCheck, dao,
									sessionDataBean);
							//							if ()//if before registration of second arm the registered CP has offset on itself.this condition is taken into consideration here.
							//							{
							//								collectionProtocolRegistrationCheck.setOffset(offset);
							//bug 6500 so that CP with null studyCalendarEventPoint inherits Appropriate date
							if (offset != 0 && protocol.getStudyCalendarEventPoint() != null)
							{
								collectionProtocolRegistrationCheck
								.setRegistrationDate(AppUtility
										.getNewDateByAdditionOfDays(
												collectionProtocolRegistrationCheck
												.getRegistrationDate(), offset));
							}

							//							}
							dao.update(collectionProtocolRegistrationCheck);
							//updateOffsetForEventsForAlreadyRegisteredCPR(dao, sessionDataBean, collectionProtocolRegistrationCheck);
							this.checkAndUpdateChildDate(dao, sessionDataBean,
									collectionProtocolRegistrationCheck);

						}
					}
					/* Here check is done for CPR not to be same and then if another arm is registered SCG's of previous arm which are not collected are disabled*/
					else if (protocol.getSequenceNumber().intValue() == sequenceNumber.intValue())
					{
						final CollectionProtocolRegistration collectionProtocolRegistrationOfPreviousArm = this
						.getCPRbyCollectionProtocolIDAndParticipantID(dao, protocol.getId(),
								collectionProtocolRegistration.getParticipant().getId());
						//						if (collectionProtocolRegistrationOfPreviousArm != null)
						//						{
						if (collectionProtocolRegistrationOfPreviousArm != null && !(collectionProtocolRegistrationOfPreviousArm.getId()
								.equals(collectionProtocolRegistration.getId())))
						{
							final Long identifier = this.getIdofCPR(dao, sessionDataBean,
									collectionProtocolRegistration);
							if (!(collectionProtocolRegistrationOfPreviousArm.getId()
									.equals(identifier)))
							{
								this.changeStatusOfEvents(dao, sessionDataBean,
										collectionProtocolRegistrationOfPreviousArm);
								this.checkForChildStatus(dao, sessionDataBean,
										collectionProtocolRegistrationOfPreviousArm);
							}
						}
						//						}

					}

				}

			}
			if (parentCPofArm != null)
			{
				//armCheckandRegistration(CPRofParent, dao, sessionDataBean);
				this.armCheckandRegistration(this.createCloneOfCPR(collectionProtocolRegistration,
						parentCPofArm), dao, sessionDataBean,cprUIObject);
			}
		}

			}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#getTotalOffset(edu.wustl.catissuecore.domain.CollectionProtocolRegistration, edu.wustl.dao.DAO, edu.wustl.common.beans.SessionDataBean)
	 */
	@Override
	public void getTotalOffset(CollectionProtocolRegistration collectionProtocolRegistration,
			DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
			{
		offset = 0;
		final CollectionProtocol collectionProtocol = collectionProtocolRegistration
		.getCollectionProtocol();
		final Long participantId = collectionProtocolRegistration.getParticipant().getId();
		CollectionProtocol mainParent = null;
		if (collectionProtocol.getParentCollectionProtocol() == null)
		{
			mainParent = collectionProtocol;

		}
		else
		{
			mainParent = this.getMainParentCP(collectionProtocol.getParentCollectionProtocol());
		}
		this.calculationOfTotalOffset(mainParent, dao, sessionDataBean, participantId,
				collectionProtocol);
			}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#calculationOfTotalOffset(edu.wustl.catissuecore.domain.CollectionProtocol, edu.wustl.dao.DAO, edu.wustl.common.beans.SessionDataBean, java.lang.Long, edu.wustl.catissuecore.domain.CollectionProtocol)
	 */
	@Override
	public void calculationOfTotalOffset(CollectionProtocol collectionProtocol, DAO dao,
			SessionDataBean sessionDataBean, Long participantId,
			CollectionProtocol collectionProtocolToRegister) throws BizLogicException
			{
		if (collectionProtocol.getId() != collectionProtocolToRegister.getId())
		{
			final CollectionProtocolRegistration cpr = this
			.getCPRbyCollectionProtocolIDAndParticipantID(dao, collectionProtocol.getId(),
					participantId);
			if (cpr != null)
			{
				final Integer offsetFromCP = cpr.getOffset();
				if (offsetFromCP != null && offsetFromCP.intValue() != 0)
				{
					//					if (offsetFromCP.intValue() != 0)
					//					{
					offset = offset + offsetFromCP.intValue();
					//					}
				}
				this.offsetFromSCG(cpr);
				if (collectionProtocol.getChildCollectionProtocolCollection() != null)
				{
					final List<CollectionProtocol> childCollectionCP = this.getChildColl(collectionProtocol);
					if (!(childCollectionCP.isEmpty()))
					{
						final Iterator<CollectionProtocol> childCollectionCPIterator = childCollectionCP
						.iterator();
						while (childCollectionCPIterator.hasNext())
						{
							final CollectionProtocol collProtocol = childCollectionCPIterator.next();
							this.calculationOfTotalOffset(collProtocol, dao, sessionDataBean, participantId,
									collectionProtocolToRegister);
						}
					}
				}
			}
		}
			}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#offsetFromSCG(edu.wustl.catissuecore.domain.CollectionProtocolRegistration)
	 */
	@Override
	public void offsetFromSCG(CollectionProtocolRegistration cpr)
	{
		final Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection = cpr
		.getSpecimenCollectionGroupCollection();
		if (specimenCollectionGroupCollection != null)
		{
			if (!specimenCollectionGroupCollection.isEmpty())
			{
				final Iterator<SpecimenCollectionGroup> specimenCollectionGroupIterator = specimenCollectionGroupCollection
				.iterator();
				while (specimenCollectionGroupIterator.hasNext())
				{
					final SpecimenCollectionGroup specimenCollectionGroup = specimenCollectionGroupIterator
					.next();
					final Integer offsetFromSCG = specimenCollectionGroup.getOffset();
					if (offsetFromSCG != null)
					{
						if (offsetFromSCG.intValue() != 0)
						{
							offset = offset + offsetFromSCG.intValue();
						}
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#getIdofCPR(edu.wustl.dao.DAO, edu.wustl.common.beans.SessionDataBean, edu.wustl.catissuecore.domain.CollectionProtocolRegistration)
	 */
	@Override
	public Long getIdofCPR(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration) throws BizLogicException
			{
		Long identifier = null;

		try
		{
			if (collectionProtocolRegistration.getCollectionProtocol() != null
					&& collectionProtocolRegistration.getParticipant().getId() != null)
			{
				final Long parentCpId = collectionProtocolRegistration.getCollectionProtocol()
				.getId();
				if (parentCpId != null)
				{
					// get the previous cp's offset if present.
					final String hql = "select  cpr.id from "
						+ CollectionProtocolRegistration.class.getName()
						+ " as cpr where cpr.collectionProtocol.id = " + parentCpId.toString()
						+ " and cpr.participant.id = "
						+ collectionProtocolRegistration.getParticipant().getId().toString();
					List idList = null;

					idList = dao.executeQuery(hql);

					if (idList != null && !idList.isEmpty())
					{
						for (int i = 0; i < idList.size(); i++)
						{
							identifier = (Long) idList.get(i);
							if (identifier != null)
							{
								return identifier;
							}
						}
					}
				}
			}
		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		return identifier;
			}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#changeStatusOfEvents(edu.wustl.dao.DAO, edu.wustl.common.beans.SessionDataBean, edu.wustl.catissuecore.domain.CollectionProtocolRegistration)
	 */
	@Override
	public void changeStatusOfEvents(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration) throws BizLogicException
			{
		try
		{
			final Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection = collectionProtocolRegistration
			.getSpecimenCollectionGroupCollection();
			if (specimenCollectionGroupCollection!=null && !specimenCollectionGroupCollection.isEmpty())
			{
				final Iterator<SpecimenCollectionGroup> specimenCollectionGroupIterator = specimenCollectionGroupCollection
				.iterator();
				while (specimenCollectionGroupIterator.hasNext())
				{
					final SpecimenCollectionGroup specimenCollectionGroup = specimenCollectionGroupIterator
					.next();
					boolean status = false;
					final Collection<Specimen> specimenCollection = specimenCollectionGroup
					.getSpecimenCollection();
					if (specimenCollection!=null && !specimenCollection.isEmpty())
					{
						final Iterator<Specimen> specimenIterator = specimenCollection.iterator();
						while (specimenIterator.hasNext())
						{
							final Specimen specimen = specimenIterator.next();
							final String collectionStatus = specimen.getCollectionStatus();
							if (!(collectionStatus.equalsIgnoreCase("Pending")))
							{
								status = true;
							}
						}
						if (status == false)
						{
							specimenCollectionGroup.setCollectionStatus("Not Collected");
							dao.update(specimenCollectionGroup);
						}
					}
				}
			}
		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
			}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#checkForChildStatus(edu.wustl.dao.DAO, edu.wustl.common.beans.SessionDataBean, edu.wustl.catissuecore.domain.CollectionProtocolRegistration)
	 */
	@Override
	public void checkForChildStatus(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration) throws BizLogicException
			{
		final String status = Constants.CHILD_STATUS;
		this.getCpAndCpr(dao, sessionDataBean, collectionProtocolRegistration, status);
			}

	/**
	 * @param dao
	 * @param sessionDataBean
	 * @param collProt
	 * @param cpr
	 * @throws BizLogicException
	 */
	private boolean checkChildStatus(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocol collProt, CollectionProtocolRegistration cpr)
	throws BizLogicException
	{
		this.changeStatusOfEvents(dao, sessionDataBean, cpr);
		if (collProt.getChildCollectionProtocolCollection() != null
				&& collProt.getChildCollectionProtocolCollection().size() != 0)
		{
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#checkAndUpdateChildDate(edu.wustl.dao.DAO, edu.wustl.common.beans.SessionDataBean, edu.wustl.catissuecore.domain.CollectionProtocolRegistration)
	 */
	@Override
	public void checkAndUpdateChildDate(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration) throws BizLogicException
			{
		final String status = Constants.CHILD_DATE;
		this.getCpAndCpr(dao, sessionDataBean, collectionProtocolRegistration, status);
			}

	/**
	 * @param dao
	 * @param sessionDataBean
	 * @param collectionProtocolRegistration
	 * @throws BizLogicException
	 */
	private void getCpAndCpr(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration, String callMethod)
	throws BizLogicException
	{
		try
		{
			final CollectionProtocol parent = collectionProtocolRegistration
			.getCollectionProtocol();
			final List<CollectionProtocol> childCPColl = this.getChildColl(parent);
			if (childCPColl != null && !childCPColl.isEmpty())
			{
				final Iterator<CollectionProtocol> iteratorofchildCP = childCPColl.iterator();
				while (iteratorofchildCP.hasNext())
				{
					final CollectionProtocol collectionProtocol = iteratorofchildCP.next();
					if (collectionProtocol != null)
					{
						final CollectionProtocolRegistration cpr = this
						.getCPRbyCollectionProtocolIDAndParticipantID(dao, collectionProtocol.getId(),
								collectionProtocolRegistration.getParticipant().getId());
						if (cpr != null)
						{
							boolean callRecursive = false;
							if (callMethod.equals(Constants.CHILD_STATUS))
							{
								callRecursive = this
								.checkChildStatus(dao, sessionDataBean, collectionProtocol, cpr);
							}
							else if (callMethod.equals(Constants.CHILD_DATE))
							{
								callRecursive = this.checkUpdateChildDate(dao, sessionDataBean,
										collectionProtocolRegistration, collectionProtocol, cpr);
							}
							else if (callMethod.equals(Constants.CHILD_OFFSET))
							{
								callRecursive = this.checkUpdateChildOffset(dao,
										offset, collectionProtocol, cpr);
							}
							if (callRecursive)
							{
								this.getCpAndCpr(dao, sessionDataBean,
										collectionProtocolRegistration, callMethod);
							}
						}
					}
				}
			}
		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * @param dao
	 * @param sessionDataBean
	 * @param collectionProtocolRegistration
	 * @param collProt
	 * @param cpr
	 * @throws BizLogicException

	 */
	private boolean checkUpdateChildDate(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration, CollectionProtocol collProt,
			CollectionProtocolRegistration cpr) throws BizLogicException, DAOException
			{
		this.setRegDate(cpr, collProt.getStudyCalendarEventPoint(), collectionProtocolRegistration
				.getRegistrationDate());
		final Integer offsetOfCurrentCPR = cpr.getOffset();
		{
			if (offsetOfCurrentCPR != null)
			{
				cpr.setOffset(offsetOfCurrentCPR);
				cpr.setRegistrationDate(AppUtility.getNewDateByAdditionOfDays(cpr
						.getRegistrationDate(), offsetOfCurrentCPR.intValue()));
			}
		}
		dao.update(cpr);
		//updateOffsetForEventsForAlreadyRegisteredCPR(dao, sessionDataBean, cpr);
		if (collProt.getChildCollectionProtocolCollection() != null
				&& collProt.getChildCollectionProtocolCollection().size() != 0)
		{
			return true;
			//checkAndUpdateChildDate(dao, sessionDataBean, cpr);
		}
		return false;
			}

	private List<CollectionProtocol> getChildColl(CollectionProtocol parent)
	{
		final Collection<CollectionProtocol> childCPcollection = parent
		.getChildCollectionProtocolCollection();
		final List<CollectionProtocol> childCPColl = new ArrayList<CollectionProtocol>();
		if (childCPcollection != null
				&& childCPcollection.size() != 0)
		{
			childCPColl.addAll(childCPcollection);
			final CollectionProtocolSeqComprator seqComp = new CollectionProtocolSeqComprator();
			java.util.Collections.sort(childCPColl, seqComp);
		}

		return childCPColl;
	}

	private Date getImmediateParentCPdate(Long maincpId, Long participantId)
	throws BizLogicException
	{
		Date regDate = null;
		try
		{
			final String hql1 = "select cpr.registrationDate from "
				+ CollectionProtocolRegistration.class.getName()
				+ " as cpr where cpr.collectionProtocol.id = " + maincpId.toString()
				+ " and cpr.participant.id = " + participantId.toString();
			final DAO dao = this.openDAOSession(null);
			final List dateList = dao.executeQuery(hql1);
			if (dateList != null && !dateList.isEmpty())
			{
				regDate = (Date) dateList.get(0);
			}
			this.closeDAOSession(dao);
		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return regDate;
	}

	/**The main parent(Collection Protocol) of the Collection Protocol is returned through this method.
	 * the main parent is the topmost level collection Protocol in the hierarchy
	 * @param protocol CP whose main parent is to be found out
	 * @return Main parent collection Protocol
	 */

	private CollectionProtocol getMainParentCP(CollectionProtocol protocol)
	{
		CollectionProtocol collectionProtocol = protocol;

		if (protocol != null)
		{
			// If cp's parent cp is null means this cp is the parent cp.
			if (protocol.getParentCollectionProtocol() == null)
			{
				return protocol;
			}
			else
			{
				collectionProtocol = this.getMainParentCP(protocol.getParentCollectionProtocol());
			}
		}
		return collectionProtocol;

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#insertCPR(edu.wustl.catissuecore.domain.CollectionProtocolRegistration, edu.wustl.dao.DAO, edu.wustl.common.beans.SessionDataBean, edu.wustl.catissuecore.uiobject.CollectionProtocolRegistrationUIObject)
	 */
	@Override
	public void insertCPR(CollectionProtocolRegistration collectionProtocolRegistration, DAO dao,
			SessionDataBean sessionDataBean,CollectionProtocolRegistrationUIObject cprUIObject) throws BizLogicException
			{
		try
		{
			boolean reportLoaderFlag = false;
			if (Constants.REGISTRATION_FOR_REPORT_LOADER.equals(collectionProtocolRegistration
					.getProtocolParticipantIdentifier()))
			{
				reportLoaderFlag = true;
				collectionProtocolRegistration.setProtocolParticipantIdentifier(null);
			}

			dao.insert(collectionProtocolRegistration);
			insertSCG(dao, sessionDataBean, collectionProtocolRegistration);
			if (armFound == false)
			{
				/*if(collectionProtocolRegistration.getIsToInsertAnticipatorySCGs()==null)
				{
					collectionProtocolRegistration.setIsToInsertAnticipatorySCGs(Boolean.TRUE);
				}*/
				if (reportLoaderFlag == false &&
						collectionProtocolRegistration.getIsToInsertAnticipatorySCGs())//collectionProtocolRegistration.getIsToInsertAnticipatorySCGs())
				{
					this.createSCG(collectionProtocolRegistration, dao, sessionDataBean);
					this.chkForChildCP(collectionProtocolRegistration, dao, sessionDataBean,cprUIObject);
				}
			}
		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
			}
	
	private void insertSCG(DAO dao, SessionDataBean sessionDataBean, CollectionProtocolRegistration cpr)
	throws BizLogicException
	{
		final Collection<SpecimenCollectionGroup> scgCollection = cpr.getSpecimenCollectionGroupCollection();
		SpecimenCollectionGroupBizLogic scgBizLogic = new SpecimenCollectionGroupBizLogic();
		if(scgCollection!=null)
		{
			for(SpecimenCollectionGroup scg:scgCollection)
			{
				if(scg!=null)
				{
					scg.setCollectionProtocolRegistration(cpr);
					if(scgBizLogic.validate(scg, dao, Constants.ADD))
					{
						scgBizLogic.insert(scg, dao, sessionDataBean);
					}	
				}	
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#chkForChildCP(edu.wustl.catissuecore.domain.CollectionProtocolRegistration, edu.wustl.dao.DAO, edu.wustl.common.beans.SessionDataBean, edu.wustl.catissuecore.uiobject.CollectionProtocolRegistrationUIObject)
	 */

	@Override
	public void chkForChildCP(CollectionProtocolRegistration cpr, DAO dao,
			SessionDataBean sessionDataBean,CollectionProtocolRegistrationUIObject cprUIObject) throws BizLogicException
			{
		final CollectionProtocol parentCP = cpr.getCollectionProtocol();
		final Date dateofCP = cpr.getRegistrationDate();
		final List childCPColl = this.getChildColl(parentCP);

		if (childCPColl != null && !childCPColl.isEmpty())
		{
			final Iterator itr = childCPColl.iterator();
			while (itr.hasNext())
			{
				final CollectionProtocol protocol = (CollectionProtocol) itr.next();
				if (protocol != null && protocol.getSequenceNumber() != null)
				{
					if (armFound == false)
					{
						if (Constants.ARM_CP_TYPE.equalsIgnoreCase(protocol.getType()))
						{
							armFound = true;
						}
						else
						{

							final CollectionProtocolRegistration cloneCPR = this.createCloneOfCPR(
									cpr, protocol);
							this.setRegDate(cloneCPR, protocol.getStudyCalendarEventPoint(), dateofCP);
							this.insertCPR(cloneCPR, dao, sessionDataBean,cprUIObject);
						}
					}
				}
			}
		}
			}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#setRegDate(edu.wustl.catissuecore.domain.CollectionProtocolRegistration, java.lang.Double, java.util.Date)
	 */
	@Override
	public void setRegDate(CollectionProtocolRegistration cpr, Double studyeventpointCalendar,
			Date dateofCP)
	{
		if (studyeventpointCalendar == null)
		{
			/**
			 * If studyeventpointCalendar of CollecttionProtocol is null then
			 * take the RegistrationDate of last Event.
			 */
			this.cntOfStudyCalEventPnt += 1;
			cpr.setRegistrationDate(AppUtility.getNewDateByAdditionOfDays(this.dateOfLastEvent,
					this.cntOfStudyCalEventPnt));
		}
		else
		{
			cpr.setRegistrationDate(AppUtility.getNewDateByAdditionOfDays(dateofCP,
					studyeventpointCalendar.intValue()));

		}

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#createCloneOfCPR(edu.wustl.catissuecore.domain.CollectionProtocolRegistration, edu.wustl.catissuecore.domain.CollectionProtocol)
	 */
	@Override
	public CollectionProtocolRegistration createCloneOfCPR(CollectionProtocolRegistration cpr,
			CollectionProtocol collProtocol)
	{
		InstanceFactory<CollectionProtocolRegistration> instFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocolRegistration.class);
		final CollectionProtocolRegistration cloneCPR = instFact.createClone(cpr);
		cloneCPR.setCollectionProtocol(collProtocol);
		return cloneCPR;

	}

	/**
	 * insert Consent Tiers.
	 * @param consentTierResponseCollection
	 * @param dao
	 * @param sessionDataBean
	 * @throws BizLogicException
	 * @throws DAOException
	 */
	private void insertConsentTiers(final Collection consentTierResponseCollection, final DAO dao,
			final SessionDataBean sessionDataBean) throws BizLogicException, DAOException
			{
		if (consentTierResponseCollection != null)
		{
			final Iterator itr = consentTierResponseCollection.iterator();
			while (itr.hasNext())
			{
				final ConsentTierResponse consentTierResponse = (ConsentTierResponse) itr.next();
				if (consentTierResponse.getConsentTier() != null)
				{
					dao.insert(consentTierResponse.getConsentTier());
				}

			}
		}
			}

	/**
	 * userID.
	 */
	private Long userID;

	private void createSCG(CollectionProtocolRegistration collectionProtocolRegistration, DAO dao,
			SessionDataBean sessionDataBean) throws BizLogicException
			{
		this.registerParticipantAndProtocol(dao, collectionProtocolRegistration);
		this.dateOfLastEvent = collectionProtocolRegistration.getRegistrationDate();
		this.cntOfStudyCalEventPnt = 0;

		final SpecimenCollectionGroupBizLogic specimenBizLogic = new SpecimenCollectionGroupBizLogic();
		final Collection<CollectionProtocolEvent> collectionProtocolEventCollection = collectionProtocolRegistration
		.getCollectionProtocol().getCollectionProtocolEventCollection();
		final Iterator<CollectionProtocolEvent> collectionProtocolEventIterator = collectionProtocolEventCollection
		.iterator();
		final Collection<SpecimenCollectionGroup> scgCollection = new HashSet<SpecimenCollectionGroup>();
		while (collectionProtocolEventIterator.hasNext())
		{
			final CollectionProtocolEvent collectionProtocolEvent = collectionProtocolEventIterator
			.next();

			if(collectionProtocolEvent.getStudyCalendarEventPoint() != null)
			{
				final int tmpCntOfStudyCalEventPnt = collectionProtocolEvent
				.getStudyCalendarEventPoint().intValue();
				if (this.cntOfStudyCalEventPnt != 0)
				{
					if (tmpCntOfStudyCalEventPnt > this.cntOfStudyCalEventPnt)
					{
						this.cntOfStudyCalEventPnt = tmpCntOfStudyCalEventPnt;
					}
				}
				if (this.cntOfStudyCalEventPnt == 0)
				{
					this.cntOfStudyCalEventPnt = tmpCntOfStudyCalEventPnt;
				}
			}

			/**
			 * Here countOfStudyCalendarEventPoint for previous
			 * CollectionProtocol which is registered is incremented as per
			 * StudyCalendarEventPoint of Events.
			 */

			SpecimenCollectionGroup specimenCollectionGroup = SpecimenCollectionGroupUtility.createSCGFromCollProtEvent(collectionProtocolEvent);
			//new SpecimenCollectionGroup(
			//collectionProtocolEvent);
			specimenCollectionGroup
			.setCollectionProtocolRegistration(collectionProtocolRegistration);
			specimenCollectionGroup=SpecimenCollectionGroupUtility.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration, specimenCollectionGroup);
			//specimenCollectionGroup
			//.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration);

			specimenBizLogic.insert(specimenCollectionGroup, dao, sessionDataBean);

			scgCollection.add(specimenCollectionGroup);
		}
		collectionProtocolRegistration.setSpecimenCollectionGroupCollection(scgCollection);

			}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#postInsert(java.lang.Object, edu.wustl.dao.DAO, edu.wustl.common.beans.SessionDataBean)
	 */
	@Override
	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		super.postInsert(obj, dao, sessionDataBean);
		// Commented by Geeta for removing the CP
		//ParticipantRegistrationCacheManager participantRegCacheManager = new ParticipantRegistrationCacheManager();
		//participantRegCacheManager.registerParticipant(collectionProtocolRegistration.getCollectionProtocol().getId(), collectionProtocolRegistration.getParticipant().getId(), collectionProtocolRegistration.getProtocolParticipantIdentifier());
	}


	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		ParticipantUIObject partUiObject= new ParticipantUIObject();
		update(dao, obj, oldObj,partUiObject, sessionDataBean);
	}
	/**
	 * Updates the persistent object in the database.
	 * @param obj
	 *            The object to be updated.
	 * @param session session
	 *            The session in which the object is saved.
	 * @throws BizLogicException bizLogicException
	 */
	@Override
	protected void update(DAO dao, Object obj, Object oldObj,Object uiObject, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		try
		{
			ParticipantUIObject partUIObject= (ParticipantUIObject)uiObject;
			final CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
			final CollectionProtocolRegistration oldCollectionProtocolRegistration = (CollectionProtocolRegistration) oldObj;
			final CollectionProtocol objCollectionProtocol = collectionProtocolRegistration.getCollectionProtocol();
			final String ppi = collectionProtocolRegistration.getProtocolParticipantIdentifier();
			final String oldPPI = oldCollectionProtocolRegistration.getProtocolParticipantIdentifier();
			CollectionProtocolRegistration persistentCPR = null;
			final Object object = dao.retrieveById(CollectionProtocolRegistration.class.getName(),
					oldCollectionProtocolRegistration.getId());
			if (object != null)
			{
				persistentCPR = (CollectionProtocolRegistration) object;
			}
			CollectionProtocolRegistrationUIObject cprUIObject = new CollectionProtocolRegistrationUIObject();
			Map<Long, CollectionProtocolRegistrationUIObject> cprCollection = null;
			if(partUIObject != null)
			{
				cprCollection = partUIObject.getCprUIObject();
			}
			
			if(cprCollection != null)
			{
				cprUIObject = (CollectionProtocolRegistrationUIObject)(
					(partUIObject.getCprUIObject()).get(objCollectionProtocol.getId()));
			}
			// Check for different Collection Protocol
			if (!collectionProtocolRegistration.getCollectionProtocol().getId().equals(
					oldCollectionProtocolRegistration.getCollectionProtocol().getId()))
			{
				this.checkStatus(dao, collectionProtocolRegistration.getCollectionProtocol(),
				"Collection Protocol");
			}
			// -- Check for different Participants and closed participant
			// old and new values are not null
			if (collectionProtocolRegistration.getParticipant() != null
					&& oldCollectionProtocolRegistration.getParticipant() != null
					&& collectionProtocolRegistration.getParticipant().getId() != null
					&& oldCollectionProtocolRegistration.getParticipant().getId() != null)
			{
				if (!collectionProtocolRegistration.getParticipant().getId().equals(
						oldCollectionProtocolRegistration.getParticipant().getId()))
				{

					this.checkStatus(dao, collectionProtocolRegistration.getParticipant(),
					"Participant");
				}
			}
			// when old participant is null and new is not null
			if (collectionProtocolRegistration.getParticipant() != null
					&& oldCollectionProtocolRegistration.getParticipant() == null)
			{
				if (collectionProtocolRegistration.getParticipant().getId() != null)
				{

					this.checkStatus(dao, collectionProtocolRegistration.getParticipant(),
					"Participant");
				}
			}
			/**
			 * Case: While updating the registration if the participant is
			 * deselected then we need to maintain the link between registration and
			 * participant by adding a dummy participant for query module.
			 */
			if (collectionProtocolRegistration.getParticipant() == null)
			{
				final Participant oldParticipant = oldCollectionProtocolRegistration
				.getParticipant();

				// Check for if the older participant was also a dummy, if true use
				// the same participant,
				// otherwise create an another dummay participant
				if (oldParticipant == null)
				{
					// create dummy participant.
					final Participant participant = this.addDummyParticipant(dao);
					persistentCPR.setParticipant(participant);


				} // oldpart != null
				else
				{

					final String firstName = CommonUtilities
					.toString(oldParticipant.getFirstName());
					final String lastName = CommonUtilities.toString(oldParticipant.getLastName());
					final String birthDate = CommonUtilities
					.toString(oldParticipant.getBirthDate());
					final String ssn = CommonUtilities.toString(oldParticipant
							.getSocialSecurityNumber());
					if (firstName.trim().length() == 0 && lastName.trim().length() == 0
							&& birthDate.trim().length() == 0 && ssn.trim().length() == 0)
					{
						persistentCPR.setParticipant(oldParticipant);
					}
					else
					{
						// create dummy participant.
						final Participant participant = this.addDummyParticipant(dao);
						persistentCPR.setParticipant(participant);
					}
				}
			}

			boolean isUnique = true;
			if(!ppi.equals(oldPPI))
			{
				isUnique = this.checkUniqueConstraint(dao, objCollectionProtocol.getId(),ppi);
			}
			if(!isUnique)
			{
				final ErrorKey errorKey = ErrorKey.getErrorKey("Err.ConstraintViolation");
				throw new BizLogicException(errorKey, null,
				"Participant:Participant Protocol ID within this Collection Protocol");
			}

			if(cprUIObject.getConsentWithdrawalOption()==null)
			{
				cprUIObject.setConsentWithdrawalOption(Constants.WITHDRAW_RESPONSE_NOACTION);
			}
			// Mandar 22-Jan-07 To disable consents accordingly in SCG and
			// Specimen(s) start
			if (!cprUIObject.getConsentWithdrawalOption().equalsIgnoreCase(
					Constants.WITHDRAW_RESPONSE_NOACTION))
			{
				this.verifyAndUpdateConsentWithdrawn(collectionProtocolRegistration,
						oldCollectionProtocolRegistration, dao, sessionDataBean,cprUIObject);
			}

			/*lazy change */

			/*Collection specimenCollectionGroupCollection =
			 	(Collection) dao.retrieveAttribute(CollectionProtocolRegistration.
			 	class.getName(), collectionProtocolRegistration.getId(),
			 	Constants.COLUMN_NAME_SCG_COLL);
				collectionProtocolRegistration.
				setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);
			updateConsentResponseForSCG(collectionProtocolRegistration, dao, sessionDataBean);*/
			final Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection = persistentCPR
			.getSpecimenCollectionGroupCollection();
			collectionProtocolRegistration
			.setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);
			this.updateConsentResponseForSCG(collectionProtocolRegistration,oldCollectionProtocolRegistration, dao);
			persistentCPR.setConsentTierResponseCollection(collectionProtocolRegistration
					.getConsentTierResponseCollection());

			setConsetResponseCollection(dao, collectionProtocolRegistration,
					persistentCPR);


			persistentCPR.setConsentWitness(collectionProtocolRegistration.getConsentWitness());
			persistentCPR.setConsentSignatureDate(collectionProtocolRegistration
					.getConsentSignatureDate());
			persistentCPR.setSignedConsentDocumentURL(collectionProtocolRegistration
					.getSignedConsentDocumentURL());
			persistentCPR.setProtocolParticipantIdentifier(collectionProtocolRegistration
					.getProtocolParticipantIdentifier());
			persistentCPR.setRegistrationDate(collectionProtocolRegistration.getRegistrationDate());
			persistentCPR.setActivityStatus(collectionProtocolRegistration.getActivityStatus());
			persistentCPR.setBarcode(AppUtility.handleEmptyStrings(collectionProtocolRegistration.getBarcode()));
			persistentCPR.setSpecimenCollectionGroupCollection(collectionProtocolRegistration
					.getSpecimenCollectionGroupCollection());
			persistentCPR.setParticipant(collectionProtocolRegistration.getParticipant());
			/* for offset 27th Dec 2007 */
			// Check if Offset is present.If it is present then all the below
			// hierarchy protocols are shifted according to the Offset.
			//Integer offsetOld=oldCollectionProtocolRegistration.getOffset();
			final Integer offsetOld = oldCollectionProtocolRegistration.getOffset();
			final Integer offsetNew = collectionProtocolRegistration.getOffset();
			if (offsetNew != null)
			{
				int offset = 0;
				if (offsetOld != null)
				{
					offset = offsetNew.intValue() - offsetOld.intValue();
				}
				else
				{
					offset = offsetNew.intValue() - 0;
				}
				if (offset != 0)
				{
					//updateOffsetForEvents(dao, sessionDataBean,
					//collectionProtocolRegistration, offset);
					this.checkAndUpdateChildOffset(dao, sessionDataBean,
							oldCollectionProtocolRegistration, offset);
					this.updateForOffset(dao, sessionDataBean, oldCollectionProtocolRegistration,
							offset);
				}

			}

			if (!collectionProtocolRegistration.getCollectionProtocol().getId().equals(0L)
					&& !persistentCPR.getCollectionProtocol().getId().equals(
							collectionProtocolRegistration.getCollectionProtocol().getId()))
			{
				persistentCPR.setCollectionProtocol(collectionProtocolRegistration
						.getCollectionProtocol());
			}
			/* offset changes end */
			// Mandar 22-Jan-07 To disable consents accordingly in SCG and
			// Specimen(s) end
			// Update registration
			dao.update(persistentCPR,oldCollectionProtocolRegistration);

			// Disable all specimen Collection group under this registration.
			this.LOGGER.debug("collectionProtocolRegistration.getActivityStatus() "
					+ collectionProtocolRegistration.getActivityStatus());
			if (cprUIObject.getConsentWithdrawalOption().equalsIgnoreCase(
					Constants.WITHDRAW_RESPONSE_RETURN)
					|| cprUIObject.getConsentWithdrawalOption()
					.equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_DISCARD))
			{
				this.LOGGER.debug("collectionProtocolRegistration.getActivityStatus() "
						+ collectionProtocolRegistration.getActivityStatus());
				final Long collectionProtocolRegistrationIDArr[] = {collectionProtocolRegistration
						.getId()};

				final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				final SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) factory
				.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
				bizLogic.disableRelatedObjects(dao, collectionProtocolRegistrationIDArr);
			}
		}
		catch (final DAOException daoExp)
		{
			this.LOGGER.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			this.LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	private void setConsetResponseCollection(
			DAO dao,
			final CollectionProtocolRegistration collectionProtocolRegistration,
			CollectionProtocolRegistration persistentCPR)
	throws BizLogicException {
		try
		{
			Collection<ConsentTierResponse> consentTierResponseColl=collectionProtocolRegistration
			.getConsentTierResponseCollection();
			if(consentTierResponseColl!=null &&consentTierResponseColl.iterator().hasNext()){
				/*
				 *  To check that the collection is lazily initialized
				 *  iterating the collection is required.
				 */
				consentTierResponseColl.iterator().next();
			}

			persistentCPR.setConsentTierResponseCollection(collectionProtocolRegistration
					.getConsentTierResponseCollection());

		}
		catch (org.hibernate.LazyInitializationException e) {
			/*
			 * for #15570
			 *
			 */
			final Collection<ConsentTierResponse> consentTierResponseCollection = (Collection<ConsentTierResponse>) this.retrieveAttribute(
					dao, CollectionProtocolRegistration.class, collectionProtocolRegistration.getId(),
			"elements(consentTierResponseCollection)");
			persistentCPR.setConsentTierResponseCollection(consentTierResponseCollection);

		}
	}

	/**
	 * @param collectionProtocolRegistration.
	 * @param dao
	 * @param sessionDataBean
	 * @throws BizLogicException
	 * @throws DAOException
	 */
	private void updateConsentResponseForSCG(
			CollectionProtocolRegistration collectionProtocolRegistration,
			CollectionProtocolRegistration oldCollectionProtocolRegistration, DAO dao
	) throws BizLogicException, DAOException
	{
		/*Collection specimenCollectionGroupCollection = (Collection)
		 * dao.retrieveAttribute(CollectionProtocolRegistration.class.getName(),
			collectionProtocolRegistration.getId(), Constants.COLUMN_NAME_SCG_COLL);*/
		final Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection = collectionProtocolRegistration
		.getSpecimenCollectionGroupCollection();
		if(specimenCollectionGroupCollection!=null)
		{
			final Iterator<SpecimenCollectionGroup> specimenCollectionGroupIterator = specimenCollectionGroupCollection
			.iterator();
			while (specimenCollectionGroupIterator.hasNext())
			{
				SpecimenCollectionGroup specimenCollectionGroup = specimenCollectionGroupIterator
				.next();
				//specimenCollectionGroup
				//.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration);
				specimenCollectionGroup=SpecimenCollectionGroupUtility.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration, specimenCollectionGroup);

				SpecimenCollectionGroup oldSpecimenCollectionGroup = getOldSpecimenCollectionGroup
				(specimenCollectionGroup.getId(),oldCollectionProtocolRegistration.getSpecimenCollectionGroupCollection());

				final Collection<Specimen> specimenCollection = specimenCollectionGroup
				.getSpecimenCollection();
				if (specimenCollection != null && !specimenCollection.isEmpty())
				{
					final Iterator<Specimen> itSpecimenCollection = specimenCollection.iterator();
					while (itSpecimenCollection.hasNext())
					{
						final Specimen specimen = itSpecimenCollection.next();
						Specimen oldSpecimen = getOldSpecimen(specimen.getId(),specimenCollectionGroup
								.getSpecimenCollection());
						SpecimenUtility.setConsentTierStatusCollectionFromSCG(specimen, specimenCollectionGroup);
						//specimen.setConsentTierStatusCollectionFromSCG(specimenCollectionGroup);
						dao.update(specimen,oldSpecimen);
					}
				}

				dao.update(specimenCollectionGroup,oldSpecimenCollectionGroup);
			}
		}
	}

	/**
	 * This method will be called and return the Old specimen collection group.
	 * @param scgId specimen collection group Id
	 * @param specimenCollectionGroupCollection SCG collection.
	 * @return Specimen collection group.
	 */
	private SpecimenCollectionGroup getOldSpecimenCollectionGroup(Long scgId,
			Collection<SpecimenCollectionGroup>specimenCollectionGroupCollection)
	{
		SpecimenCollectionGroup oldSpecimenCollectionGroup = null;


		Iterator<SpecimenCollectionGroup> itr = specimenCollectionGroupCollection.iterator();

		while(itr.hasNext())
		{
			SpecimenCollectionGroup specimenCollectionGroup = itr.next();
			if(scgId.equals(specimenCollectionGroup.getId()))
			{
				oldSpecimenCollectionGroup = specimenCollectionGroup;
				break;
			}
		}
		return oldSpecimenCollectionGroup;

	}

	/**
	 * This method will be called to get old specimen.
	 * @param specimenId Specimen Id.
	 * @param specimenCollection specimen collection.
	 * @return Specimen.
	 */
	private Specimen getOldSpecimen(Long specimenId,
			Collection<Specimen>specimenCollection)
	{
		Specimen oldSpecimen = null;


		Iterator<Specimen> itr = specimenCollection.iterator();

		while(itr.hasNext())
		{
			Specimen specimen = itr.next();
			if(specimenId.equals(specimen.getId()))
			{
				oldSpecimen = specimen;
				break;
			}
		}
		return oldSpecimen;

	}


	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#postUpdate(edu.wustl.dao.DAO, java.lang.Object, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	@Override
	public void postUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException

			{
		//ParticipantRegistrationCacheManager participantRegCacheManager =
		//new ParticipantRegistrationCacheManager();
		final CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) currentObj;
		final CollectionProtocolRegistration oldCollectionProtocolRegistration = (CollectionProtocolRegistration) oldObj;
		String oldProtocolParticipantId = null;
		if(oldCollectionProtocolRegistration!=null)
		{
			oldProtocolParticipantId= oldCollectionProtocolRegistration.getProtocolParticipantIdentifier();
		}

		if (oldProtocolParticipantId == null)
		{
			oldProtocolParticipantId = "";
		}

		String newProtocolParticipantId = collectionProtocolRegistration
		.getProtocolParticipantIdentifier();

		if (newProtocolParticipantId == null)
		{
			newProtocolParticipantId = "";
		}

		// Commented by Geeta for removing teh CP
		/*
			if (oldCPId.longValue() != newCPId.longValue() || oldParticipantId.longValue() != newParticipantId.longValue()
					|| !oldProtocolParticipantId.equals(newProtocolParticipantId))
			{
				participantRegCacheManager.deRegisterParticipant(oldCPId, oldParticipantId, oldProtocolParticipantId);
				participantRegCacheManager.registerParticipant(newCPId, newParticipantId, newProtocolParticipantId);
			}

			if (collectionProtocolRegistration.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.toString()))
			{
				participantRegCacheManager.deRegisterParticipant(newCPId, newParticipantId, newProtocolParticipantId);
			}
		 */
		super.postUpdate(dao, currentObj, oldObj, sessionDataBean);
			}

	private String[] getDynamicGroups(AbstractDomainObject obj) throws BizLogicException
	{
		try
		{
			String[] dynamicGroups = null;
			final CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
			dynamicGroups = new String[1];
			dynamicGroups[0] = CSMGroupLocator.getInstance().getPGName(
					collectionProtocolRegistration.getCollectionProtocol().getId(),
					Class.forName("edu.wustl.catissuecore.domain.CollectionProtocol"));
			return dynamicGroups;
		}
		catch (final ApplicationException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		catch (final ClassNotFoundException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, "class.notFound.exp", "");
		}

	}

	private void registerParticipantAndProtocol(DAO dao,
			CollectionProtocolRegistration collectionProtocolRegistration
	) throws BizLogicException
	{
		try
		{
			final Object collectionProtocolObj = dao.retrieveById(CollectionProtocol.class
					.getName(), collectionProtocolRegistration.getCollectionProtocol().getId());

			if (collectionProtocolObj != null)
			{
				final CollectionProtocol collectionProtocol = (CollectionProtocol) collectionProtocolObj;
				collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);
			}
		}
		catch (final DAOException exp)
		{
			LOGGER.error(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
	}

	/**
	 * Add a dummy participant when participant is registed to a protocol using
	 * participant protocol id.
	 * @throws AuditException
	 */
	private Participant addDummyParticipant(DAO dao)
	throws BizLogicException, DAOException, AuditException
	{
		InstanceFactory<Participant> partiInstFact = DomainInstanceFactory.getInstanceFactory(Participant.class);
		final Participant participant = partiInstFact.createObject();

		participant.setLastName("");
		participant.setFirstName("");
		participant.setMiddleName("");
		participant.setSocialSecurityNumber(null);
		participant.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());

		// Create a dummy participant medical identifier.
		final Set<ParticipantMedicalIdentifier> partMedIdentifierColl = new HashSet<ParticipantMedicalIdentifier>();
		InstanceFactory<ParticipantMedicalIdentifier> instFact = DomainInstanceFactory.getInstanceFactory(ParticipantMedicalIdentifier.class);
		final ParticipantMedicalIdentifier partMedIdentifier = instFact.createObject();//new ParticipantMedicalIdentifier();
		partMedIdentifier.setMedicalRecordNumber(null);
		partMedIdentifier.setSite(null);
		partMedIdentifierColl.add(partMedIdentifier);
		dao.insert(participant);

		partMedIdentifier.setParticipant(participant);
		dao.insert(partMedIdentifier);
		return participant;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#disableRelatedObjectsForParticipant(edu.wustl.dao.DAO, java.lang.Long[])
	 */
	@Override
	public void disableRelatedObjectsForParticipant(DAO dao, Long participantIDArr[])
	throws BizLogicException
	{
		final List<Long> listOfSubElement = super.disableObjects(dao,
				CollectionProtocolRegistration.class, "participant", "CATISSUE_COLL_PROT_REG",
				"PARTICIPANT_ID", participantIDArr);
		if (!listOfSubElement.isEmpty())
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) factory
			.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.disableRelatedObjects(dao, CommonUtilities.toLongArray(listOfSubElement));
		}
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#disableRelatedObjectsForCollectionProtocol(edu.wustl.dao.DAO, java.lang.Long[])
	 */
	@Override
	public void disableRelatedObjectsForCollectionProtocol(DAO dao, Long collectionProtocolIDArr[])
	throws BizLogicException
	{
		final List<Long> listOfSubElement = super.disableObjects(dao,
				CollectionProtocolRegistration.class, "collectionProtocol",
				"CATISSUE_COLL_PROT_REG", "COLLECTION_PROTOCOL_ID", collectionProtocolIDArr);
		if (!listOfSubElement.isEmpty())
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) factory
			.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.disableRelatedObjects(dao, CommonUtilities.toLongArray(listOfSubElement));
		}
	}

	/**
	 * @param dao
	 * @param objectIds
	 * @param assignToUser
	 * @param roleId
	 * @throws BizLogicException
	 * @throws SMException
	 */
	/*
		public void assignPrivilegeToRelatedObjectsForParticipant(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId,
				boolean assignToUser, boolean assignOperation) throws SMException, DAOException
		{
			List listOfSubElement = super.getRelatedObjects(dao, CollectionProtocolRegistration.class, "participant", objectIds);

			if (!listOfSubElement.isEmpty())
			{
				super.setPrivilege(dao, privilegeName, CollectionProtocolRegistration.class, Utility.toLongArray(listOfSubElement), userId, roleId,
						assignToUser, assignOperation);
				SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
						Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
				bizLogic.assignPrivilegeToRelatedObjects(dao, privilegeName, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
						assignOperation);
			}
		}*/

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class,
	 *      Long[], Long, String, boolean)
	 * @param dao
	 * @param privilegeName
	 * @param objectIds
	 * @param userId
	 * @param roleId
	 * @param assignToUser
	 * @throws SMException
	 * @throws BizLogicException
	 */
	/*
		public void assignPrivilegeToRelatedObjectsForCP(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId,
				boolean assignToUser, boolean assignOperation) throws SMException, DAOException
		{
			List listOfSubElement = super.getRelatedObjects(dao, CollectionProtocolRegistration.class, "collectionProtocol", objectIds);
			if (!listOfSubElement.isEmpty())
			{
				super.setPrivilege(dao, privilegeName, CollectionProtocolRegistration.class, Utility.toLongArray(listOfSubElement), userId, roleId,
						assignToUser, assignOperation);
				SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
						Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
				bizLogic.assignPrivilegeToRelatedObjects(dao, privilegeName, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
						assignOperation);

				ParticipantBizLogic participantBizLogic = (ParticipantBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.PARTICIPANT_FORM_ID);
				participantBizLogic.assignPrivilegeToRelatedObjectsForCPR(dao, privilegeName, Utility.toLongArray(listOfSubElement), userId, roleId,
						assignToUser, assignOperation);
			}
		}*/

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class,
	 *      Long[], Long, String, boolean)
	 */
	/*
		public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser,
				boolean assignOperation) throws SMException, DAOException
		{
			super.setPrivilege(dao, privilegeName, objectType, objectIds, userId, roleId, assignToUser, assignOperation);

			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.assignPrivilegeToRelatedObjects(dao, privilegeName, objectIds, userId, roleId, assignToUser, assignOperation);

			ParticipantBizLogic participantBizLogic = (ParticipantBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.PARTICIPANT_FORM_ID);
			participantBizLogic.assignPrivilegeToRelatedObjectsForCPR(dao, privilegeName, objectIds, userId, roleId, assignToUser, assignOperation);

		}*/

	/**
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		final CollectionProtocolRegistration registration = (CollectionProtocolRegistration) obj;

		/**
		 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of Api
		 * Search, previoulsy it was failing since there was default class level
		 * initialization on domain object. For example in User object, it was
		 * initialized as protected String lastName=""; So we removed default
		 * class level initialization on domain object and are initializing in
		 * method setAllValues() of domain object. But in case of Api Search,
		 * default values will not get set since setAllValues() method of
		 * domainObject will not get called. To avoid null pointer exception, we
		 * are setting the default values same as we were setting in
		 * setAllValues() method of domainObject.
		 */
		ApiSearchUtil.setCollectionProtocolRegistrationDefault(registration);
		// End:- Change for API Search

		// Added by Ashish
		if (registration == null)
		{
			throw this.getBizLogicException(null, "domain.object.null.err.msg",
			"Collection Protocol Registration");
		}
		final Validator validator = new Validator();
		String message = "";
		if (registration.getCollectionProtocol() == null
				|| registration.getCollectionProtocol().getId() == null)
		{
			message = ApplicationProperties
			.getValue("collectionprotocolregistration.protocoltitle");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}

		final String errorKey = validator.validateDate(CommonUtilities.parseDateToString(
				registration.getRegistrationDate(), CommonServiceLocator.getInstance()
				.getDatePattern()), true);
		if (errorKey.trim().length() > 0)
		{
			message = ApplicationProperties.getValue("collectionprotocolregistration.date");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}

		if (Validator.isEmpty(registration.getProtocolParticipantIdentifier()))
		{
			if (registration.getParticipant() == null
					|| registration.getParticipant().getId() == null)
			{
				throw this.getBizLogicException(null,
						"errors.collectionprotocolregistration.atleast", "");
			}
		}
		// if (checkedButton == true)
		// {
		/*
		 * if (registration.getParticipant() == null ||
		 * registration.getParticipant().getId() == null) { message =
		 * ApplicationProperties.getValue("collectionProtocolReg.participantName");
		 * throw new
		 * DAOException(ApplicationProperties.getValue("errors.item.required",message)); }
		 */
		// } // name selected
		// else
		// {
		/*
		 * if
		 * (validator.isEmpty(registration.getParticipant().getId().toString())) {
		 * String message =
		 * ApplicationProperties.getValue("collectionProtocolReg.participantProtocolID");
		 * throw new DAOException("errors.item.required", new
		 * String[]{message}); } // } // date validation according to bug id
		 * 707, 722 and 730 String errorKey =
		 * validator.validateDate(Utility.parseDateToString(registration.getRegistrationDate(),Constants.DATE_PATTERN_MM_DD_YYYY),true );
		 * if(errorKey.trim().length() >0 ) { String message =
		 * ApplicationProperties.getValue("collectionprotocolregistration.date");
		 * throw new DAOException("errors.item.required", new
		 * String[]{message}); } // if
		 * (!validator.isValidOption(registration.getActivityStatus())) { String
		 * message =
		 * ApplicationProperties.getValue("collectionprotocolregistration.activityStatus");
		 * throw new DAOException("errors.item.required", new
		 * String[]{message}); }
		 */
		// End
		if (operation.equals(Constants.ADD))
		{
			if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(registration.getActivityStatus()))
			{
				throw this.getBizLogicException(null, "activityStatus.active.errMsg", "");
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, registration
					.getActivityStatus()))
			{
				throw this.getBizLogicException(null, "activityStatus.errMsg", "");
			}
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#checkUniqueConstraint(edu.wustl.dao.DAO, long, java.lang.String)
	 */
	@Override
	public boolean checkUniqueConstraint(DAO dao, long cpId, String ppi) throws BizLogicException
	{
		boolean isUnique = true;
		if(ppi == null || (ppi != null && "".equals(ppi)))
		{
			return isUnique;
		}
		final String query = "select count(*) from edu.wustl.catissuecore.domain.CollectionProtocolRegistration where "
			+ "protocolParticipantIdentifier = '"+ppi+"' and collectionProtocol.id = "+cpId;
		try
		{
			final List list = dao.executeQuery(query);
			int count = 0;
			if (list.isEmpty())
			{
				LOGGER.debug("Unique Constraint Passed");
			}
			else
			{
				count = ((Integer) list.get(0)).intValue();
				if(count > 0)
				{
					LOGGER.debug("Unique Constraint Violated: ");
					isUnique = false;
				}

			}

		} catch (DAOException daoExp) {
			LOGGER.debug(daoExp.getMessage(), daoExp);
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return isUnique;
	}

	/**
	 * Name: Vijay Pande Reviewer Name: Sachin Lale Bug id: 4477 Method updated
	 * since earlier implemetation was not including CP having no registerd
	 * participant. Also short title is also fetched from DB.
	 */
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#getAllParticipantRegistrationInfo()
	 */
	@Override
	public List<ParticipantRegistrationInfo> getAllParticipantRegistrationInfo() throws BizLogicException
	{
		final List<ParticipantRegistrationInfo> participantRegistrationInfoList = new Vector<ParticipantRegistrationInfo>();
		// Getting all the CollectionProtocol those do not have activaityStatus
		// as 'Disabled'.
		String hql = this.getHql();
		DAO dao = null;
		try
		{
			dao = this.openDAOSession(null);
			final List list = dao.executeQuery(hql);
			LOGGER.info("list size -------------:" + list.size());
			// Iterating over each Collection Protocol and finding out all its registerd participant
			if (list != null)
			{
				for (int i = 0; i < list.size(); i++)
				{
					// Getitng participants for a particular CP.
					final Object[] object = (Object[]) list.get(i);
					final Long cpId = (Long) object[0];
					final String cpTitle = (String) object[1];
					final String cpShortTitle = (String) object[2];

					// Getting all active participant registered with CP
					hql = "select p.id, cpr.protocolParticipantIdentifier from "
						+ CollectionProtocolRegistration.class.getName()
						+ " as cpr right outer join cpr.participant as p"
						+ " where cpr.participant.id = p.id"
						+ " and cpr.collectionProtocol.id = " + cpId
						+ " and cpr.activityStatus != '"
						+ Status.ACTIVITY_STATUS_DISABLED.toString()
						+ "' and p.activityStatus != '"
						+ Status.ACTIVITY_STATUS_DISABLED.toString() + "' order by p.id";

					final List participantList = dao.executeQuery(hql);

					final List<String> participantInfoList = new ArrayList<String>();
					// If registered participant found then add them to
					// participantInfoList
					for (int j = 0; j < participantList.size(); j++)
					{
						final Object[] participantObj = (Object[]) participantList.get(j);

						final Long participantID = (Long) participantObj[0];
						final String protocolParticipantId = (String) participantObj[1];

						if (participantID != null)
						{
							String participantInfo = participantID.toString() + ":";
							if (protocolParticipantId != null && !protocolParticipantId.equals(""))
							{
								participantInfo = participantInfo + protocolParticipantId;
							}
							participantInfoList.add(participantInfo);

						}
					}

					// Creating ParticipanrRegistrationInfo object and storing in a
					// vector participantRegistrationInfoList.
					final ParticipantRegistrationInfo prInfo = new ParticipantRegistrationInfo();
					prInfo.setCpId(cpId);
					prInfo.setCpTitle(cpTitle);
					prInfo.setCpShortTitle(cpShortTitle);
					prInfo.setParticipantInfoCollection(participantInfoList);
					participantRegistrationInfoList.add(prInfo);
				}
			}

		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return participantRegistrationInfoList;
	}

	/**
	 * @return
	 */
	private String getHql()
	{
		final String hql = "select cp.id ,cp.title, cp.shortTitle from "
			+ CollectionProtocol.class.getName() + " as cp where  cp.activityStatus != '"
			+ Status.ACTIVITY_STATUS_DISABLED.toString() + "' and  (cp." + Constants.CP_TYPE
			+ "= '" + Constants.PARENT_CP_TYPE + "' or cp.type = null or cp.type = '')";
		return hql;
	}

	// Mandar : 11-Jan-07 For Consent Tracking Withdrawal -------- start
	/*
	 * verifyAndUpdateConsentWithdrawn(collectionProtocolRegistration)
	 * updateSCG(collectionProtocolRegistration, consentTierResponse)
	 */

	/*
	 * This method verifies and updates SCG and child elements for withdrawn
	 * consents
	 */
	private void verifyAndUpdateConsentWithdrawn(
			CollectionProtocolRegistration collectionProtocolRegistration,
			CollectionProtocolRegistration oldCollectionProtocolRegistration, DAO dao,
			SessionDataBean sessionDataBean,CollectionProtocolRegistrationUIObject cprUiObject) throws BizLogicException
			{
		final Collection<ConsentTierResponse> newConsentTierResponseCollection = collectionProtocolRegistration
		.getConsentTierResponseCollection();
		if(newConsentTierResponseCollection!=null && !newConsentTierResponseCollection.isEmpty())
		{
			final Iterator<ConsentTierResponse> itr = newConsentTierResponseCollection.iterator();
			while (itr.hasNext())
			{
				final ConsentTierResponse consentTierResponse = itr.next();
				if (consentTierResponse.getResponse().equalsIgnoreCase(Constants.WITHDRAWN))
				{
					final long consentTierID = consentTierResponse.getConsentTier().getId().longValue();
					this.updateSCG(collectionProtocolRegistration, oldCollectionProtocolRegistration,
							consentTierID, dao, sessionDataBean,cprUiObject);
				}
			}
		}
			}

	/*
	 * This method updates all the scg's associated with the selected
	 * collectionprotocolregistration.
	 */
	private void updateSCG(CollectionProtocolRegistration collectionProtocolRegistration,
			CollectionProtocolRegistration oldCollectionProtocolRegistration, long consentTierID,
			DAO dao, SessionDataBean sessionDataBean,CollectionProtocolRegistrationUIObject cprUiObject) throws BizLogicException
			{

		final Collection<SpecimenCollectionGroup> newScgCollection = new HashSet<SpecimenCollectionGroup>();
		final Collection<SpecimenCollectionGroup> scgCollection = oldCollectionProtocolRegistration
		.getSpecimenCollectionGroupCollection();
		if(scgCollection!=null)
		{
			final Iterator<SpecimenCollectionGroup> scgItr = scgCollection.iterator();
			try
			{
				while (scgItr.hasNext())
				{
					final SpecimenCollectionGroup scg = scgItr.next();
					final String cprWithdrawOption = cprUiObject.getConsentWithdrawalOption();

					ConsentUtil.updateSCG(scg, consentTierID, cprWithdrawOption, dao, sessionDataBean);

					newScgCollection.add(scg); // set updated scg in cpr
				}
				collectionProtocolRegistration.setSpecimenCollectionGroupCollection(newScgCollection);
			}
			catch (final ApplicationException e)
			{
				LOGGER.error(e.getMessage(), e);
				throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
			}
		}
			}

	// Mandar : 11-Jan-07 For Consent Tracking Withdrawal -------- end

	/* offset changes 27th Dec 2007 */

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#updateForOffset(edu.wustl.dao.DAO, edu.wustl.common.beans.SessionDataBean, edu.wustl.catissuecore.domain.CollectionProtocolRegistration, int)
	 */
	@Override
	public void updateForOffset(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration, int offset)
	throws BizLogicException
	{
		CollectionProtocol child = collectionProtocolRegistration.getCollectionProtocol();
		final Integer sequenceNumber = child.getSequenceNumber();
		final CollectionProtocol parentCPofArm = child.getParentCollectionProtocol();

		try
		{
			if (parentCPofArm != null)
			{
				final List childCPColl = this.getChildColl(parentCPofArm);
				final Iterator iteratorofchildCP = childCPColl.iterator();
				while (iteratorofchildCP.hasNext())
				{
					final CollectionProtocol protocol = (CollectionProtocol) iteratorofchildCP.next();
					if (protocol != null && protocol.getSequenceNumber() != null)
					{
						if (sequenceNumber != null && protocol.getSequenceNumber().intValue() > sequenceNumber.intValue())
						{
							CollectionProtocolRegistration cpr;

							cpr = this.getCPRbyCollectionProtocolIDAndParticipantID(dao,
									protocol.getId(), collectionProtocolRegistration.getParticipant()
									.getId());

							if (cpr != null)
							{
								cpr.setRegistrationDate(AppUtility.getNewDateByAdditionOfDays(cpr
										.getRegistrationDate(), offset));
								dao.update(cpr);
								this.checkAndUpdateChildOffset(dao, sessionDataBean, cpr, offset);
							}
						}

					}
				}

			}
			if (parentCPofArm != null)
			{
				child = parentCPofArm;
				final CollectionProtocolRegistration cprforParent = this
				.getCPRbyCollectionProtocolIDAndParticipantID(dao, child.getId(),
						collectionProtocolRegistration.getParticipant().getId());
				this.updateForOffset(dao, sessionDataBean, cprforParent, offset);
			}

		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#checkAndUpdateChildOffset(edu.wustl.dao.DAO, edu.wustl.common.beans.SessionDataBean, edu.wustl.catissuecore.domain.CollectionProtocolRegistration, int)
	 */
	@Override
	public void checkAndUpdateChildOffset(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration, int offset)
	throws BizLogicException
	{
		final String status = Constants.CHILD_OFFSET;
		this.getCpAndCpr(dao, sessionDataBean, collectionProtocolRegistration, status);
	}

	/**
	 * @param dao
	 * @param sessionDataBean
	 * @param offset
	 * @param protocol
	 * @param cpr
	 * @throws BizLogicException

	 */
	private boolean checkUpdateChildOffset(DAO dao, int offset,
			CollectionProtocol protocol, CollectionProtocolRegistration cpr) throws BizLogicException,
			DAOException
			{
		cpr.setRegistrationDate(AppUtility.getNewDateByAdditionOfDays(cpr.getRegistrationDate(),
				offset));
		dao.update(cpr);
		if (protocol.getChildCollectionProtocolCollection() != null
				&& protocol.getChildCollectionProtocolCollection().size() != 0)
		{
			return true;
		}
		return false;
			}

	/**
	 * This method is called to fetch the CollectionProtocolRegistration Object
	 * from the database for the specified Collection Protocol Id and the
	 * specified Participant Id.
	 * @param dao
	 *            The DAO object
	 * @param CpId
	 *            The CollectionProtocol Id
	 * @param ParticipantId
	 *            the Participant Id
	 * @return CollectionProtocolRegistration The CollectionProtocolRegistration
	 *         Object retrieved from Database
	 * @throws BizLogicException
	 *             DAOException
	 */
	private CollectionProtocolRegistration getCPRbyCollectionProtocolIDAndParticipantID(DAO dao,
			Long CpId, Long ParticipantId) throws BizLogicException
			{
		final String[] selectColumName = null;
		CollectionProtocolRegistration collectionProtocolRegistrationretrieve = null;
		try
		{

			final String sourceObjectName = CollectionProtocolRegistration.class.getName();

			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(
					new EqualClause("collectionProtocol." + Constants.SYSTEM_IDENTIFIER, CpId))
					.andOpr().addCondition(
							new EqualClause("participant." + Constants.SYSTEM_IDENTIFIER,
									ParticipantId));

			final List list = dao.retrieve(sourceObjectName, selectColumName, queryWhereClause);
			if (!list.isEmpty())
			{
				collectionProtocolRegistrationretrieve = (CollectionProtocolRegistration) list
				.get(0);
			}
		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return collectionProtocolRegistrationretrieve;

			}

	/**
	 * This method is called when offset is specified for the CollectionProtocol
	 * This method shifts the anticipated dates for events by the number of
	 * offset days specified.
	 * @param dao
	 *            The DAO object
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param collectionProtocol
	 *            The CollectionProtocol Object
	 * @param offset
	 *            Offset value of number of days
	 * @throws BizLogicException
	 *             DAOException
	 */

	//	private void updateOffsetForEvents(DAO dao, SessionDataBean sessionDataBean, CollectionProtocolRegistration collectionProtocolRegistration,
	//			int offset) throws UserNotAuthorizedException, DAOException
	//	{
	//		/*Collection specimenCollectionGroupCollection = (Collection) dao.retrieveAttribute(CollectionProtocolRegistration.class.getName(),
	//				collectionProtocolRegistration.getId(), Constants.COLUMN_NAME_SCG_COLL);*/
	//		Collection specimenCollectionGroupCollection = collectionProtocolRegistration.getSpecimenCollectionGroupCollection();
	//
	//		if (!specimenCollectionGroupCollection.isEmpty())
	//		{
	//			Iterator specimenCollectionGroupIterator = specimenCollectionGroupCollection.iterator();
	//			while (specimenCollectionGroupIterator.hasNext())
	//			{
	//				SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) specimenCollectionGroupIterator.next();
	//				Integer offsetToSet = specimenCollectionGroup.getOffset();
	//				if (offsetToSet != null && offsetToSet.intValue() != 0)
	//				{
	//					specimenCollectionGroup.setOffset(new Integer(offset + offsetToSet.intValue()));
	//				}
	//				else
	//					specimenCollectionGroup.setOffset(new Integer(offset));
	//				dao.update(specimenCollectionGroup, sessionDataBean, true, true, false);
	//			}
	//		}
	//
	//	}
	/*private Integer getOffsetFromPreviousSeqNoCP(DAO dao, SessionDataBean sessionDataBean, CollectionProtocol cp, Long participantId)
			throws BizLogicException, ClassNotFoundException
	{
		Integer offset = null;
		if (cp != null && participantId != null)
		{
			Long parentCpId = cp.getId();
			if (parentCpId != null)
			{
				// get the previous cp's offset if present.
				String hql = "select  cpr.offset from " + CollectionProtocolRegistration.class.getName()
						+ " as cpr where cpr.collectionProtocol.parentCollectionProtocol.id = " + parentCpId.toString()
						+ " and cpr.participant.id = " + participantId.toString() + " order by cpr.collectionProtocol.sequenceNumber desc";
				List offsetList = dao.executeQuery(hql);
				if (offsetList != null && !offsetList.isEmpty())
				{
					for (int i = 0; i < offsetList.size(); i++)
					{
						offset = (Integer) offsetList.get(i);
						if (offset != null)
							return offset;
					}
				}
			}

		}

		return offset;
	}*/
	/* offset changes finish */

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#isReadDeniedTobeChecked()
	 */
	@Override
	public boolean isReadDeniedTobeChecked()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#getReadDeniedPrivilegeName()
	 */
	@Override
	public String getReadDeniedPrivilegeName()
	{
		return Permissions.REGISTRATION + "," + Permissions.READ_DENIED;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#hasPrivilegeToView(java.lang.String, java.lang.Long, edu.wustl.common.beans.SessionDataBean)
	 */
	@Override
	public boolean hasPrivilegeToView(String objName, Long identifier,
			SessionDataBean sessionDataBean)
	{
		return AppUtility.hasPrivilegeToView(objName, identifier, sessionDataBean, this
				.getReadDeniedPrivilegeName());
	}

	/**
	 * @param CollectionProtocolRegistration Object
	 * @throws BizLogicException Database related Exception
	 */
	private void generateProtocolParticipantIdentifierLabel(
			CollectionProtocolRegistration collectionProtocolRegistration) throws BizLogicException
			{
		/**
		 * Call Protocol Participant Identifier label generator if automatic generation is specified
		 */
		if (edu.wustl.catissuecore.util.global.Variables.isProtocolParticipantIdentifierLabelGeneratorAvl)
		{
			try
			{
				final TaskTimeCalculater labelGen = TaskTimeCalculater.startTask(
						"Time required for label Generator", CollectionProtocolRegistration.class);
				final LabelGenerator spLblGenerator = LabelGeneratorFactory
				.getInstance(Constants.PROTOCOL_PARTICIPANT_IDENTIFIER_LABEL_GENERATOR_PROPERTY_NAME);
				spLblGenerator.setLabel(collectionProtocolRegistration);
				TaskTimeCalculater.endTask(labelGen);
			}
			catch (final NameGeneratorException e)
			{
				LOGGER.error(e.getMessage(), e);
				throw this.getBizLogicException(e, "name.generator.exp", "");
			}

		}
		else
		{
			if (collectionProtocolRegistration.getProtocolParticipantIdentifier() == null)
			{
				collectionProtocolRegistration.setProtocolParticipantIdentifier(null);
			}
		}
			}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.ICollectionProtocolRegistrationBizLogic#getCollectionProtocolBeanList()
	 */
	@Override
	public List<NameValueBean> getCollectionProtocolBeanList() throws BizLogicException
	{
		final List<NameValueBean> participantRegistrationBeanList = new Vector<NameValueBean>();

		// Getting all the CollectionProtocol those do not have activaityStatus
		// as 'Disabled'.
		final String hql = this.getHql();
		DAO dao = null;
		try
		{
			dao = this.openDAOSession(null);
			final List list = dao.executeQuery(hql);
			LOGGER.info("list size -----------:" + list.size());

			// Iterating over each Collection Protocol and finding out all its
			// registerd participant
			if (list != null)
			{
				for (int i = 0; i < list.size(); i++)
				{
					// Getitng participants for a particular CP.
					final Object[] obj = (Object[]) list.get(i);
					final Long cpId = (Long) obj[0];
					final String cpShortTitle = (String) obj[2];
					final NameValueBean cpDetails = new NameValueBean(cpShortTitle, cpId);
					participantRegistrationBeanList.add(cpDetails);
				}
			}
		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return participantRegistrationBeanList;
	}

	@Override
	public CollectionProtocolRegistration getRegistrationByGridId(String gridId) throws BizLogicException {
		DAO dao = null;
		CollectionProtocolRegistration cpr = null;
		try {
			dao = this.openDAOSession(null);
			final String partQueryStr = "from " + CollectionProtocolRegistration.class.getName()
					+ " where gridId ='" + StringEscapeUtils.escapeSql(gridId)
					+ "'";
			final List<CollectionProtocolRegistration> listOfParticipants = dao
					.executeQuery(partQueryStr);
			if (CollectionUtils.isNotEmpty(listOfParticipants)) {
				cpr = listOfParticipants.get(0);
			}
		} catch (final DAOException e) {
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(),
					e.getMsgValues());
		} finally {
			this.closeDAOSession(dao);
		}
		return cpr;
	}

	@Override
	public CollectionProtocolRegistration findRegistration(
			CollectionProtocol cp, Participant p) throws BizLogicException {
		CollectionProtocolRegistration cpr = null;
		if (cp!=null && p!=null && cp.getId() != null && p.getId() != null) {
			DAO dao = null;
			try {
				dao = this.openDAOSession(null);
				cpr = getCPRbyCollectionProtocolIDAndParticipantID(dao,
						cp.getId(), p.getId());
				new ParticipantBizLogic().init(cpr);
			} finally {
				this.closeDAOSession(dao);
			}
		}
		return cpr;
	}

	@Override
	public void validateCPR(CollectionProtocolRegistration cpr) throws BizLogicException {
		DAO dao = null;
		try {
			dao = this.openDAOSession(null);
			validateCPR(dao, cpr, cpr.getCollectionProtocol(), cpr.getProtocolParticipantIdentifier());
		} finally {
			this.closeDAOSession(dao);
		}
	}

	@Override
	public void saveOrUpdateRegistration(CollectionProtocolRegistration cpr,
			String userName) throws BizLogicException {
		if (cpr.getId() != null) {
			updateRegistration(cpr, userName);
		} else {
			saveRegistration(cpr, userName);
		}
	}

	private void saveRegistration(CollectionProtocolRegistration cpr,
			String userName) throws BizLogicException {

		Participant p = cpr.getParticipant();
		if (p.getCollectionProtocolRegistrationCollection()==null) {
			p.setCollectionProtocolRegistrationCollection(new ArrayList<CollectionProtocolRegistration>());
		}
		p.getCollectionProtocolRegistrationCollection().add(cpr);

		CollectionProtocol cp = cpr.getCollectionProtocol();
		if (cp.getCollectionProtocolRegistrationCollection()==null) {
			cp.setCollectionProtocolRegistrationCollection(new ArrayList<CollectionProtocolRegistration>());
		}
		cp.getCollectionProtocolRegistrationCollection().add(cpr);

		HibernateDAO hibernateDao = null;
		try {
			final String appName = this.getAppName();
			hibernateDao = (HibernateDAO) DAOConfigFactory.getInstance()
					.getDAOFactory(appName).getDAO();
			hibernateDao.openSession(null);

			participantBizLogic.registerToCPR(hibernateDao, AppUtility.getSessionDataBean(userName),p,cpr);
			hibernateDao.commit();

		} catch (final DAOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new BizLogicException(
					ErrorKey.getErrorKey("common.errors.item"), e,	"Error while opening the session");
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new BizLogicException(
					ErrorKey.getErrorKey("common.errors.item"), e,
					"Error while opening the session");
		} finally {
			try {
				hibernateDao.closeSession();
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * @param cpr
	 * @param userName
	 * @throws BizLogicException
	 */
	private void updateRegistration(CollectionProtocolRegistration cpr,
			String userName) throws BizLogicException {
		HibernateDAO hibernateDao = null;
		try {
			final String appName = this.getAppName();
			hibernateDao = (HibernateDAO) DAOConfigFactory.getInstance()
					.getDAOFactory(appName).getDAO();
			hibernateDao.openSession(null);
			AbstractDomainObject abstractDomainOld;
			abstractDomainOld = (AbstractDomainObject) hibernateDao
					.retrieveById(
							CollectionProtocolRegistration.class.getName(),
							cpr.getId());
			this.update(cpr, abstractDomainOld,
					AppUtility.getSessionDataBean(userName));
		} catch (final DAOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new BizLogicException(
					ErrorKey.getErrorKey("common.errors.item"), e,
					"Error while opening the session");
		} finally {
			try {
				hibernateDao.closeSession();
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
				throw new BizLogicException(
						ErrorKey.getErrorKey("common.errors.item"), e,
						"Failed while updating ");
			}
		}
	}

	/**
	 * @return the genericDAO
	 */
	public GenericDAO getGenericDAO() {
		return genericDAO;
	}

	/**
	 * @param genericDAO the genericDAO to set
	 */
	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	/**
	 * @return the participantBizLogic
	 */
	public IParticipantBizLogic getParticipantBizLogic() {
		return participantBizLogic;
	}

	/**
	 * @param participantBizLogic the participantBizLogic to set
	 */
	public void setParticipantBizLogic(IParticipantBizLogic participantBizLogic) {
		this.participantBizLogic = participantBizLogic;
	}
	
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		return isAuthorized( dao,  domainObject,  sessionDataBean,null);
	}
	
	protected String getPrivilegeKey(Object domainObject)
	{
		return "REGISTRATION";
	}
	
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean,Object uiObject)
	throws BizLogicException
	{
		
		boolean isAuthorized = false;//new ParticipantBizLogic().isAuthorized(dao, domainObject, sessionDataBean, uiObject);
		try
		{
			if (sessionDataBean != null && sessionDataBean.isAdmin())
			{
				return true;
			}

			final String privilegeName = this.getPrivilegeName(domainObject);
			final String protEltName = this.getObjectId(dao, domainObject);
			final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
					sessionDataBean.getUserName());

			if (protEltName.equals(Constants.ADD_GLOBAL_PARTICIPANT))
			{
				User user = null;
				try
				{
					user = (User) dao.retrieveById(User.class.getName(), sessionDataBean
							.getUserId());
				}
				catch (final DAOException e)
				{
					LOGGER.error(e.getMessage(), e);
					e.printStackTrace() ;
				}
				final Collection<CollectionProtocol> cpCollection = user
				.getAssignedProtocolCollection();
				Long cpId = 0L;
				if (cpCollection != null && !cpCollection.isEmpty())
				{
					CollectionProtocolRegistration cpr = (CollectionProtocolRegistration)domainObject;
					if(cpr.getCollectionProtocol() != null)
					{
						cpId = cpr.getCollectionProtocol().getId();
					}
					if(cpId != 0)
					{
						if (privilegeCache.hasPrivilege(CollectionProtocol.class.getName() + "_"
								+ cpId, privilegeName))
						{
							isAuthorized = true;
//							break;
						}
					}
					else
					{
//						for (final CollectionProtocol cp : cpCollection)
//						{
//							if (privilegeCache.hasPrivilege(CollectionProtocol.class.getName() + "_"
//									+ cp.getId(), privilegeName))
//							{
//								isAuthorized = true;
//								break;
//							}
//						}
						if (!isAuthorized)
						{
							isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(privilegeName,
									sessionDataBean, null);
						}
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
				final String[] prArray = protEltName.split("_");
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
				if (!protEltName.equals(Constants.ADD_GLOBAL_PARTICIPANT))
				{
					final String protEltNames[] = protEltName.split("_");

					final Long cpId = Long.valueOf(protEltNames[1]);
					final Set<Long> cpIdSet = new UserBizLogic().getRelatedCPIds(sessionDataBean
							.getUserId(), false);

					if (cpIdSet.contains(cpId))
					{
						//bug 11611 and 11659
						throw AppUtility.getUserNotAuthorizedException(privilegeName,
								protEltName, domainObject.getClass().getSimpleName());
					}
					isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(privilegeName,
							sessionDataBean, protEltNames[1]);
				}
			}
			if (!isAuthorized)
			{
				//bug 11611 and 11659
				throw AppUtility.getUserNotAuthorizedException(privilegeName,
						protEltName, domainObject.getClass().getSimpleName());
			}
		}
		catch (final SMException e1)
		{
			LOGGER.error(e1.getMessage(), e1);
			e1.printStackTrace();
		}
		return isAuthorized;
	}
	
	public String getObjectId(DAO dao, Object domainObject) throws BizLogicException
	{
		final String objectId = Constants.ADD_GLOBAL_PARTICIPANT;

		if (domainObject instanceof Participant)
		{
			final Participant participant = (Participant) domainObject;
			final Collection<CollectionProtocolRegistration> cprCollection = participant
			.getCollectionProtocolRegistrationCollection();
			if (cprCollection != null && cprCollection.isEmpty())
			{
				return objectId;
			}

			else
			{
				final StringBuffer stringBuffer = new StringBuffer();
				boolean isNewCPRPresent = false;

				if (cprCollection != null && !cprCollection.isEmpty())
				{
					stringBuffer.append(Constants.COLLECTION_PROTOCOL_CLASS_NAME);
					for (final CollectionProtocolRegistration cpr : cprCollection)
					{
						if (cpr.getId() == null)
						{
							if (cpr.getCollectionProtocol() == null)
							{
								return objectId;
							}
							checkForCollectionProtocolIdentifier(dao, cpr);
							stringBuffer.append("_").append(cpr.getCollectionProtocol().getId());
							isNewCPRPresent = true;
						}
					}
				}
				if (isNewCPRPresent)
				{
					return stringBuffer.toString();
				}
			}
		}
		return objectId;
	}
	
	private void checkForCollectionProtocolIdentifier(
			DAO dao,
			final CollectionProtocolRegistration cprID)
	throws BizLogicException
	{
		try
		{
			final String cpTitle = cprID.
			getCollectionProtocol().getTitle();
			final String sourceObjectName = CollectionProtocol.class.getName();
			final String[] selectColumnName = {"id","activityStatus"};
			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			if(cprID.getCollectionProtocol().getId() != null)
			{
				queryWhereClause.addCondition(new EqualClause("id", cprID.getCollectionProtocol().getId()));
			}
			else if(cpTitle != null)
			{
				queryWhereClause.addCondition(new EqualClause("title", cpTitle));
			}
			final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
			if (!list.isEmpty())
			{
				Object objCP[] = (Object[])list.get(0);
				cprID.getCollectionProtocol().setId((Long)objCP[0]);
				cprID.getCollectionProtocol().setActivityStatus((String)objCP[1]);
				if(Constants.DISABLE.equals(cprID.getCollectionProtocol().getActivityStatus()))
				{
					throw this.getBizLogicException(null, "cp.disabled", "");
				}
			}
			else
			{
				throw this.getBizLogicException(null, "cp.nt.found", "");
			}
			final IFactory factory = AbstractFactoryConfig.getInstance()
			.getBizLogicFactory();
			final ParticipantBizLogic participantBizLogic = (ParticipantBizLogic) factory
			.getBizLogic(Constants.PARTICIPANT_FORM_ID);
			final Collection consentTierCollection = (Collection) participantBizLogic.retrieveAttribute(CollectionProtocol.class.getName(),
					cprID.getCollectionProtocol().getId(), "elements(consentTierCollection)");
			cprID.getCollectionProtocol().setConsentTierCollection(consentTierCollection);
		}
		catch (DAOException e)
		{
			throw this.getBizLogicException(null, "cp.nt.found", "");
		}
	}




}