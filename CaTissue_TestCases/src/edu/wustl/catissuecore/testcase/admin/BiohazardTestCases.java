package edu.wustl.catissuecore.testcase.admin;

import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.BiohazardForm;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.RequestParameterUtility;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;

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
	public void testBioHazardSearch()
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
		catch (BizLogicException e) 
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
	 * Test Biohazard Edit.
	 */
	@Test
	public void testBioHazardEdit()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 * Edit Biohazard without specifying mandatory Parameters as name and type.
	 * Negative test case.
	 */
	@Test
	public void testEditBioHazardEditWithoutMandatoryParams()
	{
		//TODO
		fail("Need to write test case");
	}
	
	/**
	*/
	@Test
	public void testBiohazardAddWithInvalidType()
	{
		//TODO
		fail("Need to write test case");
	}
	
	/**
	*/
	@Test
	public void testBioHazardBizLogicAddWithNullObject()
	{
		//TODO
		fail("Need to write test case");
	}

	/**
	*/
	@Test
	public void testBiohazardBizLogicAddWithNullName()
	{
		//TODO
		fail("Need to write test case");
	}

	/**
	*/
	@Test
	public void testBiohazardBizLogicAddWithNullType()
	{
		//TODO
		fail("Need to write test case");
	}

	/**
	*/
	@Test
	public void testBiohazardBizLogicAddWithInvalidType()
	{
		//TODO
		fail("Need to write test case");
	}

}
