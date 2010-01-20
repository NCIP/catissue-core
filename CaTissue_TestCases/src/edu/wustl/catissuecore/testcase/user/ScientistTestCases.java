package edu.wustl.catissuecore.testcase.user;

import java.util.LinkedHashMap;
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
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.RequestParameterUtility;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.beans.SessionDataBean;


public class ScientistTestCases  extends CaTissueSuiteBaseTest
{
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
	

	

}
