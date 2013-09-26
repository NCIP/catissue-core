/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.bizlogic.uidomain.order;

import edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm;
import edu.wustl.catissuecore.bean.OrderSpecimenBean;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.PathologicalCaseOrderItem;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;

@InputUIRepOfDomain(OrderPathologyCaseForm.class)
public class OrderPathologyTransformer extends AbstractAddArrayableOrderDetailsTransformer<OrderPathologyCaseForm> {

    @Override
    protected SpecimenOrderItem getExistingOrderItem(OrderSpecimenBean orderSpecimenBean) {
        return getOrderItemForPathology(orderSpecimenBean);
    }

    @Override
    protected SpecimenOrderItem getDerivedOrderItem(OrderSpecimenBean orderSpecimenBean) {
        return getOrderItemForPathology(orderSpecimenBean);
    }

    /**
     * This function sets the Existing order Item for Pathology Case.
     *
     * @param orderSpecimenBean OrderSpecimenBean Object
     * @return existingOrderItem PathologicalCaseOrderItem Object
     */
    private PathologicalCaseOrderItem getOrderItemForPathology(OrderSpecimenBean orderSpecimenBean) {
    	InstanceFactory<PathologicalCaseOrderItem> instFact = DomainInstanceFactory.getInstanceFactory(PathologicalCaseOrderItem.class);
        final PathologicalCaseOrderItem orderItem = instFact.createObject();//new PathologicalCaseOrderItem();
        orderItem.setTissueSite(orderSpecimenBean.getTissueSite());
        orderItem.setPathologicalStatus(orderSpecimenBean.getPathologicalStatus());
        orderItem.setSpecimenClass(orderSpecimenBean.getSpecimenClass());
        orderItem.setSpecimenType(orderSpecimenBean.getSpecimenType());

        InstanceFactory<SpecimenCollectionGroup> scginstFact = DomainInstanceFactory.getInstanceFactory(SpecimenCollectionGroup.class);
        SpecimenCollectionGroup specimenCollGroup = scginstFact.createObject();//new SpecimenCollectionGroup();
        specimenCollGroup.setId(Long.valueOf(orderSpecimenBean.getSpecimenCollectionGroup()));
        orderItem.setSpecimenCollectionGroup(specimenCollGroup);

        return orderItem;
    }
}
