package edu.wustl.catissuecore.GSID;

import edu.wustl.catissuecore.util.GridPropertyFileReader;
import edu.wustl.common.exception.ErrorKey;
import gov.nih.nci.cacoresdk.util.GridAuthenticationClient;
import gov.nih.nci.cagrid.identifiers.client.IdentifiersNAServiceClient;
import gov.nih.nci.cagrid.identifiers.stubs.types.InvalidIdentifierFault;
import gov.nih.nci.cagrid.identifiers.stubs.types.InvalidIdentifierValuesFault;
import gov.nih.nci.cagrid.identifiers.stubs.types.NamingAuthorityConfigurationFault;
import gov.nih.nci.cagrid.identifiers.stubs.types.NamingAuthoritySecurityFault;
import gov.nih.nci.logging.api.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Properties;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;
/*******
 * @author srikalyan
 *
 */
public class GSIDClient {
	private static final String USER_NAME;
	private static final String USER_PASSWORD;
	//private static final String DORIAN_URL;
	private static final String GSID_URL;
	public static final boolean GSID_IS_ENABLED;
	private static final String REGISTER_APP_NAME;
	private static final String REGISTER_APP_URL;
	private static final String REGISTER_APP_VERSION;
	private static final String REGISTER_CONTACT_NAME;
	private static final String REGISTER_CONTACT_EMAIL;
	private static final String REGISTER_CONTACT_PHONE;
	private static final String REGISTER_ORGANIZATION;
	//private static final String GSID_SYNC_DESC_FILE;
	private static final String GSID_TARGET_GRID;
	private static final String JBOSS_HOME;
	private static GlobusCredential globusCredentials = null;
	private static IdentifiersNAServiceClient client = null;
	private static final boolean GSID_ASSIGN_BUTTON_ENABLED;
	private static boolean siteRegistered;
	
	//private static final Log LOG = LogFactory.getLog(GSIDClient.class);
	private static Logger LOG = Logger.getLogger(GSIDClient.class);
	private static final String ERROR_KEY="errors.gsid";
	
	/*****
	 * Loading the properties file.
	 */
	static {
		Properties defaultProps = new Properties();
		InputStream in = null;
		in = GSIDClient.class.getClassLoader().getResourceAsStream(
				GSIDConstant.GSID_PROPERTIES_FILE);
		if (in == null) {
			LOG.error(GSIDConstant.GSID_PROPERTIES_NOT_FOUND_ERR_MSG);
			USER_NAME = null;
			USER_PASSWORD = null;
			//DORIAN_URL = null;
			GSID_URL = null;
			GSID_IS_ENABLED = false;
			REGISTER_APP_NAME = null;
			REGISTER_APP_URL = null;
			REGISTER_APP_VERSION = null;
			REGISTER_CONTACT_NAME = null;
			REGISTER_CONTACT_EMAIL = null;
			REGISTER_CONTACT_PHONE = null;
			REGISTER_ORGANIZATION = null;
			//GSID_SYNC_DESC_FILE = null;
			GSID_TARGET_GRID=null;
			JBOSS_HOME=null;
			GSID_ASSIGN_BUTTON_ENABLED=false;
		} else {
			try {
				defaultProps.load(in);
			} catch (IOException e) {
				LOG.error(GSIDConstant.GSID_PROPERTIES_LOAD_ERROR, e);
				defaultProps = null;

			}
			if (defaultProps == null) {
				USER_NAME = null;
				USER_PASSWORD = null;
				//DORIAN_URL = null;
				GSID_URL = null;
				GSID_IS_ENABLED = false;
				REGISTER_APP_NAME = null;
				REGISTER_APP_URL = null;
				REGISTER_APP_VERSION = null;
				REGISTER_CONTACT_NAME = null;
				REGISTER_CONTACT_EMAIL = null;
				REGISTER_CONTACT_PHONE = null;
				REGISTER_ORGANIZATION = null;
				//GSID_SYNC_DESC_FILE=null;
				GSID_TARGET_GRID=null;
				JBOSS_HOME=null;
				GSID_ASSIGN_BUTTON_ENABLED=false;
			} else {
				USER_NAME = defaultProps
						.getProperty(GSIDConstant.GSID_USER_NAME_KEY);
				USER_PASSWORD = defaultProps
						.getProperty(GSIDConstant.GSID_PASSWORD_KEY);
				//DORIAN_URL = defaultProps
					//	.getProperty(GSIDConstant.GSID_DORIAN_URL_KEY);
				GSID_URL = defaultProps
						.getProperty(GSIDConstant.GSID_SERVICE_URL_KEY);
				REGISTER_APP_NAME = defaultProps
						.getProperty(GSIDConstant.GSID_REGISTER_APP_NAME);
				REGISTER_APP_URL = defaultProps
						.getProperty(GSIDConstant.GSID_REGISTER_APP_URL);
				REGISTER_APP_VERSION = defaultProps
						.getProperty(GSIDConstant.GSID_REGISTER_APP_VERSION);
				REGISTER_CONTACT_NAME = defaultProps
						.getProperty(GSIDConstant.GSID_REGISTER_CONTACT_NAME);
				REGISTER_CONTACT_EMAIL = defaultProps
						.getProperty(GSIDConstant.GSID_REGISTER_CONTACT_EMAIL);
				REGISTER_CONTACT_PHONE = defaultProps
						.getProperty(GSIDConstant.GSID_REGISTER_CONTACT_PHONE);
				REGISTER_ORGANIZATION = defaultProps
						.getProperty(GSIDConstant.GSID_REGISTER_ORGANIZATION);
				//GSID_SYNC_DESC_FILE=defaultProps.getProperty(GSIDConstant.GSID_SYNC_DESC_FIlE_KEY);
				GSID_TARGET_GRID=defaultProps.getProperty(GSIDConstant.GSID_TARGET_GRID);
				JBOSS_HOME=defaultProps.getProperty(GSIDConstant.JBOSS_HOME);
				GSID_IS_ENABLED = Boolean.valueOf(
						defaultProps
								.getProperty(GSIDConstant.GSID_IS_ENABLED_KEY))
						.booleanValue();
				GSID_ASSIGN_BUTTON_ENABLED = Boolean.valueOf(
						defaultProps
						.getProperty(GSIDConstant.GSID_ASSIGN_BUTTON_ENABLED))
				.booleanValue();
			}
			LOG.debug("GSID_IS_ENABLED = \"" + GSID_IS_ENABLED + "\"");
			LOG.debug("USER_NAME = \"" + USER_NAME + "\"");
			LOG.debug("USER_PASSWORD = \"" + USER_PASSWORD + "\"");
			//LOG.debug("DORIAN_URL = \"" + DORIAN_URL + "\"");
			LOG.debug("GSID_URL = \"" + GSID_URL + "\"");
			LOG.debug("REGISTER_APP_NAME = \"" + REGISTER_APP_NAME + "\"");
			LOG.debug("REGISTER_APP_URL = \"" + REGISTER_APP_URL + "\"");
			LOG.debug("REGISTER_APP_VERSION = \"" + REGISTER_APP_VERSION + "\"");
			LOG.debug("REGISTER_CONTACT_NAME = \"" + REGISTER_CONTACT_NAME
					+ "\"");
			LOG.debug("REGISTER_CONTACT_EMAIL = \"" + REGISTER_CONTACT_EMAIL
					+ "\"");
			LOG.debug("REGISTER_CONTACT_PHONE = \"" + REGISTER_CONTACT_PHONE
					+ "\"");
			LOG.debug("REGISTER_ORGANIZATION = \"" + REGISTER_ORGANIZATION
					+ "\"");
			//LOG.debug("GSID_SYNC_DESC_FILE = \""+GSID_SYNC_DESC_FILE+"\"");
			LOG.debug("GSID_TARGET_GRID = \""+GSID_TARGET_GRID+"\"");
			
		}

	}

	
	public boolean isAssignButtonEnabled() {
		return GSID_ASSIGN_BUTTON_ENABLED;
	}
	public String getUrl() {
		return this.GSID_URL;
	}
	/****
	 * This method is used to register the client with GSID service. 
	 * It throws an exception when the client is already regsitered. so 
	 */
	private static void registerSite() {
		if (client != null && !siteRegistered) {
			try {
				siteRegistered = true;
				client.registerSite(REGISTER_APP_NAME, REGISTER_APP_URL,
						REGISTER_APP_VERSION, REGISTER_CONTACT_NAME,
						REGISTER_CONTACT_EMAIL, REGISTER_CONTACT_PHONE,
						REGISTER_ORGANIZATION);
			} catch (NamingAuthorityConfigurationFault e) {
				LOG.error(GSIDConstant.GSID_NAMINGAUTHORITY_CONFIG_ERROR, e);
			} catch (InvalidIdentifierValuesFault e) {
				LOG.error(GSIDConstant.GSID_INVALID_VALUES_ERROR, e);
			} catch (InvalidIdentifierFault e) {
				LOG.error(GSIDConstant.GSID_INVALID_IDENTIFIER_ERROR, e);
			} catch (NamingAuthoritySecurityFault e) {
				LOG.error(GSIDConstant.GSID_SECURITY_ERROR, e);
			} catch (RemoteException e) {
				LOG.error(GSIDConstant.GSID_REGISTER_REMOTE_ERROR, e);
			}
		}
	}
	
	/******************************************
	 * This method is used to register GSID using the GSID service's client
	 * 
	 * @param suggestedIdentifier
	 *            can be null and be used if you have a unregistered identifier.
	 * @param parentIdentifiers
	 *            can be null and be used if there are any parents associated
	 *            with a specimen.
	 * @return a String representing GSID.
	 */
	protected synchronized String getGSID(String suggestedIdentifier,
			String[] parentIdentifiers, boolean sync) throws GSIDException {
		String identifier = null;
		if (sync) syncClient();
		if (client != null) {
			
			try {
				identifier = client.registerGSID(suggestedIdentifier,
						parentIdentifiers);
				
			} catch (NamingAuthorityConfigurationFault e) {
				LOG.error(GSIDConstant.GSID_NAMINGAUTHORITY_CONFIG_ERROR, e);
				throw new GSIDException(ErrorKey.getErrorKey(ERROR_KEY), e, GSIDConstant.GSID_NAMINGAUTHORITY_CONFIG_ERROR);
			} catch (InvalidIdentifierValuesFault e) {
				LOG.error(GSIDConstant.GSID_INVALID_VALUES_ERROR, e);
				throw new GSIDException(ErrorKey.getErrorKey(ERROR_KEY), e, GSIDConstant.GSID_INVALID_VALUES_ERROR);
			} catch (InvalidIdentifierFault e) {
				LOG.error(GSIDConstant.GSID_INVALID_IDENTIFIER_ERROR + " : " + suggestedIdentifier, e);
				
				throw new GSIDException(ErrorKey.getErrorKey(ERROR_KEY), e, GSIDConstant.GSID_INVALID_IDENTIFIER_ERROR + " "+ suggestedIdentifier,GSIDConstant.GSID_INVALID_IDENTIFIER_ERROR+" "+ suggestedIdentifier);
			} catch (NamingAuthoritySecurityFault e) {
				LOG.error(GSIDConstant.GSID_SECURITY_ERROR, e);
				throw new GSIDException(ErrorKey.getErrorKey(ERROR_KEY), e, GSIDConstant.GSID_SECURITY_ERROR);
			} catch (RemoteException e) {
				LOG.error(GSIDConstant.GSID_REGISTER_REMOTE_ERROR, e);
				
				GSIDServiceStatusNotifier notifier = new GSIDServiceStatusNotifier();
				notifier.sendServiceStatusFailureEmail();
				try {
					notifier.scheduleJob();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				throw new GSIDException(ErrorKey.getErrorKey(ERROR_KEY), e, GSIDConstant.GSID_REGISTER_REMOTE_ERROR,GSIDConstant.GSID_REGISTER_REMOTE_ERROR + " : " +e.getMessage());
				
			}
		}
		return identifier;
	}
	
	public synchronized boolean validateIdentifier(String identifier) throws RemoteException {

		syncClient();
		boolean isValid = false;
		if (client != null) {
			try {
				isValid = client.validateIdentifier(identifier);
			}  catch (RemoteException e) {
				throw e;
			}
		}
		return isValid;
	}
	

	/**************************************
	 * This method is used to intialize the client if client is null and update
	 * the client if globus credential's life time is near to expire. The
	 * threshold is managed using GSID_TIMEOUT_LIMIT in GSIDConstant interface.
	 */
	public static void syncClient() {

		Properties serviceUrls = GridPropertyFileReader.serviceUrls();

		String dorianUrl = serviceUrls.getProperty("cagrid.master.dorian.service.url");
		
		
		
		if (!StringUtils.isBlank(USER_NAME)
				&& !StringUtils.isBlank(USER_PASSWORD)
				//&& !StringUtils.isBlank(DORIAN_URL)
				&& !StringUtils.isBlank(GSID_URL)) {
				try {
					//installRootCertsAndSync();
					globusCredentials = GridAuthenticationClient.authenticate(
							dorianUrl, dorianUrl, USER_NAME, USER_PASSWORD);
				} catch (Exception e) {
					LOG.error(GSIDConstant.GLOBUS_INIT_ERROR, e);
				}
				if (globusCredentials != null) {
					try {
						client = new IdentifiersNAServiceClient(GSID_URL,
								globusCredentials);
						if(GSID_IS_ENABLED)
						{					
							registerSite();
						}
					} catch (MalformedURIException e) {
						LOG.error(GSIDConstant.GSID_URL_ERROR, e);
					} catch (RemoteException e) {
						LOG.error(GSIDConstant.GSID_REMOTE_ERROR, e);
					}
				}
		}
	}

}
