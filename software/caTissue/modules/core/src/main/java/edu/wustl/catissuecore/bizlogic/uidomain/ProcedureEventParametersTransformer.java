//package edu.wustl.catissuecore.bizlogic.uidomain;
//
//import edu.wustl.catissuecore.actionForm.ProcedureEventParametersForm;
//import edu.wustl.catissuecore.domain.ProcedureEventParameters;
//import edu.wustl.catissuecore.factory.DomainInstanceFactory;
//import edu.wustl.common.bizlogic.InputUIRepOfDomain;
//
//@InputUIRepOfDomain(ProcedureEventParametersForm.class)
//public class ProcedureEventParametersTransformer
//        extends
//            AbstractSpecimenEventParametersTransformer<ProcedureEventParametersForm, ProcedureEventParameters> {
//
//    public ProcedureEventParameters createDomainObject(ProcedureEventParametersForm uiRepOfDomain) {
//        ProcedureEventParameters procedureEventParameters = (ProcedureEventParameters)DomainInstanceFactory.getInstanceFactory(ProcedureEventParameters.class).createObject();//new ProcedureEventParameters();
//        overwriteDomainObject(procedureEventParameters, uiRepOfDomain);
//        return procedureEventParameters;
//    }
//
//    @Override
//    public void overwriteDomainObject(ProcedureEventParameters domainObject, ProcedureEventParametersForm uiRepOfDomain) {
//        super.overwriteDomainObject(domainObject, uiRepOfDomain);
//        domainObject.setUrl(uiRepOfDomain.getUrl());
//        domainObject.setName(uiRepOfDomain.getName());
//
//        // catch (final Exception excp)
//        // {
//        // ProcedureEventParameters.logger.error(excp.getMessage(),excp);
//        // excp.printStackTrace();
//        // final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
//        // throw new AssignDataException(errorKey, null,
//        // "ProcedureEventParameters.java :");
//        // }
//    }
//
//}
