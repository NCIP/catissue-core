package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;

public class UpdateBulkSpecimensAction extends BaseAction {

	@Override
	protected ActionForward executeAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Collection specimenCollection = (Collection) session
		.getAttribute(Constants.SPECIMEN_LIST_SESSION_MAP);
		NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();
		SessionDataBean sessionDataBean = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
		try{
			ViewSpecimenSummaryForm specimenSummaryForm =
			(ViewSpecimenSummaryForm)form;
			String eventId = specimenSummaryForm.getEventId();
			
			session = request.getSession();
			UpdateSpecimenStatusAction specimenUpdateAction = new UpdateSpecimenStatusAction();
			
			LinkedHashSet specimenDomainCollection = 
				specimenUpdateAction.getSpecimensToSave(eventId, session);
			
			bizLogic.insert(createSpecimenMap(specimenDomainCollection), 
					sessionDataBean, Constants.HIBERNATE_DAO);
			return mapping.findForward(Constants.SUCCESS);
		}
		catch(Exception exception)
		{
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(actionErrors.GLOBAL_MESSAGE, new ActionError(
					"errors.item",exception.getMessage()));
			saveErrors(request, actionErrors);			
			return mapping.findForward(Constants.FAILURE);
		}
	}
	
	private LinkedHashMap createSpecimenMap(Collection specimenCollection)
	{
		Iterator specimenIterator = specimenCollection.iterator();
		LinkedHashMap specimenMap = new LinkedHashMap ();
		while(specimenIterator.hasNext())
		{
			Specimen specimen = (Specimen) specimenIterator.next();
			ArrayList childrenList = new ArrayList();
			
			Collection childrenCollection = specimen.getChildrenSpecimen();
			Iterator childrenIterator = childrenCollection.iterator();
			
			while(childrenIterator.hasNext())
			{
				Specimen childSpecimen = (Specimen) childrenIterator.next();
				childrenList.add(childSpecimen);				
			}
			specimenMap.put(specimen, childrenList);
		}
		return specimenMap;
	}
}
