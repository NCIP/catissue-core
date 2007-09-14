package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.adobe.agl.util.StringTokenizer;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.util.global.Constants;

public class ViewSpecimenSummaryAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession();
			ViewSpecimenSummaryForm summaryForm = (ViewSpecimenSummaryForm) form;
			String eventId = summaryForm.getEventId();

			if (eventId == null) {
				eventId = (String) request
						.getParameter(Constants.COLLECTION_PROTOCOL_EVENT_ID);
				
//				new Generatedata().generate(request);
			}

			LinkedHashMap<String, GenericSpecimen> specimenMap = 
							getSpecimensFromSessoin(session, eventId);

			if (specimenMap != null) {
				populateSpecimenSummaryForm(summaryForm, specimenMap);
			}
			summaryForm.setEventId(eventId);
			return mapping.findForward(Constants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * @param session
	 * @param eventId
	 * @param specimenMap
	 * @return
	 */
	private LinkedHashMap<String, GenericSpecimen> getSpecimensFromSessoin(
			HttpSession session, String eventId) {

		LinkedHashMap<String, GenericSpecimen> specimenMap = null;

		if (eventId != null) {

			StringTokenizer stringTokenizer =new StringTokenizer(eventId, "_");
			if(stringTokenizer!=null)
			{
				if (stringTokenizer.hasMoreTokens())
				{
					eventId = stringTokenizer.nextToken();
				}
			}

			Map collectionProtocolEventMap = (Map) session
					.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
			
			if (collectionProtocolEventMap != null) {
			
				CollectionProtocolEventBean collectionProtocolEventBean = 
					(CollectionProtocolEventBean) collectionProtocolEventMap.get(eventId);
				
				if (collectionProtocolEventBean != null) {
				
					specimenMap = (LinkedHashMap) collectionProtocolEventBean
							.getSpecimenRequirementbeanMap();
					
				}
			}
			
		} else {
			specimenMap = (LinkedHashMap) session
					.getAttribute(Constants.SPECIMEN_LIST_SESSION_MAP);
		}
		return specimenMap;
	}

	/**
	 * @param summaryForm
	 * @param specimenMap
	 */
	private void populateSpecimenSummaryForm(
			ViewSpecimenSummaryForm summaryForm,
			LinkedHashMap<String, GenericSpecimen> specimenMap) {
		
		
		ArrayList<GenericSpecimen> specimenList = getSpecimenList(specimenMap.values());
		
		summaryForm.setSpecimenList(specimenList);
		String selectedSpecimenId = summaryForm.getSelectedSpecimenId();

		if (selectedSpecimenId != null) 
		{
			GenericSpecimen selectedSpecimen = specimenMap
					.get(selectedSpecimenId);

			if (selectedSpecimen != null) 
			{
				HashMap<String, GenericSpecimen> aliqutesList = selectedSpecimen
						.getAliquotSpecimenCollection();
				HashMap<String, GenericSpecimen> derivedList = selectedSpecimen
						.getDeriveSpecimenCollection();

				if (aliqutesList != null && !aliqutesList.values().isEmpty()) 
				{
					Collection nestedAliquots = new LinkedHashSet();
					getNestedChildSpecimens(aliqutesList.values(), nestedAliquots);

					specimenList = getSpecimenList(nestedAliquots);
					
					summaryForm.setAliquotList(specimenList);
				}

				if (derivedList != null && !derivedList.values().isEmpty()) 
				{
					Collection nestedDerives = new LinkedHashSet();
					getNestedChildSpecimens(derivedList.values(), nestedDerives);
					specimenList = getSpecimenList(nestedDerives);
					summaryForm.setDerivedList(specimenList);
				}
			}
		}
	}

	/**
	 * @param specimenMap
	 * @return
	 */
	private ArrayList<GenericSpecimen> getSpecimenList(
			Collection<GenericSpecimen> specimenColl) {
		ArrayList<GenericSpecimen> specimenList = new ArrayList<GenericSpecimen>();
		specimenList.addAll(specimenColl);
		return specimenList;
	}

	private void getNestedChildSpecimens(Collection topChildCollection,
			Collection nestedCollection) {

		nestedCollection.addAll(topChildCollection);
		Iterator iterator = topChildCollection.iterator();

		while (iterator.hasNext()) {
			GenericSpecimen specimen = (GenericSpecimen) iterator.next();

			if (specimen.getAliquotSpecimenCollection() != null) {
				Collection childSpecimen = specimen
						.getAliquotSpecimenCollection().values();
				if (!childSpecimen.isEmpty()) {
					getNestedChildSpecimens(childSpecimen, nestedCollection);
				}				
			}
			
			if (specimen.getDeriveSpecimenCollection() != null) {
				
				Collection childSpecimen = specimen
				.getDeriveSpecimenCollection().values();
				
				if (!childSpecimen.isEmpty()) {
					getNestedChildSpecimens(childSpecimen, nestedCollection);
				}
				
			}			

		}
	}

	}
