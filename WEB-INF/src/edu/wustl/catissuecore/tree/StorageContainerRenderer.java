/*
 * Created on Oct 4, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.tree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import edu.wustl.common.util.global.Status;

/**
 * @author prafull_kadam
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StorageContainerRenderer extends DefaultTreeCellRenderer
{

	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = -8833844135285642393L;

	/**
	 * Configures the renderer based on the passed in components.
	 * @see javax.swing.tree.TreeCellRenderer
	 * #getTreeCellRendererComponent
	 * (javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 * overrides javax.swing.tree.DefaultTreeCellRenderer.getTreeCellRendererComponent.
	 * @param tree JTree
	 * @param value value
	 * @param sel sel.
	 * @param expanded tree expanded or not.
	 * @param leaf is it leaf or not.
	 * @param row row.
	 * @param hasFocus hasFocus.
	 * @return Component.
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
			boolean expanded, boolean leaf, int row, boolean hasFocus)
	{

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		StorageContainerTreeNode treeNode = (StorageContainerTreeNode) node.getUserObject();
		Icon icon = createImageIcon("disabled.gif");
		if (treeNode.getActivityStatus().equals(Status.ACTIVITY_STATUS_ACTIVE.toString()))
		{
			icon = createImageIcon("enabled.gif");
			setIcon(icon);
		}
		setIcon(icon);
		return this;
	}

	/**
	 * Returns an Icon, or null if the path was invalid.
	 * @param name name.
	 * @return new Leaf Icon.
	 */
	protected Icon createImageIcon(String name)
	{
		Icon newLeafIcon = new ImageIcon(Thread.currentThread().getContextClassLoader()
				.getResource("images/" + name));
		return newLeafIcon;
	}
}
