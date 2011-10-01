package edu.wustl.catissuecore.bizlogic.uidomain;

import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;

@InputUIRepOfDomain(CancerResearchGroupForm.class)
public class CancerResearchGroupTransformer
        implements
            UIDomainTransformer<CancerResearchGroupForm, CancerResearchGroup> {

    public CancerResearchGroup createDomainObject(CancerResearchGroupForm form) {
        //CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
    	CancerResearchGroup cancerResearchGroup = (CancerResearchGroup)DomainInstanceFactory.getInstanceFactory(CancerResearchGroup.class).createObject();
        overwriteDomainObject(cancerResearchGroup, form);
        return cancerResearchGroup;
    }

    public void overwriteDomainObject(CancerResearchGroup cancerResearchGroup, CancerResearchGroupForm form) {
        cancerResearchGroup.setName(form.getName().trim());
    }

}
