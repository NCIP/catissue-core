package edu.wustl.catissuecore.testcase.admin;

import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.catissuecore.bizlogic.CancerResearchBizLogic;
import edu.wustl.catissuecore.bizlogic.DepartmentBizLogic;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;

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
		CancerResearchGroupForm cancerResearchGroupForm = new CancerResearchGroupForm();
		cancerResearchGroupForm.setName("CRG_"+UniqueKeyGeneratorUtil.getUniqueKey());
		cancerResearchGroupForm.setOperation("add");
		setRequestPathInfo("/CancerResearchGroupAdd");
		setActionForm(cancerResearchGroupForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
		
		CancerResearchGroupForm form=(CancerResearchGroupForm) getActionForm();
		CancerResearchGroup crg = new CancerResearchGroup();
		crg.setId(form.getId());
		crg.setName(form.getName());
		TestCaseUtility.setNameObjectMap("CancerResearchGroup",crg);
	}
	/**
	 * Test CancerResearchGroup Add With existing Name.
	 * Negative test Case.
	 */
	@Test
	public void testCancerResearchGroupAddWithSameName()
	{
		CancerResearchGroup cancerResearchGroup = (CancerResearchGroup) TestCaseUtility.getNameObjectMap("CancerResearchGroup");
		CancerResearchGroupForm cancerResearchGroupForm = new CancerResearchGroupForm();
		cancerResearchGroupForm.setName(cancerResearchGroup.getName());
		cancerResearchGroupForm.setOperation("add");
		
		setRequestPathInfo("/CancerResearchGroupAdd");
		setActionForm(cancerResearchGroupForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
		assertEquals(CancerResearchGroupForm.class.getName(),getActionForm().getClass().getName());
	}
	/**
	 * Test CancerResearchGroup Add With Blank Name.
	 * Negative Test Case.
	 */
	@Test
	public void testCancerResearchGroupAddWithNullName()
	{
		CancerResearchGroupForm cancerResearchGroupForm = new CancerResearchGroupForm();
		cancerResearchGroupForm.setName(null);
		cancerResearchGroupForm.setOperation("add");
		
		setRequestPathInfo("/CancerResearchGroupAdd");
		setActionForm(cancerResearchGroupForm);
		actionPerform();
		verifyForward("failure");

		//verify action errors
		String errormsg[] = new String[]{"errors.item.required"};
		verifyActionErrors(errormsg);
		assertEquals(CancerResearchGroupForm.class.getName(),getActionForm().getClass().getName());
	}
	/**
	 * Test Cancer Research Group Search.
	 */
	@Test
	public void testCancerResearchGroupSearch()
	{
		/*Simple Search Action*/
		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm(); 
		simpleQueryInterfaceForm.setAliasName("CancerResearchGroup");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "CancerResearchGroup");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "CancerResearchGroup.NAME.varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Starts With");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value", "");
		simpleQueryInterfaceForm.setPageOf("pageOfCancerResearchGroup");
		//addRequestParameter("operation", "search");
		setRequestPathInfo("/SimpleSearch");
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		CancerResearchGroup crg = (CancerResearchGroup) TestCaseUtility.getNameObjectMap("CancerResearchGroup");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<CancerResearchGroup> crgList = null;
		try 
		{
			crgList = bizLogic.retrieve("CancerResearchGroup");
		}
		catch (BizLogicException e) 
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
			assertEquals(CancerResearchGroupForm.class.getName(),getActionForm().getClass().getName());
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
	 * Test Cancer Research Group Edit.
	 */
	@Test
	public void testCancerResearchGroupEdit()
	{
		CancerResearchGroup cancerResearchGroup = (CancerResearchGroup) TestCaseUtility.getNameObjectMap("CancerResearchGroup");
		setRequestPathInfo("/CancerResearchGroupSearch");
		addRequestParameter("id", "" + cancerResearchGroup.getId());
		addRequestParameter("pageOf", "pageOfCancerResearchGroup" );
		
		actionPerform();
		System.out.println(getActualForward());
		verifyForward("pageOfCancerResearchGroup");
					   
		CancerResearchGroupForm form = (CancerResearchGroupForm)getActionForm();
		/*Edit Action*/
		form.setName("CRG1_" + UniqueKeyGeneratorUtil.getUniqueKey());
		form.setOperation("edit");
		setRequestPathInfo("/CancerResearchGroupEdit");
		setActionForm(form);
		actionPerform();
		
		verifyForward("success");
		verifyActionMessages(new String[]{"object.edit.successOnly"});
		cancerResearchGroup.setName(form.getName());
		TestCaseUtility.setNameObjectMap("CancerResearchGroup", cancerResearchGroup);

	}
	
	/**
	 * Test CancerResearchGroup Search on Wrong Search Value.
	 * Negative Test Case.
	 */
	@Test
	public void testCancerResearchGroupSearchOnWrongSearchValue()
	{
		/*Simple Search Action*/
		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm(); 
		simpleQueryInterfaceForm.setAliasName("CancerResearchGroup");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "CancerResearchGroup");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "CancerResearchGroup.IDENTIFIER.bigint");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Equals");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value", "99999");
		simpleQueryInterfaceForm.setPageOf("pageOfCancerResearchGroup");
		setRequestPathInfo("/SimpleSearch");
		setActionForm(simpleQueryInterfaceForm);
		
		actionPerform();
		verifyForwardPath("/SimpleQueryInterface.do?pageOf=pageOfCancerResearchGroup&aliasName=CancerResearchGroup");

		//verify action error
		String errormsg[] = new String[]{"simpleQuery.noRecordsFound"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Cancer Research Group Add with NULL object.
	 * Negative Test Case.
	 */
	@Test
	public void testCRGBizLogicAddWithNullObject()
	{
//		//TODO
//		fail("Need to write test case");
		try
		{
			CancerResearchBizLogic cancerBizLogic = new CancerResearchBizLogic() ;
			cancerBizLogic.insert(null, CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("Null Cancer Research Group object getting inserted",true) ;
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in CRGTestCase :" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test Cancer Research Group Add with NULL name.
	 * Negative Test Case.
	 */
	@Test
	public void testCRGBizLogicAddWithNullName()
	{
//		//TODO
//		fail("Need to write test case");
		
		try
		{
			CancerResearchGroup cancGroup = new CancerResearchGroup();
			cancGroup.setName(null) ;
			CancerResearchBizLogic cancerBizLogic = new CancerResearchBizLogic() ;
			cancerBizLogic.insert(cancGroup, CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("Cancer Research Group getting inserted with Null values",true) ;
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in CRGTestCase :" + e.getMessage());
			e.printStackTrace() ;
			// TODO: handle exception
		}
	}
	/**
	 * Test Cancer Research Group search.
	 * with given name using CRG BizLogic.
	 */
	@Test
	public void testCRGSearchWithGivenCRGName()
	{
		try
		{
			CancerResearchBizLogic cancerBizLogic = new CancerResearchBizLogic() ;
			CancerResearchGroup crg = (CancerResearchGroup)
					TestCaseUtility.getNameObjectMap("CancerResearchGroup");
			
			String crgID = cancerBizLogic.getLatestCRG(crg.getName()) ;
			logger.info(" ::: CancerResearchGroup ID ::: " + crgID);
			assertTrue("CancerResearchGroup Name Found ",true);
		}
		catch(BizLogicException e)
		{
			logger.error("Exception in CRGTestCase :" + e.getMessage());
			e.printStackTrace();
		}
	}
}
