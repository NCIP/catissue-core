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
 * Represents the review report parameters 
 * @hibernate.class
 * table="CATISSUE_REVIEW_PARAMS"
 */
public class PathologyReportReviewParameter extends AbstractDomainObject implements java.io.Serializable
{

	/**
	 * Default serial version id for the class
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
	 * Reviewer role 
	 */
	protected String reviewerRole;
	
	
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
	 * Surgical pathology report associated with current review parameter set.  
	 */
	protected SurgicalPathologyReport surgicalPathologyReport;
	
	/**
	 * Review Status
	 */
	protected String status;

	/**
	 * Constructor
	 */
	public PathologyReportReviewParameter()
	{

	}
	/**
	 * 
	 * @return System generated unique id.
	 * @see #setId(Integer)
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_REVIEW_PARAMS_SEQ"
	 */
	public Long getId()
	{
		return id;
	}

	/**
     *@return reviewer role 
     * @hibernate.property  name="reviewerRole"
     * type="string" column="REVIEWER_ROLE"
     * length="100"
     */
	public String getReviewerRole()
	{
		return reviewerRole;
	}

	/**
	 * @param reviewerRole sets the reviewer role.
	 */
	public void setReviewerRole(String reviewerRole)
	{
		this.reviewerRole = reviewerRole;
	}
	
	/**
	 * @return pathology report of the current review parameter.
	 * @hibernate.many-to-one name="surgicalPathologyReport"
	 * class="edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport"
	 * column="REPORT_ID" not-null="false"
	 */
	public SurgicalPathologyReport getSurgicalPathologyReport()
	{
		return surgicalPathologyReport;
	}

	/**
	 * @param surgicalPathologyReport sets surgical pathology report.
	 */
	public void setSurgicalPathologyReport(
			SurgicalPathologyReport surgicalPathologyReport) 
	{
		this.surgicalPathologyReport = surgicalPathologyReport;
	}
	
	/** 
	 * This method sets all values for the PathologyReportReviewParameter object.
	 * @param abstractForm Abstract action form
	 * @throws AssignDataException exception occured while assigning data to form attributes
	 * @see edu.wustl.catissuecore.domain.EventParameters#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
	 * 
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException 
	{
		ViewSurgicalPathologyReportForm form=(ViewSurgicalPathologyReportForm)abstractForm;
    	
		try
		{
			this.setComment(form.getComments());
			this.setTimestamp(new Date());
			this.setStatus(Constants.COMMENT_STATUS_RENDING);
			
			if(!form.getIdentifiedReportId().equals(""))
			{
				IdentifiedSurgicalPathologyReport report = new IdentifiedSurgicalPathologyReport();
				report.setId(new Long(form.getIdentifiedReportId()));
				this.setSurgicalPathologyReport(report);
				
			}
			else if(form.getDeIdentifiedReportId()!=0)
			{
				DeidentifiedSurgicalPathologyReport deidReport = new DeidentifiedSurgicalPathologyReport();
				deidReport.setId(new Long(form.getDeIdentifiedReportId()));
				this.setSurgicalPathologyReport(deidReport);
			}
		}
		catch(Exception ex)
		{
			Logger.out.error(ex.getMessage(),ex);
			ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey,null ,"PathologyReportReviewParameter.java :");
		}
	}
	
	/**
	 * Constructor for the class which takes AbstractActionForm object as an input
	 * @param form AbstractActionForm
	 * @throws AssignDataException object.
	 */
    public PathologyReportReviewParameter(AbstractActionForm form)throws AssignDataException
	{
		setAllValues(form);
	}
    
    /**
     * Returns message label to display on success add or edit
     * @return String
     */
	public String getMessageLabel()
	{
		return (" Pathology Report.");		
	}


	/**
	 * This is the method to get status
     * @return reviewe status 
     * @hibernate.property  name="status"
     * type="string" column="STATUS"
     * length="100"
     */
	public String getStatus()
	{
		return status;
	}


	/**
	 * This is the method to set the review status
	 * @param status of the review parameter
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
}