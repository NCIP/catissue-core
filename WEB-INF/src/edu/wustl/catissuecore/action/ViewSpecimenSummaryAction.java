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
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.GenericSpecimenVO;
import edu.wustl.catissuecore.util.global.Constants;

public class ViewSpecimenSummaryAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession();
			
			ViewSpecimenSummaryForm summaryForm = (ViewSpecimenSummaryForm) form;
			String eventId = summaryForm.getEventId();

			Object obj = request.getAttribute("SCGFORM");
			request.setAttribute("SCGFORM", obj);
			String target =request.getParameter("target");
			String submitAction = request.getParameter("submitAction");
			
			if (target == null)
			{
				target = Constants.SUCCESS;
			}
			
			if (submitAction != null)
			{
				summaryForm.setSubmitAction(submitAction);
			}
			
			if(summaryForm.getTargetSuccess() == null)
			{
				summaryForm.setTargetSuccess(target);
			}
			target = summaryForm.getTargetSuccess();
			if (request.getAttribute("RequestType")!=null)
			{
				summaryForm.setRequestType(ViewSpecimenSummaryForm.REQUEST_TYPE_ANTICIPAT_SPECIMENS);
			}
				
			if (eventId == null) {
				eventId = (String) request
						.getParameter(Constants.COLLECTION_PROTOCOL_EVENT_ID);
				
			}
			
			if (summaryForm.getSpecimenList()!= null  &&
					ViewSpecimenSummaryForm.REQUEST_TYPE_ANTICIPAT_SPECIMENS
					.equals(summaryForm.getRequestType()))
			{
				updateSessionBean(summaryForm, session);
			}

			if(request.getParameter("save")!=null)
			{
				return mapping.findForward(summaryForm.getSubmitAction());
			}
			summaryForm.setUserAction(ViewSpecimenSummaryForm.ADD_USER_ACTION);
			CollectionProtocolBean collectionProtocolBean = 
				(CollectionProtocolBean)session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
			
			LinkedHashMap<String, GenericSpecimen> specimenMap = 
							getSpecimensFromSessoin(session, eventId, summaryForm);

			if (specimenMap != null) {
				populateSpecimenSummaryForm(summaryForm, specimenMap);
			} 

			if (ViewSpecimenSummaryForm.REQUEST_TYPE_COLLECTION_PROTOCOL.equals(summaryForm.getRequestType()))
			{
				if("update".equals(collectionProtocolBean.getOperation()))
				{
					summaryForm.setUserAction(ViewSpecimenSummaryForm.UPDATE_USER_ACTION);
				}
			}

			String pageOf = request.getParameter(Constants.PAGEOF);
			if(pageOf != null && ViewSpecimenSummaryForm.REQUEST_TYPE_MULTI_SPECIMENS.equals(summaryForm.getRequestType()))
			{
				request.setAttribute(Constants.PAGEOF,pageOf);
				return mapping.findForward(pageOf);
			}
			summaryForm.setLastSelectedSpecimenId(summaryForm.getSelectedSpecimenId());
			return mapping.findForward(target);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * @param summaryForm
	 */
	private void updateSessionBean(ViewSpecimenSummaryForm summaryForm, HttpSession session)
	{
		String eventId = summaryForm.getEventId();
		if (eventId == null)
		{
			return;
		}
		
		Map collectionProtocolEventMap = (Map) session
		.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);		
		CollectionProtocolEventBean eventBean =(CollectionProtocolEventBean)
							collectionProtocolEventMap.get(eventId);
		LinkedHashMap specimenMap = (LinkedHashMap)eventBean.getSpecimenRequirementbeanMap();
		String selectedItem = summaryForm.getLastSelectedSpecimenId();
		GenericSpecimenVO selectedSpecimen=(GenericSpecimenVO) specimenMap.get(selectedItem);
		
		Collection specimenCollection = specimenMap.values();
		Iterator iterator = summaryForm.getSpecimenList().iterator();

		
		while(iterator.hasNext())
		{
			GenericSpecimenVO specimenFormVO =(GenericSpecimenVO) iterator.next();

			GenericSpecimenVO specimenSessionVO =(GenericSpecimenVO)
			specimenMap.get(specimenFormVO.getUniqueIdentifier());

				if(specimenSessionVO!=null)
				{
					specimenSessionVO.setCheckedSpecimen(specimenFormVO.getCheckedSpecimen());
					specimenSessionVO.setDisplayName(specimenFormVO.getDisplayName());
					specimenSessionVO.setBarCode(specimenFormVO.getBarCode());
					specimenSessionVO.setContainerId(specimenFormVO.getContainerId());
					specimenSessionVO.setSelectedContainerName(specimenFormVO.getSelectedContainerName());
					specimenSessionVO.setPositionDimensionOne(specimenFormVO.getPositionDimensionOne());
					specimenSessionVO.setPositionDimensionTwo(specimenFormVO.getPositionDimensionTwo());
					specimenSessionVO.setQuantity(specimenFormVO.getQuantity());
					specimenSessionVO.setConcentration(specimenFormVO.getConcentration());					
				}

		}	
		updateAliquotToSession(summaryForm, selectedSpecimen);
		updateDerivedToSession(summaryForm, selectedSpecimen);
		
	}

	
	private void updateDerivedToSession(ViewSpecimenSummaryForm summaryForm,
			GenericSpecimenVO selectedSpecimen) {
		Iterator derivedIterator = summaryForm.getDerivedList().iterator();
		
		while(derivedIterator.hasNext())
		{
			GenericSpecimenVO derivedFormVO =(GenericSpecimenVO) derivedIterator.next();
			String derivedKey = derivedFormVO.getUniqueIdentifier();
			GenericSpecimenVO  derivedSessionVO = (GenericSpecimenVO) 
										getDerivedSessionObject(selectedSpecimen , derivedKey);
			if(derivedSessionVO != null)
			{
				derivedSessionVO.setCheckedSpecimen(derivedFormVO.getCheckedSpecimen());
				derivedSessionVO.setDisplayName(derivedFormVO.getDisplayName());
				derivedSessionVO.setBarCode(derivedFormVO.getBarCode());
				derivedSessionVO.setContainerId(derivedFormVO.getContainerId());
				derivedSessionVO.setSelectedContainerName(derivedFormVO.getSelectedContainerName());
				derivedSessionVO.setPositionDimensionOne(derivedFormVO.getPositionDimensionOne());
				derivedSessionVO.setPositionDimensionTwo(derivedFormVO.getPositionDimensionTwo());
				derivedSessionVO.setQuantity(derivedFormVO.getQuantity());
				derivedSessionVO.setConcentration(derivedFormVO.getConcentration());
			}
			
		}
	}
	
	private GenericSpecimen getDerivedSessionObject(GenericSpecimen parentSessionObject, String derivedKey)
	{
		LinkedHashMap deriveMap = parentSessionObject.getDeriveSpecimenCollection();
		if(deriveMap == null || deriveMap.isEmpty())
		{
			return null;
		}
		GenericSpecimen derivedSessionObject =(GenericSpecimen) deriveMap.get(derivedKey);
		if (derivedSessionObject != null)
		{
			return derivedSessionObject;	
		}
		Collection parentCollection = deriveMap.values();
		Iterator parentIterator = parentCollection.iterator();
		
		while(parentIterator.hasNext())
		{
			GenericSpecimen parentDerived = (GenericSpecimen) parentIterator.next();
			derivedSessionObject = getDerivedSessionObject(parentDerived, derivedKey);
			if (derivedSessionObject != null){
				return derivedSessionObject;
			}
		}
		//Search Derived in derived specimen tree.
		LinkedHashMap aliquotMap = parentSessionObject.getAliquotSpecimenCollection();
		
		if(aliquotMap != null && !aliquotMap.isEmpty())
		{
			parentCollection = aliquotMap.values();
			parentIterator = parentCollection.iterator();
			
			while(parentIterator.hasNext())
			{
				GenericSpecimen derivedSpecimen = (GenericSpecimen) parentIterator.next();
				derivedSessionObject = getDerivedSessionObject(derivedSpecimen, derivedKey);
				if (derivedSessionObject != null){
					return derivedSessionObject;
				}
			}
		}
		return null;
		
	}

	/**
	 * @param summaryForm
	 * @param selectedSpecimen
	 */
	private void updateAliquotToSession(ViewSpecimenSummaryForm summaryForm,
			GenericSpecimenVO selectedSpecimen) {
		Iterator aliquotIterator = summaryForm.getAliquotList().iterator();
		
		while(aliquotIterator.hasNext())
		{
			GenericSpecimenVO aliquotFormVO =(GenericSpecimenVO) aliquotIterator.next();
			String aliquotKey = aliquotFormVO.getUniqueIdentifier();
			GenericSpecimenVO  aliquotSessionVO = (GenericSpecimenVO) 
										getAliquotSessionObject(selectedSpecimen , aliquotKey);
			if(aliquotSessionVO != null)
			{
				aliquotSessionVO.setCheckedSpecimen(aliquotFormVO.getCheckedSpecimen());
				aliquotSessionVO.setDisplayName(aliquotFormVO.getDisplayName());
				aliquotSessionVO.setBarCode(aliquotFormVO.getBarCode());
				aliquotSessionVO.setContainerId(aliquotFormVO.getContainerId());
				aliquotSessionVO.setSelectedContainerName(aliquotFormVO.getSelectedContainerName());
				aliquotSessionVO.setPositionDimensionOne(aliquotFormVO.getPositionDimensionOne());
				aliquotSessionVO.setPositionDimensionTwo(aliquotFormVO.getPositionDimensionTwo());
				aliquotSessionVO.setQuantity(aliquotFormVO.getQuantity());
			}
			
		}
	}
	
	private GenericSpecimen getAliquotSessionObject(GenericSpecimen parentSessionObject, String aliquotKey)
	{
		LinkedHashMap aliquotMap = parentSessionObject.getAliquotSpecimenCollection();
		if(aliquotMap == null || aliquotMap.isEmpty())
		{
			return null;
		}
		GenericSpecimen aliquotSessionObject =(GenericSpecimen) aliquotMap.get(aliquotKey);
		if (aliquotSessionObject != null)
		{
			return aliquotSessionObject;	
		}
		Collection parentCollection = aliquotMap.values();
		Iterator parentIterator = parentCollection.iterator();
		
		while(parentIterator.hasNext())
		{
			GenericSpecimen parentAliquot = (GenericSpecimen) parentIterator.next();
			aliquotSessionObject = getAliquotSessionObject(parentAliquot, aliquotKey);
			if (aliquotSessionObject != null){
				return aliquotSessionObject;
			}
		}
		//Search Aliquot in derived specimen tree.
		LinkedHashMap deriveMap = parentSessionObject.getDeriveSpecimenCollection();
		
		if(deriveMap != null && !deriveMap.isEmpty())
		{
			parentCollection = deriveMap.values();
			parentIterator = parentCollection.iterator();
			
			while(parentIterator.hasNext())
			{
				GenericSpecimen derivedSpecimen = (GenericSpecimen) parentIterator.next();
				aliquotSessionObject = getAliquotSessionObject(derivedSpecimen, aliquotKey);
				if (aliquotSessionObject != null){
					return aliquotSessionObject;
				}
			}
		}
		return null;
		
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
		
		if (eventId != null || 
				ViewSpecimenSummaryForm.REQUEST_TYPE_ANTICIPAT_SPECIMENS.equals(summaryForm.getRequestType())) 
		{
			if(summaryForm.getRequestType() == null)
			{
				summaryForm.setRequestType(ViewSpecimenSummaryForm.REQUEST_TYPE_COLLECTION_PROTOCOL);
			}
			if (eventId == null)
			{
				eventId = "dummy";
			}
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
				
					eventId =(String) collectionProtocolEventMap.keySet().iterator().next();
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
			summaryForm.setEventId(eventId);			
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
			getNestedChildSpecimens(aliquotsList.values(), nestedAliquots, nestedDerives);
//			getNestedAliquotSpecimens(aliquotsList.values(), nestedAliquots);
//			getNestedDerivedSpecimens(aliquotsList.values(), nestedDerives);
			
		}

		if (derivedList != null && !derivedList.values().isEmpty()) 
		{
			nestedDerives.addAll(derivedList.values());
			getNestedChildSpecimens(derivedList.values(), nestedAliquots, nestedDerives);			
//			getNestedAliquotSpecimens(derivedList.values(), nestedAliquots);
//			getNestedDerivedSpecimens(derivedList.values(), nestedDerives);
		}
		
		summaryForm.setAliquotList(getSpecimenList(nestedAliquots));
		summaryForm.setDerivedList(getSpecimenList(nestedDerives));
		
	}

	private void getNestedChildSpecimens(Collection topChildCollection,
			Collection nestedAliquoteCollection, Collection nestedDerivedCollection) {


		Iterator iterator = topChildCollection.iterator();

		while (iterator.hasNext()) {
			GenericSpecimen specimen = (GenericSpecimen) iterator.next();

			if (specimen.getAliquotSpecimenCollection() != null) {
				Collection childSpecimen = specimen
						.getAliquotSpecimenCollection().values();

				if (!childSpecimen.isEmpty()) {
					nestedAliquoteCollection.addAll(childSpecimen);
					getNestedChildSpecimens(childSpecimen,
							nestedAliquoteCollection,nestedDerivedCollection);
				}				
			}

			if (specimen.getDeriveSpecimenCollection() != null) {
				Collection childSpecimen = specimen
						.getDeriveSpecimenCollection().values();

				if (!childSpecimen.isEmpty()) {
					nestedDerivedCollection.addAll(childSpecimen);
					getNestedChildSpecimens(childSpecimen,
							nestedAliquoteCollection,nestedDerivedCollection);
				}				
			}

			
		}
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
		if (!specimenColl.isEmpty())
		{
			specimenList.addAll(specimenColl);
		}
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
