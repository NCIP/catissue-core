/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

/**
 *
 */
package edu.wustl.catissuecore.testcase.bizlogic;


import edu.wustl.common.test.BaseTestCase;

/**
 * @author ganesh_naikwade
 *
 */
public class CaTissueBaseTestCase extends BaseTestCase{

	/**
	 * @throws java.lang.Exception
	 */

	static CaTissueApplicationService appService = null;
	public CaTissueBaseTestCase(){
		super();
	}
	/**
	 *
	 */
	public void setUp(){

		//Logger.configure("");
		System.setProperty("javax.net.ssl.trustStore", "D://jboss_18080//server//default//conf//chap8.keystore");
		appService = new CaTissueApplicationService(); //ApplicationServiceProvider.getApplicationService();
//		ClientSession cs = ClientSession.getInstance();
		try
		{
//			cs.startSession("admin@admin.com", "Test123");
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
