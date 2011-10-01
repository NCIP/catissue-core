//package edu.wustl.catissuecore.bizlogic.uidomain;
//
//import edu.wustl.catissuecore.actionForm.CheckInCheckOutEventParametersForm;
//import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
//import edu.wustl.catissuecore.factory.DomainInstanceFactory;
//import edu.wustl.common.bizlogic.InputUIRepOfDomain;
//
//@InputUIRepOfDomain(CheckInCheckOutEventParametersForm.class)
//public class CheckInCheckOutEventParametersTransformer
//        extends
//            AbstractSpecimenEventParametersTransformer<CheckInCheckOutEventParametersForm, CheckInCheckOutEventParameter> {
//
//    public CheckInCheckOutEventParameter createDomainObject(CheckInCheckOutEventParametersForm uiRepOfDomain) {
//        CheckInCheckOutEventParameter res = (CheckInCheckOutEventParameter)DomainInstanceFactory.getInstanceFactory(CheckInCheckOutEventParameter.class).createObject();//new CheckInCheckOutEventParameter();
//        overwriteDomainObject(res, uiRepOfDomain);
//        return res;
//    }
//
//    @Override
//    public void overwriteDomainObject(CheckInCheckOutEventParameter domainObject,
//            CheckInCheckOutEventParametersForm uiRepOfDomain) {
//        super.overwriteDomainObject(domainObject, uiRepOfDomain);
//        domainObject.setStorageStatus(uiRepOfDomain.getStorageStatus());
//
//        // catch (final Exception excp)
//        // {
//        // CheckInCheckOutEventParameter.logger.error(excp.getMessage(),excp);
//        // excp.printStackTrace();
//        // }
//
//    }
//
//}
