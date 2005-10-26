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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.util.SearchUtil;
import edu.wustl.common.util.logger.Logger;

public class CollectionProtocolSearchAction extends BaseAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in CollectionProtocolSearchAction.jsp Page.
     * */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        try
		{
        	NameValueBean unknownVal = new NameValueBean(Constants.UNKNOWN,Constants.UNKNOWN);
        	
        	//Sets the Principal Investigator attribute list
        	UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
        	Collection coll =  userBizLogic.getUsers();
        	request.setAttribute(Constants.USERLIST, coll);
        	
        	//Sets the tissue site list 
	    	List tissueSiteList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_TISSUE_SITE,unknownVal);
	    	request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteList);
	    	
	    	//Sets the Pathology Status List
	    	List pathologyStatusList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_PATHOLOGICAL_STATUS,null);
	    	request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, pathologyStatusList);
	    	
	    	//Sets the Clinical Status List
	    	List clinicalStatusList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_CLINICAL_STATUS,unknownVal);
	    	request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusList);
	    	
	    	// get the Specimen class and type from the cde
	    	List specimenTypeList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_SPECIMEN_TYPE,null);
	    	request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);

        	CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
	    	Set setPV = specimenClassCDE.getPermissibleValues();
	    	Logger.out.debug("2");
	    	Iterator itr = setPV.iterator();
	    
	    	List specimenClassList =  new ArrayList();
	    	Map subTypeMap = new HashMap();
	    	Logger.out.debug("\n\n\n\n**********MAP DATA************\n");
	    	specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
	    	while(itr.hasNext())
	    	{
	    		List innerList =  new ArrayList();
	    		Object obj = itr.next();
	    		PermissibleValue pv = (PermissibleValue)obj;
	    		String tmpStr = pv.getValue();
	    		Logger.out.debug(tmpStr);
	    		specimenClassList.add(new NameValueBean( tmpStr,tmpStr));
	    		
				Set list1 = pv.getSubPermissibleValues();
				Logger.out.debug("list1 "+list1);
	        	Iterator itr1 = list1.iterator();
	        	innerList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
	        	while(itr1.hasNext())
	        	{
	        		Object obj1 = itr1.next();
	        		PermissibleValue pv1 = (PermissibleValue)obj1;
	        		// set specimen type
	        		String tmpInnerStr = pv1.getValue(); 
	        		Logger.out.debug("\t\t"+tmpInnerStr);
	        		innerList.add(new NameValueBean( tmpInnerStr,tmpInnerStr));  
	        	}
	        	subTypeMap.put(pv.getValue(),innerList);
	    	} // class and values set
	    	Logger.out.debug("\n\n\n\n**********MAP DATA************\n");
	    	
	    	// sets the Class list
	    	request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
	    	

	    	// set the map to subtype
	    	request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);
	    	
	        //Setting the operators list in request scope
	        request.setAttribute(Constants.STRING_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_STRING));
	        request.setAttribute(Constants.DATE_NUMERIC_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_NUMERIC));
	        request.setAttribute(Constants.ENUMERATED_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_ENUMERATED));
		}
    	catch(Exception e)
		{
    		Logger.out.error(e.getMessage(),e);
		}
    	String pageOf = (String)request.getParameter(Constants.PAGEOF);
    	return mapping.findForward(pageOf);
    }
}