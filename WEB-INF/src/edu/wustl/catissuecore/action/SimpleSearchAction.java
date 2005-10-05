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
        String viewAliasName = (String)simpleQueryInterfaceForm
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
            
            if(hasActivityStatus(viewAliasName))
            {
                Logger.out.debug("Adding ActivityStatus............................");
                SimpleConditionsNode activityStatusCondition = new SimpleConditionsNode();
                activityStatusCondition.getCondition().getDataElement().setTable(viewAliasName);
                activityStatusCondition.getCondition().getDataElement().setField("ACTIVITY_STATUS");
                activityStatusCondition.getCondition().getOperator().setOperator("!=");
                activityStatusCondition.getCondition().setValue("'"+Constants.ACTIVITY_STATUS_DISABLED+"'");
                
                simpleConditionsNode.getOperator().setOperator(Constants.AND_JOIN_CONDITION);
                simpleConditionNodeCollection.add(activityStatusCondition);
            }
            
//            Iterator iterator1 = fromTables.iterator();
//            while (iterator1.hasNext())
//            {
//                Logger.out.debug("From tABLES............................."+iterator1.next());
//            }
            Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, viewAliasName);
            
            //Get the view columns.
            String [] columnNames = query.setViewElements(viewAliasName);
            
            if (simpleQueryInterfaceForm.getPageOf().equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
            {
                query.setTableSet(fromTables);
                Logger.out.debug("setTableSet.......................................");
            }
            
            ((SimpleQuery)query).addConditions(simpleConditionNodeCollection);
            List list = query.execute();
            
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
                if (list.size() == 1)
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
     * Returns true if the object named aliasName contains the activityStatus 
     * data member, else returns false.
     * @param aliasName
     * @return
     */
    private boolean hasActivityStatus(String aliasName)
    {
        try
        {
            Class className = Class.forName("edu.wustl.catissuecore.domain."+aliasName);
            Logger.out.debug("Class.................."+className.getName());
            Field[] objectFields = className.getDeclaredFields();
            Logger.out.debug("Field Size..........................."+objectFields.length);
            for (int i=0;i<objectFields.length;i++)
            {
                Logger.out.debug("objectFields[i].getName().............................."+objectFields[i].getName());
                if (objectFields[i].getName().equals(Constants.ACTIVITY_STATUS))
                {
                    return true;
                }
            }
        }
        catch(ClassNotFoundException classNotExcp)
        {
            Logger.out.debug(classNotExcp.getMessage(),classNotExcp);
        }
        
        return false;
    }
}
