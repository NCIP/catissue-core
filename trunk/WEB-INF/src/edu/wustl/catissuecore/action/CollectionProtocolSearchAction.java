/**
 * <p>Title: CollectionProtocolSearchAction Class>
 * <p>Description:	This class initializes the fields of CollectionProtocolSearchActionSearch.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Poornima Govindrao
 * @version 1.00
 * Created on Oct 25, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.vo.AdvanceSearchUI;
import edu.wustl.common.vo.SearchFieldData;

public class CollectionProtocolSearchAction extends AdvanceSearchUIAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in CollectionProtocolSearchAction.jsp Page.
     * */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
    	AdvanceSearchForm aForm = (AdvanceSearchForm)form;
    	
        //Sets the Principal Investigator attribute list
        UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
        Collection coll =  userBizLogic.getUsers(request.getParameter(Constants.OPERATION));
        request.setAttribute(Constants.USERLIST, coll);

        //Set the selected node from the query tree
        String nodeCount = (String)request.getParameter("selectedNode");
        aForm.setSelectedNode(nodeCount);

        //Setting the operators list in request scope
        request.setAttribute(Constants.STRING_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_STRING));
        request.setAttribute(Constants.DATE_NUMERIC_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_NUMERIC));
        request.setAttribute(Constants.ENUMERATED_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_ENUMERATED));

    	//An array required for intializing values of CollectionProtocolRegistrationSearch.jsp
        SearchFieldData[] searchFieldData = SearchUtil.getSearchFieldData(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
    	
     	String str = request.getParameter("itemId");
         Logger.out.debug("itemid-"+str);
         if(str != null)
         {
        	setMapValue(aForm,str,request);
        	aForm.setItemNodeId(str);
 		}
         
        Map map = aForm.getValues();
     	Logger.out.debug("map--"+map);
     	
        //Checking map contains value or not
    	if( map != null)
    	{
    		setIsDisableValue(searchFieldData,map);
    	}
     	
     	AdvanceSearchUI advSearchUI = new AdvanceSearchUI("images/CollectionProtocol.GIF",Constants.COLLECTION_PROTOCOL,"collectionProtocol.queryRule",searchFieldData,"advanceSearchForm");
     	request.setAttribute("AdvanceSearchUI",advSearchUI);
         
     	String pageOf = (String)request.getParameter(Constants.PAGEOF);
     	return mapping.findForward(pageOf);
     }
     
  
}