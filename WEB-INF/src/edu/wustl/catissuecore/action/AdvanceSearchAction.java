

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.catissuecore.query.AdvancedConditionsImpl;
import edu.wustl.catissuecore.query.AdvancedConditionsNode;
import edu.wustl.catissuecore.query.AdvancedQuery;
import edu.wustl.catissuecore.query.Condition;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.QueryFactory;
import edu.wustl.catissuecore.query.SimpleConditionsNode;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


public class AdvanceSearchAction extends DispatchAction
{
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;
        
        //Get the aliasName.
        String aliasName = (String)simpleQueryInterfaceForm
        						.getValue("SimpleConditionsNode:1_Condition_DataElement_table");
        
        String target = Constants.SUCCESS;
        Set fromTables = new HashSet();
        
        try
        {
            Map map = simpleQueryInterfaceForm.getValuesMap();
            MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.query");
            Collection simpleConditionNodeCollection = parser.generateData(map, true);
            Iterator iterator = simpleConditionNodeCollection.iterator();
            
            //Set fromTables = new HashSet(); 
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
                
                if (fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_VARCHAR))
                {
                    if (fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_VARCHAR))
                    {
                        value = "'"+value+"'";
                    }
                    
                    simpleConditionsNode.getCondition().setValue(value);
                }
                
                //Prepare a Set of table names.
				fromTables.add(simpleConditionsNode.getCondition().getDataElement().getTable());
            }
            Logger.out.debug("collection size: "+simpleConditionNodeCollection.size());
            Iterator itr = simpleConditionNodeCollection.iterator();
            SimpleConditionsNode simpleCondNode = (SimpleConditionsNode)itr.next();
            Condition condition = simpleCondNode.getCondition();
            String table = condition.getDataElement().getTable();
            Logger.out.debug("table: "+table);
            AdvancedConditionsNode advancedConditionsNode = new AdvancedConditionsNode(table);
            advancedConditionsNode.addConditionToNode(condition);
           	while (itr.hasNext())
            {
           		simpleCondNode = (SimpleConditionsNode)itr.next();
           		condition = simpleCondNode.getCondition();
               	advancedConditionsNode.addConditionToNode(condition);
            }
           	DefaultMutableTreeNode child = new DefaultMutableTreeNode(advancedConditionsNode);
           	
           	HttpSession session = request.getSession();
           	DefaultMutableTreeNode root = (DefaultMutableTreeNode)session.getAttribute("root");
            if(root.getChildCount()==0)
            {
            	Logger.out.debug("Adding child to root**********");
            	root.add(child);
            }
            else
            {
            	Logger.out.debug("child count"+root.getChildCount());
            	root.getLastLeaf().add(child);
            }
            traverseTree(root);
            Query query = QueryFactory.getInstance().newQuery(Query.ADVANCED_QUERY, aliasName);
            session.setAttribute("root",root);
            
           
            //Get the view columns.
            List columnNames = query.setViewElements(fromTables);
            
            ((AdvancedConditionsImpl)((AdvancedQuery)query).getWhereConditions()).setWhereCondition(root);
//          List list = query.execute();
            /*List list = new ArrayList();
            Logger.out.debug("List......................"+list+" List Size..................."+list.size());
            
            if (list.isEmpty())
            {
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("simpleQuery.noRecordsFound"));
                saveErrors(request,errors);
                
                String path = "SimpleQueryInterface.do?pageOf="+simpleQueryInterfaceForm.getPageOf()+"&aliasName="+simpleQueryInterfaceForm.getAliasName();
                RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
                requestDispatcher.forward(request,response);
            }*/

        }
        catch(DAOException daoExp)
        {
            Logger.out.debug(daoExp.getMessage(), daoExp);
            target = Constants.FAILURE;
        }
        
        return mapping.findForward(target);
    }
    private static void traverseTree(DefaultMutableTreeNode tree)
	{
		DefaultMutableTreeNode child = new DefaultMutableTreeNode();
		int level = tree.getLevel();
		int childCount = tree.getChildCount();
		Logger.out.debug("childCount"+childCount);
		Logger.out.debug("Level"+level);
		for(int i=0;i<childCount;i++)
		{
			child = (DefaultMutableTreeNode)tree.getChildAt(i);
			AdvancedConditionsNode advNode1 = (AdvancedConditionsNode)child.getUserObject();
			Vector conditions1 = advNode1.getObjectConditions();
			Iterator itr1 = conditions1.iterator();
			while(itr1.hasNext())
			{
				Condition condition1 = (Condition)itr1.next();
				Logger.out.debug("Column Name: "+condition1.getDataElement().getField());
			}
			traverseTree(child);
		}
	}
}
