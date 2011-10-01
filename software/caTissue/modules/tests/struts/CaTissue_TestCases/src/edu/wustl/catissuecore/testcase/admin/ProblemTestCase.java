package edu.wustl.catissuecore.testcase.admin;

import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;


public class ProblemTestCase extends CaTissueSuiteBaseTest
{

	
	public void testClickOnProblemDetail()
	{
		setRequestPathInfo("/AdminReportProblem");
		addRequestParameter("operation","edit");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

	}
	
	
	public void testClickOnrediRedirectProblem()
	{
		setRequestPathInfo("/RedirectToProblem");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

	}
	
	
	public void testClickOnRedirectToHelp()
	{
		setRequestPathInfo("/RedirectToHelp");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

	}
	
	
}
