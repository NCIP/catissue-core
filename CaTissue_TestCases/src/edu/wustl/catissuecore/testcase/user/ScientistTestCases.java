package edu.wustl.catissuecore.testcase.user;

import edu.wustl.catissuecore.actionForm.BiohazardForm;
import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.catissuecore.actionForm.DepartmentForm;
import edu.wustl.catissuecore.actionForm.DistributionProtocolForm;
import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.actionForm.SpecimenArrayTypeForm;
import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.RequestParameterUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;


public class ScientistTestCases  extends CaTissueSuiteBaseTest
{
	/**
	 * Test Institution Add.
	 */
	public void testInstitutionAdd()
	{
		InstitutionForm institutionForm = new InstitutionForm();
		institutionForm.setName("Inst_"+UniqueKeyGeneratorUtil.getUniqueKey());
		institutionForm.setOperation("add");
		setRequestPathInfo("/InstitutionAdd");
		setActionForm(institutionForm);	
		
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);		
	}
	/**
	 * Test Department Add.
	 */
	
	public void testDepartmentAdd()
	{
		DepartmentForm departmentForm  = new DepartmentForm ();
		departmentForm.setName("Dept_"+UniqueKeyGeneratorUtil.getUniqueKey());
		departmentForm.setOperation("add");
		setRequestPathInfo("/DepartmentAdd");
		setActionForm(departmentForm);
		
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Site Add.
	 */
	
	public void testAddRepositorySite()
	{
		RequestParameterUtility.setAddSiteParams(this);
		addRequestParameter("type","Repository");
		setRequestPathInfo("/SiteAdd");
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test BioHazard Add Infectious BioHazard.
	 */
	public void testAddBioHazard()
	{
		BiohazardForm bioForm = new BiohazardForm();
		bioForm.setName("Biohazard_" + UniqueKeyGeneratorUtil.getUniqueKey());
		bioForm.setType("Infectious");
		bioForm.setOperation("add");
		bioForm.setComments("BioHazard Comments") ;
		setRequestPathInfo("/BiohazardAdd");
		setActionForm(bioForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Cancer Research Group Add.
	 */
	
	public void testCancerResearchGroupAdd()
	{
		CancerResearchGroupForm cancerResearchGroupForm = new CancerResearchGroupForm();
		cancerResearchGroupForm.setName("CRG_"+UniqueKeyGeneratorUtil.getUniqueKey());
		cancerResearchGroupForm.setOperation("add");
		setRequestPathInfo("/CancerResearchGroupAdd");
		setActionForm(cancerResearchGroupForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Distribution Protocol Add.
	 */
	
	public void testDistributionProtocolAdd()
	{
		DistributionProtocolForm distProtocolForm = new DistributionProtocolForm();
		distProtocolForm.setTitle("dp_"+UniqueKeyGeneratorUtil.getUniqueKey());
		distProtocolForm.setShortTitle("dp_"+UniqueKeyGeneratorUtil.getUniqueKey());
		distProtocolForm.setPrincipalInvestigatorId(1L);
		distProtocolForm.setStartDate("01-12-2009");
		distProtocolForm.setOperation("add") ;
		setRequestPathInfo("/DistributionProtocolAdd");
		setActionForm(distProtocolForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Specimen Array Type Add.
	 */
	
	public void testSpecimenArrayTypeAdd()
	{
		SpecimenArrayTypeForm specForm = new SpecimenArrayTypeForm();
		specForm.setName("array_"+UniqueKeyGeneratorUtil.getUniqueKey());
		specForm.setSpecimenClass("Fluid");
		specForm.setSpecimenTypes(new String[]{"Sweat"});
		specForm.setOneDimensionCapacity(4) ;
		specForm.setTwoDimensionCapacity(4);
		specForm.setOperation("add");
		setRequestPathInfo("/SpecimenArrayTypeAdd");
		setActionForm(specForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Storage Type Add.
	 */
	
	public void testStorageTypeAdd()
	{
		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"srType_" + UniqueKeyGeneratorUtil.getUniqueKey(),3,3,"row","col","22","Active");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	

}
