
package edu.wustl.catissuecore.tree;

import java.io.Serializable;

import edu.wustl.common.tree.TreeNodeImpl;

/**
 * @author ramya_nagraj
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class SpecimenTreeNode extends TreeNodeImpl implements Serializable
{

	/**
	 * serial Version Unique ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * No-Args Constructor.
	 */
	public SpecimenTreeNode()
	{
		super();
	}

	/**
	 * Parametrized Construstor.
	 * @param identifier identifier
	 * @param value value.
	 */
	public SpecimenTreeNode(Long identifier, String value)
	{
		super(identifier, value);
	}

	/**
	 * String containing the type of the specimen node.
	 */
	private String type;

	/**
	 * String containing the class of the specimen node.
	 */
	private String specimenClass;

	/**
	 * String containing Id of the parent node.
	 */
	private String parentIdentifier;

	/**
	 * String containing value of the parent node.
	 */
	private String parentValue;

	/**
	 * @return String containing the type of specimen node.
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * @param type the type of the specimen node.
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * Returns String containing the class of specimen.
	 * @return specimenClass
	 */
	public String getSpecimenClass()
	{
		return this.specimenClass;
	}

	/**
	 * Sets the class of specimen.
	 * @param specimenClass the class of the specimen node
	 */
	public void setSpecimenClass(String specimenClass)
	{
		this.specimenClass = specimenClass;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.tree.TreeNodeImpl#toString()
	 */
	/**
	 * overrides edu.wustl.common.tree.TreeNodeImpl.toString.
	 * @return value.
	 */
	@Override
	public String toString()
	{
		return this.getValue();
	}

	/**
	 * Returns the parentIdentifier of the tree node.
	 * @return parentIdentifier
	 */
	public String getParentIdentifier()
	{
		return this.parentIdentifier;
	}

	/**
	 * Sets the parentIdentifier of the tree node.
	 * @param parentIdentifier parent Identifier.
	 */
	public void setParentIdentifier(String parentIdentifier)
	{
		this.parentIdentifier = parentIdentifier;
	}

	/**
	 * Returns the parentValue of the tree node.
	 * @return parentValue
	 */
	public String getParentValue()
	{
		return this.parentValue;
	}

	/**
	 *Sets the parentValue of the tree node.
	 * @param parentValue parent Value.
	 */
	public void setParentValue(String parentValue)
	{
		this.parentValue = parentValue;
	}

	/**
	 * overrides TreeNodeImpl.equals method .
	 * @param obj Object.
	 * @return true if equal else false.
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
