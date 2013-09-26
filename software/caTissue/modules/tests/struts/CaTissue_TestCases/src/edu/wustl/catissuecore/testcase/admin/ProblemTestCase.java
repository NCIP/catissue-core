/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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
