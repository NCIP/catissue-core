/*
 * Created on May 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.struts;

import java.io.ObjectInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.TilesRequestProcessor;

import edu.wustl.catissuecore.client.HTTPWrapperObject;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author kapil_kaveeshwar
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java -
 * Code Style - Code Templates
 */
public class ApplicationRequestProcessor extends TilesRequestProcessor
{

	public ApplicationRequestProcessor()
	{
	}

	protected ActionForm processActionForm(HttpServletRequest request,
			HttpServletResponse response, ActionMapping mapping)
	{
		Logger.out.debug("contentType " + request.getContentType());
		if (request.getContentType() != null && request.getContentType().equals(Constants.HTTP_API))
		{
			try
			{
				ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
				HTTPWrapperObject wrapperObject = (HTTPWrapperObject)ois.readObject();
				
				String operation = wrapperObject.getOperation();
				
				ActionForm form = wrapperObject.getForm();
				
				if(operation.equals(Constants.LOGIN))
				{
					request.setAttribute(Constants.OPERATION,Constants.LOGIN);
				}
				else
				{
					request.setAttribute(Constants.OPERATION,operation);
				}
				
				Logger.out.debug("mapping.getAttribute() " + mapping.getAttribute());

				if ("request".equals(mapping.getScope()))
				{
					request.setAttribute(mapping.getAttribute(), form);
				}
				else
				{
					HttpSession session = request.getSession();
					session.setAttribute(mapping.getAttribute(), form);
				}
				return form;
			}
			catch (Exception e)
			{
				Logger.out.debug(e.getMessage(), e);
			}
			return null;
		}
		else
		{
			return super.processActionForm(request, response, mapping);
		}
	}
	
	protected void processPopulate(HttpServletRequest request, HttpServletResponse response,
			ActionForm form, ActionMapping mapping) throws ServletException
	{
		if (request.getContentType() != null && request.getContentType().equals(Constants.HTTP_API))
		{

		}
		else
		{
			super.processPopulate(request,  response, form,  mapping);
		}
	}
}