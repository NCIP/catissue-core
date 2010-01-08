package edu.wustl.catissuecore.testcase.biospecimen;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;

public class LabelGeneratorTestCase extends CaTissueSuiteBaseTest
{
	public void initLabelGeneratorVariables(boolean status) {
		
		Variables.isStorageContainerLabelGeneratorAvl = status;
		Variables.isSpecimenLabelGeneratorAvl = status;
		Variables.isSpecimenCollGroupLabelGeneratorAvl = status;
		Variables.isProtocolParticipantIdentifierLabelGeneratorAvl = status;	
		
		Variables.isCollectionProtocolRegistrationBarcodeGeneratorAvl = status;
		Variables.isSpecimenBarcodeGeneratorAvl = status;
		Variables.isSpecimenCollGroupBarcodeGeneratorAvl = status;
		Variables.isStorageContainerBarcodeGeneratorAvl = status;
		
	}
	
	public void testAddStorageContainerWithoutLabel()
	{
		initLabelGeneratorVariables(false);
		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		
		StorageContainerForm storageContainerForm = new StorageContainerForm();
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

		String[] holdsSpecimenClassCollection = new String[4];
		holdsSpecimenClassCollection[0]="Fluid";
		
		storageContainerForm.setSpecimenOrArrayType("Specimen");
		storageContainerForm.setHoldsSpecimenClassTypes(holdsSpecimenClassCollection);
		storageContainerForm.setActivityStatus("Active");
		storageContainerForm.setIsFull("False");
		storageContainerForm.setOperation("add");
		setRequestPathInfo("/StorageContainerAdd");
		setActionForm(storageContainerForm);
		actionPerform();
		verifyForward("failure");
		String errormsg[] = new String[] {"errors.item.required"};
    	verifyActionErrors(errormsg);
    	
//    	StorageContainer storageContainer = new StorageContainer();
//    	storageContainer.setName(null);
//    	storageContainer =(StorageContainer) appService.createObject(storageContainer); 
    }
	
	public void testAddparticipantWithoutPPIlabel(){

		ParticipantForm partForm = new ParticipantForm ();	
		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getNameObjectMap("CollectionProtocol");
     	partForm.setFirstName("participant_first_name_" + UniqueKeyGeneratorUtil.getUniqueKey());
	   	partForm.setLastName("participant_last_name_" + UniqueKeyGeneratorUtil.getUniqueKey());
		partForm.setGender("Male Gender");
		partForm.setVitalStatus("Alive");
		partForm.setGenotype("Klinefelter's Syndrome");
		partForm.setBirthDate("01-12-1985");
		partForm.setEthnicity("Hispanic or Latino");
		partForm.setRaceTypes(new String[]{"Asian"});
		partForm.setOperation("add");
		
		Map<String, String> collProtRegVal = new LinkedHashMap<String, String>();

		collProtRegVal.put("CollectionProtocolRegistration:" + "1_CollectionProtocol_shortTitle",
				collectionProtocol.getShortTitle());

		collProtRegVal.put("CollectionProtocol" + "Registration:1_registrationDate", "01-01-2008");

		collProtRegVal.put("CollectionProtocol" + "Registration:1_activityStatus",
				collectionProtocol.getActivityStatus());

		collProtRegVal.put("CollectionProtocol" + "Registration:1_isConsentAvailable",
				"None Defined");

		collProtRegVal.put("CollectionProtocol" + "Registration:1_CollectionProtocol_id", ""
				+ collectionProtocol.getId());

		collProtRegVal.put("CollectionProtocol" + "Registration:1_CollectionProtocol_Title",
				collectionProtocol.getTitle());

		partForm.setCollectionProtocolRegistrationValues(collProtRegVal);
		
		setRequestPathInfo("/ParticipantAdd");
		setActionForm(partForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
		Participant participantWithlabelGeneratorOFF = new Participant();
		ParticipantForm form = (ParticipantForm)getActionForm();
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		System.out.println("form id "+ form.getId());
		try 
		{
			participantWithlabelGeneratorOFF = (Participant)bizLogic.retrieve(Participant.class.getName(), form.getId());
		
		}
		catch (BizLogicException e) 
		{
			e.printStackTrace();
			System.out.println("ParticipantTestCases.testParticipantEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		participantWithlabelGeneratorOFF.setId(partForm.getId());
		System.out.println("participant.getCollectionProtocolRegistrationCollection() "+participantWithlabelGeneratorOFF.getCollectionProtocolRegistrationCollection());
		TestCaseUtility.setNameObjectMap("ParticipantWithlabelGeneratorOFF",participantWithlabelGeneratorOFF);
		
  }

	public void testEditSCGWithlabelgeneratorOff()
	{
		setRequestPathInfo("/CpBasedSearch");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getNameObjectMap("CollectionProtocol");
		setRequestPathInfo("/QueryParticipant");
		addRequestParameter("refresh", "false");
		addRequestParameter("cpSearchCpId", collectionProtocol.getId().toString());
		addRequestParameter("pageOf", "pageOfParticipantCPQuery");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("pageOfParticipantCPQuery");
		verifyNoActionErrors();

		Participant participant = (Participant) TestCaseUtility.getNameObjectMap("ParticipantWithlabelGeneratorOFF");
		setRequestPathInfo("/ParticipantSearch");
		addRequestParameter("id", "" + participant.getId());
		actionPerform();
		verifyForward("pageOfParticipant");
		verifyNoActionErrors();

		Collection<CollectionProtocolRegistration> cprColl = participant.getCollectionProtocolRegistrationCollection();
		Iterator<CollectionProtocolRegistration> cprItr = cprColl.iterator();
		CollectionProtocolRegistration collectioProtocolRegistration = (CollectionProtocolRegistration)cprItr.next();
		
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		Collection<SpecimenCollectionGroup> scgList = null;

		try 
		{
			scgList = bizLogic.retrieve(SpecimenCollectionGroup.class.getName(), "collectionProtocolRegistration",  collectioProtocolRegistration.getId());
		
		}
		catch (BizLogicException e) 
		{
			e.printStackTrace();
			System.out.println("ConsentTestCase.testParticipantEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		Iterator<SpecimenCollectionGroup> scgItr = scgList.iterator();
		SpecimenCollectionGroup scg = (SpecimenCollectionGroup)scgItr.next();
		SpecimenCollectionGroupForm form = new SpecimenCollectionGroupForm();
		form.setCollectionProtocolId(collectionProtocol.getId());
		setActionForm(form);
		
		setRequestPathInfo("/SearchObject");
		addRequestParameter("id", scg.getId().toString());
		addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
		addRequestParameter("operation", "edit");		
		actionPerform();
		verifyForward("pageOfSpecimenCollectionGroupCPQuery");
		verifyNoActionErrors();
		System.out.println(" getActualForward() 1111111111 "+ getActualForward());
		
		setRequestPathInfo("/SpecimenCollectionGroupSearch");
		addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
		actionPerform();
		verifyForward("pageOfSpecimenCollectionGroupCPQuery");
		verifyNoActionErrors();
		
		setRequestPathInfo("/SpecimenCollectionGroup");
		addRequestParameter("operation", "edit");	
		addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
		addRequestParameter("menuSelected", "14");
		addRequestParameter("showConsents", "yes");
		actionPerform();
		verifyForward("pageOfSpecimenCollectionGroupCPQuery");
		verifyNoActionErrors();
		
		SpecimenCollectionGroupForm scgForm = (SpecimenCollectionGroupForm)getActionForm();
		scgForm.setCollectionStatus("Complete");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
		scgForm.setName("");
		scgForm.setId(scg.getId()) ;
		scgForm.setSiteId(3L);                      
		scgForm.setOperation("edit");
		scgForm.setClinicalDiagnosis(scg.getClinicalDiagnosis());
		scgForm.setPageOf("pageOfSpecimenCollectionGroupCPQuery");
	    scgForm.setApplyEventsToSpecimens(true);
	
		setActionForm(scgForm);
		setRequestPathInfo("/CPQuerySpecimenCollectionGroupEdit");
		actionPerform();
		String errormsg[] = new String[] {"errors.item.required"};
    	verifyActionErrors(errormsg);
	    System.out.println("scg form id "+ scgForm.getId());
	    
		scgForm = (SpecimenCollectionGroupForm)getActionForm();
		scgForm.setCollectionStatus("Complete");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
		scgForm.setName("scg name"+ UniqueKeyGeneratorUtil.getUniqueKey());
		scgForm.setId(scg.getId()) ;
		Site site = (Site)TestCaseUtility.getNameObjectMap("Site");
		scgForm.setSiteId(site.getId());                      
		scgForm.setOperation("edit");
		scgForm.setClinicalDiagnosis(scg.getClinicalDiagnosis());
		scgForm.setPageOf("pageOfSpecimenCollectionGroupCPQuery");
	    scgForm.setApplyEventsToSpecimens(true);
	
		setActionForm(scgForm);
		setRequestPathInfo("/CPQuerySpecimenCollectionGroupEdit");
		actionPerform();
	    verifyForward("success");
		//verifyNoActionErrors();
		
		scgForm = (SpecimenCollectionGroupForm)getActionForm();
		TestCaseUtility.setNameObjectMap("scgFormWithLabelGeneratorOff", scgForm);
		
 }

  public void testAddSpecimeninSCGWithlabelGeneratorOff() {
	  
		SpecimenCollectionGroupForm scgForm = (SpecimenCollectionGroupForm)TestCaseUtility.getNameObjectMap("scgFormWithLabelGeneratorOff");
		NewSpecimenForm newSpecForm = new NewSpecimenForm() ;
		setRequestPathInfo("/NewSpecimenAdd");

		newSpecForm.setSpecimenCollectionGroupId(""+scgForm.getId()) ;
		newSpecForm.setSpecimenCollectionGroupName(scgForm.getName()) ;

		newSpecForm.setParentPresent(false);
		newSpecForm.setTissueSide("Not Specified") ;
		newSpecForm.setTissueSite("Not Specified");
		newSpecForm.setPathologicalStatus("Not Specified");

		Biohazard biohazard = (Biohazard) TestCaseUtility.getNameObjectMap("Biohazard");
		newSpecForm.setBiohazardName(biohazard.getName());
		newSpecForm.setBiohazardType(biohazard.getType());

		newSpecForm.setClassName("Fluid");
		newSpecForm.setType("Bile");
		newSpecForm.setQuantity("10") ;
		newSpecForm.setAvailable(true);
		newSpecForm.setAvailableQuantity("5");
		newSpecForm.setCollectionStatus("Pending") ;

		Map collectionProtocolEventMap =  (Map) TestCaseUtility.getNameObjectMap("CollectionProtocolEventMap");
		CollectionProtocolEventBean event = (CollectionProtocolEventBean) collectionProtocolEventMap.get("E1");

		newSpecForm.setCollectionEventId(event.getId()) ;

		newSpecForm.setCollectionEventSpecimenId(0L);
		newSpecForm.setCollectionEventdateOfEvent("01-28-2009");
		newSpecForm.setCollectionEventTimeInHours("11") ;
		newSpecForm.setCollectionEventTimeInMinutes("2") ;
		newSpecForm.setCollectionEventUserId(1L) ;
		newSpecForm.setCollectionEventCollectionProcedure("Use CP Defaults");
		newSpecForm.setCollectionEventContainer("Use CP Defaults") ;

		newSpecForm.setReceivedEventId(event.getId());
		newSpecForm.setReceivedEventDateOfEvent("01-28-2009");
		newSpecForm.setReceivedEventTimeInHours("11") ;
		newSpecForm.setReceivedEventTimeInMinutes("2") ;
		newSpecForm.setReceivedEventUserId(1L) ;
		newSpecForm.setReceivedEventReceivedQuality("Acceptable");

		newSpecForm.setOperation("add");
		newSpecForm.setPageOf("pageOfNewSpecimen");
		setActionForm(newSpecForm);
		actionPerform();
		verifyForward("failure");
		String errormsg[] = new String[] {"errors.item.required"};
    	verifyActionErrors(errormsg);
    	
    	initLabelGeneratorVariables(true);
}
	
	
}
