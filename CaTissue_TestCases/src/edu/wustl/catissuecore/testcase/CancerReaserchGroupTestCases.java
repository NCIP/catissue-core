package edu.wustl.catissuecore.testcase;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.catissuecore.domain.CancerResearchGroup;

/**
 * This class contains test cases for Cancer Research Group add/edit
 * @author Himanshu Aseeja
 */

public class CancerReaserchGroupTestCases extends CaTissueSuiteBaseTest
{

	/**
	 * Test Cancer Research Group Add.
	 */
	@Test
	public void testCancerResearchGroupAdd()
	{
		addRequestParameter("name", "CRG_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation", "add");
		setRequestPathInfo("/CancerResearchGroupAdd");
		actionPerform();
		verifyForward("success");
		
		CancerResearchGroupForm form=(CancerResearchGroupForm) getActionForm();
		CancerResearchGroup crg = new CancerResearchGroup();
		crg.setId(form.getId());
		crg.setName(form.getName());
		TestCaseUtility.setNameObjectMap("CancerResearchGroup",crg);
	}
	
	/**
	 * Test Cancer Research Group Edit.
	 */
	@Test
	public void testCancerResearchGroupEdit()
	{
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "CancerResearchGroup");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "CancerResearchGroup");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","CancerResearchGroup.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","crg");
		addRequestParameter("pageOf","pageOfCancerResearchGroup");
		addRequestParameter("operation","search");
		actionPerform();
		verifyForward("success");

	
        /*CancerResearchGroup search action to generate CancerResearchGroupForm*/
		CancerResearchGroup crg = (CancerResearchGroup) TestCaseUtility.getNameObjectMap("CancerResearchGroup");
		setRequestPathInfo("/CancerResearchGroupSearch");
		addRequestParameter("id", ""+crg.getId());
     	actionPerform();
		verifyForward("pageOfCancerResearchGroup");

		/*edit function*/
		crg.setName("CRG1_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name",crg.getName());
		setRequestPathInfo("/CancerResearchGroupEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		TestCaseUtility.setNameObjectMap("CancerResearchGroup",crg);
	}
}
