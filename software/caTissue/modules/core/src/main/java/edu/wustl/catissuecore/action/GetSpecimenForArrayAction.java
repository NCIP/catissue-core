/**
 * <p>
 * Title: CreateArrayInitAction Class>
 * <p>
 * Description: CreateArrayInitAction populates the fields in the Specimen Array
 * page with array information.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Ramya Nagraj
 * @version 1.00 Created on Dec 14,2006
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.RequestDetailsForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * GetSpecimenForArrayAction gets the specimens which are to be populated in
 * array grid.
 *
 * @author ramya_nagraj
 */
public class GetSpecimenForArrayAction extends BaseAction
{

	/**
	 * This function gets the specimens which are to be populated in array grid.
	 *
	 * @param mapping
	 *            object
	 * @param form
	 *            object
	 * @param request
	 *            object
	 * @param response
	 *            object
	 * @return ActionForward object
	 * @throws Exception
	 *             object
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final RequestDetailsForm requestDetailsForm = (RequestDetailsForm) form;
		final String arrayRowCounter = request.getParameter("arrayRowCounter");
		final String rowCounter1 = request.getParameter("rowCounter");
		final int rowCounter = new Integer(rowCounter1).intValue();
		final String arrayName = request.getParameter("array");

		// Getting Values Map from requestDetailsForm
		final Map values = requestDetailsForm.getValues();
		final String arrayNameKey = "DefinedArrayRequestBean:" + arrayRowCounter + "_arrayName";
		final String arrayNameInMap = (String) values.get(arrayNameKey);
		final List<Long> specimenIdList = new ArrayList<Long>();
		if (arrayName.equals(arrayNameInMap))
		{
			final String noOfItemsKey = "DefinedArrayRequestBean:" + arrayRowCounter + "_noOfItems";
			final int noOfItems = new Integer((String) values.get(noOfItemsKey)).intValue();
			for (int i = rowCounter; i < rowCounter + noOfItems; i++)
			{
				final String statusKey = "DefinedArrayDetailsBean:" + i + "_assignedStatus";
				if (!(values.get(statusKey).equals(
						Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE)
						|| values.get(statusKey).equals(
								Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST) || values
						.get(statusKey).equals(
								Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE)))
				{
					final String instanceOfKey = "DefinedArrayDetailsBean:" + i + "_instanceOf";

					// if specimen is derived specimen then take requestFor
					// Specimen Id .
					if (values.get(instanceOfKey).equals("Derived"))
					{
						final String requestedForKey = "DefinedArrayDetailsBean:" + i
								+ "_requestFor";

						if (values.get(requestedForKey) != null)
						{
							final Long specimenId = new Long((String) values.get(requestedForKey));
							specimenIdList.add(specimenId);
						}
					}
					else
					{
						final String specimenIdKey = "DefinedArrayDetailsBean:" + i + "_specimenId";
						if (values.get(specimenIdKey) != null)
						{
							final Long specimenId = new Long((String) values.get(specimenIdKey));
							specimenIdList.add(specimenId);
						}
					}
				}

			}

		}
		request.setAttribute(Constants.ARRAY_NAME, arrayName);
		request.setAttribute(Constants.OPERATION, "add");
		request.setAttribute(Constants.SPECIMEN_ID_LIST, specimenIdList);
		return mapping.findForward("success");
	}
}
