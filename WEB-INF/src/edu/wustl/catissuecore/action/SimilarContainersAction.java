/*
 * Created on Jul 3, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
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
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Logger.out.debug("SimilarContainersAction : executeSecureAction() form: type "
				+ form.getClass());

		StorageContainerForm similarContainersForm = (StorageContainerForm) form;

		Logger.out.info(" Map:---------------" + similarContainersForm.getSimilarContainersMap());
		IBizLogic ibizLogic = BizLogicFactory.getInstance()
				.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);

		//    	populating collection protocol list.
		List list1 = ibizLogic.retrieve(CollectionProtocol.class.getName());
		List collectionProtocolList = Utility.getCollectionProtocolList(list1);
		request.setAttribute(Constants.PROTOCOL_LIST, collectionProtocolList);

		//Gets the Storage Type List and sets it in request 
		List list2 = ibizLogic.retrieve(StorageType.class.getName());
		List storageTypeListWithAny = Utility.getStorageTypeList(list2, true);
		request.setAttribute(Constants.HOLDS_LIST1, storageTypeListWithAny);

		List StorageTypeListWithoutAny = Utility.getStorageTypeList(list2, false);
		request.setAttribute(Constants.STORAGETYPELIST, StorageTypeListWithoutAny);

		//		Populating the Site Array
		String[] siteDisplayField = {"name"};
		String valueField = "id";
		List list = ibizLogic.getList(Site.class.getName(), siteDisplayField, valueField, true);
		request.setAttribute(Constants.SITELIST, list);

		// get the Specimen class and type from the cde
    	List specimenClassTypeList=Utility.getSpecimenClassTypeListWithAny();
	  	request.setAttribute(Constants.HOLDS_LIST2, specimenClassTypeList);

		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

		List containerTypeList = ibizLogic.retrieve(StorageType.class.getName(),
				Constants.SYSTEM_IDENTIFIER, new Long(similarContainersForm.getTypeId()));
		Iterator iter = containerTypeList.iterator();
		String typeName = "", siteName = "";
		while (iter.hasNext())
		{
			StorageType storageType = (StorageType) iter.next();
			String containerType = storageType.getName();
			similarContainersForm.setTypeName(containerType);
			typeName = containerType;
			similarContainersForm.setOneDimensionLabel(storageType.getOneDimensionLabel());
			similarContainersForm.setTwoDimensionLabel(storageType.getTwoDimensionLabel());

		}

		long siteId = similarContainersForm.getSiteId();
		String valueField1 = "id";
		if (similarContainersForm.getCheckedButton() == 1)
		{
			List siteList = ibizLogic.retrieve(Site.class.getName(), valueField1, new Long(
					similarContainersForm.getSiteId()));
			if (!siteList.isEmpty())
			{
				Site site = (Site) siteList.get(0);
				similarContainersForm.setSiteName(site.getName());
				siteName = site.getName();
				siteId = site.getId().longValue();
			}
		}
		else
		{
			Logger.out
					.debug("Long.parseLong(request.getParameter(parentContainerId)......................."
							+ request.getParameter("parentContainerId"));
			Logger.out.debug("similarContainerForm.getTypeId()......................."
					+ similarContainersForm.getTypeId());
			String parentContId = request.getParameter("parentContainerId");
			if (parentContId != null)
			{
				List containerList = ibizLogic.retrieve(StorageContainer.class.getName(),
						valueField1, new Long(parentContId));
				if (!containerList.isEmpty())
				{
					StorageContainer container = (StorageContainer) containerList.get(0);
					similarContainersForm.setSiteName(container.getSite().getName());
					siteName = container.getSite().getName();
					siteId = container.getSite().getId().longValue();
					Logger.out.debug("Site Name :" + similarContainersForm.getSiteName());
				}
			}
		}

		similarContainersForm.setSiteName(siteName);
		similarContainersForm.setSiteId(siteId);
		//request.setAttribute("siteName", siteName);
		//request.setAttribute("siteId", new Long(siteId));

		String pageOf = request.getParameter(Constants.PAGEOF);
		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory
				.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

		// code to set Max(IDENTIFIER) in storage container table 
		// used for suffixing Unique numbers to auto-generated container name
		long maxId = bizLogic.getNextContainerNumber();
		request.setAttribute(Constants.MAX_IDENTIFIER, Long.toString(maxId));
		request.setAttribute("ContainerNumber",new Long(maxId).toString());
		List mapSiteList1 = bizLogic.getAllocatedContaienrMapForContainer(new Long(request
				.getParameter("typeId")).longValue());
		Map containerMap1 = (Map)mapSiteList1.get(0);
		List siteList1 = (List)mapSiteList1.get(1);
		/*Map containerMap1 = bizLogic.getAllocatedContaienrMapForContainer(new Long(request
				.getParameter("typeId")).longValue());*/
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap1);
		request.setAttribute("siteForParentList",siteList1);
		

		if (similarContainersForm.getSimilarContainersMap().size() == 0)
		{
			Logger.out.info("inside sixe 0 condition---------------------");
			int noOfContainers = Integer.parseInt((String) request.getParameter("noOfContainers"));
			int siteOrParentCont = similarContainersForm.getCheckedButton();
			Logger.out.debug("similarContainersForm.getSimilarContainerMapValue(checkedButton) "
					+ similarContainersForm.getSimilarContainerMapValue("checkedButton"));
			Logger.out.debug("siteOrParentCont " + siteOrParentCont);
			similarContainersForm.setSimilarContainerMapValue("checkedButton", Integer
					.toString(siteOrParentCont));

			Logger.out.info(" Map:---------------"
					+ similarContainersForm.getSimilarContainersMap());
			if (siteOrParentCont == 2)
			{

				List mapSiteList = bizLogic.getAllocatedContaienrMapForContainer(new Long(request
						.getParameter("typeId")).longValue());
				Map containerMap = (Map)mapSiteList.get(0);
				List siteNameList = (List)mapSiteList.get(1);
				request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
				request.setAttribute("siteForParentList",siteNameList);
				String[] startingPoints = new String[3];
				startingPoints[0] = Long.toString(similarContainersForm.getParentContainerId());
				startingPoints[1] = Integer.toString(similarContainersForm
						.getPositionDimensionOne());
				startingPoints[2] = Integer.toString(similarContainersForm
						.getPositionDimensionTwo());
				Logger.out.debug("similarContainersForm.getParentContainerId() #$% "
						+ similarContainersForm.getParentContainerId());
				if (similarContainersForm.getParentContainerId() != 0l)
				{
					Vector initialValues = getInitalValues(startingPoints, containerMap,
							noOfContainers);
					request.setAttribute("initValues", initialValues);
				}

				if (similarContainersForm.getCheckedButton() == 2
						&& !(checkAvailability(containerMap, noOfContainers)))
				{
					ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
					if (errors == null)
					{
						errors = new ActionErrors();
					}
					System.out.println("errors " + errors + ", ActionErrors.GLOBAL_ERROR "
							+ ActionErrors.GLOBAL_ERROR
							+ ", new ActionError(\"errors.storageContainer.overflow\") "
							+ new ActionError("errors.storageContainer.overflow"));
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"errors.storageContainer.overflow"));
					pageOf = Constants.PAGEOF_STORAGE_CONTAINER;
					saveErrors(request, errors);
				}
			}
			for (int i = 1; i <= noOfContainers; i++)
			{

				similarContainersForm.setSimilarContainerMapValue("simCont:" + i + "_name",
						siteName + "_" + typeName + "_" + (maxId + i - 1));
				if (similarContainersForm.getCheckedButton() == 1)
				{
					similarContainersForm.setSimilarContainerMapValue("simCont:" + i + "_siteId",
							new Long(siteId).toString());
				}

			}

			String contName = similarContainersForm.getContainerName();
			String barcode = similarContainersForm.getBarcode();

			Logger.out.debug("contName " + contName + " barcode " + barcode + " <<<<---");
			similarContainersForm.setSimilarContainerMapValue("simCont:1_name", contName);
			similarContainersForm.setSimilarContainerMapValue("simCont:1_barcode", barcode);

			request.setAttribute(Constants.PAGEOF, pageOf);
			System.out.println("SimilarContainersAction pageOf " + pageOf);

		}

		String change = request.getParameter("ResetName");
		if (change != null && !change.equals(""))
		{
			int i = Integer.parseInt(change);
			if (similarContainersForm.getCheckedButton() == 1)
			{
				String Id = (String) similarContainersForm.getSimilarContainerMapValue("simCont:"
						+ i + "_siteId");
				List siteList = ibizLogic.retrieve(Site.class.getName(), valueField1, Id);
				if (!siteList.isEmpty())
				{
					Site site = (Site) siteList.get(0);
					similarContainersForm.setSiteName(site.getName());
					siteName = site.getName();
					siteId = site.getId().longValue();
				}
			}

			similarContainersForm.setSimilarContainerMapValue("simCont:" + i + "_name", siteName
					+ "_" + typeName + "_" + (maxId + i - 1));

		}
		Logger.out.info("Similar container map value:"
				+ similarContainersForm.getSimilarContainersMap());

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

	//	public static void main(String args[])
	//	{
	//		Map dMap = new TreeMap();
	//		Map xMap = new TreeMap();
	//		List yList = new Vector();
	//		int counter = 0;
	//		int counter1 = 0;
	//		for(int i = 0 ; i < 3 ; i++)
	//		{
	//			xMap = new TreeMap();
	//			for(int j = 0; j <2; j++)
	//			{
	//				yList = new Vector();
	//				for(int k = 0; k < 3; k++)
	//				{
	//					yList.add(new Integer(counter++));
	//				}
	//				xMap.put(new Integer(counter1++),yList);
	//			}
	//			dMap.put(new Integer(i),xMap);
	//		}
	//		
	//		String[] startingPoint = new String[] {"0","1","4"};
	//		int noOfCont = 7;
	//		
	//		Vector results = SimilarContainersAction.getInitalValues(startingPoint,dMap,noOfCont);
	//		for(int i =0; i< results.size(); i++)
	//		{
	//			String[] result = (String[])results.get(i);
	//			for(int j= 0; j<result.length;j++)
	//			{
	//				System.out.println(i+" result "+result[j]);
	//			}
	//			System.out.println();
	//		}
	//		
	//		System.out.println("results "+results);
	//	}

	/**
	 * @param startingPoint
	 * @param dMap
	 * @param noOfContainers
	 * @return
	 */
	private Vector getInitalValues(String[] startingPoint, Map dMap, int noOfContainers)
	{
		//System.out.println("dmAp "+dMap+" noOfCont "+noOfContainers);
		Logger.out.info("Starting point 1:" + startingPoint[0]);
		Logger.out.info("Starting point 2:" + startingPoint[1]);
		Logger.out.info("Starting point 3:" + startingPoint[2]);
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
		//System.out.println("dMpaKey "+dMapKey.toString()+" xMap "+xMap);

		Iterator xMapIter = xMap.keySet().iterator();

		do
		{
			xMapKey = (NameValueBean) xMapIter.next();
		}
		while (!(xMapKey.getValue().equals(startingPoint[1])));
		List yList = (List) xMap.get(xMapKey);
		//System.out.println("xMapKey "+xMapKey.toString()+" yList "+yList);

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
