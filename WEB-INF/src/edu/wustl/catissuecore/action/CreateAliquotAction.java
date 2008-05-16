package edu.wustl.catissuecore.action;

import java.util.Date;
import java.util.HashMap;
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

import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractForwardToFactory;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.AbstractForwardToProcessor;
import edu.wustl.common.util.global.ApplicationProperties;

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
		LinkedHashSet<Specimen> specimenMap =null;
		List<Specimen> specimenList = null;
		String fromPrintAction = request.getParameter(Constants.FROM_PRINT_ACTION);
		SessionDataBean sessionDataBean = getSessionData(request);
		//Create SpecimenCollectionGroup Object
		SpecimenCollectionGroup scg = createSCG(aliquotForm);
		//Create ParentSpecimen Object
		Specimen parentSpecimen = createParentSpecimen(aliquotForm);
		//Create Specimen Map
		specimenMap = createAliquotDomainObject(aliquotForm, scg, parentSpecimen);
		//Insert Specimen Map
		insertAliquotSpecimen = insertAliquotSpecimen(request, sessionDataBean, specimenMap);
		//Convert Specimen HashSet to List
		specimenList= new LinkedList<Specimen>();
		specimenList.addAll(specimenMap);
		Specimen specimen = specimenList.get(0);
		if(specimen!=null)
		{
			aliquotForm.setSpCollectionGroupId(specimen.getSpecimenCollectionGroup().getId());
			aliquotForm.setScgName(specimen.getSpecimenCollectionGroup().getGroupName());
		}
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
			List<Specimen> specimenList) throws Exception
	{
		if(aliquotForm.getPrintCheckbox()!=null)
		{
			request.setAttribute(Constants.LIST_SPECIMEN, specimenList);
			PrintAction printActionObj = new PrintAction();
			SessionDataBean objBean = (SessionDataBean) request.getSession().getAttribute("sessionData");
			printActionObj.printAliquotLabel(aliquotForm, request,null,objBean);
		}
		if(insertAliquotSpecimen)
		{
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
	 */
	private boolean insertAliquotSpecimen(HttpServletRequest request, SessionDataBean sessionDataBean,
			LinkedHashSet<Specimen> specimenMap) throws UserNotAuthorizedException
	{
		NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();
		try
		{
			bizLogic.insert(specimenMap,sessionDataBean,Constants.HIBERNATE_DAO);
		}
		catch(BizLogicException ex)
		{
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(actionErrors.GLOBAL_MESSAGE, new ActionError("errors.item",ex.getMessage()));
			saveErrors(request, actionErrors);
			saveToken(request);
			return false;
		}
		return true;
	}
	/**
	 * @param aliquotForm
	 * @return
	 * @throws AssignDataException
	 */
	private Specimen createParentSpecimen(AliquotForm aliquotForm) throws AssignDataException
	{
		Specimen parentSpecimen = (Specimen) new SpecimenObjectFactory().getDomainObject(aliquotForm.getSpecimenClass());
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
	private LinkedHashSet<Specimen> createAliquotDomainObject(AliquotForm aliquotForm, SpecimenCollectionGroup scg,
			Specimen parentSpecimen)
	{
		LinkedHashSet<Specimen> specimenMap = new LinkedHashSet<Specimen>();
		boolean disposeParentSpecimen = aliquotForm.getDisposeParentSpecimen();
		Map aliquotMap = aliquotForm.getAliquotMap();
		String specimenKey = "Specimen:";
		Integer noOfAliquots = new Integer(aliquotForm.getNoOfAliquots());
		for (int i = 1; i <= noOfAliquots.intValue(); i++)
		{
			Specimen aliquotSpecimen = Utility.getSpecimen(parentSpecimen);
			StorageContainer sc = new StorageContainer();
			String quantityKey = specimenKey + i + "_quantity";
			String containerIdKey = specimenKey + i + "_StorageContainer_id";
			String posDim1Key = specimenKey + i + "_positionDimensionOne";
			String posDim2Key = specimenKey + i + "_positionDimensionTwo";

			String quantity = (String) aliquotMap.get(quantityKey);
			String containerId  = (String) aliquotMap.get(containerIdKey);
			String posDim1  = (String) aliquotMap.get(posDim1Key);
			String posDim2  = (String) aliquotMap.get(posDim2Key);

			aliquotSpecimen.setType(aliquotForm.getType());
			aliquotSpecimen.setPathologicalStatus(aliquotForm.getPathologicalStatus());
			aliquotSpecimen.setType(aliquotForm.getType());
			aliquotSpecimen.setInitialQuantity(new Quantity(quantity));
			aliquotSpecimen.setAvailableQuantity(new Quantity(quantity));
			if (containerId != null)
			{
				aliquotSpecimen.setPositionDimensionOne(new Integer(posDim1));
				aliquotSpecimen.setPositionDimensionTwo(new Integer(posDim2));
				sc.setId(new Long(containerId));
				aliquotSpecimen.setStorageContainer(sc);
			}
			else
			{
				aliquotSpecimen.setPositionDimensionOne(null);
				aliquotSpecimen.setPositionDimensionTwo(null);
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
			aliquotSpecimen.setAvailable(Boolean.TRUE);
			aliquotSpecimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
			aliquotSpecimen.setCollectionStatus(Constants.COLLECTION_STATUS_COLLECTED);
			aliquotSpecimen.setDisposeParentSpecimen(disposeParentSpecimen);
			specimenMap.add(aliquotSpecimen);
		}

		return specimenMap;
	}
	/**
	 * @param abstractForm
	 * @param abstractDomain
	 * @return
	 * @throws BizLogicException
	 */
	private HashMap generateForwardToPrintMap(AbstractActionForm abstractForm, AbstractDomainObject abstractDomain)throws BizLogicException
    {
        HashMap forwardToPrintMap = null;
        AbstractForwardToProcessor forwardToProcessor=AbstractForwardToFactory.getForwardToProcessor(
				ApplicationProperties.getValue("app.forwardToFactory"),
				"getForwardToPrintProcessor");

        //Populating HashMap of the data required to be forwarded on next page
        forwardToPrintMap = (HashMap)forwardToProcessor.populateForwardToData(abstractForm,abstractDomain);
        return forwardToPrintMap;
    }
}
