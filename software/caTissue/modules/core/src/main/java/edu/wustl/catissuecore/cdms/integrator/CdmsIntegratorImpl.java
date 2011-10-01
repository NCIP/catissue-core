package edu.wustl.catissuecore.cdms.integrator;

import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import edu.wustl.catissuecore.util.CDMSCaTissueIntegrationUtil;
import edu.wustl.catissuecore.util.global.CDMSIntegrationConstants;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

public class CdmsIntegratorImpl extends CatissueCdmsIntegrator {
	private transient ApplicationService applicationService = null;
	private final transient String ctmsServiceURL = XMLPropertyHandler
			.getValue(CDMSIntegrationConstants.CDMS_SERVICE_URL);
	private final transient String ctmsKeystorePath = XMLPropertyHandler
			.getValue(CDMSIntegrationConstants.CDMS_KEYSTORE_FILE_PATH);

	public CdmsIntegratorImpl() throws Exception {
		super();
	}

	public CdmsIntegratorImpl(Properties properties) throws Exception {
		super(properties);
	}

	public String getSpecimenCollectionGroupURL(
			CatissueCdmsURLInformationObject urlInformationObject)
			throws Exception {
		StringBuffer url = new StringBuffer();
		Long cpId = Long.valueOf(urlInformationObject
				.getCollectionProtocolIdentifier());
		if (cpId != null && cpId != 0) {
			String caTissueUrl = urlInformationObject.getUrl();
			url.append(getProperCaTissueurl(caTissueUrl));
			url.append(CDMSIntegrationConstants.DEDATAENTRYACTION).append(
					CDMSIntegrationConstants.DO).append('?').append(
					CDMSIntegrationConstants.PARTICIPANT_ID).append(
					CDMSIntegrationConstants.EQUALS).append(
					urlInformationObject.getParticipantIdentifier());
			url.append(formReqParameter(
					CDMSIntegrationConstants.COLL_PROTOCOL_EVENT_ID, String
							.valueOf(urlInformationObject
									.getCollectionProtocolEventIdentifier())));
			url.append(formReqParameter(
					CDMSIntegrationConstants.COLLECTION_PROTOCOL_ID, String
							.valueOf(cpId)));
			url.append(formReqParameter(CDMSIntegrationConstants.CSM_USER_ID,
					urlInformationObject.getUserCSMIdentifier()));
			url.append("&method=SCG");
		}
		return url.toString();
	}

	public String getProperCaTissueurl(String caTissueUrl) {
		if (caTissueUrl.contains(".do")) {
			int lastIndex = caTissueUrl.lastIndexOf('/');
			caTissueUrl = caTissueUrl.substring(0, lastIndex);
		}
		caTissueUrl = caTissueUrl + "/";
		return caTissueUrl;
	}

	private String formReqParameter(String parameterName, String value) {
		StringBuffer str = new StringBuffer();
		str.append('&').append(parameterName).append('=').append(value);
		return str.toString();
	}

	public String getVisitInformationURL(
			CatissueCdmsURLInformationObject urlInformationObject,
			String loginName, String password) throws Exception {
		initialize(loginName, password);
		new CDMSCaTissueIntegrationUtil()
				.getSCGRelatedEncounteredDate(urlInformationObject);
		// String url = (String) applicationService.getVisitInformationURL(urlInformationObject);
		return "";
	}

	public void initialize(String loginName, String password) throws Exception {
		try {
			if (!Validator.isEmpty(ctmsKeystorePath)) {
				System
						.setProperty("javax.net.ssl.trustStore",
								ctmsKeystorePath);
			}
			final HostnameVerifier hostVerifier = new HostnameVerifier() {
				public boolean verify(final String urlHostName,
						final SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(hostVerifier);
			applicationService = ApplicationServiceProvider.getApplicationServiceFromUrl(ctmsServiceURL, loginName, password);
		} catch (Exception ex) {
			Logger.out.error("Error in initializing CaCoreAPIService " + ex);
			throw ex;
		}
	}
}