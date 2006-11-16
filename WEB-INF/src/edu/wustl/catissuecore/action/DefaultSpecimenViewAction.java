/**
 * <p>Title: DefaultSpecimenViewAction Class>
 * <p>Description:	This class sets the Specimen View for the Advance Search result View page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Prafull Kadam
 * @version 1.00
 * Created on Sept 23, 2006
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
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
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

public class DefaultSpecimenViewAction extends BaseAction
{

	/**
	 * Overrides the execute method in Action class.
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String target = Constants.SUCCESS;
		HttpSession session = request.getSession();
		String isDefaultViewStr = request.getParameter(Constants.SPECIMENT_VIEW_ATTRIBUTE);
		boolean isDefaultView = false;
		AdvanceSearchForm theForm = (AdvanceSearchForm) form;

		if (isDefaultViewStr != null && isDefaultViewStr.equals(Constants.TRUE))
		{
			isDefaultView = true;
		}

		Boolean defaultViewAttribute = new Boolean(isDefaultView);
		if (isDefaultView)
		{
			QueryBizLogic bizLogic = (QueryBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.SIMPLE_QUERY_INTERFACE_ID);
			//    		List columnNameValueBeans = bizLogic.getColumnNames(Constants.SPECIMEN,true);
			List columnNameValueBeans = bizLogic.getColumnNames(request.getParameter("view"), true);
			Map columnIdsMap = (Map) session.getAttribute(Constants.COLUMN_ID_MAP);

			String[] selectColumnNames = new String[columnNameValueBeans.size()];
			;
			List columnDisplayNames = new ArrayList();
			String[] configuredColumns = new String[columnNameValueBeans.size()];
			;

			Iterator columnNameValueBeansItr = columnNameValueBeans.iterator();
			int i = 0;
			while (columnNameValueBeansItr.hasNext())
			{
				NameValueBean nameValueBean = (NameValueBean) columnNameValueBeansItr.next();
				configuredColumns[i] = nameValueBean.getValue();

				StringTokenizer selectedColumnsTokens = new StringTokenizer(configuredColumns[i],
						".");
				//get the column Id of the selected column to search in the temporary table
				int columnId = ((Integer) columnIdsMap.get(selectedColumnsTokens.nextToken() + "."
						+ selectedColumnsTokens.nextToken())).intValue() - 1;
				Logger.out.debug("column id of configured column" + columnId);
				columnDisplayNames.add(selectedColumnsTokens.nextToken());
				selectColumnNames[i] = Constants.COLUMN + columnId;
				i++;
			}

			AdvanceSearchForm advForm = (AdvanceSearchForm) form;
			advForm.setSelectedColumnNames(selectColumnNames);
			session.setAttribute(Constants.CONFIGURED_SELECT_COLUMN_LIST, selectColumnNames);
			session.setAttribute(Constants.CONFIGURED_COLUMN_DISPLAY_NAMES, columnDisplayNames);
			session.setAttribute(Constants.CONFIGURED_COLUMN_NAMES, configuredColumns);
			request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnDisplayNames);
			session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnDisplayNames);
			SessionDataBean sessionData = getSessionData(request);
			//temporary table name
			String tableName = Constants.QUERY_RESULTS_TABLE + "_" + sessionData.getUserId();
			JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
        	jdbcDAO.openSession(sessionData);
	        try
			{
	        	List list = jdbcDAO.retrieve(tableName, selectColumnNames, true);
				request.setAttribute(Constants.SPREADSHEET_DATA_LIST, list);
				session.setAttribute(Constants.SPREADSHEET_DATA_LIST, list);
				session.setAttribute(Constants.SPECIMENT_VIEW_ATTRIBUTE, defaultViewAttribute);
				request.setAttribute(Constants.SPECIMENT_VIEW_ATTRIBUTE, defaultViewAttribute);
			}
	        catch(DAOException exp)
			{
	        	Logger.out.error(exp.getMessage(),exp);
	        	ActionErrors errors = new ActionErrors();
    			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("advanceQuery.userAlreadyLoggedIn"));
    			saveErrors(request, errors);
	        	target= Constants.FAILURE;
			}
			finally
			{
	            jdbcDAO.closeSession();
			}
		}
		else
		{
			session.removeAttribute(Constants.CONFIGURED_SELECT_COLUMN_LIST);
			session.removeAttribute(Constants.CONFIGURED_COLUMN_DISPLAY_NAMES);
			session.removeAttribute(Constants.CONFIGURED_COLUMN_NAMES);
			session.removeAttribute(Constants.SPREADSHEET_COLUMN_LIST);
			session.removeAttribute(Constants.SPREADSHEET_DATA_LIST);
			session.removeAttribute(Constants.SPECIMENT_VIEW_ATTRIBUTE);
			request.setAttribute(Constants.SPECIMENT_VIEW_ATTRIBUTE, defaultViewAttribute);
		}

		request.setAttribute(Constants.PAGEOF, Constants.PAGEOF_QUERY_RESULTS);
		return mapping.findForward(target);
	}

}