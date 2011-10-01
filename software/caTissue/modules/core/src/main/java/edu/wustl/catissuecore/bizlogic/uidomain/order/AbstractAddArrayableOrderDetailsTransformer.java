package edu.wustl.catissuecore.bizlogic.uidomain.order;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.DefineArrayForm;
import edu.wustl.catissuecore.actionForm.IAddArrayableOrderDetailsForm;
import edu.wustl.catissuecore.bean.OrderSpecimenBean;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.OrderItem;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;

/*
 * this for the Specimen and Path cases; there
 */
public abstract class AbstractAddArrayableOrderDetailsTransformer<U extends AbstractActionForm & IAddArrayableOrderDetailsForm>
        extends
            AbstractAddOrderDetailsTransformer<U> {

    @Override
    protected void addOrderItems(U form, Collection orderItemsSet) {
        Map<String, NewSpecimenArrayOrderItem> newSpecimenArrayMap = putnewArrayDetailsforArray(form
                .getDefineArrayObj());

        for (OrderSpecimenBean orderSpecimenBean : getOrderItemsCollection(form)) {
            SpecimenOrderItem specimenOrderItem = getSpecimenOrderItem(orderSpecimenBean);
            orderItemsSet.add(specimenOrderItem);

            if (!orderSpecimenBean.getArrayName().equals("None")) {
                NewSpecimenArrayOrderItem newSpecimenArrayObj = newSpecimenArrayMap.get(orderSpecimenBean
                        .getArrayName());
                if (newSpecimenArrayObj == null) {
                    throw new IllegalArgumentException("array with name : " + orderSpecimenBean.getArrayName()
                            + " doesn't exist??");
                }
                specimenOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayObj);
                getArrayColl(newSpecimenArrayObj).add(specimenOrderItem);
            }
        }
        orderItemsSet.addAll(newSpecimenArrayMap.values());
    }

    private Collection<SpecimenOrderItem> getArrayColl(NewSpecimenArrayOrderItem arr) {
        Collection<SpecimenOrderItem> orderItemCollection = arr.getSpecimenOrderItemCollection();
        if (orderItemCollection == null) {
            orderItemCollection = new LinkedHashSet<SpecimenOrderItem>();
            arr.setSpecimenOrderItemCollection(orderItemCollection);
        }
        return orderItemCollection;
    }

    /**
     * @param newArrayOrderItems object
     * @return List object
     */
    private Map<String, NewSpecimenArrayOrderItem> putnewArrayDetailsforArray(List<DefineArrayForm> newArrayOrderItems) {
        // linked bcos orderItemsSet is a LinkedHashSet; so just being
        // careful...
        Map<String, NewSpecimenArrayOrderItem> newOrderItems = new LinkedHashMap<String, NewSpecimenArrayOrderItem>();
        if (newArrayOrderItems != null) {
            for (DefineArrayForm defineArrayObj : newArrayOrderItems) {
            	InstanceFactory<NewSpecimenArrayOrderItem> instFact = DomainInstanceFactory.getInstanceFactory(NewSpecimenArrayOrderItem.class);
                NewSpecimenArrayOrderItem newSpecimenArrayOrderItem =instFact.createObject(); //new NewSpecimenArrayOrderItem();
                newSpecimenArrayOrderItem.setName(defineArrayObj.getArrayName());
                newSpecimenArrayOrderItem.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);

                InstanceFactory<SpecimenArrayType> spArrTypeInstFact = DomainInstanceFactory.getInstanceFactory(SpecimenArrayType.class);
                SpecimenArrayType specimenArrayTypeObj =spArrTypeInstFact.createObject();// new SpecimenArrayType();
                // specimenArrayTypeObj.setName(defineArrayObj.getArraytype());
                specimenArrayTypeObj.setId(Long.valueOf(defineArrayObj.getArraytype()));
                newSpecimenArrayOrderItem.setSpecimenArrayType(specimenArrayTypeObj);
                newOrderItems.put(newSpecimenArrayOrderItem.getName(), newSpecimenArrayOrderItem);
            }
        }
        return newOrderItems;
    }

    private SpecimenOrderItem getSpecimenOrderItem(OrderSpecimenBean orderSpecimenBean) {
        SpecimenOrderItem specimenOrderItem;
        if (Constants.FALSE.equals(orderSpecimenBean.getIsDerived())) {
            // Existing specimen.
            specimenOrderItem = getExistingOrderItem(orderSpecimenBean);
        } else {
            specimenOrderItem = getDerivedOrderItem(orderSpecimenBean);
        }
        specimenOrderItem.setRequestedQuantity(new Double(orderSpecimenBean.getRequestedQuantity()));
        specimenOrderItem.setDescription(orderSpecimenBean.getDescription());
        specimenOrderItem.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);

        return specimenOrderItem;
    }

    protected abstract SpecimenOrderItem getExistingOrderItem(OrderSpecimenBean orderSpecimenBean);

    protected abstract SpecimenOrderItem getDerivedOrderItem(OrderSpecimenBean orderSpecimenBean);
}
