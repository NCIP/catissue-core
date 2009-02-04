/**
 * <p>Title: SpecimenCollectionGroupSearchAction Class>
 * <p>Description:	This class initializes the fields of SpecimenCollectionGroupSearch.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Nov 07, 2005
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

public class SpecimenCollectionGroupSearchAction extends AdvanceSearchUIAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in SpecimenCollectionGroupSearch.jsp Page.
     * */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception //IOException, ServletException
    {
    	//Setting the clinical diagnosis list
    	List clinicalDiagnosisList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CLINICAL_DIAGNOSIS,null);
		request.setAttribute(Constants.CLINICAL_DIAGNOSIS_LIST, clinicalDiagnosisList);
		
		//Setting the clinical status list
//		NameValueBean undefinedBean = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
        List clinicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CLINICAL_STATUS,null);
    	request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusList);
    	
//    	Set the selected node from the query tree
    	String nodeCount = (String)request.getParameter("selectedNode");
    	AdvanceSearchForm aForm = (AdvanceSearchForm)form;
    	aForm.setSelectedNode(nodeCount);
    	
    	//Setting the operators list in request scope
        request.setAttribute(Constants.STRING_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_STRING));
        request.setAttribute(Constants.DATE_NUMERIC_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_NUMERIC));
        request.setAttribute(Constants.ENUMERATED_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_ENUMERATED));
        
        //An array required for intializing values of SpecimenCollectionGroupSearch.jsp
        SearchFieldData[] searchFieldData = SearchUtil.getSearchFieldData(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
        
    	 
        //Represents id of checked checkbox
        String str = request.getParameter("itemId");
        if(str != null)
        {
        	setMapValue(aForm,str,request);
        	aForm.setItemNodeId(str);
        }
		
        Map map = aForm.getValues();
        
        Logger.out.debug("map-"+map);
        
        //Checking map contains value or not
    	if( map != null)
    	{
    		setIsDisableValue(searchFieldData,map);
    	}
    		
    	AdvanceSearchUI advSearchUI = new AdvanceSearchUI("images/SpecimenCollectionGroup.GIF",Constants.SPECIMEN_COLLECTION_GROUP,"spg.queryRule",searchFieldData,"advanceSearchForm");
    	request.setAttribute("AdvanceSearchUI",advSearchUI);
    	
    	String pageOf = (String)request.getParameter(Constants.PAGEOF);
    	return mapping.findForward(pageOf);
    }
}