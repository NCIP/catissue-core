package edu.wustl.catissuecore.testcase.admin;

import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.SiteForm;
import edu.wustl.catissuecore.bizlogic.SiteBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.RequestParameterUtility;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.LikeClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;

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
	public void testAddRepositorySite()
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
		
		//add site object to objectMap
		TestCaseUtility.setNameObjectMap("Site",site);
	}
    /**
	 * Test Site Add Collection site.
	 */
	@Test
	public void testAddCollectionSite()
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
		TestCaseUtility.setNameObjectMap("CollectionSite",site);
	}
	/**
	 * Test Site Add Laboratory site.
	 */
	@Test
	public void testAddLaboratorySite()
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
		
		//add site object to objectMap
		TestCaseUtility.setNameObjectMap("LaboratorySite",site);
	}
	/**
	 *  Test Site Add Not Specified site.
	 */
	@Test
	public void testAddNotSpecifiedSite()
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
		
		//add site object to objectMap
		TestCaseUtility.setNameObjectMap("NotSpecifiedSite",site);
	}
	/**
	 * Test Site Add without specifying mandatory Parameters.
	 * Negative test case.
	 */
	@Test
	public void testSiteAddWithoutMandatoryParams()
	{
		SiteForm siteForm = new SiteForm();
		siteForm.setName("");
		siteForm.setType("");
		siteForm.setStreet("");
		siteForm.setCity("") ;
		siteForm.setState("");
		siteForm.setCoordinatorId(0);
		siteForm.setOperation("add");
		
		setRequestPathInfo("/SiteAdd");
		setActionForm(siteForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errorNames[] = new String[]{"errors.item.required",
				"errors.item.required","errors.item.required"
				,"errors.item.required","errors.item.required","errors.item.required"};
		verifyActionErrors(errorNames);	
	}
	/**
	 * Test Site Add with duplicate name.
	 * Negative test case.
	 */
	@Test
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
	 * Test Site Add with empty site name. 
	 * Negative Test Case.
	 */
	@Test
	public void testSiteAddWithEmptyName()
	{
		RequestParameterUtility.setAddSiteParams(this);
		addRequestParameter("name", "") ;
		addRequestParameter("type","Laboratory");
		setRequestPathInfo("/SiteAdd");
		actionPerform() ;
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[] {"errors.item.required"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Site Add with empty site type.
	 * Negative Test Case.
	 */
	@Test
	public void testSiteAddWithEmptyType()
	{
		RequestParameterUtility.setAddSiteParams(this);
		addRequestParameter("type","");
		setRequestPathInfo("/SiteAdd");
		actionPerform() ;
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[] {"errors.item.required"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Site Add with empty site coordinator.
	 * Negative Test Case.
	 */
	@Test
	public void testSiteAddWithEmptyCoordinator()
	{
		RequestParameterUtility.setAddSiteParams(this);
		addRequestParameter("coordinatorId","");
		setRequestPathInfo("/SiteAdd");
		actionPerform() ;
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[] {"errors.item.required"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Site Add with empty Email address.
	 * Negative Test Case.
	 */
	@Test
	public void testSiteAddWithEmptyEmailAddress()
	{
		RequestParameterUtility.setAddSiteParams(this);
		addRequestParameter("emailAddress","");
		setRequestPathInfo("/SiteAdd");
		actionPerform() ;
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[] {"errors.item.required"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Site Add with invalid email and coordinator.
	 * Negative Test Case.
	 */
	@Test
	public void testSiteAddWithInvalidEmailAndCoordinator()
	{
		RequestParameterUtility.setAddSiteParams(this);
		addRequestParameter("emailAddress","@admin@admin.com");
		addRequestParameter("coordinatorId","-1");
		addRequestParameter("type","Laboratory");
		setRequestPathInfo("/SiteAdd");
		actionPerform() ;
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[] {"errors.item.format","errors.item.required"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Site Add with NULL object.
	 * Negative Test Case.
	 * This test case is generating a.
	 * NULL Pointer Exception.
	 */
	@Test
	public void testSiteBizLogicAddWithNullObject()
	{
		SiteBizLogic siteBizLogic = new SiteBizLogic() ;
		try
		{
			siteBizLogic.insert(null, CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("Site Object Is NULL while inserting through SiteBizLogic", true);
		}
		catch (BizLogicException e)
		{
			logger.info("Site Object Is NULL: " + e.getMessage()) ;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Test Site Add with NULL name.
	 * Negative Test Case.
	 */
	@Test
	public void testSiteBizLogicAddWithNullName()
	{
		User coordinator = new User() ;
		UserBizLogic bizLogic = new UserBizLogic();
		try
		{
			coordinator = (User) bizLogic.retrieve("edu.wustl.catissuecore.domain.User", Long.valueOf(1)) ;
		}
		catch (BizLogicException e1)
		{
			logger.error("Exception in SiteTestCase :" + e1.getMessage());
			e1.printStackTrace();
		}
		
		Site site = new Site() ;
		site.setName(null) ;
		site.setType("Laboratory");
		site.setEmailAddress("admin@admin.com");
		site.setCoordinator(coordinator);
		site.setAddress(coordinator.getAddress()) ;
		site.setActivityStatus(coordinator.getActivityStatus());
		
		SiteBizLogic siteBizLogic = new SiteBizLogic() ;
		try
		{
			siteBizLogic.insert(site, CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("Site Object Name Is NULL while inserting through SiteBizLogic", true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in SiteTestCase :" + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Test Site Add with NULL site type.
	 * Negative Test Case.
	 */
	@Test
	public void testSiteBizLogicAddWithNullType()
	{
		User coordinator = new User() ;
		User user1 = new User();
		UserBizLogic bizLogic = new UserBizLogic();
		try
		{
			coordinator = (User) bizLogic.retrieve("edu.wustl.catissuecore.domain.User",
					Long.valueOf(1)) ;
		}
		catch (BizLogicException e1)
		{
			e1.printStackTrace();
		}
		
		Site site = new Site() ;
		site.setName("ShriSite_" + UniqueKeyGeneratorUtil.getUniqueKey()) ;
		site.setType(null);
		site.setEmailAddress("admin@admin.com");
		site.setCoordinator(coordinator);
		site.setAddress(coordinator.getAddress()) ;
		site.setActivityStatus(coordinator.getActivityStatus());
		
		SiteBizLogic siteBizLogic = new SiteBizLogic() ;
		try
		{
			siteBizLogic.insert(site, CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("Site Object Type Is NULL while inserting through SiteBizLogic", true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in SiteTestCase :" + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Test Site Add with NULL site Coordinator.
	 * Negative Test Case.
	 */
	@Test
	public void testSiteBizLogicAddWithNullCoordinator()
	{
		Address address = new Address() ;
		address.setStreet("Sinhgad Road");
		address.setCity("Pune") ;
		address.setState("Maharashtra");
		address.setCountry("India") ;
		address.setPhoneNumber("1234") ;
		address.setId(Long.valueOf(1)) ;
		
		Site site = new Site() ;
		site.setName("ShriSite_" + UniqueKeyGeneratorUtil.getUniqueKey()) ;
		site.setType("Laboratory");
		site.setEmailAddress("admin@admin.com");
		site.setCoordinator(null);
		site.setAddress(address) ;
		site.setActivityStatus("Active");
		
		SiteBizLogic siteBizLogic = new SiteBizLogic() ;
		try
		{
			siteBizLogic.insert(site, CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("Site Object Coordinator Is NULL while inserting through SiteBizLogic", true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in SiteTestCase :" + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Test Site Add with NULL site Address.
	 * Negative Test Case.
	 */
	@Test
	public void testSiteBizLogicAddWithNullAddress()
	{
		Site site = new Site() ;
		User coordinator = new User() ;
		UserBizLogic bizLogic = new UserBizLogic();
		SiteBizLogic siteBizLogic = new SiteBizLogic() ;
		try
		{
			coordinator = (User) bizLogic.retrieve("edu.wustl.catissuecore.domain.User", Long.valueOf(3)) ;
		}
		catch (BizLogicException e1)
		{
			e1.printStackTrace();
		}
		site.setName("ShriSite_" + UniqueKeyGeneratorUtil.getUniqueKey()) ;
		site.setType("Laboratory");
		site.setEmailAddress("admin@admin.com");
		site.setCoordinator(coordinator);
		site.setAddress(null) ;
		site.setActivityStatus("Active");
		
		try
		{
			siteBizLogic.insert(site,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("Site Object Address Is NULL while inserting through SiteBizLogic", true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in SiteTestCase :" + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Test Site Add with NULL email Address.
	 * Negative Test Case.
	 */
	@Test
	public void testSiteBizLogicAddWithNullEmailAddress()
	{
		User coordinator = new User() ;
		UserBizLogic bizLogic = new UserBizLogic();
		try
		{
			coordinator = (User) bizLogic.retrieve("edu.wustl.catissuecore.domain.User",
					Long.valueOf(1)) ;
		}
		catch (BizLogicException e1)
		{
			e1.printStackTrace();
		}
		
		Site site = new Site() ;
		site.setName("ShriSite_" + UniqueKeyGeneratorUtil.getUniqueKey()) ;
		site.setType("Laboratory");
		site.setEmailAddress(null);
		site.setCoordinator(coordinator);
		site.setAddress(coordinator.getAddress()) ;
		site.setActivityStatus(coordinator.getActivityStatus());
		
		SiteBizLogic siteBizLogic = new SiteBizLogic() ;
		try
		{
			siteBizLogic.insert(site, CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("Site Object Email-Address Is NULL while inserting through SiteBizLogic", true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in SiteTestCase :" + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Test Site Search.
	 */
	@Test
	public void testSiteSearch()
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
		catch (BizLogicException e) 
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
	@Test
	public void testSiteEdit()
	{
		Site site = (Site)TestCaseUtility.getNameObjectMap("LaboratorySite") ;
		setRequestPathInfo("/SiteSearch") ;
		addRequestParameter("id", ""+site.getId()) ;
		addRequestParameter("pageOf", "pageOfSite");
		
		actionPerform();
		verifyForward("pageOfSite");
		setRequestPathInfo(getActualForward());
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfSite");
		addRequestParameter("menuSelected", "5");
		actionPerform();
		verifyNoActionErrors();

		SiteForm siteForm = (SiteForm) getActionForm() ;
		
		siteForm.setName("LaboratorySite_" + UniqueKeyGeneratorUtil.getUniqueKey());
		siteForm.setOperation("edit");
		setRequestPathInfo("/SiteEdit");
		setActionForm(siteForm);
		actionPerform();
		
		verifyForward("success");

		verifyActionMessages(new String[]{"object.edit.successOnly"});
		
		site.setName(siteForm.getName()) ;
		TestCaseUtility.setNameObjectMap("LaboratorySite",site) ;
	}
	/**
	 * Test Site Edit without specifying mandatory Parameters.
	 * Negative test case.
	 */
	@Test
	public void testSiteEditEmptySiteParams()
	{
		Site site = (Site)TestCaseUtility.getNameObjectMap("LaboratorySite") ;
		setRequestPathInfo("/SiteSearch") ;
		addRequestParameter("id", ""+site.getId()) ;
		addRequestParameter("pageOf", "pageOfSite");
		
		actionPerform();
		verifyForward("pageOfSite");
		
		SiteForm siteForm = (SiteForm) getActionForm() ;
		
		siteForm.setName("");
		siteForm.setType("") ;
		siteForm.setCity("");
		siteForm.setCoordinatorId(Long.valueOf(0)) ;
		siteForm.setCountry("") ;
		siteForm.setPhoneNumber("") ;
		siteForm.setActivityStatus("");
		siteForm.setOperation("edit");
		setRequestPathInfo("/SiteEdit");
		setActionForm(siteForm);
		actionPerform();
		
		verifyForward("failure");
		verifyActionErrors(new String[]{"errors.item.required","errors.item.required","errors.item.required",
										"errors.item.required","errors.item.required"});
		
		assertTrue("Cannot Edit Site Object with Empty Parameters",true);
	}

	/**
	 * Test Site Edit with Closed Status.
	 * Negative Test Case.
	 */
	@Test
	public void testSiteWithCloseStatus()
	{
		User coordinator = new User() ;
		UserBizLogic bizLogic = new UserBizLogic();
		try
		{
			coordinator = (User) bizLogic.retrieve("edu.wustl.catissuecore.domain.User", Long.valueOf(1)) ;
		}
		catch (BizLogicException e1)
		{
			e1.printStackTrace();
		}
		
		Site site = new Site() ;
		site.setName("Site_TO_Close"+UniqueKeyGeneratorUtil.getUniqueKey()) ;
		site.setType("Laboratory");
		site.setEmailAddress("admin@admin.com");
		site.setCoordinator(coordinator);
		site.setAddress(coordinator.getAddress()) ;
		site.setActivityStatus("Active");
		
		site.setActivityStatus("Active");
		site.setCoordinator(coordinator);
		site.setAddress(coordinator.getAddress()) ;
		site.getAddress().setState("Alaska");
		site.getAddress().setCity("Sri Ganga Nagar");
		site.getAddress().setStreet("xyz");
		site.getAddress().setCountry("India");
		site.getAddress().setZipCode("111111");
		site.getAddress().setPhoneNumber("9011083118");		
		SiteBizLogic siteBizLogic = new SiteBizLogic() ;
		try
		{
			siteBizLogic.insert(site) ;
			site.setActivityStatus("Closed");
			siteBizLogic.update(site) ;
			assertTrue("Site Object Status Closed successfully", true);
		}
		catch (BizLogicException e)
		{
			logger.error("Exception in SiteTestCase :" + e.getMessage());
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
