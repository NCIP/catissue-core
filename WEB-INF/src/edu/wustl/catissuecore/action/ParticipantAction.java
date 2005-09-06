/**
 * <p>Title: ParticipantAction Class>
 * <p>Description:  This class initializes the fields in the Participant Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 7, 2005
 */
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;


/**
 * This class initializes the fields in the Participant Add/Edit webpage.
 * @author gautam_shetty
 */
public class ParticipantAction  extends SecureAction
{
    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in Participant Add/Edit webpage.
     * */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit Participant Page. 
        request.setAttribute(Constants.OPERATION,operation);
        
        //Sets the genderList attribute to be used in the Add/Edit Participant Page.
        List genderList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_GENDER);
        request.setAttribute(Constants.GENDER_LIST, genderList);
        
        //Sets the genotypeList attribute to be used in the Add/Edit Participant Page.
        List genotypeList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_GENOTYPE);
        request.setAttribute(Constants.GENOTYPE_LIST, genotypeList);
        
        request.setAttribute(Constants.ETHNICITY_LIST, Constants.ETHNICITY_VALUES);
  
        //Sets the raceList attribute to be used in the Add/Edit Participant Page. 
        request.setAttribute(Constants.RACELIST,Constants.RACEARRAY);
        
        //Sets the pageOf attribute (for Add,Edit or Query Interface)
        String pageOf  = request.getParameter(Constants.PAGEOF);
        
        request.setAttribute(Constants.PAGEOF,pageOf);
                
        try
		{
        	ParticipantBizLogic dao = (ParticipantBizLogic)BizLogicFactory.getBizLogic(Constants.PARTICIPANT_FORM_ID);
             
        	//Sets the Site list of corresponding type.
            String sourceObjectName = Site.class.getName();
            String[] displayNameFields = {"name"};
            String valueField = "systemIdentifier";
            String[] whereColumnName = {"type"};
            String[] whereColumnCondition = {"="};
            Object[] whereColumnValue = {"Hospital"};
            String joinCondition = Constants.AND_JOIN_CONDITION;
            String separatorBetweenFields = "";
			
            List siteList = dao.getList(sourceObjectName, displayNameFields, valueField,whereColumnName,
            				whereColumnCondition,whereColumnValue,joinCondition,separatorBetweenFields);
            
            request.setAttribute(Constants.SITELIST, siteList);
		}
        catch(Exception e)
		{
        	e.printStackTrace();
		}
        
        return mapping.findForward(pageOf);
    }

}
