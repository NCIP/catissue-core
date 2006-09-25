
package edu.wustl.catissuecore.action;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import edu.wustl.catissuecore.applet.model.AppletModelInterface;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * This action provides common base to all the action that handles request from an applet.
 * 
 * @author Rahul Ner.
 */
public class BaseAppletAction extends DispatchAction
{

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
	}

	/**
	 * This method reads  AppletModelInterface from request 
	 * and return the map inside it.
	 * 
	 * @param request 
	 * @return map inside AppletModelInterface
	 * @throws Exception
	 */
	protected Map readMapFromRequest(HttpServletRequest request) throws Exception
	{
		ObjectInputStream inputStream = new ObjectInputStream(request.getInputStream());
		AppletModelInterface model = (AppletModelInterface) inputStream.readObject();
		return model.getData();
	}

	/**
	 * This method is overided do save input map before reading anything from request.
	 * 
	 * @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Map inputMap = readMapFromRequest(request);
		request.setAttribute(Constants.INPUT_APPLET_DATA, inputMap);

		return super.execute(actionMapping, actionForm, request, response);
	}

}
