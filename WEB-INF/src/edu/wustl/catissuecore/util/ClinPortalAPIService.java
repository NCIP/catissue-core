package edu.wustl.catissuecore.util;

import edu.wustl.catissuecore.util.global.ClinPortalIntegrationConstants;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

import java.util.HashMap;
import java.util.Map;


public class ClinPortalAPIService
{
    private ApplicationService appService = null;
    private ClientSession cs = null;
    private String clinPortalServerUrl = XMLPropertyHandler.getValue(ClinPortalIntegrationConstants.CLINPORTAL_SERVICE_URL);
    private String keyPath = XMLPropertyHandler.getValue(ClinPortalIntegrationConstants.KEYSTORE_FILE_PATH);

    /**
     * Method to initialize the CaCoreAPIService
     * @throws Exception
     */
    public void initialize(String loginName, String password) throws Exception
    {
        try
        {
            System.setProperty("javax.net.ssl.trustStore", keyPath);
            appService = ApplicationServiceProvider.getRemoteInstance(clinPortalServerUrl);
            cs = ClientSession.getInstance();
            cs.startSession(loginName, password);
        }
        catch (Exception ex)
        {
            Logger.out.error("Error in initializing CaCoreAPIService " + ex);
            Logger.out.error("Test client throws Exception = " + ex);
            throw ex;
        }
    }


    public Map<String,Long>  getClinPortalURLIds(String loginName,Long cpId, Long participantId,Long cpeId) throws Exception
    {
        Map<String,Long> map = new HashMap<String,Long>();
        map.put(ClinPortalIntegrationConstants.COLLECTION_PROTOCOL_ID,cpId );
        map.put(ClinPortalIntegrationConstants.PARTICIPANT_ID,participantId);
        map.put(ClinPortalIntegrationConstants.COLL_PROTOCOL_EVENT_ID, cpeId);

        try
        {
            String pwd=ClinPortalCaTissueIntegrationUtil.getPassword(loginName);
            initialize(loginName, pwd);
            map=(Map)appService.getClinportalUrlIds(map);
            terminateSession();
        }
        catch (ApplicationException e)
        {
            Logger.out.error("Error in getting ClinPortalURLIds CaCoreAPIService " + e);
            throw e;
        }
        return map;
     }

    /**
     * @param visitId
     * @param scgId
     */
    public void associateVisitAndScg(String loginName,String visitId,String scgId) throws Exception
    {
        try
        {
            final String pwd=ClinPortalCaTissueIntegrationUtil.getPassword(loginName);
            initialize(loginName, pwd);
            appService.associateVisitAndScg( visitId, scgId);
            terminateSession();
        }
        catch (ApplicationException e)
        {
            Logger.out.error("Error in associateVisitAndScg CaCoreAPIService " + e);
            throw e;
        }
    }

    private void terminateSession() throws Exception
    {
        cs.terminateSession();

    }
}
