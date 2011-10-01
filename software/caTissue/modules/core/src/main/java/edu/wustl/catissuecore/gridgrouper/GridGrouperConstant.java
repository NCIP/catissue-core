package edu.wustl.catissuecore.gridgrouper;

/************
 * @author srikalyan
 */
public interface GridGrouperConstant {
	
	public final String GG_PROPERTIES_FILE = "gridgrouper.properties";
	public final String GG_IS_ENABLED_KEY="gridgrouper.isEnabled";
	public final String GG_USER_NAME_KEY="gridgrouper.userName";
	public final String GG_PASSWORD_KEY="gridgrouper.password";
	public final String GG_DORIAN_URL_KEY="gridgrouper.dorianURL";
	public final String GG_SERVICE_URL_KEY="gridgrouper.serviceURL";
	public final String GG_SYNC_DESC_FIlE_KEY="gridgrouper.synDescriptionFileLocation";
	public final String GG_TARGET_GRID="gridgrouper.target.grid";
	public final String GG_STEMS_KEY="gridgrouper.stems";
	public final String JBOSS_HOME="jboss.home";
	
	public final String GG_PROPERTIES_NOT_FOUND_ERR_MSG="GSID properties file not found.";
	public final String GG_PROPERTIES_LOAD_ERROR="Unable to load GSID properties file.";
	
	public final String GLOBUS_INIT_ERROR="Unable to get the globus credentials.";
	public final String GG_URL_ERROR="MalformedURIException occurred for the GridGrouper URL.";
	public final String GG_REMOTE_ERROR="RemoteException occurred for the GridGrouper URL.";
	
	public final int GG_TIMEOUT_LIMIT=120;	
	
	public final String GG_SYNCHRONIZE_COMPLETE_MSG = "Synchronize Complete.";
	public final String GG_SYNCHRONIZE_ERROR_MSG="Unable to synchronize";
	public final String GG_SYNCHRONIZE_START_MSG = "Synchronize Once...";
	public final String GG_SERVICE_DOWN="GSID service might be down";
}
