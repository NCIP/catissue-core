package edu.wustl.catissuecore.testcase;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Add here test cases to run.
 */
@RunWith(Suite.class)
@SuiteClasses(
		{
	    InitializationTestCase.class,
		LoginTestCase.class,
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
		})
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
}
