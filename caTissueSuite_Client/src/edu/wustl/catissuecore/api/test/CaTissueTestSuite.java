package edu.wustl.catissuecore.api.test;

import junit.framework.TestSuite;
import edu.wustl.catissuecore.api.test.AdminRoleCaGridTestCases;
import edu.wustl.catissuecore.api.test.BioHazardTestCases;
import edu.wustl.catissuecore.api.test.CancerResearchGrpTestCases;
import edu.wustl.catissuecore.api.test.CollectionProtocolTestCases;
import edu.wustl.catissuecore.api.test.ComplexCollectionProtocolTestCases;
import edu.wustl.catissuecore.api.test.DeIdentifiedSurgicalPathologyReportTestCases;
import edu.wustl.catissuecore.api.test.DepartmentTestCases;
import edu.wustl.catissuecore.api.test.DisableFunctionalityTestCases;
import edu.wustl.catissuecore.api.test.DistributionProtocolTestCases;
import edu.wustl.catissuecore.api.test.IdentifiedSurgicalPathologyReportTestCases;
import edu.wustl.catissuecore.api.test.InstitutionTestCases;
import edu.wustl.catissuecore.api.test.MSRSuperAdminTestCases;
import edu.wustl.catissuecore.api.test.ParticipantEmpiTestCase;
import edu.wustl.catissuecore.api.test.ParticipantTestCases;
import edu.wustl.catissuecore.api.test.ScientistRoleCaGridTestCases;
import edu.wustl.catissuecore.api.test.ScientistRoleTestCases;
import edu.wustl.catissuecore.api.test.SiteTestCases;
import edu.wustl.catissuecore.api.test.SpecimenCollectGroupTestCases;
import edu.wustl.catissuecore.api.test.SpecimenEventTestCases;
import edu.wustl.catissuecore.api.test.SpecimenTestCases;
import edu.wustl.catissuecore.api.test.StorageContainerRestrictionsTestCases;
import edu.wustl.catissuecore.api.test.StorageContainerTestCases;
import edu.wustl.catissuecore.api.test.StorageTypeTestCases;
import edu.wustl.catissuecore.api.test.UserTestCases;

public class CaTissueTestSuite
{
	
	/*public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(CaTissueTestSuite.class);
	}*/
	
	public static junit.framework.Test suite() 
	{
		TestSuite suite = new TestSuite("Test for edu.wustl.catissuecore.bizlogic.test");
		//$JUnit-BEGIN$
		
		suite.addTestSuite(InstitutionTestCases.class);
		suite.addTestSuite(DepartmentTestCases.class);
		suite.addTestSuite(CancerResearchGrpTestCases.class);
		suite.addTestSuite(UserTestCases.class);
		suite.addTestSuite(SiteTestCases.class);
		suite.addTestSuite(BioHazardTestCases.class);
		suite.addTestSuite(CollectionProtocolTestCases.class);
		suite.addTestSuite(ParticipantTestCases.class);
		suite.addTestSuite(SpecimenCollectGroupTestCases.class);
		suite.addTestSuite(DistributionProtocolTestCases.class);
		suite.addTestSuite(IdentifiedSurgicalPathologyReportTestCases.class);
		suite.addTestSuite(DeIdentifiedSurgicalPathologyReportTestCases.class);
		suite.addTestSuite(SpecimenTestCases.class);
		suite.addTestSuite(StorageTypeTestCases.class);
		suite.addTestSuite(StorageContainerTestCases.class);
		suite.addTestSuite(SpecimenEventTestCases.class);
		suite.addTestSuite(StorageContainerRestrictionsTestCases.class);
		suite.addTestSuite(DisableFunctionalityTestCases.class);
		suite.addTestSuite(MSRSuperAdminTestCases.class);
		suite.addTestSuite(ScientistRoleTestCases.class);
		suite.addTestSuite(ComplexCollectionProtocolTestCases.class);		
		suite.addTestSuite(ScientistRoleCaGridTestCases.class);
		suite.addTestSuite(AdminRoleCaGridTestCases.class);
		suite.addTestSuite(ParticipantEmpiTestCase.class);
		suite.addTestSuite(BulkOperationTestCases.class);
	/*	suite.addTestSuite(SupervisorRoleTestCases.class);
		suite.addTestSuite(TechnicianRoleTestCases.class);	
		suite.addTestSuite(ScientistRoleTestCases.class);*/
	//	suite.addTestSuite(PrivilegeBasedTestCases.class);
		//$JUnit-END$
		return suite;
	}
	
}
