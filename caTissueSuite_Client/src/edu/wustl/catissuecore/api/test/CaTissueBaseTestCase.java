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
	public CaTissueBaseTestCase(){
		super();
	}
	/**
	 * 
	 */
	public void setUp(){
		
		//Logger.configure("");
		System.setProperty("javax.net.ssl.trustStore", "D:/Main/pcatissue/jboss-4.2.2.GA/server/default/conf/jbosskey.keystore");
		appService = ApplicationServiceProvider.getApplicationService();
		ClientSession cs = ClientSession.getInstance();
		try
		{ 
			cs.startSession("admin@admin.com", "Test123");
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
