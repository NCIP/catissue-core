package edu.wustl.catissuecore.smoketest.biospecimen;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.actionForm.OrderForm;
import edu.wustl.catissuecore.actionForm.OrderSpecimenForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.actionForm.RequestDetailsForm;
import edu.wustl.catissuecore.actionForm.SiteForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.testframework.util.DataObject;
import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.DistributionForm;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;


public class DistributionTestCase extends CaTissueSuiteSmokeBaseTest
{
		public DistributionTestCase(String name, DataObject dataObject)
		{
			super(name,dataObject);
		}
		public DistributionTestCase(String name)
		{
			super(name);
		}
		public DistributionTestCase()
		{
			super();
		}


		public void testDistribution()
		{
			String[] InputData = getDataObject().getValues();

			AdvanceSearchForm advanceSearchFormForm = new AdvanceSearchForm();
			OrderForm orderForm = new OrderForm();
			OrderSpecimenForm orderSpecimenForm = new OrderSpecimenForm();

			setRequestPathInfo("/ViewCart");
			addRequestParameter("operation","requestToDistribute");
			setActionForm(advanceSearchFormForm);
			actionPerform();

			advanceSearchFormForm = (AdvanceSearchForm)getActionForm();

			Map<String,String> map = new HashMap<String,String>();

			map.put("CHK_"+InputData[1],InputData[0]);
			advanceSearchFormForm.setValues(map);

			setRequestPathInfo("/BulkCart");
			addRequestParameter("operation","requestToDistribute");
			setActionForm(advanceSearchFormForm);
			actionPerform();

			setRequestPathInfo("/RequestToDistribute");
			addRequestParameter("operation","requestToDistribute");
			setActionForm(orderForm);
			actionPerform();

			setRequestPathInfo("/SpecDistribute");
			addRequestParameter("operation","requestToDistribute");
			addRequestParameter("typeOf","specimen");
			addRequestParameter("pageOf","specimen");
			addRequestParameter("typeOfSpecimen","existingSpecimen");
			setActionForm(orderSpecimenForm);
			actionPerform();

			orderSpecimenForm = (OrderSpecimenForm)getActionForm();

			Map<String,String> values = new HashMap<String,String>();

			values.put("OrderSpecimenBean:"+InputData[2]+"_specimenId", InputData[3]);
			values.put("OrderSpecimenBean:"+InputData[2]+"_specimenName", InputData[4]);
			values.put("OrderSpecimenBean:"+InputData[2]+"_availableQuantity", InputData[5]);
			values.put("OrderSpecimenBean:"+InputData[2]+"_requestedQuantity", InputData[6]);
			values.put("OrderSpecimenBean:"+InputData[2]+"_specimenClass", InputData[7]);
			values.put("OrderSpecimenBean:"+InputData[2]+"_specimenType", InputData[8]);
			values.put("OrderSpecimenBean:"+InputData[2]+"_isDerived", InputData[9]);
			values.put("OrderSpecimenBean:"+InputData[2]+"_typeOfItem", InputData[10]);
			values.put("OrderSpecimenBean:"+InputData[2]+"_arrayName", InputData[11]);

			orderSpecimenForm.setValues(values);
			orderSpecimenForm.setActivityStatus(InputData[12]);

			setRequestPathInfo("/specToDistribute");
			addRequestParameter("pageOf","specimen");
			addRequestParameter("typeOfSpecimen","existingSpecimen");
			setActionForm(orderSpecimenForm);
			actionPerform();

			setRequestPathInfo("/DirectDistributeSpec");
			addRequestParameter("pageOf","specimen");
			addRequestParameter("typeOfSpecimen","existingSpecimen");
			setActionForm(orderSpecimenForm);
			actionPerform();

			verifyForward("success");
		}
		
		public void testDistributionSpecimenReportWithDefineView()
	{
		String[] InputData = getDataObject().getValues();

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();

		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("aliasName", "Distribution");
		addRequestParameter("pageOf", "pageOfDistribution" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Distribution");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "Distribution."+InputData[0]+".bigint");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", InputData[1]);
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",InputData[2]);

		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "Distribution");
		addRequestParameter("pageOf", "pageOfDistribution" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		setRequestPathInfo("/SearchObject");
		addRequestParameter("pageOf", "pageOfDistribution");
		addRequestParameter("operation", "search");
		addRequestParameter("id", InputData[2]);
		actionPerform();

		DistributionForm distributionForm = new DistributionForm();

		distributionForm.setActivityStatus(InputData[3]);
		distributionForm.setId(Long.parseLong(InputData[4]));
		distributionForm.setUserId(Long.parseLong(InputData[5]));
		distributionForm.setTimeInHours(InputData[6]);
		distributionForm.setTimeInMinutes(InputData[7]);
		distributionForm.setToSite(InputData[8]);
		distributionForm.setDateOfEvent(InputData[9]);
		distributionForm.setDistributionProtocolId(InputData[10]);

		setRequestPathInfo("/DistributionSearchFromMenu");
		addRequestParameter("operation", "search");
		addRequestParameter("pageOf", "pageOfDistribution");
		setActionForm(distributionForm);
		actionPerform();

		ConfigureResultViewForm configureResultViewForm = new ConfigureResultViewForm();

		setRequestPathInfo("/ConfigureDistribution");
		setActionForm(configureResultViewForm);
		actionPerform();

		setRequestPathInfo("/ConfigureResultView");
		addRequestParameter("pageOf", "pageOfDistribution");
		setActionForm(configureResultViewForm);
		actionPerform();

		String columnName[]= new String[1];
		columnName[0]=InputData[11];
		configureResultViewForm.setColumnNames(columnName);

		configureResultViewForm.setDistributionId(Long.parseLong(InputData[12]));

		String columnNames[]= new String[8];
		columnNames[0]=InputData[13];
		columnNames[1]=InputData[14];
		columnNames[2]=InputData[15];
		columnNames[3]=InputData[16];
		columnNames[4]=InputData[17];
		columnNames[5]=InputData[18];
	    columnNames[6]=InputData[19];
	    columnNames[7]=InputData[20];

		configureResultViewForm.setSelectedColumnNames(columnNames);
		configureResultViewForm.setNextAction(InputData[21]);
		configureResultViewForm.setTableName(InputData[22]);

		setRequestPathInfo("/DistributionReport");
		addRequestParameter("pageOf", "pageOfDistribution");
		setActionForm(configureResultViewForm);
		actionPerform();

		setRequestPathInfo("/DistributionReportSave");
		setActionForm(configureResultViewForm);
		actionPerform();

		verifyNoActionErrors();

	}


	public void testDistributionArrayReport()
	{
		String[] InputData = getDataObject().getValues();

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();

		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("aliasName", "Distribution_array");
		addRequestParameter("pageOf", "pageOfArrayDistribution" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Distribution_array");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "Distribution_array."+InputData[0]+".bigint");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", InputData[1]);
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",InputData[2]);

		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "Distribution_array");
		addRequestParameter("pageOf", "pageOfArrayDistribution" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		setRequestPathInfo("/SearchObject");
		addRequestParameter("pageOf", "pageOfArrayDistribution" );
		addRequestParameter("operation", "search");
		addRequestParameter("id", InputData[2]);
		actionPerform();

		DistributionForm distributionForm = new DistributionForm();

		distributionForm.setActivityStatus(InputData[3]);
		distributionForm.setId(Long.parseLong(InputData[4]));
		distributionForm.setUserId(Long.parseLong(InputData[5]));
		distributionForm.setTimeInHours(InputData[6]);
		distributionForm.setTimeInMinutes(InputData[7]);
		distributionForm.setToSite(InputData[8]);
		distributionForm.setDateOfEvent(InputData[9]);
		distributionForm.setDistributionProtocolId(InputData[10]);

		setRequestPathInfo("/DistributionSearchFromMenu");
		addRequestParameter("operation", "search");
		addRequestParameter("pageOf", "pageOfDistribution");
		setActionForm(distributionForm);
		actionPerform();

		ConfigureResultViewForm configureResultViewForm = new ConfigureResultViewForm();

		configureResultViewForm.setDistributionId(Long.parseLong(InputData[11]));

		String columnNames[]= new String[11];
		columnNames[0]=InputData[12];
		columnNames[1]=InputData[13];
		columnNames[2]=InputData[14];
		columnNames[3]=InputData[15];
		columnNames[4]=InputData[16];
		columnNames[5]=InputData[17];
	    columnNames[6]=InputData[18];
	    columnNames[7]=InputData[19];
	    columnNames[8]=InputData[20];
	    columnNames[9]=InputData[21];
	    columnNames[10]=InputData[22];

		configureResultViewForm.setSelectedColumnNames(columnNames);

		setRequestPathInfo("/DistributionReport");
		addRequestParameter("pageOf", "pageOfDistribution");
		setActionForm(configureResultViewForm);
		actionPerform();

		setRequestPathInfo("/ArrayDistributionReportSave");
		setActionForm(configureResultViewForm);
		actionPerform();

		verifyNoActionErrors();

	}

}
