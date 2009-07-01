/**
 * <p>Title: ConflictDetailsForm Class>
 * <p>Description:	This class contains attributes to display on ConflictDetailsView.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author kalpana Thakur
 * @version 1.00
 * Created on sep 18,2007
 */

package edu.wustl.catissuecore.actionForm;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author renuka_bajpai
 *
 */
public class ConflictDetailsForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

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
		return 0; //Constants.CONFLICT_DETAILS_FORM_ID;
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
	public void setReportQueueId(final String reportQueueId)
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
	public void setParticipantId(final String participantId)
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
	public void setScgId(final String scgId)
	{
		this.scgId = scgId;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}
