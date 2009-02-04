/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.test;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author ashish_gupta
 *
 */
public class TestAll
{
	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(TestAll.class);
	}

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test suite for Ordering System and Consent Tracking Classes");
		suite.addTestSuite(OrderingSystemTest.class);
		suite.addTestSuite(ConsentTrackingTest.class);
		
		return suite;
	}

}
