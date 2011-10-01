package edu.wustl.catissuecore.bizlogic.uidomain.specimen;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.domain.UIRepOfDomain;

/* NOTE: domainObject is only for Specimen, NOT AbstractSpecimen */

public abstract class AbstractSpecimenTransformer<U extends UIRepOfDomain, D extends Specimen>
        implements
            UIDomainTransformer<U, D> {

    public abstract D createDomainObject(U uiRepOfDomain);

    public void overwriteDomainObject(D domainObject, U uiRepOfDomain) {
        if (SearchUtil.isNullobject(domainObject.getSpecimenPosition())) {
            domainObject.setSpecimenPosition(null);
        }

        if (SearchUtil.isNullobject(domainObject.getSpecimenCollectionGroup())) {
        	InstanceFactory<SpecimenCollectionGroup> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenCollectionGroup.class);
            domainObject.setSpecimenCollectionGroup(instFact.createObject());
        }

        if (SearchUtil.isNullobject(domainObject.getSpecimenCharacteristics())) {
        	InstanceFactory<SpecimenCharacteristics> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenCharacteristics.class);
            domainObject.setSpecimenCharacteristics(instFact.createObject());
        }

        if (SearchUtil.isNullobject(domainObject.getInitialQuantity())) {
            domainObject.setInitialQuantity(new Double(0));
        }

        if (SearchUtil.isNullobject(domainObject.getAvailableQuantity())) {
            domainObject.setAvailableQuantity(new Double(0));
        }
    }

}
