package edu.wustl.catissuecore.bizlogic.uidomain;

import edu.wustl.catissuecore.actionForm.BiohazardForm;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;

@InputUIRepOfDomain(BiohazardForm.class)
public class BiohazardTransformer implements UIDomainTransformer<BiohazardForm, Biohazard> {

    public Biohazard createDomainObject(BiohazardForm form) {
        Biohazard biohazard = (Biohazard) DomainInstanceFactory.getInstanceFactory(Biohazard.class).createObject();//new Biohazard();
        overwriteDomainObject(biohazard, form);
        return biohazard;
    }

    public void overwriteDomainObject(Biohazard domainObject, BiohazardForm uiRepOfDomain) {
        // try
        // {
        domainObject.setComment(uiRepOfDomain.getComments());
        domainObject.setName(uiRepOfDomain.getName().trim());
        domainObject.setType(uiRepOfDomain.getType());
        // }
        // catch (final Exception excp)
        // {
        // Biohazard.logger.error(excp.getMessage(),excp);
        // excp.printStackTrace() ;
        // throw new AssignDataException(
        // ErrorKey.getErrorKey("assign.data.error"),
        // null, "Biohazard.java :");
        // }
    }
}
