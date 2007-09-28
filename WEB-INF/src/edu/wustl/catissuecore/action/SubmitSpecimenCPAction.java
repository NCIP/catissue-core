package edu.wustl.catissuecore.action;

import java.util.ArrayList;
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
import edu.wustl.catissuecore.bean.SpecimenDataBean;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.DomainObjectFactory;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.flex.SpecimenBean;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.logger.Logger;

public class SubmitSpecimenCPAction extends BaseAction {

	private ViewSpecimenSummaryForm specimenSummaryForm;
	private SpecimenCollectionGroup specimenCollectionGroup = null;
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String target = Constants.SUCCESS;
		String pageOf = request.getParameter(Constants.PAGEOF);
		HashMap resultMap = new HashMap();
		try {
			specimenSummaryForm = (ViewSpecimenSummaryForm) form;
			String actionValue = request.getParameter("action");

			if (ViewSpecimenSummaryForm.REQUEST_TYPE_COLLECTION_PROTOCOL.equals(
					specimenSummaryForm.getRequestType())) {
				
				CollectionProtocol collectionProtocol = populateCollectionProtocolObjects(request);

				if (ViewSpecimenSummaryForm.UPDATE_USER_ACTION.equals(
						specimenSummaryForm.getUserAction()))
				{
					specimenSummaryForm.setSummaryObject(collectionProtocol);
					return mapping.findForward("updateCP");
				}
				else
				{
					insertCollectionProtocol(collectionProtocol,request.getSession());
					HttpSession session = request.getSession();
					CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
							.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);

					collectionProtocolBean.setIdentifier(collectionProtocol.getId());

					CollectionProtocolUtil.updateSession(request,collectionProtocol.getId());
					
				}
				ActionMessages actionMessages = new ActionMessages();
				actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"object.add.successOnly","Collection Protocol"));
				saveMessages(request, actionMessages);

				
			}
			
			if (ViewSpecimenSummaryForm.REQUEST_TYPE_MULTI_SPECIMENS.equals(
					specimenSummaryForm.getRequestType())) 
			{
				LinkedHashMap cpEventMap = populateSpecimenDomainObjectMap(request);
				if (ViewSpecimenSummaryForm.UPDATE_USER_ACTION.equals(
						specimenSummaryForm.getUserAction()))
				{
					//execute update multiplespecimens.
				}
				else
				{
					insertSpecimens(cpEventMap,request.getSession());
					
				} 
				ActionMessages actionMessages = new ActionMessages();
				actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"object.add.successOnly","Specimen(s)"));
				saveMessages(request, actionMessages);
				
			}
			
			target = Constants.SUCCESS;
			
			if(pageOf != null && pageOf.equals("pageOfMultipleSpWithMenu"))
				target = pageOf;
			
			specimenSummaryForm.setUserAction(ViewSpecimenSummaryForm.UPDATE_USER_ACTION);
		} catch (Exception ex) {
			target = Constants.FAILURE;
			if(pageOf!=null && pageOf.equals("pageOfMultipleSpWithMenu"))
			{
				target = "pageOfMultipleSpWithMenuFailure";
			}
				
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

	/**
	 * @param cpEventMap
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 */
	private void insertSpecimens(LinkedHashMap cpEventMap, HttpSession session )
			throws BizLogicException, UserNotAuthorizedException {
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(
				Constants.NEW_SPECIMEN_FORM_ID);
		SessionDataBean sessionDataBean = (SessionDataBean) session
				.getAttribute(Constants.SESSION_DATA);
		bizLogic.insert(cpEventMap, sessionDataBean, Constants.HIBERNATE_DAO);
	}

	/**
	 * @param collectionProtocol
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 */
	private void insertCollectionProtocol(CollectionProtocol collectionProtocol, HttpSession session)
			throws BizLogicException, UserNotAuthorizedException {
		IBizLogic bizLogic =BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
				SessionDataBean sessionDataBean = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);		
		bizLogic.insert(collectionProtocol, sessionDataBean, Constants.HIBERNATE_DAO);
	}

	private CollectionProtocol populateCollectionProtocolObjects(HttpServletRequest request)
			throws Exception {
		HttpSession session = request.getSession();
		CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);

		LinkedHashMap<String, CollectionProtocolEventBean> cpEventMap = (LinkedHashMap) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);

		CollectionProtocol collectionProtocol = createCollectionProtocolDomainObject(collectionProtocolBean);
		Collection collectionProtocolEventList = new HashSet();
		Collection collectionProtocolEventBeanColl = cpEventMap.values();
		if (collectionProtocolEventBeanColl != null)
		{
			Iterator cpEventIterator = collectionProtocolEventBeanColl.iterator();
	
			while (cpEventIterator.hasNext()) {
	
				CollectionProtocolEventBean cpEventBean = (CollectionProtocolEventBean) cpEventIterator
						.next();
				CollectionProtocolEvent collectionProtocolEvent = getCollectionProtocolEvent(cpEventBean);
				collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
				collectionProtocolEventList.add(collectionProtocolEvent);
			}
		}		
		collectionProtocol
				.setCollectionProtocolEventCollection(collectionProtocolEventList);
		return collectionProtocol;
	}

	/**
	 * This function used to create CollectionProtocolEvent domain object
	 * from given CollectionProtocolEventBean Object.
	 * @param cpEventBean 
	 * @return CollectionProtocolEvent domain object.
	 */
	private CollectionProtocolEvent getCollectionProtocolEvent(
			CollectionProtocolEventBean cpEventBean) {

		CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();
		
		collectionProtocolEvent.setClinicalStatus(cpEventBean.getClinicalStatus());
		collectionProtocolEvent.setCollectionPointLabel(cpEventBean.getCollectionPointLabel());
		collectionProtocolEvent.setStudyCalendarEventPoint(cpEventBean.getStudyCalenderEventPoint());
		
		SpecimenCollectionRequirementGroup specimenCollectionRequirementGroup = new SpecimenCollectionRequirementGroup();
		long scgId = cpEventBean.getSpecimenCollRequirementGroupId();
		if (scgId!= -1)
		{
			specimenCollectionRequirementGroup.setId(new Long(scgId));
		}
		
		specimenCollectionRequirementGroup.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		specimenCollectionRequirementGroup.setClinicalDiagnosis(cpEventBean.getClinicalDiagnosis());
		specimenCollectionRequirementGroup.setClinicalStatus(cpEventBean.getClinicalStatus());
		collectionProtocolEvent.setRequiredCollectionSpecimenGroup(specimenCollectionRequirementGroup);
		if (cpEventBean.getId()==-1){
			collectionProtocolEvent.setId(null);
		}
		else
		{
			collectionProtocolEvent.setId(new Long(cpEventBean.getId()));
		}
		Collection specimenCollection =null;
		Map specimenMap =(Map)cpEventBean.getSpecimenRequirementbeanMap();
		
		if (specimenMap!=null && !specimenMap.isEmpty()){
			specimenCollection =getSpecimens(
					specimenMap.values()
					,null, specimenCollectionRequirementGroup);	
		}
		
		specimenCollectionRequirementGroup.setSpecimenCollection(specimenCollection);
		
		//specimenCollectionRequirementGroup.setSpecimenCollectionSite()
		
		return collectionProtocolEvent;
	}

	
	/**
	 * creates collection of Specimen domain objects 
	 * @param specimenRequirementBeanColl
	 * @param parentSpecimen
	 * @param requirementGroup
	 * @return
	 */
	private Collection getSpecimens(Collection specimenRequirementBeanColl, 
			Specimen parentSpecimen, SpecimenCollectionRequirementGroup requirementGroup ) {
		
		Collection specimenCollection = new LinkedHashSet();
		Iterator iterator = specimenRequirementBeanColl.iterator();
		
		while(iterator.hasNext())
		{
			SpecimenRequirementBean specimenRequirementBean =
						(SpecimenRequirementBean)iterator.next();
			Specimen specimen = getSpecimenDomainObject(specimenRequirementBean);
			specimen.setIsCollectionProtocolRequirement(Boolean.TRUE);
			specimen.setParentSpecimen(parentSpecimen);
			
			if (parentSpecimen == null)
			{
					SpecimenCharacteristics specimenCharacteristics =
							new SpecimenCharacteristics();
					long id =specimenRequirementBean.getSpecimenCharsId();
					if(id != -1)
					{
						specimenCharacteristics.setId(new Long(id));
					}
					specimenCharacteristics.setTissueSide(
							specimenRequirementBean.getTissueSide());
					specimenCharacteristics.setTissueSite(
							specimenRequirementBean.getTissueSite());
					specimen.setSpecimenCollectionGroup(requirementGroup);					
					specimen.setSpecimenCharacteristics(specimenCharacteristics);
			}
			else
			{
				specimen.setSpecimenCharacteristics(
						parentSpecimen.getSpecimenCharacteristics());
			}
			specimen.setLineage(specimenRequirementBean.getLineage());
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

	/**
	 * creates specimen domain object from given specimen requirement bean.
	 * @param specimenRequirementBean
	 * @return
	 */
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
		if (specimenRequirementBean.getId()==-1)
		{
			specimen.setId(null);
		}
		else
		{
			specimen.setId(new Long(specimenRequirementBean.getId()));
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
		StorageContainer storageContainer = null; //new StorageContainer();
//		storageContainer.setName(
//				specimenRequirementBean.getStorageContainerForSpecimen());
		
		specimen.setStorageContainer(storageContainer);
		
		return specimen;
	}

	/**
	 * Creates collection protocol domain object from given collection protocol bean.
	 * @param cpBean
	 * @return
	 * @throws Exception
	 */
	private CollectionProtocol createCollectionProtocolDomainObject(
			CollectionProtocolBean cpBean) throws Exception {

		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setId(cpBean.getIdentifier());
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

	/**
	 * Multiple specimen 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private LinkedHashMap populateSpecimenDomainObjectMap(HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		LinkedHashMap<String, GenericSpecimen> cpEventMap;
		cpEventMap = (LinkedHashMap) session
				.getAttribute(Constants.SPECIMEN_LIST_SESSION_MAP);
		LinkedHashMap specimenMap = new LinkedHashMap();
		Collection specimenSessionColl = cpEventMap.values();
		Iterator iterator = specimenSessionColl.iterator();
		
		while(iterator.hasNext())
		{
			SpecimenDataBean specimenDataBean =(SpecimenDataBean) iterator.next();
			Specimen specimen =getSpecimenDomainObjectFromObject(specimenDataBean);
			
			if (specimen.getSpecimenCollectionGroup()!=null)
			{
				specimen.setLineage(Constants.NEW_SPECIMEN);
				if (specimenCollectionGroup == null)
				{
					/*specimenCollectionGroup = (SpecimenCollectionGroup)
					specimen.getSpecimenCollectionGroup();*/ 
				}
			}
			else
			{
				
				specimen.setLineage(Constants.DERIVED_SPECIMEN);
				specimen.setParentSpecimen(specimenDataBean.getParentSpecimen());
				
				if(specimenDataBean.getSpecimenCollectionGroup()== null)
				{
					Specimen parentSpeciemn = specimen.getParentSpecimen();
					
					Long scgId =
						parentSpeciemn.getSpecimenCollectionGroup().getId();
					
					IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
					List list =iBizLogic.retrieve(SpecimenCollectionGroup.class.getName(),
							"id", scgId);
					specimen.setSpecimenCollectionGroup((AbstractSpecimenCollectionGroup) list.get(0));
					

				}
				else
				{
					specimen.setSpecimenCollectionGroup(specimenDataBean.getSpecimenCollectionGroup());
				}
			}
			
			SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
			specimenCharacteristics.setTissueSide(specimenDataBean.getTissueSide());
			specimenCharacteristics.setTissueSite(specimenDataBean.getTissueSite());
			specimen.setSpecimenCharacteristics(specimenCharacteristics);
			
			ArrayList childSpecimenList = new ArrayList();

			if (specimenDataBean.getAliquotSpecimenCollection() != null)
			{
				getAliquotSpecimens(specimenDataBean, specimen,
						childSpecimenList);
			}

			if (specimenDataBean.getDeriveSpecimenCollection() != null)
			{
				getDerivedSpecimens(specimenDataBean, specimen,
						childSpecimenList);
			}			
			specimenMap.put(specimen, childSpecimenList);
			
		}
		return specimenMap;

	}

	/**
	 * @param specimenDataBean
	 * @param parentSpecimen
	 * @param childSpecimenList
	 */ 
	private void getDerivedSpecimens(SpecimenDataBean specimenDataBean,
			Specimen parentSpecimen, ArrayList childSpecimenList) {
		Collection derivedSpecimenCollection = specimenDataBean
											.getDeriveSpecimenCollection().values();
		Iterator derivedSpecimenIteraror = derivedSpecimenCollection.iterator();
		
		while(derivedSpecimenIteraror.hasNext())
		{
			SpecimenDataBean derivedSpecimenBean =
				(SpecimenDataBean) derivedSpecimenIteraror.next();
			Specimen derivedSpecimen = getSpecimenDomainObjectFromObject(derivedSpecimenBean);
			derivedSpecimen.setParentSpecimen(parentSpecimen);
			
			derivedSpecimen.setLineage(Constants.DERIVED_SPECIMEN);
			derivedSpecimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
			childSpecimenList.add(derivedSpecimen);
		}
	}

	/**
	 * @param specimenDataBean
	 * @param parentSpecimen
	 * @param childSpecimenList
	 */
	private void getAliquotSpecimens(SpecimenDataBean specimenDataBean,
			Specimen parentSpecimen, ArrayList childSpecimenList) {
		Collection aliquotSpecimenCollection = specimenDataBean
											.getAliquotSpecimenCollection().values();
		Iterator aliquotSpecimenIteraror = aliquotSpecimenCollection.iterator();
		
		while(aliquotSpecimenIteraror.hasNext())
		{
			SpecimenDataBean aliquotSpecimenBean =
				(SpecimenDataBean) aliquotSpecimenIteraror.next();
			Specimen aliquotSpecimen = getSpecimenDomainObjectFromObject(aliquotSpecimenBean);
			aliquotSpecimen.setParentSpecimen(parentSpecimen);
			aliquotSpecimen.setLineage(Constants.ALIQUOT);
			aliquotSpecimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
			childSpecimenList.add(aliquotSpecimen);
		}
	}
	
	private Specimen getSpecimenDomainObjectFromObject(SpecimenDataBean specimenDataBean)
	{
		NewSpecimenForm form = new NewSpecimenForm();
		form.setClassName(specimenDataBean.getClassName());
		
		Specimen specimen;
		try {
			specimen = (Specimen) new DomainObjectFactory()
				.getDomainObject(Constants.NEW_SPECIMEN_FORM_ID, form);
		} catch (AssignDataException e1) {
			e1.printStackTrace();
			return null;
		}		
		
		specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		specimen.setBarcode(specimenDataBean.getBarcode());
		specimen.setComment(specimenDataBean.getComment());
		specimen.setCreatedOn(new Date());
		specimen.setCollectionStatus(Constants.SPECIMEN_COLLECTED);
		specimen.setLabel(specimenDataBean.getLabel());
		specimen.setPathologicalStatus(specimenDataBean.getPathologicalStatus());
	
		specimen.setAvailable(Boolean.TRUE);
		Quantity availableQuantity = new Quantity();
		double value=0;
		String s=specimenDataBean.getQuantity();
		try{
			 value =Double.parseDouble(s);
		}catch(NumberFormatException e){
			value=0;
		}
		
		availableQuantity.setValue(value);
		specimen.setAvailableQuantity(availableQuantity);
		specimen.setInitialQuantity(availableQuantity);
		specimen.setLineage(specimenDataBean.getLineage());
		specimen.setPathologicalStatus(
				specimenDataBean.getPathologicalStatus());		
		specimen.setType(specimenDataBean.getType());
		
		specimen.setExternalIdentifierCollection(specimenDataBean.getExternalIdentifierCollection());
		specimen.setBiohazardCollection(specimenDataBean.getBiohazardCollection());
		specimen.setSpecimenEventCollection(specimenDataBean.getSpecimenEventCollection());
		
		if(specimenDataBean.getSpecimenEventCollection()!=null && !specimenDataBean.getSpecimenEventCollection().isEmpty())
		{
			Iterator iterator = specimenDataBean.getSpecimenEventCollection().iterator();

			while(iterator.hasNext())
			{
				SpecimenEventParameters specimenEventParameters =
					(SpecimenEventParameters) iterator.next();
				specimenEventParameters.setSpecimen(specimen);
				
			}
		}
		
		specimen.setSpecimenCollectionGroup(specimenDataBean.getSpecimenCollectionGroup());
		
		StorageContainer storageContainer = new StorageContainer();
		storageContainer.setName(
				specimenDataBean.getStorageContainerForSpecimen());
		
		specimen.setStorageContainer(null);
		
		return specimen;

	}
}
