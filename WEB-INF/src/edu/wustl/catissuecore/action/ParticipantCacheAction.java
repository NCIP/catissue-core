/*
 * Created on Sep 25, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * @author santosh_chandak
 *
 * This class is used to update cache when participant is added/edited 
 */
public class ParticipantCacheAction extends BaseAction
{

	/**
	 *  This method is used to update cache when participant is added/edited 
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		// Get the id of participant which is added or edited 
		Long identifier = Long.valueOf(request.getParameter(Constants.SYSTEM_IDENTIFIER));
		if (identifier == null || identifier.longValue() == 0)
			identifier = ((Long) request.getAttribute(Constants.SYSTEM_IDENTIFIER));

		ParticipantBizLogic participantBizLogic = (ParticipantBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.PARTICIPANT_FORM_ID);
		Participant participant = participantBizLogic.getParticipantById(identifier);
		
		
        // getting instance of catissueCoreCacheManager and getting participantMap from cache
		CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
		HashMap participantMap = (HashMap) catissueCoreCacheManager.getObjectFromCache(Constants.MAP_OF_PARTICIPANTS);
		participantMap.put(identifier, participant);
		// adding updated participantMap to cache
		catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_PARTICIPANTS,participantMap);
		
		ParticipantForm participantForm = (ParticipantForm) form;
		if(participantForm.isAddOperation())
		{
			return mapping.findForward(Constants.ADD);
		}
		return mapping.findForward(Constants.EDIT);
	}

}