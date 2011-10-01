//package edu.wustl.catissuecore.bizlogic.uidomain;
//
//import edu.wustl.catissuecore.actionForm.FrozenEventParametersForm;
//import edu.wustl.catissuecore.domain.FrozenEventParameters;
//import edu.wustl.catissuecore.factory.DomainInstanceFactory;
//import edu.wustl.common.bizlogic.InputUIRepOfDomain;
//
//@InputUIRepOfDomain(FrozenEventParametersForm.class)
//public class FrozenEventParametersTransformer
//        extends
//            AbstractSpecimenEventParametersTransformer<FrozenEventParametersForm, FrozenEventParameters> {
//
//    public FrozenEventParameters createDomainObject(FrozenEventParametersForm uiRepOfDomain) {
//        FrozenEventParameters frozenEventParameters = (FrozenEventParameters)DomainInstanceFactory.getInstanceFactory(FrozenEventParameters.class).createObject();//new FrozenEventParameters();
//        overwriteDomainObject(frozenEventParameters, uiRepOfDomain);
//        return frozenEventParameters;
//    }
//
//    @Override
//    public void overwriteDomainObject(FrozenEventParameters domainObject, FrozenEventParametersForm uiRepOfDomain) {
//        super.overwriteDomainObject(domainObject, uiRepOfDomain);
//        domainObject.setMethod(uiRepOfDomain.getMethod());
//
//        // catch (final Exception excp)
//        // {
//        // FrozenEventParameters.logger.error(excp.getMessage(),excp);
//        // excp.printStackTrace();
//        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
//        // throw new AssignDataException(errorKey, null,
//        // "FrozenEventParameters.java :");
//        // }
//
//    }
//
//}
