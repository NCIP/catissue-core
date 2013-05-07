package edu.wustl.catissuecore.testcase.bizlogic;

import junit.framework.TestSuite;

public class CsmTestSuite {
	/*public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(CsmTestSuite.class);
	}*/
	
	public static junit.framework.Test suite() 
	{
		TestSuite suite = new TestSuite("Test for edu.wustl.catissuecore.bizlogic.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(CsmTestData.class);
		suite.addTestSuite(CsmSupervisorTestCases.class);
		suite.addTestSuite(CsmTechnicianTestCases.class);
		suite.addTestSuite(CsmScientistTestCases.class);
		return suite;
    }
 }
