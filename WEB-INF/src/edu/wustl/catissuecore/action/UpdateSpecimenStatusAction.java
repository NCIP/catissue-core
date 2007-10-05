package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimenVO;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.AbstractDAO;
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
			
			Map collectionProtocolEventMap = (Map) session
			.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
			
			CollectionProtocolEventBean eventBean =(CollectionProtocolEventBean)
								collectionProtocolEventMap.get(eventId);
			
			LinkedHashMap specimenMap = (LinkedHashMap)eventBean.getSpecimenRequirementbeanMap();
			
			Collection specimenCollection = specimenMap.values();
			Iterator iterator = specimenCollection.iterator();
			SessionDataBean sessionDataBean =(SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
			NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();
			HashSet specimenDomainCollection = new HashSet();
			while(iterator.hasNext())
			{
				GenericSpecimenVO specimenVO =(GenericSpecimenVO) iterator.next();
				
				Specimen specimen = createSpecimenDomainObject(specimenVO);
				specimen.setChildrenSpecimen(getChildrenSpecimens(specimenVO));
				specimenDomainCollection.add(specimen);
	
			}
			bizLogic.updateMultipleSpecimens(specimenDomainCollection, sessionDataBean);
			
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
			return mapping.findForward(Constants.SUCCESS);
		}
		
	}

	private Collection getChildrenSpecimens(GenericSpecimenVO specimenVO) throws BizLogicException
	{
		HashSet childrenSpecimens = new HashSet();
		LinkedHashMap aliquotMap = specimenVO.getAliquotSpecimenCollection();

		if(aliquotMap!= null || !aliquotMap.isEmpty() )
		{
			Collection aliquotCollection = aliquotMap.values();
			Iterator iterator = aliquotCollection.iterator();
			while(iterator.hasNext())
			{
				GenericSpecimenVO aliquotSpecimen = (GenericSpecimenVO) iterator.next();
				Specimen specimen = createSpecimenDomainObject(aliquotSpecimen);
				specimen.setChildrenSpecimen(
						getChildrenSpecimens(aliquotSpecimen));
				childrenSpecimens.add(specimen);
			}
		}

		LinkedHashMap derivedMap = specimenVO.getDeriveSpecimenCollection();

		if(derivedMap!= null || !derivedMap.isEmpty() )
		{
			Collection aliquotCollection = derivedMap.values();
			Iterator iterator = aliquotCollection.iterator();
			while(iterator.hasNext())
			{
				GenericSpecimenVO derivedSpecimen = (GenericSpecimenVO) iterator.next();
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
	private Specimen createSpecimenDomainObject(GenericSpecimenVO specimenVO) throws BizLogicException {
		Specimen specimen = new Specimen ();
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
		return specimen;
	}

	/**
	 * @param specimenVO
	 * @param specimen
	 */
	private void setStorageContainer(GenericSpecimenVO specimenVO,
			Specimen specimen)throws BizLogicException {
		String pos1 = specimenVO.getPositionDimensionOne();
		String pos2 = specimenVO.getPositionDimensionTwo();
		if (pos1!=null)
		{
			try{
				specimen.setPositionDimensionOne( Integer.parseInt(pos1) );
			}catch(NumberFormatException exception)
			{
				throw new BizLogicException("Position dimention is missing for specimen" +
						specimenVO.getDisplayName());
			}
		}
		if (pos2!=null)
		{
			try{
				specimen.setPositionDimensionTwo( Integer.parseInt(pos2) );
			}catch(NumberFormatException exception)
			{
				throw new BizLogicException("Position dimention is missing for specimen" +
						specimenVO.getDisplayName());
			}
				
		}
		StorageContainer storageContainer = new StorageContainer();
		String containerId = specimenVO.getContainerId();
		
		if(containerId !=null && containerId.trim().length()>0)
		{
			storageContainer.setId(new Long(containerId));
		}
		storageContainer.setName(specimenVO.getSelectedContainerName());
		specimen.setStorageContainer(storageContainer);
	}

	/**
	 * @param specimenVO
	 * @return
	 */
	private Long getSpecimenId(GenericSpecimenVO specimenVO) {
		long uniqueId = specimenVO.getId();
		Long id = new Long(uniqueId);
		return id;
	}

}
