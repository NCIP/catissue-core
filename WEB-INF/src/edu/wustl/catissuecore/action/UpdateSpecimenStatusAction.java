package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenDataBean;

import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.DomainObjectFactory;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;

public class UpdateSpecimenStatusAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try{
			ViewSpecimenSummaryForm specimenSummaryForm =
			(ViewSpecimenSummaryForm)form;
			String eventId = specimenSummaryForm.getEventId();
			
			HttpSession session = request.getSession();
			NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();

			LinkedHashSet specimenDomainCollection = getSpecimensToSave(
					eventId, session);
			
			SessionDataBean sessionDataBean =(SessionDataBean)
			session.getAttribute(Constants.SESSION_DATA);
			bizLogic.updateAnticipatorySpecimens
			(specimenDomainCollection, sessionDataBean);
			
			Object obj = request.getAttribute("SCGFORM");
			request.setAttribute("SCGFORM", obj);		
			return mapping.findForward(Constants.SUCCESS);
		}
		catch(Exception exception)
		{
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(actionErrors.GLOBAL_MESSAGE, new ActionError(
					"errors.item",exception.getMessage()));
			saveErrors(request, actionErrors);		
			saveToken(request);
			return mapping.findForward(Constants.FAILURE);
		}
		
	}

	/**
	 * @param eventId
	 * @param session
	 * @return
	 * @throws BizLogicException
	 */
	protected LinkedHashSet getSpecimensToSave(String eventId, HttpSession session)
			throws BizLogicException {
		Map collectionProtocolEventMap = (Map) session
		.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		
		CollectionProtocolEventBean eventBean =(CollectionProtocolEventBean)
							collectionProtocolEventMap.get(eventId);
		
		LinkedHashMap specimenMap = (LinkedHashMap)eventBean.getSpecimenRequirementbeanMap();
		
		Collection specimenCollection = specimenMap.values();
		Iterator iterator = specimenCollection.iterator();
		
		LinkedHashSet specimenDomainCollection = new LinkedHashSet();
		while(iterator.hasNext())
		{
			GenericSpecimen specimenVO =(GenericSpecimen) iterator.next();
			
			Specimen specimen = createSpecimenDomainObject(specimenVO);
			specimen.setChildrenSpecimen(getChildrenSpecimens(specimenVO));
			specimenDomainCollection.add(specimen);

		}
		return specimenDomainCollection;
	}

	private Collection getChildrenSpecimens(GenericSpecimen specimenVO) throws BizLogicException
	{
		HashSet childrenSpecimens = new HashSet();
		LinkedHashMap aliquotMap = specimenVO.getAliquotSpecimenCollection();

		if(aliquotMap!= null && !aliquotMap.isEmpty() )
		{
			Collection aliquotCollection = aliquotMap.values();
			Iterator iterator = aliquotCollection.iterator();
			while(iterator.hasNext())
			{
				GenericSpecimen aliquotSpecimen = (GenericSpecimen) iterator.next();
				Specimen specimen = createSpecimenDomainObject(aliquotSpecimen);
				specimen.setChildrenSpecimen(
						getChildrenSpecimens(aliquotSpecimen));
				childrenSpecimens.add(specimen);
			}
		}

		LinkedHashMap derivedMap = specimenVO.getDeriveSpecimenCollection();

		if(derivedMap!= null && !derivedMap.isEmpty() )
		{
			Collection aliquotCollection = derivedMap.values();
			Iterator iterator = aliquotCollection.iterator();
			while(iterator.hasNext())
			{
				GenericSpecimen derivedSpecimen = (GenericSpecimen) iterator.next();
				Specimen specimen = createSpecimenDomainObject(derivedSpecimen);
				specimen.setChildrenSpecimen(
						getChildrenSpecimens(derivedSpecimen));
				childrenSpecimens.add(specimen);
			}
		}
		return childrenSpecimens;
	}
	/**
	 * @param specimenVO
	 * @return
	 */
	private Specimen createSpecimenDomainObject(GenericSpecimen specimenVO) throws BizLogicException {

		NewSpecimenForm form = new NewSpecimenForm();
		form.setClassName(specimenVO.getClassName());
		
		
		Specimen specimen;
		try {
			specimen = (Specimen) new DomainObjectFactory()
				.getDomainObject(Constants.NEW_SPECIMEN_FORM_ID, form);
		} catch (AssignDataException e1)
		{
			e1.printStackTrace();
			return null;
		}
		
		if (Constants.MOLECULAR.equals(specimenVO.getClassName()))
		{
			Double concentration= null;
			try
			{
				concentration = new Double(specimenVO.getConcentration());
			}
			catch (Exception exception)
			{
				concentration =  new Double(0);
			}
			((MolecularSpecimen)specimen)
				.setConcentrationInMicrogramPerMicroliter(concentration);
		}
		
		Long id = getSpecimenId(specimenVO);
		specimen.setId(id);
		specimen.setLabel(specimenVO.getDisplayName() );
		specimen.setBarcode(specimenVO.getBarCode());
		
		if ("Virtual".equals(
				specimenVO.getStorageContainerForSpecimen()))
		{
			specimen.setStorageContainer(null);
		}
		else
		{
			setStorageContainer(specimenVO, specimen);
		}
		
		String initialQuantity = specimenVO.getQuantity();
		
		if(initialQuantity != null)
		{
			Quantity quantity = new Quantity();
			quantity.setValue(new Double(initialQuantity));
			specimen.setInitialQuantity(quantity);
		}
		if(specimenVO.getCheckedSpecimen())
		{
			specimen.setCollectionStatus(Constants.SPECIMEN_COLLECTED);
		}
		else
		{
			specimen.setCollectionStatus("Pending");
		}
		
		if(specimen.getId() == null)
		{
			setValuesForNewSpecimen(specimen,specimenVO);
		}
		
		return specimen;
	}

	private void setValuesForNewSpecimen(Specimen specimen, GenericSpecimen genericSpecimen)
	{
		SpecimenDataBean specimenDataBean = (SpecimenDataBean) genericSpecimen;
		specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		specimen.setComment(specimenDataBean.getComment());
		specimen.setCreatedOn(new Date());
		specimen.setCollectionStatus(Constants.SPECIMEN_COLLECTED);
		specimen.setPathologicalStatus(specimenDataBean.getPathologicalStatus());
		specimen.setLineage(specimenDataBean.getLineage());
		SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
		specimenCharacteristics.setTissueSide(specimenDataBean.getTissueSide());
		specimenCharacteristics.setTissueSite(specimenDataBean.getTissueSite());
		specimen.setSpecimenCharacteristics(specimenCharacteristics);
		specimen.setAvailableQuantity(specimen.getInitialQuantity());
		specimen.setAvailable(Boolean.TRUE);
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
	}
	
	/**
	 * @param specimenVO
	 * @param specimen
	 */
	private void setStorageContainer(GenericSpecimen specimenVO,
			Specimen specimen)throws BizLogicException {
		String pos1 = specimenVO.getPositionDimensionOne();
		String pos2 = specimenVO.getPositionDimensionTwo();
		if (pos1!=null)
		{
			try
			{
				specimen.setPositionDimensionOne( Integer.parseInt(pos1) );
			}
			catch(NumberFormatException exception)
			{
				specimen.setPositionDimensionOne(null);
			}
		}
		if (pos2!=null)
		{
			try
			{
				specimen.setPositionDimensionTwo( Integer.parseInt(pos2) );
			}
			catch(NumberFormatException exception)
			{
				specimen.setPositionDimensionOne(null);
			}
				
		}
		StorageContainer storageContainer = new StorageContainer();
		String containerId = specimenVO.getContainerId();
		
		if(containerId !=null && containerId.trim().length()>0)
		{
			storageContainer.setId(new Long(containerId));
		}
		if (specimenVO.getSelectedContainerName() == null || specimenVO.getSelectedContainerName().trim().length()==0)
		{
			throw new BizLogicException("Container name is missing for specimen :" +specimenVO.getDisplayName());
		}
		storageContainer.setName(specimenVO.getSelectedContainerName());
		specimen.setStorageContainer(storageContainer);
	}

	/**
	 * @param specimenVO
	 * @return
	 */
	private Long getSpecimenId(GenericSpecimen specimenVO) {
		long uniqueId = specimenVO.getId();
		if(uniqueId<=0)
		{
			return null;
		}
		Long id = new Long(uniqueId);
		return id;
	}

}
