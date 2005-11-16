/**
 * <p>Title: StorageContainerAction Class>
 * <p>Description:	This class initializes the fields of StorageContainer.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;


public class StorageContainerAction  extends SecureAction
{
	/**
     * Overrides the execute method of Action class.
     * Initializes the various fields in StorageContainer.jsp Page.
     * */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		StorageContainerForm storageContainerForm = (StorageContainerForm) form;

		//	set the menu selection 
    	request.setAttribute(Constants.MENU_SELECTED, "7"  ); 
		
		//List of keys used in map of ActionForm
		List key = new ArrayList();
    	key.add("StorageContainerDetails:i_parameterName");
    	key.add("StorageContainerDetails:i_parameterValue");
    	
    	//Gets the map from ActionForm
    	Map map = storageContainerForm.getValues();
    	//Calling DeleteRow of BaseAction class
    	MapDataParser.deleteRow(key,map,request.getParameter("status"));
    
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        //Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
        request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);
       
    	StorageContainerBizLogic bizLogic = (StorageContainerBizLogic)BizLogicFactory.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
        
    	String []displayField = {"type"};  
    	String valueField = "systemIdentifier";
    	List list = bizLogic.getList(StorageType.class.getName(),displayField, valueField, false);
    	request.setAttribute(Constants.STORAGETYPELIST, list);
    	
    	//Populating the Site Array
    	String []siteDisplayField = {"name"};
    	list = bizLogic.getList(Site.class.getName(),siteDisplayField, valueField, true);
    	request.setAttribute(Constants.SITELIST,list);
    	
    	boolean isOnChange = false; 
		String str = request.getParameter("isOnChange");
		
		if(str!=null && str.equals("true"))
		{
			isOnChange = true; 
		}
		
    	if(isOnChange)
    	{

        	long typeSelected=-1;
        	String selectedType = String.valueOf(storageContainerForm.getTypeId());
        	
        	if(selectedType != null && !selectedType.equals("-1"))
            {
            	typeSelected = Long.parseLong(selectedType);
            	list = bizLogic.retrieve(StorageType.class.getName(),valueField,new Long(typeSelected));
            	if(!list.isEmpty())
            	{
            		StorageType type = (StorageType)list.get(0);
            		if(type.getDefaultTempratureInCentigrade()!= null)
            			storageContainerForm.setDefaultTemperature(type.getDefaultTempratureInCentigrade().toString() );
            		
            		storageContainerForm.setOneDimensionCapacity(type.getDefaultStorageCapacity().getOneDimensionCapacity().intValue());
            		storageContainerForm.setTwoDimensionCapacity(type.getDefaultStorageCapacity().getTwoDimensionCapacity().intValue());
            		storageContainerForm.setOneDimensionLabel(type.getOneDimensionLabel());
            		storageContainerForm.setTwoDimensionLabel(type.getTwoDimensionLabel());
            	}
            }
            else
            {
            	request.setAttribute("storageType", null);
            	storageContainerForm.setDefaultTemperature("");
        		storageContainerForm.setOneDimensionCapacity(0);
        		storageContainerForm.setTwoDimensionCapacity(0);
        		storageContainerForm.setOneDimensionLabel("Dimension One");
        		storageContainerForm.setTwoDimensionLabel("Dimension Two");
            }
    	
    	
    	    int startNumber = 1;
        	if(storageContainerForm.getCheckedButton() == 1)
        	{
        	    Logger.out.debug("storageContainerForm.getSiteId()......................."+storageContainerForm.getSiteId());
        	    Logger.out.debug("storageContainerForm.getTypeId()......................."+storageContainerForm.getTypeId());
        		startNumber = bizLogic.getNextContainerNumber(storageContainerForm.getSiteId(),storageContainerForm.getTypeId(),true);
        	}
        	else
        	{
        	    Logger.out.debug("Long.parseLong(request.getParameter(parentContainerId)......................."+request.getParameter("parentContainerId"));
        	    Logger.out.debug("storageContainerForm.getTypeId()......................."+storageContainerForm.getTypeId());
        		startNumber = bizLogic.getNextContainerNumber(Long.parseLong(request.getParameter("parentContainerId")),storageContainerForm.getTypeId(),false);
        	}
        	
        	storageContainerForm.setStartNumber(String.valueOf(startNumber));
    	}     
        	
        // ---------- Add new
		String reqPath = request.getParameter(Constants.REQ_PATH);
		
		if (reqPath != null)
		{
			request.setAttribute(Constants.REQ_PATH, reqPath);
		}

		Logger.out.debug("StorageContainerAction redirect :---------- "+ reqPath);
    
        return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
}