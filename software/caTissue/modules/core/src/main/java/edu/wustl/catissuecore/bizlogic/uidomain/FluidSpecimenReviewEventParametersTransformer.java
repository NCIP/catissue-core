//package edu.wustl.catissuecore.bizlogic.uidomain;
//
//import edu.wustl.catissuecore.actionForm.FluidSpecimenReviewEventParametersForm;
//import edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters;
//import edu.wustl.catissuecore.factory.DomainInstanceFactory;
//import edu.wustl.common.bizlogic.InputUIRepOfDomain;
//
//@InputUIRepOfDomain(FluidSpecimenReviewEventParametersForm.class)
//public class FluidSpecimenReviewEventParametersTransformer
//        extends
//            AbstractReviewEventParametersTransformer<FluidSpecimenReviewEventParametersForm, FluidSpecimenReviewEventParameters> {
//
//    public FluidSpecimenReviewEventParameters createDomainObject(FluidSpecimenReviewEventParametersForm uiRepOfDomain) {
//        FluidSpecimenReviewEventParameters f = (FluidSpecimenReviewEventParameters)DomainInstanceFactory.getInstanceFactory(FluidSpecimenReviewEventParameters.class).createObject();//new FluidSpecimenReviewEventParameters();
//        overwriteDomainObject(f, uiRepOfDomain);
//        return f;
//    }
//
//    @Override
//    public void overwriteDomainObject(FluidSpecimenReviewEventParameters domainObject,
//            FluidSpecimenReviewEventParametersForm uiRepOfDomain) {
//        super.overwriteDomainObject(domainObject, uiRepOfDomain);
//        // logger.debug("############DomainObject################## : ");
//        // logger.debug(form.getCellCount());
//        // logger.debug("############################## ");
//        if (uiRepOfDomain.getCellCount() != null && uiRepOfDomain.getCellCount().trim().length() > 0) {
//            domainObject.setCellCount(new Double(uiRepOfDomain.getCellCount()));
//        }
//
//        // catch (final Exception excp)
//        // {
//        // FluidSpecimenReviewEventParameters.logger.error(excp.getMessage(),excp);
//        // excp.printStackTrace();
//        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
//        // throw new AssignDataException(errorKey, null,
//        // "FluidSpecimenReviewEventParameters.java :");
//        // }
//    }
//
//}
