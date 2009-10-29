
package edu.wustl.catissuecore.actionForm;

import java.util.Calendar;

import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;

/**
  *
 * Description:  This Class handles the Distribution Report Data..
 */
public class DistributionReportForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;
	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(DistributionReportForm.class);
	/**
	 * System generated unique identifier.
	 * */
	private long id;

	/**
	 * Time in hours for the Event Parameter.
	 * */
	private String timeInHours;

	/**
	 * Time in minutes for the Event Parameter.
	 * */
	private String timeInMinutes;

	/**
	 * Date of the Event Parameter.
	 * */
	private String dateOfEvent;
	/**
	  * Id of the User.   
	  */
	private String userName;

	/**
	 * Comments on the event parameter.   
	 */
	private String comments;

	//private String fromSite;
	private String toSite;
	private String distributionProtocolTitle;
	private Long distributionId;

	private Integer distributionType = Integer.valueOf(Constants.SPECIMEN_DISTRIBUTION_TYPE);

	/**
	 * @return distributionType Return type of Distribution
	 */
	public Integer getDistributionType()
	{
		return this.distributionType;
	}

	/**
	 * @param distributionType Setting Distribution Type
	 */
	public void setDistributionType(Integer distributionType)
	{
		this.distributionType = distributionType;
	}

	/**
	 * @param abstractDomainObject An AbstractDomainObject obj
	 */
	public void setAllValues(AbstractDomainObject abstractDomainObject)
	{

		final Distribution distribution = (Distribution) abstractDomainObject;
		this.distributionProtocolTitle = String.valueOf(distribution.getDistributionProtocol()
				.getTitle());
		final String lName = distribution.getDistributedBy().getLastName();
		final String fName = distribution.getDistributedBy().getFirstName();
		logger.debug("User's name" + lName + " " + fName);
		this.userName = lName + ", " + fName;
		final Calendar calender = Calendar.getInstance();
		calender.setTime(distribution.getTimestamp());
		this.timeInHours = CommonUtilities.toString(Integer.toString(calender
				.get(Calendar.HOUR_OF_DAY)));
		this.timeInMinutes = CommonUtilities.toString(Integer.toString(calender
				.get(Calendar.MINUTE)));
		this.dateOfEvent = CommonUtilities.parseDateToString(distribution.getTimestamp(),
				CommonServiceLocator.getInstance().getDatePattern());
		this.id = distribution.getId().longValue();
		//this.fromSite = String.valueOf(distribution.getFromSite().getName());
		this.toSite = String.valueOf(distribution.getToSite().getName());
		this.comments = CommonUtilities.toString(distribution.getComment());
		this.distributionId = distribution.getId();
	}

	//	/**
	//	 * @return fromSite
	//	 */ 
	//	public String getFromSite() {
	//		return fromSite;
	//	}
	//
	//	/**
	//	 * @param fromSite
	//	 */
	//	public void setFromSite(String fromSite) {
	//		this.fromSite = fromSite;
	//	}

	/**
	 * @return toSite Return Site
	 */
	public String getToSite()
	{
		return this.toSite;
	}

	/**
	 * @return Returns the distributionProtocolTitle.
	 */
	public String getDistributionProtocolTitle()
	{
		return this.distributionProtocolTitle;
	}

	/**
	 * @param distributionProtocolTitle The distributionProtocolTitle to set.
	 */
	public void setDistributionProtocolTitle(String distributionProtocolTitle)
	{
		this.distributionProtocolTitle = distributionProtocolTitle;
	}

	/**
	 * @param toSite The toSite to set.
	 */
	public void setToSite(String toSite)
	{
		this.toSite = toSite;
	}

	/**
	 * @return Returns the comments.
	 */
	public String getComments()
	{
		return this.comments;
	}

	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}

	/**
	 * @return Returns the dateOfEvent.
	 */
	public String getDateOfEvent()
	{
		return this.dateOfEvent;
	}

	/**
	 * @param dateOfEvent The dateOfEvent to set.
	 */
	public void setDateOfEvent(String dateOfEvent)
	{
		this.dateOfEvent = dateOfEvent;
	}

	/**
	 * @return timeInMinutes Returns the time_InMinutes.
	 */
	public String getTimeInMinutes()
	{
		return this.timeInMinutes;
	}

	/**
	 * @param time_InMinutes The time_InMinutes to set.
	 */
	public void setTimeInMinutes(String time_InMinutes)
	{
		this.timeInMinutes = time_InMinutes;
	}

	/**
	 * @return timeInHours Returns the timeStamp.
	 */
	public String getTimeInHours()
	{
		return this.timeInHours;
	}

	/**
	 * @param timeStamp The timeStamp to set.
	 */
	public void setTimeInHours(String timeStamp)
	{
		this.timeInHours = timeStamp;
	}

	/**
	 * @return userName Returns the userId.
	 */
	public String getUserName()
	{
		return this.userName;
	}

	/**
	 * @param userName The userId to set.
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	/**
	 *@return id
	 */
	@Override
	public long getId()
	{
		return this.id;
	}

	/**
	 * @param id Setting id
	 */
	@Override
	public void setId(long id)
	{
		this.id = id;
	}

	/**
	 * @return distributionId Returns the distributionId.
	 */
	public Long getDistributionId()
	{
		return this.distributionId;
	}

	/**
	 * @param distributionId The distributionId to set.
	 */
	public void setDistributionId(Long distributionId)
	{
		this.distributionId = distributionId;
	}

	/**
	 * @return 0
	 */
	@Override
	public int getFormId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Resets the values of all the fields.
	 * This method defined in ActionForm is overridden in this class.
	 */
	@Override
	protected void reset()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}
}