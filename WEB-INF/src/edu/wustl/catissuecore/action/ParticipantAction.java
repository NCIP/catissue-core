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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

import edu.wustl.catissuecore.actionForm.ParticipantForm;
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
    	ParticipantForm participantForm = (ParticipantForm) form;
		
//    	List of keys used in map of ActionForm
		List key = new ArrayList();
    	key.add("ParticipantMedicalIdentifier:i_Site_systemIdentifier");
    	key.add("ParticipantMedicalIdentifier:i_medicalRecordNumber");
    	
//    	Gets the map from ActionForm
    	Map map = participantForm.getValues();
    	
//    	Calling DeleteRow of BaseAction class
    	MapDataParser.deleteRow(key,map,request.getParameter("status"));
    	
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit Participant Page. 
        request.setAttribute(Constants.OPERATION,operation);
        
        //Sets the pageOf attribute (for Add,Edit or Query Interface)
        String pageOf  = request.getParameter(Constants.PAGEOF);
        
        request.setAttribute(Constants.PAGEOF,pageOf);

        NameValueBean unknownVal = new NameValueBean(Constants.UNKNOWN,Constants.UNKNOWN);
        //Sets the genderList attribute to be used in the Add/Edit Participant Page.
        List genderList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_GENDER,unknownVal);
        request.setAttribute(Constants.GENDER_LIST, genderList);
        
        //Sets the genotypeList attribute to be used in the Add/Edit Participant Page.
        List genotypeList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_GENOTYPE,unknownVal);
        request.setAttribute(Constants.GENOTYPE_LIST, genotypeList);
        
        List ethnicityList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_ETHNICITY,unknownVal);
        request.setAttribute(Constants.ETHNICITY_LIST, ethnicityList);
  
        //Sets the raceList attribute to be used in the Add/Edit Participant Page.
        List raceList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_RACE,unknownVal);
        request.setAttribute(Constants.RACELIST, raceList);
        
        //Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
        request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);
        
        try
		{
        	ParticipantBizLogic dao = (ParticipantBizLogic)BizLogicFactory.getBizLogic(Constants.PARTICIPANT_FORM_ID);
             
        	//Sets the Site list of corresponding type.
            String sourceObjectName = Site.class.getName();
            String[] displayNameFields = {"name"};
            String valueField = Constants.SYSTEM_IDENTIFIER;
			
            List siteList = dao.getList(sourceObjectName, displayNameFields, valueField, true);
            
            request.setAttribute(Constants.SITELIST, siteList);
            Logger.out.debug("pageOf :---------- "+ pageOf );
            
            // ------------- add new
            String reqPath = request.getParameter(Constants.REQ_PATH);
//			if(reqPath!=null)
//			{
//				reqPath = reqPath + "|/Participant.do?operation=add&amp;pageOf=pageOfParticipant"	;			 
//			}
//			else
//			{
//				reqPath = "/Participant.do?operation=add&amp;pageOf=pageOfParticipant"	;
//			}
			request.setAttribute(Constants.REQ_PATH, reqPath);
			request.setAttribute("A", "A");
            
            AbstractActionForm aForm = (AbstractActionForm )form; 
            if(reqPath != null && aForm !=null )
            	aForm.setRedirectTo(reqPath);
            
            Logger.out.debug("redirect :---------- "+ reqPath  );
            
            
		}
        catch(Exception e)
		{
        	Logger.out.error(e.getMessage(),e);
        	mapping.findForward(Constants.FAILURE); 
		}
        return mapping.findForward(pageOf);
    }
}