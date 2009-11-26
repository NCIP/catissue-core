
package edu.wustl.catissuecore.testcase.admin;

import org.junit.Test;

import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;

public class MenuTestCases extends CaTissueSuiteBaseTest
{

	@Test
	public void testClickOnUserAdd()
	{
		setRequestPathInfo("/User");
		addRequestParameter("pageOf", "pageOfUserAdmin");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfUserAdmin");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnInstitutionAdd()
	{
		setRequestPathInfo("/Institution");
		addRequestParameter("pageOf", "pageOfInstitution");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfInstitution");
		verifyNoActionErrors();

	}

	@Test
	public void testClickOnDepartmentAdd()
	{
		setRequestPathInfo("/Department");
		addRequestParameter("pageOf", "pageOfDepartment");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfDepartment");
		verifyNoActionErrors();

	}

	@Test
	public void testClickOnCancerResearchGroupAdd()
	{
		setRequestPathInfo("/CancerResearchGroup");
		addRequestParameter("pageOf", "pageOfCancerResearchGroup");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfCancerResearchGroup");
		verifyNoActionErrors();

	}

	@Test
	public void testClickOnSiteAdd()
	{
		setRequestPathInfo("/Site");
		addRequestParameter("pageOf", "pageOfSite");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfSite");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnStorageTypeAdd()
	{
		setRequestPathInfo("/StorageType");
		addRequestParameter("pageOf", "pageOfStorageType");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfStorageType");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnStorageContainerAdd()
	{
		setRequestPathInfo("/OpenStorageContainer");
		addRequestParameter("pageOf", "pageOfStorageContainer");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnSpecimenArrayTypeAdd()
	{
		setRequestPathInfo("/SpecimenArrayType");
		addRequestParameter("pageOf", "pageOfSpecimenArrayType");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfSpecimenArrayType");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnBiohazardAdd()
	{
		setRequestPathInfo("/Biohazard");
		addRequestParameter("pageOf", "pageOfBioHazard");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfBioHazard");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnCollectionProtocolAdd()
	{
		setRequestPathInfo("/OpenCollectionProtocol");
		addRequestParameter("pageOf", "pageOfmainCP");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnDistributionProtocolAdd()
	{
		setRequestPathInfo("/DistributionProtocol");
		addRequestParameter("pageOf", "pageOfDistributionProtocol");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfDistributionProtocol");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnDefineAnnotationsInformationPageAdd()
	{
		setRequestPathInfo("/DefineAnnotationsInformationPage");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyNoActionErrors();

	}

	@Test
	public void testClickOnReportedProblemShow()
	{
		setRequestPathInfo("/ReportedProblemShow");
		addRequestParameter("pageNum", "1");
		actionPerform();
		verifyNoActionErrors();

	}

	@Test
	public void testClickOnConflictSPR()
	{
		setRequestPathInfo("/ConflictView");
		addRequestParameter("pageNum", "1");
		actionPerform();
		verifyNoActionErrors();

	}

	@Test
	public void testClickOnCpBasedSearch()
	{
		setRequestPathInfo("/CpBasedSearch");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnParticipantAdd()
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
	public void testClickOnSpecimenEdit()
	{
		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		addRequestParameter("aliasName", "Specimen");
		actionPerform();
		verifyForward("pageOfNewSpecimen");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnCreateSpecimenDerived()
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
	public void testClickOnCreateSpecimenAliquots()
	{
		setRequestPathInfo("/Aliquots");
		addRequestParameter("pageOf", "pageOfAliquot");
		actionPerform();
		verifyForward("pageOfAliquot");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnEvents()
	{
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnMultipleSpecimen()
	{
		setRequestPathInfo("/MultipleSpecimenFlexInitAction");
		addRequestParameter("pageOf", "pageOfMultipleSpWithMenu");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnSpecimenArrayAdd()
	{
		setRequestPathInfo("/SpecimenArray");
		addRequestParameter("pageOf", "pageOfSpecimenArray");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfSpecimenArray");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnSimpleQueryDistribution()
	{
		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("pageOf", "pageOfDistribution");
		addRequestParameter("aliasName", "Distribution");
		actionPerform();
		verifyForward("pageOfDistribution");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnOrderView()
	{
		setRequestPathInfo("/RequestListView");
		addRequestParameter("pageNum", "1");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnShippingTracking()
	{
		setRequestPathInfo("/ShowDashboardAction");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	@Test
	public void testClickOnBulkOperation()
	{
		setRequestPathInfo("/BulkOperation");
		addRequestParameter("pageOf", "pageOfBulkOperation");
		actionPerform();
		verifyForward("pageOfBulkOperation");
		verifyNoActionErrors();

	}
}
