package edu.wustl.catissuecore.smoketest.biospecimen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.actionForm.CPSearchForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.GenericSpecimenVO;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.testframework.util.DataObject;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;

public class SpecimenCollectionGroupTestCase extends CaTissueSuiteSmokeBaseTest{


    public SpecimenCollectionGroupTestCase(String name, DataObject dataObject)
    {
        super(name,dataObject);
    }
    public SpecimenCollectionGroupTestCase(String name)
    {
        super(name);
    }
    public SpecimenCollectionGroupTestCase()
    {
        super();
    }
    public void testSpecimenCollectionGroupAdd()
    {

        String[] arr = getDataObject().getValues();

        SpecimenCollectionGroupForm specimenCollectionGroupForm= new SpecimenCollectionGroupForm();
        specimenCollectionGroupForm.setParticipantNameWithProtocolId(arr[0]+","+arr[1]+"("+arr[2]+"_"+arr[3]+")");
        specimenCollectionGroupForm.setCollectionProtocolEventId(Long.parseLong(arr[4]));
        specimenCollectionGroupForm.setCollectionProtocolRegistrationId(Long.parseLong(arr[5]));

        specimenCollectionGroupForm.setSiteId(Long.parseLong(arr[6]));
        specimenCollectionGroupForm.setClinicalDiagnosis(arr[7]);
        specimenCollectionGroupForm.setClinicalStatus(arr[8]);
        specimenCollectionGroupForm.setActivityStatus(arr[9]);
        specimenCollectionGroupForm.setCollectionStatus(arr[10]);

        //RecievedEvent details
        specimenCollectionGroupForm.setReceivedEventUserId(Long.parseLong(arr[11]));
        specimenCollectionGroupForm.setReceivedEventDateOfEvent(arr[12]);
        specimenCollectionGroupForm.setReceivedEventTimeInHours(arr[13]);
        specimenCollectionGroupForm.setReceivedEventTimeInMinutes(arr[14]);
        specimenCollectionGroupForm.setReceivedEventReceivedQuality(arr[15]);

        //CollectedEvent details
        specimenCollectionGroupForm.setCollectionEventUserId(Long.parseLong(arr[11]));
        specimenCollectionGroupForm.setCollectionEventdateOfEvent(arr[12]);
        specimenCollectionGroupForm.setCollectionEventTimeInHours(arr[13]);
        specimenCollectionGroupForm.setCollectionEventTimeInMinutes(arr[14]);
        specimenCollectionGroupForm.setCollectionEventCollectionProcedure(arr[16]);
        specimenCollectionGroupForm.setCollectionEventContainer(arr[17]);

        //specimenCollectionGroupForm.setNumberOfSpecimens(Integer.parseInt(arr[17]));

        setRequestPathInfo("/CPQuerySpecimenCollectionGroupAdd");
        addRequestParameter("operation","add");
        addRequestParameter("pageOf","pageOfSpecimenCollectionGroupAddCPQuery");
        setActionForm(specimenCollectionGroupForm);
        actionPerform();
        verifyForward("success");
        verifyActionMessages(new String[]{"object.add.successOnly"});
    }


    public void testSpecimenCollectionGroupEdit()
    {
        String[] arr = getDataObject().getValues();

        SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();

        setRequestPathInfo("/SimpleQueryInterface");
        addRequestParameter("aliasName", "SpecimenCollectionGroup");
        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroup" );
        setActionForm(simpleQueryInterfaceForm);
        actionPerform();

        simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
        simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "SpecimenCollectionGroup");
        simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "SpecimenCollectionGroup."+arr[0]+".varchar");
        simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", arr[1]);
        simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[2]);

        //Participant.FIRST_NAME.varchar

        setRequestPathInfo("/SimpleSearch");
        addRequestParameter("aliasName", "SpecimenCollectionGroup");
        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroup" );
        setActionForm(simpleQueryInterfaceForm);
        actionPerform();


        setRequestPathInfo("/SearchObject");
        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroup");
        addRequestParameter("operation", "search");
        addRequestParameter("id", arr[2]);
        actionPerform();


        setRequestPathInfo("/SpecimenCollectionGroupSearch");
        addRequestParameter("operation", "search");
        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroup");
        actionPerform();

        SpecimenCollectionGroupForm specimenCollectionGroupForm = (SpecimenCollectionGroupForm) getActionForm() ;


        setRequestPathInfo("/SpecimenCollectionGroup");
        addRequestParameter("operation", "edit");
        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroup");
        setActionForm(specimenCollectionGroupForm);
        actionPerform();

        //SpecimenCollectionGroupForm specimenCollectionGroupForm= new SpecimenCollectionGroupForm();
        specimenCollectionGroupForm.setParticipantNameWithProtocolId(arr[3]+","+arr[4]+"("+arr[5]+"_"+arr[6]+")");
        specimenCollectionGroupForm.setCollectionProtocolEventId(Long.parseLong(arr[7]));
        specimenCollectionGroupForm.setCollectionProtocolRegistrationId(Long.parseLong(arr[8]));

        specimenCollectionGroupForm.setSiteId(Long.parseLong(arr[9]));
        specimenCollectionGroupForm.setClinicalDiagnosis(arr[10]);
        specimenCollectionGroupForm.setClinicalStatus(arr[11]);
        specimenCollectionGroupForm.setActivityStatus(arr[12]);
        specimenCollectionGroupForm.setCollectionStatus(arr[13]);

        //RecievedEvent details
        specimenCollectionGroupForm.setReceivedEventUserId(Long.parseLong(arr[14]));
        specimenCollectionGroupForm.setReceivedEventDateOfEvent(arr[15]);
        specimenCollectionGroupForm.setReceivedEventTimeInHours(arr[16]);
        specimenCollectionGroupForm.setReceivedEventTimeInMinutes(arr[17]);
        specimenCollectionGroupForm.setReceivedEventReceivedQuality(arr[18]);

        //CollectedEvent details
        specimenCollectionGroupForm.setCollectionEventUserId(Long.parseLong(arr[14]));
        specimenCollectionGroupForm.setCollectionEventdateOfEvent(arr[15]);
        specimenCollectionGroupForm.setCollectionEventTimeInHours(arr[16]);
        specimenCollectionGroupForm.setCollectionEventTimeInMinutes(arr[17]);
        specimenCollectionGroupForm.setCollectionEventCollectionProcedure(arr[19]);
        specimenCollectionGroupForm.setCollectionEventContainer(arr[20]);



        setRequestPathInfo("/SpecimenCollectionGroupEdit");
        addRequestParameter("operation", "edit");
        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroup");
        setActionForm(specimenCollectionGroupForm);
        actionPerform();
        verifyForward("success");
        verifyActionMessages(new String[]{"object.edit.successOnly"});

    }

    public void testSpecimenCollectionGroupUpdate()
    {
        String[] InputData = getDataObject().getValues();

        CPSearchForm cpSearchForm = new CPSearchForm();
        ParticipantForm participantForm = new ParticipantForm();

        setRequestPathInfo("/CpBasedSearch");
        setActionForm(cpSearchForm);
        actionPerform();

        setRequestPathInfo("/showCpAndParticipants");
        addRequestParameter("pageOf", "pageOfCpQueryResults");
        setActionForm(cpSearchForm);
        actionPerform();

        setRequestPathInfo("/blankScreenAction");
        actionPerform();

        SpecimenCollectionGroupBizLogic scg = new  SpecimenCollectionGroupBizLogic();
	    try {
			scg.getSCGTreeForCPBasedView(Long.parseLong(InputData[58]),Long.parseLong(InputData[59]));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BizLogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        setRequestPathInfo("/QueryParticipantSearch");
        addRequestParameter("id", InputData[0]);
        addRequestParameter("operation", "edit");
        addRequestParameter("pageOf", "pageOfParticipantCPQueryEdit");
        addRequestParameter("cpSearchCpId", InputData[1]);
        setActionForm(participantForm);
        actionPerform();

        setRequestPathInfo("/QueryParticipant");
        addRequestParameter("operation", "edit");
        addRequestParameter("pageOf", "pageOfParticipantCPQuery");
        setActionForm(participantForm);
        actionPerform();

        SpecimenCollectionGroupForm specimenCollectionGroupForm = new SpecimenCollectionGroupForm();

        setRequestPathInfo("/QuerySpecimenCollectionGroupSearch");
        addRequestParameter("id",InputData[2]);
        addRequestParameter("cpSearchParticipantId",InputData[3]);
        addRequestParameter("cpSearchCpId",InputData[4]);
        addRequestParameter("operation", "edit");
        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQueryEdit");
        setActionForm(specimenCollectionGroupForm);
        actionPerform();

        specimenCollectionGroupForm = (SpecimenCollectionGroupForm) getActionForm() ;

        setRequestPathInfo("/QuerySpecimenCollectionGroup");
        addRequestParameter("operation", "edit");
        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
        setActionForm(specimenCollectionGroupForm);
        actionPerform();

        specimenCollectionGroupForm.setCollectionProtocolName(InputData[5]);
        specimenCollectionGroupForm.setParticipantNameWithProtocolId(","+" ("+InputData[31]+"_"+InputData[32]+")");
        specimenCollectionGroupForm.setCollectionProtocolEventId(Long.parseLong(InputData[6]));
        specimenCollectionGroupForm.setCollectionProtocolRegistrationId(Long.parseLong(InputData[7]));

        specimenCollectionGroupForm.setName(InputData[8]);
        specimenCollectionGroupForm.setBarcode(InputData[33]);
        specimenCollectionGroupForm.setSiteId(Long.parseLong(InputData[9]));
        specimenCollectionGroupForm.setOffset(Integer.parseInt(InputData[34]));
        specimenCollectionGroupForm.setSurgicalPathologyNumber(InputData[56]);
        specimenCollectionGroupForm.setClinicalDiagnosis(InputData[10]);
        specimenCollectionGroupForm.setClinicalStatus(InputData[11]);
        specimenCollectionGroupForm.setActivityStatus(InputData[12]);
        specimenCollectionGroupForm.setCollectionStatus(InputData[13]);
        specimenCollectionGroupForm.setComment(InputData[14]);

        specimenCollectionGroupForm.setCollectionEventUserId(Long.parseLong(InputData[15]));
        specimenCollectionGroupForm.setCollectionEventdateOfEvent(InputData[16]);
        specimenCollectionGroupForm.setCollectionEventTimeInHours(InputData[17]);
        specimenCollectionGroupForm.setCollectionEventTimeInMinutes(InputData[18]);
        specimenCollectionGroupForm.setCollectionEventCollectionProcedure(InputData[19]);
        specimenCollectionGroupForm.setCollectionEventContainer(InputData[20]);
        specimenCollectionGroupForm.setCollectionEventComments(InputData[21]);

        specimenCollectionGroupForm.setReceivedEventUserId(Long.parseLong(InputData[22]));
        specimenCollectionGroupForm.setReceivedEventDateOfEvent(InputData[23]);
        specimenCollectionGroupForm.setReceivedEventTimeInHours(InputData[24]);
        specimenCollectionGroupForm.setReceivedEventTimeInMinutes(InputData[25]);
        specimenCollectionGroupForm.setReceivedEventReceivedQuality(InputData[26]);
        specimenCollectionGroupForm.setReceivedEventComments(InputData[27]);

        specimenCollectionGroupForm.setNumberOfSpecimens(Integer.parseInt(InputData[28]));
        specimenCollectionGroupForm.setRadioButtonForParticipant(Integer.parseInt(InputData[29]));
        specimenCollectionGroupForm.setCollectionProtocolId(Long.parseLong(InputData[30]));

        specimenCollectionGroupForm.setConsentTierCounter(Integer.parseInt(InputData[57]));

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("ConsentBean:"+InputData[35]+"_specimenCollectionGroupLevelResponse", InputData[38]);
        map.put("ConsentBean:"+InputData[35]+"_statement",InputData[39]);
        map.put("ConsentBean:"+InputData[35]+"_participantResponseID",InputData[40]);
        map.put("ConsentBean:"+InputData[35]+"_participantResponse",InputData[41]);
        map.put("ConsentBean:"+InputData[35]+"_specimenCollectionGroupLevelResponseID",InputData[42]);
        map.put("ConsentBean:"+InputData[35]+"_consentTierID",InputData[43]);
        map.put("ConsentBean:"+InputData[36]+"_statement",InputData[44]);
        map.put("ConsentBean:"+InputData[36]+"_specimenCollectionGroupLevelResponseID",InputData[45]);
        map.put("ConsentBean:"+InputData[36]+"_specimenCollectionGroupLevelResponse",InputData[46]);
        map.put("ConsentBean:"+InputData[36]+"_participantResponseID",InputData[47]);
        map.put("ConsentBean:"+InputData[36]+"_participantResponse",InputData[48]);
        map.put("ConsentBean:"+InputData[36]+"_consentTierID",InputData[49]);
        map.put("ConsentBean:"+InputData[37]+"_participantResponseID",InputData[50]);
        map.put("ConsentBean:"+InputData[37]+"_specimenCollectionGroupLevelResponseID",InputData[51]);
        map.put("ConsentBean:"+InputData[37]+"_specimenCollectionGroupLevelResponse",InputData[52]);
        map.put("ConsentBean:"+InputData[37]+"_consentTierID",InputData[53]);
        map.put("ConsentBean:"+InputData[37]+"_participantResponse",InputData[54]);
        map.put("ConsentBean:"+InputData[37]+"_statement",InputData[55]);

        specimenCollectionGroupForm.setConsentResponseForScgValues(map);

        setRequestPathInfo("/CPQuerySpecimenCollectionGroupEdit");
        addRequestParameter("operation", "edit");
        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
        setActionForm(specimenCollectionGroupForm);
        actionPerform();

        setRequestPathInfo("/AnticipatorySpecimenView");
        addRequestParameter("operation", "edit");
        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
        setActionForm(specimenCollectionGroupForm);
        actionPerform();

        ViewSpecimenSummaryForm viewSpecimenSummaryForm =new ViewSpecimenSummaryForm();
        setRequestPathInfo("/GenericSpecimenSummary");
        setActionForm(viewSpecimenSummaryForm);
        actionPerform();

        verifyForward("success");
        verifyNoActionErrors();
        verifyActionMessages(new String[]{"object.edit.successOnly"});

    }

	public void testSpecimenCollectionGroupSpecimenCollected()
	 {
		//In this test case we are collecting the specimen,aliquot,derivative of a particular SCG.
			String[] arr = getDataObject().getValues();

			CPSearchForm cpSearchForm = new CPSearchForm();
	        ParticipantForm participantForm = new ParticipantForm();

	        setRequestPathInfo("/CpBasedSearch");
	        setActionForm(cpSearchForm);
	        actionPerform();

	        setRequestPathInfo("/showCpAndParticipants");
	        addRequestParameter("pageOf", "pageOfCpQueryResults");
	        setActionForm(cpSearchForm);
	        actionPerform();

	        setRequestPathInfo("/blankScreenAction");
	        actionPerform();

	        setRequestPathInfo("/QueryParticipantSearch");
	        addRequestParameter("id", arr[0]);
	        addRequestParameter("operation", "edit");
	        addRequestParameter("pageOf", "pageOfParticipantCPQueryEdit");
	        addRequestParameter("cpSearchCpId", arr[1]);
	        setActionForm(participantForm);
	        actionPerform();

	        setRequestPathInfo("/QueryParticipant");
	        addRequestParameter("operation", "edit");
	        addRequestParameter("pageOf", "pageOfParticipantCPQuery");
	        setActionForm(participantForm);
	        actionPerform();

	        SpecimenCollectionGroupForm specimenCollectionGroupForm = new SpecimenCollectionGroupForm();

	        setRequestPathInfo("/QuerySpecimenCollectionGroupSearch");
	        addRequestParameter("id",arr[2]);
	        addRequestParameter("cpSearchParticipantId",arr[0]);
	        addRequestParameter("cpSearchCpId",arr[1]);
	        addRequestParameter("operation", "edit");
	        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQueryEdit");
	        setActionForm(specimenCollectionGroupForm);
	        actionPerform();

	        specimenCollectionGroupForm = (SpecimenCollectionGroupForm) getActionForm() ;

	        setRequestPathInfo("/QuerySpecimenCollectionGroup");
	        addRequestParameter("operation", "edit");
	        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
	        setActionForm(specimenCollectionGroupForm);
	        actionPerform();


	        setRequestPathInfo("/CPQuerySpecimenCollectionGroupEdit");
	        addRequestParameter("operation", "edit");
	        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
	        setActionForm(specimenCollectionGroupForm);
	        actionPerform();


	        setRequestPathInfo("/AnticipatorySpecimenView");
	        addRequestParameter("operation", "edit");
	        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
	        setActionForm(specimenCollectionGroupForm);
	        actionPerform();


			ViewSpecimenSummaryForm viewSpecimenSummaryForm =new ViewSpecimenSummaryForm();

			setRequestPathInfo("/GenericSpecimenSummary");
			addRequestParameter("target","anticipatory");
			addRequestParameter("submitAction","updateSpecimenStatus");
			setActionForm(viewSpecimenSummaryForm);
			actionPerform();
			verifyForward("anticipatory");

			viewSpecimenSummaryForm=(ViewSpecimenSummaryForm)getActionForm();
			// Specimen List added
			List<GenericSpecimen> specimenList = new LinkedList<GenericSpecimen>();
			specimenList=viewSpecimenSummaryForm.getSpecimenList();
			GenericSpecimenVO genericSpecimenVO= new GenericSpecimenVO();
			genericSpecimenVO=(GenericSpecimenVO)specimenList.get(0);

			genericSpecimenVO.setCheckedSpecimen(Boolean.parseBoolean(arr[3]));
			genericSpecimenVO.setStorageContainerForSpecimen(arr[4]);
			genericSpecimenVO.setSelectedContainerName(arr[5]);
			genericSpecimenVO.setContainerId(arr[6]);
			genericSpecimenVO.setPositionDimensionOne(arr[7]);
			genericSpecimenVO.setPositionDimensionTwo(arr[9]);
			specimenList.set(0,genericSpecimenVO);

			viewSpecimenSummaryForm.setSpecimenList(specimenList);

			//Derivative List added
			List<GenericSpecimen> derivativeList = new LinkedList<GenericSpecimen>();
			derivativeList=viewSpecimenSummaryForm.getDerivedList();
			GenericSpecimenVO genericSpecimenVO_1= new GenericSpecimenVO();
			genericSpecimenVO_1=(GenericSpecimenVO)derivativeList.get(0);

			genericSpecimenVO_1.setCheckedSpecimen(Boolean.parseBoolean(arr[3]));
			genericSpecimenVO_1.setStorageContainerForSpecimen(arr[4]);
			genericSpecimenVO_1.setSelectedContainerName(arr[5]);
			genericSpecimenVO_1.setContainerId(arr[6]);
			genericSpecimenVO_1.setPositionDimensionOne(arr[7]);
			genericSpecimenVO_1.setPositionDimensionTwo(arr[8]);
			derivativeList.set(0,genericSpecimenVO_1);

			viewSpecimenSummaryForm.setDerivedList(derivativeList);

			//Aliquot List added
			List<GenericSpecimen> aliquotList = new LinkedList<GenericSpecimen>();
			aliquotList=viewSpecimenSummaryForm.getAliquotList();
			GenericSpecimenVO genericSpecimenVO_2[]= new GenericSpecimenVO[5];
			genericSpecimenVO_2[0]= new GenericSpecimenVO();
			genericSpecimenVO_2[1]= new GenericSpecimenVO();
			genericSpecimenVO_2[2]= new GenericSpecimenVO();
			genericSpecimenVO_2[3]= new GenericSpecimenVO();
			genericSpecimenVO_2[4]= new GenericSpecimenVO();

			genericSpecimenVO_2[0]=(GenericSpecimenVO)aliquotList.get(0);
			genericSpecimenVO_2[1]=(GenericSpecimenVO)aliquotList.get(1);
			genericSpecimenVO_2[2]=(GenericSpecimenVO)aliquotList.get(2);
			genericSpecimenVO_2[3]=(GenericSpecimenVO)aliquotList.get(3);
			genericSpecimenVO_2[4]=(GenericSpecimenVO)aliquotList.get(4);

			//genericSpecimenVO_2[0].setCheckedSpecimen(Boolean.parseBoolean(arr[3]));
			//genericSpecimenVO_2[0].setStorageContainerForSpecimen(arr[4]);

			genericSpecimenVO_2[0].setCheckedSpecimen(Boolean.parseBoolean(arr[3]));
			genericSpecimenVO_2[0].setStorageContainerForSpecimen(arr[4]);
			genericSpecimenVO_2[0].setSelectedContainerName(arr[5]);
			genericSpecimenVO_2[0].setContainerId(arr[6]);
			genericSpecimenVO_2[0].setPositionDimensionOne(arr[10]);
			genericSpecimenVO_2[0].setPositionDimensionTwo(arr[11]);

			//genericSpecimenVO_2[1].setCheckedSpecimen(Boolean.parseBoolean(arr[3]));
			//genericSpecimenVO_2[1].setStorageContainerForSpecimen(arr[4]);

			genericSpecimenVO_2[1].setCheckedSpecimen(Boolean.parseBoolean(arr[3]));
			genericSpecimenVO_2[1].setStorageContainerForSpecimen(arr[4]);
			genericSpecimenVO_2[1].setSelectedContainerName(arr[5]);
			genericSpecimenVO_2[1].setContainerId(arr[6]);
			genericSpecimenVO_2[1].setPositionDimensionOne(arr[10]);
			genericSpecimenVO_2[1].setPositionDimensionTwo(arr[12]);

//			genericSpecimenVO_2[2].setCheckedSpecimen(Boolean.parseBoolean(arr[3]));
	//		genericSpecimenVO_2[2].setStorageContainerForSpecimen(arr[4]);

			genericSpecimenVO_2[2].setCheckedSpecimen(Boolean.parseBoolean(arr[3]));
			genericSpecimenVO_2[2].setStorageContainerForSpecimen(arr[4]);
			genericSpecimenVO_2[2].setSelectedContainerName(arr[5]);
			genericSpecimenVO_2[2].setContainerId(arr[6]);
			genericSpecimenVO_2[2].setPositionDimensionOne(arr[10]);
			genericSpecimenVO_2[2].setPositionDimensionTwo(arr[13]);

			//genericSpecimenVO_2[3].setCheckedSpecimen(Boolean.parseBoolean(arr[3]));
			//genericSpecimenVO_2[3].setStorageContainerForSpecimen(arr[4]);

			genericSpecimenVO_2[3].setCheckedSpecimen(Boolean.parseBoolean(arr[3]));
			genericSpecimenVO_2[3].setStorageContainerForSpecimen(arr[4]);
			genericSpecimenVO_2[3].setSelectedContainerName(arr[5]);
			genericSpecimenVO_2[3].setContainerId(arr[6]);
			genericSpecimenVO_2[3].setPositionDimensionOne(arr[10]);
			genericSpecimenVO_2[3].setPositionDimensionTwo(arr[14]);

			//genericSpecimenVO_2[4].setCheckedSpecimen(Boolean.parseBoolean(arr[3]));
			//genericSpecimenVO_2[4].setStorageContainerForSpecimen(arr[4]);

			genericSpecimenVO_2[4].setCheckedSpecimen(Boolean.parseBoolean(arr[3]));
			genericSpecimenVO_2[4].setStorageContainerForSpecimen(arr[4]);
			genericSpecimenVO_2[4].setSelectedContainerName(arr[5]);
			genericSpecimenVO_2[4].setContainerId(arr[6]);
			genericSpecimenVO_2[4].setPositionDimensionOne(arr[10]);
			genericSpecimenVO_2[4].setPositionDimensionTwo(arr[15]);

			aliquotList.set(0,genericSpecimenVO_2[0]);
			aliquotList.set(1,genericSpecimenVO_2[1]);
			aliquotList.set(2,genericSpecimenVO_2[2]);
			aliquotList.set(3,genericSpecimenVO_2[3]);
			aliquotList.set(4,genericSpecimenVO_2[4]);

			viewSpecimenSummaryForm.setDerivedList(aliquotList);


			setRequestPathInfo("/GenericSpecimenSummary");
			addRequestParameter("target","anticipatory");
			addRequestParameter("submitAction","updateSpecimenStatus");

			setActionForm(viewSpecimenSummaryForm);
			actionPerform();


			setRequestPathInfo("/updateSpecimenStatus");
			setActionForm(viewSpecimenSummaryForm);
			actionPerform();
			verifyForward("success");
	 }

	public void testParticipantAddWithSCG()
	{
		String[] arr = getDataObject().getValues();

		CPSearchForm cpSearchForm = new CPSearchForm();

		 setRequestPathInfo("/CpBasedSearch");
	     setActionForm(cpSearchForm);
	     actionPerform();

	     setRequestPathInfo("/showCpAndParticipants");
	     addRequestParameter("pageOf", "pageOfCpQueryResults");
	     setActionForm(cpSearchForm);
	     actionPerform();

	     setRequestPathInfo("/blankScreenAction");
	     actionPerform();

	     ParticipantForm participantForm = new ParticipantForm();

	     setRequestPathInfo("/QueryParticipant");
	     addRequestParameter("operation", "add");
	     addRequestParameter("pageOf", "pageOfParticipantCPQuery");
	     addRequestParameter("cpSearchCpId","181");
	     setActionForm(participantForm);
	     actionPerform();

	     participantForm = (ParticipantForm)getActionForm();

	     participantForm.setLastName("Yadav");
	     participantForm.setMiddleName("Laloo");
	     participantForm.setFirstName("Prashad");



	     setRequestPathInfo("/CPQueryParticipantAdd");
	     addRequestParameter("operation", "add");
	     addRequestParameter("pageOf", "pageOfParticipantCPQuery");
	     setActionForm(participantForm);
	     actionPerform();

	     setRequestPathInfo("/Participant");
	     addRequestParameter("operation","edit");
	     addRequestParameter("pageOf","pageOfParticipantCPQuery");
	     setActionForm(participantForm);
	     actionPerform();

	     participantForm = (ParticipantForm)getActionForm();

	     Map<String,String> collProtRegVal = new LinkedHashMap<String,String>();

	     collProtRegVal.put("CollectionProtocolRegistration:" +
					"1_CollectionProtocol_shortTitle",arr[0]) ;


			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_activityStatus", arr[1]) ;


			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_isConsentAvailable", "None Defined") ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_CollectionProtocol_id", arr[2]) ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_CollectionProtocol_Title", arr[3]) ;

			collProtRegVal.put("CollectionProtocol" +
					"Registration:1_registrationDate", arr[4]) ;

	     collProtRegVal.put("CollectionProtocol" +
					"Registration:1_barcode", arr[5]) ;


	     collProtRegVal.put("CollectionProtocol" +
					"Registration:1_id", String.valueOf(participantForm.getId()));


	     participantForm.setCollectionProtocolRegistrationValues(collProtRegVal) ;



	     setRequestPathInfo("/CPQueryParticipantEdit");
	     addRequestParameter("operation","edit");
	     addRequestParameter("pageOf","pageOfParticipantCPQuery");
	     setActionForm(participantForm);
	     actionPerform();


	     SpecimenCollectionGroupForm specimenCollectionGroupForm = new SpecimenCollectionGroupForm();


	     	Map<String,Object> map2 = new LinkedHashMap<String,Object>();
	     	map2.put("participantId",participantForm.getId());
	     	map2.put("collectionProtocolId",Long.parseLong("181"));



	     	request.setAttribute("forwardToHashMap", map2);
	     	setRequestPathInfo("/QuerySpecimenCollectionGroup");
	     	addRequestParameter("pageOf","pageOfSpecimenCollectionGroupCPQuery");
	     	addRequestParameter("operation","add");
	     	addRequestParameter("menuSelected","13");

	     	addRequestParameter("id",String.valueOf(participantForm.getId()));
	     	addRequestParameter("participantId",String.valueOf(participantForm.getId()));
	     	setActionForm(specimenCollectionGroupForm);
	     	actionPerform();


	        specimenCollectionGroupForm.setCollectionProtocolEventId(Long.parseLong("341"));//341


	        specimenCollectionGroupForm.setSiteId(Long.parseLong(arr[6]));//82
	        specimenCollectionGroupForm.setClinicalDiagnosis(arr[7]);
	        specimenCollectionGroupForm.setClinicalStatus(arr[8]);
	        specimenCollectionGroupForm.setActivityStatus(arr[9]);
	        specimenCollectionGroupForm.setCollectionStatus(arr[10]);

	        //RecievedEvent details
	        specimenCollectionGroupForm.setReceivedEventUserId(Long.parseLong(arr[11]));
	        specimenCollectionGroupForm.setReceivedEventDateOfEvent(arr[12]);
	        specimenCollectionGroupForm.setReceivedEventTimeInHours(arr[13]);
	        specimenCollectionGroupForm.setReceivedEventTimeInMinutes(arr[14]);
	        specimenCollectionGroupForm.setReceivedEventReceivedQuality(arr[15]);

	        //CollectedEvent details
	        specimenCollectionGroupForm.setCollectionEventUserId(Long.parseLong(arr[11]));
	        specimenCollectionGroupForm.setCollectionEventdateOfEvent(arr[12]);
	        specimenCollectionGroupForm.setCollectionEventTimeInHours(arr[13]);
	        specimenCollectionGroupForm.setCollectionEventTimeInMinutes(arr[14]);
	        specimenCollectionGroupForm.setCollectionEventCollectionProcedure(arr[16]);
	        specimenCollectionGroupForm.setCollectionEventContainer(arr[17]);

	        //specimenCollectionGroupForm.setNumberOfSpecimens(Integer.parseInt(arr[17]));

	        setRequestPathInfo("/CPQuerySpecimenCollectionGroupAdd");
	        addRequestParameter("operation","add");
	        addRequestParameter("pageOf","pageOfSpecimenCollectionGroupAddCPQuery");
	        setActionForm(specimenCollectionGroupForm);
	        actionPerform();

	    	request.setAttribute("forwardToHashMap",null);
	        setRequestPathInfo("/AnticipatorySpecimenView");
	        addRequestParameter("operation", "add");
	        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
	        addRequestParameter("id",String.valueOf(specimenCollectionGroupForm.getId()));
	        setActionForm(specimenCollectionGroupForm);
	        actionPerform();

	        ViewSpecimenSummaryForm viewSpecimenSummaryForm =new ViewSpecimenSummaryForm();

	        setRequestPathInfo("/GenericSpecimenSummary");
			addRequestParameter("target","anticipatory");
			addRequestParameter("submitAction","updateSpecimenStatus");
			setActionForm(viewSpecimenSummaryForm);
			actionPerform();



		//	verifyForward("success");
			verifyNoActionErrors();
		    verifyActionMessages(new String[]{"object.add.successOnly"});




	}



}
