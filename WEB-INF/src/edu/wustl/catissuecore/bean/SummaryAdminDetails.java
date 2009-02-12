/**
 * 
 */
package edu.wustl.catissuecore.bean;

import java.util.List;


/**
 * @author mandar_deshmukh
 *
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

	private List <List> adminInfo;

	
	public String getColSites()
	{
		return colSites;
	}
	
	public void setColSites(final String colSites)
	{
		this.colSites = colSites;
	}
	
	public String getCpTot()
	{
		return cpTot;
	}
	
	public void setCpTot(final String tot)
	{
		cpTot = tot;
	}
	
	public String getDpTot()
	{
		return dpTot;
	}
	
	public void setDpTot(final String tot)
	{
		dpTot = tot;
	}
	
	public String getLabSites()
	{
		return labSites;
	}
	
	public void setLabSites(final String labSites)
	{
		this.labSites = labSites;
	}
	
	public String getRepSites()
	{
		return repSites;
	}
	
	public void setRepSites(final String regSites)
	{
		this.repSites = regSites;
	}
	
	public String getRegUsers()
	{
		return regUsers;
	}
	
	public void setRegUsers(final String regUsers)
	{
		this.regUsers = regUsers;
	}
	
	
	
	public List<List> getAdminInfo()
	{
		return adminInfo;
	}

	
	public void setAdminInfo(List<List> adminInfo)
	{
		this.adminInfo = adminInfo;
	}
	

}
