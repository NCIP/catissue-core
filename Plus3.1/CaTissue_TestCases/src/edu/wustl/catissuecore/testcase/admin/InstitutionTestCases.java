
package edu.wustl.catissuecore.testcase.admin;

import java.util.List;

import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.bizlogic.InstitutionBizLogic;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;

/**
 * This class contains test cases for institution add/edit
 * @author Himanshu Aseeja
 */

public class InstitutionTestCases extends CaTissueSuiteBaseTest
{

	
	/**
	 * Test Institution Add.
	 */
	
	public void testInstitutionAdd()
	{
		InstitutionForm institutionForm = new InstitutionForm();
		institutionForm.setName("Inst_"+UniqueKeyGeneratorUtil.getUniqueKey());
		institutionForm.setOperation("add");
		setRequestPathInfo("/InstitutionAdd");
		setActionForm(institutionForm);	
		
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
		assertEquals(InstitutionForm.class.getName(),getActionForm().getClass().getName());
		
		InstitutionForm form = (InstitutionForm) getActionForm();
		Institution institution = new Institution();
		institution.setId(form.getId());
		institution.setName(form.getName());
		TestCaseUtility.setNameObjectMap("Institution", institution);
	}

	/**
	 * Test Institution Add With existing Name.
	 * Negative test Case.
	 */
	
	public void testInstitutionAddWithSameName()
	{
		Institution inst = (Institution) TestCaseUtility.getNameObjectMap("Institution");
		InstitutionForm institutionForm = new InstitutionForm();
		institutionForm.setName(inst.getName());
		institutionForm.setOperation("add");
		
		setRequestPathInfo("/InstitutionAdd");
		setActionForm(institutionForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
		assertEquals(InstitutionForm.class.getName(),getActionForm().getClass().getName());
	}

	/**
	 * Test Institution Add With Blank Name.
	 * Negative Test Case.
	 */
	
	public void testInstitutionAddWithNullName()
	{
		InstitutionForm institutionForm = new InstitutionForm();
		institutionForm.setName(null);
		institutionForm.setOperation("add");
		
		setRequestPathInfo("/InstitutionAdd");
		setActionForm(institutionForm);
		actionPerform();
		verifyForward("failure");

		//verify action errors
		String errormsg[] = new String[]{"errors.item.required"};
		verifyActionErrors(errormsg);
		assertEquals(InstitutionForm.class.getName(),getActionForm().getClass().getName());
	}

	/**
	 * Test Institution Add With null object.
	 * Negative Test Case.
	 */
	
	public void testInstitutionBizLogicAddWithNullObject()
	{
		try
		{
			new InstitutionBizLogic().insert(null, CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN);
			fail("Null Institution object is getting inserted");
		}
		catch(BizLogicException e)
		{
			logger.error("Exception in InstitutionTestCase :" + e.getMessage());
			e.printStackTrace();
			assertEquals("domain.object.null.err.msg", e.getErrorKeyAsString());
			
		}
	}
	/**
	 * Test Institution Add With NULL Name.
	 * Negative Test Case.
	 */
	
	public void testInstitutionBizLogicAddWithNullName()
	{
		try
		{
			InstitutionBizLogic institutionBizLogic = new InstitutionBizLogic();
			Institution inst = new Institution();
			inst.setName(null);
			institutionBizLogic.insert(inst, CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN);
			fail("Institution with Null name is getting inserted");
		}
		catch(BizLogicException e)
		{
			logger.error("Exception in InstitutionTestCase :" + e.getMessage());
			e.printStackTrace();
			assertEquals("Name is required.", e.getMessage());
			assertEquals("errors.item.required", e.getErrorKeyAsString());
			
		}
	}
	
	/**
	 * Test Institution Search.
	 */
	
	public void testInstitutionSearch()
	{

		/*Simple Search Action*/
		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm(); 
		simpleQueryInterfaceForm.setAliasName("Institution");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Institution");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "Institution.NAME.varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Starts With");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value", "");
		simpleQueryInterfaceForm.setPageOf("pageOfInstitution");
		//addRequestParameter("operation", "search");
		setRequestPathInfo("/SimpleSearch");
		setActionForm(simpleQueryInterfaceForm);

		actionPerform();

		Institution institution = (Institution) TestCaseUtility.getNameObjectMap("Institution");
		DefaultBizLogic bizLogic = new DefaultBizLogic();

		List<Institution> institutionList = null;
		try
		{

			institutionList = bizLogic.retrieve("Institution");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			System.out.println("InstitutionTestCases.testInstitutionEdit(): " + e.getMessage());
			fail(e.getMessage());
		}

		if (institutionList.size() > 1)
		{
			verifyForward("success");
			verifyNoActionErrors();
		}
		else if (institutionList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfInstitution&operation=search&id="
					+ institution.getId());
			verifyNoActionErrors();
			assertEquals(InstitutionForm.class.getName(),getActionForm().getClass().getName());
		}
		else
		{
			verifyForward("failure");
			//verify action errors
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
			verifyActionErrors(errorNames);
		}
		/*common search action.Generates institution form*/

		
	}
	/**
	 * Test Institution Edit.
	 */
	
	public void testInstitutionEdit()
	{
		Institution institution = (Institution) TestCaseUtility.getNameObjectMap("Institution");
		setRequestPathInfo("/InstitutionSearch");
		addRequestParameter("id", "" + institution.getId());
		addRequestParameter("pageOf", "pageOfInstitution" );
		
		actionPerform();
		System.out.println(getActualForward());
		verifyForward("pageOfInstitution");
	
		setRequestPathInfo(getActualForward());
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfInstitution");
		addRequestParameter("menuSelected", "2");
		actionPerform();
		verifyNoActionErrors();
	
		InstitutionForm form = (InstitutionForm)getActionForm();
		/*Edit Action*/
		form.setName("Inst1_" + UniqueKeyGeneratorUtil.getUniqueKey());
		form.setOperation("edit");
		setRequestPathInfo("/InstitutionEdit");
		setActionForm(form);
		actionPerform();
		
		verifyForward("success");
		verifyActionMessages(new String[]{"object.edit.successOnly"});
		institution.setName(form.getName());
		TestCaseUtility.setNameObjectMap("Institution", institution);

	}
	/**
	 * Test Institution Search With wrong Identifier.
	 * Negative Test Case.
	 */
	
	public void testInstitutionSearchOnWrongSearchString()
	{
		/*Simple Search Action*/
		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm(); 
		simpleQueryInterfaceForm.setAliasName("Institution");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Institution");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "Institution.IDENTIFIER.bigint");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Equals");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value", "99999");
		simpleQueryInterfaceForm.setPageOf("pageOfInstitution");
		setRequestPathInfo("/SimpleSearch");
		setActionForm(simpleQueryInterfaceForm);
		
		actionPerform();
		verifyForwardPath("/SimpleQueryInterface.do?pageOf=pageOfInstitution&aliasName=Institution");

		//verify action error
		String errormsg[] = new String[]{"simpleQuery.noRecordsFound"};
		verifyActionErrors(errormsg);
	}

	/**
	 * Test Institution Search With valid name using Institution BizLogic.
	 */
	
	public void testInstitutionBizLogicSearchWithGivenInstitutionName()
	{
		try
		{
			InstitutionBizLogic institutionBizLogic = new InstitutionBizLogic();
			Institution inst = (Institution) TestCaseUtility.getNameObjectMap("Institution");
			logger.info(" ::: Inst Name ::: " + inst.getName());
			String instId = institutionBizLogic.getLatestInstitution(inst.getName()) ;
			logger.info(" ::: Inst ID ::: " + instId);
			assertTrue("Institution Name Found ",true);
		}
		catch(BizLogicException e)
		{
			logger.error("Exception in InstitutionTestCase :" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void testInstitutionBizLogicgetObjectIdMethod()
	{
		InstitutionBizLogic institutionBizLogic = new InstitutionBizLogic();
		String objectId = institutionBizLogic.getObjectId(null, null);
		assertEquals(Constants.ADMIN_PROTECTION_ELEMENT, objectId);
	}
	
	/**
	 * Test Institution Add.
	 */
	
	public void testInstitutionAddByPopUp()
	{
		setRequestPathInfo("/Institution");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfPopUpInstitution");	
		actionPerform();
		verifyForward("pageOfPopUpInstitution");
		verifyNoActionErrors();
	
		setRequestPathInfo("/AddInstitution");
		addRequestParameter("pageOf", "pageOfInstitution");
		addRequestParameter("instituteName", "Inst_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation", "add");		
		actionPerform();
		verifyNoActionErrors();
		
		verifyActionMessages(new String[]{"object.add.successOnly"});
		
		InstitutionForm form = (InstitutionForm) getActionForm();
		Institution institution = new Institution();
		institution.setId(form.getId());
		institution.setName(form.getName());
		TestCaseUtility.setNameObjectMap("Institution", institution);
		
	}
}
