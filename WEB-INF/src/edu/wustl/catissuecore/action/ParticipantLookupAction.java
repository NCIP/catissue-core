/**
 * <p>Title: ParticipantLookupAction Class>
 * <p>Description:	This Class is used to search the matching participant.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author vaishali_khandelwal
 * @Created on May 19, 2006
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.ParticipantLookupLogic;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.factory.AbstractDomainObjectFactory;
import edu.wustl.common.factory.MasterFactory;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;

public class ParticipantLookupAction extends BaseAction
{
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AbstractDomainObject abstractDomain = null;
		AbstractActionForm abstractForm = (AbstractActionForm) form;

		AbstractDomainObjectFactory abstractDomainObjectFactory = (AbstractDomainObjectFactory) MasterFactory
				.getFactory("edu.wustl.catissuecore.domain.DomainObjectFactory");

		//Creating the column list which is used in Data grid to display column headings
		String[] columnHeaderList = new String[]{"System Identifier", "Last Name", "First Name",
				"Middle Name", "Birth Date", "SSN", "Probability"};
		List ColumnList = new ArrayList();
		for (int i = 0; i < columnHeaderList.length; i++)
		{
			ColumnList.add(columnHeaderList[i]);
		}

		request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, ColumnList);

		abstractDomain = abstractDomainObjectFactory.getDomainObject(abstractForm.getFormId(),
				abstractForm);

		Participant participant = (Participant) abstractDomain;

		//getting the instance of ParticipantLookupLogic class
		ParticipantLookupLogic participantLookupLogic = (ParticipantLookupLogic) Utility
				.getObject(XMLPropertyHandler.getValue("ParticipantLookup"));

		List ParticipantList = participantLookupLogic.participantLookup(participant);

		//if any matching participants are there then show the participants otherwise add the participant
		if (ParticipantList.size() > 0 || request.getParameter("ParticipantLookup") != null)
		{
			request.setAttribute(Constants.SPREADSHEET_DATA_LIST, ParticipantList);
		}
		else
		{
			return mapping.findForward("participantAdd");
		}

		return (mapping.findForward("success"));
	}
}