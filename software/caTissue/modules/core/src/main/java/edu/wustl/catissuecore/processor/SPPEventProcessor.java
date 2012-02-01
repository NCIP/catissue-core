/**
 *
 */

package edu.wustl.catissuecore.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import edu.common.dynamicextensions.domain.integration.AbstractFormContext;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsCacheException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.ApplyDataEntryFormProcessor;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.common.dynamicextensions.validation.ValidatorUtil;
import edu.wustl.catissuecore.bizlogic.ActionApplicationBizLogic;
import edu.wustl.catissuecore.bizlogic.CatissueDefaultBizLogic;
import edu.wustl.catissuecore.bizlogic.CpBasedViewBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SPPBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.ISPPBizlogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.processingprocedure.Action;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.domain.processingprocedure.DefaultAction;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedureApplication;
import edu.wustl.catissuecore.processingprocedure.SPPActionComparator;
import edu.wustl.catissuecore.uiobject.SpecimenWrapper;
import edu.wustl.catissuecore.upgrade.IntegrateDEData;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.SpecimenEventsUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.query.util.global.Variables;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * The Class SPPEventProcessor.
 *
 * @author suhas_khot
 */
public class SPPEventProcessor
{

	/** logger. */
	private static final Logger LOGGER = Logger.getCommonLogger(SPPEventProcessor.class);

	/** The default biz logic. */
	private final IBizLogic defaultBizLogic = new CatissueDefaultBizLogic();

	/**
	 * Populate spp events for a specimen.
	 *
	 * @param specimenId the specimen id
	 *
	 * @return the list< map< string, object>>
	 *
	 * @throws BizLogicException the biz logic exception
	 * @throws ApplicationException the application exception
	 */
	public List<Map<String, Object>> populateSPPEventsForASpecimen(String specimenId, HttpServletRequest request)
			throws BizLogicException, ApplicationException
	{
		//retrieves specimen object
		Specimen specimenObject = (Specimen) defaultBizLogic.retrieve(Specimen.class.getName(),
				new Long(specimenId));

		List<Map<String, Object>> sppEventDataCollection = null;
		if (specimenObject != null && specimenObject.getSpecimenRequirement() != null)
		{
			//fetch processing SPP from specimen requirement
			SpecimenProcessingProcedure processingSPP = specimenObject.getSpecimenRequirement().getProcessingSPP();
			if (processingSPP != null)
			{
				request.setAttribute("nameOfSelectedSpp", processingSPP.getName());
				request.setAttribute("selectedSppId", processingSPP.getId());
				//Fetch SPPApplication denotes that at least once data entry is performed.
				SpecimenProcessingProcedureApplication sppApplication = specimenObject.getProcessingSPPApplication();
				//If SPPApplication is null than display SPP events with default value.
				if (sppApplication == null)
				{
					request.setAttribute(Constants.DISPLAY_EVENTS_WITH_DEFAULT_VALUES, true);
					//populate SPP events data for respective SPP with default values
					return populateSPPEventsWithDefaultValue(processingSPP.getActionCollection(), true);
				}
				else
				//SPP Application is not null, data entry for some or all SPP events has already been performed
				{
					TreeSet<Action> actionList = new TreeSet<Action>(new SPPActionComparator());
					actionList.addAll(processingSPP.getActionCollection());
					return populateSPPEventsBasedOnSPPApplication(actionList, sppApplication);
				}
			}
		}
		return sppEventDataCollection;
	}

	/**
	 * Populate spp events with default value.
	 *
	 * @param actionCollection the action collection
	 *
	 * @return the list< map< string, object>>
	 *
	 * @throws ApplicationException the application exception
	 */
	public List<Map<String, Object>> populateSPPEventsWithDefaultValue(
			Collection<Action> actionCollection, boolean isDataEntryPerformed) throws ApplicationException
	{
		List<Map<String, Object>> gridData = new ArrayList<Map<String, Object>>();
		TreeSet<Action> actionList = new TreeSet<Action>(new SPPActionComparator());
		actionList.addAll(actionCollection);
		for (Action sppAction : actionList)
		{
			//For each form retrieve caption.
			String containerCaption = getContainerCaption(sppAction.getContainerId());

			//Data entry is not done for respective SPP action,
			//hence passing actionApplicationId parameter value as 0
			gridData.add(generateRowDataMap(0L, sppAction.getContainerId(), sppAction.getId(),
					containerCaption, new Date(), isDataEntryPerformed));

		}
		return gridData;
	}

	/**
	 * Populate spp events data for editing.
	 *
	 * @param actionApplicationCollection the action application collection
	 *
	 * @return the list< map< string, object>>
	 *
	 * @throws ApplicationException the application exception
	 */
	public List<Map<String, Object>> populateSPPEventsBasedOnSPPApplication(Collection<Action> actionCollection, 
			SpecimenProcessingProcedureApplication sppApplication) throws ApplicationException
	{
		List<Map<String, Object>> sppEventData = new ArrayList<Map<String, Object>>();
		//fetch all SPP Action Applications for the respective SPP Application
		Collection<ActionApplication> actionApplicationCollection = getFilteredActionApplication(sppApplication
				.getSppActionApplicationCollection());
		if (actionApplicationCollection != null)
		{
			generatSPPEventData(sppEventData, actionApplicationCollection, actionCollection);
		}
		return sppEventData;
	}
	
	/**
	 * @param actionApplicationCollection
	 * @return
	 */
	public Collection<ActionApplication> getFilteredActionApplication(Collection<ActionApplication> actionApplicationCollection)
	{
		Collection<ActionApplication> filteredActionApplication = new HashSet<ActionApplication>();
		for(ActionApplication actionApplication : actionApplicationCollection)
		{
			if(actionApplication.getApplicationRecordEntry()!=null 
					&& Constants.ACTIVITY_STATUS_VALUES[1].equals(actionApplication.getApplicationRecordEntry().getActivityStatus()))
			{
				filteredActionApplication.add(actionApplication);
			}
		}
		return filteredActionApplication;		
	}

	/**
	 * Generate spp event data for performed event.
	 *
	 * @param sppEventData the spp event data
	 * @param actionApplicationCollection the action application collection
	 *
	 * @throws ApplicationException the application exception
	 */
	private void generatSPPEventData(List<Map<String, Object>> sppEventData,
			Collection<ActionApplication> actionApplicationCollection, Collection<Action> actionCollection) throws ApplicationException
	{
		TreeSet<Action> actionList = new TreeSet<Action>(new SPPActionComparator());
		actionList.addAll(actionCollection);
		//for each action check is data present, if yes then show that data else show default values
		for (Action action : actionList)
		{
			boolean isDataPresent = false;
			
			//retrieve form caption.
			String containerCaption = getContainerCaption(action.getContainerId());
			
			//Generate SPP event data by iterating on action application
			for (ActionApplication actionApplication : actionApplicationCollection)
			{
				Long formContextId = actionApplication.getApplicationRecordEntry().getFormContext().getId();
				//if action is present in actionApplication collection than data entry is performed.
				if(action.getId().equals(formContextId) && Constants.ACTIVITY_STATUS_VALUES[1].equals(actionApplication.getApplicationRecordEntry().getActivityStatus()))
				{
					isDataPresent = true;
					sppEventData.add(generateRowDataMap(actionApplication.getId(), action.getContainerId(),
							formContextId, containerCaption, actionApplication.getTimestamp(), isDataPresent));
					break;
				}
			}
			//if data does not exist for respective action, then retrieve data with default value
			if(!isDataPresent)
			{
				//Data entry is not done for respective SPP action,
				//hence passing actionApplicationId parameter value as 0
				sppEventData.add(generateRowDataMap(0L, action.getContainerId(), action.getId(),
						containerCaption, new Date(), isDataPresent));
			}
		}
	}

	/**
	 * Gets the container caption.
	 *
	 * @param containerId the container id
	 *
	 * @return the container caption
	 *
	 * @throws ApplicationException the application exception
	 */
	@SuppressWarnings("unchecked")
	public String getContainerCaption(Long containerId) throws ApplicationException
	{
		//Fetch container caption for a given container
		List containerList = AppUtility
				.executeSQLQuery("select caption from dyextn_container where identifier="
						+ containerId);
		String containerCaption = (String) ((List) containerList.get(0)).get(0);
		return containerCaption;
	}

	/**
	 * Generate row data map.
	 *
	 * @param actionApplicationId the action application id
	 * @param containerId the container id
	 * @param formContextId the form context id
	 * @param containerCaption the container caption
	 * @param eventDate the event date
	 *
	 * @return the map< string, object>
	 */
	public Map<String, Object> generateRowDataMap(Long actionApplicationId, Long containerId,
			Long formContextId, String containerCaption, Date eventDate, boolean isSPPDataEntryDone)
	{
		Map<String, Object> rowDataMap = new HashMap<String, Object>();
		rowDataMap.put(Constants.ID, String.valueOf(actionApplicationId));
		rowDataMap.put(Constants.FORM_CONTEXT_ID, String.valueOf(formContextId));
		rowDataMap.put(Constants.CONTAINER_IDENTIFIER, containerId);
		rowDataMap.put(Constants.EVENT_DATE, eventDate);
		rowDataMap.put(Constants.PAGE_OF, Constants.PAGE_OF_DYNAMIC_EVENT);
		rowDataMap.put(Constants.CAPTION, edu.wustl.cab2b.common.util.Utility
				.getFormattedString(containerCaption));
		rowDataMap.put(Constants.IS_SPP_DATA_ENTRY_DONE, isSPPDataEntryDone);
		return rowDataMap;
	}

	/**
	 * Gets the user.
	 *
	 * @param valueField the value field
	 *
	 * @return the user
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	@SuppressWarnings("unchecked")
	private User getUser(String valueField) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic userBizLogic = factory.getBizLogic(Constants.USER_FORM_ID);
		String sourceObjectName = User.class.getName();
		String displayNameFields = Constants.ID;
		List<User> userList = userBizLogic
				.retrieve(sourceObjectName, displayNameFields, valueField);
		return userList.get(0);
	}

	/**
	 * Gets the spp by name.
	 *
	 * @param sppName the spp name
	 *
	 * @return the SPP by name
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	public SpecimenProcessingProcedure getSPPByName(final String sppName) throws BizLogicException
	{
		//Fetch processing SPP
		SpecimenProcessingProcedure processingSPP = null;
		if(sppName != null)
		{
			List<SpecimenProcessingProcedure> sppList = defaultBizLogic.retrieve(SpecimenProcessingProcedure.class.getName(), "name", sppName);
			processingSPP = sppList.get(0);
		}
		return processingSPP;
	}

	/**
	 * Insert spp application.
	 *
	 * @param actionAppBizLogic the action app biz logic
	 * @param specimen the specimen
	 * @param contextVsRecordIdMap the context vs record id map
	 * @param sessionLoginInfo the session login info
	 * @param formContextParameterMap the form context parameter map
	 * @param formContextCollection the form context collection
	 *
	 * @throws FileNotFoundException the file not found exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws SQLException the SQL exception
	 * @throws BizLogicException the biz logic exception
	 * @throws ApplicationException the application exception
	 */
	public void insertSPPApplication(final IBizLogic actionAppBizLogic, final ISPPBizlogic sppBizlogicObject,
			Map<AbstractFormContext, Long> contextVsRecordIdMap, SessionDataBean sessionLoginInfo,
			Map<AbstractFormContext, Map<String, Object>> formContextParameterMap,
			Set<AbstractFormContext> formContextCollection, String sppName) throws FileNotFoundException,
			DynamicExtensionsSystemException, IOException, DynamicExtensionsApplicationException,
			SQLException, BizLogicException, ApplicationException
	{
		//create new SPP Application object
		SpecimenProcessingProcedureApplication processingSPPApplication = new SpecimenProcessingProcedureApplication();

		Collection<ActionApplication> actionApplicationCollection = new HashSet<ActionApplication>();
		for (AbstractFormContext formContext : formContextCollection)
		{
			Map<String, Object> staticParameters = formContextParameterMap.get(formContext);

			ActionApplication actionApplication = associateRecEntryWithActionApp(actionAppBizLogic,
					sppBizlogicObject, contextVsRecordIdMap, processingSPPApplication, formContext,
					staticParameters);

			actionApplicationCollection.add(actionApplication);

		}
		sppBizlogicObject.updateSPPApplication(getSPPByName(sppName), processingSPPApplication, actionApplicationCollection,
				sessionLoginInfo);

		sppBizlogicObject.update(processingSPPApplication, sessionLoginInfo);
	}

	/**
	 * Insert action.
	 *
	 * @param actionAppBizLogic the action app biz logic
	 * @param specimen the specimen
	 * @param contextVsRecordIdMap the context vs record id map
	 * @param sessionLoginInfo the session login info
	 * @param formContextParameterMap the form context parameter map
	 * @param formContextCollection the form context collection
	 *
	 * @throws FileNotFoundException the file not found exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws SQLException the SQL exception
	 * @throws BizLogicException the biz logic exception
	 * @throws ApplicationException the application exception
	 */
	public void insertAction(final IBizLogic actionAppBizLogic, final Specimen specimen,
			Map<AbstractFormContext, Long> contextVsRecordIdMap, SessionDataBean sessionLoginInfo,
			Map<AbstractFormContext, Map<String, Object>> formContextParameterMap,
			Set<AbstractFormContext> formContextCollection) throws FileNotFoundException,
			DynamicExtensionsSystemException, IOException, DynamicExtensionsApplicationException,
			SQLException, BizLogicException, ApplicationException
	{
		//create new SPP Application object
		SpecimenProcessingProcedureApplication processingSPPApplication = null;


		ISPPBizlogic sppBizlogicObject = new SpecimenWrapper();
		sppBizlogicObject.setWrapperObject(specimen);

		Collection<ActionApplication> actionApplicationCollection = specimen.getActionApplicationCollection();
		for (AbstractFormContext formContext : formContextCollection)
		{
			Map<String, Object> staticParameters = formContextParameterMap.get(formContext);

			ActionApplication actionApplication = associateRecEntryWithActionApp(actionAppBizLogic,
					sppBizlogicObject, contextVsRecordIdMap, processingSPPApplication, formContext,
					staticParameters);

			actionApplicationCollection.add(actionApplication);
		}
		final IBizLogic bizLogic = new CatissueDefaultBizLogic();
		Specimen oldSpecimen=(Specimen) bizLogic.retrieve(Specimen.class.getName(), specimen.getId());
		bizLogic.update(specimen, oldSpecimen, sessionLoginInfo);
	}

	/**
	 * Associate rec entry with action app.
	 *
	 * @param actionAppBizLogic the action app biz logic
	 * @param specimen the specimen
	 * @param contextVsRecordIdMap the context vs record id map
	 * @param processingSPPApplication the processing spp application
	 * @param formContext the form context
	 * @param staticParameters the static parameters
	 *
	 * @return the action application
	 *
	 * @throws BizLogicException the biz logic exception
	 * @throws ApplicationException the application exception
	 * @throws DynamicExtensionsCacheException the dynamic extensions cache exception
	 */
	public ActionApplication associateRecEntryWithActionApp(final IBizLogic actionAppBizLogic,
			final ISPPBizlogic sppBizlogicObject, Map<AbstractFormContext, Long> contextVsRecordIdMap,
			SpecimenProcessingProcedureApplication processingSPPApplication, AbstractFormContext formContext,
			Map<String, Object> staticParameters) throws BizLogicException, ApplicationException, DynamicExtensionsCacheException
	{
		ActionApplicationRecordEntry actionAppRecordEntry = new ActionApplicationBizLogic()
				.insertActionApplicationRecordEntry(formContext);

		//Fetch User identifier from map.
		String userId = (String) staticParameters.get(Constants.USER_ID);

		Date dateOfEvent = populateTimeStamp(staticParameters);
		ActionApplication actionApplication = sppBizlogicObject.insertActionApplication(actionAppBizLogic,
				processingSPPApplication, staticParameters.get(Constants.REASON_DEVIATION)
						.toString(), getUser(userId), dateOfEvent,staticParameters.get(Constants.COMMENTS)
						.toString(), actionAppRecordEntry);

		IntegrateDEData integrateDEData = new IntegrateDEData();
		integrateDEData.associateRecords(formContext.getContainerId(),
				actionAppRecordEntry.getId(), contextVsRecordIdMap.get(formContext));
		return actionApplication;
	}

	/**
	 * Populate time stamp.
	 *
	 * @param staticParameters the static parameters
	 *
	 * @return the date
	 */
	private Date populateTimeStamp(Map<String, Object> staticParameters)
	{
		Date dateOfEvent = null;
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm");
		try
		{
			dateOfEvent = formatter.parse(staticParameters.get("dateOfEvent") + " "
					+ staticParameters.get("timeInHours") + ":"
					+ staticParameters.get("timeInMinutes"));
		}
		catch (ParseException e)
		{
			LOGGER.error("Invalid Date Parser Exception: " + e.getMessage(), e);
		}
		return dateOfEvent;
	}

	/**
	 * Validate de data.
	 *
	 * @param request the request
	 * @param formContextCollection the form context collection
	 *
	 * @return the list< string>
	 *
	 * @throws Exception the exception
	 */
	public List<String> validateDEData(HttpServletRequest request,
			Map<AbstractFormContext, Map<String, Object>> formContextParameterMap) throws Exception
	{
		List<String> listOfError = new ArrayList<String>();
		ApplyDataEntryFormProcessor processor = ApplyDataEntryFormProcessor.getInstance();
		Map<AbstractFormContext, Map<BaseAbstractAttributeInterface, Object>> contextDataValueMap = processor
				.generateAttributeValueMap(formContextParameterMap.keySet(), request);
		for (AbstractFormContext formContext : contextDataValueMap.keySet())
		{
			if(formContextParameterMap.get(formContext).get(Constants.EVENT_PERFORMED)!=null 
					&& Boolean.parseBoolean((String) formContextParameterMap.get(formContext).get(Constants.EVENT_PERFORMED)))
			{
				ContainerInterface containerInterface = DynamicExtensionsUtility
					.getClonedContainerFromCache(formContext.getContainerId().toString());
				ValidatorUtil.validateEntity(contextDataValueMap.get(formContext), listOfError,
					containerInterface, false);
			}
		}
		if (!listOfError.isEmpty())
		{
			for (AbstractFormContext formContext : contextDataValueMap.keySet())
			{
				ContainerInterface containerInterface = DynamicExtensionsUtility
					.getClonedContainerFromCache(formContext.getContainerId().toString());
				Stack<ContainerInterface> containerStack = new Stack<ContainerInterface>();
				Stack<Map<BaseAbstractAttributeInterface, Object>> valueMapStack = new Stack<Map<BaseAbstractAttributeInterface, Object>>();
				containerStack.add(containerInterface);
				valueMapStack.add(contextDataValueMap.get(formContext));
				CacheManager.addObjectToCache(request, DEConstants.CONTAINER_INTERFACE
						+ formContext.getId(), containerInterface);
				CacheManager.addObjectToCache(request, DEConstants.CONTAINER_STACK
						+ formContext.getId(), containerStack);
				CacheManager.addObjectToCache(request, DEConstants.VALUE_MAP_STACK
						+ formContext.getId(), valueMapStack);
			}
		}
		return listOfError;
	}

	/**
	 * Gets the specimen or scg id array.
	 *
	 * @param request the request
	 * @param scgId the scg id
	 * @param isSCG the is scg
	 *
	 * @return the specimen or scg id array
	 *
	 * @throws ApplicationException the application exception
	 */
	public String[] getSpecimenOrSCGIdArray(HttpServletRequest request, String scgId, boolean isSCG)
			throws ApplicationException
	{
		SPPBizLogic sppBizlogic = new SPPBizLogic();
		String[] scgIdArr = {scgId};

		if (Constants.TRUE.equals(request.getParameter("selectedAll")))
		{
			String sppId = request.getParameter("sppId");
			String typeObject=(String)request.getParameter("typeObject");
			List idList = null;
			if(Constants.SPECIMEN_COLLECTION_GROUP.equalsIgnoreCase(typeObject))
			{
				idList = sppBizlogic.getScgsIdBySPPID(Long.parseLong(sppId));
			}
			else if(Constants.SPECIMEN.equalsIgnoreCase(typeObject))
			{
				idList = sppBizlogic.getSpecimensIdBySPPID(Long.parseLong(sppId));
			}
			scgIdArr = new String[idList.size()];
			for (int cnt = 0; cnt < idList.size(); cnt++)
			{
				scgIdArr[cnt] = ((List) idList.get(cnt)).get(0).toString();
			}
		}
		else
		{
			if (scgId.contains(","))
			{
				scgIdArr = scgId.split(",");
			}
		}
		return scgIdArr;
	}

	/**
	 * Insert update de data for spp events.
	 *
	 * @param request the request
	 * @param contextRecordIdMap the context record id map
	 * @param formContextCollection the form context collection
	 *
	 * @return the map< abstract form context, long>
	 *
	 * @throws FileNotFoundException the file not found exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws SQLException the SQL exception
	 */
	public Map<AbstractFormContext, Long> insertUpdateDEDataForSPPEvents(
			HttpServletRequest request, Map<AbstractFormContext, Long> contextRecordIdMap,
			Set<AbstractFormContext> formContextCollection) throws FileNotFoundException,
			DynamicExtensionsSystemException, IOException, DynamicExtensionsApplicationException,
			SQLException
	{
		//insert DE data
		ApplyDataEntryFormProcessor processor = ApplyDataEntryFormProcessor.getInstance();
		Map<AbstractFormContext, Map<BaseAbstractAttributeInterface, Object>> contextDataValueMap = processor
				.generateAttributeValueMap(formContextCollection, request);
		return processor.insertUpdateDataEntryFormCollection(contextDataValueMap,
				contextRecordIdMap, null);
	}

	/**
	 * Edits the action application collection.
	 *
	 * @param actionAppBizLogic the action app biz logic
	 * @param actionApplicationCollection the action application collection
	 * @param formContextParameterMap the form context parameter map
	 * @param formContextCollection the form context collection
	 *
	 * @return the map< abstract form context, long>
	 *
	 * @throws BizLogicException the biz logic exception
	 * @throws ApplicationException the application exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws SQLException the SQL exception
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Map<AbstractFormContext, Long> editActionApplicationCollection(
			final IBizLogic actionAppBizLogic,
			Collection<ActionApplication> actionApplicationCollection,
			Map<AbstractFormContext, Map<String, Object>> formContextParameterMap,
			Set<AbstractFormContext> formContextCollection) throws BizLogicException,
			ApplicationException, DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, SQLException, FileNotFoundException, IOException
	{
		Map<AbstractFormContext, Long> contextRecordIdMap = new HashMap<AbstractFormContext, Long>();
		for (AbstractFormContext formContext : formContextCollection)
		{
			if(formContextParameterMap.get(formContext).get(Constants.EVENT_PERFORMED)!=null 
					&& Boolean.parseBoolean((String) formContextParameterMap.get(formContext).get(Constants.EVENT_PERFORMED)))
			{
				Map<String, Object> staticParametersList = formContextParameterMap.get(formContext);
				ActionApplication actionApplication = getActionApplForSPPEvent(
						actionApplicationCollection, formContext.getId());
				updateActionApplication(actionAppBizLogic, contextRecordIdMap, formContext,
						staticParametersList, actionApplication);
			}
		}
		return contextRecordIdMap;
	}

	/**
	 * Update action application.
	 *
	 * @param actionAppBizLogic the action app biz logic
	 * @param contextRecordIdMap the context record id map
	 * @param formContext the form context
	 * @param staticParametersList the static parameters list
	 * @param actionApplication the action application
	 *
	 * @throws ApplicationException the application exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws SQLException the SQL exception
	 * @throws BizLogicException the biz logic exception
	 */
	private void updateActionApplication(final IBizLogic actionAppBizLogic,
			Map<AbstractFormContext, Long> contextRecordIdMap, AbstractFormContext formContext,
			Map<String, Object> staticParametersList, ActionApplication actionApplication)
			throws ApplicationException, DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, SQLException, BizLogicException
	{
		if (actionApplication != null && Constants.ACTIVITY_STATUS_VALUES[1].equals(actionApplication
				.getApplicationRecordEntry().getActivityStatus()))
		{
			//populate recordId Map for given form ContextCollection
			Long recordIdentifier = SpecimenEventsUtility.getRecordIdentifier(actionApplication
					.getApplicationRecordEntry().getId(), formContext.getContainerId());
			contextRecordIdMap.put(formContext, recordIdentifier);
			//Update actionApplication for static data
			actionApplication.setReasonDeviation(staticParametersList.get(
					Constants.REASON_DEVIATION).toString());
			actionApplication.setComments(staticParametersList.get(Constants.COMMENTS).toString());
			Date dateOfEvent = populateTimeStamp(staticParametersList);
			actionApplication.setTimestamp(dateOfEvent);
			actionAppBizLogic.update(actionApplication);
		}
	}

	/**
	 * Edits the action application for adhoc events.
	 *
	 * @param actionAppBizLogic the action app biz logic
	 * @param recordIdentifier the record identifier
	 * @param formContextParameterMap the form context parameter map
	 * @param formContextCollection the form context collection
	 *
	 * @return the map< abstract form context, long>
	 *
	 * @throws BizLogicException the biz logic exception
	 * @throws ApplicationException the application exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws SQLException the SQL exception
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Map<AbstractFormContext, Long> editActionApplicationForAdhocEvents(
			final IBizLogic actionAppBizLogic, Long recordIdentifier,
			Map<AbstractFormContext, Map<String, Object>> formContextParameterMap,
			Set<AbstractFormContext> formContextCollection) throws BizLogicException,
			ApplicationException, DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, SQLException, FileNotFoundException, IOException
	{
		Map<AbstractFormContext, Long> contextRecordIdMap = new HashMap<AbstractFormContext, Long>();
		for (AbstractFormContext formContext : formContextCollection)
		{
			Map<String, Object> staticParametersList = formContextParameterMap.get(formContext);
			ActionApplication actionApplication = getActionAppForRecordId(Long
					.valueOf((String) staticParametersList.get("id")));
			updateActionApplication(actionAppBizLogic, contextRecordIdMap, formContext,
					staticParametersList, actionApplication);
		}
		return contextRecordIdMap;
	}

	/**
	 * Gets the action app for record id.
	 *
	 * @param id the id
	 *
	 * @return the action app for record id
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	private ActionApplication getActionAppForRecordId(Long id) throws BizLogicException
	{
		ActionApplication actionApp = null;
		actionApp = (ActionApplication) defaultBizLogic.retrieve(ActionApplication.class.getName(),
				id);
		return actionApp;
	}

	/**
	 * Gets the action appl for spp event.
	 *
	 * @param actionApplicationCollection the action application collection
	 * @param formContextId the form context id
	 *
	 * @return the action appl for spp event
	 */
	private ActionApplication getActionApplForSPPEvent(
			Collection<ActionApplication> actionApplicationCollection, Long formContextId)
	{
		for (ActionApplication actionApplication : actionApplicationCollection)
		{
			if (actionApplication.getApplicationRecordEntry().getFormContext().getId().equals(
					formContextId))
			{
				return actionApplication;
			}
		}
		return null;
	}

	/**
	 * Populate spp events.
	 *
	 * @param request the request
	 * @param specimenObject the specimen object
	 *
	 * @throws ApplicationException the application exception
	 */
	@SuppressWarnings("unchecked")
	public void populateSPPEventsForSCG(HttpServletRequest request,
			SpecimenCollectionGroup scgObject, String sppName) throws ApplicationException
	{
		List<Map<String, Object>> sppEventDataCollection = new ArrayList<Map<String, Object>>();
		Collection<SpecimenProcessingProcedureApplication> sppAppCollection = scgObject.getSppApplicationCollection();

		if (sppAppCollection != null && !sppAppCollection.isEmpty())
		{
			boolean sppDataEntryDone = false;
			Iterator<SpecimenProcessingProcedureApplication> sppAppIter = sppAppCollection.iterator();
			while (sppAppIter.hasNext())
			{
				SpecimenProcessingProcedureApplication sppApplication = sppAppIter.next();
				if (sppName.equalsIgnoreCase(sppApplication.getSpp().getName()))
				{
					Collection<ActionApplication> actionApplicationCollection = (Collection<ActionApplication>) defaultBizLogic
							.retrieveAttribute(SpecimenProcessingProcedureApplication.class, new Long(sppApplication.getId()),
									"elements(sppActionApplicationCollection)");
					if (actionApplicationCollection != null)
					{
						//populate SPP data
						generatSPPEventData(sppEventDataCollection, actionApplicationCollection, sppApplication.getSpp().getActionCollection());
					}
					sppDataEntryDone = true;
					break;
				}

			}
			if(!sppDataEntryDone)
			{
				sppEventDataCollection= displayDefaultSPPEventData(request, scgObject,  sppName);
			}
		}
		else
		{
			sppEventDataCollection= displayDefaultSPPEventData(request, scgObject,  sppName);

		}
		//Add SPP events data collecton in request scope
		request.setAttribute(Constants.SPP_EVENTS, sppEventDataCollection);
	}

	/**
	 * Populate form context parmater map.
	 *
	 * @param parameterMap the parameter map
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	public Map<AbstractFormContext, Map<String, Object>> populateFormContextParmaterMap(
			Map<String, Object> parameterMap) throws BizLogicException
	{
		Map<AbstractFormContext, Map<String, Object>> formContextParameterMap = new HashMap<AbstractFormContext, Map<String, Object>>();
		Map<String, Action> ctxIdActionMap = new HashMap<String, Action>();
		
		for (String parameterName : parameterMap.keySet()) {
			String[] paramTokens = parameterName.split(Constants.CONTROL_DELIMITER);
			if (paramTokens.length == 1) {
				continue;
			}
			
			Action action = null;
			if (ctxIdActionMap.containsKey(paramTokens[0])) {
				action = ctxIdActionMap.get(paramTokens[0]);
			} else {
				action = getActionById(paramTokens[0]);
				ctxIdActionMap.put(paramTokens[0], action);
			}
			
			if (action != null) {
				Map<String, Object> controlValueMap = formContextParameterMap.get(action);
				if (controlValueMap == null) {
					controlValueMap = new HashMap<String, Object>();
				}
				
				controlValueMap.put(paramTokens[1], ((Object[])parameterMap.get(parameterName))[0]);
				formContextParameterMap.put(action, controlValueMap);
			}
		}
		return formContextParameterMap;
	}

	/**
	 * Gets the action by id.
	 *
	 * @param formContextId the form context id
	 *
	 * @return the action by id
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	@SuppressWarnings("unchecked")
	public Action getActionById(String formContextId) throws BizLogicException
	{
		Action action = null;
		List<Action> actionList = defaultBizLogic.retrieve(Action.class.getName(), Constants.ID,
				formContextId);
		if (actionList != null && !actionList.isEmpty())
		{
			action = actionList.get(0);
		}
		return action;
	}

	/**
	 * Populate form context parmater map for adhoc event.
	 *
	 * @param parameterMap the parameter map
	 *
	 * @return the map< abstract form context, map< string, object>>
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	public Map<AbstractFormContext, Map<String, Object>> populateFormContextParmaterMapForAdhocEvent(
			Map<String, Object> parameterMap) throws BizLogicException
	{
		Map<AbstractFormContext, Map<String, Object>> formContextParameterMap = new HashMap<AbstractFormContext, Map<String, Object>>();
		Map<String, AbstractFormContext> ctxIdActionMap = new HashMap<String, AbstractFormContext>();
		
		for (String parameterName : parameterMap.keySet()) {
			String[] paramTokens = parameterName.split(Constants.CONTROL_DELIMITER);
			if (paramTokens.length == 1) {
				continue;
			}
			
			AbstractFormContext formContext = null;
			if (ctxIdActionMap.containsKey(paramTokens[0])) {
				formContext = ctxIdActionMap.get(paramTokens[0]);
			} else {
				formContext = getDefaultActionById(paramTokens[0]);
				if (formContext == null) {
					formContext = getActionById(paramTokens[0]);
				}
				ctxIdActionMap.put(paramTokens[0], formContext);
			}
			
			if (formContext != null) {
				Map<String, Object> controlValueMap = formContextParameterMap.get(formContext);
				if (controlValueMap == null) {
					controlValueMap = new HashMap<String, Object>();
				}
				
				controlValueMap.put(paramTokens[1], ((Object[])parameterMap.get(parameterName))[0]);
				formContextParameterMap.put(formContext, controlValueMap);
			}
		}
		return formContextParameterMap;
	}

	/**
	 * Gets the default action by id.
	 *
	 * @param formContextId the form context id
	 *
	 * @return the default action by id
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	@SuppressWarnings("unchecked")
	public DefaultAction getDefaultActionById(String formContextId) throws BizLogicException
	{
		DefaultAction defaultAction = null;
		List<DefaultAction> defaultActionList = defaultBizLogic.retrieve(DefaultAction.class
				.getName(), Constants.ID, formContextId);
		if (defaultActionList != null && !defaultActionList.isEmpty())
		{
			defaultAction = defaultActionList.get(0);
		}
		return defaultAction;
	}

	/**
	 * Checks if is authorized.
	 *
	 * @param object the object
	 * @param sessionLoginInfo the session login info
	 *
	 * @return true, if is authorized
	 *
	 * @throws BizLogicException the biz logic exception
	 * @throws DAOException the DAO exception
	 */
	public boolean isAuthorized(Object object, SessionDataBean sessionLoginInfo)
			throws BizLogicException, DAOException
	{
		DAO dao = null;
		boolean isAuthorized = true;
		try
		{
			if(object instanceof Specimen)
			{
				NewSpecimenBizLogic newSpecimenBizLogic = new NewSpecimenBizLogic();
				IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(
						newSpecimenBizLogic.getAppName());
				dao = daofactory.getDAO();
				dao.openSession(null);
				isAuthorized = newSpecimenBizLogic.isAuthorized(dao, object,
						sessionLoginInfo);
			}
			else if(object instanceof SpecimenCollectionGroup)
			{
				SpecimenCollectionGroupBizLogic bizLogic = new SpecimenCollectionGroupBizLogic();
				IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(
						bizLogic.getAppName());
				dao = daofactory.getDAO();
				dao.openSession(null);
				isAuthorized = bizLogic.isAuthorized(dao, object, sessionLoginInfo);
			}
		}
		finally
		{
			if (dao != null)
			{
				dao.closeSession();
			}
		}
		return isAuthorized;
	}

	/**
	 * Gets the sPP by specimen id.
	 *
	 * @param specimenId the specimen id
	 *
	 * @return the sPP by specimen id
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	public SpecimenProcessingProcedure getSPPBySpecimenId(Long specimenId) throws BizLogicException
	{
		SpecimenProcessingProcedure processingSPP = null;

		//retrieves specimen object
		Specimen specimenObject = (Specimen) defaultBizLogic.retrieve(Specimen.class.getName(),
				specimenId);

		if (specimenObject != null && specimenObject.getSpecimenRequirement() != null)
		{
			processingSPP = specimenObject.getSpecimenRequirement().getProcessingSPP();
		}
		return processingSPP;
	}

	/**
	 * Display default spp event data.
	 *
	 * @param request the request
	 * @param scgObject the scg object
	 * @param sppName the spp name
	 *
	 * @return the list
	 *
	 * @throws ApplicationException the application exception
	 */
	public List<Map<String, Object>> displayDefaultSPPEventData(HttpServletRequest request,
			SpecimenCollectionGroup scgObject, String sppName ) throws ApplicationException
	{
		List<Map<String, Object>> sppEventDataCollection = new ArrayList<Map<String, Object>>();

		Collection<SpecimenProcessingProcedure> sppCollection = scgObject.getCollectionProtocolEvent()
				.getSppCollection();
		Iterator<SpecimenProcessingProcedure> sppIter = sppCollection.iterator();
		SpecimenProcessingProcedure spp = null;
		while (sppIter.hasNext())
		{
			spp = sppIter.next();
			if (sppName.equalsIgnoreCase(spp.getName()))
			{
				break;
			}
		}

		request.setAttribute(Constants.DISPLAY_EVENTS_WITH_DEFAULT_VALUES, true);
		sppEventDataCollection = populateSPPEventsWithDefaultValue(spp.getActionCollection(), true);
		Map<String, Long> dynamicEventMap = new HashMap<String, Long>();
		new SPPBizLogic().getAllSPPEventFormNames(dynamicEventMap);
		if (request.getSession().getAttribute("dynamicEventMap") == null)
		{
			request.getSession().setAttribute("dynamicEventMap", dynamicEventMap);
		}

		return sppEventDataCollection;

	}

	/**
	 * Get all the ids of collection protocol on which the user has 'READ_DENIED' privilege.
	 * @param username username
	 * @return readDeniedIds
	 */
	public String getReadDeniedIds(String username)
	{
		StringBuffer readDeniedIds = new StringBuffer();
		try
		{
			PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
			PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(username);
			Map<String, List<NameValueBean>>  privilegeMap =
				privilegeCache.getPrivilegesforPrefix(Variables.mainProtocolObject+"_");
			Set<String> keySet = privilegeMap.keySet();
			Iterator<String> privilegeMapItr = keySet.iterator();
			while(privilegeMapItr.hasNext())
			{
				String objectId = privilegeMapItr.next();
				List<NameValueBean> privilegeList = privilegeMap.get(objectId);
				for(NameValueBean privilegeName : privilegeList)
				{
					if(privilegeName.getName().equalsIgnoreCase("READ_DENIED"))
					{
						String identifier = objectId.substring(objectId.lastIndexOf('_')+1,
								objectId.length());
						readDeniedIds.append(identifier).append(',');
					}
				}
			}
			if(readDeniedIds.length() != 0)
			{
				readDeniedIds.deleteCharAt(readDeniedIds.lastIndexOf(","));
			}
		}
		catch(SMException e)
		{
			LOGGER.error("Error in getting PrivilegeManager instance: " + e.getMessage(), e);
		}
		return readDeniedIds.toString();
	}

	public String getCPIds(SessionDataBean sessionData)
	{
		StringBuffer finalCpIds = new StringBuffer();
		try
		{
			CpBasedViewBizLogic userbizlogic=new CpBasedViewBizLogic();
			List<NameValueBean> cpIds=new ArrayList<NameValueBean>();
			StringBuffer authorizedCpIds = new StringBuffer();

			cpIds = userbizlogic.getCollectionProtocolCollection(sessionData);
			for (Iterator<NameValueBean> iterator = cpIds.iterator(); iterator.hasNext();)
			{
				NameValueBean nvb = iterator.next();
				authorizedCpIds.append(nvb.getValue()).append(',');
			}
			if(authorizedCpIds.length() != 0)
			{
				authorizedCpIds.deleteCharAt(authorizedCpIds.lastIndexOf(","));
			}
			String hql;
			if(authorizedCpIds.length() == 0)
			{
				hql="select cp.id from edu.wustl.catissuecore.domain.CollectionProtocol cp";
			}
			else
			{
				hql="select cp.id from edu.wustl.catissuecore.domain.CollectionProtocol cp where cp.id not in ("+authorizedCpIds+")";
			}
			List cpList = AppUtility.executeQuery(hql);
			for(Object cpId: cpList)
			{
				finalCpIds.append(cpId.toString()).append(',');
			}
			if(finalCpIds.length() != 0)
			{
				finalCpIds.deleteCharAt(finalCpIds.lastIndexOf(","));
			}
		}
		catch (ApplicationException e)
		{
			LOGGER.error("Error in executing the query: " + e.getMessage(), e);
		}
		return finalCpIds.toString();
	}
}