/*
 * Created on Aug 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.tree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface QueryTreeNode
{

	/**
	 * initialise Root.
	 */
	void initialiseRoot();

	/**
	 * initialise Root.
	 * @param rootName root Name.
	 */
	void initialiseRoot(String rootName);

	/**
	 * gets Parent Tree Node.
	 * @return QueryTreeNode
	 */
	QueryTreeNode getParentTreeNode();

	/**
	 * Checks is Child Of.
	 * @param treeNode tree Node
	 * @return child of or not.
	 */
	boolean isChildOf(QueryTreeNode treeNode);

	/**
	 * Checks has Equal Parents.
	 * @param treeNode tree Node
	 * @return has Equal Parents
	 */
	boolean hasEqualParents(QueryTreeNode treeNode);

	/**
	 * gets Parent Identifier.
	 * @return Parent Identifier.
	 */
	Object getParentIdentifier();

	/**
	 * gets Identifier.
	 * @return Identifier
	 */
	Object getIdentifier();

	/**
	 * Checks is Present In.
	 * @param parentNode parent Node.
	 * @return is Present In.
	 */
	boolean isPresentIn(DefaultMutableTreeNode parentNode);

}
