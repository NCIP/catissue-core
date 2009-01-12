
/**
 * <p>Title: UpdateStatusAction Class>
 * <p>Description:	UpdateStatusAction displays updated status values.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ramya Nagraj
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bean.DefinedArrayRequestBean;
import edu.wustl.catissuecore.bean.ExistingArrayDetailsBean;
import edu.wustl.catissuecore.bean.RequestDetailsBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;

/**
 * This class updates the status of all ordered specimens in one go.
 * @author ramya_nagraj
 *
 */

public class UpdateStatusAction extends BaseAction
{
	 /**
     * Overrides the execute method in Action class.
     * @param mapping ActionMapping object
     * @param form ActionForm object
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @return ActionForward object
     * @throws Exception object
     */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String updateValue = "";
		
		updateValue = request.getParameter("updateValue");
		
		List responseList = new ArrayList();
		List responseListForExistingArray = new ArrayList();
		List responseListForDefinedArray = new ArrayList();
		
		HttpSession session = request.getSession();

		if(session.getAttribute(Constants.REQUEST_DETAILS_LIST) != null)
		{
			List requestDetailsList = (ArrayList)session.getAttribute(Constants.REQUEST_DETAILS_LIST);
			responseList = constructResponseListForRequestDetailsList(requestDetailsList,updateValue);
			sendResponseString(responseList,response);
	    }
		if(session.getAttribute(Constants.DEFINEDARRAY_REQUESTS_LIST) != null && session.getAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST) != null)
		{
			List definedArrayRequestMapList = (ArrayList)session.getAttribute(Constants.DEFINEDARRAY_REQUESTS_LIST);
			List existingArrayRequestList = (ArrayList)session.getAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST);
			responseListForDefinedArray = constructResponseListForDefinedArray(definedArrayRequestMapList,updateValue);
			responseListForExistingArray = constructResponseListForExistingArray(existingArrayRequestList,updateValue);
			sendResponseString(responseListForDefinedArray,responseListForExistingArray,response);
			return null;
		}
		if(session.getAttribute(Constants.DEFINEDARRAY_REQUESTS_LIST) != null)
		{
			List definedArrayRequestMapList = (ArrayList)session.getAttribute(Constants.DEFINEDARRAY_REQUESTS_LIST);
			responseListForDefinedArray = constructResponseListForDefinedArray(definedArrayRequestMapList,updateValue);
			sendResponseString(responseListForDefinedArray,responseListForExistingArray,response);
			return null;
		}
		if(session.getAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST) != null)
		{
			List existingArrayRequestList = (ArrayList)session.getAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST);
			responseListForExistingArray = constructResponseListForExistingArray(existingArrayRequestList,updateValue);
			sendResponseString(responseListForDefinedArray,responseListForExistingArray,response);
			return null;
		}
		
		return null;
	}
	
	/**
	 * This function sends the response string to the browser
	 * @param responseList List containing the statuses of individual order items
	 * @param response HttpServletResponse object
	 */
	private void sendResponseString(List responseList,HttpServletResponse response)
	{
		String responseString = makeOutputString(responseList);
		try
		{
			PrintWriter out = response.getWriter();
			out.print(responseString);
		}
		catch(IOException ie)
		{
			Logger.out.error("Error occurred while sending response string for update status - " + ie.getMessage());
		}
	}
	
	/**
	 * This function sends the response string to the browser
	 * @param responseList List containing the statuses of defined arrays
	 * @param responseListForExistingArray List containing the statuses of existing arrays
	 * @param response HttpServletResponse object
	 */
	private void sendResponseString(List responseList,List responseListForExistingArray,HttpServletResponse response)
	{
		String responseString = makeOutputString(responseList,responseListForExistingArray);
		try
		{
			PrintWriter out = response.getWriter();
			out.print(responseString);
		}
		catch(IOException ie)
		{
			Logger.out.error("Error occurred while sending response string for update status - " + ie.getMessage());
		}
	}
	
	/**
	 * This function constructs updated response list for the defined arrays in ArrayRequests.jsp
	 * @param definedArrayRequestMapList List containing the map of defined arrays
	 * @param updateValue String containing new status value to be updated
	 * @return responseList List containing the new status of defined arrays
	 */
	private List constructResponseListForDefinedArray(List definedArrayRequestMapList,String updateValue)
	{
		List responseList = new ArrayList();
		DefinedArrayRequestBean definedArrayRequestBean = new DefinedArrayRequestBean();
			
		Iterator definedArrayRequestMapListItr = definedArrayRequestMapList.iterator();
		int i=0;
		while(definedArrayRequestMapListItr.hasNext())
		{
			Map defineArrayMap=(Map)definedArrayRequestMapListItr.next();
			Set defineArraySet = (Set) defineArrayMap.keySet();
			Iterator defineArraySetItr = defineArraySet.iterator();
			//Set has only one element that is,DefineArrayRequestBean instance
			definedArrayRequestBean = (DefinedArrayRequestBean)defineArraySetItr.next();
			List arrayStatusCollec = (ArrayList)definedArrayRequestBean.getArrayStatusList();
			List newStatusList =  constructNewStatusList(arrayStatusCollec);
			if(newStatusList.contains(updateValue))
			{
				newStatusList.remove(updateValue);
				//To display updated new value in the first option of <html:select>
				newStatusList.add(0,updateValue);
				
				/*index needed to identify the rows.
				Add individual collection in responseList*/
				responseList.add(i,newStatusList);				
			}
			else
			{								
				responseList.add(i,newStatusList);//list of beans sent in the response.
			}
			
			i++;
		}
		return responseList;
	}
	
	/**
	 * This function constructs updated response list for the order items in ExistingArray in the ArrayRequests.jsp
	 * @param existingArrayRequestList List containing the statuses of individual existing arrays
	 * @param updateValue String containing new status value to be updated
	 * @return responseList List containing the new status list for all existing arrays
	 */
	private List constructResponseListForExistingArray(List existingArrayRequestList,String updateValue)
	{
		List responseList = new ArrayList();
		ExistingArrayDetailsBean existingArrayDetailsBean = new ExistingArrayDetailsBean();
		
		Iterator existingArrayRequestListItr = existingArrayRequestList.iterator();
		int i=0;
		
		while(existingArrayRequestListItr.hasNext())
		{
			existingArrayDetailsBean = (ExistingArrayDetailsBean)existingArrayRequestListItr.next();
					
			List orderItemStatusCollec = (ArrayList)existingArrayDetailsBean.getItemStatusList();
			List newStatusList = constructNewStatusList(orderItemStatusCollec);
			
			if(newStatusList.contains(updateValue))
			{
				newStatusList.remove(updateValue);
				//To display updated new value in the first option of <html:select>
				newStatusList.add(0,updateValue);
				
				/*index needed to identify the rows.
				Add individual collection in responseList*/
				responseList.add(i,newStatusList);				
			}
			else
			{								
				responseList.add(i,newStatusList);//list of beans sent in the response.
			}
			i++;
		}
		return responseList;
	}
	
	/**
	 * This function constructs updated response list for the order items in RequestDetails.jsp
	 * @param requestDetailsList ArrayList containing the statuses of all specimens
	 * @param updateValue String containing new status value to be updated.This is obtained from the request.
	 * @return List ArrayList containing lists of new statuses of all specimens
	 */
	private List constructResponseListForRequestDetailsList(List requestDetailsList,String updateValue)
	{
		List responseList = new ArrayList();
		RequestDetailsBean requestDetailsBean = new RequestDetailsBean();
		
		Iterator requestDetailsListItr = requestDetailsList.iterator();
		int i=0;
		
		while(requestDetailsListItr.hasNext())
		{
			requestDetailsBean = (RequestDetailsBean)requestDetailsListItr.next();
					
			List orderItemStatusCollec = (ArrayList)requestDetailsBean.getItemsStatusList();
			List newStatusList = constructNewStatusList(orderItemStatusCollec);
			
			if(!(requestDetailsBean.getAssignedStatus().equals(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)) && newStatusList.contains(updateValue))
			{
				newStatusList.remove(updateValue);
				//To display updated new value in the first option of <html:select>
				newStatusList.add(0,updateValue);
				
				/*index needed to identify the rows.
				Add individual collection in responseList*/
				responseList.add(i,newStatusList);				
			}
			else
			{		
				newStatusList.remove(requestDetailsBean.getAssignedStatus());
				newStatusList.add(0,requestDetailsBean.getAssignedStatus());
				responseList.add(i,newStatusList);//list of beans sent in the response.
			}
			i++;
		}
		return responseList;
	}
	
	/**
	 * This function accepts current item status list of individual order items which is in the form of
	 * name-value form.It parses them and returns a list containing only the status values.
	 * @param currentStatusList List containing the current status list of order items
	 * @return newStatusList ArrayList containing new statuses of individual order items
	 */
	private List constructNewStatusList(List currentStatusList)
	{
		//newStatusList contains status of individual order item
		List newStatusList = new ArrayList();
		
		for(Iterator itr = currentStatusList.iterator();itr.hasNext();)
		{
			//The status value contains string in the form of name:<option> value:<option>
			String statusValue = itr.next().toString();
				
			int firstIndex = statusValue.lastIndexOf(":"); //Last Index of ":".
			int lastIndex = statusValue.length();		   //Length of String.		
			statusValue = statusValue.substring(firstIndex+1,lastIndex);
				
			newStatusList.add(statusValue);   //Add the values to Collection.
		}
		return newStatusList;
	}
	
	/**
	 * This function converts the list of objects in responseList(ArrayList) into a String form.
	 * @param responseList ArrayList containing the list of RequestDetailsBean instances.
	 * @return String containing the response text.
	 */
	private String makeOutputString(List responseList)
	{
		int i=0;
		StringBuffer responseStrBuffer = new StringBuffer();
		List statusList = new ArrayList();
		//Use ';' delimiter to separate between each row.
		while(i<responseList.size())
		{	
			statusList = (List)responseList.get(i);
			//Loop for each row.
			for(int j=0;j<statusList.size();j++)
			{
				String responseString = (String) statusList.get(j);

				//Separate OptionText and OptionValue with '|'
				responseStrBuffer.append(responseString).append("|").append(responseString);
				
				//Separate individual options by '||'
				responseStrBuffer.append("||");
				
			}
			responseStrBuffer.append(";");
			//Separate each row with ';'.
			i++;
		}
		String responseString = responseStrBuffer.toString();
		
		int lastOptionSeparator = responseString.lastIndexOf("||");
		
		responseString = responseString.substring(0,lastOptionSeparator);
		
		responseString = responseString + ";";
			
		return responseString;
	}
	
	/**
	 * This function contructs a string containing the statuses of defined arrays and existing arrays 
	 * which are separated by a delimiter
	 * @param responseListForDefinedArray ArrayList containing the new statuslist of defined arrays 
	 * @param responseListForExistingArray ArrayList containing the new status list of existing arrays
	 * @return responseString String containing the statuses of defined arrays and existing arrays 
	 * separated by a delimiter
	 */
	private String makeOutputString(List responseListForDefinedArray,List responseListForExistingArray)
	{
		int i=0;
		StringBuffer responseStrBuffer = new StringBuffer();
		List statusList = new ArrayList();
		
			//Use ';' delimiter to separate between each row.
			while(i<responseListForDefinedArray.size())
			{	
				statusList = (List)responseListForDefinedArray.get(i);
				//Loop for each row.
				for(int j=0;j<statusList.size();j++)
				{
					String responseString = (String) statusList.get(j);
	
					//Separate OptionText and OptionValue with '|'
					responseStrBuffer.append(responseString).append("|").append(responseString);
					if(j != statusList.size()-1)
					{
							responseStrBuffer.append("||");
					}
				}
				responseStrBuffer.append(";");
				//Separate each row with ';'.
				i++;
			}
			String responseString = responseStrBuffer.toString();
			
			responseString = responseString + ",";
		
		if(responseListForExistingArray.size()>0)
		{
			i=0;
			responseStrBuffer = new StringBuffer();
			while(i<responseListForExistingArray.size())
			{
				statusList = (List)responseListForExistingArray.get(i);
				//Loop for each row
				for(int j=0;j<statusList.size();j++)
				{
					String responseStringForExistingArray = (String) statusList.get(j);
	
					//Separate OptionText and OptionValue with '|'
					responseStrBuffer.append(responseStringForExistingArray).append("|").append(responseStringForExistingArray);
					
					//Separate individual options by '||'
					responseStrBuffer.append("||");
				}
				responseStrBuffer.append(";");
				//Separate each row with ';'.
				i++;
			}
			String responseStringForExistingArray = responseStrBuffer.toString();
			int lastOptionSeparator = responseStringForExistingArray.lastIndexOf("||");
			responseStringForExistingArray = responseStringForExistingArray.substring(0,lastOptionSeparator);
			
			responseString = responseString + responseStringForExistingArray;
			
		}
		return responseString;
	}

}
