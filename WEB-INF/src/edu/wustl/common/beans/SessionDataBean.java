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
 *@author Poornima Govindrao
 *@version 1.0
 */

public class SessionDataBean 
{
    private String userName=new String();
    private String ipAddress=new String();
    private Long userId = null;

    /**
     * @return Returns the userName.
     */
    public String getUserName()
    {
        return userName;
    }
    /**
     * @param userName The userName to set.
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    /**
     * @return Returns the ipAddress.
     */
    public String getIpAddress()
    {
        return ipAddress;
    }
    /**
     * @param ipAddress The ipAddress to set.
     */
    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }
    /**
     * @return Returns the userId.
     */
    public Long getUserId()
    {
        return userId;
    }
    /**
     * @param userId The userId to set.
     */
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }
    

}
