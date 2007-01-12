package edu.wustl.catissuecore.domain.pathology;

import java.util.Date;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm;
import edu.wustl.catissuecore.domain.EventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.logger.Logger;

/**
 * Represents the review report parameters 
 * @hibernate.class
 * table="CATISSUE_REVIEW_PARAMS"
 */
public class PathologyReportReviewParameter extends EventParameters
{

	/**
	 * Reviewer role 
	 */
	protected String reviewerRole;
	
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
	 * @see edu.wustl.catissuecore.domain.EventParameters#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
	 * This method sets all values for the PathologyReportReviewParameter object.
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException 
	{
		ViewSurgicalPathologyReportForm form=(ViewSurgicalPathologyReportForm)abstractForm;
    	
		try
		{
			DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
			String className;
			String colName=new String(Constants.SYSTEM_IDENTIFIER);
			//PathologyReportReviewParameter reviewParam=new PathologyReportReviewParameter();
			this.setComments(form.getComments());
			this.setTimestamp(new Date());
			
			if(form.getIdentifiedReportId()!=0)
			{
				className=IdentifiedSurgicalPathologyReport.class.getName();
				List isprList=defaultBizLogic.retrieve(className, colName, form.getIdentifiedReportId());
				IdentifiedSurgicalPathologyReport ispr=(IdentifiedSurgicalPathologyReport)isprList.get(0);
				Set pathologyReportReviewParameterSetCollection=ispr.getPathologyReportReviewParameterSetCollection();
				pathologyReportReviewParameterSetCollection.add(this);
				ispr.setPathologyReportReviewParameterSetCollection(pathologyReportReviewParameterSetCollection);
				this.setSurgicalPathologyReport(ispr);
			}
			else if(form.getDeIdentifiedReportId()!=0)
			{
				className=DeidentifiedSurgicalPathologyReport.class.getName();
				List DeisprList=defaultBizLogic.retrieve(className, colName, form.getDeIdentifiedReportId());
				DeidentifiedSurgicalPathologyReport deReport=(DeidentifiedSurgicalPathologyReport)DeisprList.get(0);
				Set pathologyReportReviewParameterSetCollection=deReport.getPathologyReportReviewParameterSetCollection();
				pathologyReportReviewParameterSetCollection.add(this);
				deReport.setPathologyReportReviewParameterSetCollection(pathologyReportReviewParameterSetCollection);
				this.setSurgicalPathologyReport(deReport);
			}
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			throw new AssignDataException();
		}
	}
	
	/**
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
     *@return reviewe status 
     * @hibernate.property  name="status"
     * type="string" column="STATUS"
     * length="100"
     */
	public String getStatus()
	{
		return status;
	}


	/**
	 * @param status sets the review status.
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
}