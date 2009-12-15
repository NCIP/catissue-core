package edu.wustl.catissuecore.testcase.login;

import org.junit.Test;

import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;


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
	}
	

}
