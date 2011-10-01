package edu.wustl.catissuecore.smoketest.biospecimen;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.ConsentResponseForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class ParticipantTestCase extends CaTissueSuiteSmokeBaseTest
{

	public ParticipantTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public ParticipantTestCase(String name)
	{
		super(name);
	}
	public ParticipantTestCase()
	{
		super();
	}

	public void testParticipantAdd()
	 {
		 String[] arr = getDataObject().getValues();

		 ParticipantForm participantForm =  new ParticipantForm();

		 setRequestPathInfo("/Participant");
		 addRequestParameter("operation","add");
		 addRequestParameter("pageOf","pageOfParticipant");
		 setActionForm(participantForm);
		 actionPerform();

		 participantForm = (ParticipantForm)getActionForm();

		 participantForm.setSocialSecurityNumberPartA(arr[0]);
		 participantForm.setSocialSecurityNumberPartB(arr[1]);
		 participantForm.setSocialSecurityNumberPartC(arr[2]);
		 participantForm.setLastName(arr[3]);
		 participantForm.setFirstName(arr[4]);
		 participantForm.setMiddleName(arr[5]);
		 participantForm.setBirthDate(arr[6]);
		 participantForm.setVitalStatus(arr[7]);
		 participantForm.setGender(arr[8]);
		 participantForm.setGenotype(arr[9]);
		 String arr1[] = {(arr[10])};
		 participantForm.setRaceTypes(arr1);
		 participantForm.setEthnicity(arr[11]);



		 setRequestPathInfo("/ParticipantLookup");
		 addRequestParameter("operation","add");
		 addRequestParameter("pageOf","pageOfParticipant");
		 setActionForm(participantForm);
		 actionPerform();


		 setRequestPathInfo("/ParticipantAdd");
		 addRequestParameter("operation","add");
		 addRequestParameter("pageOf","pageOfParticipant");
		 setActionForm(participantForm);
		 actionPerform();


		 verifyForward("success");
		 verifyNoActionErrors();
		 verifyActionMessages(new String[]{"object.add.successOnly"});

	 }

	public void testParticipantEdit()
	{
		String[] arr = getDataObject().getValues();

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();

		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("aliasName", "Participant");
		addRequestParameter("pageOf", "pageOfParticipant" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Participant");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "Participant."+arr[0]+".varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", arr[1]);
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[2]);

		//Participant.FIRST_NAME.varchar

		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "Participant");
		addRequestParameter("pageOf", "pageOfParticipant" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();


		setRequestPathInfo("/SearchObject");
		addRequestParameter("pageOf", "pageOfParticipant");
		addRequestParameter("operation", "search");
		addRequestParameter("id", arr[2]);
		actionPerform();


		setRequestPathInfo("/ParticipantSearch");
		addRequestParameter("operation", "search");
		addRequestParameter("pageOf", "pageOfParticipant");
		actionPerform();

		ParticipantForm participantForm = (ParticipantForm) getActionForm() ;


		setRequestPathInfo("/Participant");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfParticipant");
		setActionForm(participantForm);
		actionPerform();

		 participantForm.setSocialSecurityNumberPartA(arr[3]);
		 participantForm.setSocialSecurityNumberPartB(arr[4]);
		 participantForm.setSocialSecurityNumberPartC(arr[5]);
		 participantForm.setLastName(arr[6]);
		 participantForm.setFirstName(arr[7]);
		 participantForm.setMiddleName(arr[8]);
		 participantForm.setBirthDate(arr[9]);
		 participantForm.setVitalStatus(arr[10]);
		 participantForm.setGender(arr[11]);
		 participantForm.setGenotype(arr[12]);
		 String arr1[] = {(arr[13])};
		 participantForm.setRaceTypes(arr1);
		 participantForm.setEthnicity(arr[14]);

		 Map<String,String> collProtRegVal = new LinkedHashMap<String,String>();
		 collProtRegVal.put("CollectionProtocol" +
					"Registration:1_barcode", arr[15]);
		participantForm.setCollectionProtocolRegistrationValues(collProtRegVal) ;



		setRequestPathInfo("/ParticipantEdit");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfParticipant");
		setActionForm(participantForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.edit.successOnly"});

	}




	public void testParticipantAddWithProtocolRegistration()
	 {
		 String[] arr = getDataObject().getValues();

		 ParticipantForm participantForm =  new ParticipantForm();

		 setRequestPathInfo("/Participant");
		 addRequestParameter("operation","add");
		 addRequestParameter("pageOf","pageOfParticipant");
		 setActionForm(participantForm);
		 actionPerform();

		 participantForm = (ParticipantForm)getActionForm();

		 participantForm.setSocialSecurityNumberPartA(arr[0]);
		 participantForm.setSocialSecurityNumberPartB(arr[1]);
		 participantForm.setSocialSecurityNumberPartC(arr[2]);
		 participantForm.setLastName(arr[3]);
		 participantForm.setFirstName(arr[4]);
		 participantForm.setMiddleName(arr[5]);
		 participantForm.setBirthDate(arr[6]);
		 participantForm.setVitalStatus(arr[7]);
		 participantForm.setGender(arr[8]);
		 participantForm.setGenotype(arr[9]);
		 String arr1[] = {(arr[10])};
		 participantForm.setRaceTypes(arr1);
		 participantForm.setEthnicity(arr[11]);


		 Map<String,String> collProtRegVal = new LinkedHashMap<String,String>();

		 collProtRegVal.put("CollectionProtocolRegistration:" +
					"1_CollectionProtocol_shortTitle",arr[12]) ;




			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_activityStatus", arr[13]) ;


			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_isConsentAvailable", "None Defined") ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_CollectionProtocol_id", arr[14]) ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_CollectionProtocol_Title", arr[15]) ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_registrationDate", arr[16]) ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_barcode", arr[17]) ;




		participantForm.setCollectionProtocolRegistrationValues(collProtRegVal) ;



		 setRequestPathInfo("/ParticipantLookup");
		 addRequestParameter("operation","add");
		 addRequestParameter("pageOf","pageOfParticipant");
		 setActionForm(participantForm);
		 actionPerform();


		 setRequestPathInfo("/ParticipantAdd");
		 addRequestParameter("operation","add");
		 addRequestParameter("pageOf","pageOfParticipant");
		 setActionForm(participantForm);
		 actionPerform();


		 verifyForward("success");
		 verifyNoActionErrors();
		 verifyActionMessages(new String[]{"object.add.successOnly"});

	 }


	public void testParticipantAddWithMultipleProtocolRegistration()
	 {
		 String[] arr = getDataObject().getValues();

		 ParticipantForm participantForm =  new ParticipantForm();

		 setRequestPathInfo("/Participant");
		 addRequestParameter("operation","add");
		 addRequestParameter("pageOf","pageOfParticipant");
		 setActionForm(participantForm);
		 actionPerform();

		 participantForm = (ParticipantForm)getActionForm();

		 participantForm.setSocialSecurityNumberPartA(arr[0]);
		 participantForm.setSocialSecurityNumberPartB(arr[1]);
		 participantForm.setSocialSecurityNumberPartC(arr[2]);
		 participantForm.setLastName(arr[3]);
		 participantForm.setFirstName(arr[4]);
		 participantForm.setMiddleName(arr[5]);
		 participantForm.setBirthDate(arr[6]);
		 participantForm.setVitalStatus(arr[7]);
		 participantForm.setGender(arr[8]);
		 participantForm.setGenotype(arr[9]);
		 String arr1[] = {(arr[10])};
		 participantForm.setRaceTypes(arr1);
		 participantForm.setEthnicity(arr[11]);


		 Map<String,String> collProtRegVal = new LinkedHashMap<String,String>();

		 collProtRegVal.put("CollectionProtocolRegistration:" +
					"1_CollectionProtocol_shortTitle",arr[12]) ;




			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_activityStatus", arr[13]) ;


			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_isConsentAvailable", "Enter Response") ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_CollectionProtocol_id", arr[14]) ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_CollectionProtocol_Title", arr[15]) ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_registrationDate", arr[16]) ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_barcode", arr[17]) ;




		participantForm.setCollectionProtocolRegistrationValues(collProtRegVal) ;

		Map<String,String> collProtRegVal1 = new LinkedHashMap<String,String>();

		 collProtRegVal1.put("CollectionProtocolRegistration:" +
					"1_CollectionProtocol_shortTitle",arr[18]) ;




			collProtRegVal1.put("CollectionProtocol" +
					"Registration:1_activityStatus", arr[19]) ;


			collProtRegVal1.put("CollectionProtocol" +
					"Registration:1_isConsentAvailable", "Enter Response") ;

			collProtRegVal1.put("CollectionProtocol" +
					"Registration:1_CollectionProtocol_id", arr[20]) ;

			collProtRegVal1.put("CollectionProtocol" +
					"Registration:1_CollectionProtocol_Title", arr[21]) ;

			collProtRegVal1.put("CollectionProtocol" +
					"Registration:1_registrationDate", arr[22]) ;

			collProtRegVal1.put("CollectionProtocol" +
					"Registration:1_barcode", arr[23]) ;




		participantForm.setCollectionProtocolRegistrationValues(collProtRegVal1) ;



		 setRequestPathInfo("/ParticipantLookup");
		 addRequestParameter("operation","add");
		 addRequestParameter("pageOf","pageOfParticipant");
		 setActionForm(participantForm);
		 actionPerform();


		 setRequestPathInfo("/ParticipantAdd");
		 addRequestParameter("operation","add");
		 addRequestParameter("pageOf","pageOfParticipant");
		 setActionForm(participantForm);
		 actionPerform();


		 verifyForward("success");
		 verifyNoActionErrors();
		 verifyActionMessages(new String[]{"object.add.successOnly"});

	 }

	public void testParticipantAddWithMedicalRecordNumber()
	 {
		 String[] arr = getDataObject().getValues();

		 ParticipantForm participantForm =  new ParticipantForm();

		 setRequestPathInfo("/Participant");
		 addRequestParameter("operation","add");
		 addRequestParameter("pageOf","pageOfParticipant");
		 setActionForm(participantForm);
		 actionPerform();

		 participantForm = (ParticipantForm)getActionForm();

		 participantForm.setSocialSecurityNumberPartA(arr[0]);
		 participantForm.setSocialSecurityNumberPartB(arr[1]);
		 participantForm.setSocialSecurityNumberPartC(arr[2]);
		 participantForm.setLastName(arr[3]);
		 participantForm.setFirstName(arr[4]);
		 participantForm.setMiddleName(arr[5]);
		 participantForm.setBirthDate(arr[6]);
		 participantForm.setVitalStatus(arr[7]);
		 participantForm.setGender(arr[8]);
		 participantForm.setGenotype(arr[9]);
		 String arr1[] = {(arr[10])};
		 participantForm.setRaceTypes(arr1);
		 participantForm.setEthnicity(arr[11]);

		 Map<String,Object> medicalRec = new LinkedHashMap<String,Object>();

		 medicalRec.put("ParticipantMedicalIdentifier:1_id","");
		 medicalRec.put("ParticipantMedicalIdentifier:1_medicalRecordNumber",arr[12]);
		 medicalRec.put("ParticipantMedicalIdentifier:1_Site_id",arr[13]);

		 participantForm.setValues(medicalRec);


		setRequestPathInfo("/ParticipantLookup");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfParticipant");
		setActionForm(participantForm);
		actionPerform();


		setRequestPathInfo("/ParticipantAdd");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfParticipant");
		setActionForm(participantForm);
		actionPerform();


		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

	 }

	public void testParticipantAddWithConsent()
	 {
		 String[] arr = getDataObject().getValues();

		 ParticipantForm participantForm =  new ParticipantForm();

		 setRequestPathInfo("/Participant");
		 addRequestParameter("operation","add");
		 addRequestParameter("pageOf","pageOfParticipant");
		 setActionForm(participantForm);
		 actionPerform();

		 participantForm = (ParticipantForm)getActionForm();

		 participantForm.setSocialSecurityNumberPartA(arr[0]);
		 participantForm.setSocialSecurityNumberPartB(arr[1]);
		 participantForm.setSocialSecurityNumberPartC(arr[2]);
		 participantForm.setLastName(arr[3]);
		 participantForm.setFirstName(arr[4]);
		 participantForm.setMiddleName(arr[5]);
		 participantForm.setBirthDate(arr[6]);
		 participantForm.setVitalStatus(arr[7]);
		 participantForm.setGender(arr[8]);
		 participantForm.setGenotype(arr[9]);
		 String arr1[] = {(arr[10])};
		 participantForm.setRaceTypes(arr1);
		 participantForm.setEthnicity(arr[11]);

		 Map<String,String> collProtRegVal = new LinkedHashMap<String,String>();

		 collProtRegVal.put("CollectionProtocolRegistration:" +
					"1_CollectionProtocol_shortTitle",arr[12]) ;




			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_activityStatus", arr[13]) ;


			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_isConsentAvailable", "Enter Response") ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_CollectionProtocol_id", arr[14]) ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_CollectionProtocol_Title", arr[15]) ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_registrationDate", arr[16]) ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_barcode", arr[17]) ;




		participantForm.setCollectionProtocolRegistrationValues(collProtRegVal) ;

		ConsentResponseForm consentResponseForm = new ConsentResponseForm();



		setRequestPathInfo("/ConsentDisplay");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfConsent");
		addRequestParameter("cpSearchCpId",arr[19]);
		setActionForm(consentResponseForm);
		actionPerform();

		consentResponseForm.setConsentDate(arr[25]);
		consentResponseForm.setSignedConsentUrl(arr[26]);
		consentResponseForm.setWithdrawlButtonStatus(arr[27]);
		consentResponseForm.setWitnessId(Long.parseLong(arr[18]));
		consentResponseForm.setCollectionProtocolID(Long.parseLong(arr[19]));



		Map<String,String> consentRespsonseValues = new LinkedHashMap<String,String>();

		consentRespsonseValues.put("ConsentBean:1_statement","Research");
		consentRespsonseValues.put("ConsentBean:1_participantResponseID","");
		consentRespsonseValues.put("ConsentBean:1_consentTierID",arr[20]);
		consentRespsonseValues.put("ConsentBean:2_consentTierID",arr[21]);
		consentRespsonseValues.put("ConsentBean:1_participantResponse",arr[23]);
		consentRespsonseValues.put("ConsentBean:2_participantResponseID","");
		consentRespsonseValues.put("ConsentBean:2_participantResponse",arr[24]);
		consentRespsonseValues.put("ConsentBean:0_consentTierID",arr[22]);
		consentRespsonseValues.put("ConsentBean:2_statement","Can you?");
		consentRespsonseValues.put("ConsentBean:0_statement","Distribute");
		consentRespsonseValues.put("ConsentBean:0_participantResponseID","");
		consentRespsonseValues.put("ConsentBean:0_participantResponse",arr[24]);

		consentResponseForm.setConsentResponseValues(consentRespsonseValues);

		setRequestPathInfo("/ConsentSubmit");
		setActionForm(consentResponseForm);
		actionPerform();

		setRequestPathInfo("/ParticipantLookup");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfParticipant");
		setActionForm(participantForm);
		actionPerform();


		setRequestPathInfo("/ParticipantAdd");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfParticipant");
		setActionForm(participantForm);
		actionPerform();


		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

	 }
	 
	 	 public void testParticipantDisable()
	{
		String[] InputData = getDataObject().getValues();

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();

		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("aliasName", "Participant");
		addRequestParameter("pageOf", "pageOfParticipant" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Participant");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "Participant."+InputData[0]+".varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", InputData[1]);
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",InputData[2]);
		
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "Participant");
		addRequestParameter("pageOf", "pageOfParticipant" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		setRequestPathInfo("/SearchObject");
		addRequestParameter("pageOf", "pageOfParticipant");
		addRequestParameter("operation", "search");
		addRequestParameter("id", InputData[2]);
		actionPerform();

		setRequestPathInfo("/ParticipantSearch");
		addRequestParameter("operation", "search");
		addRequestParameter("pageOf", "pageOfParticipant");
		actionPerform();

		ParticipantForm participantForm = (ParticipantForm) getActionForm() ;

		setRequestPathInfo("/Participant");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfParticipant");
		setActionForm(participantForm);
		actionPerform();
	
		participantForm.setActivityStatus(InputData[3]);
		participantForm.setOnSubmit("/ManageBioSpecimen.do");

		setRequestPathInfo("/ParticipantEdit");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfParticipant");
		setActionForm(participantForm);
		actionPerform();

		setRequestPathInfo("/ManageBioSpecimen");
		actionPerform();

		verifyForward("success");
		verifyActionMessages(new String[]{"object.edit.successOnly"});

	}
}
