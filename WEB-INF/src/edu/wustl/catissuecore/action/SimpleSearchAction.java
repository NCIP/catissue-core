/**
 * Created on Aug 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.action;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.QueryFactory;
import edu.wustl.catissuecore.query.SimpleConditionsNode;
import edu.wustl.catissuecore.query.SimpleQuery;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author gautam_shetty
 */
public class SimpleSearchAction extends DispatchAction
{
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;
        
        //Get the aliasName.
        String aliasName = (String)simpleQueryInterfaceForm
        						.getValue("SimpleConditionsNode:1_Condition_DataElement_table");
        
        String target = Constants.SUCCESS;
        
        try
        {
            Map map = simpleQueryInterfaceForm.getValuesMap();
            MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.query");
            Collection simpleConditionNodeCollection = parser.generateData(map, true);
            Iterator iterator = simpleConditionNodeCollection.iterator();
            
            Set fromTables = new HashSet(); 
            
            SimpleConditionsNode simpleConditionsNode = null;
            while (iterator.hasNext())
            {
                simpleConditionsNode = (SimpleConditionsNode)iterator.next();
                String columnName = simpleConditionsNode.getCondition().getDataElement().getField();
                StringTokenizer stringToken = new StringTokenizer(columnName,".");
                simpleConditionsNode.getCondition().getDataElement().setTable(stringToken.nextToken());
                simpleConditionsNode.getCondition().getDataElement().setField(stringToken.nextToken());
                String fieldType = stringToken.nextToken();
                String value = simpleConditionsNode.getCondition().getValue();
                
                if (fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_VARCHAR) || fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_DATE))
                {
                    if (fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_VARCHAR))
                    {
                        value = "'"+value+"'";
                    }
                    else
                    {
                        value = "STR_TO_DATE('"+value+"','"+Constants.MYSQL_DATE_PATTERN+"')";
                    }
                    simpleConditionsNode.getCondition().setValue(value);
                }
                
                if (simpleQueryInterfaceForm.getPageOf().equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
                {
                    //Prepare a Set of table names.
                    fromTables.add(simpleConditionsNode.getCondition().getDataElement().getTable());
                }
            }
            
            String fullyQualifiedClassName = "edu.wustl.catissuecore.domain."+aliasName;
            SimpleConditionsNode activityStatusCondition = getActivityStatusCondition(fullyQualifiedClassName); 
            if(activityStatusCondition != null)
            {
                simpleConditionsNode.getOperator().setOperator(Constants.AND_JOIN_CONDITION);
                simpleConditionNodeCollection.add(activityStatusCondition);
            }
            
            Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, aliasName);
            
            if (simpleQueryInterfaceForm.getPageOf().equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
            {
                query.setTableSet(fromTables);
            }
            
            //Get the view columns.
            String [] columnNames = query.setViewElements(aliasName);
            
            ((SimpleQuery)query).addConditions(simpleConditionNodeCollection);
            List list = query.execute();
            Logger.out.debug("List......................"+list+" List Size..................."+list.size());
            
            if (list.isEmpty())
            {
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("simpleQuery.noRecordsFound"));
                saveErrors(request,errors);
                
                String path = "SimpleQueryInterface.do?pageOf="+simpleQueryInterfaceForm.getPageOf()+"&aliasName="+simpleQueryInterfaceForm.getAliasName();
                RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
                requestDispatcher.forward(request,response);
            }
            else
            {
                if ((list.size() == 1) 
                     && (simpleQueryInterfaceForm.getPageOf().equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE)==false))
                {
                    List rowList = (List)list.get(0);
                    String action = "SearchObject.do?pageOf="+simpleQueryInterfaceForm.getPageOf() + 
                    				"&operation=search&systemIdentifier=" + rowList.get(0);
                    
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher(action);
                    requestDispatcher.forward(request,response);
                }
                else
                {
                    request.setAttribute(Constants.PAGEOF, simpleQueryInterfaceForm.getPageOf());
                    request.setAttribute(Constants.SPREADSHEET_DATA_LIST, list);
                    request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnNames);
                }
            }
        }
        catch(DAOException daoExp)
        {
            Logger.out.debug(daoExp.getMessage(), daoExp);
            target = Constants.FAILURE;
        }
        
        return mapping.findForward(target);
    }
    
    /**
     * Returns SimpleConditionsNode if the object named aliasName contains the activityStatus 
     * data member, else returns false.
     * @param aliasName
     * @return
     */
    private SimpleConditionsNode getActivityStatusCondition(String fullyQualifiedClassName)
    {
        SimpleConditionsNode activityStatusCondition = null;
        
        try
        {
            Class className = Class.forName(fullyQualifiedClassName);
            
            Field[] objectFields = className.getDeclaredFields();
            for (int i=0;i<objectFields.length;i++)
            {
                if (objectFields[i].getName().equals(Constants.ACTIVITY_STATUS))
                {
                    activityStatusCondition = new SimpleConditionsNode();
                    
                    activityStatusCondition.getCondition().getDataElement().setTable(Utility.parseClassName(fullyQualifiedClassName));
                    activityStatusCondition.getCondition().getDataElement().setField("ACTIVITY_STATUS");
                    activityStatusCondition.getCondition().getOperator().setOperator("!=");
                    activityStatusCondition.getCondition().setValue("'"+Constants.ACTIVITY_STATUS_DISABLED+"'");
                }
            }
            
            if ((activityStatusCondition == null) && 
                   (className.getSuperclass().getName().equals("edu.wustl.catissuecore.domain.AbstractDomainObject") == false))
            {
                activityStatusCondition = getActivityStatusCondition(className.getSuperclass().getName());
            }
        }
        catch(ClassNotFoundException classNotExcp)
        {
            Logger.out.debug(classNotExcp.getMessage(),classNotExcp);
        }
        
        return activityStatusCondition;
    }
}
