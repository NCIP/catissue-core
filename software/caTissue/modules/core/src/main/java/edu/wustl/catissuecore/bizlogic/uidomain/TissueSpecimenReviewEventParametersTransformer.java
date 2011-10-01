//package edu.wustl.catissuecore.bizlogic.uidomain;
//
//import edu.wustl.catissuecore.actionForm.TissueSpecimenReviewEventParametersForm;
//import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
//import edu.wustl.catissuecore.factory.DomainInstanceFactory;
//import edu.wustl.catissuecore.factory.InstanceFactory;
//import edu.wustl.common.bizlogic.InputUIRepOfDomain;
//import edu.wustl.common.util.global.Validator;
//
//@InputUIRepOfDomain(TissueSpecimenReviewEventParametersForm.class)
//public class TissueSpecimenReviewEventParametersTransformer
//        extends
//            AbstractReviewEventParametersTransformer<TissueSpecimenReviewEventParametersForm, TissueSpecimenReviewEventParameters> {
//
//    public TissueSpecimenReviewEventParameters createDomainObject(TissueSpecimenReviewEventParametersForm uiRepOfDomain) {
//        InstanceFactory<TissueSpecimenReviewEventParameters> instFact = DomainInstanceFactory.getInstanceFactory(TissueSpecimenReviewEventParameters.class);
//    	TissueSpecimenReviewEventParameters t = instFact.createObject();//new TissueSpecimenReviewEventParameters();
//        overwriteDomainObject(t, uiRepOfDomain);
//        return t;
//    }
//
//    @Override
//    public void overwriteDomainObject(TissueSpecimenReviewEventParameters domainObject,
//            TissueSpecimenReviewEventParametersForm uiRepOfDomain) {
//        super.overwriteDomainObject(domainObject, uiRepOfDomain);
//        if (uiRepOfDomain.getNeoplasticCellularityPercentage() != null
//                && uiRepOfDomain.getNeoplasticCellularityPercentage().trim().length() > 0) {
//            domainObject.setNeoplasticCellularityPercentage(new Double(uiRepOfDomain
//                    .getNeoplasticCellularityPercentage()));
//        }
//        if (uiRepOfDomain.getNecrosisPercentage() != null && uiRepOfDomain.getNecrosisPercentage().trim().length() > 0) {
//            domainObject.setNecrosisPercentage(new Double(uiRepOfDomain.getNecrosisPercentage()));
//        }
//        if (uiRepOfDomain.getTotalCellularityPercentage() != null
//                && uiRepOfDomain.getTotalCellularityPercentage().trim().length() > 0) {
//            domainObject.setTotalCellularityPercentage(new Double(uiRepOfDomain.getTotalCellularityPercentage()));
//        }
//        if (uiRepOfDomain.getLymphocyticPercentage() != null
//                && uiRepOfDomain.getLymphocyticPercentage().trim().length() > 0) {
//            domainObject.setLymphocyticPercentage(new Double(uiRepOfDomain.getLymphocyticPercentage()));
//        }
//
//        final Validator validator = new Validator();
//        if (validator.isValidOption(uiRepOfDomain.getHistologicalQuality())) {
//            domainObject.setHistologicalQuality(uiRepOfDomain.getHistologicalQuality());
//        } else {
//            domainObject.setHistologicalQuality(null); // Purposefully set null
//            // for
//            // Edit Mode
//        }
//
//        // catch (final Exception excp)
//        // {
//        // TissueSpecimenReviewEventParameters.logger.error(excp.getMessage(),excp);
//        // excp.printStackTrace();
//        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
//        // throw new AssignDataException(errorKey, null,
//        // "TissueSpecimenReviewEventParameters.java :");
//        // }
//    }
//}
