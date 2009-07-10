/**
 *
 */

package edu.wustl.catissuecore.bean;

import java.util.List;

/**
 * @author mandar_deshmukh
 */
public class SummaryAdminDetails
{

	/**
	 * Empty  Constructor
	 */
	public SummaryAdminDetails()
	{
		super();
	}

	private String regUsers;
	private String colSites;
	private String cpTot;
	private String dpTot;
	private String repSites;
	private String labSites;

	private List < List > adminInfo;
	/**
	 * @return - colSites
	 */
	public String getColSites()
	{
		return this.colSites;
	}
	/**
	 * @param colSitesParam - colSitesParam
	 */
	public void setColSites(final String colSitesParam)
	{
		this.colSites = colSitesParam;
	}
	/**
	 * @return - cpTot
	 */
	public String getCpTot()
	{
		return this.cpTot;
	}
	/**
	 * @param tot - cpTot
	 */
	public void setCpTot(final String tot)
	{
		this.cpTot = tot;
	}
	/**
	 * @return - dpTot
	 */
	public String getDpTot()
	{
		return this.dpTot;
	}
    /**
     * @param tot - tot
     */
	public void setDpTot(final String tot)
	{
		this.dpTot = tot;
	}
	/**
	 * @return - String
	 */
	public String getLabSites()
	{
		return this.labSites;
	}
	/**
	 * @param labSitesParam - labSitesParam
	 */
	public void setLabSites(final String labSitesParam)
	{
		this.labSites = labSitesParam;
	}
	/**
	 * @return - repSites
	 */
	public String getRepSites()
	{
		return this.repSites;
	}
	/**
	 * @param regSitesParam - regSitesParam
	 */
	public void setRepSites(final String regSitesParam)
	{
		this.repSites = regSitesParam;
	}
	/**
	 * @return - regUsers
	 */
	public String getRegUsers()
	{
		return this.regUsers;
	}
	/**
	 * @param regUsersParam - regUsersParam
	 */
	public void setRegUsers(final String regUsersParam)
	{
		this.regUsers = regUsersParam;
	}
	/**
	 * @return - List
	 */
	public List < List > getAdminInfo()
	{
		return this.adminInfo;
	}
	/**
	 * @param adminInfoParam - adminInfoParam
	 */
	public void setAdminInfo(List < List > adminInfoParam)
	{
		this.adminInfo = adminInfoParam;
	}

}
