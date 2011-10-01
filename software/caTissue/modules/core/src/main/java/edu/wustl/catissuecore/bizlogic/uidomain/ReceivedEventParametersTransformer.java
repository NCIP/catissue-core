//package edu.wustl.catissuecore.bizlogic.uidomain;
//
//import edu.wustl.catissuecore.actionForm.ReceivedEventParametersForm;
//import edu.wustl.catissuecore.domain.ReceivedEventParameters;
//import edu.wustl.catissuecore.factory.DomainInstanceFactory;
//import edu.wustl.common.bizlogic.InputUIRepOfDomain;
//
//@InputUIRepOfDomain(ReceivedEventParametersForm.class)
//public class ReceivedEventParametersTransformer
//        extends
//            AbstractSpecimenEventParametersTransformer<ReceivedEventParametersForm, ReceivedEventParameters> {
//
//    public ReceivedEventParameters createDomainObject(ReceivedEventParametersForm uiRepOfDomain) {
//        ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters)DomainInstanceFactory.getInstanceFactory(ReceivedEventParameters.class).createObject();//new ReceivedEventParameters();
//        overwriteDomainObject(receivedEventParameters, uiRepOfDomain);
//        return receivedEventParameters;
//    }
//
//    @Override
//    public void overwriteDomainObject(ReceivedEventParameters domainObject, ReceivedEventParametersForm uiRepOfDomain) {
//        super.overwriteDomainObject(domainObject, uiRepOfDomain);
//        domainObject.setReceivedQuality(uiRepOfDomain.getReceivedQuality());
//        // catch (final Exception excp)
//        // {
//        // ReceivedEventParameters.logger.error(excp.getMessage(),excp);
//        // excp.printStackTrace();
//        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
//        // throw new AssignDataException(errorKey, null,
//        // "ReceivedEventParameters.java :");
//        // }
//    }
//
//}
