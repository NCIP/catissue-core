package edu.wustl.catissuecore.testcase.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.bizlogic.BaseTestCaseUtility;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;


public class SupervisorTestCases extends CaTissueSuiteBaseTest
{
	public void testSupervisorAddForCP()
	{
		try
		 {
			User user = BaseTestCaseUtility.initUser();
			user.setRoleId( "2" );
			user.setEmailAddress("Supervisor_"+ UniqueKeyGeneratorUtil.getUniqueKey()+"@Supervisor.com");
			user.setLoginName(user.getEmailAddress());
			user.setPageOf(Constants.PAGE_OF_USER_ADMIN);
			Site site = (Site)TestCaseUtility.getNameObjectMap("Site");
			Collection<Site> siteCollection = new HashSet<Site>();
			siteCollection.add(site);
			user.setSiteCollection(siteCollection);
			user = (User)appService.createObject(user);			
			Logger.out.info("Supervisor User added successfully");
			assertTrue("Supervisor User added successfully", true);
		    user.setNewPassword("Login123");
		   	user = (User)appService.updateObject(user);	
		   	TestCaseUtility.setNameObjectMap( "Supervisor", user );
			
		 }
		 catch(Exception e)
		 {
			 System.out.println("SupervisorTestCases.testSupervisorAddForCP()"+ e.getMessage());
			 e.printStackTrace();
			 assertFalse("Could not add a Supervisor into System", true);
		 }
		
	
		
	}
	/**
	 * Test Collection Protocol Add.
	 */
	
	public void testCollectionProtocolAddForSupervisor()
	{
		/*Collection Protocol Details*/
		CollectionProtocolForm collForm = new CollectionProtocolForm();
		collForm.setPrincipalInvestigatorId(1L) ;
		collForm.setTitle("cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
		collForm.setOperation("add") ;
		collForm.setShortTitle("cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
		collForm.setStartDate("01-12-2009");
		setRequestPathInfo("/OpenCollectionProtocol");
		setActionForm(collForm);
		actionPerform();
        verifyForward("success");
        //verifyForward("/pages/Layout.jsp");

        /*Event Details*/
        setRequestPathInfo("/DefineEvents");
        addRequestParameter("pageOf", "pageOfDefineEvents");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfDefineEvents");

        setRequestPathInfo("/SaveProtocolEvents");
		addRequestParameter("pageOf", "pageOfDefineEvents");
		addRequestParameter("studyCalendarEventPoint","20");
		addRequestParameter("collectionProtocolEventkey", "-1");
		addRequestParameter("collectionPointLabel", "ECP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("clinicalStatus","Not Specified");
		addRequestParameter("clinicalDiagnosis", "Not Specified");

		addRequestParameter("collectionEventId", "1");
		addRequestParameter("collectionEventUserId", "1");
		addRequestParameter("collectionUserName", "admin,admin");
		addRequestParameter("collectionEventSpecimenId", "0");

		addRequestParameter("receivedEventId", "1" );
		addRequestParameter("receivedEventUserId", "1");
		addRequestParameter("receivedUserName", "admin,admin");
		addRequestParameter("receivedEventSpecimenId", "admin,admin");
		addRequestParameter("pageOf", "specimenRequirement");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForwardPath("/CreateSpecimenTemplate.do?operation=add");

		setRequestPathInfo("/SaveSpecimenRequirements");
		addRequestParameter("displayName", "spreq_" + UniqueKeyGeneratorUtil.getUniqueKey());

		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		addRequestParameter("collectionUserName", "" + bean.getLastName() + "," + bean.getFirstName());

		addRequestParameter("collectionEventCollectionProcedure", "");

		addRequestParameter("collectionEventContainer", "Not Specified");

		addRequestParameter("key", "E1");
		addRequestParameter("receivedEventReceivedQuality", "Not Specified");
		addRequestParameter("collectionEventId", "1");
		addRequestParameter("collectionEventUserId", "1");
		addRequestParameter("collectionUserName", "admin,admin");
		addRequestParameter("collectionEventSpecimenId", "0");

		addRequestParameter("receivedEventId", "1" );
		addRequestParameter("receivedEventUserId", "1");
		addRequestParameter("receivedUserName", "admin,admin");
		addRequestParameter("receivedEventSpecimenId", "admin,admin");

		addRequestParameter("collectionEventCollectionProcedure", "Lavage");
		addRequestParameter("collectionEventContainer", "CPT");
		addRequestParameter("className", "Tissue");
		addRequestParameter("tissueSite", "Anal canal");
		addRequestParameter("tissueSide", "Left");
		addRequestParameter("pathologicalStatus", "Metastatic");
		addRequestParameter("storageLocationForSpecimen","Auto");
		addRequestParameter("type","Frozen Tissue");
		addRequestParameter("collectionEventComments", "");
		addRequestParameter("receivedEventReceivedQuality","Frozen");
		addRequestParameter("receivedEventComments", "");
		addRequestParameter("quantity", "10");
		addRequestParameter("quantityPerAliquot", "5");
		addRequestParameter("noOfAliquots", "2");
		addRequestParameter("storageLocationForAliquotSpecimen", "Virtual");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		setRequestPathInfo("/SubmitCollectionProtocol");
		addRequestParameter("operation", "add");
		//Added privileges to Supervisor user for this cp
		SiteUserRolePrivilegeBean privilegeBean = new SiteUserRolePrivilegeBean();
		privilegeBean.setRole( new NameValueBean("Supervisor","2") );
		List<NameValueBean> privilegesList = new ArrayList<NameValueBean>();
		privilegesList.add( new NameValueBean("Registration","18") );
		privilegesList.add( new NameValueBean("Specimen Processing","26") );
		privilegesList.add( new NameValueBean("Distribution","21") );		
		privilegeBean.setPrivileges( privilegesList );
		User user = (User)TestCaseUtility.getNameObjectMap( "Supervisor" );
		privilegeBean.setUser(user);
		Site site = (Site)TestCaseUtility.getNameObjectMap("Site");
		List<Site> siteList = new ArrayList<Site>();
		siteList.add( site );
		privilegeBean.setSiteList( siteList );
		privilegeBean.setCustChecked( true );
		Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap = new HashMap<String, SiteUserRolePrivilegeBean>();
		rowIdBeanMap.put( user.getId().toString(), privilegeBean );
		getSession().setAttribute( Constants.ROW_ID_OBJECT_BEAN_MAP, rowIdBeanMap );
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		Map innerLoopValues = (Map) getSession().getAttribute("collectionProtocolEventMap");

		Set s = innerLoopValues.keySet();
		CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) getSession().getAttribute("collectionProtocolBean");
		CollectionProtocolForm form = (CollectionProtocolForm) getActionForm();

		CollectionProtocol collectionProtocol = new CollectionProtocol();

		collectionProtocol.setCollectionProtocolEventCollection(s);
    	collectionProtocol.setId(collectionProtocolBean.getIdentifier());
		collectionProtocol.setTitle(form.getTitle());
		collectionProtocol.setShortTitle(form.getShortTitle());
		collectionProtocol.setCollectionProtocolEventCollection(s);
		collectionProtocolBean.setEnrollment( "1" );

		User principalInvestigator = new User();
		principalInvestigator.setId(form.getPrincipalInvestigatorId());
		collectionProtocol.setPrincipalInvestigator(principalInvestigator);

		Date startDate = new Date();
		String dd = new String();
		String mm = new String();
		String yyyy = new String();
		dd = form.getStartDate().substring(0,1);
		mm = form.getStartDate().substring(3,4);
		yyyy = form.getStartDate().substring(6,9);

		startDate.setDate(Integer.parseInt(dd));
		startDate.setMonth(Integer.parseInt(mm));
		startDate.setYear(Integer.parseInt(yyyy));
		collectionProtocol.setStartDate(startDate);

		TestCaseUtility.setNameObjectMap("CollectionProtocolEventMap",innerLoopValues);
		TestCaseUtility.setNameObjectMap("CollectionProtocol",collectionProtocol);
	}
}
