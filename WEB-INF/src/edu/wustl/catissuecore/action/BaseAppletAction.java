
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.applet.model.AppletModelInterface;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.util.logger.Logger;

/**
 * This action provides common base to all the action that handles request from
 * an applet.
 *
 * @author Rahul Ner.
 */
public abstract class BaseAppletAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(BaseAppletAction.class);

	/**
	 * This method write input map to the response in the form of
	 * BaseAppletModel.
	 *
	 * @param response : response
	 * @param outputMap
	 *            Map that need to send to applet.
	 * @throws Exception : Exception
	 */
	protected void writeMapToResponse(HttpServletResponse response, Map outputMap) throws Exception
	{
		final ObjectOutputStream outputStream = new ObjectOutputStream(response.getOutputStream());
		final BaseAppletModel appletModel = new BaseAppletModel();
		appletModel.setData(outputMap);
		outputStream.writeObject(appletModel);
		outputStream.close();
	}

	/**
	 * This method reads AppletModelInterface from request and return the map
	 * inside it.
	 * @param request : request
	 * @return Map : Map
	 * @throws IOException : IOException
	 * @throws ClassNotFoundException : ClassNotFoundException
	 */
	protected Map readMapFromRequest(HttpServletRequest request) throws IOException,
			ClassNotFoundException
	{
		final ObjectInputStream inputStream = new ObjectInputStream(request.getInputStream());
		final AppletModelInterface model = (AppletModelInterface) inputStream.readObject();
		inputStream.close();
		return model.getData();
	}

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
	 */
	@Override
	protected void preExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			final Map inputMap = this.readMapFromRequest(request);
			request.setAttribute(Constants.INPUT_APPLET_DATA, inputMap);
		}
		catch (final Exception excep)
		{
			request.setAttribute(Constants.INPUT_APPLET_DATA, null);
			this.logger.error(excep.getMessage(),excep);
			excep.printStackTrace() ;
		}
	}

	/**
	 * This method is overided do save input map before reading anything from
	 * request.
	 *
	 * @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	// public ActionForward execute(ActionMapping actionMapping, ActionForm
	// actionForm,
	// HttpServletRequest request, HttpServletResponse response) throws
	// Exception
	// {
	// try {
	// Map inputMap = readMapFromRequest(request);
	// request.setAttribute(Constants.INPUT_APPLET_DATA, inputMap);
	// } catch (Exception e) {
	//
	// request.setAttribute(Constants.INPUT_APPLET_DATA, null);
	// }
	//
	// return super.execute(actionMapping, actionForm, request, response);
	// }
	// --------- Changes By Mandar : 05Dec06 for Bug 2866
	// --------- Extending SecureAction.
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
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// ---- Code from execute
		// try {
		// Map inputMap = readMapFromRequest(request);
		// request.setAttribute(Constants.INPUT_APPLET_DATA, inputMap);
		// } catch (Exception e) {
		//
		// request.setAttribute(Constants.INPUT_APPLET_DATA, null);
		// }
		// ---- Code from execute

		// -- code for handling method calls
		final String methodName = request.getParameter(Constants.METHOD_NAME);
		if (methodName != null)
		{
			return this.invokeMethod(methodName, mapping, form, request, response);
		}
		return null;
	}

	/**
	 * @param methodName : methodName
	 * @param mapping : mapping
	 * @param form : form
	 * @param request : request
	 * @param response : response
	 * @return ActionForward : ActionForward
	 * @throws Exception : Exception
	 */
	@Override
	protected abstract ActionForward invokeMethod(String methodName, ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception;

	/**
	 * This method returns the method with the specified name if the method
	 * exists. Return null other wise.
	 * @param name : name
	 * @param className : className
	 * @return Method : Method
	 */
	@Override
	protected Method getMethod(String name, Class className)
	{
		// argument types
		final Class[] types = {ActionMapping.class, ActionForm.class, HttpServletRequest.class,
				HttpServletResponse.class};
		try
		{
			final Method method = className.getDeclaredMethod(name, types);
			return method;
		}
		catch (final NoSuchMethodException excp1)
		{
			this.logger.error(excp1.getMessage(), excp1);
			excp1.printStackTrace();
		}
		catch (final NullPointerException excp2)
		{
			this.logger.error(excp2.getMessage(), excp2);
			excp2.printStackTrace() ;
		}
		catch (final SecurityException excp3)
		{
			this.logger.error(excp3.getMessage(), excp3);
			excp3.printStackTrace() ;
		}
		return null;
	}

}
