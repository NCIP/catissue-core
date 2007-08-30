package edu.wustl.catissuecore.bizlogic.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class CaTissueTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for edu.wustl.catissuecore.bizlogic.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(CancerResearchGrpTestCases.class);
		suite.addTestSuite(InstitutionTestCases.class);
		suite.addTestSuite(ParticipantTestCases.class);
		suite.addTestSuite(DepartmentTestCases.class);
		suite.addTestSuite(BioHazardTestCases.class);
		//suite.addTestSuite(UserTestCases.class);
		suite.addTestSuite(SiteTestCases.class);
		suite.addTestSuite(DistributionProtocolTestCases.class);
		suite.addTestSuite(CollectionProtocolTestCases.class);
		suite.addTestSuite(StorageContainerTestCases.class);
		suite.addTestSuite(StorageTypeTestCases.class);
		suite.addTestSuite(OrderTestCases.class);
		//$JUnit-END$
		return suite;
	}

}
