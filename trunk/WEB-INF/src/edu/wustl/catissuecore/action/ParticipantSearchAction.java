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
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.vo.AdvanceSearchUI;
import edu.wustl.common.vo.SearchFieldData;

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
        //Sets the genderList attribute to be used in the Participant Advance Search Page.
        List genderList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_GENDER,null);
        request.setAttribute(Constants.GENDER_LIST, genderList);
        
        //Sets the genotypeList attribute to be used in the Participant Advance Search Page.
        //NameValueBean unknownVal = new NameValueBean(Constants.UNKNOWN,Constants.UNKNOWN);
        List genotypeList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_GENOTYPE,null);
        request.setAttribute(Constants.GENOTYPE_LIST, genotypeList);
        
        //Sets the ethnicityList attribute to be used in the Participant Advance Search Page.
        List ethnicityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_ETHNICITY,null);
        request.setAttribute(Constants.ETHNICITY_LIST, ethnicityList);
  
        //Sets the raceList attribute to be used in the Participant Advance Search Page.
        List raceList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RACE,null);
        request.setAttribute(Constants.RACELIST, raceList);
        
    	//Set the selected node from the query tree
        String nodeCount = (String)request.getParameter("selectedNode");
    	AdvanceSearchForm aForm = (AdvanceSearchForm)form;
    	aForm.setSelectedNode(nodeCount);
        
        //Setting the operators list in request scope
        request.setAttribute(Constants.STRING_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_STRING));
        request.setAttribute(Constants.DATE_NUMERIC_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_NUMERIC));
        request.setAttribute(Constants.ENUMERATED_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_ENUMERATED));
        
        //An array required for intializing values of ParticipantSearch.jsp
        SearchFieldData[] searchFieldData = SearchUtil.getSearchFieldData(Constants.PARTICIPANT_FORM_ID);
    	 
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