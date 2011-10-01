package edu.wustl.catissuecore.api.testcases;

import junit.framework.TestSuite;

public class CaTissueTestSuite {

	public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("TestCases for caCORE APIs");

		suite.addTestSuite(AdminTestCases.class);
		suite.addTestSuite(ScientistAsNonPIorPCTestCases.class);
		suite.addTestSuite(ScientistAsPCTestCases.class);
		suite.addTestSuite(ScientistAsPITestCases.class);
		suite.addTestSuite(ScientistWithReadAccessTestCases.class);
		suite.addTestSuite(ScientistWithReadDeniedTestCases.class);
		suite.addTestSuite(FilterTestCases.class);
		suite.addTestSuite(GridAuthenticationTestCases.class);

		return suite;
	}

}
