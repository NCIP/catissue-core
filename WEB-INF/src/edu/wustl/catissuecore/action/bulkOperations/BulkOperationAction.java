
package edu.wustl.catissuecore.action.bulkOperations;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.BulkEventOperationsForm;
import edu.wustl.catissuecore.bizlogic.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenEventParametersBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;

/**
 * @author renuka_bajpai
 */
public abstract class BulkOperationAction extends SecureAction
{

	/**
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
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// Get Specimen Ids from request. If not there then get all from cart
		LinkedList<String> specimenIds = (LinkedList<String>)request.getAttribute(Constants.SPECIMEN_ID);
		if (specimenIds == null || specimenIds.size() == 0)
		{
			specimenIds = this.getSpecimenIds(request);
		}
		// Set common request parameters for all events
		this.setCommonRequestParameters(request, specimenIds);
		final BulkEventOperationsForm eventParametersForm = (BulkEventOperationsForm) form;
		// Set operation
		final String operation = (String) request.getAttribute(Constants.OPERATION);
		eventParametersForm.setOperation(operation);

		// Set current user
		final SessionDataBean sessionData = this.getSessionData(request);
		if (sessionData != null && sessionData.getUserId() != null)
		{
			final long userId = sessionData.getUserId().longValue();
			eventParametersForm.setUserId(userId);
		}
		// set the current Date and Time for the event.
		final Calendar cal = Calendar.getInstance();
		eventParametersForm.setDateOfEvent(CommonUtilities.parseDateToString(cal.getTime(),
				CommonServiceLocator.getInstance().getDatePattern()));
		eventParametersForm.setTimeInHours(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
		eventParametersForm.setTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
      	setOrderedSpId(specimenIds, eventParametersForm);

		// Set event specific request params
		this.setEventSpecificRequestParameters(eventParametersForm, request, specimenIds);

		if (specimenIds == null || specimenIds.size() == 0)
		{
			final ActionErrors errors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("specimen.cart.size.zero");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			this.saveErrors(request, errors);
			return mapping.findForward(Constants.FAILURE);
		}
		if (operation != null)
		{
			return mapping.findForward(operation);
		}
		return mapping.findForward(Constants.SUCCESS);
	}
	/**
	 *
	 * @param specimenIds Specimen Id's
	 * @param eventParametersForm eventParametersForm object
	 */
	private void setOrderedSpId(LinkedList<String> specimenIds,
			final BulkEventOperationsForm eventParametersForm)
	{
		final StringBuffer orderedSpecimenIdsString = new StringBuffer();
		for (int iCount = 0; iCount < specimenIds.size()-1; iCount++)
		{
			orderedSpecimenIdsString.append(specimenIds.get(iCount));
			orderedSpecimenIdsString.append(",");
		}
		orderedSpecimenIdsString.append(specimenIds.get(specimenIds.size()-1));
		eventParametersForm.setOrderedString(orderedSpecimenIdsString.toString());
	}

	/**
	 * This method sets all the common parameters for the SpecimenEventParameter
	 * pages.
	 * @param request : request
	 * @param specimenIds : specimenIds
	 * @throws Exception : Exception
	 */
	private void setCommonRequestParameters(HttpServletRequest request, LinkedList<String> specimenIds)
			throws Exception
	{
		// Set the minutesList attribute
		request.setAttribute(Constants.MINUTES_LIST, Constants.MINUTES_ARRAY);

		// Set the hourList attribute
		request.setAttribute(Constants.HOUR_LIST, Constants.HOUR_ARRAY);

		// Set User List
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final UserBizLogic userBizLogic = (UserBizLogic) factory
				.getBizLogic(Constants.USER_FORM_ID);
		final Collection userCollection = userBizLogic.getUsers(Constants.ADD);
		request.setAttribute(Constants.USERLIST, userCollection);

		// Set Specimen Ids
		request.setAttribute(Constants.SPECIMEN_ID_LIST, specimenIds);
	}

	/**
	 * @param eventParametersForm : eventParametersForm
	 * @param request : request
	 * @param specimenIds : specimenIds
	 * @throws Exception : Exception
	 */
	private void setEventSpecificRequestParameters(BulkEventOperationsForm eventParametersForm,
			HttpServletRequest request, LinkedList<String> specimenIds) throws Exception
	{

		if (specimenIds != null && specimenIds.size() > 0)
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final SpecimenEventParametersBizLogic bizlogic = (SpecimenEventParametersBizLogic) factory
					.getBizLogic(Constants.BULK_OPERATIONS_FORM_ID);
			final List specimenDataList = bizlogic.getSpecimenDataForBulkOperations(
			eventParametersForm.getOperation(), specimenIds, this.getSessionData(request));
			List specimenRow = null;
			String specimenId = null;
			for (int i = 0; i < specimenDataList.size(); i++)
			{
				specimenRow = (List) specimenDataList.get(i);
				specimenId = specimenRow.get(0).toString();
				//bug 15681
				Long cpId = this.getCpIdFromSpecimenId( specimenId );
				specimenRow.add( cpId );
				/**
				 * Implemented in BulkTransferEventsAction.java
				 */
				this.fillFormData(eventParametersForm, specimenRow, specimenId, request);
			}
		}
	}
	/**
	 * Retrieves CP id from specimen id
	 * @param specimenId - specimen Id
	 * @return CP id
	 * @throws ApplicationException - ApplicationException
	 */
	private Long getCpIdFromSpecimenId(String specimenId) throws ApplicationException
	{
		final String colProtHql = "select scg.collectionProtocolRegistration.collectionProtocol.id"
			+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg,"
			+ " edu.wustl.catissuecore.domain.Specimen as spec "
			+ " where spec.specimenCollectionGroup.id=scg.id and spec.id="
			+ Long.valueOf( specimenId ).longValue();
		List collectionProtocolIdList;
		collectionProtocolIdList = AppUtility.executeQuery(colProtHql);
		final Long collectionProtocolId = (Long) collectionProtocolIdList.get(0);
		return collectionProtocolId;	
	}

	/**
	 * This method returns all specimen ids in cart.
	 * @param request : request
	 * @return List : List
	 */
	private LinkedList<String> getSpecimenIds(HttpServletRequest request)
	{
		LinkedList<String> specimenIds = new LinkedList<String>();
		final QueryShoppingCart cart = (QueryShoppingCart) request.getSession().getAttribute(
				Constants.QUERY_SHOPPING_CART);
		if (cart != null)
		{
			final QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
			specimenIds = new LinkedList<String>(bizLogic.getEntityIdsList(cart, Arrays
					.asList(Constants.specimenNameArray), null));
			if (specimenIds == null)
			{
				specimenIds = new LinkedList<String>();
			}
		}
		return specimenIds;
	}

	/**
	 * @param eventParametersForm : eventParametersForm
	 * @param specimenRow : specimenRow
	 * @param specimenId : specimenId
	 * @param request : request
	 * @throws ApplicationException 
	 */
	protected abstract void fillFormData(BulkEventOperationsForm eventParametersForm,
			List specimenRow, String specimenId, HttpServletRequest request) throws ApplicationException;

}
