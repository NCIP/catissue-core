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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.util.logger.Logger;

public class StorageTypeAction  extends SecureAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in StorageType.jsp Page.
     * @author Vaishali Khandelwal
     * */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
    	StorageTypeForm storageTypeForm=(StorageTypeForm)form;
    	
    	Logger.out.info("SpecimenArray/specimen:"+storageTypeForm.getSpecimenOrArrayType());
    	if(storageTypeForm.getSpecimenOrArrayType() == null)
    	{
    		storageTypeForm.setSpecimenOrArrayType("Specimen");
    	}
    	StorageTypeBizLogic bizLogic = (StorageTypeBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_TYPE_FORM_ID);
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
        request.setAttribute(Constants.OPERATION, operation);
 
        //Gets the Storage Type List and sets it in request
        List list1=bizLogic.retrieve(StorageType.class.getName());
    	List storageTypeList=Utility.getStorageTypeList(list1);
    	//Collections.sort(storageTypeList);
    	request.setAttribute(Constants.HOLDS_LIST1, storageTypeList);
    	
       	
    	// get the Specimen class and type from the cde
    	List specimenClassTypeList=Utility.getSpecimenClassTypeListWithAny();
	  	request.setAttribute(Constants.HOLDS_LIST2, specimenClassTypeList);
        
	  	//Gets the Specimen array Type List and sets it in request
        List list2=bizLogic.retrieve(SpecimenArrayType.class.getName());
    	List spArrayTypeList=Utility.getSpecimenArrayTypeList(list2);
    	request.setAttribute(Constants.HOLDS_LIST3, spArrayTypeList);
    	
	  	
	  	if(operation.equals(Constants.ADD))
	  	{
	  		// new model storageTypeForm.setHoldsSpecimenClassTypeIds(new long[]{1});
	  		//storageTypeForm.setHoldsStorageTypeIds(new long[]{1});
	  		storageTypeForm.setOneDimensionCapacity(0);
	  		storageTypeForm.setTwoDimensionCapacity(0);
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
   
}