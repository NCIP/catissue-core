package edu.wustl.catissuecore.action;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.bizlogic.magetab.MagetabExportBizLogic;
import edu.wustl.catissuecore.bizlogic.magetab.MagetabExportWizardBean;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.QuerySessionData;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Alexander Zgursky
 */
public class MagetabExportAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(MagetabExportAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response : response obj
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AdvanceSearchForm searchForm = (AdvanceSearchForm) form;
		HttpSession session = request.getSession();
		
		String isCheckAllAcrossAllChecked = request.getParameter(
				Constants.CHECK_ALL_ACROSS_ALL_PAGES);

		Set<String> selectedRowsSNs = searchForm.getValues().keySet();
		

		QuerySessionData querySessionData = (QuerySessionData) session.getAttribute(
				edu.wustl.common.util.global.Constants.QUERY_SESSION_DATA);
//		SessionDataBean sessionDataBean = (SessionDataBean) session.getAttribute(
//				edu.wustl.common.util.global.Constants.SESSION_DATA);
		int recordsPerPage = new Integer((String) session.getAttribute(
				Constants.RESULTS_PER_PAGE));
		List<String> columnList = (List<String>) session.getAttribute(
				Constants.SPREADSHEET_COLUMN_LIST);
		
	logger.debug("the Number of columnlist items are "+columnList.size());
	for(String temp:columnList)
		logger.debug("the column list is "+temp);
		
		final String pageNo = request.getParameter(Constants.PAGE_NUMBER);
		if (pageNo != null)	{
			request.setAttribute(Constants.PAGE_NUMBER, pageNo);
		}
		int pageNum = new Integer(pageNo);
		if (isCheckAllAcrossAllChecked != null
				&& isCheckAllAcrossAllChecked.equalsIgnoreCase("true"))
		{
			final Integer totalRecords = (Integer) session.getAttribute(Constants.TOTAL_RESULTS);
			recordsPerPage = totalRecords;
			pageNum = 1;
		}
		
		//dataList is the number of records of specimens selected.
		final List<List<String>> dataList = AppUtility.getPaginationDataList(request, this
				.getSessionData(request), recordsPerPage, pageNum, querySessionData);
		logger.debug("The number of specimens selected are "+dataList.size());
		
		int idColumnIndex = -1;
		int typeColumnIndex = -1;
		for (int i = 0; i < columnList.size(); i++) {
			final String columnName = columnList.get(i).trim();
			if (columnName.equalsIgnoreCase("ID") || columnName.equalsIgnoreCase("Identifier : Specimen") || columnName.equalsIgnoreCase("Id : Specimen")) {
				idColumnIndex = i;
				//break;
			}
			if (columnName.equalsIgnoreCase("Type : Specimen")) {
				typeColumnIndex = i;
			}
		}
		
		if (idColumnIndex <= 0) {
			throw new RuntimeException("Couldn't find primary key column in search results"); 
		}
		
		// specimen ids store the ids of the specimens selected for mage tab export.
		List<Long> specimenIds = new LinkedList<Long>();
		boolean dna = false;
		boolean rna = false;
		if (isCheckAllAcrossAllChecked != null
				&& isCheckAllAcrossAllChecked.equalsIgnoreCase("true"))
		{
			for (int i = 0; i < dataList.size(); i++) {
				List<String> row = dataList.get(i);
				specimenIds.add(Long.parseLong(row.get(idColumnIndex)));				
			}
			logger.debug("All Specimens are added and the count is "+specimenIds.size());
		} else {
			for (final String rowSN : selectedRowsSNs) {
				int indexOf = rowSN.indexOf("_") + 1;
				int index = Integer.parseInt(rowSN.substring(indexOf));
				List<String> list = dataList.get(index);
				specimenIds.add(Long.parseLong(list.get(idColumnIndex)));
				if (list.get(typeColumnIndex) != null && list.get(typeColumnIndex).toString().equals("DNA")) {
					dna = true;
				}
				if (list.get(typeColumnIndex) != null && list.get(typeColumnIndex).toString().equals("RNA")) {
					rna = true;
				}
			}
		}
		
		MagetabExportWizardBean wizardBean = (MagetabExportWizardBean)
				session.getAttribute(MagetabExportWizardBean.MAGETAB_EXPORT_WIZARD_BEAN);
		if (wizardBean == null) {
			wizardBean = new MagetabExportWizardBean();
			session.setAttribute(MagetabExportWizardBean.MAGETAB_EXPORT_WIZARD_BEAN, wizardBean);
		}
		wizardBean.init(specimenIds);
		wizardBean.setDna(dna);
		wizardBean.setRna(rna);
		return mapping.findForward("startWizard");
	}
}