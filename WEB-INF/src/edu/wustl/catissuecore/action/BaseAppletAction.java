
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
 * This action provides common base to all the action that handles request from an applet.
 * 
 * @author Rahul Ner.
 */
public abstract class BaseAppletAction extends SecureAction
{

	private transient Logger logger = Logger.getCommonLogger(BaseAppletAction.class);
	/**
	 * This method write input map to the response in the form of BaseAppletModel.
	 * 
	 * @param  response 
	 * @param  outputMap Map that need to send to applet.
	 * @throws Exception
	 */
	protected void writeMapToResponse(HttpServletResponse response, Map outputMap) throws Exception
	{
		ObjectOutputStream outputStream = new ObjectOutputStream(response.getOutputStream());
		BaseAppletModel appletModel = new BaseAppletModel();
		appletModel.setData(outputMap);
		outputStream.writeObject(appletModel);
		outputStream.close();
	}

	
	/**
	 * This method reads  AppletModelInterface from request 
	 * and return the map inside it.
	 * 
	 * @param request 
	 * @return map inside AppletModelInterface
	 * @throws Exception
	 */
	protected Map readMapFromRequest(HttpServletRequest request) throws IOException,ClassNotFoundException
	{
		ObjectInputStream inputStream = new ObjectInputStream(request.getInputStream());
		AppletModelInterface model = (AppletModelInterface) inputStream.readObject();
		inputStream.close(); 
		return model.getData();
	}
	
	/*
	 * Overriden from BaseAction.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	protected void preExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		try {
			Map inputMap = readMapFromRequest(request);
			request.setAttribute(Constants.INPUT_APPLET_DATA, inputMap);
		} catch (Exception e) {
			request.setAttribute(Constants.INPUT_APPLET_DATA, null);
		}
	}
	
	

	/**
	 * This method is overided do save input map before reading anything from request.
	 * 
	 * @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
//	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
//			HttpServletRequest request, HttpServletResponse response) throws Exception
//	{
//		try {
//			Map inputMap = readMapFromRequest(request);
//			request.setAttribute(Constants.INPUT_APPLET_DATA, inputMap);
//		} catch (Exception e) {
//			
//			request.setAttribute(Constants.INPUT_APPLET_DATA, null);
//		}
//
//		return super.execute(actionMapping, actionForm, request, response);
//	}
	
	// --------- Changes By  Mandar : 05Dec06 for Bug 2866 
	// --------- Extending SecureAction.
	
	/**
     * Overrides the executeSecureAction method of SecureAction class.
     * */
    protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
    	//---- Code from execute 
//		try {
//			Map inputMap = readMapFromRequest(request);
//			request.setAttribute(Constants.INPUT_APPLET_DATA, inputMap);
//		} catch (Exception e) {
//			
//			request.setAttribute(Constants.INPUT_APPLET_DATA, null);
//		}
		//		---- Code from execute
		
		//-- code for handling method calls
		String methodName = request.getParameter(Constants.METHOD_NAME);
		if(methodName!= null)
		{
			return invokeMethod(methodName, mapping, form, request, response);
		}
		return null;
    }
/*
 * This method calls the specified method passed as parameter.
 * 
 */
    protected abstract ActionForward invokeMethod(String methodName, ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception ;

    /*
     * This method returns the method with the specified name if the method exists. Return null other wise.
     */
	   protected Method getMethod(String name,Class className)
	   {
	   	//argument types
		    Class[] types =
	        {
	            ActionMapping.class,
	            ActionForm.class,
	            HttpServletRequest.class,
	            HttpServletResponse.class
		   };
	   		try
			{
	   			Method method = className.getDeclaredMethod(name,types );
	   			return method;
			}
	   		catch(NoSuchMethodException excp1)
			{
	   			logger.error(excp1.getMessage(),excp1 );
			}
	   		catch(NullPointerException excp2)
			{
	   			logger.error(excp2.getMessage(),excp2 );
			}
	   		catch(SecurityException excp3)
			{
	   			logger.error(excp3.getMessage(),excp3 );
			}
		    return null;
	   }

}
