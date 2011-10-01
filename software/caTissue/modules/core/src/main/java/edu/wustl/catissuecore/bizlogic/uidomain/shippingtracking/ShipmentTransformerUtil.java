package edu.wustl.catissuecore.bizlogic.uidomain.shippingtracking;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import edu.wustl.catissuecore.actionForm.shippingtracking.BaseShipmentForm;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.shippingtracking.BaseShipment;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.shippingtracking.ShippingTrackingUtility;
import edu.wustl.common.util.global.CommonUtilities;

// TODO CAN BE REMOVED...
public class ShipmentTransformerUtil {

    /**
     * domainObject method populates the container contents.
     *
     * @param shipmentForm form contraining all values.
     * @param updatedContainerCollection collection of container objects.
     */
    static void populateContainerContents(BaseShipment domainObject, BaseShipmentForm shipmentForm,
            Collection<StorageContainer> updatedContainerCollection) {
        final int containerCount = shipmentForm.getContainerCounter();
        String fieldValue = "";
        StorageContainer container = null;
        final List<String> lblOrBarcodeList = new ArrayList<String>();
        int containerNum = 0;

        if (containerCount > 0) {
            for (int containerCounter = 1; containerCounter <= containerCount; containerCounter++) {
                if (shipmentForm.isAddOperation()) {
                    fieldValue = (String) shipmentForm.getContainerDetails("containerLabel_" + containerCounter);
                } else {
                    if (shipmentForm.getContainerLabelChoice() != null
                            && shipmentForm.getContainerLabelChoice().trim().equals("ContainerLabel")) {
                        fieldValue = (String) shipmentForm.getContainerDetails("containerLabel_" + containerCounter);
                    } else if (shipmentForm.getContainerLabelChoice() != null
                            && shipmentForm.getContainerLabelChoice().trim().equals("ContainerBarcode")) {
                        fieldValue = (String) shipmentForm.getSpecimenDetails("containerBarcode_" + containerCounter);
                    }
                }
                if (fieldValue != null && !fieldValue.trim().equals("")) {
                    containerNum++;
                    lblOrBarcodeList.add(fieldValue);// bug 11026
                    InstanceFactory<StorageContainer> scInstFact = DomainInstanceFactory.getInstanceFactory(StorageContainer.class);
                    container =scInstFact.createObject();// new StorageContainer();
                    if (shipmentForm.getContainerLabelChoice().equals("ContainerLabel")) {
                        container.setName(fieldValue);
                    } else if (shipmentForm.getContainerLabelChoice().equals("ContainerBarcode")) {
                        container.setBarcode(fieldValue);
                    }
                    if(domainObject.getContainerCollection()==null){
                    	domainObject.setContainerCollection(new HashSet<StorageContainer>());
                    }
                    domainObject.getContainerCollection().add(container);

                    if (!shipmentForm.isAddOperation()) {
                        StorageContainer containerFromCollection = ShippingTrackingUtility.getContainerFromCollection(
                                domainObject.getContainerCollection(), fieldValue);
                        if (containerFromCollection == null) {
                            containerFromCollection = container;
                        }
                        updatedContainerCollection.add(containerFromCollection);
                    }
                }
            }
            if (!shipmentForm.isAddOperation() && containerNum > 0) {
                if (updatedContainerCollection != null && !updatedContainerCollection.isEmpty())// bug
                // 11410
                {
                    domainObject.getContainerCollection().clear();
                    domainObject.getContainerCollection().addAll(updatedContainerCollection);
                }
            }
            shipmentForm.setLblOrBarcodeContainerL(lblOrBarcodeList);
            // bug 11026

        }
    }

    /**
     * creates the object of Site class.
     *
     * @param siteId whose object is to be created.
     * @return object of Site class.
     */
    static Site createSitObject(long siteId) {
    	InstanceFactory<Site> instFact = DomainInstanceFactory.getInstanceFactory(Site.class);
        final Site site = instFact.createObject();
        site.setId(siteId);
        return site;
    }

    /**
     * This Method sets Shipment date Property.
     *
     * @param shipmentForm shipment Form
     * @throws ParseException Parse Exception
     */
    private static void setShipmentDateProperty(BaseShipment domainObject, BaseShipmentForm shipmentForm)
            throws ParseException {
        if (shipmentForm.getSendDate() != null && shipmentForm.getSendDate().trim().length() != 0) {
            final Calendar calendar = Calendar.getInstance();
            Date date;
            date = CommonUtilities.parseDate(shipmentForm.getSendDate(), CommonUtilities.datePattern(shipmentForm
                    .getSendDate()));
            calendar.setTime(date);
            if (shipmentForm.getSendTimeHour() != null && !shipmentForm.getSendTimeHour().trim().equals("")) {
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(shipmentForm.getSendTimeHour()));
            }
            if (shipmentForm.getSendTimeMinutes() != null && !shipmentForm.getSendTimeMinutes().trim().equals("")) {
                calendar.set(Calendar.MINUTE, Integer.parseInt(shipmentForm.getSendTimeMinutes()));
            }
            domainObject.setSendDate(calendar.getTime());
        }
    }

    static void setBasicShipmentProperties(BaseShipment domainObject, BaseShipmentForm shipmentForm) {
        if (shipmentForm.getId() != 0L) {
            domainObject.setId(shipmentForm.getId());
        }
        domainObject.setSenderComments(shipmentForm.getSenderComments());
        domainObject.setSenderSite(createSitObject(shipmentForm.getSenderSiteId()));
        domainObject.setLabel(shipmentForm.getLabel());

        try {
            setShipmentDateProperty(domainObject, shipmentForm);
        } catch (final ParseException e) {
            // domainObject.getLogger().error(e.getMessage(), e);
            e.printStackTrace();
            // throw new
            // AssignDataException(ErrorKey.getErrorKey("errors.item"), e, "item
            // missing");
        }

    }
}
