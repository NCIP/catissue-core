
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
import edu.wustl.catissuecore.bean.SpecimenDataBean;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.util.SpecimenDetailsTagUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * @author renuka_bajpai
 */
public class UpdateBulkSpecimensAction extends UpdateSpecimenStatusAction
{
	/**
	 * logger.
	 */
	private transient Logger logger = Logger.getCommonLogger(UpdateBulkSpecimensAction.class);
	/**
	 * specimenSummaryForm.
	 */
	private ViewSpecimenSummaryForm specimenSummaryForm = null;
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

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		HttpSession session = request.getSession();
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		final SessionDataBean sessionDataBean = (SessionDataBean) session
				.getAttribute(Constants.SESSION_DATA);
		try
		{
			this.specimenSummaryForm = (ViewSpecimenSummaryForm) form;
			final String eventId = this.specimenSummaryForm.getEventId();
			session = request.getSession();
			final LinkedHashSet specimenDomainCollection = this
					.getSpecimensToSave(eventId, session);
			if (ViewSpecimenSummaryForm.ADD_USER_ACTION.equals(this.specimenSummaryForm
					.getUserAction()))
			{
				// Abhishek Mehta : Performance related Changes
				final Collection<AbstractDomainObject> specimenCollection
				= new LinkedHashSet<AbstractDomainObject>();
				specimenCollection.addAll(specimenDomainCollection);
				new NewSpecimenBizLogic().insert(specimenCollection, sessionDataBean, 0, false);
				this.setLabelBarCodesToSessionData(eventId, request, specimenDomainCollection);

				this.updateWithNewStorageLocation(session, sessionDataBean, eventId,
						specimenDomainCollection);
			}
			else
			{
				((NewSpecimenBizLogic) bizLogic).bulkUpdateSpecimens(specimenDomainCollection,
						sessionDataBean);
				this.setLabelBarCodesToSessionData(eventId, request, specimenDomainCollection);

				// 11479 S
				this.updateWithNewStorageLocation(session, sessionDataBean, eventId,
						specimenDomainCollection);
				// 11479 E

				/*
				 * Iterator iter=specimenDomainCollection.iterator(); List
				 * specimenIdList=new ArrayList(); while( iter.hasNext()) {
				 * specimenIdList.add(((Specimen)iter.next()).getId()); }
				 * request.setAttribute("specimenIdList", specimenIdList);
				 */
			}
			final ActionMessages actionMessages = new ActionMessages();
			actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"object.add.successOnly", "Specimens"));
			this.specimenSummaryForm.setShowbarCode(true);
			this.specimenSummaryForm.setShowLabel(true);
			this.saveMessages(request, actionMessages);
			this.specimenSummaryForm.setReadOnly(true);
			// bug 12959
			request.setAttribute("readOnly", true);

			// if(request.getParameter("pageOf") != null)
			// return mapping.findForward(request.getParameter("pageOf"));

			// 19May08 : Mandar : For GenericSpecimen
			SpecimenDetailsTagUtil.setAnticipatorySpecimenDetails(request,
					this.specimenSummaryForm, true);
			if (request.getAttribute("printflag") != null
					&& request.getAttribute("printflag").equals("1"))
			{
				final HashMap forwardToPrintMap = new HashMap();
				forwardToPrintMap.put("printMultipleSpecimen", this.getSpecimensToPrint(
						this.specimenSummaryForm, specimenDomainCollection));
				request.setAttribute("forwardToPrintMap", forwardToPrintMap);
				request.setAttribute("printMultiple", "1");
				if (request.getParameter("pageOf") != null)
				{
					request.setAttribute("pageOf", request.getParameter("pageOf"));
				}
				// bug 12656 start
				if (this.specimenSummaryForm.getForwardTo() != null
						&& !this.specimenSummaryForm.getForwardTo().equals(
								Constants.ADD_MULTIPLE_SPECIMEN_TO_CART))
				{
					return mapping.findForward("printMultiple");
				}
				// bug 12656 end

			}
			if (this.specimenSummaryForm.getForwardTo() != null
					&& this.specimenSummaryForm.getForwardTo().equals(
							Constants.ADD_MULTIPLE_SPECIMEN_TO_CART))
			{
				final Iterator iter = specimenDomainCollection.iterator();
				final List specimenIdList = new ArrayList();
				while (iter.hasNext())
				{
					specimenIdList.add(((Specimen) iter.next()).getId());
				}
				request.setAttribute("specimenIdList", specimenIdList);
				if (request.getAttribute("pageOf") == null)
				{
					request.setAttribute("pageOf", Constants.SUCCESS);
				}
				// bug 12656 start
				if (request.getAttribute("printflag") != null
						&& request.getAttribute("printflag").equals("1"))
				{
					// request.setAttribute("pageOf",
					// Constants.SUCCESS);//commented as add to my list + print
					// - menubar disappers
					return mapping.findForward(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART_AND_PRINT);
				}
				else
				{
					if (request.getAttribute("pageOf") != null
							&& request.getAttribute("pageOf").equals(
									Constants.PAGE_OF_MULTIPLE_SPECIMEN_WITHOUT_MENU))
					{
						return mapping
								.findForward(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART_WITHOUT_MENU);//
					}
					else
					{
						return mapping.findForward(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART);
					}
				}
				// bug 12656 end

			}
			if (request.getParameter("pageOf") != null)
			{
				return mapping.findForward(request.getParameter("pageOf"));
			}

			return mapping.findForward(Constants.SUCCESS);
		}
		catch (final Exception exception)
		{
			this.logger.debug(exception.getMessage(), exception);
			// 11July08 : Mandar : For GenericSpecimen
			SpecimenDetailsTagUtil.setAnticipatorySpecimenDetails(request,
					this.specimenSummaryForm, true);

			ActionErrors actionErrors = new ActionErrors();

			// To delegate UserNotAuthorizedException forward
			if (exception instanceof UserNotAuthorizedException)
			{
				final UserNotAuthorizedException ex = (UserNotAuthorizedException) exception;
				String userName = "";

				if (sessionDataBean != null)
				{
					userName = sessionDataBean.getUserName();
				}
				final String className = Constants.SPECIMEN;
				final String decoratedPrivilegeName = AppUtility.getDisplayLabelForUnderscore(ex
						.getPrivilegeName());
				String baseObject = "";
				if (ex.getBaseObject() != null && ex.getBaseObject().trim().length() != 0)
				{
					baseObject = ex.getBaseObject();
				}
				else
				{
					baseObject = className;
				}

				final ActionError error = new ActionError("access.addedit.object.denied", userName,
						className, decoratedPrivilegeName, baseObject);
				actionErrors.add(ActionErrors.GLOBAL_ERROR, error);
				this.saveErrors(request, actionErrors);
				return (mapping.findForward("multipleSpWithMenuFaliure"));
			}
			else if(exception instanceof ApplicationException)
			{
				actionErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("errors.item",
						((ApplicationException)exception).getCustomizedMsg()));
			}
			else
			{
				actionErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("errors.item",
						exception.getMessage()));
			}
			
			logger.info(exception.getMessage(), exception);
			this.saveErrors(request, actionErrors);
			this.saveToken(request);
			if (request.getParameter("pageOf") != null)
			{
				return mapping.findForward(request.getParameter("pageOf"));
			}
			return mapping.findForward(Constants.FAILURE);
		}
	}

	/**
	 *
	 * @param specimenSummaryForm : specimenSummaryForm
	 * @param specimenDomainCollection : specimenDomainCollection
	 * @return Set < Specimen > : Set < Specimen >
	 */
	private Set<Specimen> getSpecimensToPrint(ViewSpecimenSummaryForm specimenSummaryForm,
			LinkedHashSet specimenDomainCollection)
	{
		final Set specimenprintCollection = specimenSummaryForm.getSpecimenPrintList();
		final Iterator it = specimenprintCollection.iterator();
		final Set<Specimen> specimensToPrint = new LinkedHashSet<Specimen>();
		while (it.hasNext())
		{
			final Object obj = it.next();
			if (obj instanceof GenericSpecimen)
			{
				final GenericSpecimen gSpecimen = (GenericSpecimen) obj;
				final Iterator itr = specimenDomainCollection.iterator();
				while (itr.hasNext())
				{
					final Specimen specimen = (Specimen) itr.next();
					String label = gSpecimen.getDisplayName();
					//label is coming null in case of specimens added through
					//multiple specimens page.
					if(label==null)
					{
						label = gSpecimen.getCorresSpecimen().getLabel();
					}
					if(specimen.getLabel()!=null && label!=null)
					{
						if(specimen.getLabel().equals(label))
						{
							specimensToPrint.add(specimen);
						}
					}
				}						
			}
		}
		return specimensToPrint;
	}

	/**
	 *
	 * @param session : session
	 * @param sessionDataBean : sessionDataBean
	 * @param eventId : eventId
	 * @param specimenDomainCollection : specimenDomainCollection
	 * @throws DAOException : DAOException
	 */
	private void updateWithNewStorageLocation(HttpSession session, SessionDataBean sessionDataBean,
			String eventId, LinkedHashSet specimenDomainCollection) throws DAOException
	{
		// Iterator < Specimen > specimensItr =
		// specimenDomainCollection.iterator();
		final Map collectionProtocolEventMap = (Map) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);

		final CollectionProtocolEventBean eventBean
		= (CollectionProtocolEventBean) collectionProtocolEventMap
				.get(eventId);

		final LinkedHashMap<String, GenericSpecimen> specimenMap = (LinkedHashMap) eventBean
				.getSpecimenRequirementbeanMap();
		final Collection<GenericSpecimen> specCollection = specimenMap.values();
		final Iterator<GenericSpecimen> iterator = specCollection.iterator();
		final HibernateDAO dao = (HibernateDAO) DAOConfigFactory.getInstance().getDAOFactory(
				Constants.APPLICATION_NAME).getDAO();
		dao.openSession(sessionDataBean);
		while (iterator.hasNext())
		{
			final SpecimenDataBean specimenDataBean = (SpecimenDataBean) iterator.next();
			final Specimen specimen = specimenDataBean.getCorresSpecimen();
			if ((specimen.getSpecimenPosition() != null)
					&& (specimen.getSpecimenPosition().getPositionDimensionOne() != null)
					&& (specimen.getSpecimenPosition().getPositionDimensionTwo() != null))
			{
				specimenDataBean.setPositionDimensionOne(String.valueOf(specimen
						.getSpecimenPosition().getPositionDimensionOne()));
				specimenDataBean.setPositionDimensionTwo(String.valueOf(specimen
						.getSpecimenPosition().getPositionDimensionTwo()));
			}
			final LinkedHashMap<String, GenericSpecimen> derivesMap = specimenDataBean
					.getDeriveSpecimenCollection();
			final Collection derivesCollection = derivesMap.values();
			final Iterator deriveItr = derivesCollection.iterator();
			while (deriveItr.hasNext())
			{
				final SpecimenDataBean deriveSpecimenDataBean
				= (SpecimenDataBean) deriveItr.next();
				final Specimen deriveSpec = deriveSpecimenDataBean.getCorresSpecimen();
				if ((deriveSpec.getSpecimenPosition() != null)
						&& (deriveSpec.getSpecimenPosition()
								.getPositionDimensionOne() != null)
						&& (deriveSpec.getSpecimenPosition()
								.getPositionDimensionTwo() != null))
				{
					deriveSpecimenDataBean.setPositionDimensionOne(String.valueOf(deriveSpec
							.getSpecimenPosition().getPositionDimensionOne()));
					deriveSpecimenDataBean.setPositionDimensionTwo(String.valueOf(deriveSpec
							.getSpecimenPosition().getPositionDimensionTwo()));
				}
			}
		}
		dao.closeSession();

		final ViewSpecimenSummaryAction viewSpecimenSummaryAction = new ViewSpecimenSummaryAction();
		viewSpecimenSummaryAction
				.populateSpecimenSummaryForm(this.specimenSummaryForm, specimenMap);
	}

	/**
	 * @param specimenVO : specimenVO
	 * @return Specimen : Specimen
	 * @throws ApplicationException : ApplicationException
	 */
	@Override
	protected Specimen createSpecimenDomainObject(GenericSpecimen specimenVO)
			throws ApplicationException
	{

		specimenVO = specimenVO;
		specimenVO.setCheckedSpecimen(specimenVO.getCheckedSpecimen());
		specimenVO.setPrintSpecimen(specimenVO.getPrintSpecimen());// Bug 12631
		final Specimen specimen = super.createSpecimenDomainObject(specimenVO);
		this.setValuesForSpecimen(specimen, specimenVO);
		if (ViewSpecimenSummaryForm.ADD_USER_ACTION
				.equals(this.specimenSummaryForm.getUserAction()))
		{
			this.setValuesForNewSpecimen(specimen, specimenVO);
		}

		return specimen;
	}

	/**
	 *
	 * @param specimen : specimen
	 * @param genericSpecimen : genericSpecimen
	 */
	protected void setValuesForNewSpecimen(Specimen specimen, GenericSpecimen genericSpecimen)
	{
		final SpecimenDataBean specimenDataBean = (SpecimenDataBean) genericSpecimen;

		specimen.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		specimen.setComment(specimenDataBean.getComment());
		// bug 12936 start
		if (specimenDataBean.getCheckedSpecimen())
		{
			specimen.setCollectionStatus(Constants.SPECIMEN_COLLECTED);
		}
		else
		{
			specimen.setCollectionStatus("Pending");
		}
		// bug 12936 end
		if (specimen.getCreatedOn() == null)
		{
			specimen.setCreatedOn(new Date());
		}
		// specimen.setCollectionStatus(Constants.SPECIMEN_COLLECTED);
		genericSpecimen.setCheckedSpecimen(specimenDataBean.getCheckedSpecimen());
		genericSpecimen.setPrintSpecimen(specimenDataBean.getPrintSpecimen());
		specimenDataBean.setCorresSpecimen(specimen);
		
		//specimen.setSpecimenEventCollection(specimenDataBean.getSpecimenEventCollection());
		specimen.setAvailableQuantity(specimen.getInitialQuantity());
		if (specimenDataBean.getSpecimenEventCollection() != null
				&& !specimenDataBean.getSpecimenEventCollection().isEmpty())
		{
			final Iterator iterator = specimenDataBean.getSpecimenEventCollection().iterator();
			while (iterator.hasNext())
			{
				final SpecimenEventParameters specimenEventParameters
				= (SpecimenEventParameters) iterator
						.next();
				specimenEventParameters.setSpecimen(specimen);
				specimenEventParameters.setId(null);
			}
		}
		
		Collection<ExternalIdentifier> externalIdentifierColl = specimenDataBean.getExternalIdentifierCollection();
		 Iterator<ExternalIdentifier> externalIdentifierCollItr = externalIdentifierColl.iterator();
		  while (externalIdentifierCollItr.hasNext()) {
			  ExternalIdentifier externalIdentifier  = (ExternalIdentifier) externalIdentifierCollItr.next();
			  externalIdentifier.setId(null);
		  }

	}

	/**
	 *
	 * @param specimen : specimen
	 * @param genericSpecimen : genericSpecimen
	 */
	protected void setValuesForSpecimen(Specimen specimen, GenericSpecimen genericSpecimen)
	{
		final SpecimenDataBean specimenDataBean = (SpecimenDataBean) genericSpecimen;
		specimen.setPathologicalStatus(specimenDataBean.getPathologicalStatus());
		specimen.setLineage(specimenDataBean.getLineage());
		final SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
		specimenCharacteristics.setTissueSide(specimenDataBean.getTissueSide());
		specimenCharacteristics.setTissueSite(specimenDataBean.getTissueSite());
		specimen.setSpecimenCharacteristics(specimenCharacteristics);
		specimen.setLineage(specimenDataBean.getLineage());
		specimen.setIsAvailable(Boolean.TRUE);
		specimen.setPathologicalStatus(specimenDataBean.getPathologicalStatus());
		specimen.setSpecimenType(specimenDataBean.getType());
		specimen.setParentSpecimen(specimenDataBean.getParentSpecimen());
		specimen.setComment(specimenDataBean.getComment());
		specimen
				.setExternalIdentifierCollection(specimenDataBean.getExternalIdentifierCollection());
		specimen.setBiohazardCollection(specimenDataBean.getBiohazardCollection());
		specimen.setSpecimenCollectionGroup(specimenDataBean.getSpecimenCollectionGroup());
		if (specimenDataBean.getCorresSpecimen() != null)
		{
			specimen.setCreatedOn(specimenDataBean.getCorresSpecimen().getCreatedOn());
		}

		specimenDataBean.setCorresSpecimen(specimen);
	}

	/**
	 *
	 * @param EventId : EventId
	 * @param request : request
	 * @param SpecimenCollection : SpecimenCollection
	 */
	private void setLabelBarCodesToSessionData(String EventId, HttpServletRequest request,
			Collection SpecimenCollection)
	{
		final HttpSession session = request.getSession();
		final LinkedHashMap specimenMap = (LinkedHashMap) session
				.getAttribute(Constants.SPECIMEN_LIST_SESSION_MAP);
		this.setLabelBarCodeToSpecimens(specimenMap);

	}

	/**
	 * @param specimenMap : specimenMap
	 */
	private void setLabelBarCodeToSpecimens(LinkedHashMap specimenMap)
	{
		final Collection specimenCollection = specimenMap.values();
		final Iterator iterator = specimenCollection.iterator();
		while (iterator.hasNext())
		{
			final SpecimenDataBean specimenDataBean = (SpecimenDataBean) iterator.next();
			final Specimen specimen = specimenDataBean.getCorresSpecimen();
			final GenericSpecimen formSpecimen = specimenDataBean.getFormSpecimenVo();

			if (specimen == null || formSpecimen == null)
			{
				continue;
			}
			formSpecimen.setDisplayName(specimen.getLabel());
			formSpecimen.setBarCode(specimen.getBarcode());
			this.setParentLabelToFormSpecimen(specimen, formSpecimen);
			if (specimenDataBean.getDeriveSpecimenCollection() != null)
			{
				this.setLabelBarCodeToSpecimens(specimenDataBean.getDeriveSpecimenCollection());
			}
		}
	}

	/**
	 * @param specimen : specimen
	 * @param formSpecimen : formSpecimen
	 * @return
	 */
	private void setParentLabelToFormSpecimen(Specimen specimen, GenericSpecimen formSpecimen)
	{
		final Specimen parentSpecimen = (Specimen) specimen.getParentSpecimen();
		if (parentSpecimen != null)
		{
			formSpecimen.setParentName(parentSpecimen.getLabel());
		}
	}
}
