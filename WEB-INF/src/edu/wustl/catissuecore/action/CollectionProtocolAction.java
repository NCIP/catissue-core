/**
 * <p>Title: CollectionProtocolAction Class>
 * <p>Description:	This class initializes the fields in the User Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 22, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields in the User Add/Edit webpage.
 * @author gautam_shetty
 */
public class CollectionProtocolAction extends SpecimenProtocolAction 
{
    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in User Add/Edit webpage.
     * */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	super.executeSecureAction(mapping, form, request, response);
    	
    	CollectionProtocolForm collectionProtocolForm = (CollectionProtocolForm)form;    
         String button = request.getParameter("button");
         String outer = request.getParameter("blockCounter");
         
         Map map = null;
        
         if(button != null){
         	if(button.equals("deleteSpecimenReq")){
         		List key = new ArrayList();
         		key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_specimenClass");
         		key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_specimenType");
         		key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_tissueSite");
         		key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_pathologyStatus");
         		key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_quantityIn");
     	
         		//Gets the map from ActionForm
         		map = collectionProtocolForm.getValues();
         		System.out.println("specimens'map--"+map);
         		DeleteRow(key,map,request,outer);
         	}
         	else {
         		List key = new ArrayList();
         		key.add("CollectionProtocolEvent:outer_clinicalStatus");
         		key.add("CollectionProtocolEvent:outer_studyCalendarEventPoint");
     	
         		//Gets the map from ActionForm
         		map = collectionProtocolForm.getValues();//outer block
         		System.out.println("clooection's map--"+map);
         		DeleteRow(key,map,request,null);
         	}
         }
    	
    	NameValueBean undefinedVal = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
    	List clinicalStatusList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_CLINICAL_STATUS,undefinedVal);
    	request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusList);
	    	
    	 // ---------- Add new
		String reqPath = request.getParameter(Constants.REQ_PATH);
		if (reqPath != null)
			request.setAttribute(Constants.REQ_PATH, reqPath);
		Logger.out.debug("CP Action reqPath : " + reqPath ); 
    	
    	
        return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
}