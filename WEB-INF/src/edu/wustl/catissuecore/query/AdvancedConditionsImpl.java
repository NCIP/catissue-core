
package edu.wustl.catissuecore.query;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.common.util.logger.Logger;




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
	
	private int level = 1;
	
	private String parentObject = new String();
	
	public AdvancedConditionsImpl()
	{

	}
	
	public void formatTree()
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
            
            DefaultMutableTreeNode parent1 = (DefaultMutableTreeNode) whereCondition.getParent();
            AdvancedConditionsNode parentNode =null;
            if(parent1 != null )
            {
            	parentNode = (AdvancedConditionsNode) parent1.getUserObject();
            }
            
            
            
//            Object obj =whereCondition.getUserObject();
            
            AdvancedConditionsNode currentNodeData = null;
            currentNodeData = (AdvancedConditionsNode)(whereCondition).getUserObject();
            if (currentNodeData != null && currentNodeData.getObjectConditions().size()>0)
            {
            	whereConditionsString.append(" \n(");
                
	            whereConditionsString.append(currentNodeData.toSQLString(tableSufix));
	            if(currentNodeData.getObjectConditions().size()>0 && childCount>0 && childHasConditions())
	                whereConditionsString.append(" "+Operator.AND+" (");
            }
	        if(childCount > 0 && hasConditions())
	        {
//	        	whereConditionsString.append(" "+Operator.AND+" ");
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
		                query.setLevelOfParent(this.level);
		                
		                
		                //set isParentDerivedSpecimen attribute of child query to true 
		                //if current node is a derived specimen
		                if(currentNodeData.getObjectName().equals(Query.SPECIMEN) && parentObject.equals(Query.SPECIMEN) 
		                		&& parentNode != null)
		                {
		                	
		                		if(parentNode.getOperationWithChildCondition().getOperator().equals(Operator.EXIST))
		                		{
		                			query.setParentDerivedSpecimen(false);
		                		}
		                		else
		                		{
		                			query.setParentDerivedSpecimen(true);
		                		}
		                	
		                }
		                else
		                {
		                	query.setParentDerivedSpecimen(false);
		                }
		                if(currentNodeData != null)
		                {
		                	query.setParentOfQueryStartObject(currentNodeData.getObjectName());
		                }
	                    whereConditionsString.append(" \nEXISTS \n(");//start of one child subquery
	                    ((AdvancedConditionsImpl)((AdvancedQuery)query).whereConditions).setWhereCondition(child);
	                    whereConditionsString.append(query.getString());
	                    whereConditionsString.append(" \n)"); //End of one child subquery
	                    if(i != childCount-1)
	                    {
	                    	whereConditionsString.append("\n"+currentNodeData.getOperationWithChildCondition().getOperatorParams()[0]+"\n");
	                    }
		            }
	            }
	            else
	            {
	            
		            /**
		             * start of children
		             */
		            
		            DefaultMutableTreeNode child;
		            AdvancedConditionsImpl advancedConditionsImpl;
		            boolean prevChildHasCondition;
		            for(int i = 0; i< childCount;i++)
		            {
		            	child=(DefaultMutableTreeNode)whereCondition.getChildAt(i);
		            	if(child != null && hasConditions(child))
		            	{
		            		whereConditionsString.append("(");
		            		advancedConditionsImpl = new AdvancedConditionsImpl();
		            		advancedConditionsImpl.setWhereCondition(child);
		            		advancedConditionsImpl.setLevel(child.getLevel());
		            		if(currentNodeData != null)
			                {
		            			advancedConditionsImpl.setParentObject(currentNodeData.getObjectName());
			                }
		            		whereConditionsString.append("\n "+advancedConditionsImpl.getString(tableSufix)+" ");
		            		whereConditionsString.append(")");
		            		prevChildHasCondition = true;
		            	}
		            	else
		            	{
		            		prevChildHasCondition = false;
		            	}
		                if(i != childCount -1 && prevChildHasCondition)
		                {
		                    whereConditionsString.append(Operator.OR);
		                }
		            }
		            
	            }
	        }
	        if(currentNodeData!=null && currentNodeData.getObjectConditions().size()>0)
	        {
	        	if(currentNodeData.getObjectConditions().size()>0 && childCount>0 && childHasConditions())
	                whereConditionsString.append(" )");
	        	whereConditionsString.append(")");
	        }
       
        return whereConditionsString.toString();
    }
    
    public boolean hasConditions()
    {
//    	boolean hasCondition = false;
//    	Enumeration children = whereCondition.children();
//    	DefaultMutableTreeNode child;
//    	Vector conditions;
//    	AdvancedConditionsNode advancedConditionsNode;
//    	while(children.hasMoreElements())
//    	{
//    		child = (DefaultMutableTreeNode) children.nextElement();
//    		
//    		advancedConditionsNode = (AdvancedConditionsNode) child.getUserObject();
//    		if(advancedConditionsNode!=null )
//    		{
//    			conditions = advancedConditionsNode.getObjectConditions();
//    			if(conditions != null && conditions.size()>0)
//    			{
//    				hasCondition = true;
//    			}
//    			break;
//    		}
//    	}
    	return hasConditions(whereCondition);
    }
    
    public boolean hasConditions(DefaultMutableTreeNode treeNode)
    {
    	boolean hasCondition = false;
    	
    	Vector conditions;
    	AdvancedConditionsNode advancedConditionsNode;
    	
    	//Check whether treenode itself has conditions or not
    	advancedConditionsNode = (AdvancedConditionsNode) treeNode.getUserObject();
    	if(advancedConditionsNode!=null )
		{
			conditions = advancedConditionsNode.getObjectConditions();
			if(conditions != null && conditions.size()>0)
			{
				hasCondition = true;
				return hasCondition;
			}
			
		}
    	
    	//check if children have conditions
    	Enumeration children = treeNode.children();
    	DefaultMutableTreeNode child;
    	while(children.hasMoreElements())
    	{
    		child = (DefaultMutableTreeNode) children.nextElement();
    		
    		advancedConditionsNode = (AdvancedConditionsNode) child.getUserObject();
    		if(advancedConditionsNode!=null )
    		{
    			conditions = advancedConditionsNode.getObjectConditions();
    			if(conditions != null && conditions.size()>0)
    			{
    				hasCondition = true;
    				break;
    			}
    			
    		}
    		hasCondition = hasConditions(child);
    		if(hasCondition)
    		{
    			break;
    		}
    	}
    	return hasCondition;
    }
    
    public boolean childHasConditions()
    {
    	return childHasConditions(whereCondition);
    }
    
    public boolean childHasConditions(DefaultMutableTreeNode treeNode)
    {
    	boolean hasCondition = false;
    	
    	Vector conditions;
    	AdvancedConditionsNode advancedConditionsNode;
    	
    	if(treeNode == null)
    	{
    		hasCondition = false;
    		return hasCondition;
    	}
    	
    	//check if children have conditions
    	Enumeration children = treeNode.children();
    	DefaultMutableTreeNode child;
    	while(children.hasMoreElements())
    	{
    		child = (DefaultMutableTreeNode) children.nextElement();
    		
    		advancedConditionsNode = (AdvancedConditionsNode) child.getUserObject();
    		if(advancedConditionsNode!=null )
    		{
    			conditions = advancedConditionsNode.getObjectConditions();
    			if(conditions != null && conditions.size()>0)
    			{
    				hasCondition = true;
    				break;
    			}
    			
    		}
    		hasCondition = hasConditions(child);
    		if(hasCondition)
    		{
    			break;
    		}
    	}
    	return hasCondition;
    }
    

    public DefaultMutableTreeNode getWhereCondition()
    {
        return whereCondition;
    }
    public void setWhereCondition(DefaultMutableTreeNode whereCondition)
    {
        this.whereCondition = whereCondition;
    }

//    /* (non-Javadoc)
//     * @see edu.wustl.catissuecore.query.ConditionsImpl#getQueryObjects(java.lang.String)
//     */
//    public HashSet getQueryObjects(String queryStartObject)
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.query.ConditionsImpl#getQueryObjects()
     */
    public HashSet getQueryObjects()
    {
    	HashSet set = new HashSet();
        
        Enumeration enum = whereCondition.depthFirstEnumeration();
        DefaultMutableTreeNode treeNode;
        DefaultMutableTreeNode parentNode;
        AdvancedConditionsNode advancedConditionsNode;
        AdvancedConditionsNode parentConditionsNode;
        Table table;
        while(enum.hasMoreElements())
        {
        	treeNode = (DefaultMutableTreeNode) enum.nextElement();
            advancedConditionsNode=(AdvancedConditionsNode) treeNode.getUserObject();
            parentNode = (DefaultMutableTreeNode) treeNode.getParent();
            
            if(advancedConditionsNode != null)
            {
            	if(parentNode != null)
                {
	                parentConditionsNode = (AdvancedConditionsNode) (parentNode).getUserObject();
	                if(parentConditionsNode != null)
	                {
		            	if(isPartOfSubquery(treeNode))
		            	{
		            		Logger.out.debug("Parent has EXIST operation with children so not adding tables of the node in set");
		            		continue;
		            	}
	                }
                }
                Vector conditions = advancedConditionsNode.getObjectConditions();
	            for(int i=0;i < conditions.size() ; i++)
	            {
	            	Logger.out.debug(conditions.get(i));
	            	table = ((Condition)conditions.get(i)).getDataElement().getTable();
	            	Logger.out.debug(table);
	                set.add(table);
	                if(table.getLinkingTable() != null)
	                	set.add(table.getLinkingTable());
	            }
            }
        }
        Logger.out.debug("set of tables in conditions:"+set);
        return set;
    }
    
    
   

    /**
 * @param treeNode
 * @return
 */
private boolean isPartOfSubquery(DefaultMutableTreeNode treeNode) {
	boolean isPartOfSubquery = false;
//	DefaultMutableTreeNode parent = (DefaultMutableTreeNode)treeNode.getParent();
	DefaultMutableTreeNode parent = (DefaultMutableTreeNode)treeNode.getParent();
	AdvancedConditionsNode parentNode;
	while(parent != null && !parent.equals(whereCondition.getParent()) )
	{
		
		parentNode = (AdvancedConditionsNode) parent.getUserObject();
		if(parentNode != null && parentNode.getOperationWithChildCondition().getOperator().equals(Operator.EXIST))
		{
			
			isPartOfSubquery = true;
			return isPartOfSubquery;
		}
		parent = (DefaultMutableTreeNode) parent.getParent();
		
	}
	return isPartOfSubquery;
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

   
	/**
	 * @return Returns the level.
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @param level The level to set.
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	/**
	 * @return Returns the parentObject.
	 */
	public String getParentObject() {
		return parentObject;
	}
	/**
	 * @param parentObject The parentObject to set.
	 */
	public void setParentObject(String parentObject) {
		this.parentObject = parentObject;
	}
}