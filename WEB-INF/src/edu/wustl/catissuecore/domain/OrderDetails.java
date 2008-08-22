/**
 * <p>Title: Order Class>
 * <p>Description:  Parent Class for Ordering System.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on October 10,2006
 */
package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.actionForm.DefineArrayForm;
import edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm;
import edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm;
import edu.wustl.catissuecore.actionForm.OrderSpecimenForm;
import edu.wustl.catissuecore.actionForm.RequestDetailsForm;
import edu.wustl.catissuecore.bean.DefinedArrayDetailsBean;
import edu.wustl.catissuecore.bean.DefinedArrayRequestBean;
import edu.wustl.catissuecore.bean.ExistingArrayDetailsBean;
import edu.wustl.catissuecore.bean.OrderSpecimenBean;
import edu.wustl.catissuecore.bean.RequestDetailsBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;


/**
 * Order corresponds to the orders requested by the user(scientist/researcher).
 * @hibernate.class table="CATISSUE_ORDER"
 * @author ashish_gupta
 */
public class OrderDetails extends AbstractDomainObject implements Serializable
{
	//To show custom message for add and edit.
	private boolean operationAdd;
	
	
	private static final long serialVersionUID = -2292977224238830710L;
	/**
     * System generated unique id.
     */
    protected Long id;
    
    /**
     * String containing name of the order.
     */
    protected String name;
    
    /**
     * String containing the comments entered by user.
     */
    protected String comment;
    
    /**
     * Requested Date when the order was placed.
     */
    protected Date requestedDate;
    
    /**
     * String containing the status of order.
     */
    protected String status;
    
	/**
     * Distribution Protocol object associated with that order.
     */
	protected DistributionProtocol distributionProtocol;
	
	/**
     * The Order Items associated with that order.
     */
	protected Collection orderItemCollection;
	/**
	 * The distributions associated with the order.
	 */
	protected Collection distributionCollection;
	
	protected Boolean mailNotification= new Boolean(false);
	/**
	 * Default constructor.
	 */
	public OrderDetails()
	{
		// Default Constructor, required for Hibernate
	}
	
	/**
	 * @param form AbstractActionForm
	 * @throws AssignDataException object.
	 */
	public OrderDetails(AbstractActionForm form)throws AssignDataException
	{
		setAllValues(form);
	}

	/**
     * Returns the system generated unique id.
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_ORDER_SEQ"
     * @return the system generated unique id.
     * @see #setId(Long)
     * */
	public Long getId()
	{		
		return id;
	}
	/**
     * Sets the system generated unique id.
     * @param id the system generated unique id.
     * @see #getId()
     * */
    public void setId(Long id)
    {
        this.id = id;
    }
	 /**
     * Returns the comments about the order.
     * @hibernate.property name="comments" type="string" 
     * column="COMMENTS" length="1000"
     * @return the comments about the order.
     * @see #setComments(String)
     */
	public String getComment()
	{
		return comment;
	}

	
	/**
	 * Sets the comments for the request order.
	 * @param comment the comment to set
	 * @see #getComment()
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	
	/**
	 * @return Returns the mailNotification.
	 */
	public Boolean getMailNotification()
	{
		return mailNotification;
	}

	/**
	 * @param mailNotification The mailNotification to set.
	 */
	public void setMailNotification(Boolean mailNotification)
	{
		this.mailNotification = mailNotification;
	}

	/**
	 * The distribution protocol associated with the order.
	 * @hibernate.many-to-one column="DISTRIBUTION_PROTOCOL_ID" class="edu.wustl.catissuecore.domain.DistributionProtocol"
	 * constrained="true"
	 * @return the distributionProtocol
	 */
	public DistributionProtocol getDistributionProtocol()
	{
		return distributionProtocol;
	}

	
	/**
	 * @param distributionProtocol the distributionProtocol to set
	 * @see #getDistributionProtocol()
	 */
	public void setDistributionProtocol(DistributionProtocol distributionProtocol)
	{
		this.distributionProtocol = distributionProtocol;
	}

	
	/**
	 * @return the name
	 * @hibernate.property column="NAME" name="comments" type="string" length="500"
	 * @see #setName()
	 */
	public String getName()
	{
		return name;
	}

	
	/**
	 * @param name the name to set
	 * @see #getName()
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * @return the orderItemCollection
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.OrderItem" cascade="save-update" lazy="false"
	 * @hibernate.set name="orderItemCollection" table="CATISSUE_ORDER_ITEM"
	 * @hibernate.collection-key column="ORDER_ID" 
	 */
	public Collection getOrderItemCollection()
	{
		return orderItemCollection;
	}

	
	/**
	 * @param orderItemCollection the orderItemCollection to set
	 * @see #getOrderItemCollection()
	 */
	public void setOrderItemCollection(Collection orderItemCollection)
	{
		this.orderItemCollection = orderItemCollection;
	}

	
	/**
	 * @return the requestedDate
	 * @hibernate.property name="requestedDate"  column="REQUESTED_DATE" 
	 */
	public Date getRequestedDate()
	{
		return requestedDate;
	}

	
	/**
	 * @param requestedDate the requestedDate to set
	 * @see #getRequestedDate()
	 */
	public void setRequestedDate(Date requestedDate)
	{
		this.requestedDate = requestedDate;
	}

	
	/**
	 * @return the status
	 * @hibernate.property name="status" type="string" column="STATUS" length="50"
	 */
	public String getStatus()
	{
		return status;
	}

	
	/**
	 * @param status the status to set
	 * @see #getStatus()
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * @param abstractActionForm object.
	 * @throws AssignDataException object.
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		final AbstractActionForm abstractActionForm = (AbstractActionForm) abstractForm;
		if(abstractActionForm.isAddOperation()) //insert
		{
			operationAdd = true;
			insertOrderDetails(abstractActionForm);
		}
		else //update 
		{
			updateOrderDetails(abstractActionForm);
		}
			
	}

	/**
	 * This function inserts order data to order table  
	 * @param abstractActionForm object
	 */
	private void insertOrderDetails(AbstractActionForm abstractActionForm)
	{
		HashMap orderItemsMap=null;
		Collection orderItemsCollection = new HashSet();
		List newSpecimenArrayObjList=null;

		if(abstractActionForm.getPageOf().equals(Constants.SPECIMEN_ORDER_FORM_TYPE))
		{
			final OrderSpecimenForm orderSpecimenForm = (OrderSpecimenForm) abstractActionForm;
			orderItemsMap=(HashMap)putOrderDetailsForSpecimen(orderSpecimenForm);
			newSpecimenArrayObjList=(List)putnewArrayDetailsforArray(orderSpecimenForm.getDefineArrayObj());
		}
		
		if(abstractActionForm.getPageOf().equals(Constants.ARRAY_ORDER_FORM_TYPE))
		{
			final OrderBiospecimenArrayForm orderBiospecimenArrayForm = (OrderBiospecimenArrayForm) abstractActionForm;
			orderItemsMap=(HashMap)putOrderDetailsForArray(orderBiospecimenArrayForm);
			newSpecimenArrayObjList=(List)putnewArrayDetailsforArray(orderBiospecimenArrayForm.getDefineArrayObj());
			
		}
		if(abstractActionForm.getPageOf().equals(Constants.PATHOLOGYCASE_ORDER_FORM_TYPE))
		{
			final OrderPathologyCaseForm orderPathologyCaseForm = (OrderPathologyCaseForm) abstractActionForm;
			orderItemsMap=(HashMap)putOrderDetailsForPathologyCase(orderPathologyCaseForm);
			newSpecimenArrayObjList=(List)putnewArrayDetailsforArray(orderPathologyCaseForm.getDefineArrayObj());
		}
		//Obtain orderItemCollection .
		final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.bean");
		try
		{
			orderItemsCollection = (HashSet)parser.generateData(orderItemsMap);
		}
		catch (Exception e)
		{
			Logger.out.debug("" + e);
		}

		Collection orderItemsSet = new HashSet();
		
		Iterator orderItemsCollectionItr = orderItemsCollection.iterator();
		OrderItem orderItem =null;
		SpecimenOrderItem  specimenOrderItem=null;
		
		while(orderItemsCollectionItr.hasNext())
		{
			OrderSpecimenBean orderSpecimenBean = (OrderSpecimenBean) orderItemsCollectionItr.next();
			Double reqQty = new Double(0);
			if(orderSpecimenBean.getTypeOfItem().equals("specimen"))
			{
				specimenOrderItem = (SpecimenOrderItem)setBioSpecimen(orderSpecimenBean);
				if(orderSpecimenBean.getArrayName().equals("None"))
				{
					orderItem=specimenOrderItem;
					orderItemsSet.add(orderItem);
				}
				else
				{
					if(newSpecimenArrayObjList!=null)
					{
						Iterator it = newSpecimenArrayObjList.iterator();
						while(it.hasNext())
						{
							NewSpecimenArrayOrderItem newSpecimenArrayObj=(NewSpecimenArrayOrderItem)it.next();
							if(newSpecimenArrayObj.getName().equals(orderSpecimenBean.getArrayName()))
							{
								Collection orderItemCollection = (Set)newSpecimenArrayObj.getSpecimenOrderItemCollection();
								
								if(orderItemCollection == null)
								{
									orderItemCollection = new HashSet();
								}
								specimenOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayObj);								
								orderItem = specimenOrderItem;
								//Test Line
								orderItemsSet.add(orderItem);
								orderItemCollection.add(orderItem);
								newSpecimenArrayObj.setSpecimenOrderItemCollection(orderItemCollection);
							}
						}
					}
				}
			}
			
			if(orderSpecimenBean.getTypeOfItem().equals("pathologyCase"))
			{
				specimenOrderItem = (SpecimenOrderItem)setPathologyCase(orderSpecimenBean);
				if(orderSpecimenBean.getArrayName().equals("None"))
				{
					orderItem=specimenOrderItem;
					orderItemsSet.add(orderItem);
				}
				else
				{
					if(newSpecimenArrayObjList!=null)
					{
						Iterator it = newSpecimenArrayObjList.iterator();
						while(it.hasNext())
						{
							NewSpecimenArrayOrderItem newSpecimenArrayObj=(NewSpecimenArrayOrderItem)it.next();
							if(newSpecimenArrayObj.getName().equals(orderSpecimenBean.getArrayName()))
							{
								Collection orderItemCollection=(Set)newSpecimenArrayObj.getSpecimenOrderItemCollection();
								if(orderItemCollection==null)
								{
									orderItemCollection=new HashSet();
								}
								specimenOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayObj);
								orderItem=specimenOrderItem;
//								Test Line
								orderItemsSet.add(orderItem);
								orderItemCollection.add(orderItem);
								newSpecimenArrayObj.setSpecimenOrderItemCollection(orderItemCollection);
							}
						}
					}
				}
			}
			if(orderSpecimenBean.getTypeOfItem().equals("specimenArray"))
			{
				orderItem=getOrderArrayItem(orderSpecimenBean,orderItem);
				if((orderSpecimenBean.getTypeOfItem().equals("specimenArray"))&&(orderSpecimenBean.getSpecimenClass().equals("Tissue"))&& (orderSpecimenBean.getSpecimenType().equals("unblock")))
				{
					reqQty = new Double(1);
				}
				else
				{
					reqQty = new Double(orderSpecimenBean.getRequestedQuantity());
				}
				orderItem.setRequestedQuantity(reqQty);
				orderItem.setDescription(orderSpecimenBean.getDescription());
				orderItem.setStatus("New");
				orderItemsSet.add(orderItem);

			}
		}//End While
		
		if(newSpecimenArrayObjList!=null)
		{
			Iterator it = newSpecimenArrayObjList.iterator();
			while(it.hasNext())
			{
				orderItem=(NewSpecimenArrayOrderItem)it.next();
				orderItem.setStatus("New");
				orderItemsSet.add(orderItem);
			}	
		}	
		this.setOrderItemCollection(orderItemsSet);
	}
	
	/**
	 * Thsi function sets the Bio Specimen Order Item
	 * @param orderSpecimenBean object
	 * @return specimenOrderItem SpecimenOrderItem instance
	 */
	private SpecimenOrderItem setBioSpecimen(OrderSpecimenBean orderSpecimenBean)
	{
		SpecimenOrderItem  specimenOrderItem = null;
		Double reqQty = new Double(0);
		
		if (orderSpecimenBean.getIsDerived().equals("false")) // Existing specimen.
		{
			specimenOrderItem = setExistingSpecimenOrderItem(orderSpecimenBean);
			reqQty = new Double(orderSpecimenBean.getRequestedQuantity());
			specimenOrderItem.setRequestedQuantity(reqQty);
			specimenOrderItem.setDescription(orderSpecimenBean.getDescription());
			specimenOrderItem.setStatus("New");
		}
		else //Derived specimen.
		{
			specimenOrderItem = setDerivedSpecimenOrderItem(orderSpecimenBean);
			reqQty = new Double(orderSpecimenBean.getRequestedQuantity());
			specimenOrderItem.setRequestedQuantity(reqQty);
			specimenOrderItem.setDescription(orderSpecimenBean.getDescription());
			specimenOrderItem.setStatus("New");
		}
		return specimenOrderItem;
	}
	
	/**
	 * This function sets the pathology case order item
	 * @param orderSpecimenBean object 
	 * @return specimenOrderItem SpecimenOrderItem instance
	 */
	private SpecimenOrderItem setPathologyCase(OrderSpecimenBean orderSpecimenBean)
	{
		SpecimenOrderItem  specimenOrderItem=null;
		Double reqQty = new Double(0);
		
		if (orderSpecimenBean.getIsDerived().equals("false")) // Existing specimen.
		{
			specimenOrderItem = setExistingOrderItemForPathology(orderSpecimenBean);
			reqQty = new Double(orderSpecimenBean.getRequestedQuantity());
			specimenOrderItem.setRequestedQuantity(reqQty);
			specimenOrderItem.setDescription(orderSpecimenBean.getDescription());
			specimenOrderItem.setStatus("New");
		}
		else 
		{
			specimenOrderItem = setDerivedOrderItemForPathology(orderSpecimenBean);
			reqQty = new Double(orderSpecimenBean.getRequestedQuantity());
			specimenOrderItem.setRequestedQuantity(reqQty);
			specimenOrderItem.setDescription(orderSpecimenBean.getDescription());
			specimenOrderItem.setStatus("New");
		}
		return specimenOrderItem;
	}
	
	/**
	 * This funciton sets the existingSpecimen orderitem with the values from OrderSpecimenBean
	 * @param orderSpecimenBean object
	 * @return ExistingSpecimenOrderItem object
	 */
	private ExistingSpecimenOrderItem setExistingSpecimenOrderItem(OrderSpecimenBean orderSpecimenBean)
	{
		ExistingSpecimenOrderItem existingOrderItem = new ExistingSpecimenOrderItem();
		//Set Parent specimen 
		Specimen specimen=new Specimen();
		specimen.setId(new Long(orderSpecimenBean.getSpecimenId()));
		existingOrderItem.setSpecimen(specimen);
		
		return existingOrderItem;
	}
	
	/**
	 * This funciton sets the derivedspecimen orderitem with the values from OrderSpecimenBean
	 * @param orderSpecimenBean object
	 * @return derivedSpecimenOrderItem DerivedSpecimenOrderItem object
	 */
	private DerivedSpecimenOrderItem setDerivedSpecimenOrderItem(OrderSpecimenBean orderSpecimenBean)
	{
		DerivedSpecimenOrderItem derivedSpecimenOrderItem = new DerivedSpecimenOrderItem();
		//Set Parent specimen 
		Specimen specimen=new Specimen();
		specimen.setId(new Long(orderSpecimenBean.getSpecimenId()));
		derivedSpecimenOrderItem.setParentSpecimen(specimen);
		derivedSpecimenOrderItem.setSpecimenClass(orderSpecimenBean.getSpecimenClass());
		derivedSpecimenOrderItem.setSpecimenType(orderSpecimenBean.getSpecimenType());
		
		return derivedSpecimenOrderItem;
	}
	
	/**
	 * This function sets the Existing order Item for Pathology Case
	 * @param orderSpecimenBean OrderSpecimenBean Object
	 * @return existingOrderItem PathologicalCaseOrderItem Object
	 */
	private PathologicalCaseOrderItem setExistingOrderItemForPathology(OrderSpecimenBean orderSpecimenBean)
	{
		PathologicalCaseOrderItem existingOrderItem = new PathologicalCaseOrderItem();
		existingOrderItem.setTissueSite(orderSpecimenBean.getTissueSite());
		existingOrderItem.setPathologicalStatus(orderSpecimenBean.getPathologicalStatus());
		existingOrderItem.setSpecimenClass(orderSpecimenBean.getSpecimenClass());
		existingOrderItem.setSpecimenType(orderSpecimenBean.getSpecimenType());
		SpecimenCollectionGroup specimenCollGroup=new SpecimenCollectionGroup();
		specimenCollGroup.setId(new Long(orderSpecimenBean.getSpecimenCollectionGroup()));
		existingOrderItem.setSpecimenCollectionGroup(specimenCollGroup);
		
		return existingOrderItem;
	}
	
	/**
	 * This funciton sets the Derived Order item for Pathology Case
	 * @param orderSpecimenBean OrderSpecimenBean Object
	 * @return derivedOrderItem PathologicalCaseOrderItem Object
	 */
	private PathologicalCaseOrderItem setDerivedOrderItemForPathology(OrderSpecimenBean orderSpecimenBean)
	{
		PathologicalCaseOrderItem derivedOrderItem = new PathologicalCaseOrderItem();
	    derivedOrderItem.setTissueSite(orderSpecimenBean.getTissueSite());
	    derivedOrderItem.setPathologicalStatus(orderSpecimenBean.getPathologicalStatus());
	    derivedOrderItem.setSpecimenClass(orderSpecimenBean.getSpecimenClass());
	    derivedOrderItem.setSpecimenType(orderSpecimenBean.getSpecimenType());
		SpecimenCollectionGroup specimenCollGroup=new SpecimenCollectionGroup();
		specimenCollGroup.setId(new Long(orderSpecimenBean.getSpecimenCollectionGroup()));
		derivedOrderItem.setSpecimenCollectionGroup(specimenCollGroup);
		
		return derivedOrderItem;
	}
	
	/**
	 * @param orderBiospecimenArrayForm object
	 * @return HashMap object
	 */
	private HashMap putOrderDetailsForArray(OrderBiospecimenArrayForm orderBiospecimenArrayForm)
	{
		HashMap orderItemsMap=null;
		
		this.setComment(orderBiospecimenArrayForm.getOrderForm().getComments());
		this.setName(orderBiospecimenArrayForm.getOrderForm().getOrderRequestName());
		this.setStatus("New");
		this.setRequestedDate(new Date());
		Long distributionProtocolId = new Long(orderBiospecimenArrayForm.getOrderForm().getDistributionProtocol());

		DistributionProtocol distributionProtocolObj = new DistributionProtocol();
		distributionProtocolObj.setId(distributionProtocolId);
			
		this.setDistributionProtocol(distributionProtocolObj);
		orderItemsMap = (HashMap) orderBiospecimenArrayForm.getValues();
		return orderItemsMap;
	}
	
	/**
	 * @param orderSpecimenForm object
	 * @return HashMap object
	 */
	private HashMap putOrderDetailsForSpecimen(OrderSpecimenForm orderSpecimenForm)
	{
		HashMap orderItemsMap=null;
		//IBizLogic defaultBizLogic = BizLogicFactory.getInstance().getBizLogic(-1);
		this.setComment(orderSpecimenForm.getOrderForm().getComments());
		this.setName(orderSpecimenForm.getOrderForm().getOrderRequestName());
		this.setStatus("New");
		this.setRequestedDate(new Date());
		Long distributionProtocolId = new Long(orderSpecimenForm.getOrderForm().getDistributionProtocol());

		DistributionProtocol distributionProtocolObj = new DistributionProtocol();
		distributionProtocolObj.setId(distributionProtocolId);
		this.setDistributionProtocol(distributionProtocolObj);
		
		orderItemsMap = (HashMap) orderSpecimenForm.getValues();
		return orderItemsMap;
	}
	
	/**
	 * @param orderPathologyCaseForm object
	 * @return HashMap object
	 */
	private HashMap putOrderDetailsForPathologyCase(OrderPathologyCaseForm orderPathologyCaseForm)
	{
		HashMap orderItemsMap=null;
		
		this.setComment(orderPathologyCaseForm.getOrderForm().getComments());
		this.setName(orderPathologyCaseForm.getOrderForm().getOrderRequestName());
		this.setStatus("New");
		this.setRequestedDate(new Date());
		Long distributionProtocolId = new Long(orderPathologyCaseForm.getOrderForm().getDistributionProtocol());

		DistributionProtocol distributionProtocolObj = new DistributionProtocol();
		distributionProtocolObj.setId(distributionProtocolId);
			
		this.setDistributionProtocol(distributionProtocolObj);
		orderItemsMap = (HashMap) orderPathologyCaseForm.getValues();
		return orderItemsMap;
	}
	
	/**
	 * @param orderSpecimenBean object
	 * @param orderItem object
	 * @return OrderItem object
	 */
	private OrderItem getOrderArrayItem(OrderSpecimenBean orderSpecimenBean,OrderItem orderItem)
	{
		SpecimenArray specimenArray = new SpecimenArray();
		specimenArray.setId(new Long(orderSpecimenBean.getSpecimenId()));
		
		ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = new ExistingSpecimenArrayOrderItem();
		existingSpecimenArrayOrderItem.setSpecimenArray(specimenArray);
		orderItem = existingSpecimenArrayOrderItem;
		existingSpecimenArrayOrderItem = null;
			
		return orderItem;
	}
	
	/**
	 * @param newArrayOrderItems object
	 * @return List object
	 */
	private List putnewArrayDetailsforArray(List newArrayOrderItems)
	{
		List newOrderItems=null;
		if(newArrayOrderItems!=null)
		{
			newOrderItems=new ArrayList();
			Iterator orderArrayItemsCollectionItr = newArrayOrderItems.iterator();
			
			while(orderArrayItemsCollectionItr.hasNext())
			{
				DefineArrayForm defineArrayObj=(DefineArrayForm)orderArrayItemsCollectionItr.next();
				NewSpecimenArrayOrderItem newSpecimenArrayOrderItem=new NewSpecimenArrayOrderItem();
				newSpecimenArrayOrderItem.setName(defineArrayObj.getArrayName());
	
				SpecimenArrayType specimenArrayTypeObj=new SpecimenArrayType();
				
				//specimenArrayTypeObj.setName(defineArrayObj.getArraytype());
				specimenArrayTypeObj.setId(new Long(defineArrayObj.getArraytype()));
				newSpecimenArrayOrderItem.setSpecimenArrayType(specimenArrayTypeObj);
				newOrderItems.add(newSpecimenArrayOrderItem);
			}
		}
		return 	newOrderItems;

	}
	
	/**
	 * 
	 * @param abstractActionForm object.
	 */
	private void updateOrderDetails(AbstractActionForm abstractActionForm)
	{
		RequestDetailsForm requestDetailsForm = (RequestDetailsForm)abstractActionForm;
		if(requestDetailsForm.getMailNotification() != null && requestDetailsForm.getMailNotification().booleanValue() == true){
			this.mailNotification = requestDetailsForm.getMailNotification();
		}
		//Setting Comments in Order Domain Obj.
		this.setComment(requestDetailsForm.getAdministratorComments());
		//Setting the order Id.
		Long orderId = new Long(requestDetailsForm.getId());
		this.setId(orderId); 
		
		Collection beanObjSet = parseValuesMap(requestDetailsForm.getValues());
		
		Iterator iter = beanObjSet.iterator();
		Collection<OrderItem> domainObjSet = new HashSet<OrderItem>();
		Collection distributionObjectCollection = new HashSet();
		Distribution distribution = new Distribution();	
		// set by pratha
		distribution.setComment(requestDetailsForm.getAdministratorComments());
		Collection<DistributedItem> distributedItemCollection = new HashSet<DistributedItem>();
		while(iter.hasNext())
		{
			//Setting the Order Id
			OrderDetails order = setOrderId(orderId);
			Object obj = iter.next();
			//For specimen order item.
			if(obj instanceof RequestDetailsBean)
			{
				RequestDetailsBean requestDetailsBean = (RequestDetailsBean)obj;
				 //For skipping iteration when status drop down is disabled.
				if(requestDetailsBean.getAssignedStatus() == null || requestDetailsBean.getAssignedStatus().trim().equalsIgnoreCase(""))
				{
					continue;
				}
				OrderItem orderItem = populateOrderItemForSpecimenOrderItems(requestDetailsBean,order,distributedItemCollection,distribution,requestDetailsForm,distributionObjectCollection);
				domainObjSet.add(orderItem);
			}
			//For defined array header object.
			else if(obj instanceof DefinedArrayRequestBean)
			{
				DefinedArrayRequestBean definedArrayRequestBean = (DefinedArrayRequestBean)obj;
				//For skipping iteration when status drop down is disabled.
				if(definedArrayRequestBean.getAssignedStatus() == null || definedArrayRequestBean.getAssignedStatus().trim().equalsIgnoreCase(""))
				{
					continue;
				}	
				OrderItem orderItem = populateOrderItemForArrayHeader(definedArrayRequestBean,order,distributedItemCollection,distribution,requestDetailsForm,distributionObjectCollection);
				domainObjSet.add(orderItem);
			}
			//For defined array details.
			else if(obj instanceof DefinedArrayDetailsBean)
			{
				DefinedArrayDetailsBean definedArrayDetailsBean = (DefinedArrayDetailsBean)obj;
				//For skipping iteration when status drop down is disabled.
				if(definedArrayDetailsBean.getAssignedStatus() == null || definedArrayDetailsBean.getAssignedStatus().trim().equalsIgnoreCase(""))
				{
					continue;
				}
				OrderItem orderItem = setOrderForDefineArrayDetails(order,definedArrayDetailsBean);
				domainObjSet.add(orderItem);
			}
			//For Existing array order item.
			else if(obj instanceof ExistingArrayDetailsBean)
			{
				ExistingArrayDetailsBean existingArrayDetailsBean = (ExistingArrayDetailsBean)obj;
				// For skipping iteration when status drop down is disabled.
				if(existingArrayDetailsBean.getAssignedStatus() == null || existingArrayDetailsBean.getAssignedStatus().trim().equalsIgnoreCase(""))
				{
					continue;
				}
				OrderItem orderItem = populateOrderItemForExistingArray(existingArrayDetailsBean,order,distributedItemCollection,distribution,requestDetailsForm,distributionObjectCollection);
				domainObjSet.add(orderItem);
			}
		}
		this.setDistributionCollection(distributionObjectCollection);		
		this.setOrderItemCollection(domainObjSet);	
	}
	/**
	 * @param orderItem object
	 * @param distributedItem object
	 * @param distribution object
	 * @param distributedItemCollection object
	 * @return Distribution object
	 */
	private Distribution setDistributedItemCollectionInDistribution(OrderItem orderItem,DistributedItem distributedItem,Distribution distribution,Collection<DistributedItem> distributedItemCollection)
	{
		orderItem.setDistributedItem(distributedItem);
		distributedItemCollection.add(distributedItem);
		distribution.setDistributedItemCollection(distributedItemCollection);
		return distribution;
	}
	
	/**
	 * @return the distributionCollection object
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Distribution" cascade="save-update" lazy="false"
	 * @hibernate.set name="distributionCollection" table="CATISSUE_DISTRIBUTION"
	 * @hibernate.collection-key column="ORDER_ID" 
	 */
	public Collection getDistributionCollection()
	{
		return distributionCollection;
	}

	
	/**
	 * @param distributionCollection the distributionCollection to set
	 */
	public void setDistributionCollection(Collection distributionCollection)
	{
		this.distributionCollection = distributionCollection;
	}

	 /**
     * Returns message label to display on success add or edit
     * @return String object
     */
	public String getMessageLabel()
	{
		String messageLabel;
		if(operationAdd)
		{
			messageLabel = this.name ;
		}
		else
		{
			int i = OrderBizLogic.numberItemsUpdated;
			messageLabel = " "+i+" OrderItems.";
		}
		return messageLabel;
	}
	/**
	 * @param distribution object
	 * @param requestDetailsForm object
	 * @return Distribution object
	 */
	private Distribution setDistributionProtocolInDistribution(Distribution distribution,RequestDetailsForm requestDetailsForm)
	{
		DistributionProtocol distributionProtocol = new DistributionProtocol();
		distributionProtocol.setId(new Long(requestDetailsForm.getDistributionProtocolId()));
		distribution.setDistributionProtocol(distributionProtocol);
		return distribution;
	}
	
	/**
	 * @param requestDetailsBean object
	 * @param order object
	 * @param distributedItemCollection object
	 * @param distribution object
	 * @param requestDetailsForm object
	 * @param distributionObjectCollection object
	 * @return OrderItem object
	 */
 private OrderItem populateOrderItemForSpecimenOrderItems(RequestDetailsBean requestDetailsBean, OrderDetails order,Collection distributedItemCollection,Distribution distribution,RequestDetailsForm requestDetailsForm,Collection distributionObjectCollection)
 {
	
	 OrderItem orderItem = new OrderItem(); 

	 // modified by pratha for bug# 5663	and 7355
	 DerivedSpecimenOrderItem derivedOrderItem = null;
	 ExistingSpecimenOrderItem existingOrderItem = null;
	 PathologicalCaseOrderItem pathologicalCaseOrderItem = null;
	 OrderBizLogic orderBizLogic = (OrderBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
		
	 Specimen specimen = new Specimen();
	 if(requestDetailsBean.getInstanceOf().trim().equalsIgnoreCase("Existing"))
	 {   
		 existingOrderItem =  new ExistingSpecimenOrderItem();
		 specimen.setId(new Long(requestDetailsBean.getSpecimenId()));
		 specimen.setLabel(requestDetailsBean.getRequestedItem());
		 existingOrderItem.setSpecimen(specimen);
		 orderItem = existingOrderItem;
		

	 }else if(requestDetailsBean.getInstanceOf().trim().equalsIgnoreCase("DerivedPathological") ||requestDetailsBean.getInstanceOf().trim().equalsIgnoreCase("Pathological") )
	 {
		 pathologicalCaseOrderItem =  new PathologicalCaseOrderItem();
		 SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup)orderBizLogic.retrieveSCG(Long.parseLong
				 (requestDetailsBean.getSpecimenCollGroupId()));
		 pathologicalCaseOrderItem.setSpecimenCollectionGroup(specimenCollectionGroup);
		 pathologicalCaseOrderItem.setSpecimenClass(requestDetailsBean.getClassName());
		 pathologicalCaseOrderItem.setSpecimenType(requestDetailsBean.getType());
		 orderItem = pathologicalCaseOrderItem;

	 }else {

		 derivedOrderItem = new DerivedSpecimenOrderItem();
		 specimen.setId(new Long(requestDetailsBean.getSpecimenId()));
		 specimen.setLabel(requestDetailsBean.getRequestedItem());
		 derivedOrderItem.setParentSpecimen(specimen);
		 
		 derivedOrderItem.setSpecimenClass(requestDetailsBean.getClassName());
		 derivedOrderItem.setSpecimenType(requestDetailsBean.getType());
		 orderItem = derivedOrderItem;

	 }


	 //For Distribution.
	 if(requestDetailsBean.getAssignedStatus().trim().equalsIgnoreCase("Distributed") && requestDetailsBean.getDistributedItemId().equals("")
			 ||(requestDetailsBean.getAssignedStatus().trim().equalsIgnoreCase("Distributed And Close") && requestDetailsBean.getDistributedItemId().equals("")))
	 {	
		 //Setting the Site for distribution.
		 distribution = setSiteInDistribution(distribution,requestDetailsForm);		
		 DistributedItem distributedItem = new DistributedItem();
		
		 specimen.setId(new Long(requestDetailsBean.getRequestFor()));
		 
		 distributedItem.setSpecimen(specimen);
		 //For setting assigned quantity in Distribution.
		 if(requestDetailsBean.getRequestedQty() != null && !requestDetailsBean.getRequestedQty().trim().equalsIgnoreCase(""))
		 {
			 distributedItem.setQuantity(new Double(requestDetailsBean.getRequestedQty()));
		 }

		 distribution = setDistributedItemCollectionInDistribution(orderItem,distributedItem,distribution,distributedItemCollection);

		 //Setting the distribution protocol in Distribution.
		 distribution = setDistributionProtocolInDistribution(distribution,requestDetailsForm);

		 distributionObjectCollection.add(distribution);	
	 }	
	 //Updating Description and Status.
	 orderItem.setId(new Long(requestDetailsBean.getOrderItemId()));
	 orderItem.setStatus(requestDetailsBean.getAssignedStatus());
	 orderItem.setDescription(requestDetailsBean.getDescription());
	 //Setting the order id 
	 orderItem.setOrderDetails(order);

	 return orderItem;
 }
 
 /*private void updateExistingSpecimenOrderItem(OrderItem orderItem,RequestDetailsBean requestDetailsBean)
 {
	 	existingOrderItem =  new ExistingSpecimenOrderItem();
		specimen.setId(new Long(requestDetailsBean.getRequestFor()));
		specimen.setLabel(requestDetailsBean.getRequestedItem());
		existingOrderItem.setSpecimen(specimen);
		orderItem = existingOrderItem;
 }
 
 private void updateDerivedSpecimenOrderItem(OrderItem orderItem,RequestDetailsBean requestDetailsBean)
 {
	 
 }
 
 private void updatePathologicalCaseOrderItem(OrderItem orderItem,RequestDetailsBean requestDetailsBean)
 {
	 
 }*/
 

	/**
	 * @param distribution object
	 * @param requestDetailsForm object
	 * @return Distribution object
	 */
	private Distribution setSiteInDistribution(Distribution distribution,RequestDetailsForm requestDetailsForm)
	{
		Site toSite = new Site();
		toSite.setId(new Long(requestDetailsForm.getSite()));
		distribution.setToSite(toSite);
		return distribution;
	}
	/**
	 * @param order object
	 * @param definedArrayDetailsBean object
	 * @return OrderItem object
	 */
	 
	private OrderItem  setOrderForDefineArrayDetails( OrderDetails order, DefinedArrayDetailsBean definedArrayDetailsBean)
	{
		ExistingSpecimenOrderItem existingSpecOrderItem = new ExistingSpecimenOrderItem();
	
//		For READY FOR ARRAY PREPARATION
		if(definedArrayDetailsBean.getAssignedStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
		{					
			DistributedItem distributedItem = new DistributedItem();			
			Specimen specimen = new Specimen();

				if(definedArrayDetailsBean.getRequestFor() == null || definedArrayDetailsBean.getRequestFor().trim().equalsIgnoreCase(""))
				{
					// for existing Specimen.
					if (definedArrayDetailsBean.getInstanceOf().trim()
							.equalsIgnoreCase("Existing")) {
						specimen.setId(new Long(definedArrayDetailsBean
								.getSpecimenId()));
					} else {// For derived specimen.
						specimen.setId(new Long(definedArrayDetailsBean
								.getRequestFor()));
					}
	
					distributedItem.setSpecimen(specimen);
					// Updating Description and Status.
					// orderItem = newSpecimenArrayOrderItem;
					existingSpecOrderItem.setId(new Long(definedArrayDetailsBean
							.getOrderItemId()));
					existingSpecOrderItem.setStatus(definedArrayDetailsBean
							.getAssignedStatus());
					existingSpecOrderItem.setDescription(definedArrayDetailsBean
							.getDescription());
					// Setting the order id
					existingSpecOrderItem.setOrderDetails(order);
					existingSpecOrderItem.setDistributedItem(distributedItem);
					existingSpecOrderItem.setSpecimen(specimen);
			}	
	  }
	  return existingSpecOrderItem;
	}
	
	
	/**
	 * @param orderId object
	 * @return OrderDetails object
	 */
	private OrderDetails setOrderId(Long orderId)
	{
		OrderDetails order = new OrderDetails();
		order.setId(orderId);
		return order;
		
	}
	
	/**
	 * @param definedArrayRequestBean object 
	 * @param order object
	 * @param distributedItemCollection object
	 * @param distribution object
	 * @param requestDetailsForm object
	 * @param distributionObjectCollection object
	 * @return OrderItem object
	 */
  private OrderItem populateOrderItemForArrayHeader(DefinedArrayRequestBean definedArrayRequestBean,OrderDetails order,Collection distributedItemCollection,Distribution distribution,RequestDetailsForm requestDetailsForm,Collection distributionObjectCollection)
	{
	     OrderItem orderItem = new NewSpecimenArrayOrderItem();
		//Updating Description and Status.
		orderItem.setId(new Long(definedArrayRequestBean.getOrderItemId()));
		orderItem.setStatus(definedArrayRequestBean.getAssignedStatus());						
		//Setting the order id 
		orderItem.setOrderDetails(order);	
		
		//For Distribution.
		if((definedArrayRequestBean.getAssignedStatus().trim().equalsIgnoreCase("Distributed") ||definedArrayRequestBean.getAssignedStatus().trim().equalsIgnoreCase("Distributed And Close"))&& definedArrayRequestBean.getDistributedItemId().equals(""))
		{	
			//Setting the Site for distribution.
			distribution = setSiteInDistribution(distribution,requestDetailsForm);
			
			DistributedItem distributedItem = new DistributedItem();
			SpecimenArray specimenArray = new SpecimenArray();
			if(definedArrayRequestBean.getArrayId() != null && !definedArrayRequestBean.getArrayId().trim().equalsIgnoreCase(""))
			{
				specimenArray.setId(new Long(definedArrayRequestBean.getArrayId()));
			}
			distributedItem.setSpecimenArray(specimenArray);
	
			//For setting assigned quantity in Distribution.
	        distributedItem.setQuantity(new Double("1"));
	        
		    distribution = setDistributedItemCollectionInDistribution(orderItem,distributedItem,distribution,distributedItemCollection);
			
			//Setting the distribution protocol in Distribution.
			distribution = setDistributionProtocolInDistribution(distribution,requestDetailsForm);
			
			distributionObjectCollection.add(distribution);	
		}
		return orderItem;
	}

	/**
	 * @param existingArrayDetailsBean object
	 * @param order object
	 * @param distributedItemCollection object
	 * @param distribution object
	 * @param requestDetailsForm object
	 * @param distributionObjectCollection object
	 * @return OrderItem object
	 */
  private OrderItem populateOrderItemForExistingArray(ExistingArrayDetailsBean existingArrayDetailsBean, OrderDetails order,Collection distributedItemCollection,Distribution distribution,RequestDetailsForm requestDetailsForm,Collection distributionObjectCollection)
	{
		ExistingSpecimenArrayOrderItem existingSpecArrOrderItem = new ExistingSpecimenArrayOrderItem();
		//Updating Description and Status.

		existingSpecArrOrderItem.setId(new Long(existingArrayDetailsBean.getOrderItemId()));
		existingSpecArrOrderItem.setStatus(existingArrayDetailsBean.getAssignedStatus());
		existingSpecArrOrderItem.setDescription(existingArrayDetailsBean.getAddDescription());		
		SpecimenArray specimenArray = new SpecimenArray();
		if(existingArrayDetailsBean.getArrayId() != null && !existingArrayDetailsBean.getArrayId().trim().equalsIgnoreCase(""))
		{
			specimenArray.setId(new Long(existingArrayDetailsBean.getArrayId()));
		}
		specimenArray.setName(existingArrayDetailsBean.getBioSpecimenArrayName());
		existingSpecArrOrderItem.setSpecimenArray(specimenArray);
		//Setting the order id 
		existingSpecArrOrderItem.setOrderDetails(order);
		
		//For Distribution.
		if((existingArrayDetailsBean.getAssignedStatus().trim().equalsIgnoreCase("Distributed")||existingArrayDetailsBean.getAssignedStatus().trim().equalsIgnoreCase("Distributed And Close")) && existingArrayDetailsBean.getDistributedItemId().equals(""))
		{	
			//Setting the Site for distribution.
			distribution = setSiteInDistribution(distribution,requestDetailsForm);
		    //Making distributed items
			DistributedItem distributedItem = new DistributedItem();			
			distributedItem.setSpecimenArray(specimenArray);
			
			if(existingArrayDetailsBean.getRequestedQuantity().equals("0.0"))
			{
				distributedItem.setQuantity(new Double("1"));
			}
			else
			{
				distributedItem.setQuantity(new Double(existingArrayDetailsBean.getRequestedQuantity()));
			}
			distribution = setDistributedItemCollectionInDistribution(existingSpecArrOrderItem,distributedItem,distribution,distributedItemCollection);
		    //Setting the distribution protocol in Distribution.
			distribution = setDistributionProtocolInDistribution(distribution,requestDetailsForm);
			
			distributionObjectCollection.add(distribution);	
			
		}
		
		return existingSpecArrOrderItem;
	}

	
	/**
	 * @param map object
	 * @return Collection object
	 */
	private Collection parseValuesMap(Map map)
	{
		final MapDataParser mapDataParser = new MapDataParser("edu.wustl.catissuecore.bean");
		Collection beanObjSet = null;
		try
		{
			beanObjSet = mapDataParser.generateData(map);
		}
		catch (Exception e)
		{
			Logger.out.debug(""+e);			
		}
		return beanObjSet;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.IValueObject)
	 */
	//@Override
	/*public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		// TODO Auto-generated method stub
		
	}*/
}