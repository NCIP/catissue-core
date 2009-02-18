package edu.wustl.catissuecore.testcase;

import org.junit.Test;
import edu.wustl.catissuecore.testcase.TestCaseUtility;

import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.domain.Institution;

/**
 * This class contains test cases for institution add/edit
 * @author Himanshu Aseeja
 */

public class InstitutionTestCases extends CaTissueSuiteBaseTest
{
	/**
	 * Test Institution Add.
	 */
	@Test
	public void testInstitutionAdd()
	  {
		  
		  addRequestParameter("name","Inst_"+UniqueKeyGeneratorUtil.getUniqueKey());
		  addRequestParameter("operation","add");
		  setRequestPathInfo("/InstitutionAdd");
		  actionPerform();
		  verifyForward("success");
		  
		  InstitutionForm form=(InstitutionForm) getActionForm();
		  Institution institution = new Institution();
		  institution.setId(form.getId());
		  institution.setName(form.getName());
		  TestCaseUtility.setNameObjectMap("Institution",institution);
      }	 
	
	/**
	 * Test Institution Edit.
	 */
	@Test
	public void testInstitutionEdit()
	{
        /*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "Institution");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "Institution");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","Institution.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1__Condition_value)","i");
		addRequestParameter("pageOf","pageOfInstitution");
		addRequestParameter("operation","search");
		actionPerform();
		verifyForward("success");
		
		/*common search action.Generates institution form*/
		Institution institution = (Institution) TestCaseUtility.getNameObjectMap("Institution");
	    setRequestPathInfo("/InstitutionSearch");
		addRequestParameter("id",""+institution.getId());
		actionPerform();
		verifyForward("pageOfInstitution");
		
		/*Edit Action*/
		institution.setName("Inst1_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name",institution.getName());
		setRequestPathInfo("/InstitutionEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		TestCaseUtility.setNameObjectMap("Institution",institution); 
		
	}
}
