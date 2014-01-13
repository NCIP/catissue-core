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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.TaskTimeCalculater;
import edu.wustl.catissuecore.bean.CpAndParticipentsBean;
import edu.wustl.catissuecore.dao.ConsentDAO;
import edu.wustl.catissuecore.dao.UserDAO;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.CpSyncAudit;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.namegenerator.BarcodeGenerator;
import edu.wustl.catissuecore.namegenerator.BarcodeGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
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
import edu.wustl.common.tokenprocessor.TokenManager;
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
import edu.wustl.dao.exception.AuditException;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.locator.CSMGroupLocator;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * @author kapil_kaveeshwar
 */
public class CollectionProtocolRegistrationBizLogic extends CatissueDefaultBizLogic
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

	/**
	 * Insert.
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		offset = 0;
		armFound = false;
		final CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
		final CollectionProtocol objCollectionProtocol = collectionProtocolRegistration.getCollectionProtocol();
		final String ppi = collectionProtocolRegistration.getProtocolParticipantIdentifier();
		try
		{
			// check for closed Collection Protocol
			this.checkStatus(dao, collectionProtocolRegistration.getCollectionProtocol(),
					"Collection Protocol");
			// Check for closed Participant
			this.checkStatus(dao, collectionProtocolRegistration.getParticipant(), "Participant");
			boolean isUnique = this.checkUniqueConstraint(dao, objCollectionProtocol.getId(), ppi);
			if(!isUnique)
			{
				final ErrorKey errorKey = ErrorKey.getErrorKey("Err.ConstraintViolation");
				throw new BizLogicException(errorKey, null,
						"Participant:Participant Protocol ID within this Collection Protocol");
			}
			Participant participant = null;
			if (collectionProtocolRegistration.getParticipant().getId() == null)
			{
				participant = this.addDummyParticipant(dao);
				/*final Object participantObj = dao.retrieveById(Participant.class.getName(),
						collectionProtocolRegistration.getParticipant().getId());
				if (participantObj != null)
				{
					participant = (Participant) participantObj;
				}*/
			}
			else
			{
				participant =(Participant) dao.retrieveById(collectionProtocolRegistration.getParticipant().getClass().getName(), collectionProtocolRegistration.getParticipant().getId());
				participant.getCollectionProtocolRegistrationCollection().add(collectionProtocolRegistration);
			}

			collectionProtocolRegistration.setParticipant(participant);
			final String barcode = collectionProtocolRegistration.getBarcode();
			this.generateCPRBarcode(collectionProtocolRegistration);
			//Label generator for Protocol Participant Identifier
			this.generateProtocolParticipantIdentifierLabel(collectionProtocolRegistration);

			if ((barcode != collectionProtocolRegistration.getBarcode()) && barcode != null)
			{
				collectionProtocolRegistration.setBarcode(barcode);
			}

			this.insertCPR(collectionProtocolRegistration, dao, sessionDataBean);
			if (armFound == false
					&& Constants.ARM_CP_TYPE.equals(collectionProtocolRegistration
							.getCollectionProtocol().getType()))
			{
				this.armCheckandRegistration(collectionProtocolRegistration, dao, sessionDataBean);
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
			SessionDataBean sessionDataBean) throws BizLogicException, DAOException
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
									sessionDataBean);
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
						parentCPofArm), dao, sessionDataBean);
			}
		}

	}

	/**This method is called for the protocols that are automatically registered
	 * after registration of an arm. In this
	 * method the total offset of upper level hierarchy up to that protocol is
	 * calculated for proper recalculation of the
	 * registration date.
	 * @param collectionProtocolRegistration
	 * CollectionProtocolRegistration Object for current CollectionProtocol
	 * @param dao The DAO object
	 * @param sessionDataBean  The session in which the object is saved.
	 * @throws BizLogicException
	 */
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

	/**
	 * @param collectionProtocol
	 * @param dao
	 * @param sessionDataBean
	 * @param participantId
	 * @param collectionProtocolToRegister
	 * @throws BizLogicException
	 */
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
					final List childCollectionCP = this.getChildColl(collectionProtocol);
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

	/**This method is called so as to calculate total offset of SCG for currentCPR.
	 * This method is called when an CP is automatically registered after an arm.
	 * All SCG's of upper level hierarchy which carry offset
	 * are added together for total offset,So that registration date is correct
	 * @param cpr the CollectionProtocol object which has SCG's
	 */
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

	/**The id of CPR is extracted from database with respect to
	 * CollectionProtocol id and Participant id.
	 * @param dao
	 * @param sessionDataBean
	 * @param collectionProtocolRegistration
	 * @return
	 * @throws BizLogicException
	 */
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

	/** The status of all Specimen Collection Group is changed when another arm is
	 *  registered if they are not collected.
	 * @param collectionProtocolRegistration
	 * The CollectionProtocolRegistration Object for
	 * currentCollectionProtocol.
	 * @param dao The DAO object
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws BizLogicException
	 */
	public void changeStatusOfEvents(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration) throws BizLogicException
	{
		try
		{
			final Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection = collectionProtocolRegistration
					.getSpecimenCollectionGroupCollection();
			if (!specimenCollectionGroupCollection.isEmpty())
			{
				final Iterator specimenCollectionGroupIterator = specimenCollectionGroupCollection
						.iterator();
				while (specimenCollectionGroupIterator.hasNext())
				{
					final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) specimenCollectionGroupIterator
							.next();
					boolean status = false;
					final Collection<Specimen> specimenCollection = specimenCollectionGroup
							.getSpecimenCollection();
					if (!specimenCollection.isEmpty())
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

	/**In this method the status of Specimen Collection Group of Child CollectionProtocol
	 * is changed if the previous arm has any child.
	 * The status is changed only when the Specimen is not collected
	 * @param collectionProtocolRegistration The CollectionProtocolRegistration Object
	 * for currentCollectionProtocol
	 * @param dao The DAO object
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws BizLogicException

	 */
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

	/**In this method if there is change in Offset of parent protocol then the offset of child
	 * CollectionProtocol.
	 * also changes.This is basically when upper level Hierarchy Protocol has an offset and below CP's
	 * are registered automatically.
	 * @param collectionProtocolRegistration The CollectionProtocolRegistration
	 * Object for currentCollectionProtocol
	 * @param dao The DAO object
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws BizLogicException bizLogicException
	 */
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
			final List childCPColl = this.getChildColl(parent);
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
		childCPColl.addAll(childCPcollection);
		final CollectionProtocolSeqComprator seqComp = new CollectionProtocolSeqComprator();
		java.util.Collections.sort(childCPColl, seqComp);
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

	public void insertCPR(CollectionProtocolRegistration collectionProtocolRegistration, DAO dao,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			checkConsents(collectionProtocolRegistration,dao);
			boolean reportLoaderFlag = false;
			if (Constants.REGISTRATION_FOR_REPORT_LOADER.equals(collectionProtocolRegistration
					.getProtocolParticipantIdentifier()))
			{
				reportLoaderFlag = true;
				collectionProtocolRegistration.setProtocolParticipantIdentifier(null);
			}

			dao.insert(collectionProtocolRegistration);

			if (armFound == false)
			{
				if (reportLoaderFlag == false &&
						collectionProtocolRegistration.getIsToInsertAnticipatorySCGs())
				{
					this.createSCG(collectionProtocolRegistration, dao, sessionDataBean);
					this.chkForChildCP(collectionProtocolRegistration, dao, sessionDataBean);
				}
			}
		}
		catch (final ApplicationException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	private void checkConsents(CollectionProtocolRegistration cpr, DAO dao) throws ApplicationException
	{
		Collection<ConsentTierResponse> responseColl = cpr.getConsentTierResponseCollection();
		ConsentDAO consentDAO = new ConsentDAO();
		Collection<ConsentTier> consentTierColection = consentDAO.getConsentTierFromCP((HibernateDAO)dao, cpr.getCollectionProtocol().getId());
		if((consentTierColection == null || consentTierColection.isEmpty()) && (responseColl != null && !responseColl.isEmpty()))
		{
			LOGGER.error("No Consents definition for the given CP.");
			throw this.getBizLogicException(null, "consent.definition.undefined", ""); 
		}
		Collection<ConsentTierResponse> defaultResponseColl = prepareConsentTierResponseCollection(consentTierColection);
		if(consentTierColection != null && !consentTierColection.isEmpty() && responseColl != null && !responseColl.isEmpty())
		{
			for (ConsentTierResponse consentTierResponse : defaultResponseColl)
			{
				for (ConsentTierResponse enteredResponse : responseColl)
				{
					if(consentTierResponse.getConsentTier().getStatement().equalsIgnoreCase(enteredResponse.getConsentTier().getStatement()) ||
							consentTierResponse.getConsentTier().getId().equals(enteredResponse.getConsentTier().getId()))
					{
						validateResponse(enteredResponse);
						consentTierResponse.setResponse(enteredResponse.getResponse());
						responseColl.remove(enteredResponse);
						break;
					}
				}
			}
			if(responseColl != null && !responseColl.isEmpty())
			{
				LOGGER.error("Invalid consent tier specieifed");
				throw this.getBizLogicException(null, "invalid.consentTier", "");
			}
		}
		cpr.setConsentTierResponseCollection(defaultResponseColl);
		User witness = cpr.getConsentWitness();
		if(witness != null)
		{
			if((witness.getId() == null || witness.getId() == 0l) && !Validator.isEmpty(witness.getLoginName()))
			{
				UserDAO userDAO = new UserDAO();
				witness.setId(userDAO.getUserIDFromLoginName((HibernateDAO)dao, witness.getLoginName(),Constants.ACTIVITY_STATUS_ACTIVE));
			}
		}
	}

	private Collection<ConsentTierResponse> prepareConsentTierResponseCollection(Collection consentTierCollection)
	{
		final Collection<ConsentTierResponse> consentTierResponseCollection = new HashSet<ConsentTierResponse>();
		if (consentTierCollection != null && !consentTierCollection.isEmpty())
		{
				final Iterator iter = consentTierCollection.iterator();
				while (iter.hasNext())
				{
					final ConsentTier consentTier = (ConsentTier) iter.next();
					final ConsentTierResponse consentTierResponse = new ConsentTierResponse();
					consentTierResponse.setResponse(Constants.NOT_SPECIFIED);
					consentTierResponse.setConsentTier(consentTier);
					consentTierResponseCollection.add(consentTierResponse);
				}
	}
		return consentTierResponseCollection;
	}
	private void validateResponse(ConsentTierResponse consentTierResponse) throws BizLogicException
	{
		List<String> responseValueList = AppUtility.getResponseList();
		if(Validator.isEmpty(consentTierResponse.getResponse()) || !responseValueList.contains(consentTierResponse.getResponse()))
		{
			LOGGER.error("Consent Response Can not be empty");
			throw this.getBizLogicException(null, "invalid.consents.response", "");
		}
	}

	private ConsentTier getConsentTierFromResponse(ConsentTierResponse consentTierResponse,DAO dao, Long cpId) throws ApplicationException
	{
		ConsentTier consentTier = consentTierResponse.getConsentTier();
		if(consentTier.getId() == null && !Validator.isEmpty(consentTier.getStatement()))
		{
//			String hql = "select con.id from "+ConsentTier.class.getName()+" con where con.statement='"+consentTier.getStatement()+"'";
//			List idList = dao.executeQuery(hql);
//			if(idList == null || idList.isEmpty())
//			{
//				LOGGER.error("Invalid Consent Details.");
//				throw this.getBizLogicException(null, "invalid.consents.response", "");
//			}
			ConsentDAO consentDAO = new ConsentDAO();
			consentTier.setId(consentDAO.getConsentIdFromStatement(consentTier.getStatement(),cpId));
		}
		return consentTier;
	}


	/** In this method if parent CP has any child which can be
	 * automatically registered,then these child are registered.
	 * @param cpr The CollectionProtocol Registration Object of current Collection Protocol
	 * @param dao The DAO object
	 * @param sessionDataBean the SessionDataBean
	 * @throws BizLogicException
	 */

	public void chkForChildCP(CollectionProtocolRegistration cpr, DAO dao,
			SessionDataBean sessionDataBean) throws BizLogicException
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
							this.insertCPR(cloneCPR, dao, sessionDataBean);
						}
					}
				}
			}
		}
	}

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

	/**
	 * create Clone Of CPR.
	 * @param cpr
	 * @param collProtocol
	 * @return
	 */
	public CollectionProtocolRegistration createCloneOfCPR(CollectionProtocolRegistration cpr,
			CollectionProtocol collProtocol)
	{
		final CollectionProtocolRegistration cloneCPR = new CollectionProtocolRegistration(cpr);
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
	private void insertConsentTiers(Collection consentTierResponseCollection, DAO dao,
			SessionDataBean sessionDataBean) throws BizLogicException, DAOException
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

		
		final Collection<CollectionProtocolEvent> collectionProtocolEventCollection = collectionProtocolRegistration
				.getCollectionProtocol().getCollectionProtocolEventCollection();
		SpecimenCollectionGroupBizLogic specimenCollectionGroupBizLogic=new SpecimenCollectionGroupBizLogic();
		Collection<SpecimenCollectionGroup> scgCollection =specimenCollectionGroupBizLogic.createSCGsForCPEs(collectionProtocolRegistration, dao, sessionDataBean,
				collectionProtocolEventCollection,cntOfStudyCalEventPnt);
		
		collectionProtocolRegistration.setSpecimenCollectionGroupCollection(scgCollection);

	}


	/**
	 * post Insert.
	 * @param obj
	 * @param dao
	 * @param sessionDataBean
	 * @throws BizLogicException bizLogicException
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

	/**
	 * Updates the persistent object in the database.
	 * @param obj
	 *            The object to be updated.
	 * @param session session
	 *            The session in which the object is saved.
	 * @throws BizLogicException bizLogicException
	 */
	@Override
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
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
			if(ppi!=null && !ppi.equals(oldPPI))
			{
				isUnique = this.checkUniqueConstraint(dao, objCollectionProtocol.getId(),ppi);
			}
			if(!isUnique)
			{
				final ErrorKey errorKey = ErrorKey.getErrorKey("Err.ConstraintViolation");
				throw new BizLogicException(errorKey, null,
				"Participant:Participant Protocol ID within this Collection Protocol");
			}

			// Mandar 22-Jan-07 To disable consents accordingly in SCG and
			// Specimen(s) start
			if (!collectionProtocolRegistration.getConsentWithdrawalOption().equalsIgnoreCase(
					Constants.WITHDRAW_RESPONSE_NOACTION))
			{
				this.verifyAndUpdateConsentWithdrawn(collectionProtocolRegistration,
						oldCollectionProtocolRegistration, dao, sessionDataBean);
			}

			/*lazy change */

			/*Collection specimenCollectionGroupCollection =
			 	(Collection) dao.retrieveAttribute(CollectionProtocolRegistration.
			 	class.getName(), collectionProtocolRegistration.getId(),
			 	Constants.COLUMN_NAME_SCG_COLL);
				collectionProtocolRegistration.
				setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);
			updateConsentResponseForSCG(collectionProtocolRegistration, dao, sessionDataBean);*/
			final Collection specimenCollectionGroupCollection = persistentCPR
					.getSpecimenCollectionGroupCollection();
			collectionProtocolRegistration
					.setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);
			 
			//persistentCPR.setConsentTierResponseCollection(collectionProtocolRegistration.getConsentTierResponseCollection());
			/*setConsetResponseCollection(dao, collectionProtocolRegistration,
					persistentCPR);
			this.updateConsentResponseForSCG(persistentCPR,oldCollectionProtocolRegistration, dao);
			persistentCPR.setConsentWitness(collectionProtocolRegistration.getConsentWitness());
			persistentCPR.setConsentSignatureDate(collectionProtocolRegistration
					.getConsentSignatureDate());
			persistentCPR.setSignedConsentDocumentURL(collectionProtocolRegistration
					.getSignedConsentDocumentURL());
		*/
			persistentCPR.setProtocolParticipantIdentifier(collectionProtocolRegistration
					.getProtocolParticipantIdentifier());
			persistentCPR.setRegistrationDate(collectionProtocolRegistration.getRegistrationDate());
			persistentCPR.setActivityStatus(collectionProtocolRegistration.getActivityStatus());
			persistentCPR.setBarcode(collectionProtocolRegistration.getBarcode());
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
			if (collectionProtocolRegistration.getConsentWithdrawalOption().equalsIgnoreCase(
					Constants.WITHDRAW_RESPONSE_RETURN)
					|| collectionProtocolRegistration.getConsentWithdrawalOption()
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
		
			Collection<ConsentTierResponse> consentTierResponseCollection=persistentCPR.getConsentTierResponseCollection();
			Collection<ConsentTierResponse> newConsentTierResponseCollection=collectionProtocolRegistration.getConsentTierResponseCollection();
			for (ConsentTierResponse newConsentTierResponse : newConsentTierResponseCollection) {
				 for (ConsentTierResponse consentTierResponse : consentTierResponseCollection) {
				  if(consentTierResponse.getConsentTier().getId().equals(newConsentTierResponse.getConsentTier().getId())){	 
					  consentTierResponse.setConsentTier(newConsentTierResponse.getConsentTier());
				      consentTierResponse.setResponse(newConsentTierResponse.getResponse());
				  }   
				}
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
		final Iterator<SpecimenCollectionGroup> specimenCollectionGroupIterator = specimenCollectionGroupCollection
				.iterator();
		while (specimenCollectionGroupIterator.hasNext())
		{
			final SpecimenCollectionGroup specimenCollectionGroup = specimenCollectionGroupIterator
					.next();
			specimenCollectionGroup
					.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration);

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
					specimen.setConsentTierStatusCollectionFromSCG(specimenCollectionGroup);
					dao.update(specimen,oldSpecimen);
				}
			}

			dao.update(specimenCollectionGroup,oldSpecimenCollectionGroup);
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
			SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup)itr.next();
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
			Specimen specimen = (Specimen)itr.next();
			if(specimenId.equals(specimen.getId()))
			{
				oldSpecimen = specimen;
				break;
			}
		}
		return oldSpecimen;

	}


	/**
	 * post Update.
	 * @param dao
	 * @param currentObj
	 * @param oldObj
	 * @param sessionDataBean
	 * @throws BizLogicException bizLogicException
	 */
	@Override
	public void postUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException

	{
		//ParticipantRegistrationCacheManager participantRegCacheManager =
		//new ParticipantRegistrationCacheManager();
		final CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) currentObj;
		final CollectionProtocolRegistration oldCollectionProtocolRegistration = (CollectionProtocolRegistration) oldObj;
		String oldProtocolParticipantId = oldCollectionProtocolRegistration
				.getProtocolParticipantIdentifier();

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
		final Participant participant = new Participant();

		participant.setLastName("");
		participant.setFirstName("");
		participant.setMiddleName("");
		participant.setSocialSecurityNumber(null);
		participant.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());

		// Create a dummy participant medical identifier.
		final Set<ParticipantMedicalIdentifier> partMedIdentifierColl = new HashSet();
		final ParticipantMedicalIdentifier partMedIdentifier = new ParticipantMedicalIdentifier();
		partMedIdentifier.setMedicalRecordNumber(null);
		partMedIdentifier.setSite(null);
		partMedIdentifierColl.add(partMedIdentifier);
		dao.insert(participant);

		partMedIdentifier.setParticipant(participant);
		dao.insert(partMedIdentifier);
		return participant;
	}

	/**
	 * Disable all the related collection protocol regitration for a given array
	 * of participant ids.
	 */
	public void disableRelatedObjectsForParticipant(DAO dao, Long participantIDArr[])
			throws BizLogicException
	{
		final List listOfSubElement = super.disableObjects(dao,
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

	/**
	 * Disable all the related collection protocol regitrations for a given
	 * array of collection protocol ids.
	 */
	public void disableRelatedObjectsForCollectionProtocol(DAO dao, Long collectionProtocolIDArr[])
			throws BizLogicException
	{
		final List listOfSubElement = super.disableObjects(dao,
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
		else
		{
			SynchronizeCollectionProtocolBizLogic synchronizeCollectionProtocolBizLogic=new SynchronizeCollectionProtocolBizLogic();
			CpSyncAudit cpSyncAudit = synchronizeCollectionProtocolBizLogic.getSyncStatus(registration.getCollectionProtocol().getId());
			if(cpSyncAudit!=null)
			{	
				String syncStatus=cpSyncAudit.getStatus();
				if("In Process".equalsIgnoreCase(syncStatus))
				{
					throw this.getBizLogicException(null, "errors.collectionprotocolregistration.syncinprocess", "");
				}
			}	
			
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

	/**
	 * Method to check if the unique constraint for PPI and CPid is
	 * violated or not.
	 * @param dao
	 * @param cpId
	 * @param ppi
	 * @return
	 * @throws BizLogicException
	 */
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
	/**
	 * This function finds out all the registerd participants for a particular
	 * collection protocol.
	 *
	 * @return List of ParticipantRegInfo
	 * @throws BizLogicException
	 * @throws ClassNotFoundException
	 */
	public List getAllParticipantRegistrationInfo() throws BizLogicException
	{
		final List participantRegistrationInfoList = new Vector();
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

					final List participantInfoList = new ArrayList();
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
				+ "= '" + Constants.CP_TYPE_PARENT + "' or cp.type = null or cp.type = '')";
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
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		final Collection newConsentTierResponseCollection = collectionProtocolRegistration
				.getConsentTierResponseCollection();
		final Iterator itr = newConsentTierResponseCollection.iterator();
		while (itr.hasNext())
		{
			final ConsentTierResponse consentTierResponse = (ConsentTierResponse) itr.next();
			if (consentTierResponse.getResponse().equalsIgnoreCase(Constants.WITHDRAWN))
			{
				final long consentTierID = consentTierResponse.getConsentTier().getId().longValue();
				this.updateSCG(collectionProtocolRegistration, oldCollectionProtocolRegistration,
						consentTierID, dao, sessionDataBean);
			}
		}
	}

	/*
	 * This method updates all the scg's associated with the selected
	 * collectionprotocolregistration.
	 */
	private void updateSCG(CollectionProtocolRegistration collectionProtocolRegistration,
			CollectionProtocolRegistration oldCollectionProtocolRegistration, long consentTierID,
			DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{

		final Collection newScgCollection = new HashSet();
		final Collection scgCollection = oldCollectionProtocolRegistration
				.getSpecimenCollectionGroupCollection();
		final Iterator scgItr = scgCollection.iterator();
		try
		{
			while (scgItr.hasNext())
			{
				final SpecimenCollectionGroup scg = (SpecimenCollectionGroup) scgItr.next();
				final String cprWithdrawOption = collectionProtocolRegistration
						.getConsentWithdrawalOption();

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

	// Mandar : 11-Jan-07 For Consent Tracking Withdrawal -------- end

	/* offset changes 27th Dec 2007 */

	/**
	 * This method is called if any Offset is given for shift in anticipated
	 * dates. In this method complete traversal of all the CollectionProtocols
	 * is done and the below hierarchy registered CP's are shifted in
	 * anticipated dates by the number of days as specified by the offset.
	 * @param dao
	 *            The DAO object
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param collectionProtocolRegistration
	 *            The CollectionProtocolRegistration Object
	 * @param offset
	 *            Offset value of number of days
	 * @throws BizLogicException
	 *             DAOException

	 */
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

	/**
	 * This method is called if the CollectionProtocol has Offset and also has
	 * any Child Collection Protocols so as to shift there anticipated dates as
	 * per the Offset specified
	 * @param dao
	 *            The DAO object
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param collectionProtocolRegistration
	 *            The CollectionProtocolRegistration Object
	 * @param offset
	 *            Offset value of number of days
	 * @throws BizLogicException
	 *             DAOException

	 */
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

	/**
	 * is Read Denied To be Checked.
	 */
	@Override
	public boolean isReadDeniedTobeChecked()
	{
		return true;
	}

	/**
	 * get Read Denied Privilege Name.
	 */
	@Override
	public String getReadDeniedPrivilegeName()
	{
		return Permissions.REGISTRATION + "," + Permissions.READ_DENIED;
	}

	/**
	 * has Privilege To View.
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
				String PPIdformat = getPPIdformat(collectionProtocolRegistration.getCollectionProtocol().getId());
				if(PPIdformat!=null && !Constants.DOUBLE_QUOTES.equals(PPIdformat) ){
					collectionProtocolRegistration.setProtocolParticipantIdentifier(TokenManager.getLabelValue(collectionProtocolRegistration.getCollectionProtocol(), 
							PPIdformat));
				}else{
					final LabelGenerator spLblGenerator = LabelGeneratorFactory
							.getInstance(Constants.PROTOCOL_PARTICIPANT_IDENTIFIER_LABEL_GENERATOR_PROPERTY_NAME);
					spLblGenerator.setLabel(collectionProtocolRegistration);
				}
			}
			catch (final NameGeneratorException e)
			{
				LOGGER.error(e.getMessage(), e);
				throw this.getBizLogicException(e, "name.generator.exp", "");
			}
			catch(ApplicationException e){
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
	
	/**
	 * @param sessionData
	 * @param specimenid
	 * @return
	 * @throws ApplicationException
	 * @throws DAOException
	 */
	public String getPPIdformat(Long cpid) throws ApplicationException, DAOException {
		final String hql1 = "select cp.ppidFormat"
				+ " from edu.wustl.catissuecore.domain.CollectionProtocol as cp where "
				+ "cp.id= ?";
		ColumnValueBean columnValue = new ColumnValueBean(cpid);
        List<ColumnValueBean> columnValueList = new ArrayList<ColumnValueBean>();
        columnValueList.add(columnValue);
       
		List<String> list = null;
		DAO dao = null;
		try
		{
		  dao = AppUtility.openDAOSession(null);
		  list = dao.executeQuery(hql1, columnValueList);
		}
		finally
		{
			dao.closeSession();
		}
		return list.get(0);
	}

	/**
	 * This method is as a part of removing the cp based cache.
	 * @return This will returns all the CPs from the database.
	 * @throws BizLogicException BizLogic Exception
	 */
	public List<CpAndParticipentsBean> getCollectionProtocolBeanList() throws BizLogicException
	{
		final List participantRegistrationBeanList = new Vector();

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
					final CpAndParticipentsBean cpDetails = new CpAndParticipentsBean(cpShortTitle, cpId.toString(),true);
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
	
	/**
	 * @param sessionData
	 * @param specimenid
	 * @return
	 * @throws ApplicationException
	 * @throws DAOException
	 */
	public List<Object> getCPIdandPartId(final SessionDataBean sessionData,
			String cprid) throws ApplicationException, DAOException {
		final String hql1 = "select cpr.collectionProtocol.id,cpr.participant.id"
				+ " from edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr where "
				+ "cpr.id=" + cprid;
		List<Object> list = null;
		DAO dao = null;
		try
		{
		  dao = AppUtility.openDAOSession(sessionData);
		  list = dao.executeQuery(hql1);
		}
		finally
		{
			dao.closeSession();
		}
		return list;
	}
	
	private Iterator<Object> getCPRs(Long cpId, DAO dao) throws BizLogicException
	{
		Iterator<Object> cprs=null;
		try
		{
			String getCPRQuery = "from edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr where cpr.collectionProtocol.id=:id";
			final ColumnValueBean columnValueBean=new ColumnValueBean(cpId);
			    columnValueBean.setColumnName("id");
			List<ColumnValueBean>  columnValueBeans=new ArrayList();
			columnValueBeans.add(columnValueBean);
			cprs=dao.executeParamHQLIterator(getCPRQuery,columnValueBeans);
		}
		catch(DAOException daoException)
		{
			throw new BizLogicException(daoException);
		}
		return  cprs;
	}
	/**
	 * This method will retrieve all CPRs of the given collectionprotocol and will check and update the missing consents,Events or SCGs.
	 * @param collectionProtocol 
	 * 						collection protocol for which CPRs needs to be processed. 
	 * @param sessionDataBean
	 * @param dao 
	 * 			HibernateDAO object.
	 * @throws BizLogicException
	 * @throws DAOException 
	 */
	public void processCPRs(String title,SessionDataBean sessionDataBean) throws  BizLogicException, DAOException
	{
		
		DAO dao = getHibernateDao(getAppName(),null);
		SynchronizeCollectionProtocolBizLogic synchronizeCollectionProtocolBizLogic=new SynchronizeCollectionProtocolBizLogic();
		CpSyncAudit cpSyncAudit=null;
		
		CollectionProtocolBizLogic collectionProtocolBizLogic=new CollectionProtocolBizLogic();
		CollectionProtocol collectionProtocol=collectionProtocolBizLogic.getCollectionProtocolByTitle(title,dao);
		
		Iterator<Object> registrations  = getCPRs(collectionProtocol.getId(), dao);
		Collection<ConsentTier> consentTiersInCP = collectionProtocol.getConsentTierCollection();
		DAO daoForEachCPR=null;
		
		long cprProcessCount=0;
		try
		{
			daoForEachCPR=getHibernateDao(getAppName(),null);
			// process each CPR of the CP.
			cpSyncAudit=synchronizeCollectionProtocolBizLogic.startSyncProcessAudit(collectionProtocol.getId(),daoForEachCPR,sessionDataBean.getUserId());
			while (registrations.hasNext()) {
				CollectionProtocolRegistration protocolRegistration=(CollectionProtocolRegistration) daoForEachCPR.retrieveById(CollectionProtocolRegistration.class.getName(), ((CollectionProtocolRegistration)registrations.next()).getId());
				if(Constants.ACTIVITY_STATUS_ACTIVE.equals(protocolRegistration.getActivityStatus()))
				{	
					updateConsentResponse(consentTiersInCP, protocolRegistration);
					
					SpecimenCollectionGroupBizLogic specimenCollectionGroupBizLogic=new SpecimenCollectionGroupBizLogic();
					specimenCollectionGroupBizLogic.updateSCGs(protocolRegistration,sessionDataBean,daoForEachCPR);
					
					CollectionProtocolRegistrationBizLogic protocolRegistrationBizLogic=new CollectionProtocolRegistrationBizLogic();
					protocolRegistrationBizLogic.update(daoForEachCPR, protocolRegistration,
							daoForEachCPR.retrieveById(CollectionProtocolRegistration.class.getName(), protocolRegistration.getId()), 
							sessionDataBean);
				}
				cprProcessCount++;
				if(!registrations.hasNext())
				{
					synchronizeCollectionProtocolBizLogic.updateSyncProcessStatus(cpSyncAudit,"Done",new Long(cprProcessCount),daoForEachCPR);
					daoForEachCPR.commit();
					
				}
				else if(cprProcessCount%10==0)
				{
					synchronizeCollectionProtocolBizLogic.updateSyncProcessStatus(cpSyncAudit,"In Process",new Long(cprProcessCount),daoForEachCPR);
					daoForEachCPR.commit();
					daoForEachCPR=getHibernateDao(getAppName(),null);
				}
				
			}
			//daoForEachCPR.closeSession();
			//
		}
		catch(BizLogicException bizLogicException)
		{
			synchronizeCollectionProtocolBizLogic.updateSyncProcessStatus(cpSyncAudit,"Error",new Long(cprProcessCount),daoForEachCPR);
			daoForEachCPR.commit();
			throw new BizLogicException(bizLogicException.getErrorKey(),bizLogicException,bizLogicException.getMessage());
		}
		catch(DAOException daoException)
		{
			throw new BizLogicException(daoException);
		}
		finally
		{
		  if(daoForEachCPR!=null)	
		  {
			 try 
			 {
				 daoForEachCPR.closeSession();
			 }
			 catch(DAOException exception)
			 {
				 throw new BizLogicException(exception);
			 }
		  }
		  if(dao!=null)	
		  {
			 try 
			 {
				 dao.closeSession();
			 }
			 catch(DAOException exception)
			 {
				 throw new BizLogicException(exception);
			 }
		  }
		}
	}

	private void updateConsentResponse(
			Collection<ConsentTier> consentTiersInCP,
			CollectionProtocolRegistration protocolRegistration) {
		Collection<ConsentTierResponse> consentTierResponsesInCPR=protocolRegistration.getConsentTierResponseCollection();
		//Iterate over each consent tier of cp and check whether it exist for cpr or not and if not then add that consent tier to consent
		//tier collection.
		for (ConsentTier individualConsentTierInCP : consentTiersInCP)
		{
			if(!responseCollectionContainsConsent(consentTierResponsesInCPR,
					individualConsentTierInCP))
			{
				ConsentTierResponse consentResponse = new ConsentTierResponse();
				consentResponse.setConsentTier(individualConsentTierInCP);
				consentResponse.setResponse("Not Specified");
				consentTierResponsesInCPR.add(consentResponse);
				
			}
		}
	}

	
	private boolean responseCollectionContainsConsent(
			Collection<ConsentTierResponse> consentTierResponsesInCPR,
			ConsentTier individualConsentTierInCP) {
		boolean containsConsentire=false;
		for (ConsentTierResponse consentTierResponse : consentTierResponsesInCPR) {
			if(consentTierResponse.getConsentTier().getId().equals(individualConsentTierInCP.getId()))
			{
				containsConsentire=true;
				break;
			}
		}
		return containsConsentire;
	}
	public String getCpTitlelistForParticipant(Long participantId,HibernateDAO hibernateDAO) throws BizLogicException{
	    String hql = "select cp.shortTitle from  edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr join cpr.collectionProtocol cp " +
	    		" where  cpr.participant.id = :pid";
	    ColumnValueBean columnValueBean=new ColumnValueBean(participantId);
        columnValueBean.setColumnName("pid");
        List<ColumnValueBean>  columnValueBeans=new ArrayList();
        columnValueBeans.add(columnValueBean);
        String cpNameList = "";
        try {
            Iterator<Object> cprs=hibernateDAO.executeParamHQLIterator(hql,columnValueBeans);
            while(cprs.hasNext()){
                 cpNameList += cprs.next();
                if(cprs.hasNext()){
                    cpNameList += ",";
                }
             }
        }
        catch (DAOException e) {
            throw new BizLogicException(e);
        }
        return cpNameList;
	    
	}
	public edu.wustl.catissuecore.dto.ParticipantDTO fetchCprDetailForParticipant(Long cpid,Long pid,DAO dao) throws BizLogicException{
		String hql = "select protocolParticipantIdentifier,registrationDate,barcode,id,activityStatus from edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr " +
				" where cpr.collectionProtocol.id=:cpid and  cpr.participant.id = :pid";
		ColumnValueBean columnValueBean=new ColumnValueBean(cpid);
		columnValueBean.setColumnName("cpid");
		List<ColumnValueBean>  columnValueBeans=new ArrayList();
		columnValueBeans.add(columnValueBean);
		columnValueBean=new ColumnValueBean(pid);
		columnValueBean.setColumnName("pid");
		columnValueBeans.add(columnValueBean);
		edu.wustl.catissuecore.dto.ParticipantDTO dto = new edu.wustl.catissuecore.dto.ParticipantDTO();
		
		try {
			Iterator<Object> cprs=dao.executeParamHQLIterator(hql,columnValueBeans);
			while(cprs.hasNext()){
				Object[] arr= (Object[])cprs.next();
				dto.setPpid(String.valueOf(arr[0]!=null?arr[0]:""));
				dto.setRegistrationDate((Date)arr[1]);
				dto.setBarcode(String.valueOf(arr[2]!=null?arr[2]:""));
				dto.setCprId(Long.parseLong(String.valueOf(arr[3])));
				dto.setActivityStatus((String) arr[4]);
			}
		} 
		catch (DAOException e) {
			throw new BizLogicException(e);
		}
		return dto;
		
	}

	/** Get registration id.
	 * @param sessionData
	 * @param specimenid
	 * @return
	 * @throws ApplicationException
	 * @throws DAOException
	 */
	public Long getRegistrationId(HibernateDAO hibernateDAO,
			Long participantId,Long cpId) throws ApplicationException {
//		String hql = "select cpr.id from edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr where cpr.collectionProtocol.id= "+cpId
//				+" and cpr.participant.id= "+participantId;
//		List list = null;//hibernateDAO.executeQuery(hql);
	       Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
	       params.put("0", new NamedQueryParam(DBTypes.LONG, cpId));
	       params.put("1", new NamedQueryParam(DBTypes.LONG, participantId));
	       List  list = hibernateDAO.executeNamedQuery("fetchCPRIdFromCPID", params);
	       return (Long)list.get(0);
	}

}