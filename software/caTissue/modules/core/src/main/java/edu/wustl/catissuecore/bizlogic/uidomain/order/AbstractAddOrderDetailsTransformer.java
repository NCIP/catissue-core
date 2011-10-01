package edu.wustl.catissuecore.bizlogic.uidomain.order;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.IAddOrderDetailsForm;
import edu.wustl.catissuecore.bean.OrderSpecimenBean;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

public abstract class AbstractAddOrderDetailsTransformer<U extends AbstractActionForm & IAddOrderDetailsForm>
        extends
            AbstractOrderDetailsTransformer<U> {
    private static Logger logger = Logger.getCommonLogger(AbstractAddOrderDetailsTransformer.class);

    @Override
    public void overwriteDomainObject(OrderDetails domainObject, U form) {
        if (!form.isAddOperation()) {
            throw new IllegalArgumentException("wrong form for UPDATing OrderDetails ??");
        }

        putOrderDetailsForSpecimen(domainObject, form);

        Collection orderItemsSet = new LinkedHashSet();
        domainObject.setOrderItemCollection(orderItemsSet);

        addOrderItems(form, orderItemsSet);
    }

    private void putOrderDetailsForSpecimen(OrderDetails domainObject, U form) {
        domainObject.setComment(form.getOrderForm().getComments());
        domainObject.setName(form.getOrderForm().getOrderRequestName());
        domainObject.setStatus(Constants.ORDER_REQUEST_STATUS_NEW);
        domainObject.setRequestedDate(new Date());
        final String protocolId = form.getOrderForm().getDistributionProtocol();

        if (protocolId != null && !protocolId.equals("")) {
            Long distributionId = Long.valueOf(form.getOrderForm().getDistributionProtocol());
            InstanceFactory<DistributionProtocol> instFact = DomainInstanceFactory.getInstanceFactory(DistributionProtocol.class);
            DistributionProtocol distributionProtocolObj =instFact.createObject();// new DistributionProtocol();
            distributionProtocolObj.setId(distributionId);
            domainObject.setDistributionProtocol(distributionProtocolObj);
        }
    }

    @SuppressWarnings("unchecked")
    protected final Collection<OrderSpecimenBean> getOrderItemsCollection(U form) {
        try {
            Map orderItemsMap = form.getValues();
            MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.bean");
            return parser.generateData(orderItemsMap);
        } catch (final Exception e) {
            AbstractAddOrderDetailsTransformer.logger.error(e.getMessage(), e);
            e.printStackTrace();

            // TODO
            throw new RuntimeException(e);
        }
    }

    protected abstract void addOrderItems(U form, Collection orderItemsSet);
}
