/*
 * Created on Jul 3, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.CacheException;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * @author vaishali_khandelwal
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimilarContainersAction extends SecureAction
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.SecureAction#executeSecureAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		Logger.out.debug("SimilarContainersAction : executeSecureAction() form: type " + form.getClass());

		StorageContainerForm similarContainersForm = (StorageContainerForm) form;
		//boolean to indicate whether the suitable containers to be shown in dropdown 
		//is exceeding the max limit.
		String exceedingMaxLimit = "false";
		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		String selectedParentContainer=similarContainersForm.getParentContainerSelected();
		if (similarContainersForm.getSpecimenOrArrayType() == null)
		{
			similarContainersForm.setSpecimenOrArrayType("Specimen");
		}
		Logger.out.info(" Map:---------------" + similarContainersForm.getSimilarContainersMap());
		IBizLogic ibizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);

		//    	populating collection protocol list.
		List list1 = ibizLogic.retrieve(CollectionProtocol.class.getName());
		List collectionProtocolList = Utility.getCollectionProtocolList(list1);
		request.setAttribute(Constants.PROTOCOL_LIST, collectionProtocolList);

		//Gets the Storage Type List and sets it in request 
		List list2 = ibizLogic.retrieve(StorageType.class.getName());
		List storageTypeListWithAny = Utility.getStorageTypeList(list2, true);
		request.setAttribute(Constants.HOLDS_LIST1, storageTypeListWithAny);

		List storagetypeList = new ArrayList();
		NameValueBean nvb = new NameValueBean(similarContainersForm.getTypeName(), new Long(similarContainersForm.getTypeId()));
		storagetypeList.add(nvb);
		request.setAttribute(Constants.STORAGETYPELIST, storagetypeList);
		String pageOf = request.getParameter(Constants.PAGEOF);

		//		Populating the Site Array
		String[] siteDisplayField = {"name"};
		String valueField = "id";
		
		// Added by Ravindra : Non Admin users should see only those sites to which they are associated
		List<NameValueBean> list = null;
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
		list = ibizLogic.getList(Site.class.getName(), siteDisplayField, valueField, true);
		
		List<NameValueBean> tempList = new ArrayList<NameValueBean>();
		tempList.addAll(list);
		
		if(sessionDataBean!=null && !sessionDataBean.isAdmin())
		{
			Set<Long> idSet = new UserBizLogic().getRelatedSiteIds(sessionDataBean.getUserId());
			for(NameValueBean nmv : list)
			{
				if(!idSet.contains(Long.valueOf(nmv.getValue())))
				{
					if(nmv.getValue().equalsIgnoreCase("-1"))
						continue;
					tempList.remove(nmv);
				}
			}
		}
		
		list = tempList;
		request.setAttribute(Constants.SITELIST, list);

		// get the Specimen class and type from the cde
		List specimenClassTypeList = Utility.getSpecimenClassTypeListWithAny();
		request.setAttribute(Constants.HOLDS_LIST2, specimenClassTypeList);

		//Gets the Specimen array Type List and sets it in request
		List list3 = ibizLogic.retrieve(SpecimenArrayType.class.getName());
		List spArrayTypeList = Utility.getSpecimenArrayTypeList(list3);
		request.setAttribute(Constants.HOLDS_LIST3, spArrayTypeList);

		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

		Object object = ibizLogic.retrieve(StorageType.class.getName(), new Long(similarContainersForm
				.getTypeId()));
		
		String typeName = "", siteName = "";
		StorageType storageType = (StorageType) object;
		String containerType = storageType.getName();
		similarContainersForm.setTypeName(containerType);
		typeName = containerType;
		similarContainersForm.setOneDimensionLabel(storageType.getOneDimensionLabel());
		similarContainersForm.setTwoDimensionLabel(storageType.getTwoDimensionLabel());


		long siteId = similarContainersForm.getSiteId();
		
		if(Constants.SITE.equals(selectedParentContainer))
		{
			Object siteObject = ibizLogic.retrieve(Site.class.getName(), new Long(similarContainersForm.getSiteId()));
			if (siteObject != null)
			{
				Site site = (Site) siteObject;
				similarContainersForm.setSiteName(site.getName());
				siteName = site.getName();
				siteId = site.getId().longValue();
			}
		}
		//Suman: for bug 8904
		else if (Constants.STORAGE_TYPE_POSITION_MANUAL.equals(selectedParentContainer))
		{
			Logger.out.debug("Long.parseLong(request.getParameter(parentContainerId)......................."
					+ request.getParameter("parentContainerId"));
			Logger.out.debug("similarContainerForm.getTypeId()......................." + similarContainersForm.getTypeId());
			String parentContId = request.getParameter("parentContainerId");
			//commented for bug:8904
			//if (similarContainersForm.getParentContainerId() == 0)
			{

				String containerName = similarContainersForm.getSelectedContainerName();

				StorageContainer storageContainerObj = new StorageContainer();
				String sourceObjectName = StorageContainer.class.getName();
				String[] selectColumnName = {"id"};
				String[] whereColumnName = {"name"};
				String[] whereColumnCondition = {"="};
				Object[] whereColumnValue = {containerName};
				String joinCondition = null;

				List containerIdList = bizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue,
						joinCondition);

				if (!containerIdList.isEmpty())
				{
					similarContainersForm.setParentContainerId(((Long) containerIdList.get(0)).longValue());
					
					
					
					boolean isContainerFull = false;
					/**
					 *  Following code is added to set the x and y dimension in case only storage container is given 
					 *  and x and y positions are not given 
					 */
					
					if (similarContainersForm.getPos1() == null || similarContainersForm.getPos1().equals("") ||similarContainersForm.getPos2() == null || similarContainersForm.getPos2().equals(""))
					{
					isContainerFull = true;
					Map containerMapFromCache = null;
					try
					{
						containerMapFromCache = (TreeMap) StorageContainerUtil.getContainerMapFromCache();
					}
					catch (CacheException e)
					{
						e.printStackTrace();
					}
					
					if (containerMapFromCache != null)
					{
						Iterator itr = containerMapFromCache.keySet().iterator();
						while (itr.hasNext())
						{
							nvb = (NameValueBean) itr.next();
							if(nvb.getValue().toString().equals("" + similarContainersForm.getParentContainerId()))
							{
							
								Map tempMap = (Map) containerMapFromCache.get(nvb);
								Iterator tempIterator = tempMap.keySet().iterator();;
								NameValueBean nvb1 = (NameValueBean) tempIterator.next();
								
								List yList = (List) tempMap.get(nvb1);
								NameValueBean nvb2 = (NameValueBean) yList.get(0);
												
								similarContainersForm.setPos1(nvb1.getValue());
								similarContainersForm.setPos2(nvb2.getValue());
							    isContainerFull = false;
							    break;
							}
							
						}
					}
			
					if(isContainerFull)
					{
						ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
						if (errors == null || errors.size() == 0)
						{
							errors = new ActionErrors();
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
									.getValue("storageContainer.parentContainerFull")));
							saveErrors(request, errors);
							return (mapping.findForward(Constants.PAGEOF_STORAGE_CONTAINER));
						}

					
					}
			}
					
    				similarContainersForm.setPositionDimensionOne(Integer.parseInt(similarContainersForm.getPos1()));
					similarContainersForm.setPositionDimensionTwo(Integer.parseInt(similarContainersForm.getPos2()));
				}
				else
				{
					ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
					if (errors == null || errors.size() == 0)
					{
						errors = new ActionErrors();
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
								.getValue("storageContainer.parentContainer")));
						saveErrors(request, errors);
						return (mapping.findForward(Constants.PAGEOF_STORAGE_CONTAINER));
					}

				}

			}

			if (parentContId == null)
			{
				parentContId = "" + similarContainersForm.getParentContainerId();
			}
			if (parentContId != null)
			{
				Object containerObject = ibizLogic.retrieve(StorageContainer.class.getName(), new Long(parentContId));
				if (containerObject != null)
				{
					StorageContainer container = (StorageContainer) containerObject;
					
					Site site = (Site)bizLogic.retrieveAttribute(StorageContainer.class.getName(), container.getId(), "site");//container.getSite();
					similarContainersForm.setSiteName(site.getName());
					siteName = site.getName();
					siteId = site.getId().longValue();
					Logger.out.debug("Site Name :" + similarContainersForm.getSiteName());
				}
			}
		}

		similarContainersForm.setSiteName(siteName);
		similarContainersForm.setSiteId(siteId);
		//request.setAttribute("siteName", siteName);
		//request.setAttribute("siteId", new Long(siteId));

		// code to set Max(IDENTIFIER) in storage container table 
		// used for suffixing Unique numbers to auto-generated container name
		/*
		 * by falguni
		 * long maxId = bizLogic.getNextContainerNumber();
		request.setAttribute(Constants.MAX_IDENTIFIER, Long.toString(maxId));
		request.setAttribute("ContainerNumber", new Long(maxId).toString());*/
		if("Auto".equals(selectedParentContainer))
		{
			similarContainersForm.setSelectedContainerName(null);
		}
		TreeMap containerMap = bizLogic.getAllocatedContaienrMapForContainer(new Long(request.getParameter("typeId")).longValue(), exceedingMaxLimit,similarContainersForm.getSelectedContainerName(), sessionDataBean);
		
		/*Map containerMap1 = bizLogic.getAllocatedContaienrMapForContainer(new Long(request
		 .getParameter("typeId")).longValue());*/
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
		request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
		//request.setAttribute("siteForParentList", siteList1);
		int noOfContainers = Integer.parseInt((String) request.getParameter("noOfContainers"));
		if (similarContainersForm.getSimilarContainersMap().size() == 0)
		{
			int siteOrParentCont;
			if (Constants.SITE.equals(selectedParentContainer))
			{
				siteOrParentCont=1;
			}
			else
			{
				siteOrParentCont=2;
			}
			similarContainersForm.setSimilarContainerMapValue("checkedButton", Integer.toString(siteOrParentCont));

			if (!Constants.SITE.equals(selectedParentContainer))
			{

				//List mapSiteList = bizLogic.getAllocatedContaienrMapForContainer(new Long(request
				//	.getParameter("typeId")).longValue());
				//Map containerMap = (Map) mapSiteList.get(0);
				//List siteNameList = (List) mapSiteList.get(1);
				//request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
				//request.setAttribute("siteForParentList", siteNameList);
				String[] startingPoints = new String[3];

				startingPoints[0] = Long.toString(similarContainersForm.getParentContainerId());
				startingPoints[1] = Integer.toString(similarContainersForm.getPositionDimensionOne());
				startingPoints[2] = Integer.toString(similarContainersForm.getPositionDimensionTwo());

				if (similarContainersForm.getParentContainerId() != 0l)
				{
					Vector initialValues = null;
					try
					{
					initialValues = getInitalValues(startingPoints, containerMap, noOfContainers);
					}
					catch(Exception e)
					{
						ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
						if (errors == null || errors.size() == 0)
						{
							errors = new ActionErrors();
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
									.getValue("storageContainer.parentContainerPostionInUseOrExceed")));
							saveErrors(request, errors);
							return (mapping.findForward(Constants.PAGEOF_STORAGE_CONTAINER));
						}
					}
					request.setAttribute("initValues", initialValues);
				}

				if (!Constants.SITE.equals(selectedParentContainer) && !(checkAvailability(containerMap, noOfContainers)))
				{
					ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
					if (errors == null)
					{
						errors = new ActionErrors();
					}
					System.out.println("errors " + errors + ", ActionErrors.GLOBAL_ERROR " + ActionErrors.GLOBAL_ERROR
							+ ", new ActionError(\"errors.storageContainer.overflow\") " + new ActionError("errors.storageContainer.overflow"));
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.storageContainer.overflow"));
					pageOf = Constants.PAGEOF_STORAGE_CONTAINER;
					saveErrors(request, errors);
				}
			}
			for (int i = 1; i <= noOfContainers; i++)
			{
				//Poornima:Max length of site name is 50 and Max length of container type name is 100, in Oracle the name does not truncate 
				//and it is giving error. So these fields are truncated in case it is longer than 40.
				//It also solves Bug 2829:System fails to create a default unique storage container name
				String maxSiteName = siteName;
				String maxTypeName = typeName;
				if (siteName.length() > 40)
				{
					maxSiteName = siteName.substring(0, 39);
				}
				if (typeName.length() > 40)
				{
					maxTypeName = typeName.substring(0, 39);
				}
				//falguni
				//similarContainersForm.setSimilarContainerMapValue("simCont:" + i + "_name", maxSiteName + "_" + maxTypeName + "_" + (maxId + i - 1));
				if (Constants.SITE.equals(selectedParentContainer))
				{
					similarContainersForm.setSimilarContainerMapValue("simCont:" + i + "_siteId", new Long(siteId).toString());
					similarContainersForm.setSimilarContainerMapValue("simCont:" + i + "_siteName",maxSiteName.toString());
				}

			}

			String contName = similarContainersForm.getContainerName();
			String barcode = similarContainersForm.getBarcode();

			Logger.out.debug("contName " + contName + " barcode " + barcode + " <<<<---");
			similarContainersForm.setSimilarContainerMapValue("simCont:1_name", contName);
			similarContainersForm.setSimilarContainerMapValue("simCont:1_barcode", barcode);

			request.setAttribute(Constants.PAGEOF, pageOf);

		}

		String change = request.getParameter("ResetName");
		if (change != null && !change.equals(""))
		{
			int i = Integer.parseInt(change);
			if (Constants.SITE.equals(selectedParentContainer))
			{
				String Id = (String) similarContainersForm.getSimilarContainerMapValue("simCont:" + i + "_siteId");
				Object siteObject2 = ibizLogic.retrieve(Site.class.getName(), new Long(Id));
				if (siteObject2 != null)
				{
					Site site = (Site) siteObject2;
					similarContainersForm.setSiteName(site.getName());
					siteName = site.getName();
					siteId = site.getId().longValue();
				}
			}

			//falguni
			//similarContainersForm.setSimilarContainerMapValue("simCont:" + i + "_name", siteName + "_" + typeName + "_" + (maxId + i - 1));

		}

		String errorStr = request.getParameter("error");
		if (errorStr != null && errorStr.equals("true"))
		{

			Vector returner = new Vector();
			for (int i = 0; i < noOfContainers; i++)
			{

				/*simCont:1_positionDimensionOne=4
				 simCont:1_parentContainerId=2
				 , simCont:1_positionDimensionTwo=3,*/
				String[] initValues = new String[3];
				initValues[0] = (String) similarContainersForm.getSimilarContainerMapValue("simCont:" + (i + 1) + "_parentContainerId");
				initValues[1] = (String) similarContainersForm.getSimilarContainerMapValue("simCont:" + (i + 1) + "_positionDimensionOne");
				initValues[2] = (String) similarContainersForm.getSimilarContainerMapValue("simCont:" + (i + 1) + "_positionDimensionTwo");
				returner.add(initValues);
			}

			request.setAttribute("initValues", returner);

		}
		Logger.out.debug("Similar container map value:" + similarContainersForm.getSimilarContainersMap());

		return mapping.findForward(pageOf);
	}

	private boolean checkAvailability(Map dataMap, int noOfContainersNeeded)
	{
		int counter = 0;
		Iterator dMapIter = dataMap.keySet().iterator();
		while (dMapIter.hasNext())
		{
			Map xMap = (Map) dataMap.get(dMapIter.next());
			Iterator xMapIter = xMap.keySet().iterator();
			while (xMapIter.hasNext())
			{
				List yList = (List) xMap.get(xMapIter.next());
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
	 * @param startingPoint
	 * @param dMap
	 * @param noOfContainers
	 * @return
	 */
	private Vector getInitalValues(String[] startingPoint, Map dMap, int noOfContainers)
	{
		Vector returner = new Vector();
		String[] initValues = new String[3];
		Iterator dMapIter = dMap.keySet().iterator();
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