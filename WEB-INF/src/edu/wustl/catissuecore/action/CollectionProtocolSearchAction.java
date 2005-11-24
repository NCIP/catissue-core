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

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.vo.AdvanceSearchUI;
import edu.wustl.catissuecore.vo.SearchFieldData;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.SearchUtil;
import edu.wustl.common.util.logger.Logger;

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

        NameValueBean unknownVal = new NameValueBean(Constants.UNKNOWN,Constants.UNKNOWN);
        	
        //Sets the Principal Investigator attribute list
        UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
        Collection coll =  userBizLogic.getUsers();
        request.setAttribute(Constants.USERLIST, coll);

        //Set the selected node from the query tree
        String nodeCount = (String)request.getParameter("selectedNode");
        aForm.setSelectedNode(nodeCount);

        //Setting the operators list in request scope
        request.setAttribute(Constants.STRING_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_STRING));
        request.setAttribute(Constants.DATE_NUMERIC_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_NUMERIC));
        request.setAttribute(Constants.ENUMERATED_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_ENUMERATED));

    	SearchFieldData[] searchFieldData = new SearchFieldData[8];
    	searchFieldData[0] = initSearchUIData(SearchUtil.STRING, "collectionprotocol.principalinvestigator","SpecimenProtocol","PRINCIPAL_INVESTIGATOR_ID","principalInvestigator",Constants.ENUMERATED_OPERATORS,Constants.USERLIST,"");
    	searchFieldData[1] = initSearchUIData(SearchUtil.STRING,"collectionprotocol.protocoltitle","SpecimenProtocol","TITLE","title",Constants.STRING_OPERATORS,"","");
    	searchFieldData[2] = initSearchUIData(SearchUtil.STRING, "collectionprotocol.shorttitle","SpecimenProtocol","SHORT_TITLE","shortTitle",Constants.STRING_OPERATORS,"","");
    	searchFieldData[3] = initSearchUIData(SearchUtil.STRING, "collectionprotocol.irbid","SpecimenProtocol","IRB_IDENTIFIER","irbIdentifier",Constants.STRING_OPERATORS,"","");
    	searchFieldData[4] = initSearchUIData(SearchUtil.DATE, "collectionprotocol.startdate","SpecimenProtocol","START_DATE","startDate",Constants.DATE_NUMERIC_OPERATORS,"","");
    	searchFieldData[5] = initSearchUIData(SearchUtil.DATE, "collectionprotocol.enddate","SpecimenProtocol","END_DATE","endDate",Constants.DATE_NUMERIC_OPERATORS,"","");
    	searchFieldData[6] = initSearchUIData(SearchUtil.STRING, "collectionProtocolReg.participantProtocolID","CollectionProtocolRegistration","PROTOCOL_PARTICIPANT_IDENTIFIER","protocolParticipantId",Constants.STRING_OPERATORS,"","");
    	searchFieldData[7] = initSearchUIData(SearchUtil.DATE, "collectionProtocolReg.participantRegistrationDate","CollectionProtocolRegistration","REGISTRATION_DATE","regDate",Constants.DATE_NUMERIC_OPERATORS,"","");
    	
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
     	
     	AdvanceSearchUI advSearchUI = new AdvanceSearchUI("images/CollectionProtocol.GIF","CollectionProtocol","collectionProtocol.queryRule",searchFieldData,"advanceSearchForm");
     	request.setAttribute("AdvanceSearchUI",advSearchUI);
         
     	String pageOf = (String)request.getParameter(Constants.PAGEOF);
     	return mapping.findForward(pageOf);
     }
     
  
}