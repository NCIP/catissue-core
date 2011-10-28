package edu.wustl.catissuecore.cacore;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.impl.WritableApplicationServiceImpl;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.SDKQueryResult;
import gov.nih.nci.system.query.hibernate.HQLCriteria;
import gov.nih.nci.system.util.ClassCache;
import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CaTissueWritableAppServiceImpl extends WritableApplicationServiceImpl implements CaTissueWritableAppService {

     private static Logger logger = Logger.getCommonLogger(CaTissueWritableAppServiceImpl.class);


    /**
     * Executes CQL query using the
     * gridId as the user
     *
     * @param query  CQL Query
     * @param gridId Grid Id of the requesting user
     * @return List result
     * @throws ApplicationException exception
     */
    public <E> java.util.List<E> executeQuery(CQLQuery query, String gridId) throws ApplicationException {
       final String username = getUsernameFromGridId(gridId);

        // preserve Authentication
        final Authentication originalAuthentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug(">>> original Authentication: " + originalAuthentication);

        // create new Authentication
        final Authentication auth = new UsernamePasswordAuthenticationToken(username, null, null);
        ((UsernamePasswordAuthenticationToken) auth).setDetails(username);

        // replace Authentication
        SecurityContextHolder.getContext().setAuthentication(auth);
        final List<E> result = super.query(query);

        SecurityContextHolder.getContext().setAuthentication(originalAuthentication);
        return result;
    }

     /**
     * Executes HQLCriteria using the
     * gridId as the authenticated user
     *
     * @param criteria  HQL
     * @param gridId Grid Id of the requesting user
     * @return List result
     * @throws ApplicationException exception
     */
    public <E> List<E> executeQuery(HQLCriteria criteria, String gridId) throws ApplicationException {

         final String username = getUsernameFromGridId(gridId);

        // preserve Authentication
        final Authentication originalAuthentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug(">>> original Authentication: " + originalAuthentication);

        // create new Authentication
        final Authentication auth = new UsernamePasswordAuthenticationToken(username, null, null);
        ((UsernamePasswordAuthenticationToken) auth).setDetails(username);

        // replace Authentication
        SecurityContextHolder.getContext().setAuthentication(auth);
        final List<E> result = super.query(criteria);

        SecurityContextHolder.getContext().setAuthentication(originalAuthentication);
        return result;


    }

    public SDKQueryResult executeQuery(SDKQuery query, String gridId) throws ApplicationException {

        final String username = getUsernameFromGridId(gridId);

        // preserve Authentication
        final Authentication originalAuthentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug(">>> original Authentication: " + originalAuthentication);

        // create new Authentication
        final Authentication auth = new UsernamePasswordAuthenticationToken(username, null, null);
        ((UsernamePasswordAuthenticationToken) auth).setDetails(username);

        // replace Authentication
        SecurityContextHolder.getContext().setAuthentication(auth);

        final SDKQueryResult result = super.executeQuery(query);

        logger.info(">>> RESULTS RETURNED:");

        // restore original Authentication
        SecurityContextHolder.getContext().setAuthentication(originalAuthentication);

        return result;
    }


    public String getUsernameFromGridId(String gridId) throws ApplicationException {
        logger.info(">>> INVOKING OPERATION WITH caGrid USER: " + gridId);
        String username;

        try {
            username = AppUtility.resolveIdentity(gridId);
        } catch (BizLogicException e) {
            e.printStackTrace();
            throw new ApplicationException(e.getMessage(), e);
        }

        User user;
        try {
            user = AppUtility.getUser(username);
        } catch (edu.wustl.common.exception.ApplicationException e) {
            logger.debug(e.getCause().toString(), e);
            e.printStackTrace();
            throw new ApplicationException(e.getMessage(), e);
        }

        if (user == null) {
            logger.debug(">>> USER NOT FOUND:" + username);
            throw new ApplicationException("User not found: " + username);
        } else {
            logger.info(">>> FOUND USER: " + user.getLoginName() + ", " + user);
        }
        return username;
    }

    public CaTissueWritableAppServiceImpl(ClassCache classCache) {
        super(classCache);
        logger.info(">>> constructor: CaTissueWritableAppServiceImpl");
    }

    public String getCaTissueServerProperty(String key) throws ApplicationException {
        return (String) new CaTissueCoreDelegatorInvoker().callDelegator("getCaTissueServerProperty", key);
    }

    public String getSpecimenCollectionGroupURL(Object urlInformationObject) throws ApplicationException {
        return (String) new CaTissueCoreDelegatorInvoker().callDelegator("getSpecimenCollectionGroupURL", urlInformationObject);
    }

    public String getVisitInformationURL(Object urlInformationObject) throws ApplicationException {
        return (String) new CaTissueCoreDelegatorInvoker().callDelegator("getVisitInformationURL", urlInformationObject);
    }

    public Object getVisitRelatedEncounteredDate(Map<String, Long> map) throws ApplicationException {
        return new CaTissueCoreDelegatorInvoker().callDelegator("getVisitRelatedEncounteredDate", map);
    }

    public Object registerParticipant(Object object, Long cpid) throws ApplicationException {
        String userName = CaTissueCoreAuthenticationUtil.retreiveUsernameFromSecurityContext();
        return new CaTissueCoreDelegatorInvoker().callDelegator("registerParticipant", object, cpid, userName);
    }

    public List<Object> getCaTissueLocalParticipantMatchingObects(Object object, Set<Long> cpIdSet) throws ApplicationException {
        return (List<Object>) new CaTissueCoreDelegatorInvoker().callDelegator("getCaTissueLocalParticipantMatchingObects", object, cpIdSet);
    }

    public List<Object> getParticipantMatchingObects(Object object) throws ApplicationException {
        String userName = CaTissueCoreAuthenticationUtil.retreiveUsernameFromSecurityContext();
        return (List<Object>) new CaTissueCoreDelegatorInvoker().callDelegator("getParticipantMatchingObects", object, userName);
    }

    public String getSpecimenCollectionGroupLabel(Object object) throws ApplicationException {
        return (String) new CaTissueCoreDelegatorInvoker().callDelegator("getSpecimenCollectionGroupLabel", object);
    }

}
