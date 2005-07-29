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
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.dao.AbstractDAO;
import edu.wustl.catissuecore.dao.BizLogicFactory;
import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.dao.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
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
    	
    	System.out.println("storageContainerForm "+storageContainerForm.getFormId());
    	
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        try
		{
        	StorageContainerBizLogic bizLogic = (StorageContainerBizLogic)BizLogicFactory.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
            ListIterator iterator = null;
            int i;
            
            //Populating the Storage Type Array
            List typeList = bizLogic.retrieve(StorageType.class.getName());
            String[] storageTypeArray	= new String[typeList.size() + 1];
            String[] storageTypeIdArray = new String[typeList.size() + 1];
            iterator = typeList.listIterator();
            
            storageTypeArray[0]	 = Constants.SELECT_OPTION;
            storageTypeIdArray[0]= "-1";
            i = 1;

            long typeSelected=-1;
            
            if(request.getParameter("typeSelected") != null)
            {
            	typeSelected = Long.parseLong(request.getParameter("typeSelected"));
            }
            else
            {
            	request.setAttribute("storageType", null);
            }
            
            StorageType type = null;
            while (iterator.hasNext())
            {
                StorageType newType = (StorageType) iterator.next();

                if(newType.getSystemIdentifier().longValue() == typeSelected)
                {
                	type = newType;
                }
                storageTypeArray[i] = newType.getType();
                storageTypeIdArray[i] = newType.getSystemIdentifier().toString();
                i++;
            }
            
            request.setAttribute(Constants.STORAGETYPELIST, storageTypeArray);
        	request.setAttribute(Constants.STORAGETYPEIDLIST, storageTypeIdArray);
         	
        	if(type == null)
    		{
        		storageContainerForm.setDefaultTemperature(0.0);
        		storageContainerForm.setOneDimensionCapacity(0);
        		storageContainerForm.setTwoDimensionCapacity(0);
        		storageContainerForm.setOneDimensionLabel("Dimension One");
        		storageContainerForm.setTwoDimensionLabel("Dimension Two");
    		}
        	else
        	{
        		storageContainerForm.setDefaultTemperature(type.getDefaultTempratureInCentigrade().doubleValue());
        		storageContainerForm.setOneDimensionCapacity(type.getDefaultStorageCapacity().getOneDimensionCapacity().intValue());
        		storageContainerForm.setTwoDimensionCapacity(type.getDefaultStorageCapacity().getTwoDimensionCapacity().intValue());
        		storageContainerForm.setOneDimensionLabel(type.getDefaultStorageCapacity().getOneDimensionLabel());
        		storageContainerForm.setTwoDimensionLabel(type.getDefaultStorageCapacity().getTwoDimensionLabel());
        	}
        	
        	//Populating the Site Type Array
        	List siteList = bizLogic.retrieve(Site.class.getName());
            String[] siteArray	 = new String[siteList.size() + 1];
            String[] siteIdArray = new String[siteList.size() + 1];
            
            siteArray[0]	= Constants.SELECT_OPTION;
            siteIdArray[0]	= "-1";
            
            iterator = null;
            iterator = siteList.listIterator();
            i = 1;
            
            while (iterator.hasNext())
            {
                Site site = (Site) iterator.next();
                siteArray[i] = site.getName();
                siteIdArray[i] = site.getSystemIdentifier().toString();
                i++;
            }
        	
        	request.setAttribute(Constants.SITELIST,siteArray);
        	request.setAttribute(Constants.SITEIDLIST,siteIdArray);
        	
        	request.setAttribute("startNumber",String.valueOf(bizLogic.getNextContainerNumber(1,1)));
		}
        catch(Exception e)
		{
        	e.printStackTrace();
		}
        
    
        return mapping.findForward(Constants.SUCCESS);
    }
}