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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.vo.AdvanceSearchUI;
import edu.wustl.catissuecore.vo.SearchFieldData;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.SearchUtil;
import edu.wustl.common.util.logger.Logger;

public class ParticipantSearchAction extends AdvanceSearchUIAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in ParticipantSearch.jsp Page.
     * */
    protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
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
        
    	//Set the selected node from the query tree
        String nodeCount = (String)request.getParameter("selectedNode");
    	AdvanceSearchForm aForm = (AdvanceSearchForm)form;
    	aForm.setSelectedNode(nodeCount);
        
        //Setting the operators list in request scope
        request.setAttribute(Constants.STRING_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_STRING));
        request.setAttribute(Constants.DATE_NUMERIC_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_NUMERIC));
        request.setAttribute(Constants.ENUMERATED_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_ENUMERATED));
        
        //Class for intializing value of JSP
        SearchFieldData[] searchFieldData = new SearchFieldData[9];
        searchFieldData[0] = initSearchUIData(SearchUtil.STRING, "user.lastName","Participant","LAST_NAME","lastName",Constants.STRING_OPERATORS,"","");
        searchFieldData[1] = initSearchUIData(SearchUtil.STRING,"user.firstName","Participant","FIRST_NAME","firstName",Constants.STRING_OPERATORS,"","");
        searchFieldData[2] = initSearchUIData(SearchUtil.STRING, "participant.middleName","Participant","MIDDLE_NAME","middleName",Constants.STRING_OPERATORS,"","");
        searchFieldData[3] = initSearchUIData(SearchUtil.DATE,   "participant.birthDate","Participant","BIRTH_DATE","birthDate",Constants.DATE_NUMERIC_OPERATORS,"","");
        searchFieldData[4] = initSearchUIData(SearchUtil.STRING, "participant.gender","Participant","GENDER","gender",Constants.ENUMERATED_OPERATORS,Constants.GENDER_LIST,"");
        searchFieldData[5] = initSearchUIData(SearchUtil.STRING, "participant.genotype","Participant","GENOTYPE","genotype",Constants.ENUMERATED_OPERATORS,Constants.GENOTYPE_LIST,"");
        searchFieldData[6] = initSearchUIData(SearchUtil.STRING, "participant.race","Participant","RACE","race",Constants.ENUMERATED_OPERATORS,Constants.RACELIST,"");
        searchFieldData[7] = initSearchUIData(SearchUtil.STRING, "participant.ethnicity","Participant","ETHNICITY","ethnicity",Constants.ENUMERATED_OPERATORS,Constants.ETHNICITY_LIST,"");
        searchFieldData[8] = initSearchUIData(SearchUtil.STRING, "participant.socialSecurityNumber","Participant","SOCIAL_SECURITY_NUMBER","ssn",Constants.STRING_OPERATORS,"","");
    	 
        //Represents id of checked checkbox
        String str = request.getParameter("itemId");
        if(str != null)
        {
        	setMapValue(aForm,str,request);
        	aForm.setItemNodeId(str);
        }
		Logger.out.debug("itemid from form--"+aForm.getItemNodeId());
        Map map = aForm.getValues();
        
        //Checking map contains value or not
    	if( map != null)
    	{
    		setIsDisableValue(searchFieldData,map);
    	}
    		
    	AdvanceSearchUI advSearchUI = new AdvanceSearchUI("images/Participant.GIF","Participant","participant.queryRule",searchFieldData,"advanceSearchForm");
    	request.setAttribute("AdvanceSearchUI",advSearchUI);
        
    	String pageOf = (String)request.getParameter(Constants.PAGEOF);
    	return mapping.findForward(pageOf);
    }
}