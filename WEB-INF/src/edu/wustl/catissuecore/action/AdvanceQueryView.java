/*
 * Created on Sep 1, 2005
 * This class is used to redirect the user to the Home / SignIn Page after session is timedOut.
 */

package edu.wustl.catissuecore.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.query.QueryTest;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author mandar_deshmukh
 *
 * This class is used to redirect the user to the Home / SignIn Page after session is timedOut.
 */
public class AdvanceQueryView extends BaseAction
{

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		QueryTest test = new QueryTest();
		Vector v = test.getTreeElement();
		request.setAttribute("vector",v);
		return (mapping.findForward(Constants.SUCCESS));
	}

}