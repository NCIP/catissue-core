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
 * Reprtesents quarantine report parameters
 * @hibernate.class
 * table="CATISSUE_QUARANTINE_PARAMS"
 */
public class QuarantineEventParameterSet extends EventParameters
{

	/**
	 * quarantine status of the associated deidentified report. 
	 */
	protected Boolean quarantineStatus;
	
	/**
	 * de-identified surgical pthology report.
	 */
	protected DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport;

	/**
	 * Constructor
	 */
	public QuarantineEventParameterSet()
	{

	}
	
	/**
     * 
     * @return System generated unique id.
     * @see #setId(Integer)
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_QUARANTINE_EVENT_PARAM_SEQ" 
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
	public DeidentifiedSurgicalPathologyReport getDeidentifiedSurgicalPathologyReport()
	{
		return deidentifiedSurgicalPathologyReport;
	}

	/**
	 * @param deidentifiedSurgicalPathologyReport sets deidentified pathology report.
	 */
	public void setDeidentifiedSurgicalPathologyReport(
			DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport)
	{
		this.deidentifiedSurgicalPathologyReport = deidentifiedSurgicalPathologyReport;
	}

	 /**
	  * @return quarantine status of the deidentified pathology report
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
	 * @see edu.wustl.catissuecore.domain.EventParameters#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
	 * This method sets all values for the QuarantineEventParameter object.
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException 
	{
		ViewSurgicalPathologyReportForm form=(ViewSurgicalPathologyReportForm)abstractForm;
    	
		try
		{
			DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
			String className;
			String colName=new String(Constants.SYSTEM_IDENTIFIER);
			this.setComments(form.getComments());
			this.setTimestamp(new Date());
			
			if(form.getDeIdentifiedReportId()!=0)
			{
				className=DeidentifiedSurgicalPathologyReport.class.getName();
				List deReportList=defaultBizLogic.retrieve(className, colName, form.getDeIdentifiedReportId());
				DeidentifiedSurgicalPathologyReport deReport=(DeidentifiedSurgicalPathologyReport)deReportList.get(0);
				Set quarantineEventParameterSetCollection=deReport.getQuarantinEventParameterSetCollection();
				quarantineEventParameterSetCollection.add(this);
				deReport.setQuarantinEventParameterSetCollection(quarantineEventParameterSetCollection);
				this.setDeidentifiedSurgicalPathologyReport(deReport);
				this.setQuarantineStatus(false);
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
    public QuarantineEventParameterSet(AbstractActionForm form)throws AssignDataException
	{
		setAllValues(form);
	}
}