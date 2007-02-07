
package edu.wustl.catissuecore.action.querysuite;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.action.BaseAppletAction;
import edu.wustl.catissuecore.bizlogic.querysuite.CreateQueryObjectBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputTreeBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.factory.SqlGeneratorFactory;
import edu.wustl.common.querysuite.queryengine.ISqlGenerator;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.util.logger.Logger;

/**
 * This action is a applet action called from DiagrammaticViewApplet class when user clicks on seach button of AddLimits.jsp.
 * This class gets IQuery Object from the applet and also generates sql out of it with the help of CreateQueryObjectBizLogic.
 * This sql is then fired and the results are stored in session so that then they can be seen on ViewSeachResults jsp screen.
 * An emplty map is sent back to the applet. 
 * @author deepti_shelar
 */
public class ViewSearchResultsAction extends BaseAppletAction
{
	/**
	 * This method gets the input strings from the DiagrammaticViewApplet class.
	 * A call to CreateQueryObjectBizLogic returns map which holds list of the result data for the rules added by user.
	 * @param mapping mapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	public ActionForward initData(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Map inputDataMap = (Map) request.getAttribute(Constants.INPUT_APPLET_DATA);
		if (inputDataMap != null && !inputDataMap.isEmpty())
		{
			IQuery query = (IQuery) inputDataMap.get("queryObject");
			QueryOutputTreeBizLogic outputTreeBizLogic = new QueryOutputTreeBizLogic();
			SessionDataBean sessionData = getSessionData(request);
			Vector treeData = outputTreeBizLogic.createOutputTree(query,sessionData);
			ISqlGenerator sqlGenerator = SqlGeneratorFactory.getInstance();
			String selectSql = "";
			try
			{
				selectSql = sqlGenerator.generateSQL(query);
				System.out.println(selectSql);
			}
			catch (MultipleRootsException e)
			{
				e.printStackTrace();
			}
			catch (SqlException e)
			{
				e.printStackTrace();
			}
			Logger.out.info(selectSql);
			CreateQueryObjectBizLogic bizLogic = new CreateQueryObjectBizLogic();
			Map outputData = bizLogic.fireQuery(selectSql);
			request.getSession().setAttribute(Constants.TREE_DATA, treeData);
			request.getSession().setAttribute(Constants.SPREADSHEET_DATA_LIST, outputData.get(Constants.SPREADSHEET_DATA_LIST));
			request.getSession().setAttribute(Constants.SPREADSHEET_COLUMN_LIST, outputData.get(Constants.SPREADSHEET_COLUMN_LIST));;
			Map ruleDetailsMap = new HashMap();
			writeMapToResponse(response, ruleDetailsMap);
		}
		return null;
	}

	/**
	 * This is a overloaded method to call the actions method set bt applet class.
	 * @param methodName String
	 * @param mapping ActionMapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	protected ActionForward invokeMethod(String methodName, ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception
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
