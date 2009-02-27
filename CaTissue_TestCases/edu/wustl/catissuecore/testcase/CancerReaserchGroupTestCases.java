package edu.wustl.catissuecore.testcase;

import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.util.dbManager.DAOException;

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
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","");
		addRequestParameter("pageOf","pageOfCancerResearchGroup");
		addRequestParameter("operation","search");
		actionPerform();
	
		CancerResearchGroup crg = (CancerResearchGroup) TestCaseUtility.getNameObjectMap("CancerResearchGroup");
		AbstractDAO dao = (AbstractDAO) TestCaseUtility.getNameObjectMap("DAO");
		List l = null;
		try 
		{
			dao.openSession(null);
			l = dao.retrieve("CancerResearchGroup");
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
			verifyForwardPath("/SearchObject.do?pageOf=pageOfCancerResearchGroup&operation=search&id=" + crg.getId());
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
	
        /*CancerResearchGroup search action to generate CancerResearchGroupForm*/
		
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
