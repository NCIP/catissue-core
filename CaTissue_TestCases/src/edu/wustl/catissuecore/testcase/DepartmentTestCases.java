package src.edu.wustl.catissuecore.testcase;

import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.DepartmentForm;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.common.dao.AbstractDAO;
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
		AbstractDAO dao = (AbstractDAO) TestCaseUtility.getNameObjectMap("DAO");
		List l = null;
		try 
		{
			dao.openSession(null);
			l = dao.retrieve("Department");
		}
		catch (DAOException e) 
		{
			e.printStackTrace();
		}
		
		if(l.size() > 1)
		{
		    verifyForward("success");
		}
		else if(l.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfDepartment&operation=search&id=" + dept.getId());
		}
		else
		{
			verifyForward("failure");
		}
		try
		{
			dao.closeSession();
		}
		catch (DAOException e) 
		{
		    e.printStackTrace();
		}
		
		/*common search action.Generates department form*/
		
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
