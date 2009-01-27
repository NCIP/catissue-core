package edu.wustl.catissuecore.testcase;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
/**
 * Add here test cases to run.
 */
@RunWith(Suite.class)
@SuiteClasses(
		{InitializationTestCase.class,
		LoginTestCase.class
		})
/**
 * Test suite
 */
public class CaTissueSuiteTestSuite
{
	/**
	 * Default constructor.
	 */
	public CaTissueSuiteTestSuite()
	{
		super();
	}
}
