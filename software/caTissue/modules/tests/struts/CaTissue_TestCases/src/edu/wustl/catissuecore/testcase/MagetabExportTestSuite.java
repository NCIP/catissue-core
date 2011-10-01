package edu.wustl.catissuecore.testcase;

import junit.framework.TestSuite;
import edu.wustl.catissuecore.testcase.bizlogic.MagetabExportTestCases;
import edu.wustl.catissuecore.testcase.login.FirstTimeLoginTestCase;
import edu.wustl.catissuecore.testcase.login.LoginTestCase;
import edu.wustl.catissuecore.testcase.login.LogoutTestcase;

/**
 * Test suite
 */


public class MagetabExportTestSuite
{
	/**
	 * Default constructor.
	 */
	public MagetabExportTestSuite()
	{
		super();
	}


	/**
	 * @param args :
	 */
	public static void main(String[] args)
	{
		//org.junit.runner.JUnitCore.main("edu.wustl.dao.test.HibernateInsertTestCase");

		junit.awtui.TestRunner.run(MagetabExportTestSuite.class);
		//junit.swingui.TestRunner.run(CaTissueSuiteTestSuite.class);
	}


	/**
	 * @return daoSuite.
	 */
	public static junit.framework.Test suite()
	{
		TestSuite strutsSuite = new TestSuite("MAGE-TAB Export Test Cases");
//		daoSuite.addTestSuite(HibernateTestCaseForCatissue.class);

		strutsSuite.addTestSuite(InitializationTestCase.class);
		strutsSuite.addTestSuite(FirstTimeLoginTestCase.class);
	    strutsSuite.addTestSuite(LoginTestCase.class);
	    
		strutsSuite.addTestSuite(MagetabExportTestCases.class);
	    
		strutsSuite.addTestSuite(LogoutTestcase.class);
		return strutsSuite;
	}


}
