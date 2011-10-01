/**
 * <p>
 * Title: CollectionProtocolAction Class>
 * <p>
 * Description: This class initializes the fields in the CollectionProtocol
 * Add/Edit webpage.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Mandar Deshmukh
 * @version 1.00 Created on Mar 22, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.ctrp.COPPAServiceClient;
import edu.wustl.catissuecore.ctrp.COPPAUtil;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.query.generator.ColumnValueBean;
import gov.nih.nci.coppa.po.Person;

// TODO: Auto-generated Javadoc
/**
 * This class initializes the fields in the CollectionProtocol Add/Edit webpage.
 *
 * @author Mandar Deshmukh
 */
public class CollectionProtocolAction extends SpecimenProtocolAction
{

	/**
	 * logger.
	 */
	private static transient final Logger LOGGER = Logger.getCommonLogger(CollectionProtocolAction.class);
	// This will keep track of no of consents for a particular participant
	/**
	 * consentCounter.
	 */
	int consentCounter = 0;

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
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		super.executeSecureAction(mapping, form, request, response);
		// pageOf required for Advance Search Object View.
		final String tabSel = request.getParameter("tabSel");
		request.setAttribute("tabSel", tabSel);
		final List<NameValueBean> clinicalDiagnosis = new ArrayList<NameValueBean>();
		request.setAttribute(edu.common.dynamicextensions.ui.util.Constants.SELECTED_VALUES, clinicalDiagnosis);
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		final String submittedFor = (String) request.getAttribute(Constants.SUBMITTED_FOR);
		String invokeFunction = request.getParameter("invokeFunction");
		if (invokeFunction == null)
		{
			invokeFunction = (String) request.getAttribute("invokeFunction");
		}
		String operation = request.getParameter(Constants.OPERATION);
		if (operation == null)
		{
			operation = (String) request.getAttribute(Constants.OPERATION);
		}
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		// Gets the value of the operation attribute.

		final HttpSession newSession = request.getSession();
		final CollectionProtocolBean cpBean = (CollectionProtocolBean) newSession
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);

		if (operation == null && cpBean != null
				&& cpBean.getOperation().equals("update"))
		{
			operation = Constants.EDIT;
		}
		else if (operation == null && cpBean == null)
		{
			operation = Constants.ADD;
		}
		//bug 18481 start
        boolean condition1 = (request.getParameter(Constants.ERROR_PAGE_FOR_CP)!=null && !request.getParameter(Constants.ERROR_PAGE_FOR_CP).equals(""));
		boolean condition2 = request.getParameter(Constants.REFRESH_WHOLE_PAGE)!=null && request.getParameter(Constants.REFRESH_WHOLE_PAGE).equals(Constants.FALSE);
		if(condition1 && !condition2)
		{
			invokeFunction = "cp";
		}
		//bug 18481 end
		if (invokeFunction != null && cpBean != null)
		{
			this.initCollectionProtocolPage(request, form, pageOf, mapping);
		}
		if ("add".equals(operation) && invokeFunction == null && !condition1) //bug 18481
		{
			this.initCleanSession(request);
		}


		this.LOGGER.debug("operation in coll protocol action" + operation);
		// Sets the operation attribute to be used in the Edit/View Collection
		// Protocol Page in Advance Search Object View.

		final CollectionProtocolForm collectionProtocolForm = (CollectionProtocolForm) form;
		final String cp_id = String.valueOf(collectionProtocolForm.getId());
		if (!cp_id.equalsIgnoreCase("0"))
		{
			final CollectionProtocol collectionProtocol = this.getCPObj(cp_id);
			// Resolved lazy --- collectionProtocol.getConsentTierCollection();
			ColumnValueBean columnValueBean = new ColumnValueBean(collectionProtocol.getId());
			final Collection consentTierCollection = (Collection) bizLogic.retrieveAttribute(
					CollectionProtocol.class,collectionProtocol.getId(),
					"elements(consentTierCollection)");
			final Map tempMap = this.prepareConsentMap(consentTierCollection);
			collectionProtocolForm.setConsentValues(tempMap);
			collectionProtocolForm.setConsentTierCounter(this.consentCounter);

		}
		if (collectionProtocolForm.getStartDate() == null)
		{
			collectionProtocolForm.setStartDate(CommonUtilities.parseDateToString(Calendar
					.getInstance().getTime(), CommonServiceLocator.getInstance().getDatePattern()));
		}
		// Name of delete button clicked
		final String button = request.getParameter("button");

		// Row number of outerblock
		final String outer = request.getParameter("blockCounter");

		// Gets the map from ActionForm
		final Map map = collectionProtocolForm.getValues();

		// List of keys used in map of ActionForm
		final List key = new ArrayList();
		key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_specimenClass");
		key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_specimenType");
		key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_tissueSite");
		key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_pathologyStatus");
		key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_quantity_value");

		if (button != null)
		{
			if ("deleteSpecimenReq".equals(button))
			{
				MapDataParser.deleteRow(key, map, request.getParameter("status"), outer);
			}
			else
			{
				// keys of outer block
				key.add("CollectionProtocolEvent:outer_clinicalStatus");
				key.add("CollectionProtocolEvent:outer_studyCalendarEventPoint");
				MapDataParser.deleteRow(key, map, request.getParameter("status"));
			}
		}

		final List clinicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_CLINICAL_STATUS, null);
		request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusList);

		this.LOGGER.debug("page of in collectionProtocol action:" + pageOf);
		request.setAttribute("pageOf", pageOf);

		final List tissueSiteList = (List) request.getAttribute(Constants.TISSUE_SITE_LIST);

		final List pathologyStatusList = (List) request
				.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST);
		final List predefinedConsentsList = (List) request
				.getAttribute(Constants.PREDEFINED_CADSR_CONSENTS);
		final String tab = request.getParameter("tab");
		String formName;
		final String pageView = operation;
		String editViewButton = "buttons." + Constants.EDIT;
		String currentCollectionProtocolDate = "";
		String collectionProtocolEndDate = "";
		if (collectionProtocolForm != null)
		{
			currentCollectionProtocolDate = collectionProtocolForm.getStartDate();
			if (currentCollectionProtocolDate == null)
			{
				currentCollectionProtocolDate = "";
			}
			collectionProtocolEndDate = collectionProtocolForm.getEndDate();
			if (collectionProtocolEndDate == null)
			{
				collectionProtocolEndDate = "";
			}
		}
		final String reqPath = (String) request.getAttribute(Constants.REQ_PATH);
		String appendingPath = "/CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol";
		if (reqPath != null)
		{
			appendingPath = reqPath
					+ "|/CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol";
		}
		if (!"add".equals(operation) && collectionProtocolForm != null)
		{

				appendingPath = "/CollectionProtocolSearch.do?operation="
						+ "search&pageOf=pageOfCollectionProtocol&id="
						+ collectionProtocolForm.getId();
		}

		final boolean readOnlyValue = false;
		if (Constants.EDIT.equals(operation))
		{
			editViewButton = "buttons." + Constants.VIEW;
			formName = Constants.COLLECTIONPROTOCOL_EDIT_ACTION;
			if (pageOf.equals(Constants.QUERY))
			{
				formName = Constants.QUERY_COLLECTION_PROTOCOL_EDIT_ACTION + "?pageOf=" + pageOf;
			}

		}
		else
		{
			formName = Constants.COLLECTIONPROTOCOL_ADD_ACTION;
		}
		boolean flagforPageView = false;

		if ("add".equals(pageView) || "edit".equals(pageView))
		{
			flagforPageView = true;
		}
		final Integer collectionProtocolYear = Integer.valueOf(CommonUtilities
				.getYear(currentCollectionProtocolDate));
		final Integer collectionProtocolMonth = Integer.valueOf(CommonUtilities
				.getMonth(currentCollectionProtocolDate));
		final Integer collectionProtocolDay = Integer.valueOf(CommonUtilities
				.getDay(currentCollectionProtocolDate));

		final Integer collectionProtocolEndDateYear =Integer.valueOf(CommonUtilities
				.getYear(collectionProtocolEndDate));
		final Integer collectionProtocolEndDateMonth = Integer.valueOf(CommonUtilities
				.getMonth(collectionProtocolEndDate));
		final Integer collectionProtocolEndDateDay = Integer.valueOf(CommonUtilities
				.getDay(collectionProtocolEndDate));

		request.setAttribute("collectionProtocolYear", collectionProtocolYear);
		request.setAttribute("collectionProtocolDay", collectionProtocolDay);
		request.setAttribute("collectionProtocolMonth", collectionProtocolMonth);

		request.setAttribute("collectionProtocolEndDateYear", collectionProtocolEndDateYear);
		request.setAttribute("collectionProtocolEndDateDay", collectionProtocolEndDateDay);
		request.setAttribute("collectionProtocolEndDateMonth", collectionProtocolEndDateMonth);
		if (cpBean != null)
		{
			request.setAttribute("isParticipantReg", cpBean.isParticiapantReg());
		}

		int noOfConsents = 1;
		noOfConsents = collectionProtocolForm.getConsentTierCounter();

		request.setAttribute("noOfConsents", noOfConsents);

		final String title = "collectionprotocol." + pageView + ".title";

		collectionProtocolForm.setPageOf(pageOf);
		collectionProtocolForm.setOperation(operation);
		collectionProtocolForm.setSubmittedFor(submittedFor);
		collectionProtocolForm.setRedirectTo(reqPath);
		final String fieldWidth = Utility.getColumnWidth(CollectionProtocol.class, "title");
		final String deleteAction = "deleteObject('" + formName + "','" + Constants.ADMINISTRATIVE
				+ "')";
		request.setAttribute("pageOf", pageOf);
		request.setAttribute("operation", operation);
		request.setAttribute("edit", Constants.EDIT);
		request.setAttribute("deleteAction", deleteAction);
		request.setAttribute("fieldWidth", fieldWidth);
		request.setAttribute("queryforJSP", Constants.QUERY);
		request.setAttribute("readOnlyValue", readOnlyValue);
		request.setAttribute("formName", formName);
		request.setAttribute("editViewButton", editViewButton);
		request.setAttribute("appendingPath", appendingPath);
		request.setAttribute("currentCollectionProtocolDate", currentCollectionProtocolDate);
		request.setAttribute("collectionProtocolEndDate", collectionProtocolEndDate);
		request.setAttribute("pageView", pageView);
		request.setAttribute("flagforPageView", flagforPageView);
		request.setAttribute("tab", tab);
		request.setAttribute("tissueSiteList", tissueSiteList);
		request.setAttribute("pathologyStatusList", pathologyStatusList);
		request.setAttribute("predefinedConsentsList", predefinedConsentsList);
		request.setAttribute("title", title);
		request.setAttribute("userListforJSP", Constants.USERLIST);
		request.setAttribute("userListforJSP", Constants.USERLIST);
		request.setAttribute("siteListForJSP", Constants.SITELIST);
		CollectionProtocolUtil.updateClinicalDiagnosis(request, cpBean);
		request.setAttribute("isCOPPAEnabled", COPPAUtil.isCOPPAEnabled());

		return mapping.findForward(pageOf);
	}

	/**
	 * This function will return CollectionProtocolRegistration object.
	 *
	 * @param cp_id
	 *            : cp_id
	 * @return collectionProtocolObject
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private CollectionProtocol getCPObj(final String cp_id) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final CollectionProtocolBizLogic collectionProtocolBizLogic = (CollectionProtocolBizLogic) factory
				.getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);

		final Object object = collectionProtocolBizLogic.retrieve(CollectionProtocol.class
				.getName(), Long.valueOf(cp_id));
		return (CollectionProtocol) object;
	}

	/**
	 * Prepares consent map.
	 * @param consentTierColl
	 *            : consentTierColl
	 * @return Map : Map
	 */
	private Map prepareConsentMap(Collection consentTierColl)
	{
		Map tempMap = null;
		if (consentTierColl != null)
		{
			tempMap = new HashMap();
			final Iterator consentTierCollIter = consentTierColl.iterator();
			int consentCtr = 0;
			while (consentTierCollIter.hasNext())
			{
				final ConsentTier consent = (ConsentTier) consentTierCollIter.next();
				final String statement = "ConsentBean:" + consentCtr + "_statement";
				final String statementkey = "ConsentBean:" + consentCtr + "_consentTierID";
				tempMap.put(statement, consent.getStatement());
				tempMap.put(statementkey, consent.getId());
				consentCtr++;
			}
			this.consentCounter = consentCtr;

		}
		return tempMap;
	}

	/**
	 * Initialise the collection protocol page.
	 * @param request
	 *            : request
	 * @param form
	 *            : form
	 * @param pageOf
	 *            : pageOf
	 * @param mapping
	 *            : mapping
	 * @return ActionForward : ActionForward
	 */
	private ActionForward initCollectionProtocolPage(HttpServletRequest request, ActionForm form,
			String pageOf, ActionMapping mapping)
	{
		final CollectionProtocolForm collectionProtocolForm = (CollectionProtocolForm) form;
		final HttpSession session = request.getSession();
		final CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		collectionProtocolForm.setPrincipalInvestigatorId(collectionProtocolBean
				.getPrincipalInvestigatorId());
		collectionProtocolForm.setIrbSiteId(collectionProtocolBean
				.getIrbSiteId());		
		collectionProtocolForm.setCoordinatorIds(collectionProtocolBean
				.getCoordinatorIds());
		/**For Clinical Diagnosis Subset  **/
		collectionProtocolForm.setProtocolCoordinatorIds(collectionProtocolBean.getClinicalDiagnosis());
		CollectionProtocolUtil.updateClinicalDiagnosis(request, collectionProtocolBean);

		collectionProtocolForm.setTitle(collectionProtocolBean.getTitle());
		collectionProtocolForm.setShortTitle(collectionProtocolBean.getShortTitle());
		collectionProtocolForm.setStartDate(collectionProtocolBean.getStartDate());
		collectionProtocolForm.setConsentWaived(collectionProtocolBean.isConsentWaived());
		collectionProtocolForm.setEnrollment(collectionProtocolBean.getEnrollment());
		collectionProtocolForm.setDescriptionURL(collectionProtocolBean.getDescriptionURL());
		collectionProtocolForm.setIrbID(collectionProtocolBean.getIrbID());
		collectionProtocolForm.setActivityStatus(collectionProtocolBean.getActivityStatus());
		collectionProtocolForm.setEndDate(collectionProtocolBean.getEndDate());
		collectionProtocolForm.setAliqoutInSameContainer(collectionProtocolBean
				.isAliqoutInSameContainer());
//		collectionProtocolForm.setGenerateLabel(collectionProtocolBean.isGenerateLabel());
		collectionProtocolForm.setSpecimenLabelFormat(collectionProtocolBean.getLabelFormat());
		collectionProtocolForm.setDerivativeLabelFormat(collectionProtocolBean.getDerivativeLabelFormat());
		collectionProtocolForm.setAliquotLabelFormat(collectionProtocolBean.getAliquotLabelFormat());
		// For Consent Tab
		collectionProtocolForm
				.setConsentTierCounter(collectionProtocolBean.getConsentTierCounter());
		collectionProtocolForm.setConsentValues(collectionProtocolBean.getConsentValues());
		collectionProtocolForm.setUnsignedConsentURLName(collectionProtocolBean
				.getUnsignedConsentURLName());

		collectionProtocolForm.setSequenceNumber(collectionProtocolBean.getSequenceNumber());
		collectionProtocolForm.setType(collectionProtocolBean.getType());
		collectionProtocolForm.setStudyCalendarEventPoint(collectionProtocolBean
				.getStudyCalendarEventPoint());
		collectionProtocolForm.setParentCollectionProtocolId(collectionProtocolBean
				.getParentCollectionProtocolId());
		
		//Remote Flags
		collectionProtocolForm.setRemoteId(collectionProtocolBean.getRemoteId());
		collectionProtocolForm.setRemoteManagedFlag(collectionProtocolBean.isRemoteManagedFlag());
		collectionProtocolForm.setDirtyEditFlag(collectionProtocolBean.isDirtyEditFlag());
		/*
		  try {
			COPPAServiceClient client = new COPPAServiceClient();
			String coppaProtocolId = "135568";
			Person result = client
					.getPrincipalInvestigatorByStudyProtocolId(coppaProtocolId);
			if (result != null) {
				LOGGER.debug("Principal Investigator for protocol id:"
						+ coppaProtocolId + ":"
						+ COPPAUtil.getFirstName(result) + " "
						+ COPPAUtil.getLastName(result));
				System.out.println("Principal Investigator for protocol id:"
						+ coppaProtocolId + ":"
						+ COPPAUtil.getFirstName(result) + " "
						+ COPPAUtil.getLastName(result));

			} else {
				LOGGER.debug("No Principal Investigator found for protocol id:"
						+ coppaProtocolId);

			}
		} catch (Exception e) {
			LOGGER.error(
					"Exception Occured while calling COPPA:" + e.getMessage(),
					e);
		}
		 */

		return mapping.findForward(pageOf);
	}

	/**
	 * While initialisation cleans the session.
	 * @param request
	 *            : request
	 */
	private void initCleanSession(HttpServletRequest request)
	{
		final HttpSession session = request.getSession();
		session.removeAttribute("tempKey");
		session.removeAttribute(Constants.CLICKED_NODE);
		session.removeAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		session.removeAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
	}
}