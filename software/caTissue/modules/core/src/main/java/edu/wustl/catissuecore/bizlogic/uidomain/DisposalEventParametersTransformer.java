package edu.wustl.catissuecore.bizlogic.uidomain;

import edu.wustl.catissuecore.actionForm.DisposalEventParametersForm;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;

@InputUIRepOfDomain(DisposalEventParametersForm.class)
public class DisposalEventParametersTransformer
        extends
            AbstractSpecimenEventParametersTransformer<DisposalEventParametersForm, DisposalEventParameters> {

    public DisposalEventParameters createDomainObject(DisposalEventParametersForm uiRepOfDomain) {
        DisposalEventParameters disposalEventParameters = (DisposalEventParameters)DomainInstanceFactory.getInstanceFactory(DisposalEventParameters.class).createObject();//new DisposalEventParameters();
        overwriteDomainObject(disposalEventParameters, uiRepOfDomain);
        return disposalEventParameters;
    }

    @Override
    public void overwriteDomainObject(DisposalEventParameters domainObject, DisposalEventParametersForm uiRepOfDomain) {
        super.overwriteDomainObject(domainObject, uiRepOfDomain);
        domainObject.setReason(uiRepOfDomain.getReason());
        domainObject.setActivityStatus(uiRepOfDomain.getActivityStatus());

        // catch (final Exception excp)
        // {
        // DisposalEventParameters.logger.error(excp.getMessage(),excp);
        // excp.printStackTrace();
        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
        // throw new AssignDataException(errorKey, null,
        // "DisposalEventParameters.java :");
        // }
    }

}
