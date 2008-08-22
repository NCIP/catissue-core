package edu.wustl.catissuecore.dbunit.test;

import junit.awtui.TestRunner;
import junit.framework.TestSuite;

public class CaTissueTestSuite
{
	
	public static void main(String[] args)
	{

		TestRunner.run(CaTissueTestSuite.class);
	}

	public static junit.framework.Test suite() 
	{
		TestSuite suite = new TestSuite("Test for edu.wustl.catissuecore.bizlogic.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(CaTissueBaseDBUnitTestCase.class);
		suite.addTestSuite(UserDBTestcases.class);
		return suite;
	}
	
}
