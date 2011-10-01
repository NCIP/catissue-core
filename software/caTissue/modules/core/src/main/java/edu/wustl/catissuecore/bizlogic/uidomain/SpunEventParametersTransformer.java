//package edu.wustl.catissuecore.bizlogic.uidomain;
//
//import edu.wustl.catissuecore.actionForm.SpunEventParametersForm;
//import edu.wustl.catissuecore.domain.SpunEventParameters;
//import edu.wustl.common.bizlogic.InputUIRepOfDomain;
//
//@InputUIRepOfDomain(SpunEventParametersForm.class)
//public class SpunEventParametersTransformer
//        extends
//            AbstractSpecimenEventParametersTransformer<SpunEventParametersForm, SpunEventParameters> {
//
//    public SpunEventParameters createDomainObject(SpunEventParametersForm uiRepOfDomain) {
//        SpunEventParameters spunEventParameters = new SpunEventParameters();
//        overwriteDomainObject(spunEventParameters, uiRepOfDomain);
//        return spunEventParameters;
//    }
//
//    @Override
//    public void overwriteDomainObject(SpunEventParameters domainObject, SpunEventParametersForm uiRepOfDomain) {
//        super.overwriteDomainObject(domainObject, uiRepOfDomain);
//        if (uiRepOfDomain.getGravityForce() != null && uiRepOfDomain.getGravityForce().trim().length() > 0) {
//            domainObject.setGravityForce(Double.parseDouble(uiRepOfDomain.getGravityForce()));
//        }
//        if (uiRepOfDomain.getDurationInMinutes() != null && uiRepOfDomain.getDurationInMinutes().trim().length() > 0) {
//            domainObject.setDurationInMinutes(Integer.parseInt(uiRepOfDomain.getDurationInMinutes()));
//        }
//        // catch (final Exception excp)
//        // {
//        // SpunEventParameters.logger.error(excp.getMessage(),excp);
//        // excp.printStackTrace();
//        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
//        // throw new AssignDataException(errorKey, null,
//        // "SpunEventParameters.java :");
//        // }
//    }
//
//}
