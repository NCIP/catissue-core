package edu.wustl.catissuecore.smoketest;

import junit.framework.AssertionFailedError;
import junit.framework.TestResult;
import edu.wustl.catissuecore.testcase.util.TestCaseDataUtil;

public class TearDownSmokeTestCase extends CaTissueSuiteSmokeBaseTest
{
	public void run(TestResult result)
	{
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
//			    		result.addListener(listener)

			    		TestCaseDataUtil.createSummaryReport(result);
			    		tearDown();
			    	}
			    	catch(Exception e)
			    	{
			    		 result.addError(this, e);
			    	}
			    }
			}

	public void testCreateSummaryReport()
	{
		System.out.println("Creating Summary Report");

	}
}
