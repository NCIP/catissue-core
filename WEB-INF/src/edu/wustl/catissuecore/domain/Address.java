/**
 * <p>Title: Address Class</p>
 * <p>Description: A set of attributes that defines the physical location of a User or Site.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

/**
 * A set of attributes that defines the physical location of a User or Site.
 * @hibernate.class table="CATISSUE_ADDRESS"
 * @author Mandar Deshmukh
 */
public class Address implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique identifier.
	 */
	protected Long systemIdentifier;

	/**
	 * Multi-Line Street Address.
	 */
	protected String street;
	
	/**
	 * City
	 */
	protected String city;

	/**
	 * State
	 */
	protected String state;

	/**
	 * Country
	 */
	protected String country;

	/**
	 * Zip code
	 */
	protected String zipCode;

	/**
	 * Phone number
	 */
	protected String phoneNumber;

	/**
	 * Fax number
	 */
	protected String faxNumber;

	/**
	 * Email Address
	 */
	protected String emailAddress;

	/**
	 * Returns the identifier assigned to Address.
	 * 
	 * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native" 
	 * @return a unique systemIdentifier assigned to the address.
	 */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
	 * @param systemIdentifier Unique identifier to be assigned to the address.
	 */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
	 * Returns the Street of the address.
	 * @hibernate.property name="street" type="string" column="STREET" length="50"
	 * @return Street of the address.
	 */
	public String getStreet()
	{
		return street;
	}

	/**
	 * @param street
	 * Sets the street of the address
	 */
	public void setStreet(String street)
	{
		this.street = street;
	}

	/**
	 * Returns the City of the address.
	 * @hibernate.property name="city" type="string" column="CITY" length="50"
	 * @return City of the address.
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * @param city
	 * set the City of the address.
	 */
	public void setCity(String city)
	{
		this.city = city;
	}

	/**
	 * Returns the state of the address.
	 * @hibernate.property name="state" type="string" column="STATE" length="50"
	 * @return state of the address.
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 * set the state of the address.
	 */
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	 * Returns the Country of the address.
	 * @hibernate.property name="country" type="string" column="COUNTRY" length="50"
	 * @return country of the address.
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 * set the country of the address.
	 */
	public void setCountry(String country)
	{
		this.country = country;
	}

	/**
	 * Returns the zipcode of the address.
	 * @hibernate.property name="zipCode" type="string" column="ZIPCODE" length="30"
	 * @return zipCode of the address.
	 */
	public String getZipCode()
	{
		return zipCode;
	}

	/**
	 * @param zipCode
	 * set the zipCode of the address.
	 */
	public void setZipCode(String zipCode)
	{
		this.zipCode = zipCode;
	}

	/**
	 * Returns the associated phonenumber.
	 * @hibernate.property name="phoneNumber" type="string" column="PHONE_NUMBER" length="50"
	 * @return phoneNumber of the address.
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 * set the phoneNumber of the address.
	 */
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Returns the faxnumber of the address.
	 * @hibernate.property name="faxNumber" type="string" column="FAX_NUMBER" length="50"
	 * @return faxNumber of the address.
	 */
	public String getFaxNumber()
	{
		return faxNumber;
	}

	/**
	 * @param faxNumber associated fax Number. 
	 * set the faxNumber of the address.
	 */
	public void setFaxNumber(String faxNumber)
	{
		this.faxNumber = faxNumber;
	}

	/**
	 * Returns the associated email Address.
	 * @hibernate.property name="emailAddress" type="string" column="EMAIL_ADDRESS" length="100"
	 * @return associated emailAddress.
	 */
	public String getEmailAddress()
	{
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 * set the emailAddress of the address.
	 */
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}
}