package edu.wustl.catissuecore.smoketest.biospecimen;

import edu.common.dynamicextensions.util.global.Variables;
import edu.wustl.catissuecore.actionForm.DisplaySPPEventForm;
import edu.wustl.catissuecore.actionForm.DynamicEventForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.QuickEventsForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class SPPDataEntryTestCase extends CaTissueSuiteSmokeBaseTest {

	public SPPDataEntryTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public SPPDataEntryTestCase(String name)
	{
		super(name);
	}
	public SPPDataEntryTestCase()
	{
		super();
	}

	/**
	 * Test case to add SPP data entry at Specimen level.
	 */

	public void testSPPDataEntryAddForSpecimen()
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

		DisplaySPPEventForm displaySPPEventForm=new DisplaySPPEventForm();
		displaySPPEventForm.setSpecimenId(arr[2]);
		displaySPPEventForm.setOperation("search");

		setRequestPathInfo("/DisplaySPPEventsAction");
		addRequestParameter("operation", "search");
		addRequestParameter(Constants.SPECIMEN_ID, arr[2]);
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		setActionForm(displaySPPEventForm);
		actionPerform();

		DynamicEventForm dynamicEventForm=new DynamicEventForm();

		setRequestPathInfo("/DynamicEvent");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf","pageOfDynamicEvent");
		addRequestParameter("showDefaultValues", "true");
		addRequestParameter("eventName", "Spun Event Parameters");
		setActionForm(dynamicEventForm);
		actionPerform();

		clearRequestParameters();
		displaySPPEventForm=new DisplaySPPEventForm();
		displaySPPEventForm.setSppName(null);
		setRequestPathInfo("/SaveSPPEventAction");
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		addRequestParameter("operation", "insertDEData");
		addRequestParameter("specimenId", arr[2]);
		addRequestParameter(arr[3]+"!@!operation",new String[]{"edit"});
		addRequestParameter(arr[3]+"!@!specimenId", new String[]{arr[2]});
		addRequestParameter(arr[3]+"!@!dateOfEvent", new String[]{arr[4]});
		addRequestParameter(arr[3]+"!@!displaytimeInHours",new String[]{ arr[5]});
		addRequestParameter(arr[3]+"!@!timeInHours", new String[]{arr[6]});
		addRequestParameter(arr[3]+"!@!displaytimeInMinutes",new String[]{ arr[7]});
		addRequestParameter(arr[3]+"!@!timeInMinutes", new String[]{arr[8]});
		addRequestParameter(arr[3]+"!@!userId", new String[]{arr[9]});
		addRequestParameter(arr[3]+"!@!calmoisdateOfEvent", new String[]{arr[10]});
		addRequestParameter(arr[3]+"!@!calyeardateOfEvent", new String[]{arr[11]});
		addRequestParameter(arr[3]+"!@!reasonDeviation", new String[]{arr[12]});
		addRequestParameter("Control_127_127_1_1", new String[]{arr[13]});
		addRequestParameter("Control_127_127_2_1", new String[]{arr[14]});
		Variables.jbossUrl=arr[15];
		setActionForm(displaySPPEventForm);
		actionPerform();
	}

	/**
	 * Test case to edit SPP data entry at Specimen level.
	 */


	public void testSPPDataEntryEditForSpecimen()
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

		DisplaySPPEventForm displaySPPEventForm=new DisplaySPPEventForm();
		displaySPPEventForm.setSpecimenId(arr[2]);
		displaySPPEventForm.setOperation("search");

		setRequestPathInfo("/DisplaySPPEventsAction");
		addRequestParameter("operation", "search");
		addRequestParameter(Constants.SPECIMEN_ID, arr[2]);
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		setActionForm(displaySPPEventForm);
		actionPerform();

		DynamicEventForm dynamicEventForm=new DynamicEventForm();

		setRequestPathInfo("/DynamicEvent");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf","pageOfDynamicEvent");
		addRequestParameter("showDefaultValues", "true");
		addRequestParameter("eventName", "Spun Event Parameters");
		setActionForm(dynamicEventForm);
		actionPerform();

		clearRequestParameters();
		setRequestPathInfo("/SaveSPPEventAction");
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		addRequestParameter("operation", "insertDEData");
		addRequestParameter("specimenId", arr[2]);
		addRequestParameter(arr[3]+"!@!operation",new String[]{"edit"});
		addRequestParameter(arr[3]+"!@!id", new String[]{"1"});
		addRequestParameter(arr[3]+"!@!specimenId", new String[]{arr[2]});
		addRequestParameter(arr[3]+"!@!dateOfEvent", new String[]{arr[4]});
		addRequestParameter(arr[3]+"!@!displaytimeInHours",new String[]{ arr[5]});
		addRequestParameter(arr[3]+"!@!timeInHours", new String[]{arr[6]});
		addRequestParameter(arr[3]+"!@!displaytimeInMinutes",new String[]{ arr[7]});
		addRequestParameter(arr[3]+"!@!timeInMinutes", new String[]{arr[8]});
		addRequestParameter(arr[3]+"!@!userId", new String[]{arr[9]});
		addRequestParameter(arr[3]+"!@!calmoisdateOfEvent", new String[]{arr[10]});
		addRequestParameter(arr[3]+"!@!calyeardateOfEvent", new String[]{arr[11]});
		addRequestParameter(arr[3]+"!@!reasonDeviation", new String[]{arr[12]});
		addRequestParameter("Control_127_127_1_1", new String[]{arr[13]});
		addRequestParameter("Control_127_127_2_1", new String[]{arr[14]});
		Variables.jbossUrl=arr[15];
		actionPerform();
	}

	/**
	 * Test case to add SPP data entry at SCG level.
	 */

	public void testSPPDataEntryAddForSpecimenCollectionGroup()
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

        setRequestPathInfo("/DisplaySPPForSCGAction");
        addRequestParameter("id", arr[2]);
        actionPerform();

        setRequestPathInfo("/DisplaySPPEventsForSCG");
        addRequestParameter("id", arr[2]);
        addRequestParameter("sppName", "SPP_With_SpunEvent");
        actionPerform();

        DynamicEventForm dynamicEventForm=new DynamicEventForm();
        dynamicEventForm.setOperation("add");

        setRequestPathInfo("/DynamicEvent");
        addRequestParameter("operation", "add");
        addRequestParameter("showDefaultValues", "true");
        addRequestParameter("pageOf", "pageOfDynamicEvents");
        actionPerform();

        setRequestPathInfo("/SaveSPPEventAction");
        addRequestParameter("id", arr[2]);
        addRequestParameter("pageOf", "pageOfSCG");
    	addRequestParameter(arr[3]+"!@!operation",new String[]{"edit"});
    	addRequestParameter(arr[3]+"!@!dateOfEvent", new String[]{arr[4]});
		addRequestParameter(arr[3]+"!@!displaytimeInHours",new String[]{ arr[5]});
		addRequestParameter(arr[3]+"!@!timeInHours", new String[]{arr[6]});
		addRequestParameter(arr[3]+"!@!displaytimeInMinutes",new String[]{ arr[7]});
		addRequestParameter(arr[3]+"!@!timeInMinutes", new String[]{arr[8]});
		addRequestParameter(arr[3]+"!@!userId", new String[]{arr[9]});
		addRequestParameter(arr[3]+"!@!calmoisdateOfEvent", new String[]{arr[10]});
		addRequestParameter(arr[3]+"!@!calyeardateOfEvent", new String[]{arr[11]});
		addRequestParameter(arr[3]+"!@!reasonDeviation", new String[]{arr[12]});
		addRequestParameter("Control_127_127_1_1", new String[]{arr[13]});
		addRequestParameter("Control_127_127_2_1", new String[]{arr[14]});
		Variables.jbossUrl=arr[15];
        actionPerform();
	}

	/**
	 * Test case to edit SPP data entry at SCG level.
	 */

	public void testSPPDataEntryEditForSpecimenCollectionGroup()
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

        setRequestPathInfo("/DisplaySPPForSCGAction");
        addRequestParameter("id", arr[2]);
        actionPerform();

        setRequestPathInfo("/DisplaySPPEventsForSCG");
        addRequestParameter("id", arr[2]);
        addRequestParameter("sppName", "SPP_With_SpunEvent");
        actionPerform();

        DynamicEventForm dynamicEventForm=new DynamicEventForm();
        dynamicEventForm.setOperation("add");

        setRequestPathInfo("/DynamicEvent");
        addRequestParameter("operation", "add");
        addRequestParameter("showDefaultValues", "true");
        addRequestParameter("pageOf", "pageOfDynamicEvents");
        actionPerform();

        setRequestPathInfo("/SaveSPPEventAction");
        addRequestParameter("id", arr[2]);
        addRequestParameter("pageOf", "pageOfSCG");
    	addRequestParameter(arr[3]+"!@!operation",new String[]{"edit"});
		addRequestParameter(arr[3]+"!@!id", new String[]{"8"});
		addRequestParameter(arr[3]+"!@!dateOfEvent", new String[]{arr[4]});
		addRequestParameter(arr[3]+"!@!displaytimeInHours",new String[]{ arr[5]});
		addRequestParameter(arr[3]+"!@!timeInHours", new String[]{arr[6]});
		addRequestParameter(arr[3]+"!@!displaytimeInMinutes",new String[]{ arr[7]});
		addRequestParameter(arr[3]+"!@!timeInMinutes", new String[]{arr[8]});
		addRequestParameter(arr[3]+"!@!userId", new String[]{arr[9]});
		addRequestParameter(arr[3]+"!@!calmoisdateOfEvent", new String[]{arr[10]});
		addRequestParameter(arr[3]+"!@!calyeardateOfEvent", new String[]{arr[11]});
		addRequestParameter(arr[3]+"!@!reasonDeviation", new String[]{arr[12]});
		addRequestParameter("Control_127_127_1_1", new String[]{arr[13]});
		addRequestParameter("Control_127_127_2_1", new String[]{arr[14]});
		Variables.jbossUrl=arr[15];
        actionPerform();
	}

	/**
	 * Test case to add same event to multiple Specimens based on their labels.
	 */
	public void testBulkSpecimenDataEntry()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		quickEventsForm.setCheckedButton(arr[0]);
		quickEventsForm.setSpecimenLabel(arr[1]);
		quickEventsForm.setSpecimenEventParameter("Spun Event Parameters");
		quickEventsForm.setActivityStatus("Active");
		quickEventsForm.setForwardTo("success");

		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		addRequestParameter("menuSelected", "15");
		actionPerform();

		DynamicEventForm dynamicEventForm= new DynamicEventForm();
		dynamicEventForm.setOperation("add");
		dynamicEventForm.setPageOf("pageOfDynamicEvents");
		dynamicEventForm.setEventName("SPP_With_SpunEvent");
		setActionForm(dynamicEventForm);
		setRequestPathInfo("/DynamicEvent");
		actionPerform();

		setRequestPathInfo("/DynamicEventAdd");
		addRequestParameter(arr[3]+"!@!operation",new String[]{"add"});
		//addRequestParameter(arr[3]+"!@!id", new String[]{arr[2]});
		addRequestParameter(arr[3]+"!@!dateOfEvent", new String[]{arr[4]});
		addRequestParameter(arr[3]+"!@!displaytimeInHours",new String[]{ arr[5]});
		addRequestParameter(arr[3]+"!@!timeInHours", new String[]{arr[6]});
		addRequestParameter(arr[3]+"!@!displaytimeInMinutes",new String[]{ arr[7]});
		addRequestParameter(arr[3]+"!@!timeInMinutes", new String[]{arr[8]});
		addRequestParameter(arr[3]+"!@!userId", new String[]{arr[9]});
		addRequestParameter(arr[3]+"!@!calmoisdateOfEvent", new String[]{arr[10]});
		addRequestParameter(arr[3]+"!@!calyeardateOfEvent", new String[]{arr[11]});
		addRequestParameter(arr[3]+"!@!reasonDeviation", new String[]{arr[12]});
		addRequestParameter("Control_127_127_1_1", new String[]{arr[13]});
		addRequestParameter("Control_127_127_2_1", new String[]{arr[14]});
		addRequestParameter(Constants.SPECIMEN_ID, arr[2]);
		Variables.jbossUrl=arr[15];
		actionPerform();
	}

	/**
	 * Test case to Show SPP drop down at CPE level.
	 */
	public void testDataForSPPDropDown()
	{
		addRequestParameter("limit","15");
		addRequestParameter("start","0");
		addRequestParameter("query","");
		setRequestPathInfo("/SPPData");
		actionPerform();
	}
}
