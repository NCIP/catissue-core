/*
 * Created on Jun 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.cde;


/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CDEConConfig
{
	//	used for connection
    private String proxyhostip;
    private String proxyport;
    private String username;
    private String password;

	//	the dbserver to connect
    private String dbserver;


    /**
     * @return Returns the dbserver.
     */
    public String getDbserver()
    {
        return dbserver;
    }
    /**
     * @return Returns the password.
     */
    public String getPassword()
    {
        return password;
    }
    /**
     * @return Returns the proxyhostip.
     */
    public String getProxyhostip()
    {
        return proxyhostip;
    }
    /**
     * @return Returns the proxyport.
     */
    public String getProxyport()
    {
        return proxyport;
    }
    /**
     * @return Returns the username.
     */
    public String getUsername()
    {
        return username;
    }
	public CDEConConfig(String proxyhostip, String proxyport, String username, String password, String dbserver)
	{
		this.proxyhostip = proxyhostip;
		this.proxyport = proxyport;
		this.username = username;
		this.password = password;
		this.dbserver = dbserver;
	} // CDEConConfig constructor

} // CDEConConfig