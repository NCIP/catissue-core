/**
 * <p>Title: ListSpecimenEventParametersAction Class>
 * <p>Description:	This class initializes the fields of SpecimenEventParameters.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm;
import edu.wustl.catissuecore.bizlogic.AnnotationUtil;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.User;
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
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;

public class ListSpecimenEventParametersAction extends SecureAction
{

	private transient Logger logger = Logger
			.getCommonLogger(ListSpecimenEventParametersAction.class);

	/**
	 * Overrides the execute method of Action class.
	 * Initializes the various fields in SpecimenEventParameters.jsp Page.
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws IOException I/O exception
	 * @throws ServletException servlet exception
	 * @return value for ActionForward object
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{
		//        //Gets the value of the operation parameter.
		//        String operation = request.getParameter(Constants.OPERATION);
		//        
		//        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
		//        request.setAttribute(Constants.OPERATION, operation);
		try
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);

			//*************  ForwardTo implementation *************
			//forwarded from Specimen page
			HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");

			String specimenId = null;
			String specimenLabel = null;
			String specimenClass = null;
			//-------------Mandar 04-July-06 QuickEvents start
			String fromQuickEvent = (String) request.getAttribute("isQuickEvent");
			if (fromQuickEvent == null)
			{
				fromQuickEvent = (String) request.getParameter("isQuickEvent");
			}
			String eventSelected = "";
			if (fromQuickEvent != null)
			{
				specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
				eventSelected = (String) request.getAttribute(Constants.EVENT_SELECTED);
				if (eventSelected == null && forwardToHashMap != null)
				{
					specimenClass = (String) forwardToHashMap.get("specimenClass");
					specimenId = (String) forwardToHashMap.get("specimenId");
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
				//            	System.out.println("::::::::::::::\n\n\n\n"+form.getClass().getName()   );  
				ListSpecimenEventParametersForm eventForm = (ListSpecimenEventParametersForm) form;
				if (eventForm == null)
				{
					eventForm = new ListSpecimenEventParametersForm();
				}
				eventForm.setSpecimenEventParameter(eventSelected);
				eventForm.setSpecimenId(specimenId.trim());

			}
			//-------------Mandar 04-July-06 QuickEvents end
			if (forwardToHashMap != null)
			{
				//Fetching SpecimenId from HashMap generated by ForwardToProcessor
				specimenId = (String) forwardToHashMap.get("specimenId");
				specimenLabel = (String) forwardToHashMap.get(Constants.SPECIMEN_LABEL);
				Logger.out.debug("SpecimenID found in forwardToHashMap========>>>>>>" + specimenId);
			}
			//*************  ForwardTo implementation *************

			//If new SpecimenEvent is added, specimenId in forwardToHashMap will be null, following code handles that case
			if (specimenId == null)
			{
				String eventId = (String) request.getParameter("eventId");

				// Added by Vijay Pande. While cliking on events tab both specimenId and eventId are getting null. Since there was no check on eventId it was throwing error for following retrieve call.
				// Null check is added for eventId. Fix for bug Id: 4731
				if (eventId != null)
				{
					Logger.out.debug("Event ID added===>" + eventId);
					//Retrieving list of SpecimenEvents for added SpecimenEventId
					Object object = bizLogic.retrieve(SpecimenEventParameters.class.getName(),
							new Long(eventId));

					if (object != null)
					{
						//Getting object of specimenEventParameters from the list of SepcimenEvents
						SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) object;

						//getting SpecimenId of SpecimenEventParameters
						AbstractSpecimen specimen = specimenEventParameters.getSpecimen();
						specimenId = specimen.getId().toString();
						//	specimenLabel = specimen.getLabel();
						Logger.out.debug("Specimen of Event Added====>"
								+ (specimenEventParameters.getSpecimen()).getId());
					}
				}
			}

			if (specimenId == null)
			{
				specimenId = (String) request.getParameter(Constants.SPECIMEN_ID);
				specimenLabel = (String) request.getParameter(Constants.SPECIMEN_LABEL);
			}

			request.setAttribute(Constants.SPECIMEN_ID, specimenId);

			Object object = bizLogic.retrieve(Specimen.class.getName(), new Long(specimenId));

			if (object != null)
			{
				Specimen specimen = (Specimen) object;
				if (specimenLabel == null)
				{
					specimenLabel = specimen.getLabel();
				}
				//Setting Specimen Event Parameters' Grid   

				//Ashish - 4/6/07 --- Since lazy=true, retriving the events collection. 
				Collection < SpecimenEventParameters > specimenEventCollection = getSpecimenEventParametersColl(
						specimenId, bizLogic);
				/**
				 * Name: Chetan Patil
				 * Reviewer: Sachin Lale
				 * Bug ID: Bug#4180
				 * Patch ID: Bug#4180_1
				 * Description: The values of event parameter is stored in a Map and in turn the Map is stored in a List.
				 * This is then sorted chronologically, using a date value form the Map. After sorting the List of Map is 
				 * converted into the List of List, which is used on the UI for displaying values form List on the grid.
				 */
				if (specimenEventCollection != null)
				{
					List < Map < String , Object >> gridData = new ArrayList < Map < String , Object >>();

					for (SpecimenEventParameters eventParameters : specimenEventCollection)
					{
						Map < String , Object > rowDataMap = new HashMap < String , Object >();
						if (eventParameters != null)
						{
							String[] events = EventsUtil.getEvent(eventParameters);
							rowDataMap.put(Constants.ID, String.valueOf(eventParameters.getId()));
							rowDataMap.put(Constants.EVENT_NAME, events[0]);//Event Name

							//Ashish - 4/6/07 - retrieving User
							//User user = eventParameters.getUser();							
							User user = getUser(eventParameters.getId(), bizLogic);

							rowDataMap.put(Constants.USER_NAME, user.getLastName() + ", "
									+ user.getFirstName());

							//rowDataMap.put(Constants.EVENT_DATE, Utility.parseDateToString(eventParameters.getTimestamp(), Constants.TIMESTAMP_PATTERN)); // Sri: Changed format for bug #463
							rowDataMap.put(Constants.EVENT_DATE, eventParameters.getTimestamp());
							rowDataMap.put(Constants.PAGE_OF, events[1]);//pageOf
							gridData.add(rowDataMap);
						}
					}

					List < List < String >> gridDataList = getSortedGridDataList(gridData);
					String[] columnList1 = Constants.EVENT_PARAMETERS_COLUMNS;
					List columnList = new ArrayList();
					for (int i = 0; i < columnList1.length; i++)
					{
						columnList.add(columnList1[i]);
					}
					AppUtility.setGridData(gridDataList, columnList, request);
					request.setAttribute(
							edu.wustl.simplequery.global.Constants.SPREADSHEET_DATA_LIST,
							gridDataList);
					Integer identifierFieldIndex = new Integer(0);
					request.setAttribute("identifierFieldIndex", identifierFieldIndex.intValue());
				}
			}
			if (request.getAttribute(Constants.SPECIMEN_LABEL) == null)
			{
				request.setAttribute(Constants.SPECIMEN_LABEL, specimenLabel);
			}
			request.setAttribute(Constants.EVENT_PARAMETERS_LIST, Constants.EVENT_PARAMETERS);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		request.setAttribute(Constants.MENU_SELECTED, new String("15"));
		String pageOf = request.getParameter(Constants.PAGE_OF);
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
		Long specimenEntityId = null;
		try
		{
			if (CatissueCoreCacheManager.getInstance().getObjectFromCache("specimenEntityId") != null)
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
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		request.setAttribute("specimenEntityId", specimenEntityId);
		return mapping.findForward((String) request.getParameter(Constants.PAGE_OF));
	}

	// Patch ID: Bug#4180_2
	/**
	 * This method sorts the List of the Map of grid data chronologically
	 * @param gridData List of the Map
	 * @return Sorted List of the List
	 */
	private List < List < String >> getSortedGridDataList(List < Map < String , Object >> gridData)
	{
		//Comparator to sort the List of Map chronologically.
		final Comparator EventDateComparator = new Comparator()
		{

			public int compare(Object object1, Object object2)
			{
				Map < String , Object > rowDataMap1 = (Map < String , Object >) object1;
				Date date1 = (Date) rowDataMap1.get(Constants.EVENT_DATE);

				Map < String , Object > rowDataMap2 = (Map < String , Object >) object2;
				Date date2 = (Date) rowDataMap2.get(Constants.EVENT_DATE);

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

		List < List < String >> gridDataList = getListOfRowData(gridData);
		return gridDataList;
	}

	/**
	 * This method converts List of Map<key, value> into List of values
	 * @param gridData List of Map
	 * @return List of values
	 */
	private List < List < String >> getListOfRowData(List < Map < String , Object >> gridData)
	{
		List < List < String >> gridDataList = new ArrayList < List < String >>();
		for (Map < String , Object > rowDataMap : gridData)
		{
			List < String > rowData = new ArrayList < String >();

			String eventId = (String) rowDataMap.get(Constants.ID);
			rowData.add(eventId);

			String eventName = (String) rowDataMap.get(Constants.EVENT_NAME);
			rowData.add(eventName);

			String userName = (String) rowDataMap.get(Constants.USER_NAME);
			rowData.add(userName);

			Date date = (Date) rowDataMap.get(Constants.EVENT_DATE);
			//String eventDate = Utility.parseDateToString(date,Constants.TIMESTAMP_PATTERN ); // Sri: Changed format for bug #463
			String eventDate = edu.wustl.common.util.Utility.parseDateToString(date,
					CommonServiceLocator.getInstance().getDatePattern()
							+ edu.wustl.catissuecore.util.global.Constants.TIMESTAMP_PATTERN_MM_SS); // Sri: Changed format for bug #463
			rowData.add(eventDate);

			String paggeOf = (String) rowDataMap.get(Constants.PAGE_OF);
			rowData.add(paggeOf);

			gridDataList.add(rowData);
		}
		return gridDataList;
	}

	/**
	 * @param eventId
	 * @param bizLogic
	 * @return
	 * @throws DAOException
	 * Retriving the User from SpecimenEventParameters
	 */
	private User getUser(Long eventId, IBizLogic bizLogic) throws BizLogicException
	{
		String[] selectColumnName = {"user"};
		String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnCondition = {"="};
		Object[] whereColumnValue = {eventId};
		String sourceObjectName = SpecimenEventParameters.class.getName();

		List userCollection = bizLogic.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue,
				Constants.AND_JOIN_CONDITION);

		User user = (User) userCollection.get(0);
		return user;
	}

	/**
	 * @param specimenId
	 * @param bizLogic
	 * @return
	 * @throws DAOException
	 * Retriving eventsCollection from Specimen
	 */
	private Collection getSpecimenEventParametersColl(String specimenId, IBizLogic bizLogic)
			throws BizLogicException
	{
		String className = SpecimenEventParameters.class.getName();
		String columnName = Constants.COLUMN_NAME_SPECIMEN_ID;
		Long columnValue = new Long(specimenId);
		Collection < SpecimenEventParameters > specimenEventCollection = bizLogic.retrieve(
				className, columnName, columnValue);

		return specimenEventCollection;
	}
}