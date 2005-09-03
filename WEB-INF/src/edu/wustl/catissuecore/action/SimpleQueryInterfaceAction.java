/**
 * <p>Title: SimpleQueryInterfaceAction Class>
 * <p>Description:	SimpleQueryInterfaceAction initializes the fields in the Simple Query Interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.dao.JDBCDAO;
import edu.wustl.catissuecore.domain.QueryTableData;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


/**
 * SimpleQueryInterfaceAction initializes the fields in the Simple Query Interface.
 * @author gautam_shetty
 */
public class SimpleQueryInterfaceAction extends Action
{
    
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;
        int counter = Integer.parseInt(simpleQueryInterfaceForm.getCounter());
        AbstractBizLogic dao = BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
        
        for (int i=1;i<=counter;i++)
        {
            String key = "SimpleConditionsNode:"+i+"_Condition_DataElement_table";
            String value = (String)simpleQueryInterfaceForm.getValue(key);
            if (value != null)
            {
                if (!value.equals(Constants.SELECT_OPTION))
                {
                    String attributeNameList = "attributeNameList"+i;
					String attributeDisplayNameList = "attributeDisplayNameList"+i;
					
                    String [] displayNameFields = {"displayName"};
                    String valueField = "columnName";
                    String [] whereColumnNames = {"tableData.displayName"};
                    String [] whereCondition = {"="};
                    String [] whereColumnValues = {value};
                    
                    String sql = "SELECT columnData.COLUMN_NAME,columnData.DISPLAY_NAME," +
                    			 "columnData.ATTRIBUTE_TYPE FROM CATISSUE_QUERY_INTERFACE_COLUMN_DATA columnData," +
                    			 "CATISSUE_TABLE_RELATION relationData," +
                    			 "CATISSUE_QUERY_INTERFACE_TABLE_DATA tableData " +
                    			 "where relationData.CHILD_TABLE_ID = columnData.TABLE_ID " +
                    			 "and relationData.PARENT_TABLE_ID = tableData.TABLE_ID " +
                    			 "and tableData.ALIAS_NAME='"+value+"'";
                    
                    Logger.out.debug("SQL*****************************"+sql);
                    
                    JDBCDAO jdbcDao = new JDBCDAO();
                    jdbcDao.openSession();
                    List list = jdbcDao.executeQuery(sql);
                    jdbcDao.closeSession();
                    
                    String [] columnNameList = new String[list.size()];
                    String [] columnDisplayNameList = new String[list.size()];
                    String [] columnTypeList = new String[list.size()];
                    
                    Iterator iterator = list.iterator();
                    int j = 0,k=0;
                    while (iterator.hasNext())
                    {
                        List rowList = (List)iterator.next();
                        columnNameList[k] = (String)rowList.get(j);
                        j++;
                        columnDisplayNameList[k] = (String)rowList.get(j);
                        j++;
                        columnTypeList[k] = (String)rowList.get(j);
                        j = 0;
                        k++;
                    }
                    
                    request.setAttribute(attributeNameList, columnNameList);
                    request.setAttribute(attributeDisplayNameList, columnDisplayNameList);
                }
            }
        }
        
        String sourceObjectName = QueryTableData.class.getName();
        String[] displayNameFields = {"displayName"};
        String valueField = "aliasName";
        
        String aliasName = request.getParameter("aliasName");
        request.setAttribute(Constants.TABLE_ALIAS_NAME,aliasName);
        String [] whereColumnValues = {aliasName};
        String[] whereColumnNames = {"aliasName"};
        String [] whereCondition = {"="};
        
        List tableList = dao.getList(sourceObjectName, displayNameFields, valueField, 
                				whereColumnNames, whereCondition, whereColumnValues,null,null);
        request.setAttribute(Constants.OBJECT_NAME_LIST, tableList);
        
        request.setAttribute(Constants.ATTRIBUTE_NAME_LIST, Constants.ATTRIBUTE_NAME_ARRAY);
        request.setAttribute(Constants.ATTRIBUTE_CONDITION_LIST, Constants.ATTRIBUTE_CONDITION_ARRAY);
        
        String pageOf = request.getParameter(Constants.PAGEOF);
        request.setAttribute(Constants.PAGEOF, pageOf);
        
        return mapping.findForward(pageOf);
    }
}
