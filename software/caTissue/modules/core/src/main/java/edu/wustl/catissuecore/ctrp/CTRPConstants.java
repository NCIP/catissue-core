package edu.wustl.catissuecore.ctrp;

/************
 * @author Ravi Batchu
 */
public class CTRPConstants {

	public static final String CTRP_PROPERTIES_FILE = "ctrp.properties";
	//public static final String DORIAN_LOGIN_URL_PROP_NAME = "ctrp.dorian.loginURL";
	public static final String DORIAN_TARGET_GRID_ENV = "ctrp.dorian.targetGridEnv";
	//public static final String DORIAN_SYNC_FILE_PROP_NAME = "ctrp.dorian.syncDescFile";
	public static final long DORIAN_AUTH_TIMEOUT_LIMIT = 120;

	public static final String CTRP_IS_ENABLED_KEY = "ctrp.isEnabled";
	public static final String CTRP_USER_NAME_KEY = "ctrp.userName";
	public static final String CTRP_PASSWORD_KEY = "ctrp.password";
	public static final String CTRP_DORIAN_URL_KEY = "ctrp.dorianURL";
	
	public static final String CTRP_PO_ORGANIZATION_SERVICE_URL_KEY = "ctrp.organization.serviceURL";
	public static final String CTRP_PO_PERSON_SERVICE_URL_KEY = "ctrp.person.serviceURL";
	public static final String CTRP_PO_CLINICAL_RESEARCH_STAFF_SERVICE_URL_KEY = "ctrp.po.clinicalResearchStaff.serviceURL";
	public static final String CTRP_PO_HEALTHCARE_PROVIDER_SERVICE_URL_KEY = "ctrp.po.healthCareProvider.serviceURL";
	public static final String CTRP_PO_BUSINESS_SERVICE_URL_KEY = "ctrp.po.business.serviceURL";

	public static final String CTRP_PA_STUDY_PROTOCOL_SERVICE_URL_KEY = "ctrp.pa.studyProtocol.serviceURL";
	public static final String CTRP_PA_STUDY_CONTACT_SERVICE_URL_KEY = "ctrp.pa.studyContact.serviceURL";
	public static final String CTRP_PA_STUDY_DISEASE_SERVICE_URL_KEY = "ctrp.pa.studyDisease.serviceURL";
	public static final String CTRP_PA_STUDY_SITE_SERVICE_URL_KEY = "ctrp.pa.studySite.serviceURL";

	public static final String CTRP_PROPERTIES_NOT_FOUND_ERR_MSG = "CTRP properties file not found.";
	public static final String CTRP_PROPERTIES_LOAD_ERROR = "Unable to load CTRP properties file.";

	public static final String COPPA_ORGANIZATION_IDENTIFIER_NAME = "NCI organization entity identifier";
	public static final String COPPA_ORGANIZATION_ROOT = "2.16.840.1.113883.3.26.4.2";

	public static final String COPPA_PERSON_ROOT = "2.16.840.1.113883.3.26.4.1";
	public static final String COPPA_PERSON_IDENTIFIER_NAME = "NCI person entity identifier";

	public static final String COPPA_STUDY_PROTOCOL_ROOT = "2.16.840.1.113883.3.26.4.3";
	public static final String COPPA_STUDY_PROTOCOL_IDENTIFIER_NAME = "NCI study protocol entity identifier";
	
	public static final String COPPA_STUDY_PI_ROLE_CODE = "Study Principal Investigator";

}
