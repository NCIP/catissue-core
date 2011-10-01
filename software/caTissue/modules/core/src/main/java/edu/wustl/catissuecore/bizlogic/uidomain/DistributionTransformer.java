package edu.wustl.catissuecore.bizlogic.uidomain;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.DistributionForm;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;

@InputUIRepOfDomain(DistributionForm.class)
public class DistributionTransformer implements UIDomainTransformer<DistributionForm, Distribution> {
    private static Logger logger = Logger.getCommonLogger(DistributionTransformer.class);

    public Distribution createDomainObject(DistributionForm uiRepOfDomain) {
        Distribution distribution = (Distribution)DomainInstanceFactory.getInstanceFactory(Distribution.class).createObject();//new Distribution();
        overwriteDomainObject(distribution, uiRepOfDomain);
        return distribution;
    }

    public void overwriteDomainObject(Distribution domainObject, DistributionForm uiRepOfDomain) {
        try {

            try {
                if (SearchUtil.isNullobject(domainObject.getDistributedBy())) {
                	InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
                    domainObject.setDistributedBy(instFact.createObject());
                }
                if (SearchUtil.isNullobject(domainObject.getTimestamp())) {
                    domainObject.setTimestamp(Calendar.getInstance().getTime());
                }

                domainObject.setComment(uiRepOfDomain.getComments());

                domainObject.getDistributedBy().setId(Long.valueOf(uiRepOfDomain.getUserId()));

                if (uiRepOfDomain.getDateOfEvent() != null && uiRepOfDomain.getDateOfEvent().trim().length() != 0) {
                    final Calendar calendar = Calendar.getInstance();

                    final Date date = CommonUtilities.parseDate(uiRepOfDomain.getDateOfEvent(), CommonUtilities
                            .datePattern(uiRepOfDomain.getDateOfEvent()));
                    calendar.setTime(date);
                    domainObject.setTimestamp(calendar.getTime());
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(uiRepOfDomain.getTimeInHours()));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(uiRepOfDomain.getTimeInMinutes()));
                    domainObject.setTimestamp(calendar.getTime());
                    // domainObject.getTimestamp() is added twice, if there is
                    // some
                    // exception in
                    // Integer.parseInt(uiRepOfDomain.getTimeInHours()) or
                    // Integer.
                    // parseInt(uiRepOfDomain.getTimeInMinutes())
                    // current timestamp will be set
                }

            } catch (final Exception excp) {
                // Distribution.logger.error(excp.getMessage(), excp);
                excp.printStackTrace();
                // final ErrorKey errorKey =
                // ErrorKey.getErrorKey("assign.data.error");
                // throw new AssignDataException(errorKey, null,
                // "Distribution.java :");
            }

            if (SearchUtil.isNullobject(domainObject.getToSite())) {
            	InstanceFactory<Site> instFact = DomainInstanceFactory.getInstanceFactory(Site.class);
                domainObject.setToSite(instFact.createObject());
            }

            if (SearchUtil.isNullobject(domainObject.getDistributionProtocol())) {
            	InstanceFactory<DistributionProtocol> instFact = DomainInstanceFactory.getInstanceFactory(DistributionProtocol.class);
                domainObject.setDistributionProtocol(instFact.createObject());
            }

            domainObject.getToSite().setId(Long.valueOf(uiRepOfDomain.getToSite()));
            // fromSite.setId(new Long(uiRepOfDomain.getFromSite()));
            domainObject.getDistributionProtocol().setId(Long.valueOf(uiRepOfDomain.getDistributionProtocolId()));
            domainObject.setActivityStatus(uiRepOfDomain.getActivityStatus());

            Map map = uiRepOfDomain.getValues();
            logger.debug("map " + map);
            map = fixMap(map);
            logger.debug("fixedMap " + map);
            final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
            final Collection itemCollectionMap = parser.generateData(map);
            final Collection finalItemCollecitonMap = new HashSet();
            final Iterator itr = itemCollectionMap.iterator();
            while (itr.hasNext()) {
                final DistributedItem distributedItem = (DistributedItem) itr.next();
                if (distributedItem.getSpecimen() != null) {
                    finalItemCollecitonMap.add(distributedItem);
                }
                if (distributedItem.getSpecimenArray() != null) {
                    finalItemCollecitonMap.add(distributedItem);
                }
            }
            domainObject.setDistributedItemCollection(finalItemCollecitonMap);
            logger.debug("distributedItemCollection " + domainObject.getDistributedItemCollection());

        } catch (final Exception excp) {
            // Distribution.logger.error(excp.getMessage(), excp);
            excp.printStackTrace();
        }
    }

    /**
     * Fix Map.
     *
     * @param orgMap Map.
     * @return Map.
     */
    protected Map fixMap(Map orgMap) {
        final Iterator iterator = orgMap.keySet().iterator();
        final Map newMap = new HashMap();
        while (iterator.hasNext()) {
            final String key = (String) iterator.next();
            final String value = (String) orgMap.get(key);
            if (key.endsWith("_id") || key.endsWith("uantity")) {
                newMap.put(key, value);
            }
        }
        return newMap;
    }
}
