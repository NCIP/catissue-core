
package edu.wustl.catissuecore.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * 
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class CommonExceptionHandler extends ExceptionHandler {

		
    /* (non-Javadoc)
	 * @see org.apache.struts.action.ExceptionHandler#execute(java.lang.Exception, org.apache.struts.config.ExceptionConfig, org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(Exception ex,
                               ExceptionConfig ae,
                               ActionMapping mapping,
                               ActionForm formInstance,
                               HttpServletRequest request,
                               HttpServletResponse response)
                                                   throws ServletException {
    		String errorMessage = getErrorMsg(ex);
            Logger.out.error( errorMessage, ex );
            request.getSession().setAttribute( Constants.ERROR_DETAIL, "Unhandled Exception occured in caTISSUE Core: "+ex.getMessage() );
            return super.execute( ex, ae, mapping, formInstance, request, response );

      }

      /**
	 * @param ex the Exception.
	 * @return the string of the error message.
	 */
	public String getErrorMsg( Exception ex ){
           String msg = "Exception was NULL";

           if ( ex != null ){
              ByteArrayOutputStream bo = new ByteArrayOutputStream();
	            PrintWriter pw = new PrintWriter(bo, true);
	            ex.printStackTrace(pw);
	            msg = "Unhandled Exception occured in caTISSUE Core \n" +
                    "Message: " + ex.getMessage() +"\n" +
                    "StackTrace: " + bo.toString();
           }

          return msg;

      }
	
}
