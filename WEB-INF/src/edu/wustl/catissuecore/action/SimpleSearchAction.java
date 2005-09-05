/**
 * Created on Aug 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import edu.wustl.catissuecore.query.Client;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.QueryFactory;
import edu.wustl.catissuecore.query.SimpleConditionsNode;
import edu.wustl.catissuecore.query.SimpleQuery;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.MapDataParser;

/**
 * @author gautam_shetty
 */
public class SimpleSearchAction extends DispatchAction
{
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        
        SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;
        Map map = simpleQueryInterfaceForm.getValuesMap();
        
        MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.query");
        
        Collection simpleConditionNodeCollection = parser.generateData(map);
        
        Iterator iterator = simpleConditionNodeCollection.iterator();
        
        while (iterator.hasNext())
        {
            SimpleConditionsNode simpleConditionsNode = (SimpleConditionsNode)iterator.next();
            String columnName = simpleConditionsNode.getCondition().getDataElement().getField();
            StringTokenizer stringToken = new StringTokenizer(columnName,".");
            simpleConditionsNode.getCondition().getDataElement().setTable(stringToken.nextToken());
            simpleConditionsNode.getCondition().getDataElement().setField(stringToken.nextToken());
            String fieldType = stringToken.nextToken();
            String value = simpleConditionsNode.getCondition().getValue();
            
            if (fieldType.equals(Constants.FIELD_TYPE_VARCHAR) || fieldType.equals(Constants.FIELD_TYPE_DATE))
            {
                value = "'"+value+"'";
                simpleConditionsNode.getCondition().setValue(value);
            }
        }
        
        Client.initialize();
        
        Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, simpleQueryInterfaceForm.getAliasName());
        
        String [] columnNames = query.setViewElements(simpleQueryInterfaceForm.getAliasName());
        
//        HttpSession session = request.getSession();
//        String[] columnList = (String[])request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
//        
//        if (columnList == null)
//        {
//            columnList = Constants.DEFAULT_SPREADSHEET_COLUMNS;
//        }
          
//        query.addElementToView(new DataElement(Query.PARTICIPANT,"IDENTIFIER"));
//        query.addElementToView(new DataElement(Query.COLLECTION_PROTOCOL,"IDENTIFIER"));
//        query.addElementToView(new DataElement(Query.COLLECTION_PROTOCOL_EVENT,"IDENTIFIER"));
//        query.addElementToView(new DataElement(Query.SPECIMEN_COLLECTION_GROUP,"IDENTIFIER"));
//        query.addElementToView(new DataElement(Query.SPECIMEN,"IDENTIFIER"));
//        query.addElementToView(new DataElement(Query.SPECIMEN,"TYPE"));
//        query.addElementToView(new DataElement(Query.DEPARTMENT,"IDENTIFIER"));
//        query.addElementToView(new DataElement(Query.DEPARTMENT,"NAME"));
        
        ((SimpleQuery)query).addConditions(simpleConditionNodeCollection);
        
        List list = query.execute();
        String target = simpleQueryInterfaceForm.getPageOf();
        
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
            request.setAttribute(Constants.SPREADSHEET_DATA_LIST, list);
            request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnNames);
        }
        
        return mapping.findForward(target);
    }
    
}
