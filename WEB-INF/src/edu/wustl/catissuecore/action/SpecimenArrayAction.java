/*
 * Created on Jul 13, 2006 TODO To change the template for this generated file
 * go to Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.util.SpecimenArrayAppletUtil;
import edu.wustl.catissuecore.bizlogic.SpecimenArrayBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * Specimen Array action is used to perform action level operations for specimen
 * array object.
 *
 * @author gautam_shetty
 * @author ashwin_gupta
 */
public class SpecimenArrayAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient Logger logger = Logger.getCommonLogger(SpecimenArrayAction.class);

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
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String operation = request.getParameter(Constants.OPERATION);
		request.setAttribute(Constants.OPERATION, operation);
		List < NameValueBean > storagePositionListForSpecimenArray = AppUtility
				.getStoragePositionTypeListForTransferEvent();
		request.setAttribute("storagePositionListForSpecimenArray",
				storagePositionListForSpecimenArray);
		SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm) form;
		SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		// boolean to indicate whether the suitable containers to be shown in
		// dropdown
		// is exceeding the max limit.
		String exceedingMaxLimit = "false";
		String[] arrayTypeLabelProperty = {"name"};
		String arrayTypeProperty = "id";
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic) factory
				.getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);
		List specimenArrayTypeList = new ArrayList();

		if (operation.equals(Constants.ADD))
		{
			specimenArrayTypeList = specimenArrayBizLogic.getList(
					SpecimenArrayType.class.getName(),
					arrayTypeLabelProperty, arrayTypeProperty,
					true);
			for (Iterator iter = specimenArrayTypeList.iterator(); iter.hasNext();)
			{
				NameValueBean nameValueBean = (NameValueBean) iter.next();
				// remove ANY entry from array type list
				if (nameValueBean.getValue().equals(Constants.ARRAY_TYPE_ANY_VALUE)
						&& nameValueBean.getName().
						equalsIgnoreCase(Constants.ARRAY_TYPE_ANY_NAME))
				{
					iter.remove();
					break;
				}
			}
		}
		else if (operation.equals(Constants.EDIT))
		{
			String[] selectColumnName = {"id", "name"};
			String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER};
			String[] whereColumnCondition = {"="};
			Object[] whereColumnValue = {new Long(specimenArrayForm.getSpecimenArrayTypeId())};
			String joinCondition = Constants.AND_JOIN_CONDITION;
			// specimenArrayBizLogic.retrieve(StorageContainer.class.getName(),
			// new Long(specimenArrayForm.getSpecimenArrayTypeId()));
			List specimenArrayTypes = specimenArrayBizLogic.retrieve(SpecimenArrayType.class
					.getName(), selectColumnName, whereColumnName, whereColumnCondition,
					whereColumnValue, joinCondition);
			if ((specimenArrayTypes != null) && (!specimenArrayTypes.isEmpty()))
			{
				Object[] obj = (Object[]) specimenArrayTypes.get(0);
				Long id = (Long) obj[0];
				String name = (String) obj[1];
				NameValueBean nameValueBean = new NameValueBean(name, id);

				specimenArrayTypeList.add(nameValueBean);
			}
		}

		request.setAttribute(Constants.SPECIMEN_ARRAY_TYPE_LIST, specimenArrayTypeList);
		// Setting the specimen class list
		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_CLASS, null);
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		String strMenu = request.getParameter(Constants.MENU_SELECTED);
		if (strMenu != null)
		{
			request.setAttribute(Constants.MENU_SELECTED, strMenu);
			logger.debug(Constants.MENU_SELECTED + " " + strMenu + " set successfully");
		}

		// Setting the specimen type list
		List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_TYPE, null);
		UserBizLogic userBizLogic = (UserBizLogic) factory.getBizLogic(Constants.USER_FORM_ID);
		Collection userCollection = userBizLogic.getUsers(operation);
		request.setAttribute(Constants.USERLIST, userCollection);
		TreeMap containerMap = new TreeMap();
		String subOperation = specimenArrayForm.getSubOperation();
		boolean isChangeArrayType = false;

		if (subOperation != null)
		{
			SpecimenArrayType arrayType = null;
			if (specimenArrayForm.getSpecimenArrayTypeId() > 0)
			{
				Object object = specimenArrayBizLogic.retrieve(SpecimenArrayType.class.getName(),
						specimenArrayForm.getSpecimenArrayTypeId());
				if (object != null)
				{
					arrayType = (SpecimenArrayType) object;
				}
			}
			specimenTypeList = doSetClassAndType(specimenArrayForm, specimenArrayBizLogic,
					arrayType);
			if (subOperation.equals("ChangeArraytype"))
			{
				// specimenArrayForm.setCreateSpecimenArray("no");
				isChangeArrayType = true;

				specimenArrayForm.setOneDimensionCapacity(arrayType.getCapacity()
						.getOneDimensionCapacity().intValue());
				specimenArrayForm.setTwoDimensionCapacity(arrayType.getCapacity()
						.getTwoDimensionCapacity().intValue());
				specimenArrayForm.setName(arrayType.getName() + "_"
						+ specimenArrayBizLogic.getUniqueIndexForName());

				specimenArrayForm.setCreateSpecimenArray("yes");
				request.getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,
						createSpecimenArrayMap(specimenArrayForm));
				// request.getSession().setAttribute(Constants.
				// SPECIMEN_ARRAY_CONTENT_KEY,new HashMap());
			}
			// else if ((subOperation.equalsIgnoreCase("CreateSpecimenArray"))
			// || subOperation.equalsIgnoreCase("ChangeEnterSpecimenBy"))
			else if (subOperation.equalsIgnoreCase("CreateSpecimenArray"))
			{
				specimenArrayForm.setCreateSpecimenArray("yes");
				request.getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,
						createSpecimenArrayMap(specimenArrayForm));
			}
			specimenArrayForm.setSubOperation("");
		}
		StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) factory
				.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		containerMap = storageContainerBizLogic.getAllocatedContaienrMapForSpecimenArray(
				specimenArrayForm.getSpecimenArrayTypeId(), 0, sessionData, exceedingMaxLimit);
		request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);

		List initialValues = null;
		if (isChangeArrayType)
		{
			initialValues = StorageContainerUtil.checkForInitialValues(containerMap);
		}
		else
		{
			initialValues = StorageContainerUtil.setInitialValue(specimenArrayBizLogic,
					specimenArrayForm, containerMap);
		}
		request.setAttribute("initValues", initialValues);
		if (specimenTypeList == null)
		{
			// In case of search & edit operation
			specimenTypeList = (List) request.getAttribute(Constants.SPECIMEN_TYPE_LIST);
		}

		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
		String pageOf = (String) request.getParameter(Constants.PAGE_OF);

		if (pageOf == null)
		{
			pageOf = Constants.SUCCESS;
		}

		if (operation.equals(Constants.ADD))
		{
			// set default user
			if (specimenArrayForm.getCreatedBy() == 0)
			{
				if ((userCollection != null) && (userCollection.size() > 1))
				{
					Iterator iterator = userCollection.iterator();
					iterator.next();
					NameValueBean nameValueBean = (NameValueBean) iterator.next();
					specimenArrayForm.setCreatedBy(Long.valueOf(nameValueBean.getValue())
							.longValue());
				}
			}
		}
		return mapping.findForward(pageOf);
	}

	/**
	 * set class & type values for specimen array.
	 *
	 * @param specimenArrayForm : specimenArrayForm
	 * @param specimenArrayBizLogic : specimenArrayBizLogic
	 * @param arrayType : arrayType
	 * @return List : List
	 * @throws BizLogicException : BizLogicException
	 */
	private List doSetClassAndType(SpecimenArrayForm specimenArrayForm,
			SpecimenArrayBizLogic specimenArrayBizLogic, SpecimenArrayType arrayType)
			throws BizLogicException
	{

		List specimenTypeList = null;
		if (specimenArrayForm.getSpecimenArrayTypeId() > 0)
		{
			if (arrayType != null)
			{
				specimenArrayForm.setSpecimenClass(arrayType.getSpecimenClass());
				/**
				 * Name: Virender Mehta Reviewer: Prafull Retrive Child Specimen
				 * Collection from parent Specimen String[] specimenTypeArr =
				 * new String[arrayType.getSpecimenTypeCollection().size()];
				 */
				Collection specimenTypeCollection = (Collection) specimenArrayBizLogic
						.retrieveAttribute(SpecimenArrayType.class.
								getName(), arrayType.getId(),
								"elements(specimenTypeCollection)");
				String[] specimenTypeArr = new String[specimenTypeCollection.size()];
				specimenTypeList = new ArrayList();
				int i = 0;
				String specimenType = null;
				NameValueBean nameValueBean = null;
				for (Iterator iter = specimenTypeCollection.iterator(); iter.hasNext(); i++)
				{
					specimenType = (String) iter.next();
					specimenTypeArr[i] = specimenType;
					nameValueBean = new NameValueBean(specimenType, specimenType);
					specimenTypeList.add(nameValueBean);
				}
				specimenArrayForm.setSpecimenTypes(specimenTypeArr);
			}
		}
		return specimenTypeList;
	}

	/**
	 * Creates specimen array map which will contain specimen array contents.
	 *
	 * @param specimenArrayForm
	 *            array Form
	 * @return map
	 */
	private Map createSpecimenArrayMap(SpecimenArrayForm specimenArrayForm)
	{
		Map arrayContentMap = new HashMap();
		String value = "";
		int rowCount = specimenArrayForm.getOneDimensionCapacity();
		int columnCount = specimenArrayForm.getTwoDimensionCapacity();

		for (int i = 0; i < rowCount; i++)
		{
			for (int j = 0; j < columnCount; j++)
			{
				for (int k = 0; k < AppletConstants.ARRAY_CONTENT_ATTRIBUTE_NAMES.length; k++)
				{
					value = "";
					if (k == AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_ONE_INDEX)
					{
						value = String.valueOf(i + 1);
					}
					if (k == AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_TWO_INDEX)
					{
						value = String.valueOf(j + 1);
					}
					arrayContentMap.put(SpecimenArrayAppletUtil
							.getArrayMapKey(i, j, columnCount, k), value);
				}
			}
		}
		return arrayContentMap;
	}

}
