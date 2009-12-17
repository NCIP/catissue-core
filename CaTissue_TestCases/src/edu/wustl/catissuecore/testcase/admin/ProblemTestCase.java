package edu.wustl.catissuecore.testcase.admin;

import org.junit.Test;

import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;


public class ProblemTestCase extends CaTissueSuiteBaseTest
{

	@Test
	public void testClickOnProblemDetail()
	{
		setRequestPathInfo("/AdminReportProblem");
		addRequestParameter("operation","edit");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

	}
	
	@Test
	public void testClickOnrediRedirectProblem()
	{
		setRequestPathInfo("/RedirectToProblem");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

	}
	
	@Test
	public void testClickOnRedirectToHelp()
	{
		setRequestPathInfo("/RedirectToHelp");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

	}
	
	
}
