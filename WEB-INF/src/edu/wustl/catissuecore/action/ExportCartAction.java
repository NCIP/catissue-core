package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.query.actionForm.AdvanceSearchForm;
import edu.wustl.query.querysuite.QueryShoppingCart;

/**
 * @author santhoshkumar_c
 *
 */
public class ExportCartAction extends QueryShoppingCartAction
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
		export(getCheckboxValues(searchForm), request, response);
		return null;								
	}
	
	/**
	 * Export Cart data.
	 * 
	 * @param cart a shopping cart object preset in session. 
	 * @param request HttpServletRequest.
	 * @param response HttpServletResponse.
	 * @param chkBoxValues
	*/
	public void export(List<Integer> chkBoxValues, HttpServletRequest request, 
			HttpServletResponse response)
	{		
		HttpSession session = request.getSession();
		QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		QueryShoppingCart cart = (QueryShoppingCart) session
		.getAttribute(Constants.QUERY_SHOPPING_CART);
		List<List<String>> exportList = bizLogic.export(cart, chkBoxValues);		

		// Exporting the data to the given file & sending it to user
		try
		{
			ExportReport report = new ExportReport(getFileName(session));
			report.writeData(exportList, Constants.DELIMETER);
			report.closeFile();
		}
		catch (IOException e)
		{
			ActionErrors errors = new ActionErrors();
			ActionError error = new ActionError("shoppingcart.exportfilexception");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			saveErrors(request, errors);
		}

		SendFile.sendFileToClient(response, getFileName(session), Constants.SHOPPING_CART_FILE_NAME,
				Constants.APPLICATION_DOWNLOAD);

	}

	/**
	 * @param session HttpSession object.
	 * @return file name.
	 */
	private String getFileName(HttpSession session) {
		String fileName = CommonServiceLocator.getInstance().getAppHome() + System.getProperty("file.separator")
				+ session.getId() + Constants.DOT_CSV;
		return fileName;
	}

}
