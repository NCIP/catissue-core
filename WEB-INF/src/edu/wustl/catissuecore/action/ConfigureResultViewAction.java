
package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.simplequery.bizlogic.QueryBizLogic;
import edu.wustl.simplequery.query.QueryTableData;

/**
 * @author renuka_bajpai
 *
 */
public class ConfigureResultViewAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(ConfigureResultViewAction.class);

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

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		//String target = new String();
		final IBizLogic bizlogic = factory.getBizLogic(Constants.CONFIGURE_RESULT_VIEW_ID);
		String pageOf = (String) request.getAttribute(Constants.PAGE_OF);
		if (pageOf == null)
		{
			pageOf = request.getParameter(Constants.PAGE_OF);
		}
		//String []tables = (String [])request.getAttribute(Constants.TABLE_ALIAS_NAME);
		final HttpSession session = request.getSession();

		//String []tables = (String[])session.getAttribute(Constants.TABLE_ALIAS_NAME);
		final Object[] tables = (Object[]) session.getAttribute(Constants.TABLE_ALIAS_NAME);
		final String sourceObjectName = QueryTableData.class.getName();
		final String[] displayNameField = {"displayName"};
		final String valueField = "aliasName";

		final String[] whereColumnNames = {"aliasName"};
		final String[] whereCondition = {"in"};
		final Object[] whereColumnValues = {tables};
		//List of objects containing TableNames and aliasName
		final List tableList = bizlogic.getList(sourceObjectName, displayNameField, valueField,
				whereColumnNames, whereCondition, whereColumnValues, null, null);

		//List of Column data corresponding to table names.
		/*sourceObjectName = QueryColumnData.class.getName();
		String valueField1 = "columnName";
		String [] whereCondition1 = {"="};
		whereColumnNames[0] = "tableData.identifier";*/

		final Map tableColumnDataMap = new HashMap();

		final Iterator itr = tableList.iterator();
		while (itr.hasNext())
		{
			final NameValueBean tableData = (NameValueBean) itr.next();
			if (!tableData.getName().equals(Constants.SELECT_OPTION))
			{
				final QueryBizLogic bizLogic = (QueryBizLogic) factory
						.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
				final List columnList = bizLogic.setColumnNames(tableData.getValue());
				tableColumnDataMap.put(tableData, columnList);

			}
			this.logger.debug("Table Name" + tableData.getValue());
			//Logger.out.debug("Column List"+ columnList);

		}

		this.logger.debug("Table Map" + tableColumnDataMap);
		request.setAttribute(Constants.TABLE_COLUMN_DATA_MAP, tableColumnDataMap);
		request.setAttribute(Constants.PAGE_OF, pageOf);
		this.logger.debug("pageOf in configure result view:" + pageOf);
		/*if(pageOf.equals(Constants.PAGE_OF_SIMPLE_QUERY_INTERFACE))
			target = Constants.PAGE_OF_SIMPLE_QUERY_INTERFACE;
		else if(pageOf.equals(Constants.PAGE_OF_QUERY_RESULTS))
			target = Constants.PAGE_OF_QUERY_RESULTS;*/

		return mapping.findForward(pageOf);
		//return mapping.findForward("success");
	}

}