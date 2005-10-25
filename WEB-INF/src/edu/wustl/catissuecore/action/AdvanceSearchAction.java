

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.catissuecore.query.AdvancedConditionsImpl;
import edu.wustl.catissuecore.query.AdvancedConditionsNode;
import edu.wustl.catissuecore.query.AdvancedQuery;
import edu.wustl.catissuecore.query.Condition;
import edu.wustl.catissuecore.query.Query;
import edu.wustl.catissuecore.query.QueryFactory;
import edu.wustl.catissuecore.query.SimpleConditionsNode;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.ConditionMapParser;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


public class AdvanceSearchAction extends DispatchAction
{
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        AdvanceSearchForm advanceSearchForm = (AdvanceSearchForm) form;
        
        //Get the aliasName.
        //String aliasName = (String)advanceSearchForm.getValue("SimpleConditionsNode:1_Condition_DataElement_table");
        
        String target = Constants.SUCCESS;
        Map map = advanceSearchForm.getValues();
        ConditionMapParser parser = new ConditionMapParser();
        List conditionNodeCollection = parser.parseCondition(map);
        HttpSession session = request.getSession();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)session.getAttribute("root");
        root = parser.createAdvancedQueryObj(conditionNodeCollection,root);
        
        //Query query = QueryFactory.getInstance().newQuery(Query.ADVANCED_QUERY, aliasName);
        session.setAttribute("root",root);
        //Get the view columns.
        //String [] columnNames = query.setViewElements(aliasName);
        
        //((AdvancedConditionsImpl)((AdvancedQuery)query).getWhereConditions()).setWhereCondition(root);
        //          List list = query.execute();
        /*List list = new ArrayList();
         Logger.out.debug("List......................"+list+" List Size..................."+list.size());
         }*/

        return mapping.findForward(target);
    }
  
}
