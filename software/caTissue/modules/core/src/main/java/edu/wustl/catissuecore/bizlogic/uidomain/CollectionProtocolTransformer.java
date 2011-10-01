package edu.wustl.catissuecore.bizlogic.uidomain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.domain.ClinicalDiagnosis;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.factory.utils.CollectionProtocolUtility;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.util.KeyComparator;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

@InputUIRepOfDomain(CollectionProtocolForm.class)
public class CollectionProtocolTransformer
        extends
            AbstractSpecimenProtocolTransformer<CollectionProtocolForm, CollectionProtocol> {
    private static Logger logger = Logger.getCommonLogger(CollectionProtocolTransformer.class);

    public CollectionProtocol createDomainObject(CollectionProtocolForm uiRepOfDomain) {
    	InstanceFactory<CollectionProtocol> cpInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocol.class);
        CollectionProtocol cp = cpInstFact.createObject();
        overwriteDomainObject(cp, uiRepOfDomain);
        return cp;
    }

    @Override
    public void overwriteDomainObject(CollectionProtocol domainObject, CollectionProtocolForm uiRepOfDomain) {
        super.overwriteDomainObject(domainObject, uiRepOfDomain);
        try {
        	if(domainObject.getCoordinatorCollection()==null)
        	{
        		domainObject.setCoordinatorCollection(new LinkedHashSet<User>());
        	}
            domainObject.getCoordinatorCollection().clear();
            if(domainObject.getSiteCollection()==null)
    		{
            	domainObject.setSiteCollection(new HashSet<Site>());
    		}
            domainObject.getSiteCollection().clear();
            if(domainObject.getCoordinatorCollection()==null)
        	{
        		domainObject.setCollectionProtocolEventCollection(new LinkedHashSet<CollectionProtocolEvent>());
        	}
            domainObject.getCollectionProtocolEventCollection().clear();
            if( domainObject.getClinicalDiagnosisCollection()==null)
    		{
            	 domainObject.setClinicalDiagnosisCollection(new LinkedHashSet<ClinicalDiagnosis>());
    		}
            domainObject.getClinicalDiagnosisCollection().clear();

            /** For Clinical Diagnosis Subset * */
            final String[] clinicalDiagnosisArr = uiRepOfDomain.getProtocolCoordinatorIds();
            if (clinicalDiagnosisArr != null) {
                for (final String clinicalDiagnosis : clinicalDiagnosisArr) {
                    if (!"".equals(clinicalDiagnosis)) {
                        final ClinicalDiagnosis clinicalDiagnosisObj = (ClinicalDiagnosis)DomainInstanceFactory.getInstanceFactory(ClinicalDiagnosis.class).createObject();//new ClinicalDiagnosis();
                        clinicalDiagnosisObj.setName(clinicalDiagnosis);
                        clinicalDiagnosisObj.setCollectionProtocol(domainObject);
                        domainObject.getClinicalDiagnosisCollection().add(clinicalDiagnosisObj);
                    }
                }
            }

            final long[] coordinatorsArr = uiRepOfDomain.getCoordinatorIds();
            if (coordinatorsArr != null) {
                for (final long element : coordinatorsArr) {
                    if (element != -1) {
                    	InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
                        final User coordinator = instFact.createObject();
                        coordinator.setId(new Long(element));
                        domainObject.getCoordinatorCollection().add(coordinator);
                    }
                }
            }

            final long[] siteArr = uiRepOfDomain.getSiteIds();
            InstanceFactory<Site> instFact = DomainInstanceFactory.getInstanceFactory(Site.class);
            if (siteArr != null) {
                for (final long element : siteArr) {
                    if (element != -1) {
                        final Site site = instFact.createObject();
                        site.setId(new Long(element));
                        domainObject.getSiteCollection().add(site);
                    }
                }
            }
            domainObject.setAliquotInSameContainer(new Boolean(uiRepOfDomain.isAliqoutInSameContainer()));
            final Map map = uiRepOfDomain.getValues();

            /**
             * Name : Abhishek Mehta Reviewer Name : Poornima Bug ID:
             * Collection_Event_Protocol_Order Patch ID:
             * Collection_Event_Protocol_Order_1 See also: 1-8 Description: To
             * get the collection event protocols in their insertion order.
             */
            logger.debug("PRE FIX MAP " + map);
            final Map sortedMap = sortMapOnKey(map);
            logger.debug("POST FIX MAP " + map);

            final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");

            final ArrayList cpecList = (ArrayList) parser.generateData(sortedMap, true);
            for (int i = 0; i < cpecList.size(); i++) {
                domainObject.getCollectionProtocolEventCollection().add((CollectionProtocolEvent) cpecList.get(i));
            }
            logger.debug("collectionProtocolEventCollection " + domainObject.getCollectionProtocolEventCollection());

            // Setting the unsigned doc url.
            domainObject.setUnsignedConsentDocumentURL(uiRepOfDomain.getUnsignedConsentURLName());
            // Setting the consent tier collection.
            //domainObject.setConsentTierCollection(CollectionProtocolUtil.prepareConsentTierMap(uiRepOfDomain.getConsentValues()));
            domainObject.setConsentTierCollection(CollectionProtocolUtility.prepareConsentTierCollection(uiRepOfDomain.getConsentValues()));

            domainObject.setConsentsWaived(new Boolean(uiRepOfDomain.isConsentWaived()));

            if(uiRepOfDomain.isDirtyEditFlag()){
				domainObject.setDirtyEditFlag(new Boolean(true));
	        }else {
	        	domainObject.setDirtyEditFlag(new Boolean(false));
	        }
	        if(uiRepOfDomain.isRemoteManagedFlag()){
	        	domainObject.setRemoteManagedFlag(new Boolean(true));
	        }else {
	        	domainObject.setRemoteManagedFlag(new Boolean(false));
	        }
	        if(uiRepOfDomain.getRemoteId() !=0) {
	        	domainObject.setRemoteId(uiRepOfDomain.getRemoteId());
	        }else {
	        	domainObject.setRemoteId(null);
	        }

        } catch (final Exception excp) {
            // use of logger as per bug 79
            // CollectionProtocol.logger.error(excp.getMessage(), excp);
            excp.printStackTrace();
            // final ErrorKey errorKey =
            // ErrorKey.getErrorKey("assign.data.error");
            // throw new AssignDataException(errorKey, null,
            // "CollectionProtocol.java :");
        }
    }

    /**
     * This function will sort the map based on their keys.
     *
     * @param map Map.
     * @return LinkedHashMap.
     */
    private Map sortMapOnKey(Map map) {
        final Object[] mapKeySet = map.keySet().toArray();
        final int size = mapKeySet.length;
        final ArrayList<String> mList = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            final String key = (String) mapKeySet[i];
            mList.add(key);
        }

        final KeyComparator keyComparator = new KeyComparator();
        Collections.sort(mList, keyComparator);

        final LinkedHashMap<String, String> sortedMap = new LinkedHashMap<String, String>();
        for (int i = 0; i < size; i++) {
            final String key = mList.get(i);
            final String value = (String) map.get(key);
            sortedMap.put(key, value);
        }
        return sortedMap;
    }
}
