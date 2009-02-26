package src.edu.wustl.catissuecore.testcase;

import org.junit.Before;

import edu.wustl.catissuecore.util.global.Constants;
import servletunit.HttpServletRequestSimulator;
import servletunit.struts.MockStrutsTestCase;
/**
 * Base test class.
 * @author sachin_lale
 *
 */
public class CaTissueSuiteBaseTest extends MockStrutsTestCase
{
	/**
	 * Setup method called before each test case run.
	 * @throws Exception
	 */
	@Override
	@Before
	protected void setUp() throws Exception
	{
		super.setUp();
		setConfigFile("/WEB-INF/struts-config.xml");
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
	}

}
