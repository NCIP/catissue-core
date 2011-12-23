package edu.wustl.catissuecore.api.testcases;

import junit.framework.TestSuite;

public class CaTissueOrderingTestSuite {

	public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("TestCases for caCORE APIs for Ordering and Distribution");

		suite.addTestSuite(OrderingDistributionTestCases.class);
		
		return suite;
	}

}
