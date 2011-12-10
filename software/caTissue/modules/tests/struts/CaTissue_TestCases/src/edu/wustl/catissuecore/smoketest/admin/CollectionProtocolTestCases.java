package edu.wustl.catissuecore.smoketest.admin;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.actionForm.ProtocolEventDetailsForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class CollectionProtocolTestCases  extends CaTissueSuiteSmokeBaseTest
{
	public CollectionProtocolTestCases(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public CollectionProtocolTestCases(String name)
	{
		super(name);
	}
	public CollectionProtocolTestCases()
	{
		super();
	}



	public void testAddCollectionProtocol()
	{

		String array[] = getDataObject().getValues();
		/*Collection Protocol Details*/
		CollectionProtocolForm collForm = new CollectionProtocolForm();

		long principalInvestigatorId = Long.parseLong(array[0]);
		collForm.setPrincipalInvestigatorId(principalInvestigatorId) ;
		collForm.setTitle(array[1]);
		collForm.setOperation("add") ;
		collForm.setShortTitle(array[2]);
		collForm.setStartDate(array[3]);
		String protocolCoordinatorIds[]=new String[2];
		protocolCoordinatorIds[0] = array[49];
		protocolCoordinatorIds[1] = array[50];
		collForm.setProtocolCoordinatorIds(protocolCoordinatorIds);

		long coordinatorIds[]=new long[1];
		coordinatorIds[0]=Long.parseLong(array[51]);
		collForm.setCoordinatorIds(coordinatorIds);
		collForm.setIrbID(array[52]);

		boolean consentWaived = Boolean.parseBoolean(array[53]);
		collForm.setConsentWaived(consentWaived);
		collForm.setEnrollment(array[54]);
		collForm.setDescriptionURL(array[55]);

		boolean aliqoutInSameContainer = Boolean.parseBoolean(array[56]);
		collForm.setAliqoutInSameContainer(aliqoutInSameContainer);

		collForm.setUnsignedConsentURLName(array[57]);

		Map<String,String> consentValuesmap=new HashMap<String,String>();
		consentValuesmap.put("ConsentBean:"+array[58]+"_statement",array[59]);
		consentValuesmap.put("ConsentBean:"+array[60]+"_statement",array[61]);
		collForm.setConsentValues(consentValuesmap);


		collForm.setSpecimenLabelFormat(array[4]);
		collForm.setDerivativeLabelFormat(array[5]);
		collForm.setAliquotLabelFormat(array[6]);
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
		addRequestParameter("studyCalendarEventPoint",array[7]);
		addRequestParameter("collectionProtocolEventkey",array[8]);
		addRequestParameter("collectionPointLabel",array[9] );
		addRequestParameter("clinicalStatus",array[10]);
		addRequestParameter("clinicalDiagnosis",array[11]);

		addRequestParameter("collectionEventId",array[12]);
		addRequestParameter("collectionEventUserId", array[13]);
		addRequestParameter("collectionUserName", array[14]);
		addRequestParameter("collectionEventSpecimenId",array[15]);

		addRequestParameter("receivedEventId", array[16] );
		addRequestParameter("receivedEventUserId",array[17]);
		addRequestParameter("receivedUserName", array[18]);
		addRequestParameter("receivedEventSpecimenId", array[19]);
		addRequestParameter("pageOf", "specimenRequirement");
		addRequestParameter("operation", "add");

		addRequestParameter("labelFormat", array[20]);

		addRequestParameter("labelFormatForAliquot", array[21]);

		actionPerform();
		verifyForwardPath("/CreateSpecimenTemplate.do?operation=add");


		setRequestPathInfo("/SaveSpecimenRequirements");
		addRequestParameter("displayName",array[22] );

		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		addRequestParameter("collectionUserName", "" + bean.getLastName() + "," + bean.getFirstName());

		addRequestParameter("collectionEventContainer", array[23]);

		addRequestParameter("key",array[24] );
		addRequestParameter("receivedEventReceivedQuality",array[25]);
		addRequestParameter("collectionEventId", array[26]);
		addRequestParameter("collectionEventUserId", array[27]);
		addRequestParameter("collectionUserName", array[28]);
		addRequestParameter("collectionEventSpecimenId", array[29]);

		addRequestParameter("receivedEventId", array[30] );
		addRequestParameter("receivedEventUserId", array[31]);
		addRequestParameter("receivedUserName", array[32]);
		addRequestParameter("receivedEventSpecimenId", array[33]);

		addRequestParameter("collectionEventCollectionProcedure", array[34]);
		addRequestParameter("collectionEventContainer", array[35]);
		addRequestParameter("className", array[36]);
		addRequestParameter("tissueSite", array[37]);
		addRequestParameter("tissueSide", array[38]);
		addRequestParameter("pathologicalStatus", array[39]);
		addRequestParameter("storageLocationForSpecimen",array[40]);
		addRequestParameter("type",array[41]);
		addRequestParameter("receivedEventReceivedQuality",array[42]);
		addRequestParameter("quantity", array[43]);
		addRequestParameter("quantityPerAliquot", array[44]);
		addRequestParameter("noOfAliquots", array[45]);
		addRequestParameter("storageLocationForAliquotSpecimen", array[46]);
		addRequestParameter("operation", "add");
		addRequestParameter("labelFormat", array[47]);

		addRequestParameter("labelFormatForAliquot", array[48]);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		setRequestPathInfo("/SubmitCollectionProtocol");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}


	/*Test case for Edit Collection Protocol*/
		public void testEditCollectionProtocol2()
	{
		String[] arr = getDataObject().getValues();

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();
		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("aliasName", "CollectionProtocol");
		addRequestParameter("pageOf", "pageOfCollectionProtocol" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();
		verifyForward("pageOfCollectionProtocol");
		verifyTilesForward("pageOfDepartment",".catissuecore.editSearchPageDef");
		verifyNoActionErrors();

		simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "CollectionProtocolForm");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "CollectionProtocolForm."+arr[0]+".varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator",arr[1]);
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[2]);
		simpleQueryInterfaceForm.setPageOf("pageOfCollectionProtocol");
		setRequestPathInfo("/SimpleSearch");
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		setRequestPathInfo("/SpreadsheetView");
		actionPerform();
		verifyTilesForward("pageOfCollectionProtocol",".catissuecore.editSearchResultsDef");

		setRequestPathInfo("/SearchObject");
		addRequestParameter("pageOf", "pageOfCollectionProtocol");
		addRequestParameter("operation", "search");
		addRequestParameter("id", arr[2]);
		actionPerform();

		setRequestPathInfo("/CollectionProtocolSearch");
		addRequestParameter("pageOf", "pageOfCollectionProtocol");
		actionPerform();
		verifyForward("pageOfCollectionProtocol");

		CollectionProtocolForm collectionProtocolForm = (CollectionProtocolForm) getActionForm() ;
		setRequestPathInfo("/CollectionProtocol");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfCollectionProtocol");
		addRequestParameter("menuSelected", "9");
		setActionForm(collectionProtocolForm);
		actionPerform();
		verifyForward("pageOfCollectionProtocol");
		//verifyTilesForward("pageOfCollectionProtocol",".catissuecore.collectionProtocolDef");

		collectionProtocolForm.setPrincipalInvestigatorId(Long.parseLong(arr[3]));
		collectionProtocolForm.setTitle(arr[4]);
		collectionProtocolForm.setShortTitle(arr[5]);
		collectionProtocolForm.setIrbID(arr[6]);
		collectionProtocolForm.setStartDate(arr[7]);

		 boolean theConsent = Boolean.parseBoolean(arr[8]);
		collectionProtocolForm.setConsentWaived(theConsent);

		collectionProtocolForm.setEnrollment(arr[9]);
		collectionProtocolForm.setDescriptionURL(arr[10]);
		collectionProtocolForm.setActivityStatus(arr[11]);

		String[] protocolCoordinatorIds={(arr[12].trim())};
		collectionProtocolForm.setProtocolCoordinatorIds(protocolCoordinatorIds);

		boolean aliqoutInSameContainer = Boolean.parseBoolean(arr[13]);
		collectionProtocolForm.setAliqoutInSameContainer(aliqoutInSameContainer);


		collectionProtocolForm.setDescriptionURL(arr[14]);
		/*Map consentValues = new HashMap();
		consentValues.put(arr[15],arr[16]);                     //13,14
		collectionProtocolForm.setConsentValues(consentValues);*/



		double StudyCalendarEventPoint = Double.parseDouble(arr[15]);         //17
		collectionProtocolForm.setStudyCalendarEventPoint(arr[15]);

		collectionProtocolForm.setUnsignedConsentURLName(arr[16]);
		//collectionProtocolForm.set

		collectionProtocolForm.setOperation("edit");

		setRequestPathInfo("/CollectionProtocolEdit");
		setActionForm(collectionProtocolForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.edit.successOnly"});

	}

		public void testCPAddWithPrivileges()
		{
			String arr[] = getDataObject().getValues();

			CollectionProtocolForm collectionForm = new CollectionProtocolForm();

			setRequestPathInfo("/OpenCollectionProtocol");
			addRequestParameter("pageOf","pageOfmainCP");
			addRequestParameter("operation", "add");
			setActionForm(collectionForm);
			actionPerform();

			setRequestPathInfo("/CollectionProtocol");
			addRequestParameter("pageOf","pageOfCollectionProtocol");
			addRequestParameter("operation", "add");
			setActionForm(collectionForm);
			actionPerform();

			collectionForm = (CollectionProtocolForm)getActionForm();
			collectionForm.setPrincipalInvestigatorId(Long.parseLong(arr[0].trim()));
			long ar[]={Long.parseLong(arr[1])};
			collectionForm.setCoordinatorIds(ar);
			String[] arr1={arr[2]};
			collectionForm.setProtocolCoordinatorIds(arr1);
			collectionForm.setTitle(arr[3]);
			collectionForm.setShortTitle(arr[4]);
			collectionForm.setStartDate(arr[5]);
			collectionForm.setIrbID(arr[6]);
			collectionForm.setConsentWaived(Boolean.parseBoolean(arr[7].trim()));
			collectionForm.setDescriptionURL(arr[8]);
			collectionForm.setAliqoutInSameContainer(Boolean.parseBoolean(arr[9].trim()));



			setRequestPathInfo("/DefineEvents");
			addRequestParameter("pageOf","pageOfDefineEvents");
			addRequestParameter("operation", "add");
			setActionForm(collectionForm);
			actionPerform();

			ProtocolEventDetailsForm protocolEvent = new ProtocolEventDetailsForm();

			setRequestPathInfo("/ProtocolEventsDetails");
			addRequestParameter("operation", "add");
			addRequestParameter("pageOf", "newEvent");
			setActionForm(protocolEvent);
			actionPerform();

			protocolEvent = (ProtocolEventDetailsForm)getActionForm();

			protocolEvent.setStudyCalendarEventPoint(arr[10]);
			protocolEvent.setCollectionPointLabel(arr[11]);
			protocolEvent.setClinicalDiagnosis(arr[12]);
			protocolEvent.setClinicalStatus(arr[13]);


			setRequestPathInfo("/SaveProtocolEvents");
			addRequestParameter("pageOf","defineEvents");
			addRequestParameter("operation","add");
			setActionForm(protocolEvent);
			actionPerform();


			setRequestPathInfo("/SubmitCollectionProtocol");
			addRequestParameter("operation","add");
			setActionForm(collectionForm);
			actionPerform();

			setRequestPathInfo("/OpenCollectionProtocol");
			addRequestParameter("pageOf", "collectionProtocol");
			addRequestParameter("operation","edit");
			setActionForm(collectionForm);
			actionPerform();

			setRequestPathInfo("/CollectionProtocol");
			addRequestParameter("pageOf","pageOfCollectionProtocol");
			addRequestParameter("operation","edit");
			setActionForm(collectionForm);
			actionPerform();

			setRequestPathInfo("/DefineEvents");
			addRequestParameter("pageOf","pageOfAssignPrivilegePage");
			addRequestParameter("operation", "edit");
			setActionForm(collectionForm);
			actionPerform();

			setRequestPathInfo("/ShowAssignPrivilegePage");
			addRequestParameter("operation","edit");
			addRequestParameter("pageOf","pageOfAssignPrivilegePage");
			setActionForm(collectionForm);
			actionPerform();


			setRequestPathInfo("/SaveCollectionProtocol");
			addRequestParameter("operation","edit");
			addRequestParameter("pageOf","submitSpecimen");
			setActionForm(collectionForm);
			actionPerform();

			setRequestPathInfo("/SubmitCollectionProtocol");
			addRequestParameter("operation","edit");
			addRequestParameter("pageOf","submitSpecimen");
			setActionForm(collectionForm);
			actionPerform();

			setRequestPathInfo("/OpenCollectionProtocol");
			addRequestParameter("operation","edit");
			addRequestParameter("pageOf","collectionProtocol");
			setActionForm(collectionForm);
			actionPerform();

			verifyForward("success");
			verifyNoActionErrors();
		    verifyActionMessages(new String[]{"object.add.successOnly"});

		}

		public void testAddCollectionProtocolWithSPP()
		{

			String array[] = getDataObject().getValues();
			/*Collection Protocol Details*/
			CollectionProtocolForm collForm = new CollectionProtocolForm();

			long principalInvestigatorId = Long.parseLong(array[0]);
			collForm.setPrincipalInvestigatorId(principalInvestigatorId) ;
			collForm.setTitle(array[1]);
			collForm.setOperation("add") ;
			collForm.setShortTitle(array[2]);
			collForm.setStartDate(array[3]);
			String protocolCoordinatorIds[]=new String[2];
			protocolCoordinatorIds[0] = array[49];
			protocolCoordinatorIds[1] = array[50];
			collForm.setProtocolCoordinatorIds(protocolCoordinatorIds);

			long coordinatorIds[]=new long[1];
			coordinatorIds[0]=Long.parseLong(array[51]);
			collForm.setCoordinatorIds(coordinatorIds);
			collForm.setIrbID(array[52]);

			boolean consentWaived = Boolean.parseBoolean(array[53]);
			collForm.setConsentWaived(consentWaived);
			collForm.setEnrollment(array[54]);
			collForm.setDescriptionURL(array[55]);

			boolean aliqoutInSameContainer = Boolean.parseBoolean(array[56]);
			collForm.setAliqoutInSameContainer(aliqoutInSameContainer);

			collForm.setUnsignedConsentURLName(array[57]);

			Map<String,String> consentValuesmap=new HashMap<String,String>();
			consentValuesmap.put("ConsentBean:"+array[58]+"_statement",array[59]);
			consentValuesmap.put("ConsentBean:"+array[60]+"_statement",array[61]);
			collForm.setConsentValues(consentValuesmap);


			collForm.setSpecimenLabelFormat(array[4]);
			collForm.setDerivativeLabelFormat(array[5]);
			collForm.setAliquotLabelFormat(array[6]);
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

			addRequestParameter("limit","15");
			addRequestParameter("start","0");
			addRequestParameter("query","");
			setRequestPathInfo("/SPPData");
			actionPerform();

	        setRequestPathInfo("/SaveProtocolEvents");
			addRequestParameter("pageOf", "pageOfDefineEvents");
			addRequestParameter("studyCalendarEventPoint",array[7]);
			addRequestParameter("collectionProtocolEventkey",array[8]);
			addRequestParameter("collectionPointLabel",array[9] );
			addRequestParameter("clinicalStatus",array[10]);
			addRequestParameter("clinicalDiagnosis",array[11]);

			addRequestParameter("collectionEventId",array[12]);
			addRequestParameter("collectionEventUserId", array[13]);
			addRequestParameter("collectionUserName", array[14]);
			addRequestParameter("collectionEventSpecimenId",array[15]);

			addRequestParameter("receivedEventId", array[16] );
			addRequestParameter("receivedEventUserId",array[17]);
			addRequestParameter("receivedUserName", array[18]);
			addRequestParameter("receivedEventSpecimenId", array[19]);
			addRequestParameter("pageOf", "specimenRequirement");
			addRequestParameter("operation", "add");
			addRequestParameter("specimenProcessingProcedure", new String[ ]{"SPP_With_SpunEvent"});

			addRequestParameter("labelFormat", array[20]);

			addRequestParameter("labelFormatForAliquot", array[21]);

			actionPerform();
			verifyForwardPath("/CreateSpecimenTemplate.do?operation=add");

			setRequestPathInfo("/CreateSpecimenTemplate.do?operation=add");
			actionPerform();

			addRequestParameter("limit","15");
			addRequestParameter("start","0");
			addRequestParameter("query","");
			addRequestParameter("requestFor", "specimenEvent");
			addRequestParameter("processingSPPName", "SPP_With_SpunEvent");
			setRequestPathInfo("/ClinincalStatusComboAction");
			actionPerform();

			setRequestPathInfo("/SaveSpecimenRequirements");
			addRequestParameter("displayName",array[22] );

			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			addRequestParameter("collectionUserName", "" + bean.getLastName() + "," + bean.getFirstName());

			addRequestParameter("collectionEventContainer", array[23]);

			addRequestParameter("key",array[24] );
			addRequestParameter("receivedEventReceivedQuality",array[25]);
			addRequestParameter("collectionEventId", array[26]);
			addRequestParameter("collectionEventUserId", array[27]);
			addRequestParameter("collectionUserName", array[28]);
			addRequestParameter("collectionEventSpecimenId", array[29]);

			addRequestParameter("receivedEventId", array[30] );
			addRequestParameter("receivedEventUserId", array[31]);
			addRequestParameter("receivedUserName", array[32]);
			addRequestParameter("receivedEventSpecimenId", array[33]);

			addRequestParameter("collectionEventCollectionProcedure", array[34]);
			addRequestParameter("collectionEventContainer", array[35]);
			addRequestParameter("className", array[36]);
			addRequestParameter("tissueSite", array[37]);
			addRequestParameter("tissueSide", array[38]);
			addRequestParameter("pathologicalStatus", array[39]);
			addRequestParameter("storageLocationForSpecimen",array[40]);
			addRequestParameter("type",array[41]);
			addRequestParameter("receivedEventReceivedQuality",array[42]);
			addRequestParameter("quantity", array[43]);
			addRequestParameter("quantityPerAliquot", array[44]);
			addRequestParameter("noOfAliquots", array[45]);
			addRequestParameter("storageLocationForAliquotSpecimen", array[46]);
			addRequestParameter("operation", "add");
			addRequestParameter("labelFormat", array[47]);
			addRequestParameter("processingSPPForSpecimen","SPP_With_SpunEvent");
			addRequestParameter("creationEventForSpecimen","SPP_With_SpunEvent : 6 : Spun Event Parameters");

			addRequestParameter("labelFormatForAliquot", array[48]);
			actionPerform();
			verifyForward("success");
			//verifyNoActionErrors();

			setRequestPathInfo("/SubmitCollectionProtocol");
			addRequestParameter("operation", "add");
			actionPerform();
			verifyForward("success");
			verifyNoActionErrors();
		}

}





