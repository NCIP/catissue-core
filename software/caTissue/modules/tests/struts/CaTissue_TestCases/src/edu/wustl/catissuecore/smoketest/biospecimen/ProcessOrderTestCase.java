package edu.wustl.catissuecore.smoketest.biospecimen;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.DistributionReportForm;
import edu.wustl.catissuecore.actionForm.RequestDetailsForm;
import edu.wustl.catissuecore.actionForm.RequestListFilterationForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.testframework.util.DataObject;

public class ProcessOrderTestCase extends CaTissueSuiteSmokeBaseTest
{
	public ProcessOrderTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public ProcessOrderTestCase(String name)
	{
		super(name);
	}
	public ProcessOrderTestCase()
	{
		super();
	}

	public void testProcessOrder()
	{
		String[] InputData = getDataObject().getValues();

		RequestListFilterationForm requestListFilterationForm = new RequestListFilterationForm();

		setRequestPathInfo("/RequestListView");
		addRequestParameter("pageNum","1");
		setActionForm(requestListFilterationForm);
		actionPerform();

		RequestDetailsForm requestDetailsForm = new RequestDetailsForm();

		setRequestPathInfo("/RequestDetails");
		addRequestParameter("operation","update");
		addRequestParameter("menuSelected","17");
		addRequestParameter("id",InputData[0]);
		setActionForm(requestDetailsForm);
		actionPerform();

		requestDetailsForm = (RequestDetailsForm)getActionForm();

		requestDetailsForm.setActivityStatus(InputData[1]);
		requestDetailsForm.setDistributionProtocolId(InputData[2]);
		requestDetailsForm.setRequestedDate(InputData[3]);
		requestDetailsForm.setSite(InputData[4]);
		requestDetailsForm.setAdministratorComments(InputData[5]);
		requestDetailsForm.setStatus(InputData[6]);
		requestDetailsForm.setOrderName(InputData[7]);

		Map<String,String> map = new HashMap<String,String>();
		map.put("RequestDetailsBean:"+InputData[8]+"_requestedItem",InputData[9]); //23_3
		map.put("RequestDetailsBean:"+InputData[8]+"_availableQty",InputData[10]); //1.0
		map.put("RequestDetailsBean:"+InputData[8]+"_actualSpecimenClass",InputData[11]);  //Fluid
		map.put("RequestDetailsBean:"+InputData[8]+"_selectedSpecimenQuantity",InputData[12]);  //1.0
		map.put("RequestDetailsBean:"+InputData[8]+"_rowStatuskey",InputData[13]); //enable
		map.put("RequestDetailsBean:"+InputData[8]+"_canDistribute",InputData[14]);  //true
		map.put("RequestDetailsBean:"+InputData[8]+"_actualSpecimenType",InputData[15]); //Whole Blood
		map.put("RequestDetailsBean:"+InputData[8]+"_instanceOf",InputData[16]);  //Existing
		map.put("RequestDetailsBean:"+InputData[8]+"_className",InputData[17]);  //Fluid
		map.put("RequestDetailsBean:"+InputData[8]+"_assignedStatus",InputData[18]); //Distributed
		map.put("RequestDetailsBean:"+InputData[8]+"_requestedQty",InputData[19]);  //1.0
		map.put("RequestDetailsBean:"+InputData[8]+"_orderItemId",InputData[20]); //176  //201
		map.put("RequestDetailsBean:"+InputData[8]+"_specimenQuantityUnit",InputData[21]);  //ml
		map.put("RequestDetailsBean:"+InputData[8]+"_type",InputData[22]);  //Whole Blood
		map.put("RequestDetailsBean:"+InputData[8]+"_specimenId",InputData[23]);  //310
		map.put("RequestDetailsBean:"+InputData[8]+"_requestFor",InputData[24]); //310
		map.put("RequestDetailsBean:"+InputData[8]+"_consentVerificationkey",InputData[25]); //No Consents
		map.put("RequestDetailsBean:"+InputData[8]+"_selectedSpecimenType",InputData[26]); //Whole Blood

		requestDetailsForm.setValues(map);
		int tabIndex = Integer.parseInt(InputData[27]);
		requestDetailsForm.setTabIndex(tabIndex);

		setRequestPathInfo("/SubmitRequestDetails");
		addRequestParameter("operation","update");
		addRequestParameter("menuSelected","17");
		addRequestParameter("noOfRecords","1");
		setActionForm(requestDetailsForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();


	}


	public void testProcessOrderOfDistributionArray()
	{
		String[] InputData = getDataObject().getValues();

		RequestListFilterationForm requestListFilterationForm = new RequestListFilterationForm();

		setRequestPathInfo("/RequestListView");
		addRequestParameter("pageNum","1");
		setActionForm(requestListFilterationForm);
		actionPerform();

		RequestDetailsForm requestDetailsForm = new RequestDetailsForm();

		setRequestPathInfo("/RequestDetails");
		addRequestParameter("operation","update");
		addRequestParameter("menuSelected","17");
		addRequestParameter("id",InputData[0]);  //167
		setActionForm(requestDetailsForm);
		actionPerform();

		requestDetailsForm = (RequestDetailsForm)getActionForm();

		requestDetailsForm.setActivityStatus(InputData[1]);
		requestDetailsForm.setDistributionProtocolId(InputData[2]);
		requestDetailsForm.setRequestedDate(InputData[3]);
		requestDetailsForm.setSite(InputData[4]);
		requestDetailsForm.setAdministratorComments(InputData[5]);
		requestDetailsForm.setStatus(InputData[6]);
		requestDetailsForm.setOrderName(InputData[7]);

		Map<String,String> map = new HashMap<String,String>();
	

		map.put("ExistingArrayDetailsBean:"+InputData[8]+"_distributedItemId",InputData[9]);
		map.put("ExistingArrayDetailsBean:"+InputData[8]+"_orderItemId",InputData[10]);
		map.put("ExistingArrayDetailsBean:"+InputData[8]+"_assignedStatus",InputData[11]);
		map.put("ExistingArrayDetailsBean:"+InputData[8]+"_assignedQuantity",InputData[12]);
		map.put("ExistingArrayDetailsBean:"+InputData[8]+"_addDescription",InputData[13]);
		map.put("ExistingArrayDetailsBean:"+InputData[8]+"_requestedQuantity",InputData[14]);
		map.put("ExistingArrayDetailsBean:"+InputData[8]+"_bioSpecimenArrayName",InputData[15]);
		map.put("ExistingArrayDetailsBean:"+InputData[8]+"_description",InputData[16]);
		map.put("ExistingArrayDetailsBean:"+InputData[8]+"_arrayId",InputData[17]);

		requestDetailsForm.setValues(map);
		int tabIndex = Integer.parseInt(InputData[18]);
		requestDetailsForm.setTabIndex(tabIndex);

		setRequestPathInfo("/SubmitRequestDetails");
		addRequestParameter("operation","update");
		addRequestParameter("menuSelected","17");
		addRequestParameter("noOfRecords","0");
		setActionForm(requestDetailsForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();


	}

}







