package edu.wustl.catissuecore.smoketest.search;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.testframework.util.DataObject;

public class ViewCartTestCase extends CaTissueSuiteSmokeBaseTest
{
	public ViewCartTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public ViewCartTestCase(String name)
	{
		super(name);
	}
	public ViewCartTestCase()
	{
		super();
	}

	public void testViewCart()
	{
		String[] arr = getDataObject().getValues();
		AdvanceSearchForm searchForm = new AdvanceSearchForm();
		searchForm.setObjectName(arr[0]);

		List<String> columnList = new ArrayList<String>();
		columnList.add("Activity Status : Specimen");
		columnList.add("Available Quantity : Specimen");
		columnList.add("Label : Specimen");
		columnList.add("Barcode : Specimen");
		columnList.add("Id : Specimen");
		columnList.add("Collection Status : Specimen");

		List<List<String>> cart = new ArrayList<List<String>>();
		List<String>dataList = new ArrayList<String>();
		dataList.add("Active");
		dataList.add("100");
		dataList.add("1");
		dataList.add("1");
		dataList.add("1");
		dataList.add("Collected");
		cart.add(dataList);

		QueryShoppingCart shoppingCart = new QueryShoppingCart();
		shoppingCart.setCart(cart);
		shoppingCart.setColumnList(columnList);
		shoppingCart.setCartAttributeList(null);

		HttpSession session = getSession();
		session.setAttribute(Constants.QUERY_SHOPPING_CART, shoppingCart);

		setRequestPathInfo("/ViewCart");
		setActionForm(searchForm);
		actionPerform();
		verifyForward("view");
		verifyNoActionErrors();
	}
}
