package edu.wustl.catissuecore.bizlogic.uidomain.order;

import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.factory.utils.OrderDetailsUtility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.bizlogic.UIDomainTransformer;

public abstract class AbstractOrderDetailsTransformer<U extends AbstractActionForm>
        implements
            UIDomainTransformer<U, OrderDetails> {

    public final OrderDetails createDomainObject(U uiRepOfDomain) {

    	InstanceFactory<OrderDetails> orderDetailsInstFact = DomainInstanceFactory.getInstanceFactory(OrderDetails.class);
        OrderDetails orderDetails=orderDetailsInstFact.createObject();
       // OrderDetailsUtility.updateOrderDetails(uiRepOfDomain.isAddOperation(), orderDetails);//new OrderDetails(uiRepOfDomain.isAddOperation());
        overwriteDomainObject(orderDetails, uiRepOfDomain);
        return orderDetails;
    }

    public abstract void overwriteDomainObject(OrderDetails domainObject, U abstractActionForm);
}
