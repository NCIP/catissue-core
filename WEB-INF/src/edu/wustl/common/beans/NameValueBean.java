/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.common.beans;



/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class NameValueBean implements Comparable
{
    private String name=new String();
    private String value=new String();
    
    public NameValueBean()
    {
        
    }
    
    public NameValueBean(String name, String value)
    {
        this.name = name;
        this.value = value;
    }
    
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @return Returns the value.
     */
    public String getValue()
    {
        return value;
    }
    /**
     * @param value The value to set.
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return new String("name:"+ name +" value:"+value);
    }

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compareTo(Object obj)
	{
		if(obj instanceof NameValueBean)
		{
			NameValueBean nameValueBean = (NameValueBean)obj;
			return name.compareTo(nameValueBean.getName());
		}
		return 0;
	}
}
