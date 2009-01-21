
package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author gautam_shetty
 * @author Ashwin Gupta
 * @hibernate.class table="CATISSUE_CONTAINER_TYPE"
 */
public class ContainerType extends AbstractDomainObject
{
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 2996492707028291754L;

	/**
	 * id.
	 */
	protected Long id;

	/**
	 * name.
	 */
	protected String name;

	/**
	 * oneDimensionLabel.
	 */
	protected String oneDimensionLabel;

	/**
	 * twoDimensionLabel.
	 */
	protected String twoDimensionLabel;

	/**
	 * capacity.
	 */
	protected Capacity capacity;

	/**
	 * comment.
	 */
	protected String comment;

	/**
	 * activityStatus.
	 */
	protected String activityStatus;

	/**
	 * Default Constructor.
	 */
	public ContainerType()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param abstractActionForm AbstractActionForm.
	 */
	public ContainerType(AbstractActionForm abstractActionForm)
	{
		super();
	}

	/**
	 * @see edu.wustl.common.domain.AbstractDomainObject#getId()
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_CONTAINER_TYPE_SEQ"
	 * @return Long.
	 */
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Set Identifier.
	 * (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#setId(java.lang.Long)
	 * @param identifier Long.
	 */
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * @return Returns the capacity.
	 * @hibernate.many-to-one column="CAPACITY_ID" class="edu.wustl.catissuecore.domain.Capacity"
	 * constrained="true"
	 */
	public Capacity getCapacity()
	{
		return capacity;
	}

	/**
	 * @param capacity The capacity to set.
	 */
	public void setCapacity(Capacity capacity)
	{
		this.capacity = capacity;
	}

	/**
	 * @return Returns the name.
	 * @hibernate.property name="name" type="string" column="NAME" length="255"
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return Returns the oneDimensionLabel.
	 * @hibernate.property name="oneDimensionLabel" type="string" column="ONE_DIMENSION_LABEL" length="255"
	 */
	public String getOneDimensionLabel()
	{
		return oneDimensionLabel;
	}

	/**
	 * @param oneDimensionLabel The oneDimensionLabel to set.
	 */
	public void setOneDimensionLabel(String oneDimensionLabel)
	{
		this.oneDimensionLabel = oneDimensionLabel;
	}

	/**
	 * @return Returns the twoDimensionLabel.
	 * @hibernate.property name="twoDimensionLabel" type="string" column="TWO_DIMENSION_LABEL" length="255"
	 */
	public String getTwoDimensionLabel()
	{
		return twoDimensionLabel;
	}

	/**
	 * @param twoDimensionLabel The twoDimensionLabel to set.
	 */
	public void setTwoDimensionLabel(String twoDimensionLabel)
	{
		this.twoDimensionLabel = twoDimensionLabel;
	}

	/**
	 * @return Returns the comment.
	 * @hibernate.property name="comment" type="string" column="COMMENTS" length="2000"
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * @param comment The comment to set.
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * @return Returns the activityStatus.
	 * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * @param activityStatus The activityStatus to set.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.
	 * common.actionForm.AbstractActionForm)
	 */
	/**
	 * Set All Values.
	 * @param arg0 IValueObject.
	 * @throws AssignDataException AssignDataException.
	 */
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		if (SearchUtil.isNullobject(capacity))
		{
			capacity = new Capacity();
		}
	}

	/**
	 * Returns message label to display on success add or edit.
	 * @return String
	 */
	public String getMessageLabel()
	{
		return this.name;
	}
}