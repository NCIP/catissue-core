/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


package edu.wustl.catissuecore.bean;

import java.util.Collection;

/**
 * @author janhavi_hasabnis
 */
public class SummaryPartDetails
{

	/**
	 * Empty constructor
	 */
	public SummaryPartDetails()
	{
		super();
	}

	private Collection<Object> pByCDDetails;
	private Collection<Object> pByCSDetails;
	private String totPartCount;

	/**
	 * @return - totPartCount
	 */
	public String getTotPartCount()
	{
		return this.totPartCount;
	}

	/**
	 * @param totPartCountParam - totPartCountParam
	 */
	public void setTotPartCount(final String totPartCountParam)
	{
		this.totPartCount = totPartCountParam;
	}

	/**
	 * @return - Collection
	 */
	public Collection<Object> getPByCDDetails()
	{
		return this.pByCDDetails;
	}

	/**
	 * @param byCDDetails - byCDDetails
	 */
	public void setPByCDDetails(final Collection<Object> byCDDetails)
	{
		this.pByCDDetails = byCDDetails;
	}

	/**
	 * @return - Collection
	 */
	public Collection<Object> getPByCSDetails()
	{
		return this.pByCSDetails;
	}

	/**
	 * @param byCSDetails - byCSDetails
	 */
	public void setPByCSDetails(final Collection<Object> byCSDetails)
	{
		this.pByCSDetails = byCSDetails;
	}

}
