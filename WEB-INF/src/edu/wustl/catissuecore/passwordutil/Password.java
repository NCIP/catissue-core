
package edu.wustl.catissuecore.passwordutil;

import java.io.Serializable;
import java.util.Date;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * Set of attributes which defines updated information related to password of the user.
 * @hibernate.class table="CATISSUE_PASSWORD"
 */
public class Password extends AbstractDomainObject implements Serializable, Comparable
{

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 2084916630660576930L;

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * A string containing the password of the user.
	 */
	protected String password;

	/**
	 * Last updated date of the password .
	 */
	protected Date updateDate;

	/**
	 * User.
	 */
	protected User user;

	/**
	 * Default Constructor.
	 */
	public Password()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param password String.
	 * @param user User.
	 */
	public Password(String password, User user)
	{
		super();
		this.password = password;
		this.user = user;
		this.updateDate = new Date();
	}

	/**
	 * Returns the id assigned to password.
	 * @return Returns the id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_PASSWORD_SEQ"
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Sets an id for the password.
	 * @param identifier Unique id to be assigned to the password.
	 */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns the password assigned to user.
	 * @hibernate.property name="password" type="string" column="PASSWORD" length="255"
	 * @return String.
	 */
	public String getPassword()
	{
		return this.password;
	}

	/**
	 * Sets password of the user.
	 * @param password String.
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * Returns the last updated date of password.
	 * @hibernate.property name="updateDate" type="date" column="UPDATE_DATE"
	 * @return Date.
	 */
	public Date getUpdateDate()
	{
		return this.updateDate;
	}

	/**
	 * Sets last updated date of password.
	 * @param updateDate Date.
	 */
	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}

	/**
	 * @return Returns the user.
	 * @hibernate.many-to-one column="USER_ID" class="edu.wustl.catissuecore.domain.User"
	 * constrained="true"
	 */
	public User getUser()
	{
		return this.user;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(User user)
	{
		this.user = user;
	}

	/**
	 * Set All Values.
	 * @param abstractForm IValueObject.
	 * @throws AssignDataException AssignDataException.
	 */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		if (SearchUtil.isNullobject(this.user))
		{
			this.user = new User();
		}
	}

	/**
	 *  This method will compate two date Objects. When Collections.sort() is called, it will return a list
	 *  of dates in which most recent date will be the first one.
	 *  @return integer.
	 *  @param obj Object.
	 */
	public int compareTo(Object obj)
	{
		final Password pwd = (Password) obj;
		return pwd.getUpdateDate().compareTo(this.updateDate);
	}
}