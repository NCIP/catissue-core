//package edu.wustl.catissuecore.bizlogic.uidomain;
//
//import edu.wustl.catissuecore.actionForm.FixedEventParametersForm;
//import edu.wustl.catissuecore.domain.FixedEventParameters;
//import edu.wustl.catissuecore.factory.DomainInstanceFactory;
//import edu.wustl.common.bizlogic.InputUIRepOfDomain;
//
//@InputUIRepOfDomain(FixedEventParametersForm.class)
//public class FixedEventParametersTransformer
//        extends
//            AbstractSpecimenEventParametersTransformer<FixedEventParametersForm, FixedEventParameters> {
//
//    public FixedEventParameters createDomainObject(FixedEventParametersForm uiRepOfDomain) {
//        FixedEventParameters fixedEventParameters = (FixedEventParameters)DomainInstanceFactory.getInstanceFactory(FixedEventParameters.class).createObject();//new FixedEventParameters();
//        overwriteDomainObject(fixedEventParameters, uiRepOfDomain);
//        return fixedEventParameters;
//    }
//
//    @Override
//    public void overwriteDomainObject(FixedEventParameters domainObject, FixedEventParametersForm uiRepOfDomain) {
//        super.overwriteDomainObject(domainObject, uiRepOfDomain);
//        domainObject.setFixationType(uiRepOfDomain.getFixationType());
//        if (uiRepOfDomain.getDurationInMinutes() != null && uiRepOfDomain.getDurationInMinutes().trim().length() > 0) {
//            domainObject.setDurationInMinutes(Integer.parseInt(uiRepOfDomain.getDurationInMinutes()));
//        }
//
//        // catch (final Exception excp)
//        // {
//        // FixedEventParameters.logger.error(excp.getMessage(),excp);
//        // excp.printStackTrace();
//        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
//        // throw new AssignDataException(errorKey, null,
//        // "EmbeddedEventParameters.java :");
//        // }
//    }
//
//}
