package edu.wustl.catissuecore.action.querysuite;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.action.BaseAppletAction;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryengine.impl.CommonPathFinder;

public class PathFinderAction extends BaseAppletAction
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
			EntityInterface sourceEntity = (EntityInterface) inputDataMap.get(AppletConstants.SRC_ENTITY);
			EntityInterface destEntity = (EntityInterface) inputDataMap.get(AppletConstants.DEST_ENTITY);
			CommonPathFinder pathFinder = new CommonPathFinder();
			Map pathsMap = new HashMap<EntityInterface, List<IPath>>(); 
			List<IPath> allPossiblePaths = pathFinder.getAllPossiblePaths(sourceEntity, destEntity);
			pathsMap.put(AppletConstants.PATHS, allPossiblePaths);
			writeMapToResponse(response, pathsMap);
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
