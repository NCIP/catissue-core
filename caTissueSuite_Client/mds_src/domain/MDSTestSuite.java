package domain;
import junit.framework.TestSuite;

/**
 * MDSTestSuite is a Composite of Tests. It runs a collection of test cases
 * @author virender_mehta
 */
public class MDSTestSuite
{

	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(MDSTestSuite.class);
	}
	
	public static junit.framework.Test suite() 
	{
		TestSuite suite = new TestSuite("Test for MDS");
		suite.addTestSuite(MinimalDataSharingClient.class);
		return suite;
	}
	
}
