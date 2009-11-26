
package edu.wustl.catissuecore.testcase.admin;

import org.junit.Test;

import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;

public class MenuTestCases extends CaTissueSuiteBaseTest
{

	@Test
	public void testUserAdd()
	{
		setRequestPathInfo("/User");
		addRequestParameter("pageOf", "pageOfUserAdmin");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfUserAdmin");
		verifyNoActionErrors();
	}

	@Test
	public void testInstitutionAdd()
	{
		setRequestPathInfo("/Institution");
		addRequestParameter("pageOf", "pageOfInstitution");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfInstitution");
		verifyNoActionErrors();

	}

	@Test
	public void testDepartmentAdd()
	{
		setRequestPathInfo("/Department");
		addRequestParameter("pageOf", "pageOfDepartment");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfDepartment");
		verifyNoActionErrors();

	}

	@Test
	public void testCancerResearchGroupAdd()
	{
		setRequestPathInfo("/CancerResearchGroup");
		addRequestParameter("pageOf", "pageOfCancerResearchGroup");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfCancerResearchGroup");
		verifyNoActionErrors();

	}

	@Test
	public void testSiteAdd()
	{
		setRequestPathInfo("/Site");
		addRequestParameter("pageOf", "pageOfSite");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfSite");
		verifyNoActionErrors();
	}

	@Test
	public void testStorageTypeAdd()
	{
		setRequestPathInfo("/StorageType");
		addRequestParameter("pageOf", "pageOfStorageType");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfStorageType");
		verifyNoActionErrors();
	}

	@Test
	public void testStorageContainerAdd()
	{
		setRequestPathInfo("/OpenStorageContainer");
		addRequestParameter("pageOf", "pageOfStorageContainer");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testSpecimenArrayTypeAdd()
	{
		setRequestPathInfo("/SpecimenArrayType");
		addRequestParameter("pageOf", "pageOfSpecimenArrayType");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfSpecimenArrayType");
		verifyNoActionErrors();
	}

	@Test
	public void testBiohazardAdd()
	{
		setRequestPathInfo("/Biohazard");
		addRequestParameter("pageOf", "pageOfBioHazard");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfBioHazard");
		verifyNoActionErrors();
	}

	@Test
	public void testCollectionProtocolAdd()
	{
		setRequestPathInfo("/OpenCollectionProtocol");
		addRequestParameter("pageOf", "pageOfmainCP");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testDistributionProtocolAdd()
	{
		setRequestPathInfo("/DistributionProtocol");
		addRequestParameter("pageOf", "pageOfDistributionProtocol");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfDistributionProtocol");
		verifyNoActionErrors();
	}

	@Test
	public void testDefineAnnotationsInformationPageAdd()
	{
		setRequestPathInfo("/DefineAnnotationsInformationPage");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyNoActionErrors();

	}

	@Test
	public void testReportedProblemShowAdd()
	{
		setRequestPathInfo("/ReportedProblemShow");
		addRequestParameter("pageNum", "1");
		actionPerform();
		verifyNoActionErrors();

	}

	@Test
	public void testConflictViewAdd()
	{
		setRequestPathInfo("/ConflictView");
		addRequestParameter("pageNum", "1");
		actionPerform();
		verifyNoActionErrors();

	}

	@Test
	public void testCpBasedSearch()
	{
		setRequestPathInfo("/CpBasedSearch");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testParticipantAdd()
	{
		setRequestPathInfo("/Participant");
		addRequestParameter("pageOf", "pageOfParticipant");
		addRequestParameter("clearConsentSession", "true");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfParticipant");
		verifyNoActionErrors();
	}

	@Test
	public void testSimpleQuerySpecimen()
	{
		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		addRequestParameter("aliasName", "Specimen");
		actionPerform();
		verifyForward("pageOfNewSpecimen");
		verifyNoActionErrors();
	}

	@Test
	public void testCreateSpecimenDerived()
	{
		setRequestPathInfo("/CreateSpecimen");
		addRequestParameter("pageOf", "pageOfDeriveSpecimen");
		addRequestParameter("virtualLocated", "true");
		addRequestParameter("operation", "add");

		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testCreateSpecimenAliquots()
	{
		setRequestPathInfo("/Aliquots");
		addRequestParameter("pageOf", "pageOfAliquot");
		actionPerform();
		verifyForward("pageOfAliquot");
		verifyNoActionErrors();
	}

	@Test
	public void testQuickEvents()
	{
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testMultipleSpecimen()
	{
		setRequestPathInfo("/MultipleSpecimenFlexInitAction");
		addRequestParameter("pageOf", "pageOfMultipleSpWithMenu");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testSpecimenArray()
	{
		setRequestPathInfo("/SpecimenArray");
		addRequestParameter("pageOf", "pageOfSpecimenArray");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfSpecimenArray");
		verifyNoActionErrors();
	}

	@Test
	public void testSimpleQueryDistribution()
	{
		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("pageOf", "pageOfDistribution");
		addRequestParameter("aliasName", "Distribution");
		actionPerform();
		verifyForward("pageOfDistribution");
		verifyNoActionErrors();
	}

	@Test
	public void testOrderView()
	{
		setRequestPathInfo("/RequestListView");
		addRequestParameter("pageNum", "1");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testShippingTracking()
	{
		setRequestPathInfo("/ShowDashboardAction");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testBulkOperation()
	{
		setRequestPathInfo("/BulkOperation");
		addRequestParameter("pageOf", "pageOfBulkOperation");
		actionPerform();
		verifyForward("pageOfBulkOperation");
		verifyNoActionErrors();

	}
}
