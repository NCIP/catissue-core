package edu.wustl.catissuecore.testcase;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.BiohazardForm;
import edu.wustl.catissuecore.domain.Biohazard;

/**
 * This class contains test cases for Biohazard add/edit
 * @author Himanshu Aseeja
 */
public class BiohazardTestCases extends CaTissueSuiteBaseTest 
{
	/**
	 * Test Biohazard Add.
	 */
	@Test
	public void testBioHazardAdd()
	{
		addRequestParameter("name", "Biohazard_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("type", "Infectious");
		addRequestParameter("operation", "add");
		setRequestPathInfo("/BiohazardAdd");
		actionPerform();
		verifyForward("success");
		
		BiohazardForm form=(BiohazardForm) getActionForm();
		
		Biohazard biohazard = new Biohazard();
		biohazard.setId(form.getId());
		biohazard.setName(form.getName());
		biohazard.setType(form.getType());
		
		TestCaseUtility.setNameObjectMap("Biohazard",biohazard);
	}
	
	/**
	 * Test Biohazard Edit.
	 */
	@Test
	public void testBioHazardEdit()
	{
		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "Biohazard");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "Biohazard");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","Biohazard.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","b");
		addRequestParameter("pageOf","pageOfBioHazard");
		addRequestParameter("operation","search");
		actionPerform();
		verifyForward("success");
		
		/*Biohazard search action to generate Biohazard form*/
		setRequestPathInfo("/BiohazardSearch");
		Biohazard biohazard = (Biohazard) TestCaseUtility.getNameObjectMap("Biohazard");
		addRequestParameter("id", "" + biohazard.getId());
		addRequestParameter("type", "Infectious");
     	actionPerform();
		verifyForward("pageOfBioHazard");

		/*Edit Action*/
		biohazard.setName("Biohazard1_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name",biohazard.getName());
		setRequestPathInfo("/BiohazardEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		TestCaseUtility.setNameObjectMap("Biohazard",biohazard);
	}
}
