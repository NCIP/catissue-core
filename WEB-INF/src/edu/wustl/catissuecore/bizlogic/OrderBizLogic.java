/**
 * <p>Title: Order Class>
 * <p>Description:  Bizlogic for Ordering System.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on October 10,2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.omg.CosNaming.IstringHelper;

import edu.wustl.catissuecore.bean.RequestViewBean;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup;
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
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.OrderingSystemUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

public class OrderBizLogic extends DefaultBizLogic
{
	//To calculate the Order status.
	private int orderStatusNew = 0;
	private int orderStatusPending = 0;
	private int orderStatusRejected = 0;
	private int orderStatusCompleted = 0;
	

	//To display the number of Order Items updated.
	public static int numberItemsUpdated = 0;

	/**
	 * Saves the OrderDetails object in the database.
	 * @param obj The OrderDetails Item object to be saved.
	 * @param sessionDataBean The session in which the object is saved.
	 * @param dao object.
	 * @throws DAOException object.
	 * @throws UserNotAuthorizedException object.
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		OrderDetails order = (OrderDetails) obj;
		dao.insert(order, sessionDataBean, true, true);
		//	mailOnSuccessfulSave(sessionDataBean, order, dao);
	}

	/**
	 * This function send mail to the Administrator and Scientist if items ordered by scientist is placed successfully.
	 * @param sessionDataBean object
	 * @param order object
	 * @param dao object
	 * @throws DAOException object
	 */
	private void mailOnSuccessfulSave(SessionDataBean sessionDataBean, OrderDetails order, DAO dao) throws DAOException
	{
		boolean emailSent = false;
		List userList = dao.retrieve(User.class.getName(), "emailAddress", sessionDataBean.getUserName());
		if (userList != null && !userList.isEmpty())
		{
			User userObj = (User) userList.get(0);
			EmailHandler emailHandler = new EmailHandler();

			String emailBody = makeEmailBodyForOrderPlacement(userObj, order, dao);
			String subject = "The Order " + order.getName() + " has been successfully placed.";
			emailSent = emailHandler.sendEmailForOrderingPlacement(userObj.getEmailAddress(), emailBody, subject);

			if (emailSent)
			{
				Logger.out.debug(" In OrderBizLogic --> Email sent To Admin and Scientist successfully ");
			}
			else
			{
				Logger.out.debug(" In OrderBizLogic --> Email could not be Sent ");
			}
		}
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param sessionDataBean The session in which the object is saved.
	 * @param dao object.
	 * @param oldObj The old object from DB.
	 * @throws DAOException object
	 * @throws UserNotAuthorizedException object
	 */
	
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		this.validate(obj, oldObj, dao, Constants.EDIT);
		OrderDetails orderImplObj = (OrderDetails) HibernateMetaData.getProxyObjectImpl(oldObj);
		OrderDetails orderNew = updateObject(orderImplObj, obj, dao, sessionDataBean);
		dao.update(orderNew, sessionDataBean, true, true, false);
		disposeSpecimen(orderNew, sessionDataBean, dao);
		//Sending Email only if atleast one order item is updated.
		if (numberItemsUpdated > 0 && orderNew.getMailNotification())
		{
			sendEmailOnOrderUpdate(dao, orderNew, sessionDataBean);
		}
	}
	
   /**
	 * @param obj object
	 * @param oldObj object
	 * @param dao object
	 * @param operation object
	 * @return boolean object
	 * @throws DAOException object
	 */
	protected boolean validate(Object obj, Object oldObj, DAO dao, String operation) throws DAOException
	{
		OrderDetails order = (OrderDetails) obj;
		OrderDetails oldOrder = (OrderDetails) oldObj;
		checkDistributionPtorocol(order,dao);
		if ((oldOrder.getStatus().trim().equalsIgnoreCase("Pending") && order.getStatus().trim().equalsIgnoreCase("New"))
				|| (oldOrder.getStatus().trim().equalsIgnoreCase("Rejected") && (order.getStatus().trim().equalsIgnoreCase("New") || order
						.getStatus().trim().equalsIgnoreCase("Pending")))
				|| (oldOrder.getStatus().trim().equalsIgnoreCase("Completed") && (order.getStatus().trim().equalsIgnoreCase("New")
						|| order.getStatus().trim().equalsIgnoreCase("Pending") || order.getStatus().trim().equalsIgnoreCase("Rejected"))))
		{
			throw new DAOException(ApplicationProperties.getValue("orderdistribution.status.errmsg"));
		}
		//Site is mandatory on UI.
		Collection distributionCollection = order.getDistributionCollection();
		if (distributionCollection != null)
		{
			Iterator distributionCollectionIterator = distributionCollection.iterator();
			while (distributionCollectionIterator.hasNext())
			{
				Distribution distribution = (Distribution) distributionCollectionIterator.next();
				if (distribution.getToSite().getId().compareTo(new Long(-1)) == 0)
				{
					throw new DAOException(ApplicationProperties.getValue("orderdistribution.site.required.errmsg"));
				}
			}
		}
		//Quantity is mandatory
		Collection orderItemCollection = order.getOrderItemCollection();
		Iterator iter = orderItemCollection.iterator();
		while (iter.hasNext())
		{
			OrderItem orderItem = (OrderItem) iter.next();
			/*if (orderItem.getDistributedItem() != null)
			{
				if (orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
						||orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE))
				{
					if (orderItem.getDistributedItem().getQuantity() == null
							|| orderItem.getDistributedItem().getQuantity().toString().equalsIgnoreCase(""))
					{
						throw new DAOException(ApplicationProperties.getValue("orderdistribution.quantity.required.errmsg"));
					}
					//				Quantity cannot be characters and Quantity cannot be negative number.
					if (!validator.isDouble(orderItem.getDistributedItem().getQuantity().toString()))
					{
						throw new DAOException(ApplicationProperties.getValue("orderdistribution.quantity.format.errmsg"));
					}
				}
			}*/
			Collection oldOrderItemColl = oldOrder.getOrderItemCollection();
			Iterator oldOrderItemCollIter = oldOrderItemColl.iterator();
			while (oldOrderItemCollIter.hasNext())
			{
				OrderItem oldorderItem = (OrderItem) oldOrderItemCollIter.next();
				if (oldorderItem.getId().compareTo(orderItem.getId()) == 0)
				{
					//If requestFor drop down is null, do not allow distribution of that order item
					if (orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
							||orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)
							|| orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
					{
						if (oldorderItem instanceof DerivedSpecimenOrderItem)
						{
							DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem) oldorderItem;
							if (oldorderItem.getDistributedItem() == null && orderItem.getDistributedItem().getSpecimen().getId() == null)
							{
								throw new DAOException(ApplicationProperties.getValue("orderdistribution.distribution.notpossible.errmsg"));
							}

						}
						else if (oldorderItem instanceof NewSpecimenArrayOrderItem)
						{
							SpecimenArray  specimenArray = (SpecimenArray)getSpecimenArray(oldorderItem.getId(),dao);
							NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem)oldorderItem;
							newSpecimenArrayOrderItem.setSpecimenArray(specimenArray);
							if( newSpecimenArrayOrderItem.getSpecimenArray() == null)
							{
								throw new DAOException(ApplicationProperties.getValue("orderdistribution.distribution.arrayNotCreated.errmsg"));
							}
						}
						else if (oldorderItem instanceof PathologicalCaseOrderItem)
						{
							PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) oldorderItem;
							if (pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSpecimenCollection() == null
									|| pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSpecimenCollection().size() == 0)
							{
								if (orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
								{
									throw new DAOException(ApplicationProperties.getValue("orderdistribution.arrayPrep.notpossibleForPatho.errmsg"));
								}
								else
								{
									throw new DAOException(ApplicationProperties
											.getValue("orderdistribution.distribution.notpossibleForPatho.errmsg"));
								}
							}
							if (pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSpecimenCollection() != null)
							{
								Collection specimenColl = pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSpecimenCollection();
								Iterator specimenCollIter = specimenColl.iterator();
								List totalChildrenSpecimenColl = new ArrayList();
								while (specimenCollIter.hasNext())
								{
									Specimen specimen = (Specimen) specimenCollIter.next();
									List childSpecimenCollection = OrderingSystemUtil
											.getAllChildrenSpecimen(specimen.getChildSpecimenCollection());
									List finalChildrenSpecimenCollection = null;
									if (pathologicalCaseOrderItem.getSpecimenClass() != null && pathologicalCaseOrderItem.getSpecimenType() != null
											&& !pathologicalCaseOrderItem.getSpecimenClass().trim().equalsIgnoreCase("")
											&& !pathologicalCaseOrderItem.getSpecimenType().trim().equalsIgnoreCase(""))
									{ //"Derived"	   
										finalChildrenSpecimenCollection = OrderingSystemUtil.getChildrenSpecimenForClassAndType(
												childSpecimenCollection, pathologicalCaseOrderItem.getSpecimenClass(), pathologicalCaseOrderItem
														.getSpecimenType());
									}
									else
									{ //"Block" . Specimen class = "Tissue" , Specimen Type = "Block".
										finalChildrenSpecimenCollection = OrderingSystemUtil.getChildrenSpecimenForClassAndType(
												childSpecimenCollection, "Tissue", "Block");
									}
									//						    		adding specimen to the collection
									if (specimen.getClassName().equalsIgnoreCase(pathologicalCaseOrderItem.getSpecimenClass())
											&& specimen.getSpecimenType().equalsIgnoreCase(pathologicalCaseOrderItem.getSpecimenType()))
									{
										finalChildrenSpecimenCollection.add(specimen);
									}
									if (finalChildrenSpecimenCollection != null)
									{
										Iterator finalChildrenSpecimenCollectionIterator = finalChildrenSpecimenCollection.iterator();
										while (finalChildrenSpecimenCollectionIterator.hasNext())
										{
											totalChildrenSpecimenColl.add((Specimen) (finalChildrenSpecimenCollectionIterator.next()));
										}
									}
								}
								if (totalChildrenSpecimenColl == null)
								{
									if (orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
									{
										throw new DAOException(ApplicationProperties.getValue("orderdistribution.arrayPrep.notpossible.errmsg"));
									}
									else
									{
										throw new DAOException(ApplicationProperties.getValue("orderdistribution.distribution.notpossible.errmsg"));
									}
								}
							}
						}

						//Fix me
						//Removing distributed item in case of status - Ready For Array Preparation
						if (orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
						{
							DistributedItem distributedItem = orderItem.getDistributedItem();
							if (distributedItem != null)
							{
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
	 * @param oldObj object
	 * @param obj object
	 * @param dao object
	 * @param sessionDataBean object
	 * @return OrderDetails object
	 * @throws DAOException object.
	 * @throws UserNotAuthorizedException object.
	 */
	private OrderDetails updateObject(Object oldObj, Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException,
			UserNotAuthorizedException
	{
		OrderDetails order = (OrderDetails) obj;
		OrderDetails orderOld = (OrderDetails) oldObj;
		//Getting OrderItems Collection.
		Collection oldOrderItemSet = orderOld.getOrderItemCollection();
		Collection newOrderItemSet = order.getOrderItemCollection();
		//		Adding items inside New Defined Array in oldOrderItemColl
		Collection tempNewOrderItemSet = new HashSet();
		Iterator tempNewItemIter = newOrderItemSet.iterator();
		/*while (tempNewItemIter.hasNext())
		{
			OrderItem tempNewOrderItem = (OrderItem) tempNewItemIter.next();

			if (tempNewOrderItem instanceof NewSpecimenArrayOrderItem)
			{
				NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) tempNewOrderItem;
				Collection specimenOrderItemColl = newSpecimenArrayOrderItem.getSpecimenOrderItemCollection();
				if (specimenOrderItemColl != null)
				{
					Iterator tempIter = specimenOrderItemColl.iterator();
					while (tempIter.hasNext())
					{
						tempNewOrderItemSet.add((OrderItem) tempIter.next());
					}
				}
			}
		}*/
		if (tempNewOrderItemSet.size() > 0)
		{
			newOrderItemSet.addAll(tempNewOrderItemSet);
		}
		//Iterating over OrderItems collection.
		Iterator newSetIter = newOrderItemSet.iterator();
		numberItemsUpdated = 0;
		//To insert distribution and distributed items only once.
		boolean isDistributionInserted = false;

		while (newSetIter.hasNext())
		{
			OrderItem newOrderItem = (OrderItem) newSetIter.next();

			Iterator oldSetIter = oldOrderItemSet.iterator();

			while (oldSetIter.hasNext())
			{
				OrderItem oldOrderItem = (OrderItem) oldSetIter.next();

				// Setting quantity to previous quantity.....
				if (oldOrderItem.getId().equals(newOrderItem.getId()))
				{
					newOrderItem.setRequestedQuantity(oldOrderItem.getRequestedQuantity());
				}
				//Update Old OrderItem only when its Id matches with NewOrderItem id and the order is not distributed and the oldorderitem status and neworderitem status are different or description has been updated.
				if ((oldOrderItem.getId().compareTo(newOrderItem.getId()) == 0)
						&& (oldOrderItem.getDistributedItem() == null)
						&& (!oldOrderItem.getStatus().trim().equalsIgnoreCase(newOrderItem.getStatus().trim()) || (oldOrderItem.getDescription() != null && !oldOrderItem
								.getDescription().equalsIgnoreCase(newOrderItem.getDescription()))))
				{
					if (newOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
							||newOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE))
					{
						if (!isDistributionInserted)
						{
							//Direct call to DistributionBizLogic
							Collection newDistributionColl = order.getDistributionCollection();
							Iterator iter = newDistributionColl.iterator();
							DistributionBizLogic distributionBizLogic = (DistributionBizLogic) BizLogicFactory.getInstance().getBizLogic(
									Constants.DISTRIBUTION_FORM_ID);
							while (iter.hasNext())
							{
								Distribution distribution = (Distribution) iter.next();
								//Populating Distribution Object.
								distribution.setOrderDetails(orderOld);
								//Setting the user for distribution.
								User user = new User();
								try
								{
									if(sessionDataBean.getUserId()!=null)
									{
										user.setId(sessionDataBean.getUserId());
									}
							        else
							        {
							        	user.setId(distribution.getDistributedBy().getId());
							        }
								}
								catch(NullPointerException npe)
								{
									throw new UserNotAuthorizedException("Please mention distributedBy attribute");
								}
								distribution.setDistributedBy(user);
								//Setting activity status
								distribution.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
								//Setting Event Timestamp
								Date date = new Date();
								distribution.setTimestamp(date);
								//Inserting distribution
								distributionBizLogic.insert(distribution, dao, sessionDataBean);
								isDistributionInserted = true;
							}
						}
						//For assigned Quantity and RequestFor.
						/*if(newOrderItem.getDistributedItem() != null)
						 {
						 oldOrderItem.setDistributedItem(newOrderItem.getDistributedItem());
						 }*/
				}
					//Setting Description and Status.
					if (newOrderItem.getDescription() != null)
					{
						if (newOrderItem instanceof ExistingSpecimenArrayOrderItem)
						{
							newOrderItem.setDescription(oldOrderItem.getDescription() + " " + newOrderItem.getDescription());
						}
					}
					//The number of Order Items updated.
					numberItemsUpdated++;
				}
				else if ((oldOrderItem.getId().compareTo(newOrderItem.getId()) == 0) && oldOrderItem.getDistributedItem() != null)
				{
					Object object = dao.retrieve(DistributedItem.class.getName(), oldOrderItem.getDistributedItem().getId());
					if (object != null)
					{
						DistributedItem distributedItem = (DistributedItem) object;
						newOrderItem.setDistributedItem(distributedItem);
					}

				}
			}
			calculateOrderStatus(newOrderItem);
		}
		//Updating comments.
		if (order.getComment() != null && !order.getComment().trim().equalsIgnoreCase(""))
		{
			order.setComment(orderOld.getComment() + " " + order.getComment());
		}
		//For order status.
		order = updateOrderStatus(order, newOrderItemSet);
		return order;
	}

	/**
	 * @param order object
	 * @param sessionDataBean object
	 */
	private void sendEmailOnOrderUpdate(DAO dao, OrderDetails order, SessionDataBean sessionDataBean)
	{
		EmailHandler emailHandler = new EmailHandler();
		String ccEmailAddress = order.getDistributionProtocol().getPrincipalInvestigator().getEmailAddress(); // cc
		String toEmailAddress = sessionDataBean.getUserName();  // to person ordering 
		String fromEmailAddress = XMLPropertyHandler.getValue("email.administrative.emailAddress"); // from bcc
		//ccEmailAddress += ccEmailAddress +" "+fromEmailAddress;
		String bccEmailAddress = fromEmailAddress;
		String subject = "Notification on Order " + order.getName() + " Status from caTissue Administrator";
		String body = makeEmailBodyForOrderUpdate(dao, order);
		emailHandler.sendEmailForOrderDistribution(body, toEmailAddress, fromEmailAddress, ccEmailAddress, bccEmailAddress, subject);
	}

	/**
	 * @param order object
	 * @return String object
	 */
	private String makeEmailBodyForOrderUpdate(DAO dao, OrderDetails order)
	{
		Collection orderItemColl = order.getOrderItemCollection();
		String emailFormat = "";
		
	    String emailBodyHeader = "Hello " + order.getDistributionProtocol().getPrincipalInvestigator().getFirstName() + " "
		+ order.getDistributionProtocol().getPrincipalInvestigator().getLastName() +",  \n\n"
		+ "This is in relation with the order you placed with us. Please find below the details on its status. \n\n" ;
	  
	    Iterator iter = orderItemColl.iterator();
		int serialNo = 1;
		String emailBody = "";
		while (iter.hasNext())
		{
		    OrderItem orderItem = (OrderItem) iter.next();
		    
		    if (orderItem instanceof ExistingSpecimenOrderItem)
			{
			    ExistingSpecimenOrderItem existingSpecimenOrderItem = (ExistingSpecimenOrderItem) orderItem;
				emailBody = emailBody + serialNo + ". " + existingSpecimenOrderItem.getSpecimen().getLabel() + ": "
						+ existingSpecimenOrderItem.getStatus() + "\n   Order Description: "+ existingSpecimenOrderItem.getDescription() + "\n\n";
		    	
				serialNo++;
			}
			else if (orderItem instanceof DerivedSpecimenOrderItem)
			{
				 DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem) orderItem;
					emailBody = emailBody + serialNo + ". " + derivedSpecimenOrderItem.getParentSpecimen().getLabel() + ": "
					+ derivedSpecimenOrderItem.getStatus() + "\n   Order Description: "+ derivedSpecimenOrderItem.getDescription() + "\n\n";
					
					serialNo++;
	    	
			 }
			else if (orderItem instanceof PathologicalCaseOrderItem)
			{
				PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) orderItem;
			    emailBody = emailBody + serialNo + ". " + pathologicalCaseOrderItem.getDistributedItem().getDistribution().getOrderDetails().getName()  + ": "
				+ pathologicalCaseOrderItem.getStatus() + "\n   Order Description: "+ pathologicalCaseOrderItem.getDescription() + "\n\n";
				
				serialNo++; 
    	
			 }
			else if (orderItem instanceof NewSpecimenArrayOrderItem)
			{
				NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) orderItem;
			    emailBody = emailBody + serialNo + ". " + newSpecimenArrayOrderItem.getName()  + ": "
				+ newSpecimenArrayOrderItem.getStatus() + "\n   Order Description: "+ newSpecimenArrayOrderItem.getDescription() + "\n\n";
				
				serialNo++;
    		
		    }
			else if (orderItem instanceof ExistingSpecimenArrayOrderItem)
			{
				ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = (ExistingSpecimenArrayOrderItem) orderItem;
				emailBody = emailBody + serialNo + ". " + existingSpecimenArrayOrderItem.getSpecimenArray().getName()  + ": "
				+ existingSpecimenArrayOrderItem.getStatus() + "\n   Order Description: "+ existingSpecimenArrayOrderItem.getDescription() + "\n\n";
    		
				serialNo++;
			}
		}
		String emailMsgFooterRegards = "\n" + "Regards, ";
		String emailMsgFooterSign = "\n" + "caTissueSuite Administrator";
		String emailMsgFooter = emailMsgFooterRegards + emailMsgFooterSign;
		emailFormat = emailBodyHeader + emailBody + emailMsgFooter;
		return emailFormat;
	}

	
	/**
	 * @param oldOrderItem object
	 */
	private void calculateOrderStatus(OrderItem oldOrderItem)
	{
		//Order id is null for specimen orderItems associated with NewSpecimenArrayOrderItem
		if (oldOrderItem.getOrderDetails() != null)
		{
			//			For order status
			if (oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_NEW))
			{
				orderStatusNew++;
			}//kalpana bug #5839 If the specimens inside the specimen Array and if it's status is ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION then mark it complete.
			else if (oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
					||oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)
					|| oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
			{
				orderStatusCompleted++;
			}
			else if (oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION)
					|| oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW)
					|| oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION))
			{
				orderStatusPending++;
			}
			else if (oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST)
					|| oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE)
					|| oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE))
			{
				orderStatusRejected++;
			}
		}
	}

	/**
	 * @param orderOld object
	 * @param oldOrderItemSet object
	 * @return OrderDetails object.
	 */
	private OrderDetails updateOrderStatus(OrderDetails orderNew, Collection oldOrderItemSet)
	{
		if (orderStatusNew == oldOrderItemSet.size())
		{
			orderNew.setStatus(Constants.ORDER_STATUS_NEW);
		}
		else if (orderStatusRejected == oldOrderItemSet.size())
		{
			orderNew.setStatus(Constants.ORDER_STATUS_REJECTED);
		}
		else if ((orderStatusCompleted == oldOrderItemSet.size()) || ((orderStatusCompleted + orderStatusRejected) == oldOrderItemSet.size()))
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
	 * @param userObj  User object containing the logged in user info
	 * @param order OrderDetails instance containing details of the requested order and order items under that order
	 * @param dao object
	 * @return emailBody String containing the email message body
	 * @throws DAOException object
	 */
	private String makeEmailBodyForOrderPlacement(User userObj, OrderDetails order, DAO dao) throws DAOException
	{
		String emailBodyHeader = "Dear caTissue Administrator ,";
		
		Object object = dao.retrieve(DistributionProtocol.class.getName(), order.getDistributionProtocol().getId());
		String messageLine1 = "This is to notify that the Order " + order.getName() + " requested by " + userObj.getFirstName() + " "
				+ userObj.getLastName() + " under Distribution Protocol " + ((DistributionProtocol) object).getTitle()
				+ " have been placed successfully.";

		//String messageLine2 = "The details of the Order are as follows:";

		//String emailMsgHeader = emailBodyHeader + "\n" + messageLine1 + "\n" + messageLine2 + "\n";
		String emailMsgHeader = emailBodyHeader + "\n" + messageLine1 + "\n";
		//String emailMsgFooter = "\n" + "We will get back to you shortly with status of each of the items requested";
		String emailMsgFooterRegards = "\n" + "Regards, ";
		String emailMsgFooterSign = "\n" + "caTissue Administrator";
		String emailMsgFooter = emailMsgFooterRegards + emailMsgFooterSign;

		int serialNo = 1;

		String orderItemHeader = "\n" + "#" + "\t" + "OrderItem" + "\t" + "Quantity" + "\n";

		Collection orderItemCollection = order.getOrderItemCollection();
		Iterator orderItemCollectionItr = orderItemCollection.iterator();
		String itemInfo = "";
		/*while(orderItemCollectionItr.hasNext())
		 {
		 OrderItem orderItem = (OrderItem)orderItemCollectionItr.next();
		 if(orderItem instanceof ExistingSpecimenOrderItem)
		 {
		 ExistingSpecimenOrderItem existingSpecimenOrderItem =(ExistingSpecimenOrderItem)orderItem;
		 itemInfo = itemInfo + serialNo+ "." + "\t"+ existingSpecimenOrderItem.getSpecimen().getLabel() + "\t" + existingSpecimenOrderItem.getRequestedQuantity().getValue() + "\n";
		 }
		 else if(orderItem instanceof DerivedSpecimenOrderItem)
		 {
		 DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem)orderItem;
		 itemInfo = itemInfo + + serialNo+ "." + "\t"+ derivedSpecimenOrderItem.getSpecimen().getLabel() + "\t" + derivedSpecimenOrderItem.getRequestedQuantity().getValue() + "\n";
		 }
		 else if(orderItem instanceof PathologicalCaseOrderItem)
		 {
		 PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem)orderItem;
		 itemInfo = itemInfo + + serialNo+ "." + "\t"+ pathologicalCaseOrderItem.getSpecimenCollectionGroup().getName() + "\t" + pathologicalCaseOrderItem.getRequestedQuantity().getValue() + "\n";
		 }
		 else if(orderItem instanceof NewSpecimenArrayOrderItem)
		 {
		 NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem)orderItem;
		 itemInfo = itemInfo + + serialNo+ "." + "\t"+ newSpecimenArrayOrderItem.getName() + "\t" + newSpecimenArrayOrderItem.getRequestedQuantity().getValue() + "\n";
		 }
		 else if(orderItem instanceof ExistingSpecimenArrayOrderItem)
		 {
		 ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = (ExistingSpecimenArrayOrderItem)orderItem;
		 itemInfo = itemInfo + + serialNo+ "." + "\t"+ existingSpecimenArrayOrderItem.getSpecimenArray().getName() + "\t" + existingSpecimenArrayOrderItem.getRequestedQuantity().getValue() + "\n";
		 }
		 serialNo++ ;
		 }*/

		//String emailBody = emailMsgHeader + orderItemHeader + itemInfo + emailMsgFooter;
		String emailBody = emailMsgHeader + emailMsgFooter;
		return emailBody;
	}

	//	 Populates a List of RequestViewBean objects to display the request list on RequestListAdministratorView.jsp
	/**
	 * @param requestStatusSelected object
	 * @return requestList List
	 * @throws DAOException object
	 */
	public List getRequestList(String requestStatusSelected,String userName,Long userId) throws DAOException
	{

		HibernateDAO dao = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		List orderList = new ArrayList();
		List requestViewBeanList = new ArrayList();
		dao.openSession(null);
		PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(userName);
		
		User user = getUser(dao,userName, userId);
		List siteIdsList = (List)getUserSitesWithDistributionPrev(user,privilegeCache);
		
		try
		{
			List orderListFromDB = null;
			String[] whereColumnName = {"status"};
			String[] whereColumnCondition = {"in"};

			if(!requestStatusSelected.trim().equalsIgnoreCase("All"))
			{
				List whereColValues = new ArrayList();
				whereColValues.add(new String("All"));

				Object [] whereColumnValues = {whereColValues.toArray()};
				orderListFromDB = dao.retrieve(OrderDetails.class.getName(),
						null, whereColumnName,
						whereColumnCondition, whereColumnValues,
						"");

			}else
			{
				List whereColValues = new ArrayList();
				whereColValues.add(new String("Pending"));
				whereColValues.add(new String("New"));

				Object [] whereColumnValues = {whereColValues.toArray()};
				orderListFromDB = dao.retrieve(OrderDetails.class.getName(),
						null, whereColumnName,
						whereColumnCondition, whereColumnValues,
						"");

			}

			Iterator orderListFromDBIterator = orderListFromDB.iterator();
			while(orderListFromDBIterator.hasNext())
			{
				OrderDetails orderDetails = (OrderDetails)orderListFromDBIterator.next();
				boolean hasDributionPrivilegeOnSite = isOrderItemValidTodistribute(orderDetails.getOrderItemCollection(),siteIdsList,dao);
				boolean hasDistributionPrivilegeOnCp = checkDistributionPrivilegeOnCP(user,privilegeCache,orderDetails.getOrderItemCollection());
				if(isSuperAdmin(user) || hasDributionPrivilegeOnSite || hasDistributionPrivilegeOnCp)
				{
					orderList.add(orderDetails);
				}
			}
			requestViewBeanList =(List) populateRequestViewBeanList(orderList);

		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				Logger.out.error(e.getMessage(), e);
			}
		}
		return requestViewBeanList;
	};
	
	/**
	 * Site of the order item should be one in the site list having distribution privilege. 
	 * @param orderItemCollection
	 * @param siteIdsList
	 * @return
	 */
	private boolean isOrderItemValidTodistribute(Collection orderItemCollection,List siteIdsList,HibernateDAO dao)
	{
		boolean isValidToDistribute = false;
		
		Iterator orderItemColItr = orderItemCollection.iterator();
		while(orderItemColItr.hasNext())	
		{	
			OrderItem orderItem = (OrderItem)orderItemColItr.next();
			if(orderItem instanceof ExistingSpecimenOrderItem)
			{
				ExistingSpecimenOrderItem existingSpecimenOrderItem = (ExistingSpecimenOrderItem)orderItem;
				if(existingSpecimenOrderItem.getSpecimen()!=null)
				{
					SpecimenPosition specimenPosition = existingSpecimenOrderItem.getSpecimen().getSpecimenPosition();
					if(specimenPosition != null && !(existingSpecimenOrderItem.getStatus().equals(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)||
							existingSpecimenOrderItem.getStatus().equals(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)))
					{	
						if(siteIdsList.contains(existingSpecimenOrderItem.getSpecimen().getSpecimenPosition().getStorageContainer().getSite().getId()))
						{
							isValidToDistribute = true;
							break;
						}
					}
				}
			
			}else if(orderItem instanceof ExistingSpecimenArrayOrderItem)
			{
				ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = (ExistingSpecimenArrayOrderItem) orderItem;
				if(existingSpecimenArrayOrderItem.getSpecimenArray()!=null && existingSpecimenArrayOrderItem.getSpecimenArray().getLocatedAtPosition()!=null)
				{
					StorageContainer storageContainer = (StorageContainer) existingSpecimenArrayOrderItem.getSpecimenArray().getLocatedAtPosition().getParentContainer();
					if(siteIdsList.contains(storageContainer.getSite().getId()))
					{
						isValidToDistribute = true;
						break;	
					}
				}
			}else if(orderItem instanceof DerivedSpecimenOrderItem)
			{
				DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem) orderItem;
				if(derivedSpecimenOrderItem.getParentSpecimen()!=null)
				{	
					SpecimenPosition specimenPosition = (SpecimenPosition)derivedSpecimenOrderItem.getParentSpecimen().getSpecimenPosition();
					if(specimenPosition != null && !(derivedSpecimenOrderItem.getStatus().equals(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)||
							derivedSpecimenOrderItem.getStatus().equals(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)))
					{
						Long siteId = (Long)specimenPosition.getStorageContainer().getSite().getId();
						if(siteIdsList.contains(siteId))
						{
							isValidToDistribute = true;
							break;
						}
					}
				}
				
				
			}
		}
			
				
		
		return isValidToDistribute;
	}
	
	/**
	 * Retrieve all the sites of the user having distribution privilege.
	 * @param userId
	 * @param privilegeCache
	 * @return
	 * @throws DAOException 
	 */
	public List getRelatedSiteIds(HibernateDAO dao,Long userId,PrivilegeCache privilegeCache) throws DAOException 
	{
		List siteCollWithDistriPri = new ArrayList();
		User user = (User) dao.retrieve(User.class.getName(), userId);
		if (!user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
		{
			Collection<Site> siteCollection = user.getSiteCollection();
			Iterator siteCollectionItr = siteCollection.iterator();
			while(siteCollectionItr.hasNext())
			{
				Site site = (Site)siteCollectionItr.next();
				String objectId = Constants.SITE_CLASS_NAME+"_"+site.getId();

				boolean isAuthorized = privilegeCache.hasPrivilege(objectId, Variables.privilegeDetailsMap.get(Constants.DISTRIBUTE_SPECIMENS));
				if(isAuthorized)
				{
					siteCollWithDistriPri.add(site.getId());
				}

			}
		}
		return (List)siteCollWithDistriPri;
	} 
	
	/**
	 * @param user
	 * @param privilegeCache
	 * @param orderItemCollection
	 * @return
	 * @throws DAOException
	 */
	public boolean checkDistributionPrivilegeOnCP(User user,PrivilegeCache privilegeCache,Collection orderItemCollection) throws DAOException 
	{
	
		boolean hasDistributionPrivilege = false; 
		Iterator orderItemColItr = orderItemCollection.iterator();
		while(orderItemColItr.hasNext())	
		{	
				OrderItem orderItem = (OrderItem)orderItemColItr.next();
				if(orderItem instanceof PathologicalCaseOrderItem)
				{
					PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem)orderItem;
					SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup)pathologicalCaseOrderItem.getSpecimenCollectionGroup();
					if(specimenCollectionGroup != null && !(pathologicalCaseOrderItem.getStatus().equals(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)||
							pathologicalCaseOrderItem.getStatus().equals(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)))
					{
									
						Long cpId = specimenCollectionGroup.getCollectionProtocolRegistration()
						.getCollectionProtocol().getId();
						String objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME+"_"+cpId;
						boolean isAuthorized = privilegeCache.hasPrivilege(objectId, Variables.privilegeDetailsMap.get(Constants.DISTRIBUTE_SPECIMENS));
						if(isAuthorized)
						{
							hasDistributionPrivilege = true;
						}
					}
				}
		}	
					
		
		return hasDistributionPrivilege;
	} 
	
		
	/**
	 * It returns the user object
	 * @param dao
	 * @param userName
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	private User getUser(HibernateDAO dao,String userName,Long  userId) throws DAOException
	{		
		User user = (User) dao.retrieve(User.class.getName(), userId);
		return user;
	}
	
	/**
	 * Get the list of sites of user on which user have distribution privilege
	 * @param request
	 * @return
	 * @throws DAOException 
	 */
	private List getUserSitesWithDistributionPrev(User user,PrivilegeCache privilegeCache)
	{
						
		List siteCollWithDistriPri = new ArrayList();
				
		if (!user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
		{
			
			Collection<Site> siteCollection = user.getSiteCollection();
			Iterator siteCollectionItr = siteCollection.iterator();
			while(siteCollectionItr.hasNext())
			{
				Site site = (Site)siteCollectionItr.next();
				String objectId = Constants.SITE_CLASS_NAME+"_"+site.getId();
				
				boolean isAuthorized = privilegeCache.hasPrivilege(objectId, Variables.privilegeDetailsMap.get(Constants.DISTRIBUTE_SPECIMENS));
				if(isAuthorized)
				{
					siteCollWithDistriPri.add(site.getId());
				}
						
			}
		}
	
	 return (List)siteCollWithDistriPri;
	   
	}
	
	/**
	 * Check for superAdmin
	 * @param user
	 * @return
	 */
	private boolean isSuperAdmin(User user)
	{
		boolean isSuperAdmin = false;
		
		if (!user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
		{
			isSuperAdmin = false;
		}else
		{
			isSuperAdmin = true;
		}
		
		return isSuperAdmin;
				
	}
	
	/**
	 * To populate the request view bean
	 * @param orderListFromDB object 
	 * @return List object
	 */
	private List populateRequestViewBeanList(List orderListFromDB)
	{
		List requestList  = new ArrayList();
		RequestViewBean requestViewBean = null;		
		OrderDetails order = null;
		if(orderListFromDB != null)
		{
			Iterator iter = orderListFromDB.iterator();
			while(iter.hasNext())
			{			
				order = (OrderDetails)iter.next();
				requestViewBean = OrderingSystemUtil.getRequestViewBeanToDisplay(order);
				
				requestViewBean.setRequestId(order.getId().toString());				
				requestViewBean.setStatus(order.getStatus());
				
				requestList.add(requestViewBean);
			}
		}
		return requestList;
	}
	
	
	
	/**
	 * @param id
	 * @param dao
	 * @return to retrieve the orderDetails object. 
	 * @throws NumberFormatException
	 * @throws DAOException
	 */
	public OrderDetails getOrderListFromDB(String id,AbstractDAO dao) throws NumberFormatException, DAOException
	{
		Object object = dao.retrieve(OrderDetails.class.getName(),Long.parseLong(id));
		OrderDetails OrderDetails = (OrderDetails)object;
		return OrderDetails;
	}
	
		
	/**
	 * @param request HttpServletRequest object
	 * @return List specimen array objects
	 */
	public List getSpecimenArrayDataFromDatabase(HttpServletRequest request) throws DAOException
	{
		long startTime = System.currentTimeMillis();
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
    	try
    	{
   		
    		HttpSession session = request.getSession(true);
    		
    		String sourceObjectName = SpecimenArray.class.getName();
        	List valueField=(List)session.getAttribute(Constants.SPECIMEN_ARRAY_ID);
        	
        	List specimenArrayList=new ArrayList();
    		dao.openSession(null);
	    	if(valueField != null && valueField.size() >0)
	    	{
				for(int i=0;i<valueField.size();i++)
				{
					//List SpecimenArray = bizLogic.retrieve(sourceObjectName, columnName, (String)valueField.get(i));
					Object object = dao.retrieve(sourceObjectName, Long.parseLong((String)valueField.get(i)));
					SpecimenArray specArray=(SpecimenArray)object;
					specArray.getSpecimenArrayType();
					specArray.getSpecimenArrayType().getSpecimenTypeCollection();
					specimenArrayList.add(specArray);
				}
	    	}
	    	
	    	long endTime = System.currentTimeMillis();
			Logger.out.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB -  : "+ (endTime - startTime));
	    	return specimenArrayList;	
    	}
    	catch(DAOException e)
    	{
    		Logger.out.error(e.getMessage(), e);
    		return null;
    	}
    	finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				Logger.out.error(daoEx.getMessage(), daoEx);
	    		return null;
			}
		}	
    	
    	  
		
	}
	
	/** function for getting data from database
	 * @param request HttpServletRequest object
	 * @return List of specimen objects
	 * @throws BizLogicException 
	 */
	public List getSpecimenDataFromDatabase(HttpServletRequest request)
	{
		//to get data from database when specimen id is given
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		HttpSession session = request.getSession(true);
		
		long startTime = System.currentTimeMillis();
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
				
		try
    	{
    		
	    	String sourceObjectName = Specimen.class.getName();
	    	String columnName="id";
	    	List valueField=(List)session.getAttribute("specimenId");
	    	List specimenList=new ArrayList();
	    	dao.openSession(null);
	    	if(valueField != null && valueField.size() >0)
	    	{
	    		
				for(int i=0;i<valueField.size();i++)
				{
					Object object = dao.retrieve(sourceObjectName, Long.parseLong((String)valueField.get(i)));
					Specimen spec=(Specimen)object;
					specimenList.add(spec);
				}
				
	    	}
	    	long endTime = System.currentTimeMillis();
			Logger.out.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB -  : "+ (endTime - startTime));
		
			return specimenList;
    	}
    	catch(DAOException e)
    	{
    		Logger.out.error(e.getMessage(), e);
    		return null;
    	}
    	finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				Logger.out.error(daoEx.getMessage(), daoEx);
	    		return null;
			}
		}
    	
	}
	
	/**
	 * @param request HttpServletRequest object
	 * @return List of Pathology Case objects
	 */
	public List getPathologyDataFromDatabase(HttpServletRequest request) throws DAOException
	{
		
		// to get data from database when specimen id is given
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.NEW_PATHOLOGY_FORM_ID);
		List pathologicalCaseList = new ArrayList();
		
		long startTime = System.currentTimeMillis();
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
				
		try
    	{
			dao.openSession(null);
			//retriving the id list from session.
			String [] className = {IdentifiedSurgicalPathologyReport.class.getName(),DeidentifiedSurgicalPathologyReport.class.getName(),SurgicalPathologyReport.class.getName()};
					
			if(request.getSession().getAttribute(Constants.PATHALOGICAL_CASE_ID) != null)
			{
				getList(request, Constants.PATHALOGICAL_CASE_ID , className[0], pathologicalCaseList, bizLogic ,dao);
			}
			if(request.getSession().getAttribute(Constants.DEIDENTIFIED_PATHALOGICAL_CASE_ID) != null)
			{
				getList(request , Constants.DEIDENTIFIED_PATHALOGICAL_CASE_ID ,className[1], pathologicalCaseList, bizLogic ,dao);
			}
			if(request.getSession().getAttribute(Constants.SURGICAL_PATHALOGY_CASE_ID) != null)
			{
				 getList(request, Constants.SURGICAL_PATHALOGY_CASE_ID , className[2], pathologicalCaseList, bizLogic ,dao);
			}
			
			long endTime = System.currentTimeMillis();
			Logger.out.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB -  : "+ (endTime - startTime));
			System.out.println("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB -  : "+ (endTime - startTime));
			return pathologicalCaseList;
    	}
    	catch(DAOException e)
    	{
    		Logger.out.error(e.getMessage(), e);
    		return null;
    	}
    	finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				Logger.out.error(daoEx.getMessage(), daoEx);
				return null;
			}
		}	
		
	}
	
	/**
	 * @param request
	 * @param attr
	 * @param className
	 * @param pathologicalCaseList
	 * @param bizLogic
	 * @param dao
	 * @throws DAOException
	 */
	private void getList(HttpServletRequest request , String attr , String className , List pathologicalCaseList,
			IBizLogic bizLogic,AbstractDAO dao)throws DAOException
	{
		List idList = (List)request.getSession().getAttribute(attr);
		int size = idList.size();
		for(int i=0;i<idList.size();i++)
		{
			Object object = dao.retrieve(className, Long.parseLong((String)idList.get(i)));
			SurgicalPathologyReport surgicalPathologyReport = (SurgicalPathologyReport)object;
			surgicalPathologyReport.getSpecimenCollectionGroup();
			surgicalPathologyReport.getSpecimenCollectionGroup().getSpecimenCollection();
			pathologicalCaseList.add(surgicalPathologyReport);
		}
		
	}
	
	/**
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List getDistributionProtocol(HttpServletRequest request) throws Exception
	{
//		to get the distribution protocol name
		DistributionBizLogic dao = (DistributionBizLogic) BizLogicFactory.getInstance()
        .getBizLogic(Constants.DISTRIBUTION_FORM_ID);
		
    	String sourceObjectName = DistributionProtocol.class.getName();
		String[] displayName = { "title" };
		String valueField = Constants.SYSTEM_IDENTIFIER;
		List protocolList = dao.getList(sourceObjectName, displayName,
		valueField, true);
		
		request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);
		return protocolList;
	}
	
	/**
	 * @param orderNew
	 * @param sessionDataBean
	 * @param dao
	 * @throws UserNotAuthorizedException
	 * @throws DAOException
	 */
	private void disposeSpecimen(OrderDetails orderNew,
			SessionDataBean sessionDataBean, DAO dao) throws UserNotAuthorizedException, DAOException {

		NewSpecimenBizLogic newSpecimenBizLogic=new NewSpecimenBizLogic();
		Collection orderItemCollection = orderNew.getOrderItemCollection();

		Iterator orderItemIterator = orderItemCollection.iterator();

		while (orderItemIterator.hasNext()) {
			OrderItem orderItem = (OrderItem) orderItemIterator.next();
			if ((orderItem).getStatus().equals(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)) {
				Collection distributionCollection = orderNew
				.getDistributionCollection();

				Iterator iterator = distributionCollection.iterator();

				while (iterator.hasNext()) {
					Distribution disrt = (Distribution) iterator.next();

					Collection distributedItemCollection = disrt
					.getDistributedItemCollection();

					Iterator iter = distributedItemCollection.iterator();

					while (iter.hasNext()) {
						DistributedItem distributionItem = (DistributedItem) iter
						.next();
						if(orderItem.getDistributedItem().equals(distributionItem)){

							Specimen specimen = distributionItem.getSpecimen();
							SpecimenArray specimenArray= distributionItem.getSpecimenArray();

							if(specimen!=null){

								try {
									newSpecimenBizLogic.disposeAndCloseSpecimen(sessionDataBean,specimen,dao);
								} catch (BizLogicException e) {

									throw new DAOException(e);
								}
							}
							else if(specimenArray!=null)
							{
								updateSpecimenArray(specimenArray,dao,sessionDataBean);

							}
						}
					}
				}
			}

		}
	}

	/**
	 * @param specimenArray
	 * @param dao
	 * @param sessionDataBean
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	private void updateSpecimenArray(SpecimenArray specimenArray, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException {
		
		SpecimenArrayBizLogic specimenArrayBizLogic=new SpecimenArrayBizLogic();
		SpecimenArray oldSpecimenArray = (SpecimenArray) dao.retrieve(SpecimenArray.class.getName(), specimenArray.getId());
		SpecimenArray newSpecimenArray = oldSpecimenArray;
		newSpecimenArray.setActivityStatus("Closed");
		specimenArrayBizLogic.update(dao, newSpecimenArray, oldSpecimenArray, sessionDataBean);
		
	}
	
	/**
	 * @param specimenId
	 * @return
	 */
	public Specimen getSpecimenObject(Long specimenId)
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		Specimen specimen = null;
    	try
    	{
    		dao.openSession(null);
    		String sourceObjectName = Specimen.class.getName();
    		specimen = (Specimen)dao.retrieve(sourceObjectName,specimenId);
		   		
    	}
    	catch(DAOException e)
    	{
    		Logger.out.error(e.getMessage(), e);
    		
    	}
    	finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				Logger.out.error(daoEx.getMessage(), daoEx);
				
			}
		}
    	return specimen;
	}
	/**
	 * @param specimenId
	 * @param dao
	 * @return
	 */
	public Specimen getSpecimen(Long specimenId,AbstractDAO dao)
	{
		String sourceObjectName = Specimen.class.getName();
		Specimen specimen=null;
		try {
			specimen = (Specimen)dao.retrieve(sourceObjectName,specimenId);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return specimen;
	}
	
	/**
	 * It retrieves the SCG
	 * @param scgId
	 * @return
	 */
	public SpecimenCollectionGroup retrieveSCG(Long scgId)
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		SpecimenCollectionGroup scg = null;
		try {
			dao.openSession(null);
			scg = (SpecimenCollectionGroup)dao.retrieve(SpecimenCollectionGroup.class.getName(),	scgId);
		} catch (DAOException e) {
			
			e.printStackTrace();
		}finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				Logger.out.error(daoEx.getMessage(), daoEx);
				
			}
		}
		
		return scg;
	}
	/**
	 * It retrieves the NewSpecimenArrayOrder Item
	 * @param newSpecimenArrayId
	 * @return
	 */
	public NewSpecimenArrayOrderItem retrieveNewSpecimenArrayOrderItem(Long newSpecimenArrayId)
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = null;
		try {
			dao.openSession(null);
			newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem)dao.retrieve(NewSpecimenArrayOrderItem.class.getName(),	newSpecimenArrayId);
			newSpecimenArrayOrderItem.setSpecimenArray((SpecimenArray)getSpecimenArray(newSpecimenArrayOrderItem.getId(), dao));
		
		
		} catch (DAOException e) {
			
			e.printStackTrace();
		}finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				Logger.out.error(daoEx.getMessage(), daoEx);
				
			}
		}
		
		return newSpecimenArrayOrderItem;
	}
	
	/**
	 * It retrieves the SpecimenOrderItem 
	 * @param specimenOrderItemId
	 * @return
	 */
	public SpecimenOrderItem retrieveSpecimenOrderItem(Long specimenOrderItemId)
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		SpecimenOrderItem specimenOrderItem = null;
		try {
			dao.openSession(null);
			specimenOrderItem = (SpecimenOrderItem)dao.retrieve(SpecimenOrderItem.class.getName(),	specimenOrderItemId);
			NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem)specimenOrderItem.getNewSpecimenArrayOrderItem();
			specimenOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayOrderItem);
			
		} catch (DAOException e) {
			
			e.printStackTrace();
		}finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				Logger.out.error(daoEx.getMessage(), daoEx);
				
			}
		}
		
		return specimenOrderItem;
	}
	
	/**
	 * To retreive the specimenArray
	 * @param orderItemId
	 * @param dao
	 * @return
	 * @throws DAOException
	 */
	private SpecimenArray getSpecimenArray(Long orderItemId,DAO dao) throws DAOException
	{
		SpecimenArray specimenArray = null;
		try {
			if(orderItemId!=null )
				{
					String hql =" select newSpecimenArrayOrderItem.specimenArray " +//,elements(specimenArray.specimenArrayContentCollection)" +
					" from edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem as newSpecimenArrayOrderItem " +
					" where newSpecimenArrayOrderItem.id = "+orderItemId;
						
					List specimenArrayList = dao.executeQuery(hql, null, false, null);
					if(specimenArrayList!=null && !specimenArrayList.isEmpty())
					{
						specimenArray	=(SpecimenArray)specimenArrayList.get(0);
					}
				}
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			}
		
			return specimenArray;
	}
	
	private void checkDistributionPtorocol(OrderDetails order,DAO dao) throws DAOException
	{
		String[] selectColNames = {"distributionProtocol"};
		String[] whereColNames = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColConditions = {"="};
		Object[] whereColVal = {order.getId()};
		List list = dao.retrieve(OrderDetails.class.getName(),selectColNames,whereColNames,whereColConditions,whereColVal,Constants.AND_JOIN_CONDITION);
		if(list != null && !list.isEmpty())
		{
			DistributionProtocol dist = (DistributionProtocol) list.get(0);
			if(dist != null)
			{
				checkStatus(dao,dist,"Distribution Protocol");
			}
		}
		
	}
	
	
	
}