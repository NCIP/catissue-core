/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


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
