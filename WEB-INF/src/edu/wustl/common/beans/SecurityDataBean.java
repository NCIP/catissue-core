/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.common.beans;

import java.util.HashSet;
import java.util.Set;


/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class SecurityDataBean
{
    String user = new String();
    Set group = new HashSet();
    String roleName = new String();
    String groupName = new String();
    String protectionGroupName = new String();
    
    
    /**
     * @return Returns the protectionGroupName.
     */
    public String getProtectionGroupName()
    {
        return protectionGroupName;
    }
    /**
     * @param protectionGroupName The protectionGroupName to set.
     */
    public void setProtectionGroupName(String protectionGroupName)
    {
        this.protectionGroupName = protectionGroupName;
    }
    /**
     * @return Returns the roleName.
     */
    public String getRoleName()
    {
        return roleName;
    }
    /**
     * @param roleName The roleName to set.
     */
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }
    /**
     * @return Returns the userGroup.
     */
    public Set getGroup()
    {
        return group;
    }
    /**
     * @param userGroup The userGroup to set.
     */
    public void setGroup(Set group)
    {
        this.group = group;
    }
    /**
     * @return Returns the user.
     */
    public String getUser()
    {
        return user;
    }
    /**
     * @param user The user to set.
     */
    public void setUser(String user)
    {
        this.user = user;
    }
    /**
     * @return Returns the groupName.
     */
    public String getGroupName()
    {
        return groupName;
    }
    /**
     * @param groupName The groupName to set.
     */
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return new String(" user:"+user+" groupName:"+groupName+" group:"+group.size()+" role:"+roleName+" protectionGroup:"+protectionGroupName);
    }
    
}
