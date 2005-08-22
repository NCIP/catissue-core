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

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.Constants;

//public class StorageContainerAction extends BaseAction
public class StorageContainerAction extends Action
{
	/**
     * Overrides the execute method of Action class.
     * Initializes the various fields in StorageContainer.jsp Page.
     * */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	StorageContainerForm storageContainerForm = (StorageContainerForm) form;
    	
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        try
		{
        	StorageContainerBizLogic bizLogic = (StorageContainerBizLogic)BizLogicFactory.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
            
        	String []displayField = {"type"};  
        	String valueField = "systemIdentifier";
        	List list = bizLogic.getList(StorageType.class.getName(),displayField, valueField);
        	request.setAttribute(Constants.STORAGETYPELIST, list);
        	
        	long typeSelected=-1;
        	String selectedType = (String)request.getParameter("typeSelected");
        	
        	if(selectedType != null && !selectedType.equals("-1"))
            {
            	typeSelected = Long.parseLong(selectedType);
            	list = bizLogic.retrieve(StorageType.class.getName(),valueField,new Long(typeSelected));
            	if(!list.isEmpty())
            	{
            		StorageType type = (StorageType)list.get(0);
            		
            		storageContainerForm.setDefaultTemperature(type.getDefaultTempratureInCentigrade().doubleValue());
            		storageContainerForm.setOneDimensionCapacity(type.getDefaultStorageCapacity().getOneDimensionCapacity().intValue());
            		storageContainerForm.setTwoDimensionCapacity(type.getDefaultStorageCapacity().getTwoDimensionCapacity().intValue());
            		storageContainerForm.setOneDimensionLabel(type.getOneDimensionLabel());
            		storageContainerForm.setTwoDimensionLabel(type.getTwoDimensionLabel());
            	}
            }
            else
            {
            	request.setAttribute("storageType", null);
            	storageContainerForm.setDefaultTemperature(0.0);
        		storageContainerForm.setOneDimensionCapacity(0);
        		storageContainerForm.setTwoDimensionCapacity(0);
        		storageContainerForm.setOneDimensionLabel("Dimension One");
        		storageContainerForm.setTwoDimensionLabel("Dimension Two");
            }
        	
        	//Populating the Site Array
        	String []siteDisplayField = {"name"};
        	list = bizLogic.getList(Site.class.getName(),siteDisplayField, valueField);
        	request.setAttribute(Constants.SITELIST,list);
        	
        	if(storageContainerForm.getCheckedButton() == 1)
        	{
        		request.setAttribute("startNumber",String.valueOf(bizLogic.getNextContainerNumber(storageContainerForm.getSiteId(),storageContainerForm.getTypeId(),true)));
        	}
        	else
        	{
        		request.setAttribute("startNumber",String.valueOf(bizLogic.getNextContainerNumber(Long.parseLong(request.getParameter("parentContainerId")),storageContainerForm.getTypeId(),false)));
        	}
        
		}
        catch(Exception e)
		{
        	e.printStackTrace();
		}        
    
        return mapping.findForward(Constants.SUCCESS);
    }
}