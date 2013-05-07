package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.util.global.Constants;

public class ParticipantDeleteAction  extends Action
{

	public ActionForward execute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
	    ParticipantBizLogic participantBizLogic=new ParticipantBizLogic();
		participantBizLogic.disableParticipant(new Long(request.getParameter("id")));
    	return mapping.findForward(Constants.SUCCESS);
	}
}


	
