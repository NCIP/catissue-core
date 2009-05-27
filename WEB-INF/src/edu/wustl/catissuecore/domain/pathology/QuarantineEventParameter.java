package edu.wustl.catissuecore.domain.pathology;
import java.util.Date;

import edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

/**
 * Reprtesents quarantine report parameters
 * @hibernate.class
 * table="CATISSUE_QUARANTINE_PARAMS"
 */
public class QuarantineEventParameter extends AbstractDomainObject implements java.io.Serializable
{

	/**
	 * Serial version id of the class
	 */
	private static final long serialVersionUID = 1L;

	/**
     * System generated unique id.
     */
	protected Long id;
	
	/**
     * Date and time of the event.
     */
	protected Date timestamp;
	
	/**
     * User who performs the event.
     */
	protected User user;
	
	/**
     * Text comment on event.
     */
	protected String comment;
	
	/**
	 * quarantine status of the associated deidentified report. 
	 */
	protected Boolean quarantineStatus;
	
	/**
	 * de-identified surgical pthology report.
	 */
	protected DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport;
	
	/**
	 * Quarantine comment status of de-identified surgical pthology report.
	 */
	protected String status;

	/**
	 * Constructor
	 */
	public QuarantineEventParameter()
	{

	}
	
	/**
     * 
     * @return System generated unique id.
     * @see #setId(Integer)
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_QUARANTINE_PARAMS_SEQ" 
     */
	public Long getId()
	{
		return id;
	}
	
	
	/**
	 *  @return deidentified pathology report.
	 * 	@hibernate.many-to-one 	name="deidentifiedSurgicalPathologyReport"
	 * 	class="edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport"
	 * 	column="DEID_REPORT_ID" not-null="false"
	 */
	public DeidentifiedSurgicalPathologyReport getDeIdentifiedSurgicalPathologyReport()
	{
		return deIdentifiedSurgicalPathologyReport;
	}
	/**
	 * @param deidentifiedSurgicalPathologyReport sets deidentified pathology report.
	 */
	
	public void setDeIdentifiedSurgicalPathologyReport(
			DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport)
	{
		this.deIdentifiedSurgicalPathologyReport = deIdentifiedSurgicalPathologyReport;
	}
	
	 
	public Date getTimestamp()
	{
		return timestamp;
	}

	
	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}

	
	public User getUser()
	{
		return user;
	}

	
	public void setUser(User user)
	{
		this.user = user;
	}

	
	public String getComment()
	{
		return comment;
	}

	
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	  *  @return quarantine status of the deidentified pathology report
	  *  @hibernate.property  name="quarantineStatus"
	  *  type="boolean" column="IS_QUARANTINED"
	  */
	
	public Boolean getQuarantineStatus()
	{
		return quarantineStatus;
	}

	/**
	 * @param quarantineStatus sets quarantine status of the pathology report.
	 */
	public void setQuarantineStatus(Boolean quarantineStatus) 
	{
		this.quarantineStatus = quarantineStatus;
	}
	
	/** 
	 * This method sets all values for the QuarantineEventParameter object.
	 * @param abstractForm Abstract action form
	 * @throws AssignDataException exception occured while assigning data to form attributes
	 * @see edu.wustl.catissuecore.domain.EventParameters#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
	 * 
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException 
	{
		ViewSurgicalPathologyReportForm form=(ViewSurgicalPathologyReportForm)abstractForm;
    	
			this.setComment(form.getComments());
			this.setTimestamp(new Date());
			this.setStatus(Constants.COMMENT_STATUS_RENDING);
			if(form.getAcceptReject()==1)
			{
				this.setStatus(Constants.COMMENT_STATUS_QUARANTINED);
			}
			
			if(form.getAcceptReject()==2)
			{
				this.setStatus(Constants.COMMENT_STATUS_NOT_QUARANTINED);
			}
			if(form.getDeIdentifiedReportId()!=0)
			{
				DeidentifiedSurgicalPathologyReport deidReport = new DeidentifiedSurgicalPathologyReport();
				deidReport.setId(new Long(form.getDeIdentifiedReportId()));
				this.setDeIdentifiedSurgicalPathologyReport(deidReport);
				this.setQuarantineStatus(false);
			}
		
	}
	
	/**
	 * @param form AbstractActionForm
	 * @throws AssignDataException object.
	 */
    public QuarantineEventParameter(AbstractActionForm form)throws AssignDataException
	{
		setAllValues(form);
	}
    
    /**
     * Returns message label to display on success add or edit
     * @return String
     */
	public String getMessageLabel()
	{
		return (" De-Identified Report.");
		
	}

	/**
     * @return quarantine comment status 
     * @hibernate.property  name="status"
     * type="string" column="STATUS"
     * length="100"
     */
	public String getStatus()
	{
		return status;
	}

	/**
     * Returns quarantine comment status.
     * @param status of comment
     */
	public void setStatus(String status)
	{
		this.status = status;
	}
}