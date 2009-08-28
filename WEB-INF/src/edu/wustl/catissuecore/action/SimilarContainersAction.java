/*
 * Created on Jul 3, 2006 TODO To change the template for this generated file go
 * to Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.Position;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;

/**
 * @author vaishali_khandelwal TODO To change the template for this generated
 *         type comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class SimilarContainersAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(SimilarContainersAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @throws Exception
	 *             generic exception
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 *
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		this.logger.debug("SimilarContainersAction : executeSecureAction() form: type "
				+ form.getClass());
		DAO dao = null;
		String pageOf = null;
		try
		{
			final SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
					.getAttribute(Constants.SESSION_DATA);
			dao = AppUtility.openDAOSession(sessionDataBean);
			final List<NameValueBean> storagePositionListForTransferEvent = AppUtility
					.getStoragePositionTypeListForTransferEvent();
			request
					.setAttribute("storageListForTransferEvent",
							storagePositionListForTransferEvent);

			final StorageContainerForm similarContainersForm = (StorageContainerForm) form;
			// boolean to indicate whether the suitable containers to be shown
			// in
			// dropdown
			// is exceeding the max limit.
			final String exceedingMaxLimit = "false";
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) factory
					.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
			final String selectedParentContainer = similarContainersForm
					.getParentContainerSelected();
			if (similarContainersForm.getSpecimenOrArrayType() == null)
			{
				similarContainersForm.setSpecimenOrArrayType("Specimen");
			}
			this.logger.info(" Map:---------------"
					+ similarContainersForm.getSimilarContainersMap());
			final IBizLogic ibizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);

			// request =
			//AppUtility.setCollectionProtocolList(request,similarContainersForm
			// .getSiteId());
			if ("Site".equals(similarContainersForm.getParentContainerSelected()))
			{
				request = AppUtility.setCollectionProtocolList(request, similarContainersForm
						.getSiteId(), dao);
			}
			else if ("Auto".equals(similarContainersForm.getParentContainerSelected()))
			{
				final long parentContId = similarContainersForm.getParentContainerId();

				final Site site = bizLogic.getRelatedSite(parentContId, dao);
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
			else if ("Manual".equals(similarContainersForm.getParentContainerSelected()))
			{
				final String selectedContName = similarContainersForm.getSelectedContainerName();

				final Site site = bizLogic.getRelatedSiteForManual(selectedContName, dao);
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

			// Gets the Storage Type List and sets it in request
			final List list2 = dao.retrieve(StorageType.class.getName());
			final List storageTypeListWithAny = AppUtility.getStorageTypeList(list2, true);
			request.setAttribute(Constants.HOLDS_LIST1, storageTypeListWithAny);

			final List storagetypeList = new ArrayList();
			final NameValueBean nvb = new NameValueBean(similarContainersForm.getTypeName(),
					new Long(similarContainersForm.getTypeId()));
			storagetypeList.add(nvb);
			request.setAttribute(Constants.STORAGETYPELIST, storagetypeList);
			pageOf = request.getParameter(Constants.PAGE_OF);

			// Populating the Site Array
			final String[] siteDisplayField = {"name"};
			final String valueField = "id";

			// Added by Ravindra : Non Admin users should see only those sites
			// to
			// which they are associated
			List<NameValueBean> list = null;

			list = ibizLogic.getList(Site.class.getName(), siteDisplayField, valueField, true);

			final List<NameValueBean> tempList = new ArrayList<NameValueBean>();
			tempList.addAll(list);

			if (sessionDataBean != null && !sessionDataBean.isAdmin())
			{
				final Set<Long> idSet = new UserBizLogic().getRelatedSiteIds(sessionDataBean
						.getUserId());
				for (final NameValueBean nmv : list)
				{
					if (!idSet.contains(Long.valueOf(nmv.getValue())))
					{
						if (nmv.getValue().equalsIgnoreCase("-1"))
						{
							continue;
						}
						tempList.remove(nmv);
					}
				}
			}

			list = tempList;
			request.setAttribute(Constants.SITELIST, list);

			// get the Specimen class and type from the cde
			final List specimenClassTypeList = AppUtility.getSpecimenClassTypeListWithAny();
			request.setAttribute(Constants.HOLDS_LIST2, specimenClassTypeList);

			// Gets the Specimen array Type List and sets it in request
			final List list3 = dao.retrieve(SpecimenArrayType.class.getName());
			final List spArrayTypeList = AppUtility.getSpecimenArrayTypeList(list3);
			request.setAttribute(Constants.HOLDS_LIST3, spArrayTypeList);

			request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

			final Object object = dao.retrieveById(StorageType.class.getName(), new Long(
					similarContainersForm.getTypeId()));

			String typeName = "", siteName = "";
			final StorageType storageType = (StorageType) object;
			final String containerType = storageType.getName();
			similarContainersForm.setTypeName(containerType);
			typeName = containerType;
			similarContainersForm.setOneDimensionLabel(storageType.getOneDimensionLabel());
			similarContainersForm.setTwoDimensionLabel(storageType.getTwoDimensionLabel());

			long siteId = similarContainersForm.getSiteId();

			if (Constants.SITE.equals(selectedParentContainer))
			{
				if (siteId != -1)
				{
					final Object siteObject = dao.retrieveById(Site.class.getName(), new Long(
							siteId));
					if (siteObject != null)
					{
						final Site site = (Site) siteObject;
						similarContainersForm.setSiteName(site.getName());
						siteName = site.getName();
						siteId = site.getId().longValue();
					}
				}
			}
			// Suman: for bug 8904
			else if (Constants.STORAGE_TYPE_POSITION_MANUAL.equals(selectedParentContainer))
			{
				this.logger.debug("Long.parseLong(request.getParameter" + "(parentContainerId)..."
						+ request.getParameter("parentContainerId"));
				this.logger.debug("similarContainerForm.getTypeId()......................."
						+ similarContainersForm.getTypeId());
				String parentContId = request.getParameter("parentContainerId");
				// commented for bug:8904
				// if (similarContainersForm.getParentContainerId() == 0)
				{

					final String containerName = similarContainersForm.getSelectedContainerName();

					final String sourceObjectName = StorageContainer.class.getName();
					final String[] selectColumnName = {"id"};
					final String[] whereColumnName = {"name"};
					final String[] whereColumnCondition = {"="};
					final Object[] whereColumnValue = {containerName};
					final String joinCondition = null;
					final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
					queryWhereClause.addCondition(new EqualClause("name", containerName));
					dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

					final List containerIdList = bizLogic.retrieve(sourceObjectName,
							selectColumnName, whereColumnName, whereColumnCondition,
							whereColumnValue, joinCondition);

					if (!containerIdList.isEmpty())
					{
						similarContainersForm.setParentContainerId(((Long) containerIdList.get(0))
								.longValue());

						if (similarContainersForm.getPos1() == null
								|| similarContainersForm.getPos1().equals("")
								|| similarContainersForm.getPos2() == null
								|| similarContainersForm.getPos2().equals(""))
						{
							final Container container = new Container();
							container.setId(similarContainersForm.getParentContainerId());
							final Position position = bizLogic
									.getFirstAvailablePositionInContainer(container, dao);
							if (position != null)
							{
								similarContainersForm.setPos1(position.getXPos() + "");
								similarContainersForm.setPos2(position.getYPos() + "");

							}
							else
							{
								ActionErrors errors = (ActionErrors) request
										.getAttribute(Globals.ERROR_KEY);
								if (errors == null || errors.size() == 0)
								{
									errors = new ActionErrors();
									errors
											.add(
													ActionErrors.GLOBAL_ERROR,
													new ActionError(
															"errors.item.format",
															ApplicationProperties
																	.getValue("storageContainer.parentContainerFull")));
									this.saveErrors(request, errors);
									return (mapping
											.findForward(Constants.PAGE_OF_STORAGE_CONTAINER));
								}
							}
						}

						similarContainersForm.setPositionDimensionOne(Integer
								.parseInt(similarContainersForm.getPos1()));
						similarContainersForm.setPositionDimensionTwo(Integer
								.parseInt(similarContainersForm.getPos2()));
					}
					else
					{
						ActionErrors errors = (ActionErrors) request
								.getAttribute(Globals.ERROR_KEY);
						if (errors == null || errors.size() == 0)
						{
							errors = new ActionErrors();
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"errors.item.format", ApplicationProperties
											.getValue("storageContainer.parentContainer")));
							this.saveErrors(request, errors);
							return (mapping.findForward(Constants.PAGE_OF_STORAGE_CONTAINER));
						}

					}

				}

				if (parentContId == null)
				{
					parentContId = "" + similarContainersForm.getParentContainerId();
				}
				if (parentContId != null)
				{
					final Object containerObject = dao.retrieveById(StorageContainer.class
							.getName(), new Long(parentContId));
					if (containerObject != null)
					{
						final StorageContainer container = (StorageContainer) containerObject;

						final List siteList = dao.retrieveAttribute(StorageContainer.class, "id",
								container.getId(), "site");//container.getSite();
						if ((siteList != null) && (siteList.size() > 0))
						{
							final Site site = (Site) siteList.get(0);
							similarContainersForm.setSiteName(site.getName());
							siteName = site.getName();
							siteId = site.getId().longValue();
							this.logger.debug("Site Name :" + similarContainersForm.getSiteName());
						}
					}
				}
			}

			similarContainersForm.setSiteName(siteName);
			similarContainersForm.setSiteId(siteId);
			// request.setAttribute("siteName", siteName);
			// request.setAttribute("siteId", new Long(siteId));

			// code to set Max(IDENTIFIER) in storage container table
			// used for suffixing Unique numbers to auto-generated container
			// name
			/*
			 * by falguni long maxId = bizLogic.getNextContainerNumber();
			 * request.setAttribute(Constants.MAX_IDENTIFIER,
			 * Long.toString(maxId)); request.setAttribute("ContainerNumber",
			 * new Long(maxId).toString());
			 */
			if ("Auto".equals(selectedParentContainer))
			{
				similarContainersForm.setSelectedContainerName(null);
			}
			final TreeMap containerMap = bizLogic.getAllocatedContainerMapForContainer(new Long(
					request.getParameter("typeId")).longValue(), exceedingMaxLimit,
					similarContainersForm.getSelectedContainerName(), sessionDataBean, dao);

			/*
			 * Map containerMap1 =
			 * bizLogic.getAllocatedContaienrMapForContainer(new Long(request
			 * .getParameter("typeId")).longValue());
			 */
			request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
			request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
			// request.setAttribute("siteForParentList", siteList1);
			final int noOfContainers = Integer.parseInt(request.getParameter("noOfContainers"));
			if (similarContainersForm.getSimilarContainersMap().size() == 0)
			{
				int siteOrParentCont;
				if (Constants.SITE.equals(selectedParentContainer))
				{
					siteOrParentCont = 1;
				}
				else
				{
					siteOrParentCont = 2;
				}
				similarContainersForm.setSimilarContainerMapValue("checkedButton", Integer
						.toString(siteOrParentCont));

				if (!Constants.SITE.equals(selectedParentContainer))
				{

					// List mapSiteList =
					// bizLogic.getAllocatedContaienrMapForContainer(new
					// Long(request
					// .getParameter("typeId")).longValue());
					// Map containerMap = (Map) mapSiteList.get(0);
					// List siteNameList = (List) mapSiteList.get(1);
					// request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,
					// containerMap);
					// request.setAttribute("siteForParentList", siteNameList);
					final String[] startingPoints = new String[3];

					startingPoints[0] = Long.toString(similarContainersForm.getParentContainerId());
					startingPoints[1] = Integer.toString(similarContainersForm
							.getPositionDimensionOne());
					startingPoints[2] = Integer.toString(similarContainersForm
							.getPositionDimensionTwo());

					if (similarContainersForm.getParentContainerId() != 0l)
					{
						Vector initialValues = null;
						try
						{
							initialValues = this.getInitalValues(startingPoints, containerMap,
									noOfContainers);
						}
						catch (final Exception e)
						{
							this.logger.debug(e.getMessage(), e);
							ActionErrors errors = (ActionErrors) request
									.getAttribute(Globals.ERROR_KEY);
							if (errors == null || errors.size() == 0)
							{
								errors = new ActionErrors();
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"errors.item.format", ApplicationProperties
												.getValue("storageContainer."
														+ "parentContainerPostionInUseOrExceed")));
								this.saveErrors(request, errors);
								return (mapping.findForward(Constants.PAGE_OF_STORAGE_CONTAINER));
							}
						}
						request.setAttribute("initValues", initialValues);
					}

					if (!Constants.SITE.equals(selectedParentContainer)
							&& !(this.checkAvailability(containerMap, noOfContainers)))
					{
						ActionErrors errors = (ActionErrors) request
								.getAttribute(Globals.ERROR_KEY);
						if (errors == null)
						{
							errors = new ActionErrors();
						}
						System.out.println("errors " + errors + ", ActionErrors.GLOBAL_ERROR "
								+ ActionErrors.GLOBAL_ERROR + ", new ActionError"
								+ "(\"errors.storageContainer.overflow\") "
								+ new ActionError("errors.storageContainer.overflow"));
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"errors.storageContainer.overflow"));
						pageOf = Constants.PAGE_OF_STORAGE_CONTAINER;
						this.saveErrors(request, errors);
					}
				}
				for (int i = 1; i <= noOfContainers; i++)
				{
					// Poornima:Max length of site name is 50 and Max length of
					// container type name is 100, in Oracle the name does not
					// truncate
					// and it is giving error. So these fields are truncated in
					// case
					// it is longer than 40.
					// It also solves Bug 2829:System fails to create a default
					// unique storage container name
					String maxSiteName = siteName;
					if (siteName.length() > 40)
					{
						maxSiteName = siteName.substring(0, 39);
					}
					if (typeName.length() > 40)
					{
						typeName.substring(0, 39);
					}
					// falguni
					// similarContainersForm.setSimilarContainerMapValue(
					// "simCont:"
					// + i + "_name", maxSiteName + "_" + maxTypeName + "_" +
					// (maxId
					// + i - 1));
					if (Constants.SITE.equals(selectedParentContainer))
					{
						similarContainersForm.setSimilarContainerMapValue("simCont:" + i
								+ "_siteId", new Long(siteId).toString());
						similarContainersForm.setSimilarContainerMapValue("simCont:" + i
								+ "_siteName", maxSiteName.toString());
					}

				}

				final String contName = similarContainersForm.getContainerName();
				final String barcode = similarContainersForm.getBarcode();

				this.logger.debug("contName " + contName + " barcode " + barcode + " <<<<---");
				similarContainersForm.setSimilarContainerMapValue("simCont:1_name", contName);
				similarContainersForm.setSimilarContainerMapValue("simCont:1_barcode", barcode);

				request.setAttribute(Constants.PAGE_OF, pageOf);

			}

			final String change = request.getParameter("ResetName");
			if (change != null && !change.equals(""))
			{
				final int i = Integer.parseInt(change);
				if (Constants.SITE.equals(selectedParentContainer))
				{
					final String Id = (String) similarContainersForm
							.getSimilarContainerMapValue("simCont:" + i + "_siteId");
					final Object siteObject2 = dao.retrieveById(Site.class.getName(), new Long(Id));
					if (siteObject2 != null)
					{
						final Site site = (Site) siteObject2;
						similarContainersForm.setSiteName(site.getName());
						siteName = site.getName();
						siteId = site.getId().longValue();
					}
				}

				// falguni
				// similarContainersForm.setSimilarContainerMapValue("simCont:"
				// + i
				// + "_name", siteName + "_" + typeName + "_" + (maxId + i -
				// 1));

			}

			final String errorStr = request.getParameter("error");
			if (errorStr != null && errorStr.equals("true"))
			{

				final Vector returner = new Vector();
				for (int i = 0; i < noOfContainers; i++)
				{

					/*
					 * simCont:1_positionDimensionOne=4
					 * simCont:1_parentContainerId=2 ,
					 * simCont:1_positionDimensionTwo=3,
					 */
					final String[] initValues = new String[3];
					initValues[0] = (String) similarContainersForm
							.getSimilarContainerMapValue("simCont:" + (i + 1)
									+ "_parentContainerId");
					initValues[1] = (String) similarContainersForm
							.getSimilarContainerMapValue("simCont:" + (i + 1)
									+ "_positionDimensionOne");
					initValues[2] = (String) similarContainersForm
							.getSimilarContainerMapValue("simCont:" + (i + 1)
									+ "_positionDimensionTwo");
					returner.add(initValues);
				}

				request.setAttribute("initValues", returner);

			}
			this.logger.debug("Similar container map value:"
					+ similarContainersForm.getSimilarContainersMap());
		}
		finally
		{
			dao.closeSession();
		}
		return mapping.findForward(pageOf);
	}

	/**
	 * @param dataMap : dataMap
	 * @param noOfContainersNeeded : noOfContainersNeeded
	 * @return boolean
	 */
	private boolean checkAvailability(Map dataMap, int noOfContainersNeeded)
	{
		int counter = 0;
		final Iterator dMapIter = dataMap.keySet().iterator();
		while (dMapIter.hasNext())
		{
			final Map xMap = (Map) dataMap.get(dMapIter.next());
			final Iterator xMapIter = xMap.keySet().iterator();
			while (xMapIter.hasNext())
			{
				final List yList = (List) xMap.get(xMapIter.next());
				counter += yList.size();
			}
		}
		if (noOfContainersNeeded > counter)
		{
			return false;
		}
		return true;
	}

	/**
	 * @param startingPoint : startingPoint
	 * @param dMap : dMap
	 * @param noOfContainers : noOfContainers
	 * @return : Vector
	 */
	private Vector getInitalValues(String[] startingPoint, Map dMap, int noOfContainers)
	{
		final Vector returner = new Vector();
		String[] initValues = new String[3];
		final Iterator dMapIter = dMap.keySet().iterator();
		NameValueBean dMapKey;
		NameValueBean xMapKey;
		NameValueBean yListKey;
		do
		{
			dMapKey = (NameValueBean) dMapIter.next();
		}
		while (!(dMapKey.getValue().equals(startingPoint[0])));
		Map xMap = (Map) dMap.get(dMapKey);

		Iterator xMapIter = xMap.keySet().iterator();

		do
		{
			xMapKey = (NameValueBean) xMapIter.next();
		}
		while (!(xMapKey.getValue().equals(startingPoint[1])));
		List yList = (List) xMap.get(xMapKey);

		Iterator yListIter = yList.iterator();
		do
		{
			yListKey = (NameValueBean) yListIter.next();
		}
		while (!yListKey.getValue().equals(startingPoint[2]));

		initValues[0] = dMapKey.getValue();
		initValues[1] = xMapKey.getValue();
		initValues[2] = yListKey.getValue();

		returner.add(initValues);

		for (int i = 1; i < noOfContainers; i++)
		{
			initValues = new String[]{"", "", ""};

			if (yListIter.hasNext())
			{
				yListKey = (NameValueBean) yListIter.next();
				initValues[0] = dMapKey.getValue();
				initValues[1] = xMapKey.getValue();
				initValues[2] = yListKey.getValue();
			}
			else
			{
				if (xMapIter.hasNext())
				{
					xMapKey = (NameValueBean) xMapIter.next();
					yList = (List) xMap.get(xMapKey);
					yListIter = yList.iterator();
					yListKey = (NameValueBean) yListIter.next();

					initValues[0] = dMapKey.getValue();
					initValues[1] = xMapKey.getValue();
					initValues[2] = yListKey.getValue();

				}
				else
				{
					if (dMapIter.hasNext())
					{
						dMapKey = (NameValueBean) dMapIter.next();
						xMap = (Map) dMap.get(dMapKey);
						xMapIter = xMap.keySet().iterator();
						xMapKey = (NameValueBean) xMapIter.next();
						yList = (List) xMap.get(xMapKey);
						yListIter = yList.iterator();
						yListKey = (NameValueBean) yListIter.next();

						initValues[0] = dMapKey.getValue();
						initValues[1] = xMapKey.getValue();
						initValues[2] = yListKey.getValue();

					}
				}
			}

			returner.add(initValues);
		}

		return returner;
	}

}