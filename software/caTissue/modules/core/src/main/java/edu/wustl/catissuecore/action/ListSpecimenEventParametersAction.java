/**
 * <p>
 * Title: ListSpecimenEventParametersAction Class>
 * <p>
 * Description: This class initializes the fields of SpecimenEventParameters.jsp
 * Page
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Aniruddha Phadnis
 * @version 1.00 Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.xmi.AnnotationUtil;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SPPBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.processingprocedure.AbstractApplication;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class ListSpecimenEventParametersAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(ListSpecimenEventParametersAction.class);

	Map<String, Long> dynamicEventMap = new HashMap<String, Long>();
	
	HashSet<ActionApplication> dynamicEventsForGrid=new HashSet<ActionApplication>();

	/**
	 * Overrides the execute method of Action class. Initialises the various
	 * fields in SpecimenEventParameters.jsp Page.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws IOException
	 *             I/O exception
	 * @throws ServletException
	 *             servlet exception
	 * @return value for ActionForward object
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
			{
		saveActionErrors(form, request);
		// //Gets the value of the operation parameter.
		// String operation = request.getParameter(Constants.OPERATION);
		//
		// //Sets the operation attribute to be used in the Add/Edit Institute
		// Page.
		// request.setAttribute(Constants.OPERATION, operation);
		try
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);

			// ************* ForwardTo implementation *************
			// forwarded from Specimen page
			final HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");
			Long specimenId = null;
			String specimenLabel = null;
			String specimenClass = null;
			// -------------Mandar 04-July-06 QuickEvents start
			String fromQuickEvent = (String) request.getAttribute("isQuickEvent");
			if (fromQuickEvent == null)
			{
				fromQuickEvent = request.getParameter("isQuickEvent");
			}
			String eventSelected = "";
			if (fromQuickEvent != null)
			{
				specimenId = (Long)(request.getAttribute(Constants.SPECIMEN_ID));
				eventSelected = (String) request.getAttribute(Constants.EVENT_SELECTED);
				if (eventSelected == null && forwardToHashMap != null)
				{
					specimenClass = (String) forwardToHashMap.get("specimenClass");
					specimenId = (Long) forwardToHashMap.get(Constants.SPECIMEN_ID);
					if (specimenClass.equalsIgnoreCase("Tissue"))
					{
						eventSelected = Constants.EVENT_PARAMETERS[14];
					}
					else if (specimenClass.equalsIgnoreCase("Molecular"))
					{
						eventSelected = Constants.EVENT_PARAMETERS[9];
					}
					else if (specimenClass.equalsIgnoreCase("Cell"))
					{
						eventSelected = Constants.EVENT_PARAMETERS[1];
					}
					else if (specimenClass.equalsIgnoreCase("Fluid"))
					{
						eventSelected = Constants.EVENT_PARAMETERS[7];
					}

				}
				request.setAttribute(Constants.EVENT_SELECTED, eventSelected);
				// System.out.println("::::::::::::::\n\n\n\n"+form.getClass().
				// getName() );
				ListSpecimenEventParametersForm eventForm = (ListSpecimenEventParametersForm) form;
				if (eventForm == null)
				{
					eventForm = new ListSpecimenEventParametersForm();
				}
				eventForm.setSpecimenEventParameter(eventSelected);
				eventForm.setSpecimenId(specimenId.toString());

			}
			// -------------Mandar 04-July-06 QuickEvents end
			if (forwardToHashMap != null)
			{
				// Fetching SpecimenId from HashMap generated by
				// ForwardToProcessor
				specimenId = (Long) forwardToHashMap.get(Constants.SPECIMEN_ID);
				specimenLabel = (String) forwardToHashMap.get(Constants.SPECIMEN_LABEL);
				Logger.out.debug("SpecimenID found in " + "forwardToHashMap========>>>>>>"
						+ specimenId);
			}
			// ************* ForwardTo implementation *************

			// If new SpecimenEvent is added, specimenId in forwardToHashMap
			// will be null, following code handles that case
			if (specimenId == null)
			{
				final String eventId = request.getParameter("eventId");

				// Added by Vijay Pande. While cliking on events tab both
				// specimenId and eventId are getting null. Since there was no
				// check on eventId it was throwing error for following retrieve
				// call.
				// Null check is added for eventId. Fix for bug Id: 4731
				if (eventId != null)
				{
					Logger.out.debug("Event ID added===>" + eventId);
					// Retrieving list of SpecimenEvents for added
					// SpecimenEventId
					final Object object = bizLogic.retrieve(
							SpecimenEventParameters.class.getName(), new Long(eventId));

					if (object != null)
					{
						// Getting object of specimenEventParameters from the
						// list of SepcimenEvents
						final SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) object;

						// getting SpecimenId of SpecimenEventParameters
						final AbstractSpecimen specimen = specimenEventParameters.getSpecimen();
						specimenId = specimen.getId();
						// specimenLabel = specimen.getLabel();
						Logger.out.debug("Specimen of Event Added====>"
								+ (specimenEventParameters.getSpecimen()).getId());
					}
				}
			}

			if (specimenId == null)
			{
				specimenId = (Long.valueOf(request.getParameter(Constants.SPECIMEN_ID)));
				specimenLabel = request.getParameter(Constants.SPECIMEN_LABEL);
			}
			if (specimenId == null)
			{
				specimenId =(Long.valueOf(request.getParameter(Constants.SPECIMEN_ID)));
			}

			request.setAttribute(Constants.SPECIMEN_ID, specimenId.toString());

			final Object object = bizLogic.retrieve(Specimen.class.getName(), new Long(specimenId));

			if (object != null)
			{
				final Specimen specimen = (Specimen) object;
				if (specimenLabel == null)
				{
					specimenLabel = specimen.getLabel();
				}
				// Setting Specimen Event Parameters' Grid
				ActionApplication creationEvent=specimen.getCreationEventAction();
				dynamicEventsForGrid.clear();
				final Collection<ActionApplication> dynamicEventCollection = new NewSpecimenBizLogic().getDynamicEventColl(specimenId, bizLogic, null,dynamicEventsForGrid,null);


				/**
				 * Name: Chetan Patil Reviewer: Sachin Lale Bug ID: Bug#4180
				 * Patch ID: Bug#4180_1 Description: The values of event
				 * parameter is stored in a Map and in turn the Map is stored in
				 * a List. This is then sorted chronologically, using a date
				 * value form the Map. After sorting the List of Map is
				 * converted into the List of List, which is used on the UI for
				 * displaying values form List on the grid.
				 */
				final List<Map<String, Object>> gridData = new ArrayList<Map<String, Object>>();
				if (dynamicEventCollection != null)
				{
					for (final ActionApplication actionApp : dynamicEventCollection)
					{
						final Map<String, Object> rowDataMap = new HashMap<String, Object>();
						if (actionApp != null && !actionApp.getApplicationRecordEntry().getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
						{
							//final String[] events = EventsUtil
							//		.getEvent(eventParameters);
							long contId = actionApp.getApplicationRecordEntry().getFormContext()
									.getContainerId();
							List<Object> contList = AppUtility
									.executeSQLQuery("select caption from dyextn_container where identifier="
											+ contId);
							String container = (String) ((List<Object>) contList.get(0)).get(0);
							//final Object container =bizLogic.retrieve(Container.class.getName(),new Long(contId));
							if(actionApp.getSpecimen()!=null)
							{
							rowDataMap.put(Constants.SPECIMEN_LABEL, String.valueOf(actionApp.getSpecimen().getLabel()));
							}
							else
							{
								rowDataMap.put(Constants.SPECIMEN_LABEL,new SPPBizLogic().getScgNameFromActionApplicationId(actionApp.getId()) );
							}
							rowDataMap.put(Constants.EVENT_NAME,
									edu.wustl.cab2b.common.util.Utility
									.getFormattedString(container));

							// Ashish - 4/6/07 - retrieving User
							// User user = eventParameters.getUser();
							
							rowDataMap.put(Constants.EVENT_DATE, actionApp.getTimestamp());
							
							final User user = this.getUser(actionApp.getId(), bizLogic);

							rowDataMap.put(Constants.USER_NAME, user.getLastName() + "&#44; "
									+ user.getFirstName());
						
							rowDataMap.put(Constants.PAGE_OF, "pageOfDynamicEvent");// pageOf
							rowDataMap.put(Constants.SPECIMEN_ID, request
									.getAttribute(Constants.SPECIMEN_ID));// pageOf
							rowDataMap.put(Constants.ID,String.valueOf(actionApp.getId()));
							if(creationEvent!=null && actionApp.equals(creationEvent))
							{
								highLightCreationEvent(rowDataMap);
							}
							gridData.add(rowDataMap);
						}
					}
				}

				final Collection<SpecimenEventParameters> specimenEventCollection = this
						.getSpecimenEventParametersColl(specimenId, bizLogic);
				if (specimenEventCollection != null)
				{
					for (final SpecimenEventParameters eventParameters : specimenEventCollection)
					{
						final Map<String, Object> rowDataMap = new HashMap<String, Object>();
						if (eventParameters != null)
						{
							final String[] events = EventsUtil.getEvent(eventParameters);
							rowDataMap.put(Constants.SPECIMEN_LABEL, String.valueOf(eventParameters.getSpecimen().getLabel()));
							rowDataMap.put(Constants.EVENT_NAME, events[0]);

							// Ashish - 4/6/07 - retrieving User
							// User user = eventParameters.getUser();
							final User user = this.getUserOfStaticEvent(eventParameters.getId(), bizLogic);
							rowDataMap.put(Constants.EVENT_DATE, eventParameters.getTimestamp());
							rowDataMap.put(Constants.USER_NAME, user.getLastName() + ", "
									+ user.getFirstName());

							// rowDataMap.put(Constants.EVENT_DATE,
							// Utility.parseDateToString
							// (eventParameters.getTimestamp(),
							// Constants.TIMESTAMP_PATTERN)); // Sri: Changed
							// format for bug #463
							
							rowDataMap.put(Constants.PAGE_OF, events[1]);// pageOf
							rowDataMap.put(Constants.ID, String.valueOf(eventParameters.getId()));
							gridData.add(rowDataMap);
						}
					}


					final List<List<String>> gridDataList = this.getSortedGridDataList(gridData);
					final String[] columnList1 = Constants.EVENT_PARAMETERS_COLUMNS;
					final List<String> columnList = new ArrayList<String>();
					for (final String element : columnList1)
					{
						columnList.add(element);
					}
					AppUtility.setGridData(gridDataList, columnList, request);
					request.setAttribute(
							edu.wustl.simplequery.global.Constants.SPREADSHEET_DATA_LIST,
							gridDataList);
					final Integer identifierFieldIndex = new Integer(5);
					request.setAttribute("identifierFieldIndex", identifierFieldIndex.intValue());

				}
				if (request.getAttribute(Constants.SPECIMEN_LABEL) == null)
				{
					request.setAttribute(Constants.SPECIMEN_LABEL, specimenLabel);
				}
				String[] eventNames=new SPPBizLogic().getAllSPPEventFormNames(dynamicEventMap);
				String[] eventNameArr=new String[eventNames.length+2];
				for(int i=0;i<eventNames.length;i++)
				{
					eventNameArr[i]=eventNames[i];
				}
				int len=eventNames.length;
				eventNameArr[len]=Constants.TRANSFER_EVENT;
				eventNameArr[len+1]=Constants.DISPOSAL_EVENT;
				request.setAttribute(Constants.EVENT_PARAMETERS_LIST,eventNameArr );
				request.getSession().setAttribute("dynamicEventMap", dynamicEventMap);
			}
		}
		catch (final Exception e)
		{
			this.logger.error(e.getMessage(), e);
		}
		request.setAttribute(Constants.MENU_SELECTED, new String("15"));
		String pageOf = (String) request.getParameter(Constants.PAGE_OF);
		if (pageOf == null)
			pageOf = (String) request.getAttribute(Constants.PAGE_OF);
		request.setAttribute(Constants.PAGE_OF, pageOf);

		if (pageOf.equals(Constants.PAGE_OF_LIST_SPECIMEN_EVENT_PARAMETERS_CP_QUERY))
		{
			request.getSession().setAttribute("CPQuery", "CPQuery");
		}
		else
		{
			if (request.getSession().getAttribute("CPQuery") != null)
			{
				request.getSession().removeAttribute("CPQuery");
			}
		}
		Long specimenRecEntryEntityId = null;
		try
		{
			if (CatissueCoreCacheManager.getInstance().getObjectFromCache(
					AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID) != null)
			{
				specimenRecEntryEntityId = (Long) CatissueCoreCacheManager.getInstance()
						.getObjectFromCache(AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID);
			}
			else
			{
				specimenRecEntryEntityId = AnnotationUtil
						.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY);
				CatissueCoreCacheManager.getInstance().addObjectToCache(
						AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID, specimenRecEntryEntityId);
			}

		}
		catch (final Exception e)
		{
			this.logger.error(e.getMessage(), e);
		}
		// request.setAttribute("specimenEntityId", specimenEntityId);
		request.setAttribute(AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID,
				specimenRecEntryEntityId);
		return mapping.findForward(request.getParameter(Constants.PAGE_OF));
			}

	private void highLightCreationEvent(Map<String, Object> rowDataMap) {

		String startTag="<b id='creationId'>";
		String endTag="</b>";
		StringBuilder eventName=new StringBuilder(rowDataMap.remove(Constants.EVENT_NAME).toString());
		rowDataMap.put(Constants.EVENT_NAME,startTag+eventName+endTag);
	}

	/**
	 * @param form
	 * @param request
	 */
	private void saveActionErrors(ActionForm form, HttpServletRequest request)
	{
		final HttpSession session = request.getSession();
		final ActionErrors errors = (ActionErrors) session.getAttribute(Constants.ERRORS);
		if (errors != null)
		{
			saveErrors(request, errors);
			session.removeAttribute(Constants.ERRORS);
			ListSpecimenEventParametersForm specimenEventParametersForm = (ListSpecimenEventParametersForm) form;
			specimenEventParametersForm.setSpecimenEventParameter((String) session
					.getAttribute(Constants.EVENT_NAME));

			request.setAttribute(Constants.SPECIMEN_EVENT_PARAMETER, (String) session
					.getAttribute(Constants.EVENT_NAME));
			request.setAttribute(Constants.SPECIMEN_ID, (String) session
					.getAttribute(Constants.SPECIMEN_ID));
			request.setAttribute("eventSelected", (String) session
					.getAttribute(Constants.EVENT_NAME));
			session.removeAttribute(Constants.SPECIMEN_EVENT_PARAMETER);
			session.removeAttribute(Constants.SPECIMEN_LABLE);
		}
	}


	// Patch ID: Bug#4180_2
	/**
	 * This method sorts the List of the Map of grid data chronologically.
	 *
	 * @param gridData
	 *            List of the Map
	 * @return Sorted List of the List
	 */
	private List<List<String>> getSortedGridDataList(List<Map<String, Object>> gridData)
	{
		// Comparator to sort the List of Map chronologically.
		final Comparator<Map> EventDateComparator = new Comparator()
		{

			public int compare(Object object1, Object object2)
			{
				final Map<String, Object> rowDataMap1 = (Map<String, Object>) object1;
				final Date date1 = (Date) rowDataMap1.get(Constants.EVENT_DATE);

				final Map<String, Object> rowDataMap2 = (Map<String, Object>) object2;
				final Date date2 = (Date) rowDataMap2.get(Constants.EVENT_DATE);

				int value = 0;
				if (date1 != null && date2 != null && date1.before(date2))
				{
					value = -1;
				}
				else if (date1 != null && date2 != null && date1.after(date2))
				{
					value = 1;
				}

				return value;
			}
		};

		Collections.sort(gridData, EventDateComparator);

		final List<List<String>> gridDataList = this.getListOfRowData(gridData);
		return gridDataList;
	}

	/**
	 *
	 * @param gridData
	 *            List of Map
	 * @return List of values
	 */
	private List<List<String>> getListOfRowData(List<Map<String, Object>> gridData)
	{
		final List<List<String>> gridDataList = new ArrayList<List<String>>();
		for (final Map<String, Object> rowDataMap : gridData)
		{
			final List<String> rowData = new ArrayList<String>();

			final String label = (String) rowDataMap.get(Constants.SPECIMEN_LABEL);
			rowData.add(label);

			final String eventName = (String) rowDataMap.get(Constants.EVENT_NAME);
			rowData.add(eventName);

			final Date date = (Date) rowDataMap.get(Constants.EVENT_DATE);
			// String eventDate =
			// Utility.parseDateToString(date,Constants.TIMESTAMP_PATTERN ); //
			// Sri: Changed format for bug #463
			final String eventDate = CommonUtilities.parseDateToString(date, CommonServiceLocator
					.getInstance().getDatePattern()+Constants.SPACE_STR
					+ edu.wustl.catissuecore.util.global.Constants.TIMESTAMP_PATTERN_MM_SS);
			rowData.add(eventDate);
			
			final String userName = (String) rowDataMap.get(Constants.USER_NAME);
			rowData.add(userName);

			final String paggeOf = (String) rowDataMap.get(Constants.PAGE_OF);
			rowData.add(paggeOf);
			
			final String eventId = (String) rowDataMap.get(Constants.ID);
			rowData.add(eventId);


			gridDataList.add(rowData);
		}
		return gridDataList;
	}

	/**
	 *
	 * @param eventId
	 *            : eventId
	 * @param bizLogic
	 *            : bizLogic
	 * @return User : User
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private User getUser(Long eventId, IBizLogic bizLogic) throws BizLogicException
	{
		final String[] selectColumnName = {"performedBy"};
		final String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER};
		final String[] whereColumnCondition = {"="};
		final Object[] whereColumnValue = {eventId};
		final String sourceObjectName = AbstractApplication.class.getName();

		final List userCollection = bizLogic.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue,
				Constants.AND_JOIN_CONDITION);

		final User user = (User) userCollection.get(0);
		return user;
	}

	/**
	 *
	 * @param eventId : eventId
	 * @param bizLogic : bizLogic
	 * @return User : User
	 * @throws BizLogicException : BizLogicException
	 */
	private User getUserOfStaticEvent(Long eventId, IBizLogic bizLogic) throws BizLogicException
	{
		final String[] selectColumnName = {"user"};
		final String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER};
		final String[] whereColumnCondition = {"="};
		final Object[] whereColumnValue = {eventId};
		final String sourceObjectName = SpecimenEventParameters.class.getName();

		final List userCollection = bizLogic.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue,
				Constants.AND_JOIN_CONDITION);

		final User user = (User) userCollection.get(0);
		return user;
	}


	/**
	 *
	 * @param specimenId
	 *            : specimenId
	 * @param bizLogic
	 *            : bizLogic
	 * @return Collection : Collection
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private Collection<SpecimenEventParameters> getSpecimenEventParametersColl(Long specimenId,
			IBizLogic bizLogic) throws BizLogicException
			{
		final String className = SpecimenEventParameters.class.getName();
		final String columnName = Constants.COLUMN_NAME_SPECIMEN_ID;
		final Long columnValue = specimenId;
		final Collection<SpecimenEventParameters> specimenEventCollection = bizLogic.retrieve(
				className, columnName, columnValue);

		return specimenEventCollection;
			}

	

	private Collection<Container> getContainer(String contId, IBizLogic bizLogic)
			throws BizLogicException
			{
		final String className = Container.class.getName();
		final String columnName = "id";
		final Long columnValue = new Long(contId);
		final Collection<Container> containerCollection = bizLogic.retrieve(className, columnName,
				columnValue);

		return containerCollection;
			}
}