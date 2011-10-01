
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.util.CollectionUtils;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.gridgrouper.GridGrouperUtil;
import edu.wustl.catissuecore.util.MSRUtil;
import edu.wustl.catissuecore.util.listener.CatissueCoreServletContextListener;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberDescriptor;

/**
 * Sets data for site,user,action and role on page loading and handles the ajax
 * requests.
 *
 * @author vipin_bansal
 */
public class ShowAssignPrivilegePageAction extends BaseAction
{

	/**
	 * Overrides the execute method of Action class. Initializes the various
	 * fields in AssignPrivilege.jsp Page.
	 */
	private static Logger logger = Logger
			.getCommonLogger(CatissueCoreServletContextListener.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping : object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward findForward = null;
		// String pageOf = request.getParameter(Constants.PAGE_OF);
		final MSRUtil msrUtil = new MSRUtil();
		final CollectionProtocolForm cpForm = (CollectionProtocolForm) form;
		// final String operation = (String)
		// request.getParameter(Constants.OPERATION);
		final String cpOperation = request.getParameter("cpOperation");
		request.setAttribute("cpOperation", cpOperation);
		if (("AssignPrivilegePage".equals(cpOperation)))
		{
			findForward = msrUtil.onFirstTimeLoad(mapping, request);
		}
		else if (("getGroupsForStem".equals(cpOperation)))
		{
			String stemName= request.getParameter("stemName");
			List<String> groups = new ArrayList<String>();
			for(GroupDescriptor gd : GridGrouperUtil.getGroups(stemName)){
				groups.add(gd.getName());
			}
			response.flushBuffer();
			response.getWriter().write(StringUtils.join(groups, ";;;"));
			findForward = null;
		}
		else if (("getUsersForGroup".equals(cpOperation)))
		{
			String groupName= request.getParameter("groupName");
			List<String> memberList = new ArrayList<String>();
			MemberDescriptor [] members = GridGrouperUtil.getMembers(groupName);
    		if (members !=null) {
        		for (int j=0 ;j<members.length; j++) {
        			memberList.add(members[j].getSubjectName());
        		}
    		}
			response.flushBuffer();
			response.getWriter().write(StringUtils.join(memberList, ";;;"));
			findForward = null;
		}
		else
		{
			findForward = msrUtil.setAJAXResponse(request, response, cpOperation);
		}
		request.setAttribute("CollectionProtocolForm", cpForm);
		request.setAttribute("noOfConsents", cpForm.getConsentTierCounter());

		return findForward;
	}
}
