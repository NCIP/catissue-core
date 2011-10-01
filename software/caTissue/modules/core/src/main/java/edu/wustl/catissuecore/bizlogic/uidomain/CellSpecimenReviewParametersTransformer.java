//package edu.wustl.catissuecore.bizlogic.uidomain;
//
//import edu.wustl.catissuecore.actionForm.CellSpecimenReviewParametersForm;
//import edu.wustl.catissuecore.domain.CellSpecimenReviewParameters;
//import edu.wustl.catissuecore.factory.DomainInstanceFactory;
//import edu.wustl.common.bizlogic.InputUIRepOfDomain;
//
//@InputUIRepOfDomain(CellSpecimenReviewParametersForm.class)
//public class CellSpecimenReviewParametersTransformer
//        extends
//            AbstractReviewEventParametersTransformer<CellSpecimenReviewParametersForm, CellSpecimenReviewParameters> {
//
//    public CellSpecimenReviewParameters createDomainObject(CellSpecimenReviewParametersForm uiRepOfDomain) {
//        CellSpecimenReviewParameters c = (CellSpecimenReviewParameters)DomainInstanceFactory.getInstanceFactory(CellSpecimenReviewParameters.class).createObject();//new CellSpecimenReviewParameters();
//        overwriteDomainObject(c, uiRepOfDomain);
//        return c;
//    }
//
//    @Override
//    public void overwriteDomainObject(CellSpecimenReviewParameters domainObject,
//            CellSpecimenReviewParametersForm uiRepOfDomain) {
//        super.overwriteDomainObject(domainObject, uiRepOfDomain);
//        setNeoplasticCellularityPercentage(domainObject, uiRepOfDomain);
//        if (uiRepOfDomain.getViableCellPercentage() != null
//                && uiRepOfDomain.getViableCellPercentage().trim().length() > 0) {
//            domainObject.setViableCellPercentage(new Double(uiRepOfDomain.getViableCellPercentage()));
//        }
//        // catch (final Exception excp)
//        // {
//        // CellSpecimenReviewParameters.logger.error(excp.getMessage(),excp);
//        // excp.printStackTrace();
//        // }
//    }
//
//    /**
//     * Compares the NeoplasticCellularityPercentage data.
//     *
//     * @param form CellSpecimenReviewParametersForm.
//     */
//    private void setNeoplasticCellularityPercentage(CellSpecimenReviewParameters domainObject,
//            CellSpecimenReviewParametersForm form) {
//        if (form.getNeoplasticCellularityPercentage() != null
//                && form.getNeoplasticCellularityPercentage().trim().length() > 0) {
//            domainObject.setNeoplasticCellularityPercentage(new Double(form.getNeoplasticCellularityPercentage()));
//        }
//    }
//}
