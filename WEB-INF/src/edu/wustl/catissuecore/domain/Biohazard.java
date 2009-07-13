/**
 * <p>Title: Biohazard Class>
 * <p>Description:  An attribute of a Specimen that renders
 * it potentially harmful to a User.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.BiohazardForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

/**
 * An attribute of a Specimen that renders
 * it potentially harmful to a User.
 * @hibernate.class table="CATISSUE_BIOHAZARD"
 * @author gautam_shetty
 */
public class Biohazard extends AbstractDomainObject
{

	/**
	 * Serial Version Id.
	 */
	private static final long serialVersionUID = 1234567890L;
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(Biohazard.class);

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * Name of the biohazardous agent.
	 */
	protected String name;

	/**
	 * Comment about the biohazard.
	 */
	protected String comment;

	/**
	 * Type of biohazard (Infectious, Radioactive, Toxic, Carcinogen, Mutagen).
	 */
	protected String type;

	/**
	 * boolean for checking persisted Biohazard persisted value.
	 */

	protected transient Boolean persisted;

	/**
	 * Specimen Collection.
	 */
	protected Collection specimenCollection = new HashSet();

	/**
	 * Default Constructor.
	 */
	public Biohazard()
	{
		super();
	}

	/**
	 * Parameterized constructor.
	 * @param form AbstractActionForm.
	 * @throws AssignDataException : AssignDataException
	 */
	public Biohazard(AbstractActionForm form) throws AssignDataException
	{
		super();
		this.setAllValues(form);
	}

	/**
	 * Parameterized constructor.
	 * @param bioHazard Biohazard.
	 */
	public Biohazard(Biohazard bioHazard)
	{
		super();
		this.comment = bioHazard.getComment();
		this.name = bioHazard.getName();
		this.persisted = bioHazard.getPersisted();
		this.type = bioHazard.getType();
	}

	/**
	 * Returns the system generated unique id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_BIOHAZARD_SEQ"
	 * @return the system generated unique id.
	 * @see #setId(Long)
	 * */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Sets the system generated unique id.
	 * @param identifier the system generated unique id.
	 * @see #getId()
	 * */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns the name of the biohazardous agent.
	 * @hibernate.property name="name" type="string"
	 * column="NAME" length="255" not-null="true" unique="true"
	 * @return the name of the biohazardous agent.
	 * @see #setName(String)
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Sets the name of the biohazardous agent.
	 * @param name the name of the biohazardous agent.
	 * @see #getName()
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Returns the comments about the biohazard.
	 * @hibernate.property name="comment" type="string"
	 * column="COMMENTS" length="500"
	 * @return the comments about the biohazard.
	 * @see #setComments(String)
	 */
	public String getComment()
	{
		return this.comment;
	}

	/**
	 * Sets the comment about the biohazard.
	 * @param comment the comments about the biohazard.
	 * @see #getComment()
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * Returns the type of biohazard (Infectious, Radioactive, Toxic, Carcinogen, Mutagen).
	 * @hibernate.property name="type" type="string"
	 * column="TYPE" length="50"
	 * @return the type of biohazard (Infectious, Radioactive, Toxic, Carcinogen, Mutagen).
	 * @see #setType(String)
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * Sets the type of biohazard (Infectious, Radioactive, Toxic, Carcinogen, Mutagen).
	 * @param type the type of biohazard (Infectious, Radioactive, Toxic, Carcinogen, Mutagen).
	 * @see #getType()
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/*
	 * @hibernate.many-to-one column="SPECIMEN_ID"  class="edu.wustl.catissuecore.domain.Specimen"
	 * constrained="true"
	 * @see #setParticipant(Site)
	 */
	/**
	 * @hibernate.set name="specimenCollection" table="CATISSUE_SPECIMEN_BIOHZ_REL"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="BIOHAZARD_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.Specimen" column="SPECIMEN_ID"
	 * @return Collection of specimen.
	 */
	public Collection getSpecimenCollection()
	{
		return this.specimenCollection;
	}

	/**
	 * @param specimenCollection The specimen to set.
	 */
	public void setSpecimenCollection(Collection specimenCollection)
	{
		this.specimenCollection = specimenCollection;
	}

	/**
	 * This function Copies the data from an BiohazardForm object to a Biohazard object.
	 * @param abstractForm - siteForm An SiteForm object containing the information about the site.
	 * @throws AssignDataException : AssignDataException
	 * */
	@Override
	public void setAllValues(IValueObject abstractForm) 
	throws AssignDataException
	{
		try
		{
			final BiohazardForm form = (BiohazardForm) abstractForm;
			this.comment = form.getComments();
			this.name = form.getName().trim();
			this.type = form.getType();
		}
		catch (final Exception excp)
		{
			logger.error(excp);
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "Biohazard.java :");
		}
	}

	/**
	 * Get status, if persisted.
	 * @return Boolean.
	 */
	public Boolean getPersisted()
	{
		return this.persisted;
	}

	/**
	 * Set if persisted.
	 * @param persisted boolean value.
	 */
	public void setPersisted(Boolean persisted)
	{
		this.persisted = persisted;
	}

	/**
	* Returns message label to display on success add or edit.
	* @return String
	*/
	@Override
	public String getMessageLabel()
	{
		return this.name;
	}

	/**
	 * Convert to String.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "EI{" + "id " + this.id + "\t" + "Type " + this.type + "\t" + "Name " + this.name
				+ "}";
	}
}