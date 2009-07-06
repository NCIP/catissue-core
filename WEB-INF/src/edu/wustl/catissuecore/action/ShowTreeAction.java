
package edu.wustl.catissuecore.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * This action is for getting the collection protocol and participants
 * registered for that collection protocol from cache
 *
 * @author vaishali_khandelwal
 */
public class ShowTreeAction extends BaseAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
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
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// CPSearchForm cpsearchForm = (CPSearchForm) form;
		String cpId = request.getParameter(Constants.CP_SEARCH_CP_ID);
		String participantId = request.getParameter(Constants.CP_SEARCH_PARTICIPANT_ID);
		/**
		 * Name : Deepti Shelar Reviewer's Name : Sachin Lale Bug id : 4213
		 * Patch id : 4213_1 Description : getting parameters from request and
		 * keeping them in seesion to keep the node in tree selected.
		 */
		String isParticipantChanged = request.getParameter("particiantChnaged");
		if (isParticipantChanged != null && isParticipantChanged.equalsIgnoreCase("true"))
		{
			request.getSession().setAttribute("nodeId", null);
		}
		if (request.getParameter("nodeId") != null)
		{
			String nodeId = request.getParameter("nodeId");
			if (!nodeId.equalsIgnoreCase("SpecimenCollectionGroup_0"))
			{
				request.getSession().setAttribute("nodeId", nodeId);
			}
		}
		Vector treeData = null;
		if (cpId != null && participantId != null && !cpId.equals("") && !participantId.equals(""))
		{
			// IFactory factory =
			// AbstractFactoryConfig.getInstance().getBizLogicFactory();
			/*
			 * SpecimenCollectionGroupBizLogic bizLogic =
			 * (SpecimenCollectionGroupBizLogic) factory
			 * .getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			 */
			/**
			 * Patch Id : FutureSCG_2 Description : Calling method to create
			 * tree for future scgs
			 */

			// Commented out by Baljeet as not required after Flex realted
			// changes
			// treeData = bizLogic.getSCGTreeForCPBasedView(new Long(cpId), new
			// Long(participantId));
		}
		request.setAttribute("treeData", treeData);
		request.setAttribute(Constants.CP_SEARCH_CP_ID, cpId);
		request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID, participantId);
		/**
		 * Name : Falguni Sachde Reviewer's Name : Sachin Lale Description :
		 * getting parameters Name of Collection Protocol Name from request and
		 * setting it as attribute
		 */

		String cpTitle = request.getParameter("cpTitle");

		if (cpTitle != null)
		{
			HttpSession session = request.getSession();
			session.setAttribute("cpTitle", cpTitle);
		}

		return mapping.findForward("success");
	}
}
