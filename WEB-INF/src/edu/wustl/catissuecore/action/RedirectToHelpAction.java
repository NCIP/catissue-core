package edu.wustl.catissuecore.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.wustl.catissuecore.util.global.Constants;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.util.dbManager.DAOException;

/**
 * 
 * @author pathik_sheth
 *
 */
public class RedirectToHelpAction extends Action{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException, DAOException
      {
		 ActionForward actionForward=null; 
		 RequestDispatcher dispatcher = 
			request.getRequestDispatcher(Constants.HELP_FILE);
		 dispatcher.forward(request, response);
		 return actionForward;
      }
}
