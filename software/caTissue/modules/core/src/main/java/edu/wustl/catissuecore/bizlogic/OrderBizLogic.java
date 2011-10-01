/**
 * <p>
 * Title: Order Class>
 * <p>
 * Description: Bizlogic for Ordering System.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00 Created on October 10,2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.bean.RequestViewBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.OrderItem;
import edu.wustl.catissuecore.domain.PathologicalCaseOrderItem;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.factory.OrderDetailsFactory;
import edu.wustl.catissuecore.factory.utils.UserUtility;
import edu.wustl.catissuecore.uiobject.OrderUIObject;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.OrderingSystemUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.condition.INClause;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

public class OrderBizLogic extends CatissueDefaultBizLogic
{

	private static final Logger LOGGER = Logger.getCommonLogger(OrderBizLogic.class);
	// To calculate the Order status.
	private int orderStatusNew = 0;
	private int orderStatusPending = 0;
	private int orderStatusRejected = 0;
	private int orderStatusCompleted = 0;


	/**
	 * Saves the OrderDetails object in the database.
	 * @param obj
	 *            The OrderDetails Item object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param dao
	 *            object.
	 * @throws BizLogicException
	 *             object.
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		try
		{
			final OrderDetails order = (OrderDetails) obj;
			if(order.getDistributionProtocol() != null && order.getDistributionProtocol().getId() == -1)
			{
				order.setDistributionProtocol(null);
			}
			if(checkForValidItems(order,dao))
			{
				dao.insert(order);
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
	 * Checks if the ordered specimens meet the ordering pre requisites.
	 * @param order order
	 * @param dao object
	 * @return <CODE>true</CODE> the order is valid,
	 * <CODE>false</CODE> otherwise
	 * @throws BizLogicException BizLogicException
	 */
	private boolean checkForValidItems(final OrderDetails order, final DAO dao) throws BizLogicException
	{
		boolean checkOrder = true;
		final Collection<OrderItem>  orderItemColl = order.getOrderItemCollection();
		final Collection<OrderItem>  newOrderItemColl = new HashSet<OrderItem>();
		final QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		final Iterator<OrderItem> iter = orderItemColl.iterator();
		while (iter.hasNext())
		{
			final OrderItem orderItem = iter.next();
			if(orderItem instanceof ExistingSpecimenOrderItem)
			{
				final ExistingSpecimenOrderItem specimenOrder = (ExistingSpecimenOrderItem)orderItem;
				final Specimen specimen = specimenOrder.getSpecimen();
				if(!bizLogic.isSpecimenValidToOrder(dao, specimen.getId()))
				{
					newOrderItemColl.add(orderItem);
				}
			}
		}
		orderItemColl.removeAll(newOrderItemColl);
		if(orderItemColl.isEmpty())
		{
			checkOrder = false;
		}
		return checkOrder;
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj
	 *            The object to be updated.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param dao
	 *            object.
	 * @param oldObj
	 *            The old object from DB.
	 * @throws BizLogicException
	 *             object
	 */

	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		OrderUIObject orderUIObject = new OrderUIObject();
		update(dao,obj, oldObj,orderUIObject,sessionDataBean);

	}

	protected void update(DAO dao, Object obj, Object oldObj, Object uiObject, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		try
		{
			OrderUIObject orderUIObject = (OrderUIObject)uiObject;
			this.validate(obj, oldObj, dao, Constants.EDIT);
			final OrderDetails orderImplObj = (OrderDetails) HibernateMetaData
			.getProxyObjectImpl(oldObj);
			if(orderUIObject.getMailNotification()==null)
			{
				orderUIObject.setMailNotification(Boolean.FALSE);
			}
			final OrderDetails orderNew = this
			.updateObject(orderImplObj, obj, dao, orderUIObject,sessionDataBean);
			orderNew.setOrderItemCollection(orderNew.getOrderItemCollection());
			orderImplObj.setOrderItemCollection(orderNew.getOrderItemCollection());
			dao.update(orderNew,orderImplObj);
			this.disposeSpecimen(orderNew, sessionDataBean, dao);
			// Sending Email only if atleast one order item is updated.
			if (OrderDetailsFactory.numberItemsUpdated > 0 && orderUIObject.getMailNotification().booleanValue())
			{
				this.sendEmailOnOrderUpdate(dao, orderNew, sessionDataBean);
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
	 * @param obj
	 *            object
	 * @param oldObj
	 *            object
	 * @param dao
	 *            object
	 * @param operation
	 *            object
	 * @return boolean object
	 * @throws BizLogicException
	 *             object
	 */
	protected boolean validate(Object obj, Object oldObj, DAO dao, String operation)
	throws BizLogicException
	{
		final OrderDetails order = (OrderDetails) obj;
		final OrderDetails oldOrder = (OrderDetails) oldObj;
		this.checkDistributionPtorocol(order, dao);
		if ((oldOrder.getStatus().trim().equalsIgnoreCase("Pending") && order.getStatus().trim()
				.equalsIgnoreCase("New"))
				|| (oldOrder.getStatus().trim().equalsIgnoreCase("Rejected") && (order.getStatus()
						.trim().equalsIgnoreCase("New") || order.getStatus().trim()
						.equalsIgnoreCase("Pending")))
						|| (oldOrder.getStatus().trim().equalsIgnoreCase("Completed") && (order.getStatus()
								.trim().equalsIgnoreCase("New")
								|| order.getStatus().trim().equalsIgnoreCase("Pending") || order
								.getStatus().trim().equalsIgnoreCase("Rejected"))))
		{
			throw this.getBizLogicException(null, "orderdistribution.status.errmsg", "");
		}
		// Site is mandatory on UI.
		final Collection<Distribution> distributionCollection = order.getDistributionCollection();
		if (distributionCollection != null)
		{
			final Iterator<Distribution> distributionCollectionIterator = distributionCollection.iterator();
			while (distributionCollectionIterator.hasNext())
			{
				final Distribution distribution = (Distribution) distributionCollectionIterator
				.next();
				if (distribution.getToSite().getId().compareTo(new Long(-1)) == 0)
				{
					throw this.getBizLogicException(null, "orderdistribution.site.required.errmsg",
					"");
				}
			}
		}
		// Quantity is mandatory
		final Collection<OrderItem>  orderItemCollection = order.getOrderItemCollection();
		if(orderItemCollection!=null)
		{
			final Iterator<OrderItem> iter = orderItemCollection.iterator();
			while (iter.hasNext())
			{
				final OrderItem orderItem = iter.next();
				final Collection<OrderItem> oldOrderItemColl = oldOrder.getOrderItemCollection();
				if(oldOrderItemColl!=null)
				{
					final Iterator<OrderItem> oldOrderItemCollIter = oldOrderItemColl.iterator();
					while (oldOrderItemCollIter.hasNext())
					{
						final OrderItem oldorderItem = oldOrderItemCollIter.next();
						if (oldorderItem.getId().compareTo(orderItem.getId()) == 0)
						{
							// If requestFor drop down is null, do not allow
							// distribution of that order item
							if (orderItem.getStatus().equalsIgnoreCase(
									Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
									|| orderItem.getStatus().equalsIgnoreCase(
											Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)
											|| orderItem.getStatus().equalsIgnoreCase(
													Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
							{
								if (oldorderItem instanceof DerivedSpecimenOrderItem)
								{
									//							final DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem) oldorderItem;
									if (oldorderItem.getDistributedItem() == null
											&& orderItem.getDistributedItem().getSpecimen().getId() == null)
									{
										throw this.getBizLogicException(null,
												"orderdistribution.distribution.notpossible.errmsg", "");
									}

								}
								else if (oldorderItem instanceof NewSpecimenArrayOrderItem)
								{
									final SpecimenArray specimenArray = (SpecimenArray) this
									.getSpecimenArray(oldorderItem.getId(), dao);
									final NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) oldorderItem;
									newSpecimenArrayOrderItem.setSpecimenArray(specimenArray);
									if (newSpecimenArrayOrderItem.getSpecimenArray() == null)
									{
										throw this
										.getBizLogicException(
												null,
												"orderdistribution.distribution.arrayNotCreated.errmsg",
										"");
									}
								}
								else if (oldorderItem instanceof PathologicalCaseOrderItem)
								{
									final PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) oldorderItem;
									if (pathologicalCaseOrderItem.getSpecimenCollectionGroup()
											.getSpecimenCollection() == null
											|| pathologicalCaseOrderItem.getSpecimenCollectionGroup()
											.getSpecimenCollection().size() == 0)
									{
										if (orderItem.getStatus().equalsIgnoreCase(
												Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
										{
											throw this
											.getBizLogicException(
													null,
													"orderdistribution.arrayPrep.notpossibleForPatho.errmsg",
											"");
										}
										else
										{
											throw this
											.getBizLogicException(
													null,
													"orderdistribution.distribution.notpossibleForPatho.errmsg",
											"");
										}
									}
									if (pathologicalCaseOrderItem.getSpecimenCollectionGroup()
											.getSpecimenCollection() != null)
									{
										final Collection specimenColl = pathologicalCaseOrderItem
										.getSpecimenCollectionGroup().getSpecimenCollection();
										final Iterator specimenCollIter = specimenColl.iterator();
										final List<Specimen> totalChildrenSpecimenColl = new ArrayList<Specimen>();
										while (specimenCollIter.hasNext())
										{
											final Specimen specimen = (Specimen) specimenCollIter.next();
											final List childSpecimenCollection = OrderingSystemUtil
											.getAllChildrenSpecimen(specimen
													.getChildSpecimenCollection());
											List<Specimen> finalChildrenSpecimenCollection = null;
											if (pathologicalCaseOrderItem.getSpecimenClass() != null
													&& pathologicalCaseOrderItem.getSpecimenType() != null
													&& !pathologicalCaseOrderItem.getSpecimenClass().trim()
													.equalsIgnoreCase("")
													&& !pathologicalCaseOrderItem.getSpecimenType().trim()
													.equalsIgnoreCase(""))
											{ // "Derived"
												finalChildrenSpecimenCollection = OrderingSystemUtil
												.getChildrenSpecimenForClassAndType(
														childSpecimenCollection,
														pathologicalCaseOrderItem
														.getSpecimenClass(),
														pathologicalCaseOrderItem.getSpecimenType());
											}
											else
											{ // "Block" . Specimen class = "Tissue" ,
												// Specimen Type = "Block".
												finalChildrenSpecimenCollection = OrderingSystemUtil
												.getChildrenSpecimenForClassAndType(
														childSpecimenCollection, "Tissue", "Block");
											}
											// adding specimen to the collection
											if (specimen.getSpecimenClass().equalsIgnoreCase(
													pathologicalCaseOrderItem.getSpecimenClass())
													&& specimen.getSpecimenType().equalsIgnoreCase(
															pathologicalCaseOrderItem.getSpecimenType()))
											{
												finalChildrenSpecimenCollection.add(specimen);
											}
											if (finalChildrenSpecimenCollection != null)
											{
												final Iterator<Specimen> finalChildrenSpecimenCollectionIterator = finalChildrenSpecimenCollection
												.iterator();
												while (finalChildrenSpecimenCollectionIterator.hasNext())
												{
													totalChildrenSpecimenColl
													.add((finalChildrenSpecimenCollectionIterator
															.next()));
												}
											}
										}
										if (totalChildrenSpecimenColl == null)
										{
											if (orderItem
													.getStatus()
													.equalsIgnoreCase(
															Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
											{
												throw this.getBizLogicException(null,
														"orderdistribution.arrayPrep.notpossible.errmsg",
												"");
											}
											else
											{
												throw this
												.getBizLogicException(
														null,
														"orderdistribution.distribution.notpossible.errmsg",
												"");
											}
										}
									}
								}

								// Fix me
								// Removing distributed item in case of status - Ready
								// For Array Preparation
								if (orderItem.getStatus().equalsIgnoreCase(
										Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
								{
									final DistributedItem distributedItem = orderItem.getDistributedItem();
									if (distributedItem != null)
									{
										orderItem.setDistributedItem(null);
									}
								}
							}// END DISTRIBUTED
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * @param oldObj
	 *            object
	 * @param obj
	 *            object
	 * @param dao
	 *            object
	 * @param sessionDataBean
	 *            object
	 * @return OrderDetails object
	 * @throws BizLogicException
	 *             object.
	 */
	private OrderDetails updateObject(Object oldObj, Object obj, DAO dao,OrderUIObject orderUIObject,
			SessionDataBean sessionDataBean) throws BizLogicException
			{

		try
		{
			OrderDetails order = (OrderDetails) obj;
			final OrderDetails orderOld = (OrderDetails) oldObj;
			orderUIObject.setMailNotification(orderUIObject.getMailNotification());
			// Getting OrderItems Collection.
			final Collection<OrderItem>  oldOrderItemSet = orderOld.getOrderItemCollection();
			final Collection<OrderItem>  newOrderItemSet = order.getOrderItemCollection();
			// Adding items inside New Defined Array in oldOrderItemColl
			final Collection<OrderItem>  tempNewOrderItemSet = new HashSet<OrderItem> ();
			if (tempNewOrderItemSet.size() > 0)
			{
				newOrderItemSet.addAll(tempNewOrderItemSet);
			}
			// Iterating over OrderItems collection.
			final Iterator<OrderItem>  newSetIter = newOrderItemSet.iterator();
			OrderDetailsFactory.numberItemsUpdated = 0;
			// To insert distribution and distributed items only once.
			boolean isDistributionInserted = false;

			while (newSetIter.hasNext())
			{
				final OrderItem newOrderItem = newSetIter.next();

				final Iterator<OrderItem>  oldSetIter = oldOrderItemSet.iterator();

				while (oldSetIter.hasNext())
				{
					final OrderItem oldOrderItem = oldSetIter.next();

					// Setting quantity to previous quantity.....
					if (oldOrderItem.getId().equals(newOrderItem.getId()))
					{
						newOrderItem.setRequestedQuantity(oldOrderItem.getRequestedQuantity());
					}
					// Update Old OrderItem only when its Id matches with
					// NewOrderItem id and the order is not distributed and the
					// oldorderitem status and neworderitem status are different
					// or description has been updated.
					if ((oldOrderItem.getId().compareTo(newOrderItem.getId()) == 0)
							&& (oldOrderItem.getDistributedItem() == null)
							&& (!oldOrderItem.getStatus().trim().equalsIgnoreCase(
									newOrderItem.getStatus().trim()) || (oldOrderItem
											.getDescription() != null && !oldOrderItem.getDescription()
											.equalsIgnoreCase(newOrderItem.getDescription()))))
					{
						if (newOrderItem.getStatus().trim().equalsIgnoreCase(
								Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
								|| newOrderItem.getStatus().trim().equalsIgnoreCase(
										Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE))
						{
							if (!isDistributionInserted)
							{
								// Direct call to DistributionBizLogic
								final Collection<Distribution> newDistributionColl = order
								.getDistributionCollection();
								if(newDistributionColl!=null)
								{
									final Iterator<Distribution> iter = newDistributionColl.iterator();
									final IFactory factory = AbstractFactoryConfig.getInstance()
									.getBizLogicFactory();
									final DistributionBizLogic distributionBizLogic = (DistributionBizLogic) factory
									.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
									while (iter.hasNext())
									{
										final Distribution distribution = iter.next();
										// Populating Distribution Object.
										distribution.setOrderDetails(orderOld);
										// Setting the user for distribution.
										InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
										final User user = instFact.createObject();
										if (sessionDataBean.getUserId() == null)
										{
											user.setId(distribution.getDistributedBy().getId());
										}
										else
										{
											user.setId(sessionDataBean.getUserId());
										}
										distribution.setDistributedBy(user);
										// Setting activity status
										distribution.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE
												.toString());
										// Setting Event Timestamp
										final Date date = new Date();
										distribution.setTimestamp(date);
										// Inserting distribution
										distributionBizLogic.insert(distribution, dao, sessionDataBean);
										isDistributionInserted = true;
									}
								}

							}
						}
						// Setting Description and Status.
						if (newOrderItem.getDescription() != null)
						{
							if (newOrderItem instanceof ExistingSpecimenArrayOrderItem)
							{
								newOrderItem.setDescription(oldOrderItem.getDescription() + " "
										+ newOrderItem.getDescription());
							}
						}
						// The number of Order Items updated.
						OrderDetailsFactory.numberItemsUpdated++;
					}
					else if ((oldOrderItem.getId().compareTo(newOrderItem.getId()) == 0)
							&& oldOrderItem.getDistributedItem() != null)
					{
						final Object object = dao.retrieveById(DistributedItem.class.getName(),
								oldOrderItem.getDistributedItem().getId());
						if (object != null)
						{
							final DistributedItem distributedItem = (DistributedItem) object;
							newOrderItem.setDistributedItem(distributedItem);
						}

					}
				}
				this.calculateOrderStatus(newOrderItem);
			}
			// Updating comments.
			if (order.getComment() != null && !order.getComment().trim().equalsIgnoreCase(""))
			{
				order.setComment(orderOld.getComment() + " " + order.getComment());
			}
			// Bug #12262
			final Collection<OrderItem> updatedOrderItemSet = new HashSet<OrderItem>();
			final Iterator<OrderItem> itr = newOrderItemSet.iterator();

			while (itr.hasNext())
			{
				final OrderItem newOrderItem = itr.next();
				if (newOrderItem instanceof SpecimenArrayOrderItem)
				{
					updatedOrderItemSet.add(newOrderItem);
				}
				else
				{
					if (((SpecimenOrderItem) newOrderItem).getNewSpecimenArrayOrderItem() == null)
					{
						updatedOrderItemSet.add(newOrderItem);
					}
				}
			}

			// For order status.
			order = this.updateOrderStatus(order, updatedOrderItemSet);
			return order;
		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
			}

	/**
	 *
	 * @param dao : dao
	 * @param order : order
	 * @param sessionDataBean : sessionDataBean
	 */
	private void sendEmailOnOrderUpdate(DAO dao, OrderDetails order, SessionDataBean sessionDataBean)
	{
		final EmailHandler emailHandler = new EmailHandler();
		final String ccEmailAddress = order.getDistributionProtocol().getPrincipalInvestigator()
		.getEmailAddress(); // cc
		final String toEmailAddress = sessionDataBean.getUserName(); // to person
		// ordering
		final String fromEmailAddress = XMLPropertyHandler
		.getValue("email.administrative.emailAddress"); // from
		// bcc
		// ccEmailAddress += ccEmailAddress +" "+fromEmailAddress;
		final String bccEmailAddress = fromEmailAddress;
		final String subject = "Notification on Order " + order.getName()
		+ " Status from caTissue Administrator";
		final String body = this.makeEmailBodyForOrderUpdate(dao, order);
		emailHandler.sendEmailForOrderDistribution(body, toEmailAddress, fromEmailAddress,
				ccEmailAddress, bccEmailAddress, subject);
	}

	/**
	 *
	 * @param dao : dao
	 * @param order : order
	 * @return String
	 */
	private String makeEmailBodyForOrderUpdate(DAO dao, OrderDetails order)
	{
		final Collection<OrderItem> orderItemColl = order.getOrderItemCollection();
		String emailFormat = "";

		final String emailBodyHeader = "Hello "
			+ order.getDistributionProtocol().getPrincipalInvestigator().getFirstName()
			+ " "
			+ order.getDistributionProtocol().getPrincipalInvestigator().getLastName()
			+ ",  \n\n"
			+ "This is in relation with the order you placed with us. Please find below the details on its status. \n\n";

		final Iterator<OrderItem>  iter = orderItemColl.iterator();
		int serialNo = 1;
		String emailBody = "";
		while (iter.hasNext())
		{
			final OrderItem orderItem = iter.next();

			if (orderItem instanceof ExistingSpecimenOrderItem)
			{
				final ExistingSpecimenOrderItem existingSpecimenOrderItem = (ExistingSpecimenOrderItem) orderItem;
				emailBody = emailBody + serialNo + ". "
				+ existingSpecimenOrderItem.getSpecimen().getLabel() + ": "
				+ existingSpecimenOrderItem.getStatus() + "\n   Order Description: "
				+ existingSpecimenOrderItem.getDescription() + "\n\n";

				serialNo++;
			}
			else if (orderItem instanceof DerivedSpecimenOrderItem)
			{
				final DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem) orderItem;
				emailBody = emailBody + serialNo + ". "
				+ derivedSpecimenOrderItem.getParentSpecimen().getLabel() + ": "
				+ derivedSpecimenOrderItem.getStatus() + "\n   Order Description: "
				+ derivedSpecimenOrderItem.getDescription() + "\n\n";

				serialNo++;

			}
			else if (orderItem instanceof PathologicalCaseOrderItem)
			{
				final PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) orderItem;
				emailBody = emailBody
				+ serialNo
				+ ". "
				+ pathologicalCaseOrderItem.getSpecimenCollectionGroup()
				.getSurgicalPathologyNumber() + ": "
				+ pathologicalCaseOrderItem.getStatus() + "\n   Order Description: "
				+ pathologicalCaseOrderItem.getDescription() + "\n\n";

				serialNo++;

			}
			else if (orderItem instanceof NewSpecimenArrayOrderItem)
			{
				final NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) orderItem;
				emailBody = emailBody + serialNo + ". " + newSpecimenArrayOrderItem.getName()
				+ ": " + newSpecimenArrayOrderItem.getStatus() + "\n   Order Description: "
				+ newSpecimenArrayOrderItem.getDescription() + "\n\n";

				serialNo++;

			}
			else if (orderItem instanceof ExistingSpecimenArrayOrderItem)
			{
				final ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = (ExistingSpecimenArrayOrderItem) orderItem;
				emailBody = emailBody + serialNo + ". "
				+ existingSpecimenArrayOrderItem.getSpecimenArray().getName() + ": "
				+ existingSpecimenArrayOrderItem.getStatus() + "\n   Order Description: "
				+ existingSpecimenArrayOrderItem.getDescription() + "\n\n";

				serialNo++;
			}
		}
		final String emailMsgFooterRegards = "\n" + "Regards, ";
		final String emailMsgFooterSign = "\n" + "caTissueSuite Administrator";
		final String emailMsgFooter = emailMsgFooterRegards + emailMsgFooterSign;
		emailFormat = emailBodyHeader + emailBody + emailMsgFooter;
		return emailFormat;
	}

	/**
	 * @param oldOrderItem
	 *            object
	 */
	private void calculateOrderStatus(OrderItem oldOrderItem)
	{
		// Order id is null for specimen orderItems associated with
		// NewSpecimenArrayOrderItem
		if (oldOrderItem.getOrderDetails() != null)
		{
			// For order status
			if (oldOrderItem.getStatus().trim()
					.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_NEW))
			{
				//As oldOrderItem is instanceof NewSpecimenArrayOrderItem
				//casting it in SpecimenOrderItem is giving java.lang.ClassCastException.
				if(oldOrderItem instanceof SpecimenOrderItem)//Bug 14316
				{
					if(((SpecimenOrderItem) oldOrderItem).getNewSpecimenArrayOrderItem() == null)
					{
						this.orderStatusNew++;
					}
				}

			}// kalpana bug #5839 If the specimens inside the specimen Array and
			// if it's status is
			// ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION then mark it
			// complete.
			else if ((oldOrderItem.getStatus().trim().equalsIgnoreCase(
					Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
					|| oldOrderItem.getStatus().trim().equalsIgnoreCase(
							Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE) || oldOrderItem
							.getStatus().trim().equalsIgnoreCase(
									Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION)))
			{
				if ((oldOrderItem instanceof SpecimenArrayOrderItem)
						|| ((SpecimenOrderItem) oldOrderItem).getNewSpecimenArrayOrderItem() == null)
				{
					this.orderStatusCompleted++;
				}
			}
			else if (oldOrderItem.getStatus().trim().equalsIgnoreCase(
					Constants.ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION)
					|| oldOrderItem.getStatus().trim().equalsIgnoreCase(
							Constants.ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW)
							|| oldOrderItem.getStatus().trim().equalsIgnoreCase(
									Constants.ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION))
			{
				this.orderStatusPending++;
			}
			else if (oldOrderItem.getStatus().trim().equalsIgnoreCase(
					Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST)
					|| oldOrderItem.getStatus().trim().equalsIgnoreCase(
							Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE)
							|| oldOrderItem.getStatus().trim().equalsIgnoreCase(
									Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE))
			{
				if ((oldOrderItem instanceof SpecimenArrayOrderItem)
						|| ((SpecimenOrderItem) oldOrderItem).getNewSpecimenArrayOrderItem() == null)
				{
					this.orderStatusRejected++;
				}
			}
		}
	}

	/**
	 * @param orderNew
	 *            object
	 * @param oldOrderItemSet
	 *            object
	 * @return OrderDetails object.
	 */
	private OrderDetails updateOrderStatus(OrderDetails orderNew, Collection<OrderItem> oldOrderItemSet)
	{
		if (this.orderStatusNew == oldOrderItemSet.size())
		{
			orderNew.setStatus(Constants.ORDER_STATUS_NEW);
		}
		else if (this.orderStatusRejected == oldOrderItemSet.size())
		{
			orderNew.setStatus(Constants.ORDER_STATUS_REJECTED);
		}
		else if ((this.orderStatusCompleted == oldOrderItemSet.size())
				|| ((this.orderStatusCompleted + this.orderStatusRejected) == oldOrderItemSet
						.size()))
		{
			orderNew.setStatus(Constants.ORDER_STATUS_COMPLETED);
		}
		else
		{
			orderNew.setStatus(Constants.ORDER_STATUS_PENDING);
		}

		return orderNew;
	}

	/**
	 * This function prepares email body for the Order Placement Mail
	 * @param userObj
	 *            User object containing the logged in user info
	 * @param order
	 *            OrderDetails instance containing details of the requested
	 *            order and order items under that order
	 * @param dao
	 *            object
	 * @return emailBody String containing the email message body
	 * @throws DAOException
	 *             object
	 */
	private String makeEmailBodyForOrderPlacement(User userObj, OrderDetails order, DAO dao)
	throws DAOException
	{
		final String emailBodyHeader = "Dear caTissue Administrator ,";

		final Object object = dao.retrieveById(DistributionProtocol.class.getName(), order
				.getDistributionProtocol().getId());
		final String messageLine1 = "This is to notify that the Order " + order.getName()
		+ " requested by " + userObj.getFirstName() + " " + userObj.getLastName()
		+ " under Distribution Protocol " + ((DistributionProtocol) object).getTitle()
		+ " have been placed successfully.";

		// String messageLine2 = "The details of the Order are as follows:";

		// String emailMsgHeader = emailBodyHeader + "\n" + messageLine1 + "\n"
		// + messageLine2 + "\n";
		final String emailMsgHeader = emailBodyHeader + "\n" + messageLine1 + "\n";
		// String emailMsgFooter = "\n" +
		// "We will get back to you shortly with status of each of the items requested"
		// ;
		final String emailMsgFooterRegards = "\n" + "Regards, ";
		final String emailMsgFooterSign = "\n" + "caTissue Administrator";
		final String emailMsgFooter = emailMsgFooterRegards + emailMsgFooterSign;
		return emailMsgHeader + emailMsgFooter;
	}

	// Populates a List of RequestViewBean objects to display the request list
	// on RequestListAdministratorView.jsp
	/**
	 * @param requestStatusSelected
	 *            object
	 * @param userName : userName
	 * @param userId : userId
	 * @return requestList List
	 * @throws BizLogicException
	 *             object
	 */
	public List<RequestViewBean> getRequestList(String requestStatusSelected, String userName, Long userId)
	throws BizLogicException
	{

		DAO dao = null;
		final List<OrderDetails> orderList = new ArrayList<OrderDetails>();
		List<RequestViewBean> requestViewBeanList = new ArrayList<RequestViewBean>();
		try
		{
			dao = this.openDAOSession(null);
			final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
			final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(userName);

			final User user = this.getUser(dao, userId);
			final List<Long> siteIdsList = (List<Long>) this.getUserSitesWithDistributionPrev(user,
					privilegeCache);

			List orderListFromDB = null;
			/*
			 * String[] whereColumnName = {"status"}; String[]
			 * whereColumnCondition = {"in"};
			 */
			final String[] selectColName = null;

			if (!requestStatusSelected.trim().equalsIgnoreCase("All"))
			{
				final List<String> whereColValues = new ArrayList<String>();
				whereColValues.add(new String("All"));

				// Object [] whereColumnValues = {whereColValues.toArray()};

				final QueryWhereClause queryWhereClause = new QueryWhereClause(OrderDetails.class
						.getName());
				queryWhereClause.addCondition(new INClause("status", whereColValues.toArray()));
				orderListFromDB = dao.retrieve(OrderDetails.class.getName(), selectColName,
						queryWhereClause);

			}
			else
			{
				final List<String> whereColValues = new ArrayList<String>();
				whereColValues.add(new String("Pending"));
				whereColValues.add(new String("New"));

				final QueryWhereClause queryWhereClause = new QueryWhereClause(OrderDetails.class
						.getName());
				queryWhereClause.addCondition(new INClause("status", whereColValues.toArray()));

				final Object[] whereColumnValues = {whereColValues.toArray()};
				orderListFromDB = dao.retrieve(OrderDetails.class.getName(), selectColName,
						queryWhereClause);

			}

			final Iterator orderListFromDBIterator = orderListFromDB.iterator();
			while (orderListFromDBIterator.hasNext())
			{
				final OrderDetails orderDetails = (OrderDetails) orderListFromDBIterator.next();
				final boolean hasDributionPrivilegeOnSite = this.isOrderItemValidTodistribute(
						orderDetails.getOrderItemCollection(), siteIdsList);
				final SessionDataBean sdb = new SessionDataBean();
				sdb.setUserId(userId);
				sdb.setUserName(userName);
				final boolean hasDistributionPrivilegeOnCp = this.checkDistributionPrivilegeOnCP(
						user, privilegeCache, orderDetails.getOrderItemCollection(), sdb);
				if (this.isSuperAdmin(user) || hasDributionPrivilegeOnSite
						|| hasDistributionPrivilegeOnCp)
				{
					orderList.add(orderDetails);
				}
			}
			requestViewBeanList = (List<RequestViewBean>) this.populateRequestViewBeanList(orderList);

		}
		catch (final SMException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw AppUtility.handleSMException(e);
		}
		catch (final DAOException exp)
		{
			LOGGER.error(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return requestViewBeanList;
	};

	/**
	 * Site of the order item should be one in the site list having distribution
	 * privilege.
	 * @param orderItemCollection : orderItemCollection
	 * @param siteIdsList : siteIdsList
	 * @return boolean
	 */
	private boolean isOrderItemValidTodistribute(Collection<OrderItem> orderItemCollection, List<Long> siteIdsList)
	{
		boolean isValidToDistribute = false;
		if(orderItemCollection!=null)
		{
			final Iterator<OrderItem> orderItemColItr = orderItemCollection.iterator();
			while (orderItemColItr.hasNext())
			{
				final OrderItem orderItem = orderItemColItr.next();
				if (orderItem instanceof ExistingSpecimenOrderItem)
				{
					final ExistingSpecimenOrderItem existingSpecimenOrderItem = (ExistingSpecimenOrderItem) orderItem;
					if (existingSpecimenOrderItem.getSpecimen() != null)
					{
						final SpecimenPosition specimenPosition = existingSpecimenOrderItem
						.getSpecimen().getSpecimenPosition();
						if (specimenPosition != null
								&& !(existingSpecimenOrderItem.getStatus().equals(
										Constants.ORDER_REQUEST_STATUS_DISTRIBUTED) || existingSpecimenOrderItem
										.getStatus().equals(
												Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)))
						{
							if (siteIdsList.contains(existingSpecimenOrderItem.getSpecimen()
									.getSpecimenPosition().getStorageContainer().getSite().getId()))
							{
								isValidToDistribute = true;
								break;
							}
						}
					}

				}
				else if (orderItem instanceof ExistingSpecimenArrayOrderItem)
				{
					final ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = (ExistingSpecimenArrayOrderItem) orderItem;
					if (existingSpecimenArrayOrderItem.getSpecimenArray() != null
							&& existingSpecimenArrayOrderItem.getSpecimenArray().getLocatedAtPosition() != null)
					{
						final StorageContainer storageContainer = (StorageContainer) existingSpecimenArrayOrderItem
						.getSpecimenArray().getLocatedAtPosition().getParentContainer();
						if (siteIdsList.contains(storageContainer.getSite().getId()))
						{
							isValidToDistribute = true;
							break;
						}
					}
				}
				else if (orderItem instanceof DerivedSpecimenOrderItem)
				{
					final DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem) orderItem;
					if (derivedSpecimenOrderItem.getParentSpecimen() != null)
					{
						final SpecimenPosition specimenPosition = (SpecimenPosition) derivedSpecimenOrderItem
						.getParentSpecimen().getSpecimenPosition();
						if (specimenPosition != null
								&& !(derivedSpecimenOrderItem.getStatus().equals(
										Constants.ORDER_REQUEST_STATUS_DISTRIBUTED) || derivedSpecimenOrderItem
										.getStatus().equals(
												Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)))
						{
							final Long siteId = (Long) specimenPosition.getStorageContainer().getSite()
							.getId();
							if (siteIdsList.contains(siteId))
							{
								isValidToDistribute = true;
								break;
							}
						}
					}

				}
			}
		}

		return isValidToDistribute;
	}

	/**
	 * Retrieve all the sites of the user having distribution privilege.
	 * @param dao : dao
	 * @param userId : userId
	 * @param privilegeCache : privilegeCache
	 * @return List
	 * @throws BizLogicException : BizLogicException
	 */
	public List<Long> getRelatedSiteIds(HibernateDAO dao, Long userId, PrivilegeCache privilegeCache)
	throws BizLogicException
	{
		final List<Long> siteCollWithDistriPri = new ArrayList<Long>();
		try
		{
			final User user = (User) dao.retrieveById(User.class.getName(), userId);
			this.getSiteIds(privilegeCache, siteCollWithDistriPri, user);
		}

		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		return (List<Long>) siteCollWithDistriPri;
	}

	/**
	 * Method called to get all the site ids.
	 * @param privilegeCache
	 * @param user
	 * @return
	 * @throws BizLogicException
	 */
	private void getSiteIds(PrivilegeCache privilegeCache, List<Long> siteCollWithDistriPri,
			final User user) throws BizLogicException
			{
		String objectId = "";
		try
		{
			if (!Constants.ADMIN_USER.equalsIgnoreCase(UserUtility.getRoleId(user)))
			{
				final Collection<Site> siteCollection = user.getSiteCollection();
				if(siteCollection!=null)
				{
					final Iterator<Site> siteCollectionItr = siteCollection.iterator();
					while (siteCollectionItr.hasNext())
					{
						final Site site = siteCollectionItr.next();
						objectId = Constants.SITE_CLASS_NAME + "_" + site.getId();

						final boolean isAuthorized = privilegeCache.hasPrivilege(objectId,
								Variables.privilegeDetailsMap.get(Constants.DISTRIBUTE_SPECIMENS));
						if (isAuthorized)
						{
							siteCollWithDistriPri.add(site.getId());
						}

					}
				}
			}
		}
		catch (final SMException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, "sm.priv.error", objectId);
		}
			}

	/**
	 *
	 * @param user : user
	 * @param privilegeCache : privilegeCache
	 * @param orderItemCollection : orderItemCollection
	 * @param sessionDataBean : sessionDataBean
	 * @return boolean
	 * @throws BizLogicException  :BizLogicException
	 */
	public boolean checkDistributionPrivilegeOnCP(User user, PrivilegeCache privilegeCache,
			Collection orderItemCollection, SessionDataBean sessionDataBean)
	throws BizLogicException
	{

		boolean hasDistributionPrivilege = false;
		String objectId = "";

		try
		{
			final Iterator orderItemColItr = orderItemCollection.iterator();

			while (orderItemColItr.hasNext())
			{
				final OrderItem orderItem = (OrderItem) orderItemColItr.next();
				if (orderItem instanceof PathologicalCaseOrderItem)
				{
					final PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) orderItem;
					final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) pathologicalCaseOrderItem
					.getSpecimenCollectionGroup();
					if (specimenCollectionGroup != null
							&& !(pathologicalCaseOrderItem.getStatus().equals(
									Constants.ORDER_REQUEST_STATUS_DISTRIBUTED) || pathologicalCaseOrderItem
									.getStatus().equals(
											Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)))
					{

						final Long cpId = specimenCollectionGroup
						.getCollectionProtocolRegistration().getCollectionProtocol()
						.getId();
						objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cpId;
						boolean isAuthorized = privilegeCache.hasPrivilege(objectId,
								Variables.privilegeDetailsMap.get(Constants.DISTRIBUTE_SPECIMENS));
						if (!isAuthorized)
						{
							isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(
									Permissions.DISTRIBUTION, sessionDataBean, cpId.toString());
						}

						if (isAuthorized)
						{
							hasDistributionPrivilege = true;
							break;
						}
					}
				}
			}
		}
		catch (final SMException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, "sm.priv.error", objectId);
		}

		return hasDistributionPrivilege;
	}

	/**
	 * It returns the user object
	 * @param dao : dao
	 * @param userId : userId
	 * @return User
	 * @throws DAOException : DAOException
	 */
	private User getUser(DAO dao, Long userId) throws DAOException
	{
		return (User) dao.retrieveById(User.class.getName(), userId);
	}

	/**
	 * Get the list of sites of user on which user have distribution privilege
	 * @param user : user
	 * @param privilegeCache : privilegeCache
	 * @return List
	 * @throws BizLogicException : BizLogicException
	 */
	public List<Long> getUserSitesWithDistributionPrev(User user, PrivilegeCache privilegeCache)
	throws BizLogicException
	{

		final List<Long> siteCollWithDistriPri = new ArrayList<Long>();
		this.getSiteIds(privilegeCache, siteCollWithDistriPri, user);

		return (List<Long>) siteCollWithDistriPri;

	}

	/**
	 * Check for superAdmin
	 * @param user : user
	 * @return boolean
	 */
	public boolean isSuperAdmin(User user)
	{
		boolean isSuperAdmin = false;

		if (Constants.ADMIN_USER.equalsIgnoreCase(UserUtility.getRoleId(user)))
		{
			isSuperAdmin = true;
		}

		return isSuperAdmin;

	}

	/**
	 * To populate the request view bean
	 * @param orderListFromDB
	 *            object
	 * @return List object
	 */
	private List<RequestViewBean> populateRequestViewBeanList(List<OrderDetails> orderListFromDB)
	{
		final List<RequestViewBean> requestList = new ArrayList<RequestViewBean>();
		RequestViewBean requestViewBean = null;
		OrderDetails order = null;
		if (orderListFromDB != null)
		{
			final Iterator<OrderDetails> iter = orderListFromDB.iterator();
			while (iter.hasNext())
			{
				order = iter.next();
				requestViewBean = OrderingSystemUtil.getRequestViewBeanToDisplay(order);

				requestViewBean.setRequestId(order.getId().toString());
				requestViewBean.setStatus(order.getStatus());

				requestList.add(requestViewBean);
			}
		}
		return requestList;
	}

	/**
	 * @param identifier : id
	 * @param dao : dao
	 * @return to retrieve the orderDetails object.
	 * @throws BizLogicException : BizLogicException
	 */
	public OrderDetails getOrderListFromDB(String identifier, DAO dao) throws BizLogicException
	{
		try
		{
			return (OrderDetails)dao
			.retrieveById(OrderDetails.class.getName(), Long.parseLong(identifier));
		}
		catch (final NumberFormatException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, "number.format.exp", "");
		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 *
	 * @param distributionProtId : distributionProtId
	 * @return DistributionProtocol
	 */
	public DistributionProtocol retrieveDistributionProtocol(String distributionProtId)
	{
		DAO dao = null;
		try
		{
			dao = this.openDAOSession(null);
			DistributionProtocol distributionProtocol=null;
				if(distributionProtId.lastIndexOf('(')!= -1)
				{
					distributionProtId = distributionProtId.substring(0,distributionProtId.lastIndexOf("("));
				}
				final String cprHQL = "select distProt.id"
					+ " from edu.wustl.catissuecore.domain.DistributionProtocol as distProt" +
							" where distProt.title = '"+distributionProtId+"'";
				final List dataList = AppUtility.executeQuery(cprHQL);
				if(!dataList.isEmpty())
				{
					distributionProtId = dataList.get(0).toString();
					distributionProtocol = (DistributionProtocol) dao
					.retrieveById(DistributionProtocol.class.getName(), Long
						.parseLong(distributionProtId));
				}
			return distributionProtocol;
		}
		catch (final Exception e)
		{
			LOGGER.error(e.getMessage(), e);
			return null;
		}
		finally
		{
			try
			{
				this.closeDAOSession(dao);
			}
			catch (BizLogicException e)
			{
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * @param request
	 *            HttpServletRequest object
	 * @return List specimen array objects
	 * @throws BizLogicException : BizLogicException
	 */
	public List<SpecimenArray> getSpecimenArrayDataFromDatabase(HttpServletRequest request)
	throws BizLogicException
	{
		final long startTime = System.currentTimeMillis();
		DAO dao = null;
		try
		{

			final HttpSession session = request.getSession(true);

			final String sourceObjectName = SpecimenArray.class.getName();
			final List valueField = (List) session.getAttribute(Constants.SPECIMEN_ARRAY_ID);
			dao = this.openDAOSession(null);
			final List<SpecimenArray> specimenArrayList = new ArrayList<SpecimenArray>();
			if (valueField != null && valueField.size() > 0)
			{
				for (int i = 0; i < valueField.size(); i++)
				{
					// List SpecimenArray = bizLogic.retrieve(sourceObjectName,
					// columnName, (String)valueField.get(i));
					final Object object = dao.retrieveById(sourceObjectName, Long
							.parseLong((String) valueField.get(i)));
					final SpecimenArray specArray = (SpecimenArray) object;
					specArray.getSpecimenArrayType();
					specArray.getSpecimenArrayType().getSpecimenTypeCollection();
					specimenArrayList.add(specArray);
				}
			}

			final long endTime = System.currentTimeMillis();
			LOGGER.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB -  : "
					+ (endTime - startTime));
			return specimenArrayList;
		}
		catch (final Exception e)
		{
			LOGGER.error(e.getMessage(), e);
			return null;
		}
		finally
		{
			this.closeDAOSession(dao);
		}

	}

	/**
	 * function for getting data from database
	 * @param request
	 *            HttpServletRequest object
	 * @return List of specimen objects
	 * @throws BizLogicException : BizLogicException
	 */
	public List<Specimen> getSpecimenDataFromDatabase(HttpServletRequest request) throws BizLogicException
	{
		// to get data from database when specimen id is given
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		final HttpSession session = request.getSession(true);

		final long startTime = System.currentTimeMillis();
		DAO dao = null;
		final List<Specimen> specimenList = new ArrayList<Specimen>();
		try
		{

			dao = this.openDAOSession(null);
			final String sourceObjectName = Specimen.class.getName();
			final String columnName = "id";
			final List valueField = (List) session.getAttribute("specimenId");

			if (valueField != null && valueField.size() > 0)
			{

				for (int i = 0; i < valueField.size(); i++)
				{
					final Object object = dao.retrieveById(sourceObjectName, Long
							.parseLong((String) valueField.get(i)));
					final Specimen spec = (Specimen) object;
					specimenList.add(spec);
				}

			}
			final long endTime = System.currentTimeMillis();
			LOGGER.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB -  : "
					+ (endTime - startTime));

		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return specimenList;
	}

	/**
	 * @param request
	 *            HttpServletRequest object
	 * @return List of Pathology Case objects
	 * @throws BizLogicException : BizLogicException
	 */
	public List<SurgicalPathologyReport> getPathologyDataFromDatabase(HttpServletRequest request) throws BizLogicException
	{

		// to get data from database when specimen id is given
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.NEW_PATHOLOGY_FORM_ID);
		final List<SurgicalPathologyReport> pathologicalCaseList = new ArrayList<SurgicalPathologyReport>();

		final long startTime = System.currentTimeMillis();
		DAO dao = null;

		try
		{
			dao = this.openDAOSession(null);
			// retriving the id list from session.
			final String[] className = {IdentifiedSurgicalPathologyReport.class.getName(),
					DeidentifiedSurgicalPathologyReport.class.getName(),
					SurgicalPathologyReport.class.getName()};

			if (request.getSession().getAttribute(Constants.PATHALOGICAL_CASE_ID) != null)
			{
				this.getList(request, Constants.PATHALOGICAL_CASE_ID, className[0],
						pathologicalCaseList, dao);
			}
			if (request.getSession().getAttribute(Constants.DEIDENTIFIED_PATHALOGICAL_CASE_ID) != null)
			{
				this.getList(request, Constants.DEIDENTIFIED_PATHALOGICAL_CASE_ID, className[1],
						pathologicalCaseList, dao);
			}
			if (request.getSession().getAttribute(Constants.SURGICAL_PATHALOGY_CASE_ID) != null)
			{
				this.getList(request, Constants.SURGICAL_PATHALOGY_CASE_ID, className[2],
						pathologicalCaseList, dao);
			}

			final long endTime = System.currentTimeMillis();
			LOGGER.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB -  : "
					+ (endTime - startTime));

		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return pathologicalCaseList;
	}

	/**
	 *
	 * @param request : request
	 * @param attr : attr
	 * @param className : className
	 * @param pathologicalCaseList : pathologicalCaseList
	 * @param dao : dao
	 * @throws DAOException : DAOException
	 */
	private void getList(HttpServletRequest request, String attr, String className,
			List<SurgicalPathologyReport> pathologicalCaseList,  DAO dao) throws DAOException
			{
		final List idList = (List) request.getSession().getAttribute(attr);
		final int size = idList.size();
		for (int i = 0; i < idList.size(); i++)
		{
			final Object object = dao.retrieveById(className, Long
					.parseLong((String) idList.get(i)));
			final SurgicalPathologyReport surgicalPathologyReport = (SurgicalPathologyReport) object;
			surgicalPathologyReport.getSpecimenCollectionGroup();
			surgicalPathologyReport.getSpecimenCollectionGroup().getSpecimenCollection();
			pathologicalCaseList.add(surgicalPathologyReport);
		}

			}

	/**
	 *
	 * @param request : request
	 * @return List
	 * @throws Exception : Exception
	 */
	public List getDistributionProtocol(HttpServletRequest request) throws Exception
	{
		// to get the distribution protocol name
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final DistributionBizLogic dao = (DistributionBizLogic) factory
		.getBizLogic(Constants.DISTRIBUTION_FORM_ID);

		final String sourceObjectName = DistributionProtocol.class.getName();
		final String[] displayName = {"title"};
		final String valueField = Constants.SYSTEM_IDENTIFIER;
		final List protocolList = dao.getList(sourceObjectName, displayName, valueField, true);

		request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);
		return protocolList;
	}

	/**
	 * @param orderNew : orderNew
	 * @param sessionDataBean : sessionDataBean
	 * @param dao : dao
	 * @throws BizLogicException : BizLogicException
	 */
	private void disposeSpecimen(OrderDetails orderNew, SessionDataBean sessionDataBean, DAO dao)
	throws BizLogicException
	{

		final NewSpecimenBizLogic newSpecimenBizLogic = new NewSpecimenBizLogic();
		final Collection<OrderItem> orderItemCollection = orderNew.getOrderItemCollection();
		final StringBuffer disposalReason = new StringBuffer();
		disposalReason.append("Specimen distributed to distribution protocol ");
		disposalReason.append(orderNew.getDistributionProtocol().getTitle());
		disposalReason.append(" via order ");
		disposalReason.append(orderNew.getName());

		if(orderItemCollection!=null)
		{
			final Iterator<OrderItem> orderItemIterator = orderItemCollection.iterator();
			try
			{
				while (orderItemIterator.hasNext())
				{
					final OrderItem orderItem = orderItemIterator.next();
					if (orderItem.getStatus().equals(
							Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE))
					{
						final Collection<Distribution> distributionCollection = orderNew.getDistributionCollection();
						if(distributionCollection!=null)
						{
							final Iterator<Distribution> iterator = distributionCollection.iterator();
							while (iterator.hasNext())
							{
								final Distribution disrt = iterator.next();

								final Collection<DistributedItem> distributedItemCollection = disrt
								.getDistributedItemCollection();
								if(distributedItemCollection!=null)
								{
									final Iterator<DistributedItem> iter = distributedItemCollection.iterator();

									while (iter.hasNext())
									{
										final DistributedItem distributionItem = iter.next();
										if (orderItem.getDistributedItem().equals(distributionItem))
										{

											final Specimen specimen = distributionItem.getSpecimen();
											final SpecimenArray specimenArray = distributionItem
											.getSpecimenArray();

											if (specimen != null)
											{

												newSpecimenBizLogic.disposeAndCloseSpecimen(sessionDataBean,
														specimen, dao, disposalReason.toString());

											}
											else if (specimenArray != null)
											{
												this.updateSpecimenArray(specimenArray, dao, sessionDataBean);

											}
										}
									}
								}
							}
						}
					}

				}
			}
			catch (final ApplicationException e)
			{
				LOGGER.error(e.getMessage(), e);
				throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
			}
		}
	}

	/**
	 * @param specimenArray : specimenArray
	 * @param dao ": dao
	 * @param sessionDataBean : sessionDataBean
	 * @throws BizLogicException : BizLogicException
	 * @throws DAOException : DAOException
	 */
	private void updateSpecimenArray(SpecimenArray specimenArray, DAO dao,
			SessionDataBean sessionDataBean) throws DAOException, BizLogicException
			{

		final SpecimenArrayBizLogic specimenArrayBizLogic = new SpecimenArrayBizLogic();
		final SpecimenArray oldSpecimenArray = (SpecimenArray) dao.retrieveById(SpecimenArray.class
				.getName(), specimenArray.getId());
		final SpecimenArray newSpecimenArray = oldSpecimenArray;
		newSpecimenArray.setActivityStatus("Closed");
		specimenArrayBizLogic.update(newSpecimenArray, oldSpecimenArray, sessionDataBean);

			}

	/**
	 *
	 * @param specimenId : specimenId
	 * @return Specimen
	 * @throws BizLogicException : BizLogicException
	 */
	public Specimen getSpecimenObject(Long specimenId) throws BizLogicException
	{
		DAO dao = null;
		Specimen specimen = null;
		try
		{
			dao = this.openDAOSession(null);
			final String sourceObjectName = Specimen.class.getName();
			specimen = (Specimen) dao.retrieveById(sourceObjectName, specimenId);

		}
		catch (final Exception e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return specimen;
	}

	/**
	 *
	 * @param specimenId : specimenId
	 * @param dao : dao
	 * @return Specimen
	 */
	public Specimen getSpecimen(Long specimenId, DAO dao)
	{
		final String sourceObjectName = Specimen.class.getName();
		Specimen specimen = null;
		try
		{
			specimen = (Specimen) dao.retrieveById(sourceObjectName, specimenId);
		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		return specimen;
	}

	/**
	 * It retrieves the SCG
	 * @param scgId : scgId
	 * @return SpecimenCollectionGroup
	 * @throws BizLogicException : BizLogicException
	 */
	public SpecimenCollectionGroup retrieveSCG(Long scgId) throws BizLogicException
	{
		DAO dao = null;
		SpecimenCollectionGroup scg = null;
		try
		{

			dao = this.openDAOSession(null);
			scg = (SpecimenCollectionGroup) dao.retrieveById(SpecimenCollectionGroup.class
					.getName(), scgId);
		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}

		return scg;
	}

	/**
	 * It retrieves the NewSpecimenArrayOrder Item
	 * @param newSpecimenArrayId : newSpecimenArrayId
	 * @return NewSpecimenArrayOrderItem
	 * @throws BizLogicException : BizLogicException
	 */
	public NewSpecimenArrayOrderItem retrieveNewSpecimenArrayOrderItem(Long newSpecimenArrayId)
	throws BizLogicException
	{
		DAO dao = null;
		NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = null;
		try
		{
			dao = this.openDAOSession(null);

			newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) dao.retrieveById(
					NewSpecimenArrayOrderItem.class.getName(), newSpecimenArrayId);
			newSpecimenArrayOrderItem.setSpecimenArray((SpecimenArray) this.getSpecimenArray(
					newSpecimenArrayOrderItem.getId(), dao));

		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}

		return newSpecimenArrayOrderItem;
	}

	/**
	 * It retrieves the SpecimenOrderItem
	 * @param specimenOrderItemId : specimenOrderItemId
	 * @return SpecimenOrderItem
	 * @throws BizLogicException : BizLogicException
	 */
	public SpecimenOrderItem retrieveSpecimenOrderItem(Long specimenOrderItemId)
	throws BizLogicException
	{
		DAO dao = null;
		SpecimenOrderItem specimenOrderItem = null;
		try
		{
			dao = this.openDAOSession(null);
			specimenOrderItem = (SpecimenOrderItem) dao.retrieveById(SpecimenOrderItem.class
					.getName(), specimenOrderItemId);
			final NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) specimenOrderItem
			.getNewSpecimenArrayOrderItem();
			specimenOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayOrderItem);

		}
		catch (final DAOException e)
		{
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}

		return specimenOrderItem;
	}

	/**
	 * To retreive the specimenArray
	 * @param orderItemId : orderItemId
	 * @param dao : dao
	 * @return SpecimenArray
	 * @throws BizLogicException : BizLogicException
	 */
	private SpecimenArray getSpecimenArray(Long orderItemId, DAO dao) throws BizLogicException
	{
		SpecimenArray specimenArray = null;
		try
		{
			if (orderItemId != null)
			{
				final String hql = " select newSpecimenArrayOrderItem.specimenArray "
					+ // ,elements(specimenArray.
					// specimenArrayContentCollection)" +
					" from edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem as newSpecimenArrayOrderItem "
					+ " where newSpecimenArrayOrderItem.id = " + orderItemId;

				final List specimenArrayList = dao.executeQuery(hql);
				if (specimenArrayList != null && !specimenArrayList.isEmpty())
				{
					specimenArray = (SpecimenArray) specimenArrayList.get(0);
				}
			}
		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this
			.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return specimenArray;
	}

	/**
	 *
	 * @param order : order
	 * @param dao : dao
	 * @throws BizLogicException : BizLogicException
	 */
	private void checkDistributionPtorocol(OrderDetails order, DAO dao) throws BizLogicException
	{
		try
		{
			final String[] selectColNames = {"distributionProtocol"};
			final String[] whereColNames = {Constants.SYSTEM_IDENTIFIER};
			final String[] whereColConditions = {"="};
			final Object[] whereColVal = {order.getId()};

			final QueryWhereClause queryWhereClause = new QueryWhereClause(OrderDetails.class
					.getName());
			queryWhereClause.addCondition(new EqualClause(
					edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER, order.getId()));

			final List list = dao.retrieve(OrderDetails.class.getName(), selectColNames,
					queryWhereClause);

			if (list != null && !list.isEmpty())
			{
				final DistributionProtocol dist = (DistributionProtocol) list.get(0);
				if (dist != null)
				{
					this.checkStatus(dao, dist, "Distribution Protocol");
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
	 * 	 * This method loads the title as Name and id as value of distribution
	 * protocol from database and return the namevalue bean of ditribution
	 * protocol for a given PI.
	 * @param piID : piID
	 * @param roleName : roleName
	 * @param sessionDataBean : sessionDataBean
	 * @return List : List
	 * @throws DAOException : DAOException
	 * @throws BizLogicException : BizLogicException
	 */
	public List loadDistributionProtocol(final Long piID, String roleName,
			SessionDataBean sessionDataBean) throws DAOException, BizLogicException
			{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		List distributionProtocolList = new ArrayList();

		final String sourceObjectName = DistributionProtocol.class.getName();
		final String[] displayName = {"title"};
		final String valueFieldCol = Constants.ID;

		final String[] whereColNames = {Status.ACTIVITY_STATUS.toString()};
		final String[] whereColCond = {"!="};
		final Object[] whereColVal = {Status.ACTIVITY_STATUS_CLOSED.toString()};
		final String separatorBetweenFields = "";

		// checking for the role. if role is admin / supervisor then show all
		// the distribution protocols.
		if (roleName.equals(Constants.ADMINISTRATOR) || roleName.equals(Constants.SUPERVISOR))
		{
			distributionProtocolList = bizLogic.getList(sourceObjectName, displayName,
					valueFieldCol, whereColNames, whereColCond, whereColVal,
					Constants.AND_JOIN_CONDITION, separatorBetweenFields, true);
		}
		else
		{
			final String[] whereColumnName = {"principalInvestigator.id",
					Status.ACTIVITY_STATUS.toString()};
			final String[] colCondition = {"=", "!="};
			final Object[] whereColumnValue = {piID, Status.ACTIVITY_STATUS_CLOSED.toString()};
			final String joinCondition = Constants.AND_JOIN_CONDITION;
			final boolean isToExcludeDisabled = true;

			// Get data from database
			distributionProtocolList = bizLogic.getList(sourceObjectName, displayName,
					valueFieldCol, whereColumnName, colCondition, whereColumnValue, joinCondition,
					separatorBetweenFields, isToExcludeDisabled);
		}

		// Fix for bug #9543 - start
		// Check for Distribution privilege & if privilege present, show all
		// DP's in DP list
		if (!roleName.equals(Constants.ADMINISTRATOR) && sessionDataBean != null)
		{
			final HashSet<Long> siteIds = new HashSet<Long>();
			final HashSet<Long> cpIds = new HashSet<Long>();
			boolean hasDistributionPrivilege = false;
			final IDAOFactory daoFact = DAOConfigFactory.getInstance().getDAOFactory(
					CommonServiceLocator.getInstance().getAppName());
			DAO dao = null;
			dao = daoFact.getDAO();
			try
			{
				// session = DBUtil.getCleanSession();
				dao.openSession(null);
				final User user = (User) dao.retrieveById(User.class.getName(), sessionDataBean
						.getUserId());
				final Collection<Site> siteCollection = user.getSiteCollection();
				final Collection<CollectionProtocol> cpCollection = user
				.getAssignedProtocolCollection();

				// Scientist
				if (siteCollection == null || siteCollection.isEmpty())
				{
					return distributionProtocolList;
				}
				for (final Site site : siteCollection)
				{
					siteIds.add(site.getId());
				}
				if (cpCollection != null)
				{
					for (final CollectionProtocol cp : cpCollection)
					{
						cpIds.add(cp.getId());
					}
				}

				hasDistributionPrivilege = this.checkDistributionPrivilege(sessionDataBean,
						siteIds, cpIds);

				if (hasDistributionPrivilege)
				{
					distributionProtocolList = bizLogic.getList(sourceObjectName, displayName,
							valueFieldCol, whereColNames, whereColCond, whereColVal,
							Constants.AND_JOIN_CONDITION, separatorBetweenFields, true);
				}
			}
			finally
			{
				dao.closeSession();
			}
		}

		// Fix for bug #9543 - end

		return distributionProtocolList;
			}

	/**
	 *
	 * @param sessionDataBean : sessionDataBean
	 * @param siteIds : siteIds
	 * @param cpIds : cpIds
	 * @return boolean : boolean
	 */
	private boolean checkDistributionPrivilege(SessionDataBean sessionDataBean,
			HashSet<Long> siteIds, HashSet<Long> cpIds)
	{

		boolean hasDistributionPrivilege = false;
		try
		{
			String objectId = Site.class.getName();
			final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
					sessionDataBean.getUserName());

			for (final Long siteId : siteIds)
			{
				if (privilegeCache.hasPrivilege(objectId + "_" + siteId, Permissions.DISTRIBUTION))
				{
					return true;
				}
			}
			objectId = CollectionProtocol.class.getName();
			for (final Long cpId : cpIds)
			{
				final boolean temp = privilegeCache.hasPrivilege(objectId + "_" + cpId,
						Permissions.DISTRIBUTION);
				if (temp)
				{
					return true;
				}
				hasDistributionPrivilege = AppUtility.checkForAllCurrentAndFutureCPs(
						Permissions.DISTRIBUTION, sessionDataBean, cpId.toString());
			}
		}
		catch (final SMException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		return hasDistributionPrivilege;
	}

}