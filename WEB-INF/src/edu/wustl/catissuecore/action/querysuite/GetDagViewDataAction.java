
package edu.wustl.catissuecore.action.querysuite;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.action.BaseAppletAction;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.common.querysuite.queryobject.IQuery;

/**
 * This action is called from DiagrammaticViewApplet class.
 * This class gets Query Object from session and sends it to client. 
 * @author deepti_shelar
 */
public class GetDagViewDataAction extends BaseAppletAction
{
	/**
	 * This method gets Query Object from session and sends it to client. 
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
		IQuery queryObject = (IQuery)request.getSession().getAttribute(AppletConstants.QUERY_OBJECT);
		Map<String,IQuery> queryObjMap = new HashMap<String,IQuery>();
		queryObjMap.put(AppletConstants.QUERY_OBJECT, queryObject);		
		writeMapToResponse(response, queryObjMap);
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
	protected ActionForward invokeMethod(String methodName, ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
			{
		if (methodName.trim().length() > 0)
		{
			Method method = getMethod(methodName, this.getClass());
			if (method != null)
			{
				Object args[] = {mapping, form, request, response};
				return (ActionForward) method.invoke(this, args);
			}
		}
		return null;
			}
}
