/**
 * <p>Title: AbstractDomain Class>
 * <p>Description:  AbstractDomain class is the superclass of all the domain classes. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */

package edu.wustl.common.domain;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.audit.Auditable;
import edu.wustl.common.exception.AssignDataException;


/**
 * AbstractDomain class is the superclass of all the domain classes.
 * @author gautam_shetty
 */
public abstract class AbstractDomainObject implements Auditable
{
	public String getObjectId() 
	{
		return this.getClass().getName()+ "_" + this.getSystemIdentifier();
	}
    /**
     * Parses the fully qualified classname and returns only the classname.
     * @param fullyQualifiedName The fully qualified classname. 
     * @return The classname.
     */
    public static String parseClassName(String fullyQualifiedName)
    {
        try
        {
            return fullyQualifiedName.substring(fullyQualifiedName
                    .lastIndexOf(".") + 1);
        }
        catch (Exception e)
        {
            return fullyQualifiedName;
        }
    }
    
    /**
     * Copies all values from the AbstractForm object
     * @param abstractForm The AbstractForm object
     */
    public abstract void setAllValues(AbstractActionForm abstractForm) throws AssignDataException;
    
    /**
	 * Returns the unique systemIdentifier assigned to the domain object.
     * @return returns a unique systemIdentifier assigned to the domain object.
     * @see #setIdentifier(Long)
	 * */
    public abstract Long getSystemIdentifier();

    /**
	 * Sets an systemIdentifier for the domain object.
	 * @param systemIdentifier systemIdentifier for the domain object.
	 * @see #getIdentifier()
	 * */
    public abstract void setSystemIdentifier(Long systemIdentifier);
    
    /**
	 * Returns the unique systemIdentifier assigned to the domain object.
     * @return returns a unique systemIdentifier assigned to the domain object.
     * @see #setIdentifier(Long)
	 * */
    public Long getId()
    {
    	return getSystemIdentifier();
    }

    /**
	 * Sets an systemIdentifier for the domain object.
	 * @param systemIdentifier systemIdentifier for the domain object.
	 * @see #getIdentifier()
	 * */
    public void setId(Long id)
    {
    	setSystemIdentifier(id);
    }
}