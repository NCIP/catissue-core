package edu.wustl.catissuecore.smoketest;

import servletunit.HttpServletRequestSimulator;
import edu.wustl.catissuecore.smoketest.util.CaTissueApplicationService;
import edu.wustl.catissuecore.smoketest.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.testframework.StrutsTestAutomationBase;
import edu.wustl.testframework.util.DataObject;
/**
 * Base test class.
 * @author sachin_lale
 *
 */
public class CaTissueSuiteSmokeBaseTest extends StrutsTestAutomationBase
{

	public CaTissueSuiteSmokeBaseTest(String name, DataObject dataObject)
	{
		super(name,dataObject);

	}
	public CaTissueSuiteSmokeBaseTest(String name)
	{
		super(name);
	}
	public CaTissueSuiteSmokeBaseTest()
	{
		super();
	}
	protected static CaTissueApplicationService appService = null;

	/**
	 * Setup method called before each test case run.
	 * @throws Exception
	 */
	@Override
	protected void setUp() throws Exception
	{
		System.out.println("Systennn.getprop   "+ System.getProperty("user.dir"));
		super.setUp();
		setConfigFile("/WEB-INF/struts-config.xml");
//		setConfigFile("E:/Program Files/Eclipse-Galileo/eclipse/workspace/catissuecore_/software/caTissue/src/java/WEB-INF/web.xml");
		HttpServletRequestSimulator req = (HttpServletRequestSimulator)getRequest();
		req.setRequestURL(CaTissueSuiteTestUtil.CONTEXT_URL);
		System.out.println("ssssss  :"+req.getRequestURL());
		/**
		 * Setting the session data bean in http session becuase on each test case run
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
