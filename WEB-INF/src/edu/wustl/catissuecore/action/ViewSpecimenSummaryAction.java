package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.util.global.Constants;

public class ViewSpecimenSummaryAction extends Action 
{

	public ActionForward execute(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		
		try
		{
		
		HttpSession session = request.getSession();
		ViewSpecimenSummaryForm summaryForm = (ViewSpecimenSummaryForm) form;
		String eventId = summaryForm.getEventId();
		if(eventId==null){
			//testData(session);
			eventId = (String) request.getParameter("Event_Id");
		}
		System.out.println("Action called with event id "+ eventId);
		System.out.println("Action called with Selection id "+ 
				summaryForm.getSelectedSpecimenId());

		
		LinkedHashMap<String, GenericSpecimen> specimenMap;
		if (eventId != null)
		{
			specimenMap = (LinkedHashMap) session
					.getAttribute(Constants.COLLECTION_PROTOCOL_httpSession_BEAN);

		}
		else 
		{
			specimenMap = (LinkedHashMap) session
					.getAttribute(Constants.SPECIMEN_LIST_httpSession_BEAN);
		}

		summaryForm.setSpecimenList( specimenMap.values());
//		summaryForm.setSelectedSpecimenId("specimen1");
		String selectedSpecimenId = summaryForm.getSelectedSpecimenId();
		System.out.println(selectedSpecimenId);
		if (selectedSpecimenId != null) {
			GenericSpecimen selectedSpecimen = specimenMap.get(selectedSpecimenId);
			
			if (selectedSpecimen != null){
				HashMap<String, GenericSpecimen> aliqutesList = selectedSpecimen
						.getAliquotSpecimenCollection();
				HashMap<String, GenericSpecimen> derivedList = selectedSpecimen
						.getDeriveSpecimenCollection();
				if(aliqutesList != null){
					summaryForm.setAliquoteList(aliqutesList.values());
				}
				if(derivedList != null){				
					summaryForm.setDerivedList(derivedList.values());
				}
			}
		}

		summaryForm.setEventId(eventId);
		
		return mapping.findForward(Constants.SUCCESS);
}
catch(Exception e)
{
	e.printStackTrace();
	throw e;
}
}


}
