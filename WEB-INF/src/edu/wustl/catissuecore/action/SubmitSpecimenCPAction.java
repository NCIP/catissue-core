
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;

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
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenDataBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.dto.CollectionProtocolDTO;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.MultipleSpecimenValidationUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * @author renuka_bajpai
 */
public class SubmitSpecimenCPAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(SubmitSpecimenCPAction.class);

	/**
	 * specimenSummaryForm.
	 */
	private ViewSpecimenSummaryForm specimenSummaryForm;

	/**
	 * specimenCollectionGroup.
	 */
	private SpecimenCollectionGroup specimenCollectionGroup = null;

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
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
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String target = Constants.SUCCESS;
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		final HashMap resultMap = new HashMap();
		try
		{
			this.specimenSummaryForm = (ViewSpecimenSummaryForm) form;
			// String actionValue = request.getParameter("action");

			if (ViewSpecimenSummaryForm.REQUEST_TYPE_COLLECTION_PROTOCOL
					.equals(this.specimenSummaryForm.getRequestType()))
			{

				final HttpSession session = request.getSession();
				final CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
						.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);

				if (ViewSpecimenSummaryForm.UPDATE_USER_ACTION.equals(this.specimenSummaryForm
						.getUserAction()))
				{
					// for disabling of CP set the collection protocol status:
					// kalpana
					if (collectionProtocolBean != null
							&& collectionProtocolBean.getActivityStatus() != null)
					{
						ViewSpecimenSummaryForm.setCollectionProtocolStatus(collectionProtocolBean
								.getActivityStatus());
					}
					return mapping.findForward("updateCP");
				}
				else
				{

					final CollectionProtocol collectionProtocol = CollectionProtocolUtil
							.populateCollectionProtocolObjects(request);

					final CollectionProtocolDTO collectionProtocolDTO = AppUtility
							.getCoolectionProtocolDTO(collectionProtocol, session);
					this.insertCollectionProtocol(collectionProtocolDTO, request.getSession());

					collectionProtocolBean.setIdentifier(collectionProtocol.getId());
					CollectionProtocolUtil.updateSession(request, collectionProtocol.getId());
				}
				final ActionMessages actionMessages = new ActionMessages();
				actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"object.add.successOnly", "Collection Protocol"));
				this.saveMessages(request, actionMessages);
			}

			if (ViewSpecimenSummaryForm.REQUEST_TYPE_MULTI_SPECIMENS
					.equals(this.specimenSummaryForm.getRequestType()))
			{
				final LinkedHashMap cpEventMap = this.populateSpecimenDomainObjectMap(request);

				if (ViewSpecimenSummaryForm.UPDATE_USER_ACTION.equals(this.specimenSummaryForm
						.getUserAction()))
				{
					// execute update multiplespecimens.
				}
				else
				{
					this.insertSpecimens(cpEventMap, request.getSession());
					MultipleSpecimenValidationUtil.setMultipleSpecimensInSession(request,
							this.specimenCollectionGroup.getId());

				}
				final ActionMessages actionMessages = new ActionMessages();
				actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"object.add.successOnly", "Specimen(s)"));
				this.saveMessages(request, actionMessages);

			}

			target = Constants.SUCCESS;

			if (pageOf != null && pageOf.equals("pageOfMultipleSpWithMenu"))
			{
				target = pageOf;
			}

			this.specimenSummaryForm.setUserAction(ViewSpecimenSummaryForm.UPDATE_USER_ACTION);
		}
		catch (final Exception ex)
		{
			this.logger.debug(ex.getMessage(), ex);
			target = Constants.FAILURE;
			if (pageOf != null && pageOf.equals("pageOfMultipleSpWithMenu"))
			{
				target = "pageOfMultipleSpWithMenuFailure";
			}

			final String errorMsg = ex.getMessage();
			resultMap.put(Constants.ERROR_DETAIL, errorMsg);
			ex.printStackTrace();
			final ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("errors.item", ex
					.getMessage()));

			final SessionDataBean sessionDataBean = this.getSessionData(request);
			String userName = "";
			if (sessionDataBean != null)
			{
				userName = sessionDataBean.getUserName();
			}

			// To delegate UserNotAuthorizedException forward
			if (ex instanceof UserNotAuthorizedException)
			{
				final UserNotAuthorizedException excp = (UserNotAuthorizedException) ex;

				final String className = Utility.getActualClassName(CollectionProtocol.class
						.getName());
				final String decoratedPrivilegeName = AppUtility
						.getDisplayLabelForUnderscore(((UserNotAuthorizedException) ex)
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
			}

			this.saveErrors(request, actionErrors);

		}
		resultMap.put(Constants.MULTIPLE_SPECIMEN_RESULT, target);
		// writeMapToResponse(response, resultMap);
		this.logger.debug("In MultipleSpecimenAppletAction :- resultMap : " + resultMap);

		return mapping.findForward(target);

	}

	/**
	 *
	 * @param cpEventMap : cpEventMap
	 * @param session : session
	 * @throws BizLogicException : BizLogicException
	 */
	private void insertSpecimens(LinkedHashMap cpEventMap, HttpSession session)
			throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		final SessionDataBean sessionDataBean = (SessionDataBean) session
				.getAttribute(Constants.SESSION_DATA);
		bizLogic.insert(cpEventMap, sessionDataBean, 0);
	}

	/**
	 *
	 * @param collectionProtocolDTO : collectionProtocolDTO
	 * @param session : session
	 * @throws BizLogicException : BizLogicException
	 */
	private void insertCollectionProtocol(CollectionProtocolDTO collectionProtocolDTO,
			HttpSession session) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
		final SessionDataBean sessionDataBean = (SessionDataBean) session
				.getAttribute(Constants.SESSION_DATA);
		bizLogic.insert(collectionProtocolDTO, sessionDataBean, 0);
	}

	/**
	 *
	 * @param request : request
	 * @return LinkedHashMap : LinkedHashMap
	 * @throws Exception : Exception
	 */
	private LinkedHashMap populateSpecimenDomainObjectMap(HttpServletRequest request)
			throws Exception
	{

		final HttpSession session = request.getSession();
		LinkedHashMap<String, GenericSpecimen> cpEventMap;
		cpEventMap = (LinkedHashMap) session.getAttribute(Constants.SPECIMEN_LIST_SESSION_MAP);
		final LinkedHashMap specimenMap = new LinkedHashMap();
		final Collection specimenSessionColl = cpEventMap.values();
		final Iterator iterator = specimenSessionColl.iterator();

		while (iterator.hasNext())
		{
			final SpecimenDataBean specimenDataBean = (SpecimenDataBean) iterator.next();
			final Specimen specimen = this.getSpecimenDomainObjectFromObject(specimenDataBean);

			if (specimen.getSpecimenCollectionGroup() != null)
			{
				specimen.setLineage(Constants.NEW_SPECIMEN);
				if (this.specimenCollectionGroup == null)
				{
					this.specimenCollectionGroup = specimen.getSpecimenCollectionGroup();
				}
			}
			else
			{
				specimen.setLineage(Constants.DERIVED_SPECIMEN);
				specimen.setParentSpecimen(specimenDataBean.getParentSpecimen());

				if (specimenDataBean.getSpecimenCollectionGroup() == null)
				{
					final Specimen parentSpeciemn = (Specimen) specimen.getParentSpecimen();

					final Long scgId = parentSpeciemn.getSpecimenCollectionGroup().getId();
					final IFactory factory = AbstractFactoryConfig.getInstance()
							.getBizLogicFactory();
					final IBizLogic iBizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
					final Object object = iBizLogic.retrieve(SpecimenCollectionGroup.class
							.getName(), scgId);
					specimen.setSpecimenCollectionGroup((SpecimenCollectionGroup) object);

				}
				else
				{
					specimen.setSpecimenCollectionGroup(specimenDataBean
							.getSpecimenCollectionGroup());
				}
			}

			final SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
			specimenCharacteristics.setTissueSide(specimenDataBean.getTissueSide());
			specimenCharacteristics.setTissueSite(specimenDataBean.getTissueSite());
			specimen.setSpecimenCharacteristics(specimenCharacteristics);

			final ArrayList childSpecimenList = new ArrayList();

			if (specimenDataBean.getAliquotSpecimenCollection() != null)
			{
				this.getAliquotSpecimens(specimenDataBean, specimen, childSpecimenList);
			}

			if (specimenDataBean.getDeriveSpecimenCollection() != null)
			{
				this.getDerivedSpecimens(specimenDataBean, specimen, childSpecimenList);
			}
			specimenMap.put(specimen, childSpecimenList);

		}
		return specimenMap;

	}

	/**
	 * @param specimenDataBean : specimenDataBean
	 * @param parentSpecimen : parentSpecimen
	 * @param childSpecimenList : childSpecimenList
	 */
	private void getDerivedSpecimens(SpecimenDataBean specimenDataBean, Specimen parentSpecimen,
			ArrayList childSpecimenList)
	{
		final Collection derivedSpecimenCollection = specimenDataBean.getDeriveSpecimenCollection()
				.values();
		final Iterator derivedSpecimenIteraror = derivedSpecimenCollection.iterator();

		while (derivedSpecimenIteraror.hasNext())
		{
			final SpecimenDataBean derivedSpecimenBean = (SpecimenDataBean) derivedSpecimenIteraror
					.next();
			final Specimen derivedSpecimen = this
					.getSpecimenDomainObjectFromObject(derivedSpecimenBean);
			derivedSpecimen.setParentSpecimen(parentSpecimen);

			derivedSpecimen.setLineage(Constants.DERIVED_SPECIMEN);
			derivedSpecimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
			childSpecimenList.add(derivedSpecimen);
		}
	}

	/**
	 * @param specimenDataBean : specimenDataBean
	 * @param parentSpecimen : parentSpecimen
	 * @param childSpecimenList : childSpecimenList
	 */
	private void getAliquotSpecimens(SpecimenDataBean specimenDataBean, Specimen parentSpecimen,
			ArrayList childSpecimenList)
	{
		final Collection aliquotSpecimenCollection = specimenDataBean
				.getAliquotSpecimenCollection().values();
		final Iterator aliquotSpecimenIteraror = aliquotSpecimenCollection.iterator();

		while (aliquotSpecimenIteraror.hasNext())
		{
			final SpecimenDataBean aliquotSpecimenBean = (SpecimenDataBean) aliquotSpecimenIteraror
					.next();
			final Specimen aliquotSpecimen = this
					.getSpecimenDomainObjectFromObject(aliquotSpecimenBean);
			aliquotSpecimen.setParentSpecimen(parentSpecimen);
			aliquotSpecimen.setLineage(Constants.ALIQUOT);
			aliquotSpecimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
			childSpecimenList.add(aliquotSpecimen);
		}
	}

	/**
	 *
	 * @param specimenDataBean : specimenDataBean
	 * @return Specimen : Specimen
	 */
	private Specimen getSpecimenDomainObjectFromObject(SpecimenDataBean specimenDataBean)
	{
		Specimen specimen;
		try
		{
			specimen = (Specimen) new SpecimenObjectFactory().getDomainObject(specimenDataBean
					.getClassName());
		}
		catch (final AssignDataException e1)
		{
			e1.printStackTrace();
			return null;
		}

		specimen.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		specimen.setBarcode(specimenDataBean.getBarCode());
		specimen.setComment(specimenDataBean.getComment());
		specimen.setCreatedOn(new Date());
		specimen.setCollectionStatus(Constants.SPECIMEN_COLLECTED);
		specimen.setLabel(specimenDataBean.getLabel());
		specimen.setPathologicalStatus(specimenDataBean.getPathologicalStatus());

		// specimen.setAvailable(Boolean.FALSE);
		Double availableQuantity = new Double(0);
		double value = 0;
		final String s = specimenDataBean.getQuantity();
		try
		{
			value = Double.parseDouble(s);
		}
		catch (final NumberFormatException e)
		{
			value = 0;
		}

		availableQuantity = value;
		specimen.setAvailableQuantity(availableQuantity);
		specimen.setInitialQuantity(availableQuantity);
		specimen.setLineage(specimenDataBean.getLineage());
		specimen.setPathologicalStatus(specimenDataBean.getPathologicalStatus());
		specimen.setSpecimenType(specimenDataBean.getType());

		specimen
				.setExternalIdentifierCollection(specimenDataBean.getExternalIdentifierCollection());
		specimen.setBiohazardCollection(specimenDataBean.getBiohazardCollection());

		if (specimenDataBean.getSpecimenEventCollection() != null
				&& !specimenDataBean.getSpecimenEventCollection().isEmpty())
		{
			final Iterator iterator = specimenDataBean.getSpecimenEventCollection().iterator();
			final HashSet speEventParamSet = new HashSet();
			while (iterator.hasNext())
			{
				final SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) iterator
						.next();
				if (specimenEventParameters.getUser() != null)
				{
					specimenEventParameters.setSpecimen(specimen);
					speEventParamSet.add(specimenEventParameters);
				}
			}
			specimen.setSpecimenEventCollection(speEventParamSet);
		}

		specimen.setSpecimenCollectionGroup(specimenDataBean.getSpecimenCollectionGroup());

		final StorageContainer storageContainer = new StorageContainer();
		storageContainer.setName(specimenDataBean.getStorageContainerForSpecimen());

		// specimen.setStorageContainer(null);
		specimen.setSpecimenPosition(null);
		final SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
		specimenCharacteristics.setTissueSide(specimenDataBean.getTissueSide());
		specimenCharacteristics.setTissueSite(specimenDataBean.getTissueSite());
		specimen.setSpecimenCharacteristics(specimenCharacteristics);

		return specimen;

	}
}
