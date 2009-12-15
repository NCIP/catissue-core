package edu.wustl.catissuecore.testcase;

import junit.framework.TestSuite;
import edu.wustl.catissuecore.testcase.admin.BiohazardTestCases;
import edu.wustl.catissuecore.testcase.admin.CancerReaserchGroupTestCases;
import edu.wustl.catissuecore.testcase.admin.DepartmentTestCases;
import edu.wustl.catissuecore.testcase.admin.DistributionProtocolTestCases;
import edu.wustl.catissuecore.testcase.admin.InstitutionTestCases;
import edu.wustl.catissuecore.testcase.admin.MenuTestCases;
import edu.wustl.catissuecore.testcase.admin.SiteTestCases;
import edu.wustl.catissuecore.testcase.admin.SpecimenArrayTypeTestCases;
import edu.wustl.catissuecore.testcase.admin.StorageContainerTestCases;
import edu.wustl.catissuecore.testcase.admin.StorageTypeTestCases;
import edu.wustl.catissuecore.testcase.biospecimen.BulkOperationTestCases;
import edu.wustl.catissuecore.testcase.biospecimen.ConsentsTestCase;
import edu.wustl.catissuecore.testcase.biospecimen.ParticipantTestCases;
import edu.wustl.catissuecore.testcase.biospecimen.SpecimenCollectionGroupTestCases;
import edu.wustl.catissuecore.testcase.biospecimen.SpecimenEventsTestcases;
import edu.wustl.catissuecore.testcase.biospecimen.SpecimenTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.BioHazardBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.CancerResearchGrpBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.CollectionProtocolBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.DeIdentifiedSurgicalPathologyReportBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.DepartmentBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.DistributionProtocolBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.IdentifiedSurgicalPathologyReportBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.InstitutionBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.ParticipantBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.SiteBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.SpecimenBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.SpecimenCollectGroupBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.SpecimenEventBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.StorageContainerRestrictionsBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.StorageTypeBizTestCases;
import edu.wustl.catissuecore.testcase.bizlogic.UserBizTestCases;
import edu.wustl.catissuecore.testcase.collectionprotocol.CollectionProtocolTestCases;
import edu.wustl.catissuecore.testcase.login.FirstTimeLoginTestCase;
import edu.wustl.catissuecore.testcase.login.LoginTestCase;
import edu.wustl.catissuecore.testcase.login.LogoutTestcase;

/**
 * Test suite
 */


public class CaTissueSuiteTestSuite
{
	/**
	 * Default constructor.
	 */
	public CaTissueSuiteTestSuite()
	{
		super();
	}


	/**
	 * @param args :
	 */
	public static void main(String[] args)
	{
		//org.junit.runner.JUnitCore.main("edu.wustl.dao.test.HibernateInsertTestCase");

		junit.awtui.TestRunner.run(CaTissueSuiteTestSuite.class);
		//junit.swingui.TestRunner.run(CaTissueSuiteTestSuite.class);
	}


	/**
	 * @return daoSuite.
	 */
	public static junit.framework.Test suite()
	{
		TestSuite strutsSuite = new TestSuite("caTissue Junit Test Cases");
//		daoSuite.addTestSuite(HibernateTestCaseForCatissue.class);

		strutsSuite.addTestSuite(InitializationTestCase.class);
		strutsSuite.addTestSuite(FirstTimeLoginTestCase.class);
	    strutsSuite.addTestSuite(LoginTestCase.class);
		strutsSuite.addTestSuite(MenuTestCases.class);
		strutsSuite.addTestSuite(InstitutionTestCases.class);
		strutsSuite.addTestSuite(DepartmentTestCases.class);
		strutsSuite.addTestSuite(CancerReaserchGroupTestCases.class);
		strutsSuite.addTestSuite(SiteTestCases.class);
		strutsSuite.addTestSuite(BiohazardTestCases.class);
		strutsSuite.addTestSuite(BulkOperationTestCases.class);
		strutsSuite.addTestSuite(DistributionProtocolTestCases.class);
		strutsSuite.addTestSuite(SpecimenArrayTypeTestCases.class);
		strutsSuite.addTestSuite(StorageTypeTestCases.class);
		strutsSuite.addTestSuite(StorageContainerTestCases.class);
		strutsSuite.addTestSuite(CollectionProtocolTestCases.class);
		strutsSuite.addTestSuite(ParticipantTestCases.class);
		strutsSuite.addTestSuite(SpecimenCollectionGroupTestCases.class);
		strutsSuite.addTestSuite(SpecimenTestCases.class);
		strutsSuite.addTestSuite(SpecimenEventsTestcases.class);
		strutsSuite.addTestSuite(ConsentsTestCase.class);
		strutsSuite.addTestSuite(InstitutionTestCases.class);
		strutsSuite.addTestSuite(DepartmentTestCases.class);
		strutsSuite.addTestSuite(LogoutTestcase.class);
		
		strutsSuite.addTestSuite(InstitutionBizTestCases.class);
		strutsSuite.addTestSuite(DepartmentBizTestCases.class);
		strutsSuite.addTestSuite(CancerResearchGrpBizTestCases.class);
		strutsSuite.addTestSuite(UserBizTestCases.class);
		strutsSuite.addTestSuite(SiteBizTestCases.class);
		strutsSuite.addTestSuite(BioHazardBizTestCases.class);
		strutsSuite.addTestSuite(CollectionProtocolBizTestCases.class);
		strutsSuite.addTestSuite(ParticipantBizTestCases.class);
		strutsSuite.addTestSuite(SpecimenCollectGroupBizTestCases.class);
		strutsSuite.addTestSuite(DistributionProtocolBizTestCases.class);
		strutsSuite.addTestSuite(IdentifiedSurgicalPathologyReportBizTestCases.class);
		strutsSuite.addTestSuite(DeIdentifiedSurgicalPathologyReportBizTestCases.class);
		strutsSuite.addTestSuite(SpecimenBizTestCases.class);
		strutsSuite.addTestSuite(StorageTypeBizTestCases.class);
		//suite.addTestSuite(StorageContainerBizTestCases.class);
		strutsSuite.addTestSuite(SpecimenEventBizTestCases.class);
		strutsSuite.addTestSuite(StorageContainerRestrictionsBizTestCases.class);
		return strutsSuite;
	}


}
