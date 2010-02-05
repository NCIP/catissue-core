package edu.wustl.catissuecore.testcase.user;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.BiohazardForm;
import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.actionForm.DepartmentForm;
import edu.wustl.catissuecore.actionForm.DistributionProtocolForm;
import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.actionForm.SpecimenArrayTypeForm;
import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.RequestParameterUtility;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;


public class ScientistTestCases  extends CaTissueSuiteBaseTest
{
	
	/**
	 * Test First Time Login.
	 */
	
	public void testFirstTimeScientistLogin()
	{
		setRequestPathInfo("/Login");
		User user = (User)TestCaseUtility.getNameObjectMap( "Scientist" );
		addRequestParameter("loginName", user.getEmailAddress());
		addRequestParameter("password", "Login123");
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
		addRequestParameter("oldPassword", "Login123");
		addRequestParameter("newPassword", "Test123");
		addRequestParameter("confirmNewPassword", "Test123");
		actionPerform();
		verifyForward("success");		
		System.out.println("----"+getActualForward());
	}
	/**
	 * Test Login with Valid Login name and Password.
	 */	

	public void testScientistLogin()
	{
		setRequestPathInfo("/Login") ;
		User user = (User)TestCaseUtility.getNameObjectMap( "Scientist" );
		addRequestParameter("loginName", user.getEmailAddress());
		addRequestParameter("password", "Test123");
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
	 * Test Institution Add.
	 */
	public void testInstitutionAddWithScientistUser()
	{
		InstitutionForm institutionForm = new InstitutionForm();
		institutionForm.setName("Inst_"+UniqueKeyGeneratorUtil.getUniqueKey());
		institutionForm.setOperation("add");
		setRequestPathInfo("/InstitutionAdd");
		setActionForm(institutionForm);	
		
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);		
	}
	/**
	 * Test Department Add.
	 */
	
	public void testDepartmentAddWithScientistUser()
	{
		DepartmentForm departmentForm  = new DepartmentForm ();
		departmentForm.setName("Dept_"+UniqueKeyGeneratorUtil.getUniqueKey());
		departmentForm.setOperation("add");
		setRequestPathInfo("/DepartmentAdd");
		setActionForm(departmentForm);
		
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Site Add.
	 */
	
	public void testAddRepositorySiteWithScientistUser()
	{
		RequestParameterUtility.setAddSiteParams(this);
		addRequestParameter("type","Repository");
		setRequestPathInfo("/SiteAdd");
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test BioHazard Add Infectious BioHazard.
	 */
	public void testAddBioHazardWithScientistUser()
	{
		BiohazardForm bioForm = new BiohazardForm();
		bioForm.setName("Biohazard_" + UniqueKeyGeneratorUtil.getUniqueKey());
		bioForm.setType("Infectious");
		bioForm.setOperation("add");
		bioForm.setComments("BioHazard Comments") ;
		setRequestPathInfo("/BiohazardAdd");
		setActionForm(bioForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Cancer Research Group Add.
	 */
	
	public void testCancerResearchGroupAddWithScientistUser()
	{
		CancerResearchGroupForm cancerResearchGroupForm = new CancerResearchGroupForm();
		cancerResearchGroupForm.setName("CRG_"+UniqueKeyGeneratorUtil.getUniqueKey());
		cancerResearchGroupForm.setOperation("add");
		setRequestPathInfo("/CancerResearchGroupAdd");
		setActionForm(cancerResearchGroupForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Distribution Protocol Add.
	 */
	
	public void testDistributionProtocolAddWithScientistUser()
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
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Specimen Array Type Add.
	 */
	
	public void testSpecimenArrayTypeAddWithScientistUser()
	{
		SpecimenArrayTypeForm specForm = new SpecimenArrayTypeForm();
		specForm.setName("array_"+UniqueKeyGeneratorUtil.getUniqueKey());
		specForm.setSpecimenClass("Fluid");
		specForm.setSpecimenTypes(new String[]{"Sweat"});
		specForm.setOneDimensionCapacity(4) ;
		specForm.setTwoDimensionCapacity(4);
		specForm.setOperation("add");
		setRequestPathInfo("/SpecimenArrayTypeAdd");
		setActionForm(specForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Storage Type Add.
	 */
	
	public void testStorageTypeAddWithScientistUser()
	{
		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"srType_" + UniqueKeyGeneratorUtil.getUniqueKey(),3,3,"row","col","22","Active");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Storage Container Add.
	 */
	
	public void testStorageContainerAddWithScientistUser()
	{
		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap( "StorageType" );
		StorageContainerForm storageContainerForm = new StorageContainerForm();
		String[] holdsSpecimenClassTypes = new String[4];
		holdsSpecimenClassTypes[0] = "Tissue";
		holdsSpecimenClassTypes[1] = "Cell";
		holdsSpecimenClassTypes[2] = "Fluid";
		holdsSpecimenClassTypes[3] = "Molecular";
		storageContainerForm.setHoldsSpecimenClassTypes(holdsSpecimenClassTypes);
		storageContainerForm.setTypeId(storageType.getId());
		logger.info("----StorageTypeId : " + storageType.getId());
		storageContainerForm.setTypeName(storageType.getName());

		Site site = (Site) TestCaseUtility.getNameObjectMap("Site");

		storageContainerForm.setSiteId(site.getId());
		storageContainerForm.setNoOfContainers(1);
		storageContainerForm.setOneDimensionCapacity(25);
		storageContainerForm.setTwoDimensionCapacity(25);
		storageContainerForm.setOneDimensionLabel("row");
		storageContainerForm.setTwoDimensionLabel("row");
		storageContainerForm.setDefaultTemperature("29");
		/*addRequestParameter("holdsSpecimenClassTypes", "Cell");
		addRequestParameter("specimenOrArrayType", "SpecimenArray");*/

		String[] holdsSpecimenClassCollection = new String[4];
		holdsSpecimenClassCollection[0]="Fluid";
		holdsSpecimenClassCollection[1]="Tissue";
		holdsSpecimenClassCollection[2]="Molecular";
		holdsSpecimenClassCollection[3]="Cell";

		storageContainerForm.setSpecimenOrArrayType("Specimen");
		storageContainerForm.setHoldsSpecimenClassTypes(holdsSpecimenClassCollection);
		storageContainerForm.setActivityStatus("Active");
		storageContainerForm.setIsFull("False");
		storageContainerForm.setOperation("add");
		setRequestPathInfo("/StorageContainerAdd");
		setActionForm(storageContainerForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	
	/**
	 * Test Collection Protocol Add.
	 */
	
	public void testCollectionProtocolAddWithScientistUser()
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
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Participant Add.
	 */
	
	public void testParticipantAddWithScientistUser()
	{
		/*Participant add and registration*/
		ParticipantForm partForm = new ParticipantForm() ;
		partForm.setFirstName("participant_first_name_" + UniqueKeyGeneratorUtil.getUniqueKey()) ;
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
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_registrationDate", "01-01-2008") ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_activityStatus", collectionProtocol.getActivityStatus()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_isConsentAvailable", "None Defined") ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_CollectionProtocol_id", ""+collectionProtocol.getId()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_CollectionProtocol_Title", collectionProtocol.getTitle()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_protocolParticipantIdentifier", ""+collectionProtocol.getId()) ;
		
		partForm.setCollectionProtocolRegistrationValues(collProtRegVal) ;
		
		setRequestPathInfo("/ParticipantAdd");
		setActionForm(partForm);
		actionPerform();
		verifyForward("failure");
		//verify action errors
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	
	/**
	  * Search all participant and check if PHI data is visible
	  */ 
	  public void testCaGridQueryParticipantWithScientistLogin()
	  {
		try
		{
			StringBuffer hql = new StringBuffer();
			String targetClassName = Participant.class.getName(); 
			hql.append("select xxTargetAliasxx.lastName,xxTargetAliasxx.firstName,xxTargetAliasxx.middleName,xxTargetAliasxx.birthDate,xxTargetAliasxx.empiId,xxTargetAliasxx.gender,xxTargetAliasxx.sexGenotype,xxTargetAliasxx.ethnicity,xxTargetAliasxx.socialSecurityNumber,xxTargetAliasxx.activityStatus,xxTargetAliasxx.deathDate,xxTargetAliasxx.vitalStatus,xxTargetAliasxx.metaPhoneCode,xxTargetAliasxx.id" +
					" from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
			List result = appService.query(hql.toString());
			List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);
			List parList = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);
			System.out.println("Size : "+parList.size());
			for(int i=0;i<parList.size();i++)
			{
				Participant retutnParticpant = (Participant)parList.get(i);
				if(retutnParticpant.getFirstName()!=null||retutnParticpant.getLastName()!=null||
						retutnParticpant.getMiddleName()!=null||retutnParticpant.getBirthDate()!=null||
						retutnParticpant.getSocialSecurityNumber()!=null)
				{
					fail("Participant PHI data is visible to scientist");
				}
				Collection<ParticipantMedicalIdentifier> pmiCollection 
					= retutnParticpant.getParticipantMedicalIdentifierCollection();
				if(pmiCollection!=null)
				{
					for (Iterator<ParticipantMedicalIdentifier> iterator = pmiCollection.iterator();iterator.hasNext();)
				    {
				        ParticipantMedicalIdentifier participantMedId = (ParticipantMedicalIdentifier) iterator.next();
				        if(participantMedId.getMedicalRecordNumber()!=null)
				        {
				        	fail("Participant PHI data is visible to scientist");
				        }
				    }
				}	
				
			    Collection<CollectionProtocolRegistration> cprCollection 
			    				= retutnParticpant.getCollectionProtocolRegistrationCollection();
			    if(cprCollection!=null)
			    {
				    for (Iterator<CollectionProtocolRegistration> iterator=cprCollection.iterator();iterator.hasNext();)
				    {
				        CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) iterator.next();
				        if(cpr.getRegistrationDate()!=null || cpr.getConsentSignatureDate()!=null ||
				        		cpr.getSignedConsentDocumentURL()!=null)
				        {
				        	fail("Participant PHI data is visible to scientist");
				        }
				    }
			    }    
			}
		 }
		 catch(Exception e){
			 
		     System.out
					.println("ScientistRoleTestCases.testCaGridQueryParticipantWithScientistLogin()"+e.getMessage());
			 e.printStackTrace();
			 assertFalse(e.getMessage(), true);
		 }
	  }
	 
	  /**
	   * Search all PMI and check if PHI data is visible
	   */
	  public void testCaGridQueryPMIWithScientistLogin()
	  {
		  try
		  {
			  	StringBuffer hql = new StringBuffer();
			  	String targetClassName = ParticipantMedicalIdentifier.class.getName(); 
				hql.append("select xxTargetAliasxx.medicalRecordNumber,xxTargetAliasxx.id,xxTargetAliasxx.participant.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");

				List result = appService.query(hql.toString());
				List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);
				List pmiList = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);
				for(int i=0;i<pmiList.size();i++)
				{
					 ParticipantMedicalIdentifier participantMedId = (ParticipantMedicalIdentifier) pmiList.get(i);
				     if(participantMedId.getMedicalRecordNumber()!=null)
				     {
				        	fail("ParticipantMedicalIdentifier PHI data is visible to scientist");
				     }
				}
			 }
			 catch(Exception e)
			 {
				 System.out
					.println("ScientistRoleTestCases.testSearchPMIWithScientistLogin():"+e.getMessage());	
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
	  }
	  /**
		  * Search all CPR and check if PHI data is visible
		  *
		  */ 
		  public void testCaGridQueryProtocolRegistrationWithScientistLogin()
		  {
			try
			{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = CollectionProtocolRegistration.class.getName(); 
				hql.append("Select xxTargetAliasxx.consentSignatureDate,xxTargetAliasxx.signedConsentDocumentURL,xxTargetAliasxx.protocolParticipantIdentifier,xxTargetAliasxx.barcode,xxTargetAliasxx.registrationDate,xxTargetAliasxx.activityStatus,xxTargetAliasxx.offset,xxTargetAliasxx.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");

				List result = appService.query(hql.toString());
				List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);
				List cprList = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);

				System.out.println("Size : "+cprList.size());
				for(int i=0;i<cprList.size();i++)
				{
					CollectionProtocolRegistration returnedReg = (CollectionProtocolRegistration)cprList.get(i);
					if(returnedReg.getRegistrationDate()!=null||returnedReg.getSignedConsentDocumentURL()!=null||
							returnedReg.getConsentSignatureDate()!=null)
					{
						fail("CollectionProtocolRegistration PHI data is visible to scientist");
					}
				}
			 }
			 catch(Exception e){
				 System.out
						.println("ScientistRoleTestCases.testSearchProtocolRegistrationWithScientistLogin() "+e.getMessage());
			     Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
		  }
	  /**
		  * Search all SCG and check if PHI data is visible
		  *
		  */ 
		  public void testCaGridQuerySpecimenCollectionGroupWithScientistLogin()
		  {
			try{
				StringBuffer hql = new StringBuffer();
			  	String targetClassName = SpecimenCollectionGroup.class.getName(); 
				hql.append("select xxTargetAliasxx.name,xxTargetAliasxx.barcode,xxTargetAliasxx.collectionStatus,xxTargetAliasxx.encounterTimestamp,xxTargetAliasxx.comment,xxTargetAliasxx.surgicalPathologyNumber,xxTargetAliasxx.offset,xxTargetAliasxx.clinicalDiagnosis,xxTargetAliasxx.clinicalStatus,xxTargetAliasxx.activityStatus,xxTargetAliasxx.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");

				List result = appService.query(hql.toString());
				List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);
				List scgList = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);

				System.out.println("Size : "+scgList.size());
				for(int i=0;i<scgList.size();i++)
				{
					SpecimenCollectionGroup returnedSCG = (SpecimenCollectionGroup)scgList.get(i);
					if(returnedSCG.getSurgicalPathologyNumber()!=null)
					{
						fail("SpecimenCollectionGroup PHI data is visible to scientist");
					}
					CollectionProtocolRegistration returnedReg = returnedSCG.getCollectionProtocolRegistration();
					if(returnedReg!=null)
					{
						if(returnedReg.getRegistrationDate()!=null||returnedReg.getSignedConsentDocumentURL()!=null||
								returnedReg.getConsentSignatureDate()!=null)
						{
							fail("SpecimenCollectionGroup PHI data is visible to scientist");
						}
					}	
					Collection<SpecimenEventParameters> spEvent = returnedSCG.getSpecimenEventParametersCollection();
					if(spEvent!=null)
					{
					    Iterator<SpecimenEventParameters> eveItr = spEvent.iterator();
					    while(eveItr.hasNext())
					    {
					    	SpecimenEventParameters spEventParam = (SpecimenEventParameters)eveItr.next();
					    	if(spEventParam.getTimestamp()!=null)
					    	{
					    		fail("SpecimenCollectionGroup PHI data is visible to scientist");
					    	}
					    }
					}   
				}
			 }
			 catch(Exception e){
				 System.out
						.println("ScientistRoleTestCases.testSearchSpecimenCollectionGroupWithScientistLogin() "+e.getMessage());
			     Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
		  }
		  /**
		   * Test search Tissue specimen and check for PHI data
		   */
		  public void testCaGridQueryTissueSpecimenWithScientistLogin()
		  {
				try
				{
					StringBuffer hql = new StringBuffer();
				  	String targetClassName = TissueSpecimen.class.getName(); 
					hql.append("select xxTargetAliasxx.label,xxTargetAliasxx.isAvailable,xxTargetAliasxx.comment,xxTargetAliasxx.activityStatus,xxTargetAliasxx.collectionStatus,xxTargetAliasxx.barcode,xxTargetAliasxx.createdOn,xxTargetAliasxx.availableQuantity,xxTargetAliasxx.initialQuantity,xxTargetAliasxx.pathologicalStatus,xxTargetAliasxx.lineage,xxTargetAliasxx.specimenClass,xxTargetAliasxx.specimenType,xxTargetAliasxx.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
					List result = appService.query(hql.toString());
					List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);
					List<Specimen> spCollection = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);

					validateSpecimenData(targetClassName,spCollection);
				 }
				 catch(Exception e)
				 {
					 System.out
							.println("ScientistRoleTestCases.testSearchTissueSpecimenWithScientistLogin() "+e.getMessage());
				     Logger.out.error(e.getMessage(),e);
					 e.printStackTrace();
					 assertFalse(e.getMessage(), true);
				 }
			}
			/**
			 * Test Search Molecular Specimen and test for PHI data 
			 */
		  public void testCaGridQueryMolecularSpecimenWithScientistLogin()
		  {
				try
				{
					StringBuffer hql = new StringBuffer();
				  	String targetClassName = MolecularSpecimen.class.getName(); 
					hql.append("select xxTargetAliasxx.concentrationInMicrogramPerMicroliter,xxTargetAliasxx.label,xxTargetAliasxx.isAvailable,xxTargetAliasxx.comment,xxTargetAliasxx.activityStatus,xxTargetAliasxx.collectionStatus,xxTargetAliasxx.barcode,xxTargetAliasxx.createdOn,xxTargetAliasxx.availableQuantity,xxTargetAliasxx.initialQuantity,xxTargetAliasxx.pathologicalStatus,xxTargetAliasxx.lineage,xxTargetAliasxx.specimenClass,xxTargetAliasxx.specimenType,xxTargetAliasxx.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
					List result = appService.query(hql.toString());
					List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);
					List<MolecularSpecimen> spCollection  = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);
					validateSpecimenData(targetClassName,spCollection);
				 }
				 catch(Exception e)
				 {
					 System.out
							.println("ScientistRoleTestCases.testSearchMolecularSpecimenWithScientistLogin() "+e.getMessage());
				     Logger.out.error(e.getMessage(),e);
					 e.printStackTrace();
					 assertFalse(e.getMessage(), true);
				 }
			}
		  /**
			* Test Search Cell Specimen and test for PHI data 
			*/
		  public void testCaGridQueryCellSpecimenWithScientistLogin()
		  {
				try
				{
					StringBuffer hql = new StringBuffer();
				  	String targetClassName = CellSpecimen.class.getName(); 
					hql.append("select xxTargetAliasxx.label,xxTargetAliasxx.isAvailable,xxTargetAliasxx.comment,xxTargetAliasxx.activityStatus,xxTargetAliasxx.collectionStatus,xxTargetAliasxx.barcode,xxTargetAliasxx.createdOn,xxTargetAliasxx.availableQuantity,xxTargetAliasxx.initialQuantity,xxTargetAliasxx.pathologicalStatus,xxTargetAliasxx.lineage,xxTargetAliasxx.specimenClass,xxTargetAliasxx.specimenType,xxTargetAliasxx.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
					List result = appService.query(hql.toString());
					List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);
					List<CellSpecimen> spCollection = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);
					validateSpecimenData(targetClassName,spCollection);
				 }
				 catch(Exception e)
				 {
					 System.out
							.println("ScientistRoleTestCases.testSearchCellSpecimenWithScientistLogin() "+e.getMessage());
				     Logger.out.error(e.getMessage(),e);
					 e.printStackTrace();
					 assertFalse(e.getMessage(), true);
				 }
			}
		  /**
			 * Test Search Fluid Specimen and test for PHI data 
			 */
		  public void testCaGridQueryFluidSpecimenWithScientistLogin()
		  {
				try
				{
					StringBuffer hql = new StringBuffer();
				  	String targetClassName = FluidSpecimen.class.getName(); 
					hql.append("select xxTargetAliasxx.label,xxTargetAliasxx.isAvailable,xxTargetAliasxx.comment,xxTargetAliasxx.activityStatus,xxTargetAliasxx.collectionStatus,xxTargetAliasxx.barcode,xxTargetAliasxx.createdOn,xxTargetAliasxx.availableQuantity,xxTargetAliasxx.initialQuantity,xxTargetAliasxx.pathologicalStatus,xxTargetAliasxx.lineage,xxTargetAliasxx.specimenClass,xxTargetAliasxx.specimenType,xxTargetAliasxx.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
					List result = appService.query(hql.toString());
					List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);

					List<FluidSpecimen> spCollection = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);
					validateSpecimenData(targetClassName,spCollection);
				 }
				 catch(Exception e)
				 {
					 System.out
							.println("ScientistRoleTestCases.testSearchFluidSpecimenWithScientistLogin() "+e.getMessage());
				     Logger.out.error(e.getMessage(),e);
					 e.printStackTrace();
					 assertFalse(e.getMessage(), true);
				 }
			}
		
		  
			/**
			 * Test search SpecimenArrayContent and test for PHI data
			 */
			public void testCaGridQuerySpecimenArrayContentWithScientistLogin()
			{
				try
				{
					StringBuffer hql = new StringBuffer();
				  	String targetClassName = SpecimenArrayContent.class.getName(); 
					hql.append("select xxTargetAliasxx.concentrationInMicrogramPerMicroliter,xxTargetAliasxx.initialQuantity,xxTargetAliasxx.positionDimensionOne,xxTargetAliasxx.positionDimensionTwo,xxTargetAliasxx.id,xxTargetAliasxx.specimen.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
					List result = appService.query(hql.toString());
					List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);
					
					List sacCollection = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);
					System.out.println("Total SpecimenArrayContent Count:"+sacCollection.size());
					Iterator itr = sacCollection.iterator();
					while(itr.hasNext())
					{
						SpecimenArrayContent spe = (SpecimenArrayContent)itr.next();
						if(spe.getSpecimen().getCreatedOn()!=null)
						{
							fail("SpecimenArrayContent ->Specimen PHI data is visible to scientist");
						}
					}
				}
				 catch(Exception e)
				 {
					 System.out
							.println("ScientistRoleTestCases.testSpecimenArrayContentWithScientistLogin()"+e.getMessage());
					 e.printStackTrace();
					 assertFalse(e.getMessage(), true);
				 }
				
			}
			
			/**
			 * Test search for ReceivedEventParameters and test for PHI data
			 */
			public void testCaGridQueryReceivedEventParameters()
			{
				try
				{
					StringBuffer hql = new StringBuffer();
				  	String targetClassName = ReceivedEventParameters.class.getName(); 
					hql.append("select xxTargetAliasxx.receivedQuality,xxTargetAliasxx.timestamp,xxTargetAliasxx.comment,xxTargetAliasxx.id,xxTargetAliasxx.specimenCollectionGroup.id,xxTargetAliasxx.specimen.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
					List result = appService.query(hql.toString());
					List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);

					List recColl = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);
					Iterator itr = recColl.iterator();
					while(itr.hasNext())
					{
						ReceivedEventParameters rec = (ReceivedEventParameters)itr.next();
						if(rec.getTimestamp()!=null)
						{
							fail("ReceivedEventParameters PHI data is visible to scientist");
						}
					}
				} 
				catch(Exception e)
				{
					System.out
							.println("ScientistRoleTestCases.testSearchReceivedEventParameters()"+e.getMessage());
					e.printStackTrace();
					assertFalse(e.getMessage(), true);
				}
			}
			/**
			 * Test search for CollectionEventParameters and test for PHI data
			 */
			public void testCaGridQueryCollectionEventParameters()
			{
				try
				{
					StringBuffer hql = new StringBuffer();
				  	String targetClassName = CollectionEventParameters.class.getName(); 
					hql.append("select xxTargetAliasxx.collectionProcedure,xxTargetAliasxx.container,xxTargetAliasxx.timestamp,xxTargetAliasxx.comment,xxTargetAliasxx.id,xxTargetAliasxx.specimenCollectionGroup.id,xxTargetAliasxx.specimen.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
					List result = appService.query(hql.toString());
					List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);

					List collEveColl = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);
					Iterator itr = collEveColl.iterator();
					while(itr.hasNext())
					{
						CollectionEventParameters cep = (CollectionEventParameters)itr.next();
						if(cep.getTimestamp()!=null)
						{
							fail("CollectionEventParameters PHI data is visible to scientist");
						}
					}
				} 
				catch(Exception e)
				{
					System.out
							.println("ScientistRoleTestCases.testSearchReceivedEventParameters()"+e.getMessage());
					e.printStackTrace();
					assertFalse(e.getMessage(), true);
				}
			}
			/**
			 * Test search for PathologyReportReviewParameter and test for PHI data
			 */
			public void testCaGridQueryMoleEventparam()
			{
				try
				{
					StringBuffer hql = new StringBuffer();
				  	String targetClassName = MolecularSpecimenReviewParameters.class.getName(); 
					hql.append("select xxTargetAliasxx.gelImageURL,xxTargetAliasxx.qualityIndex,xxTargetAliasxx.laneNumber,xxTargetAliasxx.gelNumber,xxTargetAliasxx.absorbanceAt260,xxTargetAliasxx.absorbanceAt280,xxTargetAliasxx.ratio28STo18S,xxTargetAliasxx.timestamp,xxTargetAliasxx.comment,xxTargetAliasxx.id,xxTargetAliasxx.specimenCollectionGroup.id,xxTargetAliasxx.specimen.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
					List result = appService.query(hql.toString());
					List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);

					List recColl = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);
					Iterator itr = recColl.iterator();
					while(itr.hasNext())
					{
						MolecularSpecimenReviewParameters mol = (MolecularSpecimenReviewParameters)itr.next();
						if(mol.getTimestamp()!=null)
						{
							fail("MolecularSpecimenReviewParameters PHI data is visible to scientist");
						}
					}
				} 
				catch(Exception e)
				{
					System.out
							.println("ScientistRoleTestCases.testsearchMoleEventparam()"+e.getMessage());
					e.printStackTrace();
					assertFalse(e.getMessage(), true);
				}
			}
			
			/**
			 * Test search for DeidentifiedSurgicalPathologyReport and test for PHI data
			 */
			public void testCaGridQueryDeidentifiedSurgicalPathologyReport()
			{
				try
				{
					StringBuffer hql = new StringBuffer();
				  	String targetClassName = DeidentifiedSurgicalPathologyReport.class.getName(); 
					hql.append("select xxTargetAliasxx.isQuarantined,xxTargetAliasxx.activityStatus,xxTargetAliasxx.isFlagForReview,xxTargetAliasxx.reportStatus,xxTargetAliasxx.collectionDateTime,xxTargetAliasxx.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
					List result = appService.query(hql.toString());
					List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);

					List spCollection = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);
					Iterator itr = spCollection.iterator();
					while(itr.hasNext())
					{
						DeidentifiedSurgicalPathologyReport deid = (DeidentifiedSurgicalPathologyReport)itr.next();
						if(deid.getCollectionDateTime()!=null || deid.getId()!=null
								|| deid.getActivityStatus()!=null||deid.getIsFlagForReview()!=null)
						{
							fail("DeIdentifiedSurgicalPathologyReport PHI data is visible to scientist");
						}
					}
				 }
				 catch(Exception e)
				 {
					 System.out
							.println("ScientistRoleTestCases.testSearchDeidentifiedSurgicalPathologyReport()"+e.getMessage());
					 e.printStackTrace();
					 assertFalse(e.getMessage(), true);
				 }
			}
			/**
			 * Test search for IdentifiedSurgicalPathologyReport and test for PHI data
			 */
			public void testCaGridQueryIdentifiedSurgicalPathologyReport()
			{
				try
				{
					StringBuffer hql = new StringBuffer();
				  	String targetClassName = IdentifiedSurgicalPathologyReport.class.getName(); 
					hql.append("select xxTargetAliasxx.activityStatus,xxTargetAliasxx.isFlagForReview,xxTargetAliasxx.reportStatus,xxTargetAliasxx.collectionDateTime,xxTargetAliasxx.id from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
					List result = appService.query(hql.toString());
					List list = CaTissueSuiteTestUtil.createObjectList(targetClassName,result);

					List spCollection = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),list);
					Iterator itr = spCollection.iterator();
					while(itr.hasNext())
					{
						IdentifiedSurgicalPathologyReport ispr = (IdentifiedSurgicalPathologyReport)itr.next();
						System.out.println("IdentifiedSurgicalPathologyReport : "+ ispr);
						if(ispr.getCollectionDateTime()!=null || ispr.getTextContent()!= null||
								ispr.getId()!=null || ispr.getActivityStatus()!=null)
						{
							fail("IdentifiedSurgicalPathologyReport PHI data is visible to scientist");
						}
					}
				 }
				 catch(Exception e)
				 {
					System.out
							.println("ScientistRoleTestCases.testSearchIdentifiedSurgicalPathologyReport()"+e.getMessage());
					 e.printStackTrace();
					 assertFalse(e.getMessage(), true);
				 }
			}
			
			private void validateSpecimenData(String className, List spCollection)
			{
				Iterator<Specimen> itr = spCollection.iterator();
				while(itr.hasNext())
				{
					Specimen spe = (Specimen)itr.next();
					if(spe.getCreatedOn()!=null)
					{
						fail(className+" PHI data is visible to scientist");
					}
				
					Collection<SpecimenEventParameters> spEvent = spe.getSpecimenEventCollection();
					if(spEvent!=null||!spEvent.isEmpty())
					{
						Iterator<SpecimenEventParameters> iter = spEvent.iterator();
						while(iter.hasNext())
						{
								SpecimenEventParameters event = iter.next();
								fail(className+" FOr caGrid queries Evetn object should no be returned");
						}
					}
					SpecimenCollectionGroup scg =spe.getSpecimenCollectionGroup();
					if(scg!=null)
					{
						fail(className+" FOr caGrid queries SpecimenCollectionGroup object should no be returned");
					}
				}
			}
			
//			/**
//			  * Search all participant and check if PHI data is visible
//			  */ 
//			  public void testQueryParticipantWithScientistLogin()
//			  {
//				try
//				{
//					StringBuffer hql = new StringBuffer();
//					String targetClassName = Participant.class.getName(); 
//					hql.append("from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
//					List result = appService.query(hql.toString());
//					List parList = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),result);
//					System.out.println("Size : "+parList.size());
//					for(int i=0;i<parList.size();i++)
//					{
//						Participant retutnParticpant = (Participant)parList.get(i);
//						if(retutnParticpant.getFirstName()!=null||retutnParticpant.getLastName()!=null||
//								retutnParticpant.getMiddleName()!=null||retutnParticpant.getBirthDate()!=null||
//								retutnParticpant.getSocialSecurityNumber()!=null)
//						{
//							fail("Participant PHI data is visible to scientist");
//						}
//						Collection<ParticipantMedicalIdentifier> pmiCollection 
//							= retutnParticpant.getParticipantMedicalIdentifierCollection();
//						if(pmiCollection!=null)
//						{
//							for (Iterator<ParticipantMedicalIdentifier> iterator = pmiCollection.iterator();iterator.hasNext();)
//						    {
//						        ParticipantMedicalIdentifier participantMedId = (ParticipantMedicalIdentifier) iterator.next();
//						        if(participantMedId.getMedicalRecordNumber()!=null)
//						        {
//						        	fail("Participant PHI data is visible to scientist");
//						        }
//						    }
//						}	
//						
//					    Collection<CollectionProtocolRegistration> cprCollection 
//					    				= retutnParticpant.getCollectionProtocolRegistrationCollection();
//					    if(cprCollection!=null)
//					    {
//						    for (Iterator<CollectionProtocolRegistration> iterator=cprCollection.iterator();iterator.hasNext();)
//						    {
//						        CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) iterator.next();
//						        if(cpr.getRegistrationDate()!=null || cpr.getConsentSignatureDate()!=null ||
//						        		cpr.getSignedConsentDocumentURL()!=null)
//						        {
//						        	fail("Participant PHI data is visible to scientist");
//						        }
//						    }
//					    }    
//					}
//				 }
//				 catch(Exception e){
//					 
//				     System.out
//							.println("ScientistRoleTestCases.testQueryParticipantWithScientistLogin()"+e.getMessage());
//					 e.printStackTrace();
//					 assertFalse(e.getMessage(), true);
//				 }
//			  }
//			 
//			  /**
//			   * Search all PMI and check if PHI data is visible
//			   */
//			  public void testQueryPMIWithScientistLogin()
//			  {
//				  try
//				  {
//					  	StringBuffer hql = new StringBuffer();
//					  	String targetClassName = ParticipantMedicalIdentifier.class.getName(); 
//						hql.append("from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
//
//						List result = appService.query(hql.toString());
//
//						List pmiList = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),result);
//						for(int i=0;i<pmiList.size();i++)
//						{
//							 ParticipantMedicalIdentifier participantMedId = (ParticipantMedicalIdentifier) pmiList.get(i);
//						     if(participantMedId.getMedicalRecordNumber()!=null)
//						     {
//						        	fail("ParticipantMedicalIdentifier PHI data is visible to scientist");
//						     }
//						}
//					 }
//					 catch(Exception e)
//					 {
//						 System.out
//							.println("ScientistRoleTestCases.testSearchPMIWithScientistLogin():"+e.getMessage());	
//						 e.printStackTrace();
//						 assertFalse(e.getMessage(), true);
//					 }
//			  }
//			  /**
//				  * Search all CPR and check if PHI data is visible
//				  *
//				  */ 
//				  public void testQueryProtocolRegistrationWithScientistLogin()
//				  {
//					try
//					{
//						StringBuffer hql = new StringBuffer();
//					  	String targetClassName = CollectionProtocolRegistration.class.getName(); 
//						hql.append("from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
//
//						List result = appService.query(hql.toString());
//						List cprList = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),result);
//
//						System.out.println("Size : "+cprList.size());
//						for(int i=0;i<cprList.size();i++)
//						{
//							CollectionProtocolRegistration returnedReg = (CollectionProtocolRegistration)cprList.get(i);
//							if(returnedReg.getRegistrationDate()!=null||returnedReg.getSignedConsentDocumentURL()!=null||
//									returnedReg.getConsentSignatureDate()!=null)
//							{
//								fail("CollectionProtocolRegistration PHI data is visible to scientist");
//							}
//						}
//					 }
//					 catch(Exception e){
//						 System.out
//								.println("ScientistRoleTestCases.testSearchProtocolRegistrationWithScientistLogin() "+e.getMessage());
//					     Logger.out.error(e.getMessage(),e);
//						 e.printStackTrace();
//						 assertFalse(e.getMessage(), true);
//					 }
//				  }
//			  /**
//				  * Search all SCG and check if PHI data is visible
//				  *
//				  */ 
//				  public void testQuerySpecimenCollectionGroupWithScientistLogin()
//				  {
//					try{
//						StringBuffer hql = new StringBuffer();
//					  	String targetClassName = SpecimenCollectionGroup.class.getName(); 
//						hql.append("select from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
//
//						List result = appService.query(hql.toString());
//
//						List scgList = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),result);
//
//						System.out.println("Size : "+scgList.size());
//						for(int i=0;i<scgList.size();i++)
//						{
//							SpecimenCollectionGroup returnedSCG = (SpecimenCollectionGroup)scgList.get(i);
//							if(returnedSCG.getSurgicalPathologyNumber()!=null)
//							{
//								fail("SpecimenCollectionGroup PHI data is visible to scientist");
//							}
//							CollectionProtocolRegistration returnedReg = returnedSCG.getCollectionProtocolRegistration();
//							if(returnedReg!=null)
//							{
//								if(returnedReg.getRegistrationDate()!=null||returnedReg.getSignedConsentDocumentURL()!=null||
//										returnedReg.getConsentSignatureDate()!=null)
//								{
//									fail("SpecimenCollectionGroup PHI data is visible to scientist");
//								}
//							}	
//							Collection<SpecimenEventParameters> spEvent = returnedSCG.getSpecimenEventParametersCollection();
//							if(spEvent!=null)
//							{
//							    Iterator<SpecimenEventParameters> eveItr = spEvent.iterator();
//							    while(eveItr.hasNext())
//							    {
//							    	SpecimenEventParameters spEventParam = (SpecimenEventParameters)eveItr.next();
//							    	if(spEventParam.getTimestamp()!=null)
//							    	{
//							    		fail("SpecimenCollectionGroup PHI data is visible to scientist");
//							    	}
//							    }
//							}   
//						}
//					 }
//					 catch(Exception e){
//						 System.out
//								.println("ScientistRoleTestCases.testSearchSpecimenCollectionGroupWithScientistLogin() "+e.getMessage());
//					     Logger.out.error(e.getMessage(),e);
//						 e.printStackTrace();
//						 assertFalse(e.getMessage(), true);
//					 }
//				  }
//				  /**
//				   * Test search Tissue specimen and check for PHI data
//				   */
//				  public void testQueryTissueSpecimenWithScientistLogin()
//				  {
//						try
//						{
//							StringBuffer hql = new StringBuffer();
//						  	String targetClassName = TissueSpecimen.class.getName(); 
//							hql.append("from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
//							List result = appService.query(hql.toString());
//			
//							List<Specimen> spCollection = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),result);
//
//							validateSpecimenData(targetClassName,spCollection);
//						 }
//						 catch(Exception e)
//						 {
//							 System.out
//									.println("ScientistRoleTestCases.testSearchTissueSpecimenWithScientistLogin() "+e.getMessage());
//						     Logger.out.error(e.getMessage(),e);
//							 e.printStackTrace();
//							 assertFalse(e.getMessage(), true);
//						 }
//					}
//				  
//					/**
//					 * Test search SpecimenArrayContent and test for PHI data
//					 */
//					public void testQuerySpecimenArrayContentWithScientistLogin()
//					{
//						try
//						{
//							StringBuffer hql = new StringBuffer();
//						  	String targetClassName = SpecimenArrayContent.class.getName(); 
//							hql.append("from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
//							List result = appService.query(hql.toString());
//							
//							List sacCollection = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),result);
//							System.out.println("Total SpecimenArrayContent Count:"+sacCollection.size());
//							Iterator itr = sacCollection.iterator();
//							while(itr.hasNext())
//							{
//								SpecimenArrayContent spe = (SpecimenArrayContent)itr.next();
//								if(spe.getSpecimen().getCreatedOn()!=null)
//								{
//									fail("SpecimenArrayContent ->Specimen PHI data is visible to scientist");
//								}
//							}
//						}
//						 catch(Exception e)
//						 {
//							 System.out
//									.println("ScientistRoleTestCases.testSpecimenArrayContentWithScientistLogin()"+e.getMessage());
//							 e.printStackTrace();
//							 assertFalse(e.getMessage(), true);
//						 }
//						
//					}
//					
//					/**
//					 * Test search for ReceivedEventParameters and test for PHI data
//					 */
//					public void testQueryReceivedEventParameters()
//					{
//						try
//						{
//							StringBuffer hql = new StringBuffer();
//						  	String targetClassName = ReceivedEventParameters.class.getName(); 
//							hql.append("from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
//							List result = appService.query(hql.toString());
//
//							List recColl = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),result);
//							Iterator itr = recColl.iterator();
//							while(itr.hasNext())
//							{
//								ReceivedEventParameters rec = (ReceivedEventParameters)itr.next();
//								if(rec.getTimestamp()!=null)
//								{
//									fail("ReceivedEventParameters PHI data is visible to scientist");
//								}
//							}
//						} 
//						catch(Exception e)
//						{
//							System.out
//									.println("ScientistRoleTestCases.testSearchReceivedEventParameters()"+e.getMessage());
//							e.printStackTrace();
//							assertFalse(e.getMessage(), true);
//						}
//					}
//
//					/**
//					 * Test search for DeidentifiedSurgicalPathologyReport and test for PHI data
//					 */
//					public void testQueryDeidentifiedSurgicalPathologyReport()
//					{
//						try
//						{
//							StringBuffer hql = new StringBuffer();
//						  	String targetClassName = DeidentifiedSurgicalPathologyReport.class.getName(); 
//							hql.append("from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
//							List result = appService.query(hql.toString());
//
//							List spCollection = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),result);
//							Iterator itr = spCollection.iterator();
//							while(itr.hasNext())
//							{
//								DeidentifiedSurgicalPathologyReport deid = (DeidentifiedSurgicalPathologyReport)itr.next();
//								if(deid.getCollectionDateTime()!=null || deid.getId()!=null
//										|| deid.getActivityStatus()!=null||deid.getIsFlagForReview()!=null)
//								{
//									fail("DeIdentifiedSurgicalPathologyReport PHI data is visible to scientist");
//								}
//							}
//						 }
//						 catch(Exception e)
//						 {
//							 System.out
//									.println("ScientistRoleTestCases.testSearchDeidentifiedSurgicalPathologyReport()"+e.getMessage());
//							 e.printStackTrace();
//							 assertFalse(e.getMessage(), true);
//						 }
//					}
//					/**
//					 * Test search for IdentifiedSurgicalPathologyReport and test for PHI data
//					 */
//					public void testQueryIdentifiedSurgicalPathologyReport()
//					{
//						try
//						{
//							StringBuffer hql = new StringBuffer();
//						  	String targetClassName = IdentifiedSurgicalPathologyReport.class.getName(); 
//							hql.append("from "+targetClassName+" xxTargetAliasxx where xxTargetAliasxx.id>=1 and xxTargetAliasxx.id<=100");
//							List result = appService.query(hql.toString());
//
//							List spCollection = appService.delegateSearchFilter(CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN.getUserName(),result);
//							Iterator itr = spCollection.iterator();
//							while(itr.hasNext())
//							{
//								IdentifiedSurgicalPathologyReport ispr = (IdentifiedSurgicalPathologyReport)itr.next();
//								System.out.println("IdentifiedSurgicalPathologyReport : "+ ispr);
//								if(ispr.getCollectionDateTime()!=null || ispr.getTextContent()!= null||
//										ispr.getId()!=null || ispr.getActivityStatus()!=null)
//								{
//									fail("IdentifiedSurgicalPathologyReport PHI data is visible to scientist");
//								}
//							}
//						 }
//						 catch(Exception e)
//						 {
//							System.out
//									.println("ScientistRoleTestCases.testSearchIdentifiedSurgicalPathologyReport()"+e.getMessage());
//							 e.printStackTrace();
//							 assertFalse(e.getMessage(), true);
//						 }
//					}
//
//
}
