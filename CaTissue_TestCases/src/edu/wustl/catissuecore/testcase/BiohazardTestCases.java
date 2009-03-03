package edu.wustl.catissuecore.testcase;

import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.BiohazardForm;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class contains test cases for Biohazard add/edit
 * @author Himanshu Aseeja
 */
public class BiohazardTestCases extends CaTissueSuiteBaseTest 
{
	/**
	 * Test Biohazard Add.
	 */
	@Test
	public void testBioHazardAdd()
	{
		addRequestParameter("name", "Biohazard_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("type", "Infectious");
		addRequestParameter("operation", "add");
		setRequestPathInfo("/BiohazardAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
		BiohazardForm form=(BiohazardForm) getActionForm();
		
		Biohazard biohazard = new Biohazard();
		biohazard.setId(form.getId());
		biohazard.setName(form.getName());
		biohazard.setType(form.getType());
		
		TestCaseUtility.setNameObjectMap("Biohazard",biohazard);
	}
	/**
	 * Test Biohazard Edit.
	 */
	@Test
	public void testBioHazardEdit()
	{
		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "Biohazard");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "Biohazard");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","Biohazard.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","");
		addRequestParameter("pageOf","pageOfBioHazard");
		addRequestParameter("operation","search");
		actionPerform();
				
		Biohazard biohazard = (Biohazard) TestCaseUtility.getNameObjectMap("Biohazard");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<Biohazard> biohazardList = null;
		try 
		{
			biohazardList = bizLogic.retrieve("Biohazard");
		}
		catch (DAOException e) 
		{
			e.printStackTrace();
			System.out.println("BiohazardTestCases.testBioHazardEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		if(biohazardList.size() > 1)
		{
		    verifyForward("success");
		    verifyNoActionErrors();
		}
		else if(biohazardList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfBioHazard&operation=search&id=" + biohazard.getId());
			verifyNoActionErrors();
		}
		else
		{
			verifyForward("failure");
			//verify action errors
			String errorNames[] = new String[1];
			errorNames[0] = "simpleQuery.noRecordsFound";
			verifyActionErrors(errorNames);
		}
				
		/*Biohazard search action to generate Biohazard form*/
		setRequestPathInfo("/BiohazardSearch");
		addRequestParameter("id", "" + biohazard.getId());
		addRequestParameter("type", "Infectious");
     	actionPerform();
		verifyForward("pageOfBioHazard");
		verifyNoActionErrors();

		/*Edit Action*/
		biohazard.setName("Biohazard1_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name",biohazard.getName());
		setRequestPathInfo("/BiohazardEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
		TestCaseUtility.setNameObjectMap("Biohazard",biohazard);
	}
}
