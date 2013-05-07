/**
 *
 */
package edu.wustl.catissuecore.api.test;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.global.Validator;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

/**
 * @author ganesh_naikwade
 *
 */
public class CaTissueBaseTestCase extends BaseTestCase{

	/**
	 * @throws java.lang.Exception
	 */

	static ApplicationService appService = null;
	static final String loginName = "admin@admin.com";
	static final String password = "Login123";
	static final String jbossURL = "http://localhost:8080/catissuecore";
	static final String keyStorePath = "";

	public CaTissueBaseTestCase(){
		super();
	}
	/**
	 *
	 */
	public void setUp(){

		//Logger.configure("");
		if(!Validator.isEmpty(keyStorePath))
		{
			System.setProperty("javax.net.ssl.trustStore", keyStorePath);
		}
		final HostnameVerifier hostVerifier = new HostnameVerifier()
        {
            public boolean verify(final String urlHostName,final SSLSession session)
            {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hostVerifier);
		appService = ApplicationServiceProvider.getApplicationService();
		ClientSession cs = ClientSession.getInstance();
		try
		{
			cs.startSession(loginName, password);
		}

		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			fail();
			System.exit(1);
		}
	}

}
