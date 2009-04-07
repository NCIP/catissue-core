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
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.TaskTimeCalculater;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.namegenerator.BarcodeGenerator;
import edu.wustl.catissuecore.namegenerator.BarcodeGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CollectionProtocolSeqComprator;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.ParticipantRegistrationCacheManager;
import edu.wustl.catissuecore.util.ParticipantRegistrationInfo;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exceptionformatter.DefaultExceptionFormatter;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.daofactory.DAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.exception.UserNotAuthorizedException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.locator.CSMGroupLocator;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * 
 * @author kapil_kaveeshwar
 */
public class CollectionProtocolRegistrationBizLogic extends CatissueDefaultBizLogic
{
	/**
	 * Saves the user object in the database.
	 * 
	 * @param obj
	 *            The user object to be saved.
	 * @param session
	 *            The session in which the object is saved.
	 * @throws DAOException
	 */
	private static boolean armFound = false;

	private Date dateOfLastEvent = null;
	private static int offset = 0;
	private int cntOfStudyCalEventPnt = 0;

	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		offset = 0;
		armFound = false;
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
		
		try 
		{
			// check for closed Collection Protocol
			checkStatus(dao, collectionProtocolRegistration.getCollectionProtocol(), "Collection Protocol");

			// Check for closed Participant
			checkStatus(dao, collectionProtocolRegistration.getParticipant(), "Participant");

			checkUniqueConstraint(dao, collectionProtocolRegistration, null);


			Participant participant = null;

			if (collectionProtocolRegistration.getParticipant() != null)
			{
				Object participantObj = dao.retrieveById(Participant.class.getName(), collectionProtocolRegistration.getParticipant().getId());

				if (participantObj != null)
				{
					participant = (Participant) participantObj;
				}
			}
			else
			{
				participant = addDummyParticipant(dao, sessionDataBean);
			}

			collectionProtocolRegistration.setParticipant(participant);

			String barcode = collectionProtocolRegistration.getBarcode();
			generateCPRBarcode(collectionProtocolRegistration);
			//Label generator for Protocol Participant Identifier
			generateProtocolParticipantIdentifierLabel(collectionProtocolRegistration);

			if((barcode!=collectionProtocolRegistration.getBarcode()) && barcode!=null)
			{
				collectionProtocolRegistration.setBarcode(barcode);
			}

			insertCPR(collectionProtocolRegistration, dao, sessionDataBean);
			if (armFound == false && Constants.ARM_CP_TYPE.equals(collectionProtocolRegistration.getCollectionProtocol().getType()))
			{
				armCheckandRegistration(collectionProtocolRegistration, dao, sessionDataBean);
			}
		} catch (DAOException e) {
			throw getBizLogicException(e, "bizlogic.error", "");
		}
	}

	/**
	 * Method to generate barcode for the SpecimenCollectionGroup
	 * @param specimenCollectionGroup Object of SpecimenCollectionGroup
	 * @throws DAOException DAO exception
	 */
	private void generateCPRBarcode(
			CollectionProtocolRegistration collectionProtocolRegistration)
			throws BizLogicException
	{
		try
		{
			if(Variables.isCollectionProtocolRegistrationBarcodeGeneratorAvl)
			{
				BarcodeGenerator collectionProtocolRegistrationBarcodeGenerator = BarcodeGeneratorFactory
				.getInstance(Constants.COLL_PROT_REG_BARCODE_GENERATOR_PROPERTY_NAME);
				collectionProtocolRegistrationBarcodeGenerator.setBarcode(collectionProtocolRegistration);
			}
		}
		catch(NameGeneratorException nameGeneratorException)
		{
			throw getBizLogicException(nameGeneratorException, "bizlogic.error", "");
		}
	}
	
	/**
	 * This method is called when any arm is registered and it has no further
	 * child arms. This method then registers the remaining phases of the parent
	 * CollectionProtocol and is called recursively till the
	 * MainParentCollectionProtocol's phases are registered
	 * 
	 * @param collectionProtocolRegistration
	 *            The CollectionProtocolRegistration Object for current
	 *            CollectionProtocol
	 * @param dao
	 *            The DAO object
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws DAOException

	 */
	private void armCheckandRegistration(CollectionProtocolRegistration collectionProtocolRegistration, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException,DAOException
	{
		Integer sequenceNumber = collectionProtocolRegistration.getCollectionProtocol().getSequenceNumber();
		CollectionProtocol parentCPofArm = collectionProtocolRegistration.getCollectionProtocol().getParentCollectionProtocol();
		Date dateofCP = new Date();
		if (parentCPofArm != null)
		{
			try
			{
				dateofCP = getImmediateParentCPdate(parentCPofArm.getId(), collectionProtocolRegistration.getParticipant().getId());
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}

			List childCPColl = getChildColl(parentCPofArm);
			Iterator iteratorofchildCP = childCPColl.iterator();
			while (iteratorofchildCP.hasNext())
			{
				if (armFound)
					break;

				CollectionProtocol cp = (CollectionProtocol) iteratorofchildCP.next();
				if (cp != null && cp.getSequenceNumber() != null)
				{
					if (cp.getSequenceNumber().intValue() > sequenceNumber.intValue())
					{
						CollectionProtocolRegistration collectionProtocolRegistrationCheck = getCPRbyCollectionProtocolIDAndParticipantID(dao, cp
								.getId(), collectionProtocolRegistration.getParticipant().getId());
						if (collectionProtocolRegistrationCheck == null)
						{
							CollectionProtocolRegistration childCollectionProtocolRegistration = createCloneOfCPR(collectionProtocolRegistration, cp);
							setRegDate(childCollectionProtocolRegistration, cp.getStudyCalendarEventPoint(), dateofCP);
							getTotalOffset(childCollectionProtocolRegistration, dao, sessionDataBean);
							//							if (childCollectionProtocolRegistration.getOffset() != null)
							//							{
							//								if (childCollectionProtocolRegistration.getOffset().intValue() != 0)
							//								{
							if (offset != 0)
							{ //bug 6500 so that CP with null studyCalendarEventPoint inherits Appropriate date
								if (cp.getStudyCalendarEventPoint() != null)
								{
									childCollectionProtocolRegistration.setRegistrationDate(edu.wustl.catissuecore.util.global.Utility
											.getNewDateByAdditionOfDays(childCollectionProtocolRegistration.getRegistrationDate(), offset));
								}
							}
							//								}
							//						}
							insertCPR(childCollectionProtocolRegistration, dao, sessionDataBean);
						}
						else
						{
							/* this lines of code is for second arm registered manually*/
							setRegDate(collectionProtocolRegistrationCheck, cp.getStudyCalendarEventPoint(), dateofCP);
							//if registered CPR has offset on itself
							if (collectionProtocolRegistrationCheck.getOffset() != null)
							{
								if (collectionProtocolRegistrationCheck.getOffset().intValue() != 0)
								{
									collectionProtocolRegistrationCheck.setRegistrationDate(edu.wustl.catissuecore.util.global.Utility
											.getNewDateByAdditionOfDays(collectionProtocolRegistrationCheck.getRegistrationDate(),
													collectionProtocolRegistrationCheck.getOffset().intValue()));
								}
							}
							//total offset of all the previously registered ColectionProtocols and SCGs
							getTotalOffset(collectionProtocolRegistrationCheck, dao, sessionDataBean);
							if (offset != 0)//if before registration of second arm the registered CP has offset on itself.this condition is taken into consideration here.
							{
								//								collectionProtocolRegistrationCheck.setOffset(offset);
								//bug 6500 so that CP with null studyCalendarEventPoint inherits Appropriate date
								if (cp.getStudyCalendarEventPoint() != null)
								{
									collectionProtocolRegistrationCheck.setRegistrationDate(edu.wustl.catissuecore.util.global.Utility
											.getNewDateByAdditionOfDays(collectionProtocolRegistrationCheck.getRegistrationDate(), offset));
								}

							}
							dao.update(collectionProtocolRegistrationCheck,false);
							//							updateOffsetForEventsForAlreadyRegisteredCPR(dao, sessionDataBean, collectionProtocolRegistrationCheck);
							checkAndUpdateChildDate(dao, sessionDataBean, collectionProtocolRegistrationCheck);

						}
					}
					/* Here check is done for CPR not to be same and then if another arm is registered SCG's of previous arm which are not collected are disabled*/
					else if (cp.getSequenceNumber().intValue() == sequenceNumber.intValue())
					{
						CollectionProtocolRegistration collectionProtocolRegistrationOfPreviousArm = getCPRbyCollectionProtocolIDAndParticipantID(
								dao, cp.getId(), collectionProtocolRegistration.getParticipant().getId());

						if (collectionProtocolRegistrationOfPreviousArm != null)
						{
							if (!(collectionProtocolRegistrationOfPreviousArm.getId().equals(collectionProtocolRegistration.getId())))
							{
								Long id = getIdofCPR(dao, sessionDataBean, collectionProtocolRegistration);
								if (!(collectionProtocolRegistrationOfPreviousArm.getId().equals(id)))
								{
									changeStatusOfEvents(dao, sessionDataBean, collectionProtocolRegistrationOfPreviousArm);
									checkForChildStatus(dao, sessionDataBean, collectionProtocolRegistrationOfPreviousArm);
								}
							}
						}

					}

				}

			}
			if (parentCPofArm != null)
			{
				//				armCheckandRegistration(CPRofParent, dao, sessionDataBean);
				armCheckandRegistration(createCloneOfCPR(collectionProtocolRegistration, parentCPofArm), dao, sessionDataBean);
			}
		}

	}

	/**This method is called for the protocols that are automatically registered after registration of an arm.In this 
	 * method the total offset of upper level hierarchy up to that protocol is calculated for proper recalculation of the 
	 * registration date
	 * @param collectionProtocolRegistrationThe CollectionProtocolRegistration Object for current CollectionProtocol
	 * @param dao The DAO object
	 * @param sessionDataBean  The session in which the object is saved.
	 * @throws DAOException
	 */
	public void getTotalOffset(CollectionProtocolRegistration collectionProtocolRegistration, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException
	{
		offset = 0;
		CollectionProtocol collectionProtocol = collectionProtocolRegistration.getCollectionProtocol();
		Long participantId = collectionProtocolRegistration.getParticipant().getId();
		CollectionProtocol mainParent = null;
		if (collectionProtocol.getParentCollectionProtocol() != null)
		{
			mainParent = getMainParentCP(collectionProtocol.getParentCollectionProtocol());
		}
		else
			mainParent = collectionProtocol;
		calculationOfTotalOffset(mainParent, dao, sessionDataBean, participantId, collectionProtocol);
	}

	public void calculationOfTotalOffset(CollectionProtocol collectionProtocol, DAO dao, SessionDataBean sessionDataBean, Long participantId,
			CollectionProtocol collectionProtocolToRegister) throws DAOException
	{
		if (collectionProtocol.getId() != collectionProtocolToRegister.getId())
		{
			CollectionProtocolRegistration cpr = getCPRbyCollectionProtocolIDAndParticipantID(dao, collectionProtocol.getId(), participantId);
			if (cpr != null)
			{
				Integer offsetFromCP = cpr.getOffset();
				if (offsetFromCP != null)
				{
					if (offsetFromCP.intValue() != 0)
						offset = offset + offsetFromCP.intValue();
				}
				offsetFromSCG(cpr);
				if (collectionProtocol.getChildCollectionProtocolCollection() != null)
				{
					List childCollectionCP = getChildColl(collectionProtocol);
					if (!(childCollectionCP.isEmpty()))
					{
						Iterator childCollectionCPIterator = childCollectionCP.iterator();
						while (childCollectionCPIterator.hasNext())
						{
							CollectionProtocol cp = (CollectionProtocol) childCollectionCPIterator.next();
							calculationOfTotalOffset(cp, dao, sessionDataBean, participantId, collectionProtocolToRegister);
						}
					}
				}
			}
		}
	}

	/**This method is called so as to calculate total offset of SCG for currentCPR.
	 * This method is called when an CP is automatically registered after an arm.All SCG's of upper level hierarchy which carry offset 
	 * are added together for total offset,So that registration date is correct
	 * @param cpr the CollectionProtocol object which has SCG's
	 */
	public void offsetFromSCG(CollectionProtocolRegistration cpr)
	{
		Collection specimenCollectionGroupCollection = cpr.getSpecimenCollectionGroupCollection();
		if (specimenCollectionGroupCollection != null)
		{
			if (!specimenCollectionGroupCollection.isEmpty())
			{
				Iterator specimenCollectionGroupIterator = specimenCollectionGroupCollection.iterator();
				while (specimenCollectionGroupIterator.hasNext())
				{
					SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) specimenCollectionGroupIterator.next();
					Integer offsetFromSCG = specimenCollectionGroup.getOffset();
					if (offsetFromSCG != null)
					{
						if (offsetFromSCG.intValue() != 0)
							offset = offset + offsetFromSCG.intValue();
					}
				}
			}
		}
	}

	/**The id of CPR is extracted from database with respect to CollectionProtocol id and Participant id
	 * 
	 * @param dao
	 * @param sessionDataBean
	 * @param collectionProtocolRegistration
	 * @return

	 * @throws DAOException
	 */
	public Long getIdofCPR(DAO dao, SessionDataBean sessionDataBean, CollectionProtocolRegistration collectionProtocolRegistration)
			throws BizLogicException
	{
		Long id = null;
		
		try
		{
			if (collectionProtocolRegistration.getCollectionProtocol() != null && collectionProtocolRegistration.getParticipant().getId() != null)
			{
				Long parentCpId = collectionProtocolRegistration.getCollectionProtocol().getId();
				if (parentCpId != null)
				{
					// get the previous cp's offset if present.
					String hql = "select  cpr.id from " + CollectionProtocolRegistration.class.getName() + " as cpr where cpr.collectionProtocol.id = "
					+ parentCpId.toString() + " and cpr.participant.id = " + collectionProtocolRegistration.getParticipant().getId().toString();
					List idList = null;

					idList = dao.executeQuery(hql);

					if (idList != null && !idList.isEmpty())
					{
						for (int i = 0; i < idList.size(); i++)
						{
							id = (Long) idList.get(i);
							if (id != null)
								return id;
						}
					}
				}

			}
		}
		catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	/** The status of all Specimen Collection Group is changed when another arm is registered if they are not collected
	 * @param collectionProtocolRegistration The CollectionProtocolRegistration Object for currentCollectionProtocol
	 * @param dao The DAO object
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws DAOException
	
	 */
	public void changeStatusOfEvents(DAO dao, SessionDataBean sessionDataBean, CollectionProtocolRegistration collectionProtocolRegistration)
			throws BizLogicException
	{
		try 
		{
		Collection specimenCollectionGroupCollection = collectionProtocolRegistration.getSpecimenCollectionGroupCollection();
		if (!specimenCollectionGroupCollection.isEmpty())
		{
			Iterator specimenCollectionGroupIterator = specimenCollectionGroupCollection.iterator();
			while (specimenCollectionGroupIterator.hasNext())
			{
				SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) specimenCollectionGroupIterator.next();
				boolean status = false;
				Collection specimenCollection = specimenCollectionGroup.getSpecimenCollection();
				if (!specimenCollection.isEmpty())
				{
					Iterator specimenIterator = specimenCollection.iterator();
					while (specimenIterator.hasNext())
					{
						Specimen specimen = (Specimen) specimenIterator.next();
						String collectionStatus = specimen.getCollectionStatus();
						if (!(collectionStatus.equalsIgnoreCase("Pending")))
							status = true;
					}
					if (status == false)
					{
						specimenCollectionGroup.setCollectionStatus("Not Collected");
						
							dao.update(specimenCollectionGroup,false);
						
					}
				}
			}
		}
		
		} catch (DAOException e) {
			throw getBizLogicException(e, "bizlogic.error", "");
		}
	}

	/**In this method the status of Specimen Collection Group of Child CollectionProtocol is changed if the previous arm has any child 
	 * The status is changed only when the Specimen is not collected
	 * @param collectionProtocolRegistration The CollectionProtocolRegistration Object for currentCollectionProtocol
	 * @param dao The DAO object
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws DAOException
	
	 */
	public void checkForChildStatus(DAO dao, SessionDataBean sessionDataBean, CollectionProtocolRegistration collectionProtocolRegistration)
			throws BizLogicException
	{
			String status = Constants.CHILD_STATUS;
			getCpAndCpr(dao, sessionDataBean, collectionProtocolRegistration, status);
		
	}

	/**
	 * @param dao
	 * @param sessionDataBean
	 * @param cp
	 * @param cpr
	
	 * @throws DAOException
	 */
	private boolean checkChildStatus(DAO dao, SessionDataBean sessionDataBean, CollectionProtocol cp,
			CollectionProtocolRegistration cpr) throws BizLogicException
	{
		changeStatusOfEvents(dao, sessionDataBean, cpr);
		if (cp.getChildCollectionProtocolCollection() != null && cp.getChildCollectionProtocolCollection().size() != 0)
		{
			return true;
		}
		return false;
	}

	/**In this method if there is change in Offset of parent protocol then the offset of child CollectionProtocol 
	 * also changes.This is basically when upper level Hierarchy Protocol has an offset and below CP's are registered automatically.
	 * @param collectionProtocolRegistration The CollectionProtocolRegistration Object for currentCollectionProtocol
	 * @param dao The DAO object
	 * @param sessionDataBean The session in which the object is saved.
	 * @throws DAOException

	 */
	public void checkAndUpdateChildDate(DAO dao, SessionDataBean sessionDataBean, CollectionProtocolRegistration collectionProtocolRegistration)
			throws BizLogicException
	{
		String status = Constants.CHILD_DATE;
		getCpAndCpr(dao, sessionDataBean, collectionProtocolRegistration,status);
	}

	/**
	 * @param dao
	 * @param sessionDataBean
	 * @param collectionProtocolRegistration
	 * @throws DAOException
	
	 */
	private void getCpAndCpr(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration, String callMethod) throws BizLogicException
	{
		try
		{
			CollectionProtocol parent = collectionProtocolRegistration.getCollectionProtocol();
			List childCPColl = getChildColl(parent);
			if (childCPColl != null && !childCPColl.isEmpty())
			{
				Iterator iteratorofchildCP = childCPColl.iterator();
				while (iteratorofchildCP.hasNext())
				{
					CollectionProtocol cp = (CollectionProtocol) iteratorofchildCP.next();
					if (cp != null)
					{
						CollectionProtocolRegistration cpr = getCPRbyCollectionProtocolIDAndParticipantID(dao, cp.getId(), collectionProtocolRegistration
								.getParticipant().getId());
						if (cpr != null)
						{
							boolean callRecursive = false;
							if(callMethod.equals(Constants.CHILD_STATUS))
							{
								callRecursive = checkChildStatus(dao, sessionDataBean, cp, cpr);
							}
							else if(callMethod.equals(Constants.CHILD_DATE))
							{
								callRecursive = checkUpdateChildDate(dao, sessionDataBean, collectionProtocolRegistration,cp, cpr);
							}
							else if(callMethod.equals(Constants.CHILD_OFFSET))
							{
								callRecursive = checkUpdateChildOffset(dao, sessionDataBean, offset, cp, cpr);
							}
							if(callRecursive)
							{
								getCpAndCpr(dao, sessionDataBean, collectionProtocolRegistration,callMethod);
							}
						}
					}
				}
			}
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "bizlogic.error", "");
		}
	}

	/**
	 * @param dao
	 * @param sessionDataBean
	 * @param collectionProtocolRegistration
	 * @param cp
	 * @param cpr
	 * @throws DAOException

	 */
	private boolean checkUpdateChildDate(DAO dao, SessionDataBean sessionDataBean,
			CollectionProtocolRegistration collectionProtocolRegistration, CollectionProtocol cp,
			CollectionProtocolRegistration cpr) throws BizLogicException, DAOException
	{
		setRegDate(cpr, cp.getStudyCalendarEventPoint(), collectionProtocolRegistration.getRegistrationDate());
		Integer offsetOfCurrentCPR = cpr.getOffset();
		{
			if (offsetOfCurrentCPR != null)
			{
				cpr.setOffset(offsetOfCurrentCPR);
				cpr.setRegistrationDate(edu.wustl.catissuecore.util.global.Utility.getNewDateByAdditionOfDays(cpr
						.getRegistrationDate(), offsetOfCurrentCPR.intValue()));
			}
		}
		dao.update(cpr,false);
		//						updateOffsetForEventsForAlreadyRegisteredCPR(dao, sessionDataBean, cpr);
		if (cp.getChildCollectionProtocolCollection() != null && cp.getChildCollectionProtocolCollection().size() != 0)
		{
			return true;
			//checkAndUpdateChildDate(dao, sessionDataBean, cpr);
		}
		return false;
	}

	//	private void updateOffsetForEventsForAlreadyRegisteredCPR(DAO dao, SessionDataBean sessionDataBean,
	//			CollectionProtocolRegistration collectionProtocolRegistration) throws UserNotAuthorizedException, DAOException
	//	{
	//		Collection specimenCollectionGroupCollection = (Collection) dao.retrieveAttribute(CollectionProtocolRegistration.class.getName(),
	//				collectionProtocolRegistration.getId(), Constants.COLUMN_NAME_SCG_COLL);
	//		if (!specimenCollectionGroupCollection.isEmpty())
	//		{
	//			Iterator specimenCollectionGroupIterator = specimenCollectionGroupCollection.iterator();
	//			while (specimenCollectionGroupIterator.hasNext())
	//			{
	//				SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) specimenCollectionGroupIterator.next();
	//				Integer offset = collectionProtocolRegistration.getOffset();
	//				if (offset != null)
	//				{
	//					specimenCollectionGroup.setOffset(new Integer(offset));
	//				}
	//				dao.update(specimenCollectionGroup, sessionDataBean, true, true, false);
	//			}
	//		}
	//
	//	}

	private List getChildColl(CollectionProtocol parent)
	{
		Collection childCPcollection = parent.getChildCollectionProtocolCollection();
		List childCPColl = new ArrayList();
		childCPColl.addAll(childCPcollection);
		CollectionProtocolSeqComprator seqComp = new CollectionProtocolSeqComprator();
		java.util.Collections.sort(childCPColl, seqComp);
		return childCPColl;
	}

	private Date getImmediateParentCPdate(Long maincpId, Long participantId) throws DAOException, ClassNotFoundException
	{
		Date regDate = null;
		String hql1 = "select cpr.registrationDate from " + CollectionProtocolRegistration.class.getName()
				+ " as cpr where cpr.collectionProtocol.id = " + maincpId.toString() + " and cpr.participant.id = " + participantId.toString();
		DAO dao = (HibernateDAO)openDAOSession();
		List dateList = dao.executeQuery(hql1);
		if (dateList != null && dateList.size() > 0)
		{
			regDate = (Date) dateList.get(0);
		}
		dao.closeSession();
		return regDate;
	}

	/**The main parent(Collection Protocol) of the Collection Protocol is returned through this method.
	 * the main parent is the topmost level collection Protocol in the hierarchy
	 * @param cp CP whose main parent is to be found out
	 * @return Main parent collection Protocol
	 */

	private CollectionProtocol getMainParentCP(CollectionProtocol cp)
	{
		if (cp != null)
		{
			// If cp's parent cp is null means this cp is the parent cp.
			if (cp.getParentCollectionProtocol() == null)
				return cp;
			else
				cp = getMainParentCP(cp.getParentCollectionProtocol());
		}
		return cp;

	}

	public void insertCPR(CollectionProtocolRegistration collectionProtocolRegistration, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			boolean reportLoaderFlag = false;
			if(Constants.REGISTRATION_FOR_REPORT_LOADER.equals(collectionProtocolRegistration.getProtocolParticipantIdentifier()))
			{
				reportLoaderFlag = true;
				collectionProtocolRegistration.setProtocolParticipantIdentifier(null);
			}


			dao.insert(collectionProtocolRegistration,true);

			if (armFound == false)
			{
				if(reportLoaderFlag==false)
				{
					createSCG(collectionProtocolRegistration, dao, sessionDataBean);
					chkForChildCP(collectionProtocolRegistration, dao, sessionDataBean);
				}
			}

		} catch (DAOException e) {
			throw getBizLogicException(e, "bizlogic.error", "");
		}	
	}

	/** In this method if parent CP has any child which can be automatically registered,then these child are registered
	 * 
	 * @param cpr The CollectionProtocol Registration Object of current Collection Protocol
	 * @param dao The DAO object
	 * @param sessionDataBean the SessionDataBean
	 * @throws DAOException
	
	 */

	public void chkForChildCP(CollectionProtocolRegistration cpr, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		CollectionProtocol parentCP = cpr.getCollectionProtocol();
		Date dateofCP = cpr.getRegistrationDate();
		List childCPColl = getChildColl(parentCP);

		if (childCPColl != null && !childCPColl.isEmpty())
		{
			Iterator itr = childCPColl.iterator();
			while (itr.hasNext())
			{
				CollectionProtocol cp = (CollectionProtocol) itr.next();
				if (cp != null && cp.getSequenceNumber() != null)
				{
					if (armFound == false)
					{
						if (!Constants.ARM_CP_TYPE.equalsIgnoreCase(cp.getType()))
						{

							CollectionProtocolRegistration cloneCPR = createCloneOfCPR(cpr, cp);
							setRegDate(cloneCPR, cp.getStudyCalendarEventPoint(), dateofCP);
							//The offset for child is calculated twice...bug 6843
							//							if (cloneCPR.getOffset() != null)
							//							{
							//								if (cloneCPR.getOffset().intValue() != 0)
							//								{
							//									cloneCPR.setRegistrationDate(edu.wustl.catissuecore.util.global.Utility.getNewDateByAdditionOfDays(cloneCPR
							//											.getRegistrationDate(), cloneCPR.getOffset().intValue()));
							//								}
							//							}
							insertCPR(cloneCPR, dao, sessionDataBean);
						}
						else
						{
							armFound = true;
						}
					}
				}
			}
		}

	}

	public void setRegDate(CollectionProtocolRegistration cpr, Double studyeventpointCalendar, Date dateofCP)
	{
		if (studyeventpointCalendar != null)
		{
			cpr.setRegistrationDate(edu.wustl.catissuecore.util.global.Utility.getNewDateByAdditionOfDays(dateofCP, studyeventpointCalendar
					.intValue()));
		}
		else
		{
			/**
			 * If studyeventpointCalendar of CollecttionProtocol is null then
			 * take the RegistrationDate of last Event.
			 */
			cntOfStudyCalEventPnt += 1;
			cpr.setRegistrationDate(edu.wustl.catissuecore.util.global.Utility.getNewDateByAdditionOfDays(dateOfLastEvent,
					cntOfStudyCalEventPnt));
		}

	}

	public CollectionProtocolRegistration createCloneOfCPR(CollectionProtocolRegistration cpr, CollectionProtocol cp)
	{
		CollectionProtocolRegistration cloneCPR = new CollectionProtocolRegistration(cpr);
		cloneCPR.setCollectionProtocol(cp);
		return cloneCPR;

	}

	private void insertConsentTiers(Collection consentTierResponseCollection, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException, DAOException
	{
		if (consentTierResponseCollection != null)
		{
			Iterator itr = consentTierResponseCollection.iterator();
			while (itr.hasNext())
			{
				ConsentTierResponse consentTierResponse = (ConsentTierResponse) itr.next();
				if (consentTierResponse.getConsentTier() != null)
				{
					dao.insert(consentTierResponse.getConsentTier(), false);
				}

			}
		}

	}

	//Abhishek Mehta : Performance related Changes
	/*
	 * Creating SCG and specimen while creating participant.
	 */

	private Long userID;

	private void createSCG(CollectionProtocolRegistration collectionProtocolRegistration, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try 
		{
		registerParticipantAndProtocol(dao, collectionProtocolRegistration, sessionDataBean);
		dateOfLastEvent = collectionProtocolRegistration.getRegistrationDate();
		cntOfStudyCalEventPnt = 0;

		SpecimenCollectionGroupBizLogic specimenBizLogic = new SpecimenCollectionGroupBizLogic();
		Collection collectionProtocolEventCollection = collectionProtocolRegistration.getCollectionProtocol()
				.getCollectionProtocolEventCollection();
		Iterator collectionProtocolEventIterator = collectionProtocolEventCollection.iterator();
		Collection scgCollection = new HashSet();
		while (collectionProtocolEventIterator.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) collectionProtocolEventIterator.next();

			int tmpCntOfStudyCalEventPnt = (collectionProtocolEvent.getStudyCalendarEventPoint()).intValue();
			if (cntOfStudyCalEventPnt != 0)
			{
				if (tmpCntOfStudyCalEventPnt > cntOfStudyCalEventPnt)
				{
					cntOfStudyCalEventPnt = tmpCntOfStudyCalEventPnt;
				}
			}
			if (cntOfStudyCalEventPnt == 0)
			{
				cntOfStudyCalEventPnt = tmpCntOfStudyCalEventPnt;
			}

			/**
			 * Here countOfStudyCalendarEventPoint for previous
			 * CollectionProtocol which is registered is incremented as per
			 * StudyCalendarEventPoint of Events.
			 */
			SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup(collectionProtocolEvent);
			specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
			specimenCollectionGroup.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration);

	
				specimenBizLogic.insert(specimenCollectionGroup,dao,sessionDataBean);
			
			scgCollection.add(specimenCollectionGroup);
		}
		collectionProtocolRegistration.setSpecimenCollectionGroupCollection(scgCollection);
		
		} catch (UserNotAuthorizedException e) {
			throw getBizLogicException(e, "bizlogic.error", "");
		} catch (DAOException e) {
			throw getBizLogicException(e, "bizlogic.error", "");
		}
	}


	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
		ParticipantRegistrationCacheManager participantRegCacheManager = new ParticipantRegistrationCacheManager();
		participantRegCacheManager.registerParticipant(collectionProtocolRegistration.getCollectionProtocol().getId(), collectionProtocolRegistration
				.getParticipant().getId(), collectionProtocolRegistration.getProtocolParticipantIdentifier());
	}

	/**
	 * Updates the persistent object in the database.
	 * 
	 * @param obj
	 *            The object to be updated.
	 * @param session
	 *            The session in which the object is saved.
	 * @throws DAOException
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException
	{
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
		CollectionProtocolRegistration oldCollectionProtocolRegistration = (CollectionProtocolRegistration) oldObj;
		CollectionProtocolRegistration persistentCPR = null;
		Object object = dao.retrieveById(CollectionProtocolRegistration.class.getName(), oldCollectionProtocolRegistration.getId());
		if(object !=null)
		{
			persistentCPR=(CollectionProtocolRegistration) object;	
		}
		
		// Check for different Collection Protocol
		if (!collectionProtocolRegistration.getCollectionProtocol().getId().equals(oldCollectionProtocolRegistration.getCollectionProtocol().getId()))
		{	
			checkStatus(dao, collectionProtocolRegistration.getCollectionProtocol(), "Collection Protocol");
		}

		// -- Check for different Participants and closed participant
		// old and new values are not null
		if (collectionProtocolRegistration.getParticipant() != null && oldCollectionProtocolRegistration.getParticipant() != null
				&& collectionProtocolRegistration.getParticipant().getId() != null
				&& oldCollectionProtocolRegistration.getParticipant().getId() != null)
		{
			if (!collectionProtocolRegistration.getParticipant().getId().equals(oldCollectionProtocolRegistration.getParticipant().getId()))
			{
				
				checkStatus(dao, collectionProtocolRegistration.getParticipant(), "Participant");
			}
		}

		// when old participant is null and new is not null
		if (collectionProtocolRegistration.getParticipant() != null && oldCollectionProtocolRegistration.getParticipant() == null)
		{
			if (collectionProtocolRegistration.getParticipant().getId() != null)
			{
				
				checkStatus(dao, collectionProtocolRegistration.getParticipant(), "Participant");
			}
		}

		/**
		 * Case: While updating the registration if the participant is
		 * deselected then we need to maintain the link between registration and
		 * participant by adding a dummy participant for query module.
		 */
		if (collectionProtocolRegistration.getParticipant() == null)
		{
			Participant oldParticipant = oldCollectionProtocolRegistration.getParticipant();

			// Check for if the older participant was also a dummy, if true use
			// the same participant,
			// otherwise create an another dummay participant
			if (oldParticipant != null)
			{
				String firstName = Utility.toString(oldParticipant.getFirstName());
				String lastName = Utility.toString(oldParticipant.getLastName());
				String birthDate = Utility.toString(oldParticipant.getBirthDate());
				String ssn = Utility.toString(oldParticipant.getSocialSecurityNumber());
				if (firstName.trim().length() == 0 && lastName.trim().length() == 0 && birthDate.trim().length() == 0 && ssn.trim().length() == 0)
				{
					persistentCPR.setParticipant(oldParticipant);
				}
				else
				{
					// create dummy participant.
					Participant participant = addDummyParticipant(dao, sessionDataBean);
					persistentCPR.setParticipant(participant);
				}

			} // oldpart != null
			else
			{
				// create dummy participant.
				Participant participant = addDummyParticipant(dao, sessionDataBean);
				persistentCPR.setParticipant(participant);
			}
		}
		
		checkUniqueConstraint(dao, collectionProtocolRegistration, oldCollectionProtocolRegistration);
		
		// Mandar 22-Jan-07 To disable consents accordingly in SCG and
		// Specimen(s) start
		if (!collectionProtocolRegistration.getConsentWithdrawalOption().equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_NOACTION))
		{
			verifyAndUpdateConsentWithdrawn(collectionProtocolRegistration, oldCollectionProtocolRegistration, dao, sessionDataBean);
			//kalpana bug #5911
			//collectionProtocolRegistration.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
		}

		/*lazy change */
		
		/*Collection specimenCollectionGroupCollection = (Collection) dao.retrieveAttribute(CollectionProtocolRegistration.class.getName(),
				collectionProtocolRegistration.getId(), Constants.COLUMN_NAME_SCG_COLL);
		collectionProtocolRegistration.setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);
		
		updateConsentResponseForSCG(collectionProtocolRegistration, dao, sessionDataBean);*/
		Collection specimenCollectionGroupCollection = persistentCPR.getSpecimenCollectionGroupCollection();
		collectionProtocolRegistration.setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);
		updateConsentResponseForSCG(collectionProtocolRegistration, dao, sessionDataBean);
		persistentCPR.setConsentTierResponseCollection(collectionProtocolRegistration.getConsentTierResponseCollection());
		persistentCPR.setConsentWitness(collectionProtocolRegistration.getConsentWitness());
		persistentCPR.setConsentSignatureDate(collectionProtocolRegistration.getConsentSignatureDate());
		persistentCPR.setSignedConsentDocumentURL(collectionProtocolRegistration.getSignedConsentDocumentURL());
		persistentCPR.setProtocolParticipantIdentifier(collectionProtocolRegistration.getProtocolParticipantIdentifier());
		persistentCPR.setRegistrationDate(collectionProtocolRegistration.getRegistrationDate());
		persistentCPR.setActivityStatus(collectionProtocolRegistration.getActivityStatus());
		persistentCPR.setBarcode(collectionProtocolRegistration.getBarcode());
		persistentCPR.setSpecimenCollectionGroupCollection(collectionProtocolRegistration.getSpecimenCollectionGroupCollection());
		/* for offset 27th Dec 2007 */
		// Check if Offset is present.If it is present then all the below
		// hierarchy protocols are shifted according to the Offset.Integer offsetOld=oldCollectionProtocolRegistration.getOffset();
		Integer offsetOld = oldCollectionProtocolRegistration.getOffset();
		Integer offsetNew = collectionProtocolRegistration.getOffset();
		if (offsetNew != null)
		{
			int offset = 0;
			if (offsetOld != null)
				offset = offsetNew.intValue() - offsetOld.intValue();
			else
				offset = offsetNew.intValue() - 0;
			if (offset != 0)
			{
				//updateOffsetForEvents(dao, sessionDataBean, collectionProtocolRegistration, offset);
				checkAndUpdateChildOffset(dao, sessionDataBean, oldCollectionProtocolRegistration, offset);
				updateForOffset(dao, sessionDataBean, oldCollectionProtocolRegistration, offset);
			}
			/*else
			{
				//updateConsentResponseForSCG(collectionProtocolRegistration, dao, sessionDataBean);
			}*/
		}

		/* offset changes end */
		// Mandar 22-Jan-07 To disable consents accordingly in SCG and
		// Specimen(s) end
		// Update registration
		dao.update(persistentCPR,false);
		
		

		// Audit.
		dao.audit(obj, oldObj);

		// Disable all specimen Collection group under this registration.
		Logger.out.debug("collectionProtocolRegistration.getActivityStatus() " + collectionProtocolRegistration.getActivityStatus());
		if (collectionProtocolRegistration.getConsentWithdrawalOption().equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_RETURN)
				||collectionProtocolRegistration.getConsentWithdrawalOption().equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_DISCARD))
		{
			Logger.out.debug("collectionProtocolRegistration.getActivityStatus() " + collectionProtocolRegistration.getActivityStatus());
			Long collectionProtocolRegistrationIDArr[] = {collectionProtocolRegistration.getId()};

			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.disableRelatedObjects(dao, collectionProtocolRegistrationIDArr);
		}
	}

	private void updateConsentResponseForSCG(CollectionProtocolRegistration collectionProtocolRegistration, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException,DAOException
	{
		/*Collection specimenCollectionGroupCollection = (Collection) dao.retrieveAttribute(CollectionProtocolRegistration.class.getName(),
				collectionProtocolRegistration.getId(), Constants.COLUMN_NAME_SCG_COLL);*/
		Collection specimenCollectionGroupCollection = collectionProtocolRegistration.getSpecimenCollectionGroupCollection();
		Iterator specimenCollectionGroupIterator = specimenCollectionGroupCollection.iterator();
		while (specimenCollectionGroupIterator.hasNext())
		{
			SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) specimenCollectionGroupIterator.next();
			specimenCollectionGroup.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration);

			Collection specimenCollection = specimenCollectionGroup.getSpecimenCollection();
			if (specimenCollection != null && !specimenCollection.isEmpty())
			{
				Iterator itSpecimenCollection = specimenCollection.iterator();
				while (itSpecimenCollection.hasNext())
				{
					Specimen specimen = (Specimen) itSpecimenCollection.next();
					specimen.setConsentTierStatusCollectionFromSCG(specimenCollectionGroup);
					dao.update(specimen,false);
				}
			}
			
			dao.update(specimenCollectionGroup,false);
		}
	}

	public void postUpdate(DAO dao, Object currentObj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException
			
	{
		ParticipantRegistrationCacheManager participantRegCacheManager = new ParticipantRegistrationCacheManager();
		CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) currentObj;
		CollectionProtocolRegistration oldCollectionProtocolRegistration = (CollectionProtocolRegistration) oldObj;

		Long oldCPId = oldCollectionProtocolRegistration.getCollectionProtocol().getId();
		Long newCPId = collectionProtocolRegistration.getCollectionProtocol().getId();
		Long oldParticipantId = oldCollectionProtocolRegistration.getParticipant().getId();
		Long newParticipantId = collectionProtocolRegistration.getParticipant().getId();
		String oldProtocolParticipantId = oldCollectionProtocolRegistration.getProtocolParticipantIdentifier();

		if (oldProtocolParticipantId == null)
			oldProtocolParticipantId = "";

		String newProtocolParticipantId = collectionProtocolRegistration.getProtocolParticipantIdentifier();

		if (newProtocolParticipantId == null)
			newProtocolParticipantId = "";

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

	}
	
	private String[] getDynamicGroups(AbstractDomainObject obj)throws BizLogicException
	{
		try 
		{
			String[] dynamicGroups = null;
			CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) obj;
			dynamicGroups = new String[1];
			dynamicGroups[0] = CSMGroupLocator.getInstance().getPGName(collectionProtocolRegistration.getCollectionProtocol().getId(),Class.forName("edu.wustl.catissuecore.domain.CollectionProtocol"));
			return dynamicGroups;
		} catch (ApplicationException e) {
			throw getBizLogicException(e, "bizlogic.error", "");
		} catch (ClassNotFoundException e) {
			throw getBizLogicException(e, "bizlogic.error", "");
		}
		
	}

	private void registerParticipantAndProtocol(DAO dao, CollectionProtocolRegistration collectionProtocolRegistration,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		// Case of registering Participant on its participant ID

		try
		{
		Object collectionProtocolObj = dao.retrieveById(CollectionProtocol.class.getName(), collectionProtocolRegistration.getCollectionProtocol()
				.getId());

		if (collectionProtocolObj != null)
		{
			CollectionProtocol collectionProtocol = (CollectionProtocol) collectionProtocolObj;
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);

		}
		}
		catch(DAOException exp)
		{
			throw getBizLogicException(exp, "bizlogic.error", "");
		}
	}

	/**
	 * Add a dummy participant when participant is registed to a protocol using
	 * participant protocol id
	 */
	private Participant addDummyParticipant(DAO dao, SessionDataBean sessionDataBean) 
	throws BizLogicException,DAOException
	{
		Participant participant = new Participant();

		participant.setLastName("");
		participant.setFirstName("");
		participant.setMiddleName("");
		participant.setSocialSecurityNumber(null);
		participant.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);

		// Create a dummy participant medical identifier.
		Set partMedIdentifierColl = new HashSet();
		ParticipantMedicalIdentifier partMedIdentifier = new ParticipantMedicalIdentifier();
		partMedIdentifier.setMedicalRecordNumber(null);
		partMedIdentifier.setSite(null);
		partMedIdentifierColl.add(partMedIdentifier);

		dao.insert(participant, true);

		partMedIdentifier.setParticipant(participant);
		dao.insert(partMedIdentifier, true);

		/* inserting dummy participant in participant cache */
		ParticipantRegistrationCacheManager participantRegCache = new ParticipantRegistrationCacheManager();
		participantRegCache.addParticipant(participant);
		return participant;
	}

	/**
	 * Disable all the related collection protocol regitration for a given array
	 * of participant ids.
	 */
	public void disableRelatedObjectsForParticipant(DAO dao, Long participantIDArr[]) throws BizLogicException
	{
		List listOfSubElement = super.disableObjects(dao, CollectionProtocolRegistration.class, "participant", "CATISSUE_COLL_PROT_REG",
				"PARTICIPANT_ID", participantIDArr);
		if (!listOfSubElement.isEmpty())
		{
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.disableRelatedObjects(dao, Utility.toLongArray(listOfSubElement));
		}
	}

	/**
	 * Disable all the related collection protocol regitrations for a given
	 * array of collection protocol ids.
	 */
	public void disableRelatedObjectsForCollectionProtocol(DAO dao, Long collectionProtocolIDArr[]) throws BizLogicException
	{
		List listOfSubElement = super.disableObjects(dao, CollectionProtocolRegistration.class, "collectionProtocol", "CATISSUE_COLL_PROT_REG",
				"COLLECTION_PROTOCOL_ID", collectionProtocolIDArr);
		if (!listOfSubElement.isEmpty())
		{
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			bizLogic.disableRelatedObjects(dao, Utility.toLongArray(listOfSubElement));
		}
	}

	/**
	 * @param dao
	 * @param objectIds
	 * @param assignToUser
	 * @param roleId
	 * @throws DAOException
	 * @throws SMException
	 *//*
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
	 * @throws DAOException
	 *//*
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
	 *//*
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
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		CollectionProtocolRegistration registration = (CollectionProtocolRegistration) obj;

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
			throw getBizLogicException(null, "domain.object.null.err.msg", "Collection Protocol Registration");
		}
		Validator validator = new Validator();
		String message = "";
		if (registration.getCollectionProtocol() == null || registration.getCollectionProtocol().getId() == null)
		{
			message = ApplicationProperties.getValue("collectionprotocolregistration.protocoltitle");
			throw getBizLogicException(null, "errors.item.required", message);
		}

		String errorKey = validator.validateDate(Utility.parseDateToString(registration.getRegistrationDate(),CommonServiceLocator.getInstance().getDatePattern()),
				true);
		if (errorKey.trim().length() > 0)
		{
			message = ApplicationProperties.getValue("collectionprotocolregistration.date");
			throw getBizLogicException(null, "errors.item.required", message);
		}

		if (validator.isEmpty(registration.getProtocolParticipantIdentifier()))
		{
			if (registration.getParticipant() == null || registration.getParticipant().getId() == null)
			{
				throw getBizLogicException(null, "errors.collectionprotocolregistration.atleast", "");
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
			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(registration.getActivityStatus()))
			{
				throw getBizLogicException(null, "activityStatus.active.errMsg", "");
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, registration.getActivityStatus()))
			{
				throw getBizLogicException(null, "activityStatus.errMsg", "");
			}
		}

		return true;
	}

	public void checkUniqueConstraint(DAO dao, CollectionProtocolRegistration collectionProtocolRegistration,
			CollectionProtocolRegistration oldcollectionProtocolRegistration) throws BizLogicException
	{
		CollectionProtocol objCollectionProtocol = collectionProtocolRegistration.getCollectionProtocol();
		String sourceObjectName = collectionProtocolRegistration.getClass().getName();
		String[] selectColumns = null;
		String[] whereColumnName = null;
		String[] whereColumnCondition = new String[]{"=", "="};
		Object[] whereColumnValue = null;
		String arguments[] = null;
		String errMsg = "";
		// check for update opeartion and old values equals to new values
		int count = 0;

		/**
		 * Name : kalpana thakur Reviewer Name : Vaishali Bug ID: 4926
		 * Description: Combination of collection protocol id and protocol
		 * participant id should be unique.
		 */
		
		
		try
		{

			if (!(collectionProtocolRegistration.getProtocolParticipantIdentifier() == null)
					&& !(collectionProtocolRegistration.getProtocolParticipantIdentifier().equals("")))
			{ // build
				// query
				// for
				// collectionProtocol_id
				// AND
				// protocol_participant_id

				selectColumns = new String[]{"collectionProtocol.id", "protocolParticipantIdentifier"};
				arguments = new String[]{"Collection Protocol Registration ", "COLLECTION_PROTOCOL_ID,PROTOCOL_PARTICIPANT_ID"};

				QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
				queryWhereClause.addCondition(new EqualClause("collectionProtocol.id",objCollectionProtocol.getId())).andOpr().
				addCondition(new EqualClause("protocolParticipantIdentifier",collectionProtocolRegistration.
						getProtocolParticipantIdentifier()));
				List l = dao.retrieve(sourceObjectName, selectColumns,queryWhereClause);

				if (l.size() > 0)
				{

					if (oldcollectionProtocolRegistration == null
							|| !(collectionProtocolRegistration.getProtocolParticipantIdentifier().equals(oldcollectionProtocolRegistration
									.getProtocolParticipantIdentifier())))
					{
						// if list is not empty the Constraint Violation occurs
						Logger.out.debug("Unique Constraint Violated: " + l.get(0));
						errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.ConstraintViolation", arguments);
						Logger.out.debug("Unique Constraint Violated: " + errMsg);

						ErrorKey errorKey = ErrorKey.getErrorKey("bizlogic.error");
						throw new BizLogicException(errorKey,new Exception() ,"CollectionProtocolBizLogic.java :"+errMsg);
					}
					else
					{
						Logger.out.debug("Unique Constraint Passed");
					}
				}
				else
				{
					Logger.out.debug("Unique Constraint Passed");
				}

			}

			if (oldcollectionProtocolRegistration != null)
			{
				if (collectionProtocolRegistration.getParticipant() != null && oldcollectionProtocolRegistration.getParticipant() != null)
				{
					if (collectionProtocolRegistration.getParticipant().getId().equals(oldcollectionProtocolRegistration.getParticipant().getId()))
					{
						count++;
					}
					if (collectionProtocolRegistration.getCollectionProtocol().getId().equals(
							oldcollectionProtocolRegistration.getCollectionProtocol().getId()))
					{
						count++;
					}
				}
				else if (collectionProtocolRegistration.getProtocolParticipantIdentifier() != null
						&& oldcollectionProtocolRegistration.getProtocolParticipantIdentifier() != null)
				{
					if (collectionProtocolRegistration.getProtocolParticipantIdentifier().equals(
							oldcollectionProtocolRegistration.getProtocolParticipantIdentifier()))
					{
						count++;
					}
					if (collectionProtocolRegistration.getCollectionProtocol().getId().equals(
							oldcollectionProtocolRegistration.getCollectionProtocol().getId()))
					{
						count++;
					}
				}
				// if count=0 return i.e. old values equals new values
				if (count == 2)
					return;
			}
			if (collectionProtocolRegistration.getParticipant() != null)
			{
				// build query for collectionProtocol_id AND participant_id
				Participant objParticipant = collectionProtocolRegistration.getParticipant();
				selectColumns = new String[]{"collectionProtocol.id", "participant.id"};
				arguments = new String[]{"Collection Protocol Registration ", "COLLECTION_PROTOCOL_ID,PARTICIPANT_ID"};


				QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
				queryWhereClause.addCondition(new EqualClause("collectionProtocol.id",objCollectionProtocol.getId())).andOpr().
				addCondition(new EqualClause("participant.id", objParticipant.getId()));

				List l = dao.retrieve(sourceObjectName, selectColumns, queryWhereClause);
				if (l.size() > 0)
				{
					// if list is not empty the Constraint Violation occurs
					Logger.out.debug("Unique Constraint Violated: " + l.get(0));
					errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.ConstraintViolation", arguments);
					Logger.out.debug("Unique Constraint Violated: " + errMsg);
					ErrorKey errorKey = ErrorKey.getErrorKey("bizlogic.error");
					throw new BizLogicException(errorKey,new Exception() ,"CollectionProtocolBizLogic.java :"+errMsg);
				}
				else
				{
					Logger.out.debug("Unique Constraint Passed");
				}
			}

		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "bizlogic.error", "");
		}

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
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public List getAllParticipantRegistrationInfo() throws DAOException, ClassNotFoundException
	{
		List participantRegistrationInfoList = new Vector();

		// Getting all the CollectionProtocol those do not have activaityStatus
		// as 'Disabled'.
		String hql = "select cp.id ,cp.title, cp.shortTitle from " + CollectionProtocol.class.getName() + " as cp where  cp.activityStatus != '"
				+ Status.ACTIVITY_STATUS_DISABLED.toString() + "' and  (cp." + Constants.CP_TYPE + "= '" + Constants.PARENT_CP_TYPE + "' or cp.type = null)";

		DAO dao = openDAOSession();
		dao.openSession(null);

		List list = dao.executeQuery(hql);
		Logger.out.info("list size -----------:" + list.size());

		// Iterating over each Collection Protocol and finding out all its
		// registerd participant
		if (list != null)
		{
			for (int i = 0; i < list.size(); i++)
			{
				// Getitng participants for a particular CP.
				Object[] obj = (Object[]) list.get(i);
				Long cpId = (Long) obj[0];
				String cpTitle = (String) obj[1];
				String cpShortTitle = (String) obj[2];

				// Getting all active participant registered with CP
				hql = "select p.id, cpr.protocolParticipantIdentifier from " + CollectionProtocolRegistration.class.getName()
						+ " as cpr right outer join cpr.participant as p where cpr.participant.id = p.id and cpr.collectionProtocol.id = " + cpId
						+ " and cpr.activityStatus != '" + Status.ACTIVITY_STATUS_DISABLED.toString() + "' and p.activityStatus != '"
						+ Status.ACTIVITY_STATUS_DISABLED.toString() + "' order by p.id";

				List participantList = dao.executeQuery(hql);

				List participantInfoList = new ArrayList();
				// If registered participant found then add them to
				// participantInfoList
				for (int j = 0; j < participantList.size(); j++)
				{
					Object[] participantObj = (Object[]) participantList.get(j);

					Long participantID = (Long) participantObj[0];
					String protocolParticipantId = (String) participantObj[1];

					if (participantID != null)
					{
						String participantInfo = participantID.toString() + ":";
						if (protocolParticipantId != null && !protocolParticipantId.equals(""))
							participantInfo = participantInfo + protocolParticipantId;
						participantInfoList.add(participantInfo);

					}
				}

				// Creating ParticipanrRegistrationInfo object and storing in a
				// vector participantRegistrationInfoList.
				ParticipantRegistrationInfo prInfo = new ParticipantRegistrationInfo();
				prInfo.setCpId(cpId);
				prInfo.setCpTitle(cpTitle);
				prInfo.setCpShortTitle(cpShortTitle);
				prInfo.setParticipantInfoCollection(participantInfoList);
				participantRegistrationInfoList.add(prInfo);
			}
		}
		dao.closeSession();
		return participantRegistrationInfoList;
	}

	// Mandar : 11-Jan-07 For Consent Tracking Withdrawal -------- start
	/*
	 * verifyAndUpdateConsentWithdrawn(collectionProtocolRegistration)
	 * updateSCG(collectionProtocolRegistration, consentTierResponse)
	 * 
	 */

	/*
	 * This method verifies and updates SCG and child elements for withdrawn
	 * consents
	 */
	private void verifyAndUpdateConsentWithdrawn(CollectionProtocolRegistration collectionProtocolRegistration,
			CollectionProtocolRegistration oldCollectionProtocolRegistration, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		Collection newConsentTierResponseCollection = collectionProtocolRegistration.getConsentTierResponseCollection();
		Iterator itr = newConsentTierResponseCollection.iterator();
		while (itr.hasNext())
		{
			ConsentTierResponse consentTierResponse = (ConsentTierResponse) itr.next();
			if (consentTierResponse.getResponse().equalsIgnoreCase(Constants.WITHDRAWN))
			{
				long consentTierID = consentTierResponse.getConsentTier().getId().longValue();
				updateSCG(collectionProtocolRegistration, oldCollectionProtocolRegistration, consentTierID, dao, sessionDataBean);
			}
		}
	}

	/*
	 * This method updates all the scg's associated with the selected
	 * collectionprotocolregistration.
	 */
	private void updateSCG(CollectionProtocolRegistration collectionProtocolRegistration,
			CollectionProtocolRegistration oldCollectionProtocolRegistration, long consentTierID, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{

		Collection newScgCollection = new HashSet();
		Collection scgCollection = oldCollectionProtocolRegistration.getSpecimenCollectionGroupCollection();
		Iterator scgItr = scgCollection.iterator();

		while (scgItr.hasNext())
		{
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) scgItr.next();
			String cprWithdrawOption = collectionProtocolRegistration.getConsentWithdrawalOption();
			ConsentUtil.updateSCG(scg, consentTierID, cprWithdrawOption, dao, sessionDataBean);
			newScgCollection.add(scg); // set updated scg in cpr
		}
		collectionProtocolRegistration.setSpecimenCollectionGroupCollection(newScgCollection);
	}

	// Mandar : 11-Jan-07 For Consent Tracking Withdrawal -------- end

	/* offset changes 27th Dec 2007 */

	/**
	 * This method is called if any Offset is given for shift in anticipated
	 * dates. In this method complete traversal of all the CollectionProtocols
	 * is done and the below hierarchy registered CP's are shifted in
	 * anticipated dates by the number of days as specified by the offset.
	 * 
	 * @param dao
	 *            The DAO object
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param collectionProtocolRegistration
	 *            The CollectionProtocolRegistration Object
	 * @param offset
	 *            Offset value of number of days
	 * @throws DAOException
	 *             DAOException

	 */
	public void updateForOffset(DAO dao, SessionDataBean sessionDataBean, CollectionProtocolRegistration collectionProtocolRegistration, int offset)
			throws BizLogicException
	{
		CollectionProtocol child = collectionProtocolRegistration.getCollectionProtocol();
		Integer sequenceNumber = child.getSequenceNumber();
		CollectionProtocol parentCPofArm = child.getParentCollectionProtocol();
		
		try {
			if (parentCPofArm != null)
			{
				List childCPColl = getChildColl(parentCPofArm);
				Iterator iteratorofchildCP = childCPColl.iterator();
				while (iteratorofchildCP.hasNext())
				{
					CollectionProtocol cp = (CollectionProtocol) iteratorofchildCP.next();
					if (cp != null && cp.getSequenceNumber() != null)
					{
						if (cp.getSequenceNumber().intValue() > sequenceNumber.intValue())
						{
							CollectionProtocolRegistration cpr;

							cpr = getCPRbyCollectionProtocolIDAndParticipantID(dao, cp.getId(),
									collectionProtocolRegistration.getParticipant().getId());

							if (cpr != null)
							{
								cpr.setRegistrationDate(edu.wustl.catissuecore.util.global.Utility.getNewDateByAdditionOfDays(cpr.getRegistrationDate(),
										offset));
								//							Integer offsetToSet = cpr.getOffset();
								//							if (offsetToSet != null && offsetToSet.intValue() != 0)
								//							{
								//								cpr.setOffset(new Integer(offset + offsetToSet.intValue()));
								//							}
								//							else
								//								cpr.setOffset(new Integer(offset));
								//							updateOffsetForEvents(dao, sessionDataBean, cpr, offset);
								dao.update(cpr, false);

								checkAndUpdateChildOffset(dao, sessionDataBean, cpr, offset);
							}
						}

					}
				}

			}
			if (parentCPofArm != null)
			{
				child = parentCPofArm;
				CollectionProtocolRegistration cprforParent = getCPRbyCollectionProtocolIDAndParticipantID(dao, child.getId(),
						collectionProtocolRegistration.getParticipant().getId());
				updateForOffset(dao, sessionDataBean, cprforParent, offset);
			}

		} catch (DAOException e) {
			throw getBizLogicException(e, "bizlogic.error", "");
		}
	}

	/**
	 * This method is called if the CollectionProtocol has Offset and also has
	 * any Child Collection Protocols so as to shift there anticipated dates as
	 * per the Offset specified
	 * 
	 * @param dao
	 *            The DAO object
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param collectionProtocolRegistration
	 *            The CollectionProtocolRegistration Object
	 * @param offset
	 *            Offset value of number of days
	 * @throws DAOException
	 *             DAOException

	 */
	public void checkAndUpdateChildOffset(DAO dao, SessionDataBean sessionDataBean, CollectionProtocolRegistration collectionProtocolRegistration,
			int offset) throws BizLogicException
	{
		String status = Constants.CHILD_OFFSET;
		getCpAndCpr(dao, sessionDataBean, collectionProtocolRegistration,status);
	}

	/**
	 * @param dao
	 * @param sessionDataBean
	 * @param offset
	 * @param cp
	 * @param cpr
	 * @throws DAOException
	
	 */
	private boolean checkUpdateChildOffset(DAO dao, SessionDataBean sessionDataBean, int offset,
			CollectionProtocol cp, CollectionProtocolRegistration cpr) throws BizLogicException,DAOException
	{
		cpr.setRegistrationDate(edu.wustl.catissuecore.util.global.Utility.getNewDateByAdditionOfDays(cpr.getRegistrationDate(),
				offset));
		dao.update(cpr, false);
		if (cp.getChildCollectionProtocolCollection() != null && cp.getChildCollectionProtocolCollection().size() != 0)
		{
			return true; 
		}
		return false;
	}

	/**
	 * This method is called to fetch the CollectionProtocolRegistration Object
	 * from the database for the specified Collection Protocol Id and the
	 * specified Participant Id
	 * 
	 * @param dao
	 *            The DAO object
	 * @param CpId
	 *            The CollectionProtocol Id
	 * @param ParticipantId
	 *            the Participant Id
	 * @return CollectionProtocolRegistration The CollectionProtocolRegistration
	 *         Object retrieved from Database
	 * @throws DAOException
	 *             DAOException
	 */
	private CollectionProtocolRegistration getCPRbyCollectionProtocolIDAndParticipantID(DAO dao, Long CpId, Long ParticipantId) throws DAOException
	{
		String[] selectColumName = null;
		CollectionProtocolRegistration collectionProtocolRegistrationretrieve = null;
		String sourceObjectName = CollectionProtocolRegistration.class.getName();
		
		QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new EqualClause("collectionProtocol." + Constants.SYSTEM_IDENTIFIER,CpId)).andOpr().
		addCondition(new EqualClause("participant." + Constants.SYSTEM_IDENTIFIER,ParticipantId));
		
		List list = dao.retrieve(sourceObjectName, selectColumName,queryWhereClause);
		if (!list.isEmpty())
		{
			collectionProtocolRegistrationretrieve = (CollectionProtocolRegistration) list.get(0);
		}
		return collectionProtocolRegistrationretrieve;

	}

	/**
	 * This method is called when offset is specified for the CollectionProtocol
	 * This method shifts the anticipated dates for events by the number of
	 * offset days specified
	 * 
	 * @param dao
	 *            The DAO object
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param collectionProtocol
	 *            The CollectionProtocol Object
	 * @param offset
	 *            Offset value of number of days
	
	 * @throws DAOException
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
			throws DAOException, ClassNotFoundException
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
		return edu.wustl.catissuecore.util.global.Utility.hasPrivilegeToView(objName, identifier, sessionDataBean, getReadDeniedPrivilegeName());
	}
	
	/**
	 * @param CollectionProtocolRegistration Object
	 * @throws DAOException Database related Exception
	 */
	private void generateProtocolParticipantIdentifierLabel(CollectionProtocolRegistration 
			collectionProtocolRegistration)	throws BizLogicException
	{
		/**
		 * Call Protocol Participant Identifier label generator if automatic generation is specified
		 */
		if (edu.wustl.catissuecore.util.global.Variables.isProtocolParticipantIdentifierLabelGeneratorAvl)
		{
			try
			{
				TaskTimeCalculater labelGen = TaskTimeCalculater.startTask(
						"Time required for label Generator", CollectionProtocolRegistration.class);
				LabelGenerator spLblGenerator = LabelGeneratorFactory
						.getInstance(Constants.PROTOCOL_PARTICIPANT_IDENTIFIER_LABEL_GENERATOR_PROPERTY_NAME);
				spLblGenerator.setLabel(collectionProtocolRegistration);
				TaskTimeCalculater.endTask(labelGen);
			}
			catch (NameGeneratorException e)
			{
				throw getBizLogicException(e, "bizlogic.error", "");
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
}