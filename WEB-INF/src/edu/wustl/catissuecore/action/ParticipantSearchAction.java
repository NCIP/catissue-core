/**
 * <p>Title: ParticipantSearchAction Class>
 * <p>Description:	This class initializes the fields of ParticipantSearch.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Oct 11, 2005
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

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.SearchUtil;

public class ParticipantSearchAction extends BaseAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in ParticipantSearch.jsp Page.
     * */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	NameValueBean unknownVal = new NameValueBean(Constants.UNKNOWN,Constants.UNKNOWN);
        //Sets the genderList attribute to be used in the Participant Advance Search Page.
        List genderList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_GENDER,unknownVal);
        request.setAttribute(Constants.GENDER_LIST, genderList);
        
        //Sets the genotypeList attribute to be used in the Participant Advance Search Page.
        List genotypeList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_GENOTYPE,unknownVal);
        request.setAttribute(Constants.GENOTYPE_LIST, genotypeList);
        
        //Sets the ethnicityList attribute to be used in the Participant Advance Search Page.
        List ethnicityList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_ETHNICITY,unknownVal);
        request.setAttribute(Constants.ETHNICITY_LIST, ethnicityList);
  
        //Sets the raceList attribute to be used in the Participant Advance Search Page.
        List raceList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_RACE,unknownVal);
        request.setAttribute(Constants.RACELIST, raceList);
        
        //Setting the operators list in request scope
        request.setAttribute(Constants.STRING_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_STRING));
        request.setAttribute(Constants.DATE_NUMERIC_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_NUMERIC));
        
    	String pageOf = (String)request.getParameter(Constants.PAGEOF);
    	return mapping.findForward(pageOf);
    }
}