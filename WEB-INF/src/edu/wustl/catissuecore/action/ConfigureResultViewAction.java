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
import edu.wustl.catissuecore.dao.JDBCDAO;
import edu.wustl.catissuecore.domain.QueryColumnData;
import edu.wustl.catissuecore.domain.QueryTableData;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


/**
 * This is the action class for configuring data 
 * @author Poornima Govindrao
 *  
 */
public class ConfigureResultViewAction extends Action  {

	public final ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			AbstractBizLogic dao = BizLogicFactory.getBizLogic(Constants.CONFIGURE_RESULT_VIEW_ID);
			
			String []tables = (String [])request.getAttribute(Constants.CONFIGURATION_TABLES);
			
			String sourceObjectName = QueryTableData.class.getName();
	        String[] displayNameField = {"displayName"};
	        String valueField = "aliasName";
	        
	        String[] whereColumnNames = {"aliasName"};
	        String [] whereCondition = {"in"};
	        Object [] whereColumnValues = {tables};
	        //List of objects containing TableNames and aliasName
	        List tableList = dao.getList(sourceObjectName, displayNameField, valueField, 
    				whereColumnNames, whereCondition, whereColumnValues,null,null,false);
     
	        //List of Column data corresponding to table names.
	        /*sourceObjectName = QueryColumnData.class.getName();
	        String valueField1 = "columnName";
	        String [] whereCondition1 = {"="};
	        whereColumnNames[0] = "tableData.identifier";*/
	       
	      
	        Map tableColumnDataMap = new HashMap();
	        
	        Iterator itr = tableList.iterator();
	        while(itr.hasNext())
	        {
	        	NameValueBean tableData = (NameValueBean)itr.next();
	        	if(!tableData.getName().equals(Constants.SELECT_OPTION))
	        	{
	        		List columnList =  setColumnNames(tableData.getName());
	        		tableColumnDataMap.put(tableData,columnList);
	        		
	        	}
	        	Logger.out.debug("Table Name"+ tableData.getName());
		        //Logger.out.debug("Column List"+ columnList);
		        
	        }
	        
	        Logger.out.debug("Table Map"+tableColumnDataMap);
	        request.setAttribute(Constants.TABLE_COLUMN_DATA_MAP,tableColumnDataMap);
			return mapping.findForward("Success");
	}
	private List setColumnNames(String value) throws DAOException, ClassNotFoundException
    {
        String sql = 	" SELECT temp.COLUMN_NAME, temp.DISPLAY_NAME " +
				        " from CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData2 join " +
				        " ( SELECT  columnData.COLUMN_NAME, columnData.TABLE_ID, displayData.DISPLAY_NAME " +
				        " FROM CATISSUE_QUERY_INTERFACE_COLUMN_DATA columnData, " +
				        " CATISSUE_TABLE_RELATION relationData, " +
				        " CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData, " +
				        " CATISSUE_SEARCH_DISPLAY_DATA displayData " +
				        " where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and " +
				        " relationData.PARENT_TABLE_ID = tableData.TABLE_ID and " +
				        " relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and " +
				        " columnData.IDENTIFIER = displayData.COL_ID and " +
				        " tableData.ALIAS_NAME = '"+value+"') as temp " +
				        " on temp.TABLE_ID = tableData2.TABLE_ID";
        
        Logger.out.debug("SQL*****************************"+sql);
        
        JDBCDAO jdbcDao = new JDBCDAO();
        jdbcDao.openSession(null);
        List list = jdbcDao.executeQuery(sql, null, Constants.INSECURE_RETRIEVE, null,null);
        jdbcDao.closeSession();
        
        String columnName = new String();
        String columnDisplayName = new String();
        List columnList = new ArrayList();
        Iterator iterator = list.iterator();
        int j = 0,k=0;
        while (iterator.hasNext())
        {
            List rowList = (List)iterator.next();
            columnName = (String)rowList.get(j++);
            columnDisplayName = (String)rowList.get(j++);
            String columnValue = value+"."+columnDisplayName+"."+columnName;
            NameValueBean columns = new NameValueBean(columnDisplayName,columnValue);
            columnList.add(columns);
            j = 0;
            k++;
        }
        
        return columnList;
    }
}