package edu.wustl.catissuecore.smoketest.admin;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.DistributionProtocolForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class DistributionProtocolTestCases extends CaTissueSuiteSmokeBaseTest
{
	public DistributionProtocolTestCases(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public DistributionProtocolTestCases(String name)
	{
		super(name);
	}
	public DistributionProtocolTestCases()
	{
		super();
	}

	public void testDistributionProtocolAdd(){

		DistributionProtocolForm dpForm = new DistributionProtocolForm();
		String arr[] = getDataObject().getValues();
		dpForm.setTitle(arr[0]);
		dpForm.setShortTitle(arr[1]);
		dpForm.setPrincipalInvestigatorId(Long.parseLong(arr[2]));
		dpForm.setStartDate(arr[3]);
		dpForm.setOperation("add");
		setRequestPathInfo("/InstitutionAdd");
		setActionForm(dpForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
		assertEquals(DistributionProtocolForm.class.getName(),getActionForm().getClass().getName());
	}

	public void testDistributionProtocolEdit()
	{
	String[] arr = getDataObject().getValues();

	SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();
	setRequestPathInfo("/SimpleQueryInterface");
	addRequestParameter("aliasName", "DistributionProtocol");
	addRequestParameter("pageOf", "pageOfDistributionProtocol" );
	setActionForm(simpleQueryInterfaceForm);
	actionPerform();
	verifyForward("pageOfDistributionProtocol");
	verifyTilesForward("pageOfDistributionProtocol",".catissuecore.editSearchPageDef");
	verifyNoActionErrors();


	simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "DistributionProtocol");
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "DistributionProtocol."+arr[0]+".varchar");
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", arr[1]);
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[2]);
	simpleQueryInterfaceForm.setPageOf("pageOfDistributionProtocol");
	setRequestPathInfo("/SimpleSearch");
	setActionForm(simpleQueryInterfaceForm);
	actionPerform();


	setRequestPathInfo("/SpreadsheetView");
	actionPerform();
	verifyTilesForward("pageOfDistributionProtocol",".catissuecore.editSearchResultsDef");


	setRequestPathInfo("/SearchObject");
	addRequestParameter("pageOf", "pageOfDistributionProtocol");
	addRequestParameter("operation", "search");
	addRequestParameter("id", arr[2]);
	actionPerform();

	setRequestPathInfo("/DistributionProtocolSearch");
	addRequestParameter("pageOf", "pageOfDistributionProtocol");
	actionPerform();
	verifyForward("pageOfDistributionProtocol");

	DistributionProtocolForm distributionProtocolForm = (DistributionProtocolForm)getActionForm() ;
	setRequestPathInfo("/DistributionProtocol");
	addRequestParameter("operation", "edit");
	addRequestParameter("pageOf", "pageOfDistributionProtocol");
	addRequestParameter("menuSelected", "10");
	setActionForm(distributionProtocolForm);
	actionPerform();
	verifyForward("pageOfDistributionProtocol");
	verifyTilesForward("pageOfDistributionProtocol",".catissuecore.distributionProtocolDef");


	distributionProtocolForm.setPrincipalInvestigatorId((Long.valueOf(arr[3])));
	distributionProtocolForm.setTitle(arr[4]);
	distributionProtocolForm.setShortTitle(arr[5]);
	distributionProtocolForm.setIrbID(arr[6]);
	distributionProtocolForm.setStartDate(arr[7]);
	distributionProtocolForm.setEnrollment(arr[8]);
	distributionProtocolForm.setDescriptionURL(arr[9]);
	distributionProtocolForm.setActivityStatus(arr[10]);
	Map<String,String> map = new HashMap<String,String>();
	map.put("DistributionSpecimenRequirement:"+arr[11]+"_specimenClass",arr[12]);
	map.put("DistributionSpecimenRequirement:"+arr[11]+"_specimenType",arr[13]);
	map.put("DistributionSpecimenRequirement:"+arr[11]+"_tissueSite",arr[14]);
	map.put("DistributionSpecimenRequirement:"+arr[11]+"_pathologyStatus",arr[15]);
	map.put("DistributionSpecimenRequirement:"+arr[11]+"_quantity",arr[16]);

	distributionProtocolForm.setValues(map);

	distributionProtocolForm.setOperation("edit");
	setRequestPathInfo("/DistributionProtocolEdit");
	setActionForm(distributionProtocolForm);
	actionPerform();
	verifyForward("success");
	verifyActionMessages(new String[]{"object.edit.successOnly"});

	}


	public void testDistributionProtocolAdd1(){

		DistributionProtocolForm distributionProtocolForm = new DistributionProtocolForm();
		String arr[] = getDataObject().getValues();
		distributionProtocolForm.setTitle(arr[0]);
		distributionProtocolForm.setShortTitle(arr[1]);
		distributionProtocolForm.setPrincipalInvestigatorId(Long.parseLong(arr[2]));
		distributionProtocolForm.setStartDate(arr[3]);
		distributionProtocolForm.setIrbID(arr[4]);
		distributionProtocolForm.setEnrollment(arr[5]);
		distributionProtocolForm.setDescriptionURL(arr[6]);
	//	distributionProtocolForm.setActivityStatus(arr[7]);

		Map<String,String> specimenRequirementmap = new HashMap<String,String>();
		specimenRequirementmap.put("DistributionSpecimenRequirement:"+arr[7]+"_specimenClass",arr[8]);
		specimenRequirementmap.put("DistributionSpecimenRequirement:"+arr[7]+"_specimenType",arr[9]);
		specimenRequirementmap.put("DistributionSpecimenRequirement:"+arr[7]+"_tissueSite",arr[10]);
		specimenRequirementmap.put("DistributionSpecimenRequirement:"+arr[7]+"_pathologyStatus",arr[11]);
		specimenRequirementmap.put("DistributionSpecimenRequirement:"+arr[7]+"_quantity",arr[12]);

		distributionProtocolForm.setValues(specimenRequirementmap);

		distributionProtocolForm.setOperation("add");
		setRequestPathInfo("/DistributionProtocolAdd");
		setActionForm(distributionProtocolForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
		//assertEquals(DistributionProtocolForm.class.getName(),getActionForm().getClass().getName());
	}

	public void testDistributionProtocolDisable()
	{
	String[] InputData = getDataObject().getValues();

	SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();
	setRequestPathInfo("/SimpleQueryInterface");
	addRequestParameter("aliasName", "DistributionProtocol");
	addRequestParameter("pageOf", "pageOfDistributionProtocol" );
	setActionForm(simpleQueryInterfaceForm);
	actionPerform();
	verifyForward("pageOfDistributionProtocol");
	verifyTilesForward("pageOfDistributionProtocol",".catissuecore.editSearchPageDef");
	verifyNoActionErrors();


	simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "DistributionProtocol");
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "DistributionProtocol."+InputData[0]+".varchar");
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", InputData[1]);
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",InputData[2]);
	simpleQueryInterfaceForm.setPageOf("pageOfDistributionProtocol");
	setRequestPathInfo("/SimpleSearch");
	setActionForm(simpleQueryInterfaceForm);
	actionPerform();


	setRequestPathInfo("/SpreadsheetView");
	actionPerform();
	verifyTilesForward("pageOfDistributionProtocol",".catissuecore.editSearchResultsDef");


	setRequestPathInfo("/SearchObject");
	addRequestParameter("pageOf", "pageOfDistributionProtocol");
	addRequestParameter("operation", "search");
	addRequestParameter("id", InputData[2]);
	actionPerform();

	setRequestPathInfo("/DistributionProtocolSearch");
	addRequestParameter("pageOf", "pageOfDistributionProtocol");
	actionPerform();
	verifyForward("pageOfDistributionProtocol");

	DistributionProtocolForm distributionProtocolForm = (DistributionProtocolForm)getActionForm() ;
	setRequestPathInfo("/DistributionProtocol");
	addRequestParameter("operation", "edit");
	addRequestParameter("pageOf", "pageOfDistributionProtocol");
	addRequestParameter("menuSelected", "10");
	setActionForm(distributionProtocolForm);
	actionPerform();
	verifyForward("pageOfDistributionProtocol");
	verifyTilesForward("pageOfDistributionProtocol",".catissuecore.distributionProtocolDef");

	distributionProtocolForm.setActivityStatus(InputData[3]);

	distributionProtocolForm.setOnSubmit("/ManageAdministrativeData.do");

	distributionProtocolForm.setOperation("edit");
	setRequestPathInfo("/DistributionProtocolEdit");
	setActionForm(distributionProtocolForm);
	actionPerform();

	setRequestPathInfo("/ManageAdministrativeData");
	actionPerform();

	verifyForward("success");
	verifyActionMessages(new String[]{"object.edit.successOnly"});
	}
}
