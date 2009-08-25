
package edu.wustl.catissuecore.applet.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.io.Serializable;

/**
 * <p>This class initializes the fields of VerticalLayout.java</p>.
 * @author Ashwin Gupta
 * @version 1.1
 */
public class VerticalLayout implements LayoutManager2, Serializable
{

	/**
	 * Specify the serialVersionUID field.
	 */
	private static final long serialVersionUID = 1L;

	/** Minimum width. */
	private final int min = 0;

	/** Horizontal Gap. */
	int hgap;

	/** Vertical Gap. */
	int vgap;

	/**
	 * Constructs a new vertical layout with no gaps between components.
	 */
	public VerticalLayout()
	{
		this(0, 0);
	}

	/**
	 * Constructs a vertical layout with the specified gaps between components.
	 * The horizontal gap is specified by <code>hgap</code> and the vertical
	 * gap is specified by <code>vgap</code>.
	 * @param hgap
	 *            the horizontal gap.
	 * @param vgap
	 *            the vertical gap.
	 */
	public VerticalLayout(int hgap, int vgap)
	{
		this.hgap = hgap;
		this.vgap = vgap;
	}

	/**
	 * Gets the vertical Layout alignment.
	 * @param target
	 *            The container object
	 * @return returns the float value of the alignment
	 * @see VerticalLayout#getLayoutAlignmentY(Container)
	 * @since TODO
	 */
	public float getLayoutAlignmentY(Container target)
	{
		return 0.0f;
	}

	/**
	 * Gets the horizontal Layout alignment.
	 * @param target
	 *            the container object
	 * @return returns the float value of the alignment
	 * @see VerticalLayout#getLayoutAlignmentX(Container)
	 * @since TODO
	 */
	public float getLayoutAlignmentX(Container target)
	{
		return 0.0f;
	}

	/**
	 * This function adds the Layout component.
	 * @param comp
	 *            the Component object
	 * @param constraints
	 *            the constraints defined for above component
	 * @see VerticalLayout#addLayoutComponent(Component, Object)
	 * @since TODO
	 */
	public void addLayoutComponent(Component comp, Object constraints)
	{
	}

	/**
	 * This function adds the Layout component.
	 * @param comp
	 *            the Component object
	 * @param name
	 *            the String object referring the name of the component
	 * @see VerticalLayout#addLayoutComponent(String, Component)
	 * @since TODO
	 */
	public void addLayoutComponent(String name, Component comp)
	{
	}

	/**
	 * This function removes the Layout component.
	 * @param comp
	 *            the Component object
	 * @see VerticalLayout#removeLayoutComponent(Component)
	 * @since TODO
	 */
	public void removeLayoutComponent(Component comp)
	{
	}

	/**
	 * Returns the maximum size of this component.
	 * @param parent
	 *            the Component object
	 * @see java.awt.Component#getMinimumSize()
	 * @see java.awt.Component#getPreferredSize()
	 * @see LayoutManager
	 * @return returns the maximum size of the component
	 * @since TODO
	 */
	public Dimension maximumLayoutSize(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			final Insets insets = parent.getInsets();
			int width = 0;
			int height = this.vgap;
			for (int i = 0; i < parent.getComponentCount(); i++)
			{
				final Component child = parent.getComponent(i);
				width = Math.max(child.getMaximumSize().width, width);
				height += child.getMaximumSize().height + this.vgap;
			}
			return new Dimension(insets.left + width + insets.right + this.hgap * 2, insets.top
					+ height + insets.bottom);
		}
	}

	/**
	 * Invalidates the layout, indicating that if the layout manager has cached
	 * information it should be discarded.
	 * @param target
	 *            the container object
	 * @see VerticalLayout#invalidateLayout(Container)
	 * @since TODO
	 */
	public void invalidateLayout(Container target)
	{
	}

	/**
	 * Calculates the minimum size dimensions for the specified panel given the
	 * components in the specified parent container.
	 * @see #preferredLayoutSize
	 * @param parent
	 *            Container
	 * @return the minimum layout size
	 * @since TODO
	 */
	public Dimension minimumLayoutSize(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			final Insets insets = parent.getInsets();
			int width = 0;
			int height = this.vgap;
			for (int i = 0; i < parent.getComponentCount(); i++)
			{
				final Component child = parent.getComponent(i);
				width = Math.max(child.getMinimumSize().width, width);
				height += child.getMinimumSize().height + this.vgap;
			}
			return new Dimension(insets.left + width + insets.right + this.hgap * 2, insets.top
					+ height + insets.bottom);
		}
	}

	/**
	 * Calculates the preferred size dimensions for the specified panel given
	 * the components in the specified parent container.
	 * @param parent
	 *            the component to be laid out
	 * @see #minimumLayoutSize
	 * @return returns the preferred Layout size
	 * @since TODO
	 */
	public Dimension preferredLayoutSize(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			final Insets insets = parent.getInsets();
			int height = this.vgap;
			int width = this.min;
			for (int i = 0; i < parent.getComponentCount(); i++)
			{
				final Component child = parent.getComponent(i);
				final Dimension d = child.getPreferredSize();
				width = Math.max(width, d.width);
				height += d.height + this.vgap;
			}
			return new Dimension(insets.left + width + insets.right + this.hgap * 2, insets.top
					+ height + insets.bottom);
		}
	}

	/**
	 * Lays out the container in the specified panel.
	 * @param parent
	 *            the component which needs to be laid out
	 * @since TODO
	 */
	public void layoutContainer(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			final Insets insets = parent.getInsets();
			final Rectangle bounds = parent.getBounds();

			final int xPos = insets.left + this.hgap;
			int yPos = insets.top + this.vgap;
			final int width = bounds.width - (insets.left + insets.right + this.hgap * 2);

			for (int i = 0; i < parent.getComponentCount(); i++)
			{
				final Component child = parent.getComponent(i);
				child.setSize(width, child.getHeight());
				final Dimension prefSize = child.getPreferredSize();
				child.setBounds(xPos, yPos, width, prefSize.height);
				yPos += prefSize.height + this.vgap;
			}
		}
	}

	/**
	 * Returns the horizontal gap between components.
	 * @return int hgap
	 * @since TODO
	 */
	public int getHgap()
	{
		return this.hgap;
	}

	/**
	 * Sets the horizontal gap for the components.
	 * @param hgap
	 *            integer value specifying the horizontal gap
	 * @since TODO
	 */
	public void setHgap(int hgap)
	{
		this.hgap = hgap;
	}

	/**
	 * Returns the vertical gap between components.
	 * @return int vgap
	 * @since TODO
	 */
	public int getVgap()
	{
		return this.vgap;
	}

	/**
	 * Sets the vertical gap between the components.
	 * @param vgap
	 *            integer value specifying the vertical gap
	 * @since TODO
	 */
	public void setVgap(int vgap)
	{
		this.vgap = vgap;
	}
}
