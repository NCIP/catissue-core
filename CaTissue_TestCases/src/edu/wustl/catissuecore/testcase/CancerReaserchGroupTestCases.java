package edu.wustl.catissuecore.testcase;

import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.common.bizlogic.DefaultBizLogic;
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
		verifyNoActionErrors();
		
		CancerResearchGroupForm form=(CancerResearchGroupForm) getActionForm();
		CancerResearchGroup crg = new CancerResearchGroup();
		crg.setId(form.getId());
		crg.setName(form.getName());
		TestCaseUtility.setNameObjectMap("CancerResearchGroup",crg);
	}
	/**
	 * Test CancerResearchGroup Add With existing Name.
	 * Negative test.
	 */
	@Test
	public void testCancerResearchGroupAddWithSameName()
	{
		CancerResearchGroup crg = (CancerResearchGroup) TestCaseUtility.getNameObjectMap("CancerResearchGroup");
		addRequestParameter("name",crg.getName());
		addRequestParameter("operation","add");
		setRequestPathInfo("/CancerResearchGroupAdd");
		actionPerform();
		verifyForward("failure");
		
		//verify action error
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test CancerResearchGroup Add With Blank Name.
	 * Negative Test
	 */
	@Test
	public void testCancerResearchGroupAddWithNullName()
	{
		addRequestParameter("name","");
		addRequestParameter("operation","add");
		setRequestPathInfo("/CancerResearchGroupAdd");
		actionPerform();
		verifyForward("failure");
		
		//verify action error
		String errormsg[] = new String[]{"errors.item.required"};
		verifyActionErrors(errormsg);
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
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<CancerResearchGroup> crgList = null;
		try 
		{
			crgList = bizLogic.retrieve("CancerResearchGroup");
		}
		catch (DAOException e) 
		{
			e.printStackTrace();
			System.out.println("CancerResearchGroupTestCases.testCancerResearchGroupEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		if(crgList.size() > 1)
		{
		    verifyForward("success");
		    verifyNoActionErrors();
		}
		else if(crgList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfCancerResearchGroup&operation=search&id=" + crg.getId());
			verifyNoActionErrors();
		}
		else
		{
			verifyForward("failure");
			
			//verify action errors
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
			verifyActionErrors(errorNames);
		}
		
		/*CancerResearchGroup search action to generate CancerResearchGroupForm*/
		
		setRequestPathInfo("/CancerResearchGroupSearch");
		addRequestParameter("id", ""+crg.getId());
     	actionPerform();
		verifyForward("pageOfCancerResearchGroup");
		verifyNoActionErrors();

		/*edit function*/
		crg.setName("CRG1_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name",crg.getName());
		setRequestPathInfo("/CancerResearchGroupEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
		TestCaseUtility.setNameObjectMap("CancerResearchGroup",crg);
	}
	/**
	 * Test CancerResearchGroup Search on Wrong Search Value.
	 * Negative Test
	 */
	@Test
	public void testCancerResearchGroupSearchOnWrongSearchValue()
	{
		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "CancerResearchGroup");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "CancerResearchGroup");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","CancerResearchGroup.IDENTIFIER.bigint");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Equals");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","1234");
		addRequestParameter("pageOf","pageOfCancerResearchGroup");
		addRequestParameter("operation","search");
		actionPerform();
		verifyForwardPath("/SimpleQueryInterface.do?pageOf=pageOfCancerResearchGroup&aliasName=CancerResearchGroup");
		
		//verify action error
		String errormsg[] = new String[]{"simpleQuery.noRecordsFound"};
		verifyActionErrors(errormsg);
	}
}
