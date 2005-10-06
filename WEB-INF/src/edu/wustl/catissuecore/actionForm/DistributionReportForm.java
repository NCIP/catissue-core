
package edu.wustl.catissuecore.actionForm;


import java.util.Calendar;

import org.apache.struts.action.ActionForm;

import edu.wustl.catissuecore.action.LoginAction;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.logger.Logger;

/**
  *
 * Description:  This Class handles the Distribution Report Data..
 */
public class DistributionReportForm extends ActionForm
{
	
	/**
     * System generated unique identifier.
     * */
	private long systemIdentifier;
    
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


	private String fromSite;
	private String toSite;
	private String distributionProtocolTitle;
	private Long distributionId;

	
 	
	
	public void setAllValues(Distribution distribution) throws Exception
	{
		this.distributionProtocolTitle = String.valueOf(distribution.getDistributionProtocol().getTitle());
		gov.nih.nci.security.authorization.domainobjects.User userData = SecurityManager.getInstance(
				LoginAction.class).getUserById(distribution.getUser().getSystemIdentifier().toString());
		String lName = userData.getLastName();
 		String fName = userData.getFirstName();
 		Logger.out.debug("User's name"+lName+" "+fName );
 		this.userName = lName + ", " + fName;
 		Calendar calender = Calendar.getInstance();
 		calender.setTime(distribution.getTimestamp());
 		this.timeInHours = Utility.toString(Integer.toString( calender.get(Calendar.HOUR_OF_DAY)));
 		this.timeInMinutes = Utility.toString(Integer.toString(calender.get(Calendar.MINUTE)));
 		this.dateOfEvent = Utility.parseDateToString(distribution.getTimestamp(),Constants.DATE_PATTERN_MM_DD_YYYY);
 		this.systemIdentifier = distribution.getSystemIdentifier().longValue() ;
		this.fromSite = String.valueOf(distribution.getFromSite().getName());
		this.toSite = String.valueOf(distribution.getToSite().getName());
		this.comments  = Utility.toString(distribution.getComments());
		distributionId = distribution.getSystemIdentifier();
		
	}
	
	/**
	 * @return fromSite
	 */ 
	public String getFromSite() {
		return fromSite;
	}

	/**
	 * @param fromSite
	 */
	public void setFromSite(String fromSite) {
		this.fromSite = fromSite;
	}

	public String getToSite() {
		return toSite;
	}
	/**
	 * @return Returns the distributionProtocolTitle.
	 */
	public String getDistributionProtocolTitle() {
		return distributionProtocolTitle;
	}
	/**
	 * @param distributionProtocolTitle The distributionProtocolTitle to set.
	 */
	public void setDistributionProtocolTitle(String distributionProtocolTitle) {
		this.distributionProtocolTitle = distributionProtocolTitle;
	}
	/**
	 * @param toSite The toSite to set.
	 */
	public void setToSite(String toSite) {
		this.toSite = toSite;
	}

	/**
	 * @return Returns the comments.
	 */
	public String getComments()
	{
		return comments;
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
		return dateOfEvent;
	}
	
	/**
	 * @param dateOfEvent The dateOfEvent to set.
	 */
	public void setDateOfEvent(String dateOfEvent)
	{
		this.dateOfEvent = dateOfEvent;
	}
	
	/**
	 * @return Returns the time_InMinutes.
	 */
	public String getTimeInMinutes()
	{
		return timeInMinutes;
	}
	
	/**
	 * @param time_InMinutes The time_InMinutes to set.
	 */
	public void setTimeInMinutes(String time_InMinutes)
	{
		this.timeInMinutes = time_InMinutes;
	}
		
	/**
	 * @return Returns the timeStamp.
	 */
	public String getTimeInHours()
	{
		return timeInHours;
	}
	
	/**
	 * @param timeStamp The timeStamp to set.
	 */
	public void setTimeInHours(String timeStamp)
	{
		this.timeInHours = timeStamp;
	}
	
	/**
	 * @return Returns the userId.
	 */
	public String getUserName()
	{
		return userName;
	}
	
	/**
	 * @param userId The userId to set.
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}    
	
	public long getSystemIdentifier()
	{
		return this.systemIdentifier ;
	}

	public void setSystemIdentifier(long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier ; 
	}
	/**
	 * @return Returns the distributionId.
	 */
	public Long getDistributionId() {
		return distributionId;
	}
	/**
	 * @param distributionId The distributionId to set.
	 */
	public void setDistributionId(Long distributionId) {
		this.distributionId = distributionId;
	}


}