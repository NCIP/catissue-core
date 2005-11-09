/*
 * Created on Sep 1, 2005
 * This class is used to redirect the user to the Home / SignIn Page after session is timedOut.
 */

package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.query.AdvancedConditionsNode;
import edu.wustl.catissuecore.query.Condition;
import edu.wustl.catissuecore.query.DataElement;
import edu.wustl.catissuecore.query.Operator;
import edu.wustl.catissuecore.query.TreeView;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 *
 * This class is used to redirect the user to the Home / SignIn Page after session is timedOut.
 */
public class AdvanceQueryView extends BaseAction
{
	//private Vector element = new Vector();
 	//private int temp =0;
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
       	DefaultMutableTreeNode root = (DefaultMutableTreeNode)session.getAttribute("root");
       	int childCount = root.getChildCount();
       	Logger.out.debug("child count in tree view action"+childCount);
		Vector tree=new Vector();
		//int nodeCount = 0;
		Map advancedConditionNodesMap = new HashMap();
		if(childCount==0)
		{
			tree.add(""); 
		}
		else
		{
			TreeView view = new TreeView();
			view.arrangeTree(root,0,tree,advancedConditionNodesMap);
			Logger.out.debug("advancedConditionNodesMap in AdvanceQueryViewAction"+advancedConditionNodesMap);
			Logger.out.debug("Size of advancedConditionNodesMap in AdvanceQueryViewAction"+advancedConditionNodesMap.size());
		}
		session.setAttribute("advancedConditionNodesMap",advancedConditionNodesMap);
		Logger.out.debug("Vector size of the tree"+tree);
		request.setAttribute("vector",tree);
		return (mapping.findForward(Constants.SUCCESS));
	}
	/*public void arrangeTree(DefaultMutableTreeNode node,int count,Vector tree,int temp,Map advancedConditionNodesMap,int nodeCount){
		
		temp++;
		Logger.out.debug("after incre--"+temp);
		
		
		for(int i = 0; i < node.getChildCount();i++){
			nodeCount++;
			DefaultMutableTreeNode n = (DefaultMutableTreeNode)node.getChildAt(i);
			AdvancedConditionsNode advConditionNode = (AdvancedConditionsNode)n.getUserObject();
			Logger.out.debug("nodeCount!!!!!!!!!!!"+nodeCount);
			advancedConditionNodesMap.put(new Integer(nodeCount),n);
			
			Vector v = advConditionNode.getObjectConditions();
			String tableName = advConditionNode.getObjectName();
			Logger.out.debug("object name for advance node"+tableName);
			Logger.out.debug("size-->"+v.size());
			String str = "";
			Condition con = null;
			DataElement data = null;
			Logger.out.debug("before str--"+temp);
			
			for(int k = 0; k < v.size(); k++){
				con = (Condition)v.get(k);
				data = con.getDataElement();
		        Operator op = con.getOperator();
		        if(k == 0)
		        	//str = temp + "|" + count + "|" +data.getTable()+": "+data.getField()+ " "+op.getOperator() + " "+con.getValue();
		        	str = temp + "|" + count + "|" +data.getField()+ " "+op.getOperator() + " '" + con.getValue()+ "'";
		        else
		        	//str = str +" "+"AND"+" "+data.getField()+" "+op.getOperator() + " "+con.getValue();
		        	str = str +" "+"<font color='red'>AND</font>"+" "+data.getField()+" "+op.getOperator() + " '"+con.getValue()+ "'";
		        // entered by Mandar for validation of single quotes around the values.
		        Logger.out.debug( "STR :---------- : "+ str);
		    }
			if(data != null)
				str = str +  "|" + tableName;
			
			if(con == null){
				str = temp + "|" + count + "|" + "ANY" + "|"+advConditionNode.getObjectName();
			}
				
			tree.add(str);
			Logger.out.debug("before arrangetree--"+temp);
			if(n.isLeaf())
			{
				temp++;
				
				Logger.out.debug("inside leaf loop"+temp);
			}
			else
				arrangeTree(n,temp,tree,temp,advancedConditionNodesMap,nodeCount);
			Logger.out.debug("temp after rec"+temp);
		}
	
	}*/

}


