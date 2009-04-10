package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.catissuecore.util.SpecimenDetailsTagUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.exception.DAOException;

public class ViewSpecimenSummaryAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String target = Constants.SUCCESS;
			ViewSpecimenSummaryForm summaryForm = (ViewSpecimenSummaryForm) form;
		try {
			HttpSession session = request.getSession();
//			summaryForm.setLastSelectedSpecimenId(summaryForm.getSelectedSpecimenId());
			// Mandar : 5Aug08 ----------- start
			String sid = (String)request.getParameter("sid");
			if(sid!= null)
			{
				getAvailablePosition(request, response);
				return null;
			}	
//			Mandar : 5Aug08 ----------- end
			String eventId = summaryForm.getEventId();
			session.setAttribute(Constants.TREE_NODE_ID,(String)request.getParameter("nodeId"));
			
			Object obj = request.getAttribute("SCGFORM");
			request.setAttribute("SCGFORM", obj);
			target =request.getParameter("target");
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
			
			if (summaryForm.getSpecimenList()!= null  )
			{
				updateSessionBean(summaryForm, session);
				verifyCollectedStatus(summaryForm, session);	
				this.verifyPrintStatus(summaryForm, session);//janhavi
			}
		
			if(request.getParameter("save")!=null)
			{
				if (!isTokenValid(request))
				{
					summaryForm.setReadOnly(true);
					throw new CatissueException ("cannot submit duplicate request.");
				}

				resetToken(request);
				if((summaryForm.getSubmitAction().equals("bulkUpdateSpecimens") || summaryForm.getSubmitAction().equals("pageOfbulkUpdateSpecimens")) && (request.getParameter("printflag")!=null && request.getParameter("printflag").equals("1")))
				{
					request.setAttribute("printflag","1");
					return mapping.findForward(summaryForm.getSubmitAction() );
				}
				else
				{
					return mapping.findForward(summaryForm.getSubmitAction());
				}
			}
			else
			{
				saveToken(request);
			}
			

			CollectionProtocolBean collectionProtocolBean = 
				(CollectionProtocolBean)session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
						
			//for disabling of CP set the collection protocol status: kalpana
	
			if(collectionProtocolBean!=null && collectionProtocolBean.getActivityStatus()!=null){
			
			//checked the associated specimens to the cp	
				boolean isSpecimenExist=(boolean)isSpecimenExists((Long)collectionProtocolBean.getIdentifier());
				if(isSpecimenExist)
				{
					ViewSpecimenSummaryForm.setSpecimenExist("true");
				}
				else
				{
					ViewSpecimenSummaryForm.setSpecimenExist("false");
				}
		
			
				ViewSpecimenSummaryForm.setCollectionProtocolStatus(collectionProtocolBean.getActivityStatus());
			}
			
			LinkedHashMap<String, GenericSpecimen> specimenMap = 
							getSpecimensFromSessoin(session, eventId, summaryForm);

			if (specimenMap != null) {
				populateSpecimenSummaryForm(summaryForm, specimenMap);
			} 

			if (ViewSpecimenSummaryForm.REQUEST_TYPE_COLLECTION_PROTOCOL.equals(summaryForm.getRequestType()))
			{
				summaryForm.setUserAction(ViewSpecimenSummaryForm.ADD_USER_ACTION);
				if("update".equals(collectionProtocolBean.getOperation()))
				{
					summaryForm.setUserAction(ViewSpecimenSummaryForm.UPDATE_USER_ACTION);
				}
			}
			String pageOf = request.getParameter(Constants.PAGE_OF);
			request.setAttribute(Constants.PAGE_OF,pageOf);
			
			//Mandar: 16May2008 : For specimenDetails customtag --- start ---
			if("anticipatory".equalsIgnoreCase(target) )
			{
				SpecimenDetailsTagUtil.setAnticipatorySpecimenDetails(request, summaryForm, false);
			}
			else if ("pageOfMultipleSpWithMenu".equalsIgnoreCase(target))
			{
				SpecimenDetailsTagUtil.setAnticipatorySpecimenDetails(request, summaryForm, true);
			}
			else 
			{
				SpecimenDetailsTagUtil.setSpecimenSummaryDetails(request, summaryForm);
			}
			
			//Mandar: 16May2008 : For specimenDetails customtag --- end ---
			summaryForm.setLastSelectedSpecimenId(summaryForm.getSelectedSpecimenId());
			if(pageOf != null && ViewSpecimenSummaryForm.REQUEST_TYPE_MULTI_SPECIMENS.equals(summaryForm.getRequestType()))
			{
				//request.setAttribute(Constants.PAGE_OF,pageOf);
				return mapping.findForward(target);
			}
			

			return mapping.findForward(target);
		} catch (Exception exception) {
//			exception.printStackTrace();
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(actionErrors.GLOBAL_MESSAGE, new ActionError(
					"errors.item",exception.getMessage()));
			saveErrors(request, actionErrors);		
			//Mandar: 17JULY2008 : For specimenDetails customtag --- start ---
			if("anticipatory".equalsIgnoreCase(target) )
			{
				SpecimenDetailsTagUtil.setAnticipatorySpecimenDetails(request, summaryForm, false);
			}
			else if ("pageOfMultipleSpWithMenu".equalsIgnoreCase(target))
			{
				SpecimenDetailsTagUtil.setAnticipatorySpecimenDetails(request, summaryForm, true);
			}
			else 
			{
				SpecimenDetailsTagUtil.setSpecimenSummaryDetails(request, summaryForm);
			}
			
			//Mandar: 17JULY2008 : For specimenDetails customtag --- end ---

			return mapping.findForward(target);
		}

	}

	/**
	 * @param summaryForm
	 */
	private void updateSessionBean(ViewSpecimenSummaryForm summaryForm, HttpSession session)
	{
		String eventId = summaryForm.getEventId();
		if (eventId == null || session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP) == null)
		{
			return;
		}
		
		Map collectionProtocolEventMap = (Map) session
		.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);		
		CollectionProtocolEventBean eventBean =(CollectionProtocolEventBean)
							collectionProtocolEventMap.get(eventId);	// get nullpointer sometimes
		LinkedHashMap specimenMap = (LinkedHashMap)eventBean.getSpecimenRequirementbeanMap();
		String selectedItem = summaryForm.getLastSelectedSpecimenId();
		GenericSpecimen selectedSpecimen=(GenericSpecimen) specimenMap.get(selectedItem);
		
		updateSpecimenToSession(summaryForm, specimenMap);
		if(selectedSpecimen != null)
		{
			updateAliquotToSession(summaryForm, selectedSpecimen);
			updateDerivedToSession(summaryForm, selectedSpecimen);
		}
	}

	/**
	 * @param summaryForm
	 * @param specimenMap
	 */
	private void updateSpecimenToSession(ViewSpecimenSummaryForm summaryForm,
			LinkedHashMap specimenMap) {
		Collection specimenCollection = specimenMap.values();
		Iterator iterator = summaryForm.getSpecimenList().iterator();

		
		while(iterator.hasNext())
		{
			GenericSpecimen specimenFormVO =(GenericSpecimen) iterator.next();

			GenericSpecimen specimenSessionVO =(GenericSpecimen)
			specimenMap.get(specimenFormVO.getUniqueIdentifier());

				if(specimenSessionVO!=null)
				{
					setFormValuesToSession(specimenFormVO, specimenSessionVO);
				}

		}
	}


	/**
	 * @param summaryForm
	 * @param selectedSpecimen
	 */
	private void updateAliquotToSession(ViewSpecimenSummaryForm summaryForm,
			GenericSpecimen selectedSpecimen) {
		Iterator aliquotIterator = summaryForm.getAliquotList().iterator();
		
		while(aliquotIterator.hasNext())
		{
			GenericSpecimen aliquotFormVO =(GenericSpecimen) aliquotIterator.next();
			String aliquotKey = aliquotFormVO.getUniqueIdentifier();
			GenericSpecimen  aliquotSessionVO = (GenericSpecimen) 
										getAliquotSessionObject(selectedSpecimen , aliquotKey);
			if(aliquotSessionVO != null)
			{
				setFormValuesToSession(aliquotFormVO, aliquotSessionVO);
			}
			
		}
	}
	
	
	private void updateDerivedToSession(ViewSpecimenSummaryForm summaryForm,
			GenericSpecimen selectedSpecimen) {
		Iterator derivedIterator = summaryForm.getDerivedList().iterator();
		
		while(derivedIterator.hasNext())
		{
			GenericSpecimen derivedFormVO =(GenericSpecimen) derivedIterator.next();
			String derivedKey = derivedFormVO.getUniqueIdentifier();
			GenericSpecimen  derivedSessionVO = (GenericSpecimen) 
										getDerivedSessionObject(selectedSpecimen , derivedKey);
			if(derivedSessionVO != null)
			{
				setFormValuesToSession(derivedFormVO, derivedSessionVO);
			}

		}
	}

	/**
	 * @param derivedFormVO
	 * @param derivedSessionVO
	 */
	private void setFormValuesToSession(GenericSpecimen derivedFormVO,
			GenericSpecimen derivedSessionVO) {
		derivedSessionVO.setCheckedSpecimen(derivedFormVO.getCheckedSpecimen());
		derivedSessionVO.setPrintSpecimen(derivedFormVO.getPrintSpecimen());//janhavi
		derivedSessionVO.setDisplayName(derivedFormVO.getDisplayName());
		derivedSessionVO.setBarCode(derivedFormVO.getBarCode());
		//derivedSessionVO.setContainerId(derivedFormVO.getContainerId());
		derivedSessionVO.setContainerId(null);
		//Mandar : 6August08 ------- start
		derivedSessionVO.setStorageContainerForSpecimen(derivedFormVO.getStorageContainerForSpecimen());
		//Mandar : 6August08 ------- end
		derivedSessionVO.setSelectedContainerName(derivedFormVO.getSelectedContainerName());
		derivedSessionVO.setPositionDimensionOne(derivedFormVO.getPositionDimensionOne());
		derivedSessionVO.setPositionDimensionTwo(derivedFormVO.getPositionDimensionTwo());
		derivedSessionVO.setQuantity(derivedFormVO.getQuantity());
		derivedSessionVO.setConcentration(derivedFormVO.getConcentration());
		derivedSessionVO.setFormSpecimenVo(derivedFormVO);
	}
	
	private GenericSpecimen getDerivedSessionObject(GenericSpecimen parentSessionObject, String derivedKey)
	{
		LinkedHashMap deriveMap = parentSessionObject.getDeriveSpecimenCollection();
		Collection parentCollection;
		if(deriveMap != null && !deriveMap.isEmpty())
		{
		//	return null;
		
			GenericSpecimen derivedSessionObject =(GenericSpecimen) deriveMap.get(derivedKey);
			if (derivedSessionObject != null)
			{
				return derivedSessionObject;	
			}
			parentCollection = deriveMap.values();
			Iterator parentIterator = parentCollection.iterator();
			
			while(parentIterator.hasNext())
			{
				GenericSpecimen parentDerived = (GenericSpecimen) parentIterator.next();
				derivedSessionObject = getDerivedSessionObject(parentDerived, derivedKey);
				if (derivedSessionObject != null){
					return derivedSessionObject;
				}
			}
		}
		//Search Derived in derived specimen tree.
		LinkedHashMap aliquotMap = parentSessionObject.getAliquotSpecimenCollection();
		
		if(aliquotMap != null && !aliquotMap.isEmpty())
		{
			parentCollection = aliquotMap.values();
			Iterator parentIterator = parentCollection.iterator();
			
			while(parentIterator.hasNext())
			{
				GenericSpecimen derivedSpecimen = (GenericSpecimen) parentIterator.next();
				GenericSpecimen derivedSessionObject = getDerivedSessionObject(derivedSpecimen, derivedKey);
				if (derivedSessionObject != null){
					return derivedSessionObject;
				}
			}
		}
		return null;
		
	}
	private GenericSpecimen getAliquotSessionObject(GenericSpecimen parentSessionObject, String aliquotKey)
	{
		LinkedHashMap aliquotMap = parentSessionObject.getAliquotSpecimenCollection();
		if(aliquotMap != null && !aliquotMap.isEmpty())
		{
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

		}
		//Search Aliquot in derived specimen tree.
		LinkedHashMap deriveMap = parentSessionObject.getDeriveSpecimenCollection();
		
		if(deriveMap != null && !deriveMap.isEmpty())
		{
			Collection parentCollection = deriveMap.values();
			Iterator parentIterator = parentCollection.iterator();
			
			while(parentIterator.hasNext())
			{
				GenericSpecimen derivedSpecimen = (GenericSpecimen) parentIterator.next();
				GenericSpecimen aliquotSessionObject = getAliquotSessionObject(derivedSpecimen, aliquotKey);
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
		
		if (eventId == null)
		{
			eventId = "dummy";
		}

		if (eventId != null ) 
		{
			if(summaryForm.getRequestType() == null)
			{
				summaryForm.setRequestType(ViewSpecimenSummaryForm.REQUEST_TYPE_COLLECTION_PROTOCOL);
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
	public void populateSpecimenSummaryForm(
			ViewSpecimenSummaryForm summaryForm,
			LinkedHashMap<String, GenericSpecimen> specimenMap) {
				
		LinkedList<GenericSpecimen> specimenList = getSpecimenList(specimenMap.values());
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
		
		LinkedHashMap<String, GenericSpecimen> aliquotsList = selectedSpecimen
				.getAliquotSpecimenCollection();
		LinkedHashMap<String, GenericSpecimen> derivedList = selectedSpecimen
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
		AppUtility.setDefaultPrinterTypeLocation(summaryForm);
		
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
	private LinkedList<GenericSpecimen> getSpecimenList(
			Collection<GenericSpecimen> specimenColl) {
		LinkedList<GenericSpecimen> specimenList = new LinkedList<GenericSpecimen>();
		if (!specimenColl.isEmpty())
		{
			specimenList.addAll(specimenColl);
			
			IdComparator speciemnIdComp = new IdComparator();
			Collections.sort(specimenList,speciemnIdComp);

		}
		return specimenList;
	}	
	
	/**
	 * To check the associated specimens to the Collection protocol
	 * @param cpId
	 * @return
	 * @throws ClassNotFoundException
	 * @throws ApplicationException 
	 */
	protected boolean isSpecimenExists(Long cpId) throws ApplicationException
	{
		
		String hql = " select" +
        " elements(scg.specimenCollection) " +
        "from " +
        " edu.wustl.catissuecore.domain.CollectionProtocol as cp" +
        ", edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr" +
        ", edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg" +
        ", edu.wustl.catissuecore.domain.Specimen as s" +
        " where cp.id = "+cpId+"  and"+
        " cp.id = cpr.collectionProtocol.id and" +
        " cpr.id = scg.collectionProtocolRegistration.id and" +
        " scg.id = s.specimenCollectionGroup.id and " +
        " s.activityStatus = '"+Constants.ACTIVITY_STATUS_ACTIVE+"'";
		
		List specimenList=(List)AppUtility.executeQuery(hql);
		if((specimenList!=null) && (specimenList).size()>0)
		{
			return true;
		}	
		else
		{
			return false;
		}
		
	}
	
	private void verifyCollectedStatus(ViewSpecimenSummaryForm summaryForm, HttpSession session)
	{
		String eventId = summaryForm.getEventId();
		if (eventId == null || session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP) == null)
		{
			return;
		}
		
		Map collectionProtocolEventMap = (Map) session
		.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);		
		CollectionProtocolEventBean eventBean =(CollectionProtocolEventBean)
							collectionProtocolEventMap.get(eventId);	// get nullpointer sometimes
		LinkedHashMap specimenMap = (LinkedHashMap)eventBean.getSpecimenRequirementbeanMap();
		
		Iterator specItr = specimenMap.values().iterator();
		while(specItr.hasNext())
		{
			GenericSpecimen pSpecimen=(GenericSpecimen)specItr.next();
			if(pSpecimen.getCheckedSpecimen()== false)
			{
				unCheckChildSpecimens(pSpecimen);
			}
		}
	}
	//bug 11169 start
	private void verifyPrintStatus(ViewSpecimenSummaryForm summaryForm, HttpSession session)
	{
		Set printSpecimenSet = new HashSet();
		List<GenericSpecimen> specimenList = new ArrayList<GenericSpecimen>();
		specimenList.addAll(summaryForm.getAliquotList());
		specimenList.addAll(summaryForm.getDerivedList());
		specimenList.addAll(summaryForm.getSpecimenList());		
		for(GenericSpecimen pSpecimen : specimenList)
		{
			if(pSpecimen.getPrintSpecimen()== true)
			{
				printSpecimenSet.add(pSpecimen);
			}			
		}
		if(!printSpecimenSet.isEmpty())
		{
			summaryForm.setSpecimenPrintList(printSpecimenSet);
		}
		
	}
	//bug 11169 end
	
	private void unCheckChildSpecimens(GenericSpecimen pSpecimen)
	{
		if(pSpecimen.getAliquotSpecimenCollection() != null)
		{
			Iterator aliqItr = pSpecimen.getAliquotSpecimenCollection().values().iterator();
			while(aliqItr.hasNext())
			{
				GenericSpecimen aliqSpecimen=(GenericSpecimen)aliqItr.next();
				aliqSpecimen.setCheckedSpecimen(false);
				unCheckChildSpecimens(aliqSpecimen);
			}
		}
		if(pSpecimen.getDeriveSpecimenCollection() != null)
		{
			Iterator derItr = pSpecimen.getDeriveSpecimenCollection().values().iterator();
			while(derItr.hasNext())
			{
				GenericSpecimen derSpecimen=(GenericSpecimen)derItr.next();
				derSpecimen.setCheckedSpecimen(false);
				unCheckChildSpecimens(derSpecimen);
			}
		}
	}


	private void getAvailablePosition(HttpServletRequest request, HttpServletResponse response) throws IOException, BizLogicException
	{
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		HttpSession session = request.getSession();
		Set asignedPositonSet = (HashSet)session.getAttribute("asignedPositonSet");
		if(asignedPositonSet == null)
		{	asignedPositonSet = new HashSet();	}
		//TODO 
		//to get available position from SC for the specimen.
		String sid = (String)request.getParameter("sid");
		String className = (String)request.getParameter("cName");
		String cpid = (String)request.getParameter("cpid");
		List initialValues = null;
		TreeMap containerMap = new TreeMap();
		StorageContainerBizLogic scbizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
				Constants.STORAGE_CONTAINER_FORM_ID);
		String exceedingMaxLimit = new String();
		SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
			long cpId = 0;
			cpId = Long.parseLong(cpid);
			containerMap = scbizLogic.getAllocatedContaienrMapForSpecimen(cpId, className, 0, exceedingMaxLimit, sessionData, true);
			String containerName = ((NameValueBean)(containerMap.keySet().iterator().next())).getName();
			StringBuffer sb = new StringBuffer();
			if (containerMap.isEmpty()) 
			{
				sb.append("No Container available for the specimen");
			}
			else
			{
				//initialValues = StorageContainerUtil.checkForInitialValues(containerMap);
				sb.append(checkForFreeInitialValues(containerMap, asignedPositonSet));
				session.setAttribute("asignedPositonSet",asignedPositonSet);
			}
			String msg = sb.toString();
		response.getWriter().write(msg);
		
	}

	private String checkForFreeInitialValues(Map containerMap, Set asignedPositonSet)
	{
		System.out.println("containerMap :: "+containerMap+"\n\n");
		if (containerMap.size() > 0)
		{
			StringBuffer mainKey = null;
			Set containerMapkeySet = containerMap.keySet();
			Iterator containerMapkeySetitr = containerMapkeySet.iterator();
			while(containerMapkeySetitr.hasNext())
			{
				mainKey = new StringBuffer();
				NameValueBean containerMapkey = (NameValueBean) containerMapkeySetitr.next();
				System.out.println("\t"+containerMapkey);
				mainKey.append(containerMapkey.getName());
				mainKey.append("#");
				mainKey.append(containerMapkey.getValue());
				mainKey.append("#");
				Map maincontainerMapvaluemap1 = (Map) containerMap.get(containerMapkey);
				Set maincontainerMapvaluemap1keySet = maincontainerMapvaluemap1.keySet();
				Iterator maincontainerMapvaluemap1keySetitr = maincontainerMapvaluemap1keySet.iterator();
				
				while(maincontainerMapvaluemap1keySetitr.hasNext())
				{
					NameValueBean maincontainerMapvaluemap1key = (NameValueBean) maincontainerMapvaluemap1keySetitr.next();
					System.out.println("\t\t"+maincontainerMapvaluemap1key);
					StringBuffer pos1 = new StringBuffer();
					pos1.append(maincontainerMapvaluemap1key.getValue());
					pos1.append("#");
					List list = (List) maincontainerMapvaluemap1.get(maincontainerMapvaluemap1key);
					
					for(int i=0; i< list.size(); i++)
					{
						NameValueBean maincontainerMapvaluemap1value = (NameValueBean) list.get(i);
						System.out.println("\t\t\t\t"+maincontainerMapvaluemap1value);
						StringBuffer pos2 = new StringBuffer();
						pos2.append(maincontainerMapvaluemap1value.getValue());
						StringBuffer availablePos = new StringBuffer();
						availablePos.append(mainKey);availablePos.append(pos1);availablePos.append(pos2);
						String freePosition = availablePos.toString();
						if(!asignedPositonSet.contains(freePosition))
						{
							asignedPositonSet.add(freePosition);
							return freePosition;
						}
					}
				}
			}
		}
		return "";
	}

}
