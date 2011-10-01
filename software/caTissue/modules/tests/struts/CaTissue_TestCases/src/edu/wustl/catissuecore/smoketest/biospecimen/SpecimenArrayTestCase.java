package edu.wustl.catissuecore.smoketest.biospecimen;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.actionForm.SpecimenArrayAliquotForm;
import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class SpecimenArrayTestCase extends CaTissueSuiteSmokeBaseTest {

	public SpecimenArrayTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public SpecimenArrayTestCase(String name)
	{
		super(name);
	}
	public SpecimenArrayTestCase()
	{
		super();
	}

	public void testSpecimenArrayAdd(){
		String[] arr = getDataObject().getValues();

		SpecimenArrayForm specimenArrayForm=new SpecimenArrayForm();
		setRequestPathInfo("/SpecimenArray");
		addRequestParameter("pageOf", "pageOfSpecimenArray" );
		addRequestParameter("operation","add");
		setActionForm(specimenArrayForm);
		actionPerform();

		specimenArrayForm.setSpecimenArrayTypeId(Long.parseLong(arr[0]));
		specimenArrayForm.setStorageContainer(arr[1]);
		specimenArrayForm.setPositionDimensionOne(Integer.parseInt(arr[2]));
		specimenArrayForm.setPositionDimensionTwo(Integer.parseInt(arr[3]));
		specimenArrayForm.setEnterSpecimenBy(arr[4]);
		specimenArrayForm.setSubOperation("ChangeArraytype");

		setRequestPathInfo("/SpecimenArray");
		setActionForm(specimenArrayForm);
		actionPerform();

		Map<String,String> arrayContentMap= new HashMap<String,String>();
		arrayContentMap.put("SpecimenArrayContent:"+arr[5]+"_Specimen_label", arr[6]);
		arrayContentMap.put("SpecimenArrayContent:"+arr[5]+"_positionDimensionOne",arr[7]);
		arrayContentMap.put("SpecimenArrayContent:"+arr[5]+"_positionDimensionTwo",arr[8]);

		final HttpSession session = request.getSession();
		session.setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY, arrayContentMap);

		setRequestPathInfo("/SpecimenArrayAdd");
		addRequestParameter("menuSelected", "20" );
		addRequestParameter("operation","add");
		setActionForm(specimenArrayForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

	}
	public void testSpecimenArrayEdit()
	{
		String[] arr = getDataObject().getValues();

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();

		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("aliasName", "SpecimenArray");
		addRequestParameter("pageOf", "pageOfSpecimenArray" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "SpecimenArray");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "SpecimenArray."+arr[0]+".varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", arr[1]);
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[2]);


		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "SpecimenArray");
		addRequestParameter("pageOf", "pageOfSpecimenArray" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();


		setRequestPathInfo("/SearchObject");
		addRequestParameter("pageOf", "pageOfSpecimenArray");
		addRequestParameter("operation", "search");
		addRequestParameter("id", arr[2]);
		actionPerform();

		setRequestPathInfo("/SpecimenArraySearch");
		addRequestParameter("operation", "search");
		addRequestParameter("pageOf", "pageOfSpecimenArray");
		actionPerform();

		SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm)getActionForm();

		setRequestPathInfo("/SpecimenArray");
		addRequestParameter("menuSelected", "20");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfSpecimenArray");
		setActionForm(specimenArrayForm);
		actionPerform();

		specimenArrayForm.setName(arr[3]);
		specimenArrayForm.setStorageContainer(arr[4]);
		specimenArrayForm.setPositionDimensionOne(Integer.parseInt(arr[5]));
		specimenArrayForm.setPositionDimensionTwo(Integer.parseInt(arr[6]));
		specimenArrayForm.setEnterSpecimenBy(arr[11]);
		specimenArrayForm.setSubOperation("ChangeArraytype");


		Map<String,String> arrayContentMap= new HashMap<String,String>();
		arrayContentMap.put("SpecimenArrayContent:"+arr[7]+"_Specimen_label", arr[8]);
		arrayContentMap.put("SpecimenArrayContent:"+arr[7]+"_positionDimensionOne",arr[9]);
		arrayContentMap.put("SpecimenArrayContent:"+arr[7]+"_positionDimensionTwo",arr[10]);

		final HttpSession session = request.getSession();
		session.setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY, arrayContentMap);

		setRequestPathInfo("/SpecimenArrayEdit");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfSpecimenArray");
		setActionForm(specimenArrayForm);
		actionPerform();

		verifyForward("success");
		verifyActionMessages(new String[]{"object.edit.successOnly"});

	}

	public void testSpecimenArrAddAliquot(){
		String[] arr = getDataObject().getValues();

		SpecimenArrayAliquotForm specimenArrayAliquotForm= new SpecimenArrayAliquotForm();
		setRequestPathInfo("/SpecimenArrayAliquots");
		addRequestParameter("pageOf", "pageOfSpecimenArrayAliquot" );
		setActionForm(specimenArrayAliquotForm);
		actionPerform();
		verifyTilesForward("pageOfSpecimenArrayAliquot",".catissuecore.specimenArrayAliquotDef");

		specimenArrayAliquotForm.setParentSpecimenArrayLabel(arr[0]);
		specimenArrayAliquotForm.setAliquotCount(arr[1]);

		setRequestPathInfo("/SpecimenArrayCreateAliquots");
		addRequestParameter("pageOf", "pageOfSpecimenArrayCreateAliquot" );
		addRequestParameter("operation", "add");
		addRequestParameter("buttonClicked", "submit");
		setActionForm(specimenArrayAliquotForm);
		actionPerform();
		verifyTilesForward("pageOfSpecimenArrayCreateAliquot",".catissuecore.specimenArrayAliquotDef");

		specimenArrayAliquotForm=(SpecimenArrayAliquotForm)getActionForm();
		//specimenArrayAliquotForm.setCreatedDate(arr[2]);
		specimenArrayAliquotForm.setValue("SpecimenArray:1_StorageContainer_id",arr[2]);
		specimenArrayAliquotForm.setValue("SpecimenArray:1_positionDimensionOne",arr[3]);
		specimenArrayAliquotForm.setValue("SpecimenArray:1_positionDimensionTwo",arr[4]);

		setRequestPathInfo("/SpecimenArrayCreateAliquots");
		addRequestParameter("pageOf", "pageOfSpecimenArrayCreateAliquot" );
		addRequestParameter("operation", "add");
		addRequestParameter("menuSelected","20");
		addRequestParameter("buttonClicked","create");
		setActionForm(specimenArrayAliquotForm);
		actionPerform();
		verifyForward("commonAddEdit");

		setRequestPathInfo("/SpecimenArrayAliquotsAdd");
		addRequestParameter("pageOf", "pageOfSpecimenArrayAliquotSummary" );
		addRequestParameter("operation", "add");
		addRequestParameter("menuSelected","20");
		addRequestParameter("buttonClicked","none");
		setActionForm(specimenArrayAliquotForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		}
}
