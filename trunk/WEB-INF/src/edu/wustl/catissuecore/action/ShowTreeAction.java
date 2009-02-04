
package edu.wustl.catissuecore.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * This action is for getting the collection protocol and 
 * participants registered for that collection protocol from cache
 * @author vaishali_khandelwal
 *
 */
public class ShowTreeAction extends BaseAction
{

	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		//CPSearchForm cpsearchForm = (CPSearchForm) form;
		String cpId = request.getParameter(Constants.CP_SEARCH_CP_ID);
		String participantId = request.getParameter(Constants.CP_SEARCH_PARTICIPANT_ID);
		Vector treeData = null;
		if (cpId != null && participantId != null && !cpId.equals("") && !participantId.equals(""))
		{
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			treeData = bizLogic.getSCGandSpecimens(new Long(cpId), new Long(participantId));
		}
		request.setAttribute("treeData", treeData);
		request.setAttribute(Constants.CP_SEARCH_CP_ID, cpId);
		request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID, participantId);
		return mapping.findForward("success");
	}
}
