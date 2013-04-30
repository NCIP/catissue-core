/**
 * <p>
 * Title: ShoppingCartAction Class>
 * <p>
 * Description: This class initializes the fields of ShoppingCart.jsp Page
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Aniruddha Phadnis
 * @version 1.00 Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.bizlogic.ShoppingCartBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.QuerySessionData;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.query.util.global.AQConstants;
import edu.wustl.simplequery.query.ShoppingCart;

/**
 * @author renuka_bajpai
 *
 */
public class ShoppingCartAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(ShoppingCartAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final HttpSession session = request.getSession(true);
		// Gets the value of the operation parameter.
		final String operation = request.getParameter(Constants.OPERATION);
		final String pageNo = request.getParameter(Constants.PAGE_NUMBER);
		final String recordsPerPageStr = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
		final String pageOff = request.getParameter(Constants.PAGE_OF);
		List paginationDataList = null;
		if (pageNo != null)
		{
			request.setAttribute(Constants.PAGE_NUMBER, pageNo);
		}
		String target = Constants.SUCCESS;

		ShoppingCart cart = (ShoppingCart) session.getAttribute(Constants.SHOPPING_CART);
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final ShoppingCartBizLogic bizLogic = (ShoppingCartBizLogic) factory
				.getBizLogic(Constants.SHOPPING_CART_FORM_ID);
		// ShoppingCartForm shopForm = (ShoppingCartForm)form;
		final AdvanceSearchForm advForm = (AdvanceSearchForm) form;
		final String isCheckAllAcrossAllChecked = request
				.getParameter(Constants.CHECK_ALL_ACROSS_ALL_PAGES);
		if (cart == null)
		{
			cart = new ShoppingCart();
		}

		if (operation == null)
		{
			/*
			 * List specimenList = bizLogic.retrieve(Specimen.class.getName());
			 * Iterator it = specimenList.iterator(); while(it.hasNext()) {
			 * Specimen specimen = (Specimen)it.next(); cart.add(specimen); }
			 * session.setAttribute(Constants.SHOPPING_CART,cart);
			 */

			request.setAttribute(AQConstants.SPREADSHEET_DATA_LIST, this.makeGridData(cart));
		}
		else
		{
			session.setAttribute("OrderForm", "true");
			if (operation.equals(Constants.ADD)) // IF OPERATION IS "ADD"
			{
				// Get the checkbox map values
				final Map map = advForm.getValues();
				Logger.out.debug("map of shopping form:" + map);
				final Object[] obj = map.keySet().toArray();

				if (pageOff != null && pageOff.equals(Constants.PAGE_OF_QUERY_MODULE))
				{
					/*
					 * List spreadsheetColumns = (List) session
					 * .getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
					 */
					System.out.println("");
				}
				// Get the column Ids from session
				final Map columnIdsMap = (Map) session.getAttribute(Constants.COLUMN_ID_MAP);

				// Get the select column List from session to get the specimen
				// data
				final String[] selectedColumns = (String[]) session
						.getAttribute(Constants.SELECT_COLUMN_LIST);

				/**
				 * Name: Deepti Description: Query performance issue. Instead of
				 * saving complete query results in session, resultd will be
				 * fetched for each result page navigation. object of class
				 * QuerySessionData will be saved in session, which will contain
				 * the required information for query execution while navigating
				 * through query result pages. Here, as results are not stored
				 * in session, the sql is executed again to form the shopping
				 * cart list.
				 */
				int recordsPerPage = new Integer(recordsPerPageStr);
				int pageNum = new Integer(pageNo);
				final QuerySessionData querySessionData = (QuerySessionData) session
						.getAttribute(edu.wustl.common.util.global.Constants.QUERY_SESSION_DATA);
				if (isCheckAllAcrossAllChecked != null && isCheckAllAcrossAllChecked.equals("true"))
				{
					final Integer totalRecords = (Integer) session
							.getAttribute(Constants.TOTAL_RESULTS);
					recordsPerPage = totalRecords;
					pageNum = 1;
				}
				paginationDataList = AppUtility.getPaginationDataList(request, this
						.getSessionData(request), recordsPerPage, pageNum, querySessionData);

				request.setAttribute(Constants.PAGINATION_DATA_LIST, paginationDataList);

				Logger.out.debug("column ids map in shopping cart" + columnIdsMap);

				// get the specimen column id from the map
				final int specimenColumnId = ((Integer) columnIdsMap.get(Constants.SPECIMEN + "."
						+ Constants.IDENTIFIER)).intValue() - 1;
				Logger.out.debug("specimen column id in shopping cart" + specimenColumnId);
				int spreadsheetSpecimenIndex = -1;
				// get the column in which the specimen column id is displayed
				// in the spreadsheet data
				for (int k = 0; k < selectedColumns.length; k++)
				{
					if (selectedColumns[k].equals(Constants.COLUMN + specimenColumnId))
					{
						spreadsheetSpecimenIndex = k;
						break;
					}
				}
				Object[] selectedSpecimenIds = null;
				boolean isError = false;
				// Bug#2003: For having unique records in result view
				if (spreadsheetSpecimenIndex == -1)
				{
					final ActionErrors errors = new ActionErrors();
					final ActionError error = new ActionError("error.specimenId.add");
					errors.add(ActionErrors.GLOBAL_ERROR, error);
					this.saveErrors(request, errors);
					Logger.out.error("Specimen Id column not selected");
					target = new String(Constants.DUPLICATE_SPECIMEN);
					isError = true;
				}
				else if (isCheckAllAcrossAllChecked != null
						&& isCheckAllAcrossAllChecked.equals("true"))
				{
					final int listSize = paginationDataList.size();
					selectedSpecimenIds = new Object[listSize];
					for (int index = 0; index < listSize; index++)
					{
						final List selectedRow = (List) paginationDataList.get(index);
						Logger.out.debug("index selected :" + index);
						selectedSpecimenIds[index] = selectedRow.get(spreadsheetSpecimenIndex);
						Logger.out.debug("specimen id to be added to cart :"
								+ selectedSpecimenIds[index]);
					}
					System.out.println("selectedSpecimenIds  " + selectedSpecimenIds);
				}
				else
				{
					// Add to cart the selected specified Ids.
					selectedSpecimenIds = new Object[obj.length];
					for (int j = 0; j < obj.length; j++)
					{
						final String str = obj[j].toString();
						final StringTokenizer strTokens = new StringTokenizer(str, "_");
						strTokens.nextToken();
						final int index = Integer.parseInt(strTokens.nextToken());
						final List selectedRow = (List) paginationDataList.get(index);
						Logger.out.debug("index selected :" + index);
						selectedSpecimenIds[j] = selectedRow.get(spreadsheetSpecimenIndex);
						Logger.out.debug("specimen id to be added to cart :"
								+ selectedSpecimenIds[j]);
					}
					// Mandar 27-Apr-06 : bug 1129
				}
				try
				{
					bizLogic.add(cart, selectedSpecimenIds);
				}
				catch (final BizLogicException bizEx)
				{
					this.logger.error(bizEx.getMessage(), bizEx);
					final ActionErrors errors = new ActionErrors();
					final ActionError error = new ActionError("shoppingcart.error", bizEx
							.getMessage());
					errors.add(ActionErrors.GLOBAL_ERROR, error);
					this.saveErrors(request, errors);
					Logger.out.error(bizEx.getMessage(), bizEx);
					target = new String(Constants.DUPLICATE_SPECIMEN);
					isError = true;
				}
				session.setAttribute(Constants.SHOPPING_CART, cart);
				// List dataList = (List)
				// session.getAttribute(Constants.SPREADSHEET_DATA_LIST);
				final List columnList = (List) session
						.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
				request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnList);
				request.setAttribute(Constants.PAGE_OF, AQConstants.PAGEOF_QUERY_RESULTS);
				if (!isError)
				{
					target = Constants.SHOPPING_CART_ADD;
				}
			}
			else if (operation.equals(Constants.DELETE))
			{

				// Extracting map from formbean that gives rows to be deleted
				final Map map = advForm.getValues();
				this.logger.debug("map of shopping form:" + map);
				final Object[] obj = map.keySet().toArray();
				this.logger.debug("cart in shopping cart action " + cart.getCart());
				/*
				 * Deleting the selected rows from Shopping Cart object &
				 * settingit again in the session
				 */
				session.setAttribute(Constants.SHOPPING_CART, bizLogic.delete(cart, obj));
			}
			else if (operation.equals(Constants.EXPORT))
			{
				final String fileName = CommonServiceLocator.getInstance().getAppHome()
						+ System.getProperty("file.separator") + session.getId() + ".csv";

				// Extracting map from formbean that gives rows to be exported
				final Map map = advForm.getValues();
				final Object[] obj = map.keySet().toArray();

				final List cartList = bizLogic.export(cart, obj, fileName);
				final String delimiter = Constants.DELIMETER;
				// Exporting the data to shopping cart file & sending it to user
				final ExportReport report = new ExportReport(fileName);
				report.writeData(cartList, delimiter);
				report.closeFile();

				SendFile.sendFileToClient(response, fileName, "ShoppingCart.csv",
						"application/download");

				final String path = "/" + fileName;
				return new ActionForward(path);
			}

			else if (operation.equals("addToOrderList"))
			{
				this.addToOrderLiist(advForm, request, cart, session);
				target = new String("requestToOrder");
			}
			request.setAttribute(AQConstants.SPREADSHEET_DATA_LIST, this.makeGridData(cart));
		}
		// Sets the operation attribute to be used in the Add/Edit Shopping Cart
		// Page.
		request.setAttribute(Constants.OPERATION, operation);

		request.setAttribute(Constants.MENU_SELECTED, new String("18"));
		this.logger.debug(Constants.MENU_SELECTED + " set in ShoppingCart Action : 18  -- ");

		if (advForm.getValues().size() != 0)
		{
			if (session.getAttribute("OrderForm") == null)
			{
				final ActionErrors errors = new ActionErrors();
				final ActionError error = new ActionError("errors.order.alreadygiven");
				errors.add(ActionErrors.GLOBAL_ERROR, error);
				this.saveErrors(request, errors);
			}
			else
			{
				if (session.getAttribute("RequestedBioSpecimens") != null)
				{
					session.removeAttribute("RequestedBioSpecimens");
				}

				if (session.getAttribute("OrderForm") != null)
				{
					session.removeAttribute("OrderForm");
				}

				if (session.getAttribute("DefineArrayFormObjects") != null)
				{
					session.removeAttribute("DefineArrayFormObjects");
				}
			}
			request.setAttribute(Constants.IS_SPECIMENID_PRESENT, "true");
		}
		return mapping.findForward(target);
	}

	/**
	 * This function prepares the data in Grid Format.
	 * @param cart : cart
	 * @return List : list
	 */
	private List makeGridData(ShoppingCart cart)
	{
		final List gridData = new ArrayList();

		if (cart != null)
		{
			final Hashtable cartTable = cart.getCart();

			if (cartTable != null && cartTable.size() != 0)
			{
				final Enumeration cartIterator = cartTable.keys();
				int id = 0;

				while (cartIterator.hasMoreElements())
				{
					final String key = (String) cartIterator.nextElement();
					final Specimen specimen = (Specimen) cartTable.get(key);

					final List rowData = new ArrayList();

					// Adding checkbox as a first column of the grid
					// rowData.add("<input type='checkbox' name='value(CB_" +
					// specimen.getId() + ")' id='" + id +
					// "' onClick='changeData(this)'>");
					rowData.add(String.valueOf(specimen.getId()));
					rowData.add(String.valueOf(specimen.getId()));
					rowData.add(specimen.getClassName());

					if (specimen.getSpecimenType() != null)
					{
						rowData.add(specimen.getSpecimenType());
					}
					else
					{
						rowData.add("");
					}

					rowData.add(specimen.getSpecimenCharacteristics().getTissueSite());
					rowData.add(specimen.getSpecimenCharacteristics().getTissueSide());
					rowData.add(specimen.getPathologicalStatus());
					rowData.add("1");

					gridData.add(rowData);

					id++;
				}
			}
		}

		return gridData;
	}

	/**
	 *
	 * @param advForm : advForm
	 * @param request : request
	 * @param cart : cart
	 * @param session : session
	 */
	private void addToOrderLiist(AdvanceSearchForm advForm, HttpServletRequest request,
			ShoppingCart cart, HttpSession session)
	{
		final Map map = advForm.getValues();
		final Object[] obj = map.keySet().toArray();
		if (cart != null)
		{
			final Hashtable table = cart.getCart();
			if (table != null && table.size() != 0)
			{
				final List specimenIdList = new ArrayList();
				String strSpecimenId;
				for (final Object element : obj)
				{
					final String str = element.toString();

					final int index = str.indexOf("_") + 1;
					String key = str.substring(index);
					key = key.trim();
					final Specimen specimen = (Specimen) table.get(key);
					strSpecimenId = String.valueOf(specimen.getId());
					specimenIdList.add(strSpecimenId);
				}
				// request.setAttribute("specimenId", specimenIdList);
				session.setAttribute("specimenId", specimenIdList);
			}
		}
	}
}