package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
			
			summaryForm.setUserAction(ViewSpecimenSummaryForm.ADD_USER_ACTION);
			
			if("update".equals(request.getParameter("action")))
			{
				summaryForm.setUserAction(ViewSpecimenSummaryForm.UPDATE_USER_ACTION);
			}
				
			LinkedHashMap<String, GenericSpecimen> specimenMap = 
							getSpecimensFromSessoin(session, eventId, summaryForm);

			if (specimenMap != null) {
				populateSpecimenSummaryForm(summaryForm, specimenMap);
			}
			summaryForm.setEventId(eventId);
			String pageOf = request.getParameter(Constants.PAGEOF);
			if(pageOf != null)
			{
				return mapping.findForward(pageOf);
			}
			return mapping.findForward(Constants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * This function retrieves the Map of specimens from session.
	 * @param session
	 * @param eventId
	 * @param specimenMap
	 * @return
	 */
	private LinkedHashMap<String, GenericSpecimen> getSpecimensFromSessoin(
			HttpSession session, String eventId, ViewSpecimenSummaryForm summaryForm) {

		LinkedHashMap<String, GenericSpecimen> specimenMap = null;
		
		if (eventId != null) {
			summaryForm.setRequestType(ViewSpecimenSummaryForm.REQUEST_TYPE_COLLECTION_PROTOCOL);
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
			
			if (collectionProtocolEventMap != null && !collectionProtocolEventMap.isEmpty()) {
			
				CollectionProtocolEventBean collectionProtocolEventBean = 
					(CollectionProtocolEventBean) collectionProtocolEventMap.get(eventId);

				if (collectionProtocolEventBean == null  ) {
				
					Collection cl =collectionProtocolEventMap.values();

					if (cl!=null && !cl.isEmpty())
					{
						collectionProtocolEventBean = 
							(CollectionProtocolEventBean) cl.iterator().next();
					}
					
				}				
				if (collectionProtocolEventBean != null) {
				
					specimenMap = (LinkedHashMap) collectionProtocolEventBean
							.getSpecimenRequirementbeanMap();
					
				}
			}
			
		} else {
			summaryForm.setRequestType(ViewSpecimenSummaryForm.REQUEST_TYPE_MULTI_SPECIMENS);
			specimenMap = (LinkedHashMap) session
					.getAttribute(Constants.SPECIMEN_LIST_SESSION_MAP);
		}
		
		return specimenMap;
	}

	/**
	 * Populates form object of the action with speimen, aliquot specimen
	 * and derived specimen object
	 * @param summaryForm
	 * @param specimenMap
	 */
	private void populateSpecimenSummaryForm(
			ViewSpecimenSummaryForm summaryForm,
			LinkedHashMap<String, GenericSpecimen> specimenMap) {
				
		ArrayList<GenericSpecimen> specimenList = getSpecimenList(specimenMap.values());
		summaryForm.setSpecimenList(specimenList);
		String selectedSpecimenId = summaryForm.getSelectedSpecimenId();

		if (selectedSpecimenId == null) 
		{
			if(specimenList!=null && !specimenList.isEmpty())
			{
				selectedSpecimenId =((GenericSpecimen) specimenList.get(0)).getUniqueIdentifier();
				summaryForm.setSelectedSpecimenId(selectedSpecimenId);
			}
		}
		GenericSpecimen selectedSpecimen = specimenMap
				.get(selectedSpecimenId);

		if (selectedSpecimen == null) 
		{
			return;
		}
		
		HashMap<String, GenericSpecimen> aliquotsList = selectedSpecimen
				.getAliquotSpecimenCollection();
		HashMap<String, GenericSpecimen> derivedList = selectedSpecimen
				.getDeriveSpecimenCollection();

		Collection nestedAliquots = new LinkedHashSet();
		Collection nestedDerives = new LinkedHashSet();
		if (aliquotsList != null && !aliquotsList.values().isEmpty()) 
		{
			nestedAliquots.addAll(aliquotsList.values());
			getNestedAliquotSpecimens(aliquotsList.values(), nestedAliquots);
			getNestedDerivedSpecimens(aliquotsList.values(), nestedDerives);
			
		}

		if (derivedList != null && !derivedList.values().isEmpty()) 
		{
			nestedDerives.addAll(derivedList.values());
			getNestedAliquotSpecimens(derivedList.values(), nestedAliquots);
			getNestedDerivedSpecimens(derivedList.values(), nestedDerives);
		}

		summaryForm.setAliquotList(getSpecimenList(nestedAliquots));
		summaryForm.setDerivedList(getSpecimenList(nestedDerives));
		
	}


	private void getNestedAliquotSpecimens(Collection topChildCollection,
			Collection nestedCollection) {


		Iterator iterator = topChildCollection.iterator();

		while (iterator.hasNext()) {
			GenericSpecimen specimen = (GenericSpecimen) iterator.next();

			if (specimen.getAliquotSpecimenCollection() != null) {
				Collection childSpecimen = specimen
						.getAliquotSpecimenCollection().values();
				if (!childSpecimen.isEmpty()) {
					nestedCollection.addAll(childSpecimen);
					getNestedAliquotSpecimens(childSpecimen, nestedCollection);
				}				
			}
		}
	}

	private void getNestedDerivedSpecimens(Collection topChildCollection,
			Collection nestedCollection) {

		Iterator iterator = topChildCollection.iterator();

		while (iterator.hasNext()) {
			GenericSpecimen specimen = (GenericSpecimen) iterator.next();

			if (specimen.getDeriveSpecimenCollection() != null) {
				Collection childSpecimen = specimen
						.getDeriveSpecimenCollection().values();

				if (!childSpecimen.isEmpty()) {
					nestedCollection.addAll(childSpecimen);
					getNestedDerivedSpecimens(childSpecimen, nestedCollection);
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
/*
	class Generatedata {

			public void generate(HttpServletRequest request) {
				HttpSession session = request.getSession();
				CollectionProtocolBean collectionProtocolBean = getCPBeanObject();
				session.setAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN,
						collectionProtocolBean);
				CollectionProtocolEventBean eventBean = new CollectionProtocolEventBean();
				eventBean.setClinicalStatus("New Diagnosis");
				eventBean.setCollectionPointLabel("E_1");
				eventBean.setStudyCalenderEventPoint(new Double(1));
				eventBean.setUniqueIdentifier("1");
				LinkedHashMap eventMap = new LinkedHashMap();
				eventMap.put("1", eventBean);
				session.setAttribute(
						Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP, eventMap);
				LinkedHashMap specimenMap = createSpecimens(3, "Specimen", null);
				SpecimenRequirementBean specimen = (SpecimenRequirementBean) specimenMap
						.get("Specimen0");
				specimen.setAliquotSpecimenCollection(createSpecimens(2, "Aliquot",
						specimen.getDisplayName()));
				specimen.setDeriveSpecimenCollection(createSpecimens(1, "Derived",
						specimen.getDisplayName()));
				eventBean.setSpecimenRequirementbeanMap(specimenMap);
				specimen = (SpecimenRequirementBean) specimen
						.getDeriveSpecimenCollection().get("Derived0");
				specimen.setDeriveSpecimenCollection(createSpecimens(1,
						"D_Specimen", specimen.getDisplayName()));
				specimen.setAliquotSpecimenCollection(createSpecimens(1,
						"AD_Specimen", specimen.getDisplayName()));
			}

			private LinkedHashMap createSpecimens(int count, String type,
					String parentName) {

				LinkedHashMap specimenMap = new LinkedHashMap();
				for (int i = 0; i < count; i++) {
					SpecimenRequirementBean specimenBean = new SpecimenRequirementBean();
					specimenBean.setClassName("Tissue");
					specimenBean.setType("Fixed Tissue");
					specimenBean.setLineage(Constants.NEW_SPECIMEN);
					specimenBean.setDisplayName(type + i);
					specimenBean.setUniqueIdentifier(type + i);
					specimenBean.setTissueSide("Not Specified");
					specimenBean.setTissueSite("Not Specified");
					specimenBean.setPathologicalStatus("Malignant");
					specimenBean.setStorageContainerForSpecimen("Virtual");
					specimenBean.setQuantity("10");
					specimenBean.setConcentration("0");
					specimenBean.setParentName(parentName);
					specimenMap.put(specimenBean.getUniqueIdentifier(),
							specimenBean);

				}
				return specimenMap;
			}

			private CollectionProtocolBean getCPBeanObject() {
				CollectionProtocolBean collectionProtocol = new CollectionProtocolBean();
				Collection consentTierColl = new HashSet();
				//			
				// ConsentTier c1 = new ConsentTier();
				// c1.setStatement("Consent for aids research");
				// consentTierColl.add(c1);
				// ConsentTier c2 = new ConsentTier();
				// c2.setStatement("Consent for cancer research");
				// consentTierColl.add(c2);
				// ConsentTier c3 = new ConsentTier();
				// c3.setStatement("Consent for Tb research");
				// consentTierColl.add(c3);
				//			
				// collectionProtocol. setConsentTierCollection(consentTierColl);
				//			

				collectionProtocol.setDescriptionURL("");
				collectionProtocol.setEnrollment(null);
				collectionProtocol.setIrbID("7777");
				collectionProtocol
						.setTitle("Study Collection Protocol For Consent track..123");
				collectionProtocol.setShortTitle("Cp Consent");
				collectionProtocol.setUnsignedConsentURLName("C:\\consent12.pdf");

				collectionProtocol.setStartDate("28/07/1975");
				collectionProtocol.setPrincipalInvestigatorId(1L);
				return collectionProtocol;

			}
		}
*/
	
	}
