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
	 * Test Infectious Biohazard Add.
	 */
	@Test
	public void testAddBioHazard()
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
		
		//add Biohazard object to objectMap
		TestCaseUtility.setNameObjectMap("Biohazard",biohazard);
	}
	@Test
	/**
	 *  Add Carcinogen Biohazard.
	 */
	public void testAddCarcinogenBioHazard()
	{
		addRequestParameter("name", "Biohazard_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("type", "Carcinogen");
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
		
		//add Biohazard object to objectMap
		TestCaseUtility.setNameObjectMap("CarcinogenBiohazard",biohazard);
	}
	@Test
	/**
	 * Add Mutagen Biohazard.
	 */
	public void testAddMutagenBioHazard()
	{
		addRequestParameter("name", "Biohazard_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("type", "Mutagen");
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
		
		//add Biohazard object to objectMap
		TestCaseUtility.setNameObjectMap("MutagenBiohazard",biohazard);
	}
	@Test
	/**
	 * Add Not Specified Biohazard.
	 */
	public void testAddNotSpecifiedBioHazard()
	{
		addRequestParameter("name", "Biohazard_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("type", "Not Specified");
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
		
		//add Biohazard object to objectMap
		TestCaseUtility.setNameObjectMap("NotSpecifiedBiohazard",biohazard);
	}
	@Test
	/**
	 * Add Radioactive Biohazard.
	 */
	public void testAddRadioactiveBioHazard()
	{
		addRequestParameter("name", "Biohazard_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("type", "Radioactive");
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
		
		//add Biohazard object to objectMap
		TestCaseUtility.setNameObjectMap("RadioactiveBiohazard",biohazard);
	}
	@Test
	/**
	 * Add Toxic Biohazard.
	 */
	public void testAddToxicBioHazard()
	{
		addRequestParameter("name", "Biohazard_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("type", "Toxic");
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
		
		//add Biohazard object to objectMap
		TestCaseUtility.setNameObjectMap("ToxicBiohazard",biohazard);
	}
	/**
	 * Test Biohazard Edit.
	 */
	@Test
	public void testBioHazardEdit()
	{
		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		RequestParameterUtility.setEditBioHazardParams(this);
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
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
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
	/**
	 * Add Biohazard without specifying mandatory Parameters as name and type.
	 * Negative test case.
	 */
	@Test
	public void testAddBioHazardAddWithoutMandatoryParams()
	{
		addRequestParameter("name", "");
		addRequestParameter("type", "");
		addRequestParameter("operation", "add");
		setRequestPathInfo("/BiohazardAdd");
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errorNames[] = new String[]{"errors.item.required","errors.item.selected"};
		verifyActionErrors(errorNames);	
	}
	/**
	 * Edit Biohazard without specifying mandatory Parameters as name and type.
	 * Negative test case.
	 */
	@Test
	public void testEditBioHazardEditWithoutMandatoryParams()
	{
		setRequestPathInfo("/SimpleSearch");
		RequestParameterUtility.setEditBioHazardParams(this);
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
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
			verifyActionErrors(errorNames);
		}
		addRequestParameter("name", "");
		addRequestParameter("type", "");
		addRequestParameter("operation", "edit");
		setRequestPathInfo("/BiohazardEdit");
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errorNames[] = new String[]{"errors.item.required","errors.item.selected"};
		verifyActionErrors(errorNames);	
	}
}
