package edu.wustl.catissuecore.testcase;

import java.util.List;

import org.junit.Test;
import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.util.dbManager.DAOException;

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
		addRequestParameter("value(SimpleConditionsNode:1__Condition_value)","");
		addRequestParameter("pageOf","pageOfInstitution");
		addRequestParameter("operation","search");
		actionPerform();
		
		Institution institution = (Institution) TestCaseUtility.getNameObjectMap("Institution");
		AbstractDAO dao = (AbstractDAO) TestCaseUtility.getNameObjectMap("DAO");
		List l = null;
		try 
		{
			dao.openSession(null);
			l = dao.retrieve("Institution");
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
			verifyForwardPath("/SearchObject.do?pageOf=pageOfInstitution&operation=search&id=" + institution.getId());
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
}
