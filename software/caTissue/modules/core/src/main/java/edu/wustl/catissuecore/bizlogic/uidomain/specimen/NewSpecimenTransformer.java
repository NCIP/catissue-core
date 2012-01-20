package edu.wustl.catissuecore.bizlogic.uidomain.specimen;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

@InputUIRepOfDomain(NewSpecimenForm.class)
public class NewSpecimenTransformer extends SpecimenTransformer<NewSpecimenForm> {
    private static Logger logger = Logger.getCommonLogger(NewSpecimenTransformer.class);

    @Override
    public void overwriteDomainObject(Specimen domainObject, NewSpecimenForm uiRepOfDomain) {
        super.overwriteDomainObject(domainObject, uiRepOfDomain);
        try {
            if (!(uiRepOfDomain.getSpecimenCollectionGroupId() == null && uiRepOfDomain.getSpecimenCollectionGroupId()
                    .equals(""))) {
                domainObject.getSpecimenCollectionGroup().setId(
                        Long.valueOf(uiRepOfDomain.getSpecimenCollectionGroupId()));
            }
            if (uiRepOfDomain.getSpecimenCollectionGroupName() != null
                    && !uiRepOfDomain.getSpecimenCollectionGroupName().equals("")) {
                domainObject.getSpecimenCollectionGroup().setName(uiRepOfDomain.getSpecimenCollectionGroupName());
            }
            /** For Migration End* */
            domainObject.setActivityStatus(uiRepOfDomain.getActivityStatus());
            if (uiRepOfDomain.isAddOperation()) {
                domainObject.setCollectionStatus(Constants.COLLECTION_STATUS_COLLECTED);
            } else {
                domainObject.setCollectionStatus(uiRepOfDomain.getCollectionStatus());
            }

            String barcode = uiRepOfDomain.getBarcode();
            if(!Validator.isEmpty(barcode)){
                domainObject.setBarcode(barcode);
            }

            domainObject.setComment(uiRepOfDomain.getComments());
            domainObject.setSpecimenClass(uiRepOfDomain.getClassName());
            domainObject.setSpecimenType(uiRepOfDomain.getType());


            if (uiRepOfDomain.isAddOperation()) {
                domainObject.setIsAvailable(Boolean.TRUE);
            } else {
                domainObject.setIsAvailable(Boolean.valueOf(uiRepOfDomain.isAvailable()));
            }

            // in case of edit
           /* if (!uiRepOfDomain.isAddOperation()) {
                // specimen is a new specimen
                if (domainObject.getParentSpecimen() == null) {
                    final String parentSpecimenId = uiRepOfDomain.getParentSpecimenId();
                    // specimen created from another specimen
                    if (parentSpecimenId != null && !parentSpecimenId.trim().equals("")
                            && Long.parseLong(parentSpecimenId) > 0) {
                        domainObject.setParentChanged(true);
                    }
                } else
                // specimen created from another specimen
                {
                    if (!((Specimen) domainObject.getParentSpecimen()).getLabel().equalsIgnoreCase(
                            uiRepOfDomain.getParentSpecimenName())) {
                        domainObject.setParentChanged(true);
                    }
                }
                *//**
                 * Patch ID: 3835_1_3 See also: 1_1 to 1_5 Description : Set
                 * createdOn date in edit mode for new specimen
                 *//*
                domainObject.setCreatedOn(CommonUtilities.parseDate(uiRepOfDomain.getCreatedDate(),
                        CommonServiceLocator.getInstance().getDatePattern()));
            }*/

           // logger.debug("isParentChanged " + domainObject.isParentChanged());

            // Setting the SpecimenCharacteristics
            domainObject.setPathologicalStatus(uiRepOfDomain.getPathologicalStatus());
            domainObject.getSpecimenCharacteristics().setTissueSide(uiRepOfDomain.getTissueSide());
            domainObject.getSpecimenCharacteristics().setTissueSite(uiRepOfDomain.getTissueSite());

            // Getting the Map of External Identifiers
            final Map<String, String> extMap = uiRepOfDomain.getExternalIdentifier();

            MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");

            final Collection extCollection = parser.generateData(extMap);
            domainObject.setExternalIdentifierCollection(extCollection);

            Map<String, String> bioMap = uiRepOfDomain.getBiohazard();
            logger.debug("PRE FIX MAP " + bioMap);
            bioMap = fixMap(bioMap);
            logger.debug("POST FIX MAP " + bioMap);

            // Getting the Map of Biohazards
            parser = new MapDataParser("edu.wustl.catissuecore.domain");
            final Collection bioCollection = parser.generateData(bioMap);

            logger.debug("BIO-COL : " + bioCollection);

            domainObject.setBiohazardCollection(bioCollection);


            if (uiRepOfDomain.isAddOperation())
            {//||(uiRepOfDomain.getOperation().equals("edit")&&uiRepOfDomain.getTransferStatus().equals("transferDone"))) {
                setSpecimenPosition(domainObject, uiRepOfDomain);
            } else {
                if (domainObject.getSpecimenPosition() == null) {
                	InstanceFactory<SpecimenPosition> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenPosition.class);
                    domainObject.setSpecimenPosition(instFact.createObject());
                    InstanceFactory<StorageContainer> scInstFact = DomainInstanceFactory.getInstanceFactory(StorageContainer.class);
                    domainObject.getSpecimenPosition().setStorageContainer(scInstFact.createObject());

                    if (uiRepOfDomain.getStContSelection() == 1) {
                        domainObject.setSpecimenPosition(null);
                    }
                    if (uiRepOfDomain.getStContSelection() == 2) {
                        final long stContainerId = Long.parseLong(uiRepOfDomain.getStorageContainer());

                        /*
                         * if (domainObject.getSpecimenPosition() == null ||
                         * domainObject.getSpecimenPosition().storageContainer ==
                         * null) { domainObject.specimenPosition = new
                         * SpecimenPosition();
                         * domainObject.getSpecimenPosition().storageContainer =
                         * new StorageContainer(); }
                         */
                        domainObject.getSpecimenPosition().getStorageContainer().setId(stContainerId);
                        domainObject.getSpecimenPosition().setPositionDimensionOne(
                                Integer.valueOf(uiRepOfDomain.getPositionDimensionOne()));
                        domainObject.getSpecimenPosition().setPositionDimensionTwo(
                                Integer.valueOf(uiRepOfDomain.getPositionDimensionTwo()));
                        domainObject.getSpecimenPosition().setSpecimen(domainObject);
                    } else if (uiRepOfDomain.getStContSelection() == 3) {

                        if (uiRepOfDomain.getPos1() != null && !uiRepOfDomain.getPos1().trim().equals("")
                                && uiRepOfDomain.getPos2() != null && !uiRepOfDomain.getPos2().trim().equals("")) {
                            /*
                             * if (domainObject.getSpecimenPosition() == null ||
                             * domainObject.getSpecimenPosition().storageContainer ==
                             * null) { domainObject.specimenPosition = new
                             * SpecimenPosition();
                             * domainObject.getSpecimenPosition().storageContainer =
                             * new StorageContainer(); }
                             */
                            domainObject.getSpecimenPosition().getStorageContainer().setName(
                                    uiRepOfDomain.getSelectedContainerName());
                            domainObject.getSpecimenPosition().setPositionDimensionOne(
                                    Integer.valueOf(uiRepOfDomain.getPos1()));
                            domainObject.getSpecimenPosition().setPositionDimensionTwo(
                                    Integer.valueOf(uiRepOfDomain.getPos2()));
                            domainObject.getSpecimenPosition().setSpecimen(domainObject);
                        }
                        // bug 11479 S
                        else {
                            domainObject.getSpecimenPosition().getStorageContainer().setName(
                                    uiRepOfDomain.getSelectedContainerName());
                        }
                    } else {
                        domainObject.setSpecimenPosition(null);
                    }
                } else {
                    domainObject.getSpecimenPosition().getStorageContainer().setName(
                            uiRepOfDomain.getSelectedContainerName());
                    domainObject.getSpecimenPosition().setPositionDimensionOne(
                            Integer.valueOf(uiRepOfDomain.getPositionDimensionOne()));
                    domainObject.getSpecimenPosition().setPositionDimensionTwo(
                            Integer.valueOf(uiRepOfDomain.getPositionDimensionTwo()));
                    domainObject.getSpecimenPosition().setSpecimen(domainObject);
                }
            }
            if (uiRepOfDomain.isParentPresent()) {
                logger.info(Constants.DOUBLE_QUOTES);
                /*
                 * lazy change parent Specimen link is set to false so not
                 * required to set
                 */
                /*
                 * parentSpecimen = new CellSpecimen(); parentSpecimen.setId(new
                 * Long(uiRepOfDomain.getParentSpecimenId()));
                 * parentSpecimen.setLabel(uiRepOfDomain.getParentSpecimenName());
                 */
            } else {
                domainObject.setParentSpecimen(null);
                // specimenCollectionGroup = null;
                domainObject.getSpecimenCollectionGroup().setId(
                        Long.valueOf(uiRepOfDomain.getSpecimenCollectionGroupId()));
                // domainObject.getSpecimenCollectionGroup().setGroupName
                // (uiRepOfDomain.getSpecimenCollectionGroupName());
                /* lazy change */
                /*
                 * IBizLogic iBizLogic =
                 * BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
                 * List scgList =
                 * iBizLogic.retrieve(SpecimenCollectionGroup.class.getName(),
                 * "name", uiRepOfDomain.getSpecimenCollectionGroupName()); if
                 * (!scgList.isEmpty()) { domainObject.specimenCollectionGroup =
                 * (SpecimenCollectionGroup) scgList.get(0); }
                 * if(parentSpecimen.getSpecimenCollectionGroup()!=null) {
                 * domainObject.specimenCollectionGroup =
                 * parentSpecimen.getSpecimenCollectionGroup(); }
                 */
            }

        } catch (final Exception excp) {
            // Specimen.logger.error(excp.getMessage(), excp);
            excp.printStackTrace();
            // final ErrorKey errorKey =
            // ErrorKey.getErrorKey("assign.data.error");
            // throw new AssignDataException(errorKey, null, "Specimen.java :");
            //
        }

        domainObject.setConsentTierStatusCollection(prepareParticipantResponseCollection(uiRepOfDomain));
        // ----------- Mandar --16-Jan-07
       // domainObject.setConsentWithdrawalOption(uiRepOfDomain.getWithdrawlButtonStatus());
        // ----- Mandar : ---23-jan-07 For bug 3464.
       // domainObject.setApplyChangesTo(uiRepOfDomain.getApplyChangesTo());
    }

    /**
     * For Consent Tracking. Setting the Domain Object.
     *
     * @param form CollectionProtocolRegistrationForm.
     * @return consentResponseColl.
     */
    private Collection<ConsentTierStatus> prepareParticipantResponseCollection(NewSpecimenForm form) {
        final MapDataParser mapdataParser = new MapDataParser("edu.wustl.catissuecore.bean");
        Collection<ConsentBean> beanObjColl = null;
        try {
            beanObjColl = mapdataParser.generateData(ConsentUtil.updateConsentMap(form.getConsentResponseForSpecimenValues()));
        } catch (final Exception e) {
            // Specimen.logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        final Iterator<ConsentBean> iter = beanObjColl.iterator();
        final Collection<ConsentTierStatus> consentResponseColl = new HashSet<ConsentTierStatus>();
        while (iter.hasNext()) {
            final ConsentBean consentBean = (ConsentBean) iter.next();
            final ConsentTierStatus consentTierstatus = (ConsentTierStatus)DomainInstanceFactory.getInstanceFactory(ConsentTierStatus.class).createObject();//new ConsentTierStatus();
            // Setting response
            consentTierstatus.setStatus(consentBean.getSpecimenLevelResponse());
            if (consentBean.getSpecimenLevelResponseID() != null
                    && consentBean.getSpecimenLevelResponseID().trim().length() > 0) {
                consentTierstatus.setId(Long.parseLong(consentBean.getSpecimenLevelResponseID()));
            }
            // Setting consent tier
            final ConsentTier consentTier = (ConsentTier)DomainInstanceFactory.getInstanceFactory(ConsentTier.class).createObject();//new ConsentTier();
            consentTier.setId(Long.valueOf(consentBean.getConsentTierID()));
            consentTierstatus.setConsentTier(consentTier);
            consentResponseColl.add(consentTierstatus);
        }
        return consentResponseColl;
    }

    /**
     * fixMap.
     *
     * @param orgMap Map.
     * @return Map.
     */
    private Map<String, String> fixMap(Map<String, String> orgMap) {
        final Map<String, String> newMap = new HashMap<String, String>();
        final Iterator<String> iterator = orgMap.keySet().iterator();
        while (iterator.hasNext()) {
            final String key = iterator.next();
            // Logger.out.debug("key "+key);

            if (key.indexOf("persisted") == -1) {
                final String value = String.valueOf(orgMap.get(key));
                newMap.put(key, value);
            }
        }
        return newMap;
    }
}
