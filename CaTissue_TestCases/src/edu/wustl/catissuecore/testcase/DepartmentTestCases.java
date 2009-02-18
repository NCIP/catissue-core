package edu.wustl.catissuecore.testcase;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.DepartmentForm;
import edu.wustl.catissuecore.domain.Department;


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
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","Dept");
		addRequestParameter("pageOf","pageOfDepartment");
		addRequestParameter("operation","search");
		actionPerform();
		verifyForward("success");

		/*common search action.Generates department form*/
		Department dept = (Department) TestCaseUtility.getNameObjectMap("Department");
		setRequestPathInfo("/DepartmentSearch");
		addRequestParameter("name", "testDepartment");
		addRequestParameter("id", ""+dept.getId());
		actionPerform();
		verifyForward("pageOfDepartment");

		/*Edit Action*/
		dept.setName("Dept1_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name",dept.getName());
		setRequestPathInfo("/DepartmentEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		TestCaseUtility.setNameObjectMap("Department",dept);
	}
}
