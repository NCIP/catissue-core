
package edu.wustl.catissuecore.action.shippingtracking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.shippingtracking.BaseShipmentForm;
import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentForm;
import edu.wustl.catissuecore.actionForm.shippingtracking.ShipmentRequestForm;
import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentBizLogic;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.catissuecore.util.shippingtracking.ShippingTrackingUtility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;

/**
 * class for base shipment action.
 */
public class BaseShipmentAction extends SecureAction
{

	/**
	 * action method for base shipment.
	 * @param mapping object of ActionMapping class.
	 * @param form object of ActionForm class.
	 * @param request object of HttpServletRequest class.
	 * @param response object of HttpServletResponse class.
	 * @return forward mapping.
	 * @throws Exception if some problem occurs.
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String operation = request
				.getParameter(edu.wustl.catissuecore.util.global.Constants.OPERATION);
		Long shipmentId = (Long) request
				.getAttribute(edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER);
		if (operation == null || operation.trim().equals(""))
		{
			operation = (String) request
					.getAttribute(edu.wustl.catissuecore.util.global.Constants.OPERATION);
		}
		if (operation == null || operation.trim().equals(""))
		{
			operation = edu.wustl.catissuecore.util.global.Constants.ADD;
		}
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.OPERATION, operation);
		List senderSiteList = null;
		List receiverSiteList = null;
		IBizLogic bizLogic = null;
		SessionDataBean sessionDataBean = getSessionData(request);

		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		bizLogic = factory.getBizLogic(Constants.SHIPMENT_FORM_ID);
		Long userId = null;
		boolean isAdmin = false;
		if (sessionDataBean != null)
		{
			userId = sessionDataBean.getUserId();
			isAdmin = sessionDataBean.isAdmin();
		}
		if (operation != null
				&& (operation.trim().equals(edu.wustl.catissuecore.util.global.Constants.EDIT) || (operation
						.trim().equals(Constants.VIEW))))
		{
			String pageOf = request.getParameter(edu.wustl.common.util.global.Constants.PAGEOF);
			if (pageOf == null || pageOf.trim().equals(""))
			{
				pageOf = (String) request
						.getAttribute(edu.wustl.common.util.global.Constants.PAGEOF);
			}
			if (pageOf != null && pageOf.trim().equals("pageOfShipment"))
			{
				form = (ShipmentForm) request.getAttribute("shipmentForm");
			}
			else if (pageOf != null && pageOf.trim().equals("pageOfShipmentRequest"))
			{
				form = (ShipmentRequestForm) request.getAttribute("shipmentRequestForm");
			}
		}
		BaseShipmentForm shipmentForm = null;
		if (form == null)
		{
			if (request.getAttribute("shipmentRequestForm") != null)
			{
				form = shipmentForm = (BaseShipmentForm) request
						.getAttribute("shipmentRequestForm");
			}
			else if (request.getAttribute("shipmentForm") != null)
			{
				form = shipmentForm = (BaseShipmentForm) request.getAttribute("shipmentForm");
			}
		}
		if (form instanceof BaseShipmentForm)
		{
			shipmentForm = (BaseShipmentForm) form;
			if (shipmentForm.getOperation() != null
					&& shipmentForm.getOperation().trim().equals(
							edu.wustl.catissuecore.util.global.Constants.ADD))
			{
				final Calendar cal = Calendar.getInstance();
				if (shipmentForm.getSendDate() == null)
				{
					// date Format added by geeta
					shipmentForm.setSendDate(Utility.parseDateToString(cal.getTime(),
							CommonServiceLocator.getInstance().getDatePattern()));
				}
				if (shipmentForm.getSendTimeHour() == null)
				{
					shipmentForm.setSendTimeHour(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
				}
				if (shipmentForm.getSendTimeMinutes() == null)
				{
					shipmentForm.setSendTimeMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
				}
				shipmentForm.setSenderContactId(userId);
				shipmentForm.setSpecimenLabelChoice("Label");
				shipmentForm.setContainerLabelChoice("Name");
			}
		}

		if (userId != null)
		{
			senderSiteList = getSitesAsNVBList(((ShipmentBizLogic) bizLogic)
					.getPermittedSitesForUser(userId, isAdmin));
		}
		//Sets the sender and reciever site list attribute
		final String sourceObjectName = Site.class.getName();
		final String[] displayNameFields = {edu.wustl.catissuecore.util.global.Constants.NAME};
		final String valueField = edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER;
		//Collection<NameValueBean> siteList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, false);
		Collection < NameValueBean > siteList = this.getNVBRepositorySites();//bug 12247
		siteList = removeInTransitSite(siteList);
		receiverSiteList = getReceiverSiteList(siteList);
		if (isAdmin)
		{
			receiverSiteList = senderSiteList;
		}
		request.setAttribute(Constants.SENDERS_SITE_LIST, senderSiteList);
		request.setAttribute(Constants.RECIEVERS_SITE_LIST, receiverSiteList);
		request.setAttribute(Constants.REQUESTERS_SITE_LIST, senderSiteList);
		// Sets the minutesList attribute to be used in the Add/Edit FrozenEventParameters Page.
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.MINUTES_LIST,
				edu.wustl.catissuecore.util.global.Constants.MINUTES_ARRAY);

		//Sets the hourList attribute to be used in the Add/Edit FrozenEventParameters Page.
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.HOUR_LIST,
				edu.wustl.catissuecore.util.global.Constants.HOUR_ARRAY);
		if (operation != null && operation.equals("view")
				&& shipmentForm instanceof BaseShipmentForm)
		{
			if (shipmentForm.getSenderSiteId() != 0l)
			{
				Object senderSiteName = ShippingTrackingUtility.getDisplayName(siteList, ""
						+ shipmentForm.getSenderSiteId());
				request.setAttribute("senderSiteName", senderSiteName);
				//bug 11026
				if (senderSiteName != null)
				{
					shipmentForm.setSenderSiteName(senderSiteName.toString());
				}
			}
			if (shipmentForm.getReceiverSiteId() != 0l)
			{
				Object receiverSiteName = ShippingTrackingUtility.getDisplayName(siteList, ""
						+ shipmentForm.getReceiverSiteId());
				request.setAttribute("receiverSiteName", receiverSiteName);
				//bug 11026
				if (receiverSiteName != null)
				{
					shipmentForm.setReceiverSiteName(receiverSiteName.toString());
				}
			}
			//bug 11026 start
			this.setSenderInfoInForm(this.getSenderContactPerson(userId), shipmentForm);
			this.setShipmentReceiverSiteData(shipmentForm);
			this.getSpecimens(shipmentForm);
			this.getContainers(shipmentForm);
			//bug 11026 end
			this.setShipmentBarcode(form, shipmentId);
		}
		if (senderSiteList != null && !senderSiteList.isEmpty())
		{
			request.setAttribute("initialSiteSelected", ((NameValueBean) senderSiteList.get(0))
					.getValue());
		}
		else
		{
			request.setAttribute("initialSiteSelected", "");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		// date format added by geeta
		request.setAttribute("initialShippingDate", Utility.parseDateToString(new Date(),
				CommonServiceLocator.getInstance().getDatePattern()));
		request.setAttribute("initialShippingHour", "" + calendar.get(calendar.HOUR_OF_DAY));
		request.setAttribute("initialShippingmMinute", "" + calendar.get(calendar.MINUTE));
		return mapping.findForward(edu.wustl.catissuecore.util.global.Constants.SUCCESS);
	}

	/**
	 * gets the receiver site list.
	 * @param siteList collection of name value bean for sites.
	 * @return list of sites of receiver.
	 */
	private List getReceiverSiteList(final Collection < NameValueBean > siteList)
	{
		List < NameValueBean > receiverSiteList = new ArrayList < NameValueBean >();
		for (NameValueBean bean : siteList)
		{
			//To add all sites in receiver's Site
			//if(!senderSiteList.contains(bean))
			{
				receiverSiteList.add(bean);
			}
		}
		return receiverSiteList;
	}

	/**
	 * removes site InTransit.
	 * @param siteList collection of name value bean for site.
	 * @return updated collection of NameValueBean.
	 */
	private Collection < NameValueBean > removeInTransitSite(Collection < NameValueBean > siteList)
	{
		List < NameValueBean > updateedSiteList = new ArrayList < NameValueBean >();
		Iterator < NameValueBean > siteIterator = siteList.iterator();
		while (siteIterator.hasNext())
		{
			NameValueBean siteNVB = siteIterator.next();
			if (siteNVB != null && siteNVB.getName() != null
					&& !siteNVB.getName().trim().equals(Constants.IN_TRANSIT_SITE_NAME))
			{
				updateedSiteList.add(siteNVB);
			}
		}
		return updateedSiteList;
	}

	/**
	 * checks the request for authorization.
	 * @param arg0 HttpServletRequest object.
	 * @return boolean result.
	 * @throws Exception if some operation fails.
	 */
	protected boolean isAuthorizedToExecute(HttpServletRequest arg0) throws Exception
	{
		return true;
	}

	/**
	 * gets the site as list of NVB objects.
	 * @param permittedSitesForUser collection of permitted sites.
	 * @return list of NameValueBean objects.
	 */
	private List < NameValueBean > getSitesAsNVBList(Collection < Site > permittedSitesForUser)
	{
		List < NameValueBean > siteNVBList = new ArrayList < NameValueBean >();
		if (permittedSitesForUser != null && !permittedSitesForUser.isEmpty())
		{
			Iterator < Site > siteIterator = permittedSitesForUser.iterator();
			while (siteIterator.hasNext())
			{
				Site site = siteIterator.next();
				if (site.getType().equalsIgnoreCase("Repository"))//bug 12247
				{
					NameValueBean valueBean = new NameValueBean(site.getName(), site.getId());
					siteNVBList.add(valueBean);
				}
			}
		}
		return siteNVBList;
	}

	/**
	 * bug 12247
	 * Added to set only Repository sites in receiver combobox in shipment and shipment request.
	 * This method will return list of NameValueBean objects of all Repository sites.
	 * @return
	 * @throws BizLogicException
	 */
	private List < NameValueBean > getNVBRepositorySites() throws BizLogicException
	{

		List < NameValueBean > siteNVBList = new ArrayList < NameValueBean >();
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory
				.getBizLogic(edu.wustl.catissuecore.util.global.Constants.DEFAULT_BIZ_LOGIC);

		List list = bizLogic.retrieve(Site.class.getName(), new String[]{"id", "name"},
				new String[]{"type"}, new String[]{"="}, new String[]{"Repository"}, null);
		if (list != null && !list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Object[] returnedOject = (Object[]) list.get(i);
				Site site = new Site();
				site.setId((Long) returnedOject[0]);
				site.setName((String) returnedOject[1]);
				NameValueBean valueBean = new NameValueBean(site.getName(), site.getId());
				siteNVBList.add(valueBean);
			}
		}
		return siteNVBList;
	}

	/**
	 * gets the sender contact person based on id.
	 * @param userId id of the person.
	 * @return object of User class.
	 * @throws BizLogicException if bizlogic error occurs.
	 */
	private User getSenderContactPerson(Long userId) throws BizLogicException
	{
		List list = null;
		User sender = null;
		if (userId != null)
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			IBizLogic bizLogic = factory
					.getBizLogic(edu.wustl.catissuecore.util.global.Constants.DEFAULT_BIZ_LOGIC);
			list = bizLogic.retrieve(User.class.getName(),
					edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER, userId);
			if (!list.isEmpty())
			{
				sender = (User) list.get(0);
			}
		}
		return sender;
	}

	/**
	 * sets the receiver site data.
	 * @param shipmentForm form containing all values.
	 * @throws BizLogicException if bizlogic error occurs.
	 */
	private void setShipmentReceiverSiteData(BaseShipmentForm shipmentForm)
			throws BizLogicException
	{
		Site receiverSite = null;
		List list = null;
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory
				.getBizLogic(edu.wustl.catissuecore.util.global.Constants.DEFAULT_BIZ_LOGIC);
		long siteId = shipmentForm.getReceiverSiteId();
		if (siteId != 0l)
		{
			list = bizLogic.retrieve(Site.class.getName(),
					edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER, siteId);
			if (list != null && !list.isEmpty())
			{
				receiverSite = (Site) list.get(0);
			}
			User receiverSiteCoordinator = receiverSite.getCoordinator();
			StringBuffer stbuff = new StringBuffer();
			stbuff.append(receiverSiteCoordinator.getFirstName());
			stbuff.append(" ");
			stbuff.append(receiverSiteCoordinator.getLastName());
			String coordinatorName = stbuff.toString();
			shipmentForm.setReceiverSiteCoordinator(coordinatorName);
			shipmentForm.setReceiverSiteCoordinatorPhone(receiverSiteCoordinator.getAddress()
					.getPhoneNumber());
		}
	}

	/**
	 * sets the sender information.
	 * @param sender whose info is to be set.
	 * @param shipmentForm form in which info is to be set.
	 */
	private void setSenderInfoInForm(User sender, BaseShipmentForm shipmentForm)
	{
		String senderName = sender.getFirstName() + " " + sender.getLastName();
		shipmentForm.setSenderName(senderName);
		shipmentForm.setSenderEmail(sender.getEmailAddress());
		Address address = sender.getAddress();
		shipmentForm.setSenderPhone(address.getPhoneNumber());
	}

	/**
	 * sets the shipment barcode.
	 * @param shipmentForm in which barcode is to be set.
	 * @param identifier the id of shipment.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private void setShipmentBarcode(ActionForm shipmentForm, Long identifier)
			throws BizLogicException
	{
		List list = null;
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory
				.getBizLogic(edu.wustl.catissuecore.util.global.Constants.DEFAULT_BIZ_LOGIC);
		list = bizLogic.retrieve(Shipment.class.getName(), new String[]{"barcode"},
				new String[]{"id"}, new String[]{"="}, new Long[]{identifier}, null);
		if (list != null && !list.isEmpty())
		{
			String barcode = ((String) list.get(0));
			if (shipmentForm instanceof ShipmentForm)
			{
				((ShipmentForm) shipmentForm).setBarcode(barcode);
			}
		}
	}

	/**
	 * In BaseShipment,created list of specimen's label or barcode
	 * 	in populateSpecimenContents() and set it in shipmentForm.
	 * list contains values of label or barcode according to SpecimenLabelChoice.
	 * @param shipmentForm form containing all values.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private void getSpecimens(BaseShipmentForm shipmentForm) throws BizLogicException
	{
		List list = null;
		String fieldValue = "";
		String optionForQuery = "";
		List < String > lblOrBarcodes = new ArrayList < String >();
		List < Specimen > specimenL = new ArrayList < Specimen >();
		String option = shipmentForm.getSpecimenLabelChoice();
		if ("SpecimenLabel".equals(option))
		{
			optionForQuery = Constants.SPECIMEN_PROPERTY_LABEL;
		}
		else if ("SpecimenBarcode".equals(option))
		{
			optionForQuery = Constants.SPECIMEN_PROPERTY_BARCODE;
		}
		//bug 12220 start
		if (shipmentForm instanceof ShipmentRequestForm)
		{
			Map < String , String > specimenMap = shipmentForm.getSpecimenDetailsMap();
			Set keySet = specimenMap.keySet();
			Iterator it = keySet.iterator();
			while (it.hasNext())
			{
				String specimenLblKey = (String) it.next();
				String[] specimenLabelChoice = specimenLblKey.split("_");
				if (specimenLabelChoice.length > 1)
				{
					if (option.equalsIgnoreCase(specimenLabelChoice[0]))
					{
						fieldValue = (String) shipmentForm.getSpecimenDetails(specimenLblKey);
						lblOrBarcodes.add(fieldValue);
					}
				}
			}
		}
		//bug 12220 end
		else
		{
			lblOrBarcodes = shipmentForm.getLblOrBarcodeSpecimenL();
		}

		if (lblOrBarcodes != null && !lblOrBarcodes.isEmpty())
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			IBizLogic bizLogic = factory
					.getBizLogic(edu.wustl.catissuecore.util.global.Constants.DEFAULT_BIZ_LOGIC);
			for (String lbl : lblOrBarcodes)
			{
				list = bizLogic.retrieve(Specimen.class.getName(), new String[]{"id", "label",
						"barcode"}, new String[]{optionForQuery}, new String[]{"="},
						new String[]{lbl}, null);
				//list = bizLogic.retrieve(Specimen.class.getName(),option,lbl);
				if (list != null && !list.isEmpty())
				{
					Object[] returnedOject = (Object[]) list.get(0);
					Specimen specimen = new Specimen();
					specimen.setId((Long) returnedOject[0]);
					specimen.setLabel((String) returnedOject[1]);
					specimen.setBarcode((String) returnedOject[2]);
					specimenL.add(specimen);
				}
			}
			if (specimenL != null && !specimenL.isEmpty())
			{
				shipmentForm.setShipmentContentsUsingSpecimen(specimenL);
			}
		}
		else
		{
			shipmentForm.setSpecimenCounter(0);
		}

	}

	/**
	 * retreives the containers list.
	 * @param shipmentForm form containing all values.
	 * @throws BizLogicException if some database operation fails.
	 */
	private void getContainers(BaseShipmentForm shipmentForm) throws BizLogicException
	{
		List list = null;
		String fieldValue = "";
		String optionForQuery = "";
		List < StorageContainer > containerL = new ArrayList < StorageContainer >();
		List < String > lblOrBarcodes = new ArrayList < String >();
		String option = shipmentForm.getContainerLabelChoice();
		if ("ContainerLabel".equals(option))
		{
			optionForQuery = Constants.CONTAINER_PROPERTY_NAME;
		}
		else if ("ContainerBarcode".equals(option))
		{
			optionForQuery = Constants.CONTAINER_PROPERTY_BARCODE;
		}
		//bug 12220 start
		if (shipmentForm instanceof ShipmentRequestForm)
		{
			Map < String , String > conatinerMap = shipmentForm.getContainerDetailsMap();
			Set keySet = conatinerMap.keySet();
			Iterator it = keySet.iterator();
			while (it.hasNext())
			{
				String containerLblKey = (String) it.next();
				String[] containerLabelChoice = containerLblKey.split("_");
				if (containerLabelChoice.length > 1)
				{
					if (option.equalsIgnoreCase(containerLabelChoice[0]))
					{
						fieldValue = (String) shipmentForm.getContainerDetails(containerLblKey);
						lblOrBarcodes.add(fieldValue);
					}
				}
			}
		}
		//bug 12220 end
		else
		{
			lblOrBarcodes = shipmentForm.getLblOrBarcodeContainerL();
		}
		if (lblOrBarcodes != null && !lblOrBarcodes.isEmpty())
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			IBizLogic bizLogic = factory
					.getBizLogic(edu.wustl.catissuecore.util.global.Constants.DEFAULT_BIZ_LOGIC);
			for (String lbl : lblOrBarcodes)
			{
				list = bizLogic.retrieve(StorageContainer.class.getName(), new String[]{"id",
						"name", "barcode"}, new String[]{optionForQuery}, new String[]{"="},
						new String[]{lbl}, null);
				//list = bizLogic.retrieve(Specimen.class.getName(),option,lbl);
				if (list != null && !list.isEmpty())
				{
					Object[] returnedOject = (Object[]) list.get(0);
					StorageContainer sContainer = new StorageContainer();
					sContainer.setId((Long) returnedOject[0]);
					sContainer.setName((String) returnedOject[1]);
					sContainer.setBarcode((String) returnedOject[2]);
					containerL.add(sContainer);
				}
			}
			if (containerL != null && !containerL.isEmpty())
			{
				shipmentForm.setShipmentContentsUsingContainer(containerL);
			}
		}
		else
		{
			shipmentForm.setContainerCounter(0);
		}
	}
}
