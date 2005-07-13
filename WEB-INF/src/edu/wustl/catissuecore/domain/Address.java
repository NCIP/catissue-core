/**
 * <p>Title: Address Class>
 * <p>Description:  Models the Address information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Kapil Kaveeshwar
 * @version 1.00
 */
package edu.wustl.catissuecore.domain;


/**
 * @hibernate.class table="CATISSUE_ADDRESS"
 */
public class Address
{
	/**
	 * id used by hibernate for as unique identifier
	 * */
	private Long identifier;
	
	/**
	 * A string containing the street of the user.
	 */
	private String street = null;
	
	/**
	 * A string containing the City where the user stays.
	 */
	private String city = null;
	
	/**
	 * A string containing the name of the State where the user stays.
	 */
	private String state = null;
	
	/**
	 * A string containing the name of the Country where the user stays.
	 */
	private String country = null; 
	
	/**
	 * A String containing the zip code of city where the user stays.
	 * */
	private String zip = null;
	
	/**
	 * A String containing the phone number of the user.
	 * */
	private String phone = null;
	
	/**
	 * A String containing the fax number of the user.
	 * */
	private String fax = null;
	
	
	/**
	 * Initialize a new Address instance. 
	 * Note: Hibernate invokes this constructor through reflection API.  
	 */
	public Address()
	{
		
	}
	
	/**
	 * Returns the identifier assigned to Address.
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return int representing the id assigned to Address.
     * @see #setIdentifier(int)
	 * */
	public Long getIdentifier()
	{
		return (this.identifier);
	}
	
	/**
	 * Sets an id for the Address.
	 * @param identifier id to be assigned to the Addres.
	 * @see #getIdentifier()
	 * */
	public void setIdentifier(Long identifier)
	{
		this.identifier = identifier;
	}
	
	/**
	 * Returns the street of the user.
	 * @hibernate.property name="street" type="string" 
     * column="STREET" length="50"
	 * @return String representing street of the user.
	 * @see #setStreet(String)
	 */
	public String getStreet() 
	{
		return (this.street);
	}
	
	/**
	 * Sets the street of the user.
	 * @param String representing street of the user.
	 * @see #getStreet()
	 */
	public void setStreet(String street)
	{
		this.street = street;
	}
	
	/**
	 * @hibernate.property name="city" type="string"  
     * column="CITY" length="50"
	 * Returns the City where the user stays.
	 * @return String representing city of the user.
	 * @see #setCity(String)
	 */
	public String getCity() 
	{
		return (this.city);
	}
	
	/**
	 * Sets the City where the user stays.
	 * @param city String representing city of the user.
	 * @see #getCity()
	 */
	public void setCity(String city)
	{
		this.city = city;
	}
	
	/**
	 * @hibernate.property name="state" type="string" 
     * column="STATE" length="50"
	 * Returns the State where the user stays.
	 * @return String representing state of the user.
	 * @see #setState(String)
	 */
	public String getState() 
	{
		return (this.state);
	}
	
	/**
	 * Sets the State where the user stays.
	 * @param state String representing state of the user.
	 * @see #getState()
	 */
	public void setState(String state)
	{
		this.state = state;
	}
	
	/**
	 * @hibernate.property name="country" type="string" 
     * column="COUNTRY" length="50"
	 * Returns the Country where the user stays.
	 * @return String representing country of the user.
	 * @see #setCountry(String)
	 */
	public String getCountry() 
	{
		return (this.country);
	}
	
	/**
	 * 
	 * Sets the Country where the user stays.
	 * @param country String representing country of the user.
	 * @see #getCountry()
	 */
	public void setCountry(String country)
	{
		this.country = country;
	}
	
	/**
	 * @hibernate.property name="zip" type="string"
     * column="ZIP" length="50" 
	 * Returns the zip code of the user's city. 
	 * @return Returns the zip.
	 * @see #setZip(String)
	 */
	public String getZip()
	{
		return zip;
	}
	
	/**
	 * Sets the zip code of the user's city.
	 * @param zip The zip code to set.
	 * @see #getZip()
	 */
	public void setZip(String zip)
	{
		this.zip = zip;
	}
	
	/**
	 * @hibernate.property name="phone" type="string"
     * column="PHONE" length="50" 
	 * Returns the phone number of the user.
	 * @return Returns the phone number.
	 * @see #setPhone(String)
	 */
	public String getPhone()
	{
		return phone;
	}
	
	/**
	 * Sets the phone number of the user. 
	 * @param phone The phone number to set.
	 * @see #getPhone()
	 */
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	
	/**
	 * @hibernate.property name="fax" type="string" 
     * column="FAX" length="50"
	 * Returns the fax number of the user.
	 * @return Returns the fax.
	 * @see #setFax(String)
	 */
	public String getFax()
	{
		return fax;
	}
	/**
	 * Sets the fax number of the user.
	 * @param fax The fax to set.
	 * @see #getFax()
	 */
	public void setFax(String fax)
	{
		this.fax = fax;
	}
}