package edu.wustl.catissuecore.caties.test;

import edu.wustl.catissuecore.bizlogic.test.DeIdentifiedSurgicalPathologyReportBizLogicTest;
import edu.wustl.catissuecore.bizlogic.test.IdentifiedSurgicalPathologyReportBizLogicTest;
import edu.wustl.catissuecore.deid.DeidReportThreadTest;
import edu.wustl.catissuecore.deid.DeidUtilsTest;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author vijay_pande
 * Class for Test Suite to run all caTIES related JUnit test cases
 */
public class TestAllCaTIES
{
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(TestAllCaTIES.class);
	}

	/**
	 * Default method which executes set of JUnit test case files one-by-one
	 * @return suite object of TestSuite
	 */
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test suite for caTIES related Classes");
		suite.addTestSuite(DeidReportThreadTest.class);
		suite.addTestSuite(DeidUtilsTest.class);
		suite.addTestSuite(HL7FileParserTest.class);
		suite.addTestSuite(IdentifiedSurgicalPathologyReportBizLogicTest.class);
		suite.addTestSuite(DeIdentifiedSurgicalPathologyReportBizLogicTest.class);
		return suite;
	}
}
