/**
 * <p>Title: Site Class>
 * <p>Description:  Models the Site information of the bio-specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

/**
 * Models the Site information of the bio-specimen.
 * @hibernate.class table="CATISSUE_SITE"
 * @author gautam_shetty
 */
public class Site implements java.io.Serializable
{

    private static final long serialVersionUID = 1234567890L;

    /**
	 * identifier is a unique id assigned to each site.
	 */
    protected Long identifier;

    /**
     * Name of the site.
     */
    protected String name;

    /**
     * Type of site (e.g. Collection site, repository, or laboratory).
     */
    protected String type;

    /**
     * Departmental affiliation.
     */
    protected String department;

    /**
     * Shipping address of site.
     */
    protected String shippingAddress;

    /**
     * Person responsible at that site.
     */
    protected User coordinator;

    /**
     * Activity Status of Site, it could be CLOSED, ACTIVE, DISABLED.
     */
    protected ActivityStatus activityStatus;

    /**
     * Physical location of the site (e.g. room number, building).
     */
    private Address address;

    /**
     * Institute of the Site.
     */
    private Institute institute;

    /**
	 * Returns the identifier assigned to site.
	 * @return Long representing the id assigned to site.
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @see #setIdentifier(Long)
	 */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
     * Sets the identifier assigned to site.
     * @param identifier the identifier assigned to site.
     * @see #getIdentifier()
     */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Return the name of the site.
	 * @hibernate.property name="name" type="string" column="NAME" length="50"
     * @return the name of the site.
     * @see #setName(String)
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the site.
     * @param name the name of the site.
     * @see #getName()
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the type of the site.
     * @hibernate.property name="type" type="string" column="TYPE" length="50"
     * @return the type of the site.
     * @see #setType(String)
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the type of the site.
     * @param type the type of the site.
     * @see #getType()
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Returns the department affiliation of the site.
     * @hibernate.property name="department" type="string" column="DEPARTMENT" length="50"
     * @return the department affiliation of the site.
     * @see #setDepartment(String)
     */
    public String getDepartment()
    {
        return department;
    }

    /**
     * Sets the department affiliation of the site.
     * @param department the department affiliation of the site.
     * @see #getDepartment()
     */
    public void setDepartment(String department)
    {
        this.department = department;
    }

    /**
     * Returns the shipping address of site.
     * @hibernate.property name="shippingAddress" type="string" column="SHIPPING_ADDRESS" length="50"
     * @return the shipping address of site.
     * @see #setShippingAddress(String)
     */
    public String getShippingAddress()
    {
        return shippingAddress;
    }

    /**
     * Sets the shipping address of site.
     * @param shippingAddress the shipping address of site.
     * @see #getShippingAddress()
     */
    public void setShippingAddress(String shippingAddress)
    {
        this.shippingAddress = shippingAddress;
    }

    /**
     * Returns the person who is responsible at that site.
     * @hibernate.many-to-one column="COORDINATOR" class="edu.wustl.catissuecore.domain.User"
	 * constrained="true"
     * @return the person who is responsible at that site.
     * @see #setCoordinator(User)
     */
    public User getCoordinator()
    {
        return coordinator;
    }

    /**
     * Sets the person who is responsible at that site.
     * @param coordinator the person who is responsible at that site.
     * @see #getCoordinator()
     */
    public void setCoordinator(User coordinator)
    {
        this.coordinator = coordinator;
    }

    /**
	 * Returns the activity status of the Site.
	 * @hibernate.many-to-one column="ACTIVITY_STATUS_ID"
	 * class="edu.wustl.catissuecore.domain.ActivityStatus"
	 * constrained="true"
	 * @return Returns the activity status of the Site.
	 * @see #setActivityStatus(ActivityStatus)
	 */
    public ActivityStatus getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * Sets the activity status of the Site.
     * @param activityStatus the activity status of the Site.
     * @see #getActivityStatus()
     */
    public void setActivityStatus(ActivityStatus activityStatus)
    {
        this.activityStatus = activityStatus;
    }

    /**
     * Returns the address of the site.
	 * @hibernate.many-to-one column="ADDRESS_ID" 
	 * class="edu.wustl.catissuecore.domain.Address" constrained="true"
     * @return the address of the site.
     * @see #setAddress(Address)
     */
    public Address getAddress()
    {
        return address;
    }

    /**
     * Sets the address of the site.
     * @param address the address of the site.
     * @see #getAddress()
     */
    public void setAddress(Address address)
    {
        this.address = address;
    }

    /**
     * Returns the institute of the site.
     * @hibernate.many-to-one column="INSTITUTE_ID" 
     * class="edu.wustl.catissuecore.domain.Institute" constrained="true"
     * @return Returns the institute of the site.
     * @see #setInstitute(Institute)
     */
    public Institute getInstitute()
    {

        return institute;
    }

    /**
     * Sets the institute of the site.
     * @param institute the institute of the site.
     * @see #getInstitute()
     */
    public void setInstitute(Institute institute)
    {
        this.institute = institute;
    }
}