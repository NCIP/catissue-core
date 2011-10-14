
package edu.wustl.catissuecore.cdms.integrator;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import edu.wustl.catissuecore.util.CDMSCaTissueIntegrationUtil;
import edu.wustl.catissuecore.util.global.CDMSIntegrationConstants;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

public class CdmsIntegratorImpl extends CatissueCdmsIntegrator
{

	private transient ApplicationService applicationService = null;
	private transient ClientSession clientSession = null;
	private final transient String ctmsServiceURL = XMLPropertyHandler
			.getValue(CDMSIntegrationConstants.CDMS_SERVICE_URL);
	private final transient String ctmsKeystorePath = XMLPropertyHandler
			.getValue(CDMSIntegrationConstants.CDMS_KEYSTORE_FILE_PATH);

	public CdmsIntegratorImpl() throws Exception
	{
		super();
	}

	public CdmsIntegratorImpl(Properties properties) throws Exception
	{
		super(properties);
	}

	public String getSpecimenCollectionGroupURL(
			CatissueCdmsURLInformationObject urlInformationObject) throws Exception
	{
		StringBuffer url = new StringBuffer();
		Long cpId = Long.valueOf(urlInformationObject.getCollectionProtocolIdentifier());
		if (cpId != null && cpId != 0)
		{
			String caTissueUrl = urlInformationObject.getUrl();
			url.append(getProperCaTissueurl(caTissueUrl));
			url.append(CDMSIntegrationConstants.DEDATAENTRYACTION).append(
					CDMSIntegrationConstants.DO).append('?').append(
					CDMSIntegrationConstants.PARTICIPANT_ID)
					.append(CDMSIntegrationConstants.EQUALS).append(
							urlInformationObject.getParticipantIdentifier());
			url.append(formReqParameter(CDMSIntegrationConstants.COLL_PROTOCOL_EVENT_ID, String
					.valueOf(urlInformationObject.getCollectionProtocolEventIdentifier())));
			url.append(formReqParameter(CDMSIntegrationConstants.COLLECTION_PROTOCOL_ID, String
					.valueOf(cpId)));
			url.append(formReqParameter(CDMSIntegrationConstants.CSM_USER_ID, urlInformationObject
					.getUserCSMIdentifier()));
			url.append("&method=SCG");
		}
		return url.toString();
	}

	public String getProperCaTissueurl(String caTissueUrl)
	{
		if (caTissueUrl.contains(".do"))
		{
			int lastIndex = caTissueUrl.lastIndexOf('/');
			caTissueUrl = caTissueUrl.substring(0, lastIndex);
		}
		caTissueUrl = caTissueUrl + "/";
		return caTissueUrl;
	}

	private String formReqParameter(String parameterName, String value)
	{
		StringBuffer str = new StringBuffer();
		str.append('&').append(parameterName).append('=').append(value);
		return str.toString();
	}

	public String getVisitInformationURL(CatissueCdmsURLInformationObject urlInformationObject,
			String loginName, String password) throws Exception
	{
		initialize(loginName, password);
		new CDMSCaTissueIntegrationUtil().getSCGRelatedEncounteredDate(urlInformationObject);
		Map infoMap= new HashMap();
		infoMap.put(CDMSIntegrationConstants.COLLECTION_PROTOCOL_ID, urlInformationObject.getCollectionProtocolIdentifier());
		infoMap.put(CDMSIntegrationConstants.COLL_PROTOCOL_EVENT_ID, urlInformationObject.getCollectionProtocolEventIdentifier());
		infoMap.put(CDMSIntegrationConstants.PARTICIPANT_ID, urlInformationObject.getParticipantIdentifier());
		infoMap.put(CDMSIntegrationConstants.PREV_SCG_DATE, urlInformationObject.getPreviousSpecimenCollectionGroupDate());
		infoMap.put(CDMSIntegrationConstants.RECENT_SCG_DATE, urlInformationObject.getRecentSpecimenCollectionGroupDate());
		Map<String, Long> map = (Map<String,Long>)applicationService.getClinportalUrlIds(infoMap);
		StringBuilder url = new StringBuilder();
		Long scgID = Long.valueOf(urlInformationObject.getSpecimenCollectionGroupIdentifier());
		if(CDMSCaTissueIntegrationUtil.validateClinPortalMap(map))
		{
            url.append(CDMSIntegrationConstants.CDMS_URL_CONTEXT(urlInformationObject.getUrl()));
            final Long csId = map.get(CDMSIntegrationConstants.CLINICAL_STUDY_ID);
            String	visitId=null;
            if(map.get(CDMSIntegrationConstants.EVENTENTRYID)!=null)
            {
            	visitId=map.get(CDMSIntegrationConstants.EVENTENTRYID).toString();
            }
            if (visitId != null && scgID != null && !visitId.equals("0") && scgID >0)
            {
                url.append(CDMSIntegrationConstants.EVENTENTRYID).append(
                		CDMSIntegrationConstants.EQUALS).append(
                        String.valueOf(visitId));
            }
            else
            {
                /*
                 * get  CS id from CP id , get CSE id from CPE id, get PId and user login name
                 */
                final Long cseId = map.get(CDMSIntegrationConstants.EVENT_ID);
                final Long pId = map.get(CDMSIntegrationConstants.CP_PARTICIPANT_ID);

                url.append(CDMSIntegrationConstants.CP_PARTICIPANT_ID)
                        .append(CDMSIntegrationConstants.EQUALS).append(pId);
                url.append(CDMSCaTissueIntegrationUtil.formReqParameter(
                		CDMSIntegrationConstants.CLINICAL_STUDY_ID,String.valueOf(csId)));
                url.append(CDMSCaTissueIntegrationUtil.formReqParameter(
                		CDMSIntegrationConstants.EVENT_ID, String.valueOf(cseId)));
            }
            url.append(CDMSCaTissueIntegrationUtil.formReqParameter(CDMSIntegrationConstants.SCGID,Utility.toString(scgID)));
            url.append(CDMSCaTissueIntegrationUtil.formReqParameter(CDMSIntegrationConstants.CSM_USER_ID, urlInformationObject.getUserCSMIdentifier()));
            url.append(CDMSCaTissueIntegrationUtil.formReqParameter("&method", CDMSIntegrationConstants.LOGIN));
            if(csId==null || csId<=0)
            {
                url=new StringBuilder();
            }
		}
		//String url = applicationService.getVisitInformationURL(urlInformationObject);
		terminateSession();
		return url.toString();
	}




	public void initialize(String loginName, String password) throws Exception
	{
		try
		{
			if (!Validator.isEmpty(ctmsKeystorePath))
			{
				System.setProperty("javax.net.ssl.trustStore", ctmsKeystorePath);
			}
			final HostnameVerifier hostVerifier = new HostnameVerifier()
			{

				public boolean verify(final String urlHostName, final SSLSession session)
				{
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(hostVerifier);
			applicationService = ApplicationServiceProvider.getRemoteInstance(ctmsServiceURL);
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



	private void terminateSession()
	{
		clientSession.terminateSession();
	}
}