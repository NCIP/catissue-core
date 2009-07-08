/**
 * <p>Title: SpecimenProtocolBizLogic Class>
 * <p>Description:	SpecimenProtocolBizLogic is a class which contains the common moethods requird for 
 * Collection Protocol and Distribution Protocol.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on April 06, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.wustl.catissuecore.domain.SpecimenProtocol;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;

/**
 * SpecimenProtocolBizLogic is a class which contains the common moethods requird for
 * Collection Protocol and Distribution Protocol.
 * @author gautam_shetty
 */
public class SpecimenProtocolBizLogic extends CatissueDefaultBizLogic
{

	/**
	 * Logger object.
	 */
	private transient final Logger logger = Logger.getCommonLogger(SpecimenProtocolBizLogic.class);

	/**
	 * This method checks for the change in the Activity status of the object. If change is found
	 *  then it calls the setClosedDate() to update the End date.
	 * @param newObject Object representing the current data.
	 * @param oldObject Object before the changes.
	 */
	protected void checkForChangedStatus(SpecimenProtocol newObject, SpecimenProtocol oldObject)
	{
		this.logger.debug("newObject.getActivityStatus() : " + newObject.getActivityStatus());
		this.logger.debug("oldObject.getActivityStatus()   " + oldObject.getActivityStatus());

		if (!newObject.getActivityStatus().equals(oldObject.getActivityStatus()))
		{
			this.setClosedDate(newObject);
		}
	}

	/**
	 * @author mandar_deshmukh.
	 * This method is used for setting the Stop / End date for the Protocol.
	 * @param protocol The domain object whose date is to be set
	 */
	private void setClosedDate(SpecimenProtocol protocol)
	{
		final String activityStatus = protocol.getActivityStatus();
		this.logger.debug("in setClosedDate of DBZL, ActivityStatus  : " + activityStatus);
		if (activityStatus.equalsIgnoreCase(Status.ACTIVITY_STATUS_CLOSED.toString()))
		{
			final Date currentDate = Calendar.getInstance().getTime();
			protocol.setEndDate(currentDate);
			this.logger.debug("EndDate set");
		}
		else if (activityStatus.equalsIgnoreCase(Status.ACTIVITY_STATUS_ACTIVE.toString()))
		{
			protocol.setEndDate(null);
			this.logger.debug("EndDate cleared");
		}
	}

	/**
	 * @param specimenProtocolIdentifier ID of the specimen protocol whose end date is to be searched.
	 * @param sessionBean Object of SessionDataBean
	 * @return end date of the specimen protocol referred by the given ID. Empty string if enddate is null.
	 * @throws BizLogicException throws BizLogicException
	 */
	public String getEndDate(long specimenProtocolIdentifier, SessionDataBean sessionBean)
			throws BizLogicException
	{
		String endDate = "";
		final String hqlQuery = "select endDate from edu.wustl.catissuecore.domain.SpecimenProtocol where id="
				+ specimenProtocolIdentifier;
		final List endDateList = this.executeQuery(hqlQuery);

		if (endDateList != null && !endDateList.isEmpty())
		{
			final Date tmpDate = (Date) endDateList.get(0);
			endDate = CommonUtilities.parseDateToString(tmpDate, CommonServiceLocator.getInstance()
					.getDatePattern());
		}

		return endDate;
	}
}
