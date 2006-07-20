/**
 * <p>Title: StorageTypeAction Class>
 * <p>Description:	This class initializes the fields of StorageType.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.domain.SpecimenClass;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.logger.Logger;

public class StorageTypeAction  extends SecureAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in StorageType.jsp Page.
     * */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
    	StorageTypeForm storageTypeForm=(StorageTypeForm)form;
    	
    	StorageTypeBizLogic bizLogic = (StorageTypeBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_TYPE_FORM_ID);
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
        request.setAttribute(Constants.OPERATION, operation);
 
        //Gets the Storage Type List and sets it in request 
        List list1=bizLogic.retrieve(StorageType.class.getName());
    	List storageTypeList=getStorageTypeList(list1);
    	//Collections.sort(storageTypeList);
    	request.setAttribute(Constants.HOLDS_LIST1, storageTypeList);
    	
    	//Gets the Specimen Class Type List and sets it in request
    	List list2=bizLogic.retrieve(SpecimenClass.class.getName());
        List specimenClassTypeList = getSpecimenClassTypeList(list2);
        //Collections.sort(specimenClassTypeList);
	  	request.setAttribute(Constants.HOLDS_LIST2, specimenClassTypeList);
        
	  	if(operation.equals(Constants.ADD))
	  	{
	  		storageTypeForm.setHoldsSpecimenClassTypeIds(new long[]{1});
	  		storageTypeForm.setHoldsStorageTypeIds(new long[]{1});
	  	}
	    // ------------- add new
        String reqPath = request.getParameter(Constants.REQ_PATH);
		request.setAttribute(Constants.REQ_PATH, reqPath);
		
        AbstractActionForm aForm = (AbstractActionForm )form; 
        if(reqPath != null  && aForm !=null)
        	aForm.setRedirectTo(reqPath);
        
        Logger.out.debug("StorageTypeAction redirect :---------- "+ reqPath  );
        
        return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
    
    /* this Function gets the list of all storage types as argument and  
     * create a list in which nameValueBean is stored with Type and Identifier of storage type.
     * and returns this list
     */ 
    private List getStorageTypeList(List list)
    {
    	NameValueBean typeAny=null;
    	List storageTypeList=new ArrayList();
    	Iterator typeItr=list.iterator();
    	while(typeItr.hasNext())
    	{
    		StorageType type=(StorageType)typeItr.next();
    		if(type.getSystemIdentifier().longValue()==1)
    		{
    			typeAny=new NameValueBean(type.getType(),type.getSystemIdentifier());
    		}
    		else
    		{
    			storageTypeList.add(new NameValueBean(type.getType(),type.getSystemIdentifier()));
    		}
    	}
    	Collections.sort(storageTypeList);
    	storageTypeList.add(0,typeAny);
    	return storageTypeList;
    	
    }
    /* this Function gets the list of all Specimen Class Types as argument and  
     * create a list in which nameValueBean is stored with Name and Identifier of specimen Class Type.
     * and returns this list
     */
    private List getSpecimenClassTypeList(List list)
    {
    	List specimenClassTypeList=new ArrayList();
    	NameValueBean specimenClassAny=null;
    	
    	Iterator specimentypeItr=list.iterator();
    	while(specimentypeItr.hasNext())
    	{
    		SpecimenClass specimenClass=(SpecimenClass)specimentypeItr.next();
    		if(specimenClass.getSystemIdentifier().longValue()==1)
    		{
    			specimenClassAny=new NameValueBean(specimenClass.getName(),specimenClass.getSystemIdentifier());
    		}
    		else
    		{
    			specimenClassTypeList.add(new NameValueBean(specimenClass.getName(),specimenClass.getSystemIdentifier()));
    		}
    	}
    	Collections.sort(specimenClassTypeList);
    	specimenClassTypeList.add(0,specimenClassAny);
    	return specimenClassTypeList;
    	
    }
}