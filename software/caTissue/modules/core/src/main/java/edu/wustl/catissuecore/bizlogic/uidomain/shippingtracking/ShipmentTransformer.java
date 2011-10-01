package edu.wustl.catissuecore.bizlogic.uidomain.shippingtracking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentForm;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.shippingtracking.ShippingTrackingUtility;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.global.Validator;

@InputUIRepOfDomain(ShipmentForm.class)
public class ShipmentTransformer implements UIDomainTransformer<ShipmentForm, Shipment> {

    public Shipment createDomainObject(ShipmentForm uiRepOfDomain) {
    	InstanceFactory<Shipment> shipInstFact = DomainInstanceFactory.getInstanceFactory(Shipment.class);
    	Shipment shipment = shipInstFact.createObject();//new Shipment();
        overwriteDomainObject(shipment, uiRepOfDomain);
        return shipment;
    }

    public void overwriteDomainObject(Shipment domainObject, ShipmentForm uiRepOfDomain) {
        setBasicShipmentProperties(domainObject, uiRepOfDomain);
        setShipmentContents(domainObject, uiRepOfDomain);

        domainObject.setBarcode(uiRepOfDomain.getBarcode());
        if (uiRepOfDomain.getShipmentRequestId() != 0) {
        	InstanceFactory<ShipmentRequest> shipReqInstFact = DomainInstanceFactory.getInstanceFactory(ShipmentRequest.class);
            final ShipmentRequest request = shipReqInstFact.createObject();//new ShipmentRequest();
            request.setId(uiRepOfDomain.getShipmentRequestId());
            domainObject.setShipmentRequest(request);
        }

    }

    /**
     * sets the basic shipment properties like label,site,comments,etc.
     *
     * @param shipmentForm form containing all values.
     * @throws AssignDataException if some assigning operation fails.
     */
    private void setBasicShipmentProperties(Shipment domainObject, ShipmentForm shipmentForm) {
        ShipmentTransformerUtil.setBasicShipmentProperties(domainObject, shipmentForm);
        domainObject.setReceiverSite(ShipmentTransformerUtil.createSitObject(shipmentForm.getReceiverSiteId()));
    }

    /**
     * sets the shipment comments.
     *
     * @param shipmentForm form containing all values.
     */
    private void setShipmentContents(Shipment domainObject, ShipmentForm shipmentForm) {
        final Collection<StorageContainer> updatedContainerCollection = new HashSet<StorageContainer>();
        if (shipmentForm.isAddOperation()) {
            domainObject.getContainerCollection().clear();
        }

        final StorageContainer storageContainer = populateSpecimenContents(domainObject, shipmentForm);
        ShipmentTransformerUtil.populateContainerContents(domainObject, shipmentForm, updatedContainerCollection);

        if (!shipmentForm.isAddOperation()) {
            if (storageContainer != null)// bug 11410
            {
                updatedContainerCollection.add(storageContainer);
            }
            domainObject.getContainerCollection().clear();
            domainObject.getContainerCollection().addAll(updatedContainerCollection);
        } else {
            if (storageContainer != null) {
                domainObject.getContainerCollection().add(storageContainer);
            }
        }
    }

    /**
     * domainObject method populates specimen contents.
     *
     * @param shipmentForm form containing all values.
     * @return object of StorageContainer class.
     */
    private StorageContainer populateSpecimenContents(Shipment domainObject, ShipmentForm shipmentForm) {
        StorageContainer container = null;
        final int specimenCount = shipmentForm.getSpecimenCounter();
        final List<String> lblOrBarcodeList = new ArrayList<String>();
        String fieldValue = "";
        boolean containsSpecimens = false;
        Specimen specimen = null;
        int numOfSpecimens = 0;
        final Collection<SpecimenPosition> updatedSpecimenPosCollection = new HashSet<SpecimenPosition>();

        if (specimenCount > 0) {
            if (shipmentForm.isAddOperation()) {
                container = ShippingTrackingUtility.createInTransitContainer(shipmentForm);
            } else {
                container = ShippingTrackingUtility.getInTransitContainer(domainObject.getContainerCollection());
                if (container == null)// bug 11410
                {
                    container = ShippingTrackingUtility.createInTransitContainer(shipmentForm);
                }
            }

            for (int specimenCounter = 1; specimenCounter <= specimenCount; specimenCounter++) {
                // Get specimen label or barcode
                if (shipmentForm.isAddOperation()) {
                    fieldValue = (String) shipmentForm.getSpecimenDetails("specimenLabel_" + specimenCounter);
                } else {
                    if (shipmentForm.getSpecimenLabelChoice() != null
                            && shipmentForm.getSpecimenLabelChoice().trim().equals("SpecimenLabel")) {
                        fieldValue = (String) shipmentForm.getSpecimenDetails("specimenLabel_" + specimenCounter);
                    } else if (shipmentForm.getSpecimenLabelChoice() != null
                            && shipmentForm.getSpecimenLabelChoice().trim().equals("SpecimenBarcode")) {
                        fieldValue = (String) shipmentForm.getSpecimenDetails("specimenBarcode_" + specimenCounter);
                    }
                }

                if(!Validator.isEmpty(fieldValue))
                {
                    containsSpecimens = true;
                    lblOrBarcodeList.add(fieldValue); // bug 11026
                    InstanceFactory<Specimen> instFact= DomainInstanceFactory.getInstanceFactory(Specimen.class);
                    specimen = instFact.createObject();//new Specimen();
                    if (shipmentForm.getSpecimenLabelChoice().equalsIgnoreCase("SpecimenLabel")) {
                        specimen.setLabel(fieldValue);
                    } else if (shipmentForm.getSpecimenLabelChoice().equals("SpecimenBarcode")) {
                        specimen.setBarcode(AppUtility.handleEmptyStrings(fieldValue));
                    }

                    if (shipmentForm.isAddOperation()) {
                        numOfSpecimens++;

                        // Get SpecimenPosition object and set it to specimen
                        final SpecimenPosition specimenPosition = createSpecimenPosition(specimen, container,
                                specimenCounter);
                        specimen.setSpecimenPosition(specimenPosition);

                        // Add the SpecimenPostion object to container
                        container.getSpecimenPositionCollection().add(specimenPosition);
                    } else {
                        final SpecimenPosition specimenPosFromCollection = ShippingTrackingUtility
                                .getSpecimenPositionFromCollection(container.getSpecimenPositionCollection(),
                                        fieldValue);
                        numOfSpecimens++;
                        if (specimenPosFromCollection == null) {
                            // A new specimen is being added
                            // Get SpecimenPosition object and set it to
                            // specimen
                            final SpecimenPosition specimenPosition = createSpecimenPosition(specimen, container,
                                    specimenCounter);
                            specimen.setSpecimenPosition(specimenPosition);

                            // Add the SpecimenPostion object to container
                            container.getSpecimenPositionCollection().add(specimenPosition);
                            updatedSpecimenPosCollection.add(specimenPosition);
                        } else {
                            // This specimen was already present in the shipment
                            specimenPosFromCollection.setPositionDimensionOne(specimenCounter);
                            specimen = specimenPosFromCollection.getSpecimen();
                            updatedSpecimenPosCollection.add(specimenPosFromCollection);
                        }
                    }
                }
            }
            container.getCapacity().setOneDimensionCapacity(numOfSpecimens);
            // Process the specimens which have been deleted from the shipment
            updateSpecimenPositionToVirtual(container.getSpecimenPositionCollection(), updatedSpecimenPosCollection);
            if (!shipmentForm.isAddOperation()) {
                container.getSpecimenPositionCollection().clear();
                container.getSpecimenPositionCollection().addAll(updatedSpecimenPosCollection);
            }
            shipmentForm.setLblOrBarcodeSpecimenL(lblOrBarcodeList);// bug 11026

        }
        if (!containsSpecimens) {
            container = null;
        }
        return container;

    }

    /**
     * domainObject method updates the specimen position setting it to virtual.
     *
     * @param specimenPositionCollection collection of specimens.
     * @param updatedSpecimenPosCollection collection to be updated.
     */
    private void updateSpecimenPositionToVirtual(Collection<SpecimenPosition> specimenPositionCollection,
            Collection<SpecimenPosition> updatedSpecimenPosCollection) {
        final Iterator<SpecimenPosition> specimenPosIterator = specimenPositionCollection.iterator();
        while (specimenPosIterator.hasNext()) {
            final SpecimenPosition position = specimenPosIterator.next();
            if (position != null && position.getSpecimen() != null && position.getSpecimen().getLabel() != null) {
                final SpecimenPosition specimenPosFromCollection = ShippingTrackingUtility
                        .getSpecimenPositionFromCollection(updatedSpecimenPosCollection, position.getSpecimen()
                                .getLabel());
                if (specimenPosFromCollection == null) {
                    position.getSpecimen().setSpecimenPosition(null);
                }
            }

        }
    }

    /**
     * creates specimen position.
     *
     * @param specimen whose position is to be created.
     * @param container inside which specimen position is to be created.
     * @param positionDimensionOne dimension of the container.
     * @return position of the specimen.
     */
    private SpecimenPosition createSpecimenPosition(Specimen specimen, StorageContainer container,
            int positionDimensionOne) {
    	InstanceFactory<SpecimenPosition> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenPosition.class);
        final SpecimenPosition position = instFact.createObject();//new SpecimenPosition();
        position.setSpecimen(specimen);
        // Put the specimen at (positionDimensionOne,1), since the container is
        // of dimension (number of specimens,1)
        position.setPositionDimensionOne(positionDimensionOne);
        position.setPositionDimensionTwo(1);
        position.setStorageContainer(container);

        return position;
    }

}
