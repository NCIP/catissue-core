package edu.wustl.catissuecore.bizlogic.uidomain;

import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.bizlogic.UIDomainTransformer;

/*
 * no base form class here....
 */
public abstract class AbstractSpecimenArrayTransformer<U extends AbstractActionForm>
        implements
            UIDomainTransformer<U, SpecimenArray> {

    public final SpecimenArray createDomainObject(U uiRepOfDomain) {
    	InstanceFactory<SpecimenArray> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenArray.class);
        SpecimenArray specimenArray = instFact.createObject();//new SpecimenArray();
        overwriteDomainObject(specimenArray, uiRepOfDomain);
        return specimenArray;
    }

    public void overwriteDomainObject(SpecimenArray domainObject, U uiRepOfDomain) {
        if (SearchUtil.isNullobject(domainObject.getSpecimenArrayType())) {
        	InstanceFactory<SpecimenArrayType> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenArrayType.class);
            domainObject.setSpecimenArrayType(instFact.createObject());
        }
        if (SearchUtil.isNullobject(domainObject.getCreatedBy())) {
        	InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
            domainObject.setCreatedBy(instFact.createObject());
        }
        if (SearchUtil.isNullobject(domainObject.getLocatedAtPosition())) {
        	InstanceFactory<ContainerPosition> instFact = DomainInstanceFactory.getInstanceFactory(ContainerPosition.class);
            domainObject.setLocatedAtPosition(instFact.createObject());
            //domainObject.setLocatedAtPosition(new ContainerPosition());
        }
        if (SearchUtil.isNullobject(domainObject.getLocatedAtPosition().getParentContainer())) {
        	InstanceFactory<StorageContainer> scInstFact = DomainInstanceFactory.getInstanceFactory(StorageContainer.class);
            domainObject.getLocatedAtPosition().setParentContainer(scInstFact.createObject());
        }
        if (SearchUtil.isNullobject(domainObject.getAvailable())) {
            domainObject.setAvailable(Boolean.TRUE);
        }
    }

}
