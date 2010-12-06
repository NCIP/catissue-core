package edu.wustl.catissuecore.smoketest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite
 */


public class CaTissueSuiteSmokeTestSuite
{
	/**
	 * Default constructor.
	 */
	public CaTissueSuiteSmokeTestSuite()
	{
		super();
	}


	/**
	 * @param args :
	 */
	public static void main(String[] args)
	{
		//org.junit.runner.JUnitCore.main("edu.wustl.dao.test.HibernateInsertTestCase");

//		junit.awtui.TestRunner.run(CaTissueSuiteTestSuite.class);
		junit.swingui.TestRunner.run(CaTissueSuiteSmokeTestSuite.class);
	}


	/**
	 * @return daoSuite.
	 */
	public static Test suite()
	{
		TestSuite strutsSuite = new TestSuite("caTissue Junit Test Cases");
//		daoSuite.addTestSuite(HibernateTestCaseForCatissue.class);

		strutsSuite.addTestSuite(InitializationSmokeTestCase.class);
		strutsSuite.addTestSuite(edu.wustl.catissuecore.smoketest.login.FirstTimeLoginTestCase.class);
	    strutsSuite.addTestSuite(edu.wustl.catissuecore.smoketest.login.LoginTestCase.class);
		strutsSuite.addTestSuite(edu.wustl.catissuecore.smoketest.admin.InstitutionTestCases.class);
		strutsSuite.addTestSuite(edu.wustl.catissuecore.smoketest.admin.DepartmentTestCases.class);
		strutsSuite.addTestSuite(edu.wustl.catissuecore.smoketest.admin.CancerReaserchGroupTestCases.class);

		strutsSuite.addTestSuite(edu.wustl.catissuecore.smoketest.login.LogoutTestcase.class);
		strutsSuite.addTestSuite(TearDownSmokeTestCase.class);
		return strutsSuite;
	}


}
