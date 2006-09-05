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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import edu.wustl.catissuecore.domain.SpecimenArrayType;
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
        
        //request.setAttribute(Constants.IS_CONTAINER_FULL_LIST, Constants.IS_CONTAINER_FULL_VALUES );

        
    	StorageContainerBizLogic bizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
    	long container_number=bizLogic.getNextContainerNumber();
    	request.setAttribute("ContainerNumber",new Long(container_number).toString());
    	
    	//    	 ---- chetan 15-06-06 ----
        
    	Map containerMap=new HashMap();
    	List mapSiteList = new ArrayList();
    	List siteList = new ArrayList();
    	if(operation.equals(Constants.ADD) && storageContainerForm.getTypeId()!=-1)
        {
    		mapSiteList = bizLogic.getAllocatedContaienrMapForContainer(storageContainerForm.getTypeId());
    		containerMap = (Map) mapSiteList.get(0);
    		siteList = (List) mapSiteList.get(1);
    		
    	} 
        else
        {
        	containerMap = new TreeMap();
        	Integer id = new Integer((int)storageContainerForm.getParentContainerId());
        	String parentContainerName="";
        	String valueField = "id";
        	List containerList = bizLogic.retrieve(StorageContainer.class.getName(),valueField,new Long(storageContainerForm.getParentContainerId()));
			if(!containerList.isEmpty())
			{
				StorageContainer container = (StorageContainer)containerList.get(0);
				parentContainerName=container.getName();
				            		
			}
        	
        	Integer pos1 = new Integer(storageContainerForm.getPositionDimensionOne());        	
        	Integer pos2 = new Integer(storageContainerForm.getPositionDimensionTwo());
        	
        	List pos2List = new ArrayList();
        	pos2List.add(new NameValueBean(pos2,pos2));
        	
        	Map pos1Map = new TreeMap();
        	pos1Map.put(new NameValueBean(pos1,pos1),pos2List);
        	containerMap.put(new NameValueBean(parentContainerName,id),pos1Map);
        }
        request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,containerMap);
        request.setAttribute("siteForParentList",siteList);
    	
    	//Populating the Site Array
    	String []siteDisplayField = {"name"};
    	String valueField = "id";
    	List list = bizLogic.getList(Site.class.getName(),siteDisplayField, valueField, true);
    	request.setAttribute(Constants.SITELIST,list);
    	
    	//populating collection protocol list.
    	List list1=bizLogic.retrieve(CollectionProtocol.class.getName());
    	List collectionProtocolList=Utility.getCollectionProtocolList(list1);
    	request.setAttribute(Constants.PROTOCOL_LIST, collectionProtocolList);
	  	
        //Gets the Storage Type List and sets it in request 
        List list2=bizLogic.retrieve(StorageType.class.getName());
    	List storageTypeListWithAny=Utility.getStorageTypeList(list2,true);
    	request.setAttribute(Constants.HOLDS_LIST1, storageTypeListWithAny);
    	
    	List StorageTypeListWithoutAny=Utility.getStorageTypeList(list2,false);
    	request.setAttribute(Constants.STORAGETYPELIST, StorageTypeListWithoutAny);
    
    	
    	// get the Specimen class and type from the cde
    	List specimenClassTypeList=Utility.getSpecimenClassTypeListWithAny();
	  	request.setAttribute(Constants.HOLDS_LIST2, specimenClassTypeList);
	  	
	  	//Gets the Specimen array Type List and sets it in request
        List list3=bizLogic.retrieve(SpecimenArrayType.class.getName());
    	List spArrayTypeList=Utility.getSpecimenArrayTypeList(list3);
    	request.setAttribute(Constants.HOLDS_LIST3, spArrayTypeList);


    	
    	
    	boolean isOnChange = false;
    	boolean isTypeChange=false;
    	
    	boolean isSiteOrParentContainerChange=false;
    	
    	
    	String str = request.getParameter("isOnChange");
		
		if(str!=null && str.equals("true"))
		{
			isOnChange = true; 
		}
	
		str = request.getParameter("isNameChange");
		
		
		
		str = request.getParameter("typeChange");
		if(str!=null && str.equals("true"))
		{
			isTypeChange=true;
			
		}
		str = request.getParameter("siteOrParentContainerChange");
		if(str!=null && str.equals("true"))
		{
			isSiteOrParentContainerChange=true;
			
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
  
		
    	if(isTypeChange || request.getAttribute(Constants.SUBMITTED_FOR)!=null)
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
            		
            			storageContainerForm.setOneDimensionCapacity(type.getCapacity().getOneDimensionCapacity().intValue());
            			storageContainerForm.setTwoDimensionCapacity(type.getCapacity().getTwoDimensionCapacity().intValue());
            			storageContainerForm.setOneDimensionLabel(type.getOneDimensionLabel());
            			storageContainerForm.setTwoDimensionLabel(Utility.toString(type.getTwoDimensionLabel()));
            			storageContainerForm.setTypeName(type.getName());
            			//type_name=type.getType();
            		
            		Logger.out.debug("Type Name:"+storageContainerForm.getTypeName());
            		
            		// If operation is add opeartion then set the holds list according to storage type selected.
            		if(operation!=null && operation.equals(Constants.ADD))
            		{
            			long[] defHoldsStorageTypeList=getDefaultHoldStorageTypeList(type);
            			if(defHoldsStorageTypeList!=null)
            			{
            				storageContainerForm.setHoldsStorageTypeIds(defHoldsStorageTypeList);
            			}
            			
            			String[] defHoldsSpecimenClassTypeList=getDefaultHoldsSpecimenClasstypeList(type);
            			if(defHoldsSpecimenClassTypeList!=null)
            			{
            	      		storageContainerForm.setHoldsSpecimenClassTypes(defHoldsSpecimenClassTypeList);
	          			}
            			for(int i=0;i<storageContainerForm.getHoldsSpecimenClassTypes().length;i++)
            			{
            				Logger.out.info("Specimen class in form:"+storageContainerForm.getHoldsSpecimenClassTypes()[i]);
            			}
            			long[] defHoldsSpecimenArrayTypeList=getDefaultHoldSpecimenArrayTypeList(type);
            			if(defHoldsSpecimenArrayTypeList!=null)
            			{
            				storageContainerForm.setHoldsSpecimenArrTypeIds(defHoldsSpecimenArrayTypeList);
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
        		storageContainerForm.setTypeName("");
        		//type_name="";
            }
    	}
    	
    	if(isSiteOrParentContainerChange || request.getAttribute(Constants.SUBMITTED_FOR)!=null && storageContainerForm.getContainerName().equals(""))
    	{
            	
    		if(storageContainerForm.getCheckedButton() == 1)
    		{
    			Logger.out.debug("storageContainerForm.getSiteId()......................."+storageContainerForm.getSiteId());
    			Logger.out.debug("storageContainerForm.getTypeId()......................."+storageContainerForm.getTypeId());
    			list = bizLogic.retrieve(Site.class.getName(),valueField,new Long(storageContainerForm.getSiteId()));
    			if(!list.isEmpty())
    			{
    				Site site = (Site)list.get(0);
    				storageContainerForm.setSiteName(site.getName());
    				//site_name=site.getName();
    				Logger.out.debug("Site Name :"+storageContainerForm.getSiteName());
        		}	
    		}
    		else
    		{
    			Logger.out.debug("Long.parseLong(request.getParameter(parentContainerId)......................."+request.getParameter("parentContainerId"));
    			Logger.out.debug("storageContainerForm.getTypeId()......................."+storageContainerForm.getTypeId());
    			String parentContId = request.getParameter("parentContainerId");
    			if(parentContId != null)
    			{
    				list = bizLogic.retrieve(StorageContainer.class.getName(),valueField,new Long(parentContId));
    				if(!list.isEmpty())
    				{
    					StorageContainer container = (StorageContainer)list.get(0);
    					//site_name=container.getSite().getName();
    					storageContainerForm.setSiteName(container.getSite().getName());
    					Logger.out.debug("Site Name :"+storageContainerForm.getSiteName());            		
    				}
    			}
    		}
    	}
    	Logger.out.info("Container name:"+storageContainerForm.getContainerName());
    	Logger.out.info("Site Name:"+storageContainerForm.getSiteName());
    	Logger.out.info("type:"+storageContainerForm.getTypeName());
    	if(storageContainerForm.getContainerName().equals(""))
		{
    		storageContainerForm.setContainerName(bizLogic.getContainerName(storageContainerForm.getSiteName(),storageContainerForm.getTypeName(),operation, storageContainerForm.getId()));
    		
		}
    	
        // ---------- Add new
		String reqPath = request.getParameter(Constants.REQ_PATH);
		
		if (reqPath != null)
		{
			request.setAttribute(Constants.REQ_PATH, reqPath);
		}
		
		
		
        return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
		
	    
    /* this function finds out the storage type holds list for a storage type given 
     * and sets the container's storage type holds list
     * */
    private long[] getDefaultHoldStorageTypeList(StorageType type)
    {
    	//Populating the storage type-id array
    	
		Logger.out.info("Storage type size:"+type.getHoldsStorageTypeCollection().size());
		Collection storageTypeCollection = type.getHoldsStorageTypeCollection();
		
		if(storageTypeCollection != null)
		{
			long holdsStorageTypeList[] = new long[storageTypeCollection.size()];
			int i=0;
			Iterator it = storageTypeCollection.iterator();
			while(it.hasNext())
			{
				StorageType holdStorageType = (StorageType)it.next();
				holdsStorageTypeList[i] = holdStorageType.getId().longValue();
				i++;
			}
			return holdsStorageTypeList;
		}
    	return null;
    }
    
    /* this function finds out the specimen class holds list for a storage type given 
     * and sets the container's specimen class holds list
     * */
    private String[] getDefaultHoldsSpecimenClasstypeList(StorageType type)
    {
    	String[] holdsSpecimenClassList = null;
    	//Populating the specimen class type-id array
		Logger.out.info("Specimen class type size:"+type.getHoldsSpecimenClassCollection().size());
		Collection specimenClassTypeCollection = type.getHoldsSpecimenClassCollection();
		
		if(specimenClassTypeCollection != null)
		{
			if(specimenClassTypeCollection.size() == Utility.getSpecimenClassTypes().size())
			{
				holdsSpecimenClassList = new String[1];
				holdsSpecimenClassList[0] = "-1";
			}
			else
			{
				holdsSpecimenClassList = new String[specimenClassTypeCollection.size()];
				int i=0;

				Iterator it = specimenClassTypeCollection.iterator();
				while(it.hasNext())
				{
					String specimenClassType=(String)it.next();
					Logger.out.info("specimen class type:"+specimenClassType);
					holdsSpecimenClassList[i]=specimenClassType;
					i++;
				}
			}	
			return holdsSpecimenClassList;
			
		}
    	return null;
    }
    
    /* this function finds out the specimen array type holds list for a storage type given 
     * and sets the container's storage type holds list
     * */
    private long[] getDefaultHoldSpecimenArrayTypeList(StorageType type)
    {
    	//Populating the storage type-id array
    	
		Logger.out.info("Storage type size:"+type.getHoldsSpArrayTypeCollection().size());
		Collection spcimenArrayTypeCollection = type.getHoldsSpArrayTypeCollection();
		
		if(spcimenArrayTypeCollection != null)
		{
			long holdsSpecimenArrayTypeList[] = new long[spcimenArrayTypeCollection.size()];
			int i=0;
			Iterator it = spcimenArrayTypeCollection.iterator();
			while(it.hasNext())
			{
				SpecimenArrayType holdSpArrayType = (SpecimenArrayType)it.next();
				holdsSpecimenArrayTypeList[i] = holdSpArrayType.getId().longValue();
				i++;
			}
			return holdsSpecimenArrayTypeList;
		}
    	return null;
    }
}