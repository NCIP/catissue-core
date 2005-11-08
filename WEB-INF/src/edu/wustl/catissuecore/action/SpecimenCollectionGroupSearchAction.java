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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.DefaultBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.SearchUtil;
import edu.wustl.common.util.logger.Logger;

public class SpecimenCollectionGroupSearchAction extends BaseAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in SpecimenCollectionGroupSearch.jsp Page.
     * */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	DefaultBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
    	
    	//Setting the site list
    	List siteList = null;
    	
    	try
		{
		   	String displayFields[] = {"name"};
		   	siteList = bizLogic.getList(Site.class.getName(),displayFields,Constants.SYSTEM_IDENTIFIER, true);
		}
    	catch(Exception e)
		{
    		siteList = new ArrayList();
    		siteList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
    		Logger.out.debug(e.getMessage(),e);
		}
    	finally
		{
		   	request.setAttribute(Constants.SITELIST, siteList);
		}
	   	
    	//Setting the clinical diagnosis list
    	List clinicalDiagnosisList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_CLINICAL_DIAGNOSIS,null);
		request.setAttribute(Constants.CLINICAL_DIAGNOSIS_LIST, clinicalDiagnosisList);
		
		//Setting the clinical status list
		NameValueBean undefinedBean = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
        List clinicalStatusList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_CLINICAL_STATUS,undefinedBean);
    	request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusList);
    	
    	//Setting the operators list in request scope
        request.setAttribute(Constants.STRING_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_STRING));
        request.setAttribute(Constants.DATE_NUMERIC_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_NUMERIC));
        request.setAttribute(Constants.ENUMERATED_OPERATORS,SearchUtil.getOperatorList(SearchUtil.DATATYPE_ENUMERATED));
    	
    	String pageOf = (String)request.getParameter(Constants.PAGEOF);
    	return mapping.findForward(pageOf);
    }
}