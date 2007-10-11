package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.DomainObjectFactory;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.MultipleSpecimenValidationUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.Utility;
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
				

				HttpSession session = request.getSession();
				CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
	
				if (ViewSpecimenSummaryForm.UPDATE_USER_ACTION.equals(
						specimenSummaryForm.getUserAction()))
				{					
//					for disabling of CP set the collection protocol status: kalpana
					if(collectionProtocolBean!=null && collectionProtocolBean.getActivityStatus()!=null){
						ViewSpecimenSummaryForm.setCollectionProtocolStatus(collectionProtocolBean.getActivityStatus());
					}
					return mapping.findForward("updateCP");
				}
				else
				{
					CollectionProtocol collectionProtocol = CollectionProtocolUtil
					.populateCollectionProtocolObjects(request);

					insertCollectionProtocol(collectionProtocol,request.getSession());
					
				
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
					MultipleSpecimenValidationUtil
					.setMultipleSpecimensInSession(request, specimenCollectionGroup.getId());
					
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
		} 
		catch (Exception ex) {
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
					specimenCollectionGroup = (SpecimenCollectionGroup)
					specimen.getSpecimenCollectionGroup(); 
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
