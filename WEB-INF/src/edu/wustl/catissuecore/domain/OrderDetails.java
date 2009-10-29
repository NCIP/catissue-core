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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.exolab.castor.xml.schema.Order;

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
import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * Order corresponds to the orders requested by the user(scientist/researcher).
 * @hibernate.class table="CATISSUE_ORDER"
 * @author ashish_gupta
 */

public class OrderDetails extends AbstractDomainObject implements Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(OrderDetails.class);

	/**
	 * To show custom message for add and edit.
	 */
	private boolean operationAdd;

	/**
	 * Serial Version ID.
	 */
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

	/**
	 * mailNotification.
	 */
	protected Boolean mailNotification = Boolean.FALSE;

	/**
	 * Default constructor.
	 */
	public OrderDetails()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param form AbstractActionForm
	 * @throws AssignDataException object.
	 */
	public OrderDetails(AbstractActionForm form) throws AssignDataException
	{
		super();
		this.setAllValues(form);
	}

	/**
	 * Returns the system generated unique id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_ORDER_SEQ"
	 * @return the system generated unique id.
	 * @see #setId(Long)
	 * */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Sets the system generated unique id.
	 * @param identifier the system generated unique id.
	 * @see #getId()
	 * */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;
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
		return this.comment;
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
		return this.mailNotification;
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
	 * @hibernate.many-to-one column="DISTRIBUTION_PROTOCOL_ID" class="edu.wustl.
	 * catissuecore.domain.DistributionProtocol"
	 * constrained="true"
	 * @return the distributionProtocol
	 */
	public DistributionProtocol getDistributionProtocol()
	{
		return this.distributionProtocol;
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
		return this.name;
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
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.
	 * OrderItem" cascade="save-update" lazy="false"
	 * @hibernate.set name="orderItemCollection" table="CATISSUE_ORDER_ITEM"
	 * @hibernate.collection-key column="ORDER_ID"
	 */
	public Collection getOrderItemCollection()
	{
		return this.orderItemCollection;
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
		return this.requestedDate;
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
		return this.status;
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
	 * @param abstractForm object.
	 */
	@Override
	public void setAllValues(IValueObject abstractForm)
	{
		try
		{

			final AbstractActionForm abstractActionForm = (AbstractActionForm) abstractForm;
			if (abstractActionForm.isAddOperation()) //insert
			{
				this.operationAdd = true;
				this.insertOrderDetails(abstractActionForm);
			}
			else
			//update
			{
				this.updateOrderDetails(abstractActionForm);
			}
		}
		catch (final BizLogicException exp)
		{
			OrderDetails.logger.error(exp.getMessage(),exp);
			exp.printStackTrace();
		}
	}

	/**
	 * This function inserts order data to order table.
	 * @param abstractActionForm object
	 */
	private void insertOrderDetails(AbstractActionForm abstractActionForm)
	{
		Map orderItemsMap = null;
		Collection orderItemsCollection = new LinkedHashSet();
		List newSpecimenArrayObjList = null;

		if (abstractActionForm.getPageOf().equals(Constants.SPECIMEN_ORDER_FORM_TYPE))
		{
			final OrderSpecimenForm orderSpecimenForm = (OrderSpecimenForm) abstractActionForm;
			orderItemsMap = this.putOrderDetailsForSpecimen(orderSpecimenForm);
			newSpecimenArrayObjList = this.putnewArrayDetailsforArray(orderSpecimenForm
					.getDefineArrayObj());
		}

		if (abstractActionForm.getPageOf().equals(Constants.ARRAY_ORDER_FORM_TYPE))
		{
			final OrderBiospecimenArrayForm orderBiospecimenArrayForm = (OrderBiospecimenArrayForm) abstractActionForm;
			orderItemsMap = this.putOrderDetailsForArray(orderBiospecimenArrayForm);
			newSpecimenArrayObjList = this.putnewArrayDetailsforArray(orderBiospecimenArrayForm
					.getDefineArrayObj());

		}
		if (abstractActionForm.getPageOf().equals(Constants.PATHOLOGYCASE_ORDER_FORM_TYPE))
		{
			final OrderPathologyCaseForm orderPathologyCaseForm = (OrderPathologyCaseForm) abstractActionForm;
			orderItemsMap = this.putOrderDetailsForPathologyCase(orderPathologyCaseForm);
			newSpecimenArrayObjList = this.putnewArrayDetailsforArray(orderPathologyCaseForm
					.getDefineArrayObj());
		}
		//Obtain orderItemCollection.
		final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.bean");
		try
		{
			orderItemsCollection = parser.generateData(orderItemsMap);
		}
		catch (final Exception e)
		{
			OrderDetails.logger.error(e.getMessage(),e);
			e.printStackTrace();
		}

		final Collection orderItemsSet = new LinkedHashSet();

		final Iterator orderItemsCollectionItr = orderItemsCollection.iterator();
		OrderItem orderItem = null;
		SpecimenOrderItem specimenOrderItem = null;

		while (orderItemsCollectionItr.hasNext())
		{
			final OrderSpecimenBean orderSpecimenBean = (OrderSpecimenBean) orderItemsCollectionItr
					.next();
			Double reqQty = new Double(0);
			if (orderSpecimenBean.getTypeOfItem().equals("specimen"))
			{
				specimenOrderItem = this.setBioSpecimen(orderSpecimenBean);
				if (orderSpecimenBean.getArrayName().equals("None"))
				{
					orderItem = specimenOrderItem;
					orderItemsSet.add(orderItem);
				}
				else
				{
					if (newSpecimenArrayObjList != null)
					{
						final Iterator iteratorNewSpecimen = newSpecimenArrayObjList.iterator();
						while (iteratorNewSpecimen.hasNext())
						{
							final NewSpecimenArrayOrderItem newSpecimenArrayObj = (NewSpecimenArrayOrderItem) iteratorNewSpecimen
									.next();
							if (newSpecimenArrayObj.getName().equals(
									orderSpecimenBean.getArrayName()))
							{
								Collection orderItemCollection = newSpecimenArrayObj
										.getSpecimenOrderItemCollection();
								if (orderItemCollection == null)
								{
									orderItemCollection = new LinkedHashSet();
								}
								specimenOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayObj);
								orderItem = specimenOrderItem;
								//Test Line
								orderItemsSet.add(orderItem);
								orderItemCollection.add(orderItem);
								newSpecimenArrayObj
										.setSpecimenOrderItemCollection(orderItemCollection);
							}
						}
					}
				}
			}

			if (Constants.PATHOLOGYCASE_ORDER_FORM_TYPE.equals(orderSpecimenBean.getTypeOfItem()))
			{
				specimenOrderItem = this.setPathologyCase(orderSpecimenBean);
				if (Constants.NONE.equals(orderSpecimenBean.getArrayName()))
				{
					orderItem = specimenOrderItem;
					orderItemsSet.add(orderItem);
				}
				else
				{
					if (newSpecimenArrayObjList != null)
					{
						final Iterator iteratorNewSpecArrObj = newSpecimenArrayObjList.iterator();
						while (iteratorNewSpecArrObj.hasNext())
						{
							final NewSpecimenArrayOrderItem newSpecimenArrayObj = (NewSpecimenArrayOrderItem) iteratorNewSpecArrObj
									.next();
							if (newSpecimenArrayObj.getName().equals(
									orderSpecimenBean.getArrayName()))
							{
								Collection orderItemCollection = newSpecimenArrayObj
										.getSpecimenOrderItemCollection();
								if (orderItemCollection == null)
								{
									orderItemCollection = new LinkedHashSet();
								}
								specimenOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayObj);
								orderItem = specimenOrderItem;
								//Test Line
								orderItemsSet.add(orderItem);
								orderItemCollection.add(orderItem);
								newSpecimenArrayObj
										.setSpecimenOrderItemCollection(orderItemCollection);
							}
						}
					}
				}
			}
			if (Constants.ARRAY_ORDER_FORM_TYPE.equals(orderSpecimenBean.getTypeOfItem()))
			{
				orderItem = this.getOrderArrayItem(orderSpecimenBean, orderItem);
				if ((Constants.ARRAY_ORDER_FORM_TYPE.equals(orderSpecimenBean.getTypeOfItem()))
						&& (Constants.TISSUE.equals(orderSpecimenBean.getSpecimenClass()))
						&& (orderSpecimenBean.getSpecimenType().equals("unblock")))
				{
					reqQty = new Double(1);
				}
				else
				{
					reqQty = new Double(orderSpecimenBean.getRequestedQuantity());
				}
				orderItem.setRequestedQuantity(reqQty);
				orderItem.setDescription(orderSpecimenBean.getDescription());
				orderItem.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
				orderItemsSet.add(orderItem);
			}
		}//End While

		if (newSpecimenArrayObjList != null)
		{
			final Iterator iteratorNewSpecObj = newSpecimenArrayObjList.iterator();
			while (iteratorNewSpecObj.hasNext())
			{
				orderItem = (NewSpecimenArrayOrderItem) iteratorNewSpecObj.next();
				orderItem.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
				orderItemsSet.add(orderItem);
			}
		}
		this.setOrderItemCollection(orderItemsSet);
	}

	/**
	 * This function sets the Bio Specimen Order Item.
	 * @param orderSpecimenBean object
	 * @return specimenOrderItem SpecimenOrderItem instance
	 */
	private SpecimenOrderItem setBioSpecimen(OrderSpecimenBean orderSpecimenBean)
	{
		SpecimenOrderItem specimenOrderItem = null;
		Double reqQty = new Double(0);

		if (Constants.FALSE.equals(orderSpecimenBean.getIsDerived())) //Existing specimen.
		{
			specimenOrderItem = this.setExistingSpecimenOrderItem(orderSpecimenBean);
			reqQty = new Double(orderSpecimenBean.getRequestedQuantity());
			specimenOrderItem.setRequestedQuantity(reqQty);
			specimenOrderItem.setDescription(orderSpecimenBean.getDescription());
			specimenOrderItem.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
		}
		else
		//Derived specimen.
		{
			specimenOrderItem = this.setDerivedSpecimenOrderItem(orderSpecimenBean);
			reqQty = new Double(orderSpecimenBean.getRequestedQuantity());
			specimenOrderItem.setRequestedQuantity(reqQty);
			specimenOrderItem.setDescription(orderSpecimenBean.getDescription());
			specimenOrderItem.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
		}
		return specimenOrderItem;
	}

	/**
	 * This function sets the pathology case order item.
	 * @param orderSpecimenBean object
	 * @return specimenOrderItem SpecimenOrderItem instance
	 */
	private SpecimenOrderItem setPathologyCase(OrderSpecimenBean orderSpecimenBean)
	{
		SpecimenOrderItem specimenOrderItem = null;
		Double reqQty = new Double(0);

		if (Constants.FALSE.equals(orderSpecimenBean.getIsDerived())) //Existing specimen.
		{
			specimenOrderItem = this.setExistingOrderItemForPathology(orderSpecimenBean);
			reqQty = new Double(orderSpecimenBean.getRequestedQuantity());
			specimenOrderItem.setRequestedQuantity(reqQty);
			specimenOrderItem.setDescription(orderSpecimenBean.getDescription());
			specimenOrderItem.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
		}
		else
		{
			specimenOrderItem = this.setDerivedOrderItemForPathology(orderSpecimenBean);
			reqQty = new Double(orderSpecimenBean.getRequestedQuantity());
			specimenOrderItem.setRequestedQuantity(reqQty);
			specimenOrderItem.setDescription(orderSpecimenBean.getDescription());
			specimenOrderItem.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
		}
		return specimenOrderItem;
	}

	/**
	 * This function sets the existingSpecimen order item with the values from OrderSpecimenBean.
	 * @param orderSpecimenBean object
	 * @return ExistingSpecimenOrderItem object
	 */
	private ExistingSpecimenOrderItem setExistingSpecimenOrderItem(
			OrderSpecimenBean orderSpecimenBean)
	{
		final ExistingSpecimenOrderItem existingOrderItem = new ExistingSpecimenOrderItem();
		//Set Parent specimen
		final Specimen specimen = new Specimen();
		specimen.setId(Long.valueOf(orderSpecimenBean.getSpecimenId()));
		existingOrderItem.setSpecimen(specimen);

		return existingOrderItem;
	}

	/**
	 * This funciton sets the derivedspecimen orderitem with the values from OrderSpecimenBean.
	 * @param orderSpecimenBean object
	 * @return derivedSpecimenOrderItem DerivedSpecimenOrderItem object
	 */
	private DerivedSpecimenOrderItem setDerivedSpecimenOrderItem(OrderSpecimenBean orderSpecimenBean)
	{
		final DerivedSpecimenOrderItem derivedSpecimenOrderItem = new DerivedSpecimenOrderItem();
		//Set Parent specimen
		final Specimen specimen = new Specimen();
		specimen.setId(Long.valueOf(orderSpecimenBean.getSpecimenId()));
		derivedSpecimenOrderItem.setParentSpecimen(specimen);
		derivedSpecimenOrderItem.setSpecimenClass(orderSpecimenBean.getSpecimenClass());
		derivedSpecimenOrderItem.setSpecimenType(orderSpecimenBean.getSpecimenType());

		return derivedSpecimenOrderItem;
	}

	/**
	 * This function sets the Existing order Item for Pathology Case.
	 * @param orderSpecimenBean OrderSpecimenBean Object
	 * @return existingOrderItem PathologicalCaseOrderItem Object
	 */
	private PathologicalCaseOrderItem setExistingOrderItemForPathology(
			OrderSpecimenBean orderSpecimenBean)
	{
		final PathologicalCaseOrderItem existingOrderItem = new PathologicalCaseOrderItem();
		existingOrderItem.setTissueSite(orderSpecimenBean.getTissueSite());
		existingOrderItem.setPathologicalStatus(orderSpecimenBean.getPathologicalStatus());
		existingOrderItem.setSpecimenClass(orderSpecimenBean.getSpecimenClass());
		existingOrderItem.setSpecimenType(orderSpecimenBean.getSpecimenType());
		final SpecimenCollectionGroup specimenCollGroup = new SpecimenCollectionGroup();
		specimenCollGroup.setId(Long.valueOf(orderSpecimenBean.getSpecimenCollectionGroup()));
		existingOrderItem.setSpecimenCollectionGroup(specimenCollGroup);

		return existingOrderItem;
	}

	/**
	 * This function sets the Derived Order item for Pathology Case.
	 * @param orderSpecimenBean OrderSpecimenBean Object
	 * @return derivedOrderItem PathologicalCaseOrderItem Object
	 */
	private PathologicalCaseOrderItem setDerivedOrderItemForPathology(
			OrderSpecimenBean orderSpecimenBean)
	{
		final PathologicalCaseOrderItem derivedOrderItem = new PathologicalCaseOrderItem();
		derivedOrderItem.setTissueSite(orderSpecimenBean.getTissueSite());
		derivedOrderItem.setPathologicalStatus(orderSpecimenBean.getPathologicalStatus());
		derivedOrderItem.setSpecimenClass(orderSpecimenBean.getSpecimenClass());
		derivedOrderItem.setSpecimenType(orderSpecimenBean.getSpecimenType());
		final SpecimenCollectionGroup specimenCollGroup = new SpecimenCollectionGroup();
		specimenCollGroup.setId(Long.valueOf(orderSpecimenBean.getSpecimenCollectionGroup()));
		derivedOrderItem.setSpecimenCollectionGroup(specimenCollGroup);

		return derivedOrderItem;
	}

	/**
	 * @param orderBiospecimenArrayForm object
	 * @return HashMap object
	 */
	private Map putOrderDetailsForArray(OrderBiospecimenArrayForm orderBiospecimenArrayForm)
	{
		HashMap orderItemsMap = null;

		this.setComment(orderBiospecimenArrayForm.getOrderForm().getComments());
		this.setName(orderBiospecimenArrayForm.getOrderForm().getOrderRequestName());
		this.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
		this.setRequestedDate(new Date());
		final String protocolId = orderBiospecimenArrayForm.getOrderForm()
				.getDistributionProtocol();

		if (protocolId != null && !protocolId.equals(""))
		{
			final Long distributionId = Long.valueOf(orderBiospecimenArrayForm.getOrderForm()
					.getDistributionProtocol());
			final DistributionProtocol distributionProtocolObj = new DistributionProtocol();
			distributionProtocolObj.setId(distributionId);
			this.setDistributionProtocol(distributionProtocolObj);
		}

		orderItemsMap = (HashMap) orderBiospecimenArrayForm.getValues();
		return orderItemsMap;
	}

	/**
	 * @param orderSpecimenForm object
	 * @return HashMap object
	 */
	private Map putOrderDetailsForSpecimen(OrderSpecimenForm orderSpecimenForm)
	{
		//IBizLogic defaultBizLogic = BizLogicFactory.getInstance().getBizLogic(-1);
		this.setComment(orderSpecimenForm.getOrderForm().getComments());
		this.setName(orderSpecimenForm.getOrderForm().getOrderRequestName());
		this.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
		this.setRequestedDate(new Date());
		final String protocolId = orderSpecimenForm.getOrderForm().getDistributionProtocol();

		if (protocolId != null && !protocolId.equals(""))
		{
			final Long distributionId = Long.valueOf(orderSpecimenForm.getOrderForm()
					.getDistributionProtocol());
			final DistributionProtocol distributionProtocolObj = new DistributionProtocol();
			distributionProtocolObj.setId(distributionId);
			this.setDistributionProtocol(distributionProtocolObj);
		}

		final Map orderItemsMap = orderSpecimenForm.getValues();
		return orderItemsMap;
	}

	/**
	 * @param orderPathologyCaseForm object
	 * @return HashMap object
	 */
	private Map putOrderDetailsForPathologyCase(OrderPathologyCaseForm orderPathologyCaseForm)
	{
		HashMap orderItemsMap = null;

		this.setComment(orderPathologyCaseForm.getOrderForm().getComments());
		this.setName(orderPathologyCaseForm.getOrderForm().getOrderRequestName());
		this.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
		this.setRequestedDate(new Date());
		final String protocolId = orderPathologyCaseForm.getOrderForm().getDistributionProtocol();

		if (protocolId != null && !protocolId.equals(""))
		{
			final Long distributionId = Long.valueOf(orderPathologyCaseForm.getOrderForm()
					.getDistributionProtocol());
			final DistributionProtocol distributionProtocolObj = new DistributionProtocol();
			distributionProtocolObj.setId(distributionId);
			this.setDistributionProtocol(distributionProtocolObj);
		}
		orderItemsMap = (HashMap) orderPathologyCaseForm.getValues();
		return orderItemsMap;
	}

	/**
	 * @param orderSpecimenBean object
	 * @param orderItem object
	 * @return OrderItem object
	 */
	private OrderItem getOrderArrayItem(OrderSpecimenBean orderSpecimenBean, OrderItem orderItem)
	{
		final SpecimenArray specimenArray = new SpecimenArray();
		specimenArray.setId(Long.valueOf(orderSpecimenBean.getSpecimenId()));

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
		List newOrderItems = null;
		if (newArrayOrderItems != null)
		{
			newOrderItems = new ArrayList();
			final Iterator orderArrayItemsCollectionItr = newArrayOrderItems.iterator();

			while (orderArrayItemsCollectionItr.hasNext())
			{
				final DefineArrayForm defineArrayObj = (DefineArrayForm) orderArrayItemsCollectionItr
						.next();
				final NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = new NewSpecimenArrayOrderItem();
				newSpecimenArrayOrderItem.setName(defineArrayObj.getArrayName());

				final SpecimenArrayType specimenArrayTypeObj = new SpecimenArrayType();

				//specimenArrayTypeObj.setName(defineArrayObj.getArraytype());
				specimenArrayTypeObj.setId(Long.valueOf(defineArrayObj.getArraytype()));
				newSpecimenArrayOrderItem.setSpecimenArrayType(specimenArrayTypeObj);
				newOrderItems.add(newSpecimenArrayOrderItem);
			}
		}
		return newOrderItems;
	}

	/**
	 * Update the Order Details.
	 * @param abstractActionForm object.
	 * @throws BizLogicException : BizLogicException
	 */
	private void updateOrderDetails(AbstractActionForm abstractActionForm) throws BizLogicException
	{
		final RequestDetailsForm requestDetailsForm = (RequestDetailsForm) abstractActionForm;
		if (requestDetailsForm.getMailNotification() != null
				&& requestDetailsForm.getMailNotification().booleanValue() == Boolean.TRUE)
		{
			this.mailNotification = requestDetailsForm.getMailNotification();
		}
		//Setting Comments in Order Domain Obj.
		this.setComment(requestDetailsForm.getAdministratorComments());
		//Setting the order Id.
		final Long orderId = Long.valueOf(requestDetailsForm.getId());
		this.setId(orderId);

		if (requestDetailsForm.getIsDirectDistribution() != null
				&& requestDetailsForm.getIsDirectDistribution().booleanValue() == Boolean.TRUE)
		{
			this.setName(requestDetailsForm.getOrderName());
			this.setDistributionProtocol(requestDetailsForm.getDistributionProtocolId());
		}

		final Collection beanObjSet = this.parseValuesMap(requestDetailsForm.getValues());
		final Iterator iter = beanObjSet.iterator();
		final Collection<OrderItem> domainObjSet = new HashSet<OrderItem>();
		final Collection distributionObjectCollection = new HashSet();
		final Distribution distribution = new Distribution();
		// set by pratha
		distribution.setComment(requestDetailsForm.getAdministratorComments());
		final Collection<DistributedItem> distributedItemCollection = new HashSet<DistributedItem>();
		while (iter.hasNext())
		{
			//Setting the Order Id
			final OrderDetails order = this.setOrderId(orderId);
			final Object obj = iter.next();
			//For specimen order item.
			if (obj instanceof RequestDetailsBean)
			{
				final RequestDetailsBean requestDetailsBean = (RequestDetailsBean) obj;
				//For skipping iteration when status drop down is disabled.
				if (requestDetailsBean.getAssignedStatus() == null
						|| Constants.DOUBLE_QUOTES.equalsIgnoreCase(requestDetailsBean
								.getAssignedStatus().trim()))
				{
					continue;
				}
				final OrderItem orderItem = this.populateOrderItemForSpecimenOrderItems(
						requestDetailsBean, order, distributedItemCollection, distribution,
						requestDetailsForm, distributionObjectCollection);
				domainObjSet.add(orderItem);
			}
			//For defined array header object.
			else if (obj instanceof DefinedArrayRequestBean)
			{
				final DefinedArrayRequestBean definedArrayRequestBean = (DefinedArrayRequestBean) obj;
				//For skipping iteration when status drop down is disabled.
				if (definedArrayRequestBean.getAssignedStatus() == null
						|| Constants.DOUBLE_QUOTES.equalsIgnoreCase(definedArrayRequestBean
								.getAssignedStatus().trim()))
				{
					continue;
				}
				final OrderItem orderItem = this.populateOrderItemForArrayHeader(
						definedArrayRequestBean, order, distributedItemCollection, distribution,
						requestDetailsForm, distributionObjectCollection);
				domainObjSet.add(orderItem);
			}
			//For defined array details.
			else if (obj instanceof DefinedArrayDetailsBean)
			{
				final DefinedArrayDetailsBean definedArrayDetailsBean = (DefinedArrayDetailsBean) obj;
				//For skipping iteration when status drop down is disabled.
				if (definedArrayDetailsBean.getAssignedStatus() == null
						|| Constants.DOUBLE_QUOTES.equalsIgnoreCase(definedArrayDetailsBean
								.getAssignedStatus().trim()))
				{
					continue;
				}
				final OrderItem orderItem = this.setOrderForDefineArrayDetails(order,
						definedArrayDetailsBean);
				domainObjSet.add(orderItem);
			}
			//For Existing array order item.
			else if (obj instanceof ExistingArrayDetailsBean)
			{
				final ExistingArrayDetailsBean existingArrayDetailsBean = (ExistingArrayDetailsBean) obj;
				// For skipping iteration when status drop down is disabled.
				if (existingArrayDetailsBean.getAssignedStatus() == null
						|| Constants.DOUBLE_QUOTES.equalsIgnoreCase(existingArrayDetailsBean
								.getAssignedStatus().trim()))
				{
					continue;
				}
				final OrderItem orderItem = this.populateOrderItemForExistingArray(
						existingArrayDetailsBean, order, distributedItemCollection, distribution,
						requestDetailsForm, distributionObjectCollection);
				domainObjSet.add(orderItem);
			}
		}
		this.setDistributionCollection(distributionObjectCollection);
		this.setOrderItemCollection(domainObjSet);
	}

	/**
	 * @param distributionProtId : distributionProtId
	 */
	private void setDistributionProtocol(String distributionProtId)
	{

		try
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();

			final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
					.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
			final DistributionProtocol distributionProtocol = orderBizLogic
					.retrieveDistributionProtocol(distributionProtId);
			this.setDistributionProtocol(distributionProtocol);
		}
		catch (final BizLogicException e)
		{
			OrderDetails.logger.error(e.getMessage(),e);
			e.printStackTrace();
		}

	}

	/**
	 * @param orderItem object
	 * @param distributedItem object
	 * @param distribution object
	 * @param distributedItemCollection object
	 * @return Distribution object
	 */
	private Distribution setDistributedItemCollectionInDistribution(OrderItem orderItem,
			DistributedItem distributedItem, Distribution distribution,
			Collection<DistributedItem> distributedItemCollection)
	{
		orderItem.setDistributedItem(distributedItem);
		distributedItemCollection.add(distributedItem);
		distribution.setDistributedItemCollection(distributedItemCollection);
		return distribution;
	}

	/**
	 * @return the distributionCollection object
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Distribution"
	 * cascade="save-update" lazy="false"
	 * @hibernate.set name="distributionCollection" table="CATISSUE_DISTRIBUTION"
	 * @hibernate.collection-key column="ORDER_ID"
	 */
	public Collection getDistributionCollection()
	{
		return this.distributionCollection;
	}

	/**
	 * @param distributionCollection the distributionCollection to set
	 */
	public void setDistributionCollection(Collection distributionCollection)
	{
		this.distributionCollection = distributionCollection;
	}

	/**
	* Returns message label to display on success add or edit.
	* @return String object
	*/
	@Override
	public String getMessageLabel()
	{
		String messageLabel;
		if (this.operationAdd)
		{
			messageLabel = this.name;
		}
		else
		{
			final int numberItem = OrderBizLogic.numberItemsUpdated;
			messageLabel = " " + numberItem + " OrderItems.";
		}
		return messageLabel;
	}

	/**
	 * @param distribution object
	 * @param requestDetailsForm object
	 * @return Distribution object
	 */
	private Distribution setDistributionProtocolInDistribution(Distribution distribution,
			RequestDetailsForm requestDetailsForm)
	{
		final DistributionProtocol distributionProtocol = new DistributionProtocol();
		distributionProtocol.setId(Long.valueOf(requestDetailsForm.getDistributionProtocolId()));
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
	 * @throws BizLogicException : BizLogicException
	 */
	private OrderItem populateOrderItemForSpecimenOrderItems(RequestDetailsBean requestDetailsBean,
			OrderDetails order, Collection distributedItemCollection, Distribution distribution,
			RequestDetailsForm requestDetailsForm, Collection distributionObjectCollection)
			throws BizLogicException
	{

		OrderItem orderItem = new OrderItem();
		// modified by pratha for bug# 5663	and 7355
		DerivedSpecimenOrderItem derivedOrderItem = null;
		ExistingSpecimenOrderItem existingOrderItem = null;
		PathologicalCaseOrderItem pathologicalCaseOrderItem = null;
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
				.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);

		final Specimen specimen = new Specimen();
		if (requestDetailsBean.getInstanceOf().trim().equalsIgnoreCase("Existing"))
		{
			existingOrderItem = new ExistingSpecimenOrderItem();
			specimen.setId(Long.valueOf(requestDetailsBean.getSpecimenId()));
			specimen.setLabel(requestDetailsBean.getRequestedItem());
			existingOrderItem.setSpecimen(specimen);
			orderItem = existingOrderItem;

		}
		else if ("DerivedPathological".equalsIgnoreCase(requestDetailsBean.getInstanceOf().trim())
				|| "Pathological".equalsIgnoreCase(requestDetailsBean.getInstanceOf().trim()))
		{
			pathologicalCaseOrderItem = new PathologicalCaseOrderItem();
			SpecimenCollectionGroup specimenCollectionGroup = null;
			try
			{
				specimenCollectionGroup = orderBizLogic.retrieveSCG(Long
						.parseLong(requestDetailsBean.getSpecimenCollGroupId()));
			}
			catch (final BizLogicException exception)
			{
				OrderDetails.logger.error("Not able to retrieve" +
						" SpecimenCollectionGroup."
						+exception.getMessage(), exception);
				exception.printStackTrace();
			}
			pathologicalCaseOrderItem.setSpecimenCollectionGroup(specimenCollectionGroup);
			pathologicalCaseOrderItem.setSpecimenClass(requestDetailsBean.getClassName());
			pathologicalCaseOrderItem.setSpecimenType(requestDetailsBean.getType());
			orderItem = pathologicalCaseOrderItem;
		}
		else
		{
			derivedOrderItem = new DerivedSpecimenOrderItem();
			specimen.setId(Long.valueOf(requestDetailsBean.getSpecimenId()));
			specimen.setLabel(requestDetailsBean.getRequestedItem());
			derivedOrderItem.setParentSpecimen(specimen);

			derivedOrderItem.setSpecimenClass(requestDetailsBean.getClassName());
			derivedOrderItem.setSpecimenType(requestDetailsBean.getType());
			orderItem = derivedOrderItem;
		}

		//For Distribution.
		if ("Distributed".equalsIgnoreCase(requestDetailsBean.getAssignedStatus().trim())
				&& Constants.DOUBLE_QUOTES.equals(requestDetailsBean.getDistributedItemId())
				|| ("Distributed And Close".equalsIgnoreCase(requestDetailsBean.getAssignedStatus()
						.trim()) && Constants.DOUBLE_QUOTES.equals(requestDetailsBean
						.getDistributedItemId())))
		{
			//Setting the Site for distribution.
			distribution = this.setSiteInDistribution(distribution, requestDetailsForm);
			final DistributedItem distributedItem = new DistributedItem();

			specimen.setId(Long.valueOf(requestDetailsBean.getRequestFor()));

			distributedItem.setSpecimen(specimen);
			//For setting assigned quantity in Distribution.
			if (requestDetailsBean.getRequestedQty() != null
					&& !Constants.DOUBLE_QUOTES.equalsIgnoreCase(requestDetailsBean
							.getRequestedQty().trim()))
			{
				distributedItem.setQuantity(new Double(requestDetailsBean.getRequestedQty()));
			}

			distribution = this.setDistributedItemCollectionInDistribution(orderItem,
					distributedItem, distribution, distributedItemCollection);

			//Setting the distribution protocol in Distribution.
			distribution = this.setDistributionProtocolInDistribution(distribution,
					requestDetailsForm);

			distributionObjectCollection.add(distribution);
		}
		//Updating Description and Status.
		orderItem.setId(Long.valueOf(requestDetailsBean.getOrderItemId()));
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
	private Distribution setSiteInDistribution(Distribution distribution,
			RequestDetailsForm requestDetailsForm)
	{
		final Site toSite = new Site();
		toSite.setId(Long.valueOf(requestDetailsForm.getSite()));
		distribution.setToSite(toSite);
		return distribution;
	}

	/**
	 * @param order object
	 * @param definedArrayDetailsBean object
	 * @return OrderItem object
	 * @throws BizLogicException : BizLogicException
	 */

	private OrderItem setOrderForDefineArrayDetails(OrderDetails order,
			DefinedArrayDetailsBean definedArrayDetailsBean) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
				.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);

		SpecimenOrderItem specOrderItem = null;
		OrderItem orderItem = null;
		ExistingSpecimenOrderItem existingSpecimenOrderItem = null;
		DerivedSpecimenOrderItem derivedSpecimenOrderItem = null;
		PathologicalCaseOrderItem pathologicalCaseOrderItem = null;
		try
		{
			specOrderItem = orderBizLogic.retrieveSpecimenOrderItem(Long
					.parseLong(definedArrayDetailsBean.getOrderItemId()));
		}
		catch (final BizLogicException exception)
		{
			OrderDetails.logger.error("Not able to retrieve" +
					" SpecimenCollectionGroup."
					+exception.getMessage(), exception);
			exception.printStackTrace();
		}

		if (specOrderItem instanceof ExistingSpecimenOrderItem)
		{
			existingSpecimenOrderItem = (ExistingSpecimenOrderItem) specOrderItem;
			orderItem = existingSpecimenOrderItem;
		}
		else if (specOrderItem instanceof DerivedSpecimenOrderItem)
		{// For derived specimen.
			derivedSpecimenOrderItem = (DerivedSpecimenOrderItem) specOrderItem;
			orderItem = derivedSpecimenOrderItem;
		}
		else
		{
			pathologicalCaseOrderItem = (PathologicalCaseOrderItem) specOrderItem;
			orderItem = pathologicalCaseOrderItem;
		}

		//For READY FOR ARRAY PREPARATION
		if (Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION
				.equalsIgnoreCase(definedArrayDetailsBean.getAssignedStatus().trim()))
		{
			final DistributedItem distributedItem = new DistributedItem();
			final Specimen specimen = new Specimen();

			if (definedArrayDetailsBean.getRequestFor() == null
					|| Constants.DOUBLE_QUOTES.equalsIgnoreCase(definedArrayDetailsBean
							.getRequestFor().trim()))
			{
				// for existing Specimen.
				if (definedArrayDetailsBean.getInstanceOf().trim().equalsIgnoreCase("Existing"))
				{
					specimen.setId(Long.valueOf(definedArrayDetailsBean.getSpecimenId()));
					existingSpecimenOrderItem.setSpecimen(specimen);
				}
				else
				{// For derived specimen.
					specimen.setId(Long.valueOf(definedArrayDetailsBean.getRequestFor()));
				}

				distributedItem.setSpecimen(specimen);
				// Updating Description and Status.
				// orderItem = newSpecimenArrayOrderItem;
				orderItem.setStatus(definedArrayDetailsBean.getAssignedStatus());
				orderItem.setDescription(definedArrayDetailsBean.getDescription());
				// Setting the order id
				orderItem.setOrderDetails(order);
				orderItem.setDistributedItem(distributedItem);
			}
		}
		return orderItem;
	}

	/**
	 * @param orderId object
	 * @return OrderDetails object
	 */
	private OrderDetails setOrderId(Long orderId)
	{
		final OrderDetails order = new OrderDetails();
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
	 * @throws BizLogicException : BizLogicException
	 */
	private OrderItem populateOrderItemForArrayHeader(
			DefinedArrayRequestBean definedArrayRequestBean, OrderDetails order,
			Collection distributedItemCollection, Distribution distribution,
			RequestDetailsForm requestDetailsForm, Collection distributionObjectCollection)
			throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
				.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
		NewSpecimenArrayOrderItem orderItem = null;
		try
		{
			orderItem = orderBizLogic.retrieveNewSpecimenArrayOrderItem(Long
					.valueOf(definedArrayRequestBean.getOrderItemId()));
		}
		catch (final BizLogicException exception)
		{
			OrderDetails.logger.error("Not able to retrieve Order item."+
					exception.getMessage(), exception);
			exception.printStackTrace() ;
		}

		//Updating Description and Status.
		orderItem.setStatus(definedArrayRequestBean.getAssignedStatus());
		//Setting the order id
		orderItem.setOrderDetails(order);

		//For Distribution.
		if (("Distributed".equalsIgnoreCase(definedArrayRequestBean.getAssignedStatus().trim()) || "Distributed And Close"
				.equalsIgnoreCase(definedArrayRequestBean.getAssignedStatus().trim()))
				&& Constants.DOUBLE_QUOTES.equals(definedArrayRequestBean.getDistributedItemId()))
		{
			//Setting the Site for distribution.
			distribution = this.setSiteInDistribution(distribution, requestDetailsForm);

			final DistributedItem distributedItem = new DistributedItem();
			if (orderItem.getSpecimenArray() != null
					&& orderItem.getSpecimenArray().getId() != null)
			{
				distributedItem.setSpecimenArray(orderItem.getSpecimenArray());
			}
			/*SpecimenArray specimenArray = new SpecimenArray();
			if(definedArrayRequestBean.getArrayId() != null && !definedArrayRequestBean.getArrayId().
			trim().equalsIgnoreCase(""))
			{
				specimenArray.setId(new Long(definedArrayRequestBean.getArrayId()));
			}
			distributedItem.setSpecimenArray(specimenArray);*/

			//For setting assigned quantity in Distribution.
			distributedItem.setQuantity(new Double("1"));

			distribution = this.setDistributedItemCollectionInDistribution(orderItem,
					distributedItem, distribution, distributedItemCollection);

			//Setting the distribution protocol in Distribution.
			distribution = this.setDistributionProtocolInDistribution(distribution,
					requestDetailsForm);

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
	private OrderItem populateOrderItemForExistingArray(
			ExistingArrayDetailsBean existingArrayDetailsBean, OrderDetails order,
			Collection distributedItemCollection, Distribution distribution,
			RequestDetailsForm requestDetailsForm, Collection distributionObjectCollection)
	{
		final ExistingSpecimenArrayOrderItem existingSpecArrOrderItem = new ExistingSpecimenArrayOrderItem();
		//Updating Description and Status.

		existingSpecArrOrderItem.setId(Long.valueOf(existingArrayDetailsBean.getOrderItemId()));
		existingSpecArrOrderItem.setStatus(existingArrayDetailsBean.getAssignedStatus());
		existingSpecArrOrderItem.setDescription(existingArrayDetailsBean.getAddDescription());
		final SpecimenArray specimenArray = new SpecimenArray();
		if (existingArrayDetailsBean.getArrayId() != null
				&& !Constants.DOUBLE_QUOTES.equalsIgnoreCase(existingArrayDetailsBean.getArrayId()
						.trim()))
		{
			specimenArray.setId(Long.valueOf(existingArrayDetailsBean.getArrayId()));
		}
		specimenArray.setName(existingArrayDetailsBean.getBioSpecimenArrayName());
		existingSpecArrOrderItem.setSpecimenArray(specimenArray);
		//Setting the order id
		existingSpecArrOrderItem.setOrderDetails(order);

		//For Distribution.
		if (("Distributed".equalsIgnoreCase(existingArrayDetailsBean.getAssignedStatus().trim()) || "Distributed And Close"
				.equalsIgnoreCase(existingArrayDetailsBean.getAssignedStatus().trim()))
				&& Constants.DOUBLE_QUOTES.equals(existingArrayDetailsBean.getDistributedItemId()))
		{
			//Setting the Site for distribution.
			distribution = this.setSiteInDistribution(distribution, requestDetailsForm);
			//Making distributed items
			final DistributedItem distributedItem = new DistributedItem();
			distributedItem.setSpecimenArray(specimenArray);

			if (existingArrayDetailsBean.getRequestedQuantity().equals("0.0"))
			{
				distributedItem.setQuantity(new Double("1"));
			}
			else
			{
				distributedItem.setQuantity(new Double(existingArrayDetailsBean
						.getRequestedQuantity()));
			}
			distribution = this.setDistributedItemCollectionInDistribution(
					existingSpecArrOrderItem, distributedItem, distribution,
					distributedItemCollection);
			//Setting the distribution protocol in Distribution.
			distribution = this.setDistributionProtocolInDistribution(distribution,
					requestDetailsForm);
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
		catch (final Exception e)
		{
			OrderDetails.logger.error(e.getMessage(),e);
			e.printStackTrace() ;
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