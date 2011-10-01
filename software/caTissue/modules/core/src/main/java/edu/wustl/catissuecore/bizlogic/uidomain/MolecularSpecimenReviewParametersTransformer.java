//package edu.wustl.catissuecore.bizlogic.uidomain;
//
//import edu.wustl.catissuecore.actionForm.MolecularSpecimenReviewParametersForm;
//import edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters;
//import edu.wustl.catissuecore.factory.DomainInstanceFactory;
//import edu.wustl.catissuecore.factory.InstanceFactory;
//import edu.wustl.common.bizlogic.InputUIRepOfDomain;
//import edu.wustl.common.util.global.CommonUtilities;
//
//@InputUIRepOfDomain(MolecularSpecimenReviewParametersForm.class)
//public class MolecularSpecimenReviewParametersTransformer
//        extends
//            AbstractReviewEventParametersTransformer<MolecularSpecimenReviewParametersForm, MolecularSpecimenReviewParameters> {
//
//    public MolecularSpecimenReviewParameters createDomainObject(MolecularSpecimenReviewParametersForm uiRepOfDomain) {
//        InstanceFactory<MolecularSpecimenReviewParameters> instFact = DomainInstanceFactory.getInstanceFactory(MolecularSpecimenReviewParameters.class);
//    	MolecularSpecimenReviewParameters m = instFact.createObject();//new MolecularSpecimenReviewParameters();
//        overwriteDomainObject(m, uiRepOfDomain);
//        return m;
//    }
//
//    @Override
//    public void overwriteDomainObject(MolecularSpecimenReviewParameters domainObject,
//            MolecularSpecimenReviewParametersForm uiRepOfDomain) {
//        super.overwriteDomainObject(domainObject, uiRepOfDomain);
//        domainObject.setGelImageURL(CommonUtilities.toString(uiRepOfDomain.getGelImageURL()));
//        domainObject.setQualityIndex(CommonUtilities.toString(uiRepOfDomain.getQualityIndex()));
//        domainObject.setLaneNumber(CommonUtilities.toString(uiRepOfDomain.getLaneNumber()));
//        if (CommonUtilities.toString(uiRepOfDomain.getGelNumber()).trim().length() > 0) {
//            domainObject.setGelNumber(Integer.valueOf(uiRepOfDomain.getGelNumber()));
//        }
//        if (CommonUtilities.toString(uiRepOfDomain.getAbsorbanceAt260()).trim().length() > 0) {
//            domainObject.setAbsorbanceAt260(new Double(uiRepOfDomain.getAbsorbanceAt260()));
//        }
//        if (CommonUtilities.toString(uiRepOfDomain.getAbsorbanceAt280()).trim().length() > 0) {
//            domainObject.setAbsorbanceAt280(new Double(uiRepOfDomain.getAbsorbanceAt280()));
//        }
//        if (CommonUtilities.toString(uiRepOfDomain.getRatio28STo18S()).trim().length() > 0) {
//            domainObject.setRatio28STo18S(new Double(uiRepOfDomain.getRatio28STo18S()));
//        }
//
//        // catch (final Exception excp)
//        // {
//        // MolecularSpecimenReviewParameters.logger.error(excp.getMessage(),excp);
//        // excp.printStackTrace();
//        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
//        // throw new AssignDataException(errorKey, null,
//        // "MolecularSpecimenReviewParameters.java :");
//        // }
//    }
//
//}
