/**
 * <p>Title: CollectionProtocolAction Class>
 * <p>Description:	This class initializes the fields in the CollectionProtocol Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Mar 22, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields in the CollectionProtocol Add/Edit webpage.
 * @author Mandar Deshmukh
 */
public class CollectionProtocolAction extends SpecimenProtocolAction 
{
    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in CollectionProtocol Add/Edit webpage.
     * */
    protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
    	super.executeSecureAction(mapping, form, request, response);
    	
    	
    	//pageOf required for Advance Search Object View.
    	String pageOf = (String)request.getParameter(Constants.PAGEOF);
    	
        //Gets the value of the operation attribute.
    	String operation = (String)request.getParameter(Constants.OPERATION);
        Logger.out.debug("operation in coll prot action"+operation);
        //Sets the operation attribute to be used in the Edit/View Collection Protocol Page in Advance Search Object View. 
        request.setAttribute(Constants.OPERATION,operation);

    	
    	CollectionProtocolForm collectionProtocolForm = (CollectionProtocolForm)form; 
    	
    	//Name of delete button clicked
         String button = request.getParameter("button");
         
         //Row number of outerblock
         String outer = request.getParameter("blockCounter");
         
//       Gets the map from ActionForm
         Map map = collectionProtocolForm.getValues();
         
//       List of keys used in map of ActionForm
         List key = new ArrayList();
  		 key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_specimenClass");
  		 key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_specimenType");
  		 key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_tissueSite");
  		 key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_pathologyStatus");
  		 key.add("CollectionProtocolEvent:outer_SpecimenRequirement:inner_quantityIn");
        
         if(button != null){
         	if(button.equals("deleteSpecimenReq"))
         	   MapDataParser.deleteRow(key,map,request.getParameter("status"),outer);
         	
         	else {
         		//keys of outer block
         		key.add("CollectionProtocolEvent:outer_clinicalStatus");
         		key.add("CollectionProtocolEvent:outer_studyCalendarEventPoint");
         		MapDataParser.deleteRow(key,map,request.getParameter("status"));
         	}
         }
    	
    	NameValueBean undefinedVal = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
    	List clinicalStatusList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_CLINICAL_STATUS,undefinedVal);
    	request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusList);
	    	
    	 // ---------- Used for Add new
		String reqPath = request.getParameter(Constants.REQ_PATH);
		if (reqPath != null)
			request.setAttribute(Constants.REQ_PATH, reqPath);
		Logger.out.debug("CP Action reqPath : " + reqPath ); 
		Logger.out.debug("page of in collectionProtocol action:"+pageOf);
		request.setAttribute(Constants.PAGEOF,pageOf);
        return mapping.findForward(pageOf);
    }
}