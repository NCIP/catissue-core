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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
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

        //Sets the isContainerFullList attribute to be used in the StorageContainer Add/Edit Page.
        request.setAttribute(Constants.IS_CONTAINER_FULL_LIST, Constants.IS_CONTAINER_FULL_VALUES );

        
    	StorageContainerBizLogic bizLogic = (StorageContainerBizLogic)BizLogicFactory.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
        
    	String []displayField = {"type"};  
    	String valueField = "systemIdentifier";
    	List list = bizLogic.getList(StorageType.class.getName(),displayField, valueField, false);
    	request.setAttribute(Constants.STORAGETYPELIST, list);
    	
    	//Populating the Site Array
    	String []siteDisplayField = {"name"};
    	list = bizLogic.getList(Site.class.getName(),siteDisplayField, valueField, true);
    	request.setAttribute(Constants.SITELIST,list);
    	/* added by vaishali on 19th June 2006 3.47 pm */
    	Logger.out.debug("Getting Collection Protocol Ids");
//    	//populating collection protocol list.
		
		String [] displayNameFields = {"title"};
		valueField = Constants.SYSTEM_IDENTIFIER;
	  	List collectionProtocolList = bizLogic.getList(CollectionProtocol.class.getName(),displayNameFields,valueField, true);
	  	Logger.out.debug("Protocol list size:"+collectionProtocolList.size());
	  	Iterator collcectionProtocolItr=collectionProtocolList.iterator();
	  	List collectionProtocolList1=new ArrayList();
	  	while(collcectionProtocolItr.hasNext())
	  	{
	  		NameValueBean nvb=(NameValueBean)collcectionProtocolItr.next();
	  		if(nvb.getValue().equals("-1"))
	  		{
	  			NameValueBean nvb1=new NameValueBean("-- Any --","-1");
	  			collectionProtocolList1.add(nvb1);
	  		}
	  		else
	  		{
	  			collectionProtocolList1.add(nvb);
	  		}
	  	}
	  	request.setAttribute(Constants.PROTOCOL_LIST, collectionProtocolList1);
		
//		Setting the specimen class list
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
    	Set setPV = specimenClassCDE.getPermissibleValues();
    	
    	
    	List holdsList=new ArrayList();
    	holdsList.add(new NameValueBean("-- Any --","-1"));
    	
    	List typeList=bizLogic.retrieve(StorageType.class.getName());
    	Logger.out.debug("Type List Size:"+typeList.size());
    	Iterator typeItr=typeList.iterator();
    	while(typeItr.hasNext())
    	{
    		StorageType type=(StorageType)typeItr.next();
    		holdsList.add(new NameValueBean(type.getType(),type.getSystemIdentifier()));
    	}
        
    	holdsList.add(new NameValueBean("Any Specimen","-1"));
        Iterator itr = setPV.iterator();
          	
    	while(itr.hasNext())
    	{
    		Object obj = itr.next();
    		PermissibleValue pv = (PermissibleValue)obj;
    		String tmpStr = pv.getValue()+" Specimen";
    		Logger.out.debug(" Specimen value-------------"+tmpStr);
    		holdsList.add(new NameValueBean( tmpStr,tmpStr));
    		
    	}	
    	request.setAttribute(Constants.HOLDS_LIST, holdsList);
    	/* added finish */
    	
    	boolean isOnChange = false; 
    	/* added by vaishali on 21st June 2006 1.37 pm */
    	String type_name="";
    	String site_name="";
    	/* added finish */
		String str = request.getParameter("isOnChange");
		
		if(str!=null && str.equals("true"))
		{
			isOnChange = true; 
		}
		// Mandar : code for Addnew Storage Type data 23-Jan-06
		String storageTypeID = (String)request.getAttribute(Constants.ADD_NEW_STORAGE_TYPE_ID);
		if(storageTypeID != null && storageTypeID.trim().length() > 0 )
		{
			Logger.out.debug(">>>>>>>>>>><<<<<<<<<<<<<<<<>>>>>>>>>>>>> ST : "+ storageTypeID  );
			storageContainerForm.setTypeId(Long.parseLong(storageTypeID) );
		}
		// -- 23-Jan-06 end
		// Mandar : code for Addnew Site data 24-Jan-06
		String siteID = (String)request.getAttribute(Constants.ADD_NEW_SITE_ID);
		if(siteID != null && siteID.trim().length() > 0 )
		{
			Logger.out.debug(">>>>>>>>>>><<<<<<<<<<<<<<<<>>>>>>>>>>>>> ToSite ID in Distribution Action : "+ siteID  );
			storageContainerForm.setSiteId(Long.parseLong(siteID));
		}
		// -- 24-Jan-06 end
  
		
    	if(isOnChange)
    	{

        	long typeSelected=-1;
        	String selectedType = String.valueOf(storageContainerForm.getTypeId());
        	Logger.out.debug(">>>>>>>>>>><<<<<<<<<<<<<<<<>>>>>>>>>>>>> ST : "+ selectedType  );
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
            		storageContainerForm.setTwoDimensionLabel(Utility.toString(type.getTwoDimensionLabel()));
            		
            		/* added by vaishali on 21st June 2006 1.38 pm */
            		type_name=type.getType();
            		Logger.out.debug("Type Name:"+type_name);
            		/* added finish */
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
        		type_name="";
            }
    	
    	
    	    int startNumber = 1;
        	if(storageContainerForm.getCheckedButton() == 1)
        	{
        	    Logger.out.debug("storageContainerForm.getSiteId()......................."+storageContainerForm.getSiteId());
        	    Logger.out.debug("storageContainerForm.getTypeId()......................."+storageContainerForm.getTypeId());
        		startNumber = bizLogic.getNextContainerNumber(storageContainerForm.getSiteId(),storageContainerForm.getTypeId(),true);
        		/* added by vaishali on 21st june 2006 1.39 pm */
        		list = bizLogic.retrieve(Site.class.getName(),valueField,new Long(storageContainerForm.getSiteId()));
            	if(!list.isEmpty())
            	{
            		Site site = (Site)list.get(0);
            		site_name=site.getName();
            		Logger.out.debug("Site Name :"+site_name);
            		
            	}	
        		/* added finish */
        	}
        	else
        	{
        		
        	    Logger.out.debug("Long.parseLong(request.getParameter(parentContainerId)......................."+request.getParameter("parentContainerId"));
        	    Logger.out.debug("storageContainerForm.getTypeId()......................."+storageContainerForm.getTypeId());
        		startNumber = bizLogic.getNextContainerNumber(Long.parseLong(request.getParameter("parentContainerId")),storageContainerForm.getTypeId(),false);
        		
        		list = bizLogic.retrieve(StorageContainer.class.getName(),valueField,new Long(request.getParameter("parentContainerId")));
            	if(!list.isEmpty())
            	{
            		StorageContainer container = (StorageContainer)list.get(0);
            		site_name=container.getSite().getName();
            		Logger.out.debug("Site Name :"+site_name);
            		
            	}	
        	}
        	Logger.out.debug("Start Number : " + startNumber); 
        	storageContainerForm.setStartNumber(String.valueOf(startNumber));
        	/* adde byvaishali on 21st june 2006 1.46 pm -*/
        	int containerName=bizLogic.getNextContainerName();
        	
        	//if(storageContainerForm.getContainerName()!=null && storageContainerForm.getContainerName().equals(""))
        //	{
        		if(!type_name.equals("")&& !site_name.equals(""))
        		{	
        			storageContainerForm.setContainerName(site_name+"_"+type_name+"_"+String.valueOf(containerName));
        		}
        		/*added finish*/
        			
        	//}     
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