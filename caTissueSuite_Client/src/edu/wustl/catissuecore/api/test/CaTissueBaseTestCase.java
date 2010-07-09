/**
 *
 */
package edu.wustl.catissuecore.api.test;


import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.logger.Logger;
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
	static final String jbossURL = "http://ps4153:8080/catissuecore";
	static final String keyStorePath = "G:/jboss-4.2.2.GA/server/default/conf/chap8.keystore";

	public CaTissueBaseTestCase(){
		super();
	}
	/**
	 *
	 */
	public void setUp(){

		//Logger.configure("");
		System.setProperty("javax.net.ssl.trustStore", keyStorePath);
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
