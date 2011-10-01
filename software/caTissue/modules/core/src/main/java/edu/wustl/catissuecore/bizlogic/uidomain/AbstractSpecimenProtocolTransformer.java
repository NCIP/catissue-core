package edu.wustl.catissuecore.bizlogic.uidomain;

import edu.wustl.catissuecore.actionForm.SpecimenProtocolForm;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenProtocol;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.util.global.CommonUtilities;

public abstract class AbstractSpecimenProtocolTransformer<U extends SpecimenProtocolForm, D extends SpecimenProtocol>
        implements
            UIDomainTransformer<U, D> {

    public abstract D createDomainObject(U uiRepOfDomain);

    public void overwriteDomainObject(D domainObject, U uiRepOfDomain) {
        try {
        	InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
            // Change for API Search --- Ashwin 04/10/2006
            if (SearchUtil.isNullobject(domainObject.getPrincipalInvestigator())) {
                domainObject.setPrincipalInvestigator(instFact.createObject());
            }

            domainObject.setTitle(uiRepOfDomain.getTitle());
            domainObject.setShortTitle(uiRepOfDomain.getShortTitle());
            domainObject.setIrbIdentifier(uiRepOfDomain.getIrbID());
            domainObject.setSpecimenLabelFormat(uiRepOfDomain.getSpecimenLabelFormat());
            domainObject.setStartDate(CommonUtilities.parseDate(uiRepOfDomain.getStartDate(), CommonUtilities
                    .datePattern(uiRepOfDomain.getStartDate())));
            domainObject.setEndDate(CommonUtilities.parseDate(uiRepOfDomain.getEndDate(), CommonUtilities
                    .datePattern(uiRepOfDomain.getEndDate())));

            if (uiRepOfDomain.getEnrollment() != null && uiRepOfDomain.getEnrollment().trim().length() > 0) {
                domainObject.setEnrollment(Integer.valueOf(uiRepOfDomain.getEnrollment()));
            }

            domainObject.setDescriptionURL(uiRepOfDomain.getDescriptionURL());
            domainObject.setActivityStatus(uiRepOfDomain.getActivityStatus());

            domainObject.setPrincipalInvestigator(instFact.createObject());
            domainObject.getPrincipalInvestigator().setId(Long.valueOf(uiRepOfDomain.getPrincipalInvestigatorId()));
            
            if (uiRepOfDomain.getIrbSiteId()>0) {
            	domainObject.setIrbSite((Site) DomainInstanceFactory.getInstanceFactory(Site.class).createObject());
            	domainObject.getIrbSite().setId(Long.valueOf(uiRepOfDomain.getIrbSiteId()));
            } else {
            	domainObject.setIrbSite(null);
            }
            
        } catch (final Exception excp) {
            // TODO
            // SpecimenProtocol.logger.error(excp.getMessage(), excp);
            excp.printStackTrace();
            // final ErrorKey errorKey =
            // ErrorKey.getErrorKey("assign.data.error");
            // throw new AssignDataException(errorKey, null,
            // "SpecimenProtocol.java
            // :");
        }
    }
}
