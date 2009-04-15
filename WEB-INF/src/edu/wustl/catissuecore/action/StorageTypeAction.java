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
import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

public class StorageTypeAction  extends SecureAction
{
	private transient Logger logger = Logger.getCommonLogger(StorageTypeAction.class);
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
        String operation = (String) request.getAttribute(Constants.OPERATION);
        storageTypeForm.setOperation(operation);
        String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
        storageTypeForm.setSubmittedFor(submittedFor);
		String forwardTo=(String)request.getAttribute(Constants.FORWARD_TO);
		storageTypeForm.setForwardTo(forwardTo);
		String reqPath = request.getParameter(Constants.REQ_PATH);
		storageTypeForm.setRedirectTo(reqPath);
        String formName;
       if (operation.equals(Constants.EDIT))
        {
            formName = Constants.STORAGE_TYPE_EDIT_ACTION;
        }
        else
        {
            formName = Constants.STORAGE_TYPE_ADD_ACTION;
        }
       request.setAttribute("formName", formName);
       request.setAttribute("operationAdd", Constants.ADD);
   	   request.setAttribute("operationEdit", Constants.EDIT);
   	   request.setAttribute("holds_List_1", Constants.HOLDS_LIST1);
   	   request.setAttribute("holds_List_2", Constants.HOLDS_LIST2);
   	   request.setAttribute("holds_List_3", Constants.HOLDS_LIST3);
   	   	
	//Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label
	int dimTwoCapacity = 0;
	if(storageTypeForm!=null)
	{
		dimTwoCapacity = storageTypeForm.getTwoDimensionCapacity();
	}
	String tdClassName ="formLabel";
	String strStar = "&nbsp;";
	if(dimTwoCapacity > 1)
	{
		tdClassName="formRequiredLabel";
		strStar = "<span class="+"blue_ar_b"+"><img src="+"images/uIEnhancementImages/star.gif"+" alt="+"Mandatory"+" width="+"6"+" height="+"6"+" hspace="+"0"+" vspace="+"3"+" /></span>";
	}
	request.setAttribute("tdClassName", tdClassName);
	request.setAttribute("strStar", strStar);
 
	
	String normalSubmit = "validate('" + submittedFor+ "','" + Constants.STORAGE_TYPE_FORWARD_TO_LIST[0][1]+"')";
	String forwardToSubmit = "validate('ForwardTo','" + Constants.STORAGE_TYPE_FORWARD_TO_LIST[1][1]+"')";
	request.setAttribute("normalSubmit", normalSubmit);
	request.setAttribute("forwardToSubmit", forwardToSubmit);
	
	
	request.setAttribute("submit", Constants.STORAGE_TYPE_FORWARD_TO_LIST[0][0]);
	request.setAttribute("addContainer", Constants.STORAGE_TYPE_FORWARD_TO_LIST[1][0]);
	
	

	//Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label end	

	
    	
	    logger.info("SpecimenArray/specimen:"+storageTypeForm.getSpecimenOrArrayType());
    	if(storageTypeForm.getSpecimenOrArrayType() == null)
    	{
    		storageTypeForm.setSpecimenOrArrayType("Specimen");
    	}
    	IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
    	StorageTypeBizLogic bizLogic = (StorageTypeBizLogic)factory.getBizLogic(Constants.STORAGE_TYPE_FORM_ID);
        //Gets the value of the operation parameter.
        
        
        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
        //Gets the Storage Type List and sets it in request
        List list1=bizLogic.retrieve(StorageType.class.getName());
    	List storageTypeList=AppUtility.getStorageTypeList(list1,true);
    	//Collections.sort(storageTypeList);
    	request.setAttribute(Constants.HOLDS_LIST1, storageTypeList);
    	
       	
    	// get the Specimen class and type from the cde
    	List specimenClassTypeList=AppUtility.getSpecimenClassTypeListWithAny();
	  	request.setAttribute(Constants.HOLDS_LIST2, specimenClassTypeList);
        
	  	//Gets the Specimen array Type List and sets it in request
        List list2=bizLogic.retrieve(SpecimenArrayType.class.getName());
    	List spArrayTypeList=AppUtility.getSpecimenArrayTypeList(list2);
    	request.setAttribute(Constants.HOLDS_LIST3, spArrayTypeList);
    	
    	//Bug #4297
//	  	if(operation.equals(Constants.ADD))
//	  	{
//	  		// new model storageTypeForm.setHoldsSpecimenClassTypeIds(new long[]{1});
//	  		//storageTypeForm.setHoldsStorageTypeIds(new long[]{1});
//	  		if(storageTypeForm.getOneDimensionCapacity() == 0 &&
//	  				storageTypeForm.getTwoDimensionCapacity() == 0)
//	  		{
//	  			storageTypeForm.setOneDimensionCapacity(0);
//	  			storageTypeForm.setTwoDimensionCapacity(0);
//	  		}
//	  	}
	    // ------------- add new
       
		
        AbstractActionForm aForm = (AbstractActionForm )form; 
        if(reqPath != null  && aForm !=null)
        	aForm.setRedirectTo(reqPath);
        
        logger.debug("StorageTypeAction redirect :---------- "+ reqPath  );
        
        return mapping.findForward((String)request.getParameter(Constants.PAGE_OF));
    }
   
}