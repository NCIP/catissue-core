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
import java.util.Collection;
import java.util.Iterator;
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
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenClass;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
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
		Logger.out.info("Add New Attribute in StorageContainerAction:"+request.getAttribute(Constants.SUBMITTED_FOR));
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

        
    	StorageContainerBizLogic bizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
        
    	String []displayField = {"type"};  
    	String valueField = "systemIdentifier";
    	List list = bizLogic.getList(StorageType.class.getName(),displayField, valueField, false);
    	request.setAttribute(Constants.STORAGETYPELIST, list);
    	
    	//Populating the Site Array
    	String []siteDisplayField = {"name"};
    	list = bizLogic.getList(Site.class.getName(),siteDisplayField, valueField, true);
    	request.setAttribute(Constants.SITELIST,list);
    	
    	Logger.out.debug("Getting Collection Protocol Ids");
    	
    	//populating collection protocol list.
    	List list1=bizLogic.retrieve(CollectionProtocol.class.getName());
    	List collectionProtocolList=getCollectionProtocolList(list1);
    	request.setAttribute(Constants.PROTOCOL_LIST, collectionProtocolList);
	  	
        //Gets the Storage Type List and sets it in request 
        List list2=bizLogic.retrieve(StorageType.class.getName());
    	List storageTypeList=getStorageTypeList(list2);
    	request.setAttribute(Constants.HOLDS_LIST1, storageTypeList);
    	
    	//Gets the Specimen Class Type List and sets it in request
    	List list3=bizLogic.retrieve(SpecimenClass.class.getName());
        List specimenClassTypeList = getSpecimenClassTypeList(list3);
	  	request.setAttribute(Constants.HOLDS_LIST2, specimenClassTypeList);
	  	
    	boolean isOnChange = false; 
    	String type_name="";
    	String site_name="";
    	String str = request.getParameter("isOnChange");
		
		if(str!=null && str.equals("true"))
		{
			isOnChange = true; 
		}
		Logger.out.info("Onchange parameter in StorageContainerAction:"+isOnChange);
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
  
		
    	if(isOnChange || request.getAttribute(Constants.SUBMITTED_FOR)!=null)
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
            		
            		type_name=type.getType();
            		Logger.out.debug("Type Name:"+type_name);
            		
            		if(operation!=null && operation.equals(Constants.ADD))
            		{
            			long[] defHoldsStorageTypeList=getDefaultHoldStorageTypeList(type);
            			if(defHoldsStorageTypeList!=null)
            			{
            				storageContainerForm.setHoldsStorageTypeIds(defHoldsStorageTypeList);
            			}
            			
            			long[] defHoldsSpecimenClassTypeList=getDefaultHoldsSpecimenClasstypeList(type);
            			if(defHoldsSpecimenClassTypeList!=null)
            			{
            	      		storageContainerForm.setHoldsSpecimenClassTypeIds(defHoldsSpecimenClassTypeList);
            			}

            		}
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
        		list = bizLogic.retrieve(Site.class.getName(),valueField,new Long(storageContainerForm.getSiteId()));
            	if(!list.isEmpty())
            	{
            		Site site = (Site)list.get(0);
            		site_name=site.getName();
            		Logger.out.debug("Site Name :"+site_name);
            		
            	}	
        	}
        	else
        	{
        		Logger.out.debug("Long.parseLong(request.getParameter(parentContainerId)......................."+request.getParameter("parentContainerId"));
        	    Logger.out.debug("storageContainerForm.getTypeId()......................."+storageContainerForm.getTypeId());
        		
        		
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
        	int containerName=bizLogic.getNextContainerName();
        	if(!type_name.equals("")&& !site_name.equals(""))
        	{	
        		if(operation.equals(Constants.ADD))
        		{
        			storageContainerForm.setContainerName(site_name+"_"+type_name+"_"+String.valueOf(containerName));
        		}
        	}
        }	
        // ---------- Add new
		String reqPath = request.getParameter(Constants.REQ_PATH);
		
		if (reqPath != null)
		{
			request.setAttribute(Constants.REQ_PATH, reqPath);
		}
        return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
		
	/* This function gets the list of all collection protocols as argument and  
     * create a list in which nameValueBean is stored with Title and Identifier of Collection Protocol.
     * and returns this list
     */ 
    private List getCollectionProtocolList(List list)
    {
    	List collectionProtocolList=new ArrayList();
    	collectionProtocolList.add(new NameValueBean("-- Any --","-1"));
    	
    	Iterator cpItr=list.iterator();
    	while(cpItr.hasNext())
    	{
    		CollectionProtocol cp=(CollectionProtocol)cpItr.next();
    		collectionProtocolList.add(new NameValueBean(cp.getTitle(),cp.getSystemIdentifier()));
    	}
    	return collectionProtocolList;
    }
    /* this Function gets the list of all storage types as argument and  
     * create a list in which nameValueBean is stored with Type and Identifier of storage type.
     * and returns this list
     */ 
    private List getStorageTypeList(List list)
    {
    	List storageTypeList=new ArrayList();
    	storageTypeList.add(new NameValueBean("-- Any --","-1"));
    	
    	Iterator typeItr=list.iterator();
    	while(typeItr.hasNext())
    	{
    		StorageType type=(StorageType)typeItr.next();
    		storageTypeList.add(new NameValueBean(type.getType(),type.getSystemIdentifier()));
    	}
    	return storageTypeList;
    }
    /* this Function gets the list of all Specimen Class Types as argument and  
     * create a list in which nameValueBean is stored with Name and Identifier of specimen Class Type.
     * and returns this list
     */
    private List getSpecimenClassTypeList(List list)
    {
    	List specimenClassTypeList=new ArrayList();
    	specimenClassTypeList.add(new NameValueBean("-- Any Specimen--","-1"));
    	
    	Iterator specimentypeItr=list.iterator();
    	while(specimentypeItr.hasNext())
    	{
    		SpecimenClass specimenClassType=(SpecimenClass)specimentypeItr.next();
    		specimenClassTypeList.add(new NameValueBean(specimenClassType.getName(),specimenClassType.getSystemIdentifier()));
    	}
    	return specimenClassTypeList;
    }
    
    private long[] getDefaultHoldStorageTypeList(StorageType type)
    {
    	//Populating the storage type-id array
    	
		Logger.out.info("Storage type size:"+type.getStorageTypeCollection().size());
		Collection storageTypeCollection = type.getStorageTypeCollection();
		
		if(storageTypeCollection != null)
		{
			long holdsStorageTypeList[] = new long[storageTypeCollection.size()];
			int i=0;
			Iterator it = storageTypeCollection.iterator();
			while(it.hasNext())
			{
				StorageType holdStorageType = (StorageType)it.next();
				holdsStorageTypeList[i] = holdStorageType.getSystemIdentifier().longValue();
				i++;
			}
			return holdsStorageTypeList;
		}
    	return null;
    }
    
    private long[] getDefaultHoldsSpecimenClasstypeList(StorageType type)
    {
    	//Populating the specimen class type-id array
		Logger.out.info("Specimen class type size:"+type.getSpecimenClassCollection().size());
		Collection specimenClassTypeCollection = type.getSpecimenClassCollection();
		
		if(specimenClassTypeCollection != null)
		{
			long holdsSpecimenClassList[] = new long[specimenClassTypeCollection.size()];
			int i=0;

			Iterator it = specimenClassTypeCollection.iterator();
			while(it.hasNext())
			{
				SpecimenClass specimenClass = (SpecimenClass)it.next();
				holdsSpecimenClassList[i] = specimenClass.getSystemIdentifier().longValue();
				i++;
			}
			return holdsSpecimenClassList;
			
		}
    	return null;
    }
}