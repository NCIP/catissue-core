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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.MSRUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.labelSQLApp.bizlogic.LabelSQLAssociationBizlogic;
import edu.wustl.common.labelSQLApp.domain.LabelSQLAssociation;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.query.generator.ColumnValueBean;


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
	private static transient final Logger LOGGER = Logger
			.getCommonLogger(CollectionProtocolAction.class);
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
		final String nodeDeleted = request.getParameter("nodeDeleted");
		String nodeId = request.getParameter(Constants.TREE_NODE_ID);
		if(nodeId!=null && !Constants.TRUE.equals(nodeDeleted))
			request.getSession().setAttribute(Constants.TREE_NODE_ID,nodeId);
		request.setAttribute("tabSel", tabSel);
		final List<NameValueBean> clinicalDiagnosis = new ArrayList<NameValueBean>();
		request.setAttribute("selectedCoordinators",
				clinicalDiagnosis);
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

		if (operation == null && cpBean != null && cpBean.getOperation().equals("update"))
		{
			operation = Constants.EDIT;
		}
		else if (operation == null && cpBean == null)
		{
			operation = Constants.ADD;
		}
		//bug 18481 start
		boolean condition1 = (request.getParameter(Constants.ERROR_PAGE_FOR_CP) != null && !request
				.getParameter(Constants.ERROR_PAGE_FOR_CP).equals(""));
		boolean condition2 = request.getParameter(Constants.REFRESH_WHOLE_PAGE) != null
				&& request.getParameter(Constants.REFRESH_WHOLE_PAGE).equals(Constants.FALSE);
		if (condition1 && !condition2)
		{
			invokeFunction = "cp";
		}
		//bug 18481 end
		if (invokeFunction != null && cpBean != null)
		{
			this.initCollectionProtocolPage(request, form, pageOf, mapping);
		}
		if (Constants.ADD.equals(operation) && invokeFunction == null && !condition1) //bug 18481
		{
			this.initCleanSession(request);
			final CollectionProtocolForm collectionProtocolForm = (CollectionProtocolForm) form;
			if(!edu.wustl.common.util.global.Constants.DEFAULT_DASHBOARD_ITEMS.isEmpty())
			{
			 collectionProtocolForm.setDashboardLabelJsonValue(CollectionProtocolUtil.populateDashboardLabelJsonValue(null));
			 request.setAttribute("isDefaultDashBoard",true);
			}
			
		}

		this.LOGGER.debug("operation in coll prot action" + operation);
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
					CollectionProtocol.class, collectionProtocol.getId(),
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
		collectionProtocolForm.setType((collectionProtocolForm.getType() == null || "".equals(collectionProtocolForm.getType()))? Constants.CP_TYPE_PARENT : collectionProtocolForm.getType());
		boolean hasParent = false;
		if(collectionProtocolForm.getType() == null || !Constants.CP_TYPE_PARENT.equals(collectionProtocolForm.getType()))
			{
				hasParent = true;
			}
		request.setAttribute("hasParent",hasParent);
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
					+ "search&pageOf=pageOfCollectionProtocol&id=" + collectionProtocolForm.getId();
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

		final Integer collectionProtocolEndDateYear = Integer.valueOf(CommonUtilities
				.getYear(collectionProtocolEndDate));
		final Integer collectionProtocolEndDateMonth = Integer.valueOf(CommonUtilities
				.getMonth(collectionProtocolEndDate));
		final Integer collectionProtocolEndDateDay = Integer.valueOf(CommonUtilities
				.getDay(collectionProtocolEndDate));
		
		CollectionProtocolBizLogic cpBizLogic = new CollectionProtocolBizLogic();
		List<NameValueBean> cpList = cpBizLogic.getAllCPNameValueBeanList();
		//cpList.add(0,new NameValueBean(Constants.SELECT_OPTION,Long.valueOf(Constants.SELECT_OPTION_VALUE)));
		
		final MSRUtil msrUtil = new MSRUtil();
		final String cpOperation = request.getParameter("cpOperation");
		
		request.setAttribute("cpOperation", cpOperation);
		if ("AssignPrivilegePage".equals(cpOperation) || cpOperation == null)
		{
			msrUtil.onFirstTimeLoad(mapping, request);
		}
		else
		{
			msrUtil.setAJAXResponse(request, response, cpOperation);
		}
		
		request.setAttribute(Constants.CP_LIST, cpList);
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
		request.setAttribute(Constants.CP_ASSOCIATION_TYPE_LIST,Constants.CP_ASSOCIATION_TYPE_VALUES);
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
		/*UserBizLogic userBizLogic = new UserBizLogic();
		final Collection userCollection = userBizLogic.getUsers(operation);
		//userCollection.add(new NameValueBean(Constants.SELECT_OPTION,Long.valueOf(Constants.SELECT_OPTION_VALUE)));
		request.setAttribute(Constants.USERLIST, userCollection);*/
		request.setAttribute("userListforJSP", Constants.USERLIST);
		CollectionProtocolUtil.updateClinicalDiagnosis(request, cpBean);
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
	 * @throws Exception 
	 */
	private ActionForward initCollectionProtocolPage(HttpServletRequest request, ActionForm form,
			String pageOf, ActionMapping mapping) throws Exception
	{
		final CollectionProtocolForm collectionProtocolForm = (CollectionProtocolForm) form;
		final HttpSession session = request.getSession();
		final CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		collectionProtocolForm.setParentCollectionProtocolId(collectionProtocolBean.getParentCollectionProtocolId());
		collectionProtocolForm.setParentCollectionProtocol(collectionProtocolBean.getParentCollectionProtocol());
		collectionProtocolForm.setPrincipalInvestigatorId(collectionProtocolBean
				.getPrincipalInvestigatorId());
		collectionProtocolForm.setCoordinatorIds(collectionProtocolBean.getCoordinatorIds());
		Collection coordinatorList = collectionProtocolBean.getCoordinatorCollection();
		List<NameValueBean> selectedCoordinatorsList = new ArrayList<NameValueBean>();
		if(coordinatorList != null)
		{
			Iterator iterator = coordinatorList.iterator();
			while (iterator.hasNext())
			{
				User user = (User) iterator.next();
				NameValueBean nvb = new NameValueBean();
				nvb.setName(AppUtility.populateDisplayName(user.getFirstName(),user.getLastName()));
				nvb.setValue(user.getId().toString());
				selectedCoordinatorsList.add(nvb);
			}
		}
		
		request.setAttribute("selectedCPCoordinatorIds", selectedCoordinatorsList);
		/**For Clinical Diagnosis Subset  **/
		collectionProtocolForm.setProtocolCoordinatorIds(collectionProtocolBean
				.getClinicalDiagnosis());
		//CollectionProtocolUtil.updateCoordinatorIds(request, collectionProtocolBean);
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
		collectionProtocolForm.setDerivativeLabelFormat(collectionProtocolBean
				.getDerivativeLabelFormat());
		collectionProtocolForm
				.setAliquotLabelFormat(collectionProtocolBean.getAliquotLabelFormat());
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
		collectionProtocolForm.setIsEMPIEnable(collectionProtocolBean.getIsEMPIEnable());
		Boolean isDefaultDashboard = false;
		//Retrieving associations from CPId
		List<LabelSQLAssociation> labelSQLAssociations = new LabelSQLAssociationBizlogic()
				.getLabelSQLAssocCollection(collectionProtocolBean
						.getIdentifier());
		if(labelSQLAssociations.isEmpty() && !edu.wustl.common.util.global.Constants.DEFAULT_DASHBOARD_ITEMS.isEmpty())
		{
			isDefaultDashboard = true;
		}
		collectionProtocolForm
				.setDashboardLabelJsonValue(CollectionProtocolUtil.populateDashboardLabelJsonValue(labelSQLAssociations));
		request.setAttribute("isDefaultDashBoard", isDefaultDashboard);
		collectionProtocolForm.setPpidFormat(collectionProtocolBean.getPpidFormat());
		if(!Validator.isEmpty(collectionProtocolBean.getPpidFormat())){
			formPidFormat(collectionProtocolForm,collectionProtocolBean.getPpidFormat());
		}
		return mapping.findForward(pageOf);
	}
	
	private void formPidFormat(CollectionProtocolForm form ,String Format){
		Format = Format.substring(1,(Format.length()-"\",PPID".length()));
		String pattern = "%\\d*d";
	    Pattern p = Pattern.compile(pattern);
	    Matcher m = p.matcher(Format);
	    m.find();
	    String[] strArray = p.split(Format);
	    if(strArray.length==2){
	    	form.setPrefixPid(strArray[0]);
	    	form.setPostfixPid(strArray[1]);
	    	
	    }else if(strArray.length==1){
		    if(m.start()==0){
		    	form.setPostfixPid(strArray[0]);
		    }else{
		    	form.setPrefixPid(strArray[0]);
		    }
		 }
	    String noOfDigit = Format.substring(m.start(),m.end());
	    form.setNoOfDigitPid(noOfDigit.substring(1,(noOfDigit.length()-1)));
		
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
