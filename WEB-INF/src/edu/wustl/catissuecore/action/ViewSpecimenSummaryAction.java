
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bizlogic.StorageContainerForSpecimenBizLogic;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.util.SpecimenDetailsTagUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * @author renuka_bajpai
 *
 */
public class ViewSpecimenSummaryAction extends Action
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(ViewSpecimenSummaryAction.class);

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
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		String target = Constants.SUCCESS;
		final ViewSpecimenSummaryForm summaryForm = (ViewSpecimenSummaryForm) form;
		try
		{
			final HttpSession session = request.getSession();
			// summaryForm.setLastSelectedSpecimenId(summaryForm.
			// getSelectedSpecimenId());
			// Mandar : 5Aug08 ----------- start
			//bug 13164 start
			final String forwardToValue = request.getParameter(Constants.FORWARD_TO);
			if (forwardToValue != null)
			{
				if (Constants.CP_CHILD_SUBMIT.equals(forwardToValue))
				{
					request.setAttribute(Constants.IS_SCG_SUBMIT, Constants.SPECIMEN_SUBMIT);
				}
				else
				{
					request.setAttribute(Constants.IS_SCG_SUBMIT, Constants.SCG_SUBMIT);
				}
			}
			//bug 13164 end
			final String sid = request.getParameter("sid");
			if (sid != null)
			{
				this.getAvailablePosition(request, response);
				return null;
			}
			// Mandar : 5Aug08 ----------- end
			String eventId = summaryForm.getEventId();
			session.setAttribute(Constants.TREE_NODE_ID, request.getParameter("nodeId"));

			final Object obj = request.getAttribute("SCGFORM");
			request.setAttribute("SCGFORM", obj);
			target = request.getParameter("target");
			final String submitAction = request.getParameter("submitAction");

			if (target == null)
			{
				target = Constants.SUCCESS;
			}

			if (submitAction != null)
			{
				summaryForm.setSubmitAction(submitAction);
			}

			if (summaryForm.getTargetSuccess() == null)
			{
				summaryForm.setTargetSuccess(target);
			}
			target = summaryForm.getTargetSuccess();

			if (request.getAttribute("RequestType") != null)
			{
				summaryForm
						.setRequestType(ViewSpecimenSummaryForm.REQUEST_TYPE_ANTICIPAT_SPECIMENS);
			}
			// bug 12656
			if (request.getParameter("pageOf") != null)
			{
				request.setAttribute("pageOf", request.getParameter("pageOf"));
			}

			if (eventId == null)
			{
				eventId = request.getParameter(Constants.COLLECTION_PROTOCOL_EVENT_ID);
			}

			if (summaryForm.getSpecimenList() != null)
			{
				this.updateSessionBean(summaryForm, session);
				this.verifyCollectedStatus(summaryForm, session);
				this.verifyPrintStatus(summaryForm, session);// janhavi
			}

			if (request.getParameter("save") != null)
			{
				if (!this.isTokenValid(request))
				{
					summaryForm.setReadOnly(true);
					throw new CatissueException("cannot submit duplicate request.");
				}

				this.resetToken(request);
				if ((summaryForm.getSubmitAction().equals("bulkUpdateSpecimens") || summaryForm
						.getSubmitAction().equals("pageOfbulkUpdateSpecimens"))
						&& (request.getParameter("printflag") != null && request.getParameter(
								"printflag").equals("1")))
				{
					request.setAttribute("printflag", "1");
					return mapping.findForward(summaryForm.getSubmitAction());
				}
				else
				{
					return mapping.findForward(summaryForm.getSubmitAction());
				}
			}
			else
			{
				this.saveToken(request);
			}

			final CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
					.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);

			// for disabling of CP set the collection protocol status: kalpana

			if (collectionProtocolBean != null
					&& collectionProtocolBean.getActivityStatus() != null)
			{

				// checked the associated specimens to the cp
				final boolean isSpecimenExist = this.isSpecimenExists(collectionProtocolBean
						.getIdentifier());
				if (isSpecimenExist)
				{
					ViewSpecimenSummaryForm.setSpecimenExist("true");
				}
				else
				{
					ViewSpecimenSummaryForm.setSpecimenExist("false");
				}

				ViewSpecimenSummaryForm.setCollectionProtocolStatus(collectionProtocolBean
						.getActivityStatus());
			}

			final LinkedHashMap<String, GenericSpecimen> specimenMap = this
					.getSpecimensFromSessoin(session, eventId, summaryForm);

			if (specimenMap != null)
			{
				this.populateSpecimenSummaryForm(summaryForm, specimenMap);
			}

			if (ViewSpecimenSummaryForm.REQUEST_TYPE_COLLECTION_PROTOCOL.equals(summaryForm
					.getRequestType()))
			{
				summaryForm.setUserAction(ViewSpecimenSummaryForm.ADD_USER_ACTION);
				if ("update".equals(collectionProtocolBean.getOperation()))
				{
					summaryForm.setUserAction(ViewSpecimenSummaryForm.UPDATE_USER_ACTION);
				}
			}
			final String pageOf = request.getParameter(Constants.PAGE_OF);
			request.setAttribute(Constants.PAGE_OF, pageOf);

			// Mandar: 16May2008 : For specimenDetails customtag --- start ---
			if ("anticipatory".equalsIgnoreCase(target))
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

			// Mandar: 16May2008 : For specimenDetails customtag --- end ---
			summaryForm.setLastSelectedSpecimenId(summaryForm.getSelectedSpecimenId());
			if (pageOf != null
					&& ViewSpecimenSummaryForm.REQUEST_TYPE_MULTI_SPECIMENS.equals(summaryForm
							.getRequestType()))
			{
				// request.setAttribute(Constants.PAGE_OF,pageOf);
				return mapping.findForward(target);
			}

			return mapping.findForward(target);
		}
		catch (final Exception exception)
		{
			this.logger.error(exception.getMessage(), exception);
			// exception.printStackTrace();
			final ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("errors.item",
					exception.getMessage()));
			this.saveErrors(request, actionErrors);
			// Mandar: 17JULY2008 : For specimenDetails customtag --- start ---
			if ("anticipatory".equalsIgnoreCase(target))
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

			// Mandar: 17JULY2008 : For specimenDetails customtag --- end ---

			return mapping.findForward(target);
		}

	}

	/**
	 * @param summaryForm
	 *            : summaryForm
	 * @param session
	 *            : session
	 */
	private void updateSessionBean(ViewSpecimenSummaryForm summaryForm, HttpSession session)
	{
		final String eventId = summaryForm.getEventId();
		if (eventId == null
				|| session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP) == null)
		{
			return;
		}

		final Map collectionProtocolEventMap = (Map) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		final CollectionProtocolEventBean eventBean = (CollectionProtocolEventBean) collectionProtocolEventMap
				.get(eventId); // get nullpointer sometimes
		final LinkedHashMap specimenMap = (LinkedHashMap) eventBean.getSpecimenRequirementbeanMap();
		final String selectedItem = summaryForm.getLastSelectedSpecimenId();
		final GenericSpecimen selectedSpecimen = (GenericSpecimen) specimenMap.get(selectedItem);

		this.updateSpecimenToSession(summaryForm, specimenMap);
		if (selectedSpecimen != null)
		{
			this.updateAliquotToSession(summaryForm, selectedSpecimen);
			this.updateDerivedToSession(summaryForm, selectedSpecimen);
		}
	}

	/**
	 * @param summaryForm
	 *            : summaryForm
	 * @param specimenMap
	 *            : specimenMap
	 */
	private void updateSpecimenToSession(ViewSpecimenSummaryForm summaryForm,
			LinkedHashMap specimenMap)
	{
		// Collection specimenCollection = specimenMap.values();
		final Iterator iterator = summaryForm.getSpecimenList().iterator();

		while (iterator.hasNext())
		{
			final GenericSpecimen specimenFormVO = (GenericSpecimen) iterator.next();

			final GenericSpecimen specimenSessionVO = (GenericSpecimen) specimenMap
					.get(specimenFormVO.getUniqueIdentifier());

			if (specimenSessionVO != null)
			{
				this.setFormValuesToSession(specimenFormVO, specimenSessionVO);
			}

		}
	}

	/**
	 * @param summaryForm
	 *            : summaryForm
	 * @param selectedSpecimen
	 *            : selectedSpecimen
	 */
	private void updateAliquotToSession(ViewSpecimenSummaryForm summaryForm,
			GenericSpecimen selectedSpecimen)
	{
		final Iterator aliquotIterator = summaryForm.getAliquotList().iterator();

		while (aliquotIterator.hasNext())
		{
			final GenericSpecimen aliquotFormVO = (GenericSpecimen) aliquotIterator.next();
			final String aliquotKey = aliquotFormVO.getUniqueIdentifier();
			final GenericSpecimen aliquotSessionVO = this.getAliquotSessionObject(selectedSpecimen,
					aliquotKey);
			if (aliquotSessionVO != null)
			{
				this.setFormValuesToSession(aliquotFormVO, aliquotSessionVO);
			}

		}
	}

	/**
	 * @param summaryForm
	 *            : summaryForm
	 * @param selectedSpecimen
	 *            : selectedSpecimen
	 */
	private void updateDerivedToSession(ViewSpecimenSummaryForm summaryForm,
			GenericSpecimen selectedSpecimen)
	{
		final Iterator derivedIterator = summaryForm.getDerivedList().iterator();

		while (derivedIterator.hasNext())
		{
			final GenericSpecimen derivedFormVO = (GenericSpecimen) derivedIterator.next();
			final String derivedKey = derivedFormVO.getUniqueIdentifier();
			final GenericSpecimen derivedSessionVO = this.getDerivedSessionObject(selectedSpecimen,
					derivedKey);
			if (derivedSessionVO != null)
			{
				this.setFormValuesToSession(derivedFormVO, derivedSessionVO);
			}

		}
	}

	/**
	 * @param derivedFormVO
	 *            : derivedFormVO
	 * @param derivedSessionVO
	 *            : derivedSessionVO
	 */
	private void setFormValuesToSession(GenericSpecimen derivedFormVO,
			GenericSpecimen derivedSessionVO)
	{
		derivedSessionVO.setCheckedSpecimen(derivedFormVO.getCheckedSpecimen());
		derivedSessionVO.setPrintSpecimen(derivedFormVO.getPrintSpecimen());// janhavi
		derivedSessionVO.setDisplayName(derivedFormVO.getDisplayName());
		derivedSessionVO.setBarCode(derivedFormVO.getBarCode());
		// derivedSessionVO.setContainerId(derivedFormVO.getContainerId());
		derivedSessionVO.setContainerId(null);
		// Mandar : 6August08 ------- start
		derivedSessionVO.setStorageContainerForSpecimen(derivedFormVO
				.getStorageContainerForSpecimen());
		// Mandar : 6August08 ------- end
		derivedSessionVO.setSelectedContainerName(derivedFormVO.getSelectedContainerName());
		derivedSessionVO.setPositionDimensionOne(derivedFormVO.getPositionDimensionOne());
		derivedSessionVO.setPositionDimensionTwo(derivedFormVO.getPositionDimensionTwo());
		derivedSessionVO.setQuantity(derivedFormVO.getQuantity());
		derivedSessionVO.setConcentration(derivedFormVO.getConcentration());
		derivedSessionVO.setFormSpecimenVo(derivedFormVO);
	}

	/**
	 * @param parentSessionObject
	 *            : parentSessionObject
	 * @param derivedKey
	 *            : derivedKey
	 * @return GenericSpecimen : GenericSpecimen
	 */
	private GenericSpecimen getDerivedSessionObject(GenericSpecimen parentSessionObject,
			String derivedKey)
	{
		final LinkedHashMap deriveMap = parentSessionObject.getDeriveSpecimenCollection();
		Collection parentCollection;
		if (deriveMap != null && !deriveMap.isEmpty())
		{
			// return null;

			GenericSpecimen derivedSessionObject = (GenericSpecimen) deriveMap.get(derivedKey);
			if (derivedSessionObject != null)
			{
				return derivedSessionObject;
			}
			parentCollection = deriveMap.values();
			final Iterator parentIterator = parentCollection.iterator();

			while (parentIterator.hasNext())
			{
				final GenericSpecimen parentDerived = (GenericSpecimen) parentIterator.next();
				derivedSessionObject = this.getDerivedSessionObject(parentDerived, derivedKey);
				if (derivedSessionObject != null)
				{
					return derivedSessionObject;
				}
			}
		}
		// Search Derived in derived specimen tree.
		final LinkedHashMap aliquotMap = parentSessionObject.getAliquotSpecimenCollection();

		if (aliquotMap != null && !aliquotMap.isEmpty())
		{
			parentCollection = aliquotMap.values();
			final Iterator parentIterator = parentCollection.iterator();

			while (parentIterator.hasNext())
			{
				final GenericSpecimen derivedSpecimen = (GenericSpecimen) parentIterator.next();
				final GenericSpecimen derivedSessionObject = this.getDerivedSessionObject(
						derivedSpecimen, derivedKey);
				if (derivedSessionObject != null)
				{
					return derivedSessionObject;
				}
			}
		}
		return null;

	}

	/**
	 * @param parentSessionObject
	 *            : parentSessionObject
	 * @param aliquotKey
	 *            : aliquotKey
	 * @return GenericSpecimen : GenericSpecimen
	 */
	private GenericSpecimen getAliquotSessionObject(GenericSpecimen parentSessionObject,
			String aliquotKey)
	{
		final LinkedHashMap aliquotMap = parentSessionObject.getAliquotSpecimenCollection();
		if (aliquotMap != null && !aliquotMap.isEmpty())
		{
			GenericSpecimen aliquotSessionObject = (GenericSpecimen) aliquotMap.get(aliquotKey);
			if (aliquotSessionObject != null)
			{
				return aliquotSessionObject;
			}
			final Collection parentCollection = aliquotMap.values();
			final Iterator parentIterator = parentCollection.iterator();

			while (parentIterator.hasNext())
			{
				final GenericSpecimen parentAliquot = (GenericSpecimen) parentIterator.next();
				aliquotSessionObject = this.getAliquotSessionObject(parentAliquot, aliquotKey);
				if (aliquotSessionObject != null)
				{
					return aliquotSessionObject;
				}
			}

		}
		// Search Aliquot in derived specimen tree.
		final LinkedHashMap deriveMap = parentSessionObject.getDeriveSpecimenCollection();

		if (deriveMap != null && !deriveMap.isEmpty())
		{
			final Collection parentCollection = deriveMap.values();
			final Iterator parentIterator = parentCollection.iterator();

			while (parentIterator.hasNext())
			{
				final GenericSpecimen derivedSpecimen = (GenericSpecimen) parentIterator.next();
				final GenericSpecimen aliquotSessionObject = this.getAliquotSessionObject(
						derivedSpecimen, aliquotKey);
				if (aliquotSessionObject != null)
				{
					return aliquotSessionObject;
				}
			}
		}
		return null;

	}

	/**
	 * This function retrieves the Map of specimens from session.
	 *
	 * @param session
	 *            : session
	 * @param eventId
	 *            : eventId
	 * @param summaryForm
	 *            : summaryForm
	 * @return LinkedHashMap < String , GenericSpecimen > : LinkedHashMap <
	 *         String , GenericSpecimen >
	 */
	private LinkedHashMap<String, GenericSpecimen> getSpecimensFromSessoin(HttpSession session,
			String eventId, ViewSpecimenSummaryForm summaryForm)
	{

		LinkedHashMap<String, GenericSpecimen> specimenMap = null;

		if (eventId == null)
		{
			eventId = "dummy";
		}

		if (eventId != null)
		{
			if (summaryForm.getRequestType() == null)
			{
				summaryForm
						.setRequestType(ViewSpecimenSummaryForm.REQUEST_TYPE_COLLECTION_PROTOCOL);
			}
			final StringTokenizer stringTokenizer = new StringTokenizer(eventId, "_");
			if (stringTokenizer != null)
			{
				if (stringTokenizer.hasMoreTokens())
				{
					eventId = stringTokenizer.nextToken();
				}
			}

			final Map collectionProtocolEventMap = (Map) session
					.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);

			if (collectionProtocolEventMap != null && !collectionProtocolEventMap.isEmpty())
			{

				CollectionProtocolEventBean collectionProtocolEventBean = (CollectionProtocolEventBean) collectionProtocolEventMap
						.get(eventId);

				if (collectionProtocolEventBean == null)
				{

					eventId = (String) collectionProtocolEventMap.keySet().iterator().next();
					final Collection cl = collectionProtocolEventMap.values();

					if (cl != null && !cl.isEmpty())
					{

						collectionProtocolEventBean = (CollectionProtocolEventBean) cl.iterator()
								.next();
					}

				}
				if (collectionProtocolEventBean != null)
				{

					specimenMap = (LinkedHashMap) collectionProtocolEventBean
							.getSpecimenRequirementbeanMap();

				}
			}
			summaryForm.setEventId(eventId);
		}
		else
		{
			summaryForm.setRequestType(ViewSpecimenSummaryForm.REQUEST_TYPE_MULTI_SPECIMENS);
			specimenMap = (LinkedHashMap) session.getAttribute(Constants.SPECIMEN_LIST_SESSION_MAP);
		}

		return specimenMap;
	}

	/**
	 * Populates form object of the action with speimen, aliquot specimen and
	 * derived specimen object.
	 *
	 * @param summaryForm
	 *            : summaryForm
	 * @param specimenMap
	 *            : specimenMap
	 */
	public void populateSpecimenSummaryForm(ViewSpecimenSummaryForm summaryForm,
			LinkedHashMap<String, GenericSpecimen> specimenMap)
	{

		final LinkedList<GenericSpecimen> specimenList = this.getSpecimenList(specimenMap.values());
		summaryForm.setSpecimenList(specimenList);
		String selectedSpecimenId = summaryForm.getSelectedSpecimenId();

		if (selectedSpecimenId == null)
		{
			if (specimenList != null && !specimenList.isEmpty())
			{
				selectedSpecimenId = (specimenList.get(0)).getUniqueIdentifier();
				summaryForm.setSelectedSpecimenId(selectedSpecimenId);
			}
		}
		final GenericSpecimen selectedSpecimen = specimenMap.get(selectedSpecimenId);

		if (selectedSpecimen == null)
		{
			return;
		}

		final LinkedHashMap<String, GenericSpecimen> aliquotsList = selectedSpecimen
				.getAliquotSpecimenCollection();
		final LinkedHashMap<String, GenericSpecimen> derivedList = selectedSpecimen
				.getDeriveSpecimenCollection();

		final Collection nestedAliquots = new LinkedHashSet();
		final Collection nestedDerives = new LinkedHashSet();
		if (aliquotsList != null && !aliquotsList.values().isEmpty())
		{
			nestedAliquots.addAll(aliquotsList.values());
			this.getNestedChildSpecimens(aliquotsList.values(), nestedAliquots, nestedDerives);
			// getNestedAliquotSpecimens(aliquotsList.values(), nestedAliquots);
			// getNestedDerivedSpecimens(aliquotsList.values(), nestedDerives);

		}

		if (derivedList != null && !derivedList.values().isEmpty())
		{
			nestedDerives.addAll(derivedList.values());
			this.getNestedChildSpecimens(derivedList.values(), nestedAliquots, nestedDerives);
			// getNestedAliquotSpecimens(derivedList.values(), nestedAliquots);
			// getNestedDerivedSpecimens(derivedList.values(), nestedDerives);
		}

		summaryForm.setAliquotList(this.getSpecimenList(nestedAliquots));
		summaryForm.setDerivedList(this.getSpecimenList(nestedDerives));
		AppUtility.setDefaultPrinterTypeLocation(summaryForm);

	}

	/**
	 * @param topChildCollection
	 *            : topChildCollection
	 * @param nestedAliquoteCollection
	 *            : nestedAliquoteCollection
	 * @param nestedDerivedCollection
	 *            : nestedDerivedCollection
	 */
	private void getNestedChildSpecimens(Collection topChildCollection,
			Collection nestedAliquoteCollection, Collection nestedDerivedCollection)
	{

		final Iterator iterator = topChildCollection.iterator();

		while (iterator.hasNext())
		{
			final GenericSpecimen specimen = (GenericSpecimen) iterator.next();

			if (specimen.getAliquotSpecimenCollection() != null)
			{
				final Collection childSpecimen = specimen.getAliquotSpecimenCollection().values();

				if (!childSpecimen.isEmpty())
				{
					nestedAliquoteCollection.addAll(childSpecimen);
					this.getNestedChildSpecimens(childSpecimen, nestedAliquoteCollection,
							nestedDerivedCollection);
				}
			}

			if (specimen.getDeriveSpecimenCollection() != null)
			{
				final Collection childSpecimen = specimen.getDeriveSpecimenCollection().values();

				if (!childSpecimen.isEmpty())
				{
					nestedDerivedCollection.addAll(childSpecimen);
					this.getNestedChildSpecimens(childSpecimen, nestedAliquoteCollection,
							nestedDerivedCollection);
				}
			}

		}
	}

	/*
	 * private void getNestedAliquotSpecimens(Collection topChildCollection,
	 * Collection nestedCollection) { Iterator iterator =
	 * topChildCollection.iterator(); while (iterator.hasNext()) {
	 * GenericSpecimen specimen = (GenericSpecimen) iterator.next(); if
	 * (specimen.getAliquotSpecimenCollection() != null) { Collection
	 * childSpecimen = specimen.getAliquotSpecimenCollection().values(); if
	 * (!childSpecimen.isEmpty()) { nestedCollection.addAll(childSpecimen);
	 * getNestedAliquotSpecimens(childSpecimen, nestedCollection); } } } }
	 */

	/*
	 * private void getNestedDerivedSpecimens(Collection topChildCollection,
	 * Collection nestedCollection) { Iterator iterator =
	 * topChildCollection.iterator(); while (iterator.hasNext()) {
	 * GenericSpecimen specimen = (GenericSpecimen) iterator.next(); if
	 * (specimen.getDeriveSpecimenCollection() != null) { Collection
	 * childSpecimen = specimen.getDeriveSpecimenCollection().values(); if
	 * (!childSpecimen.isEmpty()) { nestedCollection.addAll(childSpecimen);
	 * getNestedDerivedSpecimens(childSpecimen, nestedCollection); } } } }
	 */

	/**
	 * @param specimenColl
	 *            : specimenColl
	 * @return LinkedList < GenericSpecimen > : LinkedList < GenericSpecimen >
	 */
	private LinkedList<GenericSpecimen> getSpecimenList(Collection<GenericSpecimen> specimenColl)
	{
		final LinkedList<GenericSpecimen> specimenList = new LinkedList<GenericSpecimen>();
		if (!specimenColl.isEmpty())
		{
			specimenList.addAll(specimenColl);

			// IdComparator speciemnIdComp = new IdComparator();
			// Collections.sort(specimenList,speciemnIdComp);

		}
		return specimenList;
	}

	/**
	 * To check the associated specimens to the Collection protocol.
	 *
	 * @param cpId
	 *            : cpId
	 * @return boolean : boolean
	 * @throws ApplicationException
	 *             : ApplicationException
	 */
	protected boolean isSpecimenExists(Long cpId) throws ApplicationException
	{

		final String hql = " select" + " elements(scg.specimenCollection) " + "from "
				+ " edu.wustl.catissuecore.domain.CollectionProtocol as cp"
				+ ", edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr"
				+ ", edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg"
				+ ", edu.wustl.catissuecore.domain.Specimen as s" + " where cp.id = " + cpId
				+ "  and" + " cp.id = cpr.collectionProtocol.id and"
				+ " cpr.id = scg.collectionProtocolRegistration.id and"
				+ " scg.id = s.specimenCollectionGroup.id and " + " s.activityStatus = '"
				+ Status.ACTIVITY_STATUS_ACTIVE.toString() + "'";

		final List specimenList = AppUtility.executeQuery(hql);
		if ((specimenList != null) && (specimenList).size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	/**
	 * @param summaryForm
	 *            : summaryForm
	 * @param session
	 *            : session
	 */
	private void verifyCollectedStatus(ViewSpecimenSummaryForm summaryForm, HttpSession session)
	{
		final String eventId = summaryForm.getEventId();
		if (eventId == null
				|| session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP) == null)
		{
			return;
		}
		final Map collectionProtocolEventMap = (Map) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
	}

	/**
	 * @param summaryForm
	 *            : summaryForm
	 * @param session
	 *            : session
	 */
	private void verifyPrintStatus(ViewSpecimenSummaryForm summaryForm, HttpSession session)
	{
		final String eventId = summaryForm.getEventId();
		if (eventId == null
				|| session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP) == null)
		{
			return;
		}
		final Map collectionProtocolEventMap = (Map) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		final CollectionProtocolEventBean eventBean = (CollectionProtocolEventBean) collectionProtocolEventMap
				.get(eventId); // get nullpointer sometimes
		final LinkedHashMap specimenMap = (LinkedHashMap) eventBean.getSpecimenRequirementbeanMap();
		specimenMap.values().iterator();
		final List<GenericSpecimen> allSpcimens = new ArrayList<GenericSpecimen>();
		allSpcimens.addAll(specimenMap.values());
		final Set printSpecimenSet = new HashSet();
		//bug 13157
		this.getSpecimenList(specimenMap, allSpcimens);
		/*while (specItr.hasNext())
		{
			final GenericSpecimen pSpecimen = (GenericSpecimen) specItr.next();
			final LinkedHashMap < String, GenericSpecimen > aliquots = pSpecimen
					.getAliquotSpecimenCollection();
			if (aliquots != null && !aliquots.isEmpty())
			{
				allSpcimens.addAll( aliquots.values() );
			}
			final LinkedHashMap < String, GenericSpecimen > derivaties = pSpecimen
					.getDeriveSpecimenCollection();
			if (derivaties != null && !derivaties.isEmpty())
			{
				allSpcimens.addAll( derivaties.values() );
			}
		}*/
		for (final GenericSpecimen specimen : allSpcimens)
		{
			if (specimen.getPrintSpecimen() == true)
			{
				printSpecimenSet.add(specimen);
			}
		}
		if (!printSpecimenSet.isEmpty())
		{
			summaryForm.setSpecimenPrintList(printSpecimenSet);
		}

	}

	private Collection getSpecimenList(LinkedHashMap specimenMap, List<GenericSpecimen> allSpcimens)
	{
		if (specimenMap != null)
		{
			final Iterator<GenericSpecimen> specItr = specimenMap.values().iterator();
			while (specItr.hasNext())
			{
				final GenericSpecimen pSpecimen = specItr.next();
				final LinkedHashMap<String, GenericSpecimen> aliquots = pSpecimen
						.getAliquotSpecimenCollection();
				if (aliquots != null && !aliquots.isEmpty())
				{
					allSpcimens.addAll(aliquots.values());
					this.getSpecimenList(aliquots, allSpcimens);
				}
				final LinkedHashMap<String, GenericSpecimen> derivaties = pSpecimen
						.getDeriveSpecimenCollection();
				if (derivaties != null && !derivaties.isEmpty())
				{
					allSpcimens.addAll(derivaties.values());
					this.getSpecimenList(derivaties, allSpcimens);
				}
			}
		}
		return allSpcimens;
	}

	/**
	 * @param request
	 *            : request
	 * @param response
	 *            : response
	 * @throws IOException
	 *             : IOException
	 * @throws ApplicationException
	 *             : ApplicationException
	 */
	private void getAvailablePosition(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ApplicationException
	{
		DAO dao = null;

		try
		{
			final SessionDataBean sessionData = (SessionDataBean) request.getSession()
					.getAttribute(Constants.SESSION_DATA);
			dao = AppUtility.openDAOSession(sessionData);
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			final HttpSession session = request.getSession();
			Set asignedPositonSet = (HashSet) session.getAttribute("asignedPositonSet");
			if (asignedPositonSet == null)
			{
				asignedPositonSet = new HashSet();
			}
			// TODO
			// to get available position from SC for the specimen.
			// String sid = (String) request.getParameter("sid");
			final String className = request.getParameter("cName");
			final String cpid = request.getParameter("cpid");
			// List initialValues = null;
			TreeMap containerMap = new TreeMap();
			final StorageContainerForSpecimenBizLogic scbizLogic = 
			new StorageContainerForSpecimenBizLogic();
			long cpId = 0;
			cpId = Long.parseLong(cpid);
			containerMap = scbizLogic.getAllocatedContainerMapForSpecimen(AppUtility.setparameterList(cpId,className,0),
					sessionData, dao);
			final StringBuffer sb = new StringBuffer();
			if (containerMap.isEmpty())
			{
				sb.append("No Container available for the specimen");
			}
			else
			{
				sb.append(this.checkForFreeInitialValues(containerMap, asignedPositonSet));
				session.setAttribute("asignedPositonSet", asignedPositonSet);
			}
			final String msg = sb.toString();
			response.getWriter().write(msg);
		}

		catch (final DAOException daoException)
		{
			this.logger.error(daoException.getMessage(),daoException);
			throw AppUtility.getApplicationException(daoException, daoException.getErrorKeyName(),
					daoException.getMsgValues());
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}

	}

	/**
	 * @param containerMap
	 *            : containerMap
	 * @param asignedPositonSet
	 *            : asignedPositonSet
	 * @return String : String
	 */
	private String checkForFreeInitialValues(Map containerMap, Set asignedPositonSet)
	{
		//System.out.println("containerMap :: " + containerMap + "\n\n");
		if (containerMap.size() > 0)
		{
			StringBuffer mainKey = null;
			final Set containerMapkeySet = containerMap.keySet();
			final Iterator containerMapkeySetitr = containerMapkeySet.iterator();
			while (containerMapkeySetitr.hasNext())
			{
				mainKey = new StringBuffer();
				final NameValueBean containerMapkey = (NameValueBean) containerMapkeySetitr.next();
				//System.out.println("\t" + containerMapkey);
				mainKey.append(containerMapkey.getName());
				mainKey.append("#");
				mainKey.append(containerMapkey.getValue());
				mainKey.append("#");
				final Map maincontainerMapvaluemap1 = (Map) containerMap.get(containerMapkey);
				final Set maincontainerMapvaluemap1keySet = maincontainerMapvaluemap1.keySet();
				final Iterator maincontainerMapvaluemap1keySetitr = maincontainerMapvaluemap1keySet
						.iterator();

				while (maincontainerMapvaluemap1keySetitr.hasNext())
				{
					final NameValueBean maincontainerMapvaluemap1key = (NameValueBean) maincontainerMapvaluemap1keySetitr
							.next();
					//System.out.println("\t\t" + maincontainerMapvaluemap1key);
					final StringBuffer pos1 = new StringBuffer();
					pos1.append(maincontainerMapvaluemap1key.getValue());
					pos1.append("#");
					final List list = (List) maincontainerMapvaluemap1
							.get(maincontainerMapvaluemap1key);

					for (int i = 0; i < list.size(); i++)
					{
						final NameValueBean maincontainerMapvaluemap1value = (NameValueBean) list
								.get(i);
						//System.out.println("\t\t\t\t" + maincontainerMapvaluemap1value);
						final StringBuffer pos2 = new StringBuffer();
						pos2.append(maincontainerMapvaluemap1value.getValue());
						final StringBuffer availablePos = new StringBuffer();
						availablePos.append(mainKey);
						availablePos.append(pos1);
						availablePos.append(pos2);
						final String freePosition = availablePos.toString();
						if (!asignedPositonSet.contains(freePosition))
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
