//package edu.wustl.catissuecore.bizlogic.uidomain;
//
//import edu.wustl.catissuecore.actionForm.EmbeddedEventParametersForm;
//import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
//import edu.wustl.catissuecore.factory.DomainInstanceFactory;
//import edu.wustl.common.bizlogic.InputUIRepOfDomain;
//
//@InputUIRepOfDomain(EmbeddedEventParametersForm.class)
//public class EmbeddedEventParametersTransformer
//        extends
//            AbstractSpecimenEventParametersTransformer<EmbeddedEventParametersForm, EmbeddedEventParameters> {
//
//    public EmbeddedEventParameters createDomainObject(EmbeddedEventParametersForm uiRepOfDomain) {
//        EmbeddedEventParameters embeddedEventParameters = (EmbeddedEventParameters)DomainInstanceFactory.getInstanceFactory(EmbeddedEventParameters.class).createObject();//new EmbeddedEventParameters();
//        overwriteDomainObject(embeddedEventParameters, uiRepOfDomain);
//        return embeddedEventParameters;
//    }
//
//    @Override
//    public void overwriteDomainObject(EmbeddedEventParameters domainObject, EmbeddedEventParametersForm uiRepOfDomain) {
//        super.overwriteDomainObject(domainObject, uiRepOfDomain);
//        domainObject.setEmbeddingMedium(uiRepOfDomain.getEmbeddingMedium());
//
//        // catch (final Exception excp)
//        // {
//        // EmbeddedEventParameters.logger.error(excp.getMessage(),excp);
//        // excp.printStackTrace();
//        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
//        // throw new AssignDataException(errorKey, null,
//        // "EmbeddedEventParameters.java :");
//        //
//        // }
//    }
//
//}
