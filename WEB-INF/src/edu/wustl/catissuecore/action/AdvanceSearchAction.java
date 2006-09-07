

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

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.util.ConditionMapParser;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author poornima_govindrao
 *
 *This class is Advanced Search Action which forms the root node to form the query. 
 * 
 */
public class AdvanceSearchAction extends BaseAction//DispatchAction 
{
    
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        AdvanceSearchForm advanceSearchForm = (AdvanceSearchForm) form;
        
        //Get the aliasName.
        //String aliasName = (String)advanceSearchForm.getValue("SimpleConditionsNode:1_Condition_DataElement_table");
        String target = Constants.SUCCESS;
        
        //Get the Map values from UI
        Map conditionsMap = advanceSearchForm.getValues();
        Map eventParametersConditionsMap = advanceSearchForm.getEventValues();
        Logger.out.debug("eventParametersConditionsMap "+eventParametersConditionsMap);
        
        //Parse the conditions to for the advancedConditionNode
        ConditionMapParser parser = new ConditionMapParser();
        //List conditionNodeCollectionForView = parser.parseConditionForQueryView(map);
        List conditionNodeCollectionForQuery = parser.parseConditionForQuery(conditionsMap);
        //Get the conditions list for event parameters if there are any conditions selected from the UI
        if(!eventParametersConditionsMap.isEmpty())
        {
        	String value = (String)eventParametersConditionsMap.get("EventName_1");
        	Logger.out.debug("value of first key:"+value);
        	if(!value.equals("-1"))
        	{
        		//Parse the eventParameters map suitable for parseConditionForQuery method.
        		Map eventMap = ConditionMapParser.parseEventParameterMap(eventParametersConditionsMap);
        		List eventConditions = parser.parseConditionForQuery(eventMap);
        		conditionNodeCollectionForQuery.addAll(eventConditions);
        	}
        }
        HttpSession session = request.getSession();
        //session object for query results
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)session.getAttribute(Constants.ADVANCED_CONDITIONS_ROOT);
        
        //session object for query view
        //DefaultMutableTreeNode queryViewRoot = (DefaultMutableTreeNode)session.getAttribute(Constants.ADVANCED_CONDITIONS_QUERY_VIEW);
        String objectName = advanceSearchForm.getObjectName();
        
        String selectedNode = advanceSearchForm.getSelectedNode();
        Logger.out.debug("selectedNode--"+selectedNode);
        Map advancedConditionNodesMap = (Map)session.getAttribute(Constants.ADVANCED_CONDITION_NODES_MAP);
        
        String temp = (String)session.getAttribute("lastNodeId");
        if(temp != null && selectedNode.equals(""))
        {
        	selectedNode = temp;
        }
        
        if (selectedNode.equals("-1"))
        {
        	selectedNode = "";
        }
        //ItemNode Id represents id of checked checbox used in Edit operation
        String editStr = advanceSearchForm.getItemNodeId();
        Integer nodeId = null;
        if(!editStr.equals(""))
        {
        	nodeId = Integer.decode(editStr);
        }
        
        /** Delete function**/
        //Represents whether delete is true or not
        String strDelete = request.getParameter("delete");
        
        //Represents node to be deleted
        String deleteNode = request.getParameter("itemId");
        
        if((strDelete != null && deleteNode != null))
        {
        	 //Delete function
        	 parser.deleteSelectedNode(deleteNode,advancedConditionNodesMap);
        }
        else
        {
        	//Add or Edit function
        	root = parser.createAdvancedQueryObj(conditionNodeCollectionForQuery,root,objectName,selectedNode,advancedConditionNodesMap,nodeId,session);
        }
        	
        session.setAttribute(Constants.ADVANCED_CONDITIONS_ROOT,root);
        //session.setAttribute(Constants.ADVANCED_CONDITIONS_QUERY_VIEW,root);

        return mapping.findForward(target);
    }
  
}
