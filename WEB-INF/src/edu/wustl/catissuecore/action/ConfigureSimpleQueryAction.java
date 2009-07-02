
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
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
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.simplequery.bizlogic.QueryBizLogic;

/**
 * @author renuka_bajpai
 *
 */
public class ConfigureSimpleQueryAction extends BaseAction
{

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
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//Set the tables for the configuration
		String pageOf = request.getParameter(Constants.PAGE_OF);
		if (pageOf.equals(Constants.PAGE_OF_SIMPLE_QUERY_INTERFACE))
		{
			SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;
			HttpSession session = request.getSession();
			Map map = simpleQueryInterfaceForm.getValuesMap();
			Logger.out.debug("Form map size" + map.size());
			Logger.out.debug("Form map" + map);
			if (map.size() == 0)
			{
				map = (Map) session
						.getAttribute(edu.wustl.simplequery.
								global.Constants.SIMPLE_QUERY_MAP);
				Logger.out.debug("Session map size" + map.size());
				Logger.out.debug("Session map" + map);
			}
			Iterator iterator = map.keySet().iterator();

			//Retrieve the size of the condition list to set size of array of tables.
			MapDataParser parser = new MapDataParser("edu.wustl.simplequery.query");

			Collection simpleConditionNodeCollection = parser.generateData(map, true);
			int counter = simpleConditionNodeCollection.size();
			String[] selectedTables = new String[counter];
			int tableCount = 0;
			while (iterator.hasNext())
			{
				String key = (String) iterator.next();
				Logger.out.debug("map key" + key);
				if (key.endsWith("_table"))
				{
					String table = (String) map.get(key);
					boolean exists = false;
					for (int arrayCount = 0; arrayCount < selectedTables.length; arrayCount++)
					{
						if (selectedTables[arrayCount] != null)
						{
							if (selectedTables[arrayCount].equals(table))
							{
								exists = true;
							}
						}
					}
					if (!exists)
					{
						selectedTables[tableCount] = table;
						tableCount++;
					}
				}
			}
			//Set the selected columns for population in the list of ConfigureResultView.jsp
			String[] selectedColumns = simpleQueryInterfaceForm.getSelectedColumnNames();
			if (selectedColumns == null)
			{
				selectedColumns = (String[]) session
						.getAttribute(Constants.CONFIGURED_SELECT_COLUMN_LIST);
				if (selectedColumns == null)
				{

					IFactory factory =
						AbstractFactoryConfig.getInstance().getBizLogicFactory();
					QueryBizLogic bizLogic = (QueryBizLogic) factory
							.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
					List columnNameValueBeans = new ArrayList();
					int i;
					for (i = 0; i < selectedTables.length; i++)
					{
						columnNameValueBeans.addAll(bizLogic
								.getColumnNames(selectedTables[i], true));
						//columnNameValueBeans
						//.addAll(bizLogic.setColumnNames(selectedTables[i]));
					}
					selectedColumns = new String[columnNameValueBeans.size()];
					Iterator columnNameValueBeansItr = columnNameValueBeans.iterator();
					i = 0;
					while (columnNameValueBeansItr.hasNext())
					{
						selectedColumns[i] =
							((NameValueBean) columnNameValueBeansItr.next())
								.getValue();
						i++;
					}
				}
				simpleQueryInterfaceForm.setSelectedColumnNames(selectedColumns);
			}
			session.setAttribute(Constants.TABLE_ALIAS_NAME, selectedTables);
			//session.setAttribute(Constants.SIMPLE_QUERY_COUNTER,new String(""+counter));
			//Logger.out.debug("counter in configure"+(String)
			//session.getAttribute(Constants.SIMPLE_QUERY_COUNTER));
			//Counter required for redefining the query
			map.put("counter", new String("" + counter));
			session.setAttribute(edu.wustl.simplequery.global.Constants.SIMPLE_QUERY_MAP, map);
		}

		request.setAttribute(Constants.PAGE_OF, pageOf);

		return (mapping.findForward(pageOf));
	}

}
