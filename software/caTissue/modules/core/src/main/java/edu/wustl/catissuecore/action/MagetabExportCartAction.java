
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.bizlogic.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.bizlogic.magetab.MagetabExportWizardBean;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author santhoshkumar_c
 * @author Alexander Zgursky
 */
public class MagetabExportCartAction extends QueryShoppingCartAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(MagetabExportCartAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
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
		final AdvanceSearchForm searchForm = (AdvanceSearchForm) form;
		List<Integer> chkBoxValues = this.getCheckboxValues(searchForm);
		
		final HttpSession session = request.getSession();
		final QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		final QueryShoppingCart cart = (QueryShoppingCart) session
				.getAttribute(Constants.QUERY_SHOPPING_CART);
		
		final List<List<String>> exportList = bizLogic.export(cart, chkBoxValues);
		List<String> columnList = exportList.get(0);
		
		int idColumnIndex = -1;
		for (int i = 0; i < columnList.size(); i++) {
			final String columnName = columnList.get(i).trim();
			if (columnName.equalsIgnoreCase("ID") || columnName.equalsIgnoreCase("Id : Specimen")) {
				idColumnIndex = i;
				break;
			}
		}
		
		if (idColumnIndex <= 0) {
			throw new RuntimeException("Couldn't find primary key column in search results"); 
		}
		
		List<Long> specimenIds = new LinkedList<Long>();
		
//		Set<String> strIds = bizLogic.getEntityIdsList(
//				cart, 
//				Arrays.asList(new String[] {"Specimen"}), 
//				chkBoxValues);
//
//		for (String strId : strIds) {
//			specimenIds.add(Long.parseLong(strId));
//		}
//		
		List<List<String>> dataList = exportList.subList(1, exportList.size());
		for (List<String> row : dataList) {
			specimenIds.add(Long.parseLong(row.get(idColumnIndex)));
		}
		
		MagetabExportWizardBean wizardBean = (MagetabExportWizardBean)
				session.getAttribute(MagetabExportWizardBean.MAGETAB_EXPORT_WIZARD_BEAN);
		if (wizardBean == null) {
			wizardBean = new MagetabExportWizardBean();
			session.setAttribute(MagetabExportWizardBean.MAGETAB_EXPORT_WIZARD_BEAN, wizardBean);
		}
		wizardBean.init(specimenIds);
		return mapping.findForward("startWizard");
		
		
		
		
		
//		
//		
//		// Exporting the data to the given file & sending it to user
//		try
//		{
//			magetabExport(cart, chkBoxValues);
////			final ExportReport report = new ExportReport(this.getFileName(session));
////			report.writeData(exportList, Constants.DELIMETER);
////			report.closeFile();
//		}
//		catch (final IOException e)
//		{
//			this.logger.error(e.getMessage(), e);
//			final ActionErrors errors = new ActionErrors();
//			final ActionError error = new ActionError("shoppingcart.exportfilexception");
//			errors.add(ActionErrors.GLOBAL_ERROR, error);
//			this.saveErrors(request, errors);
//		}

//		SendFile.sendFileToClient(response, this.getFileName(session),
//				Constants.SHOPPING_CART_FILE_NAME, Constants.APPLICATION_DOWNLOAD);

	}
	
	private void magetabExport(QueryShoppingCart cart, List<Integer> chkBoxValues) throws IOException {
		
	}

	/**
	 * @param session
	 *            HttpSession object.
	 * @return file name.
	 */
	private String getFileName(HttpSession session)
	{
		final String fileName = CommonServiceLocator.getInstance().getAppHome()
				+ System.getProperty("file.separator") + session.getId() + Constants.DOT_CSV;
		return fileName;
	}

}
