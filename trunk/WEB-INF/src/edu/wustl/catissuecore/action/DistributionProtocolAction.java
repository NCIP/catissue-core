/**
 * <p>Title: DistributionProtocolAction Class>
 * <p>Description:	This class initializes the fields in the DistributionProtocol Add/Edit webpage.</p>
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

import edu.wustl.catissuecore.actionForm.DistributionProtocolForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;


/**
 * This class initializes the fields in the DistributionProtocol Add/Edit webpage.
 * @author Mandar Deshmukh
 */
public class DistributionProtocolAction extends SpecimenProtocolAction 
{

    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in DistributionProtocol Add/Edit webpage.
     * */
    protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {     
    	DistributionProtocolForm distributionProtocolForm = (DistributionProtocolForm) form;
    	
    	//List of keys used in map of ActionForm
		List key = new ArrayList();
    	key.add("SpecimenRequirement:i_specimenClass");
    	key.add("SpecimenRequirement:i_specimenType");
    	key.add("SpecimenRequirement:i_tissueSite");
    	key.add("SpecimenRequirement:i_pathologyStatus");
    	key.add("SpecimenRequirement:i_quantity_value");
    	
    	//Gets the map from ActionForm
    	Map map = distributionProtocolForm.getValues();
    	
    	//Calling DeleteRow of BaseAction class
    	MapDataParser.deleteRow(key,map,request.getParameter("status"));
    	
    	// ----------For Add new
		String reqPath = request.getParameter(Constants.REQ_PATH);
		if (reqPath != null)
			request.setAttribute(Constants.REQ_PATH, reqPath);
		Logger.out.debug("DP Action reqPath : ---- " + reqPath );
		
		// Mandar : code for Addnew Coordinator data 24-Jan-06
		String coordinatorID = (String)request.getAttribute(Constants.ADD_NEW_USER_ID);
		if(coordinatorID != null && coordinatorID.trim().length() > 0 )
		{
			Logger.out.debug(">>>>>>>>>>><<<<<<<<<<<<<<<<>>>>>>>>>>>>> User ID in DP : "+ coordinatorID  );
			distributionProtocolForm.setPrincipalInvestigatorId(Long.parseLong(coordinatorID ) ) ;
		}
		// -- 24-Jan-06 end

		
    	return super.executeSecureAction(mapping, form, request, response);
    }
}