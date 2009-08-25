
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
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;

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
	@Override
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String operation = request
				.getParameter(edu.wustl.catissuecore.util.global.Constants.OPERATION);
		final Long shipmentId = (Long) request
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
		final SessionDataBean sessionDataBean = this.getSessionData(request);

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		bizLogic = factory.getBizLogic(Constants.SHIPMENT_FORM_ID);
		Long userId = null;
		boolean isAdmin = false;
		if (sessionDataBean != null)
		{
			userId = sessionDataBean.getUserId();
			isAdmin = sessionDataBean.isAdmin();
		}
		//bug 12814
		/**
		 * Added new operation - viewNonEditable for shipment request having status processed or rejected.
		 */
		if (operation != null
				&& (operation.trim().equals(edu.wustl.catissuecore.util.global.Constants.EDIT) || (operation
						.trim().equals(Constants.VIEW)))
				|| operation.trim().equals("viewNonEditable"))

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
					shipmentForm.setSendDate(CommonUtilities.parseDateToString(cal.getTime(),
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
			senderSiteList = this.getSitesAsNVBList(((ShipmentBizLogic) bizLogic)
					.getPermittedSitesForUser(userId, isAdmin));
		}
		//Sets the sender and reciever site list attribute
		//final String sourceObjectName = Site.class.getName();
		//final String[] displayNameFields = {edu.wustl.catissuecore.util.global.Constants.NAME};
		//final String valueField = edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER;
		//Collection<NameValueBean> siteList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, false);
		Collection<NameValueBean> siteList = this.getNVBRepositorySites();//bug 12247
		siteList = this.removeInTransitSite(siteList);
		receiverSiteList = this.getReceiverSiteList(siteList);
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
		//bug 12814
		if (operation != null
				&& (operation.equals("view") || operation.trim().equals("viewNonEditable"))
				&& shipmentForm instanceof BaseShipmentForm)
		{
			if (shipmentForm.getSenderSiteId() != 0l)
			{
				final Object senderSiteName = ShippingTrackingUtility.getDisplayName(siteList, ""
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
				final Object receiverSiteName = ShippingTrackingUtility.getDisplayName(siteList, ""
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
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		// date format added by geeta
		request.setAttribute("initialShippingDate", CommonUtilities.parseDateToString(new Date(),
				CommonServiceLocator.getInstance().getDatePattern()));
		request.setAttribute("initialShippingHour", "" + calendar.get(Calendar.HOUR_OF_DAY));
		request.setAttribute("initialShippingmMinute", "" + calendar.get(Calendar.MINUTE));
		return mapping.findForward(edu.wustl.catissuecore.util.global.Constants.SUCCESS);
	}

	/**
	 * gets the receiver site list.
	 * @param siteList collection of name value bean for sites.
	 * @return list of sites of receiver.
	 */
	private List getReceiverSiteList(final Collection<NameValueBean> siteList)
	{
		final List<NameValueBean> receiverSiteList = new ArrayList<NameValueBean>();
		for (final NameValueBean bean : siteList)
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
	private Collection<NameValueBean> removeInTransitSite(Collection<NameValueBean> siteList)
	{
		final List<NameValueBean> updateedSiteList = new ArrayList<NameValueBean>();
		final Iterator<NameValueBean> siteIterator = siteList.iterator();
		while (siteIterator.hasNext())
		{
			final NameValueBean siteNVB = siteIterator.next();
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
	private List<NameValueBean> getSitesAsNVBList(Collection<Site> permittedSitesForUser)
	{
		final List<NameValueBean> siteNVBList = new ArrayList<NameValueBean>();
		if (permittedSitesForUser != null && !permittedSitesForUser.isEmpty())
		{
			final Iterator<Site> siteIterator = permittedSitesForUser.iterator();
			while (siteIterator.hasNext())
			{
				final Site site = siteIterator.next();
				if (site.getType().equalsIgnoreCase("Repository"))//bug 12247
				{
					final NameValueBean valueBean = new NameValueBean(site.getName(), site.getId());
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
	 * @return List : List < NameValueBean >
	 * @throws BizLogicException : BizLogicException
	 */
	private List<NameValueBean> getNVBRepositorySites() throws BizLogicException
	{

		final List<NameValueBean> siteNVBList = new ArrayList<NameValueBean>();
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory
				.getBizLogic(edu.wustl.catissuecore.util.global.Constants.DEFAULT_BIZ_LOGIC);

		final List list = bizLogic.retrieve(Site.class.getName(), new String[]{"id", "name"},
				new String[]{"type"}, new String[]{"="}, new String[]{"Repository"}, null);
		if (list != null && !list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				final Object[] returnedOject = (Object[]) list.get(i);
				final Site site = new Site();
				site.setId((Long) returnedOject[0]);
				site.setName((String) returnedOject[1]);
				final NameValueBean valueBean = new NameValueBean(site.getName(), site.getId());
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
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic bizLogic = factory
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
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory
				.getBizLogic(edu.wustl.catissuecore.util.global.Constants.DEFAULT_BIZ_LOGIC);
		final long siteId = shipmentForm.getReceiverSiteId();
		if (siteId != 0l)
		{
			list = bizLogic.retrieve(Site.class.getName(),
					edu.wustl.catissuecore.util.global.Constants.SYSTEM_IDENTIFIER, siteId);
			if (list != null && !list.isEmpty())
			{
				receiverSite = (Site) list.get(0);
			}
			final User receiverSiteCoordinator = receiverSite.getCoordinator();
			final StringBuffer stbuff = new StringBuffer();
			stbuff.append(receiverSiteCoordinator.getFirstName());
			stbuff.append(" ");
			stbuff.append(receiverSiteCoordinator.getLastName());
			final String coordinatorName = stbuff.toString();
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
		final String senderName = sender.getFirstName() + " " + sender.getLastName();
		shipmentForm.setSenderName(senderName);
		shipmentForm.setSenderEmail(sender.getEmailAddress());
		final Address address = sender.getAddress();
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
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory
				.getBizLogic(edu.wustl.catissuecore.util.global.Constants.DEFAULT_BIZ_LOGIC);
		list = bizLogic.retrieve(Shipment.class.getName(), new String[]{"barcode"},
				new String[]{"id"}, new String[]{"="}, new Long[]{identifier}, null);
		if (list != null && !list.isEmpty())
		{
			final String barcode = ((String) list.get(0));
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
		List<String> lblOrBarcodes = new ArrayList<String>();
		final List<Specimen> specimenL = new ArrayList<Specimen>();
		final String option = shipmentForm.getSpecimenLabelChoice();
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
			final Map<String, String> specimenMap = shipmentForm.getSpecimenDetailsMap();
			final Set keySet = specimenMap.keySet();
			final Iterator it = keySet.iterator();
			while (it.hasNext())
			{
				final String specimenLblKey = (String) it.next();
				final String[] specimenLabelChoice = specimenLblKey.split("_");
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
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic bizLogic = factory
					.getBizLogic(edu.wustl.catissuecore.util.global.Constants.DEFAULT_BIZ_LOGIC);
			for (final String lbl : lblOrBarcodes)
			{
				list = bizLogic.retrieve(Specimen.class.getName(), new String[]{"id", "label",
						"barcode"}, new String[]{optionForQuery}, new String[]{"="},
						new String[]{lbl}, null);
				//list = bizLogic.retrieve(Specimen.class.getName(),option,lbl);
				if (list != null && !list.isEmpty())
				{
					final Object[] returnedOject = (Object[]) list.get(0);
					final Specimen specimen = new Specimen();
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
		final List<StorageContainer> containerL = new ArrayList<StorageContainer>();
		List<String> lblOrBarcodes = new ArrayList<String>();
		final String option = shipmentForm.getContainerLabelChoice();
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
			final Map<String, String> conatinerMap = shipmentForm.getContainerDetailsMap();
			final Set keySet = conatinerMap.keySet();
			final Iterator it = keySet.iterator();
			while (it.hasNext())
			{
				final String containerLblKey = (String) it.next();
				final String[] containerLabelChoice = containerLblKey.split("_");
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
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic bizLogic = factory
					.getBizLogic(edu.wustl.catissuecore.util.global.Constants.DEFAULT_BIZ_LOGIC);
			for (final String lbl : lblOrBarcodes)
			{
				list = bizLogic.retrieve(StorageContainer.class.getName(), new String[]{"id",
						"name", "barcode"}, new String[]{optionForQuery}, new String[]{"="},
						new String[]{lbl}, null);
				//list = bizLogic.retrieve(Specimen.class.getName(),option,lbl);
				if (list != null && !list.isEmpty())
				{
					final Object[] returnedOject = (Object[]) list.get(0);
					final StorageContainer sContainer = new StorageContainer();
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
