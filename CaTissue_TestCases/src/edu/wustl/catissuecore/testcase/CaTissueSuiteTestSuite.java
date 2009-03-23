package edu.wustl.catissuecore.testcase;

import junit.framework.TestSuite;
/**
 * Add here test cases to run.
 *//*
@RunWith(Suite.class)
@SuiteClasses(
		{
			edu.wustl.catissuecore.testcase.InitializationTestCase.class
		//LoginTestCase.class,
		//FirstTimeLoginTestCase.class
		//DirectDistributionTestCase.class
		InstitutionTestCases.class,
		DepartmentTestCases.class,
		CancerReaserchGroupTestCases.class,
		SiteTestCases.class,
		BiohazardTestCases.class,
		DistributionProtocolTestCases.class,
		SpecimenArrayTypeTestCases.class,
		StorageTypeTestCases.class,
		StorageContainerTestCases.class,
		CollectionProtocolTestCases.class,
		ParticipantTestCases.class,
		SpecimenCollectionGroupTestCases.class,
		SpecimenTestCases.class
		})*/
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
	}

	/**
	 * @return daoSuite.
	 */
	public static junit.framework.Test suite()
	{
		TestSuite strutsSuite = new TestSuite("Test struts cases");
		//daoSuite.addTestSuite(HibernateTestCaseForCatissue.class);
		
		strutsSuite.addTestSuite(InitializationTestCase.class);
		strutsSuite.addTestSuite(LoginTestCase.class);
		//strutsSuite.addTestSuite(FirstTimeLoginTestCase.class);
		strutsSuite.addTestSuite(InstitutionTestCases.class);
		strutsSuite.addTestSuite(DepartmentTestCases.class);
		strutsSuite.addTestSuite(CancerReaserchGroupTestCases.class);
		strutsSuite.addTestSuite(SiteTestCases.class);
		strutsSuite.addTestSuite(BiohazardTestCases.class);
		strutsSuite.addTestSuite(DistributionProtocolTestCases.class);
		strutsSuite.addTestSuite(SpecimenArrayTypeTestCases.class);
		strutsSuite.addTestSuite(StorageTypeTestCases.class);
		strutsSuite.addTestSuite(StorageContainerTestCases.class);
		strutsSuite.addTestSuite(CollectionProtocolTestCases.class);
		strutsSuite.addTestSuite(ParticipantTestCases.class);
		strutsSuite.addTestSuite(SpecimenCollectionGroupTestCases.class);
		strutsSuite.addTestSuite(SpecimenTestCases.class);
		//daoSuite.addTestSuite(HibernateTestCaseForCider.class);
		//daoSuite.addTestSuite(JDBCTestCasesForCider.class);
		//return new junit.framework.JUnit4TestAdapter(HibernateInsertTestCase.class);
		return strutsSuite;
	}


}
