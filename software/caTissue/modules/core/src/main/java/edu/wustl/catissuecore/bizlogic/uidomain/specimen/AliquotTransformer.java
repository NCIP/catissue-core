package edu.wustl.catissuecore.bizlogic.uidomain.specimen;

import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;

@InputUIRepOfDomain(AliquotForm.class)
public class AliquotTransformer extends AbstractSpecimenTransformer<AliquotForm, Specimen> {

    public Specimen createDomainObject(AliquotForm form) {
    	InstanceFactory<Specimen> instFact = DomainInstanceFactory.getInstanceFactory(Specimen.class);
        Specimen specimen =instFact.createObject();// new Specimen();
        overwriteDomainObject(specimen, form);
        return specimen;
    }

    public void overwriteDomainObject(Specimen domainObject, AliquotForm uiRepOfDomain) {
        /*super.overwriteDomainObject(domainObject, uiRepOfDomain);
        // Dispose parent specimen Bug 3773
        try {
            domainObject.setDisposeParentSpecimen(uiRepOfDomain.getDisposeParentSpecimen());

            domainObject.setAliqoutMap(uiRepOfDomain.getAliquotMap());
            domainObject.setNoOfAliquots(Integer.parseInt(uiRepOfDomain.getNoOfAliquots()));
            domainObject.setParentSpecimen(new Specimen());
            domainObject.setCollectionStatus(Constants.COLLECTION_STATUS_COLLECTED);
            domainObject.setLineage(Constants.ALIQUOT);

            /**
             * Patch ID: 3835_1_2 See also: 1_1 to 1_5 Description : Set
             * createdOn date for aliquot.
             */

         /*   domainObject.setCreatedOn(CommonUtilities.parseDate(uiRepOfDomain.getCreatedDate(), CommonServiceLocator
                    .getInstance().getDatePattern()));

            if (!Validator.isEmpty(uiRepOfDomain.getSpecimenLabel())) {
                ((Specimen) domainObject.getParentSpecimen()).setLabel(uiRepOfDomain.getSpecimenLabel());
                domainObject.getParentSpecimen().setId(Long.valueOf(uiRepOfDomain.getSpecimenID()));
            } else if (!Validator.isEmpty(uiRepOfDomain.getBarcode())) {
                domainObject.getParentSpecimen().setId(Long.valueOf(uiRepOfDomain.getSpecimenID()));
                ((Specimen) domainObject.getParentSpecimen()).setBarcode(uiRepOfDomain.getBarcode());
            }

        } catch (final Exception excp) {
            // Specimen.logger.error(excp.getMessage(), excp);
            // excp.printStackTrace();
            // final ErrorKey errorKey =
            // ErrorKey.getErrorKey("assign.data.error");
            // throw new AssignDataException(errorKey, null, "Specimen.java :");
            //
        }*/
    }

}
