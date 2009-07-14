package edu.wustl.catissuecore.testcase.admin;

import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.DepartmentForm;
import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;


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
		DepartmentForm departmentForm  = new DepartmentForm ();
		departmentForm.setName("Dept_"+UniqueKeyGeneratorUtil.getUniqueKey());
		departmentForm.setOperation("add");
		setRequestPathInfo("/DepartmentAdd");
		setActionForm(departmentForm);
		
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
				
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
		DepartmentForm departmentForm  = new DepartmentForm ();
		departmentForm.setName(dept.getName());
		departmentForm.setOperation("add");
		setRequestPathInfo("/DepartmentAdd");
		setActionForm(departmentForm);
		actionPerform();
		verifyForward("failure");
		
		//verify action error
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
		
		assertEquals(DepartmentForm.class.getName(),getActionForm().getClass().getName());
	}
	/**
	 * Test Department Add With Blank Name.
	 * Negative Test
	 */
	@Test
	public void testDepartmentAddWithNullName()
	{
		DepartmentForm departmentForm  = new DepartmentForm ();
		departmentForm.setName(null);
		departmentForm.setOperation("add");
		setRequestPathInfo("/DepartmentAdd");
		setActionForm(departmentForm);
		
		actionPerform();
		verifyForward("failure");
		
		//verify action error
		String errormsg[] = new String[]{"errors.item.required"};
		verifyActionErrors(errormsg);
		assertEquals(DepartmentForm.class.getName(),getActionForm().getClass().getName());
	}
	/**
	 * Test Department Edit.
	 */
	@Test
	public void testDepartmentSearch()
	{

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm(); 
		simpleQueryInterfaceForm.setAliasName("Institution");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Department");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "Department.NAME.varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Starts With");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value", "");
		simpleQueryInterfaceForm.setPageOf("pageOfDepartment");
		
		setRequestPathInfo("/SimpleSearch");
		setActionForm(simpleQueryInterfaceForm);

		actionPerform();
		
		Department dept = (Department) TestCaseUtility.getNameObjectMap("Department");
		DefaultBizLogic bizLogic = new DefaultBizLogic(); 
		List<Department> departmentList = null;
		try 
		{
			departmentList = bizLogic.retrieve("Department");
		}
		catch (BizLogicException e) 
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
			assertEquals(DepartmentForm.class.getName(),getActionForm().getClass().getName());
		}
		else
		{
			verifyForward("failure");
			//verify action errors
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
			verifyActionErrors(errorNames);
		}
		
	}
	
	/**
	 * 
	 */
	@Test
	public void testDepartmentEdit()
	{
		Department department = (Department) TestCaseUtility.getNameObjectMap("Department");
		setRequestPathInfo("/DepartmentSearch");
		addRequestParameter("id", "" + department.getId());
		addRequestParameter("pageOf", "pageOfDepartment" );
		
		actionPerform();
		System.out.println(getActualForward());
		verifyForward("pageOfDepartment");
					   
		DepartmentForm form = (DepartmentForm)getActionForm();
		/*Edit Action*/
		form.setName("Dept1_" + UniqueKeyGeneratorUtil.getUniqueKey());
		form.setOperation("edit");
		setRequestPathInfo("/DepartmentEdit");
		setActionForm(form);
		actionPerform();
		
		verifyForward("success");
		verifyActionMessages(new String[]{"object.edit.successOnly"});
		department.setName(form.getName());
		TestCaseUtility.setNameObjectMap("Institution", department);

	}
	
	/**
	 * Test Department Search With wrong Identifier.
	 * Negative Test
	 */
	@Test
	public void testDepartmentSearchOnWrongSearchValue()
	{
		/*Simple Search Action*/
		/*Simple Search Action*/
		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm(); 
		simpleQueryInterfaceForm.setAliasName("Department");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Department");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "Department.IDENTIFIER.bigint");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Equals");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value", "99999");
		simpleQueryInterfaceForm.setPageOf("pageOfDepartment");
		setRequestPathInfo("/SimpleSearch");
		setActionForm(simpleQueryInterfaceForm);
		
		actionPerform();
		verifyForwardPath("/SimpleQueryInterface.do?pageOf=pageOfDepartment&aliasName=Department");

		//verify action error
		String errormsg[] = new String[]{"simpleQuery.noRecordsFound"};
		verifyActionErrors(errormsg);
	}
	
	/**
	 */
	@Test
	public void testDepartmentBizLogicAddWithNullObject()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testDepartmentBizLogicAddWithNullName()
	{
		//TODO
		fail("Need to write test case");
	}
}
