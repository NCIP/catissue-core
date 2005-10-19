/*
 * Created on Sep 1, 2005
 * This class is used to redirect the user to the Home / SignIn Page after session is timedOut.
 */

package edu.wustl.catissuecore.action;

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
		//QueryTest test = new QueryTest();
		//Vector v = test.getTreeElement();
		HttpSession session = request.getSession();
       	DefaultMutableTreeNode root = (DefaultMutableTreeNode)session.getAttribute("root");
       	int childCount = root.getChildCount();
       	Logger.out.debug("child count in tree view action"+childCount);
		Vector tree=new Vector();
		
		if(childCount==0)
		{
			tree.add(""); 
		}
		else
			arrangeTree(root,0,tree,0);
		Logger.out.debug("Vector size of the tree"+tree);
		request.setAttribute("vector",tree);
		return (mapping.findForward(Constants.SUCCESS));
	}
	public void arrangeTree(DefaultMutableTreeNode node,int count,Vector tree,int temp){
		//int temp = count + 1;
		temp++;
		//System.out.println("temp-->"+temp);
		
		//if(!node.isLeaf()){
			for(int i = 0; i < node.getChildCount();i++){
				
				DefaultMutableTreeNode n = (DefaultMutableTreeNode)node.getChildAt(i);
				AdvancedConditionsNode advConditionNode = (AdvancedConditionsNode)n.getUserObject();
				Vector v = advConditionNode.getObjectConditions();
				Logger.out.debug("size-->"+v.size());
				String str = "";
				Condition con = null;
				for(int k = 0; k < v.size(); k++){
					con = (Condition)v.get(k);
					DataElement data = con.getDataElement();
		        	Operator op = con.getOperator();
		        	if(k == 0)
		        		str = temp + "|" + count + "|" +data.getTable()+": "+data.getField()+ " "+op.getOperator() + " "+con.getValue();
		        	else
		        		str = str +" "+"AND"+" "+data.getField()+" "+op.getOperator() + " "+con.getValue();
		        	
				}
				
			if(con == null){
				str = temp + "|" + count + "|"+advConditionNode.getObjectName()+": "+"ANY";
			}
			//System.out.println("str-->"+str);
			tree.add(str);
			
				arrangeTree(n,temp,tree,temp);
        }

		
		
	//}
			
		//return element;
}
// 	public Vector getTreeElement(){
// 		//System.out.println("element--"+element.size());
// 		return element;
// 	}
}


