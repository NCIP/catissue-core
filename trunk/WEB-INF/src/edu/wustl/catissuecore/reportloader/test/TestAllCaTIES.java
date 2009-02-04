package edu.wustl.catissuecore.reportloader.test;

import edu.wustl.catissuecore.bizlogic.test.DeidentifiedSurgicalPathologyReportBizLogicTest;
import edu.wustl.catissuecore.bizlogic.test.IdentifiedSurgicalPathologyReportBizLogicTest;
import edu.wustl.catissuecore.deid.DeidReportTest;
import edu.wustl.catissuecore.deid.DeidUtilsTest;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author vijay_pande
 *
 */
public class TestAllCaTIES
{
	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(TestAllCaTIES.class);
	}

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test suite for caTIES related Classes");
		suite.addTestSuite(DeidReportTest.class);
		suite.addTestSuite(DeidUtilsTest.class);
		suite.addTestSuite(HL7FileParserTest.class);
		suite.addTestSuite(IdentifiedSurgicalPathologyReportBizLogicTest.class);
		suite.addTestSuite(DeidentifiedSurgicalPathologyReportBizLogicTest.class);
		return suite;
	}
}
