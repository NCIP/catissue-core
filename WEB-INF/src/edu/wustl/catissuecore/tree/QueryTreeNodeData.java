/**
 * <p>Title: QueryTreeNodeData Class>
 * <p>Description: QueryTreeNodeData represents the node in the query result view tree.</p>
 * Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Poornima Govindrao
 * @version 1.00
 */

package edu.wustl.catissuecore.tree;

import java.io.Serializable;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.common.util.global.Constants;

/**
 * QueryTreeNodeData represents the node in the query result view tree.
 * @author poornima_govindrao
 */
public class QueryTreeNodeData implements QueryTreeNode, Serializable
{

	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = -5357987791504167664L;

	/**
	 * Specify identifier.
	 */
	private String identifier;

	/**
	 * Specify object Name.
	 */
	private String objectName;

	/**
	 * This will be Containe the actual name display label of the node.
	 */
	private String displayName;

	/**
	 * Specify parent Objec Identifier.
	 */
	private transient String parentObjectIdentifier;

	/**
	 * Specify parent Object Name.
	 */
	private String parentObjectName;

	/**
	 * Specify combined Parent Identifier.
	 */
	private String combinedParentIdentifier;

	/**
	 * Specify combined Parent Object Name.
	 */
	private String combinedParentObjectName;

	/**
	 * Specify tool Tip Text.
	 */
	private String toolTipText;

	/**
	 * Initializes an empty node.
	 */
	public QueryTreeNodeData()
	{

	}

	/**
	 * Sets the id of the data this node represents.
	 * @param identifier the id.
	 * @see #getId()
	 */
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * initialise Root.
	 * @param rootName root Name.
	 */
	public void initialiseRoot(String rootName)
	{
		initialiseRoot();
	}

	/**
	 * Returns the id of the data this node represents.
	 * @return the id of the data this node represents.
	 * @see #setId(long)
	 */
	public Object getIdentifier()
	{
		return identifier;
	}

	/**
	 * @return Returns the objectName.
	 */
	public String getObjectName()
	{
		return objectName;
	}

	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	/**
	 * @param objectName The objectName to set.
	 */
	public void setObjectName(String objectName)
	{
		this.objectName = objectName;
	}

	/**
	 * @return Returns the parentObjectName.
	 */
	public String getParentObjectName()
	{
		return parentObjectName;
	}

	/**
	 * @param parentObjectName The parentObjectName to set.
	 */
	public void setParentObjectName(String parentObjectName)
	{
		this.parentObjectName = parentObjectName;
	}

	/**
	 * @param parentIdentifier The parentIdentifier to set.
	 */
	public void setParentIdentifier(String parentIdentifier)
	{
		this.parentObjectIdentifier = parentIdentifier;
	}

	/**
	 * initialise Root.
	 */
	public void initialiseRoot()
	{
		this.setObjectName(Constants.ROOT);

	}

	/**
	 * gets Parent Tree Node.
	 * @return QueryTreeNode.
	 */
	public QueryTreeNode getParentTreeNode()
	{
		QueryTreeNodeData node = new QueryTreeNodeData();
		node.setIdentifier(this.combinedParentIdentifier);
		node.setObjectName(this.getCombinedParentObjectName());

		return node;
	}

	/**
	 * This method checks is child of.
	 * @param treeNode tree Node.
	 * @return is child of.
	 */
	public boolean isChildOf(QueryTreeNode treeNode)
	{
		QueryTreeNodeData node = (QueryTreeNodeData) treeNode;
		boolean isChild = this.parentObjectIdentifier.equals(node.getIdentifier())
				&& this.parentObjectName.equals(node.getObjectName());

		if ((this.getCombinedParentIdentifier() != null) && (this.getParentIdentifier() != null))
		{
			isChild = (this.parentObjectIdentifier.equals(node.getIdentifier())
					&& this.parentObjectName
					.equals(node.getObjectName()))
					&& (this.combinedParentIdentifier.equals(node.
							getCombinedParentIdentifier())
							&& this.combinedParentObjectName
							.equals(node.getCombinedParentObjectName()));
		}

		return isChild;
	}

	/**
	 * This method checks for Equal Parents.
	 * @param treeNode tree Node.
	 * @return true if Equal Parents else false.
	 */
	public boolean hasEqualParents(QueryTreeNode treeNode)
	{
		QueryTreeNodeData node = (QueryTreeNodeData) treeNode;
		return this.getIdentifier().equals(node.getCombinedParentIdentifier());
	}

	/**
	 * gets Parent Identifier.
	 * @return parent Object Identifier.
	 */
	public Object getParentIdentifier()
	{
		return this.parentObjectIdentifier;
	}

	/**
	 * @return Returns the combinedParentIdentifier.
	 */
	public String getCombinedParentIdentifier()
	{
		return combinedParentIdentifier;
	}

	/**
	 * @param combinedParentIdentifier The combinedParentIdentifier to set.
	 */
	public void setCombinedParentIdentifier(String combinedParentIdentifier)
	{
		this.combinedParentIdentifier = combinedParentIdentifier;
	}

	/**
	 * @return Returns the combinedParentObjectName.
	 */
	public String getCombinedParentObjectName()
	{
		return combinedParentObjectName;
	}

	/**
	 * @param combinedParentObjectName The combinedParentObjectName to set.
	 */
	public void setCombinedParentObjectName(String combinedParentObjectName)
	{
		this.combinedParentObjectName = combinedParentObjectName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.vo.QueryTreeNode#isPresentIn(javax.swing.tree.DefaultMutableTreeNode)
	 */
	/**
	 * This method checks the node present or not.
	 * @param parentNode parent Node.
	 * @return true if present else false.
	 */
	public boolean isPresentIn(DefaultMutableTreeNode parentNode)
	{
		boolean isPresent = false;
		for (int i = 0; i < parentNode.getChildCount(); i++)
		{
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
			QueryTreeNodeData node = (QueryTreeNodeData) childNode.getUserObject();

			if (this.identifier.equals(node.getIdentifier()))
			{
				isPresent = true;
				break;
			}
		}

		return isPresent;
	}

	/**
	 * overrides java.lang.Object.toString.
	 * @return node Name.
	 */
	public String toString()
	{
		StringBuffer nodeName = new StringBuffer(this.objectName);
		if (this.identifier != null)
		{
			nodeName.append(':').append(this.identifier);
		}

		return nodeName.toString();
	}

	/**
	 * gets Tool Tip Text.
	 * @return the tool Tip Text.
	 */
	public String getToolTipText()
	{
		return toolTipText;
	}

	/**
	 * set Tool Tip Text.
	 * @param toolTipText the tool Tip Text.
	 */
	public void setToolTipText(String toolTipText)
	{
		this.toolTipText = toolTipText;
	}
}
