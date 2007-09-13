package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
				eventId = (String) request.getParameter(Constants.COLLECTION_PROTOCOL_EVENT_ID);
	//			new Generatedata().generate(request);
			}
					
			LinkedHashMap<String, GenericSpecimen> specimenMap;
			if (eventId != null)
			{
				Map collectionProtocolEventMap = (Map)session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
				CollectionProtocolEventBean collectionProtocolEventBean =(CollectionProtocolEventBean)collectionProtocolEventMap.get(eventId);
				specimenMap = (LinkedHashMap)collectionProtocolEventBean.getSpecimenRequirementbeanMap();
			}
			else 
			{
				specimenMap = (LinkedHashMap) session.getAttribute(Constants.SPECIMEN_LIST_SESSION_MAP);
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

					if(aliqutesList != null && !aliqutesList.values().isEmpty())
					{						
						Collection nestedAliquots = new LinkedHashSet();
						getNestedAliquots(aliqutesList.values(),nestedAliquots);
						summaryForm.setAliquoteList(nestedAliquots);
					}
					
					if(derivedList != null && !derivedList.values().isEmpty())
					{						
						Collection nestedDerives = new LinkedHashSet();
						getNestedDerives(derivedList.values(),nestedDerives);
						summaryForm.setDerivedList(nestedDerives);
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
	public void getNestedAliquots(Collection topChildCollection, Collection nestedCollection){
		
		nestedCollection.addAll(topChildCollection);
		Iterator iterator = topChildCollection.iterator();
		
		while(iterator.hasNext()){
			GenericSpecimen specimen = (GenericSpecimen) iterator.next();
			
			if (specimen.getAliquotSpecimenCollection()!=null){
				Collection childAliquots = specimen.getAliquotSpecimenCollection().values();
				if(!childAliquots.isEmpty()){
					getNestedAliquots(childAliquots, nestedCollection);
				}
			}
			
		}
	}
	public void getNestedDerives(Collection topChildCollection, Collection nestedCollection){
		
		nestedCollection.addAll(topChildCollection);
		Iterator iterator = topChildCollection.iterator();
		
		while(iterator.hasNext()){
			GenericSpecimen specimen = (GenericSpecimen) iterator.next();
			
			if (specimen.getDeriveSpecimenCollection()!=null){
				
				Collection childDerives = specimen.getDeriveSpecimenCollection().values();
				if(!childDerives.isEmpty()){
					getNestedDerives(childDerives, nestedCollection);
				}
			}
			
		}
	}
/*
	class Generatedata{
		
		public  void generate(HttpServletRequest request){
			HttpSession session = request.getSession();
			CollectionProtocolBean collectionProtocolBean = getCPBeanObject();
			session.setAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN, collectionProtocolBean);
			CollectionProtocolEventBean eventBean = new CollectionProtocolEventBean();
			eventBean.setClinicalStatus("New Diagnosis");
			eventBean.setStudyCalenderEventPoint(new Double(1));
			eventBean.setUniqueIdentifier("1");
			LinkedHashMap eventMap = new LinkedHashMap();
			eventMap.put("1", eventBean);
			session.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP, eventMap);
			LinkedHashMap specimenMap =createSpecimens(3,"Specimen",null);
			SpecimenRequirementBean specimen = (SpecimenRequirementBean)specimenMap.get("Specimen0");
			specimen.setAliquotSpecimenCollection(createSpecimens(2,"Aliquot",specimen.getDisplayName()));
			specimen.setDeriveSpecimenCollection(createSpecimens(1,"Derived",specimen.getDisplayName()));
			eventBean.setSpecimenRequirementbeanMap(specimenMap);
			specimen = (SpecimenRequirementBean)specimen.getDeriveSpecimenCollection().get("Derived0");
			specimen.setDeriveSpecimenCollection(createSpecimens(1,"D_Specimen",specimen.getDisplayName()));
		}
		private LinkedHashMap createSpecimens(int count,String type, String parentName){
			
			LinkedHashMap specimenMap = new LinkedHashMap();
			for(int i=0;i<count;i++){
				SpecimenRequirementBean specimenBean = new SpecimenRequirementBean();
				specimenBean.setClassName("Tissue");
				specimenBean.setType("Frozen Cell Block");
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
				specimenMap.put(specimenBean.getUniqueIdentifier(), specimenBean);
				
			}
			return specimenMap;
		}
		private CollectionProtocolBean getCPBeanObject(){
			CollectionProtocolBean collectionProtocol = new CollectionProtocolBean();
			Collection consentTierColl = new HashSet();
//			
//			ConsentTier c1 = new ConsentTier();
//			c1.setStatement("Consent for aids research");
//			consentTierColl.add(c1);
//			ConsentTier c2 = new ConsentTier();
//			c2.setStatement("Consent for cancer research");
//			consentTierColl.add(c2);		
//			ConsentTier c3 = new ConsentTier();
//			c3.setStatement("Consent for Tb research");
//			consentTierColl.add(c3);
//			
//			collectionProtocol. setConsentTierCollection(consentTierColl);
//			
			
			collectionProtocol.setDescriptionURL("");			
			collectionProtocol.setEnrollment(null);
			collectionProtocol.setIrbID("7777");
			collectionProtocol.setTitle("Aids Study Collection Protocol For Consent track");
			collectionProtocol.setShortTitle("Cp Consent");
			collectionProtocol.setUnsignedConsentURLName("C:\\consent1.pdf");
			
			collectionProtocol.setStartDate("28/07/1975");
			collectionProtocol.setPrincipalInvestigatorId(1L);
			return collectionProtocol;
			
		}
	} */
}
