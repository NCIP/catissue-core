
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

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.action.BaseAppletAction;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.querysuite.queryobject.IQuery;

/**
 * This action is called from DiagrammaticViewApplet class.
 * This class gets Query Object from session and sends it to client. 
 * This class also sets the query obj to session.
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
	 * This method gets Query Object from session and sends it to client. 
	 * @param mapping mapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	public ActionForward getData(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Map<String,EntityInterface> entityDataMap = new HashMap<String,EntityInterface>();
		Map entityMap = (Map)request.getSession().getAttribute(Constants.SEARCHED_ENTITIES_MAP);
		Map inputDataMap = (Map) request.getAttribute(Constants.INPUT_APPLET_DATA);
		if (inputDataMap != null && !inputDataMap.isEmpty())
		{
			String[] entityArr = (String[])inputDataMap.get(AppletConstants.ENTITY_STR);
			for(int i=0;i<entityArr.length; i++)
			{
				String entityName = entityArr[i];
				EntityInterface entity = (EntityInterface)entityMap.get(entityName);
				entityDataMap.put(entityName, entity);
			}
		}
		writeMapToResponse(response, entityDataMap);
		return null;
	}
	/**
	 * This method sets Query Object to session . 
	 * @param mapping mapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	public ActionForward setData(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Map inputDataMap = (Map) request.getAttribute(Constants.INPUT_APPLET_DATA);
		if (inputDataMap != null && !inputDataMap.isEmpty())
		{
			IQuery query = (IQuery) inputDataMap.get(AppletConstants.QUERY_OBJECT);
			HttpSession session = request.getSession();
			session.setAttribute(AppletConstants.QUERY_OBJECT, query);
		}
		writeMapToResponse(response, inputDataMap);
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
