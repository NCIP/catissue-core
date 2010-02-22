
package edu.wustl.catissuecore.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * @author virender_mehta
 */
public class CreateAliquotAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(CreateAliquotAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final AliquotForm aliquotForm = (AliquotForm) form;
		boolean insertAliquotSpecimen = true;
		Collection<AbstractDomainObject> specimenCollection = null;
		//List<AbstractDomainObject> specimenList = null;
		//final String fromPrintAction = request.getParameter(Constants.FROM_PRINT_ACTION);
		//final SessionDataBean sessionDataBean = this.getSessionData(request);
		// Create SpecimenCollectionGroup Object
		final SpecimenCollectionGroup scg = this.createSCG(aliquotForm);
		// Create ParentSpecimen Object
		final Specimen parentSpecimen = this.createParentSpecimen(aliquotForm);
		// Create Specimen Map
		try
		{
			specimenCollection = this.createAliquotDomainObject(aliquotForm, scg, parentSpecimen);
		}
		catch (final ApplicationException e)
		{
			this.logger.error(e.getMessage(), e);
			final ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("errors.item", e
					.getMessage()));
			this.saveErrors(request, actionErrors);
			this.saveToken(request);
			return mapping.findForward(Constants.FAILURE);
		}

		final SessionDataBean sessionDataBean = this.getSessionData(request);
		// Insert Specimen Map
		insertAliquotSpecimen = this.insertAliquotSpecimen(request, sessionDataBean,
				specimenCollection);
		// Convert Specimen HashSet to List
		List<AbstractDomainObject> specimenList = new LinkedList<AbstractDomainObject>();
		specimenList.addAll(specimenCollection);
		if(insertAliquotSpecimen)
		{
			final Specimen specimen = (Specimen) specimenList.get(0);
			request.setAttribute(Constants.PARENT_SPECIMEN_ID, parentSpecimen.getId().toString());
			if (specimen != null)
			{
				aliquotForm.setSpCollectionGroupId(specimen.getSpecimenCollectionGroup().getId());
				aliquotForm.setScgName(specimen.getSpecimenCollectionGroup().getGroupName());
			}
			this.calculateAvailableQuantityForParent(specimenList, aliquotForm);
			this.updateParentSpecimen(parentSpecimen,
			Double.parseDouble(aliquotForm.getAvailableQuantity()));
			aliquotForm.setSpecimenList(specimenList);
		}
		final String fromPrintAction = request.getParameter(Constants.FROM_PRINT_ACTION);
		// mapping.findforward
		return this.getFindForward(mapping, request, aliquotForm, fromPrintAction,
				insertAliquotSpecimen, specimenList);
	}

	/**
	 * @param mapping
	 *            : mapping
	 * @param request
	 *            : request
	 * @param aliquotForm
	 *            : aliquotForm
	 * @param fromPrintAction
	 *            : fromPrintAction
	 * @param insertAliquotSpecimen
	 *            : insertAliquotSpecimen
	 * @param specimenList
	 *            : specimenList
	 * @return ActionForward : ActionForward
	 * @throws Exception
	 *             : Exception
	 */
	private ActionForward getFindForward(ActionMapping mapping, HttpServletRequest request,
			AliquotForm aliquotForm, String fromPrintAction, boolean insertAliquotSpecimen,
			List<AbstractDomainObject> specimenList) throws Exception
	{
		if (aliquotForm.getPrintCheckbox() != null)
		{
			SpecimenCollectionGroup scg = null;
			String hql = "from edu.wustl.catissuecore.domain.SpecimenCollectionGroup scg where scg.id="+aliquotForm.getSpCollectionGroupId();
			List scgList = AppUtility.executeQuery(hql);
			if(scgList!=null && !scgList.isEmpty())
			{
				scg = (SpecimenCollectionGroup)scgList.get(0) ;
			}
			Iterator<AbstractDomainObject> itr = specimenList.iterator();
			while(itr.hasNext())
			{
				Specimen specimen = (Specimen)itr.next();
				specimen.setSpecimenCollectionGroup(scg);
			}

			request.setAttribute(Constants.LIST_SPECIMEN, specimenList);
			final PrintAction printActionObj = new PrintAction();
			final SessionDataBean objBean = (SessionDataBean) request.getSession().getAttribute(
					"sessionData");
			printActionObj.printAliquotLabel(aliquotForm, request, null, objBean);
		}
		if (Constants.ADD_SPECIMEN_TO_CART.equals(aliquotForm.getForwardTo())
				&& insertAliquotSpecimen)
		{
			return mapping.findForward(Constants.ADD_SPECIMEN_TO_CART);
		}
		else if (insertAliquotSpecimen)
		{
			if (aliquotForm.getForwardTo().equals(Constants.ORDER_DETAILS))
			{
				final Specimen specimen = (Specimen) specimenList.get(0);
				final String parentSpecimenLable = specimen.getParentSpecimen().getLabel();
				final ActionMessages actionMessages = new ActionMessages();
				actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.distribution.aliquots.created", parentSpecimenLable));
				this.saveMessages(request, actionMessages);
				return mapping.findForward(aliquotForm.getForwardTo());
			}
			else
			{
				return mapping.findForward(Constants.SUCCESS);
			}
		}
		else
		{
			return mapping.findForward(Constants.FAILURE);
		}
	}

	/**
	 * @param request
	 *            : request
	 * @param sessionDataBean
	 *            : sessionDataBean
	 * @param specimenCollection
	 *            : specimenCollection
	 * @return boolean : boolean
	 * @throws UserNotAuthorizedException
	 *             : UserNotAuthorizedException
	 */
	private boolean insertAliquotSpecimen(HttpServletRequest request,
			SessionDataBean sessionDataBean, Collection<AbstractDomainObject> specimenCollection)
			throws UserNotAuthorizedException
	{
		try
		{
			new NewSpecimenBizLogic().insert(specimenCollection, sessionDataBean, 0, false);
			this.disposeParentSpecimen(sessionDataBean, specimenCollection,
					Constants.SPECIMEN_DISPOSAL_REASON);
		}
		catch (final BizLogicException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
			final ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("errors.item", e
					.getCustomizedMsg()));
			this.saveErrors(request, actionErrors);
			this.saveToken(request);
			return false;
		}
		catch (final DAOException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
			final ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("errors.item", e
					.getCustomizedMsg()));
			this.saveErrors(request, actionErrors);
			this.saveToken(request);
			return false;
		}
		catch (final UserNotAuthorizedException e)
		{
			this.logger.error(e.getMessage(),e);
			e.printStackTrace() ;
			String userName = "";
			if (sessionDataBean != null)
			{
				userName = sessionDataBean.getUserName();
			}
			final UserNotAuthorizedException excp = e;
			final ActionErrors actionErrors = new ActionErrors();

			final String className = Utility.getActualClassName(Specimen.class.getName());
			final String decoratedPrivilegeName = AppUtility.getDisplayLabelForUnderscore(e
					.getPrivilegeName());
			String baseObject = "";
			if (excp.getBaseObject() != null && excp.getBaseObject().trim().length() != 0)
			{
				baseObject = excp.getBaseObject();
			}
			else
			{
				baseObject = className;
			}
			final ActionError error = new ActionError("access.addedit.object.denied", userName,
					className, decoratedPrivilegeName, baseObject);
			actionErrors.add(ActionErrors.GLOBAL_ERROR, error);

			// ActionErrors actionErrors = new ActionErrors();
			// ActionError error = new
			// ActionError("access.addedit.object.denied",
			// sessionDataBean.getUserName(), Specimen.class.getName());
			// actionErrors.add(ActionErrors.GLOBAL_ERROR, error);
			this.saveErrors(request, actionErrors);
			return false;
		}
		return true;
	}

	/**
	 * @param sessionDataBean
	 *            : sessionDataBean
	 * @param specimenCollection
	 *            : specimenCollection
	 * @param specimenDisposeReason
	 *            : specimenDisposeReason
	 * @throws DAOException
	 *             : DAOException
	 * @throws UserNotAuthorizedException
	 *             : UserNotAuthorizedException
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private void disposeParentSpecimen(SessionDataBean sessionDataBean,
			Collection<AbstractDomainObject> specimenCollection, String specimenDisposeReason)
			throws DAOException, UserNotAuthorizedException, BizLogicException
	{
		final Iterator<AbstractDomainObject> spItr = specimenCollection.iterator();
		final Specimen specimen = (Specimen) spItr.next();
		if (specimen != null && specimen.getDisposeParentSpecimen())
		{
			new NewSpecimenBizLogic().disposeSpecimen(sessionDataBean,
					specimen.getParentSpecimen(), specimenDisposeReason);
		}
	}

	/**
	 * @param aliquotForm
	 *            : aliquotForm
	 * @return Specimen : Specimen
	 * @throws AssignDataException
	 *             : AssignDataException
	 */
	private Specimen createParentSpecimen(AliquotForm aliquotForm) throws AssignDataException
	{
		final Specimen parentSpecimen = (Specimen) new SpecimenObjectFactory()
				.getDomainObject(aliquotForm.getClassName());
		parentSpecimen.setId(Long.valueOf(aliquotForm.getSpecimenID()));
		String label = aliquotForm.getSpecimenLabel();
		if (label != null && label.equals(""))
		{
			label = null;
		}
		parentSpecimen.setLabel(label);
		parentSpecimen.setBarcode(aliquotForm.getBarcode());
		return parentSpecimen;
	}

	/**
	 *
	 * @param aliquotForm : aliquotForm
	 * @return SpecimenCollectionGroup : SpecimenCollectionGroup
	 */
	private SpecimenCollectionGroup createSCG(AliquotForm aliquotForm)
	{
		final SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setId(Long.valueOf(aliquotForm.getSpCollectionGroupId()));
		return scg;
	}

	/**
	 *
	 * @param aliquotForm : aliquotForm
	 * @param scg : scg
	 * @param parentSpecimen : parentSpecimen
	 * @return Collection < AbstractDomainObject > : Collection < AbstractDomainObject >
	 * @throws ApplicationException : ApplicationException
	 */
	private Collection<AbstractDomainObject> createAliquotDomainObject(AliquotForm aliquotForm,
			SpecimenCollectionGroup scg, Specimen parentSpecimen) throws ApplicationException
	{
		final Collection<AbstractDomainObject> specimenCollection = new LinkedHashSet<AbstractDomainObject>();
		final boolean disposeParentSpecimen = aliquotForm.getDisposeParentSpecimen();
		final Map aliquotMap = aliquotForm.getAliquotMap();
		final String specimenKey = "Specimen:";
		final Integer noOfAliquots = Integer.valueOf(aliquotForm.getNoOfAliquots());
		for (int i = 1; i <= noOfAliquots.intValue(); i++)
		{
			//final Specimen aliquotSpecimen = AppUtility.getSpecimen(parentSpecimen);
			//final StorageContainer sc = new StorageContainer();
			final String fromMapsuffixKey = "_fromMap";
			// boolean booleanfromMap = false;

			//String quantityKey = null;
			String containerIdKey = null;
			String containerNameKey = null;
			String posDim1Key = null;
			String posDim2Key = null;
			final String radioButton = (String) aliquotMap.get("radio_" + i);
			// if radio button =2 else conatiner selected from Combo box
			String quantityKey = specimenKey + i + "_quantity";
			if (radioButton != null && radioButton.equals("2"))
			{
				containerIdKey = specimenKey + i + "_StorageContainer_id";
				posDim1Key = specimenKey + i + "_positionDimensionOne";
				posDim2Key = specimenKey + i + "_positionDimensionTwo";
			}
			else if (radioButton != null && radioButton.equals("3"))
			{
				// Container selected from Map button
				containerIdKey = specimenKey + i + "_StorageContainer_id" + fromMapsuffixKey;
				containerNameKey = specimenKey + i + "_StorageContainer_name" + fromMapsuffixKey;
				posDim1Key = specimenKey + i + "_positionDimensionOne" + fromMapsuffixKey;
				posDim2Key = specimenKey + i + "_positionDimensionTwo" + fromMapsuffixKey;
			}
			new Validator();
			final String quantity = (String) aliquotMap.get(quantityKey);
			String containerId = (String) aliquotMap.get(containerIdKey);
			Long storageContainerId = null;
			if (Validator.isEmpty(containerId))
			{
				containerId = null;
			}
			else if (containerId != null)
			{
				storageContainerId = Long.valueOf(containerId);
			}
			final String containername = (String) aliquotMap.get(containerNameKey);
			final String posDim1 = (String) aliquotMap.get(posDim1Key);
			final String posDim2 = (String) aliquotMap.get(posDim2Key);

			/**
			 * validate the the container name,id,pos1 and pos2 is not empty
			 * depending on Auto,virtual,manual selection
			 */
			if (radioButton != null)
			{
				if ("2".equals(radioButton) && Validator.isEmpty(containerId))
				{
					throw AppUtility.getApplicationException(null, "errors.item.format",
							ApplicationProperties.getValue("specimen.storageContainer"));
				}
				if ("3".equals(radioButton) && Validator.isEmpty(containername))
				{
					throw AppUtility.getApplicationException(null, "errors.item.format",
							ApplicationProperties.getValue("specimen" + ".storageContainer"));
				}
				// bug 11479 S bug
				if ("2".equals(radioButton)
						&& (Validator.isEmpty(posDim1) || Validator.isEmpty(posDim2)))

				{
					throw AppUtility.getApplicationException(null, "errors.item.format",
							ApplicationProperties.getValue("specimen"
									+ ".positionInStorageContainer"));
				}
			}
			final Specimen aliquotSpecimen = AppUtility.getSpecimen(parentSpecimen);
			aliquotSpecimen.setSpecimenClass(aliquotForm.getClassName());
			aliquotSpecimen.setSpecimenType(aliquotForm.getType());
			aliquotSpecimen.setPathologicalStatus(aliquotForm.getPathologicalStatus());
			aliquotSpecimen.setInitialQuantity(new Double(quantity));
			aliquotSpecimen.setAvailableQuantity(new Double(quantity));

			final StorageContainer sc = new StorageContainer();
			// bug 11479
			if ((containerId != null || containername != null) && posDim1 != null
					&& posDim2 != null)
			{
				final SpecimenPosition specPos = new SpecimenPosition();

				if (!"".equals(posDim1) && !"".equals(posDim2))
				{
					specPos.setPositionDimensionOne(Integer.valueOf(posDim1));
					specPos.setPositionDimensionTwo(Integer.valueOf(posDim2));
					sc.setId(storageContainerId);
				}
				sc.setName(containername);
				specPos.setStorageContainer(sc);
				specPos.setSpecimen(aliquotSpecimen);
				aliquotSpecimen.setSpecimenPosition(specPos);
			}

			if (aliquotSpecimen instanceof MolecularSpecimen)
			{
				if (aliquotForm.getConcentration().equals(""))
				{
					((MolecularSpecimen) aliquotSpecimen)
							.setConcentrationInMicrogramPerMicroliter(0.0);
				}
				else
				{
					final Double concentration = new Double(aliquotForm.getConcentration());
					if (concentration != null)
					{
						((MolecularSpecimen) aliquotSpecimen)
								.setConcentrationInMicrogramPerMicroliter(concentration);
					}
				}
			}
			// Date currentDate = new Date();
			final DateFormat myDateFormat = new SimpleDateFormat(CommonServiceLocator.getInstance()
					.getDatePattern());
			Date myDate = null;
			try
			{
				myDate = myDateFormat.parse(aliquotForm.getCreatedDate());

			}
			catch (final ParseException e)
			{
				this.logger.error("Invalid Date Parser Exception: " + e.getMessage(), e);
				// System.out.println("Invalid Date Parser Exception ");
				//e.printStackTrace();
			}
			aliquotSpecimen.setCreatedOn(myDate);
			aliquotSpecimen.setParentSpecimen(parentSpecimen);
			aliquotSpecimen.setSpecimenCollectionGroup(scg);
			aliquotSpecimen.setLineage(Constants.ALIQUOT);
			aliquotSpecimen.setIsAvailable(Boolean.TRUE);
			aliquotSpecimen.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
			aliquotSpecimen.setCollectionStatus(Constants.COLLECTION_STATUS_COLLECTED);
			aliquotSpecimen.setDisposeParentSpecimen(disposeParentSpecimen);
			final SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
			specimenCharacteristics.setTissueSide(aliquotForm.getTissueSide());
			specimenCharacteristics.setTissueSite(aliquotForm.getTissueSite());
			aliquotSpecimen.setSpecimenCharacteristics(specimenCharacteristics);
			// bug no. 8081 and 8083
			if (!edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl)
			{
				aliquotSpecimen.setLabel((String) aliquotMap.get(specimenKey + i + "_label"));
			}
			specimenCollection.add(aliquotSpecimen);
		}

		return specimenCollection;
	}

	/**
	 * This function calculates the available quantity of parent after creating
	 * aliquots.
	 * @param specimenList : specimenList
	 * @param aliquotForm : aliquotForm
	 */
	private void calculateAvailableQuantityForParent(List specimenList, AliquotForm aliquotForm)
	{
		Double totalAliquotQty = 0.0;

		if (specimenList != null && !specimenList.isEmpty())
		{
			final Iterator itr = specimenList.iterator();
			while (itr.hasNext())
			{
				final Specimen specimen = (Specimen) itr.next();
				if (specimen.getInitialQuantity() != null)
				{
					totalAliquotQty = totalAliquotQty + specimen.getInitialQuantity();
				}

			}
			if (aliquotForm.getInitialAvailableQuantity() != null)
			{
				final Double availableQuantity = Double.parseDouble(aliquotForm
						.getInitialAvailableQuantity())
						- totalAliquotQty;
				aliquotForm.setAvailableQuantity(availableQuantity.toString());
			}
		}
	}

	private void updateParentSpecimen(Specimen parentSpecimen, Double quantity) throws Exception
	{
		DAO dao = AppUtility.openDAOSession(null);
		Object pSpec =  dao.retrieveById(AbstractSpecimen.class.getName(),  parentSpecimen.getId());
		((Specimen)pSpec).setAvailableQuantity(quantity);
		dao.update(pSpec);
		dao.commit();
		dao.closeSession();
	}
}