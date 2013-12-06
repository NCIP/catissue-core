
package krishagni.catissueplus.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.common.util.global.ApplicationProperties;

import krishagni.catissueplus.throttling.Event;
import krishagni.catissueplus.throttling.ThrottleService;
import krishagni.catissueplus.throttling.ThrottlingResourceEnum;
import krishagni.catissueplus.throttling.impl.ThrottleServiceImpl;

public class ThrottlingFilter implements Filter
{

	private String errorPage = "";

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{
		if (request instanceof HttpServletRequest)
		{
			String ipAddress = request.getRemoteAddr();

			final String url = ((HttpServletRequest) request).getRequestURI().replaceFirst(
					((HttpServletRequest) request).getContextPath() + "/", "");
			ThrottlingResourceEnum.getStatus(url);
			Event event = new Event(ipAddress, ThrottlingResourceEnum.getStatus("/Login"));

			ThrottleService throttleService = ThrottleServiceImpl.getInstance();
			if (!throttleService.throttle(event))
			{
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalidLoginAttempts", ""));
				request.setAttribute("error", "error");
				request.setAttribute(Globals.ERROR_KEY, actionErrors);
				request.getRequestDispatcher("RedirectHome.do").forward(request, response);
			}
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		errorPage = filterConfig.getInitParameter("error-page");
	}

}
