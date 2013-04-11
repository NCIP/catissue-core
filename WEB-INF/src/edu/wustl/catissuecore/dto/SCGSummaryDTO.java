package edu.wustl.catissuecore.dto;

import java.util.Date;


/**
 * @author rinku
 *
 */
public class SCGSummaryDTO
{
	Long scgId;
	String scgName;
	Long site;
	Date receivedDate;
	Long  receiver;
	Date collectedDate;
	Long collector;
			
	public String getScgName()
	{
		return scgName;
	}
	
	public void setScgName(String scgName)
	{
		this.scgName = scgName;
	}
	
	public Long getSite()
	{
		return site;
	}
	
	public void setSite(Long site)
	{
		this.site = site;
	}
	
	public Date getReceivedDate()
	{
		return receivedDate;
	}
	
	public void setReceivedDate(Date receivedDate)
	{
		this.receivedDate = receivedDate;
	}
	
	public Long getReceiver()
	{
		return receiver;
	}
	
	public void setReceiver(Long receiver)
	{
		this.receiver = receiver;
	}
	
	public Date getCollectedDate()
	{
		return collectedDate;
	}
	
	public void setCollectedDate(Date collectedDate)
	{
		this.collectedDate = collectedDate;
	}
	
	public Long getCollector()
	{
		return collector;
	}
	
	public void setCollector(Long collector)
	{
		this.collector = collector;
	}

	
	public Long getScgId()
	{
		return scgId;
	}
	
	public void setScgId(Long scgId)
	{
		this.scgId = scgId;
	}

}
