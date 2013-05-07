/**
 * 
 */

package edu.wustl.catissuecore.api.test;

import edu.wustl.bulkoperator.client.BulkOperationCommand;

/**
 * @author sagar_baldwa
 *
 */
public class BulkOperationTestCases extends CaTissueBaseTestCase
{
	/**
	 * Test Bulk Operation With API Without XML Template.
	 */
	public void testBulkOperationWithOutDefaultArtifactLocation()
	{
		try
		{
			String operationName = "addParticipant";
			String csvFilePath = System.getProperty("user.dir")
					+ "/BulkOperations/addParticipantData.csv";
			String xmlFilePath = System.getProperty("user.dir")
					+ "/BulkOperations/addParticipant.xml";
			System.out.println("csvFile : " + csvFilePath);
			System.out.println("xmlFile : " + xmlFilePath);
			String[] args = {operationName, csvFilePath, jbossURL, loginName, password,
					xmlFilePath, keyStorePath, System.getProperty("user.dir") + "/BulkOperations"};

			BulkOperationCommand.startCommandLine(args);

			System.out
					.println("Bulk Operation template added successfully : " +
							"testBulkOperationWithDefaultArtifactLocation");
			assertTrue("Bulk Operation API ran successfully", true);
		}
		catch (Exception exp)
		{
			System.out.println("Exception in testBulkOperationWithDefaultArtifactLocation");
			System.out.println("Test case failed.");
			exp.printStackTrace();
			assertFalse("Bulk Operation API failed.", true);
		}
	}
}