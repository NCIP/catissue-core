
package edu.wustl.catissuecore.domain.pathology;

import java.util.Date;

import edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * Represents the review report parameters.
 * @hibernate.class
 * table="CATISSUE_REVIEW_PARAMS"
 */
public class PathologyReportReviewParameter extends AbstractDomainObject
		implements
			java.io.Serializable
{

	/**
	 * Default serial version id for the class.
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
	 * Reviewer role.
	 */
	protected String reviewerRole;

	/**
	 * @return Date
	 */
	public Date getTimestamp()
	{
		return this.timestamp;
	}

	/**
	 * @param timestamp : timestamp
	 */
	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}

	/**
	 * @return User
	 */
	public User getUser()
	{
		return this.user;
	}

	/**
	 * @param user : user
	 */
	public void setUser(User user)
	{
		this.user = user;
	}

	/**
	 * @return String
	 */
	public String getComment()
	{
		return this.comment;
	}

	/**
	 * @param comment : comment
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * @param identifier : id
	 */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Surgical pathology report associated with current review parameter set.
	 */
	protected SurgicalPathologyReport surgicalPathologyReport;

	/**
	 * Review Status.
	 */
	protected String status;

	/**
	 * Constructor.
	 */
	public PathologyReportReviewParameter()
	{
		super();
	}

	/**
	 * @return System generated unique id.
	 * @see #setId(Integer)
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_REVIEW_PARAMS_SEQ"
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 *@return reviewer role
	 * @hibernate.property  name="reviewerRole"
	 * type="string" column="REVIEWER_ROLE"
	 * length="100"
	 */
	public String getReviewerRole()
	{
		return this.reviewerRole;
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
		return this.surgicalPathologyReport;
	}

	/**
	 * @param surgicalPathologyReport sets surgical pathology report.
	 */
	public void setSurgicalPathologyReport(SurgicalPathologyReport surgicalPathologyReport)
	{
		this.surgicalPathologyReport = surgicalPathologyReport;
	}

	/**
	 * This method sets all values for the PathologyReportReviewParameter object.
	 * @param abstractForm Abstract action form
	 * @throws AssignDataException exception occured while assigning data to form attributes
	 * @see edu.wustl.catissuecore.domain.EventParameters
	 * #setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
	 */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		final ViewSurgicalPathologyReportForm form = (ViewSurgicalPathologyReportForm) abstractForm;

		this.setComment(form.getComments());
		this.setTimestamp(new Date());
		this.setStatus(Constants.COMMENT_STATUS_RENDING);

		if (!form.getIdentifiedReportId().equals(""))
		{
			final IdentifiedSurgicalPathologyReport report = new IdentifiedSurgicalPathologyReport();
			report.setId(Long.valueOf(form.getIdentifiedReportId()));
			this.setSurgicalPathologyReport(report);

		}
		else if (form.getDeIdentifiedReportId() != 0)
		{
			final DeidentifiedSurgicalPathologyReport deidReport = new DeidentifiedSurgicalPathologyReport();
			deidReport.setId(Long.valueOf(form.getDeIdentifiedReportId()));
			this.setSurgicalPathologyReport(deidReport);
		}

	}

	/**
	 * Constructor for the class which takes AbstractActionForm object as an input.
	 * @param form AbstractActionForm
	 * @throws AssignDataException object.
	 */
	public PathologyReportReviewParameter(AbstractActionForm form) throws AssignDataException
	{
		super();
		this.setAllValues(form);
	}

	/**
	 * Returns message label to display on success add or edit.
	 * @return String
	 */
	@Override
	public String getMessageLabel()
	{
		return " Pathology Report.";
	}

	/**
	 * This is the method to get status.
	 * @return reviewe status
	 * @hibernate.property  name="status"
	 * type="string" column="STATUS"
	 * length="100"
	 */
	public String getStatus()
	{
		return this.status;
	}

	/**
	 * This is the method to set the review status.
	 * @param status of the review parameter
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
}