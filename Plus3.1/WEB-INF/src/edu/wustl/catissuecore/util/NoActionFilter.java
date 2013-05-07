package edu.wustl.catissuecore.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * This filter escapes any other filter calls in the application by redirecting
 * to the requested action. The purpose of this filter is to avoid calls to CAS
 * filter for URLs which do not require authentication.
 *
 * @author niharika_sharma
 *
 */
public class NoActionFilter implements Filter
{

    public void destroy()
    {
        // TODO Auto-generated method stub

    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain arg2)
            throws IOException, ServletException
    {
        if (request instanceof HttpServletRequest)
        {
            final String url = ((HttpServletRequest) request).getRequestURI().replaceFirst(
                    ((HttpServletRequest) request).getContextPath() + "/", "");
            final RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
        }

    }

    public void init(final FilterConfig arg0) throws ServletException
    {
        // TODO Auto-generated method stub

    }

}
