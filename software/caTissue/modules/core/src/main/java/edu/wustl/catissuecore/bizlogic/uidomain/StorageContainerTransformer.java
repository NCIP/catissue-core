package edu.wustl.catissuecore.bizlogic.uidomain;

import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;

/*
 * domainObject seems weird bcos StorageContainer.setAllValues didn't call
 * Container.setAllValues().
 */
@InputUIRepOfDomain(StorageContainerForm.class)
public class StorageContainerTransformer implements UIDomainTransformer<StorageContainerForm, StorageContainer> {

    public StorageContainer createDomainObject(StorageContainerForm uiRepOfDomain) {
    	InstanceFactory<StorageContainer> scInstFact = DomainInstanceFactory.getInstanceFactory(StorageContainer.class);
        StorageContainer storageContainer = scInstFact.createObject();
        overwriteDomainObject(storageContainer, uiRepOfDomain);
        return storageContainer;
    }

    public void overwriteDomainObject(StorageContainer domainObject, StorageContainerForm uiRepOfDomain) {
        try {
            domainObject.setName(uiRepOfDomain.getContainerName());
            //domainObject.setNoOfContainers(new Integer(uiRepOfDomain.getNoOfContainers()));
            if (CommonUtilities.toString(uiRepOfDomain.getDefaultTemperature()).trim().length() > 0) {
                domainObject.setTemperatureInCentigrade(new Double(uiRepOfDomain.getDefaultTemperature()));
            }
            if (uiRepOfDomain.getBarcode() != null && uiRepOfDomain.getBarcode().equals("")) {
                domainObject.setBarcode(null);
            } else {
                domainObject.setBarcode(uiRepOfDomain.getBarcode());
            }
            domainObject.setFull(new Boolean(uiRepOfDomain.getIsFull()));
            domainObject.setActivityStatus(uiRepOfDomain.getActivityStatus());
            //domainObject.setNoOfContainers(new Integer(uiRepOfDomain.getNoOfContainers()));
            InstanceFactory<StorageType> stTypeInstFact = DomainInstanceFactory.getInstanceFactory(StorageType.class);
            domainObject.setStorageType(stTypeInstFact.createObject());
            domainObject.getStorageType().setId(new Long(uiRepOfDomain.getTypeId()));
            domainObject.getStorageType().setOneDimensionLabel(uiRepOfDomain.getOneDimensionLabel());
            domainObject.getStorageType().setTwoDimensionLabel(uiRepOfDomain.getTwoDimensionLabel());
            domainObject.getStorageType().setName(uiRepOfDomain.getTypeName());
            if (SearchUtil.isNullobject(domainObject.getCapacity())) {
                domainObject.setCapacity((Capacity)DomainInstanceFactory.getInstanceFactory(Capacity.class).createObject());//new Capacity()
            }
            domainObject.getCapacity().setOneDimensionCapacity(new Integer(uiRepOfDomain.getOneDimensionCapacity()));
            domainObject.getCapacity().setTwoDimensionCapacity(new Integer(uiRepOfDomain.getTwoDimensionCapacity()));
            /*if (!uiRepOfDomain.isAddOperation()) {
                // Previously Container was in a site
                if (domainObject.getLocatedAtPosition() != null
                        && domainObject.getLocatedAtPosition().getParentContainer() == null) {
                    if (uiRepOfDomain.getStContSelection() == 1) {
                        // Put domainObject in another container
                        if (uiRepOfDomain.getParentContainerId() > 0) {
                            domainObject.setParentChanged(true);
                        }
                    } else {
                        if (!uiRepOfDomain.getContainerId().equals("0")) {
                            domainObject.setParentChanged(true);
                        }
                    }
                } else
                // in another container
                {
                    if (uiRepOfDomain.getStContSelection() == 1) {
                        if (domainObject.getLocatedAtPosition() != null
                                && domainObject.getLocatedAtPosition().getParentContainer() != null
                                && domainObject.getLocatedAtPosition().getParentContainer().getId().longValue() != uiRepOfDomain
                                        .getParentContainerId()) {
                            domainObject.setParentChanged(true);
                        }
                    } else if (domainObject.getLocatedAtPosition() != null
                            && domainObject.getLocatedAtPosition().getParentContainer() != null
                            && domainObject.getLocatedAtPosition().getParentContainer().getId().equals(
                                    uiRepOfDomain.getContainerId())) {
                        domainObject.setParentChanged(true);
                    }

                    // code to check if the position of the container is changed

                    if (uiRepOfDomain.getStContSelection() == 1) {
                        if ((domainObject.getLocatedAtPosition() != null && domainObject.getLocatedAtPosition()
                                .getPositionDimensionOne().intValue() != uiRepOfDomain.getPositionDimensionOne())
                                || (domainObject.getLocatedAtPosition() != null && domainObject.getLocatedAtPosition()
                                        .getPositionDimensionTwo().intValue() != uiRepOfDomain
                                        .getPositionDimensionOne())) {
                            domainObject.setPositionChanged(true);
                        }
                    } else {
                        if (domainObject.getLocatedAtPosition() != null
                                && domainObject.getLocatedAtPosition().getPositionDimensionOne().equals(
                                        uiRepOfDomain.getPos1())
                                || domainObject.getLocatedAtPosition().getPositionDimensionTwo().equals(
                                        uiRepOfDomain.getPos2())) {
                            domainObject.setPositionChanged(true);
                        }
                    }

                }
            }*/

            // logger.debug("isParentChanged " +
            // domainObject.getIsParentChanged());
            InstanceFactory<StorageContainer> scInstFact = DomainInstanceFactory.getInstanceFactory(StorageContainer.class);
            if (!Constants.SITE.equals(uiRepOfDomain.getParentContainerSelected())) {
                if (domainObject.getLocatedAtPosition() == null) {
                	InstanceFactory<ContainerPosition> instFact = DomainInstanceFactory.getInstanceFactory(ContainerPosition.class);
                	domainObject.setLocatedAtPosition(instFact.createObject());
                	//domainObject.setLocatedAtPosition(new ContainerPosition());
                }
                if ("Auto".equals(uiRepOfDomain.getParentContainerSelected())) {

                    domainObject.getLocatedAtPosition().setParentContainer(scInstFact.createObject());
                    domainObject.getLocatedAtPosition().getParentContainer().setId(
                            new Long(uiRepOfDomain.getParentContainerId()));

                    domainObject.getLocatedAtPosition().setPositionDimensionOne(
                            new Integer(uiRepOfDomain.getPositionDimensionOne()));
                    domainObject.getLocatedAtPosition().setPositionDimensionTwo(
                            new Integer(uiRepOfDomain.getPositionDimensionTwo()));
                    domainObject.getLocatedAtPosition().setOccupiedContainer(domainObject);
                } else {
                    domainObject.getLocatedAtPosition().setParentContainer(scInstFact.createObject());
                    if (uiRepOfDomain.getContainerId() != null && !uiRepOfDomain.getContainerId().trim().equals("")) {
                        domainObject.getLocatedAtPosition().getParentContainer().setId(
                                new Long(uiRepOfDomain.getContainerId()));
                    } else {
                        domainObject.getLocatedAtPosition().getParentContainer().setName(
                                uiRepOfDomain.getSelectedContainerName());
                    }
                    if (uiRepOfDomain.getPos1() != null && !uiRepOfDomain.getPos1().trim().equals("")
                            && uiRepOfDomain.getPos2() != null && !uiRepOfDomain.getPos2().trim().equals("")) {
                        if (domainObject.getLocatedAtPosition() == null) {
                        	InstanceFactory<ContainerPosition> instFact = DomainInstanceFactory.getInstanceFactory(ContainerPosition.class);
                        	domainObject.setLocatedAtPosition(instFact.createObject());
                            //domainObject.setLocatedAtPosition(new ContainerPosition());
                        }
                        domainObject.getLocatedAtPosition().setPositionDimensionOne(
                                new Integer(uiRepOfDomain.getPos1().trim()));
                        domainObject.getLocatedAtPosition().setPositionDimensionTwo(
                                new Integer(uiRepOfDomain.getPos2().trim()));
                        domainObject.getLocatedAtPosition().setOccupiedContainer(domainObject);
                    }
                }
            } else {
                domainObject.setLocatedAtPosition(null);
                InstanceFactory<Site> instFact = DomainInstanceFactory.getInstanceFactory(Site.class);
                domainObject.setSite(instFact.createObject());
                domainObject.getSite().setId(new Long(uiRepOfDomain.getSiteId()));
                domainObject.getSite().setName(uiRepOfDomain.getSiteName());
                //domainObject.getSite().setCtepId(uiRepOfDomain.getCtepId());
            }
            domainObject.setCollectionProtocolCollection(new HashSet());
            final long[] collectionProtocolArr = uiRepOfDomain.getCollectionIds();
            if (collectionProtocolArr != null) {
            	InstanceFactory<CollectionProtocol> cpInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocol.class);
                for (final long element : collectionProtocolArr) {
                    if (element != -1) {
                        final CollectionProtocol collectionProtocol = cpInstFact.createObject();
                        collectionProtocol.setId(new Long(element));
                        domainObject.getCollectionProtocolCollection().add(collectionProtocol);
                    }
                }
            }
            domainObject.setHoldsStorageTypeCollection(new HashSet());
            final long[] storageTypeArr = uiRepOfDomain.getHoldsStorageTypeIds();
            if (storageTypeArr != null) {
                for (final long element : storageTypeArr) {
                    // logger.debug("Storage Type Id :" + element);
                    if (element != -1) {
                        final StorageType storageType = stTypeInstFact.createObject();//new StorageType();
                        storageType.setId(new Long(element));
                        domainObject.getHoldsStorageTypeCollection().add(storageType);
                    }
                }
            }
            setClassColl(domainObject, uiRepOfDomain);
            setSpTypeColl(domainObject, uiRepOfDomain);
            // holdsSpArrayTypeCollection.clear();
            domainObject.setHoldsSpecimenArrayTypeCollection(new HashSet());
            if (uiRepOfDomain.getSpecimenOrArrayType().equals("SpecimenArray")) {
                final long[] specimenArrayTypeArr = uiRepOfDomain.getHoldsSpecimenArrTypeIds();
                if (specimenArrayTypeArr != null) {
                	InstanceFactory<SpecimenArrayType> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenArrayType.class);
                    for (final long element : specimenArrayTypeArr) {
                        Logger.out.debug("specimen array type Id :" + element);
                        if (element != -1) {
                            final SpecimenArrayType spArrayType = instFact.createObject();//new SpecimenArrayType();
                            spArrayType.setId(new Long(element));
                            domainObject.getHoldsSpecimenArrayTypeCollection().add(spArrayType);
                        }
                    }
                }
            }
           /* if (uiRepOfDomain.getNoOfContainers() > 1) {
                Logger.out.info("--------------------------:" + uiRepOfDomain.getSimilarContainersMap());
                domainObject.setSimilarContainerMap(uiRepOfDomain.getSimilarContainersMap());
            }*/
        } catch (final Exception excp) {
            // StorageContainer.logger.error(excp.getMessage(), excp);
            excp.printStackTrace();
            // final ErrorKey errorKey =
            // ErrorKey.getErrorKey("assign.data.error");
            // throw new AssignDataException(errorKey, null,
            // "StorageContainer.java
            // :");
        }
    }

    /**
     * @param form StorageContainerForm
     * @throws ApplicationException ApplicationException
     */
    private void setSpTypeColl(StorageContainer domainObject, StorageContainerForm form) throws ApplicationException {
        domainObject.setHoldsSpecimenTypeCollection(new HashSet<String>());
        if (form.getSpecimenOrArrayType().equals("Specimen")) {
            StorageContainerUtil.setSpTypeColl(form, domainObject);
        }
    }

    /**
     * @param form StorageContainerForm
     */
    private void setClassColl(StorageContainer domainObject, StorageContainerForm form) {
        domainObject.setHoldsSpecimenClassCollection(new HashSet<String>());
        if (form.getSpecimenOrArrayType().equals("Specimen")) {
            final String[] specimenClassArr = form.getHoldsSpecimenClassTypes();
            if (specimenClassArr != null) {
                for (final String element : specimenClassArr) {
                    // logger.debug("type Id :" + element);
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

}
