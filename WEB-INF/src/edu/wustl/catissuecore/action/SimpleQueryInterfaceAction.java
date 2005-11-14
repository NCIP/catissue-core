/**
 * <p>Title: SimpleQueryInterfaceAction Class>
 * <p>Description:	SimpleQueryInterfaceAction initializes the fields in the Simple Query Interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.QueryBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.beans.NameValueBean;

/**
 * SimpleQueryInterfaceAction initializes the fields in the Simple Query Interface.
 * @author gautam_shetty
 */
public class SimpleQueryInterfaceAction extends SecureAction
{
    
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;
        int counter = Integer.parseInt(simpleQueryInterfaceForm.getCounter());
        QueryBizLogic queryBizLogic = (QueryBizLogic) BizLogicFactory.getBizLogic(simpleQueryInterfaceForm.getFormId());  
        
        for (int i=1;i<=counter;i++)
        {
            //Key of present object.
            String key = "SimpleConditionsNode:"+i+"_Condition_DataElement_table";
            String value = (String)simpleQueryInterfaceForm.getValue(key);
            
            Validator validator = new Validator();
            if ((value != null) && (validator.isValidOption(value)))
            {
                List columnNameValueBeanList = queryBizLogic.getColumnNames(value);
                if (columnNameValueBeanList.isEmpty() == false)
                {
                    String attributeNameList = "attributeNameList"+i;
                    request.setAttribute(attributeNameList, columnNameValueBeanList);
                }
            }
            
            if (i == counter) 
            {
                //Key of previous object.
                String prevKey = "SimpleConditionsNode:"+(i-1)+"_Condition_DataElement_table";
                String prevValue = (String)simpleQueryInterfaceForm.getValue(prevKey);
                
                //If previous table name is not null, get the list of table name related to it.
                if (prevValue != null)
                {
                    Set nextTableNameList = queryBizLogic.getNextTableNames(prevValue);
                    if (nextTableNameList.isEmpty() == false)
                    {
                        String objectNameList = "objectList"+i;
                        request.setAttribute(objectNameList, nextTableNameList);
                    }
                }
                else
                {
                    String aliasName = request.getParameter(Constants.TABLE_ALIAS_NAME);
                    request.setAttribute(Constants.TABLE_ALIAS_NAME,aliasName);
                    
                    // Get all the table names.  
                    Set objectNameValueBeanList = queryBizLogic.getAllTableNames(aliasName);
                    if (objectNameValueBeanList.isEmpty() == false)
                    {
                        request.setAttribute(Constants.OBJECT_NAME_LIST, objectNameValueBeanList);
                    }
                    
                    if ((aliasName != null) && (!"".equals(aliasName)))
            		{
            		    List columnNameValueBeanList = queryBizLogic.getColumnNames(aliasName);
                        if (columnNameValueBeanList.isEmpty() == false)
                        {
                            String attributeNameList = "attributeNameList1";
                            request.setAttribute(attributeNameList, columnNameValueBeanList);
                        }
            		}
                }
            }
            else
            {
                //Key of the next operator (AND/OR).
                String nextOperatorKey = "SimpleConditionsNode:"+i+"_Operator_operator";
                String nextOperatorValue = (String)simpleQueryInterfaceForm.getValue(nextOperatorKey);
                
                if (nextOperatorValue != null && !"".equals(nextOperatorValue))
                {
                    String objectNameValueBeanList = "objectList"+i;
                    String prevValueDisplayName = queryBizLogic.getDisplayName(value);
                    
                    NameValueBean nameValueBean = new NameValueBean();
                    nameValueBean.setName(prevValueDisplayName);
                    nameValueBean.setValue(value);
                    
                    List objectList = new ArrayList();
                    objectList.add(nameValueBean);
                    
                    request.setAttribute(objectNameValueBeanList, objectList);
                }
            }
        }
        
        request.setAttribute(Constants.ATTRIBUTE_NAME_LIST, Constants.ATTRIBUTE_NAME_ARRAY);
        request.setAttribute(Constants.ATTRIBUTE_CONDITION_LIST, Constants.ATTRIBUTE_CONDITION_ARRAY);
        
        HttpSession session = request.getSession();
        session.setAttribute(Constants.SIMPLE_QUERY_ALIAS_NAME,null);
        session.setAttribute(Constants.SIMPLE_QUERY_COUNTER,null);
        session.setAttribute(Constants.SIMPLE_QUERY_MAP,null);
        
        String pageOf = request.getParameter(Constants.PAGEOF);
        request.setAttribute(Constants.PAGEOF, pageOf);
        
        return mapping.findForward(pageOf);
    }
    
    /**
     * Returns the object id of the protection element that represents
     * the Action that is being requested for invocation.
     * @param clazz
     * @return
     */
    protected String getObjectIdForSecureMethodAccess(HttpServletRequest request)
    {
        String aliasName = request.getParameter("aliasName");
        if(aliasName!=null && !aliasName.equals(""))
        {
            return this.getClass().getName()+"_"+aliasName;
        }
        else
        {
            return super.getObjectIdForSecureMethodAccess(request);
        }
    }
    
    /**
     * @param mapping
     * @return
     */
    protected ActionForward getActionForward(HttpServletRequest request,ActionMapping mapping)
    {
        String aliasName = request.getParameter("aliasName");
        if(aliasName.equals("User") || aliasName.equals("Institution") || aliasName.equals("Department")|| aliasName.equals("CancerResearchGroup")|| aliasName.equals("Site")|| aliasName.equals("StorageType")|| aliasName.equals("StorageContainer")|| aliasName.equals("BioHazard")|| aliasName.equals("CollectionProtocol")|| aliasName.equals("DistributionProtocol"))
        {
            return mapping.findForward(Constants.ACCESS_DENIED_ADMIN);
        }
        else if(aliasName.equals("Participant") ||aliasName.equals("CollectionProtocolRegistration") ||aliasName.equals("SpecimenCollectionGroup") ||aliasName.equals("Specimen") ||aliasName.equals("Distribution"))
        {
            return mapping.findForward(Constants.ACCESS_DENIED_BIOSPECIMEN);
        }
        else
        {
            return mapping.findForward(Constants.ACCESS_DENIED);
        }
    }
}