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

import edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.OrderItem;
import edu.wustl.catissuecore.domain.PathologicalCaseOrderItem;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.OrderingSystemUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
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
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		OrderDetails order = (OrderDetails) obj;
		dao.insert(order, sessionDataBean, true, true);
		mailOnSuccessfulSave(sessionDataBean,order,dao);
	}

	 
	/**
	 * This function send mail to the Administrator and Scientist if items ordered by scientist is placed successfully.
	 * @param sessionDataBean object
	 * @param order object
	 * @param dao object
	 * @throws DAOException object
	 */
	private void mailOnSuccessfulSave(SessionDataBean sessionDataBean,OrderDetails order,DAO dao) throws DAOException
	{
		boolean emailSent = false;
		List userList = dao.retrieve(User.class.getName(), "emailAddress", sessionDataBean.getUserName());
		if(userList != null && !userList.isEmpty())
		{
			User userObj = (User)userList.get(0);
			EmailHandler emailHandler = new EmailHandler();
			
			String emailBody = makeEmailBodyForOrderPlacement(userObj,order,dao);
			String subject = "The Order " + order.getName() +" has been successfully placed.";
			emailSent = emailHandler.sendEmailForOrderingPlacement(userObj.getEmailAddress(),emailBody,subject);
			
			if(emailSent)
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
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		this.validate(obj, oldObj, dao, Constants.EDIT);
		OrderDetails orderOld = updateOldObject(oldObj, obj, dao, sessionDataBean);
				
		dao.update(orderOld, sessionDataBean, true, true, false);
		//Sending Email only if atleast one order item is updated.
		if(numberItemsUpdated > 0)
		{
			sendEmailOnOrderUpdate(orderOld,sessionDataBean);
		}
	}

	//	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	//	{
	//		OrderDetails order = (OrderDetails) obj;
	//		if(!order.getStatus().trim().equalsIgnoreCase("New"))
	//		{			
	//			throw new DAOException("Initial OrderDetails Status has to be 'New'.");
	//		}
	//		
	//		return true;
	//	}
	/**
	 * @param obj object
	 * @param oldObj object
	 * @param dao object
	 * @param operation object
	 * @return boolean object
	 * @throws DAOException object
	 */
	protected boolean validate(Object obj, Object oldObj, DAO dao, String operation)
			throws DAOException
	{
		OrderDetails order = (OrderDetails) obj;
		OrderDetails oldOrder = (OrderDetails) oldObj;
		Validator validator = new Validator();
		if(
			(oldOrder.getStatus().trim().equalsIgnoreCase("Pending") && order.getStatus().trim().equalsIgnoreCase("New"))
			|| (oldOrder.getStatus().trim().equalsIgnoreCase("Rejected") && (order.getStatus().trim().equalsIgnoreCase("New") || order.getStatus().trim().equalsIgnoreCase("Pending")))
			|| (oldOrder.getStatus().trim().equalsIgnoreCase("Completed") && (order.getStatus().trim().equalsIgnoreCase("New") || order.getStatus().trim().equalsIgnoreCase("Pending") || order.getStatus().trim().equalsIgnoreCase("Rejected"))) 
		  )
		{
			throw new DAOException(ApplicationProperties.getValue("orderdistribution.status.errmsg"));
		}
		//Site is mandatory on UI.
		Collection distributionCollection = order.getDistributionCollection();
		if(distributionCollection != null)
		{
			Iterator distributionCollectionIterator = distributionCollection.iterator();
			while(distributionCollectionIterator.hasNext())
			{
				Distribution distribution = (Distribution)distributionCollectionIterator.next();
				if(distribution.getToSite().getId().compareTo(new Long(-1)) == 0)
				{
					throw new DAOException(ApplicationProperties.getValue("orderdistribution.site.required.errmsg"));
				}
			}
		}
		//Quantity is mandatory
		Collection orderItemCollection = order.getOrderItemCollection();
		Iterator iter = orderItemCollection.iterator();
		while(iter.hasNext())
		{
			OrderItem orderItem = (OrderItem)iter.next();
			if(orderItem.getDistributedItem() != null)
			{
				if(orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED))
				{
					if(orderItem.getDistributedItem().getQuantity() == null || orderItem.getDistributedItem().getQuantity().toString().equalsIgnoreCase(""))
					{
						throw new DAOException(ApplicationProperties.getValue("orderdistribution.quantity.required.errmsg"));
					}
	//				Quantity cannot be characters and Quantity cannot be negative number.
					if(!validator.isDouble(orderItem.getDistributedItem().getQuantity().toString()))
					{
						throw new DAOException(ApplicationProperties.getValue("orderdistribution.quantity.format.errmsg"));
					}
				}
			}	
			Collection oldOrderItemColl = oldOrder.getOrderItemCollection();
			Iterator oldOrderItemCollIter = oldOrderItemColl.iterator();
			while(oldOrderItemCollIter.hasNext())
			{
				OrderItem oldorderItem = (OrderItem)oldOrderItemCollIter.next();
				if(oldorderItem.getId().compareTo(orderItem.getId()) == 0)
				{
					//If requestFor drop down is null, do not allow distribution of that order item
					if(orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED) || orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
					{
						if(oldorderItem instanceof DerivedSpecimenOrderItem)
						{
							DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem)oldorderItem;
							if(orderItem.getDistributedItem().getSpecimen().getId() == null)
							{
								throw new DAOException(ApplicationProperties.getValue("orderdistribution.distribution.notpossible.errmsg"));
							}
//							List childrenSpecimenCollection = OrderingSystemUtil.getAllChildrenSpecimen(orderItem.getDistributedItem().getSpecimen(),orderItem.getDistributedItem().getSpecimen().getChildrenSpecimen());
//							List finalChildrenSpecimenCollection = null;
//							if(childrenSpecimenCollection != null)
//							{
//								finalChildrenSpecimenCollection = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childrenSpecimenCollection,derivedSpecimenOrderItem.getSpecimenClass(),derivedSpecimenOrderItem.getSpecimenType());
//							}
//							if(finalChildrenSpecimenCollection == null || finalChildrenSpecimenCollection.isEmpty())
//							{
//								if(orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
//								{
//									throw new DAOException(ApplicationProperties.getValue("orderdistribution.arrayPrep.notpossible.errmsg"));
//								}
//								else
//								{
//									throw new DAOException(ApplicationProperties.getValue("orderdistribution.distribution.notpossible.errmsg"));
//								}
//							}
						}
						else if(oldorderItem instanceof NewSpecimenArrayOrderItem)
						{
							NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem)oldorderItem;
							if(newSpecimenArrayOrderItem.getSpecimenArray() == null)
							{
								throw new DAOException(ApplicationProperties.getValue("orderdistribution.distribution.arrayNotCreated.errmsg"));
							}
						}
						else if(oldorderItem instanceof PathologicalCaseOrderItem)
						{
							PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem)oldorderItem;
							if(pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSpecimenCollection() == null || pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSpecimenCollection().size() == 0)
							{
								if(orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
								{
									throw new DAOException(ApplicationProperties.getValue("orderdistribution.arrayPrep.notpossibleForPatho.errmsg"));
								}
								else
								{
									throw new DAOException(ApplicationProperties.getValue("orderdistribution.distribution.notpossibleForPatho.errmsg"));
								}
							}
							if(pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSpecimenCollection() != null)
							{
								Collection specimenColl = pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSpecimenCollection();
								Iterator specimenCollIter = specimenColl.iterator();
								List totalChildrenSpecimenColl = new ArrayList();
								while(specimenCollIter.hasNext())
								{
									Specimen specimen = (Specimen)specimenCollIter.next();
						    		List childSpecimenCollection = OrderingSystemUtil.getAllChildrenSpecimen(specimen,specimen.getChildrenSpecimen());
						    		List finalChildrenSpecimenCollection = null;
						    		if(pathologicalCaseOrderItem.getSpecimenClass() != null && pathologicalCaseOrderItem.getSpecimenType() != null && !pathologicalCaseOrderItem.getSpecimenClass().trim().equalsIgnoreCase("") && !pathologicalCaseOrderItem.getSpecimenType().trim().equalsIgnoreCase(""))
						    	    {	//"Derived"	   
						    			finalChildrenSpecimenCollection = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childSpecimenCollection,pathologicalCaseOrderItem.getSpecimenClass(),pathologicalCaseOrderItem.getSpecimenType());						    			
						    	    }	    	
								    else
								    {  	//"Block" . Specimen class = "Tissue" , Specimen Type = "Block".
								    	finalChildrenSpecimenCollection = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childSpecimenCollection,"Tissue","Block");
								    }
//						    		adding specimen to the collection
						    		if(specimen.getClassName().equalsIgnoreCase(pathologicalCaseOrderItem.getSpecimenClass()) && specimen.getType().equalsIgnoreCase(pathologicalCaseOrderItem.getSpecimenType()))
									{
						    			finalChildrenSpecimenCollection.add(specimen);
									}
						    		if(finalChildrenSpecimenCollection!=null)
						    		{
						    			Iterator finalChildrenSpecimenCollectionIterator = finalChildrenSpecimenCollection.iterator();
						    			while(finalChildrenSpecimenCollectionIterator.hasNext())
						    			{	    		
						    				totalChildrenSpecimenColl.add((Specimen)(finalChildrenSpecimenCollectionIterator.next()));
						    			}	    			
						    		}
								}
						    	if(totalChildrenSpecimenColl == null)
						    	{
						    		if(orderItem.getStatus().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
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
	private OrderDetails updateOldObject(Object oldObj, Object obj , DAO dao, SessionDataBean sessionDataBean) throws DAOException,UserNotAuthorizedException
	{
		OrderDetails order = (OrderDetails) obj;
		OrderDetails orderOld = (OrderDetails) oldObj;
		//Getting OrderItems Collection.
		Collection oldOrderItemSet = orderOld.getOrderItemCollection();
		Collection newOrderItemSet = order.getOrderItemCollection();
//		Adding items inside New Defined Array in oldOrderItemColl
		Collection tempOldOrderItemSet = new HashSet();
		Iterator tempOldItemIter = oldOrderItemSet.iterator();
		while(tempOldItemIter.hasNext())
		{
			OrderItem tempOldOrderItem = (OrderItem) tempOldItemIter.next();
			
			if(tempOldOrderItem instanceof NewSpecimenArrayOrderItem)
			{				
				NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem)tempOldOrderItem;
				Collection specimenOrderItemColl = newSpecimenArrayOrderItem.getSpecimenOrderItemCollection();
				if(specimenOrderItemColl != null)
				{
					Iterator tempIter = specimenOrderItemColl.iterator();
					while(tempIter.hasNext())
					{
						tempOldOrderItemSet.add((OrderItem)tempIter.next());
					}
				}
			}
		}
		if(tempOldOrderItemSet.size() > 0)
		{
			oldOrderItemSet.addAll(tempOldOrderItemSet);
		}
		//Iterating over OrderItems collection.
		Iterator oldSetIter = oldOrderItemSet.iterator();			
		numberItemsUpdated = 0;
		//To insert distribution and distributed items only once.
		boolean isDistributionInserted = false;
		
		while (oldSetIter.hasNext())
		{
			OrderItem oldOrderItem = (OrderItem) oldSetIter.next();
			Iterator newSetIter = newOrderItemSet.iterator();
			
			while (newSetIter.hasNext())
			{
				OrderItem newOrderItem = (OrderItem) newSetIter.next();
				//Update Old OrderItem only when its Id matches with NewOrderItem id and the order is not distributed and the oldorderitem status and neworderitem status are different or description has been updated.
				if ((oldOrderItem.getId().compareTo(newOrderItem.getId()) == 0) && (oldOrderItem.getDistributedItem() == null) && (!oldOrderItem.getStatus().trim().equalsIgnoreCase(newOrderItem.getStatus().trim()) || (oldOrderItem.getDescription() != null && !oldOrderItem.getDescription().equalsIgnoreCase(newOrderItem.getDescription()))))
				{					
						if (newOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED))
						{
							if(!isDistributionInserted)
							{
								//Direct call to DistributionBizLogic
								Collection newDistributionColl = order.getDistributionCollection();
								Iterator iter = newDistributionColl.iterator();
								DistributionBizLogic distributionBizLogic = (DistributionBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.DISTRIBUTION_FORM_ID);
								while(iter.hasNext())
								{
									Distribution distribution = (Distribution)iter.next();
									//Populating Distribution Object.
									distribution.setOrderId(orderOld);
									//Setting the user for distribution.
									User user = new User();
									user.setId(sessionDataBean.getUserId());
									distribution.setUser(user);	
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
							if(newOrderItem.getDistributedItem() != null)
							{
								oldOrderItem.setDistributedItem(newOrderItem.getDistributedItem());
							}
						}
						//Setting Description and Status.
						if(newOrderItem.getDescription() != null)
						{
							if(oldOrderItem instanceof ExistingSpecimenArrayOrderItem)
							{
								oldOrderItem.setDescription(oldOrderItem.getDescription()+" "+newOrderItem.getDescription());
							}
							else
							{
								oldOrderItem.setDescription(newOrderItem.getDescription());
							}
						}
						oldOrderItem.setStatus(newOrderItem.getStatus());												
						//The number of Order Items updated.
						numberItemsUpdated ++;
				}
			}	
			calculateOrderStatus(oldOrderItem);
		}
		//Updating comments.
		if (order.getComment() != null && !order.getComment().trim().equalsIgnoreCase(""))
		{
			orderOld.setComment(orderOld.getComment()+" "+order.getComment());
		}
		//For order status.
		orderOld = updateOrderStatus(orderOld,oldOrderItemSet);	
		return orderOld;
	}
	/**
	 * @param order object
	 * @param sessionDataBean object
	 */
	private void sendEmailOnOrderUpdate(OrderDetails order,SessionDataBean sessionDataBean)
	{
		EmailHandler emailHandler = new EmailHandler();
		String toEmailAddress = order.getDistributionProtocol().getPrincipalInvestigator().getEmailAddress();
		String fromEmailAddress = sessionDataBean.getUserName();
		String subject = "Update on Order " + order.getName();
		String body = makeEmailBodyForOrderUpdate(order);
		emailHandler.sendEmailForOrderDistribution(body, toEmailAddress, fromEmailAddress, subject);
	}
	/**
	 * @param order object
	 * @return String object
	 */
	private String makeEmailBodyForOrderUpdate(OrderDetails order)
	{
		Collection orderItemColl = order.getOrderItemCollection();
		String emailFormat = "";
		String emailBodyHeader = "Hello "+order.getDistributionProtocol().getPrincipalInvestigator().getLastName()+", "+order.getDistributionProtocol().getPrincipalInvestigator().getFirstName()
									+"\n"+"Following is the update on the order placed by you."+"\n"
									+"#"+"\t"+"OrderItemName"+"\t"+"Previous Status"+"\t"+"\t"+"Current Status"+"\t"+"\t"+"Description"+"\n";

		Iterator iter = orderItemColl.iterator();
		int serialNo = 1;
		String emailBody = "";
		while(iter.hasNext())
		{
			OrderItem orderItem = (OrderItem)iter.next();
			if(orderItem instanceof ExistingSpecimenOrderItem)
			{
				ExistingSpecimenOrderItem existingSpecimenOrderItem =(ExistingSpecimenOrderItem)orderItem;
				emailBody = emailBody+serialNo+". "+existingSpecimenOrderItem.getSpecimen().getLabel()
							+"--"+existingSpecimenOrderItem.getStatus()+"\n";
			}
			else if(orderItem instanceof DerivedSpecimenOrderItem)
			{
				DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem)orderItem;
				emailBody = emailBody+serialNo+". "+derivedSpecimenOrderItem.getParentSpecimen().getLabel()
							+"--"+derivedSpecimenOrderItem.getSpecimenClass()+"--"+derivedSpecimenOrderItem.getSpecimenType()
							+"--"+derivedSpecimenOrderItem.getStatus()+"\n";
			}
			else if(orderItem instanceof PathologicalCaseOrderItem)
			{
				PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem)orderItem;
				emailBody = emailBody+serialNo+". "+pathologicalCaseOrderItem.getSpecimenCollectionGroup().getName()
							+"--"+pathologicalCaseOrderItem.getStatus()+"\n";
			}
			else if(orderItem instanceof NewSpecimenArrayOrderItem)
			{
				NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem)orderItem;
				emailBody = emailBody+serialNo+". "+newSpecimenArrayOrderItem.getName()
				+"--"+newSpecimenArrayOrderItem.getStatus()+"\n";
			}
			else if(orderItem instanceof ExistingSpecimenArrayOrderItem)
			{
				ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = (ExistingSpecimenArrayOrderItem)orderItem;
				emailBody = emailBody+serialNo+". "+existingSpecimenArrayOrderItem.getSpecimenArray().getName()
				+"--"+existingSpecimenArrayOrderItem.getStatus()+"\n";
			}
			serialNo++;
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
		if(oldOrderItem.getOrder() != null)
		{
//			For order status
			if (oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_NEW))
			{
				orderStatusNew++;
			}
			else if (oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED))
			{
				orderStatusCompleted++;
			}
			else if (oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION) || oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW) || oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION))
			{
				orderStatusPending++;
			}
			else if (oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST) || oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE) || oldOrderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE))
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
	private OrderDetails updateOrderStatus(OrderDetails orderOld,Collection oldOrderItemSet)
	{
		if (orderStatusNew == oldOrderItemSet.size())
		{
			orderOld.setStatus(Constants.ORDER_STATUS_NEW);
		}
		else if (orderStatusRejected == oldOrderItemSet.size())
		{
			orderOld.setStatus(Constants.ORDER_STATUS_REJECTED);
		}
		else if ((orderStatusCompleted == oldOrderItemSet.size()) || ((orderStatusCompleted + orderStatusRejected) == oldOrderItemSet.size()))
		{
			orderOld.setStatus(Constants.ORDER_STATUS_COMPLETED);
		}
		else
		{
			orderOld.setStatus(Constants.ORDER_STATUS_PENDING);
		}
		
		return orderOld;
	}
	
	/**
	 * This function prepares email body for the Order Placement Mail
	 * @param userObj  User object containing the logged in user info
	 * @param order OrderDetails instance containing details of the requested order and order items under that order
	 * @param dao object
	 * @return emailBody String containing the email message body
	 * @throws DAOException object
	 */
	private String makeEmailBodyForOrderPlacement(User userObj,OrderDetails order,DAO dao) throws DAOException
	{
		String emailBodyHeader = "Dear caTissue Administrator ,";
		String colName="id";
		List distributionProtocolList = dao.retrieve(DistributionProtocol.class.getName(),colName,order.getDistributionProtocol().getId());
		String messageLine1 = "This is to notify that the Order " + order.getName() + " requested by " + userObj.getFirstName() + " " + userObj.getLastName() + " under Distribution Protocol " + ((DistributionProtocol)distributionProtocolList.get(0)).getTitle() + " have been placed successfully.";
		
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
}