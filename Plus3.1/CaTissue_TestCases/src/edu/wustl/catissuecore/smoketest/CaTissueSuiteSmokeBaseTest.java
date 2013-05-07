package edu.wustl.catissuecore.smoketest;

import java.lang.reflect.Method;

import junit.framework.AssertionFailedError;
import junit.framework.TestResult;
import servletunit.HttpServletRequestSimulator;
import servletunit.struts.MockStrutsTestCase;
import edu.wustl.catissuecore.testcase.bizlogic.CaTissueApplicationService;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.TestCaseDataUtil;
import edu.wustl.catissuecore.util.global.Constants;
/**
 * Base test class.
 * @author sachin_lale
 *
 */
public class CaTissueSuiteSmokeBaseTest extends MockStrutsTestCase
{
	protected static CaTissueApplicationService appService = null;

	public void run(TestResult result) {
String exp = "";
	    result.startTest(this);

	    try {
	    	 setUp();
	        runTest();
	    }
	    catch (AssertionFailedError e) { //1
	    	exp=e.getMessage();
	        result.addFailure(this, e);
	    }

	    catch (Throwable e) { // 2
	    	exp=e.getCause().getMessage();
	    	if(exp == null)
	    	{
	    		exp = e.toString();
	    	}
	        result.addError(this, e);
	    }
	    finally {
	    	try
	    	{
//	    		result.addListener(listener)
	    		System.out.println("err count : "+result.errorCount());
	    		System.out.println("err count : "+result.runCount());
	    		System.out.println("err count : "+result.failureCount());
	    		System.out.println("err count : "+result.toString());
	    		TestCaseDataUtil.writeToFile(result,getName(),exp);
	    		tearDown();
	    	}
	    	catch(Exception e)
	    	{
	    		 result.addError(this, e);
	    	}
	    }
	}


	public TestResult run() {
	    TestResult result= createResult();
	    run(result);
	    return result;
	}
//	protected void runTest() throws Throwable {
//////	   this.runTest();
////		this.
////	   runBare();
////	}
//
	protected void runTest() throws Throwable {
	    Method runMethod= null;
	    try {
	        runMethod= getClass().getMethod(getName(), new Class[0]);
	    } catch (NoSuchMethodException e) {
	        assertTrue("Method \""+getName()+"\" not found", false);
	    }
	    finally {
	        runMethod.invoke(this, new Class[0]);
	    }
	    // catch InvocationTargetException and IllegalAccessException
	}



//	public MyTargetTestClass run() {
//		MyTargetTestClass result= null;//createResult();
//	    run(result);
//	    return result;
//	}

	protected void tearDown() throws Exception
	{
		super.tearDown();

		System.out.println("##############dddddd####");
	}
	/**
	 * Setup method called before each test case run.
	 * @throws Exception
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		setConfigFile("/WEB-INF/web.xml");
		HttpServletRequestSimulator req = (HttpServletRequestSimulator)getRequest();
		req.setRequestURL(CaTissueSuiteTestUtil.CONTEXT_URL);
		/**
		 * Setting the session daat bean in http session becuase on each test case run
		 * the session is  lost
		 */
		if(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN!=null)
		{
			getSession().setAttribute(Constants.SESSION_DATA,
					CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN);
		}
		if (appService == null)
		{
			appService = new CaTissueApplicationService(); //ApplicationServiceProvider.getApplicationService();
		}
	}

}
