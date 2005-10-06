package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.QueryColumnData;
import edu.wustl.catissuecore.domain.QueryTableData;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.logger.Logger;


/**
 * This is the action class for configuring data for QueryInterface
 * @author Poornima Govindrao
 *  
 */
public class ConfigureResultViewAction extends Action  {

	public final ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			AbstractBizLogic dao = BizLogicFactory.getBizLogic(Constants.CONFIGURE_RESULT_VIEW_ID);
			
			String []tableIds = (String [])request.getAttribute(Constants.TABLE_IDS);
			
			String sourceObjectName = QueryTableData.class.getName();
	        String[] displayNameField = {"displayName"};
	        String valueField = "aliasName";
	        
	        String[] whereColumnNames = {"identifier"};
	        String [] whereCondition = {"in"};
	        Object [] whereColumnValues = {tableIds};
	        //List of objects containing TableNames and aliasName
	        List tableList = dao.getList(sourceObjectName, displayNameField, valueField, 
    				whereColumnNames, whereCondition, whereColumnValues,null,null,false);
     
	        //List of Column data corresponding to table names.
	        sourceObjectName = QueryColumnData.class.getName();
	        String valueField1 = "columnName";
	        String [] whereCondition1 = {"="};
	        whereColumnNames[0] = "tableData.identifier";
	       
	        int i=0;
	        Map tableColumnDataMap = new HashMap();
	        
	        Iterator itr = tableList.iterator();
	        while(itr.hasNext() && i<tableIds.length)
	        {
	        	NameValueBean tableData = (NameValueBean)itr.next();
	        	
	        	List columnList =  new ArrayList();
	        	if(!tableData.getName().equals(Constants.SELECT_OPTION))
	        	{
	        		String []whereColumnValue = {tableIds[i]};
	        		columnList = dao.getList(sourceObjectName, displayNameField, valueField1, 
	        				whereColumnNames, whereCondition1, whereColumnValue,null,null,false);
	        		tableColumnDataMap.put(tableData,columnList);
	        		i++;
	        	}
	        	Logger.out.debug("Table Name"+ tableData.getName());
		        Logger.out.debug("Column List"+ columnList);
		        
	        }
	        
	        Logger.out.debug("Table Map"+tableColumnDataMap);
	        request.setAttribute(Constants.TABLE_COLUMN_DATA_MAP,tableColumnDataMap);
			return mapping.findForward("Success");
	}
}