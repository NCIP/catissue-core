package edu.wustl.catissuecore.testcase.admin;


import java.util.Date;
import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.DistributionProtocolForm;
import edu.wustl.catissuecore.bizlogic.DistributionProtocolBizLogic;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.DistributionSpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;

/**
 * This class contains test cases for Distribution Protocol add/edit
 * @author Himanshu Aseeja
 */
public class DistributionProtocolTestCases extends CaTissueSuiteBaseTest
{
	/**
	 * Test Distribution Protocol Add.
	 */
	@Test
	public void testDistributionProtocolAdd()
	{
		DistributionProtocolForm distProtocolForm = new DistributionProtocolForm();
		distProtocolForm.setTitle("dp_"+UniqueKeyGeneratorUtil.getUniqueKey());
		distProtocolForm.setShortTitle("dp_"+UniqueKeyGeneratorUtil.getUniqueKey());
		distProtocolForm.setPrincipalInvestigatorId(1L);
		distProtocolForm.setStartDate("01-12-2009");
		distProtocolForm.setOperation("add") ;
		setRequestPathInfo("/DistributionProtocolAdd");
		setActionForm(distProtocolForm);
		actionPerform();
		verifyForward("success");	
		verifyNoActionErrors();
		
		DistributionProtocolForm form = (DistributionProtocolForm) getActionForm();
		DistributionProtocol distributionProtocol = new DistributionProtocol();
		
		User principalInvestigator=new User();
		principalInvestigator.setId(form.getId());
		
		distributionProtocol.setTitle(form.getTitle());
		distributionProtocol.setShortTitle(form.getShortTitle());
		distributionProtocol.setId(form.getId());
		
		Date date = new Date();
		String dd = new String();
		String mm = new String();
		String yyyy = new String();
		dd = form.getStartDate().substring(0,1);
		mm = form.getStartDate().substring(3,4);
		yyyy = form.getStartDate().substring(6,9);
		date.setDate(Integer.parseInt(dd));
		date.setMonth(Integer.parseInt(mm));
		date.setYear(Integer.parseInt(yyyy));
		distributionProtocol.setStartDate(date);
		
		TestCaseUtility.setNameObjectMap("DistributionProtocol",distributionProtocol);
	}
	/**
	 * Test Distribution Protocol Search.
	 */
	@Test
	public void testDistributionProtocolSearch()
	{
		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		SimpleQueryInterfaceForm simpleForm = new SimpleQueryInterfaceForm() ;
		simpleForm.setAliasName("DistributionProtocol") ;
		simpleForm.setPageOf("pageOfDistributionProtocol");
		simpleForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "DistributionProtocol");
		simpleForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "SpecimenProtocol.TITLE.varchar");
		simpleForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Starts With");
		simpleForm.setValue("SimpleConditionsNode:1_Condition_value", "d");
		
		setActionForm(simpleForm) ;

		actionPerform();

		DistributionProtocol distributionProtocol = (DistributionProtocol) TestCaseUtility.getNameObjectMap("DistributionProtocol");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<DistributionProtocol> distributionProtocolList = null;
		try 
		{
			distributionProtocolList = bizLogic.retrieve("DistributionProtocol");
		}
		catch (BizLogicException e) 
		{
			e.printStackTrace();
			System.out.println("DistributionProtocolTestCases.testDistributionProtocolEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		if(distributionProtocolList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfDistributionProtocol&operation=search&id=" + distributionProtocol.getId());
			verifyNoActionErrors();
		}
		else if(distributionProtocolList.size() > 1)
		{
		    verifyForward("success");
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
	 * Test Distribution Protocol Edit.
	 */
	@Test
	public void testDistributionProtocolEdit()
	{
//		//TODO 
//		fail("Need to write test case");
		DistributionProtocol protocol = (DistributionProtocol)
				TestCaseUtility.getNameObjectMap("DistributionProtocol") ;
		
		setRequestPathInfo("/DistributionProtocolSearch");
		addRequestParameter("id", "" + protocol.getId());
		addRequestParameter("pageOf", "pageOfDistributionProtocol");
			
		actionPerform();
		verifyForward("pageOfDistributionProtocol");	
		//verifyNoActionErrors();
		setRequestPathInfo(getActualForward());
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfDistributionProtocol");
		addRequestParameter("menuSelected", "10");
		actionPerform();
		verifyNoActionErrors();

		DistributionProtocolForm form = (DistributionProtocolForm) getActionForm();
	
		form.setTitle("ShriDP_" +UniqueKeyGeneratorUtil.getUniqueKey()) ;
		form.setShortTitle("ShriDP_" +UniqueKeyGeneratorUtil.getUniqueKey());
		form.setOperation("edit");
		setRequestPathInfo("/DistributionProtocolEdit");
		actionPerform() ;
		verifyForward("success") ;
		//verifyNoActionErrors();
		verifyActionMessages(new String[] {"object.edit.successOnly"});
	
		protocol.setTitle(form.getTitle());
		protocol.setShortTitle(form.getShortTitle());
		TestCaseUtility.setNameObjectMap("DistributionProtocol",protocol);
	}
	
	/**
	 * Test Distribution Protocol Add with Empty Title.
	 * Negative Test Case.
	 */
	@Test
	public void testDistributionProtocolAddWithEmptyTitle()
	{
//		//TODO
//		fail("Need to write test case");

		DistributionProtocolForm distProtocolForm = new DistributionProtocolForm();
		distProtocolForm.setTitle("");
		distProtocolForm.setShortTitle("dp_"+UniqueKeyGeneratorUtil.getUniqueKey());
		distProtocolForm.setPrincipalInvestigatorId(1L);
		distProtocolForm.setStartDate("01-12-2009");
		distProtocolForm.setOperation("add") ;
		setRequestPathInfo("/DistributionProtocolAdd");
		setActionForm(distProtocolForm);
		actionPerform();
		verifyForward("failure");	
		verifyActionErrors(new String[]{"errors.item.required"});
	}
	/**
	 * Test Distribution Protocol Add with empty short title.
	 * Negative Test Case.
	 */
	@Test
	public void testDistributionProtocolAddWithEmptyShortTitle()
	{
//		//TODO
//		fail("Need to write test case");
		DistributionProtocolForm distProtocolForm = new DistributionProtocolForm();
		distProtocolForm.setTitle("dp_"+UniqueKeyGeneratorUtil.getUniqueKey());
		distProtocolForm.setShortTitle("");
		distProtocolForm.setPrincipalInvestigatorId(1L);
		distProtocolForm.setStartDate("01-12-2009");
		distProtocolForm.setOperation("add") ;
		setRequestPathInfo("/DistributionProtocolAdd");
		setActionForm(distProtocolForm);
		actionPerform();
		verifyForward("failure");	
		verifyActionErrors(new String[]{"errors.item.required"});
	}
	/**
	 * Test Distribution Protocol Add with invalid PI.
	 * Negative Test Case.
	 */
	@Test
	public void testDistributionProtocolAddWithInvalidPI()
	{
//		//TODO
//		fail("Need to write test case");
		DistributionProtocolForm distProtocolForm = new DistributionProtocolForm();
		distProtocolForm.setTitle("dp_"+UniqueKeyGeneratorUtil.getUniqueKey());
		distProtocolForm.setShortTitle("dp_"+UniqueKeyGeneratorUtil.getUniqueKey());
		distProtocolForm.setPrincipalInvestigatorId(-1L);
		distProtocolForm.setStartDate("01-12-2009");
		distProtocolForm.setOperation("add") ;
		setRequestPathInfo("/DistributionProtocolAdd");
		setActionForm(distProtocolForm);
		actionPerform();
		verifyForward("failure");	
		verifyActionErrors(new String[]{"errors.item.selected"});
	}
	/**
	 * Test Distribution Protocol Add with Invalid Specimen Class.
	 * Negative Test Case.
	 */
	@Test
	public void testDistributionProtocolBizLogicAddWithInvalidSpecimenClass()
	{
//		//TODO
//		fail("Need to write test case");
		
		DistributionProtocol protocol = new DistributionProtocol();
		protocol.setTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setShortTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setStartDate(new Date()) ;
		User user = new User();
		user.setId(1L);
		protocol.setPrincipalInvestigator(user) ;
		protocol.setActivityStatus("Active");
		
		DistributionSpecimenRequirement requirement = new DistributionSpecimenRequirement();
		requirement.setSpecimenClass("InValid") ;
		requirement.setSpecimenType("Fixed Tissue") ;
		requirement.setTissueSite("Clitoris") ;
		requirement.setPathologyStatus("Metastatic");
		requirement.setQuantity(3D);
		requirement.setDistributionProtocol(protocol);
		protocol.getDistributionSpecimenRequirementCollection().add(requirement) ;
		
		DistributionProtocolBizLogic bizLogic = new DistributionProtocolBizLogic();
		try
		{
			bizLogic.insert(protocol,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("DP Object SPECIMEN CLASS Is INVALID while inserting through BizLogic",true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in DPTestCase :" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Test Distribution Protocol Add with Invalid Specimen Type.
	 * Negative Test Case.
	 */
	@Test
	public void testDistributionProtocolBizLogicAddWithInvalidSpecimenType()
	{
//		//TODO
//		fail("Need to write test case");
		
		DistributionProtocol protocol = new DistributionProtocol();
		protocol.setTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setShortTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setStartDate(new Date()) ;
		User user = new User();
		user.setId(1L);
		protocol.setPrincipalInvestigator(user) ;
		protocol.setActivityStatus("Active");
		
		DistributionSpecimenRequirement requirement = new DistributionSpecimenRequirement();
		requirement.setSpecimenClass("Tissue") ;
		requirement.setSpecimenType("InValid Tissue") ;
		requirement.setTissueSite("Clitoris") ;
		requirement.setPathologyStatus("Metastatic");
		requirement.setQuantity(3D);
		requirement.setDistributionProtocol(protocol);
		protocol.getDistributionSpecimenRequirementCollection().add(requirement) ;
		
		DistributionProtocolBizLogic bizLogic = new DistributionProtocolBizLogic();
		try
		{
			bizLogic.insert(protocol,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("DP Object SPECIMEN TYPE Is INVALID while inserting through BizLogic",true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in DPTestCase :" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Test Distribution Protocol Add with Invalid Tissue Site.
	 * Negative Test Case.
	 */
	@Test
	public void testDistributionProtocolBizLogicAddWithInvalidTissueSite()
	{
//		//TODO
//		fail("Need to write test case");
		DistributionProtocol protocol = new DistributionProtocol();
		protocol.setTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setShortTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setStartDate(new Date()) ;
		User user = new User();
		user.setId(1L);
		protocol.setPrincipalInvestigator(user) ;
		protocol.setActivityStatus("Active");
		
		DistributionSpecimenRequirement requirement = new DistributionSpecimenRequirement();
		requirement.setSpecimenClass("Tissue") ;
		requirement.setSpecimenType("Fixed Tissue") ;
		requirement.setTissueSite("InValidTissueSite") ;
		requirement.setPathologyStatus("Metastatic");
		requirement.setQuantity(3D);
		
		requirement.setDistributionProtocol(protocol);
		protocol.getDistributionSpecimenRequirementCollection().add(requirement) ;
		
		DistributionProtocolBizLogic bizLogic = new DistributionProtocolBizLogic();
		try
		{
			bizLogic.insert(protocol,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("DP Object TISSUE SITE Is INVALID while inserting through BizLogic",true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in DPTestCase :" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 *  Test Distribution Protocol Add with Invalid Pathological status.
	 * Negative Test Case.
	 */
	@Test
	public void testDistributionProtocolBizLogicAddWithInvalidPathStatus()
	{
//		//TODO
//		fail("Need to write test case");
		DistributionProtocol protocol = new DistributionProtocol();
		protocol.setTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setShortTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setStartDate(new Date()) ;
		User user = new User();
		user.setId(1L);
		protocol.setPrincipalInvestigator(user) ;
		protocol.setActivityStatus("Active");
		
		DistributionSpecimenRequirement requirement = new DistributionSpecimenRequirement();
		requirement.setSpecimenClass("Tissue") ;
		requirement.setSpecimenType("Fixed Tissue") ;
		requirement.setTissueSite("Clitoris") ;
		requirement.setPathologyStatus("InValidPathStatus");
		requirement.setQuantity(3D);
		requirement.setDistributionProtocol(protocol);
		protocol.getDistributionSpecimenRequirementCollection().add(requirement) ;
		
		DistributionProtocolBizLogic bizLogic = new DistributionProtocolBizLogic();
		try
		{
			bizLogic.insert(protocol,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("DP Object PATHOLOGICAL STATUS CLASS Is " +
					"INVALID while inserting through BizLogic",true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in DPTestCase :" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Test Distribution Protocol Add with Invalid quantity.
	 * Negative Test Case.
	 * Quantity is not validated in validate method of DP BizLogic
	 * Thats why, this test case is getting failed.
	 */
	@Test
	public void testDistributionProtocolBizLogicAddWithInvalidQuantity()
	{
//		//TODO
//		fail("Need to write test case");
		DistributionProtocol protocol = new DistributionProtocol();
		protocol.setTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setShortTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setStartDate(new Date()) ;
		User newUser = new User();
		newUser.setId(2L);
		protocol.setPrincipalInvestigator(newUser) ;
		protocol.setActivityStatus("Active");
		
		DistributionSpecimenRequirement requirement = new DistributionSpecimenRequirement();
		requirement.setSpecimenClass("Tissue") ;
		requirement.setSpecimenType(Constants.FIXED_TISSUE_SLIDE) ;
		requirement.setTissueSite("Clitoris") ;
		requirement.setPathologyStatus("Metastatic");
		requirement.setQuantity(-3D);
	
		requirement.setDistributionProtocol(protocol);
		protocol.getDistributionSpecimenRequirementCollection().add(requirement) ;
		
		DistributionProtocolBizLogic bizLogic = new DistributionProtocolBizLogic();
		try
		{
			bizLogic.insert(protocol,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("DP Object QUANTITY Is " +
					"INVALID while inserting through BizLogic",true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in DPTestCase :" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Test Distribution Protocol Add with Invalid options
	 * Negative Test Case. 
	 */
	@Test
	public void testDistributionProtocolWithInvalidOptionSelected()
	{
		DistributionProtocolForm distProtocolForm = new DistributionProtocolForm();
		distProtocolForm.setTitle("dp_"+UniqueKeyGeneratorUtil.getUniqueKey());
		distProtocolForm.setShortTitle("dp_"+UniqueKeyGeneratorUtil.getUniqueKey());
		distProtocolForm.setPrincipalInvestigatorId(1L);
		distProtocolForm.setStartDate("01-12-2009");
		distProtocolForm.setOperation("add") ;
		distProtocolForm.setMutable(true);
		distProtocolForm.setValue("specimenClass", "-1") ;
		distProtocolForm.setValue("specimenType", "") ;
		distProtocolForm.setValue("tissueSite", "-1") ;
		distProtocolForm.setValue("pathologyStatus", "") ;
		
		setRequestPathInfo("/DistributionProtocolAdd");
		setActionForm(distProtocolForm);
		actionPerform();
		verifyForward("failure");	
		verifyActionErrors(new String[] {"errors.item.selected","errors.item.selected"
				,"errors.item.selected","errors.item.selected"});
	}
	/**
	 * Test Distribution Protocol Add with Invalid NULL DP object.
	 * Negative Test Case.
	 * This test case generating an NULL Pointer Exception.
	 */
	@Test
	public void testDistributionProtocolBizLogicAddWithNullObject()
	{
		//TODO
		fail("Need to write test case");
//		DistributionProtocolBizLogic bizLogic = new DistributionProtocolBizLogic();
//		try
//		{
//			bizLogic.insert(null,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
//			assertFalse("DP Object Is NULL while inserting through BizLogic",true);
//		}
//		catch (BizLogicException e)
//		{
//			logger.error("Exception in DPTestCase :" + e.getMessage());
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	@Test
	/**
	 * Test Distribution Protocol Add with NULL PI.
	 * Negative Test Case.
	 */
	public void testDistributionProtoclBizLogicAddWithNullPI()
	{
//		//TODO
//		fail("Need to write test case");
		DistributionProtocol protocol = new DistributionProtocol();
		protocol.setTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setShortTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setStartDate(new Date()) ;
		protocol.setPrincipalInvestigator(null) ;
		protocol.setActivityStatus("Active");
		DistributionProtocolBizLogic bizLogic = new DistributionProtocolBizLogic();
		try
		{
			bizLogic.insert(protocol,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("DP Object PI Is NULL while inserting through BizLogic",true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in DPTestCase :" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Test Distribution Protocol Add with NULL DP title.
	 * Negative Test Case.
	 */
	@Test
	public void testDistributionProtoclBizLogicAddWithNullTitle()
	{
//		//TODO
//		fail("Need to write test case");
		DistributionProtocol protocol = new DistributionProtocol();
		protocol.setTitle(null);
		protocol.setShortTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setStartDate(new Date()) ;
		User user = new User() ;
		user.setId(1L) ;
		protocol.setPrincipalInvestigator(user) ;
		protocol.setActivityStatus("Active");
		DistributionProtocolBizLogic bizLogic = new DistributionProtocolBizLogic();
		try
		{
			bizLogic.insert(protocol,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("DP Object TITLE Is NULL while inserting through BizLogic",true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in DPTestCase :" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Test Distribution Protocol Add with NULL DP short title.
	 * Negative Test Case.
	 */
	@Test
	public void testDistributionProtoclBizLogicAddWithNullShortTitle()
	{
//		//TODO
//		fail("Need to write test case");
		DistributionProtocol protocol = new DistributionProtocol();
		protocol.setTitle("DP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		protocol.setShortTitle(null);
		protocol.setStartDate(new Date()) ;
		User user = new User() ;
		user.setId(1L) ;
		protocol.setPrincipalInvestigator(user) ;
		protocol.setActivityStatus("Active");
		DistributionProtocolBizLogic bizLogic = new DistributionProtocolBizLogic();
		try
		{
			bizLogic.insert(protocol,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("DP Object SHORT-TITLE Is NULL while inserting through BizLogic",true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in DPTestCase :" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Test Distribution Protocol update with status disabled.
	 * Negative Test Case.
	 */
	@Test
	public void testDistributionProtoclBizLogicUpdateWithStatusDisabled()
	{
		DistributionProtocol protocolCurrent =(DistributionProtocol) 
								TestCaseUtility.getNameObjectMap("DistributionProtocol") ;
	
		DistributionProtocol protocolOld =(DistributionProtocol) 
			TestCaseUtility.getNameObjectMap("DistributionProtocol") ;
		
		protocolCurrent.setActivityStatus("Disabled") ;
		DistributionProtocolBizLogic bizLogic = new DistributionProtocolBizLogic();
		try
		{
			bizLogic.update(protocolCurrent,protocolOld,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in DPTestCase :" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
