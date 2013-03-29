package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupViewForm;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.dto.SpecimenCollectionGroupDTO;
import edu.wustl.catissuecore.util.global.Constants;

public class SpecimenCollectionGroupViewAction extends Action
{
	public ActionForward execute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		SpecimenCollectionGroupViewForm  specimenCollectionGroupViewForm= (SpecimenCollectionGroupViewForm) form;
		String pageOf = request.getParameter(Constants.PAGE_OF);
		
		specimenCollectionGroupViewForm.setIdValue(request.getParameter("id"));
		specimenCollectionGroupViewForm.setParticipantId(request.getParameter(Constants.CP_SEARCH_PARTICIPANT_ID));
		specimenCollectionGroupViewForm.setCpId(request.getParameter(Constants.CP_SEARCH_CP_ID));
		specimenCollectionGroupViewForm.setEvtDate(request.getParameter("evtDate"));
		specimenCollectionGroupViewForm.setClickedNodeId(request.getParameter("clickedNodeId"));
		
		SpecimenCollectionGroupBizLogic bizLogic= new SpecimenCollectionGroupBizLogic();
		SpecimenCollectionGroupDTO specimenCollectionGroupDTO=bizLogic.getSpecimenCollectionGroupDTO(new Long(request.getParameter("id")));
		specimenCollectionGroupDTO.setSpecimenGroupName("Brain Cancer Study_94_297");
		specimenCollectionGroupDTO.setBarcode("56");
		specimenCollectionGroupDTO.setStudyCalendarEventPoint("0.0, Surgery Event");
		specimenCollectionGroupDTO.setCollectionSite("Lowy Repository");
		specimenCollectionGroupDTO.setClinicalDignosis("Disorder of brain (disorder)");
		specimenCollectionGroupDTO.setSurgicalPathologyNumber("123");
		specimenCollectionGroupDTO.setCollectionStatus("Pending");
		specimenCollectionGroupDTO.setClinicalStatus("Operative");
		specimenCollectionGroupDTO.setCollectedDate("10-18-2012 (21:14:00)");
		specimenCollectionGroupDTO.setReceivedDate("10-22-2012 (21:14:00)");
		specimenCollectionGroupDTO.setCollectedProcedure("Needle Aspirate");
		specimenCollectionGroupDTO.setCollectedContainer("Use CP Defaults");
		specimenCollectionGroupDTO.setReceivedQuality("Use CP Defaults");
		specimenCollectionGroupDTO.setActivityStatus("Active");
		
		specimenCollectionGroupViewForm.setSpecimenGroupName(specimenCollectionGroupDTO.getSpecimenGroupName());
		specimenCollectionGroupViewForm.setBarcode(specimenCollectionGroupDTO.getBarcode());
		specimenCollectionGroupViewForm.setStudyCalendarEventPoint(specimenCollectionGroupDTO.getStudyCalendarEventPoint());
		specimenCollectionGroupViewForm.setCollectionSite(specimenCollectionGroupDTO.getCollectionSite());
		specimenCollectionGroupViewForm.setClinicalDignosis(specimenCollectionGroupDTO.getClinicalDignosis());
		specimenCollectionGroupViewForm.setSurgicalPathologyNumber(specimenCollectionGroupDTO.getSurgicalPathologyNumber());
		specimenCollectionGroupViewForm.setCollectionStatus(specimenCollectionGroupDTO.getCollectionStatus());
		specimenCollectionGroupViewForm.setClinicalStatus(specimenCollectionGroupDTO.getClinicalStatus());
		specimenCollectionGroupViewForm.setCollectedDate(specimenCollectionGroupDTO.getCollectedDate());
		specimenCollectionGroupViewForm.setReceivedDate(specimenCollectionGroupDTO.getReceivedDate());
		specimenCollectionGroupViewForm.setCollectedProcedure(specimenCollectionGroupDTO.getCollectedProcedure());
		specimenCollectionGroupViewForm.setCollectedContainer(specimenCollectionGroupDTO.getCollectedContainer());
		specimenCollectionGroupViewForm.setReceivedQuality(specimenCollectionGroupDTO.getReceivedQuality());
		specimenCollectionGroupViewForm.setActivityStatus(specimenCollectionGroupDTO.getActivityStatus());
		
	     return mapping.findForward(pageOf);
	}
}
