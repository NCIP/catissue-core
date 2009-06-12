package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.catissuecore.util.SpecimenDetailsTagUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

public class UpdateSpecimenStatusAction extends BaseAction
{

	private transient Logger logger = Logger.getCommonLogger(UpdateSpecimenStatusAction.class);
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		ViewSpecimenSummaryForm specimenSummaryForm = (ViewSpecimenSummaryForm) form;
		try
		{
			String eventId = specimenSummaryForm.getEventId();

			HttpSession session = request.getSession();
			NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();

			LinkedHashSet specimenDomainCollection = getSpecimensToSave(eventId, session);
			
			SessionDataBean sessionDataBean = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);

			//bizLogic.updaupdateAnticipatorySpecimens(specimenDomainCollection, sessionDataBean);
			if (specimenDomainCollection != null && specimenDomainCollection.size() > 0) {
				Iterator<Specimen> spcItr = specimenDomainCollection.iterator();
				Date timeStamp = getTimeStamp(spcItr.next());
				setCreatedOnDate(specimenDomainCollection, timeStamp);
				bizLogic.update(specimenDomainCollection, specimenDomainCollection, 0, sessionDataBean);
			}
			Object obj = session.getAttribute("SCGFORM");

			//11July08 : Mandar : For GenericSpecimen
			SpecimenDetailsTagUtil.setAnticipatorySpecimenDetails(request, specimenSummaryForm, false);
			//added this to disable collected checkboxes  
			if (request.getParameter("target") != null && request.getParameter("target").equals("viewSummary"))
			{
				ActionMessages actionMessages = new ActionMessages();
				actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("object.add.successOnly", "Specimens"));
				specimenSummaryForm.setShowbarCode(true);
				specimenSummaryForm.setShowLabel(true);
				saveMessages(request, actionMessages);
				specimenSummaryForm.setReadOnly(true);

			}
			if (specimenSummaryForm.getPrintCheckbox() != null && specimenSummaryForm.getPrintCheckbox().equals("true"))
			{
				//By Falguni Sachde
				//Code Reviewer:Abhijit Naik
				//Bug :6569 : In case of collected SCG ,the specimenDomainCollection not contains all specimen.
				//To get all specimen related with give SCG ,query with SCG id and get SpecimenCollection 
				if (obj == null)
				{
					logger.fatal("SCG id is null failed to execute print of scg -UpdateSpecimenStatusAction");
				}
				else
				{
					//HashSet specimenprintCollection = getSpecimensToPrint((Long) obj, sessionDataBean);
					//bug 11169
				    Set specimenprintCollection = specimenSummaryForm.getSpecimenPrintList();
					Set domainObjSet = this.getSpecimensFromGenericSpecimens(specimenprintCollection);					
					
					HashMap forwardToPrintMap = new HashMap();					
					forwardToPrintMap.put("printAntiSpecimen", domainObjSet);
					request.setAttribute("forwardToPrintMap", forwardToPrintMap);
					request.setAttribute("AntiSpecimen", "1");
					//bug 12141 start
					if(specimenSummaryForm.getForwardTo() != null && !specimenSummaryForm.getForwardTo().equals(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART))
					{
						return mapping.findForward(Constants.PRINT_ANTICIPATORY_SPECIMENS);
					}
					//bug 12141 end
				}
			}

			if (specimenSummaryForm.getForwardTo() != null && specimenSummaryForm.getForwardTo().equals(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART))
			{
				HashSet specimenprintCollection = getSpecimensToPrint((Long) obj, sessionDataBean);

				Iterator iter = specimenprintCollection.iterator();
				List specimenIdList = new ArrayList();
				while (iter.hasNext())
				{
					specimenIdList.add(((Specimen) iter.next()).getId());
				}
				request.setAttribute("specimenIdList", specimenIdList);				
				saveToken(request);
				//bug 12141 start
				if (specimenSummaryForm.getPrintCheckbox() != null && specimenSummaryForm.getPrintCheckbox().equals("true"))
				{
				   return mapping.findForward(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART_AND_PRINT);//"printAnticipatorySpecimens";
				}
				else
				{
					return mapping.findForward(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART);
				}
				//bug 12141 end
			}
			
			return mapping.findForward(Constants.SUCCESS);
		}
		catch (Exception exception)
		{
			logger.debug(exception.getMessage(), exception);
			//11July08 : Mandar : For GenericSpecimen
			SpecimenDetailsTagUtil.setAnticipatorySpecimenDetails(request, specimenSummaryForm, false);
			// Suman-For bug #8228
			String s = "";
			if (exception.getMessage().equals("Failed to update multiple specimen Stroage location already in use")
					|| exception.getMessage().equals("Failed to update multiple specimen Either Storagecontainer is full! or it cannot accomodate all the specimens.")
					|| exception.getMessage().equals("Failed to update multiple specimen Storagecontainer information not found!"))
			{
				clearSCLocation(specimenSummaryForm);
				s = "Please allocate a different container to the specimens shown below with empty container names as the container you specified has insufficient space";
			} else {
				s = exception.getMessage();
			}
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(actionErrors.GLOBAL_MESSAGE, new ActionError("errors.item", s));
			saveErrors(request, actionErrors);
			saveToken(request);
			String pageOf = request.getParameter(Constants.PAGE_OF);
			if(pageOf != null)
				request.setAttribute(Constants.PAGE_OF,pageOf);
			return mapping.findForward(Constants.FAILURE);
		}

	}
	
	private Set getSpecimensFromGenericSpecimens(Set specimenprintCollection) throws ApplicationException
	{
		Set<Specimen> specimens = new LinkedHashSet<Specimen>();
		Iterator<GenericSpecimen> it = specimenprintCollection.iterator();
		while(it.hasNext())
		{
			Specimen specimen = this.createSpecimenDomainObject(it.next());
			specimens.add(specimen);
		}
		//Bug 11509 start
		List<Specimen> listToSort = new ArrayList<Specimen>();
		listToSort.addAll(specimens);
		Collections.sort(listToSort,new IdComparator());
		//Bug 11509 end
		specimens.removeAll(specimens);
		specimens.addAll(listToSort);
		return specimens;
	}
	// bug 8228 -suman
	// this method checks for free locations of a container and clears the ones
	// which are not available
	private void clearSCLocation(ViewSpecimenSummaryForm specimenSummaryForm)
			throws Exception {
		List<GenericSpecimen> specimenList = specimenSummaryForm.getSpecimenList();
		List<GenericSpecimen> aliquotList = specimenSummaryForm.getAliquotList();
		List<GenericSpecimen> derivedList = specimenSummaryForm.getDerivedList();
		List<String> allocatedPositions = new ArrayList<String>();
		int freeSizeofContainer = 0;
		for (GenericSpecimen spec : specimenList) 
		{
			String conName = spec.getSelectedContainerName();
			String conId = spec.getContainerId();
			try {
				freeSizeofContainer = StorageContainerUtil.getCountofFreeLocationOfContainer(conId, conName);
			}
			catch (Exception e) 
			{
				logger.debug(e.getMessage(), e);
				e.printStackTrace();
		    }
		}
		int tempContainerSize = checkList(specimenList, allocatedPositions,freeSizeofContainer);
		tempContainerSize = checkList(aliquotList, allocatedPositions,tempContainerSize);
		checkList(derivedList, allocatedPositions, tempContainerSize);
	}

	// this method check if the x and y positions are given and clears them if
	// not allocated.
	public int checkList(List<GenericSpecimen> gs,
			List<String> allocatedPositions, int containerSize) {
		for (GenericSpecimen spec : gs) {
			String positionOne = spec.getPositionDimensionOne();
			String positionTwo = spec.getPositionDimensionTwo();
			String containerName = spec.getStorageContainerForSpecimen();
			String key = containerName + ":" + positionOne + "," + positionTwo;
			if (positionOne != "" && positionTwo != "") 
			{
				if (!(StorageContainerUtil.isPostionAvaialble(spec.getContainerId(), spec.getSelectedContainerName(),positionOne, positionTwo))
						|| allocatedPositions.contains(key)) {
					spec.setPositionDimensionOne("");
					spec.setPositionDimensionTwo("");
					spec.setStorageContainerForSpecimen("");
					spec.setSelectedContainerName("");
				} else {
					allocatedPositions.add(key);
				}
			} else {
				if (containerSize >= 1) {
					allocatedPositions.add(key);
				} else {
					spec.setPositionDimensionOne("");
					spec.setPositionDimensionTwo("");
					spec.setStorageContainerForSpecimen("");
					spec.setSelectedContainerName("");
				}
			}
			containerSize = containerSize - 1;
		}
		return containerSize;
	}

	// end bug 8228 - Suman
	

	/**
	 * @param eventId
	 * @param session
	 * @return
	 * @throws ApplicationException 
	 */
	protected LinkedHashSet getSpecimensToSave(String eventId, HttpSession session) throws ApplicationException
	{
		Map collectionProtocolEventMap = (Map) session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);

		CollectionProtocolEventBean eventBean = (CollectionProtocolEventBean) collectionProtocolEventMap.get(eventId);

		LinkedHashMap specimenMap = (LinkedHashMap) eventBean.getSpecimenRequirementbeanMap();

		Collection specimenCollection = specimenMap.values();
		Iterator iterator = specimenCollection.iterator();

		LinkedHashSet specimenDomainCollection = new LinkedHashSet();
		while (iterator.hasNext())
		{
			GenericSpecimen specimenVO = (GenericSpecimen) iterator.next();
			Specimen specimen = null;
			if (!specimenVO.getReadOnly())
			{
				specimen = createSpecimenDomainObject(specimenVO);
				specimen.setChildSpecimenCollection(getChildrenSpecimens(specimenVO, specimen));
				specimenDomainCollection.add(specimen);
			}
			else
			{
				specimenDomainCollection.addAll(getChildrenSpecimens(specimenVO, specimen));
			}
		}
		return specimenDomainCollection;
	}

	//Abhishek Mehta : Performance related Changes
	private Collection getChildrenSpecimens(GenericSpecimen specimenVO, Specimen parentSpecimen) throws ApplicationException
	{
		LinkedHashSet childrenSpecimens = new LinkedHashSet();
		LinkedHashMap aliquotMap = specimenVO.getAliquotSpecimenCollection();

		if (aliquotMap != null && !aliquotMap.isEmpty())
		{
			Collection aliquotCollection = aliquotMap.values();
			Iterator iterator = aliquotCollection.iterator();
			while (iterator.hasNext())
			{
				GenericSpecimen aliquotSpecimen = (GenericSpecimen) iterator.next();
				Specimen specimen = null;
				if (!aliquotSpecimen.getReadOnly())
				{
					specimen = createSpecimenDomainObject(aliquotSpecimen);
					specimen.setParentSpecimen(parentSpecimen);
					specimen.setChildSpecimenCollection(getChildrenSpecimens(aliquotSpecimen, specimen));
					childrenSpecimens.add(specimen);
				}
				else
				{
					childrenSpecimens.addAll(getChildrenSpecimens(aliquotSpecimen, specimen));
				}
			}
		}

		LinkedHashMap derivedMap = specimenVO.getDeriveSpecimenCollection();

		if (derivedMap != null && !derivedMap.isEmpty())
		{
			Collection aliquotCollection = derivedMap.values();
			Iterator iterator = aliquotCollection.iterator();
			while (iterator.hasNext())
			{
				GenericSpecimen derivedSpecimen = (GenericSpecimen) iterator.next();
				Specimen specimen = null;
				if (!derivedSpecimen.getReadOnly())
				{
					specimen = createSpecimenDomainObject(derivedSpecimen);
					specimen.setParentSpecimen(parentSpecimen);
					specimen.setChildSpecimenCollection(getChildrenSpecimens(derivedSpecimen, specimen));
					childrenSpecimens.add(specimen);
				}
				else
				{
					childrenSpecimens.addAll(getChildrenSpecimens(derivedSpecimen, specimen));
				}
			}
		}
		return childrenSpecimens;
	}

	/**
	 * @param specimenVO
	 * @return
	 * @throws ApplicationException Application Exception
	 */
	protected Specimen createSpecimenDomainObject(GenericSpecimen specimenVO) throws ApplicationException
	{

		Specimen specimen;
		try
		{
			specimen = (Specimen) new SpecimenObjectFactory().getDomainObject(specimenVO.getClassName());
		}
		catch (AssignDataException e1)
		{
			logger.debug(e1.getMessage(), e1);
			e1.printStackTrace();
			return null;
		}

		if (Constants.MOLECULAR.equals(specimenVO.getClassName()))
		{
			Double concentration = null;
			try
			{
				concentration = new Double(specimenVO.getConcentration());
			}
			catch (Exception exception)
			{
				logger.debug(exception.getMessage(), exception);
				concentration = new Double(0);
			}
			((MolecularSpecimen) specimen).setConcentrationInMicrogramPerMicroliter(concentration);
		}
		Long id = getSpecimenId(specimenVO);
		specimen.setId(id);
		specimen.setSpecimenClass(specimenVO.getClassName());
		specimen.setSpecimenType(specimenVO.getType());
		if ("".equals(specimenVO.getDisplayName()))
			specimen.setLabel(null);
		else
			specimen.setLabel(specimenVO.getDisplayName());

		specimen.setBarcode(specimenVO.getBarCode());

		/* bug 6015  vaishali khandelwal*/

		/* end bug 6015 */

		String initialQuantity = specimenVO.getQuantity();
		if (initialQuantity != null)
		{
			if (!initialQuantity.equals(""))
			{
				specimen.setInitialQuantity(new Double(initialQuantity));
			}
		}
		if (specimenVO.getCheckedSpecimen())
		{
			specimen.setCollectionStatus(Constants.SPECIMEN_COLLECTED);
			specimen.setAvailableQuantity(new Double(initialQuantity));
			if ((specimen.getAvailableQuantity() != null && specimen.getAvailableQuantity().doubleValue() > 0))
			{
				specimen.setIsAvailable(Boolean.TRUE);
			}
		}
		else
		{
			specimen.setCollectionStatus("Pending");
			//Mandar : 25July08: ------- start ------------
			specimen.setAvailableQuantity(new Double(0));
			//Mandar : 25July08: ------- end ------------
		}

		if ("Virtual".equals(specimenVO.getStorageContainerForSpecimen()))
		{
			//	specimen.setStorageContainer(null);
			specimen.setSpecimenPosition(null);
		}
		else
		{
			setStorageContainer(specimenVO, specimen);
		}
		
		return specimen;
	}

	/**
	 * @param specimenVO
	 * @param specimen
	 * @throws ApplicationException 
	 */
	private void setStorageContainer(GenericSpecimen specimenVO, Specimen specimen) throws ApplicationException
	{

		String pos1 = specimenVO.getPositionDimensionOne();
		String pos2 = specimenVO.getPositionDimensionTwo();

		if (!specimenVO.getCheckedSpecimen())
		{
			specimenVO.setPositionDimensionOne(String
					.valueOf(CollectionProtocolUtil.getStorageTypeValue(specimenVO.getStorageContainerForSpecimen())));

			return;
		}
		SpecimenPosition specPos = specimen.getSpecimenPosition();

		if (specPos == null)
		{
			specPos = new SpecimenPosition();
		}
		if (pos1 != null)
		{
			try
			{
				specPos.setPositionDimensionOne(Integer.parseInt(pos1));
			}
			catch (NumberFormatException exception)
			{
				logger.debug(exception.getMessage(), exception);
				specPos.setPositionDimensionOne(null);
			}
		}
		if (pos2 != null)
		{
			try
			{
				specPos.setPositionDimensionTwo(Integer.parseInt(pos2));
			}
			catch (NumberFormatException exception)
			{
				logger.debug(exception.getMessage(), exception);
				specPos.setPositionDimensionTwo(null);
			}
		}
		StorageContainer storageContainer = new StorageContainer();
		specPos.setSpecimen(specimen);
		specPos.setStorageContainer(storageContainer);
		specimen.setSpecimenPosition(specPos);

		String containerId = specimenVO.getContainerId();

		if (containerId != null && containerId.trim().length() > 0)
		{
			storageContainer.setId(new Long(containerId));
		}
		if (specimenVO.getSelectedContainerName() == null || specimenVO.getSelectedContainerName().trim().length() == 0)
		{
			ErrorKey errorKey = ErrorKey.getErrorKey("action.error");
			
			throw AppUtility.getApplicationException(null, "spec.storage.missing", specimenVO.getDisplayName());
		}
		storageContainer.setName(specimenVO.getSelectedContainerName());
		//	specimen.setStorageContainer(storageContainer);
	}

	/**
	 * @param specimenVO
	 * @return
	 */
	private Long getSpecimenId(GenericSpecimen specimenVO)
	{
		long uniqueId = specimenVO.getId();
		if (uniqueId <= 0)
		{
			return null;
		}
		Long id = new Long(uniqueId);
		return id;
	}

	/**
	 * @param scgId
	 * @param sessionDataBean
	 * @return
	 * @throws BizLogicException
	 */
	protected HashSet getSpecimensToPrint(Long scgId, SessionDataBean sessionDataBean) throws BizLogicException
	{

		SpecimenCollectionGroupBizLogic bizLogic = new SpecimenCollectionGroupBizLogic();
		SpecimenCollectionGroup objSCG = bizLogic.getSCGFromId(scgId, sessionDataBean, true);
		HashSet specimenCollection = new HashSet(objSCG.getSpecimenCollection());

		return specimenCollection;

	}
	
	private void setCreatedOnDate(Collection<AbstractSpecimen> specimenColl, Date timeStamp) 
	{
		try{
			Iterator<AbstractSpecimen> spcItr = specimenColl.iterator();
			while(spcItr.hasNext()){
				Specimen specimen = (Specimen)spcItr.next();
				if((specimen != null) && (Constants.COLLECTION_STATUS_COLLECTED.equals(specimen.getCollectionStatus()))) {
					specimen.setCreatedOn(timeStamp);
					Collection<AbstractSpecimen> childSpecColl =  specimen.getChildSpecimenCollection();
					if(childSpecColl!= null && childSpecColl.size() > 0) {
						setCreatedOnDate(childSpecColl, timeStamp);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Date getTimeStamp (Specimen specimen) 
	{
		Date timeStamp = null;
		try {
			String query ="select collectionEventParameters.timestamp from edu.wustl.catissuecore.domain.CollectionEventParameters as collectionEventParameters where "+
	   		" collectionEventParameters.specimenCollectionGroup.id = (select specimen.specimenCollectionGroup.id from edu.wustl.catissuecore.domain.Specimen as specimen where "+
		   		"specimen.id = "+specimen.getId()+")";
				
			List<Date> list = new DefaultBizLogic().executeQuery(query);
			Iterator<Date> itr = list.iterator();
			while(itr.hasNext()){
			  timeStamp =  (Date)itr.next();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return timeStamp;	
	}
	
	
	

}