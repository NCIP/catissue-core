
package edu.wustl.catissuecore.bizlogic.bulkOperations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.bizlogic.SpecimenEventParametersBizLogic;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;

/**
 * @author janhavi_hasabnis
 *
 */
public class BulkOperationsBizlogic extends SpecimenEventParametersBizLogic
{

	private transient final Logger logger = Logger.getCommonLogger(BulkOperationsBizlogic.class);

	/**
	 * @param operation - operation
	 * @param sessionDataBean - SessionDataBean obj
	 * @param specimenIds - list of specimen ids
	 * @param userId - user Id
	 * @param date - date
	 * @param timeInHours - timeInHours
	 * @param timeInseconds - timeInHours
	 * @param comments - comments
	 * @param eventSpecificData - map of eventSpecificData
	 * @throws BizLogicException - BizLogicException
	 */
	public void insertEvents(String operation, SessionDataBean sessionDataBean, List specimenIds,
			Long userId, String date, String timeInHours, String timeInseconds, String comments,
			Map<String, String> eventSpecificData) throws BizLogicException
	{
		try
		{
			final List eventObjects = this.getEventDomainObjects(operation, specimenIds, userId,
					date, timeInHours, timeInseconds, comments, eventSpecificData);
			// IBizLogic bizLogic =
			// BizLogicFactory.getInstance().getBizLogic(Constants
			// .TRANSFER_EVENT_PARAMETERS_FORM_ID);
			this.insert(eventObjects, sessionDataBean, 0);

		}
		catch (final ParseException exp)
		{
			this.logger.debug(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, "db.date.parse.error", "");
		}

	}

	/**
	 * @param operation - operation
	 * @param specimenIds - list of specimen ids
	 * @param userId - user Id
	 * @param dateOfEvent - date of event
	 * @param timeInHours - timeInHours
	 * @param timeInseconds - timeInseconds
	 * @param comments - comments
	 * @param eventSpecificData - map of eventSpecificData
	 * @return - list of events
	 * @throws ParseException - ParseException
	 */

	private List getEventDomainObjects(String operation, List specimenIds, Long userId,
			String dateOfEvent, String timeInHours, String timeInseconds, String comments,
			Map<String, String> eventSpecificData) throws ParseException
	{
		List events;
		if (operation.equals(Constants.BULK_TRANSFERS))
		{
			events = this.getTransferEventObjects(specimenIds, userId, dateOfEvent, timeInHours,
					timeInseconds, comments, eventSpecificData);
		}
		else
		{
			events = this.getDisposalEventObjects(specimenIds, userId, dateOfEvent, timeInHours,
					timeInseconds, comments, eventSpecificData);
		}
		return events;
	}

	/**
	 * @param specimenIds - specimenIds
	 * @param userId - userId
	 * @param dateOfEvent - dateOfEvent
	 * @param timeInHours - timeInHours
	 * @param timeInseconds - timeInseconds
	 * @param comments - comments
	 * @param eventSpecificData - eventSpecificData
	 * @return List
	 * @throws ParseException - ParseException
	 */
	private List getTransferEventObjects(List specimenIds, Long userId, String dateOfEvent,
			String timeInHours, String timeInseconds, String comments,
			Map<String, String> eventSpecificData) throws ParseException
	{
		final List<TransferEventParameters> events = new ArrayList<TransferEventParameters>();
		TransferEventParameters transferEventParameters = null;
		StorageContainer fromContainer = null;
		StorageContainer toContainer = null;
		String specimenId = null;
		for (int i = 0; i < specimenIds.size(); i++)
		{
			transferEventParameters = new TransferEventParameters();
			specimenId = (String) specimenIds.get(i);
			this.setCommonEventParameters(specimenId, userId, dateOfEvent, timeInHours,
					timeInseconds, comments, transferEventParameters, i);

			fromContainer = new StorageContainer();
			if (eventSpecificData.get("ID_" + specimenId + "_FROMLOCID").equals(""))
			{
				transferEventParameters.setFromPositionDimensionOne(null);
				transferEventParameters.setFromPositionDimensionTwo(null);
				transferEventParameters.setFromStorageContainer(null);

			}
			else
			{
				fromContainer.setId(new Long(eventSpecificData.get("ID_" + specimenId
						+ "_FROMLOCID")));
				transferEventParameters.setFromPositionDimensionOne(Integer
						.valueOf(eventSpecificData.get("ID_" + specimenId + "_FROMLOCPOS1")));
				transferEventParameters.setFromPositionDimensionTwo(Integer
						.valueOf(eventSpecificData.get("ID_" + specimenId + "_FROMLOCPOS2")));
				transferEventParameters.setFromStorageContainer(fromContainer);
			}

			toContainer = new StorageContainer();
			toContainer.setName(eventSpecificData.get("ID_" + specimenId + "_TOSCLABEL"));
			transferEventParameters.setToStorageContainer(toContainer);

			events.add(transferEventParameters);
		}
		return events;
	}

	/**
	 * @param specimenId - specimenId
	 * @param userId - userId
	 * @param dateOfEvent - dateOfEvent
	 * @param timeInHours - timeInHours
	 * @param timeInseconds - timeInseconds
	 * @param comments - comments
	 * @param eventParameters - eventParameters
	 * @param i - integer
	 * @throws ParseException - ParseException
	 */
	private void setCommonEventParameters(String specimenId, Long userId, String dateOfEvent,
			String timeInHours, String timeInseconds, String comments,
			SpecimenEventParameters eventParameters, int i) throws ParseException
	{
		User user;
		Specimen specimen;
		user = new User();
		user.setId(userId);

		eventParameters.setComment(comments);
		eventParameters.setUser(user);

		specimen = new Specimen();
		specimen.setId(new Long(specimenId));
		eventParameters.setSpecimen(specimen);

		final Calendar calendar = Calendar.getInstance();
		final Date date = CommonUtilities.parseDate(dateOfEvent, CommonUtilities
				.datePattern(dateOfEvent));
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeInHours));
		calendar.set(Calendar.MINUTE, Integer.parseInt(timeInseconds));
		eventParameters.setTimestamp(calendar.getTime());
	}

	/**
	 * @param specimenIds - specimenIds
	 * @param userId - userId
	 * @param dateOfEvent - dateOfEvent
	 * @param timeInHours - timeInHours
	 * @param timeInseconds - timeInseconds
	 * @param comments - comments
	 * @param eventSpecificData - eventSpecificData
	 * @return List
	 * @throws ParseException - ParseException
	 */
	private List getDisposalEventObjects(List specimenIds, Long userId, String dateOfEvent,
			String timeInHours, String timeInseconds, String comments,
			Map<String, String> eventSpecificData) throws ParseException
	{
		final List<DisposalEventParameters> events = new ArrayList<DisposalEventParameters>();
		DisposalEventParameters disposalEventParameters = null;
		String specimenId = null;
		for (int i = 0; i < specimenIds.size(); i++)
		{
			disposalEventParameters = new DisposalEventParameters();
			specimenId = (String) specimenIds.get(i);
			this.setCommonEventParameters(specimenId, userId, dateOfEvent, timeInHours,
					timeInseconds, comments, disposalEventParameters, i);
			disposalEventParameters.setActivityStatus(eventSpecificData.get("ID_ALL_STATUS"));
			disposalEventParameters.setReason(eventSpecificData.get("ID_ALL_REASON"));
			events.add(disposalEventParameters);
		}
		return events;
	}

}
