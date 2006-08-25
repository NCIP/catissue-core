package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Date;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * Set of attributes which defines updated information related to password of the user.
 * @hibernate.class table="CATISSUE_PASSWORD"
 */
public class Password extends AbstractDomainObject implements Serializable, Comparable
{
	private static final long serialVersionUID = 2084916630660576930L;
	
	/**
	 * System generated unique systemIdentifier.
	 */
	protected Long systemIdentifier;
	
	/**
	 * A string containing the password of the user.
	 */
	protected String password;
	
	/**
     * Last updated date of the password .
     */
	protected Date updateDate;
	
	protected User user = new User();
	
	public Password()
	{
	    
	}
	
	public Password(String password, User user)
	{
	    this.password = password;
	    this.user = user;
	    updateDate = new Date();
	}
	
	/**
     * Returns the systemIdentifier assigned to password.
     * @return Returns the systemIdentifier.
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_PASSWORD_SEQ"
     */
	public Long getSystemIdentifier() 
	{
		return systemIdentifier;
	}
	
	/**
	 * Sets an systemIdentifier for the password.
	 * @param systemIdentifier Unique systemIdentifier to be assigned to the password.
	 */
	public void setSystemIdentifier(Long systemIdentifier) 
	{
	    this.systemIdentifier = systemIdentifier;
    }
	
	/**
	 * Returns the password assigned to user.
	 * @hibernate.property name="password" type="string" column="PASSWORD" length="50"
	 */
	public String getPassword() 
	{
		return password;
	}
	
	/**
	 * Sets password of the user.
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	/**
	 * Returns the last updated date of password.
	 * @hibernate.property name="updateDate" type="date" column="UPDATE_DATE"
	 */
	public Date getUpdateDate() 
	{
		return updateDate;
	}
	
	/**
	 * Sets last updated date of password.
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
        return user;
    }
    
    /**
     * @param user The user to set.
     */
    public void setUser(User user)
    {
        this.user = user;
    }
    
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException 
	{
    }
	
	public int compareTo(Object obj)
	{
	    Password pwd = (Password)obj;
	    return this.updateDate.compareTo(pwd.getUpdateDate());
	}
}