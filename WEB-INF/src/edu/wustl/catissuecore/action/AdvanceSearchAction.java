

package edu.wustl.catissuecore.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.ConditionMapParser;
import edu.wustl.common.util.logger.Logger;
/**
 * @author poornima_govindrao
 *
 *This class is for Advanced Search Action
 * 
 */

public class AdvanceSearchAction extends DispatchAction 
{
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        AdvanceSearchForm advanceSearchForm = (AdvanceSearchForm) form;
        
        //Get the aliasName.
        //String aliasName = (String)advanceSearchForm.getValue("SimpleConditionsNode:1_Condition_DataElement_table");
        String target = Constants.SUCCESS;
        
        //Get the Map values from UI
        Map map = advanceSearchForm.getValues();
        
        //Parse the conditions to for the advancedConditionNode
        ConditionMapParser parser = new ConditionMapParser();
        List conditionNodeCollection = parser.parseCondition(map);
        
        HttpSession session = request.getSession();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)session.getAttribute(Constants.ADVANCED_CONDITIONS_ROOT);
        String objectName = advanceSearchForm.getObjectName();
        String selectedNode = advanceSearchForm.getSelectedNode();
        Map advancedConditionNodesMap = (Map)session.getAttribute(Constants.ADVANCED_CONDITION_NODES_MAP);
      
        
        //ItemNode Id represents id of checked checbox used in Edit operation
        String str = advanceSearchForm.getItemNodeId();
        Integer nodeId = null;
        
        if(!str.equals(""))
        {
        	nodeId = Integer.decode(str);
        }
        
        /** Delete function**/
        //Represents whether delete is true or not
        String strDelete = request.getParameter("delete");
        
        //Represents node to be deleted
        String deleteNode = request.getParameter("itemId");
        
        if((strDelete != null && deleteNode != null))
        {
        	 nodeId = Integer.decode(deleteNode);
        	 //Delete function
        	 parser.deleteSelectedNode(nodeId,advancedConditionNodesMap);
        }
        else
        {
        	//Add or Edit function
        	root = parser.createAdvancedQueryObj(conditionNodeCollection,root,objectName,selectedNode,advancedConditionNodesMap,nodeId);
        }
        
        
        
        
       
       
        	
       //Query query = QueryFactory.getInstance().newQuery(Query.ADVANCED_QUERY, aliasName);
        session.setAttribute(Constants.ADVANCED_CONDITIONS_ROOT,root);
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
