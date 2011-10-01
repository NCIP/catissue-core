package edu.wustl.catissuecore.smoketest.admin;

import java.io.File;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.struts.upload.FormFile;

import edu.wustl.catissuecore.actionForm.CPSearchForm;
import edu.wustl.catissuecore.actionForm.DisplaySOPEventForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.actionForm.SOPForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.actionForm.UtilizeSopForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class SpecimenProcessingProcedureTestCase extends CaTissueSuiteSmokeBaseTest
{

	public SpecimenProcessingProcedureTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public SpecimenProcessingProcedureTestCase(String name)
	{
		super(name);
	}
	public SpecimenProcessingProcedureTestCase()
	{
		super();
	}

	//Test Case to add Specimen Processing Procedure

	public void testAddSPP()
	{
		String[] arr = getDataObject().getValues();

		SOPForm sopForm = new SOPForm();

		setRequestPathInfo("/SOP");
		addRequestParameter("pageOf","pageOfSOP");
		addRequestParameter("operation","add");
		setActionForm(sopForm);
		actionPerform();

		sopForm.setName(arr[0]);
		

		FormFile formFile;
		FileItem fileItem;
		File fname;
		fname = new File(arr[1]);
		fileItem=   new DiskFileItem(arr[2],arr[3], Boolean.parseBoolean(arr[4]),arr[5],Integer.parseInt(arr[6]),fname);


		CommonForm commonForm = new CommonForm(fileItem,arr[7]);
	    formFile =   (FormFile) commonForm;
		sopForm.setXmlFileName(formFile);
		sopForm.setBarcode(arr[8]);

		setRequestPathInfo("/SOPCreate");
		addRequestParameter("operation","add");
		setActionForm(sopForm);
		actionPerform();

		verifyActionMessages(new String[]{"object.add.successOnly"});

	}
	
	//Test Case to add Specimen Processing Procedure with defaults

	public void testAddSPPWithDefault()
	{
		addRequestParameter(Constants.SET_DEFAULT_VALUE,"true");
		testAddSPP();		
	}
	
	public void testAddSPPBarCodeNull()
	{
		testAddSPP();
	}

	//Test Case to edit Specimen Processing Procedure

	public void testEditSPP()
	{
		String[] arr = getDataObject().getValues();

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();

		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("aliasName", "SpecimenProcessingProcedure");
		addRequestParameter("pageOf", "pageOfSOP" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "SpecimenProcessingProcedure");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "SpecimenProcessingProcedure."+arr[0]+".bigint");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", arr[1]);
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[2]);


		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "SpecimenProcessingProcedure");
		addRequestParameter("pageOf", "pageOfSOP" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();


		setRequestPathInfo("/SearchObject");
		addRequestParameter("pageOf", "pageOfSOP");
		addRequestParameter("operation", "search");
		addRequestParameter("id", arr[2]);
		actionPerform();


		setRequestPathInfo("/SOPSearch");
		addRequestParameter("operation", "search");
		addRequestParameter("pageOf", "pageOfSOP");
		actionPerform();

		SOPForm sopForm = (SOPForm) getActionForm() ;


		setRequestPathInfo("/SOP");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfSOP");
		setActionForm(sopForm);
		actionPerform();

		sopForm.setName(arr[3]);
		FormFile formFile;
		FileItem fileItem;
		File fname;
		fname = new File(arr[4]);
		fileItem=   new DiskFileItem(arr[5],arr[6], Boolean.parseBoolean(arr[7]),arr[8],Integer.parseInt(arr[9]),fname);


		CommonForm commonForm = new CommonForm(fileItem,arr[10]);
	    formFile =   (FormFile) commonForm;
		sopForm.setXmlFileName(formFile);

		setRequestPathInfo("/SOPEdit");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfSOP");
		setActionForm(sopForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.edit.successOnly"});
	}

	public void testDisplaySPP()
	{
		    String[] inputData = getDataObject().getValues();

		    CPSearchForm cpSearchForm = new CPSearchForm();
	        ParticipantForm participantForm = new ParticipantForm();
	        NewSpecimenForm newSpecimenForm = new NewSpecimenForm();
	        DisplaySOPEventForm displaySOPEventForm = new DisplaySOPEventForm();
	        SpecimenCollectionGroupForm specimenCollectionGroupForm =new SpecimenCollectionGroupForm();


	        setRequestPathInfo("/CpBaSsedSearch");
	        setActionForm(cpSearchForm);
	        actionPerform();

	        setRequestPathInfo("/showCpAndParticipants");
	        addRequestParameter("pageOf", "pageOfCpQueryResults");
	        setActionForm(cpSearchForm);
	        actionPerform();

	        setRequestPathInfo("/blankScreenAction");
	        actionPerform();

	        setRequestPathInfo("/QueryParticipantSearch");
	        addRequestParameter("id", "1");//886//1001
	        addRequestParameter("operation", "edit");
	        addRequestParameter("pageOf", "pageOfParticipantCPQueryEdit");
	        addRequestParameter("cpSearchCpId", "1");//341
	        setActionForm(participantForm);
	        actionPerform();

	        setRequestPathInfo("/QueryParticipant");
	        addRequestParameter("operation", "edit");
	        addRequestParameter("pageOf", "pageOfParticipantCPQuery");
	        addRequestParameter("refresh", "false");
	        setActionForm(participantForm);
	        actionPerform();

	        setRequestPathInfo("/QuerySpecimenCollectionGroupSearch");
	        addRequestParameter("id","2");
	        addRequestParameter("cpSearchParticipantId","1");
	        addRequestParameter("cpSearchCpId","1");
	        addRequestParameter("operation", "edit");
	        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQueryEdit");
	        setActionForm(specimenCollectionGroupForm);
	        actionPerform();

	        setRequestPathInfo("/QuerySpecimenCollectionGroup");
	        addRequestParameter("operation", "edit");
	        addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
	        addRequestParameter("redirect", "false");
	        setActionForm(specimenCollectionGroupForm);
	        actionPerform();

	        setRequestPathInfo("/QuerySpecimenSearch");
	        addRequestParameter("id", "2");
	        addRequestParameter("operation", "edit");
	        addRequestParameter("pageOf", "pageOfNewSpecimenCPQuery");
	        addRequestParameter("cpSearchParticipantId","1");
	        addRequestParameter("cpSearchCpId","1");
	        addRequestParameter("specimenId","2");
	        setActionForm(newSpecimenForm);
	        actionPerform();

	        setRequestPathInfo("/QuerySpecimen");
	        addRequestParameter("operation", "edit");
	        addRequestParameter("pageOf", "pageOfNewSpecimenCPQuery");
	        setActionForm(newSpecimenForm);
	        actionPerform();

	        setRequestPathInfo("/DisplaySOPEventsAction");
	        addRequestParameter("id", "2");
	        addRequestParameter("operation", "edit");
	        addRequestParameter("pageOf", "pageOfDynamicEvent");
	        setActionForm(displaySOPEventForm);
	        actionPerform();

	      //  verifyForward("success");


	}
	
	public void testApplySppForSpecimen()
	{
		    String[] inputData = getDataObject().getValues();
		    addRequestParameter("sopValue", "1");
		    addRequestParameter("operation", "specimen");
			addRequestParameter("pageOf","pageOfSOP");
		    setRequestPathInfo("/UtilizeSop");
		    UtilizeSopForm utilizeSopForm = new UtilizeSopForm();
		    setActionForm(utilizeSopForm);
		    actionPerform();
	}
	
	public void testApplySppForScg()
	{
		    String[] inputData = getDataObject().getValues();
		    addRequestParameter("sopValue", "1");
		    addRequestParameter("operation", "scg");
			addRequestParameter("pageOf","pageOfSOP");
		    setRequestPathInfo("/UtilizeSop");
		    UtilizeSopForm utilizeSopForm = new UtilizeSopForm();
		    setActionForm(utilizeSopForm);
		    actionPerform();
	}
	
}
