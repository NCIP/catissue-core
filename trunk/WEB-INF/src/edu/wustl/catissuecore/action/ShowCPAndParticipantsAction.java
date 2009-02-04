
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.wustl.catissuecore.util.global.Constants;

import edu.wustl.catissuecore.actionForm.CPSearchForm;
import edu.wustl.catissuecore.util.ParticipantRegistrationCacheManager;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;

/**
 * This action is for getting the collection protocol and 
 * participants registered for that collection protocol from cache
 * @author vaishali_khandelwal
 *
 */
public class ShowCPAndParticipantsAction extends BaseAction
{

	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		CPSearchForm cpsearchForm = (CPSearchForm) form;
		//Getting the instance of participantRegistrationCacheManager
		ParticipantRegistrationCacheManager participantRegCacheManager = new ParticipantRegistrationCacheManager();
		//Gettiing the CP list 
		List cpColl = participantRegCacheManager.getCPDetailCollection();
		//Adding default value for CP

		Collections.sort(cpColl);
		cpColl.add(0, new NameValueBean("--Select--", "-1"));
		//Setting the list in request
		request.setAttribute(Constants.CP_LIST, cpColl);

		List participantColl = new ArrayList();

		Long cpId = null;
		if (cpsearchForm.getCpId() != null && cpsearchForm.getCpId().longValue() != -1 )
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
			//getting the list of participants from cache for particular CP.
			List participantNamesWithId = participantRegCacheManager.getParticipantNames(cpId);

			//Values in participantNamesWithID will be in format (ID:lastName firstName) 
			//tokenize the value and create nameValueBean with name as (lastName firstName) and value as participantId 
			//and store in the list
			Iterator itr = participantNamesWithId.iterator();
			while (itr.hasNext())
			{
				String participantIdAndName = (String) itr.next();
				int index = participantIdAndName.indexOf(":");
				Long Id = null;
				String name="";
				Id = new Long(participantIdAndName.substring(0, index));
				name = participantIdAndName.substring(index + 1);
				participantColl.add(new NameValueBean(name, Id));
			}
		}
		Collections.sort(participantColl);
		request.setAttribute(Constants.REGISTERED_PARTICIPANT_LIST, participantColl);
		
		//if participantId is not null then set that in form for further use.
		
		if (request.getParameter("participantId") != null && !request.getParameter("participantId").equals(""))
		{
			Long participantId = new Long(request.getParameter("participantId"));
			cpsearchForm.setParticipantId(participantId);
		}
//		when collection protocol gets changes then don't shown any participant seelcted...
		String cpChange = request.getParameter("cpChange");
		if(cpChange != null)
		{
			cpsearchForm.setParticipantId(null);
		}
		
		return mapping.findForward("success");
	}
}
