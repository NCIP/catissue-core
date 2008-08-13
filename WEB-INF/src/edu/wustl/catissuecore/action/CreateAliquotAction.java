package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author virender_mehta
 *
 */
public class CreateAliquotAction extends BaseAction
{
	/**
     * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 * @return mapping.findforword
     * */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AliquotForm aliquotForm = (AliquotForm)form;
		boolean insertAliquotSpecimen= true;
		Collection<AbstractDomainObject> specimenCollection =null;
		List<AbstractDomainObject> specimenList = null;
		String fromPrintAction = request.getParameter(Constants.FROM_PRINT_ACTION);
		SessionDataBean sessionDataBean = getSessionData(request);
		//Create SpecimenCollectionGroup Object
		SpecimenCollectionGroup scg = createSCG(aliquotForm);
		//Create ParentSpecimen Object
		Specimen parentSpecimen = createParentSpecimen(aliquotForm);
		//Create Specimen Map
		specimenCollection = createAliquotDomainObject(aliquotForm, scg, parentSpecimen);
		//Insert Specimen Map
		insertAliquotSpecimen = insertAliquotSpecimen(request, sessionDataBean, specimenCollection);
		//Convert Specimen HashSet to List
		specimenList= new LinkedList<AbstractDomainObject>();
		specimenList.addAll(specimenCollection);
		
		Specimen specimen = (Specimen)specimenList.get(0);
		
		request.setAttribute(Constants.PARENT_SPECIMEN_ID, parentSpecimen.getId().toString());
		
		if(specimen!=null)
		{
			aliquotForm.setSpCollectionGroupId(specimen.getSpecimenCollectionGroup().getId());
			aliquotForm.setScgName(specimen.getSpecimenCollectionGroup().getGroupName());
		}
		calculateAvailableQuantityForParent(specimenList,aliquotForm);
		aliquotForm.setSpecimenList(specimenList);
		//mapping.findforward
		return getFindForward(mapping, request, aliquotForm, fromPrintAction,
				insertAliquotSpecimen, specimenList);
	}

	/**
	 * @param mapping
	 * @param request
	 * @param aliquotForm
	 * @param fromPrintAction
	 * @param insertAliquotSpecimen
	 * @param specimenList
	 * @return
	 * @throws Exception 
	 */
	private ActionForward getFindForward(ActionMapping mapping, HttpServletRequest request,
			AliquotForm aliquotForm, String fromPrintAction, boolean insertAliquotSpecimen,
			List<AbstractDomainObject> specimenList) throws Exception
	{
		if(aliquotForm.getPrintCheckbox()!=null)
		{
			request.setAttribute(Constants.LIST_SPECIMEN, specimenList);
			PrintAction printActionObj = new PrintAction();
			SessionDataBean objBean = (SessionDataBean) request.getSession().getAttribute("sessionData");
			printActionObj.printAliquotLabel(aliquotForm, request,null,objBean);
		}
		if(Constants.ADD_SPECIMEN_TO_CART .equals(aliquotForm.getForwardTo())&&insertAliquotSpecimen)
		{
			return mapping.findForward(Constants.ADD_SPECIMEN_TO_CART);
		}
		 else if(insertAliquotSpecimen)
		{
			 if(aliquotForm.getForwardTo().equals(Constants.ORDER_DETAILS))
			 {
				 Specimen specimen = (Specimen)specimenList.get(0);
				 String parentSpecimenLable= specimen.getParentSpecimen().getLabel();
				 ActionMessages actionMessages = new ActionMessages();
				 actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.distribution.aliquots.created",parentSpecimenLable));
				 saveMessages(request, actionMessages);
				 return mapping.findForward(aliquotForm.getForwardTo());
			 }	 
			 else
				 return mapping.findForward(Constants.SUCCESS);
		}
		else
		{
			return mapping.findForward(Constants.FAILURE);
		}
	}
	/**
	 * @param request
	 * @param sessionDataBean
	 * @param specimenMap
	 * @return
	 * @throws UserNotAuthorizedException
	 * @throws DAOException 
	 * @throws  
	 * @throws DAOException 
	 * @throws DAOException 
	 */
	private boolean insertAliquotSpecimen(HttpServletRequest request, SessionDataBean sessionDataBean,
			Collection<AbstractDomainObject> specimenCollection) throws UserNotAuthorizedException
	{
		try
		{
			new NewSpecimenBizLogic().insert(specimenCollection, sessionDataBean,Constants.HIBERNATE_DAO , false);
			disposeParentSpecimen(sessionDataBean, specimenCollection);
		}
		catch (BizLogicException e)
		{
		    ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(actionErrors.GLOBAL_MESSAGE, new ActionError("errors.item",e.getMessage()));
			saveErrors(request, actionErrors);
			saveToken(request);
			return false;
		}
		catch(DAOException e)
		{
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(actionErrors.GLOBAL_MESSAGE, new ActionError("errors.item",e.getMessage()));
			saveErrors(request, actionErrors);
			saveToken(request);
			return false;	
		}
		catch(UserNotAuthorizedException e)
		{
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = new ActionError("access.addedit.object.denied", sessionDataBean.getUserName(), Specimen.class.getName());
        	actionErrors.add(ActionErrors.GLOBAL_ERROR, error);
        	saveErrors(request, actionErrors);
        	return false;
		}
		return true;
	}
	
	/**
	 * @param sessionDataBean
	 * @param specimenCollection
	 * @param dao
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 * @throws BizLogicException 
	 */
	private void disposeParentSpecimen(SessionDataBean sessionDataBean,
			Collection<AbstractDomainObject> specimenCollection)
			throws DAOException, UserNotAuthorizedException, BizLogicException
	{
		Iterator<AbstractDomainObject> spItr = specimenCollection.iterator();
		Specimen specimen =(Specimen)spItr.next();
		if(specimen!=null && specimen.getDisposeParentSpecimen())
		{
			new NewSpecimenBizLogic().disposeSpecimen(sessionDataBean, specimen.getParentSpecimen());
		}
	}

	/**
	 * @param aliquotForm
	 * @return
	 * @throws AssignDataException
	 */
	private Specimen createParentSpecimen(AliquotForm aliquotForm) throws AssignDataException
	{
		Specimen parentSpecimen = (Specimen) new SpecimenObjectFactory().getDomainObject(aliquotForm.getClassName());
		parentSpecimen.setId(new Long(aliquotForm.getSpecimenID()));
		parentSpecimen.setLabel(aliquotForm.getSpecimenLabel());
		return parentSpecimen;
	}

	/**
	 * @param aliquotForm
	 * @return
	 */
	private SpecimenCollectionGroup createSCG(AliquotForm aliquotForm)
	{
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setId(new Long(aliquotForm.getSpCollectionGroupId()));
		return scg;
	}

	/**
	 * @param aliquotForm
	 * @param scg
	 * @param parentSpecimen
	 * @return
	 */
	private Collection<AbstractDomainObject> createAliquotDomainObject(AliquotForm aliquotForm, SpecimenCollectionGroup scg,
			Specimen parentSpecimen)
	{
		Collection<AbstractDomainObject> specimenCollection = new LinkedHashSet<AbstractDomainObject>();
		boolean disposeParentSpecimen = aliquotForm.getDisposeParentSpecimen();
		Map aliquotMap = aliquotForm.getAliquotMap();
		String specimenKey = "Specimen:";
		Integer noOfAliquots = new Integer(aliquotForm.getNoOfAliquots());
		for (int i = 1; i <= noOfAliquots.intValue(); i++)
		{
			Specimen aliquotSpecimen = Utility.getSpecimen(parentSpecimen);
			StorageContainer sc = new StorageContainer();
			String fromMapsuffixKey = "_fromMap";
			boolean booleanfromMap = false;
			
			String quantityKey = null;
			String containerIdKey = null;
			String posDim1Key = null;
			String posDim2Key = null;
			String radioButton = (String)aliquotMap.get("radio_"+i);
			// if radio button =2 else conatiner selected from Combo box
			quantityKey = specimenKey + i + "_quantity";
			if(radioButton!=null&&radioButton.equals("2"))
			{
				containerIdKey = specimenKey + i + "_StorageContainer_id";
				posDim1Key = specimenKey + i + "_positionDimensionOne";
				posDim2Key = specimenKey + i + "_positionDimensionTwo";
			}
			else if(radioButton!=null&&radioButton.equals("3"))
			{
				// Container selected from Map button
				containerIdKey = specimenKey + i + "_StorageContainer_id"+fromMapsuffixKey;
				posDim1Key = specimenKey + i + "_positionDimensionOne"+fromMapsuffixKey;
				posDim2Key = specimenKey + i + "_positionDimensionTwo"+fromMapsuffixKey;
			}
			
			String quantity = (String) aliquotMap.get(quantityKey);
			String containerId  = (String) aliquotMap.get(containerIdKey);
			String posDim1  = (String) aliquotMap.get(posDim1Key);
			String posDim2  = (String) aliquotMap.get(posDim2Key);

			aliquotSpecimen.setSpecimenClass(aliquotForm.getClassName());
			aliquotSpecimen.setSpecimenType(aliquotForm.getType());
			aliquotSpecimen.setPathologicalStatus(aliquotForm.getPathologicalStatus());
			aliquotSpecimen.setInitialQuantity(new Double(quantity));
			aliquotSpecimen.setAvailableQuantity(new Double(quantity));
			
			
			
			
			if (containerId != null && posDim1 != null && posDim2 != null)
			{
				SpecimenPosition specPos = new SpecimenPosition();
				specPos.setPositionDimensionOne(new Integer(posDim1));
				specPos.setPositionDimensionTwo(new Integer(posDim2));				
				sc.setId(new Long(containerId));
				specPos.setStorageContainer(sc);
				specPos.setSpecimen(aliquotSpecimen);
				aliquotSpecimen.setSpecimenPosition(specPos);
			}
			
			if (aliquotSpecimen instanceof MolecularSpecimen)
			{
				if(aliquotForm.getConcentration().equals(""))
				{
					((MolecularSpecimen) aliquotSpecimen).setConcentrationInMicrogramPerMicroliter(0.0);
				}
				else
				{
					Double concentration = new Double(aliquotForm.getConcentration());
					if (concentration != null)
					{
						((MolecularSpecimen) aliquotSpecimen).setConcentrationInMicrogramPerMicroliter(concentration);
					}
				}
			}
			Date currentDate = new Date();
			aliquotSpecimen.setCreatedOn(currentDate);
			aliquotSpecimen.setParentSpecimen(parentSpecimen);
			aliquotSpecimen.setSpecimenCollectionGroup(scg);
			aliquotSpecimen.setLineage(Constants.ALIQUOT);
			aliquotSpecimen.setIsAvailable(Boolean.TRUE);
			aliquotSpecimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
			aliquotSpecimen.setCollectionStatus(Constants.COLLECTION_STATUS_COLLECTED);
			aliquotSpecimen.setDisposeParentSpecimen(disposeParentSpecimen);
			SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
			specimenCharacteristics.setTissueSide(aliquotForm.getTissueSide());
			specimenCharacteristics.setTissueSite(aliquotForm.getTissueSite());
			aliquotSpecimen.setSpecimenCharacteristics(specimenCharacteristics);
			//bug no. 8081 and 8083
			if(!edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl)
			{
			 aliquotSpecimen.setLabel((String) aliquotMap.get(specimenKey + i + "_label"));
			}
			specimenCollection.add(aliquotSpecimen);
		}

		return specimenCollection;
	}
	/**
	 * This function calculates the avialble qty of parent after creating aliquots.
	 * @param specimenList - list of aliquots
	 * @param aliquotForm
	 */
	private void calculateAvailableQuantityForParent(List specimenList,AliquotForm aliquotForm)
	{
		Double totalAliquotQty = 0.0;
		
		if(specimenList != null && specimenList.size()>0)
		{
			Iterator itr = specimenList.iterator();
			while(itr.hasNext())
			{
				Specimen specimen =  (Specimen) itr.next();
				if(specimen.getInitialQuantity() != null)
				{
					totalAliquotQty = totalAliquotQty + specimen.getInitialQuantity();
				}
				
			}
			if(aliquotForm.getInitialAvailableQuantity() != null)
			{
				Double availableQuantity = Double.parseDouble(aliquotForm.getInitialAvailableQuantity()) - totalAliquotQty;
				aliquotForm.setAvailableQuantity(availableQuantity.toString());
			}
		}
	}
}