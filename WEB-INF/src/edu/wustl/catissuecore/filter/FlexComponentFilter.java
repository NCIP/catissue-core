
package edu.wustl.catissuecore.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * This filter is for filtering urls which access files under flexclient folder without login.
 * @author vaishali_methi
 *
 */
public class FlexComponentFilter implements Filter
{

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException
	{
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession();

		if (session != null && session.getAttribute(Constants.SESSION_DATA) != null)
		{
			filterChain.doFilter(req,res);
		}
		else
		{
			response.sendRedirect("../Home.do");
		}


	}

	public void init(FilterConfig arg0) throws ServletException
	{
	}

	public void destroy()
	{
	}
}
