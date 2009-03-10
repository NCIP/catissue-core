package edu.wustl.catissuecore.testcase;

import java.util.List;
import org.junit.Test;
import edu.wustl.catissuecore.actionForm.SiteForm;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class contains test cases for Site add/edit
 * @author Himanshu Aseeja
 * @author Janhavi Hasabnis
 */
public class SiteTestCases extends CaTissueSuiteBaseTest
{
	/**
	 * Test Site Add.
	 */
	@Test
	public void testRepositorySiteAdd()
	{
		RequestParameterUtility.setAddSiteParams(this);
		addRequestParameter("type","Repository");
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
	
	public void testCollectionSiteAdd()
	{
		RequestParameterUtility.setAddSiteParams(this);
		addRequestParameter("type","Collection Site");	
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

	public void testLaboratorySiteAdd()
	{
		RequestParameterUtility.setAddSiteParams(this);
		addRequestParameter("type","Laboratory");		
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
	public void testNotSpecifiedSiteAdd()
	{
		RequestParameterUtility.setAddSiteParams(this);
		addRequestParameter("type","Not Specified"); 
		
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
	public void testSiteAddWithoutMandatoryParams()
	{
		addRequestParameter("name","");
		addRequestParameter("type","");
		addRequestParameter("street","");
		addRequestParameter("state","");
		addRequestParameter("city","");
		addRequestParameter("coordinatorId","");
		addRequestParameter("operation", "add");
		setRequestPathInfo("/SiteAdd");
		actionPerform();
		verifyForward("failure");
		String errorNames[] = new String[]{"errors.item.required","errors.item.required","errors.item.required"
				,"errors.item.required","errors.item.required","errors.item.required"};
		verifyActionErrors(errorNames);	
	}
	
	public void testSiteAddWithSameName()
	{
		Site site = (Site) TestCaseUtility.getNameObjectMap("Site");
		RequestParameterUtility.setAddSiteParams(this);
		addRequestParameter("name",site.getName());
		addRequestParameter("type","Laboratory");
		setRequestPathInfo("/SiteAdd");
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[] {"errors.item"};
		verifyActionErrors(errormsg);
	}
	
	/**
	 * Test Site Edit.
	 */
	//@Test
	public void testSiteEdit()
	{
		setRequestPathInfo("/SimpleSearch");
		RequestParameterUtility.setEditSiteParams(this);
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
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
    		verifyActionErrors(errorNames);
		}
		
		//Site action.Generates SiteForm
		setRequestPathInfo("/SiteSearch");
		addRequestParameter("id", "" + site.getId());
		actionPerform();
		verifyForward("pageOfSite");
        verifyNoActionErrors();
		
		//edit function
		site.setName("Site1_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name",site.getName());
		setRequestPathInfo("/SiteEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
		TestCaseUtility.setNameObjectMap("Site",site);
	}
	
	/**
	 * Test Site Edit.
	 */
	//@Test
	public void testSiteEditEmptySiteParams()
	{
		setRequestPathInfo("/SimpleSearch");
		RequestParameterUtility.setEditSiteParams(this);
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
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
    		verifyActionErrors(errorNames);
		}
		
		//Site action.Generates SiteForm
		setRequestPathInfo("/SiteSearch");
		addRequestParameter("id", "" + site.getId());
		actionPerform();
		verifyForward("pageOfSite");
        verifyNoActionErrors();
		
		//edit function
        addRequestParameter("name","");
        addRequestParameter("type","");
		addRequestParameter("street","");
		addRequestParameter("state","");
		addRequestParameter("country","");
		addRequestParameter("city","");
		addRequestParameter("activityStatus","");
		setRequestPathInfo("/SiteEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("failure");
		String errorNames[] = new String[]{"errors.item.required","errors.item.required","errors.item.required"
				,"errors.item.required","errors.item.required","errors.item.required","errors.item.required"};
		verifyActionErrors(errorNames);	
	}

}
