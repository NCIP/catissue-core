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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.util.SpecimenArrayAppletUtil;
import edu.wustl.catissuecore.bean.DefinedArrayRequestBean;
import edu.wustl.catissuecore.bizlogic.SpecimenArrayBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * @author renuka_bajpai
 */
public class CreateArrayInitAction extends BaseAction
{
	/**
	 * Logger object.
	 */
	private Logger logger = Logger.getCommonLogger(CreateArrayInitAction.class);
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
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		DAO dao = null;
		try
		{
			// Obtain session object from request.
			final HttpSession session = request.getSession();
			final SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
					.getAttribute(Constants.SESSION_DATA);
			dao = AppUtility.openDAOSession(sessionDataBean);

			// Retrieve from query string
			// String arrayName = request.getParameter("array");
			// String operation = request.getParameter("operation");

			final String arrayName = (String) request.getAttribute(Constants.ARRAY_NAME);
			final String operation = (String) request.getAttribute(Constants.OPERATION);
			final List<NameValueBean> storagePositionListForSpecimenArray = AppUtility
					.getStoragePositionTypeListForTransferEvent();
			request.setAttribute("storagePositionListForSpecimenArray",
					storagePositionListForSpecimenArray);

			final String exceedingMaxLimit = "false";
			final SessionDataBean sessionData = (SessionDataBean) request.getSession()
					.getAttribute(Constants.SESSION_DATA);

			// Obtain specimenArray BizLogic
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic) factory
					.getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);

			final List definedArrayRequestMapList = (ArrayList) session
					.getAttribute(Constants.DEFINEDARRAY_REQUESTS_LIST);
			final Iterator definedArrayRequestMapListItr = definedArrayRequestMapList.iterator();
			while (definedArrayRequestMapListItr.hasNext())
			{
				final Map defineArrayMap = (Map) definedArrayRequestMapListItr.next();
				final Set defineArrayKeySet = defineArrayMap.keySet();
				final Iterator defineArrayKeySetItr = defineArrayKeySet.iterator();

				// The Set has only one element(i.e,defineArrayRequestBean
				// instance)
				final DefinedArrayRequestBean definedArrayRequestBean = (DefinedArrayRequestBean) defineArrayKeySetItr
						.next();

				if (definedArrayRequestBean.getArrayName().equals(arrayName))
				{
					final SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm) form;

					// Set Specimen Class in request attribute to be displayed
					// in
					// SpecimenArray.jsp
					final List specimenClassList = new ArrayList();

					final String colName = "specimenClass";
					final String colValue = definedArrayRequestBean.getArrayClass();
					final List specimenArrayTypeList = dao.retrieve(SpecimenArrayType.class
							.getName(), colName, colValue);
					final SpecimenArrayType specimenArrayType = (SpecimenArrayType) specimenArrayTypeList
							.get(0);

					final String specimenClassId = specimenArrayType.getId().toString();
					final String specimenClass = specimenArrayType.getSpecimenClass();

					// Populate Specimen Class List in the request scope.
					final NameValueBean specClassNameValue = new NameValueBean(specimenClass,
							specimenClassId);
					specimenClassList.add(specClassNameValue);
					request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

					specimenArrayForm.setSpecimenClass(specimenClass);

					// Set Specimen Types
					List specimenTypeList = this.setClassAndtype(specimenArrayForm,
							specimenArrayType, dao);

					// specimens ArrayList contains the id of all specimens in
					// the
					// given defined array.
					/*
					 * List definedArrayDetailsBeanList = (ArrayList)
					 * defineArrayMap .get(definedArrayRequestBean);
					 */

					List specimensObjList = new ArrayList();
					final List specimenIdList = (ArrayList) request
							.getAttribute(Constants.SPECIMEN_ID_LIST);
					// specimensObjList =
					// constructSpecimenObjList(definedArrayDetailsBeanList);
					specimensObjList = this.constructSpecimenObjList(specimenIdList, dao);
					// Populate arraycontentmap and set it in request scope.
					Map arrayContentMap = new HashMap();
					arrayContentMap = this.populateArrayContentMap(definedArrayRequestBean,
							specimensObjList);
					request.getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,
							arrayContentMap);
					specimenArrayForm.setSubOperation("");
					specimenArrayForm.setCreateSpecimenArray("yes");

					request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);

					// Set the specimen name (i.e,label) in the form
					specimenArrayForm.setName(definedArrayRequestBean.getArrayName());

					// Setting newSpecimenArrayOrderItem Id
					specimenArrayForm.setNewArrayOrderItemId(definedArrayRequestBean
							.getOrderItemId());
					specimenArrayForm.setIsDefinedArray("True");

					// Set the array type in request attribute to be viewed in
					// SpecimenArray.jsp
					final List arrayTypeList = new ArrayList();
					final String arrayTypeName = definedArrayRequestBean.getArrayType();
					final String arrayTypeId = definedArrayRequestBean.getArrayTypeId();
					final NameValueBean typeNameValueBean = new NameValueBean(arrayTypeName,
							arrayTypeId);
					arrayTypeList.add(typeNameValueBean);
					request.setAttribute(Constants.SPECIMEN_ARRAY_TYPE_LIST, arrayTypeList);

					specimenArrayForm.setSpecimenArrayTypeId(new Long(arrayTypeId).longValue());

					// Set Dimensions in the form
					specimenArrayForm.setOneDimensionCapacity(Integer
							.parseInt(definedArrayRequestBean.getOneDimensionCapacity()));
					specimenArrayForm.setTwoDimensionCapacity(Integer
							.parseInt(definedArrayRequestBean.getTwoDimensionCapacity()));

					// Set the User List in request attribute.
					final UserBizLogic userBizLogic = (UserBizLogic) factory
							.getBizLogic(Constants.USER_FORM_ID);
					final Collection userCollection = userBizLogic.getUsers(operation);
					request.setAttribute(Constants.USERLIST, userCollection);

					final String subOperation = specimenArrayForm.getSubOperation();
					TreeMap containerMap = new TreeMap();
					// boolean isChangeArrayType = false;

					if (subOperation != null)
					{
						SpecimenArrayType arrayType = null;
						if (specimenArrayForm.getSpecimenArrayTypeId() > 0)
						{
							final Object object = dao.retrieveById(SpecimenArrayType.class
									.getName(), specimenArrayForm.getSpecimenArrayTypeId());
							if (object != null)
							{
								arrayType = (SpecimenArrayType) object;
							}
						}
						specimenTypeList = this.setClassAndtype(specimenArrayForm, arrayType, dao);
					}

					final StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) factory
							.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
					containerMap = storageContainerBizLogic
							.getAllocatedContainerMapForSpecimenArray(specimenArrayForm
									.getSpecimenArrayTypeId(), 0, sessionData, exceedingMaxLimit,
									dao);
					request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
					request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);

					List initialValues = null;
					initialValues = StorageContainerUtil.setInitialValue(specimenArrayBizLogic,
							specimenArrayForm, containerMap, dao);
					request.setAttribute("initValues", initialValues);

					break;
				}// End
				// if(definedArrayRequestBean.getArrayName().equals(arrayName
				// ))
			}// End Outer While
			final SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm) form;
			specimenArrayForm.setForwardTo("orderDetails");
		}
		finally
		{
			dao.closeSession();
		}
		return mapping.findForward("success");
	}

	/**
	 * This function retrieves specimens given the id and populates them in the
	 * arraylist.
	 *
	 * @param specimenIdList
	 *            : specimenIdList
	 * @return List : List
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private List constructSpecimenObjList(List specimenIdList, DAO dao) throws BizLogicException
	{
		final List specimensObjList = new ArrayList();
		try
		{
			final Iterator itr = specimenIdList.iterator();
			while (itr.hasNext())
			{
				// DefinedArrayDetailsBean definedArrayDetailsBean =
				// (DefinedArrayDetailsBean)definedArrayDetailsBeanListItr.next();
				final Long specimenId = (Long) itr.next();
				final Object object = dao.retrieveById(Specimen.class.getName(), specimenId);
				// Set the specimen domain instances in the specimens object list.
				specimensObjList.add(object);
			}
		}
		catch (final DAOException excep)
		{
			this.logger.error(excep.getMessage(),excep);
			excep.printStackTrace();
			throw new BizLogicException(excep);
		}
		return specimensObjList;
	}

	/**
	 * This function populates the map to dispaly it in the grid.
	 *
	 * @param definedArrayRequestBean
	 *            : definedArrayRequestBean
	 * @param specimensObjList
	 *            : specimensObjList
	 * @return Map : Map
	 */
	private Map populateArrayContentMap(DefinedArrayRequestBean definedArrayRequestBean,
			List specimensObjList)
	{
		final Map arrayContentMap = new HashMap();
		String key = null;
		String value = null;
		// Obtain the one and two dimension capacity
		final int row = new Integer(definedArrayRequestBean.getOneDimensionCapacity()).intValue();
		final int col = new Integer(definedArrayRequestBean.getTwoDimensionCapacity()).intValue();
		final int columnCount = col;
		final int listCount = row * col;

		/* Size of specimenList is < or == value of listIndex */
		int listIndex = 0;
		for (int i = 0; i < row; i++)
		{
			for (int j = 0; j < col; j++)
			{
				if (listIndex <= listCount && listIndex < specimensObjList.size()
						&& specimensObjList.get(listIndex) != null)
				{
					key = SpecimenArrayAppletUtil.getArrayMapKey(i, j, columnCount,
							AppletConstants.ARRAY_CONTENT_ATTR_LABEL_INDEX);
					final Specimen specimen = (Specimen) specimensObjList.get(listIndex);
					value = specimen.getLabel();
					arrayContentMap.put(key, value);

					key = SpecimenArrayAppletUtil.getArrayMapKey(i, j, columnCount,
							AppletConstants.ARRAY_CONTENT_ATTR_ID_INDEX);
					value = specimen.getId().toString();
					arrayContentMap.put(key, value);

					key = SpecimenArrayAppletUtil.getArrayMapKey(i, j, columnCount,
							AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_ONE_INDEX);
					value = String.valueOf(row);
					arrayContentMap.put(key, value);

					key = SpecimenArrayAppletUtil.getArrayMapKey(i, j, columnCount,
							AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_TWO_INDEX);
					value = String.valueOf(col);
					arrayContentMap.put(key, value);

					listIndex++;
				}
			}
		}
		return arrayContentMap;
	}

	/**
	 * set class & type values for specimen array.
	 *
	 * @param specimenArrayForm
	 *            : specimenArrayForm
	 * @param specimenArrayType
	 *            : specimenArrayType
	 * @return List : List
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private List setClassAndtype(SpecimenArrayForm specimenArrayForm,
			SpecimenArrayType specimenArrayType, DAO dao) throws BizLogicException
	{
		final List specimenTypeList = new ArrayList();
		try
		{
			String specimentype = new String();
			NameValueBean specTypeNameValue = null;
			final Collection specimenArrayTypeCollection = dao.retrieveAttribute(
					SpecimenArrayType.class, "id", specimenArrayType.getId(),
					"elements(specimenTypeCollection)");

			specimenArrayType.setSpecimenTypeCollection(specimenArrayTypeCollection);
			final String[] specimenTypeArr = new String[specimenArrayType
					.getSpecimenTypeCollection().size()];
			final int i = 0;
			final Iterator listItr = specimenArrayType.getSpecimenTypeCollection().iterator();
			while (listItr.hasNext())
			{
				specimentype = (String) listItr.next();
				specimenTypeArr[i] = specimentype;
				specTypeNameValue = new NameValueBean(specimentype, specimentype);
				specimenTypeList.add(specTypeNameValue);
			}
			// Set specimenType in the form
			specimenArrayForm.setSpecimenTypes(specimenTypeArr);
		}
		catch (final DAOException excep)
		{
			this.logger.error(excep.getMessage(),excep);
			excep.printStackTrace();
			throw new BizLogicException(excep);
		}
		return specimenTypeList;
	}

}
