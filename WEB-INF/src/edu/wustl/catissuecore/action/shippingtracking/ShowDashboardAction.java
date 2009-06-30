
package edu.wustl.catissuecore.action.shippingtracking;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.shippingtracking.DashboardForm;
import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentBizLogic;
import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentRequestBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * this class implements dashboard viewing action.
 */
public class ShowDashboardAction extends SecureAction
{

	/**
	 * action method for shipment add/edit.
	 * @param mapping object of ActionMapping class.
	 * @param form object of ActionForm class.
	 * @param request object of HttpServletRequest class.
	 * @param response object of HttpServletResponse class.
	 * @return forward mapping.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		//		Create DAO for passing as an argument to bizlogic's validate
		Logger logger = Logger.getCommonLogger(ShowDashboardAction.class);
		DAO dao = null;
		try
		{
			dao = AppUtility.openDAOSession(null);
			ShipmentBizLogic bizLogic = new ShipmentBizLogic();
			ShipmentRequestBizLogic requestBizLogic = new ShipmentRequestBizLogic();
			// Get logged in user's site id
			SessionDataBean sessionDataBean = getSessionData(request);
			Long loggedInUserId = null;
			Long[] loggedInUserSiteId = null;
			int recordsPerPage = 5;
			if (form instanceof DashboardForm)
			{
				if (((DashboardForm) form).getRecordsPerPage() != 0)
				{
					recordsPerPage = ((DashboardForm) form).getRecordsPerPage();
				}
			}
			if (sessionDataBean != null)
			{
				loggedInUserId = sessionDataBean.getUserId();
				if (loggedInUserId != null)
				{
					List < Long > siteIds = bizLogic.getPermittedSiteIdsForUser(loggedInUserId,
							sessionDataBean.isAdmin());
					if (siteIds != null && siteIds.size() > 0)
					{
						loggedInUserSiteId = new Long[siteIds.size()];
						loggedInUserSiteId = siteIds.toArray(loggedInUserSiteId);
						setRequestsReceivedInfo(request, requestBizLogic, loggedInUserSiteId,
								recordsPerPage);
						setShipmentsReceivedInfo(request, bizLogic, loggedInUserSiteId,
								recordsPerPage);
						setOutgoingShipmentsInfo(request, bizLogic, loggedInUserSiteId,
								recordsPerPage);
						setRequestsSentInfo(request, requestBizLogic, loggedInUserSiteId,
								recordsPerPage);
						request.setAttribute("identifierFieldIndex", 0);
						request.setAttribute("isAdmin", sessionDataBean.isAdmin());
						setRecordsPerPageToRequest(request, recordsPerPage);
						String requestFor = request.getParameter("requestFor");
						if (requestFor != null)
						{
							request.setAttribute("requestFor", requestFor);
						}
					}
					else
					{
						setEmptyDashboardData(request);
					}
				}
				else
				{
					logger.debug("user is not authorized for the operation");
					throw new UserNotAuthorizedException(ErrorKey
							.getErrorKey("access.execute.action.denied"), null,
							"user is not authorized for the operation");
				}
			}
		}
		//		catch(DAOException e)
		//		{
		//			e.printStackTrace();
		//		}
		catch (BizLogicException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		catch (ApplicationException appException)
		{
			logger.debug(appException.getMessage(), appException);
			appException.printStackTrace();
		}
		finally
		{
			try
			{
				AppUtility.closeDAOSession(dao);
			}
			catch (ApplicationException e)
			{
				logger.debug(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		return mapping.findForward(edu.wustl.catissuecore.util.global.Constants.SUCCESS);
	}

	/**
	 * this method sets the dashboard data.
	 * @param request request to be processed.
	 */
	private void setEmptyDashboardData(HttpServletRequest request)
	{
		setRecordsPerPageToRequest(request, 5);
		request.setAttribute("identifierFieldIndex", 0);
		request.setAttribute("isAdmin", false);
		String requestFor = request.getParameter("requestFor");
		List < Object[] > emptyList = new ArrayList < Object[] >();
		request.setAttribute("requestsReceivedHeader", getRequestsReceivedHeader());
		request.setAttribute("requestsReceivedList", emptyList);
		request.setAttribute("receivedReqUserNameIndex", 4);
		request.setAttribute("requestsSentHeader", getRequestsSentHeader());
		request.setAttribute("requestsSentList", emptyList);
		request.setAttribute("sentReqUserNameIndex", 4);
		request.setAttribute("sentReqActivityStatusIndex", 6);
		request.setAttribute("sentReqReceiverSiteNameIndex", 3);
		request.setAttribute("incomingShipmentsHeader", getIncomingShipmentsHeader());
		request.setAttribute("incomingShipmentsList", emptyList);
		request.setAttribute("incomingShipUserNameIndex", 5);
		request.setAttribute("outgoingShipmentsHeader", getOutgoingShipmentsHeader());
		request.setAttribute("outgoingShipmentsList", emptyList);
		request.setAttribute("outgoingShipUserNameIndex", 5);
		if (requestFor != null)
		{
			request.setAttribute("requestFor", requestFor);
		}
	}

	/**
	 * set request details.
	 * @param request request object to receive parameters.
	 * @param bizLogic object of ShipmentRequestBizLogic class.
	 * @param loggedInUserSiteId user ids array.
	 * @param recordsPerPage count of records per page.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private void setRequestsSentInfo(HttpServletRequest request, ShipmentRequestBizLogic bizLogic,
			Long[] loggedInUserSiteId, Integer recordsPerPage) throws BizLogicException
	{
		List < String > requestsSentHeader = getRequestsSentHeader();
		List < Object[] > requestsSentList = null;
		String pageNoFromReq = request.getParameter("reqSentCurrentPageNo");
		Integer currentPageNo = new Integer(1);
		if (pageNoFromReq != null)
		{
			currentPageNo = Integer.parseInt(pageNoFromReq);
		}
		else if (request.getSession().getAttribute("reqSentCurrentPageNo") != null)
		{
			currentPageNo = (Integer) request.getSession().getAttribute("reqSentCurrentPageNo");
		}
		else
		{
			currentPageNo = 1;
		}
		Integer totalRecords = (Integer) request.getSession().getAttribute("reqSentTotalRecords");
		Integer totalPages = (Integer) request.getSession().getAttribute("reqSentTotalPages");
		int startIndex = 0;
		int numOfRecords = 1;
		if (currentPageNo != null && recordsPerPage != null)
		{
			startIndex = (currentPageNo.intValue() - 1) * recordsPerPage.intValue();
			numOfRecords = recordsPerPage.intValue();
		}
		else
		{
			startIndex = 0;
			numOfRecords = 5;
			currentPageNo = 1;
		}
		// Pass the siteiID array of logged in user
		requestsSentList = getRequestsSentList(bizLogic, loggedInUserSiteId, startIndex,
				numOfRecords);
		totalRecords = bizLogic.getShipmentRequestsCount("senderSite.id", "createdDate",
				loggedInUserSiteId, startIndex, numOfRecords);
		totalPages = totalRecords % numOfRecords == 0
				? totalRecords / numOfRecords
				: (totalRecords / numOfRecords) + 1;
		recordsPerPage = numOfRecords;
		setPagenationInfoToSession(request, "reqSentCurrentPageNo", currentPageNo,
				"reqSentTotalRecords", totalRecords, "reqSentTotalPages", totalPages,
				"reqSentRecordsPerPage", recordsPerPage);
		request.setAttribute("requestsSentHeader", requestsSentHeader);
		request.setAttribute("requestsSentList", requestsSentList);
		request.setAttribute("sentReqUserNameIndex", 4);
		request.setAttribute("sentReqActivityStatusIndex", 6);
		request.setAttribute("sentReqReceiverSiteNameIndex", 3);
	}

	/**
	 * gets the requests list.
	 * @param bizLogic object of ShipmentRequestBizLogic class.
	 * @param loggedInUserSiteId ids of users.
	 * @param startIndex the starting index.
	 * @param numOfRecords count of number of records per page.
	 * @return list if objects.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private List < Object[] > getRequestsSentList(ShipmentRequestBizLogic bizLogic,
			Long[] loggedInUserSiteId, int startIndex, int numOfRecords) throws BizLogicException
	{
		String selectColumnName = "shipment.id,shipment.senderContactPerson.lastName, shipment.label, shipment.receiverSite.name, shipment.senderContactPerson.firstName, shipment.sendDate, shipment.activityStatus";
		return bizLogic.getShipmentRequests(selectColumnName, "senderSite.id", "createdDate",
				loggedInUserSiteId, startIndex, numOfRecords, "AND shipment.activityStatus <> '"
						+ Status.ACTIVITY_STATUS_CLOSED.toString() + "'");
	}

	/**
	 * gets send requests header.
	 * @return list of strings.
	 */
	private List < String > getRequestsSentHeader()
	{
		List < String > requestsSentHeader = new ArrayList < String >();
		requestsSentHeader.add("Label");
		requestsSentHeader.add("Receiver Site");
		requestsSentHeader.add("Requester");
		requestsSentHeader.add("Requested On");
		requestsSentHeader.add("Status");
		return requestsSentHeader;
	}

	/**
	 * sets the records per page to the request.
	 * @param request to be accessed.
	 * @param recordsPerPage count.
	 */
	private void setRecordsPerPageToRequest(HttpServletRequest request, int recordsPerPage)
	{
		List < NameValueBean > resultsPerPageList = new ArrayList < NameValueBean >();
		resultsPerPageList.add(new NameValueBean("5", "5"));
		resultsPerPageList.add(new NameValueBean("10", "10"));
		resultsPerPageList.add(new NameValueBean("15", "15"));
		resultsPerPageList.add(new NameValueBean("20", "20"));
		DashboardForm dashboardForm = new DashboardForm();
		dashboardForm.setRecordsPerPage(recordsPerPage);
		request.setAttribute("dashboardForm", dashboardForm);
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.RESULTS_PER_PAGE,
				resultsPerPageList);
	}

	/**
	 * sets the shipment received info.
	 * @param request to fetch parameters.
	 * @param bizLogic ShipmentBizLogic object.
	 * @param loggedInUserSiteId site ids of logged in user.
	 * @param recordsPerPage count of records to be displayed.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	//For Incoming Shipments
	private void setShipmentsReceivedInfo(HttpServletRequest request, ShipmentBizLogic bizLogic,
			Long[] loggedInUserSiteId, Integer recordsPerPage) throws BizLogicException
	{
		List < String > incomingShipmentsHeader = getIncomingShipmentsHeader();
		// Pass the siteiID array of logged in user
		List < Object[] > incomingShipmentsList = null;
		String pageNoFromReq = request.getParameter("incomingShipCurrentPageNo");
		Integer currentPageNo = new Integer(1);
		if (pageNoFromReq != null)
		{
			currentPageNo = Integer.parseInt(pageNoFromReq);
		}
		else if (request.getSession().getAttribute("incomingShipCurrentPageNo") != null)
		{
			currentPageNo = (Integer) request.getSession()
					.getAttribute("incomingShipCurrentPageNo");
		}
		else
		{
			currentPageNo = 1;
		}
		Integer totalRecords = (Integer) request.getSession().getAttribute(
				"incomingShipTotalRecords");
		Integer totalPages = (Integer) request.getSession().getAttribute("incomingShipTotalPages");
		if (totalRecords == null)
		{
			totalRecords = 0;
		}
		int startIndex = 0;
		int numOfRecords = 10;
		if (currentPageNo != null && recordsPerPage != null)
		{
			startIndex = (currentPageNo.intValue() - 1) * recordsPerPage.intValue();
			numOfRecords = recordsPerPage.intValue();
		}
		else
		{
			startIndex = 0;
			numOfRecords = 5;
			currentPageNo = 1;
		}
		// Pass the siteiID array of logged in user
		incomingShipmentsList = getIncomingShipmentsList(bizLogic, loggedInUserSiteId, startIndex,
				numOfRecords);
		totalRecords = bizLogic.getShipmentsCount("receiverSite.id", "createdDate",
				loggedInUserSiteId, startIndex, numOfRecords);
		totalPages = totalRecords % numOfRecords == 0
				? totalRecords / numOfRecords
				: (totalRecords / numOfRecords) + 1;
		recordsPerPage = numOfRecords;
		setPagenationInfoToSession(request, "incomingShipCurrentPageNo", currentPageNo,
				"incomingShipTotalRecords", totalRecords, "incomingShipTotalPages", totalPages,
				"incomingShipRecordsPerPage", recordsPerPage);
		request.setAttribute("incomingShipmentsHeader", incomingShipmentsHeader);
		request.setAttribute("incomingShipmentsList", incomingShipmentsList);
		request.setAttribute("incomingShipUserNameIndex", 5);
	}

	/**
	 * gets the incoming shipment headers.
	 * @return list of string containing headers.
	 */
	private List < String > getIncomingShipmentsHeader()
	{
		List < String > incomingShipmentsHeader = new ArrayList < String >();
		incomingShipmentsHeader.add("Sender Site");
		incomingShipmentsHeader.add("Label");
		incomingShipmentsHeader.add("Barcode");
		incomingShipmentsHeader.add("Sender");
		incomingShipmentsHeader.add("Sent On");
		//outgoingShipmentsHeader.add("Time");
		incomingShipmentsHeader.add("Status");
		return incomingShipmentsHeader;
	}

	/**
	 * gets the list of incoming shipments.
	 * @param bizLogic the ShipmentBizLogic object.
	 * @param siteId id of the site.
	 * @param startIndex the starting index of shipments.
	 * @param numOfRecords integer containing no. of records.
	 * @return list of objects.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private List < Object[] > getIncomingShipmentsList(ShipmentBizLogic bizLogic, Long[] siteId,
			int startIndex, int numOfRecords) throws BizLogicException
	{
		String selectColumnName = "shipment.id,shipment.senderContactPerson.lastName, shipment.senderSite.name, shipment.label, shipment.barcode, shipment.senderContactPerson.firstName, shipment.sendDate, shipment.activityStatus";
		return bizLogic.getShipments(selectColumnName, "receiverSite.id", "sendDate", siteId,
				startIndex, numOfRecords);
	}

	// For Outgoing Shipments
	/**
	 * sets the outgoing shipment details.
	 * @param request the request to be processed.
	 * @param bizLogic ShipmentBizlogic object.
	 * @param loggedInUserSiteId site ids of logged in users.
	 * @param recordsPerPage count containing records per page.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private void setOutgoingShipmentsInfo(HttpServletRequest request, ShipmentBizLogic bizLogic,
			Long[] loggedInUserSiteId, Integer recordsPerPage) throws BizLogicException
	{
		List < String > outgoingShipmentsHeader = getOutgoingShipmentsHeader();
		List < Object[] > outgoingShipmentsList = null;
		String pageNoFromReq = request.getParameter("outgoingShipCurrentPageNo");
		Integer currentPageNo = new Integer(1);
		if (pageNoFromReq != null)
		{
			currentPageNo = Integer.parseInt(pageNoFromReq);
		}
		else if (request.getSession().getAttribute("outgoingShipCurrentPageNo") != null)
		{
			currentPageNo = (Integer) request.getSession()
					.getAttribute("outgoingShipCurrentPageNo");
		}
		else
		{
			currentPageNo = 1;
		}
		Integer totalRecords = (Integer) request.getSession().getAttribute(
				"outgoingShipTotalRecords");
		Integer totalPages = (Integer) request.getSession().getAttribute("outgoingShipTotalPages");
		int startIndex = 0;
		int numOfRecords = 10;
		if (currentPageNo != null && recordsPerPage != null)
		{
			startIndex = (currentPageNo.intValue() - 1) * recordsPerPage.intValue();
			numOfRecords = recordsPerPage.intValue();
		}
		else
		{
			startIndex = 0;
			numOfRecords = 5;
			currentPageNo = 1;
		}
		// Pass the siteiID array of logged in user
		outgoingShipmentsList = getOutgoingShipmentsList(bizLogic, loggedInUserSiteId, startIndex,
				numOfRecords);

		totalRecords = bizLogic.getShipmentsCount("senderSite.id", "createdDate",
				loggedInUserSiteId, startIndex, numOfRecords);
		totalPages = totalRecords % numOfRecords == 0
				? totalRecords / numOfRecords
				: (totalRecords / numOfRecords) + 1;
		recordsPerPage = numOfRecords;
		setPagenationInfoToSession(request, "outgoingShipCurrentPageNo", currentPageNo,
				"outgoingShipTotalRecords", totalRecords, "outgoingShipTotalPages", totalPages,
				"outgoingShipRecordsPerPage", recordsPerPage);
		request.setAttribute("outgoingShipmentsHeader", outgoingShipmentsHeader);
		request.setAttribute("outgoingShipmentsList", outgoingShipmentsList);
		request.setAttribute("outgoingShipUserNameIndex", 5);
	}

	/**
	 * gets the outgoing shipment list.
	 * @param bizLogic ShipmentBizLogic object.
	 * @param siteId the site id.
	 * @param startIndex starting index integer.
	 * @param numOfRecords count of records per page.
	 * @return list of objects.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private List < Object[] > getOutgoingShipmentsList(ShipmentBizLogic bizLogic, Long[] siteId,
			int startIndex, int numOfRecords) throws BizLogicException
	{
		String selectColumnName = "shipment.id,shipment.senderContactPerson.lastName, shipment.receiverSite.name, shipment.label, shipment.barcode, shipment.senderContactPerson.firstName, shipment.sendDate, shipment.activityStatus";
		return bizLogic.getShipments(selectColumnName, "senderSite.id", "createdDate", siteId,
				startIndex, numOfRecords);
	}

	/**
	 * gets the outgoing shipment header.
	 * @return list of strings containing the header.
	 */
	private List < String > getOutgoingShipmentsHeader()
	{
		List < String > outgoingShipmentsHeader = new ArrayList < String >();
		outgoingShipmentsHeader.add("Receiver Site");
		outgoingShipmentsHeader.add("Label");
		outgoingShipmentsHeader.add("Barcode");
		outgoingShipmentsHeader.add("Sender");
		outgoingShipmentsHeader.add("Sent On");
		//outgoingShipmentsHeader.add("Time");
		outgoingShipmentsHeader.add("Status");
		return outgoingShipmentsHeader;
	}

	// For Requests Shipments
	/**
	 * sets the receiving requests info.
	 * @param request request to process.
	 * @param bizLogic ShipmentRequestBizLogic object.
	 * @param loggedInUserSiteId site ids of logged in users.
	 * @param recordsPerPage integer containing records per page count.
	 * @throws BizLogicException if some bizlogic error occurs.
	 */
	private void setRequestsReceivedInfo(HttpServletRequest request,
			ShipmentRequestBizLogic bizLogic, Long[] loggedInUserSiteId, Integer recordsPerPage)
			throws BizLogicException
	{
		List < String > requestsReceivedHeader = getRequestsReceivedHeader();
		List < Object[] > requestsReceivedList = null;
		String pageNoFromReq = request.getParameter("reqReceivedCurrentPageNo");
		Integer currentPageNo = new Integer(1);
		if (pageNoFromReq != null)
		{
			currentPageNo = Integer.parseInt(pageNoFromReq);
		}
		else if (request.getSession().getAttribute("reqReceivedCurrentPageNo") != null)
		{
			currentPageNo = (Integer) request.getSession().getAttribute("reqReceivedCurrentPageNo");
		}
		else
		{
			currentPageNo = 1;
		}
		Integer totalRecords = (Integer) request.getSession().getAttribute(
				"reqReceivedTotalRecords");
		Integer totalPages = (Integer) request.getSession().getAttribute("reqReceivedTotalPages");
		int startIndex = 0;
		int numOfRecords = 1;
		if (currentPageNo != null && recordsPerPage != null)
		{
			startIndex = (currentPageNo.intValue() - 1) * recordsPerPage.intValue();
			numOfRecords = recordsPerPage.intValue();
		}
		else
		{
			startIndex = 0;
			numOfRecords = 5;
			currentPageNo = 1;
		}
		// Pass the siteiID array of logged in user
		requestsReceivedList = getRequestsReceivedList(bizLogic, loggedInUserSiteId, startIndex,
				numOfRecords);
		totalRecords = bizLogic.getShipmentRequestsCount("receiverSite.id", "sendDate",
				loggedInUserSiteId, startIndex, numOfRecords);
		totalPages = totalRecords % numOfRecords == 0
				? totalRecords / numOfRecords
				: (totalRecords / numOfRecords) + 1;
		recordsPerPage = numOfRecords;
		setPagenationInfoToSession(request, "reqReceivedCurrentPageNo", currentPageNo,
				"reqReceivedTotalRecords", totalRecords, "reqReceivedTotalPages", totalPages,
				"reqReceivedRecordsPerPage", recordsPerPage);
		request.setAttribute("requestsReceivedHeader", requestsReceivedHeader);
		request.setAttribute("requestsReceivedList", requestsReceivedList);
		request.setAttribute("receivedReqUserNameIndex", 4);
	}

	/**
	 * sets the pagination information to session.
	 * @param request to be processed.
	 * @param currentPageNoName name of the current page.
	 * @param currentPageNo integer denoting current page.
	 * @param totalRecordsName names of records.
	 * @param totalRecords count of total records.
	 * @param totalPagesName name of total pages.
	 * @param totalPages total pages count.
	 * @param recordsPerPageName records per page.
	 * @param recordsPerPage count of records per page.
	 */
	private void setPagenationInfoToSession(HttpServletRequest request, String currentPageNoName,
			Integer currentPageNo, String totalRecordsName, Integer totalRecords,
			String totalPagesName, Integer totalPages, String recordsPerPageName,
			Integer recordsPerPage)
	{
		request.getSession().setAttribute(currentPageNoName, currentPageNo);
		request.getSession().setAttribute(totalRecordsName, totalRecords);
		request.getSession().setAttribute(totalPagesName, totalPages);
		request.getSession().setAttribute(recordsPerPageName, recordsPerPage);
	}

	/**
	 * gets the requests received list.
	 * @param bizLogic ShipmentRequestBizLogic object.
	 * @param loggedInUserSiteIds site ids of logged in user.
	 * @param startIndex starting index integer.
	 * @param numOfRecords count of numbar of records.
	 * @return list of objects.
	 * @throws BizLogicException if some bizlogic operation fails.
	 */
	private List < Object[] > getRequestsReceivedList(ShipmentRequestBizLogic bizLogic,
			Long[] loggedInUserSiteIds, int startIndex, int numOfRecords) throws BizLogicException
	{
		String selectColumnName = "shipment.id, shipment.senderContactPerson.lastName, shipment.label, shipment.senderSite.name, shipment.senderContactPerson.firstName, shipment.sendDate, shipment.activityStatus";
		return bizLogic.getShipmentRequests(selectColumnName, "receiverSite.id", "sendDate",
				loggedInUserSiteIds, startIndex, numOfRecords,
				"AND shipment.senderSite <> shipment.receiverSite");
	}

	/**
	 * gets the requests received header.
	 * @return list of string containing headers.
	 */
	private List < String > getRequestsReceivedHeader()
	{
		List < String > requestsReceivedHeader = new ArrayList < String >();
		requestsReceivedHeader.add("Label");
		requestsReceivedHeader.add("Requester Site");
		requestsReceivedHeader.add("Requester");
		requestsReceivedHeader.add("Requested On");
		requestsReceivedHeader.add("Status");
		return requestsReceivedHeader;
	}
}
