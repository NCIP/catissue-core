package edu.wustl.catissuecore.bizlogic.uidomain.order;

import edu.wustl.catissuecore.actionForm.OrderSpecimenForm;
import edu.wustl.catissuecore.bean.OrderSpecimenBean;
import edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;

@InputUIRepOfDomain(OrderSpecimenForm.class)
public class OrderSpecimenTransformer extends AbstractAddArrayableOrderDetailsTransformer<OrderSpecimenForm> {

    @Override
    protected SpecimenOrderItem getExistingOrderItem(OrderSpecimenBean orderSpecimenBean) {
    	InstanceFactory<ExistingSpecimenOrderItem> existSpecOrderItemInstFact = DomainInstanceFactory.getInstanceFactory(ExistingSpecimenOrderItem.class);
        ExistingSpecimenOrderItem existingOrderItem = existSpecOrderItemInstFact.createObject();//new ExistingSpecimenOrderItem();
        existingOrderItem.setSpecimen(getSpecimen(orderSpecimenBean.getSpecimenId()));
        return existingOrderItem;
    }

    @Override
    protected SpecimenOrderItem getDerivedOrderItem(OrderSpecimenBean orderSpecimenBean) {
    	InstanceFactory<DerivedSpecimenOrderItem> derSpecOrderInstFact = DomainInstanceFactory.getInstanceFactory(DerivedSpecimenOrderItem.class);
        DerivedSpecimenOrderItem derivedSpecimenOrderItem = derSpecOrderInstFact.createObject();//new DerivedSpecimenOrderItem();
        derivedSpecimenOrderItem.setParentSpecimen(getSpecimen(orderSpecimenBean.getSpecimenId()));
        derivedSpecimenOrderItem.setSpecimenClass(orderSpecimenBean.getSpecimenClass());
        derivedSpecimenOrderItem.setSpecimenType(orderSpecimenBean.getSpecimenType());

        return derivedSpecimenOrderItem;
    }

    private Specimen getSpecimen(String id) {
    	InstanceFactory<Specimen> instFact= DomainInstanceFactory.getInstanceFactory(Specimen.class);
        Specimen specimen = instFact.createObject();//new Specimen();
        specimen.setId(Long.valueOf(id));
        return specimen;
    }
}
