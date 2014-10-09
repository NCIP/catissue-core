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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import edu.wustl.catissuecore.bean.RequestViewBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.OrderItem;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
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
import edu.wustl.catissuecore.dto.ConsentDTO;
import edu.wustl.catissuecore.dto.ConsentTierDTO;
import edu.wustl.catissuecore.dto.DisplayOrderDTO;
import edu.wustl.catissuecore.dto.ExportedItemDto;
import edu.wustl.catissuecore.dto.OrderErrorDTO;
import edu.wustl.catissuecore.dto.OrderItemDTO;
import edu.wustl.catissuecore.dto.OrderItemSubmissionDTO;
import edu.wustl.catissuecore.dto.OrderStatusDTO;
import edu.wustl.catissuecore.dto.OrderSubmissionDTO;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.catissuecore.util.OrderingSystemUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.velocity.VelocityManager;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.dao.util.NamedQueryParam;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

public class OrderBizLogic extends CatissueDefaultBizLogic {

	private static final Logger LOGGER = Logger.getCommonLogger(OrderBizLogic.class);

	// To calculate the Order status.
	private int orderStatusNew = 0;

	private int orderStatusPending = 0;

	private int orderStatusRejected = 0;

	private int orderStatusCompleted = 0;

	// To display the number of Order Items updated.
	public static int numberItemsUpdated = 0;

	public static final String[] DISTRIBUTE_STATUS_LIST = {Constants.ORDER_REQUEST_STATUS_DISTRIBUTED,
			Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE,
			Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE_SPECIAL};

	public static final String[] CLOSED_STATUS_LIST = {Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE,
			Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE_SPECIAL};

	public static final String[] PROCESSED_STATUS_LIST = {Constants.ORDER_REQUEST_STATUS_DISTRIBUTED,
			Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE,
			Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE_SPECIAL,
			Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST,
			Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE,
			Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE};

	public static final String[] PENDING_STATUS_LIST = {Constants.ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW,
			Constants.ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION,
			Constants.ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION};

	public static final String[] REJECTED_STATUS_LIST = {Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST,
			Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE,
			Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE};

	public static final String[] DISTRIBUTED_STATUS_LIST = {Constants.ORDER_REQUEST_STATUS_DISTRIBUTED,
			Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE,
			Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE_SPECIAL};

	/**
	 * Saves the OrderDetails object in the database.
	 * 
	 * @param obj
	 *            The OrderDetails Item object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @param dao
	 *            object.
	 * @throws BizLogicException
	 *             object.
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException {
		try {
			final OrderDetails order = (OrderDetails) obj;
			User requestedUser = new User();
			requestedUser.setId(sessionDataBean.getUserId());
			order.setRequestedBy(requestedUser);
			dao.insert(order);
		}
		catch (final DAOException daoExp) {
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * This function send mail to the Administrator and Scientist if items
	 * ordered by scientist is placed successfully.
	 * 
	 * @param sessionDataBean
	 *            object
	 * @param order
	 *            object
	 * @param dao
	 *            object
	 * @throws BizLogicException
	 *             object
	 */
	private void mailOnSuccessfulSave(SessionDataBean sessionDataBean, OrderDetails order, DAO dao)
			throws BizLogicException {
		try {

			boolean emailSent = false;
			final List userList = dao.retrieve(User.class.getName(), "emailAddress", sessionDataBean.getUserName());
			if (userList != null && !userList.isEmpty()) {
				final User userObj = (User) userList.get(0);
				final EmailHandler emailHandler = new EmailHandler();

				emailSent = emailHandler.sendEmailForOrderPlacement(userObj, order);

				if (emailSent) {
					LOGGER.debug(" In OrderBizLogic --> Email sent To Admin and Scientist successfully ");
				}
				else {
					LOGGER.debug(" In OrderBizLogic --> Email could not be Sent ");
				}
			}
		}
		catch (final DAOException daoExp) {
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * Updates the persistent object in the database.
	 * 
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

	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException {
		try {
			this.validate(obj, oldObj, dao, Constants.EDIT);
			final OrderDetails orderImplObj = (OrderDetails) HibernateMetaData.getProxyObjectImpl(oldObj);
			final OrderDetails orderNew = this.updateObject(orderImplObj, obj, dao, sessionDataBean);
			orderNew.setOrderItemCollection(orderNew.getOrderItemCollection());
			orderImplObj.setOrderItemCollection(orderNew.getOrderItemCollection());
			dao.update(orderNew, orderImplObj);
			this.disposeSpecimen(orderNew, sessionDataBean, dao);
			// Sending Email only if atleast one order item is updated.
			if (numberItemsUpdated > 0 && orderNew.getMailNotification()) {
				this.sendEmailOnOrderUpdate(dao, orderNew, sessionDataBean);
			}
		}
		catch (final DAOException daoExp) {
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
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
	protected boolean validate(Object obj, Object oldObj, DAO dao, String operation) throws BizLogicException {
		final OrderDetails order = (OrderDetails) obj;
		final OrderDetails oldOrder = (OrderDetails) oldObj;
		this.checkDistributionPtorocol(order, dao);
		if ((oldOrder.getStatus().trim().equalsIgnoreCase("Pending") && order.getStatus().trim().equalsIgnoreCase("New"))
				|| (oldOrder.getStatus().trim().equalsIgnoreCase("Rejected") && (order.getStatus().trim()
						.equalsIgnoreCase("New") || order.getStatus().trim().equalsIgnoreCase("Pending")))
				|| (oldOrder.getStatus().trim().equalsIgnoreCase("Completed") && (order.getStatus().trim()
						.equalsIgnoreCase("New")
						|| order.getStatus().trim().equalsIgnoreCase("Pending") || order.getStatus().trim()
						.equalsIgnoreCase("Rejected")))) {
			throw this.getBizLogicException(null, "orderdistribution.status.errmsg", "");
		}
		// Site is mandatory on UI.
		final Collection distributionCollection = order.getDistributionCollection();
		if (distributionCollection != null) {
			final Iterator distributionCollectionIterator = distributionCollection.iterator();
			while (distributionCollectionIterator.hasNext()) {
				final Distribution distribution = (Distribution) distributionCollectionIterator.next();
				if (distribution.getToSite().getId().compareTo(new Long(-1)) == 0) {
					throw this.getBizLogicException(null, "orderdistribution.site.required.errmsg", "");
				}
			}
		}
		// Quantity is mandatory
		final Collection orderItemCollection = order.getOrderItemCollection();
		final Iterator iter = orderItemCollection.iterator();
		while (iter.hasNext()) {
			final OrderItem orderItem = (OrderItem) iter.next();
			/*
			 * if (orderItem.getDistributedItem() != null) { if
			 * (orderItem.getStatus
			 * ().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
			 * ||orderItem.getStatus().equalsIgnoreCase(Constants.
			 * ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)) { if
			 * (orderItem.getDistributedItem().getQuantity() == null ||
			 * orderItem
			 * .getDistributedItem().getQuantity().toString().equalsIgnoreCase
			 * ("")) { throw newDAOException(ApplicationProperties.getValue(
			 * "orderdistribution.quantity.required.errmsg")); } // Quantity
			 * cannot be characters and Quantity cannot be negative number. if
			 * (!
			 * validator.isDouble(orderItem.getDistributedItem().getQuantity().
			 * toString())) { throw new
			 * DAOException(ApplicationProperties.getValue
			 * ("orderdistribution.quantity.format.errmsg")); } } }
			 */
			final Collection oldOrderItemColl = oldOrder.getOrderItemCollection();
			final Iterator oldOrderItemCollIter = oldOrderItemColl.iterator();
			while (oldOrderItemCollIter.hasNext()) {
				final OrderItem oldorderItem = (OrderItem) oldOrderItemCollIter.next();
				if (oldorderItem.getId().compareTo(orderItem.getId()) == 0) {
					// If requestFor drop down is null, do not allow
					// distribution of that order item
					if (orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
							|| orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)
							|| orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION)) {
						if (oldorderItem instanceof DerivedSpecimenOrderItem) {
							// final DerivedSpecimenOrderItem
							// derivedSpecimenOrderItem =
							// (DerivedSpecimenOrderItem) oldorderItem;
							if (oldorderItem.getDistributedItem() == null
									&& orderItem.getDistributedItem().getSpecimen().getId() == null) {
								throw this.getBizLogicException(null, "orderdistribution.distribution.notpossible.errmsg", "");
							}

						}
						else if (oldorderItem instanceof NewSpecimenArrayOrderItem) {
							final SpecimenArray specimenArray = this.getSpecimenArray(oldorderItem.getId(), dao);
							final NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) oldorderItem;
							newSpecimenArrayOrderItem.setSpecimenArray(specimenArray);
							if (newSpecimenArrayOrderItem.getSpecimenArray() == null) {
								throw this.getBizLogicException(null, "orderdistribution.distribution.arrayNotCreated.errmsg", "");
							}
						}
						else if (oldorderItem instanceof PathologicalCaseOrderItem) {
							final PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) oldorderItem;
							if (pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSpecimenCollection() == null
									|| pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSpecimenCollection().size() == 0) {
								if (orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION)) {
									throw this.getBizLogicException(null, "orderdistribution.arrayPrep.notpossibleForPatho.errmsg", "");
								}
								else {
									throw this
											.getBizLogicException(null, "orderdistribution.distribution.notpossibleForPatho.errmsg", "");
								}
							}
							if (pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSpecimenCollection() != null) {
								final Collection specimenColl = pathologicalCaseOrderItem.getSpecimenCollectionGroup()
										.getSpecimenCollection();
								final Iterator specimenCollIter = specimenColl.iterator();
								final List totalChildrenSpecimenColl = new ArrayList();
								while (specimenCollIter.hasNext()) {
									final Specimen specimen = (Specimen) specimenCollIter.next();
									final List childSpecimenCollection = OrderingSystemUtil.getAllChildrenSpecimen(specimen
											.getChildSpecimenCollection());
									List finalChildrenSpecimenCollection = null;
									if (pathologicalCaseOrderItem.getSpecimenClass() != null
											&& pathologicalCaseOrderItem.getSpecimenType() != null
											&& !pathologicalCaseOrderItem.getSpecimenClass().trim().equalsIgnoreCase("")
											&& !pathologicalCaseOrderItem.getSpecimenType().trim().equalsIgnoreCase("")) { // "Derived"
										finalChildrenSpecimenCollection = OrderingSystemUtil.getChildrenSpecimenForClassAndType(
												childSpecimenCollection, pathologicalCaseOrderItem.getSpecimenClass(),
												pathologicalCaseOrderItem.getSpecimenType());
									}
									else { // "Block" . Specimen class =
													// "Tissue" ,
													// Specimen Type = "Block".
										finalChildrenSpecimenCollection = OrderingSystemUtil.getChildrenSpecimenForClassAndType(
												childSpecimenCollection, "Tissue", "Block");
									}
									// adding specimen to the collection
									if (specimen.getClassName().equalsIgnoreCase(pathologicalCaseOrderItem.getSpecimenClass())
											&& specimen.getSpecimenType().equalsIgnoreCase(pathologicalCaseOrderItem.getSpecimenType())) {
										finalChildrenSpecimenCollection.add(specimen);
									}
									if (finalChildrenSpecimenCollection != null) {
										final Iterator finalChildrenSpecimenCollectionIterator = finalChildrenSpecimenCollection.iterator();
										while (finalChildrenSpecimenCollectionIterator.hasNext()) {
											totalChildrenSpecimenColl.add((finalChildrenSpecimenCollectionIterator.next()));
										}
									}
								}
								if (totalChildrenSpecimenColl == null) {
									if (orderItem.getStatus()
											.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION)) {
										throw this.getBizLogicException(null, "orderdistribution.arrayPrep.notpossible.errmsg", "");
									}
									else {
										throw this.getBizLogicException(null, "orderdistribution.distribution.notpossible.errmsg", "");
									}
								}
							}
						}

						// Fix me
						// Removing distributed item in case of status - Ready
						// For Array Preparation
						if (orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION)) {
							final DistributedItem distributedItem = orderItem.getDistributedItem();
							if (distributedItem != null) {
								orderItem.setDistributedItem(null);
							}
						}
					}// END DISTRIBUTED
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
	private OrderDetails updateObject(Object oldObj, Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException {

		try {
			OrderDetails order = (OrderDetails) obj;
			final OrderDetails orderOld = (OrderDetails) oldObj;
			// Getting OrderItems Collection.
			final Collection oldOrderItemSet = orderOld.getOrderItemCollection();
			final Collection newOrderItemSet = order.getOrderItemCollection();
			// Adding items inside New Defined Array in oldOrderItemColl
			final Collection tempNewOrderItemSet = new HashSet();
			// final Iterator tempNewItemIter = newOrderItemSet.iterator();
			/*
			 * while (tempNewItemIter.hasNext()) { OrderItem tempNewOrderItem =
			 * (OrderItem) tempNewItemIter.next(); if (tempNewOrderItem
			 * instanceof NewSpecimenArrayOrderItem) { NewSpecimenArrayOrderItem
			 * newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem)
			 * tempNewOrderItem; Collection specimenOrderItemColl =
			 * newSpecimenArrayOrderItem.getSpecimenOrderItemCollection(); if
			 * (specimenOrderItemColl != null) { Iterator tempIter =
			 * specimenOrderItemColl.iterator(); while (tempIter.hasNext()) {
			 * tempNewOrderItemSet.add((OrderItem) tempIter.next()); } } } }
			 */
			if (tempNewOrderItemSet.size() > 0) {
				newOrderItemSet.addAll(tempNewOrderItemSet);
			}
			// Iterating over OrderItems collection.
			final Iterator newSetIter = newOrderItemSet.iterator();
			numberItemsUpdated = 0;
			// To insert distribution and distributed items only once.
			boolean isDistributionInserted = false;

			while (newSetIter.hasNext()) {
				final OrderItem newOrderItem = (OrderItem) newSetIter.next();

				final Iterator oldSetIter = oldOrderItemSet.iterator();

				while (oldSetIter.hasNext()) {
					final OrderItem oldOrderItem = (OrderItem) oldSetIter.next();

					// Setting quantity to previous quantity.....
					if (oldOrderItem.getId().equals(newOrderItem.getId())) {
						newOrderItem.setRequestedQuantity(oldOrderItem.getRequestedQuantity());
					}
					// Update Old OrderItem only when its Id matches with
					// NewOrderItem id and the order is not distributed and the
					// oldorderitem status and neworderitem status are different
					// or description has been updated.
					if ((oldOrderItem.getId().compareTo(newOrderItem.getId()) == 0)
							&& (oldOrderItem.getDistributedItem() == null)
							&& (!oldOrderItem.getStatus().trim().equalsIgnoreCase(newOrderItem.getStatus().trim()) || (oldOrderItem
									.getDescription() != null && !oldOrderItem.getDescription().equalsIgnoreCase(
									newOrderItem.getDescription())))) {
						if (newOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
								|| newOrderItem.getStatus().trim()
										.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)) {
							if (!isDistributionInserted) {
								// Direct call to DistributionBizLogic
								final Collection newDistributionColl = order.getDistributionCollection();
								final Iterator iter = newDistributionColl.iterator();
								final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
								final DistributionBizLogic distributionBizLogic = (DistributionBizLogic) factory
										.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
								while (iter.hasNext()) {
									final Distribution distribution = (Distribution) iter.next();
									// Populating Distribution Object.
									distribution.setOrderDetails(orderOld);
									// Setting the user for distribution.
									final User user = new User();
									/*
									 * try {
									 */
									if (sessionDataBean.getUserId() == null) {
										user.setId(distribution.getDistributedBy().getId());

									}
									else {
										user.setId(sessionDataBean.getUserId());
									}
									/*
									 * } catch(NullPointerException npe) {
									 * logger.debug(npe.getMessage(), npe);
									 * throw getBizLogicException(null,
									 * "dao.error",
									 * "Please mention distributedBy attribute"
									 * ); }
									 */
									distribution.setDistributedBy(user);
									// Setting activity status
									distribution.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
									// Setting Event Timestamp
									final Date date = new Date();
									distribution.setTimestamp(date);
									// Inserting distribution
									distributionBizLogic.insert(distribution, dao, sessionDataBean);
									isDistributionInserted = true;
								}
							}
							// For assigned Quantity and RequestFor.
							/*
							 * if(newOrderItem.getDistributedItem() != null) {
							 * oldOrderItem
							 * .setDistributedItem(newOrderItem.getDistributedItem
							 * ()); }
							 */
						}
						// Setting Description and Status.
						if (newOrderItem.getDescription() != null) {
							if (newOrderItem instanceof ExistingSpecimenArrayOrderItem) {
								newOrderItem.setDescription(oldOrderItem.getDescription() + " " + newOrderItem.getDescription());
							}
						}
						// The number of Order Items updated.
						numberItemsUpdated++;
					}
					else if ((oldOrderItem.getId().compareTo(newOrderItem.getId()) == 0)
							&& oldOrderItem.getDistributedItem() != null) {
						final Object object = dao.retrieveById(DistributedItem.class.getName(), oldOrderItem.getDistributedItem()
								.getId());
						if (object != null) {
							final DistributedItem distributedItem = (DistributedItem) object;
							newOrderItem.setDistributedItem(distributedItem);
						}

					}
				}
				this.calculateOrderStatus(newOrderItem);
			}
			// Updating comments.
			if (order.getComment() != null && !order.getComment().trim().equalsIgnoreCase("")) {
				order.setComment(orderOld.getComment() + " " + order.getComment());
			}
			// Bug #12262
			final Collection updatedOrderItemSet = new HashSet();
			final Iterator itr = newOrderItemSet.iterator();

			while (itr.hasNext()) {
				final OrderItem newOrderItem = (OrderItem) itr.next();
				if (newOrderItem instanceof SpecimenArrayOrderItem) {
					updatedOrderItemSet.add(newOrderItem);
				}
				else {
					if (((SpecimenOrderItem) newOrderItem).getNewSpecimenArrayOrderItem() == null) {
						updatedOrderItemSet.add(newOrderItem);
					}
				}
			}

			// For order status.
			order = this.updateOrderStatus(order, updatedOrderItemSet);
			return order;
		}
		catch (final DAOException daoExp) {
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * 
	 * @param dao
	 *            : dao
	 * @param order
	 *            : order
	 * @param sessionDataBean
	 *            : sessionDataBean
	 */
	private void sendEmailOnOrderUpdate(DAO dao, OrderDetails order, SessionDataBean sessionDataBean) {
		final EmailHandler emailHandler = new EmailHandler();
		final String ccEmailAddress = order.getDistributionProtocol().getPrincipalInvestigator().getEmailAddress(); // cc
		final String toEmailAddress = sessionDataBean.getUserName(); // to
		// person
		// ordering
		final String fromEmailAddress = XMLPropertyHandler.getValue("email.administrative.emailAddress"); // from
		// bcc
		// ccEmailAddress += ccEmailAddress +" "+fromEmailAddress;
		final String bccEmailAddress = fromEmailAddress;
		final String subject = "Notification on Order " + order.getName() + " Status from caTissue Administrator";
		final String body = this.makeEmailBodyForOrderUpdate(dao, order);
		emailHandler.sendEmailForOrderDistribution(body, toEmailAddress, fromEmailAddress, ccEmailAddress, bccEmailAddress,
				subject);
	}

	public void sendOrderUpdateEmail(String requestorFirstName, String requestorLastName, String ccEmailAddress,
			String toEmailAddress, String orderName, String updaterLastName, String updaterFirstName, String orderStatus,
			Long orderId, Map<String, Object> csvFileData) throws IOException {

		final EmailHandler emailHandler = new EmailHandler();

		final String bccEmailAddress = XMLPropertyHandler.getValue("email.administrative.emailAddress");
		List<File> attachmentOrderCsv = new ArrayList<File>();
		File csvFile = null;
		try {
			csvFile = new File(csvFileData.get("fileName").toString());
			csvFile.createNewFile();
			FileOutputStream out = new FileOutputStream(csvFile);
			out.write((byte[]) csvFileData.get("fileData"));
			out.close();
			attachmentOrderCsv.add(csvFile);
			emailHandler.sendOrderUpdateEmail(requestorFirstName, requestorLastName, toEmailAddress, ccEmailAddress,
					bccEmailAddress, orderName, updaterFirstName, updaterLastName, orderStatus, orderId, attachmentOrderCsv);

		}
		finally {
			csvFile.delete();
		}
	}

	/**
	 * 
	 * @param dao
	 *            : dao
	 * @param order
	 *            : order
	 * @return String
	 */
	private String makeEmailBodyForOrderUpdate(DAO dao, OrderDetails order) {
		final Collection orderItemColl = order.getOrderItemCollection();
		String emailFormat = "";

		final String emailBodyHeader = "Hello " + order.getDistributionProtocol().getPrincipalInvestigator().getFirstName()
				+ " " + order.getDistributionProtocol().getPrincipalInvestigator().getLastName() + ",  \n\n"
				+ "This is in relation with the order you placed with us. Please find below the details on its status. \n\n";

		final Iterator iter = orderItemColl.iterator();
		int serialNo = 1;
		String emailBody = "";
		while (iter.hasNext()) {
			final OrderItem orderItem = (OrderItem) iter.next();

			if (orderItem instanceof ExistingSpecimenOrderItem) {
				final ExistingSpecimenOrderItem existingSpecimenOrderItem = (ExistingSpecimenOrderItem) orderItem;
				emailBody = emailBody + serialNo + ". " + existingSpecimenOrderItem.getSpecimen().getLabel() + ": "
						+ existingSpecimenOrderItem.getStatus() + "\n   Order Description: "
						+ existingSpecimenOrderItem.getDescription() + "\n\n";

				serialNo++;
			}
			else if (orderItem instanceof DerivedSpecimenOrderItem) {
				final DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem) orderItem;
				emailBody = emailBody + serialNo + ". " + derivedSpecimenOrderItem.getParentSpecimen().getLabel() + ": "
						+ derivedSpecimenOrderItem.getStatus() + "\n   Order Description: "
						+ derivedSpecimenOrderItem.getDescription() + "\n\n";

				serialNo++;

			}
			else if (orderItem instanceof PathologicalCaseOrderItem) {
				final PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) orderItem;
				emailBody = emailBody + serialNo + ". "
						+ pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSurgicalPathologyNumber() + ": "
						+ pathologicalCaseOrderItem.getStatus() + "\n   Order Description: "
						+ pathologicalCaseOrderItem.getDescription() + "\n\n";

				serialNo++;

			}
			else if (orderItem instanceof NewSpecimenArrayOrderItem) {
				final NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) orderItem;
				emailBody = emailBody + serialNo + ". " + newSpecimenArrayOrderItem.getName() + ": "
						+ newSpecimenArrayOrderItem.getStatus() + "\n   Order Description: "
						+ newSpecimenArrayOrderItem.getDescription() + "\n\n";

				serialNo++;

			}
			else if (orderItem instanceof ExistingSpecimenArrayOrderItem) {
				final ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = (ExistingSpecimenArrayOrderItem) orderItem;
				emailBody = emailBody + serialNo + ". " + existingSpecimenArrayOrderItem.getSpecimenArray().getName() + ": "
						+ existingSpecimenArrayOrderItem.getStatus() + "\n   Order Description: "
						+ existingSpecimenArrayOrderItem.getDescription() + "\n\n";

				serialNo++;
			}
		}
		final String emailMsgFooterRegards = "\n" + "Regards, ";
		final String emailMsgFooterSign = "\n" + "caTissuePlus Administrator";
		final String emailMsgFooter = emailMsgFooterRegards + emailMsgFooterSign;
		emailFormat = emailBodyHeader + emailBody + emailMsgFooter;
		return emailFormat;
	}

	/**
	 * @param oldOrderItem
	 *            object
	 */
	private void calculateOrderStatus(OrderItem oldOrderItem) {
		// Order id is null for specimen orderItems associated with
		// NewSpecimenArrayOrderItem
		if (oldOrderItem.getOrderDetails() != null) {
			// For order status
			if (oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_NEW)) {
				// As oldOrderItem is instanceof NewSpecimenArrayOrderItem
				// casting it in SpecimenOrderItem is giving
				// java.lang.ClassCastException.
				if (oldOrderItem instanceof SpecimenOrderItem)// Bug 14316
				{
					if (((SpecimenOrderItem) oldOrderItem).getNewSpecimenArrayOrderItem() == null) {
						orderStatusNew++;
					}
				}

			}// kalpana bug #5839 If the specimens inside the specimen Array and
				// if it's status is
				// ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION then mark it
				// complete.
			else if ((oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
					|| oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE) || oldOrderItem
					.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))) {
				if ((oldOrderItem instanceof SpecimenArrayOrderItem)
						|| ((SpecimenOrderItem) oldOrderItem).getNewSpecimenArrayOrderItem() == null) {
					orderStatusCompleted++;
				}
			}
			else if (oldOrderItem.getStatus().trim()
					.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION)
					|| oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW)
					|| oldOrderItem.getStatus().trim()
							.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION)) {
				orderStatusPending++;
			}
			else if (oldOrderItem.getStatus().trim()
					.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST)
					|| oldOrderItem.getStatus().trim()
							.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE)
					|| oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE)) {
				if ((oldOrderItem instanceof SpecimenArrayOrderItem)
						|| ((SpecimenOrderItem) oldOrderItem).getNewSpecimenArrayOrderItem() == null) {
					orderStatusRejected++;
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
	private OrderDetails updateOrderStatus(OrderDetails orderNew, Collection oldOrderItemSet) {
		if (orderStatusNew == oldOrderItemSet.size()) {
			orderNew.setStatus(Constants.ORDER_STATUS_NEW);
		}
		else if (orderStatusRejected == oldOrderItemSet.size()) {
			orderNew.setStatus(Constants.ORDER_STATUS_REJECTED);
		}
		else if ((orderStatusCompleted == oldOrderItemSet.size())
				|| ((orderStatusCompleted + orderStatusRejected) == oldOrderItemSet.size())) {
			orderNew.setStatus(Constants.ORDER_STATUS_COMPLETED);
		}
		else {
			orderNew.setStatus(Constants.ORDER_STATUS_PENDING);
		}

		return orderNew;
	}

	// Populates a List of RequestViewBean objects to display the request list
	// on RequestListAdministratorView.jsp
	/**
	 * @param requestStatusSelected
	 *            object
	 * @param userName
	 *            : userName
	 * @param userId
	 *            : userId
	 * @return requestList List
	 * @throws BizLogicException
	 *             object
	 */
	public List getRequestList(String requestStatusSelected, String userName, Long userId, String dpName)
			throws BizLogicException {

		DAO dao = null;
		final List orderList = new ArrayList();
		List requestViewBeanList = new ArrayList();
		try {
			dao = this.openDAOSession(null);
			final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
			final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(userName);

			final User user = this.getUser(dao, userId);
			final List siteIdsList = this.getUserSitesWithDistributionPrev(user, privilegeCache);

			List orderListFromDB = null;
			/*
			 * String[] whereColumnName = {"status"}; String[]
			 * whereColumnCondition = {"in"};
			 */
			String hql = null;
			if (dpName == null) {
				hql = " from " + OrderDetails.class.getName() + " order where order.status in ('New','Pending') "
						+ " order by order.requestedDate desc";
			}
			else {
				System.out.println();
				hql = "select orderDetails from "
						+ OrderDetails.class.getName()
						+ " orderDetails left outer join orderDetails.distributionProtocol where orderDetails.status = 'Completed' and orderDetails.distributionProtocol.shortTitle='"
						+ dpName + "' order by orderDetails.requestedDate desc";
			}
			orderListFromDB = dao.executeQuery(hql, null);
			// orderListFromDB = dao.retrieve(OrderDetails.class.getName(),
			// selectColName,
			// queryWhereClause);

			// }

			final Iterator orderListFromDBIterator = orderListFromDB.iterator();
			while (orderListFromDBIterator.hasNext()) {
				final OrderDetails orderDetails = (OrderDetails) orderListFromDBIterator.next();
				if (this.isSuperAdmin(user)) {
					orderList.add(orderDetails);
				}
				else {
					final boolean hasDributionPrivilegeOnSite = this.isOrderItemValidTodistribute(
							orderDetails.getOrderItemCollection(), siteIdsList);
					final SessionDataBean sdb = new SessionDataBean();
					sdb.setUserId(userId);
					sdb.setUserName(userName);
					final boolean hasDistributionPrivilegeOnCp = this.checkDistributionPrivilegeOnCP(user, privilegeCache,
							orderDetails.getOrderItemCollection(), sdb);
					if (hasDributionPrivilegeOnSite || hasDistributionPrivilegeOnCp) {
						orderList.add(orderDetails);
					}
				}
			}
			requestViewBeanList = this.populateRequestViewBeanList(orderList);

		}
		catch (final SMException e) {
			LOGGER.error(e.getMessage(), e);
			throw AppUtility.handleSMException(e);
		}
		catch (final DAOException exp) {
			LOGGER.error(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
		finally {
			this.closeDAOSession(dao);
		}
		return requestViewBeanList;
	};

	/**
	 * Site of the order item should be one in the site list having distribution
	 * privilege.
	 * 
	 * @param orderItemCollection
	 *            : orderItemCollection
	 * @param siteIdsList
	 *            : siteIdsList
	 * @return boolean
	 */
	private boolean isOrderItemValidTodistribute(Collection orderItemCollection, List siteIdsList) {
		boolean isValidToDistribute = false;

		final Iterator orderItemColItr = orderItemCollection.iterator();
		if (orderItemColItr.hasNext()) {
			final OrderItem orderItem = (OrderItem) orderItemColItr.next();
			if (orderItem instanceof ExistingSpecimenOrderItem) {
				final ExistingSpecimenOrderItem existingSpecimenOrderItem = (ExistingSpecimenOrderItem) orderItem;
				if (existingSpecimenOrderItem.getSpecimen() != null) {
					final SpecimenPosition specimenPosition = existingSpecimenOrderItem.getSpecimen().getSpecimenPosition();
					if (specimenPosition != null) {
						if (siteIdsList.contains(existingSpecimenOrderItem.getSpecimen().getSpecimenPosition()
								.getStorageContainer().getSite().getId())) {
							isValidToDistribute = true;
							// break;
						}
					}
				}

			}
			else if (orderItem instanceof ExistingSpecimenArrayOrderItem) {
				final ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = (ExistingSpecimenArrayOrderItem) orderItem;
				if (existingSpecimenArrayOrderItem.getSpecimenArray() != null
						&& existingSpecimenArrayOrderItem.getSpecimenArray().getLocatedAtPosition() != null) {
					final StorageContainer storageContainer = (StorageContainer) existingSpecimenArrayOrderItem
							.getSpecimenArray().getLocatedAtPosition().getParentContainer();
					if (siteIdsList.contains(storageContainer.getSite().getId())) {
						isValidToDistribute = true;
						// break;
					}
				}
			}
			else if (orderItem instanceof DerivedSpecimenOrderItem) {
				final DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem) orderItem;
				if (derivedSpecimenOrderItem.getParentSpecimen() != null) {
					final SpecimenPosition specimenPosition = derivedSpecimenOrderItem.getParentSpecimen().getSpecimenPosition();
					if (specimenPosition != null
							&& !(derivedSpecimenOrderItem.getStatus().equals(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED) || derivedSpecimenOrderItem
									.getStatus().equals(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE))) {
						final Long siteId = specimenPosition.getStorageContainer().getSite().getId();
						if (siteIdsList.contains(siteId)) {
							isValidToDistribute = true;
							// break;
						}
					}
				}

			}
		}

		return isValidToDistribute;
	}

	/**
	 * Retrieve all the sites of the user having distribution privilege.
	 * 
	 * @param dao
	 *            : dao
	 * @param userId
	 *            : userId
	 * @param privilegeCache
	 *            : privilegeCache
	 * @return List
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public List getRelatedSiteIds(HibernateDAO dao, Long userId, PrivilegeCache privilegeCache) throws BizLogicException {
		final List siteCollWithDistriPri = new ArrayList();
		try {
			final User user = (User) dao.retrieveById(User.class.getName(), userId);
			this.getSiteIds(privilegeCache, siteCollWithDistriPri, user);
		}

		catch (final DAOException e) {
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		return siteCollWithDistriPri;
	}

	/**
	 * Method called to get all the site ids.
	 * 
	 * @param privilegeCache
	 * @param user
	 * @return
	 * @throws BizLogicException
	 */
	private void getSiteIds(PrivilegeCache privilegeCache, List siteCollWithDistriPri, final User user)
			throws BizLogicException {
		String objectId = "";
		try {
			if (!user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER)) {
				final Collection<Site> siteCollection = user.getSiteCollection();
				final Iterator siteCollectionItr = siteCollection.iterator();
				while (siteCollectionItr.hasNext()) {
					final Site site = (Site) siteCollectionItr.next();
					objectId = Constants.SITE_CLASS_NAME + "_" + site.getId();

					final boolean isAuthorized = privilegeCache.hasPrivilege(objectId,
							Variables.privilegeDetailsMap.get(Constants.DISTRIBUTE_SPECIMENS));
					if (isAuthorized) {
						siteCollWithDistriPri.add(site.getId());
					}

				}
			}
		}
		catch (final SMException e) {
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, "sm.priv.error", objectId);
		}
	}

	/**
	 * 
	 * @param user
	 *            : user
	 * @param privilegeCache
	 *            : privilegeCache
	 * @param orderItemCollection
	 *            : orderItemCollection
	 * @param sessionDataBean
	 *            : sessionDataBean
	 * @return boolean
	 * @throws BizLogicException
	 *             :BizLogicException
	 */
	public boolean checkDistributionPrivilegeOnCP(User user, PrivilegeCache privilegeCache,
			Collection orderItemCollection, SessionDataBean sessionDataBean) throws BizLogicException {

		boolean hasDistributionPrivilege = false;
		String objectId = "";

		try {
			final Iterator orderItemColItr = orderItemCollection.iterator();

			if (orderItemColItr.hasNext()) {
				final OrderItem orderItem = (OrderItem) orderItemColItr.next();
				if (orderItem instanceof ExistingSpecimenOrderItem) {
					ExistingSpecimenOrderItem item = (ExistingSpecimenOrderItem) orderItem;
					if (item != null) {
						//							&& (item.getStatus().equals(
						//									Constants.ORDER_REQUEST_STATUS_DISTRIBUTED) || item
						//									.getStatus().equals(
						//											Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE))){
						Specimen specimen = item.getSpecimen();
						SpecimenCollectionGroup specimenCollectionGroup = specimen.getSpecimenCollectionGroup();
						final Long cpId = specimenCollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol()
								.getId();
						objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cpId;
						hasDistributionPrivilege = hasDistributionPrivileges(privilegeCache, sessionDataBean, objectId, cpId);
					}

				}
				if (orderItem instanceof PathologicalCaseOrderItem) {
					final PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) orderItem;
					final SpecimenCollectionGroup specimenCollectionGroup = pathologicalCaseOrderItem
							.getSpecimenCollectionGroup();
					if (specimenCollectionGroup != null
							&& !(pathologicalCaseOrderItem.getStatus().equals(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED) || pathologicalCaseOrderItem
									.getStatus().equals(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE))) {

						final Long cpId = specimenCollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol()
								.getId();
						objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cpId;
						hasDistributionPrivilege = hasDistributionPrivileges(privilegeCache, sessionDataBean, objectId, cpId);
					}
				}
			}
		}
		catch (final SMException e) {
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, "sm.priv.error", objectId);
		}

		return hasDistributionPrivilege;
	}

	private boolean hasDistributionPrivileges(PrivilegeCache privilegeCache, SessionDataBean sessionDataBean,
			String objectId, final Long cpId) throws SMException {
		boolean isAuthorized = privilegeCache.hasPrivilege(objectId,
				Variables.privilegeDetailsMap.get(Constants.DISTRIBUTE_SPECIMENS));
		if (!isAuthorized) {
			isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(Permissions.DISTRIBUTION, sessionDataBean,
					cpId.toString());
		}

		return isAuthorized;
	}

	/**
	 * It returns the user object
	 * 
	 * @param dao
	 *            : dao
	 * @param userId
	 *            : userId
	 * @return User
	 * @throws DAOException
	 *             : DAOException
	 */
	private User getUser(DAO dao, Long userId) throws DAOException {
		return (User) dao.retrieveById(User.class.getName(), userId);
	}

	/**
	 * Get the list of sites of user on which user have distribution privilege
	 * 
	 * @param user
	 *            : user
	 * @param privilegeCache
	 *            : privilegeCache
	 * @return List
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public List getUserSitesWithDistributionPrev(User user, PrivilegeCache privilegeCache) throws BizLogicException {

		final List siteCollWithDistriPri = new ArrayList();
		this.getSiteIds(privilegeCache, siteCollWithDistriPri, user);

		return siteCollWithDistriPri;

	}

	/**
	 * Check for superAdmin
	 * 
	 * @param user
	 *            : user
	 * @return boolean
	 */
	public boolean isSuperAdmin(User user) {
		boolean isSuperAdmin = false;

		if (user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER)) {
			isSuperAdmin = true;
		}

		return isSuperAdmin;

	}

	/**
	 * To populate the request view bean
	 * 
	 * @param orderListFromDB
	 *            object
	 * @return List object
	 */
	private List populateRequestViewBeanList(List orderListFromDB) {
		final List requestList = new ArrayList();
		RequestViewBean requestViewBean = null;
		OrderDetails order = null;
		if (orderListFromDB != null) {
			final Iterator iter = orderListFromDB.iterator();
			while (iter.hasNext()) {
				order = (OrderDetails) iter.next();
				requestViewBean = OrderingSystemUtil.getRequestViewBeanToDisplay(order);

				requestViewBean.setRequestId(order.getId().toString());
				requestViewBean.setStatus(order.getStatus());
				if (order.getOrderItemCollection() != null) {
					requestViewBean.setOrderItemCount(order.getOrderItemCollection().size());
				}

				requestList.add(requestViewBean);
			}
		}
		return requestList;
	}

	/**
	 * @param identifier
	 *            : id
	 * @param dao
	 *            : dao
	 * @return to retrieve the orderDetails object.
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public OrderDetails getOrderListFromDB(String identifier, DAO dao) throws BizLogicException {
		try {
			return (OrderDetails) dao.retrieveById(OrderDetails.class.getName(), Long.parseLong(identifier));
		}
		catch (final NumberFormatException e) {
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, "number.format.exp", "");
		}
		catch (final DAOException e) {
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * 
	 * @param distributionProtId
	 *            : distributionProtId
	 * @return DistributionProtocol
	 */
	public DistributionProtocol retrieveDistributionProtocol(String distributionProtId) {
		try {
			final DAO dao = this.openDAOSession(null);
			final DistributionProtocol distributionProtocol = (DistributionProtocol) dao.retrieveById(
					DistributionProtocol.class.getName(), Long.parseLong(distributionProtId));
			dao.closeSession();
			return distributionProtocol;
		}
		catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * @param request
	 *            HttpServletRequest object
	 * @return List specimen array objects
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public List getSpecimenArrayDataFromDatabase(HttpServletRequest request) throws BizLogicException {
		final long startTime = System.currentTimeMillis();
		DAO dao = null;
		try {

			final HttpSession session = request.getSession(true);

			final String sourceObjectName = SpecimenArray.class.getName();
			final List valueField = (List) session.getAttribute(Constants.SPECIMEN_ARRAY_ID);
			dao = this.openDAOSession(null);
			final List specimenArrayList = new ArrayList();
			if (valueField != null && valueField.size() > 0) {
				for (int i = 0; i < valueField.size(); i++) {
					// List SpecimenArray = bizLogic.retrieve(sourceObjectName,
					// columnName, (String)valueField.get(i));
					final Object object = dao.retrieveById(sourceObjectName, Long.parseLong((String) valueField.get(i)));
					final SpecimenArray specArray = (SpecimenArray) object;
					specArray.getSpecimenArrayType();
					specArray.getSpecimenArrayType().getSpecimenTypeCollection();
					specimenArrayList.add(specArray);
				}
			}

			final long endTime = System.currentTimeMillis();
			LOGGER.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB -  : " + (endTime - startTime));
			return specimenArrayList;
		}
		catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
		finally {
			this.closeDAOSession(dao);
		}

	}

	/**
	 * function for getting data from database
	 * 
	 * @param request
	 *            HttpServletRequest object
	 * @return List of specimen objects
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public List getSpecimenDataFromDatabase(HttpServletRequest request) throws BizLogicException {
		// to get data from database when specimen id is given
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		final HttpSession session = request.getSession(true);

		final long startTime = System.currentTimeMillis();
		DAO dao = null;
		final List specimenList = new ArrayList();
		try {

			dao = this.openDAOSession(null);
			final String sourceObjectName = Specimen.class.getName();
			final String columnName = "id";
			final List valueField = (List) session.getAttribute("specimenId");

			if (valueField != null && valueField.size() > 0) {

				for (int i = 0; i < valueField.size(); i++) {
					final Object object = dao.retrieveById(sourceObjectName, Long.parseLong((String) valueField.get(i)));
					final Specimen spec = (Specimen) object;
					if (checkIfAvailableForOrder(spec)) {
						specimenList.add(spec);
					}
				}

			}
			final long endTime = System.currentTimeMillis();
			LOGGER.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB -  : " + (endTime - startTime));

		}
		catch (final DAOException e) {
			LOGGER.error(e.getMessage(), e);
			this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally {
			this.closeDAOSession(dao);
		}
		return specimenList;
	}

	private boolean checkIfAvailableForOrder(final Specimen spec) {
		return spec.getAvailableQuantity() > 0 && spec.getIsAvailable() && spec.getSpecimenPosition() != null
				&& Constants.ACTIVITY_STATUS_ACTIVE.equalsIgnoreCase(spec.getActivityStatus());
	}

	/**
	 * @param request
	 *            HttpServletRequest object
	 * @return List of Pathology Case objects
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public List getPathologyDataFromDatabase(HttpServletRequest request) throws BizLogicException {

		// to get data from database when specimen id is given
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.NEW_PATHOLOGY_FORM_ID);
		final List pathologicalCaseList = new ArrayList();

		final long startTime = System.currentTimeMillis();
		DAO dao = null;

		try {
			dao = this.openDAOSession(null);
			// retriving the id list from session.
			final String[] className = {IdentifiedSurgicalPathologyReport.class.getName(),
					DeidentifiedSurgicalPathologyReport.class.getName(), SurgicalPathologyReport.class.getName()};

			if (request.getSession().getAttribute(Constants.PATHALOGICAL_CASE_ID) != null) {
				this.getList(request, Constants.PATHALOGICAL_CASE_ID, className[0], pathologicalCaseList, dao);
			}
			if (request.getSession().getAttribute(Constants.DEIDENTIFIED_PATHALOGICAL_CASE_ID) != null) {
				this.getList(request, Constants.DEIDENTIFIED_PATHALOGICAL_CASE_ID, className[1], pathologicalCaseList, dao);
			}
			if (request.getSession().getAttribute(Constants.SURGICAL_PATHALOGY_CASE_ID) != null) {
				this.getList(request, Constants.SURGICAL_PATHALOGY_CASE_ID, className[2], pathologicalCaseList, dao);
			}

			final long endTime = System.currentTimeMillis();
			LOGGER.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB -  : " + (endTime - startTime));

		}
		catch (final DAOException e) {
			LOGGER.error(e.getMessage(), e);
			this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally {
			this.closeDAOSession(dao);
		}
		return pathologicalCaseList;
	}

	/**
	 * 
	 * @param request
	 *            : request
	 * @param attr
	 *            : attr
	 * @param className
	 *            : className
	 * @param pathologicalCaseList
	 *            : pathologicalCaseList
	 * @param dao
	 *            : dao
	 * @throws DAOException
	 *             : DAOException
	 */
	private void getList(HttpServletRequest request, String attr, String className, List pathologicalCaseList, DAO dao)
			throws DAOException {
		final List idList = (List) request.getSession().getAttribute(attr);
		final int size = idList.size();
		for (int i = 0; i < idList.size(); i++) {
			final Object object = dao.retrieveById(className, Long.parseLong((String) idList.get(i)));
			final SurgicalPathologyReport surgicalPathologyReport = (SurgicalPathologyReport) object;
			surgicalPathologyReport.getSpecimenCollectionGroup();
			surgicalPathologyReport.getSpecimenCollectionGroup().getSpecimenCollection();
			pathologicalCaseList.add(surgicalPathologyReport);
		}

	}

	/**
	 * 
	 * @param request
	 *            : request
	 * @return List
	 * @throws Exception
	 *             : Exception
	 */
	public List getDistributionProtocol(HttpServletRequest request) throws Exception {
		// to get the distribution protocol name
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final DistributionBizLogic dao = (DistributionBizLogic) factory.getBizLogic(Constants.DISTRIBUTION_FORM_ID);

		final String sourceObjectName = DistributionProtocol.class.getName();
		final String[] displayName = {"title"};
		final String valueField = Constants.SYSTEM_IDENTIFIER;
		final List protocolList = dao.getList(sourceObjectName, displayName, valueField, true);

		request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);
		return protocolList;
	}

	/**
	 * @param orderNew
	 *            : orderNew
	 * @param sessionDataBean
	 *            : sessionDataBean
	 * @param dao
	 *            : dao
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private void disposeSpecimen(OrderDetails orderNew, SessionDataBean sessionDataBean, DAO dao)
			throws BizLogicException {

		final NewSpecimenBizLogic newSpecimenBizLogic = new NewSpecimenBizLogic();
		final Collection orderItemCollection = orderNew.getOrderItemCollection();
		final StringBuffer disposalReason = new StringBuffer();
		disposalReason.append("Specimen distributed to distribution protocol ");
		disposalReason.append(orderNew.getDistributionProtocol().getTitle());
		disposalReason.append(" via order ");
		disposalReason.append(orderNew.getName());

		final Iterator orderItemIterator = orderItemCollection.iterator();
		try {
			while (orderItemIterator.hasNext()) {
				final OrderItem orderItem = (OrderItem) orderItemIterator.next();
				if (orderItem.getStatus().equals(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)) {
					final Collection distributionCollection = orderNew.getDistributionCollection();

					final Iterator iterator = distributionCollection.iterator();

					while (iterator.hasNext()) {
						final Distribution disrt = (Distribution) iterator.next();

						final Collection distributedItemCollection = disrt.getDistributedItemCollection();

						final Iterator iter = distributedItemCollection.iterator();

						while (iter.hasNext()) {
							final DistributedItem distributionItem = (DistributedItem) iter.next();
							if (orderItem.getDistributedItem().equals(distributionItem)) {

								final Specimen specimen = distributionItem.getSpecimen();
								final SpecimenArray specimenArray = distributionItem.getSpecimenArray();

								if (specimen != null) {

									newSpecimenBizLogic
											.disposeAndCloseSpecimen(sessionDataBean, specimen, dao, disposalReason.toString());

								}
								else if (specimenArray != null) {
									this.updateSpecimenArray(specimenArray, dao, sessionDataBean);

								}
							}
						}
					}
				}

			}
		}
		catch (final ApplicationException e) {
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * @param specimenArray
	 *            : specimenArray
	 * @param dao
	 *            ": dao
	 * @param sessionDataBean
	 *            : sessionDataBean
	 * @throws BizLogicException
	 *             : BizLogicException
	 * @throws DAOException
	 *             : DAOException
	 */
	private void updateSpecimenArray(SpecimenArray specimenArray, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, BizLogicException {

		final SpecimenArrayBizLogic specimenArrayBizLogic = new SpecimenArrayBizLogic();
		final SpecimenArray oldSpecimenArray = (SpecimenArray) dao.retrieveById(SpecimenArray.class.getName(),
				specimenArray.getId());
		final SpecimenArray newSpecimenArray = oldSpecimenArray;
		newSpecimenArray.setActivityStatus("Closed");
		specimenArrayBizLogic.update(dao, newSpecimenArray, oldSpecimenArray, sessionDataBean);

	}

	/**
	 * 
	 * @param specimenId
	 *            : specimenId
	 * @return Specimen
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public Specimen getSpecimenObject(Long specimenId) throws BizLogicException {
		DAO dao = null;
		Specimen specimen = null;
		try {
			dao = this.openDAOSession(null);
			final String sourceObjectName = Specimen.class.getName();
			specimen = (Specimen) dao.retrieveById(sourceObjectName, specimenId);

		}
		catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		finally {
			this.closeDAOSession(dao);
		}
		return specimen;
	}

	/**
	 * 
	 * @param specimenId
	 *            : specimenId
	 * @param dao
	 *            : dao
	 * @return Specimen
	 */
	public Specimen getSpecimen(Long specimenId, DAO dao) {
		final String sourceObjectName = Specimen.class.getName();
		Specimen specimen = null;
		try {
			specimen = (Specimen) dao.retrieveById(sourceObjectName, specimenId);
		}
		catch (final DAOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return specimen;
	}

	/**
	 * It retrieves the SCG
	 * 
	 * @param scgId
	 *            : scgId
	 * @return SpecimenCollectionGroup
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public SpecimenCollectionGroup retrieveSCG(Long scgId) throws BizLogicException {
		DAO dao = null;
		SpecimenCollectionGroup scg = null;
		try {

			dao = this.openDAOSession(null);
			scg = (SpecimenCollectionGroup) dao.retrieveById(SpecimenCollectionGroup.class.getName(), scgId);
		}
		catch (final DAOException e) {
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally {
			this.closeDAOSession(dao);
		}

		return scg;
	}

	/**
	 * It retrieves the NewSpecimenArrayOrder Item
	 * 
	 * @param newSpecimenArrayId
	 *            : newSpecimenArrayId
	 * @return NewSpecimenArrayOrderItem
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public NewSpecimenArrayOrderItem retrieveNewSpecimenArrayOrderItem(Long newSpecimenArrayId) throws BizLogicException {
		DAO dao = null;
		NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = null;
		try {
			dao = this.openDAOSession(null);

			newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) dao.retrieveById(
					NewSpecimenArrayOrderItem.class.getName(), newSpecimenArrayId);
			newSpecimenArrayOrderItem.setSpecimenArray(this.getSpecimenArray(newSpecimenArrayOrderItem.getId(), dao));

		}
		catch (final DAOException e) {
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally {
			this.closeDAOSession(dao);
		}

		return newSpecimenArrayOrderItem;
	}

	/**
	 * It retrieves the SpecimenOrderItem
	 * 
	 * @param specimenOrderItemId
	 *            : specimenOrderItemId
	 * @return SpecimenOrderItem
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public SpecimenOrderItem retrieveSpecimenOrderItem(Long specimenOrderItemId) throws BizLogicException {
		DAO dao = null;
		SpecimenOrderItem specimenOrderItem = null;
		try {
			dao = this.openDAOSession(null);
			specimenOrderItem = (SpecimenOrderItem) dao.retrieveById(SpecimenOrderItem.class.getName(), specimenOrderItemId);
			final NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = specimenOrderItem.getNewSpecimenArrayOrderItem();
			specimenOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayOrderItem);

		}
		catch (final DAOException e) {
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally {
			this.closeDAOSession(dao);
		}

		return specimenOrderItem;
	}

	/**
	 * To retreive the specimenArray
	 * 
	 * @param orderItemId
	 *            : orderItemId
	 * @param dao
	 *            : dao
	 * @return SpecimenArray
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private SpecimenArray getSpecimenArray(Long orderItemId, DAO dao) throws BizLogicException {
		SpecimenArray specimenArray = null;
		try {
			if (orderItemId != null) {
				final String hql = " select newSpecimenArrayOrderItem.specimenArray "
						+ // ,elements(specimenArray.
							// specimenArrayContentCollection)" +
						" from edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem as newSpecimenArrayOrderItem "
						+ " where newSpecimenArrayOrderItem.id = " + orderItemId;

				final List specimenArrayList = dao.executeQuery(hql);
				if (specimenArrayList != null && !specimenArrayList.isEmpty()) {
					specimenArray = (SpecimenArray) specimenArrayList.get(0);
				}
			}
		}
		catch (final DAOException daoExp) {
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return specimenArray;
	}

	/**
	 * 
	 * @param order
	 *            : order
	 * @param dao
	 *            : dao
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private void checkDistributionPtorocol(OrderDetails order, DAO dao) throws BizLogicException {
		try {
			final String[] selectColNames = {"distributionProtocol"};
			final String[] whereColNames = {Constants.SYSTEM_IDENTIFIER};
			final String[] whereColConditions = {"="};
			final Object[] whereColVal = {order.getId()};

			final QueryWhereClause queryWhereClause = new QueryWhereClause(OrderDetails.class.getName());
			queryWhereClause.addCondition(new EqualClause(edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER, order
					.getId()));

			final List list = dao.retrieve(OrderDetails.class.getName(), selectColNames, queryWhereClause);

			if (list != null && !list.isEmpty()) {
				final DistributionProtocol dist = (DistributionProtocol) list.get(0);
				if (dist != null) {
					this.checkStatus(dao, dist, "Distribution Protocol");
				}
			}
		}
		catch (final DAOException daoExp) {
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	/**
	 * * This method loads the title as Name and id as value of distribution
	 * protocol from database and return the namevalue bean of ditribution
	 * protocol for a given PI.
	 * 
	 * @param piID
	 *            : piID
	 * @param roleName
	 *            : roleName
	 * @param sessionDataBean
	 *            : sessionDataBean
	 * @return List : List
	 * @throws DAOException
	 *             : DAOException
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public List loadDistributionProtocol(final Long piID, String roleName, SessionDataBean sessionDataBean,
			String attributeToDisplay) throws DAOException, BizLogicException {
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		List distributionProtocolList = new ArrayList();

		final String sourceObjectName = DistributionProtocol.class.getName();
		final String[] displayName = {attributeToDisplay};
		final String valueFieldCol = Constants.ID;

		final String[] whereColNames = {Status.ACTIVITY_STATUS.toString()};
		final String[] whereColCond = {"!="};
		final Object[] whereColVal = {Status.ACTIVITY_STATUS_CLOSED.toString()};
		final String separatorBetweenFields = "";

		// checking for the role. if role is admin / supervisor then show all
		// the distribution protocols.
		if (roleName.equals(Constants.ADMINISTRATOR) || roleName.equals(Constants.SUPERVISOR)) {
			distributionProtocolList = bizLogic.getList(sourceObjectName, displayName, valueFieldCol, whereColNames,
					whereColCond, whereColVal, Constants.AND_JOIN_CONDITION, separatorBetweenFields, true);
		}
		else {
			final String[] whereColumnName = {"principalInvestigator.id", Status.ACTIVITY_STATUS.toString()};
			final String[] colCondition = {"=", "!="};
			final Object[] whereColumnValue = {piID, Status.ACTIVITY_STATUS_CLOSED.toString()};
			final String joinCondition = Constants.AND_JOIN_CONDITION;
			final boolean isToExcludeDisabled = true;

			// Get data from database
			distributionProtocolList = bizLogic.getList(sourceObjectName, displayName, valueFieldCol, whereColumnName,
					colCondition, whereColumnValue, joinCondition, separatorBetweenFields, isToExcludeDisabled);
		}

		// Fix for bug #9543 - start
		// Check for Distribution privilege & if privilege present, show all
		// DP's in DP list
		if (!roleName.equals(Constants.ADMINISTRATOR) && sessionDataBean != null) {
			final HashSet<Long> siteIds = new HashSet<Long>();
			final HashSet<Long> cpIds = new HashSet<Long>();
			boolean hasDistributionPrivilege = false;
			final IDAOFactory daoFact = DAOConfigFactory.getInstance().getDAOFactory(
					CommonServiceLocator.getInstance().getAppName());
			DAO dao = null;
			dao = daoFact.getDAO();
			try {
				// session = DBUtil.getCleanSession();
				dao.openSession(null);
				final User user = (User) dao.retrieveById(User.class.getName(), sessionDataBean.getUserId());
				final Collection<Site> siteCollection = user.getSiteCollection();
				final Collection<CollectionProtocol> cpCollection = user.getAssignedProtocolCollection();

				// Scientist
				if (siteCollection == null || siteCollection.isEmpty()) {
					return distributionProtocolList;
				}
				for (final Site site : siteCollection) {
					siteIds.add(site.getId());
				}
				if (cpCollection != null) {
					for (final CollectionProtocol cp : cpCollection) {
						cpIds.add(cp.getId());
					}
				}

				hasDistributionPrivilege = this.checkDistributionPrivilege(sessionDataBean, siteIds, cpIds);

				if (hasDistributionPrivilege) {
					distributionProtocolList = bizLogic.getList(sourceObjectName, displayName, valueFieldCol, whereColNames,
							whereColCond, whereColVal, Constants.AND_JOIN_CONDITION, separatorBetweenFields, true);
				}
			}
			finally {
				dao.closeSession();
			}
		}

		// Fix for bug #9543 - end

		return distributionProtocolList;
	}

	/**
	 * 
	 * @param sessionDataBean
	 *            : sessionDataBean
	 * @param siteIds
	 *            : siteIds
	 * @param cpIds
	 *            : cpIds
	 * @return boolean : boolean
	 */
	private boolean checkDistributionPrivilege(SessionDataBean sessionDataBean, HashSet<Long> siteIds, HashSet<Long> cpIds) {

		boolean hasDistributionPrivilege = false;
		try {
			String objectId = Site.class.getName();
			final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
					sessionDataBean.getUserName());

			for (final Long siteId : siteIds) {
				if (privilegeCache.hasPrivilege(objectId + "_" + siteId, Permissions.DISTRIBUTION)) {
					return true;
				}
			}
			objectId = CollectionProtocol.class.getName();
			for (final Long cpId : cpIds) {
				final boolean temp = privilegeCache.hasPrivilege(objectId + "_" + cpId, Permissions.DISTRIBUTION);
				if (temp) {
					return true;
				}
				hasDistributionPrivilege = AppUtility.checkForAllCurrentAndFutureCPs(Permissions.DISTRIBUTION, sessionDataBean,
						cpId.toString());
			}
		}
		catch (final SMException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return hasDistributionPrivilege;
	}

	public int getOrderCount(String status) {
		int count = 0;
		try {
			String sql = "select count(*) from catissue_order where status like '" + status + "'";
			List result = AppUtility.executeSQLQuery(sql);
			if (result != null) {
				List innnerResult = (List) result.get(0);
				count = Integer.valueOf(innnerResult.get(0).toString());
			}

		}
		catch (ApplicationException e) {
			e.printStackTrace();
		}
		return count;
	}

	public void getOrderDetail(Long orderId) throws ApplicationException {
		final IDAOFactory daoFact = DAOConfigFactory.getInstance().getDAOFactory(
				CommonServiceLocator.getInstance().getAppName());
		JDBCDAO dao = AppUtility.openJDBCSession();

		// OrderingSystemUtil.getSpecItemDetails(orderId,dao);

		String sql = "select cat.identifier,spec.identifier, spec.specimen_class, spec.specimen_type, spec.label, spec.AVAILABLE_QUANTITY,cot.REQUESTED_QUANTITY "
				+ " from catissue_specimen spec, catissue_existing_sp_ord_item cat,catissue_order_item cot where "
				+ " spec.identifier =cat.specimen_id and "
				+ " cat.identifier=cot.identifier and order_id=? "
				+ " union "
				+ "select cat.identifier,spec.identifier, spec.specimen_class, spec.specimen_type, spec.label, spec.AVAILABLE_QUANTITY,cot.REQUESTED_QUANTITY "
				+ " from catissue_specimen spec, catissue_derieved_sp_ord_item cat,catissue_order_item cot where "
				+ " spec.identifier =cat.specimen_id and " + " cat.identifier=cot.identifier and order_id=? ";
		ColumnValueBean bean = new ColumnValueBean(orderId);
		List attrList = new ArrayList();
		attrList.add(bean);
		attrList.add(bean);
		// List list = dao.executeQuery(sql);
		List list = dao.executeQuery(sql, attrList);
	}

	public Collection<ConsentDTO> getConsentDetails(List<String> specimenLabels, HibernateDAO dao)
			throws BizLogicException {
		Map<String, ConsentDTO> consentMap = new HashMap<String, ConsentDTO>();
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		for (String specimenLabel : specimenLabels) {
			NewSpecimenBizLogic newSpecimenBizLogic = new NewSpecimenBizLogic();
			Collection<ConsentTierDTO> consentTierDTOCollection = newSpecimenBizLogic.getConsentTireDTOs(specimenLabel, dao);

			// find the unique key
			String key = getUniqueKey((ArrayList<ConsentTierDTO>) consentTierDTOCollection);
			if (!consentTierDTOCollection.isEmpty()) {
				if (consentMap.containsKey(key)) {
					ConsentDTO existingConsentDTO = consentMap.get(key.toString());
					existingConsentDTO.getSpecimenLabels().add(specimenLabel);
				}
				else {
					ConsentDTO newConsentDTO = new ConsentDTO();
					List<String> labels = new ArrayList<String>();
					labels.add(specimenLabel);
					newConsentDTO.setSpecimenLabels(labels);
					newConsentDTO.setConsentTierDTOCollection(consentTierDTOCollection);
					consentMap.put(key, newConsentDTO);
				}
			}
		}
		return consentMap.values();
	}

	private String getUniqueKey(ArrayList<ConsentTierDTO> consentTierDTOs) {
		StringBuilder consentkey = new StringBuilder(100);
		Collections.sort(consentTierDTOs, new IdComparator());
		for (ConsentTierDTO consentTierDTO : consentTierDTOs) {
			consentkey.append(consentTierDTO.getId());
			consentkey.append(consentTierDTO.getStatus());
		}
		return consentkey.toString();
	}

	public List getOrderItemsDetail(Long orderId, HibernateDAO dao) throws Exception {
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.LONG, orderId));
		List<ExistingSpecimenOrderItem> orderItemsDetails = dao.executeNamedQuery("getOrderItems", substParams);
		List<OrderItemDTO> orderItemsDTOs = new ArrayList<OrderItemDTO>();
		for (ExistingSpecimenOrderItem object : orderItemsDetails) {
			//			Object[] orderItemDetails = (Object[]) object;
			OrderItemDTO orderItemDTO = new OrderItemDTO();
			Specimen specimen = object.getSpecimen();
			SpecimenCollectionGroup scg = specimen.getSpecimenCollectionGroup();
			CollectionProtocolRegistration cpr = scg.getCollectionProtocolRegistration();
			Participant participant = cpr.getParticipant();

			orderItemDTO.populateSpecimen(specimen);
			orderItemDTO.populateScg(scg);
			orderItemDTO.populateParticipant(participant);
			orderItemDTO.populateCpr(cpr);

			orderItemDTO.setAvailableQuantity(specimen.getAvailableQuantity());
			orderItemDTO.setRequestedQuantity(object.getRequestedQuantity());
			orderItemDTO.setStatus(object.getStatus());
			orderItemDTO.setDescription(object.getDescription());

			orderItemDTO.setOrderItemId(object.getId());
			orderItemDTO.setSpecimenId(specimen.getId());

			orderItemDTO.setDistributedQuantity(object.getDistribtedQuantity());

			Collection<ExternalIdentifier> exts = specimen.getExternalIdentifierCollection();
			Collection<ParticipantMedicalIdentifier> pmiColl = participant.getParticipantMedicalIdentifierCollection();
			int extIdSize = exts == null ? 0 : exts.size();
			int mrnSize = pmiColl == null ? 0 : exts.size();
			if (mrnSize == 0 && extIdSize == 0) {
				orderItemsDTOs.add(orderItemDTO);
			}
			else {
				if (extIdSize >= mrnSize) {
					Iterator<ParticipantMedicalIdentifier> itr = pmiColl.iterator();
					for (ExternalIdentifier exId : exts) {
						if (!StringUtils.isBlank(exId.getName())) {
							orderItemDTO.setExternalId(exId.getName()==null?"":exId.getName());
							orderItemDTO.setExternalValue(exId.getValue()==null?"":exId.getValue());
						}

						if (itr.hasNext()) {
							ParticipantMedicalIdentifier pmi = itr.next();
							if (!StringUtils.isBlank(pmi.getMedicalRecordNumber())) {
								orderItemDTO.setMrn(pmi.getMedicalRecordNumber());
								orderItemDTO.setSiteName(pmi.getSite().getName());
							}
						}
						orderItemsDTOs.add(orderItemDTO);

					}
				}
				else if (mrnSize >= extIdSize) {
					Iterator<ExternalIdentifier> itr = exts.iterator();
					for (ParticipantMedicalIdentifier pmi : pmiColl) {
						if (!StringUtils.isBlank(pmi.getMedicalRecordNumber())) {
							orderItemDTO.setMrn(pmi.getMedicalRecordNumber());
							orderItemDTO.setSiteName(pmi.getSite().getName());
						}

						if (itr.hasNext()) {
							ExternalIdentifier exId = itr.next();
							if (!StringUtils.isBlank(exId.getName())) {
								orderItemDTO.setExternalId(exId.getName());
								orderItemDTO.setExternalValue(exId.getValue());
							}
						}

						orderItemsDTOs.add(orderItemDTO);
					}
				}
			}
		}
		return orderItemsDTOs;
	}

	public DisplayOrderDTO getOrderDetails(Long orderId, HibernateDAO dao) throws BizLogicException, DAOException,
			ParseException {
		DisplayOrderDTO displayOrderDTO = new DisplayOrderDTO();
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.LONG, orderId));
		final List orderDetails = dao.executeNamedQuery("getOrderDetails", substParams);
		Object[] orderDetailObject = (Object[]) orderDetails.get(0);

		if (orderDetailObject[0] != null) {
			displayOrderDTO.setOrderName(orderDetailObject[0].toString());
		}
		else {
			displayOrderDTO.setOrderName("Order_" + orderId.toString());
		}
		if (orderDetailObject[2] != null) {
			displayOrderDTO.setDistributionProtocolName(orderDetailObject[2].toString());
		}
		if (orderDetailObject[4] != null && orderDetailObject[3] != null) {
			displayOrderDTO.setRequestorName(orderDetailObject[4] + ", " + orderDetailObject[3]);
		}
		if (orderDetailObject[5] != null) {
			displayOrderDTO.setRequestorEmail(orderDetailObject[5].toString());
		}
		displayOrderDTO.setRequestedDate(new Date(((Timestamp) orderDetailObject[6]).getTime()));
		if (orderDetailObject[7] != null) {
			displayOrderDTO.setComments(orderDetailObject[7].toString());
		}
		if (orderDetailObject[8] != null) {
			displayOrderDTO.setDistributorsComment(orderDetailObject[8].toString());
		}
		substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.LONG, orderId));
		final List distributionSites = dao.executeNamedQuery("getDistributionSiteName", substParams);
		if (!distributionSites.isEmpty()) {
			displayOrderDTO.setSiteName(distributionSites.get(0).toString());
		}
		return displayOrderDTO;
	}

	public OrderStatusDTO updateOrder(OrderSubmissionDTO orderSubmissionDTO, Long userId, HibernateDAO dao)
			throws BizLogicException {
		OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
		try {
			Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
			boolean isOrderProcessed = true;
			boolean isOrderNew = true;
			boolean isOrderRejected = true;
			Long distributionId = null;

			params = new HashMap<String, NamedQueryParam>();
			params.put("0", new NamedQueryParam(DBTypes.LONG, orderSubmissionDTO.getId()));
			List distributionDetails = dao.executeNamedQuery("getDistributionId", params);
			if (distributionDetails.isEmpty()) {
				DistributionBizLogic distributionBizLogic = new DistributionBizLogic();
				Distribution distribution = distributionBizLogic.insertDistribution(orderSubmissionDTO.getId(), userId,
						orderSubmissionDTO.getSite(), orderSubmissionDTO.getDisptributionProtocolId(), dao);
				distributionId = distribution.getId();
			}
			else {
				String updateDistributionDetails = "UPDATE edu.wustl.catissuecore.domain.Distribution distribution SET "
						+ "distribution.toSite = :site,distributionProtocol=:distributionProtocolId WHERE distribution.id=:distributionId";
				distributionId = Long.parseLong(distributionDetails.get(0).toString());
				List<ColumnValueBean> nameValueParams = new ArrayList<ColumnValueBean>();
				ColumnValueBean siteBean = new ColumnValueBean("site", orderSubmissionDTO.getSite());
				nameValueParams.add(siteBean);
				ColumnValueBean distributionProtocolIdBean = new ColumnValueBean("distributionProtocolId",
						orderSubmissionDTO.getDisptributionProtocolId());
				nameValueParams.add(distributionProtocolIdBean);
				ColumnValueBean distributionIdBean = new ColumnValueBean("distributionId", distributionId);
				nameValueParams.add(distributionIdBean);
				dao.executeUpdateHQL(updateDistributionDetails, nameValueParams);
			}

			for (OrderItemSubmissionDTO orderItemSubmissionDTO : orderSubmissionDTO.getOrderItemSubmissionDTOs()) {

				ColumnValueBean columnValueBean = new ColumnValueBean(orderItemSubmissionDTO.getOrderitemId());
				columnValueBean.setColumnName("id");
				List orderItems = dao.retrieve(OrderItem.class.getName(), columnValueBean);
				OrderItem orderItem = (OrderItem) orderItems.get(0);
				String orderItemlOldStatus = orderItem.getStatus();
				if (isUpdateAllowed(orderItemSubmissionDTO.getStatus(), orderItemlOldStatus)) {

					SpecimenEventParametersBizLogic specimenEventParametersBizLogic = new SpecimenEventParametersBizLogic();
					NewSpecimenBizLogic specimenBizLogic = new NewSpecimenBizLogic();

					if (isDistributed(orderItemSubmissionDTO.getStatus()) && !isDistributed(orderItemlOldStatus)) {

						String specimenUpdateStatus = specimenBizLogic.reduceQuantity(orderItemSubmissionDTO.getDistQty(),
								orderItemSubmissionDTO.getSpecimenId(), dao);
						if (!Constants.SUCCESS.equals(specimenUpdateStatus)) {
							OrderErrorDTO orderErrorDTO = new OrderErrorDTO();
							orderErrorDTO.setSpecimenLabel(orderItemSubmissionDTO.getSpecimenLabel());
							orderErrorDTO.setError(specimenUpdateStatus);
							orderStatusDTO.getOrderErrorDTOs().add(orderErrorDTO);
							if (ApplicationProperties.getValue("specimen.closed.unavailable").equals(specimenUpdateStatus)
									|| ApplicationProperties.getValue("specimen.virtualLocation").equals(specimenUpdateStatus)) {
								orderErrorDTO.setNewStatus(Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE);
							}
							else {
								orderErrorDTO.setNewStatus(Constants.ORDER_REQUEST_STATUS_NEW);
							}
							continue;
						}
						orderItem.setDistribtedQuantity(orderItemSubmissionDTO.getDistQty());
						specimenEventParametersBizLogic.createDistributionEvent(orderItemSubmissionDTO.getDistQty(),
								orderItemSubmissionDTO.getComments(), orderItemSubmissionDTO.getSpecimenId(), distributionId, dao,
								userId);
					}
					if (isClosed(orderItemSubmissionDTO.getStatus()) && !isClosed(orderItemlOldStatus)) {

						List<String> disposeReason = new ArrayList<String>();
						disposeReason.add(orderSubmissionDTO.getDisptributionProtocolName());
						disposeReason.add(orderSubmissionDTO.getOrderName());

						specimenEventParametersBizLogic.disposalEvent(
								ApplicationProperties.getValue("orderdistribution.disposeReason", disposeReason),
								orderItemSubmissionDTO.getSpecimenLabel(), Status.ACTIVITY_STATUS_CLOSED.toString(), dao, userId);
					}

					orderItem.setStatus(orderItemSubmissionDTO.getStatus());
					orderItem.setDescription(orderItemSubmissionDTO.getComments());
					dao.update(orderItem);
					isOrderProcessed = isOrderProcessed & isOrderItemProcessed(orderItemSubmissionDTO.getStatus());
					isOrderNew = isOrderNew & isOrderItemNew(orderItemSubmissionDTO.getStatus());
					isOrderRejected = isOrderRejected & isOrderItemRejected(orderItemSubmissionDTO.getStatus());
				}
				else {
					List<String> parameters = new ArrayList<String>();
					parameters.add(orderItemlOldStatus);
					parameters.add(orderItemSubmissionDTO.getStatus());

					OrderErrorDTO orderErrorDTO = new OrderErrorDTO();
					orderErrorDTO.setSpecimenLabel(orderItemSubmissionDTO.getSpecimenLabel());
					orderErrorDTO.setError(ApplicationProperties.getValue("orderItem.status.change.not.allow", parameters));
					orderErrorDTO.setNewStatus(orderItemlOldStatus);
					orderStatusDTO.getOrderErrorDTOs().add(orderErrorDTO);
				}
			}

			String orderStatus = Constants.ORDER_STATUS_PENDING;
			if (isOrderProcessed) {
				orderStatus = Constants.ORDER_STATUS_COMPLETED;
			}
			else if (isOrderNew) {
				orderStatus = Constants.ORDER_STATUS_NEW;
			}
			else if (isOrderRejected) {
				orderStatus = Constants.ORDER_STATUS_REJECTED;
			}
			String updateOrderDetailsQuery = "update edu.wustl.catissuecore.domain.OrderDetails orderDetails SET "
					+ " orderDetails.name=:name,orderDetails.status=:status,orderDetails.distributorsComment=:distributorsComment,"
					+ "orderDetails.distributionProtocol=:distributionProtocol,orderDetails.requestedBy=:requestedBy,orderDetails.requestedDate=:requestedDate "
					+ " where orderDetails.id=:id";

			List<ColumnValueBean> nameValueParams = new ArrayList<ColumnValueBean>();
			ColumnValueBean orderNameBean = new ColumnValueBean("name", orderSubmissionDTO.getOrderName());
			nameValueParams.add(orderNameBean);
			ColumnValueBean orderStatusBean = new ColumnValueBean("status", orderStatus);
			nameValueParams.add(orderStatusBean);
			ColumnValueBean distibutorsCommentBean = null;

			if (orderSubmissionDTO.getDistributorsComment() == null) {
				distibutorsCommentBean = new ColumnValueBean("distributorsComment", "");
			}
			else {
				distibutorsCommentBean = new ColumnValueBean("distributorsComment", orderSubmissionDTO.getDistributorsComment());
			}

			nameValueParams.add(distibutorsCommentBean);

			ColumnValueBean distributionProtocolBean = new ColumnValueBean("distributionProtocol",
					orderSubmissionDTO.getDisptributionProtocolId());
			nameValueParams.add(distributionProtocolBean);

			ColumnValueBean requestedByBean = new ColumnValueBean("requestedBy", orderSubmissionDTO.getRequestorId());
			nameValueParams.add(requestedByBean);

			ColumnValueBean requestedDateBean = new ColumnValueBean("requestedDate", orderSubmissionDTO.getRequestedDate());
			nameValueParams.add(requestedDateBean);

			ColumnValueBean idBean = new ColumnValueBean("id", orderSubmissionDTO.getId());
			nameValueParams.add(idBean);

			dao.executeUpdateHQL(updateOrderDetailsQuery, nameValueParams);

			orderStatusDTO.setStatus(orderStatus);
			orderStatusDTO.setOrderId(orderSubmissionDTO.getId());
		}
		catch (DAOException daoException) {
			throw new BizLogicException(null, daoException, null);
		}
		return orderStatusDTO;
	}

	private boolean isUpdateAllowed(String newStatus, String orderItemlOldStatus) {
		if (isDistributed(orderItemlOldStatus) && !isDistributed(newStatus)) {
			return false;
		}
		return true;
	}

	private boolean isOrderItemNew(String orderStatus) {
		return Constants.ORDER_STATUS_NEW.equals(orderStatus);
	}

	private boolean isOrderItemRejected(String orderStatus) {
		for (String status : REJECTED_STATUS_LIST) {
			if (status.equals(orderStatus)) {
				return true;
			}
		}
		return false;
	}

	private boolean isOrderItemPending(String orderStatus) {
		for (String status : PENDING_STATUS_LIST) {
			if (status.equals(orderStatus)) {
				return true;
			}
		}
		return false;
	}

	private boolean isOrderItemProcessed(String orderStatus) {
		for (String status : PROCESSED_STATUS_LIST) {
			if (status.equals(orderStatus)) {
				return true;
			}
		}
		return false;
	}

	private boolean isClosed(String orderStatus) {
		for (String status : CLOSED_STATUS_LIST) {
			if (status.equals(orderStatus)) {
				return true;
			}
		}
		return false;
	}

	private boolean isDistributed(String orderStatus) {
		for (String status : DISTRIBUTE_STATUS_LIST) {
			if (status.equals(orderStatus)) {
				return true;
			}
		}
		return false;
	}

	public String getGridXMLString(Long orderId, HibernateDAO dao) throws Exception {
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.LONG, orderId));
		List orderItems = dao.executeNamedQuery("getOrderItems", substParams);
		String gridXMLString = VelocityManager.getInstance().evaluate(orderItems, "orderGridTemplate.vm");
		return gridXMLString;
	}

	public Map<String, Object> getOrderCsv(Long orderId, String exportedBy, HibernateDAO dao, String exportedItems)
			throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		DisplayOrderDTO displayOrderDTO = getOrderDetails(orderId, dao);
		PrintWriter pw = null;
		Map<String, Object> orderDetailMap = new HashMap<String, Object>();
		try {
			String currentDate = CommonUtilities.parseDateToString(new Date(), CommonServiceLocator.getInstance()
					.getDatePattern());
			String requestedDateStr = CommonUtilities.parseDateToString(displayOrderDTO.getRequestedDate(),
					CommonServiceLocator.getInstance().getDatePattern());
			orderDetailMap.put("orderDetailMap", displayOrderDTO);
			orderDetailMap.put("requestedDate", requestedDateStr);
			orderDetailMap.put("exportedOn", currentDate);
			orderDetailMap.put("exportedBy", exportedBy);
			orderDetailMap.put("orderItemsDetail", getOrderItemsDetail(orderId, dao));
			ExportedItemDto dto = populateITemList(exportedItems);
			orderDetailMap.put("exportedItems", dto.getExportedItems());
			orderDetailMap.put("exportDto", dto);

			orderDetailMap.put("StringUtils", new StringUtils());
			String obj = VelocityManager.getInstance().evaluate(orderDetailMap, "orderGridCsvTemplate.vm");
			returnMap.put("fileData", obj.getBytes());
			returnMap.put("fileName", displayOrderDTO.getOrderName() + "_" + currentDate + Constants.CSV_FILE_EXTENTION);

		}
		finally {
			// pw.close();
		}
		// requestlist.dataTabel.OrderName.label

		return returnMap;

	}

	private ExportedItemDto populateITemList(String exportedItems) {
		ExportedItemDto dto = new ExportedItemDto(exportedItems);
		return dto;
	}

	public List getOrderItemsDetailForGrid(long orderId, HibernateDAO dao) throws DAOException {
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.LONG, orderId));
		List<ExistingSpecimenOrderItem> orderItemsDetails = dao.executeNamedQuery("getOrderItems", substParams);
		List<OrderItemDTO> orderItemsDTOs = new ArrayList<OrderItemDTO>();
		for (ExistingSpecimenOrderItem object : orderItemsDetails) {
			//			Object[] orderItemDetails = (Object[]) object;
			OrderItemDTO orderItemDTO = new OrderItemDTO();
			Specimen specimen = object.getSpecimen();

			orderItemDTO.populateSpecimen(specimen);

			orderItemDTO
					.setAvailableQuantity(specimen.getAvailableQuantity() == null ? 0.0 : specimen.getAvailableQuantity());
			orderItemDTO.setRequestedQuantity(object.getRequestedQuantity());
			orderItemDTO.setStatus(object.getStatus());
			orderItemDTO.setDescription(object.getDescription());

			orderItemDTO.setOrderItemId(object.getId());
			orderItemDTO.setSpecimenId(specimen.getId());

			orderItemDTO.setDistributedQuantity(object.getDistribtedQuantity());

			orderItemsDTOs.add(orderItemDTO);
		}

		return orderItemsDTOs;
	}

}