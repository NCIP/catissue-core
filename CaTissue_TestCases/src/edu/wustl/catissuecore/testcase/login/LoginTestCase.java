package edu.wustl.catissuecore.testcase.login;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.common.beans.SessionDataBean;
/**
*
 * @author sachin_lale
 *
 */
public class LoginTestCase extends CaTissueSuiteBaseTest
{
	/**
	 * Negative Test Case.
	 * Test Login with empty Login name.
	 */
	
	public void testLoginWithEmptyLoginName()
	{
		setRequestPathInfo("/Login") ;
		addRequestParameter("loginName", "");
		addRequestParameter("password", "Test");
		actionPerform();
		//verifyForward("/Home.do");
		verifyForward("failure");

		//verify action errors
		String errormsg[] = new String[] {"errors.item.required"};
		verifyActionErrors(errormsg);
	}
	
	/**
	 * Negative Test Case.
	 * Test Login with empty Password.
	 */
	
	public void testLoginWithEmptyLoginPassword()
	{
		setRequestPathInfo("/Login") ;
		addRequestParameter("loginName", "admin@admin.com");
		addRequestParameter("password", "");
		actionPerform();
		//verifyForward("/Home.do");
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[] {"errors.item.required"};
		verifyActionErrors(errormsg);
	}
	
	/**
	 * Negative Test Case.
	 * Test Login with Invalid Format of Login and Password.
	 */
	
	public void testLoginWithInvalidFormatLogin()
	{
		setRequestPathInfo("/Login") ;
		addRequestParameter("loginName", "@admin");
		addRequestParameter("password", "Test");
		actionPerform();
		//verifyForward("/Home.do");
		verifyForward("failure");

		//verify action errors
		String errormsg[] = new String[] {"errors.item.format"};
		verifyActionErrors(errormsg);
	}

	/**
	 * Negative test case.
	 * Test Login with Invalid Login name and password.
	 * bug 10997
	 */
	
	public void testInvalidLogin()
	{
		/**
		 * reset method is modified for running successfully.
		 * this test case using Login Form. 
		 */
		setRequestPathInfo("/Login") ;
		addRequestParameter("loginName", "admin@admin.com");
		addRequestParameter("password", "Test");
		actionPerform();
		//verifyForward("/Home.do");
		verifyForward("failure");

		//verify action errors
		String errormsg[] = new String[] {"errors.incorrectLoginIDPassword"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Login with Valid Login name and Password.
	 */
	
	public void testSuccessfulLogin()
	{
		
		setRequestPathInfo("/Login") ;
		addRequestParameter("loginName", "admin@admin.com");
		addRequestParameter("password", "Test123");
		logger.info("Sachin in login");
		System.out.println("Sachin in login");
		actionPerform();
		//verifyForward("/Home.do");
		verifyForward("pageOfNonWashU");

		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		assertEquals("user name should be equal to logged in username","admin@admin.com",bean.getUserName());
		CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN=bean;
		verifyNoActionErrors();
	} 
}
