package edu.wustl.catissuecore.testcase;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.SiteForm;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.Address;

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
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","site");
		addRequestParameter("counter","1");
		addRequestParameter("pageOf","pageOfSite");
		addRequestParameter("operation","search");
		actionPerform();
		verifyForward("success");

		/*Site action.Generates SiteForm*/
		Site site = (Site) TestCaseUtility.getNameObjectMap("Site");
		setRequestPathInfo("/SiteSearch");
		addRequestParameter("id", "" + site.getId());
		actionPerform();
		verifyForward("pageOfSite");

		
		/*edit function*/
		site.setName("Site1_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name",site.getName());
		setRequestPathInfo("/SiteEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		TestCaseUtility.setNameObjectMap("Site",site);
	}
}
