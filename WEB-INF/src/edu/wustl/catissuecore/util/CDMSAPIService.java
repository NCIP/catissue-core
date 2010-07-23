package edu.wustl.catissuecore.util;

import edu.wustl.catissuecore.util.global.CDMSIntegrationConstants;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 *
 * @author shital_lawhale
 * @version
 */
public class CDMSAPIService
{
    private ApplicationService appService = null;
    private ClientSession clientSession = null;
	private final String CPServerUrl = XMLPropertyHandler
			.getValue(CDMSIntegrationConstants.CLINPORTAL_SERVICE_URL);
	private final String ctmsKeystorePath = XMLPropertyHandler
			.getValue(CDMSIntegrationConstants.KEYSTORE_FILE_PATH);

    /**
     * Method to initialize the CaCoreAPIService
     * @param loginName
     * @param password
     * @throws Exception
     */
    public void initialize(final String loginName,final String password) throws Exception
    {
        try
        {
        	if(!Validator.isEmpty(ctmsKeystorePath))
        	{
        		System.setProperty("javax.net.ssl.trustStore", ctmsKeystorePath);
        	}
            final HostnameVerifier hostVerifier = new HostnameVerifier()
            {
                public boolean verify(final String urlHostName,final SSLSession session)
                {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hostVerifier);
            appService = ApplicationServiceProvider.getRemoteInstance(CPServerUrl);
            clientSession = ClientSession.getInstance();
            clientSession.startSession(loginName, password);
        }
        catch (Exception ex)
        {
            Logger.out.error("Error in initializing CaCoreAPIService " + ex);
            Logger.out.error("Test client throws Exception = " + ex);
            throw ex;
        }
    }

    /**
     *
     * @param loginName
     * @param cpId
     * @param participantId
     * @param cpeId
     * @param scgId
     * @return
     * @throws Exception
     */
    public Map getClinPortalURLIds(final String loginName,final Long cpId,final  Long participantId,final Long cpeId,final Long scgId) throws Exception
    {
        Map map = new HashMap();

        map.put(CDMSIntegrationConstants.COLLECTION_PROTOCOL_ID,cpId );
        map.put(CDMSIntegrationConstants.PARTICIPANT_ID,participantId);
        map.put(CDMSIntegrationConstants.COLL_PROTOCOL_EVENT_ID, cpeId);
        map.put(CDMSIntegrationConstants.SCGID,scgId);

        Date currDate=null;
        Date prevDate=null;
        if(scgId!=null)
        {
              Map scgDateMap = new CDMSCaTissueIntegrationUtil().getSCGRelatedEncounteredDate_OLD(cpId, participantId,cpeId,scgId,currDate, prevDate);
              map.putAll(scgDateMap);
        }
        try
        {
            final String pwd=CDMSCaTissueIntegrationUtil.getPassword(loginName);
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
     *
     * @param loginName
     * @param visitId
     * @param cpeId
     * @param cpId
     * @param participantId
     * @return
     * @throws Exception
     */
    public Map<String, Date> getVisitRelatedEncounteredDate(final String loginName,final Long visitId,final Long cpeId,final Long cpId,final  Long participantId) throws Exception
    {
        final Map<String,Long>  map = new HashMap<String,Long>();
        Map<String,Date>  dateMap=null;
        try
        {
            map.put(CDMSIntegrationConstants.COLL_PROTOCOL_EVENT_ID, cpeId);
            map.put(CDMSIntegrationConstants.EVENTENTRYID,visitId);
            map.put(CDMSIntegrationConstants.COLLECTION_PROTOCOL_ID,cpId );
            map.put(CDMSIntegrationConstants.PARTICIPANT_ID,participantId);
            final String pwd=CDMSCaTissueIntegrationUtil.getPassword(loginName);
            initialize(loginName, pwd);
            dateMap=(Map) appService.getVisitRelatedEncounteredDate( map);
            terminateSession();
        }
		catch (ApplicationException e)
		{
			Logger.out
					.error("Error in getVisitRelatedEncounteredDate method from CaCoreAPIService "
							+ e);
			throw e;
		}
        return dateMap;
    }

    private void terminateSession()
    {
        clientSession.terminateSession();
    }
}
