package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;



/**
 * <p>This class initializes the fields of SpecimenArrayAppletAction.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayAppletAction extends BaseAppletAction 
{
	
	/**
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//ObjectInputStream inputStream = new ObjectInputStream(request.getInputStream());
		Object object = getObject(request);
		String operation = request.getParameter(Constants.OPERATION);
		
		if (operation != null)
		{	
			if (operation.equals("getArrayData")) 
			{
				return getArrayData(mapping,form,request,response,object);
			}	
			else if (operation.equals("updateSessionData")) 
			{
				return updateSessionData(mapping,form,request,response,object);
			}
		}
		return null;
	}
	*/
	
	/**
	 * @param request http request
	 * @return stream object
	 * @throws Exception
	private Object getObject(HttpServletRequest request) throws Exception
	{
		ObjectInputStream inputStream = new ObjectInputStream(request.getInputStream());
		Object object = inputStream.readObject();
		return object;
	}
	*/
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public ActionForward getArrayData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Map tableModelMap = null;
		tableModelMap = (Map) request.getSession().getAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY);
		if (tableModelMap == null) 
		{
			tableModelMap = new HashMap();
		}
		writeMapToResponse(response,tableModelMap);
		return null;
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateSessionData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Map arrayContentMap = (Map) request.getAttribute(Constants.INPUT_APPLET_DATA);
		HttpSession session = request.getSession();
		session.setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,arrayContentMap);
		writeMapToResponse(response,arrayContentMap);
		return null;
	}
}
