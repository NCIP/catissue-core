package edu.wustl.catissuecore.bizlogic.uidomain.specimen;

import java.util.Collection;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;

@InputUIRepOfDomain(CreateSpecimenForm.class)
public class CreateSpecimenTransformer extends SpecimenTransformer<CreateSpecimenForm> {

    @Override
    public void overwriteDomainObject(Specimen domainObject, CreateSpecimenForm uiRepOfDomain) {
        super.overwriteDomainObject(domainObject, uiRepOfDomain);
        try {
            // bug no.4265
           // domainObject.setDisposeParentSpecimen(uiRepOfDomain.getDisposeParentSpecimen());
            if (domainObject.getLineage() == null) {
                domainObject.setLineage("Derived");
            }
            domainObject.setActivityStatus(uiRepOfDomain.getActivityStatus());
            domainObject.setCollectionStatus(Constants.COLLECTION_STATUS_COLLECTED);

            if (!Validator.isEmpty(uiRepOfDomain.getBarcode())) {
            	domainObject.setBarcode(AppUtility.handleEmptyStrings(uiRepOfDomain.getBarcode()));
            }

            domainObject.setComment(uiRepOfDomain.getComments());
            // domainObject.getPositionDimensionOne() = new
            // Integer(uiRepOfDomain.getPositionDimensionOne());
            // domainObject.getPositionDimensionTwo() = new
            // Integer(uiRepOfDomain.getPositionDimensionTwo());
            domainObject.setSpecimenType(uiRepOfDomain.getType());
            domainObject.setSpecimenClass(uiRepOfDomain.getClassName());

            /**
             * Patch ID: 3835_1_5 See also: 1_1 to 1_5 Description : Set
             * createdOn date for derived specimen .
             */
            domainObject.setCreatedOn(CommonUtilities.parseDate(uiRepOfDomain.getCreatedDate(), CommonServiceLocator
                    .getInstance().getDatePattern()));

            if (uiRepOfDomain.isAddOperation()) {
                domainObject.setIsAvailable(Boolean.TRUE);
            } else {
                domainObject.setIsAvailable(Boolean.valueOf(uiRepOfDomain.isAvailable()));
            }

            // domainObject.getStorageContainer().setId(new
            // Long(uiRepOfDomain.getStorageContainer()));
            InstanceFactory<CellSpecimen> instFact = DomainInstanceFactory.getInstanceFactory(CellSpecimen.class);
            domainObject.setParentSpecimen(instFact.createObject());

            // domainObject.getParentSpecimen().setId(new
            // Long(uiRepOfDomain.getParentSpecimenId()));
            ((Specimen) domainObject.getParentSpecimen()).setLabel(uiRepOfDomain.getParentSpecimenLabel());
            ((Specimen) domainObject.getParentSpecimen()).setBarcode(AppUtility.handleEmptyStrings(uiRepOfDomain.getParentSpecimenBarcode()));
            // Getting the Map of External Identifiers
            final Map extMap = uiRepOfDomain.getExternalIdentifier();

            final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");

            final Collection extCollection = parser.generateData(extMap);
            domainObject.setExternalIdentifierCollection(extCollection);

            // setting the value of storage container
            if (uiRepOfDomain.isAddOperation()) {
                setSpecimenPosition(domainObject, uiRepOfDomain);
            }
        } catch (final Exception excp) {
            // Specimen.logger.error(excp.getMessage(), excp);
            excp.printStackTrace();
            // final ErrorKey errorKey =
            // ErrorKey.getErrorKey("assign.data.error");
            // throw new AssignDataException(errorKey, null, "Specimen.java :");
        }
    }
}
