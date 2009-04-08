package edu.wustl.catissuecore.testcase;

import java.util.List;

import org.junit.Test;
import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.dao.exception.DAOException;

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
		  verifyNoActionErrors();
		  
		  InstitutionForm form=(InstitutionForm) getActionForm();
		  Institution institution = new Institution();
		  institution.setId(form.getId());
		  institution.setName(form.getName());
		  TestCaseUtility.setNameObjectMap("Institution",institution);
      }	 
	/**
	 * Test Institution Add With existing Name.
	 * Negative test.
	 */
	@Test
	public void testInstitutionAddWithSameName()
	{
		Institution inst = (Institution) TestCaseUtility.getNameObjectMap("Institution");
		addRequestParameter("name",inst.getName());
		addRequestParameter("operation","add");
		setRequestPathInfo("/InstitutionAdd");
		actionPerform();
		verifyForward("failure");
		
		//verify action errors
		String errormsg[] = new String[] {"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Institution Add With Blank Name.
	 * Negative Test
	 */
	@Test
	public void testInstitutionAddWithNullName()
	{
		addRequestParameter("name","");
		addRequestParameter("operation","add");
		setRequestPathInfo("/InstitutionAdd");
		actionPerform();
		verifyForward("failure");
		
		//verify action errors
		String errormsg[] = new String[]{"errors.item.required"};
		verifyActionErrors(errormsg);
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
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","");
		addRequestParameter("pageOf","pageOfInstitution");
		addRequestParameter("operation","search");
		actionPerform();
		
		Institution institution = (Institution) TestCaseUtility.getNameObjectMap("Institution");
		DefaultBizLogic bizLogic = new DefaultBizLogic(); 
		
		List<Institution> institutionList = null;
		try 
		{

			institutionList = bizLogic.retrieve("Institution");

		}
		catch (DAOException e) 
		{
			e.printStackTrace();
			System.out.println("InstitutionTestCases.testInstitutionEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		if(institutionList.size() > 1)
		{
		    verifyForward("success");
		    verifyNoActionErrors();
		}
		else if(institutionList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfInstitution&operation=search&id=" + institution.getId());
			verifyNoActionErrors();
		}
		else
		{
			verifyForward("failure");
			//verify action errors
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
			verifyActionErrors(errorNames);
		}
		/*common search action.Generates institution form*/
		
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
	/**
	 * Test Institution Search With wrong Identifier.
	 * Negative Test
	 */
	@Test
	public void testInstitutionSearchOnWrongSearchString()
	{
		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "Institution");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "Institution");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","Institution.IDENTIFIER.bigint");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Equals");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","1234");
		addRequestParameter("pageOf","pageOfInstitution");
		addRequestParameter("operation","search");
		actionPerform();
		verifyForwardPath("/SimpleQueryInterface.do?pageOf=pageOfInstitution&aliasName=Institution");
		
		//verify action error
		String errormsg[] = new String[]{"simpleQuery.noRecordsFound"};
		verifyActionErrors(errormsg);
	}
}
