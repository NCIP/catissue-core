/**
 * <p>
 * Title: NewSpecimenAction Class>
 * <p>
 * Description: NewSpecimenAction initializes the fields in the New Specimen
 * page.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.xmi.AnnotationUtil;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerForSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;

/**
 * @author renuka_bajpai
 *
 */
public class NewSpecimenAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(NewSpecimenAction.class);

	/**
	 * Overrides the execute method of Action class.
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
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		DAO dao = null;
		try
		{
			final SessionDataBean sessionData = (SessionDataBean) request.getSession()
					.getAttribute(Constants.SESSION_DATA);
			dao = AppUtility.openDAOSession(sessionData);

			// Logger.out.debug("NewSpecimenAction start@@@@@@@@@");
			final NewSpecimenForm specimenForm = (NewSpecimenForm) form;
			final List<NameValueBean> storagePositionList = AppUtility.getStoragePositionTypeList();
			request.setAttribute("storageList", storagePositionList);
			String pageOf = request.getParameter(Constants.PAGE_OF);
			final String forwardPage = specimenForm.getForwardTo();

			if (forwardPage.equals(Constants.PAGE_OF_SPECIMEN_COLLECTION_REQUIREMENT_GROUP))
			{
				pageOf = forwardPage;
				request.setAttribute(Constants.STATUS_MESSAGE_KEY,
						"errors.specimencollectionrequirementgroupedit");
				return mapping.findForward(pageOf);
			}
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) factory
					.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);

			final String treeRefresh = request.getParameter("refresh");
			request.setAttribute("refresh", treeRefresh);

			// Gets the value of the operation parameter.
			final String operation = (String) request.getParameter(Constants.OPERATION);
			// boolean to indicate whether the suitable containers to be shown in
			// dropdown
			// is exceeding the max limit.
			final String exceedingMaxLimit = new String();

			// Sets the operation attribute to be used in the Edit/View Specimen
			// Page in Advance Search Object View.
			request.setAttribute(Constants.OPERATION, operation);

			if (operation != null && operation.equalsIgnoreCase(Constants.ADD))
			{
				specimenForm.setId(0);
			}
			final String virtuallyLocated = request.getParameter("virtualLocated");
			if (virtuallyLocated != null && virtuallyLocated.equals("true"))
			{
				specimenForm.setVirtuallyLocated(true);
			}

			// Name of button clicked
			final String button = request.getParameter("button");
			Map map = null;

			if (button != null)
			{
				if (button.equals("deleteExId"))
				{
					final List key = new ArrayList();
					key.add("ExternalIdentifier:i_name");
					key.add("ExternalIdentifier:i_value");

					// Gets the map from ActionForm
					map = specimenForm.getExternalIdentifier();
					MapDataParser.deleteRow(key, map, request.getParameter("status"));
				}
				else
				{
					final List key = new ArrayList();
					key.add("Biohazard:i_type");
					key.add("Biohazard:i_id");

					// Gets the map from ActionForm
					map = specimenForm.getBiohazard();
					MapDataParser.deleteRow(key, map, request.getParameter("status"));
				}
			}

			// ************* ForwardTo implementation *************
			final HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");
			String specimenCollectionGroupName = "";
			if (forwardToHashMap != null)
			{
				final String specimenCollectionGroupId = (String) forwardToHashMap
						.get("specimenCollectionGroupId");
				/** For Migration Start **/

				specimenCollectionGroupName = (String) forwardToHashMap
						.get("specimenCollectionGroupName");
				this.logger
						.debug("specimenCollectionGroupName found in forwardToHashMap========>>>>>>"
								+ specimenCollectionGroupName);
				specimenForm.setSpecimenCollectionGroupName(specimenCollectionGroupName);
				/** For Migration End **/
				this.logger
						.debug("SpecimenCollectionGroupId found in forwardToHashMap========>>>>>>"
								+ specimenCollectionGroupId);

				if (specimenCollectionGroupId != null)
				{
					/**
					 * Retaining properties of specimen when more is clicked. Bug no
					 * -- 2623
					 */
					// Populating the specimen collection group name in the specimen
					// page
					this.setFormValues(specimenForm, specimenCollectionGroupId,
							specimenCollectionGroupName);
				}
			}

			// ************* ForwardTo implementation *************

			// Consent Tracking (Virender Mehta) - Start
			// Adding name,value pair in NameValueBean
			final String tabSelected = request.getParameter(Constants.SELECTED_TAB);
			if (tabSelected != null)
			{
				request.setAttribute(Constants.SELECTED_TAB, tabSelected);
			}
			final String scg_id = String.valueOf(specimenForm.getSpecimenCollectionGroupId());
			final SpecimenCollectionGroup specimenCollectionGroup = AppUtility.getSCGObj(scg_id,
					dao);
			// PHI Data
			String initialURLValue = "";
			String initialWitnessValue = "";
			String initialSignedConsentDateValue = "";
			// Lazy - specimenCollectionGroup.getCollectionProtocolRegistration()
			final CollectionProtocolRegistration collectionProtocolRegistration = specimenCollectionGroup
					.getCollectionProtocolRegistration();
			specimenForm.setGenerateLabel(collectionProtocolRegistration.getCollectionProtocol().getGenerateLabel());
			if (collectionProtocolRegistration == null
					|| collectionProtocolRegistration.getSignedConsentDocumentURL() == null)
			{
				initialURLValue = Constants.NULL;
			}
			User consentWitness = null;
			if (collectionProtocolRegistration.getId() != null)
			{
				final List consentWitnessList = dao.retrieveAttribute(
						(Class) (CollectionProtocolRegistration.class), "id",
						collectionProtocolRegistration.getId(), "consentWitness");
				if ((consentWitnessList != null) && (consentWitnessList.size() > 0))
				{
					consentWitness = (User) consentWitnessList.get(0);
				}
			}
			// User consentWitness=
			// collectionProtocolRegistration.getConsentWitness();
			// Resolved lazy
			if (consentWitness == null)
			{
				initialWitnessValue = Constants.NULL;
			}
			if (collectionProtocolRegistration.getConsentSignatureDate() == null)
			{
				initialSignedConsentDateValue = Constants.NULL;
			}
			final List cprObjectList = new ArrayList();
			cprObjectList.add(collectionProtocolRegistration);
			/*
			 * SessionDataBean sessionDataBean = (SessionDataBean)
			 * request.getSession().getAttribute( Constants.SESSION_DATA);
			 */
			// CaCoreAppServicesDelegator caCoreAppServicesDelegator = new
			// CaCoreAppServicesDelegator();
			// String userName = Utility.toString(sessionDataBean.getUserName());
			/*
			 * List collProtObject =
			 * caCoreAppServicesDelegator.delegateSearchFilter(userName,
			 * cprObjectList);
			 */
			CollectionProtocolRegistration cprObject = null;
			cprObject = collectionProtocolRegistration;

			String witnessName = "";
			String getConsentDate = "";
			String getSignedConsentURL = "";
			User witness = cprObject.getConsentWitness();
			if (witness == null)
			{
				if (initialWitnessValue.equals(Constants.NULL))
				{
					witnessName = "";
				}
				else
				{
					witnessName = Constants.HASHED_OUT;
				}
				specimenForm.setWitnessName(witnessName);
			}
			else
			{
				final List userList = dao.retrieveAttribute(
						(Class) CollectionProtocolRegistration.class, "id", cprObject.getId(),
						"consentWitness");
				if ((userList != null) && (userList.size() > 0))
				{
					witness = (User) userList.get(0);
				}
				final String witnessFullName = witness.getLastName() + ", "
						+ witness.getFirstName();
				specimenForm.setWitnessName(witnessFullName);
			}
			if (cprObject.getConsentSignatureDate() == null)
			{
				if (initialSignedConsentDateValue.equals(Constants.NULL))
				{
					getConsentDate = "";
				}
				else
				{
					getConsentDate = Constants.HASHED_OUT;
				}
			}
			else
			{
				getConsentDate = Utility.parseDateToString(cprObject.getConsentSignatureDate(),
						CommonServiceLocator.getInstance().getDatePattern());
			}

			if (cprObject.getSignedConsentDocumentURL() == null)
			{
				if (initialURLValue.equals(Constants.NULL))
				{
					getSignedConsentURL = "";
				}
				else
				{
					getSignedConsentURL = Constants.HASHED_OUT;
				}
			}
			else
			{
				getSignedConsentURL = Utility.toString(cprObject.getSignedConsentDocumentURL());
			}
			specimenForm.setConsentDate(getConsentDate);
			specimenForm.setSignedConsentUrl(getSignedConsentURL);

			final List consentTierRespCollection = dao.retrieveAttribute(
					CollectionProtocolRegistration.class, "id", cprObject.getId(),
					"elements(consentTierResponseCollection)");
			// Lazy Resolved --- cprObject.getConsentTierResponseCollection()

			List participantResponseList;

			if (consentTierRespCollection != null)
			{
				participantResponseList = new ArrayList(consentTierRespCollection);
			}
			else
			{
				participantResponseList = new ArrayList();
			}

			if (operation.equalsIgnoreCase(Constants.ADD))
			{
				final ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
				if (errors == null)
				{
					final String scgDropDown = request.getParameter(Constants.SCG_DROPDOWN);
					if (scgDropDown == null || scgDropDown.equalsIgnoreCase(Constants.TRUE))
					{
						final Collection consentResponseStatuslevel = (Collection) dao
								.retrieveAttribute((Class) SpecimenCollectionGroup.class, "id",
										specimenCollectionGroup.getId(),
										"elements(consentTierStatusCollection)");
						final Map tempMap = this.prepareConsentMap(participantResponseList,
								consentResponseStatuslevel);
						specimenForm.setConsentResponseForSpecimenValues(tempMap);
					}
				}
				specimenForm.setConsentTierCounter(participantResponseList.size());
			}
			else
			{
				String specimenID = null;
				specimenID = String.valueOf(specimenForm.getId());
				// List added for grid
				final List specimenDetails = new ArrayList();

				final Specimen specimen = bizLogic.getSpecimen(specimenID, specimenDetails,
						sessionData, dao);
				if(specimen != null && specimen.getIsAvailable() != null)
				{
					specimenForm.setAvailable(specimen.getIsAvailable());
				}
				// Added by Falguni=To set Specimen label in Form.
				/*
				 * Bug id = 11480Resolved by : Himanshu Aseeja
				 */
				final List columnList = this.columnNames();
				final String consentResponseHql = "select elements(scg.collectionProtocolRegistration."
						+ "consentTierResponseCollection)"
						+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg,"
						+ " edu.wustl.catissuecore.domain.Specimen as spec "
						+ " where spec.specimenCollectionGroup.id=scg.id and spec.id="
						+ specimen.getId();
				final Collection consentResponse = dao.executeQuery(consentResponseHql);

				final List consentResponseStatuslevel = dao.retrieveAttribute(Specimen.class, "id",
						specimen.getId(), "elements(consentTierStatusCollection)");
				final String specimenResponse = "_specimenLevelResponse";
				final String specimenResponseId = "_specimenLevelResponseID";
				final Map tempMap = ConsentUtil.prepareSCGResponseMap(consentResponseStatuslevel,
						consentResponse, specimenResponse, specimenResponseId);
				specimenForm.setConsentResponseForSpecimenValues(tempMap);
				specimenForm.setConsentTierCounter(participantResponseList.size());
				final HttpSession session = request.getSession();
				request.setAttribute("showContainer", specimen.getCollectionStatus());
				session.setAttribute(Constants.SPECIMEN_LIST, specimenDetails);
				session.setAttribute(Constants.COLUMNLIST, columnList);
			}
			List specimenResponseList = new ArrayList();
			specimenResponseList = AppUtility.responceList(operation);
			request.setAttribute(Constants.SPECIMEN_RESPONSELIST, specimenResponseList);
			// Consent Tracking (Virender Mehta) - Stop

			pageOf = request.getParameter(Constants.PAGE_OF);
			request.setAttribute(Constants.PAGE_OF, pageOf);

			// Sets the activityStatusList attribute to be used in the Site Add/Edit
			// Page.
			request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);
			// Sets the collectionStatusList attribute to be used in the Site
			// Add/Edit Page.
			request.setAttribute(Constants.COLLECTIONSTATUSLIST,
					Constants.SPECIMEN_COLLECTION_STATUS_VALUES);

			// NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic)
			// BizLogicFactory.getInstance
			// ().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);

			if (specimenForm.isParentPresent())// If parent specimen is present then
			{
				// String[] fields = {Constants.SYSTEM_LABEL};
				// List parentSpecimenList =
				// bizLogic.getList(Specimen.class.getName(), fields,
				// Constants.SYSTEM_IDENTIFIER, true);
				// request.setAttribute(Constants.PARENT_SPECIMEN_ID_LIST,
				// parentSpecimenList);
			}

			String[] bhIdArray = {"-1"};
			String[] bhTypeArray = {Constants.SELECT_OPTION};
			String[] bhNameArray = {Constants.SELECT_OPTION};

			final String[] selectColNames = {Constants.SYSTEM_IDENTIFIER, "name", "type"};
			List biohazardList = dao.retrieve(Biohazard.class.getName(), selectColNames);
			final Iterator iterator = biohazardList.iterator();

			// Creating & setting the biohazard name, id & type list
			if (biohazardList != null && !biohazardList.isEmpty())
			{
				bhIdArray = new String[biohazardList.size() + 1];
				bhTypeArray = new String[biohazardList.size() + 1];
				bhNameArray = new String[biohazardList.size() + 1];

				bhIdArray[0] = "-1";
				bhTypeArray[0] = "";
				bhNameArray[0] = Constants.SELECT_OPTION;

				int i = 1;

				while (iterator.hasNext())
				{
					final Object[] hazard = (Object[]) iterator.next();
					bhIdArray[i] = String.valueOf(hazard[0]);
					bhNameArray[i] = (String) hazard[1];
					bhTypeArray[i] = (String) hazard[2];
					i++;
				}
			}

			request.setAttribute(Constants.BIOHAZARD_NAME_LIST, bhNameArray);
			request.setAttribute(Constants.BIOHAZARD_ID_LIST, bhIdArray);
			request.setAttribute(Constants.BIOHAZARD_TYPES_LIST, bhTypeArray);

			/**
			 * Name: Chetan Patil Reviewer: Sachin Lale Bug ID: Bug#3184 Patch ID:
			 * Bug#3184_1 Also See: 2-6 Description: Here the older code has been
			 * integrated again inorder to restrict the specimen values based on
			 * requirements of study calendar event point. Two method are written to
			 * separate the code. Method populateAllRestrictedLists() will populate
			 * the values for the lists form Specimen Requirements whereas, method
			 * populateAllLists() will populate the values of the list form the
			 * system.
			 */
			// Setting Secimen Collection Group
			/** For Migration Start **/
			// initializeAndSetSpecimenCollectionGroupIdList(bizLogic,request);
			/** For Migration Start **/
			/**
			 * Patch ID: Bug#4245_2
			 */
			final List<NameValueBean> specimenClassList = new ArrayList<NameValueBean>();
			final List<NameValueBean> specimenTypeList = new ArrayList<NameValueBean>();
			final List<NameValueBean> tissueSiteList = new ArrayList<NameValueBean>();
			final List<NameValueBean> tissueSideList = new ArrayList<NameValueBean>();
			final List<NameValueBean> pathologicalStatusList = new ArrayList<NameValueBean>();
			final Map<String, List<NameValueBean>> subTypeMap = new HashMap<String, List<NameValueBean>>();

			final String specimenCollectionGroupId = specimenForm.getSpecimenCollectionGroupId();
			if (specimenCollectionGroupId != null && !specimenCollectionGroupId.equals(""))
			{ // If specimen is being added form specimen collection group page or a
				// specimen is being edited.

				if (Constants.ALIQUOT.equals(specimenForm.getLineage()))
				{
					this.populateListBoxes(specimenForm, request);
				}

				this.populateAllLists(specimenForm, specimenClassList, specimenTypeList,
						tissueSiteList, tissueSideList, pathologicalStatusList, subTypeMap);

			}
			else
			{ // On adding a new specimen independently.
				this.populateAllLists(specimenForm, specimenClassList, specimenTypeList,
						tissueSiteList, tissueSideList, pathologicalStatusList, subTypeMap);
			}

			/**
			 * Name : Virender Mehta Reviewer: Sachin Lale Bug ID:
			 * defaultValueConfiguration_BugID Patch
			 * ID:defaultValueConfiguration_BugID_10 See
			 * also:defaultValueConfiguration_BugID_9,11 Description: Configuration
			 * of default value for TissueSite, TissueSite, PathologicalStatus Note
			 * by Chetan: Value setting for TissueSite and PathologicalStatus has
			 * been moved into the method populateAllLists().
			 */
			// Setting the default values
			if (specimenForm.getTissueSide() == null || specimenForm.getTissueSide().equals("-1"))
			{
				specimenForm.setTissueSide((String) DefaultValueManager
						.getDefaultValue(Constants.DEFAULT_TISSUE_SIDE));
			}

			// sets the Specimen Class list
			request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
			// sets the Specimen Type list
			request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
			// sets the Tissue Site list
			request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteList);
			// sets the PathologicalStatus list
			request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, pathologicalStatusList);
			// sets the Side list
			request.setAttribute(Constants.TISSUE_SIDE_LIST, tissueSideList);
			// set the map of subtype
			request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);

			// Setting biohazard list
			biohazardList = CDEManager.getCDEManager().getPermissibleValueList(
					Constants.CDE_NAME_BIOHAZARD, null);
			request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);

			/**
			 * Name : Ashish Gupta Reviewer Name : Sachin Lale Bug ID: 2741 Patch
			 * ID: 2741_12 Description: Propagating events from scg to multiple
			 * specimens
			 */
			if (operation.equals(Constants.ADD))
			{
				populateEventsFromScg(specimenCollectionGroup, specimenForm);
			}
			final Object scgForm = request.getAttribute("scgForm");
			if (scgForm != null)
			{
				// SpecimenCollectionGroupForm specimenCollectionGroupForm =
				// (SpecimenCollectionGroupForm) scgForm;

			}
			else
			{
				// Mandar : 10-July-06 AutoEvents : CollectionEvent
				this.setCollectionEventRequestParameters(request, specimenForm);

				// Mandar : 11-July-06 AutoEvents : ReceivedEvent
				this.setReceivedEventRequestParameters(request, specimenForm);

				// Mandar : set default date and time too event fields
				this.setDateParameters(specimenForm);
			}
			// ---- chetan 15-06-06 ----
			final StorageContainerForSpecimenBizLogic scbizLogic = new StorageContainerForSpecimenBizLogic();
			TreeMap containerMap = new TreeMap();
			List initialValues = null;

			if (operation.equals(Constants.ADD))
			{

				if (specimenForm.getLabel() == null || specimenForm.getLabel().equals(""))
				{
					// int totalNoOfSpecimen =
					// bizLogic.totalNoOfSpecimen(sessionData)+1;
					/**
					 * Name : Virender Mehta Reviewer: Sachin Lale Description:
					 * By getting instance of AbstractSpecimenGenerator abstract
					 * class current label retrived and set.
					 */
				}

				if (specimenForm.getSpecimenCollectionGroupName() != null
						&& !specimenForm.getSpecimenCollectionGroupName().equals("")
						&& specimenForm.getClassName() != null
						&& !specimenForm.getClassName().equals("")
						&& !specimenForm.getClassName().equals("-1"))
				{
					final List spCollGroupList = this.getSpCollGroupList(specimenForm
							.getSpecimenCollectionGroupName(), dao);
					if (spCollGroupList != null && !spCollGroupList.isEmpty())
					{
						final long cpId = ((Long) spCollGroupList.get(0)).longValue();
						final String spClass = specimenForm.getClassName();
						final String spType = specimenForm.getType();
						this.logger.info("cpId :" + cpId + "spClass:" + spClass);
						request.setAttribute(Constants.COLLECTION_PROTOCOL_ID, cpId + "");
						if (virtuallyLocated != null && virtuallyLocated.equals("false"))
						{
							specimenForm.setVirtuallyLocated(false);
						}

						if (specimenForm.getStContSelection() == 2)
						{
							containerMap = scbizLogic.getAllocatedContainerMapForSpecimen
							(AppUtility.setparameterList(cpId,spClass,0,spType),sessionData, dao);
							this.logger
									.debug("finish ---calling getAllocatedContaienrMapForSpecimen() function from NewSpecimenAction---");
							ActionErrors errors = (ActionErrors) request
									.getAttribute(Globals.ERROR_KEY);
							if (containerMap.isEmpty())
							{

								if (errors == null || errors.size() == 0)
								{
									errors = new ActionErrors();
								}
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"storageposition.not.available"));
								this.saveErrors(request, errors);
							}
							this.logger
									.debug("calling checkForInitialValues() function from NewSpecimenAction---");
							if (errors == null || errors.size() == 0)
							{
								initialValues = StorageContainerUtil
										.checkForInitialValues(containerMap);
							}
							else
							{
								final String[] startingPoints = new String[3];
								startingPoints[0] = specimenForm.getStorageContainer();
								startingPoints[1] = specimenForm.getPositionDimensionOne();
								startingPoints[2] = specimenForm.getPositionDimensionTwo();
								initialValues = new Vector();
								initialValues.add(startingPoints);
							}
							this.logger
									.debug("finish ---calling"
											+ " checkForInitialValues() function from NewSpecimenAction---");
						}
					}
				}
			}
			else
			{
				containerMap = new TreeMap();
				final String[] startingPoints = new String[]{"-1", "-1", "-1"};

				this.logger.info("--------------container:" + specimenForm.getStorageContainer());
				this.logger.info("--------------pos1:" + specimenForm.getPositionDimensionOne());
				this.logger.info("--------------pos2:" + specimenForm.getPositionDimensionTwo());

				if (specimenForm.getStorageContainer() != null
						&& !specimenForm.getStorageContainer().equals(""))
				{
					final Integer id = new Integer(specimenForm.getStorageContainer());
					String parentContainerName = "";

					Object object = null;
					if (id > 0)
					{
						object = dao.retrieveById(StorageContainer.class.getName(), new Long(
								specimenForm.getStorageContainer()));
					}
					if (object != null)
					{
						final StorageContainer container = (StorageContainer) object;
						parentContainerName = container.getName();

					}
					final Integer pos1 = new Integer(specimenForm.getPositionDimensionOne());
					final Integer pos2 = new Integer(specimenForm.getPositionDimensionTwo());

					final List pos2List = new ArrayList();
					pos2List.add(new NameValueBean(pos2, pos2));

					final Map pos1Map = new TreeMap();
					pos1Map.put(new NameValueBean(pos1, pos1), pos2List);
					containerMap.put(new NameValueBean(parentContainerName, id), pos1Map);

					if (specimenForm.getStorageContainer() != null
							&& !specimenForm.getStorageContainer().equals("-1"))
					{
						startingPoints[0] = specimenForm.getStorageContainer();

					}
					if (specimenForm.getPositionDimensionOne() != null
							&& !specimenForm.getPositionDimensionOne().equals("-1"))
					{
						startingPoints[1] = specimenForm.getPositionDimensionOne();
					}
					if (specimenForm.getPositionDimensionTwo() != null
							&& !specimenForm.getPositionDimensionTwo().equals("-1"))
					{
						startingPoints[2] = specimenForm.getPositionDimensionTwo();
					}
				}
				initialValues = new Vector();
				this.logger.info("Starting points[0]" + startingPoints[0]);
				this.logger.info("Starting points[1]" + startingPoints[1]);
				this.logger.info("Starting points[2]" + startingPoints[2]);
				initialValues.add(startingPoints);

				final String showContainer = (String) request.getAttribute("showContainer");
				if (showContainer == null || showContainer.equals("Pending"))
				{
					if ((specimenForm.getStContSelection() == Constants.RADIO_BUTTON_FOR_MAP || specimenForm
							.getStContSelection() == 2))
					{

						final List spCollGroupList = this.getSpCollGroupList(specimenForm
								.getSpecimenCollectionGroupName(), dao);

						if (spCollGroupList != null && !spCollGroupList.isEmpty())
						{

							final long cpId = ((Long) spCollGroupList.get(0)).longValue();
							final String spClass = specimenForm.getClassName();
							final String spType = specimenForm.getType();
							logger.info("cpId :" + cpId + "spClass:" + spClass);
							request.setAttribute(Constants.COLLECTION_PROTOCOL_ID, cpId + "");
							if (virtuallyLocated != null && virtuallyLocated.equals("false"))
							{
								specimenForm.setVirtuallyLocated(false);
							}

							containerMap = scbizLogic.getAllocatedContainerMapForSpecimen(AppUtility.
								setparameterList(cpId,spClass,0,spType), sessionData, dao);
							logger.debug("finish ---calling getAllocatedContaienrMapForSpecimen() function from NewSpecimenAction---");
							ActionErrors errors = (ActionErrors) request
									.getAttribute(Globals.ERROR_KEY);
							if (containerMap.isEmpty())
							{
								if (specimenForm.getSelectedContainerName() == null
										|| "".equals(specimenForm.getSelectedContainerName()))
								{
									if (errors == null || errors.size() == 0)
									{
										errors = new ActionErrors();
									}
									errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
											"storageposition.not." + "available"));
									this.saveErrors(request, errors);
								}
							}
							Logger.out.debug("calling checkForInitialValues()"
									+ " function from NewSpecimenAction---");
							if (errors == null || errors.size() == 0)
							{
								initialValues = StorageContainerUtil
										.checkForInitialValues(containerMap);
							}
							else
							{
								final String[] startingPoints1 = new String[3];
								startingPoints1[0] = specimenForm.getStorageContainer();
								startingPoints1[1] = specimenForm.getPositionDimensionOne();
								startingPoints1[2] = specimenForm.getPositionDimensionTwo();
								initialValues = new Vector();
								initialValues.add(startingPoints1);

							}

							if (spClass != null
									&& specimenForm.getStContSelection() == Constants.RADIO_BUTTON_FOR_MAP)
							{
								final String[] startingPoints2 = new String[]{"-1", "-1", "-1"};
								initialValues = new ArrayList();
								initialValues.add(startingPoints2);
								request.setAttribute("initValues", initialValues);
							}
						}
					}
				}
			}
			request.setAttribute("initValues", initialValues);
			request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
			request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
			// -------------------------
			// Falguni:Performance Enhancement.
			Long specimenEntityId = null;
			/*if (CatissueCoreCacheManager.getInstance().getObjectFromCache("specimenEntityId") != null)
			{
				specimenEntityId = (Long) CatissueCoreCacheManager.getInstance()
						.getObjectFromCache("specimenEntityId");
			}
			else
			{
				specimenEntityId = AnnotationUtil
						.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN);
				CatissueCoreCacheManager.getInstance().addObjectToCache("specimenEntityId",
						specimenEntityId);
			}

			request.setAttribute("specimenEntityId", specimenEntityId);*/
			if (CatissueCoreCacheManager.getInstance().getObjectFromCache(
					AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID) != null)
			{
				specimenEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache(
						AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID);
			}
			else
			{
				specimenEntityId = AnnotationUtil
						.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY);
				CatissueCoreCacheManager.getInstance().addObjectToCache(
						AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID, specimenEntityId);
			}
			request.setAttribute(AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID, specimenEntityId);

			if (specimenForm.isVirtuallyLocated())
			{
				request.setAttribute("disabled", "true");
			}

			// set associated identified report id
			Long reportId = this.getAssociatedIdentifiedReportId(specimenForm.getId(), dao);
			if (reportId == null)
			{
				reportId = new Long(-1);
			}
			else if (AppUtility.isQuarantined(reportId))
			{
				reportId = new Long(-2);
			}
			final HttpSession session = request.getSession();
			session.setAttribute(Constants.IDENTIFIED_REPORT_ID, reportId);
			// Logger.out.debug("End of specimen action");

			request.setAttribute("createdDate", specimenForm.getCreatedDate());
			request.setAttribute("specimenActivityStatus",
					Constants.SPECIMEN_ACTIVITY_STATUS_VALUES);
			return mapping.findForward(pageOf);

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
	 * This method populates the list boxes for type, tissue site, tissue side
	 * and pathological status if this specimen is an aliquot.
	 *
	 * @param specimenForm
	 *            object of NewSpecimenForm
	 * @param request
	 *            object of HttpServletRequest
	 */
	private void populateListBoxes(NewSpecimenForm specimenForm, HttpServletRequest request)
	{
		// Setting the specimen type list
		NameValueBean bean = new NameValueBean(specimenForm.getType(), specimenForm.getType());
		final List specimenTypeList = new ArrayList();
		specimenTypeList.add(bean);
		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);

		// Setting tissue site list
		bean = new NameValueBean(specimenForm.getTissueSite(), specimenForm.getTissueSite());
		final List tissueSiteList = new ArrayList();
		tissueSiteList.add(bean);
		request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteList);

		// Setting tissue side list
		bean = new NameValueBean(specimenForm.getTissueSide(), specimenForm.getTissueSide());
		final List tissueSideList = new ArrayList();
		tissueSideList.add(bean);
		request.setAttribute(Constants.TISSUE_SIDE_LIST, tissueSideList);

		// Setting pathological status list
		bean = new NameValueBean(specimenForm.getPathologicalStatus(), specimenForm
				.getPathologicalStatus());
		final List pathologicalStatusList = new ArrayList();
		pathologicalStatusList.add(bean);
		request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, pathologicalStatusList);
	}

	// Mandar AutoEvents CollectionEvent start
	/**
	 * This method sets all the collection event parameters for the
	 * SpecimenEventParameter pages.
	 *
	 * @param request
	 *            HttpServletRequest instance in which the data will be set.
	 * @param specimenForm
	 *            NewSpecimenForm instance
	 * @throws Exception
	 *             Throws Exception. Helps in handling exceptions at one common
	 *             point.
	 */
	private void setCollectionEventRequestParameters(HttpServletRequest request,
			NewSpecimenForm specimenForm) throws Exception
	{
		// Gets the value of the operation parameter.
		final String operation = request.getParameter(Constants.OPERATION);

		// Sets the operation attribute to be used in the Add/Edit
		// FrozenEventParameters Page.
		request.setAttribute(Constants.OPERATION, operation);

		// Sets the minutesList attribute to be used in the Add/Edit
		// FrozenEventParameters Page.
		request.setAttribute(Constants.MINUTES_LIST, Constants.MINUTES_ARRAY);

		// Sets the hourList attribute to be used in the Add/Edit
		// FrozenEventParameters Page.
		request.setAttribute(Constants.HOUR_LIST, Constants.HOUR_ARRAY);

		// //The id of specimen of this event.
		// String specimenId = request.getParameter(Constants.SPECIMEN_ID);
		// request.setAttribute(Constants.SPECIMEN_ID, specimenId);
		// Logger.out.debug(
		// "\t\t SpecimenEventParametersAction************************************ : "
		// +specimenId );
		//
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final UserBizLogic userBizLogic = (UserBizLogic) factory
				.getBizLogic(Constants.USER_FORM_ID);
		final Collection userCollection = userBizLogic.getUsers(operation);

		request.setAttribute(Constants.USERLIST, userCollection);

		final SessionDataBean sessionData = this.getSessionData(request);
		if (sessionData != null)
		{
			final String user = sessionData.getLastName() + ", " + sessionData.getFirstName();
			final long collectionEventUserId = EventsUtil.getIdFromCollection(userCollection, user);

			if (specimenForm.getCollectionEventUserId() == 0)
			{
				specimenForm.setCollectionEventUserId(collectionEventUserId);
			}
			if (specimenForm.getReceivedEventUserId() == 0)
			{
				specimenForm.setReceivedEventUserId(collectionEventUserId);
			}
		}

		/**
		 * Name : Virender Mehta Reviewer: Sachin Lale Bug ID:
		 * defaultValueConfiguration_BugID Patch
		 * ID:defaultValueConfiguration_BugID_11 See
		 * also:defaultValueConfiguration_BugID_9,10,11 Description:
		 * Configuration for default value for Collection Procedure, Container
		 * and Quality
		 */

		// set the procedure lists
		final List procedureList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
		request.setAttribute(Constants.PROCEDURE_LIST, procedureList);
		// Bug- setting the default collection event procedure
		if (specimenForm.getCollectionEventCollectionProcedure() == null)
		{
			specimenForm.setCollectionEventCollectionProcedure((String) DefaultValueManager
					.getDefaultValue(Constants.DEFAULT_COLLECTION_PROCEDURE));
		}

		// set the container lists
		final List containerList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_CONTAINER, null);
		request.setAttribute(Constants.CONTAINER_LIST, containerList);
		// Bug- setting the default collection event container
		if (specimenForm.getCollectionEventContainer() == null)
		{
			specimenForm.setCollectionEventContainer((String) DefaultValueManager
					.getDefaultValue(Constants.DEFAULT_CONTAINER));
		}

	}

	// Mandar AutoEvents CollectionEvent end

	// Mandar Autoevents ReceivedEvent start
	/**
	 * This method sets all the received event parameters for the
	 * SpecimenEventParameter pages.
	 *
	 * @param request
	 *            HttpServletRequest instance in which the data will be set.
	 * @param specimenForm
	 *            NewSpecimenForm instance
	 * @throws Exception
	 *             Throws Exception. Helps in handling exceptions at one common
	 *             point.
	 */
	private void setReceivedEventRequestParameters(HttpServletRequest request,
			NewSpecimenForm specimenForm) throws Exception
	{

		final List qualityList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_RECEIVED_QUALITY, null);
		request.setAttribute(Constants.RECEIVED_QUALITY_LIST, qualityList);
		// Bug- setting the default recieved event quality
		if (specimenForm.getReceivedEventReceivedQuality() == null)
		{
			specimenForm.setReceivedEventReceivedQuality((String) DefaultValueManager
					.getDefaultValue(Constants.DEFAULT_RECEIVED_QUALITY));
		}
	}

	/**
	 * @param specimenForm
	 *            instance of NewSpecimenForm
	 */
	private void setDateParameters(NewSpecimenForm specimenForm)
	{
		// set the current Date and Time for the event.
		final Calendar cal = Calendar.getInstance();
		// Collection Event fields
		if (specimenForm.getCollectionEventdateOfEvent() == null)
		{
			specimenForm.setCollectionEventdateOfEvent(Utility.parseDateToString(cal.getTime(),
					CommonServiceLocator.getInstance().getDatePattern()));
		}
		if (specimenForm.getCollectionEventTimeInHours() == null)
		{
			specimenForm.setCollectionEventTimeInHours(Integer.toString(cal
					.get(Calendar.HOUR_OF_DAY)));
		}
		if (specimenForm.getCollectionEventTimeInMinutes() == null)
		{
			specimenForm
					.setCollectionEventTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
		}

		// ReceivedEvent Fields
		if (specimenForm.getReceivedEventDateOfEvent() == null)
		{
			specimenForm.setReceivedEventDateOfEvent(Utility.parseDateToString(cal.getTime(),
					CommonServiceLocator.getInstance().getDatePattern()));
		}
		if (specimenForm.getReceivedEventTimeInHours() == null)
		{
			specimenForm.setReceivedEventTimeInHours(Integer
					.toString(cal.get(Calendar.HOUR_OF_DAY)));
		}
		if (specimenForm.getReceivedEventTimeInMinutes() == null)
		{
			specimenForm.setReceivedEventTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
		}

	}

	/**
	 * @param specimenForm
	 *            instance of NewSpecimenForm
	 */
	/*
	 * private void clearCollectionEvent(NewSpecimenForm specimenForm) {
	 * specimenForm.setCollectionEventCollectionProcedure("");
	 * specimenForm.setCollectionEventComments("");
	 * specimenForm.setCollectionEventContainer("");
	 * specimenForm.setCollectionEventdateOfEvent("");
	 * specimenForm.setCollectionEventTimeInHours("");
	 * specimenForm.setCollectionEventTimeInMinutes("");
	 * specimenForm.setCollectionEventUserId(-1); }
	 */

	/**
	 * @param specimenForm
	 *            instance of NewSpecimenForm
	 */
	/*
	 * private void clearReceivedEvent(NewSpecimenForm specimenForm) {
	 * specimenForm.setReceivedEventComments("");
	 * specimenForm.setReceivedEventDateOfEvent("");
	 * specimenForm.setReceivedEventReceivedQuality("");
	 * specimenForm.setReceivedEventTimeInHours("");
	 * specimenForm.setReceivedEventTimeInMinutes("");
	 * specimenForm.setReceivedEventUserId(-1); }
	 */

	/**
	 *
	 * @param participantResponseList : participantResponseList
	 * @param consentResponse : consentResponse
	 * @return Map : Map
	 */
	private Map prepareConsentMap(List participantResponseList, Collection consentResponse)
	{
		final Map tempMap = new HashMap();
		Long consentTierID;
		Long consentID;
		if (participantResponseList != null || consentResponse != null)
		{
			int i = 0;
			final Iterator consentResponseCollectionIter = participantResponseList.iterator();
			while (consentResponseCollectionIter.hasNext())
			{
				final ConsentTierResponse consentTierResponse = (ConsentTierResponse) consentResponseCollectionIter
						.next();
				final Iterator statusCollectionIter = consentResponse.iterator();
				consentTierID = consentTierResponse.getConsentTier().getId();
				while (statusCollectionIter.hasNext())
				{
					final ConsentTierStatus consentTierstatus = (ConsentTierStatus) statusCollectionIter
							.next();
					consentID = consentTierstatus.getConsentTier().getId();
					if (consentTierID.longValue() == consentID.longValue())
					{
						final ConsentTier consent = consentTierResponse.getConsentTier();
						final String idKey = "ConsentBean:" + i + "_consentTierID";
						final String statementKey = "ConsentBean:" + i + "_statement";
						final String responseKey = "ConsentBean:" + i + "_participantResponse";
						final String participantResponceIdKey = "ConsentBean:" + i
								+ "_participantResponseID";
						final String scgResponsekey = "ConsentBean:" + i
								+ "_specimenCollectionGroupLevelResponse";
						final String scgResponseIDkey = "ConsentBean:" + i
								+ "_specimenCollectionGroupLevelResponseID";
						final String specimenResponsekey = "ConsentBean:" + i
								+ "_specimenLevelResponse";
						final String specimenResponseIDkey = "ConsentBean:" + i
								+ "_specimenLevelResponseID";

						tempMap.put(idKey, consent.getId());
						tempMap.put(statementKey, consent.getStatement());
						tempMap.put(responseKey, consentTierResponse.getResponse());
						tempMap.put(participantResponceIdKey, consentTierResponse.getId());
						tempMap.put(scgResponsekey, consentTierstatus.getStatus());
						tempMap.put(scgResponseIDkey, consentTierstatus.getId());
						tempMap.put(specimenResponsekey, consentTierstatus.getStatus());
						tempMap.put(specimenResponseIDkey, null);
						i++;
						break;
					}
				}
			}
			return tempMap;
		}
		else
		{
			return null;
		}
	}

	// Consent Tracking (Virender Mehta)

	/**
	 * @param specimenForm
	 *            instance of NewSpecimenForm
	 * @param specimenCollectionGroupId
	 *            String containing specimen collection group Id
	 * @param specimenCollectionGroupName
	 *            String containing specimen collection group name
	 */
	private void setFormValues(NewSpecimenForm specimenForm, String specimenCollectionGroupId,
			String specimenCollectionGroupName)
	{
		specimenForm.setSpecimenCollectionGroupId(specimenCollectionGroupId);
		specimenForm.setSpecimenCollectionGroupName(specimenCollectionGroupName);
		specimenForm.setVirtuallyLocated(true);
		specimenForm.setParentSpecimenId("");
		specimenForm.setLabel("");
		specimenForm.setBarcode("");
		specimenForm.setPositionInStorageContainer("");
		specimenForm.setPositionDimensionOne("");
		specimenForm.setPositionDimensionTwo("");
		specimenForm.setStorageContainer("");

	}

	/**
	 * Patch ID: Bug#3184_2
	 */
	/**
	 * This method initializes the List of SpecimenCollectionGroup in the system
	 * and sets the list as an attribute in the request.
	 *
	 * @param bizLogic
	 *            NewSpecimenBizLogic to fetch the SpecimenCollectionGroup list
	 * @param request
	 *            HttpServletRequest in which the list is set as an attribute
	 * @throws DAOException
	 *             on failure to initialize the list
	 */
	/** For Migration Start **/
	/*
	 * private void
	 * initializeAndSetSpecimenCollectionGroupIdList(NewSpecimenBizLogic
	 * bizLogic, HttpServletRequest request) throws DAOException { String
	 * sourceObjectName = SpecimenCollectionGroup.class.getName(); String[]
	 * displayNameFields = {"name"}; String valueField =
	 * Constants.SYSTEM_IDENTIFIER; List specimenCollectionGroupList =
	 * bizLogic.getList(sourceObjectName, displayNameFields, valueField, true);
	 * request.setAttribute(Constants.SPECIMEN_COLLECTION_GROUP_LIST,
	 * specimenCollectionGroupList); }
	 */
	/**
	 * This method generates Map of SpecimenClass and the List of the
	 * corresponding Types.
	 *
	 * @param tempMap
	 *            a temporary Map for avoiding duplication of values.
	 * @param subTypeMap
	 *            the Map of SpecimenClass and the List of the corresponding
	 *            Types
	 * @param specimenClass
	 *            Class of Speciment
	 * @param specimenType
	 *            Type of Specimen
	 */
	/*
	 * private void populateSpecimenTypeLists(Map < String , String > tempMap,
	 * Map < String , List < NameValueBean >> subTypeMap, String specimenClass,
	 * String specimenType) { List < NameValueBean > tempList =
	 * subTypeMap.get(specimenClass); if (tempList == null) { tempList = new
	 * ArrayList < NameValueBean >(); tempList.add(new
	 * NameValueBean(Constants.SELECT_OPTION, "-1")); tempList.add(new
	 * NameValueBean(specimenType, specimenType)); subTypeMap.put(specimenClass,
	 * tempList); tempMap.put(specimenClass + specimenType +
	 * Constants.SPECIMEN_TYPE, specimenType); } else { tempList =
	 * subTypeMap.get(specimenClass); tempList.add(new
	 * NameValueBean(specimenType, specimenType)); Collections.sort(tempList);
	 * subTypeMap.put(specimenClass, tempList); tempMap.put(specimenClass +
	 * specimenType + Constants.SPECIMEN_TYPE, specimenType); } }
	 */

	/**
	 * This method populates all the values form the system for the respective
	 * lists.
	 *
	 * @param specimenForm
	 *            NewSpecimenForm to set the List of TissueSite and
	 *            PathologicalStatus
	 * @param specimenClassList
	 *            List of Specimen Class
	 * @param specimenTypeList
	 *            List of Specimen Type
	 * @param tissueSiteList
	 *            List of Tissue Site
	 * @param tissueSideList
	 *            List of Tissue Side
	 * @param pathologicalStatusList
	 *            List of Pathological Status
	 * @param subTypeMap
	 *            Map of the Class and their corresponding Types
	 * @throws BizLogicException
	 *             on failure to populate values from the system
	 */
	private void populateAllLists(NewSpecimenForm specimenForm,
			List<NameValueBean> specimenClassList, List<NameValueBean> specimenTypeList,
			List<NameValueBean> tissueSiteList, List<NameValueBean> tissueSideList,
			List<NameValueBean> pathologicalStatusList, Map<String, List<NameValueBean>> subTypeMap)
			throws BizLogicException
	{
		// Getting the specimen type list
		specimenTypeList = AppUtility.getListFromCDE(Constants.CDE_NAME_SPECIMEN_TYPE);

		/**
		 * Name : Virender Mehta Reviewer: Sachin Lale Bug ID:
		 * TissueSiteCombo_BugID Patch ID:TissueSiteCombo_BugID_1 See
		 * also:TissueSiteCombo_BugID_2 Description: Getting TissueList with
		 * only Leaf node
		 */
		tissueSiteList.addAll(AppUtility.tissueSiteList());

		// Getting tissue side list
		tissueSideList.addAll(AppUtility.getListFromCDE(Constants.CDE_NAME_TISSUE_SIDE));

		// Getting pathological status list
		pathologicalStatusList.addAll(AppUtility
				.getListFromCDE(Constants.CDE_NAME_PATHOLOGICAL_STATUS));

		// get the Specimen class and type from the cde
		final CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_SPECIMEN_CLASS);
		final Set<PermissibleValue> setPV = specimenClassCDE.getPermissibleValues();
		for (final PermissibleValue pv : setPV)
		{
			final String tmpStr = pv.getValue();
			this.logger.debug(tmpStr);
			specimenClassList.add(new NameValueBean(tmpStr, tmpStr));

			final List<NameValueBean> innerList = new ArrayList<NameValueBean>();
			innerList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

			final Set<PermissibleValue> list1 = pv.getSubPermissibleValues();
			this.logger.debug("list1 " + list1);
			for (final PermissibleValue pv1 : list1)
			{
				// set specimen type
				final String tmpInnerStr = pv1.getValue();
				this.logger.debug("\t\t" + tmpInnerStr);
				innerList.add(new NameValueBean(tmpInnerStr, tmpInnerStr));
			}
			Collections.sort(innerList);
			subTypeMap.put(tmpStr, innerList);
		} // class and values set
		this.logger.debug("\n\n\n\n**********MAP DATA************\n");

		// Setting the default values
		if (specimenForm.getTissueSite() == null)
		{
			specimenForm.setTissueSite((String) DefaultValueManager
					.getDefaultValue(Constants.DEFAULT_TISSUE_SITE));
		}
		if (specimenForm.getPathologicalStatus() == null)
		{
			specimenForm.setPathologicalStatus((String) DefaultValueManager
					.getDefaultValue(Constants.DEFAULT_PATHOLOGICAL_STATUS));
		}
		AppUtility.setDefaultPrinterTypeLocation(specimenForm);
	}

	/**
	 * Name : Ashish Gupta Reviewer Name : Sachin Lale Bug ID: 2741 Patch ID:
	 * 2741_13 Description: Method to propagate Events to multiple specimens
	 * from scg
	 */
	/**
	 * @param specimenCollectionGroupForm
	 *            instance of SpecimenCollectionGroupForm
	 * @param specimenForm
	 *            instance of NewSpecimenForm
	 */
	/*
	 * public static void populateEventsFromScg1(SpecimenCollectionGroupForm
	 * specimenCollectionGroupForm,NewSpecimenForm specimenForm) {
	 * specimenForm.setCollectionEventUserId
	 * (specimenCollectionGroupForm.getCollectionEventUserId());
	 * specimenForm.setReceivedEventUserId
	 * (specimenCollectionGroupForm.getReceivedEventUserId());
	 * specimenForm.setCollectionEventCollectionProcedure("Not Specified");
	 * specimenForm.setCollectionEventContainer("Not Specified");
	 * specimenForm.setReceivedEventReceivedQuality("Not Specified");
	 * specimenForm.setCollectionEventdateOfEvent(specimenCollectionGroupForm.
	 * getCollectionEventdateOfEvent());
	 * specimenForm.setCollectionEventTimeInHours
	 * (specimenCollectionGroupForm.getCollectionEventTimeInHours());
	 * specimenForm.setCollectionEventTimeInMinutes(specimenCollectionGroupForm.
	 * getCollectionEventTimeInMinutes());
	 * specimenForm.setReceivedEventDateOfEvent
	 * (specimenCollectionGroupForm.getReceivedEventDateOfEvent());
	 * specimenForm.setReceivedEventTimeInHours(specimenCollectionGroupForm.
	 * getReceivedEventTimeInHours());
	 * specimenForm.setReceivedEventTimeInMinutes
	 * (specimenCollectionGroupForm.getReceivedEventTimeInMinutes()); }
	 */
	/**
	 *
	 * @param specimenCollectionGroup : specimenCollectionGroup
	 * @param specimenForm : specimenForm
	 */

	public static void populateEventsFromScg(SpecimenCollectionGroup specimenCollectionGroup,
			NewSpecimenForm specimenForm)
	{
		final Calendar calender = Calendar.getInstance();

		final Collection scgEventColl = specimenCollectionGroup
				.getSpecimenEventParametersCollection();
		if (scgEventColl != null && !scgEventColl.isEmpty())
		{
			final Iterator itr = scgEventColl.iterator();
			while (itr.hasNext())
			{
				final Object specimenEventParameter = itr.next();
				if (specimenEventParameter instanceof CollectionEventParameters)
				{
					final CollectionEventParameters scgCollEventParam = (CollectionEventParameters) specimenEventParameter;

					calender.setTime(scgCollEventParam.getTimestamp());

					specimenForm.setCollectionEventUserId(scgCollEventParam.getUser().getId()
							.longValue());
					specimenForm.setCollectionEventCollectionProcedure(scgCollEventParam
							.getCollectionProcedure());
					specimenForm.setCollectionEventContainer(scgCollEventParam.getContainer());
					specimenForm.setCollectionEventdateOfEvent(Utility.parseDateToString(
							scgCollEventParam.getTimestamp(), CommonServiceLocator.getInstance()
									.getDatePattern()));
					specimenForm.setCollectionEventTimeInHours(Utility.toString(Integer
							.toString(calender.get(Calendar.HOUR_OF_DAY))));
					specimenForm.setCollectionEventTimeInMinutes(Utility.toString(Integer
							.toString(calender.get(Calendar.MINUTE))));
				}
				if (specimenEventParameter instanceof ReceivedEventParameters)
				{
					final ReceivedEventParameters scgReceivedEventParam = (ReceivedEventParameters) specimenEventParameter;
					calender.setTime(scgReceivedEventParam.getTimestamp());

					specimenForm.setReceivedEventUserId(scgReceivedEventParam.getUser().getId()
							.longValue());
					specimenForm.setReceivedEventReceivedQuality(scgReceivedEventParam
							.getReceivedQuality());
					specimenForm.setReceivedEventDateOfEvent(Utility.parseDateToString(
							scgReceivedEventParam.getTimestamp(), CommonServiceLocator
									.getInstance().getDatePattern()));
					specimenForm.setReceivedEventTimeInHours(Utility.toString(Integer
							.toString(calender.get(Calendar.HOUR_OF_DAY))));
					specimenForm.setReceivedEventTimeInMinutes(Utility.toString(Integer
							.toString(calender.get(Calendar.MINUTE))));

				}
			}
		}

		/*
		 * specimenForm.setCollectionEventUserId(specimenCollectionGroupForm.getCollectionEventUserId
		 * ());specimenForm.setReceivedEventUserId(specimenCollectionGroupForm.
		 * getReceivedEventUserId());
		 * specimenForm.setCollectionEventCollectionProcedure("Not Specified");
		 * specimenForm.setCollectionEventContainer("Not Specified");
		 * specimenForm.setReceivedEventReceivedQuality("Not Specified");
		 * specimenForm
		 * .setCollectionEventdateOfEvent(specimenCollectionGroupForm
		 * .getCollectionEventdateOfEvent());
		 * specimenForm.setCollectionEventTimeInHours
		 * (specimenCollectionGroupForm.getCollectionEventTimeInHours());
		 * specimenForm
		 * .setCollectionEventTimeInMinutes(specimenCollectionGroupForm
		 * .getCollectionEventTimeInMinutes());
		 * specimenForm.setReceivedEventDateOfEvent
		 * (specimenCollectionGroupForm.getReceivedEventDateOfEvent());
		 * specimenForm.setReceivedEventTimeInHours(specimenCollectionGroupForm.
		 * getReceivedEventTimeInHours());
		 * specimenForm.setReceivedEventTimeInMinutes
		 * (specimenCollectionGroupForm.getReceivedEventTimeInMinutes());
		 */
	}

	/**
	 * This function adds the columns to the List.
	 *
	 * @return columnList
	 */
	public List columnNames()
	{
		final List columnList = new ArrayList();
		columnList.add(Constants.LABLE);
		columnList.add(Constants.TYPE);
		columnList.add(Constants.STORAGE_CONTAINER_LOCATION);
		columnList.add(Constants.CLASS_NAME);
		return columnList;
	}

	/**
	 *
	 * @param specimenId : specimenId
	 * @return Long : Long
	 * @throws ApplicationException : ApplicationException
	 */
	private Long getAssociatedIdentifiedReportId(Long specimenId, DAO dao)
			throws ApplicationException
	{
		final String hqlString = "select scg.identifiedSurgicalPathologyReport.id "
				+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg, "
				+ " edu.wustl.catissuecore.domain.Specimen as specimen" + " where specimen.id = "
				+ specimenId + " and specimen.id in elements(scg.specimenCollection)";
		final List reportIDList = dao.executeQuery(hqlString);
		if (reportIDList != null && reportIDList.size() > 0)
		{
			return ((Long) reportIDList.get(0));
		}
		return null;
	}

	/**
	 *
	 * @param form : form
	 * @return String : String
	 */

	/*protected String getObjectId(AbstractActionForm form)
	{
		NewSpecimenForm specimenForm = (NewSpecimenForm) form;
		SpecimenCollectionGroup specimenCollectionGroup = null;
		if (specimenForm.getSpecimenCollectionGroupId() != null
				|| specimenForm.getSpecimenCollectionGroupId() != "")
		{
			try
			{
				specimenCollectionGroup = AppUtility.getSCGObj(specimenForm
						.getSpecimenCollectionGroupId());
				CollectionProtocolRegistration cpr = specimenCollectionGroup
						.getCollectionProtocolRegistration();
				if (cpr != null)
				{
					CollectionProtocol cp = cpr.getCollectionProtocol();
					return Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cp.getId();
				}
			}
			catch (ApplicationException e)
			{
				logger.debug(e.getMessage(), e);
				e.printStackTrace();
			}

		}
		return null;

	}*/

	private List getSpCollGroupList(String scgName, DAO dao) throws BizLogicException
	{
		final String[] selectColumnName = {"collectionProtocolRegistration.collectionProtocol.id"};
		List spCollGroupList = null;
		try
		{
			final QueryWhereClause queryWhereClause = new QueryWhereClause(
					SpecimenCollectionGroup.class.getName());
			 if(scgName.contains("'"))
	            {
	             System.out.println("scgName "+scgName);
				 scgName = scgName.replace("'", "''");
	           }
			queryWhereClause.addCondition(new EqualClause("name", scgName));

			spCollGroupList = dao.retrieve(SpecimenCollectionGroup.class.getName(),
					selectColumnName, queryWhereClause);
		}
		catch (final DAOException e)
		{
			this.logger.error(e.getMessage(), e);
			throw new BizLogicException(e);
		}
		return spCollGroupList;
	}
}
