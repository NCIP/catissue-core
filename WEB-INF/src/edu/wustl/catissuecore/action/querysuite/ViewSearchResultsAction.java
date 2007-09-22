
package edu.wustl.catissuecore.action.querysuite;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.action.BaseAppletAction;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * This action is an applet action called from DiagrammaticViewApplet class when user clicks on seach button of AddLimits.jsp 
 * and defineresultsView.jsp.This class gets IQuery Object from the applet and generates sql out of it with the help of sqlGenerator.
 * This sql is then fired and each time a new table is created in database for each session user with the help of QueryOutputTreeBizLogic .
 * Then data for first level (default) tree and spreadsheet is generated and returned back from QueryOutputTreeBizLogic and QueryOutputSpreadsheetBizLogic.
 * These results are stored in session so that they can be retrived at the time of loading of result's screen.
 * An empty map is sent back to the applet. 
 * @author deepti_shelar
 */
public class ViewSearchResultsAction extends BaseAppletAction
{

	/**
	 * This method gets IQuery Object from the applet and also generates sql out of it with the help of sqlGenerator.
	 * This sql is then fired and each time a new table is created in database for each session user with the help of QueryOutputTreeBizLogic .
	 * Then data for first level (default) tree and spreadsheet is generated and returned back from QueryOutputTreeBizLogic and QueryOutputSpreadsheetBizLogic.
	 * These results are stored in session so that they can be retirved at the time of loading of result's screen.
	 * An empty map is sent back to the applet. 
	 * @param mapping mapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	public ActionForward initData(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Map inputDataMap = (Map) request.getAttribute(Constants.INPUT_APPLET_DATA);
		if (inputDataMap != null && !inputDataMap.isEmpty())
		{
			IQuery query = (IQuery) inputDataMap.get(AppletConstants.QUERY_OBJECT);
			Map<String, String> validationMessagesMap = new HashMap<String, String>();
			String errorMessage = null;

			HttpSession session = request.getSession();
			int errorCode = QueryModuleUtil.searchQuery(session, query,null);
			switch (errorCode)
			{
				case QueryModuleUtil.EMPTY_DAG :
					errorMessage = ApplicationProperties.getValue("query.empty.dag");
					break;
				case QueryModuleUtil.MULTIPLE_ROOT :
					errorMessage = ApplicationProperties
							.getValue("errors.executeQuery.multipleRoots");
					break;
				case QueryModuleUtil.NO_RESULT_PRESENT :
					errorMessage = ApplicationProperties.getValue("query.zero.records.present");
					break;
				case QueryModuleUtil.SQL_EXCEPTION :
				case QueryModuleUtil.DAO_EXCEPTION :
				case QueryModuleUtil.CLASS_NOT_FOUND :
					errorMessage = ApplicationProperties
							.getValue("errors.executeQuery.genericmessage");
			}

			if (errorMessage != null)
			{
				validationMessagesMap.put(AppletConstants.ERROR_MESSAGE, errorMessage);
			}

			writeMapToResponse(response, validationMessagesMap);
		}

		return null;
	}

	/**
	 * This is a overloaded method to call the actions method set by applet class.
	 * @param methodName String
	 * @param mapping ActionMapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	protected ActionForward invokeMethod(String methodName, ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		if (methodName.trim().length() > 0)
		{
			Method method = getMethod(methodName, this.getClass());
			if (method != null)
			{
				Object args[] = {mapping, form, request, response};
				return (ActionForward) method.invoke(this, args);
			}
			else
				return null;
		}
		return null;
	}
}
