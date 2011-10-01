package edu.wustl.catissuecore.bizlogic.uidomain;

import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;

@InputUIRepOfDomain(InstitutionForm.class)
public class InstitutionTransformer implements UIDomainTransformer<InstitutionForm, Institution> {

    public Institution createDomainObject(InstitutionForm form) {
        Institution institution = (Institution)DomainInstanceFactory.getInstanceFactory(Institution.class).createObject();//new Institution();
        overwriteDomainObject(institution, form);
        return institution;
    }

    public void overwriteDomainObject(Institution institution, InstitutionForm form) {
        institution.setName(form.getName().trim());
        if(form.isDirtyEditFlag()){
        	institution.setDirtyEditFlag(new Boolean(true));
        }else {
        	institution.setDirtyEditFlag(new Boolean(false));
        }
        if(form.isRemoteManagedFlag()){
        	institution.setRemoteManagedFlag(new Boolean(true));
        }else {
        	institution.setRemoteManagedFlag(new Boolean(false));
        }
        if(form.getRemoteId() !=0) {
        	institution.setRemoteId(form.getRemoteId());
        }
    }

}
