package edu.wustl.catissuecore.GSID;

/************
 * @author srikalyan
 */
public interface GSIDConstant {
	
	public final String GSID_PROPERTIES_FILE = "gsid.properties";
	public final String GSID_IS_ENABLED_KEY="gsid.isEnabled";
	public final String GSID_USER_NAME_KEY="gsid.userName";
	public final String GSID_PASSWORD_KEY="gsid.password";
	public final String GSID_DORIAN_URL_KEY="gsid.dorianURL";
	public final String GSID_SERVICE_URL_KEY="gsid.serviceURL";
	public final String GSID_REGISTER_APP_NAME="gsid.registerSite.applicationName";
	public final String GSID_REGISTER_APP_URL="gsid.registerSite.applicationURL";
	public final String GSID_REGISTER_APP_VERSION="gsid.registerSite.applicationVersionNumber";
	public final String GSID_REGISTER_CONTACT_NAME="gsid.regsiterSite.contactName";
	public final String GSID_REGISTER_CONTACT_EMAIL="gsid.registerSite.contactEmail";
	public final String GSID_REGISTER_CONTACT_PHONE="gsid.registerSite.phoneNumber";
	public final String GSID_REGISTER_ORGANIZATION="gsid.registerSite.organizationName";
	public final String GSID_SYNC_DESC_FIlE_KEY="gsid.synDescriptionFileLocation";
	public final String GSID_TARGET_GRID="gsid.target.grid";
	public final String JBOSS_HOME="jboss.home";
	public final String GSID_ASSIGN_BUTTON_ENABLED="enable.gsid.assign.button";
	
	public final String GSID_PROPERTIES_NOT_FOUND_ERR_MSG="GSID properties file not found.";
	public final String GSID_PROPERTIES_LOAD_ERROR="Unable to load GSID properties file.";
	
	public final String GLOBUS_INIT_ERROR="Unable to get the globus credentials.";
	public final String GSID_URL_ERROR="MalformedURIException occurred for the GSID URL.";
	public final String GSID_REMOTE_ERROR="RemoteException occurred for the GSID URL.";
	
	public final String GSID_NAMINGAUTHORITY_CONFIG_ERROR="NamingAuthorityConfigurationFault error occurred during regsitration of GSID.";
	public final String GSID_INVALID_VALUES_ERROR="InvalidIdentifierValuesFault error occurred during registration of GSID.";
	public final String GSID_INVALID_IDENTIFIER_ERROR="Suggested GSID is Invalid .";
	public final String GSID_SECURITY_ERROR="NamingAuthoritySecurityFault error occurred during registration of GSID.";
	public final String GSID_REGISTER_REMOTE_ERROR="RemoteException occurred during registration of GSID";
	
	public final int GSID_TIMEOUT_LIMIT=120;	
	
	public final String GSID_SYNCHRONIZE_COMPLETE_MSG = "Synchronize Complete.";
	public final String GSID_SYNCHRONIZE_ERROR_MSG="Unable to synchronize";
	public final String GSID_SYNCHRONIZE_START_MSG = "Synchronize Once...";
	
	
	public final String GSID_UPDATE_PARENT_SPECIMEN_MSG="Please first update the parent specimen";
	public final String GSID_IDENTIFIER_PRINT_MSG="The identifier is ";
	public final String GSID_PARENT_IDENTIFIER_PRINT_MSG="The parent identifier is ";
	public final String GSID_PARENT_UPDATE_ERROR="Unable to update the parent specimen";
	public final String GSID_CLOSING_SESSION_ERROR="Error occured while closing the session";
	
	
	public final String GSID_UI_LABEL="Global Specimen Identifier";
	public final String GSID_UPDATE_PARENT_FIRST="Please first assign a GSID for parent";
	public final String GSID_SERVICE_DOWN="GSID service might be down";
	public final String GSID_UI_NOTIFICATION_MSG="GSID Batch Update Notifications";
	public final String GSID_ASSIGN_BUTTON_LABEL="Assign";
	

}
