/**
 * <p>Title: CDEImpl Class
 * <p>Description: This class provides the implementation of the CDE interface.</p> 
 *<p>It also provides methods to set the values of the CDE and obtain the inforamtion about the CDE.</p> 
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on May 26, 2005
 */

package edu.wustl.common.cde;
import java.util.Set;

/**
 * @author mandar_deshmukh
 *
 * <p>Description: This class provides the implementation of the CDE interface.</p> 
 *<p>It also provides methods to set the values of the CDE and obtain the inforamtion about the CDE.</p>
 * @hibernate.class table="CATISSUE_CDE" 
 */
public class CDEImpl implements CDE 
{

	/**
     * publicid is a unique id assigned to each CDE.
     * */
	private String publicid;
	
	/**
     * preferredname is the name of the CDE that is preferred. 
     * */
	private String preferredname;
	
	/**
     * longname is the complete name of the CDE. 
     * */
	private String longname;
	
	/**
     * defination is the defination of the CDE. 
     * */
	private String defination;
	
	/**
     * version is the current version of the CDE. 
     * */
	private String version;
	
	/**
     * permissiblevalues is the List of all the Permissible Values for the CDE. 
     * */
	private Set permissiblevalues;
	
	/* (non-Javadoc)
	 * @see edu.wustl.common.cde.CDE#getPublicId()
	 */

	/**
     * getPublicId method returns the public id of the CDE.
     * @returns PublicID as a String Object of the CDE.   
     * @hibernate.id name="publicid" column="PUBLIC_ID" type="string"
	 * length="30" unsaved-value="null" generator-class="assigned"
     * */
	public String getPublicId()
	{
		return this.publicid ;
	} 

	/**
     * setPublicId method is used for setting the publicid of the CDE. 
     * @param accepts a String object.     
     * */
	public void setPublicId(String publicid)
	{
		this.publicid = publicid;	
	}
		
	/* (non-Javadoc)
	 * @see edu.wustl.common.cde.CDE#getLongName()
	 */
	
	/**
     * getLongName method returns the long name of the CDE.
     * @returns LongName as a String Object of the CDE.   
     * @hibernate.property name="longname" type="string" column="LONG_NAME" length="200"
     * */
	public String getLongName()
	{
		return this.longname;
	}

	/**
     * setLongName method is used for setting the longname of the CDE 
     * @param accepts a String object.   
     * */
	public void setLongName(String longname)
	{
		this.longname = longname; 
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.cde.CDE#getDefination()
	 */
	
	/**
     * getDefination method returns the Defination of the CDE.
     * @returns Defination of the CDE as a String Object.
     * @hibernate.property name="defination" type="string" column="DEFINITION" length="500"   
     * */
	public String getDefination()
	{
		return this.defination;
	}

	/**
     * setDefination method is used for setting the defination of the CDE 
     * @param accepts a String object.   
     * */
	public void setDefination(String defination)
	{
		this.defination  = defination; 
	}
	
	/**
     * getVersion method returns the version of the CDE.
     * @returns Version of the CDE as a String Object.   
     * @hibernate.property name="version" type="string" column="VERSION" length="50"
     * */
	public String getVersion()
	{
		return this.version;
	}

	/**
     * setVersion method is used for setting the version of the CDE 
     * @param accepts a String object.   
     * */
	public void setVersion(String version)
	{
		this.version   = version; 
	}
	
	/**
     * getPermissibleValues method returns the Permissible Values of the CDE.
     * @returns List of the PermissibleValues of the CDE.   
     * @hibernate.set name="permissiblevalues" table="CATISSUE_PERMISSIBLE_VALUE"
	 * @hibernate.collection-key column="PUBLIC_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.common.cde.PermissibleValueImpl"
     * */
	public Set getPermissibleValues()
	{
		return this.permissiblevalues;
	}	
	/**
     * setPermissibleValues method is used for setting the PermissibleValues of the CDE 
     * @param accepts a List object as a parameter.   
     * */
	public void setPermissibleValues(Set permissiblevalues)
	{
		this.permissiblevalues = permissiblevalues; 
	}

	
	/**
	 * 
	 * @author Mandar Deshmukh</p>
	 * 
	 * addPermisibleValue method allows the user to add a permissible value to the existing
	 * list of available values.
	 * @param accepts an object of PermissibleValueImpl class.
	 * @return boolean value indicating whether the object was successfully added or not.  
	 */
		
	public boolean addPermissibleValue(PermissibleValueImpl pv)
	{
		return this.permissiblevalues.add(pv); 
	}
		
	/**
	 * 
	 * @author Mandar Deshmukh</p>
	 *
	 * removePermissibleValue method allows the user to remove a permissible value from the existing 
	 * list of available values.
	 * @param accepts an object of PermissibleValueImpl class.
	 * @return boolean value indicating whether the object was successfully removed or not.  
	 * 
	 */
	public boolean removePermissibleValue(PermissibleValueImpl pv)
	{
		return this.permissiblevalues.remove(pv); 
	}
		

	
	/**
	 * @return Returns the preferredname.
	 */
	public String getPreferredname() 
	{
		return preferredname;
	}
	/**
	 * @param preferredname The preferredname to set.
	 */
	public void setPreferredname(String preferredname) 
	{
		this.preferredname = preferredname;
	}
} // class
