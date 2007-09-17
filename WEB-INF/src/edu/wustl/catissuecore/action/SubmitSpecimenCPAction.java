package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Map;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.DomainObjectFactory;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

public class SubmitSpecimenCPAction extends Action {

	private ViewSpecimenSummaryForm specimenSummaryForm;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String target = Constants.SUCCESS;
		HashMap resultMap = new HashMap();
		try {
			specimenSummaryForm = (ViewSpecimenSummaryForm) form;
			String actionValue = request.getParameter("action");
			if ("collectionprotocol".equals(actionValue)) {
				insertCollectionProtocol(request);	
			}
			
			if ("specimens".equals(actionValue)) {
				insertSpecimens(request);
			}
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"object.add.successOnly","Collection Protocol"));
			saveMessages(request, actionMessages);
			target = Constants.SUCCESS;
		} catch (Exception ex) {
			target = Constants.FAILURE;
			String errorMsg = ex.getMessage();
			resultMap.put(Constants.ERROR_DETAIL, errorMsg);
			ex.printStackTrace();
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(actionErrors.GLOBAL_MESSAGE, new ActionError(
					"errors.item",ex.getMessage()));
			saveErrors(request, actionErrors);			
			
		}
		resultMap.put(Constants.MULTIPLE_SPECIMEN_RESULT, target);
		// writeMapToResponse(response, resultMap);
		Logger.out.debug("In MultipleSpecimenAppletAction :- resultMap : "
				+ resultMap);
		
		return mapping.findForward(target);

	}

	private void insertCollectionProtocol(HttpServletRequest request)
			throws Exception {
		HttpSession session = request.getSession();
		CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);

		LinkedHashMap<String, CollectionProtocolEventBean> cpEventMap = (LinkedHashMap) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);

		CollectionProtocol collectionProtocol = getCollectionProtocolDomainObject(collectionProtocolBean);
		Collection collectionProtocolEventList = new HashSet();
		Collection collectionProtocolEventBean = cpEventMap.values();
		Iterator cpEventIterator = collectionProtocolEventBean.iterator();

		while (cpEventIterator.hasNext()) {

			CollectionProtocolEventBean cpEventBean = (CollectionProtocolEventBean) cpEventIterator
					.next();
			CollectionProtocolEvent collectionProtocolEvent = getCollectionProtocolEvent(cpEventBean);
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			collectionProtocolEventList.add(collectionProtocolEvent);
		}
		
		collectionProtocol
				.setCollectionProtocolEventCollection(collectionProtocolEventList);
		IBizLogic bizLogic =BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
//		SessionDataBean sessionDataBean = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setUserId(new Long("1"));
		sessionDataBean.setUserName("admin@admin.com");
		
		bizLogic.insert(collectionProtocol, sessionDataBean, Constants.HIBERNATE_DAO); 
		
	}

	private CollectionProtocolEvent getCollectionProtocolEvent(
			CollectionProtocolEventBean cpEventBean) {

		CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();
		
		collectionProtocolEvent.setClinicalStatus(cpEventBean.getClinicalStatus());
		collectionProtocolEvent.setCollectionPointLabel(cpEventBean.getCollectionPointLabel());
		collectionProtocolEvent.setStudyCalendarEventPoint(cpEventBean.getStudyCalenderEventPoint());
		
		SpecimenCollectionRequirementGroup specimenCollectionRequirementGroup = new SpecimenCollectionRequirementGroup();
		
		specimenCollectionRequirementGroup.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		specimenCollectionRequirementGroup.setClinicalDiagnosis(cpEventBean.getClinicalDiagnosis());
		specimenCollectionRequirementGroup.setClinicalStatus(cpEventBean.getClinicalStatus());
		collectionProtocolEvent.setRequiredCollectionSpecimenGroup(specimenCollectionRequirementGroup);
		Collection specimenCollection =null;
		Map specimenMap =(Map)cpEventBean.getSpecimenRequirementbeanMap();
		
		if (specimenMap!=null && !specimenMap.isEmpty()){
			specimenCollection =getSpecimens(
					cpEventBean.getSpecimenRequirementbeanMap().values()
					,null, specimenCollectionRequirementGroup);	
		}
		
		specimenCollectionRequirementGroup.setSpecimenCollection(specimenCollection);
		
		//specimenCollectionRequirementGroup.setSpecimenCollectionSite()
		
		return collectionProtocolEvent;
	}

	private Collection getSpecimens(Collection specimenRequirementBeanColl, 
			Specimen parentSpecimen, SpecimenCollectionRequirementGroup requirementGroup ) {
		
		Collection specimenCollection = new LinkedHashSet();
		Iterator iterator = specimenRequirementBeanColl.iterator();
		
		while(iterator.hasNext())
		{
			SpecimenRequirementBean specimenRequirementBean =
						(SpecimenRequirementBean)iterator.next();
			Specimen specimen = getSpecimenDomainObject(specimenRequirementBean);
			specimen.setParentSpecimen(parentSpecimen);
			specimen.setSpecimenCollectionGroup(requirementGroup);
			specimenCollection.add(specimen);

			if(specimenRequirementBean.getAliquotSpecimenCollection()!=null)
			{
				Collection aliquotCollection= specimenRequirementBean.getAliquotSpecimenCollection().values();
				Collection childSpecimens = 
					getSpecimens(aliquotCollection, specimen, requirementGroup);
				specimenCollection.addAll(childSpecimens);
			}

			if(specimenRequirementBean.getDeriveSpecimenCollection()!=null)
			{
				Collection derivedCollection= specimenRequirementBean.getDeriveSpecimenCollection().values();
				Collection childSpecimens = 
					getSpecimens(derivedCollection, specimen, requirementGroup);
				specimenCollection.addAll(childSpecimens);
			}
			
			
		}
		
		return specimenCollection;
	}

	private Specimen getSpecimenDomainObject(SpecimenRequirementBean specimenRequirementBean){

		NewSpecimenForm form = new NewSpecimenForm();
		form.setClassName(specimenRequirementBean.getClassName());
		
		Specimen specimen;
		try {
			specimen = (Specimen) new DomainObjectFactory()
				.getDomainObject(Constants.NEW_SPECIMEN_FORM_ID, form);
		} catch (AssignDataException e1) {
			e1.printStackTrace();
			return null;
		}
		
		specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		specimen.setAvailable(Boolean.TRUE);
		Quantity availableQuantity = new Quantity();
		double value=0;
		String s=specimenRequirementBean.getQuantity();
		try{
			 value =Double.parseDouble(s);
		}catch(NumberFormatException e){
			value=0;
		}
		
		availableQuantity.setValue(value);
		specimen.setAvailableQuantity(availableQuantity);
		specimen.setInitialQuantity(availableQuantity);
		specimen.setLineage(specimenRequirementBean.getLineage());
		specimen.setPathologicalStatus(
				specimenRequirementBean.getPathologicalStatus());		
		specimen.setType(specimenRequirementBean.getType());
		StorageContainer storageContainer = new StorageContainer();
		storageContainer.setName(
				specimenRequirementBean.getStorageContainerForSpecimen());		
		specimen.setStorageContainer(storageContainer);
		
		return specimen;
	}
	private CollectionProtocol getCollectionProtocolDomainObject(
			CollectionProtocolBean cpBean) throws Exception {

		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		collectionProtocol.setAliquotInSameContainer(Boolean.TRUE);
		collectionProtocol.setConsentsWaived(cpBean.isConsentWaived());
		collectionProtocol.setConsentTierCollection(collectionProtocol.prepareConsentTierCollection(cpBean.getConsentValues()));
		Collection coordinatorCollection = new LinkedHashSet();
		long[] coordinatorsArr = cpBean.getProtocolCoordinatorIds();

		if (coordinatorsArr != null) {
			for (int i = 0; i < coordinatorsArr.length; i++) {
				if (coordinatorsArr[i] != -1) {
					User coordinator = new User();
					coordinator.setId(new Long(coordinatorsArr[i]));
					coordinatorCollection.add(coordinator);
				}
			}
			collectionProtocol.setCoordinatorCollection(coordinatorCollection);
		}

		collectionProtocol.setDescriptionURL(cpBean.getDescriptionURL());
		Integer enrollmentNo=null;
		try{
			enrollmentNo = new Integer(cpBean.getEnrollment());
		}catch(NumberFormatException e){
			enrollmentNo = new Integer(0);
		}
		collectionProtocol.setEnrollment(enrollmentNo);
		User principalInvestigator = new User();
		principalInvestigator.setId(new Long(cpBean
				.getPrincipalInvestigatorId()));

		collectionProtocol.setPrincipalInvestigator(principalInvestigator);
		collectionProtocol.setShortTitle(cpBean.getShortTitle());
		Date startDate = Utility.parseDate(cpBean.getStartDate(), Utility
				.datePattern(cpBean.getStartDate()));
		collectionProtocol.setStartDate(startDate);
		collectionProtocol.setTitle(cpBean.getTitle());
		collectionProtocol.setUnsignedConsentDocumentURL(cpBean
				.getUnsignedConsentURLName());

		return collectionProtocol;
	}

	private void insertSpecimens(HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		LinkedHashMap<String, GenericSpecimen> cpEventMap;
		cpEventMap = (LinkedHashMap) session
				.getAttribute(Constants.SPECIMEN_LIST_SESSION_MAP);

		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(
				Constants.NEW_SPECIMEN_FORM_ID);
		SessionDataBean sessionDataBean = (SessionDataBean) session
				.getAttribute(Constants.SESSION_DATA);
		bizLogic.insert(cpEventMap, sessionDataBean, Constants.HIBERNATE_DAO);

	}
}
