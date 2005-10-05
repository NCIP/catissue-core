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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
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
        String button = request.getParameter("button");
        Map map = null;
        
        if(button != null){
        	if(button.equals("deleteExId")){
        		List key = new ArrayList();
        		key.add("ExternalIdentifier:i_name");
        		key.add("ExternalIdentifier:i_value");
    	
        		//Gets the map from ActionForm
        		map = specimenForm.getExternalIdentifier();
        		DeleteRow(key,map,request);
        	}
        	else {
        		List key = new ArrayList();
        		key.add("Biohazard:i_type");
        		key.add("Biohazard:i_systemIdentifier");
    	
        		//Gets the map from ActionForm
        		map = specimenForm.getBiohazard();
        		DeleteRow(key,map,request);
        	}
        }
        
    	

    	// ----------- redirected from specimencollection group
        String specimenCollectionGroupId = (String)request.getAttribute(Constants.SPECIMEN_COLLECTION_GROUP_ID);
        if(specimenCollectionGroupId != null)
		{
        	specimenForm.setSpecimenCollectionGroupId( specimenCollectionGroupId); 
		}

    	
        String pageOf = request.getParameter(Constants.PAGEOF);
        request.setAttribute(Constants.PAGEOF,pageOf);

        //Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
        request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);
        
        NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic)BizLogicFactory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
        
        try
		{	
        	if(specimenForm.isParentPresent())
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
			String[] displayNameFields = {Constants.SYSTEM_IDENTIFIER};
			String valueField = Constants.SYSTEM_IDENTIFIER;
	
			List specimenList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, true);
			request.setAttribute(Constants.SPECIMEN_COLLECTION_GROUP_LIST, specimenList);
		}
        catch(Exception e)
		{
        	Logger.out.error(e.getMessage(),e);
        	return mapping.findForward(Constants.FAILURE);
		}
        
        List specimenClassList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_SPECIMEN_CLASS,null);
    	request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
    	
    	List specimenTypeList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_SPECIMEN_TYPE,null);
    	request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
        
    	NameValueBean undefinedVal = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
    	List tissueSiteList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_TISSUE_SITE,undefinedVal);
    	request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteList);

    	
    	NameValueBean unknownVal = new NameValueBean(Constants.UNKNOWN,Constants.UNKNOWN);
    	List tissueSideList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_TISSUE_SIDE,unknownVal);
    	request.setAttribute(Constants.TISSUE_SIDE_LIST, tissueSideList);
        
    	List pathologicalStatusList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_PATHOLOGICAL_STATUS,null);
    	request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, pathologicalStatusList);
        
    	List biohazardList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_BIOHAZARD,null);
    	request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);
        //----------------------------------------
    	try
		{
        	Logger.out.debug("1");
        	// get the Specimen class and type from the cde
        	CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
	    	Set setPV = specimenClassCDE.getPermissibleValues();
	    	Logger.out.debug("2");
	    	Iterator itr = setPV.iterator();
	    
	    	specimenClassList =  new ArrayList();
	    	Map subTypeMap = new HashMap();
	    	Logger.out.debug("\n\n\n\n**********MAP DATA************\n");
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
	    	Logger.out.debug("************************************\n\n\nDone**********\n");
		}
        catch(Exception excp)
		{
        	Logger.out.error(excp.getMessage(),excp);
        	return mapping.findForward(Constants.FAILURE);
		}

        
    	//----------------------------------------
    	return mapping.findForward(pageOf);
    }
}