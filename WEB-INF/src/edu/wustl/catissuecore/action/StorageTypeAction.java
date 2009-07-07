/**
 * <p>
 * Title: StorageTypeAction Class>
 * <p>
 * Description: This class initializes the fields of StorageType.jsp Page
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Aniruddha Phadnis
 * @version 1.00 Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class StorageTypeAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(StorageTypeAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
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
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final StorageTypeForm storageTypeForm = (StorageTypeForm) form;
		final String operation = (String) request.getAttribute(Constants.OPERATION);
		storageTypeForm.setOperation(operation);
		final String submittedFor = (String) request.getAttribute(Constants.SUBMITTED_FOR);
		storageTypeForm.setSubmittedFor(submittedFor);
		final String forwardTo = (String) request.getAttribute(Constants.FORWARD_TO);
		storageTypeForm.setForwardTo(forwardTo);
		final String reqPath = request.getParameter(Constants.REQ_PATH);
		storageTypeForm.setRedirectTo(reqPath);
		String formName;
		if (operation.equals(Constants.EDIT))
		{
			formName = Constants.STORAGE_TYPE_EDIT_ACTION;
		}
		else
		{
			formName = Constants.STORAGE_TYPE_ADD_ACTION;
		}
		request.setAttribute("formName", formName);
		request.setAttribute("operationAdd", Constants.ADD);
		request.setAttribute("operationEdit", Constants.EDIT);
		request.setAttribute("holds_List_1", Constants.HOLDS_LIST1);
		request.setAttribute("holds_List_2", Constants.HOLDS_LIST2);
		request.setAttribute("holds_List_3", Constants.HOLDS_LIST3);

		// Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label
		int dimTwoCapacity = 0;
		if (storageTypeForm != null)
		{
			dimTwoCapacity = storageTypeForm.getTwoDimensionCapacity();
		}
		String tdClassName = "formLabel";
		String strStar = "&nbsp;";
		if (dimTwoCapacity > 1)
		{
			tdClassName = "formRequiredLabel";
			strStar = "<span class=" + "blue_ar_b" + "><img src="
					+ "images/uIEnhancementImages/star.gif" + " alt=" + "Mandatory" + " width="
					+ "6" + " height=" + "6" + " hspace=" + "0" + " vspace=" + "3" + " /></span>";
		}
		request.setAttribute("tdClassName", tdClassName);
		request.setAttribute("strStar", strStar);

		final String normalSubmit = "validate('" + submittedFor + "','"
				+ Constants.STORAGE_TYPE_FORWARD_TO_LIST[0][1] + "')";
		final String forwardToSubmit = "validate('ForwardTo','"
				+ Constants.STORAGE_TYPE_FORWARD_TO_LIST[1][1] + "')";
		request.setAttribute("normalSubmit", normalSubmit);
		request.setAttribute("forwardToSubmit", forwardToSubmit);

		request.setAttribute("submit", Constants.STORAGE_TYPE_FORWARD_TO_LIST[0][0]);
		request.setAttribute("addContainer", Constants.STORAGE_TYPE_FORWARD_TO_LIST[1][0]);

		// Mandar : 18-Apr-06 : bugid: 644 : - Dimension 2 capacity label end

		this.logger.info("SpecimenArray/specimen:" + storageTypeForm.getSpecimenOrArrayType());
		if (storageTypeForm.getSpecimenOrArrayType() == null)
		{
			storageTypeForm.setSpecimenOrArrayType("Specimen");
		}
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final StorageTypeBizLogic bizLogic = (StorageTypeBizLogic) factory
				.getBizLogic(Constants.STORAGE_TYPE_FORM_ID);
		// Gets the value of the operation parameter.

		// Sets the operation attribute to be used in the Add/Edit Institute
		// Page.
		// Gets the Storage Type List and sets it in request
		final List list1 = bizLogic.retrieve(StorageType.class.getName());
		final List storageTypeList = AppUtility.getStorageTypeList(list1, true);
		// Collections.sort(storageTypeList);
		request.setAttribute(Constants.HOLDS_LIST1, storageTypeList);

		// get the Specimen class and type from the cde
		final List specimenClassTypeList = AppUtility.getSpecimenClassTypeListWithAny();
		request.setAttribute(Constants.HOLDS_LIST2, specimenClassTypeList);

		// Gets the Specimen array Type List and sets it in request
		final List list2 = bizLogic.retrieve(SpecimenArrayType.class.getName());
		final List spArrayTypeList = AppUtility.getSpecimenArrayTypeList(list2);
		request.setAttribute(Constants.HOLDS_LIST3, spArrayTypeList);

		// Bug #4297
		// if(operation.equals(Constants.ADD))
		// {
		// // new model storageTypeForm.setHoldsSpecimenClassTypeIds(new
		// long[]{1});
		// //storageTypeForm.setHoldsStorageTypeIds(new long[]{1});
		// if(storageTypeForm.getOneDimensionCapacity() == 0 &&
		// storageTypeForm.getTwoDimensionCapacity() == 0)
		// {
		// storageTypeForm.setOneDimensionCapacity(0);
		// storageTypeForm.setTwoDimensionCapacity(0);
		// }
		// }
		// ------------- add new

		final AbstractActionForm aForm = (AbstractActionForm) form;
		if (reqPath != null && aForm != null)
		{
			aForm.setRedirectTo(reqPath);
		}

		this.logger.debug("StorageTypeAction redirect :---------- " + reqPath);

		return mapping.findForward(request.getParameter(Constants.PAGE_OF));
	}

}