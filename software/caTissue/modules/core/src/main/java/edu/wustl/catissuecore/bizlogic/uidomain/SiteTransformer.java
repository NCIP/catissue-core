package edu.wustl.catissuecore.bizlogic.uidomain;

import org.apache.commons.lang.StringUtils;

import edu.wustl.catissuecore.actionForm.SiteForm;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;

@InputUIRepOfDomain(SiteForm.class)
public class SiteTransformer implements UIDomainTransformer<SiteForm, Site> {

    public Site createDomainObject(SiteForm uiRepOfDomain) {
    	InstanceFactory<Site> instFact = DomainInstanceFactory.getInstanceFactory(Site.class);
        Site site = instFact.createObject();
        overwriteDomainObject(site, uiRepOfDomain);
        return site;
    }

    public void overwriteDomainObject(Site domainObject, SiteForm uiRepOfDomain) {
        // try {
        // Change for API Search --- Ashwin 04/10/2006
        if (SearchUtil.isNullobject(domainObject.getCoordinator())) {
        	InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
            domainObject.setCoordinator(instFact.createObject());
        }
        // Change for API Search --- Ashwin 04/10/2006
        if (SearchUtil.isNullobject(domainObject.getAddress())) {
            //domainObject.setAddress(new Address());
        	domainObject.setAddress((Address)DomainInstanceFactory.getInstanceFactory(Address.class).createObject());
        }

        domainObject.setId(Long.valueOf(uiRepOfDomain.getId()));
        domainObject.setName(uiRepOfDomain.getName().trim());
        final String ctep = StringUtils.trim(uiRepOfDomain.getCtepId());
		domainObject.setCtepId(StringUtils.isBlank(ctep)?null:ctep);
        domainObject.setType(uiRepOfDomain.getType());

        domainObject.setEmailAddress(uiRepOfDomain.getEmailAddress());

        domainObject.setActivityStatus(uiRepOfDomain.getActivityStatus());
//        logger.debug("uiRepOfDomain.getCoordinatorId() " + uiRepOfDomain.getCoordinatorId());
        domainObject.getCoordinator().setId(Long.valueOf(uiRepOfDomain.getCoordinatorId()));

        domainObject.getAddress().setStreet(uiRepOfDomain.getStreet());
        domainObject.getAddress().setCity(uiRepOfDomain.getCity());
        domainObject.getAddress().setState(uiRepOfDomain.getState());
        domainObject.getAddress().setCountry(uiRepOfDomain.getCountry());
        domainObject.getAddress().setZipCode(uiRepOfDomain.getZipCode());
        domainObject.getAddress().setPhoneNumber(uiRepOfDomain.getPhoneNumber());
        domainObject.getAddress().setFaxNumber(uiRepOfDomain.getFaxNumber());
        // } catch (final Exception excp) {
        // Site.logger.error(excp.getMessage(), excp);
        // excp.printStackTrace();
        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
        // throw new AssignDataException(errorKey, null, "Site.java :");
        // }
    }

}
