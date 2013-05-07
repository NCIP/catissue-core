package edu.wustl.catissuecore.testcase.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.bizlogic.BaseTestCaseUtility;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;


public class SupervisorTestCases extends CaTissueSuiteBaseTest
{
	public void testSupervisorAddForCP()
	{
		try
		 {
			User user = BaseTestCaseUtility.initUser();
			user.setRoleId( "2" );
			user.setEmailAddress("Supervisor_jan_"+ UniqueKeyGeneratorUtil.getUniqueKey()+"@Supervisor.com");
			user.setLoginName(user.getEmailAddress());
			user.setPageOf(Constants.PAGE_OF_USER_ADMIN);
			Site site = (Site)TestCaseUtility.getNameObjectMap("Site");
			Collection<Site> siteCollection = new HashSet<Site>();
			siteCollection.add(site);
			user.setSiteCollection(siteCollection);
			user = (User)appService.createObject(user);
			System.out.println("superviosr "+user.getFirstName());
			Logger.out.info("Supervisor User added successfully");
			assertTrue("Supervisor User added successfully", true);
		    user.setNewPassword("Test123");
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
		try
		{
			CollectionProtocolForm collForm = new CollectionProtocolForm();
			collForm.setPrincipalInvestigatorId(1L) ;
			collForm.setTitle("cp_janhavi_" + UniqueKeyGeneratorUtil.getUniqueKey());
			System.out.println("cp ttle "+collForm.getTitle());
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
		catch(Exception e)
		{
			System.out.println( "SupervisorTestCases.testCollectionProtocolAddForSupervisor()"+ e.getMessage() );
			e.printStackTrace();
		}
	}
	/**
	 * change Supervisor password
	 */
	public void testSupervisorUserLoginAndChangePassword()
	{
		setRequestPathInfo("/Login");
		User user = (User)TestCaseUtility.getNameObjectMap( "Supervisor" );
		addRequestParameter("loginName", user.getEmailAddress());
		addRequestParameter("password", "Test123");
		actionPerform();
		String pageOf = (String) request.getAttribute(Constants.PAGE_OF);
		SessionDataBean sessionData = null;
		if(getSession().getAttribute(Constants.TEMP_SESSION_DATA) != null)
		{
			sessionData = (SessionDataBean)getSession().getAttribute(Constants.TEMP_SESSION_DATA);
		}
		else
		{
			sessionData = (SessionDataBean)getSession().getAttribute(Constants.SESSION_DATA);
		}
		String userId = sessionData.getUserId().toString();
		setRequestPathInfo("/UpdatePassword");
		addRequestParameter("id",userId);
		addRequestParameter("operation", "");
		addRequestParameter("pageOf",pageOf);
		addRequestParameter("oldPassword", "Test123");
		addRequestParameter("newPassword", "Login123");
		addRequestParameter("confirmNewPassword", "Login123");
		actionPerform();
		verifyForward("success");
		System.out.println("----"+getActualForward());
	}
	/**
	 * Test Login with Valid Login name and Password.
	 */

	public void testSupervisorLogin()
	{
		setRequestPathInfo("/Login") ;
		User user = (User)TestCaseUtility.getNameObjectMap( "Supervisor" );
		addRequestParameter("loginName", user.getEmailAddress());
		addRequestParameter("password", "Login123");
		logger.info("start in login");
		actionPerform();
		logger.info("Login: "+getActualForward());
		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		assertEquals("user name should be equal to logged in username",user.getEmailAddress(),bean.getUserName());
		CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN=bean;
		verifyNoActionErrors();
		logger.info("end in login");
	}

	/**
	 * Test Participant Add.
	 */

	public void testParticipantAddWithSupervisorUser()
	{
		/*Participant add and registration*/
		ParticipantForm partForm = new ParticipantForm() ;
		partForm.setFirstName("participant_first_name_janhavi" + UniqueKeyGeneratorUtil.getUniqueKey()) ;
		partForm.setLastName("participant_last_name_" + UniqueKeyGeneratorUtil.getUniqueKey()) ;
		partForm.setGender("Male Gender") ;
		partForm.setVitalStatus("Alive") ;
		partForm.setGenotype("Klinefelter's Syndrome");
		partForm.setBirthDate("01-12-1985");
		partForm.setEthnicity("Hispanic or Latino");
		partForm.setRaceTypes(new String[] {"Asian"});
		partForm.setOperation("add") ;

		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getNameObjectMap("CollectionProtocol");

		Map<String,String> collProtRegVal = new LinkedHashMap<String,String>();

		collProtRegVal.put("CollectionProtocolRegistration:" +
				"1_CollectionProtocol_shortTitle",collectionProtocol.getShortTitle()) ;

		collProtRegVal.put("CollectionProtocolRegistration:" +
				"1_registrationDate", "01-01-2008") ;

		collProtRegVal.put("CollectionProtocolRegistration:" +
				"1_activityStatus", collectionProtocol.getActivityStatus()) ;

		collProtRegVal.put("CollectionProtocolRegistration:" +
				"1_isConsentAvailable", "None Defined") ;

		collProtRegVal.put("CollectionProtocolRegistration:" +
				"1_CollectionProtocol_id", ""+collectionProtocol.getId()) ;

		collProtRegVal.put("CollectionProtocolRegistration:" +
				"1_CollectionProtocol_Title", collectionProtocol.getTitle()) ;

		collProtRegVal.put("CollectionProtocolRegistration:" +
				"1_protocolParticipantIdentifier", ""+collectionProtocol.getId()) ;

		partForm.setCollectionProtocolRegistrationValues(collProtRegVal) ;

		setRequestPathInfo("/ParticipantAdd");
		setActionForm(partForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		ParticipantForm form=(ParticipantForm) getActionForm();

		Participant participant = new Participant();
		participant.setId(form.getId());
		participant.setFirstName(form.getFirstName());
		participant.setLastName(form.getLastName());

		Date birthDate = new Date();
		String dd = new String();
		String mm = new String();
		String yyyy = new String();
		dd = form.getBirthDate().substring(0,1);
		mm = form.getBirthDate().substring(3,4);
		yyyy = form.getBirthDate().substring(6,9);

		birthDate.setDate(Integer.parseInt(dd));
		birthDate.setMonth(Integer.parseInt(mm));
		birthDate.setYear(Integer.parseInt(yyyy));
		participant.setBirthDate(birthDate);

		Collection collectionProtocolRegistrationCollection = new HashSet();
		collectionProtocolRegistrationCollection.add(collectionProtocol);
		participant.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection);
		TestCaseUtility.setNameObjectMap("Participant",participant);
	}

	public void testSupervisorUserUpdateSpecimen()
	{
		Specimen specimen = null;
		Participant participant = (Participant) TestCaseUtility.getNameObjectMap("Participant");
		System.out.println("participant "+participant.getFirstName()+" id "+participant.getId());
		SpecimenCollectionGroup scg = BaseTestCaseUtility.getSCGFromParticipant(participant.getId(),appService);
		try
		{
			String query = "from edu.wustl.catissuecore.domain.Specimen as specimen where "
				+ "specimen.specimenCollectionGroup.id= "+scg.getId();
			List resultList = appService.search(query);

			if (resultList != null && resultList.size() > 0)
			{
				specimen = (Specimen) resultList.get(0);
				System.out.println("specimen "+specimen.getId());
			}
		}
		catch (Exception e)
		{
			System.out.println( "SupervisorTestCases.testSupervisorUserUpdateSpecimen()" +e.getMessage());
			e.printStackTrace();
		}
		System.out.println("testSupervisorUserUpdateSpecimen sp name "+specimen.getLabel());
		try
		{
			specimen.setCollectionStatus("Collected");
			specimen.setExternalIdentifierCollection(null);
			System.out.println(specimen + ": sp");
			specimen = (Specimen) appService.updateObject(specimen);
			System.out.println(specimen + ": sp After Update");
			assertTrue(" Domain Object is successfully added ---->    Name:: " + specimen, true);
			assertEquals(specimen.getIsAvailable().booleanValue(),true);
			TestCaseUtility.setNameObjectMap("SupervisorUserSpecimen",specimen);
		}
		catch(Exception e)
		{
			System.out.println("Exception in updating specimen : testSupervisorUserUpdateSpecimen()");
			e.printStackTrace();
		}

	}

	public void testSupervisorUserUpdateSCG()
	{
		SpecimenCollectionGroup scg = null;
		Participant participant = (Participant) TestCaseUtility.getNameObjectMap("Participant");
		System.out.println("participant "+participant.getFirstName()+" id "+participant.getId());
		scg = BaseTestCaseUtility.getSCGFromParticipant(participant.getId(),appService);
		Site site = (Site)TestCaseUtility.getNameObjectMap("Site");

		try
		{
			System.out.println("Before Update");
			scg.setSpecimenCollectionSite( site );
			scg.setCollectionStatus( "Complete" );
			CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getNameObjectMap("CollectionProtocol");
			Collection consentTierCollection = collectionProtocol.getConsentTierCollection();
			Collection consentTierStatusCollection = new HashSet();
			if(consentTierCollection!=null)
			{
				Iterator consentTierItr = consentTierCollection.iterator();
				while(consentTierItr.hasNext())
				{
					ConsentTier consentTier = (ConsentTier)consentTierItr.next();
					ConsentTierStatus consentStatus = new ConsentTierStatus();
					consentStatus.setConsentTier(consentTier);
					consentStatus.setStatus("No");
					consentTierStatusCollection.add(consentStatus);
				}
			}
			scg.setConsentTierStatusCollection(consentTierStatusCollection);
			scg.getCollectionProtocolRegistration().setParticipant(participant);
			BaseTestCaseUtility.setEventParameters( scg );
			SpecimenCollectionGroup updatedSCG = (SpecimenCollectionGroup)appService.updateObject(scg);
			System.out.println(updatedSCG.getCollectionStatus().equals("Complete"));
			if(updatedSCG.getCollectionStatus().equals("Complete"))
			{
				assertTrue("SCG Collected ---->", true);
			}
		}
		catch(Exception e)
		{
			System.out.println( "SupervisorTestCases.testSupervisorUserUpdateSCG()" );
			e.printStackTrace();
		}

	}





}
