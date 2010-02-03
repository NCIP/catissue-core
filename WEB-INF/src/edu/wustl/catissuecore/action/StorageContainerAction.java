/**
 * <p>
 * Title: StorageContainerAction Class>
 * <p>
 * Description: This class initializes the fields of StorageContainer.jsp Page
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Aniruddha Phadnis
 * @version 1.00 Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.bean.StorageContainerBean;
import edu.wustl.catissuecore.bizlogic.SiteBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerForContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * @author renuka_bajpai
 */
public class StorageContainerAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(StorageContainerAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping : obj of ActionMapping
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
		final SessionDataBean sessionDataBean = this.getSessionData(request);
		final DAO dao = AppUtility.openDAOSession(sessionDataBean);
		try
		{
			final StorageContainerForm storageContainerForm = (StorageContainerForm) form;
			final HttpSession session = request.getSession();
			final StorageContainerBean storageContainerBean = (StorageContainerBean) session
					.getAttribute(Constants.STORAGE_CONTAINER_SESSION_BEAN);
			final String exceedingMaxLimit = "false";
			final String pageOf = request.getParameter(Constants.PAGE_OF);
			final String containerId = request.getParameter("containerIdentifier");
			final String isPageFromStorageType = (String) session
					.getAttribute("isPageFromStorageType");
			final String isContainerChanged = request.getParameter("isContainerChanged");
			if (storageContainerForm.getSpecimenOrArrayType() == null)
			{
				storageContainerForm.setSpecimenOrArrayType("Specimen");
			}
			request.setAttribute(Constants.MENU_SELECTED, "7");
			boolean isTypeChange = false;
			String str = request.getParameter("isOnChange");
			str = request.getParameter("typeChange");
			if (str != null && str.equals("true"))
			{
				isTypeChange = true;
			}
			final String operation = request.getParameter(Constants.OPERATION);
			request.setAttribute(Constants.OPERATION, operation);
			if (operation.equals(Constants.EDIT) && storageContainerBean != null
					&& Constants.PAGE_OF_STORAGE_CONTAINER.equals(pageOf))
			{
				this.initStorageContainerForm(storageContainerForm, storageContainerBean);
			}
			this.setRequestAttributes(request, storageContainerForm, dao);
			this.setStorageType(request, storageContainerForm, session);
			final StorageContainerForContainerBizLogic bizLogic = new StorageContainerForContainerBizLogic();
			final SiteBizLogic siteBizLogic = new SiteBizLogic();
			if (isContainerChanged != null && isContainerChanged.equals("true"))
			{
				final List<JSONObject> jsonList = new ArrayList<JSONObject>();
				final String parentEleType = request.getParameter("parentEleType");
				if (parentEleType.equals("parentContAuto"))
				{
					final long contId = new Long(request.getParameter("parentContainerId"));
					final Site site = siteBizLogic.getRelatedSite(contId, dao);
					if (site != null)
					{
						final long siteId = site.getId();
						AppUtility.setCollectionProtocolList(request, siteId, dao);
					}
				}
				else if (parentEleType.equals("parentContManual"))
				{
					final String contName = request.getParameter("selectedContainerName");
					SiteBizLogic siteBiz = new SiteBizLogic();
					final Site site = siteBiz.getRelatedSiteForManual(contName, dao);
					if (site != null)
					{
						final long siteId = site.getId();
						AppUtility.setCollectionProtocolList(request, siteId, dao);
					}
				}
				else if (parentEleType.equals("parentContSite"))
				{
					final long siteId = new Long(request.getParameter("siteId"));
					// String contName =
					// storageContainerForm.getSelectedContainerName();
					AppUtility.setCollectionProtocolList(request, siteId, dao);
				}
				JSONObject jsonObject = null;
				final List<NameValueBean> cpList = (List<NameValueBean>) request
						.getAttribute(Constants.PROTOCOL_LIST);
				if (cpList != null && !cpList.isEmpty())
				{
					for (final NameValueBean nvbean : cpList)
					{
						jsonObject = new JSONObject();
						jsonObject.append("cpName", nvbean.getName());
						jsonObject.append("cpValue", nvbean.getValue());
						jsonList.add(jsonObject);
					}
				}
				response.flushBuffer();
				response.getWriter().write(new JSONObject().put("locations", jsonList).toString());

				return null;

			}

			TreeMap containerMap = new TreeMap();

			if (storageContainerForm.getTypeId() != -1)
			{
				final long start = System.currentTimeMillis();
				containerMap = bizLogic.getAllocatedContainerMapForContainer(storageContainerForm
						.getTypeId(), sessionDataBean, dao,storageContainerForm.getParentContainerSelected() );
				final long end = System.currentTimeMillis();

				System.out.println("Time taken for getAllocatedMapForCOntainer:" + (end - start));
			}
			if (containerId != null)
			{
				final Long id = new Long(containerId);
				final List storageTypeList = dao.retrieveAttribute(StorageContainer.class, "id",
						id, "storageType");
				if ((storageTypeList != null) && (storageTypeList.size() > 0))
				{
					final StorageType storageType = (StorageType) storageTypeList.get(0);
					final Long typeId = storageType.getId();
					final long start = System.currentTimeMillis();
					containerMap = bizLogic.getAllocatedContainerMapForContainer(typeId,
					sessionDataBean, dao,storageContainerForm.getParentContainerSelected());
					final long end = System.currentTimeMillis();
					System.out.println("Time taken for getAllocatedMapForCOntainer:"
							+ (end - start));

				}
			}

			if (operation.equals(Constants.ADD))
			{
				this.setParentStorageContainersForAdd(containerMap, storageContainerForm, request,
						dao);
			}

			if (operation.equals(Constants.EDIT))
			{

				if (StorageContainerUtil.isContainerFull(storageContainerForm.getId() + "",
						storageContainerForm.getContainerName()))
				{
					storageContainerForm.setIsFull("true");
				}
				final List storagetypeList = new ArrayList();
				final NameValueBean nvb = new NameValueBean(storageContainerForm.getTypeName(),
						new Long(storageContainerForm.getTypeId()));
				storagetypeList.add(nvb);
				request.setAttribute(Constants.STORAGETYPELIST, storagetypeList);
				this.setParentStorageContainersForEdit(containerMap, storageContainerForm, request,
						dao);
			}

			request.setAttribute("storageContainerIdentifier", storageContainerForm.getId());
			request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
			request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
			this.setFormAttributesForAddNew(request, storageContainerForm);
			if (isTypeChange || request.getAttribute(Constants.SUBMITTED_FOR) != null
					|| Constants.YES.equals(isPageFromStorageType))
			{
				this.onTypeChange(storageContainerForm, operation, request);
			}
			if("Specimen".equals(storageContainerForm.getSpecimenOrArrayType()))
			{
				StorageContainerUtil.setSpTypeList(request, storageContainerForm);
			}
			if (request.getAttribute(Constants.SUBMITTED_FOR) != null)
			{
				final long[] collectionIds = this.parentContChange(request);
				storageContainerForm.setCollectionIds(collectionIds);
			}
			final String reqPath = request.getParameter(Constants.REQ_PATH);

			if (reqPath != null)
			{
				request.setAttribute(Constants.REQ_PATH, reqPath);
			}
			final List<NameValueBean> parentContainerTypeList = AppUtility
					.getParentContainerTypeList();
			request.setAttribute("parentContainerTypeList", parentContainerTypeList);
			request.setAttribute("parentContainerSelected", storageContainerForm
					.getParentContainerSelected());
			session.removeAttribute(Constants.STORAGE_CONTAINER_SESSION_BEAN);
			session.removeAttribute("isPageFromStorageType");
			AppUtility.setDefaultPrinterTypeLocation(storageContainerForm);
		}
		finally
		{
			dao.closeSession();
		}
		return mapping.findForward(request.getParameter(Constants.PAGE_OF));
	}

	/**
	 * @param request
	 *            : request
	 * @param storageContainerForm
	 *            : storageContainerForm
	 * @param session
	 *            : session
	 */
	private void setStorageType(HttpServletRequest request,
			StorageContainerForm storageContainerForm, HttpSession session)
	{
		// *************Start Bug:1938 ForwardTo implementation *************
		HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");
		if (forwardToHashMap == null)
		{
			forwardToHashMap = (HashMap) session.getAttribute("forwardToHashMap");
			session.removeAttribute("forwardToHashMap");
		}
		if (forwardToHashMap != null && forwardToHashMap.size() > 0)
		{
			final Long storageTypeId = (Long) forwardToHashMap.get("storageTypeId");
			this.logger.debug("storageTypeId found in forwardToHashMap========>>>>>>"
					+ storageTypeId);
			storageContainerForm.setTypeId(storageTypeId.longValue());
		}
		else
		{
			if (request.getParameter("storageTypeId") != null)
			{
				final Long storageTypeId = new Long(request.getParameter("storageTypeId"));
				storageContainerForm.setTypeId(storageTypeId.longValue());
			}
			else if (session.getAttribute("storageTypeIdentifier") != null)
			{
				final Long storageTypeId = (Long) session.getAttribute("storageTypeIdentifier");
				storageContainerForm.setTypeId(storageTypeId.longValue());
				session.removeAttribute("storageTypeIdentifier");
			}
		}
		// *************End Bug:1938 ForwardTo implementation *************

	}

	/**
	 * @param request
	 *            : request
	 * @param storageContainerForm
	 *            : storageContainerForm
	 * @throws ApplicationException
	 *             : ApplicationException
	 */
	private void setRequestAttributes(HttpServletRequest request,
			StorageContainerForm storageContainerForm, DAO dao) throws ApplicationException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) factory
				.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		final SiteBizLogic siteBizlog = new SiteBizLogic();
		// Gets the value of the operation parameter.
		final String operation = request.getParameter(Constants.OPERATION);
		// Sets the operation attribute to be used in the Add/Edit Institute
		// Page.
		request.setAttribute(Constants.OPERATION, operation);
		// Sets the activityStatusList attribute to be used in the Site Add/Edit
		// Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

		// Populating the Site Array
		final String[] siteDisplayField = {"name"};
		final String valueField = "id";

		/**
		 * Name : kalpana thakur Reviewer Name : Vaishali Bug ID: 4922
		 * Description: get the list of site with activity status "Active"
		 */
		final String[] activityStatusArray = {Status.ACTIVITY_STATUS_DISABLED.toString(),
				Status.ACTIVITY_STATUS_CLOSED.toString()};
		final SessionDataBean sessionDataBean = this.getSessionData(request);
		final List list = siteBizlog.getSiteList(siteDisplayField, valueField, activityStatusArray,
				sessionDataBean.getUserId());
		NameValueBean nvbForSelect = null;
		if (list != null && !list.isEmpty())
		{
			nvbForSelect = (NameValueBean) list.get(0);

			if (!"-1".equals(nvbForSelect.getValue()))
			{
				final NameValueBean nvb = new NameValueBean("--Select--", "-1");
				list.add(0, nvb);
			}
		}
		request.setAttribute(Constants.SITELIST, list);
		// get the Specimen class and type from the cde
		final List specimenClassTypeList = AppUtility.getSpecimenClassTypeListWithAny();
		request.setAttribute(Constants.HOLDS_LIST2, specimenClassTypeList);
		// Gets the Specimen array Type List and sets it in request
		final List list3 = bizLogic.retrieve(SpecimenArrayType.class.getName());
		final List spArrayTypeList = AppUtility.getSpecimenArrayTypeList(list3);
		request.setAttribute(Constants.HOLDS_LIST3, spArrayTypeList);
		setSpecimenClass(storageContainerForm, bizLogic);
		final List list2 = bizLogic.retrieve(StorageType.class.getName());
		final List storageTypeListWithAny = AppUtility.getStorageTypeList(list2, true);
		request.setAttribute(Constants.HOLDS_LIST1, storageTypeListWithAny);
		request.setAttribute(Constants.SPECIMEN_TYPE_MAP, AppUtility.getSpecimenTypeMap());

		if (Constants.ADD.equals(request.getAttribute(Constants.OPERATION)))
		{
			final List StorageTypeListWithoutAny = AppUtility.getStorageTypeList(list2, false);
			request.setAttribute(Constants.STORAGETYPELIST, StorageTypeListWithoutAny);
		}
		if ("Site".equals(storageContainerForm.getParentContainerSelected()))
		{
			request = AppUtility.setCollectionProtocolList(request, storageContainerForm
					.getSiteId(), dao);
		}
		else if ("Auto".equals(storageContainerForm.getParentContainerSelected()))
		{
			final long parentContId = storageContainerForm.getParentContainerId();
			final Site site = siteBizlog.getRelatedSite(parentContId, dao);
			if (site != null)
			{
				request = AppUtility.setCollectionProtocolList(request, site.getId(), dao);
			}
			else
			{
				final List<NameValueBean> cpList = new ArrayList<NameValueBean>();
				final Map<Long, String> cpTitleMap = new HashMap<Long, String>();
				request.setAttribute(Constants.PROTOCOL_LIST, cpList);
				request.setAttribute(Constants.CP_ID_TITLE_MAP, cpTitleMap);
			}
		}
		else if ("Manual".equals(storageContainerForm.getParentContainerSelected()))
		{
			final String containerName = storageContainerForm.getSelectedContainerName();
			final Site site = siteBizlog.getRelatedSiteForManual(containerName, dao);
			if (site != null)
			{
				request = AppUtility.setCollectionProtocolList(request, site.getId(), dao);
			}
			else
			{
				final List<NameValueBean> cpList = new ArrayList<NameValueBean>();
				final Map<Long, String> cpTitleMap = new HashMap<Long, String>();
				request.setAttribute(Constants.PROTOCOL_LIST, cpList);
				request.setAttribute(Constants.CP_ID_TITLE_MAP, cpTitleMap);
			}
		}
	}
	/**
	 * This method will set Specimen class in storage type form.
	 * @param storageTypeForm StorageType Form
	 * @param operation Operation Add/Edit
	 * @param bizLogic StorageType BizLogic
	 * @throws BizLogicException BizLogicException
	 */
	private void setSpecimenClass(final StorageContainerForm sceForm,
			final StorageContainerBizLogic bizLogic) throws BizLogicException
	{
		if(sceForm.getId()>0 && 
			"Specimen".equals(sceForm.getSpecimenOrArrayType()))
		{
			final StorageContainer storagecont = (StorageContainer)bizLogic.
			retrieve(StorageContainer.class.getName(), sceForm.getId());
			final Collection<String> spClassTypeCollection = storagecont
			.getHoldsSpecimenClassCollection();
			final Collection<String> spTypeCollection = storagecont.getHoldsSpecimenTypeCollection();
			if (spClassTypeCollection != null && !spClassTypeCollection.isEmpty())
			{
				String [] holdsSpecimenClassTypes = new String[spClassTypeCollection.size()];
				int i = 0;
				final Iterator it = spClassTypeCollection.iterator();
				while (it.hasNext())
				{
					final String specimenClass = (String) it.next();
					holdsSpecimenClassTypes[i] = specimenClass;
					i++;
				}
				sceForm.setHoldsSpecimenClassTypes(holdsSpecimenClassTypes);
			}
			if(spTypeCollection!=null && !spTypeCollection.isEmpty())
			{
				StorageContainerUtil.populateSpType(spClassTypeCollection, spTypeCollection, sceForm);
			}
		}
	}

	/**
	 * @param storageContainerForm
	 *            : storageContainerForm
	 * @param operation
	 *            : operation
	 * @param request
	 *            : request
	 * @throws ApplicationException 
	 */
	private void onTypeChange(StorageContainerForm storageContainerForm, String operation,
			HttpServletRequest request) throws ApplicationException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) factory
				.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		// long typeSelected = -1;

		final String selectedType = String.valueOf(storageContainerForm.getTypeId());
		this.logger.debug(">>>>>>>>>>><<<<<<<<<<<<<<<<>>>>>>>>>>>>> ST : " + selectedType);
		if (selectedType != null && !selectedType.equals("-1"))
		{

			final Object object = bizLogic.retrieve(StorageType.class.getName(),
					storageContainerForm.getTypeId());
			if (object != null)
			{
				final StorageType type = (StorageType) object;
				// setFormAttributesForSelectedType(type,storageContainerForm);
				if (type.getDefaultTempratureInCentigrade() != null)
				{
					storageContainerForm.setDefaultTemperature(type
							.getDefaultTempratureInCentigrade().toString());
				}

				storageContainerForm.setOneDimensionCapacity(type.getCapacity()
						.getOneDimensionCapacity().intValue());
				storageContainerForm.setTwoDimensionCapacity(type.getCapacity()
						.getTwoDimensionCapacity().intValue());
				storageContainerForm.setOneDimensionLabel(type.getOneDimensionLabel());
				storageContainerForm.setTwoDimensionLabel(CommonUtilities.toString(type
						.getTwoDimensionLabel()));
				storageContainerForm.setTypeName(type.getName());

				if (type.getHoldsSpecimenClassCollection().size() > 0)
				{
					storageContainerForm.setSpecimenOrArrayType("Specimen");
				}
				final Collection holdsSpArrayTypeCollection = (Collection) bizLogic
						.retrieveAttribute(StorageType.class.getName(), type.getId(),
								"elements(holdsSpecimenArrayTypeCollection)");
				type.setHoldsSpecimenArrayTypeCollection(holdsSpArrayTypeCollection);
				if (holdsSpArrayTypeCollection.size() > 0)
				{
					storageContainerForm.setSpecimenOrArrayType("SpecimenArray");
				}
				this.logger.debug("Type Name:" + storageContainerForm.getTypeName());
				if (operation != null && operation.equals(Constants.ADD))
				{
					final StorageTypeBizLogic storageTypebizLogic = (StorageTypeBizLogic) factory
							.getBizLogic(Constants.STORAGE_TYPE_FORM_ID);
					final long[] defHoldsStorageTypeList = storageTypebizLogic
							.getDefaultHoldStorageTypeList(type);
					if (defHoldsStorageTypeList != null)
					{
						storageContainerForm.setHoldsStorageTypeIds(defHoldsStorageTypeList);
					}

					final String[] defHoldsSpecimenClassTypeList = storageTypebizLogic
							.getDefaultHoldsSpecimenClasstypeList(type);
					if (defHoldsSpecimenClassTypeList != null)
					{
						storageContainerForm
								.setHoldsSpecimenClassTypes(defHoldsSpecimenClassTypeList);
					}
					final Collection specimenTypeCollection = type.getHoldsSpecimenTypeCollection();
					if(specimenTypeCollection!=null && !specimenTypeCollection.isEmpty())
					{
						StorageContainerUtil.populateSpType(type.getHoldsSpecimenClassCollection(), specimenTypeCollection,
								storageContainerForm);
					}
					final long[] defHoldsSpecimenArrayTypeList = storageTypebizLogic
							.getDefaultHoldSpecimenArrayTypeList(type);
					if (defHoldsSpecimenArrayTypeList != null)
					{
						storageContainerForm
								.setHoldsSpecimenArrTypeIds(defHoldsSpecimenArrayTypeList);
					}
				}
			}

		}
		else
		{
			request.setAttribute("storageType", null);
			storageContainerForm.setDefaultTemperature("");
			storageContainerForm.setOneDimensionCapacity(0);
			storageContainerForm.setTwoDimensionCapacity(0);
			storageContainerForm.setOneDimensionLabel("Dimension One");
			storageContainerForm.setTwoDimensionLabel("Dimension Two");
			storageContainerForm.setTypeName("");
			// type_name="";
		}

	}

	/**
	 * @param containerMap
	 *            : containerMap
	 * @param storageContainerForm
	 *            : storageContainerForm
	 * @param request
	 *            : request
	 * @throws ApplicationException
	 *             : ApplicationException
	 */
	private void setParentStorageContainersForAdd(TreeMap containerMap,
			StorageContainerForm storageContainerForm, HttpServletRequest request, DAO dao)
			throws ApplicationException
	{
		List initialValues = null;

		initialValues = StorageContainerUtil.checkForInitialValues(containerMap);
		if (initialValues != null)
		{
			// Getting the default values in add case
			String[] initValues = new String[3];
			initValues = (String[]) initialValues.get(0);

			// getting collection protocol list and name of the container for
			// default selected parent container

			final Object object = dao.retrieveById(StorageContainer.class.getName(), new Long(
					initValues[0]));
			if (object != null)
			{
				if ("Auto".equals(storageContainerForm.getParentContainerSelected()))
				{
					final StorageContainer container = (StorageContainer) object;
					final Site site = container.getSite();
					if (site != null)
					{
						AppUtility.setCollectionProtocolList(request, site.getId(), dao);
					}
					// storageContainerForm.setCollectionIds(collectionIds);
					// storageContainerForm.setCollectionIds(bizLogic.
					// getDefaultHoldCollectionProtocolList(container));
				}
				// else
				// {
				// storageContainerForm.setCollectionIds(new long[]{-1});
				// }
			}

		}
		request.setAttribute("initValues", initialValues);
	}

	/**
	 * @param containerMap
	 *            : containerMap
	 * @param storageContainerForm
	 *            : storageContainerForm
	 * @param request
	 *            : request
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private void setParentStorageContainersForEdit(TreeMap containerMap,
			StorageContainerForm storageContainerForm, HttpServletRequest request, DAO dao)
			throws BizLogicException
	{
		List initialValues = null;
		try
		{
			if (!Constants.SITE.equals(storageContainerForm.getParentContainerSelected()))
			{
				final String[] startingPoints = new String[]{"-1", "-1", "-1"};

				if (storageContainerForm.getParentContainerId() != -1)
				{
					startingPoints[0] = new Long(storageContainerForm.getParentContainerId())
							.toString();
				}
				if (storageContainerForm.getPositionDimensionOne() != -1)
				{
					startingPoints[1] = new Integer(storageContainerForm.getPositionDimensionOne())
							.toString();
				}
				if (storageContainerForm.getPositionDimensionTwo() != -1)
				{
					startingPoints[2] = new Integer(storageContainerForm.getPositionDimensionTwo())
							.toString();
				}

				initialValues = new Vector();
				initialValues.add(startingPoints);
			}
			else if (Constants.SITE.equals(storageContainerForm.getParentContainerSelected()))
			{
				initialValues = StorageContainerUtil.checkForInitialValues(containerMap);
				// falguni
				// get container name by getting storage container object from db.
				if (storageContainerForm.getContainerName() == null)
				{
					final Object object = dao.retrieveById(StorageContainer.class.getName(),
							storageContainerForm.getId());
					if (object != null)
					{
						final StorageContainer cont = (StorageContainer) object;
						storageContainerForm.setContainerName(cont.getName());
					}
				}

			}

			request.setAttribute("initValues", initialValues);
			StorageContainerUtil.addAllocatedPositionToMap(containerMap, storageContainerForm
					.getParentContainerId(), storageContainerForm.getPositionDimensionOne(),
					storageContainerForm.getPositionDimensionTwo(), dao);
		}
		catch (final DAOException daoEx)
		{
			this.logger.error(daoEx.getMessage(),daoEx);
			daoEx.printStackTrace();
			throw new BizLogicException(daoEx);
		}
	}

	/**
	 * @param request
	 *            : request
	 * @param storageContainerForm
	 *            : storageContainerForm
	 */
	private void setFormAttributesForAddNew(HttpServletRequest request,
			StorageContainerForm storageContainerForm)
	{
		// Mandar : code for Addnew Storage Type data 23-Jan-06
		final String storageTypeID = (String) request
				.getAttribute(Constants.ADD_NEW_STORAGE_TYPE_ID);
		if (storageTypeID != null && storageTypeID.trim().length() > 0)
		{
			this.logger.debug(">>>>>>>>>>><<<<<<<<<<<<<<<<>>>>>>>>>>>>> ST : " + storageTypeID);
			storageContainerForm.setTypeId(Long.parseLong(storageTypeID));
		}
		// -- 23-Jan-06 end
		// Mandar : code for Addnew Site data 24-Jan-06
		final String siteID = (String) request.getAttribute(Constants.ADD_NEW_SITE_ID);
		if (siteID != null && siteID.trim().length() > 0)
		{
			this.logger.debug(" ToSite ID in Distribution Action : " + siteID);
			storageContainerForm.setSiteId(Long.parseLong(siteID));
		}

	}
	
	/**
	 * @param request
	 *            : request
	 * @return long[] : long[]
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private long[] parentContChange(HttpServletRequest request) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) factory
				.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

		final String parentContId = request.getParameter("parentContainerId");
		if (parentContId != null)
		{
			final Object object = bizLogic.retrieve(StorageContainer.class.getName(), new Long(
					parentContId));
			if (object != null)
			{
				final StorageContainer container = (StorageContainer) object;
				return bizLogic.getDefaultHoldCollectionProtocolList(container);
			}
		}
		return new long[]{-1};
	}

	/**
	 * @param storageContainerForm
	 *            : storageContainerForm
	 * @param storageContainerBean
	 *            : storageContainerBean
	 */

	private void initStorageContainerForm(StorageContainerForm storageContainerForm,
			StorageContainerBean storageContainerBean)
	{
		storageContainerForm.setBarcode(storageContainerBean.getBarcode());
		storageContainerForm.setCollectionIds(storageContainerBean.getCollectionIds());
		storageContainerForm.setContainerId(storageContainerBean.getContainerId());
		storageContainerForm.setContainerName(storageContainerBean.getContainerName());
		storageContainerForm.setDefaultTemperature(storageContainerBean.getDefaultTemperature());
		storageContainerForm.setHoldsSpecimenArrTypeIds(storageContainerBean
				.getHoldsSpecimenArrTypeIds());
		storageContainerForm.setHoldsSpecimenClassTypes(storageContainerBean
				.getHoldsSpecimenClassTypes());
		storageContainerForm.setHoldsStorageTypeIds(storageContainerBean.getHoldsStorageTypeIds());
		storageContainerForm.setTypeId(storageContainerBean.getTypeId());
		storageContainerForm.setTypeName(storageContainerBean.getTypeName());
		storageContainerForm.setId(storageContainerBean.getID());
		storageContainerForm.setParentContainerId(storageContainerBean.getParentContainerId());
		storageContainerForm.setPos1(storageContainerBean.getPos1());
		storageContainerForm.setPos2(storageContainerBean.getPos2());
		storageContainerForm
				.setPositionDimensionOne(storageContainerBean.getPositionDimensionOne());
		storageContainerForm
				.setPositionDimensionTwo(storageContainerBean.getPositionDimensionTwo());
		storageContainerForm.setContainerId(storageContainerBean.getContainerId());
		storageContainerForm.setContainerName(storageContainerBean.getContainerName());
	
		storageContainerForm.setHoldsSpecimenArrTypeIds(storageContainerBean
				.getHoldsSpecimenArrTypeIds());
		storageContainerForm.setHoldsSpecimenClassTypes(storageContainerBean
				.getHoldsSpecimenClassTypes());
		storageContainerForm.setHoldsTissueSpType(storageContainerBean.getHoldsTissueSpType());
		storageContainerForm.setHoldsFluidSpType(storageContainerBean.getHoldsFluidSpType());
		storageContainerForm.setHoldsCellSpType(storageContainerBean.getHoldsCellSpType());
		storageContainerForm.setHoldsMolSpType(storageContainerBean.getHoldsMolSpType());
		storageContainerForm.setHoldsStorageTypeIds(storageContainerBean.getHoldsStorageTypeIds());
		storageContainerForm
				.setOneDimensionCapacity(storageContainerBean.getOneDimensionCapacity());
		storageContainerForm
				.setTwoDimensionCapacity(storageContainerBean.getTwoDimensionCapacity());
		storageContainerForm.setOneDimensionLabel(storageContainerBean.getOneDimensionLabel());
		storageContainerForm.setTwoDimensionLabel(storageContainerBean.getTwoDimensionLabel());
		storageContainerForm.setSiteId(storageContainerBean.getSiteId());
		storageContainerForm.setSiteName(storageContainerBean.getSiteName());
		storageContainerForm.setSiteForParentContainer(storageContainerBean
				.getSiteForParentContainer());
		storageContainerForm.setParentContainerSelected(storageContainerBean
				.getParentContainerSelected());
		// 12064 S
		storageContainerForm.setActivityStatus(storageContainerBean.getActivityStatus());
		storageContainerForm.setIsFull(storageContainerBean.getIsFull());
		// 12064 E
	}

}