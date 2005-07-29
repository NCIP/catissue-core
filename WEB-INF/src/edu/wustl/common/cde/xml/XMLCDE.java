/*
 * Created on Jun 15, 2005
 * Last Modified Jun 21, 2005
 *
 */
package edu.wustl.common.cde.xml;

import java.util.List;
import java.util.ArrayList;

/**
 * @author mandar_deshmukh
 *
 */
public class XMLCDE
{

    boolean cache;
    boolean lazyLoading;
    String name;
    String publicId;
    List xmlPermissibleValues;

    
    
	/**
	 * @param cache The cache to set.
	 */
	public void setCache(boolean cache)
	{
		this.cache = cache;
	}

	/**
	 * @param lazyLoading The lazyLoading to set.
	 */
	public void setLazyLoading(boolean lazyLoading)
	{
		this.lazyLoading = lazyLoading;
	}
	
	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * @param publicId The publicId to set.
	 */
	public void setPublicId(String publicId)
	{
		this.publicId = publicId;
	}
	
	/**
	 * @param xmlPermissibleValues The xmlPermissibleValues to set.
	 */
	public void setXmlPermissibleValues(List xmlPermissibleValues)
	{
		this.xmlPermissibleValues = xmlPermissibleValues;
	}
    
	/**
     * @param cache
     * @param lazyLoading
     * @param name
     * @param publicId
     * @param xmlPermissibleValues
     * Parameterised Constructor to create an object using the given values 
     */
    public XMLCDE(boolean cache, boolean lazyLoading, 
    				String name, String publicId,
					List xmlPermissibleValues)
    {
        this.cache = cache;
        this.lazyLoading = lazyLoading;
        this.name = name;
        this.publicId = publicId;
        this.xmlPermissibleValues = xmlPermissibleValues;
    } // constructor XMLCDE
    
    public XMLCDE()
    {
    	xmlPermissibleValues = new ArrayList();
    } // constructor
    
    /**
     * @return Returns the cache.
     */
    public boolean isCache()
    {
        return cache;
    }
    
    /**
     * @return Returns the lazyLoading.
     */
    public boolean isLazyLoading()
    {
        return lazyLoading;
    }
    
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * @return Returns the publicId.
     */
    public String getPublicId()
    {
        return publicId;
    }
    
    /**
     * @return Returns the xmlPermissibleValues.
     */
    public List getXmlPermissibleValues()
    {
        return xmlPermissibleValues;
    }
    
    public boolean addXmlPermissibleValue(XMLPermissibleValue obj)
    {
    	try
		{
    		return xmlPermissibleValues.add(obj); 
		} // try
    	catch(Exception e)
		{
    		return false;
		} // catch
    } // addXmlPermissibleValue
} // XMLCDE
