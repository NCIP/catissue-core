/**
 * <p>Title: SpecimenCollectionGroupAction Class>
 * <p>Description:	SpecimenCollectionGroupAction initializes the fields in the
 * New Specimen Collection Group page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * @author vaishali_khandelwal
 */
public class SpecimenCollectionGroupForTechAction extends BaseAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		/**
		 * Name: Chetan patil
		 * Reviewer: Sachin Lale
		 * Bug ID: Bug#4129
		 * Patch ID: Bug#4129_1
		 * Description: Changes made to support the restriction on SCG.
		 */
		//Accessing specimen collection group id and name
		final String specimenCollectionGroupId = request.getParameter("id");
		final String specimenCollectionGroupName = request.getParameter("name");

		ActionForward actionForward = null;

		if (specimenCollectionGroupId != null)
		{
			//setting id in request to show SCG selected in specimen page
			request.setAttribute(Constants.SPECIMEN_COLLECTION_GROUP_ID, specimenCollectionGroupId);

			//setting name in session to show SCG selected in multiple specimen page
			request.getSession().setAttribute(Constants.SPECIMEN_COLL_GP_NAME,
					specimenCollectionGroupName);
			request.getSession().setAttribute(Constants.SPECIMEN_COLLECTION_GROUP_ID,
					specimenCollectionGroupId);

			int numberOfSpecimen = 1;
			CollectionProtocolEvent collectionProtocolEvent = null;
			Collection specimenRequirementCollection = null;
			if (specimenCollectionGroupId.equals("0"))
			{
				final ActionErrors actionErrors = new ActionErrors();
				final ActionError error = new ActionError("access.execute.action.denied");
				actionErrors.add(ActionErrors.GLOBAL_ERROR, error);
				this.saveErrors(request, actionErrors);

				actionForward = mapping.findForward(Constants.ACCESS_DENIED);
			}
			else
			{
				final SpecimenCollectionGroup specimenCollectionGroup = AppUtility
						.getSpecimenCollectionGroup(specimenCollectionGroupId);
				collectionProtocolEvent = specimenCollectionGroup.getCollectionProtocolEvent();
				//SpecimenCollectionRequirementGroup collectionRequirementGroup//
				//= collectionProtocolEvent.getRequiredCollectionSpecimenGroup();

				specimenRequirementCollection = collectionProtocolEvent
						.getSpecimenRequirementCollection();
				//collectionRequirementGroup.getSpecimenCollection();

				//Populate the number of Specimen Requirements.
				numberOfSpecimen = specimenRequirementCollection.size();
			}
			//Sets the value for number of specimen field on the specimen collection group page.
			request.setAttribute(Constants.NUMBER_OF_SPECIMEN, numberOfSpecimen);

			if (actionForward == null)
			{
				if ((specimenRequirementCollection != null)
						&& (!specimenRequirementCollection.isEmpty()))
				{
					//Set checkbox status depending upon the days
					//of study calendar event point.
					// If it is zero, then unset the restrict
					//checkbox, otherwise set the restrict checkbox
					if (collectionProtocolEvent != null)
					{
						final Double studyCalendarEventPoint = collectionProtocolEvent
								.getStudyCalendarEventPoint();
						if (studyCalendarEventPoint.doubleValue() == 0)
						{
							request.setAttribute(Constants.RESTRICT_SCG_CHECKBOX, Constants.FALSE);
						}
						else
						{
							request.setAttribute(Constants.RESTRICT_SCG_CHECKBOX, Constants.TRUE);
						}
					}
					else
					{
						request.setAttribute(Constants.RESTRICT_SCG_CHECKBOX, Constants.FALSE);
					}
				}

				actionForward = mapping.findForward(Constants.SUCCESS);
			}
		}
		else if (actionForward == null)
		{
			actionForward = mapping.findForward(Constants.FAILURE);
		}
		return actionForward;
	}
}