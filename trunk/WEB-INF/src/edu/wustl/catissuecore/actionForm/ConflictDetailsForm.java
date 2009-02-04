/**
 * <p>Title: ConflictViewForm Class>
 * <p>Description:	This class contains attributes to display on ConflictDetails.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Feb 27,2007
 */
package edu.wustl.catissuecore.actionForm;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;


/**
 * @author ashish_gupta
 *
 */
public class ConflictDetailsForm extends AbstractActionForm
{
	/**
	 * 
	 */
	protected String participantId;
	/**
	 * 
	 */
	protected String scgId;	
	/**
	 * The report queue Id whose details we have to display
	 */
	protected String reportQueueId;

	public int getFormId()
	{		
		return Constants.CONFLICT_DETAILS_FORM_ID;
	}

	protected void reset()
	{		
	}

	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		
	}

	
	/**
	 * @return the reportQueueId
	 */
	public String getReportQueueId()
	{
		return reportQueueId;
	}

	
	/**
	 * @param reportQueueId the reportQueueId to set
	 */
	public void setReportQueueId(String reportQueueId)
	{
		this.reportQueueId = reportQueueId;
	}

	
	/**
	 * @return the participantId
	 */
	public String getParticipantId()
	{
		return participantId;
	}

	
	/**
	 * @param participantId the participantId to set
	 */
	public void setParticipantId(String participantId)
	{
		this.participantId = participantId;
	}

	
	/**
	 * @return the scgId
	 */
	public String getScgId()
	{
		return scgId;
	}

	
	/**
	 * @param scgId the scgId to set
	 */
	public void setScgId(String scgId)
	{
		this.scgId = scgId;
	}

}
