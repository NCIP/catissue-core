package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

public class ViewSpecimenSummaryAction extends BaseAction 
{
	public ActionForward executeAction(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		try
		{
			HttpSession session = request.getSession();
			ViewSpecimenSummaryForm summaryForm = (ViewSpecimenSummaryForm) form;
			String eventId = summaryForm.getEventId();
			if(eventId==null)
			{
				eventId = (String) request.getParameter("Event_Id");
			}
					
			LinkedHashMap<String, GenericSpecimen> specimenMap;
			if (eventId != null)
			{
				Map collectionProtocolEventMap = (Map)session.getAttribute("collectionProtocolEventMap");
				CollectionProtocolEventBean collectionProtocolEventBean =(CollectionProtocolEventBean)collectionProtocolEventMap.get(eventId);
				specimenMap = (LinkedHashMap)collectionProtocolEventBean.getSpecimenRequirementbeanMap();
			}
			else 
			{
				specimenMap = (LinkedHashMap) session.getAttribute(Constants.SPECIMEN_LIST_httpSession_BEAN);
			}
	
			summaryForm.setSpecimenList(specimenMap.values());
			String selectedSpecimenId = summaryForm.getSelectedSpecimenId();
			
			if (selectedSpecimenId != null) 
			{
				GenericSpecimen selectedSpecimen = specimenMap.get(selectedSpecimenId);
				if (selectedSpecimen != null)
				{
					HashMap<String, GenericSpecimen> aliqutesList = selectedSpecimen.getAliquotSpecimenCollection();
					HashMap<String, GenericSpecimen> derivedList = selectedSpecimen.getDeriveSpecimenCollection();
					if(aliqutesList != null)
					{
						summaryForm.setAliquoteList(aliqutesList.values());
					}
					if(derivedList != null)
					{				
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
