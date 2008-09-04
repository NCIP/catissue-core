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
		TestSuite suite = new TestSuite("Test for edu.wustl.catissuecore.dbunit.test");
		//$JUnit-BEGIN$
		//suite.addTestSuite(CaTissueBaseDBUnitTestCase.class);
		//suite.addTestSuite(UserDBTestcases.class);
		suite.addTestSuite(DepartmentDBTestcases.class);
		//suite.addTestSuite(InstitutionDBTestCase.class);
		//suite.addTestSuite(CancerResearchGroupDBTestcases.class);
		
		//2-Sept-08
		//suite.addTestSuite(AddressDBTestcases.class);
	//	suite.addTestSuite(CapacityDBTestcases.class);
	//suite.addTestSuite(CellSpecimenDBTestcases.class);
	//	suite.addTestSuite(CellSpecimenRequirementDBTestcases.class);
		//suite.addTestSuite(CellSpecimenReviewParametersDBTestcases.class);
		
		//suite.addTestSuite(CheckInCheckOutEventParameterDBTestcases.class);
		//suite.addTestSuite(ClinicalStudyDBTestcases.class);
		//suite.addTestSuite(ClinicalStudyEventDBTestcases.class);
		//suite.addTestSuite(ClinicalStudyEventEntryDBTestcases.class);
		//suite.addTestSuite(ClinicalStudyRegistrationDBTestcases.class);
		//suite.addTestSuite(CollectionEventParametersDBTestcases.class);
		//suite.addTestSuite(CollectionProtocolDBTestcases.class);
		//suite.addTestSuite(CollectionProtocolEventDBTestcases.class);
		//suite.addTestSuite(CollectionProtocolRegistrationDBTestcases.class);
		//suite.addTestSuite(ConsentTierDBTestcases.class);
		//suite.addTestSuite(ConsentTierResponseDBtestcases.class);
		//suite.addTestSuite(ConsentTierStatusDBTestcases.class);
		//suite.addTestSuite(ContainerDBTestcases.class);
		//suite.addTestSuite(ContainerPositionDBTestcases.class);
		//suite.addTestSuite(ContainerTypeDBTestcases.class);
		//suite.addTestSuite(DerivedSpecimenOrderItemDBTestcases.class);
		//suite.addTestSuite(DisposalEventParametersDBTestcases.class);
		
		return suite;
	}
	
}
