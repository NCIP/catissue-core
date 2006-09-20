package edu.wustl.catissuecore.action;

import java.io.ObjectOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.actions.DispatchAction;

import edu.wustl.catissuecore.applet.model.BaseAppletModel;


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
	protected void writeMapToResponse(HttpServletResponse response,Map outputMap) throws Exception {
		ObjectOutputStream outputStream = new ObjectOutputStream(response.getOutputStream());
		BaseAppletModel appletModel = new BaseAppletModel();
		appletModel.setData(outputMap);
		outputStream.writeObject(appletModel);
	}


}
