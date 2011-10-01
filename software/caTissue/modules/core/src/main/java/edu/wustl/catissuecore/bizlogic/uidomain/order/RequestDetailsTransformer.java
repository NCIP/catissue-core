package edu.wustl.catissuecore.bizlogic.uidomain.order;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.RequestDetailsForm;
import edu.wustl.catissuecore.bean.DefinedArrayDetailsBean;
import edu.wustl.catissuecore.bean.DefinedArrayRequestBean;
import edu.wustl.catissuecore.bean.ExistingArrayDetailsBean;
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
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;

/*
 * TODO Check if can Refactor methods here..
 */
@InputUIRepOfDomain(RequestDetailsForm.class)
public class RequestDetailsTransformer extends AbstractOrderDetailsTransformer<RequestDetailsForm> {
    private static Logger logger = Logger.getCommonLogger(RequestDetailsTransformer.class);

    @Override
    public void overwriteDomainObject(OrderDetails domainObject, RequestDetailsForm requestDetailsForm) {
        if (requestDetailsForm.isAddOperation()) {
            throw new IllegalArgumentException("ADD RequestDetailsForm ??");
        }
        try {
            updateOrderDetails(domainObject, requestDetailsForm);
        } catch (final BizLogicException exp) {
            RequestDetailsTransformer.logger.error(exp.getMessage(), exp);
            exp.printStackTrace();
        }
    }

    /**
     * Update the Order Details.
     *
     * @param abstractActionForm object.
     * @throws BizLogicException : BizLogicException
     */
    private void updateOrderDetails(OrderDetails domainObject, AbstractActionForm abstractActionForm)
            throws BizLogicException {
        final RequestDetailsForm requestDetailsForm = (RequestDetailsForm) abstractActionForm;
       /* if (requestDetailsForm.getMailNotification() != null
                && requestDetailsForm.getMailNotification().booleanValue() == Boolean.TRUE) {
            domainObject.setMailNotification(requestDetailsForm.getMailNotification());
        }*/
        // Setting Comments in Order Domain Obj.
        domainObject.setComment(requestDetailsForm.getAdministratorComments());
        // Setting the order Id.
        final Long orderId = Long.valueOf(requestDetailsForm.getId());
        domainObject.setId(orderId);
        try {
			domainObject.setRequestedDate(CommonUtilities.parseDate(requestDetailsForm.getRequestedDate(), CommonUtilities
					.datePattern(requestDetailsForm.getRequestedDate())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (requestDetailsForm.getIsDirectDistribution() != null
                && requestDetailsForm.getIsDirectDistribution().booleanValue() == Boolean.TRUE) {
            domainObject.setName(requestDetailsForm.getOrderName());
            setDistributionProtocol(domainObject, requestDetailsForm.getDistributionProtocolId());
            requestDetailsForm.setDistributionProtocolId(domainObject.getDistributionProtocol().getId().toString());
        }

        final Collection beanObjSet = parseValuesMap(requestDetailsForm.getValues());
        final Iterator iter = beanObjSet.iterator();
        final Collection<OrderItem> domainObjSet = new HashSet<OrderItem>();
        final Collection distributionObjectCollection = new HashSet();
        final Distribution distribution = (Distribution)DomainInstanceFactory.getInstanceFactory(Distribution.class).createObject();//new Distribution();
        // set by pratha
        distribution.setComment(requestDetailsForm.getAdministratorComments());
        final Collection<DistributedItem> distributedItemCollection = new HashSet<DistributedItem>();
        while (iter.hasNext()) {
            // Setting the Order Id
            final OrderDetails order = setOrderId(orderId);
            final Object obj = iter.next();
            // For specimen order item.
            if (obj instanceof RequestDetailsBean) {
                final RequestDetailsBean requestDetailsBean = (RequestDetailsBean) obj;
                // For skipping iteration when status drop down is disabled.
                if (requestDetailsBean.getAssignedStatus() == null
                        || Constants.DOUBLE_QUOTES.equalsIgnoreCase(requestDetailsBean.getAssignedStatus().trim())) {
                    continue;
                }
                final OrderItem orderItem = populateOrderItemForSpecimenOrderItems(requestDetailsBean, order,
                        distributedItemCollection, distribution, requestDetailsForm, distributionObjectCollection);
                domainObjSet.add(orderItem);
            }
            // For defined array header object.
            else if (obj instanceof DefinedArrayRequestBean) {
                final DefinedArrayRequestBean definedArrayRequestBean = (DefinedArrayRequestBean) obj;
                // For skipping iteration when status drop down is disabled.
                if (definedArrayRequestBean.getAssignedStatus() == null
                        || Constants.DOUBLE_QUOTES.equalsIgnoreCase(definedArrayRequestBean.getAssignedStatus().trim())) {
                    continue;
                }
                final OrderItem orderItem = populateOrderItemForArrayHeader(definedArrayRequestBean, order,
                        distributedItemCollection, distribution, requestDetailsForm, distributionObjectCollection);
                domainObjSet.add(orderItem);
            }
            // For defined array details.
            else if (obj instanceof DefinedArrayDetailsBean) {
                final DefinedArrayDetailsBean definedArrayDetailsBean = (DefinedArrayDetailsBean) obj;
                // For skipping iteration when status drop down is disabled.
                if (definedArrayDetailsBean.getAssignedStatus() == null
                        || Constants.DOUBLE_QUOTES.equalsIgnoreCase(definedArrayDetailsBean.getAssignedStatus().trim())) {
                    continue;
                }
                final OrderItem orderItem = setOrderForDefineArrayDetails(order, definedArrayDetailsBean);
                domainObjSet.add(orderItem);
            }
            // For Existing array order item.
            else if (obj instanceof ExistingArrayDetailsBean) {
                final ExistingArrayDetailsBean existingArrayDetailsBean = (ExistingArrayDetailsBean) obj;
                // For skipping iteration when status drop down is disabled.
                if (existingArrayDetailsBean.getAssignedStatus() == null
                        || Constants.DOUBLE_QUOTES
                                .equalsIgnoreCase(existingArrayDetailsBean.getAssignedStatus().trim())) {
                    continue;
                }
                final OrderItem orderItem = populateOrderItemForExistingArray(existingArrayDetailsBean, order,
                        distributedItemCollection, distribution, requestDetailsForm, distributionObjectCollection);
                domainObjSet.add(orderItem);
            }
        }
        domainObject.setDistributionCollection(distributionObjectCollection);
        domainObject.setOrderItemCollection(domainObjSet);
    }

    /**
     * @param distributionProtId : distributionProtId
     */
    private void setDistributionProtocol(OrderDetails domainObject, String distributionProtId) {

        try {
            final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();

            final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
                    .getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
            final DistributionProtocol distributionProtocol = orderBizLogic
                    .retrieveDistributionProtocol(distributionProtId);
            domainObject.setDistributionProtocol(distributionProtocol);
        } catch (final BizLogicException e) {
            RequestDetailsTransformer.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

    }

    /**
     * @param map object
     * @return Collection object
     */
    private Collection parseValuesMap(Map map) {
        final MapDataParser mapDataParser = new MapDataParser("edu.wustl.catissuecore.bean");
        Collection beanObjSet = null;
        try {
            beanObjSet = mapDataParser.generateData(map);
        } catch (final Exception e) {
            RequestDetailsTransformer.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return beanObjSet;
    }

    /**
     * @param orderId object
     * @return OrderDetails object
     */
    private OrderDetails setOrderId(Long orderId) {
    	InstanceFactory<OrderDetails> orderDetailsInstFact = DomainInstanceFactory.getInstanceFactory(OrderDetails.class);
        final OrderDetails order = orderDetailsInstFact.createObject();//new OrderDetails();
        order.setId(orderId);
        return order;
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
    private OrderItem populateOrderItemForSpecimenOrderItems(RequestDetailsBean requestDetailsBean, OrderDetails order,
            Collection<DistributedItem> distributedItemCollection, Distribution distribution, RequestDetailsForm requestDetailsForm,
            Collection distributionObjectCollection) throws BizLogicException {

    	InstanceFactory<OrderItem> orderInstFact = DomainInstanceFactory.getInstanceFactory(OrderItem.class);
        OrderItem orderItem =orderInstFact.createObject();// new OrderItem();
        // modified by pratha for bug# 5663 and 7355
        DerivedSpecimenOrderItem derivedOrderItem = null;
        ExistingSpecimenOrderItem existingOrderItem = null;
        PathologicalCaseOrderItem pathologicalCaseOrderItem = null;
        final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
        final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
                .getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
        InstanceFactory<Specimen> specimenInstFact= DomainInstanceFactory.getInstanceFactory(Specimen.class);
        final Specimen specimen = specimenInstFact.createObject();//new Specimen();
        if (requestDetailsBean.getInstanceOf().trim().equalsIgnoreCase("Existing")) {
        	InstanceFactory<ExistingSpecimenOrderItem> existSpecOrderItemInstFact = DomainInstanceFactory.getInstanceFactory(ExistingSpecimenOrderItem.class);
            existingOrderItem = existSpecOrderItemInstFact.createObject();//new ExistingSpecimenOrderItem();
            specimen.setId(Long.valueOf(requestDetailsBean.getSpecimenId()));
            specimen.setLabel(requestDetailsBean.getRequestedItem());
            existingOrderItem.setSpecimen(specimen);
            orderItem = existingOrderItem;

        } else if ("DerivedPathological".equalsIgnoreCase(requestDetailsBean.getInstanceOf().trim())
                || "Pathological".equalsIgnoreCase(requestDetailsBean.getInstanceOf().trim())) {
        	InstanceFactory<PathologicalCaseOrderItem> instFact = DomainInstanceFactory.getInstanceFactory(PathologicalCaseOrderItem.class);
            pathologicalCaseOrderItem = instFact.createObject();//new PathologicalCaseOrderItem();
            SpecimenCollectionGroup specimenCollectionGroup = null;
            try {
                specimenCollectionGroup = orderBizLogic.retrieveSCG(Long.parseLong(requestDetailsBean
                        .getSpecimenCollGroupId()));
            } catch (final BizLogicException exception) {
                RequestDetailsTransformer.logger.error("Not able to retrieve" + " SpecimenCollectionGroup."
                        + exception.getMessage(), exception);
                exception.printStackTrace();
            }
            pathologicalCaseOrderItem.setSpecimenCollectionGroup(specimenCollectionGroup);
            pathologicalCaseOrderItem.setSpecimenClass(requestDetailsBean.getClassName());
            pathologicalCaseOrderItem.setSpecimenType(requestDetailsBean.getType());
            orderItem = pathologicalCaseOrderItem;
        } else {
        	InstanceFactory<DerivedSpecimenOrderItem> derSpecOrderInstFact = DomainInstanceFactory.getInstanceFactory(DerivedSpecimenOrderItem.class);
            derivedOrderItem = derSpecOrderInstFact.createObject();//new DerivedSpecimenOrderItem();
            specimen.setId(Long.valueOf(requestDetailsBean.getSpecimenId()));
            specimen.setLabel(requestDetailsBean.getRequestedItem());
            derivedOrderItem.setParentSpecimen(specimen);

            derivedOrderItem.setSpecimenClass(requestDetailsBean.getClassName());
            derivedOrderItem.setSpecimenType(requestDetailsBean.getType());
            orderItem = derivedOrderItem;
        }

        // For Distribution.
        if ("Distributed".equalsIgnoreCase(requestDetailsBean.getAssignedStatus().trim())
                && Constants.DOUBLE_QUOTES.equals(requestDetailsBean.getDistributedItemId())
                || ("Distributed And Close".equalsIgnoreCase(requestDetailsBean.getAssignedStatus().trim()) && Constants.DOUBLE_QUOTES
                        .equals(requestDetailsBean.getDistributedItemId()))) {
            // Setting the Site for distribution.
            distribution = setSiteInDistribution(distribution, requestDetailsForm);
            final DistributedItem distributedItem = (DistributedItem)DomainInstanceFactory.getInstanceFactory(DistributedItem.class).createObject();//new DistributedItem();

            specimen.setId(Long.valueOf(requestDetailsBean.getRequestFor()));

            distributedItem.setSpecimen(specimen);
            // For setting assigned quantity in Distribution.
            if (requestDetailsBean.getRequestedQty() != null
                    && !Constants.DOUBLE_QUOTES.equalsIgnoreCase(requestDetailsBean.getRequestedQty().trim())) {
                distributedItem.setQuantity(new Double(requestDetailsBean.getRequestedQty()));
            }

            distribution = setDistributedItemCollectionInDistribution(orderItem, distributedItem, distribution,
                    distributedItemCollection);

            // Setting the distribution protocol in Distribution.
            distribution = setDistributionProtocolInDistribution(distribution, requestDetailsForm);

            distributionObjectCollection.add(distribution);
        }
        // Updating Description and Status.
        orderItem.setId(Long.valueOf(requestDetailsBean.getOrderItemId()));
        orderItem.setStatus(requestDetailsBean.getAssignedStatus());
        orderItem.setDescription(requestDetailsBean.getDescription());
        // Setting the order id
        orderItem.setOrderDetails(order);

        return orderItem;
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
    private OrderItem populateOrderItemForArrayHeader(DefinedArrayRequestBean definedArrayRequestBean,
            OrderDetails order, Collection distributedItemCollection, Distribution distribution,
            RequestDetailsForm requestDetailsForm, Collection distributionObjectCollection) throws BizLogicException {
        final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
        final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
                .getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
        NewSpecimenArrayOrderItem orderItem = null;
        try {
            orderItem = orderBizLogic.retrieveNewSpecimenArrayOrderItem(Long.valueOf(definedArrayRequestBean
                    .getOrderItemId()));
        } catch (final BizLogicException exception) {
            RequestDetailsTransformer.logger.error("Not able to retrieve Order item." + exception.getMessage(),
                    exception);
            exception.printStackTrace();
        }

        // Updating Description and Status.
        orderItem.setStatus(definedArrayRequestBean.getAssignedStatus());
        // Setting the order id
        orderItem.setOrderDetails(order);

        // For Distribution.
        if (("Distributed".equalsIgnoreCase(definedArrayRequestBean.getAssignedStatus().trim()) || "Distributed And Close"
                .equalsIgnoreCase(definedArrayRequestBean.getAssignedStatus().trim()))
                && Constants.DOUBLE_QUOTES.equals(definedArrayRequestBean.getDistributedItemId())) {
            // Setting the Site for distribution.
            distribution = setSiteInDistribution(distribution, requestDetailsForm);

            final DistributedItem distributedItem = (DistributedItem)DomainInstanceFactory.getInstanceFactory(DistributedItem.class).createObject();//new DistributedItem();
            if (orderItem.getSpecimenArray() != null && orderItem.getSpecimenArray().getId() != null) {
                distributedItem.setSpecimenArray(orderItem.getSpecimenArray());
            }
            /*
             * SpecimenArray specimenArray = new SpecimenArray();
             * if(definedArrayRequestBean.getArrayId() != null &&
             * !definedArrayRequestBean.getArrayId().
             * trim().equalsIgnoreCase("")) { specimenArray.setId(new
             * Long(definedArrayRequestBean.getArrayId())); }
             * distributedItem.setSpecimenArray(specimenArray);
             */

            // For setting assigned quantity in Distribution.
            distributedItem.setQuantity(new Double("1"));

            distribution = setDistributedItemCollectionInDistribution(orderItem, distributedItem, distribution,
                    distributedItemCollection);

            // Setting the distribution protocol in Distribution.
            distribution = setDistributionProtocolInDistribution(distribution, requestDetailsForm);

            distributionObjectCollection.add(distribution);
        }
        return orderItem;
    }

    /**
     * @param order object
     * @param definedArrayDetailsBean object
     * @return OrderItem object
     * @throws BizLogicException : BizLogicException
     */

    private OrderItem setOrderForDefineArrayDetails(OrderDetails order, DefinedArrayDetailsBean definedArrayDetailsBean)
            throws BizLogicException {
        final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
        final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
                .getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);

        SpecimenOrderItem specOrderItem = null;
        OrderItem orderItem = null;
        ExistingSpecimenOrderItem existingSpecimenOrderItem = null;
        DerivedSpecimenOrderItem derivedSpecimenOrderItem = null;
        PathologicalCaseOrderItem pathologicalCaseOrderItem = null;
        try {
            specOrderItem = orderBizLogic.retrieveSpecimenOrderItem(Long.parseLong(definedArrayDetailsBean
                    .getOrderItemId()));
        } catch (final BizLogicException exception) {
            RequestDetailsTransformer.logger.error("Not able to retrieve" + " SpecimenCollectionGroup."
                    + exception.getMessage(), exception);
            exception.printStackTrace();
        }

        if (specOrderItem instanceof ExistingSpecimenOrderItem) {
            existingSpecimenOrderItem = (ExistingSpecimenOrderItem) specOrderItem;
            orderItem = existingSpecimenOrderItem;
        } else if (specOrderItem instanceof DerivedSpecimenOrderItem) {// For
            // derived
            // specimen.
            derivedSpecimenOrderItem = (DerivedSpecimenOrderItem) specOrderItem;
            orderItem = derivedSpecimenOrderItem;
        } else {
            pathologicalCaseOrderItem = (PathologicalCaseOrderItem) specOrderItem;
            orderItem = pathologicalCaseOrderItem;
        }

        // For READY FOR ARRAY PREPARATION
        if (Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION.equalsIgnoreCase(definedArrayDetailsBean
                .getAssignedStatus().trim())) {
            final DistributedItem distributedItem = (DistributedItem)DomainInstanceFactory.getInstanceFactory(DistributedItem.class).createObject();//new DistributedItem();
            InstanceFactory<Specimen> instFact= DomainInstanceFactory.getInstanceFactory(Specimen.class);
            final Specimen specimen = instFact.createObject();//new Specimen();

            if (definedArrayDetailsBean.getRequestFor() == null
                    || Constants.DOUBLE_QUOTES.equalsIgnoreCase(definedArrayDetailsBean.getRequestFor().trim())) {
                // for existing Specimen.
                if (definedArrayDetailsBean.getInstanceOf().trim().equalsIgnoreCase("Existing")) {
                    specimen.setId(Long.valueOf(definedArrayDetailsBean.getSpecimenId()));
                    existingSpecimenOrderItem.setSpecimen(specimen);
                } else {// For derived specimen.
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
     * @param existingArrayDetailsBean object
     * @param order object
     * @param distributedItemCollection object
     * @param distribution object
     * @param requestDetailsForm object
     * @param distributionObjectCollection object
     * @return OrderItem object
     */
    private OrderItem populateOrderItemForExistingArray(ExistingArrayDetailsBean existingArrayDetailsBean,
            OrderDetails order, Collection distributedItemCollection, Distribution distribution,
            RequestDetailsForm requestDetailsForm, Collection distributionObjectCollection) {
    	InstanceFactory<ExistingSpecimenArrayOrderItem> instFact = DomainInstanceFactory.getInstanceFactory(ExistingSpecimenArrayOrderItem.class);
        final ExistingSpecimenArrayOrderItem existingSpecArrOrderItem = instFact.createObject();//new ExistingSpecimenArrayOrderItem();
        // Updating Description and Status.

        existingSpecArrOrderItem.setId(Long.valueOf(existingArrayDetailsBean.getOrderItemId()));
        existingSpecArrOrderItem.setStatus(existingArrayDetailsBean.getAssignedStatus());
        existingSpecArrOrderItem.setDescription(existingArrayDetailsBean.getAddDescription());
        InstanceFactory<SpecimenArray> spArrInstFact = DomainInstanceFactory.getInstanceFactory(SpecimenArray.class);
        final SpecimenArray specimenArray = spArrInstFact.createObject();//new SpecimenArray();
        if (existingArrayDetailsBean.getArrayId() != null
                && !Constants.DOUBLE_QUOTES.equalsIgnoreCase(existingArrayDetailsBean.getArrayId().trim())) {
            specimenArray.setId(Long.valueOf(existingArrayDetailsBean.getArrayId()));
        }
        specimenArray.setName(existingArrayDetailsBean.getBioSpecimenArrayName());
        existingSpecArrOrderItem.setSpecimenArray(specimenArray);
        // Setting the order id
        existingSpecArrOrderItem.setOrderDetails(order);

        // For Distribution.
        if (("Distributed".equalsIgnoreCase(existingArrayDetailsBean.getAssignedStatus().trim()) || "Distributed And Close"
                .equalsIgnoreCase(existingArrayDetailsBean.getAssignedStatus().trim()))
                && Constants.DOUBLE_QUOTES.equals(existingArrayDetailsBean.getDistributedItemId())) {
            // Setting the Site for distribution.
            distribution = setSiteInDistribution(distribution, requestDetailsForm);
            // Making distributed items
            final DistributedItem distributedItem = (DistributedItem)DomainInstanceFactory.getInstanceFactory(DistributedItem.class).createObject();//new DistributedItem();
            distributedItem.setSpecimenArray(specimenArray);

            if (existingArrayDetailsBean.getRequestedQuantity().equals("0.0")) {
                distributedItem.setQuantity(new Double("1"));
            } else {
                distributedItem.setQuantity(new Double(existingArrayDetailsBean.getRequestedQuantity()));
            }
            distribution = setDistributedItemCollectionInDistribution(existingSpecArrOrderItem, distributedItem,
                    distribution, distributedItemCollection);
            // Setting the distribution protocol in Distribution.
            distribution = setDistributionProtocolInDistribution(distribution, requestDetailsForm);
            distributionObjectCollection.add(distribution);
        }
        return existingSpecArrOrderItem;
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
            Collection<DistributedItem> distributedItemCollection) {
        orderItem.setDistributedItem(distributedItem);
        distributedItemCollection.add(distributedItem);
        distribution.setDistributedItemCollection(distributedItemCollection);
        return distribution;
    }

    /**
     * @param distribution object
     * @param requestDetailsForm object
     * @return Distribution object
     */
    private Distribution setDistributionProtocolInDistribution(Distribution distribution,
            RequestDetailsForm requestDetailsForm)
    {
    	try
	    {
    		DistributionProtocol distributionProtocol;

    			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
    	        final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
    	                .getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
    	        distributionProtocol = orderBizLogic
    	                .retrieveDistributionProtocol(requestDetailsForm.getDistributionProtocolId());
    	        if(distributionProtocol == null)
    	        {
			    	InstanceFactory<DistributionProtocol> instFact = DomainInstanceFactory.getInstanceFactory(DistributionProtocol.class);
			    	distributionProtocol =instFact.createObject();// new DistributionProtocol();
			        distributionProtocol.setId(Long.parseLong(requestDetailsForm.getDistributionProtocolId()));
    	        }
		     requestDetailsForm.setDistributionProtocolId(distributionProtocol.getId().toString());
	        distribution.setDistributionProtocol(distributionProtocol);
	    }
    	catch(final BizLogicException exp)
    	{
    		RequestDetailsTransformer.logger.error(exp.getMessage(), exp);
            exp.printStackTrace();
    	}
        return distribution;
    }

    /**
     * @param distribution object
     * @param requestDetailsForm object
     * @return Distribution object
     */
    private Distribution setSiteInDistribution(Distribution distribution, RequestDetailsForm requestDetailsForm) {
    	InstanceFactory<Site> instFact = DomainInstanceFactory.getInstanceFactory(Site.class);
        final Site toSite = instFact.createObject();
        toSite.setId(Long.valueOf(requestDetailsForm.getSite()));
        distribution.setToSite(toSite);
        return distribution;
    }
}
