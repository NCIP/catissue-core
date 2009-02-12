package edu.wustl.catissuecore.bean;

import java.util.Collection;


public class SummaryPartDetails
{

	/**
	 * Empty constructor
	 *
	 */
	public SummaryPartDetails()
	{
		super();
	}
	private Collection<Object> pByCDDetails;
	private Collection<Object> pByCSDetails;
	private String totPartCount;
	
	public String getTotPartCount()
	{
		return totPartCount;
	}

	
	public void setTotPartCount(final String totPartCount)
	{
		this.totPartCount = totPartCount;
	}

	public Collection<Object> getPByCDDetails()
	{
		return pByCDDetails;
	}
	
	public void setPByCDDetails(final Collection<Object> byCDDetails)
	{
		pByCDDetails = byCDDetails;
	}
	
	public Collection<Object> getPByCSDetails()
	{
		return pByCSDetails;
	}
	
	public void setPByCSDetails(final Collection<Object> byCSDetails)
	{
		pByCSDetails = byCSDetails;
	}

}
