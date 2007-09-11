/**
 * 
 */

package edu.wustl.catissuecore.action.querysuite;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author supriya_dankh Handles all the actions related to shopping cart.
 */
public class QueryShoppingCartAction extends BaseAction
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AdvanceSearchForm searchForm = (AdvanceSearchForm) form;
		HttpSession session = request.getSession();
		String target = "";
		String operation = request.getParameter(Constants.OPERATION);

		if (operation == null)
			operation = "";

		// Extracting map from formbean which gives the serial numbers of
		// selected rows
		Map map = searchForm.getValues();
		Set chkBoxValues = map.keySet();

		String isCheckAllAcrossAllChecked = (String) request
				.getParameter(Constants.CHECK_ALL_ACROSS_ALL_PAGES);
		QueryShoppingCart cart = (QueryShoppingCart) session
				.getAttribute(Constants.QUERY_SHOPPING_CART);

		// Getting column data & Attribute list from session.
		List<AttributeInterface> attributeList = (List<AttributeInterface>) session
				.getAttribute(Constants.ATTRIBUTES);
		List<List<String>> columnList = (List<List<String>>) session
				.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);

		// Check if user wants to add in Shopping Cart.
		if (operation.equals(Constants.ADD))
		{
			target = addToCart(request, chkBoxValues, isCheckAllAcrossAllChecked, cart, attributeList, columnList);

		}// Check if user wants to delete record from cart.
		else if (operation.equalsIgnoreCase(Constants.DELETE))
		{
			deleteFromCart(cart, chkBoxValues);
			target = new String(Constants.SHOPPING_CART_DELETE);
		}
		// Check if user wants to export cart.
		else if (operation.equalsIgnoreCase(Constants.EXPORT))
		{

			export(cart, chkBoxValues, session, request, response);
            return null;
		}// Check if user wants to view the cart.
		else if (operation.equalsIgnoreCase("view"))
		{
			target = new String(Constants.VIEW);
			session.removeAttribute(Constants.ADD_TO_CART);
		}

		request.setAttribute(Constants.PAGEOF, Constants.PAGEOF_QUERY_MODULE);
		return mapping.findForward(target);

	}

	/**
	 * @param request
	 * @param chkBoxValues
	 * @param isCheckAllAcrossAllChecked
	 * @param cart
	 * @param attributeList
	 * @param columnList
	 * @return
	 * @throws DAOException
	 */
	private String addToCart(HttpServletRequest request, Set chkBoxValues, String isCheckAllAcrossAllChecked,
			QueryShoppingCart cart, List<AttributeInterface> attributeList,
			List<List<String>> columnList) throws DAOException
	{
		String target;
		boolean isError = false;
		//if cart is not present in session create new cart object
		if (cart == null)
		{
			cart = new QueryShoppingCart();
			cart.setCartAttributeList(attributeList);
			cart.setColumnList(columnList);
		}
		else
		{
			/*
			 * if cart is in session but user has deleted all the records
			 * from cart.Then take same cart object from session and set the
			 * view of cart according to current records.
			 */
			if (cart.getCartAttributeList() == null)
			{
				cart.setCartAttributeList(attributeList);
				cart.setColumnList(columnList);
			}
			else
			{
				/*
				 * check if view i.e. attribute list of current selection of
				 * user and cart view is same if not display error message
				 * to user.
				 */
				List<AttributeInterface> oldAttributeList = cart.getCartAttributeList();
				if (!(oldAttributeList.equals(attributeList)))
				{
					ActionErrors errors = new ActionErrors();
					ActionError error = new ActionError("shoppingcart.differentViewError");
					errors.add(ActionErrors.GLOBAL_ERROR, error);
					saveErrors(request, errors);
					target = new String(Constants.DIFFERENT_VIEW_IN_CART);
					isError = true;
				}
			}

		}

		HttpSession session = request.getSession();
		List<List<String>> dataList = getPaginationDataList(request, session);
		// If no error add object in cart.
		if (!isError)
		{
			if (isCheckAllAcrossAllChecked != null
					&& isCheckAllAcrossAllChecked.equalsIgnoreCase(Constants.TRUE))
				chkBoxValues = null;
			addDataToCart(cart, dataList, chkBoxValues, request, session, columnList);

		}
		target = new String(Constants.SHOPPING_CART_ADD);
		request.setAttribute(Constants.PAGINATION_DATA_LIST, dataList);
		return target;
	}

	/**
	 * Add data in Cart .
	 * 
	 * @param cart a shopping cart object preset in session.
	 * @param dataList List of records.
	 * @param session HttpSession object. 
	 * @param request HttpServletRequest.
	 * @param response HttpServletResponse.
	*/
	public void addDataToCart(QueryShoppingCart cart, List<List<String>> dataList, Set chkBoxValues,
			HttpServletRequest request, HttpSession session, List<List<String>> columnList)
	{
		QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		// Add a record to cart.
		int addRecordCount = bizLogic.add(cart, dataList, chkBoxValues);
		int duplicateRecordCount = 0;

		if (chkBoxValues != null)
			duplicateRecordCount = chkBoxValues.size() - addRecordCount;
		else
			duplicateRecordCount = dataList.size() - addRecordCount;

		ActionErrors errors = new ActionErrors();
		// Check if no. of duplicate records is not zero then set a
		// error message.
		if (duplicateRecordCount != 0)
		{
			ActionError error = null;
			if (duplicateRecordCount == 1)
				error = new ActionError("shoppingcart.duplicateObjError", addRecordCount,
						duplicateRecordCount);
			else
				error = new ActionError("shoppingcart.duplicateObjsError", addRecordCount,
						duplicateRecordCount);
			errors.add(ActionErrors.GLOBAL_ERROR, error);

		}
		else
		{
			ActionError addMsg = new ActionError("shoppingCart.addMessage", addRecordCount);
			errors.add(ActionErrors.GLOBAL_ERROR, addMsg);
			session.setAttribute(Constants.QUERY_SHOPPING_CART, cart);
			request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnList);

		}
		saveErrors(request, errors);

	}

	/**
	 * Get Pegination data list .
	 * 
	 * @param session HttpSession object. 
	 * @param request HttpServletRequest.
	*/
	public List<List<String>> getPaginationDataList(HttpServletRequest request, HttpSession session)
			throws DAOException
	{
		String pageNo = (String) request.getParameter(Constants.PAGE_NUMBER);
		String recordsPerPageStr = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);// Integer.parseInt(XMLPropertyHandler.getValue(Constants.NO_OF_RECORDS_PER_PAGE));
		String isCheckAllAcrossAllChecked = (String) request
				.getParameter(Constants.CHECK_ALL_ACROSS_ALL_PAGES);

		if (pageNo != null)
		{
			request.setAttribute(Constants.PAGE_NUMBER, pageNo);
		}

		int recordsPerPage = Integer.parseInt(recordsPerPageStr);
		int pageNum = Integer.parseInt(pageNo);

		if (isCheckAllAcrossAllChecked != null
				&& isCheckAllAcrossAllChecked.equalsIgnoreCase(Constants.TRUE))
		{
			Integer totalRecords = (Integer) session.getAttribute(Constants.TOTAL_RESULTS);
			recordsPerPage = totalRecords;
			pageNum = 1;
		}

		QuerySessionData querySessionData = (QuerySessionData) request.getSession().getAttribute(
				edu.wustl.common.util.global.Constants.QUERY_SESSION_DATA);
		List<List<String>> dataList = Utility.getPaginationDataList(request,
				getSessionData(request), recordsPerPage, pageNum, querySessionData);

		return dataList;

	}

	public void deleteFromCart(QueryShoppingCart cart, Set chkBoxValues)
	{
		QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		bizLogic.delete(cart, chkBoxValues);
	}

	/**
	 * Export Cart data.
	 * 
	 * @param cart a shopping cart object preset in session.
	 * @param session HttpSession object. 
	 * @param request HttpServletRequest.
	 * @param response HttpServletResponse.
	*/
	public void export(QueryShoppingCart cart, Set chkBoxValues, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
	{
		QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		String fileName = Variables.applicationHome + System.getProperty("file.separator")
				+ session.getId() + ".csv";
		List<List<String>> exportList = bizLogic.export(cart, chkBoxValues);

		String delimiter = Constants.DELIMETER;
		// Exporting the data to the given file & sending it to user

		try
		{
			ExportReport report = new ExportReport(fileName);
			report.writeData(exportList, delimiter);
			report.closeFile();
		}
		catch (IOException e)
		{
			ActionErrors errors = new ActionErrors();
			ActionError error = new ActionError("shoppingcart.exportfilexception");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			saveErrors(request, errors);
		}

		SendFile.sendFileToClient(response, fileName, Constants.SHOPPING_CART_FILE_NAME,
				"application/download");

	}

}
