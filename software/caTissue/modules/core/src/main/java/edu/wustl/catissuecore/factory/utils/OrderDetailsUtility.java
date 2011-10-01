package edu.wustl.catissuecore.factory.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

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
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.MapDataParser;


public class OrderDetailsUtility
{
	/*public static void updateOrderDetails(Boolean operationAdd, OrderDetails orderDetails)
	{
		orderDetails.setOperationAdd(operationAdd);
	}*/

	/**
	 * This function inserts order data to order table.
	 * @param abstractActionForm object
	 */
	private void insertOrderDetails(final AbstractActionForm abstractActionForm, final OrderDetails orderDetails)
	{
		Map orderItemsMap = null;
		Collection<OrderItem> orderItemsCollection = new LinkedHashSet<OrderItem>();
		List<NewSpecimenArrayOrderItem> newSpecimenArrayObjList = null;

		if (abstractActionForm.getPageOf().equals(Constants.SPECIMEN_ORDER_FORM_TYPE))
		{
			final OrderSpecimenForm orderSpecimenForm = (OrderSpecimenForm) abstractActionForm;
			orderItemsMap = putOrderDetailsForSpecimen(orderSpecimenForm,orderDetails);
			newSpecimenArrayObjList = this.putnewArrayDetailsforArray(orderSpecimenForm
					.getDefineArrayObj());
		}

		if (abstractActionForm.getPageOf().equals(Constants.ARRAY_ORDER_FORM_TYPE))
		{
			final OrderBiospecimenArrayForm orderBiospecimenArrayForm = (OrderBiospecimenArrayForm) abstractActionForm;
			orderItemsMap = putOrderDetailsForArray(orderBiospecimenArrayForm,orderDetails);
			newSpecimenArrayObjList = this.putnewArrayDetailsforArray(orderBiospecimenArrayForm
					.getDefineArrayObj());

		}
		if (abstractActionForm.getPageOf().equals(Constants.PATHOLOGYCASE_ORDER_FORM_TYPE))
		{
			final OrderPathologyCaseForm orderPathologyCaseForm = (OrderPathologyCaseForm) abstractActionForm;
			orderItemsMap = putOrderDetailsForPathologyCase(orderPathologyCaseForm,orderDetails);
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
			//OrderDetails.logger.error(e.getMessage(),e);
			e.printStackTrace();
		}

		final Collection<OrderItem> orderItemsSet = new LinkedHashSet<OrderItem>();

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
						final Iterator<NewSpecimenArrayOrderItem> iteratorNewSpecimen = newSpecimenArrayObjList.iterator();
						while (iteratorNewSpecimen.hasNext())
						{
							final NewSpecimenArrayOrderItem newSpecimenArrayObj = iteratorNewSpecimen
									.next();
							if (newSpecimenArrayObj.getName().equals(
									orderSpecimenBean.getArrayName()))
							{
								Collection<SpecimenOrderItem> orderItemCollection = newSpecimenArrayObj
										.getSpecimenOrderItemCollection();
								if (orderItemCollection == null)
								{
									orderItemCollection = new LinkedHashSet<SpecimenOrderItem>();
								}
								specimenOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayObj);
								orderItem = specimenOrderItem;
								//Test Line
								orderItemsSet.add(orderItem);
								orderItemCollection.add((SpecimenOrderItem)orderItem);
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
						final Iterator<NewSpecimenArrayOrderItem> iteratorNewSpecArrObj = newSpecimenArrayObjList.iterator();
						while (iteratorNewSpecArrObj.hasNext())
						{
							final NewSpecimenArrayOrderItem newSpecimenArrayObj = iteratorNewSpecArrObj
									.next();
							if (newSpecimenArrayObj.getName().equals(
									orderSpecimenBean.getArrayName()))
							{
								Collection<SpecimenOrderItem> orderItemCollection = newSpecimenArrayObj
										.getSpecimenOrderItemCollection();
								if (orderItemCollection == null)
								{
									orderItemCollection = new LinkedHashSet<SpecimenOrderItem>();
								}
								specimenOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayObj);
								orderItem = specimenOrderItem;
								//Test Line
								orderItemsSet.add(orderItem);
								orderItemCollection.add((SpecimenOrderItem)orderItem);
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
			final Iterator<NewSpecimenArrayOrderItem> iteratorNewSpecObj = newSpecimenArrayObjList.iterator();
			while (iteratorNewSpecObj.hasNext())
			{
				orderItem = iteratorNewSpecObj.next();
				orderItem.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
				orderItemsSet.add(orderItem);
			}
		}
		orderDetails.setOrderItemCollection(orderItemsSet);
	}


	/**
	 * @param orderBiospecimenArrayForm object
	 * @return HashMap object
	 */
	private Map putOrderDetailsForArray(OrderBiospecimenArrayForm orderBiospecimenArrayForm, OrderDetails orderDetails)
	{
		HashMap orderItemsMap = null;

		orderDetails.setComment(orderBiospecimenArrayForm.getOrderForm().getComments());
		orderDetails.setName(orderBiospecimenArrayForm.getOrderForm().getOrderRequestName());
		orderDetails.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
		orderDetails.setRequestedDate(new Date());
		final String protocolId = orderBiospecimenArrayForm.getOrderForm()
				.getDistributionProtocol();

		if (protocolId != null && !protocolId.equals(""))
		{
			final Long distributionId = Long.valueOf(orderBiospecimenArrayForm.getOrderForm()
					.getDistributionProtocol());
			InstanceFactory<DistributionProtocol> instFact = DomainInstanceFactory.getInstanceFactory(DistributionProtocol.class);
			final DistributionProtocol distributionProtocolObj =instFact.createObject();// new DistributionProtocol();
			distributionProtocolObj.setId(distributionId);
			orderDetails.setDistributionProtocol(distributionProtocolObj);
		}

		orderItemsMap = (HashMap) orderBiospecimenArrayForm.getValues();
		return orderItemsMap;
	}

	/**
	 * @param orderSpecimenForm object
	 * @return HashMap object
	 */
	private Map putOrderDetailsForSpecimen(OrderSpecimenForm orderSpecimenForm,OrderDetails orderDetails)
	{
		//IBizLogic defaultBizLogic = BizLogicFactory.getInstance().getBizLogic(-1);
		orderDetails.setComment(orderSpecimenForm.getOrderForm().getComments());
		orderDetails.setName(orderSpecimenForm.getOrderForm().getOrderRequestName());
		orderDetails.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
		orderDetails.setRequestedDate(new Date());
		final String protocolId = orderSpecimenForm.getOrderForm().getDistributionProtocol();

		if (protocolId != null && !protocolId.equals(""))
		{
			final Long distributionId = Long.valueOf(orderSpecimenForm.getOrderForm()
					.getDistributionProtocol());
			InstanceFactory<DistributionProtocol> instFact = DomainInstanceFactory.getInstanceFactory(DistributionProtocol.class);
			final DistributionProtocol distributionProtocolObj = instFact.createObject();//new DistributionProtocol();
			distributionProtocolObj.setId(distributionId);
			orderDetails.setDistributionProtocol(distributionProtocolObj);
		}

		final Map orderItemsMap = orderSpecimenForm.getValues();
		return orderItemsMap;
	}

	/**
	 * @param orderPathologyCaseForm object
	 * @return HashMap object
	 */
	private Map putOrderDetailsForPathologyCase(OrderPathologyCaseForm orderPathologyCaseForm, OrderDetails orderDetails)
	{
		HashMap orderItemsMap = null;

		orderDetails.setComment(orderPathologyCaseForm.getOrderForm().getComments());
		orderDetails.setName(orderPathologyCaseForm.getOrderForm().getOrderRequestName());
		orderDetails.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
		orderDetails.setRequestedDate(new Date());
		final String protocolId = orderPathologyCaseForm.getOrderForm().getDistributionProtocol();

		if (protocolId != null && !protocolId.equals(""))
		{
			final Long distributionId = Long.valueOf(orderPathologyCaseForm.getOrderForm()
					.getDistributionProtocol());
			InstanceFactory<DistributionProtocol> instFact = DomainInstanceFactory.getInstanceFactory(DistributionProtocol.class);
			final DistributionProtocol distributionProtocolObj = instFact.createObject();//new DistributionProtocol();
			distributionProtocolObj.setId(distributionId);
			orderDetails.setDistributionProtocol(distributionProtocolObj);
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
		InstanceFactory<SpecimenArray> spArrInstFact = DomainInstanceFactory.getInstanceFactory(SpecimenArray.class);
		final SpecimenArray specimenArray = spArrInstFact.createObject();//new SpecimenArray();
		specimenArray.setId(Long.valueOf(orderSpecimenBean.getSpecimenId()));

		InstanceFactory<ExistingSpecimenArrayOrderItem> instFact = DomainInstanceFactory.getInstanceFactory(ExistingSpecimenArrayOrderItem.class);
		ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = instFact.createObject();//new ExistingSpecimenArrayOrderItem();
		existingSpecimenArrayOrderItem.setSpecimenArray(specimenArray);
		orderItem = existingSpecimenArrayOrderItem;
		existingSpecimenArrayOrderItem = null;

		return orderItem;
	}

	/**
	 * @param newArrayOrderItems object
	 * @return List object
	 */
	private List<NewSpecimenArrayOrderItem> putnewArrayDetailsforArray(List newArrayOrderItems)
	{
		List<NewSpecimenArrayOrderItem> newOrderItems = null;
		if (newArrayOrderItems != null)
		{
			newOrderItems = new ArrayList<NewSpecimenArrayOrderItem>();
			final Iterator orderArrayItemsCollectionItr = newArrayOrderItems.iterator();

			while (orderArrayItemsCollectionItr.hasNext())
			{
				final DefineArrayForm defineArrayObj = (DefineArrayForm) orderArrayItemsCollectionItr
						.next();
				InstanceFactory<NewSpecimenArrayOrderItem> instFact = DomainInstanceFactory.getInstanceFactory(NewSpecimenArrayOrderItem.class);
				final NewSpecimenArrayOrderItem newSpecimenArrayOrderItem =instFact.createObject();// new NewSpecimenArrayOrderItem();
				newSpecimenArrayOrderItem.setName(defineArrayObj.getArrayName());

				InstanceFactory<SpecimenArrayType> spArrInstFact = DomainInstanceFactory.getInstanceFactory(SpecimenArrayType.class);
				final SpecimenArrayType specimenArrayTypeObj =spArrInstFact.createObject();// new SpecimenArrayType();

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
	private void updateOrderDetails(final AbstractActionForm abstractActionForm,final OrderDetails orderDetails) throws BizLogicException
	{
		final RequestDetailsForm requestDetailsForm = (RequestDetailsForm) abstractActionForm;
		/*if (requestDetailsForm.getMailNotification() != null
				&& requestDetailsForm.getMailNotification().booleanValue() == Boolean.TRUE)
		{
			orderDetails.setMailNotification(requestDetailsForm.getMailNotification());
		}*/
		//Setting Comments in Order Domain Obj.
		orderDetails.setComment(requestDetailsForm.getAdministratorComments());
		//Setting the order Id.
		final Long orderId = Long.valueOf(requestDetailsForm.getId());
		orderDetails.setId(orderId);

		if (requestDetailsForm.getIsDirectDistribution() != null
				&& requestDetailsForm.getIsDirectDistribution().booleanValue() == Boolean.TRUE)
		{
			orderDetails.setName(requestDetailsForm.getOrderName());
			this.setDistributionProtocol(requestDetailsForm.getDistributionProtocolId(),orderDetails);
		}

		final Collection beanObjSet = this.parseValuesMap(requestDetailsForm.getValues());
		final Iterator iter = beanObjSet.iterator();
		final Collection<OrderItem> domainObjSet = new HashSet<OrderItem>();
		final Collection<Distribution> distributionObjectCollection = new HashSet<Distribution>();
		final Distribution distribution = (Distribution)DomainInstanceFactory.getInstanceFactory(Distribution.class).createObject();//new Distribution();
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
		orderDetails.setDistributionCollection(distributionObjectCollection);
		orderDetails.setOrderItemCollection(domainObjSet);
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
			OrderDetails order, Collection<DistributedItem> distributedItemCollection, Distribution distribution,
			RequestDetailsForm requestDetailsForm, Collection<Distribution> distributionObjectCollection)
			throws BizLogicException
	{
		InstanceFactory<OrderItem> orderInstFact = DomainInstanceFactory.getInstanceFactory(OrderItem.class);
		OrderItem orderItem = orderInstFact.createObject();// new OrderItem();
		// modified by pratha for bug# 5663	and 7355
		DerivedSpecimenOrderItem derivedOrderItem = null;
		ExistingSpecimenOrderItem existingOrderItem = null;
		PathologicalCaseOrderItem pathologicalCaseOrderItem = null;
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
				.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
		InstanceFactory<Specimen> specimenInstFact= DomainInstanceFactory.getInstanceFactory(Specimen.class);
		final Specimen specimen = specimenInstFact.createObject();//new Specimen();
		if (requestDetailsBean.getInstanceOf().trim().equalsIgnoreCase("Existing"))
		{
			InstanceFactory<ExistingSpecimenOrderItem> existSpecOrderItemInstFact = DomainInstanceFactory.getInstanceFactory(ExistingSpecimenOrderItem.class);
			existingOrderItem = existSpecOrderItemInstFact.createObject();//new ExistingSpecimenOrderItem();
			specimen.setId(Long.valueOf(requestDetailsBean.getSpecimenId()));
			specimen.setLabel(requestDetailsBean.getRequestedItem());
			existingOrderItem.setSpecimen(specimen);
			orderItem = existingOrderItem;

		}
		else if ("DerivedPathological".equalsIgnoreCase(requestDetailsBean.getInstanceOf().trim())
				|| "Pathological".equalsIgnoreCase(requestDetailsBean.getInstanceOf().trim()))
		{
			InstanceFactory<PathologicalCaseOrderItem> instFact = DomainInstanceFactory.getInstanceFactory(PathologicalCaseOrderItem.class);
			pathologicalCaseOrderItem = instFact.createObject();//new PathologicalCaseOrderItem();
			SpecimenCollectionGroup specimenCollectionGroup = null;
			try
			{
				specimenCollectionGroup = orderBizLogic.retrieveSCG(Long
						.parseLong(requestDetailsBean.getSpecimenCollGroupId()));
			}
			catch (final BizLogicException exception)
			{
				/*OrderDetails.logger.error("Not able to retrieve" +
						" SpecimenCollectionGroup."
						+exception.getMessage(), exception);*/
				exception.printStackTrace();
			}
			pathologicalCaseOrderItem.setSpecimenCollectionGroup(specimenCollectionGroup);
			pathologicalCaseOrderItem.setSpecimenClass(requestDetailsBean.getClassName());
			pathologicalCaseOrderItem.setSpecimenType(requestDetailsBean.getType());
			orderItem = pathologicalCaseOrderItem;
		}
		else
		{
			InstanceFactory<DerivedSpecimenOrderItem> derSpecOrderInstFact = DomainInstanceFactory.getInstanceFactory(DerivedSpecimenOrderItem.class);
			derivedOrderItem = derSpecOrderInstFact.createObject();//new DerivedSpecimenOrderItem();
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
			final DistributedItem distributedItem = (DistributedItem)DomainInstanceFactory.getInstanceFactory(DistributedItem.class).createObject();//new DistributedItem();

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
			/*OrderDetails.logger.error("Not able to retrieve" +
					" SpecimenCollectionGroup."
					+exception.getMessage(), exception);*/
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
			final DistributedItem distributedItem = (DistributedItem)DomainInstanceFactory.getInstanceFactory(DistributedItem.class).createObject();//new DistributedItem();
			final Specimen specimen = (Specimen)DomainInstanceFactory.getInstanceFactory(Specimen.class).createObject();
			/*
				InstanceFactory<Specimen> instFact= DomainInstanceFactory.getInstanceFactory(Specimen.class);
			  	final Specimen specimen = instFact.createObject();//new Specimen();
			*/
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
		InstanceFactory<OrderDetails> orderDetailsInstFact = DomainInstanceFactory.getInstanceFactory(OrderDetails.class);
		final OrderDetails order = orderDetailsInstFact.createObject();//new OrderDetails();
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
			Collection<DistributedItem> distributedItemCollection, Distribution distribution,
			RequestDetailsForm requestDetailsForm, Collection<Distribution> distributionObjectCollection)
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
			/*OrderDetails.logger.error("Not able to retrieve Order item."+
					exception.getMessage(), exception);*/
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

			final DistributedItem distributedItem = (DistributedItem)DomainInstanceFactory.getInstanceFactory(DistributedItem.class).createObject();//new DistributedItem();
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
			Collection<DistributedItem> distributedItemCollection, Distribution distribution,
			RequestDetailsForm requestDetailsForm, Collection<Distribution> distributionObjectCollection)
	{
		InstanceFactory<ExistingSpecimenArrayOrderItem> instFact = DomainInstanceFactory.getInstanceFactory(ExistingSpecimenArrayOrderItem.class);
		final ExistingSpecimenArrayOrderItem existingSpecArrOrderItem =instFact.createObject();// new ExistingSpecimenArrayOrderItem();
		//Updating Description and Status.

		existingSpecArrOrderItem.setId(Long.valueOf(existingArrayDetailsBean.getOrderItemId()));
		existingSpecArrOrderItem.setStatus(existingArrayDetailsBean.getAssignedStatus());
		existingSpecArrOrderItem.setDescription(existingArrayDetailsBean.getAddDescription());
		InstanceFactory<SpecimenArray> spArrInstFact = DomainInstanceFactory.getInstanceFactory(SpecimenArray.class);
		final SpecimenArray specimenArray =spArrInstFact.createObject();// new SpecimenArray();
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
			final DistributedItem distributedItem = (DistributedItem)DomainInstanceFactory.getInstanceFactory(DistributedItem.class).createObject();//new DistributedItem();
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
	 * @param distribution object
	 * @param requestDetailsForm object
	 * @return Distribution object
	 */
	private Distribution setDistributionProtocolInDistribution(Distribution distribution,
			RequestDetailsForm requestDetailsForm)
	{
		InstanceFactory<DistributionProtocol> instFact = DomainInstanceFactory.getInstanceFactory(DistributionProtocol.class);
		final DistributionProtocol distributionProtocol = instFact.createObject();//new DistributionProtocol();
		distributionProtocol.setId(Long.valueOf(requestDetailsForm.getDistributionProtocolId()));
		distribution.setDistributionProtocol(distributionProtocol);
		return distribution;
	}


	/**
	 * @param distribution object
	 * @param requestDetailsForm object
	 * @return Distribution object
	 */
	private Distribution setSiteInDistribution(Distribution distribution,
			RequestDetailsForm requestDetailsForm)
	{
		InstanceFactory<Site> instFact = DomainInstanceFactory.getInstanceFactory(Site.class);
		final Site toSite = instFact.createObject();
		toSite.setId(Long.valueOf(requestDetailsForm.getSite()));
		distribution.setToSite(toSite);
		return distribution;
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
			//OrderDetails.logger.error(e.getMessage(),e);
			e.printStackTrace() ;
		}
		return beanObjSet;
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
		InstanceFactory<ExistingSpecimenOrderItem> existSpecOrderItemInstFact = DomainInstanceFactory.getInstanceFactory(ExistingSpecimenOrderItem.class);
		final ExistingSpecimenOrderItem existingOrderItem = existSpecOrderItemInstFact.createObject();//new ExistingSpecimenOrderItem();
		//Set Parent specimen
		InstanceFactory<Specimen> instFact= DomainInstanceFactory.getInstanceFactory(Specimen.class);
		final Specimen specimen = instFact.createObject();//new Specimen();
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
		InstanceFactory<DerivedSpecimenOrderItem> derSpecOrderInstFact = DomainInstanceFactory.getInstanceFactory(DerivedSpecimenOrderItem.class);
		final DerivedSpecimenOrderItem derivedSpecimenOrderItem = derSpecOrderInstFact.createObject();//new DerivedSpecimenOrderItem();
		//Set Parent specimen
		InstanceFactory<Specimen> instFact= DomainInstanceFactory.getInstanceFactory(Specimen.class);
		final Specimen specimen =instFact.createObject();// new Specimen();
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
		InstanceFactory<PathologicalCaseOrderItem> instFact = DomainInstanceFactory.getInstanceFactory(PathologicalCaseOrderItem.class);
		final PathologicalCaseOrderItem existingOrderItem = instFact.createObject();//new PathologicalCaseOrderItem();
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
		InstanceFactory<PathologicalCaseOrderItem> instFact = DomainInstanceFactory.getInstanceFactory(PathologicalCaseOrderItem.class);
		final PathologicalCaseOrderItem derivedOrderItem = instFact.createObject();//new PathologicalCaseOrderItem();
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
	 * @param distributionProtId : distributionProtId
	 */
	private void setDistributionProtocol(String distributionProtId,OrderDetails orderDetails)
	{

		try
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();

			final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
					.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
			final DistributionProtocol distributionProtocol = orderBizLogic
					.retrieveDistributionProtocol(distributionProtId);
			orderDetails.setDistributionProtocol(distributionProtocol);
		}
		catch (final BizLogicException e)
		{
			//OrderDetails.logger.error(e.getMessage(),e);
			e.printStackTrace();
		}

	}


}
