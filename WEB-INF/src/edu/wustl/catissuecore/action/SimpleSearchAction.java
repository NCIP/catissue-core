/*
 * Created on Aug 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.catissuecore.query.Client;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.QueryFactory;
import edu.wustl.catissuecore.query.SimpleQuery;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.MapDataParser;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleSearchAction extends Action
{
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        
        SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;
        Map map = simpleQueryInterfaceForm.getValuesMap();
        
        MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.query");
        
        Collection simpleConditionNodeCollection = parser.generateData(map);
        
        Client.initialize();
        
        Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, Query.PARTICIPANT);
//        HttpSession session = request.getSession();
//        String[] columnList = (String[])request.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
//        
//        if (columnList == null)
//        {
//            columnList = Constants.DEFAULT_SPREADSHEET_COLUMNS;
//        }
        
        query.addElementToView(new DataElement(Query.PARTICIPANT,"IDENTIFIER"));
        query.addElementToView(new DataElement(Query.COLLECTION_PROTOCOL,"IDENTIFIER"));
        query.addElementToView(new DataElement(Query.COLLECTION_PROTOCOL_EVENT,"IDENTIFIER"));
        query.addElementToView(new DataElement(Query.SPECIMEN_COLLECTION_GROUP,"IDENTIFIER"));
        query.addElementToView(new DataElement(Query.SPECIMEN,"IDENTIFIER"));
        query.addElementToView(new DataElement(Query.SPECIMEN,"TYPE"));
        
        ((SimpleQuery)query).addConditions(simpleConditionNodeCollection);
        
        query.execute();
        
        request.setAttribute(Constants.STATUS_MESSAGE_KEY,"2.true");
        
        return mapping.findForward(Constants.SUCCESS);
    }
}
