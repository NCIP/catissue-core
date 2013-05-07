/**
 * <p>Title: Address Class</p>
 * <p>Description: A set of attributes that defines the physical location of a User or Site.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Date;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * A set of attributes that defines status of the cp synchronization process.
 * @hibernate.class table="catissue_synch_cp_audit"
 * @author Pathik Sheth
 */
public class CpSyncAudit extends AbstractDomainObject implements java.io.Serializable
{

	/**
	 * Serial Version Id for the class.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Process identifier.
	 */
	protected Long id;

	/**
	 * Title of the collection protocol.
	 */
	protected Long cpId;

	/**
	 * Status of the process.
	 */
	protected String status;
	
	/**
	 * Count of CPR processed.
	 */
	protected Long processedCPRCount;
	
	
	/**
	 * Start date when last time processes is started. 
	 */
	protected Date startedDate;
	
	/**
	 * End date when last time process is performed.
	 */
	protected Date endDate;
	
	
	protected Long userId;
	
	/**
	 * Return the title of the CP.
	 * @return
	 * 		Title of CP.
	 */
	public Long getCpId() {
		return cpId;
	}
	/**
	 * Set CP title.
	 * @param cpId
	 *          Title of CP.
	 */
	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	/**
	 * Returns the status of the process.
	 * @return
	 *      Status of the process.
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Set status of the process.
	 * @param status
	 *          Status of process.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Count of number of CPR processed.
	 * @return
	 *    Number of CPR processed.
	 */
	public Long getProcessedCPRCount() {
		return processedCPRCount;
	}
	/**
	 * Set number of CPR processed.
	 * @param processedCPRCount
	 * 				Number of CPR processed.
	 */
	public void setProcessedCPRCount(Long processedCPRCount) {
		this.processedCPRCount = processedCPRCount;
	}
	
	/**
	 * Returns the identifier assigned to process.
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * @param identifier Unique identifier to be assigned to the address.
	 */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns the date when process is started.
	 * @return
	 *     Date when process is started.
	 */
	public Date getStartedDate() {
		return startedDate;
	}
	
	/**
	 * Sets date when prosses is started.
	 * @param startedDate
	 */
	public void setStartedDate(Date startedDate) {
		this.startedDate = startedDate;
	}

	/**
	 * Returns the date when last time process ended.
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * Sets the date when last time process ended.
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues
	 * (edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	/**
	 * Set all values.
	 * @param abstractForm IValueObject.
	 */
	@Override
	public void setAllValues(IValueObject abstractForm)
	{
		//
	}
}