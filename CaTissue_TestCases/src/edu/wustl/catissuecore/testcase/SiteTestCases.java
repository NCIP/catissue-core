package edu.wustl.catissuecore.testcase;

import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.SiteForm;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class contains test cases for Site add/edit
 * @author Himanshu Aseeja
 */
public class SiteTestCases extends CaTissueSuiteBaseTest
{
	/**
	 * Test Site Add.
	 */
	@Test
	public void testSiteAdd()
	{
		addRequestParameter("name","Site_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("type","Repository");
		addRequestParameter("emailAddress","himanshu_aseeja@persistent.co.in");
		addRequestParameter("coordinatorId","2");
		addRequestParameter("street", "xyz");
		addRequestParameter("state","Alaska");
		addRequestParameter("country","India");
		addRequestParameter("zipCode","335001");
		addRequestParameter("phoneNumber","9011083118");
		addRequestParameter("city","Sri Ganga Nagar");
		addRequestParameter("operation","add");
		setRequestPathInfo("/SiteAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
				
		SiteForm form=(SiteForm) getActionForm();
		
		User coordinator=new User();
		coordinator.setId(form.getId());
		
		Site site = new Site();
		
		site.setId(form.getId());
		site.setName(form.getName());
		site.setType(form.getType());
		site.setEmailAddress(form.getEmailAddress());
		site.setCoordinator(coordinator);
		Address address=new Address();
		
		address.setStreet(form.getStreet());
		address.setCity(form.getCity());
		address.setState(form.getState());
		address.setCountry(form.getCountry());
		address.setZipCode(form.getZipCode());
		address.setPhoneNumber(form.getPhoneNumber());
		
		site.setAddress(address);
		
		TestCaseUtility.setNameObjectMap("Site",site);
	}
	/**
	 * Test Site Edit.
	 */
	@Test
	public void testSiteEdit()
	{
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "Site");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "Site");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","Site.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","");
		addRequestParameter("counter","1");
		addRequestParameter("pageOf","pageOfSite");
		addRequestParameter("operation","search");
		actionPerform();
				
		Site site = (Site) TestCaseUtility.getNameObjectMap("Site");
		DefaultBizLogic bizLogic = new DefaultBizLogic(); 
		List<Site> siteList = null;
		try 
		{
			siteList = bizLogic.retrieve("Site");
		}
		catch (DAOException e) 
		{
			e.printStackTrace();
			System.out.println("SiteTestCases.testSiteEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		if(siteList.size() > 1)
		{
		    verifyForward("success");
		    verifyNoActionErrors();
		}
		else if(siteList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfSite&operation=search&id=" + site.getId());
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
		
		/*Site action.Generates SiteForm*/
		setRequestPathInfo("/SiteSearch");
		addRequestParameter("id", "" + site.getId());
		actionPerform();
		verifyForward("pageOfSite");
        verifyNoActionErrors();
		
		/*edit function*/
		site.setName("Site1_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name",site.getName());
		setRequestPathInfo("/SiteEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
		TestCaseUtility.setNameObjectMap("Site",site);
	}
}
