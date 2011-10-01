package edu.wustl.catissuecore.smoketest.biospecimen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.actionForm.SpecimenForm;
import edu.wustl.catissuecore.actionForm.shippingtracking.DashboardForm;
import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentForm;
import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentReceivingForm;
import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentRequestForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.testframework.util.DataObject;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;

public class ShipmentTestCase extends CaTissueSuiteSmokeBaseTest

{
	public ShipmentTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public ShipmentTestCase(String name)
	{
		super(name);
	}
	public ShipmentTestCase()
	{
		super();
	}
//Start of create Shipment test cases
	public void testCreateNewShipment()
	{
		String arr[] = getDataObject().getValues();
		AdvanceSearchForm advanceForm = new AdvanceSearchForm();

		setRequestPathInfo("/ViewCart");
		setActionForm(advanceForm);
		actionPerform();
		advanceForm = (AdvanceSearchForm)getActionForm();

		Map<String,String> values = new HashMap<String,String>();
		values.put("CHK_0","1");
		advanceForm.setValues(values);

		setRequestPathInfo("/BulkCart");
		setActionForm(advanceForm);
		actionPerform();

		ShipmentForm shipmentForm = new ShipmentForm();
		setRequestPathInfo("/ProcessMyListCreateShipment");
		addRequestParameter("operation","createShipment");
		setActionForm(shipmentForm);
		actionPerform();

		setRequestPathInfo("/BaseShipment");
		addRequestParameter("operation", "add");
		actionPerform();

		shipmentForm.setActivityStatus(arr[0]);
		shipmentForm.setContainerLabelChoice(arr[1]);
		shipmentForm.setLabel(arr[2]);
		shipmentForm.setSendDate(arr[3]);
		shipmentForm.setSendTimeHour(arr[4]);
		shipmentForm.setSendTimeMinutes(arr[5]);
		shipmentForm.setSpecimenLabelChoice(arr[6]);
		shipmentForm.setSenderSiteId(Long.parseLong(arr[7]));
		shipmentForm.setReceiverSiteId(Long.parseLong(arr[8]));
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("specimenLabel_1", arr[9]);
		shipmentForm.setSpecimenDetailsMap(map);

		ArrayList<String> list = new ArrayList<String>();
		list.add(arr[10]);
		shipmentForm.setLblOrBarcodeSpecimenL(list);
		//shipmentForm.setId(Long.parseLong("102"));
		shipmentForm.setSpecimenCounter(Integer.parseInt(arr[11]));
		shipmentForm.setContainerCounter(Integer.parseInt(arr[12]));
		shipmentForm.setContainerLabelChoice(arr[13]);
		shipmentForm.setSenderComments(arr[14]);

		setRequestPathInfo("/CreateNewShipment");
		addRequestParameter("operation", "add");
		setActionForm(shipmentForm);
		actionPerform();

		setRequestPathInfo("/ShowShipmentSummary");
		addRequestParameter("operation","view");
		setActionForm(shipmentForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();

		verifyActionMessages(new String[]{"object.add.successOnly"});
	}


	public void testCreateNewShipmentWithSpecimenAndContainer()
	{
		String arr[] = getDataObject().getValues();
		AdvanceSearchForm advanceForm = new AdvanceSearchForm();

		setRequestPathInfo("/ViewCart");
		setActionForm(advanceForm);
		actionPerform();
		advanceForm = (AdvanceSearchForm)getActionForm();

		Map<String,String> values = new HashMap<String,String>();
		values.put("CHK_0","1");
		advanceForm.setValues(values);

		setRequestPathInfo("/BulkCart");
		setActionForm(advanceForm);
		actionPerform();

		ShipmentForm shipmentForm = new ShipmentForm();
		setRequestPathInfo("/ProcessMyListCreateShipment");
		addRequestParameter("operation","createShipment");
		setActionForm(shipmentForm);
		actionPerform();

		setRequestPathInfo("/BaseShipment");
		addRequestParameter("operation", "add");
		actionPerform();

		shipmentForm.setActivityStatus(arr[0]);
		shipmentForm.setContainerLabelChoice(arr[1]);
		shipmentForm.setLabel(arr[2]);
		shipmentForm.setSendDate(arr[3]);
		shipmentForm.setSendTimeHour(arr[4]);
		shipmentForm.setSendTimeMinutes(arr[5]);
		shipmentForm.setSpecimenLabelChoice(arr[6]);
		shipmentForm.setSenderSiteId(Long.parseLong(arr[7]));
		shipmentForm.setReceiverSiteId(Long.parseLong(arr[8]));
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("specimenLabel_1", arr[9]);
		shipmentForm.setSpecimenDetailsMap(map);

		ArrayList<String> list = new ArrayList<String>();
		list.add(arr[10]);
		shipmentForm.setLblOrBarcodeSpecimenL(list);
		//shipmentForm.setId(Long.parseLong("102"));
		shipmentForm.setSpecimenCounter(Integer.parseInt(arr[11]));
		shipmentForm.setContainerCounter(Integer.parseInt(arr[12]));
		shipmentForm.setContainerLabelChoice(arr[13]);
		shipmentForm.setSenderComments(arr[14]);
		shipmentForm.setBarcode(arr[15]);
		Map<String,String> map1 = new LinkedHashMap<String,String>();
		map1.put("containerLabel_1", arr[16]);
		shipmentForm.setContainerDetailsMap(map1);
		ArrayList<String> list1 = new ArrayList<String>();
		list1.add(arr[16]);
		shipmentForm.setLblOrBarcodeContainerL(list1);

		setRequestPathInfo("/CreateNewShipment");
		addRequestParameter("operation", "add");
		setActionForm(shipmentForm);
		actionPerform();

		setRequestPathInfo("/ShowShipmentSummary");
		addRequestParameter("operation","view");
		setActionForm(shipmentForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();

		verifyActionMessages(new String[]{"object.add.successOnly"});
	}


	public void testCreateShipmentWithContainer()
	{
		String arr[] = getDataObject().getValues();

		setRequestPathInfo("/BaseShipment");
		addRequestParameter("operation", "add");
		actionPerform();

		ShipmentForm shipmentForm = new ShipmentForm();


		Map<String,String> map1 = new LinkedHashMap<String,String>();
		map1.put("containerLabel_1", arr[0]); //Ship_Container_6
		shipmentForm.setContainerDetailsMap(map1);
		shipmentForm.setContainerLabelChoice(arr[1]); //ContainerLabel
		shipmentForm.setLabel(arr[2]);
		shipmentForm.setSendDate(arr[3]);
		shipmentForm.setSendTimeHour(arr[4]);
		shipmentForm.setSendTimeMinutes(arr[5]);
		shipmentForm.setSpecimenLabelChoice(arr[6]);
		shipmentForm.setSenderSiteId(Long.parseLong(arr[7])); //81
		shipmentForm.setReceiverSiteId(Long.parseLong(arr[8])); //21
		shipmentForm.setSpecimenCounter(Integer.parseInt(arr[9]));
		shipmentForm.setContainerCounter(Integer.parseInt(arr[9]));

		setRequestPathInfo("/CreateNewShipment");
		addRequestParameter("operation", "add");
		setActionForm(shipmentForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();

		verifyActionMessages(new String[]{"object.add.successOnly"});

	}
//End of  create shipment  test cases

//Start of Create New Shipment Request
	public void testCreateNewShipmentRequestWithSpecimen()
	{
		String arr[] = getDataObject().getValues();
		AdvanceSearchForm advanceForm = new AdvanceSearchForm();

		setRequestPathInfo("/ViewCart");
		setActionForm(advanceForm);
		actionPerform();
		advanceForm = (AdvanceSearchForm)getActionForm();

		Map<String,String> values = new HashMap<String,String>();
		values.put("CHK_0","1");
		advanceForm.setValues(values);

		setRequestPathInfo("/BulkCart");
		setActionForm(advanceForm);
		actionPerform();


		ShipmentRequestForm shipmentRequestForm = new ShipmentRequestForm();

		setRequestPathInfo("/ProcessMyListCreateShipmentRequest");
		addRequestParameter("operation","createShipmentRequest");
		setActionForm(shipmentRequestForm);
		actionPerform();

		setRequestPathInfo("/ShipmentRequestAction");
		addRequestParameter("operation", "add");
		actionPerform();

		shipmentRequestForm.setLabel(arr[0]);
		shipmentRequestForm.setSendDate(arr[1]);
		shipmentRequestForm.setSendTimeHour(arr[2]);
		shipmentRequestForm.setSendTimeMinutes(arr[3]);
		shipmentRequestForm.setSenderSiteId(Long.parseLong(arr[4]));
		shipmentRequestForm.setSpecimenCounter(Integer.parseInt(arr[5]));
		shipmentRequestForm.setContainerCounter(Integer.parseInt(arr[5]));
		shipmentRequestForm.setSpecimenLabelChoice(arr[7]);
		shipmentRequestForm.setContainerLabelChoice(arr[6]);

		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("specimenLabel_1", arr[8]);
		shipmentRequestForm.setSpecimenDetailsMap(map);

		setRequestPathInfo("/ViewRequestSummary");
		setActionForm(shipmentRequestForm);
		actionPerform();

		setRequestPathInfo("/AddEditShipmentRequest");
		addRequestParameter("operation", "add");
		setActionForm(shipmentRequestForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();

		verifyActionMessages(new String[]{"object.add.success"});
	}

	public void testCreateNewShipmentRequestWithSpecAndCont()
	{
		String arr[] = getDataObject().getValues();
		AdvanceSearchForm advanceForm = new AdvanceSearchForm();

		setRequestPathInfo("/ViewCart");
		setActionForm(advanceForm);
		actionPerform();
		advanceForm = (AdvanceSearchForm)getActionForm();

		Map<String,String> values = new HashMap<String,String>();
		values.put("CHK_0","1");
		advanceForm.setValues(values);

		setRequestPathInfo("/BulkCart");
		setActionForm(advanceForm);
		actionPerform();


		ShipmentRequestForm shipmentRequestForm = new ShipmentRequestForm();

		setRequestPathInfo("/ProcessMyListCreateShipmentRequest");
		addRequestParameter("operation","createShipmentRequest");
		setActionForm(shipmentRequestForm);
		actionPerform();

		setRequestPathInfo("/ShipmentRequestAction");
		addRequestParameter("operation", "add");
		actionPerform();

		shipmentRequestForm.setLabel(arr[0]);
		shipmentRequestForm.setSendDate(arr[1]);
		shipmentRequestForm.setSendTimeHour(arr[2]);
		shipmentRequestForm.setSendTimeMinutes(arr[3]);
		shipmentRequestForm.setSenderSiteId(Long.parseLong(arr[4]));
		shipmentRequestForm.setSpecimenCounter(Integer.parseInt(arr[5]));
		shipmentRequestForm.setContainerCounter(Integer.parseInt(arr[5]));
		shipmentRequestForm.setSpecimenLabelChoice(arr[7]);
		shipmentRequestForm.setContainerLabelChoice(arr[6]);

		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("specimenLabel_1", arr[8]);
		shipmentRequestForm.setSpecimenDetailsMap(map);

		Map<String,String> map1 = new LinkedHashMap<String,String>();
		map1.put("containerLabel_1", arr[9]);
		shipmentRequestForm.setContainerDetailsMap(map1);



		setRequestPathInfo("/ViewRequestSummary");
		setActionForm(shipmentRequestForm);
		actionPerform();

		setRequestPathInfo("/AddEditShipmentRequest");
		addRequestParameter("operation", "add");
		setActionForm(shipmentRequestForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();

		verifyActionMessages(new String[]{"object.add.success"});
	}

	public void testCreateNewShipmentRequestWithContainer()
	{
		String arr[] = getDataObject().getValues();

		setRequestPathInfo("/ShipmentRequestAction");
		addRequestParameter("operation", "add");
		actionPerform();

		ShipmentRequestForm shipmentRequestForm = new ShipmentRequestForm();


		Map<String,String> map1 = new LinkedHashMap<String,String>();
		map1.put("containerLabel_1", arr[0]);
		shipmentRequestForm.setContainerDetailsMap(map1);
		shipmentRequestForm.setContainerLabelChoice(arr[1]);
		shipmentRequestForm.setLabel(arr[2]);
		shipmentRequestForm.setSendDate(arr[3]);
		shipmentRequestForm.setSendTimeHour(arr[4]);
		shipmentRequestForm.setSendTimeMinutes(arr[5]);
		shipmentRequestForm.setSpecimenLabelChoice(arr[6]);
		shipmentRequestForm.setSenderSiteId(Long.parseLong(arr[7]));
		shipmentRequestForm.setSpecimenCounter(Integer.parseInt(arr[8]));
		shipmentRequestForm.setContainerCounter(Integer.parseInt(arr[8]));

		setRequestPathInfo("/ViewRequestSummary");
		setActionForm(shipmentRequestForm);
		actionPerform();

		setRequestPathInfo("/AddEditShipmentRequest");
		addRequestParameter("operation", "add");
		setActionForm(shipmentRequestForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();

		verifyActionMessages(new String[]{"object.add.success"});
	}

	// End of Create New Shipment Request


	public void testProcessCreateShipment()
	{
		String arr[] = getDataObject().getValues();


		ShipmentReceivingForm shipmentReceivingForm = new ShipmentReceivingForm();

		setRequestPathInfo("/st_ShowShipmentReceving");
		addRequestParameter("id",arr[0]);
		addRequestParameter("fromPage","dashboard");
		setActionForm(shipmentReceivingForm);
		actionPerform();

		setRequestPathInfo("/ProcessShipmentReceived");
		addRequestParameter("operation","");
		setActionForm(shipmentReceivingForm);
		actionPerform();


		verifyForward("success");
		verifyNoActionErrors();

		verifyActionMessages(new String[]{"object.edit.successOnly"});


	}

	public void testProcessCreateShipmentRequest()
	{
		String arr[] = getDataObject().getValues();

		ShipmentRequestForm shipmentRequestForm=new ShipmentRequestForm();

		setRequestPathInfo("/RequestReceived");
		addRequestParameter("id",arr[0]);
		actionPerform();

		setRequestPathInfo("/ShipmentRequestAction");
		addRequestParameter("operation","edit");
		addRequestParameter("pageOf","pageOfShipmentRequest");
		actionPerform();


		shipmentRequestForm.setSpecimenCounter(Integer.parseInt(arr[1]));
		shipmentRequestForm.setContainerCounter(Integer.parseInt(arr[1]));
		shipmentRequestForm.setLabel(arr[2]);
		shipmentRequestForm.setSenderSiteId(Long.parseLong(arr[3]));
		shipmentRequestForm.setSendDate(arr[4]);

		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("specimenLabel_1",arr[5]);
		map.put("specimenBarcode_1",arr[6]);
		shipmentRequestForm.setSpecimenDetailsMap(map);

		Map<String,String> map1 = new LinkedHashMap<String,String>();
		map1.put("containerBarcode_1","");
		map1.put("containerLabel_1","");

		shipmentRequestForm.setContainerDetailsMap(map1);
		shipmentRequestForm.setContainerLabelChoice(arr[7]);
		shipmentRequestForm.setSpecimenLabelChoice(arr[8]);
		shipmentRequestForm.setId(Long.parseLong(arr[0]));


		setRequestPathInfo("/ViewRequestSummary");
		addRequestParameter("operation","add");
		setActionForm(shipmentRequestForm);
		actionPerform();

		setRequestPathInfo("/AddEditShipmentRequest");
		addRequestParameter("operation", "add");
		setActionForm(shipmentRequestForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();

		verifyActionMessages(new String[]{"object.add.success"});

	}


}
