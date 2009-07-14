package edu.wustl.catissuecore.testcase;

import junit.framework.TestSuite;
import edu.wustl.catissuecore.testcase.admin.BiohazardTestCases;
import edu.wustl.catissuecore.testcase.admin.CancerReaserchGroupTestCases;
import edu.wustl.catissuecore.testcase.admin.DepartmentTestCases;
import edu.wustl.catissuecore.testcase.admin.DistributionProtocolTestCases;
import edu.wustl.catissuecore.testcase.admin.InstitutionTestCases;
import edu.wustl.catissuecore.testcase.admin.SiteTestCases;
import edu.wustl.catissuecore.testcase.admin.SpecimenArrayTypeTestCases;
import edu.wustl.catissuecore.testcase.admin.StorageTypeTestCases;
import edu.wustl.catissuecore.testcase.login.LoginTestCase;

/**
 * Test suite
 */


public class CaTissueSuiteTestSuite
{
	/**
	 * Default constructor.
	 */
	public CaTissueSuiteTestSuite()
	{
		super();
	}
	

	/**
	 * @param args :
	 */
	public static void main(String[] args)
	{
		//org.junit.runner.JUnitCore.main("edu.wustl.dao.test.HibernateInsertTestCase");

		junit.awtui.TestRunner.run(CaTissueSuiteTestSuite.class);
		//junit.swingui.TestRunner.run(CaTissueSuiteTestSuite.class);
	}

	
	/**
	 * @return daoSuite.
	 */
	public static junit.framework.Test suite()
	{
		TestSuite strutsSuite = new TestSuite("caTissue Junit Test Cases");
		//daoSuite.addTestSuite(HibernateTestCaseForCatissue.class);
		
		strutsSuite.addTestSuite(InitializationTestCase.class);
//		strutsSuite.addTestSuite(FirstTimeLoginTestCase.class);
		strutsSuite.addTestSuite(LoginTestCase.class);
//		strutsSuite.addTestSuite(InstitutionTestCases.class);
//		strutsSuite.addTestSuite(DepartmentTestCases.class);
//		strutsSuite.addTestSuite(CancerReaserchGroupTestCases.class);
//		strutsSuite.addTestSuite(SiteTestCases.class);
//		strutsSuite.addTestSuite(BiohazardTestCases.class);
//		strutsSuite.addTestSuite(DistributionProtocolTestCases.class);

//		strutsSuite.addTestSuite(SpecimenArrayTypeTestCases.class);
		strutsSuite.addTestSuite(StorageTypeTestCases.class);
/*		strutsSuite.addTestSuite(StorageContainerTestCases.class);
		strutsSuite.addTestSuite(CollectionProtocolTestCases.class);
		strutsSuite.addTestSuite(ParticipantTestCases.class);
		strutsSuite.addTestSuite(SpecimenCollectionGroupTestCases.class);
		strutsSuite.addTestSuite(SpecimenTestCases.class);
*/		return strutsSuite;
	}


}
