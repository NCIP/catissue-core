package edu.wustl.catissuecore.testcase.login;

import org.junit.Test;

import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;


public class LogoutTestcase extends CaTissueSuiteBaseTest
{
	/**
	 * Negative Test Case.
	 * Test logout.
	 */
	@Test
	public void testLogout()
	{
		setRequestPathInfo("/Logout") ;
		actionPerform();		
		verifyForward("success");
		CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN = null;
	}
	

}
