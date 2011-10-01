package edu.wustl.catissuecore.bizlogic.uidomain;

import edu.wustl.catissuecore.actionForm.SpecimenArrayAliquotForm;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;

@InputUIRepOfDomain(SpecimenArrayAliquotForm.class)
public class SpecimenArrayAliquotTransformer extends AbstractSpecimenArrayTransformer<SpecimenArrayAliquotForm> {

    @Override
    public void overwriteDomainObject(SpecimenArray domainObject, SpecimenArrayAliquotForm uiRepOfDomain) {
        super.overwriteDomainObject(domainObject, uiRepOfDomain);
        domainObject.setId(Long.valueOf(uiRepOfDomain.getSpecimenArrayId()));
    }

}
