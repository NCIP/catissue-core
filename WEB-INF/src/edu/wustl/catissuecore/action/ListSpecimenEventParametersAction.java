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

import edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.CellSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.EventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.ProcedureEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpunEventParameters;
import edu.wustl.catissuecore.domain.ThawEventParameters;
import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

public class ListSpecimenEventParametersAction extends SecureAction
{
	/**
	 * Overrides the execute method of Action class.
	 * Initializes the various fields in SpecimenEventParameters.jsp Page.
	 * */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
		//        //Gets the value of the operation parameter.
		//        String operation = request.getParameter(Constants.OPERATION);
		//        
		//        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
		//        request.setAttribute(Constants.OPERATION, operation);
		try
		{
			IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);

			//*************  ForwardTo implementation *************
			//forwarded from Specimen page
			HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");

			String specimenId = null;
			String specimenLabel = null;
			String specimenClass = null;
			//-------------Mandar 04-July-06 QuickEvents start
			String fromQuickEvent = (String) request.getAttribute("isQuickEvent");
			if(fromQuickEvent==null)
			{
				fromQuickEvent = (String) request.getParameter("isQuickEvent");
			}
			String eventSelected = "";
			if (fromQuickEvent != null)
			{
				specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
				eventSelected = (String) request.getAttribute(Constants.EVENT_SELECTED);
				if(eventSelected==null&&forwardToHashMap!=null)
				{
					specimenClass=(String) forwardToHashMap.get("specimenClass");
					specimenId = (String) forwardToHashMap.get("specimenId");
					if(specimenClass.equalsIgnoreCase("Tissue"))
					{
						eventSelected = Constants.EVENT_PARAMETERS[14];
					}
					else if(specimenClass.equalsIgnoreCase("Molecular"))
					{
						eventSelected = Constants.EVENT_PARAMETERS[9];
					}
					else if(specimenClass.equalsIgnoreCase("Cell"))
					{
						eventSelected = Constants.EVENT_PARAMETERS[1];
					}
					else if(specimenClass.equalsIgnoreCase("Fluid"))
					{
						eventSelected = Constants.EVENT_PARAMETERS[7];
					}
					
				}
				request.setAttribute(Constants.EVENT_SELECTED, eventSelected);
				//            	System.out.println("::::::::::::::\n\n\n\n"+form.getClass().getName()   );  
				ListSpecimenEventParametersForm eventForm = (ListSpecimenEventParametersForm) form;
				if(eventForm==null)
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
				if(eventId!=null)
				{
					Logger.out.debug("Event ID added===>" + eventId);
					//Retrieving list of SpecimenEvents for added SpecimenEventId
					List specimenEventList = bizLogic.retrieve(SpecimenEventParameters.class.getName(), Constants.SYSTEM_IDENTIFIER, eventId);
	
					if (specimenEventList != null && specimenEventList.size() != 0)
					{
						//Getting object of specimenEventParameters from the list of SepcimenEvents
						SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) specimenEventList.get(0);
	
						//getting SpecimenId of SpecimenEventParameters
						Specimen specimen = specimenEventParameters.getSpecimen();
						specimenId = specimen.getId().toString();
					//	specimenLabel = specimen.getLabel();
						Logger.out.debug("Specimen of Event Added====>" + (specimenEventParameters.getSpecimen()).getId());
					}
				}
			}

			if (specimenId == null)
			{
				specimenId = (String) request.getParameter(Constants.SPECIMEN_ID);
				specimenLabel = (String) request.getParameter(Constants.SPECIMEN_LABEL);
			}

			request.setAttribute(Constants.SPECIMEN_ID, specimenId);
				

			List specimenList = bizLogic.retrieve(Specimen.class.getName(), Constants.SYSTEM_IDENTIFIER, specimenId);

			if (specimenList != null && specimenList.size() != 0)
			{
				Specimen specimen = (Specimen) specimenList.get(0);
				if(specimenLabel == null)
				{
					specimenLabel = specimen.getLabel();					
				}
				//Setting Specimen Event Parameters' Grid   
				
				//Ashish - 4/6/07 --- Since lazy=true, retriving the events collection. 
				Collection<EventParameters> specimenEventCollection = getSpecimenEventParametersColl(specimenId,bizLogic);
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
					List<Map<String, Object>> gridData = new ArrayList<Map<String, Object>>();
					
					for(EventParameters eventParameters : specimenEventCollection)
					{
						Map<String, Object> rowDataMap = new HashMap<String, Object>();
						if (eventParameters != null)
						{
							String[] events = getEvent(eventParameters);
							rowDataMap.put(Constants.ID, String.valueOf(eventParameters.getId()));
							rowDataMap.put(Constants.EVENT_NAME, events[0]);//Event Name

							//Ashish - 4/6/07 - retrieving User
							//User user = eventParameters.getUser();							
							User user = getUser(eventParameters.getId(),bizLogic);
							
							rowDataMap.put(Constants.USER_NAME, user.getLastName() + ", " + user.getFirstName());

							//rowDataMap.put(Constants.EVENT_DATE, Utility.parseDateToString(eventParameters.getTimestamp(), Constants.TIMESTAMP_PATTERN)); // Sri: Changed format for bug #463
							rowDataMap.put(Constants.EVENT_DATE, eventParameters.getTimestamp());
							rowDataMap.put(Constants.PAGE_OF, events[1]);//pageOf
							gridData.add(rowDataMap);
						}
					}
					
					List<List<String>> gridDataList = getSortedGridDataList(gridData);
					request.setAttribute(Constants.SPREADSHEET_DATA_LIST, gridDataList);
				}
			}
			if(request.getAttribute(Constants.SPECIMEN_LABEL)==null)
			{
				request.setAttribute(Constants.SPECIMEN_LABEL, specimenLabel);
			}
			request.setAttribute(Constants.EVENT_PARAMETERS_LIST, Constants.EVENT_PARAMETERS);
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(), e);
		}
		request.setAttribute(Constants.MENU_SELECTED, new String("15"));
		String pageOf = request.getParameter(Constants.PAGEOF);
		request.setAttribute(Constants.PAGEOF, pageOf);

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

		return mapping.findForward((String) request.getParameter(Constants.PAGEOF));
	}

	// Patch ID: Bug#4180_2
	/**
	 * This method sorts the List of the Map of grid data chronologically
	 * @param gridData List of the Map
	 * @return Sorted List of the List
	 */
	private List<List<String>> getSortedGridDataList(List<Map<String, Object>> gridData) 
	{
		//Comparator to sort the List of Map chronologically.
		final Comparator EventDateComparator = new Comparator() 
		{
			public int compare(Object object1, Object object2)
			{
				Map<String, Object> rowDataMap1 = (Map<String, Object>)object1;
				Date date1 = (Date)rowDataMap1.get(Constants.EVENT_DATE);
								
				Map<String, Object> rowDataMap2 = (Map<String, Object>)object2;
				Date date2 = (Date)rowDataMap2.get(Constants.EVENT_DATE);
				
				int value = 0;
				if (date1!=null&&date2!=null&&date1.before(date2))
				{
					value = -1;
				}
				else if (date1!=null&&date2!=null&&date1.after(date2))
				{
					value = 1;
				}
				
				return value;
		    }
		};
		
		Collections.sort(gridData, EventDateComparator);
		
		List<List<String>> gridDataList = getListOfRowData(gridData);
		return gridDataList;
	}

	/**
	 * This method converts List of Map<key, value> into List of values
	 * @param gridData List of Map
	 * @return List of values
	 */
	private List<List<String>> getListOfRowData(List<Map<String, Object>> gridData) 
	{
		List<List<String>> gridDataList = new ArrayList<List<String>>();
		for(Map<String, Object> rowDataMap : gridData)
		{
			List<String> rowData = new ArrayList<String>();
			
			String eventId = (String)rowDataMap.get(Constants.ID);
			rowData.add(eventId);
			
			String eventName = (String)rowDataMap.get(Constants.EVENT_NAME);
			rowData.add(eventName);
			
			String userName = (String)rowDataMap.get(Constants.USER_NAME);
			rowData.add(userName);
			
			Date date = (Date)rowDataMap.get(Constants.EVENT_DATE);
			String eventDate = Utility.parseDateToString(date, Constants.TIMESTAMP_PATTERN); // Sri: Changed format for bug #463
			rowData.add(eventDate);
			
			String paggeOf = (String)rowDataMap.get(Constants.PAGE_OF);
			rowData.add(paggeOf);
			
			gridDataList.add(rowData);
		}
		return gridDataList;
	}

	private String[] getEvent(EventParameters eventParameters)
	{
		String[] events = new String[2];

		if (eventParameters instanceof CellSpecimenReviewParameters)
		{
			events[0] = "Cell Specimen Review";
			events[1] = "pageOfCellSpecimenReviewParameters";
		}
		else if (eventParameters instanceof CheckInCheckOutEventParameter)
		{
			events[0] = "Check In Check Out";
			events[1] = "pageOfCheckInCheckOutEventParameters";
		}
		else if (eventParameters instanceof CollectionEventParameters)
		{
			events[0] = "Collection";
			events[1] = "pageOfCollectionEventParameters";
		}
		else if (eventParameters instanceof DisposalEventParameters)
		{
			events[0] = "Disposal";
			events[1] = "pageOfDisposalEventParameters";
		}
		else if (eventParameters instanceof EmbeddedEventParameters)
		{
			events[0] = "Embedded";
			events[1] = "pageOfEmbeddedEventParameters";
		}
		else if (eventParameters instanceof FixedEventParameters)
		{
			events[0] = "Fixed";
			events[1] = "pageOfFixedEventParameters";
		}
		else if (eventParameters instanceof FluidSpecimenReviewEventParameters)
		{
			events[0] = "Fluid Specimen Review";
			events[1] = "pageOfFluidSpecimenReviewParameters";
		}
		else if (eventParameters instanceof FrozenEventParameters)
		{
			events[0] = "Frozen";
			events[1] = "pageOfFrozenEventParameters";
		}
		else if (eventParameters instanceof MolecularSpecimenReviewParameters)
		{
			events[0] = "Molecular Specimen Review";
			events[1] = "pageOfMolecularSpecimenReviewParameters";
		}
		else if (eventParameters instanceof ProcedureEventParameters)
		{
			events[0] = "Procedure Event";
			events[1] = "pageOfProcedureEventParameters";
		}
		else if (eventParameters instanceof ReceivedEventParameters)
		{
			events[0] = "Received Event";
			events[1] = "pageOfReceivedEventParameters";
		}
		else if (eventParameters instanceof SpunEventParameters)
		{
			events[0] = "Spun";
			events[1] = "pageOfSpunEventParameters";
		}
		else if (eventParameters instanceof ThawEventParameters)
		{
			events[0] = "Thaw";
			events[1] = "pageOfThawEventParameters";
		}
		else if (eventParameters instanceof TissueSpecimenReviewEventParameters)
		{
			events[0] = "Tissue Specimen Review";
			events[1] = "pageOfTissueSpecimenReviewParameters";
		}
		else if (eventParameters instanceof TransferEventParameters)
		{
			events[0] = "Transfer";
			events[1] = "pageOfTransferEventParameters";
		}

		return events;
	}

	/**
	 * @param eventId
	 * @param bizLogic
	 * @return
	 * @throws DAOException
	 * Retriving the User from SpecimenEventParameters
	 */
	private User getUser(Long eventId,IBizLogic bizLogic)throws DAOException
	{
		String[] selectColumnName = {"user"};
		String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnCondition = {"="};
		Object[] whereColumnValue = {eventId};
		String sourceObjectName = SpecimenEventParameters.class.getName();
		
		List userCollection = bizLogic.retrieve(sourceObjectName,selectColumnName,
	            whereColumnName, whereColumnCondition,
	            whereColumnValue,Constants.AND_JOIN_CONDITION );
		
		User user = (User)userCollection.get(0);
		return user;
	}
	/**
	 * @param specimenId
	 * @param bizLogic
	 * @return
	 * @throws DAOException
	 * Retriving eventsCollection from Specimen
	 */
	private Collection getSpecimenEventParametersColl(String specimenId,IBizLogic bizLogic)throws DAOException
	{
		String className = SpecimenEventParameters.class.getName();
		String columnName = Constants.COLUMN_NAME_SPECIMEN_ID;
		Object columnValue = specimenId;
		Collection<EventParameters> specimenEventCollection = bizLogic.retrieve(className,columnName, columnValue);
		
		return specimenEventCollection;
	}
}