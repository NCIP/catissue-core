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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
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
    	
    	/* added by vaishali on 22nd June 10.10 am */
    	StorageTypeBizLogic bizLogic = (StorageTypeBizLogic)BizLogicFactory.getBizLogic(Constants.STORAGE_TYPE_FORM_ID);
    	/* added finish */
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
        request.setAttribute(Constants.OPERATION, operation);
        /* added by vaishali on 21 st June 06 6.07 pm */
        //Setting the specimen class list
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
    	Set setPV = specimenClassCDE.getPermissibleValues();
    	
    	
    	List holdsList=new ArrayList();
    	holdsList.add(new NameValueBean("-- Any --","-1"));
    	
    	List list=bizLogic.retrieve(StorageType.class.getName());
    	Logger.out.debug("Type List Size:"+list.size());
    	Iterator typeItr=list.iterator();
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