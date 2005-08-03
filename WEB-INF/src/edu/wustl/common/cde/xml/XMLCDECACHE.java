/*
 * Created on Jun 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.cde.xml;
import java.util.ArrayList;
import java.util.List;


/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XMLCDECache
{
    long refreshTime;
    boolean lazyLoading;
    List xmlCDEs;
    
    
	/**
	 * @param lazyLoading The lazyLoading to set.
	 */
	public void setLazyLoading(boolean lazyLoading)
	{
		this.lazyLoading = lazyLoading;
	}
	
	/**
	 * @param refreshTime The refreshTime to set.
	 */
	public void setRefreshTime(long refreshTime)
	{
		this.refreshTime = refreshTime;
	}
	
	/**
	 * @param xmlCDEs The xmlCDEs to set.
	 */
	public void setXmlCDEs(List xmlCDEs)
	{
		this.xmlCDEs = xmlCDEs;
	}
	
    /**
     * @param refreshTime
     * @param lazyLoading
     * @param xmlCDEs
     * Constructor to accept values and set at object creation
     */
    public XMLCDECache(long refreshTime, boolean lazyLoading, List xmlCDEs)
	{
        this.refreshTime = refreshTime;
        this.lazyLoading = lazyLoading;
        this.xmlCDEs =  xmlCDEs;
	} // constructor XMLCDECache

    /**
     * Empty constructor. Will be used for creating the object and
     * setting the values as required. 
     */
    public XMLCDECache()
	{    	
    	xmlCDEs = new ArrayList();
	} // XMLCDECache
    
    /**
     * @return Returns the lazyLoading.
     */
    public boolean isLazyLoading()
    {
        return lazyLoading;
    }
    /**
     * @return Returns the refreshTime.
     */
    public long getRefreshTime()
    {
        return refreshTime;
    }
    /**
     * @return Returns the xmlCDEs.
     */
    public List getXmlCDEs()
    {
        return xmlCDEs;
    }
    
    public boolean addXmlCDE(XMLCDE obj)
    {
    	try
		{
    		return xmlCDEs.add(obj); 
		} // try
    	catch(Exception e)
		{
    		return false;
		} // catch
    } // addXmlCDE
} // XMLCDECache
