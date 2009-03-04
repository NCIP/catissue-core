package edu.wustl.catissuecore.testcase;

import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.DepartmentForm;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * This class contains test cases for Department add/edit
 * @author Himanshu Aseeja
 */

public class DepartmentTestCases extends CaTissueSuiteBaseTest
{
	/**
	 * Test Department Add.
	 */
	@Test
	public void testDepartmentAdd()
	{
		addRequestParameter("name","Dept_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation","add");
		setRequestPathInfo("/DepartmentAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		assertEquals("edu.wustl.catissuecore.actionForm.DepartmentForm",getActionForm().getClass().getName());
		
		DepartmentForm form=(DepartmentForm) getActionForm();
		Department dept = new Department();
		dept.setId(form.getId());
		dept.setName(form.getName());
		TestCaseUtility.setNameObjectMap("Department",dept);
	}
	/**
	 * Test Department Add With existing Name.
	 * Negative test.
	 */
	@Test
	public void testDepartmentAddWithSameName()
	{
		Department dept = (Department) TestCaseUtility.getNameObjectMap("Department");
		addRequestParameter("name",dept.getName());
		addRequestParameter("operation","add");
		setRequestPathInfo("/DepartmentAdd");
		actionPerform();
		verifyForward("failure");
		
		//verify action error
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Department Add With Blank Name.
	 * Negative Test
	 */
	@Test
	public void testDepartmentAddWithNullName()
	{
		addRequestParameter("name","");
		addRequestParameter("operation","add");
		setRequestPathInfo("/DepartmentAdd");
		actionPerform();
		verifyForward("failure");
		
		//verify action error
		String errormsg[] = new String[]{"errors.item.required"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Department Edit.
	 */
	@Test
	public void testDepartmentEdit()
	{

		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "Department");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "Department");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","Department.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","");
		addRequestParameter("pageOf","pageOfDepartment");
		addRequestParameter("operation","search");
		actionPerform();
		
		Department dept = (Department) TestCaseUtility.getNameObjectMap("Department");
		DefaultBizLogic bizLogic = new DefaultBizLogic(); 
		List<Department> departmentList = null;
		try 
		{
			departmentList = bizLogic.retrieve("Department");
		}
		catch (DAOException e) 
		{
			e.printStackTrace();
	     	System.out.println("DepartmentTestCases.testDepartmentEdit(): "+e.getMessage());
	     	fail(e.getMessage());
		}
		
		if(departmentList.size() > 1)
		{
		    verifyForward("success");
			verifyNoActionErrors();
		}
		else if(departmentList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfDepartment&operation=search&id=" + dept.getId());
			verifyNoActionErrors();
		}
		else
		{
			verifyForward("failure");
			//verify action errors
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
			verifyActionErrors(errorNames);
		}
		
		
		/*common search action.Generates department form*/
		
		setRequestPathInfo("/DepartmentSearch");
		addRequestParameter("name", "testDepartment");
		addRequestParameter("id", ""+dept.getId());
		actionPerform();
		verifyForward("pageOfDepartment");
		verifyNoActionErrors();

		/*Edit Action*/
		dept.setName("Dept1_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name",dept.getName());
		setRequestPathInfo("/DepartmentEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
		TestCaseUtility.setNameObjectMap("Department",dept);
	}
	/**
	 * Test Department Search With wrong Identifier.
	 * Negative Test
	 */
	@Test
	public void testDepartmentSearchOnWrongSearchValue()
	{
		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "Department");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "Department");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","Department.IDENTIFIER.bigint");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Equals");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","1234");
		addRequestParameter("pageOf","pageOfDepartment");
		addRequestParameter("operation","search");
		actionPerform();
		verifyForwardPath("/SimpleQueryInterface.do?pageOf=pageOfDepartment&aliasName=Department");
		
		//verify action error
		String errormsg[] = new String[]{"simpleQuery.noRecordsFound"};
		verifyActionErrors(errormsg);
	}
}
