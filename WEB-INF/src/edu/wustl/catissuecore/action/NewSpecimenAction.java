/**
 * <p>Title: NewSpecimenAction Class>
 * <p>Description:	NewSpecimenAction initializes the fields in the New Specimen page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * NewSpecimenAction initializes the fields in the New Specimen page.
 * @author aniruddha_phadnis
 */
public class NewSpecimenAction  extends SecureAction
{   
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        NewSpecimenForm specimenForm = (NewSpecimenForm)form;   
        
        //Gets the value of the operation parameter.
        String operation = (String)request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Edit/View Specimen Page in Advance Search Object View. 
        request.setAttribute(Constants.OPERATION,operation);
        
        if(operation != null && operation.equalsIgnoreCase(Constants.ADD ) )
        {
        	specimenForm.setId(0);
        }

        
        //Name of button clicked
        String button = request.getParameter("button");
        Map map = null;
        
        if(button != null){
        	if(button.equals("deleteExId")){
        		List key = new ArrayList();
        		key.add("ExternalIdentifier:i_name");
        		key.add("ExternalIdentifier:i_value");
    	
        		//Gets the map from ActionForm
        		map = specimenForm.getExternalIdentifier();
        		MapDataParser.deleteRow(key,map,request.getParameter("status"));
        	}
        	else {
        		List key = new ArrayList();
        		key.add("Biohazard:i_type");
        		key.add("Biohazard:i_id");
    	
        		//Gets the map from ActionForm
        		map = specimenForm.getBiohazard();
        		MapDataParser.deleteRow(key,map,request.getParameter("status"));
        	}
        }

        //*************  ForwardTo implementation *************
        HashMap forwardToHashMap=(HashMap)request.getAttribute("forwardToHashMap");
        
        if(forwardToHashMap !=null)
        {
            String specimenCollectionGroupId=(String)forwardToHashMap.get("specimenCollectionGroupId");
            Logger.out.debug("SpecimenCollectionGroupId found in forwardToHashMap========>>>>>>"+specimenCollectionGroupId);
            
            if(specimenCollectionGroupId != null)
            {
                specimenForm.setSpecimenCollectionGroupId(specimenCollectionGroupId);
                specimenForm.setParentSpecimenId("");
            	specimenForm.setPositionInStorageContainer("");
            	specimenForm.setQuantity(""); 
            	specimenForm.setClassName("");
            	specimenForm.setTissueSide("");
            	specimenForm.setTissueSite("");
            	specimenForm.setPathologicalStatus("");
            	specimenForm.setPositionDimensionOne("");
            	specimenForm.setPositionDimensionTwo("");
            	specimenForm.setStorageContainer("");
            	
            	clearCollectionEvent(specimenForm);
            	clearReceivedEvent(specimenForm);
            	
            }
        }
        //*************  ForwardTo implementation *************
        

        // - set the specimen id
//       	String specimenID = (String)request.getAttribute(Constants.SPECIMEN_ID);
//       	if(specimenID !=null)
//       		specimenForm.setId(Long.parseLong(specimenID  )); 
//    	
//    	Logger.out.debug("SpecimenID in NewSpecimenAction : " + specimenID  );
    	
        String pageOf = request.getParameter(Constants.PAGEOF);
        request.setAttribute(Constants.PAGEOF,pageOf);
        
        
        //Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
        request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);
        
        NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
        
        if(specimenForm.isParentPresent())//If parent specimen is present then
    	{
    		String [] fields = {Constants.SYSTEM_IDENTIFIER};
            List parentSpecimenList = bizLogic.getList(Specimen.class.getName(), fields, Constants.SYSTEM_IDENTIFIER, true); 	 	
    	 	request.setAttribute(Constants.PARENT_SPECIMEN_ID_LIST, parentSpecimenList);
    	}
    	
    	String [] bhIdArray =  {"-1"};
    	String [] bhTypeArray =  {Constants.SELECT_OPTION};
    	String [] bhNameArray =  {Constants.SELECT_OPTION};
    	
    	String selectColNames[] = {Constants.SYSTEM_IDENTIFIER,"name","type"}; 
    	List biohazardList = bizLogic.retrieve(Biohazard.class.getName(), selectColNames);
    	Iterator iterator = biohazardList.iterator();
    	
    	//Creating & setting the biohazard name, id & type list
    	if(biohazardList!=null && !biohazardList.isEmpty())
    	{
        	bhIdArray =  new String[biohazardList.size() + 1];
        	bhTypeArray =  new String[biohazardList.size() + 1];
        	bhNameArray =  new String[biohazardList.size() + 1];
        	
        	bhIdArray[0] = "-1";
        	bhTypeArray[0] = "";
        	bhNameArray[0] = Constants.SELECT_OPTION;
        	
        	int i=1;
        	
        	while(iterator.hasNext())
        	{
        		Object hazard[] = (Object[])iterator.next();
        		bhIdArray[i] = String.valueOf(hazard[0]);
        		bhNameArray[i] = (String)hazard[1];
        		bhTypeArray[i] = (String)hazard[2];
        		i++;
        	}
    	}
    	
    	request.setAttribute(Constants.BIOHAZARD_NAME_LIST, bhNameArray);
    	request.setAttribute(Constants.BIOHAZARD_ID_LIST, bhIdArray);
    	request.setAttribute(Constants.BIOHAZARD_TYPES_LIST, bhTypeArray);
    	
    	//Setting Secimen Collection Group
		String sourceObjectName = SpecimenCollectionGroup.class.getName();
		String[] displayNameFields = {"name"};
		String valueField = Constants.SYSTEM_IDENTIFIER;

		List specimenList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, true);
		request.setAttribute(Constants.SPECIMEN_COLLECTION_GROUP_LIST, specimenList);
				
		//Setting the specimen class list
        List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS,null);
    	request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
    	
		if(Constants.ALIQUOT.equals(specimenForm.getLineage()))
		{
			populateListBoxes(specimenForm,request);
		}
		else
		{
			//Setting the specimen type list
	    	List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_TYPE,null);
	    	request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
	        
	    	//Setting tissue site list
	    	List tissueSiteList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_TISSUE_SITE,null);
	    	request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteList);
	
	    	//Setting tissue side list
	    	List tissueSideList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_TISSUE_SIDE,null);
	    	request.setAttribute(Constants.TISSUE_SIDE_LIST, tissueSideList);
	        
	    	//Setting pathological status list
	    	List pathologicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_PATHOLOGICAL_STATUS,null);
	    	request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, pathologicalStatusList);
		}
		
		//get the Specimen class and type from the cde
    	CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
    	Set setPV = specimenClassCDE.getPermissibleValues();
    	Iterator itr = setPV.iterator();
    
    	specimenClassList =  new ArrayList();
    	Map subTypeMap = new HashMap();
    	
    	specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
    	
    	while(itr.hasNext())
    	{
    		List innerList =  new ArrayList();
    		Object obj = itr.next();
    		PermissibleValue pv = (PermissibleValue)obj;
    		String tmpStr = pv.getValue();
    		Logger.out.debug(tmpStr);
    		specimenClassList.add(new NameValueBean( tmpStr,tmpStr));
    		
			Set list1 = pv.getSubPermissibleValues();
			Logger.out.debug("list1 "+list1);
        	Iterator itr1 = list1.iterator();
        	innerList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
        	while(itr1.hasNext())
        	{
        		Object obj1 = itr1.next();
        		PermissibleValue pv1 = (PermissibleValue)obj1;
        		// set specimen type
        		String tmpInnerStr = pv1.getValue(); 
        		Logger.out.debug("\t\t"+tmpInnerStr);
        		innerList.add(new NameValueBean( tmpInnerStr,tmpInnerStr));  
        	}
        	subTypeMap.put(pv.getValue(),innerList);
    	} // class and values set
    	Logger.out.debug("\n\n\n\n**********MAP DATA************\n");
    	
    	// sets the Class list
    	request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

    	// set the map to subtype
    	request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);
        
    	//Setting biohazard list
    	biohazardList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_BIOHAZARD,null);
    	request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);
    	
    	//Mandar : 10-July-06 AutoEvents : CollectionEvent
    	setCollectionEventRequestParameters(request);

    	//Mandar : 11-July-06 AutoEvents : ReceivedEvent
    	setReceivedEventRequestParameters(request);
    	
    	//Mandar : set default date and time too event fields
    	setDateParameters(specimenForm);
    	
    	//    	 ---- chetan 15-06-06 ----
    	StorageContainerBizLogic scbizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
        Map containerMap = new HashMap();
        if(operation.equals(Constants.ADD))
        {
        	if(request.getParameter("Change")!=null)
        	{
        		List spCollGroupList = bizLogic.retrieve(SpecimenCollectionGroup.class.getName(),Constants.SYSTEM_IDENTIFIER,new Long(specimenForm.getSpecimenCollectionGroupId()));
            	
    			if(!spCollGroupList.isEmpty())
    			{
    				SpecimenCollectionGroup spCollGroup = (SpecimenCollectionGroup)spCollGroupList.get(0);
    				long cpId = spCollGroup.getCollectionProtocolRegistration().getCollectionProtocol().getId().longValue();
    				String spClass = specimenForm.getClassName();
    				Logger.out.info("cpId :"+cpId +"spClass:"+spClass);
    				containerMap = scbizLogic.getAllocatedContaienrMapForSpecimen(cpId,spClass);
    			}
           		
        	}
        } 
        else
        {
        	containerMap = new TreeMap();
        	Integer id = new Integer(specimenForm.getStorageContainer());
        	String parentContainerName="";
        	String valueField1 = "id";
        	List list = bizLogic.retrieve(StorageContainer.class.getName(),valueField1,new Long(specimenForm.getStorageContainer()));
			if(!list.isEmpty())
			{
				StorageContainer container = (StorageContainer)list.get(0);
				parentContainerName=container.getName();
				            		
			}
        	Integer pos1 = new Integer(specimenForm.getPositionDimensionOne());        	
        	Integer pos2 = new Integer(specimenForm.getPositionDimensionTwo());
        	
        	List pos2List = new ArrayList();
        	pos2List.add(new NameValueBean(pos2,pos2));
        	
        	Map pos1Map = new TreeMap();
        	pos1Map.put(new NameValueBean(pos1,pos1),pos2List);
        	containerMap.put(new NameValueBean(parentContainerName,id),pos1Map);
        	
        		
        	
        }
        request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,containerMap);
        // -------------------------
        
    	return mapping.findForward(pageOf);
    }
    
    /* This method populates the list boxes for type, tissue site, tissue side
     * and pathological status if this specimen is an aliquot.
     */
    private void populateListBoxes(NewSpecimenForm specimenForm,HttpServletRequest request)
    {
    	//Setting the specimen type list
    	NameValueBean bean = new NameValueBean(specimenForm.getType(),specimenForm.getType());
    	List specimenTypeList = new ArrayList();
    	specimenTypeList.add(bean);
    	request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
    	
    	//Setting tissue site list
    	bean = new NameValueBean(specimenForm.getTissueSite(),specimenForm.getTissueSite());
    	List tissueSiteList = new ArrayList();
    	tissueSiteList.add(bean);
    	request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteList);
    	
    	//Setting tissue side list
    	bean = new NameValueBean(specimenForm.getTissueSide(),specimenForm.getTissueSide());
    	List tissueSideList = new ArrayList();
    	tissueSideList.add(bean);
    	request.setAttribute(Constants.TISSUE_SIDE_LIST, tissueSideList);
    	
    	//Setting pathological status list
    	bean = new NameValueBean(specimenForm.getPathologicalStatus(),specimenForm.getPathologicalStatus());
    	List pathologicalStatusList = new ArrayList();
    	pathologicalStatusList.add(bean);
    	request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, pathologicalStatusList);
    }

    // Mandar AutoEvents CollectionEvent start
	/**
	 * This method sets all the collection event parameters for the SpecimenEventParameter pages
	 * @param request HttpServletRequest instance in which the data will be set. 
	 * @throws Exception Throws Exception. Helps in handling exceptions at one common point.
	 */
	private void setCollectionEventRequestParameters(HttpServletRequest request) throws Exception
	{
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit FrozenEventParameters Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        //Sets the minutesList attribute to be used in the Add/Edit FrozenEventParameters Page.
        request.setAttribute(Constants.MINUTES_LIST, Constants.MINUTES_ARRAY);

        //Sets the hourList attribute to be used in the Add/Edit FrozenEventParameters Page.
        request.setAttribute(Constants.HOUR_LIST, Constants.HOUR_ARRAY);
         
//        //The id of specimen of this event.
//        String specimenId = request.getParameter(Constants.SPECIMEN_ID); 
//        request.setAttribute(Constants.SPECIMEN_ID, specimenId);
//        Logger.out.debug("\t\t SpecimenEventParametersAction************************************ : "+specimenId );
//        
       	UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
    	Collection userCollection =  userBizLogic.getUsers(operation);
    	
    	request.setAttribute(Constants.USERLIST, userCollection);

		// set the procedure lists
		List procedureList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COLLECTION_PROCEDURE,null);
    	request.setAttribute(Constants.PROCEDURE_LIST, procedureList);
	    
	    // set the container lists
    	List containerList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CONTAINER,null);
    	request.setAttribute(Constants.CONTAINER_LIST, containerList);
    	
	}

    // Mandar AutoEvents CollectionEvent end

	// Mandar Autoevents ReceivedEvent start
	/**
	 * This method sets all the received event parameters for the SpecimenEventParameter pages
	 * @param request HttpServletRequest instance in which the data will be set. 
	 * @throws Exception Throws Exception. Helps in handling exceptions at one common point.
	 */
	private void setReceivedEventRequestParameters(HttpServletRequest request) throws Exception
	{
		
		List qualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RECEIVED_QUALITY,null);
    	request.setAttribute(Constants.RECEIVED_QUALITY_LIST, qualityList);
	}
	
	private void setDateParameters(NewSpecimenForm specimenForm)
	{
		// set the current Date and Time for the event.
		Calendar cal = Calendar.getInstance();
		//Collection Event fields
		if(specimenForm.getCollectionEventdateOfEvent() == null)
		{
			specimenForm.setCollectionEventdateOfEvent(Utility.parseDateToString(cal.getTime(), Constants.DATE_PATTERN_MM_DD_YYYY));
		}
		if(specimenForm.getCollectionEventTimeInHours() == null)
		{
			specimenForm.setCollectionEventTimeInHours(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
		}
		if(specimenForm.getCollectionEventTimeInMinutes()== null)
		{
			specimenForm.setCollectionEventTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
		}
		
		//ReceivedEvent Fields
		if(specimenForm.getReceivedEventDateOfEvent()==null)
		{
			specimenForm.setReceivedEventDateOfEvent(Utility.parseDateToString(cal.getTime(), Constants.DATE_PATTERN_MM_DD_YYYY));
		}
		if(specimenForm.getReceivedEventTimeInHours()==null)
		{
			specimenForm.setReceivedEventTimeInHours(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
		}
		if(specimenForm.getReceivedEventTimeInMinutes() == null)
		{
			specimenForm.setReceivedEventTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
		}

	}
	
	private void clearCollectionEvent(NewSpecimenForm specimenForm )
	{
		specimenForm.setCollectionEventCollectionProcedure("" );
		specimenForm.setCollectionEventComments("" );
		specimenForm.setCollectionEventContainer("");
		specimenForm.setCollectionEventdateOfEvent("" );
		specimenForm.setCollectionEventTimeInHours("" );
		specimenForm.setCollectionEventTimeInMinutes("" );
		specimenForm.setCollectionEventUserId(-1);
	}
	
	private void clearReceivedEvent(NewSpecimenForm specimenForm )
	{
		specimenForm.setReceivedEventComments("" );
		specimenForm.setReceivedEventDateOfEvent("" );
		specimenForm.setReceivedEventReceivedQuality("" );
		specimenForm.setReceivedEventTimeInHours("" );
		specimenForm.setReceivedEventTimeInMinutes("" );
		specimenForm.setReceivedEventUserId(-1);
	}

	
}