package edu.wustl.catissuecore.smoketest.biospecimen;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.actionForm.DefineArrayForm;
import edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm;
import edu.wustl.catissuecore.actionForm.OrderForm;
import edu.wustl.catissuecore.actionForm.OrderSpecimenForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.testframework.util.DataObject;
import edu.wustl.catissuecore.util.global.Constants;


public class OrderingTestCase extends CaTissueSuiteSmokeBaseTest
{

	public OrderingTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public OrderingTestCase(String name)
	{
		super(name);
	}
	public OrderingTestCase()
	{
		super();
	}

	public void testAddOrderBioSpecimen()
	{
		String arr[] = getDataObject().getValues();

		AdvanceSearchForm advanceForm = new AdvanceSearchForm();


		setRequestPathInfo("/ViewCart");
		setActionForm(advanceForm);
		actionPerform();

		advanceForm = (AdvanceSearchForm)getActionForm();

		advanceForm.setOrderedString(arr[0]);
		Map<String,String> values = new HashMap<String,String>();
		values.put("CHK_0","1");
		advanceForm.setValues(values);




		setRequestPathInfo("/BulkCart");
		setActionForm(advanceForm);
		actionPerform();

		OrderForm orderForm = new OrderForm();

		setRequestPathInfo("/RequestToOrder");
		addRequestParameter("operation","addToOrderList");
		setActionForm(orderForm);
		actionPerform();

		orderForm = (OrderForm)getActionForm();

		orderForm.setOrderRequestName(arr[1]);
		orderForm.setDistributionProtocol(arr[2]);

		orderForm.setComments(arr[3]);

		setRequestPathInfo("/RequestToOrderSubmit");
		setActionForm(orderForm);
		addRequestParameter("orderRequestName",arr[1]);
		addRequestParameter("distributionProtocol",arr[2]);
		addRequestParameter("comments",arr[3]);
		actionPerform();

		OrderSpecimenForm specimenForm = new OrderSpecimenForm();

		setRequestPathInfo("/OrderExistingSpecimen");
		addRequestParameter("typeOfSpecimen","existingSpecimen");
		setActionForm(specimenForm);
		actionPerform();

		specimenForm = (OrderSpecimenForm)getActionForm();

		Map<String,String> value1 = new LinkedHashMap<String,String>();
		String arr1[] = {arr[4]};
		specimenForm.setSelectedItems(arr1);
		specimenForm.setAddToArray(arr[5]);

		value1.put("OrderSpecimenBean:0_specimenType",arr[6]);
		value1.put("OrderSpecimenBean:0_specimenName",arr[7]);
		value1.put("OrderSpecimenBean:0_distributionSite",arr[8]);
		value1.put("OrderSpecimenBean:0_specimenId",arr[9]);
		value1.put("OrderSpecimenBean:0_availableQuantity",arr[10]);
		value1.put("OrderSpecimenBean:0_specimenClass",arr[11]);
		value1.put("OrderSpecimenBean:0_requestedQuantity",arr[12]);
		value1.put("OrderSpecimenBean:0_typeOfItem",arr[13]);
		value1.put("OrderSpecimenBean:0_collectionStatus",arr[14]);
		value1.put("OrderSpecimenBean:0_isAvailable",arr[15]);
		value1.put("OrderSpecimenBean:0_arrayName",arr[5]);

		specimenForm.setValues(value1);

		setRequestPathInfo("/AddToOrderListSpecimen");
		setActionForm(specimenForm);
		addRequestParameter("selectedItems",arr1);
		addRequestParameter("addToArray",arr[5]);
		addRequestParameter("typeOf", "specimen");
		actionPerform();


		setRequestPathInfo("/SaveOrderItems");
		setActionForm(specimenForm);
		actionPerform();


		setRequestPathInfo("/InsertOrderItems");
		addRequestParameter("pageOf", "specimen");
		setActionForm(specimenForm);
		actionPerform();

		setRequestPathInfo("/ShoppingCart");
		setActionForm(advanceForm);
		actionPerform();

		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
	}

	public void testAddOrderBioSpecimenWithDefineArray()
	{
		String arr[] = getDataObject().getValues();

		AdvanceSearchForm advanceForm = new AdvanceSearchForm();

		setRequestPathInfo("/ViewCart");
		setActionForm(advanceForm);
		actionPerform();

		advanceForm = (AdvanceSearchForm)getActionForm();

		advanceForm.setOrderedString(arr[0]);
		Map<String,String> values = new HashMap<String,String>();
		values.put("CHK_0","1");
		advanceForm.setValues(values);

		setRequestPathInfo("/BulkCart");
		setActionForm(advanceForm);
		actionPerform();

		OrderForm orderForm = new OrderForm();

		setRequestPathInfo("/RequestToOrder");
		addRequestParameter("operation","addToOrderList");
		setActionForm(orderForm);
		actionPerform();

		orderForm = (OrderForm)getActionForm();

		orderForm.setOrderRequestName(arr[1]);
		orderForm.setDistributionProtocol(arr[2]);
		orderForm.setComments(arr[3]);

		setRequestPathInfo("/RequestToOrderSubmit");
		setActionForm(orderForm);
		addRequestParameter("orderRequestName",arr[1]);
		addRequestParameter("distributionProtocol",arr[2]);
		addRequestParameter("comments",arr[3]);
		actionPerform();

		DefineArrayForm defineArrayForm = new DefineArrayForm();

		setRequestPathInfo("/DefineArray");
		addRequestParameter("typeOf", "specimen");
		setActionForm(defineArrayForm);
		actionPerform();

		defineArrayForm = (DefineArrayForm)getActionForm();

		setRequestPathInfo("/DefineArraySubmit");
		addRequestParameter("typeOf", "specimen");
		addRequestParameter("activityStatus", arr[16]);
		addRequestParameter("arrayClass", arr[17]);
		addRequestParameter("arrayName", arr[5]);
		addRequestParameter("arraytype", arr[18]);
		addRequestParameter("arrayTypeName", arr[19]);
		addRequestParameter("dimenmsionX", arr[20]);
		addRequestParameter("dimenmsionY", arr[20]);

		setActionForm(defineArrayForm);
		actionPerform();

		OrderSpecimenForm specimenForm = new OrderSpecimenForm();

		setRequestPathInfo("/OrderExistingSpecimen");
		addRequestParameter("typeOfSpecimen","existingSpecimen");
		setActionForm(specimenForm);
		actionPerform();

		specimenForm = (OrderSpecimenForm)getActionForm();

		Map<String,String> value1 = new LinkedHashMap<String,String>();
		String arr1[] = {arr[4]};
		specimenForm.setSelectedItems(arr1);
		specimenForm.setAddToArray(arr[5]);

		value1.put("OrderSpecimenBean:0_specimenType",arr[6]);
		value1.put("OrderSpecimenBean:0_specimenName",arr[7]);
		value1.put("OrderSpecimenBean:0_distributionSite",arr[8]);
		value1.put("OrderSpecimenBean:0_specimenId",arr[9]);
		value1.put("OrderSpecimenBean:0_availableQuantity",arr[10]);
		value1.put("OrderSpecimenBean:0_specimenClass",arr[11]);
		value1.put("OrderSpecimenBean:0_requestedQuantity",arr[12]);
		value1.put("OrderSpecimenBean:0_typeOfItem",arr[13]);
		value1.put("OrderSpecimenBean:0_collectionStatus",arr[14]);
		value1.put("OrderSpecimenBean:0_isAvailable",arr[15]);
		value1.put("OrderSpecimenBean:0_arrayName",arr[5]);

		specimenForm.setValues(value1);

		setRequestPathInfo("/AddToOrderListSpecimen");
		setActionForm(specimenForm);
		addRequestParameter("selectedItems",arr1);
		addRequestParameter("addToArray",arr[5]);
		addRequestParameter("typeOf", "specimen");
		actionPerform();

		setRequestPathInfo("/SaveOrderItems");
		setActionForm(specimenForm);
		actionPerform();

		setRequestPathInfo("/InsertOrderItems");
		addRequestParameter("pageOf", "specimen");
		setActionForm(specimenForm);
		actionPerform();

		setRequestPathInfo("/ShoppingCart");
		setActionForm(advanceForm);
		actionPerform();

		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
	}

	public void testAddOrderBiospecimenArray()
	{
		String arr[] = getDataObject().getValues();

		AdvanceSearchForm advanceForm = new AdvanceSearchForm();


		setRequestPathInfo("/ViewCart");
		setActionForm(advanceForm);
		actionPerform();

		advanceForm = (AdvanceSearchForm)getActionForm();

		advanceForm.setOrderedString(arr[0]);
		Map<String,String> values = new HashMap<String,String>();
		values.put("CHK_0","1");
		advanceForm.setValues(values);


		setRequestPathInfo("/BulkCart");
		setActionForm(advanceForm);
		actionPerform();

		OrderForm orderForm = new OrderForm();

		setRequestPathInfo("/RequestToOrder");
		addRequestParameter("operation","addToOrderList");
		setActionForm(orderForm);
		actionPerform();

		orderForm = (OrderForm)getActionForm();

		orderForm.setOrderRequestName(arr[1]);
		orderForm.setDistributionProtocol(arr[2]);
		orderForm.setComments(arr[3]);

		setRequestPathInfo("/RequestToOrderSubmit");
		setActionForm(orderForm);
		addRequestParameter("orderRequestName",arr[1]);
		addRequestParameter("distributionProtocol",arr[2]);
		addRequestParameter("comments",arr[3]);
		actionPerform();

		OrderSpecimenForm specimenForm = new OrderSpecimenForm();

		setRequestPathInfo("/OrderExistingSpecimen");
		addRequestParameter("typeOfSpecimen","existingSpecimen");
		setActionForm(specimenForm);
		actionPerform();

		OrderBiospecimenArrayForm orderBiospecimenArrayForm = new OrderBiospecimenArrayForm();

		setRequestPathInfo("/OrderBiospecimenArray");
		setActionForm(orderBiospecimenArrayForm);
		addRequestParameter("typeOfArray","existingArray");
		actionPerform();



		Map<String,String> value1 = new LinkedHashMap<String,String>();
		String arr1[] = {arr[4]};
		orderBiospecimenArrayForm.setSelectedItems(arr1);
		orderBiospecimenArrayForm.setTypeOfArray("false");

		value1.put("OrderSpecimenBean:0_typeOfItem",arr[5]);
		value1.put("OrderSpecimenBean:0_specimenName",arr[6]);
		value1.put("OrderSpecimenBean:0_distributionSite",arr[7]);
		value1.put("OrderSpecimenBean:0_specimenId",arr[8]);
		value1.put("OrderSpecimenBean:0_specimenClass",arr[9]);
		value1.put("OrderSpecimenBean:0_specimenType",arr[10]);

		orderBiospecimenArrayForm.setValues(value1);

		setRequestPathInfo("/AddToOrderListArray");
		setActionForm(orderBiospecimenArrayForm);
		addRequestParameter("selectedItems",arr1);
		addRequestParameter("typeOfArray","false");
		addRequestParameter("typeOf", "specimenArray");
		actionPerform();

		setRequestPathInfo("/OrderBiospecimenArray");
		setActionForm(orderBiospecimenArrayForm);
		addRequestParameter("typeOfArray","existingArray");
		actionPerform();


		setRequestPathInfo("/SaveOrderArrayItems");
		setActionForm(orderBiospecimenArrayForm);
		actionPerform();


		setRequestPathInfo("/InsertOrderArrayItems");
		addRequestParameter("pageOf", "specimenArray");
		setActionForm(orderBiospecimenArrayForm);
		actionPerform();

		setRequestPathInfo("/ShoppingCart");
		setActionForm(advanceForm);
		actionPerform();

		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
	}
}
