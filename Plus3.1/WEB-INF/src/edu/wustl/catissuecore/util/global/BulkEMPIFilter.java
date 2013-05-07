package edu.wustl.catissuecore.util.global;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.ISecurityManager;
import edu.wustl.security.manager.SecurityManagerFactory;
import gov.nih.nci.security.authorization.domainobjects.Role;



public class BulkEMPIFilter implements Filter
{

	public void destroy()
	{
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		// TODO Auto-generated method stub
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		Object sessionData = httpServletRequest.getSession().getAttribute(
				Constants.SESSION_DATA);
		if(sessionData==null)
		{
			((HttpServletRequest) request).getRequestDispatcher(Constants.REDIRECT_HOME).forward(request,
					response);
		}
		else
		{

			//check if user is authorized to perform operations
			try
			{
				if (isAuthorized(httpServletRequest))
				{
					chain.doFilter(request, response);
				}
				else
				{
					//Forward to the Login page with error messages
					redirectToHome(request, response, httpServletRequest);
				}
			}
			catch (Exception e)
			{

				redirectToHome(request, response, httpServletRequest);
			}
		}

	}

	/**
	 * This method returns current sessionDataBean.
	 * @param request
	 * @return
	 */
	private SessionDataBean getSessionData(ServletRequest request)
	{
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(
				Constants.SESSION_DATA);
	}

	private boolean isAuthorized(HttpServletRequest httpServletRequest) throws ServletException
	{
		boolean isAuthorized = false;
		SessionDataBean sessionDataBean = getSessionData(httpServletRequest);
		try
		{
			ISecurityManager securityManager =  SecurityManagerFactory
					.getSecurityManager();
			Role role = securityManager.getUserRole(Long.parseLong(sessionDataBean.getCsmUserId()));
			if (role != null)
			{
				if (edu.wustl.security.global.Constants. ADMINISTRATOR.equals(role.getName()))
				{
					//super Admin Role has all privileges
					isAuthorized = true;
				}
			}
		}
		catch (SMException e)
		{
			throw new ServletException(e);
		}
		return isAuthorized;
	}

	private void redirectToHome(ServletRequest request, ServletResponse response,
			HttpServletRequest httpServletRequest) throws ServletException, IOException
	{
		ActionErrors errors = new ActionErrors();
		ActionError error = new ActionError(Constants.ACCESS_DENIED_MSG);
		errors.add(ActionErrors.GLOBAL_ERROR, error);
		httpServletRequest.setAttribute("org.apache.struts.action.ERROR", errors);
		httpServletRequest.getRequestDispatcher(
				Constants.REDIRECT_HOME).forward(
				request, response);
	}

	public void init(FilterConfig arg0) throws ServletException
	{
		// TODO Auto-generated method stub

	}

}
