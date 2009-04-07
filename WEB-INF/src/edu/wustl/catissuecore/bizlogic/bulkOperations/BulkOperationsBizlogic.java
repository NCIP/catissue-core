
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
import edu.wustl.common.util.Utility;
import edu.wustl.security.exception.UserNotAuthorizedException;

public class BulkOperationsBizlogic extends SpecimenEventParametersBizLogic
{
	public void insertEvents(String operation, SessionDataBean sessionDataBean, List specimenIds, Long userId, String date, String timeInHours,
			String timeInseconds, String comments, Map<String, String> eventSpecificData) throws BizLogicException
	{
		try
		{
			List eventObjects = getEventDomainObjects(operation, specimenIds, userId, date, timeInHours, timeInseconds, comments, eventSpecificData);
			//IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.TRANSFER_EVENT_PARAMETERS_FORM_ID);
			insert(eventObjects, sessionDataBean, 0);

		}
		catch (ParseException exp)
		{
			throw getBizLogicException(exp, "bizlogic.error", "");
		}

	}

	private List getEventDomainObjects(String operation, List specimenIds, Long userId, String dateOfEvent, String timeInHours, String timeInseconds, String comments,
			Map<String, String> eventSpecificData) throws ParseException
	{
		List events;
		if(operation.equals(Constants.BULK_TRANSFERS))
		{
			events = getTransferEventObjects(specimenIds, userId, dateOfEvent, timeInHours, timeInseconds, comments, eventSpecificData);
		}
		else
		{
			events = getDisposalEventObjects(specimenIds, userId, dateOfEvent, timeInHours, timeInseconds, comments, eventSpecificData);
		}
		return events;
	}

	private List getTransferEventObjects(List specimenIds, Long userId, String dateOfEvent, String timeInHours, String timeInseconds, String comments, Map<String, String> eventSpecificData) throws ParseException
	{
		List<TransferEventParameters> events = new ArrayList<TransferEventParameters>();
		TransferEventParameters transferEventParameters = null;
		StorageContainer fromContainer = null;
		StorageContainer toContainer = null;
		String specimenId = null;
		Specimen specimen = null;
		for (int i = 0; i < specimenIds.size(); i++)
		{
			transferEventParameters = new TransferEventParameters();
			specimenId = (String) specimenIds.get(i); 
			setCommonEventParameters(specimenId, userId, dateOfEvent, timeInHours, timeInseconds, comments, transferEventParameters, i);
			
			fromContainer = new StorageContainer();
			if (eventSpecificData.get("ID_" + specimenId + "_FROMLOCID").equals(""))
			{
				transferEventParameters.setFromPositionDimensionOne(null);
				transferEventParameters.setFromPositionDimensionTwo(null);
				transferEventParameters.setFromStorageContainer(null);

			}
			else
			{
				fromContainer.setId(new Long(eventSpecificData.get("ID_" + specimenId + "_FROMLOCID")));
				transferEventParameters.setFromPositionDimensionOne(Integer.valueOf(eventSpecificData.get("ID_" + specimenId + "_FROMLOCPOS1")));
				transferEventParameters.setFromPositionDimensionTwo(Integer.valueOf(eventSpecificData.get("ID_" + specimenId + "_FROMLOCPOS2")));
				transferEventParameters.setFromStorageContainer(fromContainer);
			}

			toContainer = new StorageContainer();
			toContainer.setName(eventSpecificData.get("ID_" + specimenId + "_TOSCLABEL"));
			transferEventParameters.setToStorageContainer(toContainer);

			events.add(transferEventParameters);
		}
		return events;
	}

	private void setCommonEventParameters(String specimenId, Long userId, String dateOfEvent, String timeInHours, String timeInseconds, String comments, SpecimenEventParameters eventParameters, int i) throws ParseException
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

		Calendar calendar = Calendar.getInstance();
		Date date = Utility.parseDate(dateOfEvent, Utility.datePattern(dateOfEvent));
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeInHours));
		calendar.set(Calendar.MINUTE, Integer.parseInt(timeInseconds));
		eventParameters.setTimestamp(calendar.getTime());
	}
	
	private List getDisposalEventObjects(List specimenIds, Long userId, String dateOfEvent, String timeInHours, String timeInseconds, String comments, Map<String, String> eventSpecificData) throws ParseException
	{
		List<DisposalEventParameters> events = new ArrayList<DisposalEventParameters>();
		DisposalEventParameters disposalEventParameters = null;
		String specimenId = null;
		Specimen specimen = null;
		for (int i = 0; i < specimenIds.size(); i++)
		{
			disposalEventParameters = new DisposalEventParameters();
			specimenId = (String) specimenIds.get(i); 
			setCommonEventParameters(specimenId, userId, dateOfEvent, timeInHours, timeInseconds, comments, disposalEventParameters, i);
			disposalEventParameters.setActivityStatus(eventSpecificData.get("ID_ALL_STATUS"));
			disposalEventParameters.setReason(eventSpecificData.get("ID_ALL_REASON"));
			events.add(disposalEventParameters);
		}
		return events;
	}

}
