package edu.wustl.catissuecore.action.querysuite;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.tree.QueryTreeNodeData;
/**
 * This action is called when user clicks on Search button after forming the query object.This class loads required tree data in session/request.
 * And then it forwards control to QueryTreeView.jsp.
 * @author deepti_shelar
 */
public class QueryTreeViewAction extends BaseAction
{
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession session = request.getSession();
		Long noOfTrees = (Long)session.getAttribute(Constants.NO_OF_TREES);
		for(int i=0; i<noOfTrees ;i++)
		{
			String key = Constants.TREE_DATA+"_"+i;
			Vector<QueryTreeNodeData> treeData = (Vector<QueryTreeNodeData>) session.getAttribute(key);
			request.setAttribute(key,treeData);
			session.removeAttribute(key);
		}
		return mapping.findForward(Constants.SUCCESS);
	}
}
