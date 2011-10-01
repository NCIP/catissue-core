//package edu.wustl.catissuecore.bizlogic.uidomain;
//
//import edu.wustl.catissuecore.actionForm.CollectionEventParametersForm;
//import edu.wustl.catissuecore.domain.CollectionEventParameters;
//import edu.wustl.catissuecore.factory.DomainInstanceFactory;
//import edu.wustl.common.bizlogic.InputUIRepOfDomain;
//import edu.wustl.common.util.global.Validator;
//
//@InputUIRepOfDomain(CollectionEventParametersForm.class)
//public class CollectionEventParametersTransformer
//        extends
//            AbstractSpecimenEventParametersTransformer<CollectionEventParametersForm, CollectionEventParameters> {
//
//    public CollectionEventParameters createDomainObject(CollectionEventParametersForm uiRepOfDomain) {
//        CollectionEventParameters collectionEventParameters = (CollectionEventParameters)DomainInstanceFactory.getInstanceFactory(CollectionEventParameters.class).createObject();//new CollectionEventParameters();
//        overwriteDomainObject(collectionEventParameters, uiRepOfDomain);
//        return collectionEventParameters;
//    }
//
//    @Override
//    public void overwriteDomainObject(CollectionEventParameters domainObject,
//            CollectionEventParametersForm uiRepOfDomain) {
//        super.overwriteDomainObject(domainObject, uiRepOfDomain);
//        final Validator validator = new Validator();
//
//        if (validator.isValidOption(uiRepOfDomain.getContainer())) {
//            domainObject.setContainer(uiRepOfDomain.getContainer());
//        } else {
//            domainObject.setContainer(null); // Purposefully set null for
//            // Edit Mode
//        }
//
//        domainObject.setCollectionProcedure(uiRepOfDomain.getCollectionProcedure());
//
//        // catch (final Exception excp)
//        // {
//        // CollectionEventParameters.logger.error(excp.getMessage(),excp);
//        // excp.printStackTrace();
//        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
//        // throw new AssignDataException(errorKey, null,
//        // "CollectionEventParameters.java :");
//        // }
//    }
//
//}
