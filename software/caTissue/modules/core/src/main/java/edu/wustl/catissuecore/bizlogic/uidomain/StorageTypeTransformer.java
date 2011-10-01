package edu.wustl.catissuecore.bizlogic.uidomain;

import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.util.logger.Logger;

@InputUIRepOfDomain(StorageTypeForm.class)
public class StorageTypeTransformer extends ContainerTypeTransformer<StorageTypeForm, StorageType> {
    private static final Logger logger = Logger.getCommonLogger(StorageTypeTransformer.class);

    public StorageType createDomainObject(StorageTypeForm uiRepOfDomain) {
    	InstanceFactory<StorageType> stTypeInstFact = DomainInstanceFactory.getInstanceFactory(StorageType.class);
        StorageType storageType = stTypeInstFact.createObject();//new StorageType();
        overwriteDomainObject(storageType, uiRepOfDomain);
        return storageType;
    }

    public void overwriteDomainObject(StorageType domainObject, StorageTypeForm uiRepOfDomain) {
        // SEE comments in superclass
        try {
            domainObject.setId(Long.valueOf(uiRepOfDomain.getId()));
            domainObject.setName(uiRepOfDomain.getType().trim());
            setTemp(domainObject, uiRepOfDomain);
            domainObject.setOneDimensionLabel(uiRepOfDomain.getOneDimensionLabel());
            domainObject.setTwoDimensionLabel(uiRepOfDomain.getTwoDimensionLabel());
            if (SearchUtil.isNullobject(domainObject.getCapacity())) {
                domainObject.setCapacity((Capacity)DomainInstanceFactory.getInstanceFactory(Capacity.class).createObject());//new Capacity()
            }
            domainObject.getCapacity()
                    .setOneDimensionCapacity(Integer.valueOf(uiRepOfDomain.getOneDimensionCapacity()));
            domainObject.getCapacity()
                    .setTwoDimensionCapacity(Integer.valueOf(uiRepOfDomain.getTwoDimensionCapacity()));
            domainObject.setHoldsStorageTypeCollection(new HashSet<StorageType>());
            final long[] storageTypeArr = uiRepOfDomain.getHoldsStorageTypeIds();
            setStorageTypeColl(domainObject, storageTypeArr);
            domainObject.setHoldsSpecimenClassCollection(new HashSet<String>());
            setSpClassColl(domainObject, uiRepOfDomain);
            domainObject.setHoldsSpecimenTypeCollection(new HashSet<String>());
            if (uiRepOfDomain.getSpecimenOrArrayType().equals("Specimen")) {
                StorageContainerUtil.setSpTypeColl(uiRepOfDomain, domainObject);
                // setSpTypeColl(uiRepOfDomain);
            }
            domainObject.setHoldsSpecimenArrayTypeCollection(new HashSet<SpecimenArrayType>());
            populateHoldsSpecimenArrayTypeCollection(domainObject, uiRepOfDomain);
            domainObject.setActivityStatus("Active");
        } catch (final Exception excp) {
            // StorageType.logger.error(excp.getMessage(),excp);
            excp.printStackTrace();
            // final ErrorKey errorKey =
            // ErrorKey.getErrorKey("assign.data.error");
            // throw new AssignDataException(errorKey, null, "Specimen.java :");
        }
    }

    /**
     * @param storageTypeArr storageTypeArr
     */
    private void setStorageTypeColl(StorageType domainObject, long[] storageTypeArr) {
        if (storageTypeArr != null) {
        	InstanceFactory<StorageType> stTypeInstFact = DomainInstanceFactory.getInstanceFactory(StorageType.class);
            for (final long element : storageTypeArr) {
                logger.debug("type Id :" + element);
                if (element != -1) {
                    final StorageType storageType = stTypeInstFact.createObject();//new StorageType();
                    storageType.setId(Long.valueOf(element));
                    domainObject.getHoldsStorageTypeCollection().add(storageType);
                }
            }
        }
    }

    /**
     * Set Temperature.
     *
     * @param storageTypeForm Storage Type Form Object
     */
    private void setTemp(StorageType domainObject, StorageTypeForm storageTypeForm) {
        if (storageTypeForm.getDefaultTemperature() != null
                && storageTypeForm.getDefaultTemperature().trim().length() > 0) {
            domainObject.setDefaultTemperatureInCentigrade(new Double(storageTypeForm.getDefaultTemperature()));
        }
    }

    /**
     * @param storageTypeForm Storage Type Form
     */
    private void setSpClassColl(StorageType domainObject, StorageTypeForm storageTypeForm) {
        if (storageTypeForm.getSpecimenOrArrayType().equals("Specimen")) {
            final String[] specimenClassTypeArr = storageTypeForm.getHoldsSpecimenClassTypes();
            if (specimenClassTypeArr != null) {

                for (final String element : specimenClassTypeArr) {
                    logger.debug("type Id :" + element);
                    if (element.equals("-1")) {
                        domainObject.getHoldsSpecimenClassCollection().addAll(AppUtility.getSpecimenClassTypes());
                        break;
                    } else {
                        domainObject.getHoldsSpecimenClassCollection().add(element);
                    }
                }
            }
        }
    }

    /**
     * @param storageTypeForm of StorageTypeForm type.
     */
    private void populateHoldsSpecimenArrayTypeCollection(StorageType domainObject, StorageTypeForm storageTypeForm) {
        if (storageTypeForm.getSpecimenOrArrayType().equals("SpecimenArray")) {
            final long[] specimenArrayTypeArr = storageTypeForm.getHoldsSpecimenArrTypeIds();
            if (specimenArrayTypeArr != null) {
            	InstanceFactory<SpecimenArrayType> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenArrayType.class);
                for (final long element : specimenArrayTypeArr) {
                    logger.debug("specimen array type Id :" + element);
                    if (element != -1) {
                        final SpecimenArrayType spArrayType = instFact.createObject();// new SpecimenArrayType();
                        spArrayType.setId(Long.valueOf(element));
                        domainObject.getHoldsSpecimenArrayTypeCollection().add(spArrayType);
                    }
                }
            }
        }
    }
}
