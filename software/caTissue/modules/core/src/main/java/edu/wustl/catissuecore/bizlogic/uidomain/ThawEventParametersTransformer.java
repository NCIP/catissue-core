//package edu.wustl.catissuecore.bizlogic.uidomain;
//
//import edu.wustl.catissuecore.actionForm.ThawEventParametersForm;
//import edu.wustl.catissuecore.domain.ThawEventParameters;
//import edu.wustl.catissuecore.factory.DomainInstanceFactory;
//import edu.wustl.common.bizlogic.InputUIRepOfDomain;
//
//@InputUIRepOfDomain(ThawEventParametersForm.class)
//public class ThawEventParametersTransformer
//        extends
//            AbstractSpecimenEventParametersTransformer<ThawEventParametersForm, ThawEventParameters> {
//
//    public ThawEventParameters createDomainObject(ThawEventParametersForm uiRepOfDomain) {
//        ThawEventParameters thawEventParameters = (ThawEventParameters)DomainInstanceFactory.getInstanceFactory(ThawEventParameters.class).createObject();//new ThawEventParameters();
//        overwriteDomainObject(thawEventParameters, uiRepOfDomain);
//        return thawEventParameters;
//    }
//
//    @Override
//    public void overwriteDomainObject(ThawEventParameters domainObject, ThawEventParametersForm uiRepOfDomain) {
//        super.overwriteDomainObject(domainObject, uiRepOfDomain);
//
//        // catch (final Exception excp)
//        // {
//        // ThawEventParameters.logger.error(excp.getMessage(),excp);
//        // excp.printStackTrace();
//        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
//        // throw new AssignDataException(errorKey, null,
//        // "ThawEventParameters.java :");
//        // }
//    }
//}
