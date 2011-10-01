package edu.wustl.catissuecore.bizlogic.uidomain.shippingtracking;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentRequestForm;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.global.Validator;

/*
 * TODO: this doesn't use the super-class AT ALL. Possibly remove this inh? Then
 * maybe super class itself isn't needed! See setShipmentContents() etc that
 * AREN't overridden!!
 */
@InputUIRepOfDomain(ShipmentRequestForm.class)
public class ShipmentRequestTransformer implements UIDomainTransformer<ShipmentRequestForm, ShipmentRequest> {

    public ShipmentRequest createDomainObject(ShipmentRequestForm form) {
    	InstanceFactory<ShipmentRequest> shipReqInstFact = DomainInstanceFactory.getInstanceFactory(ShipmentRequest.class);
        ShipmentRequest shipmentRequest = shipReqInstFact.createObject();//new ShipmentRequest();
        overwriteDomainObject(shipmentRequest, form);
        return shipmentRequest;
    }

    public void overwriteDomainObject(ShipmentRequest domainObject, ShipmentRequestForm form) {
        setBasicShipmentRequestProperties(domainObject, form);
        setShipmentContents(domainObject, form);
    }

    /**
     * sets the shipment contents.
     *
     * @param shipmentForm object of ShipmentRequestForm class.
     */
    // @Override
    // TODO HUH???
    private void setShipmentContents(ShipmentRequest domainObject, ShipmentRequestForm shipmentForm) {
        final Collection<StorageContainer> updatedContainerCollection = new HashSet<StorageContainer>();
        // Call to super class's method to set information related to populate
        // container info
        ShipmentTransformerUtil.populateContainerContents(domainObject, shipmentForm, updatedContainerCollection);
        if (!shipmentForm.isAddOperation()) {
            domainObject.getContainerCollection().clear();
            domainObject.getContainerCollection().addAll(updatedContainerCollection);
        }
        // Populate the specimenCollection
        populateSpecimenCollection(domainObject, shipmentForm);
    }

    /**
     * populates specimen collection.
     *
     * @param shipmentForm form containing all values.
     */
    private void populateSpecimenCollection(ShipmentRequest domainObject, ShipmentRequestForm shipmentForm) {
        final int specimenCount = shipmentForm.getSpecimenCounter();
        String fieldValue = "";
        Specimen specimen = null;
        if(domainObject.getSpecimenCollection()==null){
        	domainObject.setSpecimenCollection(new HashSet<Specimen>());
        }
        domainObject.getSpecimenCollection().clear();
        if (specimenCount > 0) {
            for (int specimenCounter = 1; specimenCounter <= specimenCount; specimenCounter++) {
                fieldValue = (String) shipmentForm.getSpecimenDetails("specimenLabel_" + specimenCounter);
                if(!Validator.isEmpty(fieldValue))
                {
                	InstanceFactory<Specimen> instFact= DomainInstanceFactory.getInstanceFactory(Specimen.class);
                    specimen = instFact.createObject();//new Specimen();
                    if (shipmentForm.getSpecimenLabelChoice().equalsIgnoreCase("SpecimenLabel")) {
                        specimen.setLabel(fieldValue);
                    } else if (shipmentForm.getSpecimenLabelChoice().equals("SpecimenBarcode")) {
                        specimen.setBarcode(fieldValue);
                    }
                    domainObject.getSpecimenCollection().add(specimen);
                }
            }
        }
    }

    /**
     * sets the basic shipment request properties.
     *
     * @param shipmentForm form object containing all values.
     * @throws AssignDataException if some assignment operation fails.
     */
    private void setBasicShipmentRequestProperties(ShipmentRequest domainObject, ShipmentRequestForm shipmentForm) {
        ShipmentTransformerUtil.setBasicShipmentProperties(domainObject, shipmentForm);
        if (shipmentForm.getActivityStatus() != null && !shipmentForm.getActivityStatus().trim().equals("")) {
            domainObject.setActivityStatus(shipmentForm.getActivityStatus());
        }
    }
}
