package edu.wustl.catissuecore.smoketest.biospecimen;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm;
import edu.wustl.catissuecore.actionForm.OrderForm;
import edu.wustl.catissuecore.actionForm.OrderSpecimenForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.testframework.util.DataObject;
import edu.wustl.catissuecore.util.global.Constants;


public class ArrayReportTestCases extends CaTissueSuiteSmokeBaseTest
{

	public ArrayReportTestCases(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public ArrayReportTestCases(String name)
	{
		super(name);
	}
	public ArrayReportTestCases()
	{
		super();
	}

	public void testSpecimenArrayDistribute()
	{
		String[] InputData = getDataObject().getValues();

		AdvanceSearchForm advanceSearchForm = new AdvanceSearchForm();
		OrderForm orderForm = new OrderForm();
		OrderBiospecimenArrayForm orderBiospecimenArrayForm = new OrderBiospecimenArrayForm();

		setRequestPathInfo("/ViewCart");
		addRequestParameter("operation","view");
		setActionForm(advanceSearchForm);
		actionPerform();

		advanceSearchForm = (AdvanceSearchForm)getActionForm();

		Map<String,String> map = new HashMap<String,String>();

		map.put("CHK_"+InputData[1],InputData[0]);//1   //0
		advanceSearchForm.setValues(map);

		setRequestPathInfo("/BulkCart");
		addRequestParameter("operation","requestToDistribute");
		setActionForm(advanceSearchForm);
		actionPerform();

	//	OrderForm orderForm = new OrderForm();
	//	orderForm = (OrderForm)getActionForm();

	//	orderForm.setActivityStatus("Active");
		//orderForm.set

		setRequestPathInfo("/RequestToDistribute");
		addRequestParameter("operation","requestToDistribute");
		addRequestParameter("SpecimenArrayIds",InputData[10]);
		setActionForm(orderForm);
		actionPerform();

		setRequestPathInfo("/SpecArrayDistribute");
		addRequestParameter("operation","requestToDistribute");
		addRequestParameter("typeOf","specimenArray");
		addRequestParameter("pageOf","specimenArray");
		addRequestParameter("typeOfArray","existingArray");
		setActionForm(orderBiospecimenArrayForm);
		actionPerform();

		orderBiospecimenArrayForm = (OrderBiospecimenArrayForm)getActionForm();

		Map<String,String> values = new HashMap<String,String>();

		values.put("OrderSpecimenBean:"+InputData[2]+"_isDerived", InputData[3]);//false
		values.put("OrderSpecimenBean:"+InputData[2]+"_typeOfItem", InputData[4]);//specimenArray
		values.put("OrderSpecimenBean:"+InputData[2]+"_availableQuantity", InputData[5]);//space
		values.put("OrderSpecimenBean:"+InputData[2]+"_arrayName", InputData[6]);//None
		values.put("OrderSpecimenBean:"+InputData[2]+"_specimenName", InputData[7]);//FArray_706
		values.put("OrderSpecimenBean:"+InputData[2]+"_description", InputData[8]);//s
		values.put("OrderSpecimenBean:"+InputData[2]+"_distributionSite", InputData[9]);//s
		values.put("OrderSpecimenBean:"+InputData[2]+"_specimenId", InputData[10]);//706
		values.put("OrderSpecimenBean:"+InputData[2]+"_unitRequestedQuantity", InputData[11]);//s
		values.put("OrderSpecimenBean:"+InputData[2]+"_requestedQuantity", InputData[12]);//0.0

		orderBiospecimenArrayForm.setValues(values);
		orderBiospecimenArrayForm.setActivityStatus(InputData[13]);

		orderBiospecimenArrayForm.setOrderForm(orderForm);

		setRequestPathInfo("/specArrayToDistribute");
		addRequestParameter("pageOf","specimenArray");
		addRequestParameter("typeOfArray","existingArray");
		//addRequestParameter("id","167");
		setActionForm(orderBiospecimenArrayForm);
		actionPerform();

		setRequestPathInfo("/DirectDistributeSpecArray");
		addRequestParameter("pageOf","specimenArray");
		addRequestParameter("typeOf","specimenArray");
		setActionForm(orderBiospecimenArrayForm);
		actionPerform();

		verifyForward("success");
	}
}
