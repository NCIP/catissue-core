package edu.wustl.catissuecore.smoketest.biospecimen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.action.UpdateBulkSpecimensAction;
import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.actionForm.CPSearchForm;
import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.actionForm.SpecimenForm;
import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenDataBean;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class SpecimenTestCases extends CaTissueSuiteSmokeBaseTest{

	public SpecimenTestCases(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public SpecimenTestCases(String name)
	{
		super(name);
	}
	public SpecimenTestCases()
	{
		super();
	}
	/**
	 * Test Specimen Add.
	 */

	public void testSpecimenAdd()									//only covers the ClassName:Tissue,Fluid,Cell
	{
		String[] inputData = getDataObject().getValues();
		NewSpecimenForm newSpecimenForm = new NewSpecimenForm() ;

		newSpecimenForm.setLabel(inputData[0]);
		newSpecimenForm.setBarcode(inputData[1]);
		newSpecimenForm.setSpecimenCollectionGroupId(inputData[2]) ;

		newSpecimenForm.setParentPresent(Boolean.parseBoolean(inputData[3]));
		newSpecimenForm.setClassName(inputData[4]);
		newSpecimenForm.setType(inputData[5]);
		newSpecimenForm.setTissueSite(inputData[6]);
		newSpecimenForm.setTissueSide(inputData[7]);

		newSpecimenForm.setPathologicalStatus(inputData[8]);
		newSpecimenForm.setQuantity(inputData[9]) ;

		//CollectedEvent Details
		newSpecimenForm.setCollectionEventUserId(Long.parseLong(inputData[10]));
		newSpecimenForm.setCollectionEventdateOfEvent(inputData[11]);
		newSpecimenForm.setCollectionEventTimeInHours(inputData[12]) ;
		newSpecimenForm.setCollectionEventTimeInMinutes(inputData[13]) ;
		newSpecimenForm.setCollectionEventCollectionProcedure(inputData[14]);
		newSpecimenForm.setCollectionEventContainer(inputData[15]) ;

		//RecievedEvent Details
		newSpecimenForm.setReceivedEventUserId(Long.parseLong(inputData[10])) ;
		newSpecimenForm.setReceivedEventDateOfEvent(inputData[11]);
		newSpecimenForm.setReceivedEventTimeInHours(inputData[12]) ;
		newSpecimenForm.setReceivedEventTimeInMinutes(inputData[13]) ;
		newSpecimenForm.setReceivedEventReceivedQuality(inputData[16]);

		newSpecimenForm.setBiohazardName(inputData[17]);
		newSpecimenForm.setBiohazardType(inputData[18]);
		//newSpecimenForm.setConcentration(inputData[19]);

		setRequestPathInfo("/CPQueryNewSpecimenAdd");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfNewSpecimenCPQuery");
		setActionForm(newSpecimenForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});

   	}

	public void testSpecimenEdit()
	{
		String[] arr = getDataObject().getValues();

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();

		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("aliasName", "Specimen");
		addRequestParameter("pageOf", "pageOfNewSpecimen" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Specimen");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "Specimen."+arr[0]+".varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", arr[1]);
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[2]);


		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "Specimen");
		addRequestParameter("pageOf", "pageOfNewSpecimen" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();


		setRequestPathInfo("/SearchObject");
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		addRequestParameter("operation", "search");
		addRequestParameter("id", arr[2]);
		actionPerform();


		setRequestPathInfo("/NewSpecimenSearch");
		addRequestParameter("operation", "search");
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		actionPerform();

		NewSpecimenForm newSpecimenForm= (NewSpecimenForm) getActionForm() ;


		setRequestPathInfo("/NewSpecimen");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		setActionForm(newSpecimenForm);
		actionPerform();


		newSpecimenForm.setLabel(arr[3]);
		newSpecimenForm.setBarcode(arr[4]);
		newSpecimenForm.setSpecimenCollectionGroupId(arr[5]) ;

		newSpecimenForm.setParentPresent(Boolean.parseBoolean(arr[6]));
		newSpecimenForm.setClassName(arr[7]);
		newSpecimenForm.setType(arr[8]);
		newSpecimenForm.setTissueSite(arr[9]);
		newSpecimenForm.setTissueSide(arr[10]);

		newSpecimenForm.setPathologicalStatus(arr[11]);
		newSpecimenForm.setQuantity(arr[12]) ;

		newSpecimenForm.setCollectionEventUserId(Long.parseLong(arr[13]));
		newSpecimenForm.setCollectionEventdateOfEvent(arr[14]);
		newSpecimenForm.setCollectionEventTimeInHours(arr[15]) ;
		newSpecimenForm.setCollectionEventTimeInMinutes(arr[16]) ;
		newSpecimenForm.setCollectionEventCollectionProcedure(arr[17]);
		newSpecimenForm.setCollectionEventContainer(arr[18]) ;


		newSpecimenForm.setReceivedEventUserId(Long.parseLong(arr[13])) ;
		newSpecimenForm.setReceivedEventDateOfEvent(arr[14]);
		newSpecimenForm.setReceivedEventTimeInHours(arr[15]) ;
		newSpecimenForm.setReceivedEventTimeInMinutes(arr[16]) ;
		newSpecimenForm.setReceivedEventReceivedQuality(arr[19]);

		newSpecimenForm.setBiohazardName(arr[20]);
		newSpecimenForm.setBiohazardType(arr[21]);



		setRequestPathInfo("/NewSpecimenEdit");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		setActionForm(newSpecimenForm);
		actionPerform();

		verifyForward("success");
		verifyActionMessages(new String[]{"object.edit.successOnly"});

	}

	/* Test Case for Specimen Derivative */

	public void testSpecimenAddDerivative()
	{
		String[] InputData = getDataObject().getValues();
		CreateSpecimenForm createSpecimenForm = new CreateSpecimenForm() ;

		setRequestPathInfo("/CreateSpecimen");
		addRequestParameter("pageOf", "pageOfDeriveSpecimen");
		addRequestParameter("operation", "add");
		setActionForm(createSpecimenForm);
		actionPerform();

		createSpecimenForm= (CreateSpecimenForm) getActionForm() ;

		createSpecimenForm.setParentSpecimenLabel(InputData[0]);
		createSpecimenForm.setLabel(InputData[3]);
		createSpecimenForm.setQuantity(InputData[4]);
		createSpecimenForm.setCreatedDate(InputData[5]);
		createSpecimenForm.setVirtuallyLocated(Boolean.parseBoolean(InputData[6]));

		Map<String,String> map = new HashMap<String,String>();
		map.put("ExternalIdentifier:"+InputData[8]+"_name",InputData[9]);
		map.put("ExternalIdentifier:"+InputData[8]+"_value",InputData[10]);

		createSpecimenForm.setExternalIdentifier(map);

		setRequestPathInfo("/AddSpecimen");
		addRequestParameter("operation","add");
		setActionForm(createSpecimenForm);
		actionPerform();

		createSpecimenForm = (CreateSpecimenForm)getActionForm();
		setRequestPathInfo("/CreateSpecimenAdd");
		addRequestParameter("operation", "add");

		createSpecimenForm.setClassName(InputData[1]);
		createSpecimenForm.setType(InputData[2]);
		createSpecimenForm.setComments(InputData[7]);
		setActionForm(createSpecimenForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

	}

	public void testSpecimenAddAliquot(){
		String[] arr = getDataObject().getValues();

		AliquotForm aliquotForm= new AliquotForm();
		setRequestPathInfo("/Aliquots");
		addRequestParameter("pageOf", "pageOfAliquot" );
		setActionForm(aliquotForm);
		actionPerform();
		verifyTilesForward("pageOfCreateAliquot",".catissuecore.aliquotDef");

		aliquotForm.setSpecimenLabel(arr[0]);
		aliquotForm.setNoOfAliquots(arr[1]);

		setRequestPathInfo("/CreateAliquots");
		addRequestParameter("pageOf", "pageOfCreateAliquot" );
		addRequestParameter("operation", "add");
		addRequestParameter("buttonClicked", "submit");
		setActionForm(aliquotForm);
		actionPerform();
		verifyTilesForward("pageOfCreateAliquot",".catissuecore.aliquotDef");

		aliquotForm=(AliquotForm)getActionForm();
		aliquotForm.setCreatedDate(arr[2]);
		aliquotForm.setValue("Specimen:1_StorageContainer_id",arr[3]);
		aliquotForm.setValue("Specimen:1_positionDimensionOne",arr[4]);
		aliquotForm.setValue("Specimen:1_positionDimensionTwo",arr[5]);
		setRequestPathInfo("/CreateAliquots");
		addRequestParameter("pageOf", "pageOfCreateAliquot" );
		addRequestParameter("operation", "add");
		addRequestParameter("menuSelected","15");
		addRequestParameter("buttonClicked","create");
		setActionForm(aliquotForm);
		actionPerform();
		verifyForward("commonAddEdit");

		setRequestPathInfo("/AliquotAdd");
		addRequestParameter("pageOf", "pageOfAliquotSummary" );
		addRequestParameter("operation", "add");
		addRequestParameter("menuSelected","15");
		addRequestParameter("buttonClicked","none");
		setActionForm(aliquotForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		}


	public void testAddWithMultipleSpecimen()
	{
		String[] arr = getDataObject().getValues();

		setRequestPathInfo("/MultipleSpecimenFlexInitAction");
		addRequestParameter("pageOf","pageOfMultipleSpWithMenu");
		addRequestParameter("mode","add");
		actionPerform();

		ViewSpecimenSummaryForm viewSpecimenSummaryForm = new ViewSpecimenSummaryForm();

		Map<String,GenericSpecimen> list = new LinkedHashMap<String,GenericSpecimen>();
		SpecimenDataBean[] sdb = new SpecimenDataBean[2];
		sdb[0] = new SpecimenDataBean();
		sdb[1] = new SpecimenDataBean();

		sdb[0].setClassName(arr[0]);
		sdb[1].setClassName(arr[0]);
		sdb[0].setType(arr[1]);
		sdb[1].setType(arr[1]);
		sdb[0].setTissueSide(arr[1]);
		sdb[1].setTissueSide(arr[1]);
		sdb[0].setTissueSite(arr[1]);
		sdb[1].setTissueSite(arr[1]);
		sdb[0].setPathologicalStatus(arr[1]);
		sdb[1].setPathologicalStatus(arr[1]);
		sdb[0].setQuantity(arr[2]);
		sdb[1].setQuantity(arr[2]);
		sdb[0].setSelectedContainerName(arr[3]);
		sdb[0].setPositionDimensionOne(arr[4]);
		sdb[0].setPositionDimensionTwo(arr[4]);
		sdb[0].setContainerId(arr[5]);
		sdb[1].setSelectedContainerName(arr[3]);
		sdb[1].setPositionDimensionOne(arr[4]);
		sdb[1].setPositionDimensionTwo(arr[6]);
		sdb[1].setContainerId(arr[5]);

		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setName(arr[7]);

		sdb[0].setSpecimenCollectionGroup(scg);
		sdb[1].setSpecimenCollectionGroup(scg);

		list.put("1",sdb[0]);
		list.put("2",sdb[1]);

		HttpSession ses = request.getSession();
		ses.setAttribute("SpecimenListBean",list);

		setRequestPathInfo("/MultipleSpecimenView");
		addRequestParameter("pageOf","pageOfMultipleSpWithMenu");
		addRequestParameter("mode","add");
		setActionForm(viewSpecimenSummaryForm);
		actionPerform();

		setRequestPathInfo("/GenericSpecimenSummary");
		addRequestParameter("submitAction","pageOfbulkUpdateSpecimens");
		addRequestParameter("target","pageOfMultipleSpWithMenu");
		addRequestParameter("userAction","ADD");
		setActionForm(viewSpecimenSummaryForm);
		actionPerform();

		setRequestPathInfo("/bulkUpdateSpecimens");
		addRequestParameter("pageOf", "multipleSpWithMenu");
		setActionForm(viewSpecimenSummaryForm);
		actionPerform();

		//verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});


	}

	public void testSpecimenAddWithExternalIdentifier()									//only covers the ClassName:Tissue,Fluid,Cell
	{
		String[] inputData = getDataObject().getValues();
		NewSpecimenForm newSpecimenForm = new NewSpecimenForm() ;

		newSpecimenForm.setLabel(inputData[0]);
		newSpecimenForm.setBarcode(inputData[1]);
		newSpecimenForm.setSpecimenCollectionGroupId(inputData[2]) ;

		newSpecimenForm.setParentPresent(Boolean.parseBoolean(inputData[3]));
		newSpecimenForm.setClassName(inputData[4]);
		newSpecimenForm.setType(inputData[5]);
		newSpecimenForm.setTissueSite(inputData[6]);
		newSpecimenForm.setTissueSide(inputData[7]);

		newSpecimenForm.setPathologicalStatus(inputData[8]);
		newSpecimenForm.setQuantity(inputData[9]) ;

		//CollectedEvent Details
		newSpecimenForm.setCollectionEventUserId(Long.parseLong(inputData[10]));
		newSpecimenForm.setCollectionEventdateOfEvent(inputData[11]);
		newSpecimenForm.setCollectionEventTimeInHours(inputData[12]) ;
		newSpecimenForm.setCollectionEventTimeInMinutes(inputData[13]) ;
		newSpecimenForm.setCollectionEventCollectionProcedure(inputData[14]);
		newSpecimenForm.setCollectionEventContainer(inputData[15]) ;

		//RecievedEvent Details
		newSpecimenForm.setReceivedEventUserId(Long.parseLong(inputData[10])) ;
		newSpecimenForm.setReceivedEventDateOfEvent(inputData[11]);
		newSpecimenForm.setReceivedEventTimeInHours(inputData[12]) ;
		newSpecimenForm.setReceivedEventTimeInMinutes(inputData[13]) ;
		newSpecimenForm.setReceivedEventReceivedQuality(inputData[16]);

		newSpecimenForm.setBiohazardName(inputData[17]);
		newSpecimenForm.setBiohazardType(inputData[18]);
		//newSpecimenForm.setConcentration(inputData[19]);

		Map<String,String> externalIdentifier = new LinkedHashMap<String,String>();
		externalIdentifier.put("ExternalIdentifier:1_name",inputData[19]);
		externalIdentifier.put("ExternalIdentifier:1_value",inputData[20]);
		newSpecimenForm.setExternalIdentifier(externalIdentifier);


		setRequestPathInfo("/CPQueryNewSpecimenAdd");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfNewSpecimenCPQuery");
		setActionForm(newSpecimenForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});

   	}

	public void testSpecimenAddDerivativeCPBasedView(){

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

        setRequestPathInfo("/QueryParticipantSearch");
        addRequestParameter("id", InputData[0]);//886//1001
        addRequestParameter("operation", "edit");
        addRequestParameter("pageOf", "pageOfParticipantCPQueryEdit");
        addRequestParameter("cpSearchCpId", InputData[1]);//341
        setActionForm(participantForm);
        actionPerform();

        SpecimenCollectionGroupForm specimenCollectionGroupForm = new SpecimenCollectionGroupForm();

        setRequestPathInfo("/QuerySpecimenCollectionGroupSearch");
        addRequestParameter("id",InputData[2]);//967//1121
        addRequestParameter("cpSearchParticipantId",InputData[0]);
        addRequestParameter("cpSearchCpId",InputData[1]);
        addRequestParameter("operation", "edit");
        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQueryEdit");
        setActionForm(specimenCollectionGroupForm);
        actionPerform();


        NewSpecimenForm newSpecimenForm= new NewSpecimenForm();
        setRequestPathInfo("/QuerySpecimenSearch");
        addRequestParameter("id",InputData[3]);//4161
        addRequestParameter("operation", "edit");
        addRequestParameter("cpSearchParticipantId",InputData[0]);
        addRequestParameter("cpSearchCpId",InputData[1]);
        addRequestParameter("pageOf", "pageOfNewSpecimenCPQuery");
        setActionForm(newSpecimenForm);
        actionPerform();

          newSpecimenForm= (NewSpecimenForm)getActionForm();
        newSpecimenForm.setDerivedClicked(Boolean.parseBoolean("true"));
        newSpecimenForm.setNumberOfSpecimens(InputData[13]);

		setRequestPathInfo("/CPQueryNewSpecimenEdit");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfNewSpecimenCPQuery");
		setActionForm(newSpecimenForm);
        actionPerform();

        CreateSpecimenForm createSpecimenForm=new CreateSpecimenForm();

        Map<String,Object> map1 =  new LinkedHashMap<String,Object>();

        map1.put("parentSpecimenId",Long.parseLong(InputData[3]));//4161
        map1.put("specimenCollectionGroupName", InputData[4]);//ACHD_1001_1093
        map1.put("LABEL",InputData[5]);//1358
        map1.put("generateLabel",Boolean.parseBoolean(InputData[6]));

        request.setAttribute("forwardToHashMap",map1);

        setRequestPathInfo("/CPQueryCreateSpecimen");
        addRequestParameter("operation","add");
        addRequestParameter("pageOf","pageOfCreateSpecimenCPQuery");
        addRequestParameter("menuSelected","15");
        setActionForm(createSpecimenForm);
        actionPerform();


        HttpSession sess = request.getSession();
        sess.setAttribute("pageOf",null);

        setRequestPathInfo("/CPQueryAddSpecimen");
        addRequestParameter("quantity",InputData[7]);
        addRequestParameter("label",InputData[8]);
        addRequestParameter("parentSpecimenLabel",InputData[5]);
        addRequestParameter("className",InputData[9]);
        addRequestParameter("type",InputData[10]);
        addRequestParameter("storageContainer",InputData[11]);
        addRequestParameter("id",InputData[12]);
		addRequestParameter("operation","add");
		setActionForm(createSpecimenForm);
        actionPerform();

        setRequestPathInfo("/CPQueryCreateSpecimenAdd");
        addRequestParameter("operation","add");
        setActionForm(createSpecimenForm);
        actionPerform();
        verifyForward("success");
	}

	public void testSpecimenAddAliquoteCPBasedView()
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

        setRequestPathInfo("/QueryParticipantSearch");
        addRequestParameter("id", InputData[0]);//886//1001
        addRequestParameter("operation", "edit");
        addRequestParameter("pageOf", "pageOfParticipantCPQueryEdit");
        addRequestParameter("cpSearchCpId", InputData[1]);//341
        setActionForm(participantForm);
        actionPerform();

        SpecimenCollectionGroupForm specimenCollectionGroupForm = new SpecimenCollectionGroupForm();

        setRequestPathInfo("/QuerySpecimenCollectionGroupSearch");
        addRequestParameter("id",InputData[2]);//967//1121
        addRequestParameter("cpSearchParticipantId",InputData[0]);
        addRequestParameter("cpSearchCpId",InputData[1]);
        addRequestParameter("operation", "edit");
        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQueryEdit");
        setActionForm(specimenCollectionGroupForm);
        actionPerform();


        NewSpecimenForm newSpecimenForm= new NewSpecimenForm();
        setRequestPathInfo("/QuerySpecimenSearch");
        addRequestParameter("id",InputData[3]);//4161
        addRequestParameter("operation", "edit");
        addRequestParameter("cpSearchParticipantId",InputData[0]);
        addRequestParameter("cpSearchCpId",InputData[1]);
        addRequestParameter("pageOf", "pageOfNewSpecimenCPQuery");
        setActionForm(newSpecimenForm);
        actionPerform();

        newSpecimenForm= (NewSpecimenForm)getActionForm();
        newSpecimenForm.setNoOfAliquots("1");
        newSpecimenForm.setQuantityPerAliquot("1");

        setRequestPathInfo("/CPQueryNewSpecimenEdit");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfNewSpecimenCPQuery");
		setActionForm(newSpecimenForm);
        actionPerform();

        AliquotForm aliquotForm= new AliquotForm();

        setRequestPathInfo("/CPQueryCreateAliquots");
        addRequestParameter("operation","add");
        addRequestParameter("buttonClicked","submit");
        addRequestParameter("pageOf", "pageOfCreateAliquot");
        addRequestParameter("menuSelected","15");
        addRequestParameter("CPQuery","CPQuery");
        addRequestParameter("parentSpecimenId","-1");

        addRequestParameter("barcode",newSpecimenForm.getBarcode());
        addRequestParameter("className",newSpecimenForm.getClassName());
        addRequestParameter("noOfAliquots",newSpecimenForm.getNoOfAliquots());
        addRequestParameter("quantityPerAliquot",newSpecimenForm.getQuantityPerAliquot());
        addRequestParameter("nextForwardTo","");
        addRequestParameter("pathologicalStatus",newSpecimenForm.getPathologicalStatus());
        addRequestParameter("submittedFor","ForwardTo");
        addRequestParameter("tissueSide",newSpecimenForm.getTissueSide());
        addRequestParameter("tissueSite",newSpecimenForm.getTissueSite());
        addRequestParameter("type",newSpecimenForm.getType());
        addRequestParameter("spCollectionGroupId", InputData[2]);
        addRequestParameter("specimenID", InputData[3]);
        addRequestParameter("colProtId", InputData[1]);
        addRequestParameter("specimenLabel", newSpecimenForm.getLabel());
        addRequestParameter("checkedButton", "1");
        setActionForm(aliquotForm);
        actionPerform();

        setRequestPathInfo("/CPQueryCreateAliquots");
        addRequestParameter("operation","add");
        addRequestParameter("buttonClicked","create");
		addRequestParameter("pageOf", "pageOfCreateAliquot");
        setActionForm(aliquotForm);
        actionPerform();

        setRequestPathInfo("/CPQueryAliquotAdd");
        addRequestParameter("operation","add");
        addRequestParameter("buttonClicked","none");
		addRequestParameter("pageOf", "pageOfAliquotSummary");
		addRequestParameter("menuSelected","15");
		addRequestParameter("CPQuery","true");
        setActionForm(aliquotForm);
        actionPerform();
        verifyForward("success");
	}




}
