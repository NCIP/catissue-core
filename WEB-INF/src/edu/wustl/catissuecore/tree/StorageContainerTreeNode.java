/*
 * Created on Jul 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.tree;

import java.io.Serializable;

import edu.wustl.common.tree.TreeNodeImpl;
import edu.wustl.common.util.global.Status;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated storageContainerType comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StorageContainerTreeNode extends TreeNodeImpl implements Serializable, Comparable
{

	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Type of storage container.
	 */
	private String type;

	/**
	 * Specify tool Tip.
	 */
	private String toolTip = "";

	/**
	 * Specify activity Status.
	 */
	private String activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();

	/**
	 * Default constructor.
	 */
	public StorageContainerTreeNode()
	{
		super();
	}

	/**
	 * Parameterized constructor.
	 * @param identifier identifier to set.
	 * @param value value to set.
	 * @param type type to set.
	 */
	public StorageContainerTreeNode(Long identifier, String value, String type)
	{
		super(identifier, value);
		this.type = type;
	}

	/**
	 * constructor with tooltip.
	 * @param identifier identifier to set.
	 * @param value value to set.
	 * @param type type to set.
	 * @param toolTip tool Tip to set.
	 * @param activityStatus activity Status to set.
	 */
	public StorageContainerTreeNode(Long identifier, String value, String type, String toolTip,
			String activityStatus)
	{
		super(identifier, value);
		this.type = type;
		this.toolTip = toolTip;
		this.activityStatus = activityStatus;
	}

	/**
	 * Parameterized constructor.
	 * @param identifier identifier to set.
	 * @param value value to set.
	 * @param type type to set.
	 * @param activityStatus activity Status to set. 
	 */
	public StorageContainerTreeNode(Long identifier, String value, String type,
			String activityStatus)
	{
		super(identifier, value);
		this.type = type;
		this.activityStatus = activityStatus;
	}

	/**
	 * @return Returns the toolTip.
	 */
	public String getToolTip()
	{
		return this.toolTip;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return Returns the activityStatus.
	 */
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * @param activityStatus The activityStatus to set.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/**
	 * overrides edu.wustl.common.tree.TreeNodeImpl.toString.
	 * @return node Name.
	 */
	@Override
	public String toString()
	{
		return this.getValue();
	}

	/**
	 *  implements java.lang.Comparable.compareTo.
	 *  @param tmpobj tmp object.
	 *  @return int.
	 */
	public int compareTo(Object tmpobj)
	{
		final StorageContainerTreeNode treeNode = (StorageContainerTreeNode) tmpobj;
		return this.getIdentifier().compareTo(treeNode.getIdentifier());
	}

	/**
	 * overrides TreeNodeImpl.equals method.
	 * @param obj Object.
	 * @return true if equal.
	 */
	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}

	/**
	 * overrides TreeNodeImpl.hashCode method.
	 * @return hashCode.
	 */
	@Override
	public int hashCode()
	{
		return super.hashCode();
	}
}