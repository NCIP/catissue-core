package edu.wustl.catissuecore.bizlogic.uidomain.order;

import java.util.Collection;

import edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm;
import edu.wustl.catissuecore.bean.OrderSpecimenBean;
import edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;

@InputUIRepOfDomain(OrderBiospecimenArrayForm.class)
public class OrderArrayTransformer extends AbstractAddOrderDetailsTransformer<OrderBiospecimenArrayForm> {

    @Override
    protected void addOrderItems(OrderBiospecimenArrayForm form, Collection orderItemsSet) {
        for (OrderSpecimenBean orderSpecimenBean : getOrderItemsCollection(form)) {
            orderItemsSet.add(createOrderItem(orderSpecimenBean));
        }
    }

    private Object createOrderItem(OrderSpecimenBean orderSpecimenBean) {
    	InstanceFactory<ExistingSpecimenArrayOrderItem> instFact = DomainInstanceFactory.getInstanceFactory(ExistingSpecimenArrayOrderItem.class);
        ExistingSpecimenArrayOrderItem orderItem = instFact.createObject();//new ExistingSpecimenArrayOrderItem();
        orderItem.setSpecimenArray(getSpecimenArray(orderSpecimenBean.getSpecimenId()));
        orderItem.setRequestedQuantity(getReqQty(orderSpecimenBean));

        orderItem.setDescription(orderSpecimenBean.getDescription());
        orderItem.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
        return orderItem;
    }

    private Double getReqQty(OrderSpecimenBean orderSpecimenBean) {
        Double reqQty;
        if ((Constants.ARRAY_ORDER_FORM_TYPE.equals(orderSpecimenBean.getTypeOfItem()))
                && (Constants.TISSUE.equals(orderSpecimenBean.getSpecimenClass()))
                && (orderSpecimenBean.getSpecimenType().equals("unblock"))) {
            reqQty = new Double(1);
        } else {
            reqQty = new Double(orderSpecimenBean.getRequestedQuantity());
        }
        return reqQty;
    }

    /**
     * @param orderSpecimenBean object
     * @param orderItem object
     * @return OrderItem object
     */
    private SpecimenArray getSpecimenArray(String specimenId) {
    	InstanceFactory<SpecimenArray> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenArray.class);
        SpecimenArray specimenArray = instFact.createObject();//new SpecimenArray();
        specimenArray.setId(Long.valueOf(specimenId));
        return specimenArray;
    }

}
