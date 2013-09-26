/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.ctrp;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.globus.gsi.GlobusCredential;

import edu.wustl.catissuecore.util.GridPropertyFileReader;
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

	/**
	 * Get globus credential per user login.
	 * @param authURL 
	 * 
	 */
	public static GlobusCredential getCredential(String userLogin,
			String userPassword, String authURL) throws Exception {
		try {
			Properties serviceUrls = GridPropertyFileReader.serviceUrls();
			String dorianUrl = serviceUrls.getProperty("cagrid.master.dorian.service.url");
			if (StringUtils.isBlank(authURL)) {
			    authURL = dorianUrl;
			}			
			if ((credentialMap.get(userLogin) == null)
					|| isReadyToExpire(credentialMap.get(userLogin))) {
				GlobusCredential credential = GridAuthenticationClient
						.authenticate(dorianUrl, authURL, userLogin,
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

	public static boolean isReadyToExpire(GlobusCredential credential)
			throws Exception {
		if (credential.getTimeLeft() <= CTRPConstants.DORIAN_AUTH_TIMEOUT_LIMIT) {
			return true;
		} else {
			return false;
		}
	}
}
