
package edu.wustl.catissuecore.action.bulkOperations;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.BulkEventOperationsForm;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author renuka_bajpai
 *
 */
public class BulkDisposalEventsAction extends BulkOperationAction
{

	/**
	 * @param eventParametersForm : eventParametersForm
	 * @param specimenRow : specimenRow
	 * @param specimenId : specimenId
	 * @param request : request
	 */
	@Override
	protected void fillFormData(BulkEventOperationsForm eventParametersForm, List specimenRow,
			String specimenId, HttpServletRequest request)
	{
		eventParametersForm.setFieldValue("ID_" + specimenId + "_LABEL", specimenRow.get(1)
				.toString());
		request.setAttribute(Constants.ACTIVITYSTATUSLIST,
				Constants.DISPOSAL_EVENT_ACTIVITY_STATUS_VALUES);
	}
}
