/**
 * <p>Title: FrozenEventParameters Class>
 * <p>Description:  Attributes associated with a freezing event of a specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */
package edu.wustl.catissuecore.domain;

/**
 * Attributes associated with a freezing event of a specimen.
 * @hibernate.joined-subclass table="CATISSUE_FROZEN_EVENT_PARAMETERS"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 * @author Aniruddha Phadnis
 */
public class FrozenEventParameters extends SpecimenEventParameters implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
     * Method applied on specimen to freeze it.
     */
	protected String method;

	/**
     * Returns method applied on specimen to freeze it.
     * @return Method applied on specimen to freeze it.
     * @see #setMethod(String)
     * @hibernate.property name="method" type="string" 
     * column="METHOD" length="50"
     */
	public String getMethod()
	{
		return method;
	}

	/**
     * Sets method applied on specimen to freeze it.
     * @param method method applied on specimen to freeze it.
     * @see #getMethod()
     */
	public void setMethod(String method)
	{
		this.method = method;
	}
}