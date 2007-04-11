
package edu.wustl.catissuecore.action.querysuite;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.action.BaseAppletAction;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.CreateQueryObjectBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.querysuite.queryobject.IQuery;

/**
 * This action is called when user clicks on Add Limits button of AddLimits.jsp.
 * This action is called from DiagrammaticViewApplet class.
 * This class creates Query Object and also generates validation messages with the help of CreateQueryObjectBizLogic. 
 * @author deepti_shelar
 */
public class AddToLimitSetAction extends BaseAppletAction
{
	/**
	 * This method gets the input strings from the DiagrammaticViewApplet class.
	 * A call to CreateQueryObjectBizLogic returns map which holds details for the rule added by user.
	 * This map is again sent back to applet class to add to the applet panel.
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
			IQuery query = (IQuery) inputDataMap.get(AppletConstants.QUERY_OBJECT);
			HttpSession session = request.getSession();
			session.setAttribute(AppletConstants.QUERY_OBJECT, query);
			String strToCreateQueryObject = (String) inputDataMap.get(AppletConstants.STR_TO_CREATE_QUERY_OBJ);
			String entityName = (String) inputDataMap.get(AppletConstants.ENTITY_NAME);
			Map searchedEntitiesMap = (Map)session.getAttribute(Constants.SEARCHED_ENTITIES_MAP);
			EntityInterface entity = (Entity) searchedEntitiesMap.get(entityName);
			addEntityToSession(entity,request);
			CreateQueryObjectBizLogic queryBizLogic = new CreateQueryObjectBizLogic();
			if (!strToCreateQueryObject.equalsIgnoreCase(""))
			{
				Map ruleDetailsMap = queryBizLogic.getRuleDetailsMap(strToCreateQueryObject, entity);
				writeMapToResponse(response, ruleDetailsMap);
			}
		}
		return null;
	}
	/**
	 * This method first checks the wheather the list of entitites is present in session , if not then creates a new list and then
	 * adds the entity to list and keeps the list again in the session. 
	 * @param entity Entity
	 * @param request HttpServletRequest
	 */
	private void addEntityToSession(EntityInterface entity, HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		List listOfEntitiesInQuery = (List)session.getAttribute(Constants.LIST_OF_ENTITIES_IN_QUERY);
		if(listOfEntitiesInQuery == null)
		{
			listOfEntitiesInQuery = new ArrayList<Entity>();
		}
		listOfEntitiesInQuery.add(entity);
		session.setAttribute(Constants.LIST_OF_ENTITIES_IN_QUERY,listOfEntitiesInQuery);
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
