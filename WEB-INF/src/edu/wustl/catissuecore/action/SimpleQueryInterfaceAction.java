/**
 * <p>Title: SimpleQueryInterfaceAction Class>
 * <p>Description:	SimpleQueryInterfaceAction initializes the fields in the Simple Query Interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

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
import edu.wustl.catissuecore.domain.QueryColumnData;
import edu.wustl.catissuecore.domain.QueryTableData;
import edu.wustl.catissuecore.util.global.Constants;


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
                    String attributeNameList = "SimpleConditionsNode"+i;
                    
                    String [] displayNameFields = {"displayName"};
                    String valueField = "columnName";
                    String [] whereColumnNames = {"tableData.displayName"};
                    String [] whereCondition = {"="};
                    String [] whereColumnValues = {value};
                    
                    List columnList = dao.getList(QueryColumnData.class.getName(), displayNameFields,
                            		  			  valueField, whereColumnNames, whereCondition,
                            		  			  whereColumnValues,null,null);
                    
                    request.setAttribute(attributeNameList, columnList);
                }
            }
        }
        
        String sourceObjectName = QueryTableData.class.getName();
        String[] displayNameFields = {"displayName"};
        String valueField = "tableName";
        
        List tableList = dao.getList(sourceObjectName, displayNameFields, valueField);
        request.setAttribute(Constants.OBJECT_NAME_LIST, tableList);
        
        request.setAttribute(Constants.ATTRIBUTE_NAME_LIST, Constants.ATTRIBUTE_NAME_ARRAY);
        request.setAttribute(Constants.ATTRIBUTE_CONDITION_LIST, Constants.ATTRIBUTE_CONDITION_ARRAY);
        
        return mapping.findForward(Constants.SUCCESS);
    }
}
