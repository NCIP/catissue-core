
package edu.wustl.catissuecore.testcase.admin;

import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;

public class MenuTestCases extends CaTissueSuiteBaseTest
{

	
	public void testClickOnUserAdd()
	{
		setRequestPathInfo("/User");
		addRequestParameter("pageOf", "pageOfUserAdmin");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfUserAdmin");
		verifyNoActionErrors();
	}

	
	public void testClickOnInstitutionAdd()
	{
		setRequestPathInfo("/Institution");
		addRequestParameter("pageOf", "pageOfInstitution");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfInstitution");
		verifyNoActionErrors();

	}

	
	public void testClickOnDepartmentAdd()
	{
		setRequestPathInfo("/Department");
		addRequestParameter("pageOf", "pageOfDepartment");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfDepartment");
		verifyNoActionErrors();

	}

	
	public void testClickOnCancerResearchGroupAdd()
	{
		setRequestPathInfo("/CancerResearchGroup");
		addRequestParameter("pageOf", "pageOfCancerResearchGroup");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfCancerResearchGroup");
		verifyNoActionErrors();

	}

	
	public void testClickOnSiteAdd()
	{
		setRequestPathInfo("/Site");
		addRequestParameter("pageOf", "pageOfSite");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfSite");
		verifyNoActionErrors();
	}

	
	public void testClickOnStorageTypeAdd()
	{
		setRequestPathInfo("/StorageType");
		addRequestParameter("pageOf", "pageOfStorageType");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfStorageType");
		verifyNoActionErrors();
	}

	
	public void testClickOnStorageContainerAdd()
	{
		setRequestPathInfo("/OpenStorageContainer");
		addRequestParameter("pageOf", "pageOfStorageContainer");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	
	public void testClickOnSpecimenArrayTypeAdd()
	{
		setRequestPathInfo("/SpecimenArrayType");
		addRequestParameter("pageOf", "pageOfSpecimenArrayType");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfSpecimenArrayType");
		verifyNoActionErrors();
	}

	
	public void testClickOnBiohazardAdd()
	{
		setRequestPathInfo("/Biohazard");
		addRequestParameter("pageOf", "pageOfBioHazard");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfBioHazard");
		verifyNoActionErrors();
	}

	
	public void testClickOnCollectionProtocolAdd()
	{
		setRequestPathInfo("/OpenCollectionProtocol");
		addRequestParameter("pageOf", "pageOfmainCP");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	
	public void testClickOnDistributionProtocolAdd()
	{
		setRequestPathInfo("/DistributionProtocol");
		addRequestParameter("pageOf", "pageOfDistributionProtocol");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfDistributionProtocol");
		verifyNoActionErrors();
	}

	
	public void testClickOnDefineAnnotationsInformationPageAdd()
	{
		setRequestPathInfo("/DefineAnnotationsInformationPage");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyNoActionErrors();

	}

	
	public void testClickOnReportedProblemShow()
	{
		setRequestPathInfo("/ReportedProblemShow");
		addRequestParameter("pageNum", "1");
		actionPerform();
		verifyNoActionErrors();

	}

	
	public void testClickOnConflictSPR()
	{
		setRequestPathInfo("/ConflictView");
		addRequestParameter("pageNum", "1");
		actionPerform();
		verifyNoActionErrors();

	}

	
	public void testClickOnCpBasedSearch()
	{
		setRequestPathInfo("/CpBasedSearch");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
		setRequestPathInfo("/showCpAndParticipants");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
	}

	
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

	
	public void testClickOnSpecimenEdit()
	{
		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		addRequestParameter("aliasName", "Specimen");
		actionPerform();
		verifyForward("pageOfNewSpecimen");
		verifyNoActionErrors();
	}

	
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

	
	public void testClickOnCreateSpecimenAliquots()
	{
		setRequestPathInfo("/Aliquots");
		addRequestParameter("pageOf", "pageOfAliquot");
		actionPerform();
		verifyForward("pageOfAliquot");
		verifyNoActionErrors();
	}

	
	public void testClickOnEvents()
	{
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	
	public void testClickOnMultipleSpecimen()
	{
		setRequestPathInfo("/MultipleSpecimenFlexInitAction");
		addRequestParameter("pageOf", "pageOfMultipleSpWithMenu");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	
	public void testClickOnSpecimenArrayAdd()
	{
		setRequestPathInfo("/SpecimenArray");
		addRequestParameter("pageOf", "pageOfSpecimenArray");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfSpecimenArray");
		verifyNoActionErrors();
	}

	
	public void testClickOnSimpleQueryDistribution()
	{
		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("pageOf", "pageOfDistribution");
		addRequestParameter("aliasName", "Distribution");
		actionPerform();
		verifyForward("pageOfDistribution");
		verifyNoActionErrors();
	}

	
	public void testClickOnOrderView()
	{
		setRequestPathInfo("/RequestListView");
		addRequestParameter("pageNum", "1");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}

	
	public void testClickOnShippingTracking()
	{
		setRequestPathInfo("/ShowDashboardAction");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}
	
	
	public void testClickOnSummary()
	{
		setRequestPathInfo("/Summary");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}	
}