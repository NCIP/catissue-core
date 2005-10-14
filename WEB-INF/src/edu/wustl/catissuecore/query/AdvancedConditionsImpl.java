
package edu.wustl.catissuecore.query;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;




/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class AdvancedConditionsImpl extends ConditionsImpl {
    
    /**
     * Root node for the tree formed from advanced query interface
     */
	private DefaultMutableTreeNode whereCondition = new DefaultMutableTreeNode();
	
	public AdvancedConditionsImpl()
	{

	}

	/**
	 * Adds condition to root node
     * @param condition
     * @return
     */
    public boolean addCondition(AdvancedConditionsNode condition)
    {
        whereCondition.add(new DefaultMutableTreeNode(condition));
        return false;
    }
    
    /**
     * Adds child node to parent node
     * @param parent - parent node
     * @param child - child node
     * @return
     */
	public boolean addCondition(AdvancedConditionsNode parent , AdvancedConditionsNode child)
	{
	    int index=-1;
		if ((index=  whereCondition.getIndex(new DefaultMutableTreeNode(parent))) != -1)
		{
		    ((DefaultMutableTreeNode) whereCondition.getChildAt(index)).add(new DefaultMutableTreeNode(child));
		    return true;
		}
		else
		{
		    return false;
		}
	}
	
	
//	public AdvancedConditionsNode getCondition(AdvancedConditionsNode condition)
//	{
//	    int index=-1;
//		if ((index=  whereCondition.getIndex(new DefaultMutableTreeNode(condition))) != -1)
//		{
//		    return (AdvancedConditionsNode)((DefaultMutableTreeNode) whereCondition.getChildAt(index)).getUserObject();
//		}
//		else
//		{
//		    return null;
//		}
//	}

	public boolean editCondition(){
		return false;
	}

    /* (non-Javadoc)
     * @see edu.wustl.caTISSUECore.query.ConditionsImpl#getString()
     */
    public String getString(int tableSufix)
    {
        StringBuffer whereConditionsString = new StringBuffer();
       
            int childCount =whereCondition.getChildCount();
            
            whereConditionsString.append(" \n(");
            Object obj =whereCondition.getUserObject();
            
            AdvancedConditionsNode currentNodeData = null;
            if (obj != null)
            {
                currentNodeData = (AdvancedConditionsNode)(whereCondition).getUserObject();
	            whereConditionsString.append(currentNodeData.toSQLString(tableSufix));
	            if(currentNodeData.getObjectConditions().size()>0 && childCount>0)
	                whereConditionsString.append(" "+Operator.AND+" ");
            }
	        if(childCount > 0)
	        {
	            /**
	             * In case operation with children is EXIST a subquery is formed
	             */
	            if(currentNodeData != null && currentNodeData.getOperationWithChildCondition().getOperator().equals(Operator.EXIST) )
	            {
	                DefaultMutableTreeNode child;
	                
	                for(int i = 0; i< childCount;i++)
		            {
	                    child = (DefaultMutableTreeNode)whereCondition.getChildAt(i);
	                    Query query = QueryFactory.getInstance().newQuery(Query.ADVANCED_QUERY,((AdvancedConditionsNode)child.getUserObject()).getObjectName());
		                query.setTableSufix(tableSufix+1);
		                query.setParentOfQueryStartObject(currentNodeData.getObjectName());
	                    whereConditionsString.append(" \nEXISTS \n(");//start of one child subquery
	                    ((AdvancedConditionsImpl)((AdvancedQuery)query).whereConditions).setWhereCondition(child);
	                    whereConditionsString.append(query.getString());
	                    whereConditionsString.append(" \n)"); //End of one child subquery
	                    whereConditionsString.append("\n"+Operator.AND+"\n");
		            }
	            }
	            
	            /**
	             * start of children
	             */
	            whereConditionsString.append("(");
	            for(int i = 0; i< childCount;i++)
	            {
	                AdvancedConditionsImpl advancedConditionsImpl = new AdvancedConditionsImpl();
	                advancedConditionsImpl.setWhereCondition((DefaultMutableTreeNode)whereCondition.getChildAt(i));
	                whereConditionsString.append("\n "+advancedConditionsImpl.getString(tableSufix)+" ");
	                if(i != childCount -1 )
	                    whereConditionsString.append(Operator.OR);
	            }
	            whereConditionsString.append(")");
	        }
	        whereConditionsString.append(")");
       
        return whereConditionsString.toString();
    }

    public DefaultMutableTreeNode getWhereCondition()
    {
        return whereCondition;
    }
    public void setWhereCondition(DefaultMutableTreeNode whereCondition)
    {
        this.whereCondition = whereCondition;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.query.ConditionsImpl#getQueryObjects(java.lang.String)
     */
    public HashSet getQueryObjects(String queryStartObject)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.query.ConditionsImpl#getQueryObjects()
     */
    public HashSet getQueryObjects()
    {
    	HashSet set = new HashSet();
        
        Enumeration enum = whereCondition.depthFirstEnumeration();
        while(enum.hasMoreElements())
        {
            AdvancedConditionsNode advancedConditionsNode=(AdvancedConditionsNode) ((DefaultMutableTreeNode) enum.nextElement()).getUserObject();
            
            if(advancedConditionsNode != null)
            {
                Vector conditions = advancedConditionsNode.getObjectConditions();
	            for(int i=0;i < conditions.size() ; i++)
	            {
	                set.add(((Condition)conditions.get(i)).getDataElement().getTable());
	            }
            }
        }
        return set;
    }

    /* (non-Javadoc)
     * @see edu.wustl.caTISSUECore.query.ConditionsImpl#getConditionObjects()
     */
//    public HashSet getConditionObjects()
//    {
//        HashSet set = new HashSet();
//        
//        Enumeration enum = whereCondition.depthFirstEnumeration();
//        while(enum.hasMoreElements())
//        {
//            AdvancedConditionsNode advancedConditionsNode=(AdvancedConditionsNode) ((DefaultMutableTreeNode) enum.nextElement()).getUserObject();
//            
//            if(advancedConditionsNode != null)
//            {
//                Vector conditions = advancedConditionsNode.getObjectConditions();
//	            for(int i=0;i < conditions.size() ; i++)
//	            {
//	                set.add(((Condition)conditions.get(i)).getDataElement().getTable());
//	            }
//            }
//        }
//        return set;
//    }

   
}