package edu.wustl.catissuecore.ctrp;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.globus.gsi.GlobusCredential;

import edu.wustl.catissuecore.GSID.GSIDClient;
import edu.wustl.catissuecore.GSID.TargetGrid;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cacoresdk.util.GridAuthenticationClient;

public class CTRPGlobusCredentialCache {

	/**
	 * Logger Object.
	 */
	private static Logger logger = Logger
			.getCommonLogger(CTRPGlobusCredentialCache.class);

	/**
	 * Private constructor.
	 */
	private CTRPGlobusCredentialCache() {

	}

	/**
	 * Singleton instance of GlobusCredntialCache.
	 */
	private static Map<String, GlobusCredential> credentialMap = new HashMap<String, GlobusCredential>();
	private static Properties dorianProps = new Properties();
	private static String dorianURL;
	private static String dorianSyncFilePath;
	private static TargetGrid dorianTargetGridEnv;

	/**
	 * Get globus credential per user login.
	 * 
	 */
	public static GlobusCredential getCredential(String userLogin,
			String userPassword) throws Exception {
		try {
			if (dorianProps.size() == 0) {
				loadDorianProperties();
			}
			if ((credentialMap.get(userLogin) == null)
					|| isReadyToExpire(credentialMap.get(userLogin))) {
				// Sync trusted CA certificates
//				GSIDClient.installRootCerts(dorianTargetGridEnv);
//				GridAuthenticationClient.synchronizeOnce(dorianSyncFilePath);
				// Get authentication credentials by logging into dorian
				GlobusCredential credential = GridAuthenticationClient
						.authenticate(dorianURL, dorianURL, userLogin,
								userPassword);
				if (credential != null) {
					credentialMap.put(userLogin, credential);
				} else {
					return null;
				}
			}
			return (GlobusCredential) credentialMap.get(userLogin);
		} catch (final Exception ex) {
			CTRPGlobusCredentialCache.logger.error(ex.getMessage(), ex);
			throw new Exception(ex.getMessage(), ex);
		}
	}

	public static void loadDorianProperties() throws Exception {
		InputStream inputStream = CTRPGlobusCredentialCache.class
				.getClassLoader().getResourceAsStream(
						CTRPConstants.CTRP_PROPERTIES_FILE);
		if (inputStream == null) {
			throw new Exception("Properties file not found:"
					+ CTRPConstants.CTRP_PROPERTIES_FILE);
		}
		dorianProps.load(inputStream);
		dorianURL = dorianProps
				.getProperty(CTRPConstants.DORIAN_LOGIN_URL_PROP_NAME);
		if ((dorianURL == null) || (dorianURL.length() == 0)) {
			throw new Exception("Property not found:"
					+ CTRPConstants.DORIAN_LOGIN_URL_PROP_NAME + ":in:"
					+ CTRPConstants.CTRP_PROPERTIES_FILE);
		}
		dorianSyncFilePath = dorianProps
				.getProperty(CTRPConstants.DORIAN_SYNC_FILE_PROP_NAME);

		if ((dorianSyncFilePath == null) || (dorianSyncFilePath.length() == 0)) {
			throw new Exception("Property not found:"
					+ CTRPConstants.DORIAN_SYNC_FILE_PROP_NAME + ":in:"
					+ CTRPConstants.CTRP_PROPERTIES_FILE);
		}
		File dorianSyncFile = new File(dorianSyncFilePath);
		if (!dorianSyncFile.exists()) {
			throw new Exception("File not found:" + dorianSyncFilePath);
		}
		String dorianTargetGridEnvStr;
		dorianTargetGridEnvStr = dorianProps
				.getProperty(CTRPConstants.DORIAN_TARGET_GRID_ENV);
		if ((dorianTargetGridEnvStr == null)
				|| (dorianTargetGridEnvStr.length() == 0)) {
			throw new Exception("Property not found:"
					+ CTRPConstants.DORIAN_TARGET_GRID_ENV + ":in:"
					+ CTRPConstants.CTRP_PROPERTIES_FILE);
		}
		dorianTargetGridEnv = TargetGrid.byName(dorianTargetGridEnvStr);
		if (dorianTargetGridEnvStr == null) {
			throw new Exception("Invalid grid env mentioned:"
					+ dorianTargetGridEnvStr + ":in:"
					+ CTRPConstants.CTRP_PROPERTIES_FILE);
		}
	}

	public static boolean isReadyToExpire(GlobusCredential credential)
			throws Exception {
		if (credential.getTimeLeft() <= CTRPConstants.DORIAN_AUTH_TIMEOUT_LIMIT) {
			return true;
		} else {

			return false;
		}
	}
}
