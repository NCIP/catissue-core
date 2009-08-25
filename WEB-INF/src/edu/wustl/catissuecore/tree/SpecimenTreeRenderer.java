/*
 * Created on Nov 2, 2006
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

import edu.wustl.common.util.global.Constants;

/**
 * @author ramya_nagraj
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SpecimenTreeRenderer extends DefaultTreeCellRenderer
{

	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = 7215306683340446468L;

	/**
	 * String containing the type of specimen.
	 */
	private transient String specimenType;

	/**
	 * String containing the class of specimen.
	 */
	private transient String specimenClass;

	/**
	 * public no-args constructor.
	 */
	public SpecimenTreeRenderer()
	{
		super();
	}

	/**
	 * constructor.
	 * @param specimenType specimen Type
	 * @param specimenClass specimen Class.
	 */
	public SpecimenTreeRenderer(String specimenType, String specimenClass)
	{
		this();
		this.specimenType = specimenType;
		this.specimenClass = specimenClass;
	}

	/**
	* Configures the renderer based on the passed in components.
	* @see javax.swing.tree.TreeCellRenderer
	* #getTreeCellRendererComponent
	* (javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	* overrides javax.swing.tree.DefaultTreeCellRenderer.getTreeCellRendererComponent.
	* @param tree JTree
	* @param value value
	* @param sel sel.
	* @param expanded expanded boolean value.
	* @param leaf leaf boolean value.
	* @param row row.
	* @param hasFocus hasFocus.
	* @return Component.
	*/
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
			boolean expanded, boolean leaf, int row, boolean hasFocus)
	{

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		final SpecimenTreeNode treeNode = (SpecimenTreeNode) node.getUserObject();

		//If the clicked node is root node
		//(i.e, label for specimen tree),then dont display Specimen.gif icon.
		Icon icon = this.createImageIcon("Specimen.gif");

		if (!treeNode.toString().equals(Constants.SPECIMEN_TREE_ROOT_NAME))
		{
			/*The node is clickable when the specimen
			 * type and specimen class of the node is same as that selected by user.
			 * In that case,display enabled.gif image to indicate to the use that
			 * it is clickable.Otherwise,
			 * display disabled.gif.
			 */

			if (treeNode.getType().equalsIgnoreCase(this.specimenType)
					&& treeNode.getSpecimenClass().equalsIgnoreCase(this.specimenClass))
			{
				icon = this.createImageIcon("enabled.gif");
				this.setIcon(icon);
			}
			else
			{
				icon = this.createImageIcon("disabled.gif");
			}

		}
		this.setIcon(icon);
		return this;
	}

	/**
	 * Returns an Icon, or null if the path was invalid.
	 * @param name name.
	 * @return new Leaf Icon.
	 */
	protected Icon createImageIcon(String name)
	{
		final Icon newLefIcon = new ImageIcon(Thread.currentThread().getContextClassLoader()
				.getResource("images/" + name));

		return newLefIcon;
	}
}
