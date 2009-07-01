
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CPSearchForm;
import edu.wustl.catissuecore.bean.CpAndParticipentsBean;
import edu.wustl.catissuecore.bizlogic.CpBasedViewBizLogic;
import edu.wustl.common.action.BaseAction;

/**
 * This action is for getting the collection protocol and 
 * participants registered for that collection protocol from cache
 * @author vaishali_khandelwal
 *
 */
public class ShowCPAndParticipantsAction extends BaseAction
{

	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		CPSearchForm cpsearchForm = (CPSearchForm) form;
		// Removed the cache manager related code : Geeta
		CpBasedViewBizLogic cpBizLogic = new CpBasedViewBizLogic();
		List participantColl = new ArrayList();
		Long cpId = null;
		if (cpsearchForm.getCpId() != null && cpsearchForm.getCpId().longValue() != -1)
		{
			cpId = cpsearchForm.getCpId();
		}
		if (cpId == null && request.getParameter("CpId") != null)
		{
			cpId = new Long(request.getParameter("CpId"));
		}
		//If cpId ! = null meand if user had selected COllection protocol then getting the list of 
		//participants registered for that CP.
		if (cpId != null)
		{
			participantColl = cpBizLogic.getRegisteredParticipantInfoCollection(cpId);
			Collections.sort(participantColl);
		}
		if (request.getParameter("participantId") != null
				&& !request.getParameter("participantId").equals(""))
		{
			Long participantId = new Long(request.getParameter("participantId"));
			cpsearchForm.setParticipantId(participantId);
		}
		//when collection protocol gets changes then don't shown any participant seelcted...
		String cpChange = request.getParameter("cpChange");
		if (cpChange != null)
		{
			cpsearchForm.setParticipantId(null);
		}
		return mapping.findForward("success");
	}
}
